package org.nh.artha.service.impl;


import org.elasticsearch.index.query.Operator;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.MisReport;
import org.nh.artha.domain.enumeration.MisReportStatus;
import org.nh.artha.job.MisReportTriggerJob;
import org.nh.artha.repository.MisReportRepository;
import org.nh.artha.repository.search.MisReportSearchRepository;
import org.nh.artha.service.MisReportService;
import org.nh.artha.web.rest.errors.CustomParameterizedException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;


/**
 * Service Implementation for managing MisReport.
 */
@Service
@Transactional
public class MisReportServiceImpl implements MisReportService {

    private final Logger log = LoggerFactory.getLogger(MisReportServiceImpl.class);

    private final MisReportRepository misReportRepository;

    private static final String scheduledDateError = "1002";

    private static final String duplicateReportError = "1003";


    private Scheduler scheduler;

    private final ApplicationProperties applicationProperties;

    private final MisReportSearchRepository misReportSearchRepository;

    public MisReportServiceImpl(MisReportRepository misReportRepository,MisReportSearchRepository misReportSearchRepository,ApplicationProperties applicationProperties,
                                Scheduler scheduler) {
        this.misReportRepository = misReportRepository;
        this.applicationProperties=applicationProperties;
        this.misReportSearchRepository=misReportSearchRepository;
        this.scheduler=scheduler;
    }

    /**
     * Save a misReport.
     *
     * @param misReport the entity to save
     * @return the persisted entity
     */
    @Override
    public MisReport save(MisReport misReport, Map filterMap) {
        log.debug("Request to save MisReport : {}", misReport);
        misReport.setStatus(MisReportStatus.PENDING);
        String hashValue = hashString(filterMap);
        List<MisReport> misReports = misReportRepository.filterReportByHashValue(hashValue);
        MisReport misReportByHashValue = null;
        if (misReports != null && misReports.size() > 0) {
            if (misReport.getDuplicate()) {
                //compare scheduled date given from UI and with last report for the same hashvalue
                Integer compIndex = misReport.getScheduleDate().compareTo(LocalDateTime.now().plusHours(24));
                if (compIndex > 0) {
                    misReportByHashValue = misReports.get(0);
                    if (misReportByHashValue != null) {
                        //check the status of last report
                        if (misReportByHashValue.getStatus().equals(MisReportStatus.COMPLETED) || misReportByHashValue.getStatus().equals(MisReportStatus.FAILED)) {
                            misReport.setHashValue(hashValue);
                            misReport.setQueryParams(queryParamMap(filterMap));
                            MisReport result = misReportRepository.save(misReport);
                            misReportSearchRepository.save(result);
                            createJobDetail(result, filterMap);
                            return result;
                        } else {
                            return misReportByHashValue;
                        }
                    } else {
                        return misReports.get(0);
                    }
                } else {
                    throw new CustomParameterizedException(scheduledDateError, "Cannot Scheduled the report new scheduled time must be greater than 24 hrs");
                }
            } else {
                throw new CustomParameterizedException(duplicateReportError, "Duplicates report scheduling  are not allowed for the configuration");
            }
        } else {
            misReport.setHashValue(hashValue);
            misReport.setQueryParams(queryParamMap(filterMap));
            MisReport result = misReportRepository.save(misReport);
            misReportSearchRepository.save(result);
            createJobDetail(result, filterMap);
            return result;
        }

    }



    /**
     *  Get all the misReports.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MisReport> findAll(Pageable pageable) {
        log.debug("Request to get all MisReports");
        return misReportRepository.findAll(pageable);
    }

    /**
     *  Get one misReport by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MisReport findOne(Long id) {
        log.debug("Request to get MisReport : {}", id);
        return misReportRepository.getOne(id);
    }

    /**
     *  Delete the  misReport by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MisReport : {}", id);
        misReportRepository.deleteById(id);
    }

    public JobDetail createJobDetail(MisReport misReport,Map filterMap) {
        JobDataMap jobDataMap = new JobDataMap();
        if(misReport.getId()!=null)
        jobDataMap.put("misReportId", misReport.getId().toString());
        if(filterMap!=null && filterMap.containsKey("filterCriteria") && filterMap.get("filterCriteria")!=null && !filterMap.get("filterCriteria").toString().isEmpty())
        jobDataMap.put("filterCriteria",filterMap.get("filterCriteria").toString());
        JobDetail jobDetail = JobBuilder.newJob(MisReportTriggerJob.class)
            .storeDurably(false)
            .withDescription("Report name :: " + misReport.getId())
            .withIdentity(new StringBuilder(misReport.getReportName()).append("-").append(misReport.getId()).toString(), "Mis-Resport-Schedules")
            .requestRecovery(true)
            .usingJobData(jobDataMap)
            .build();

        Trigger trigger = createTrigger(jobDetail, misReport);
        try {
             scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("Scheduler exception occurred .",e);
            throw new RuntimeException(e);
        }
        return jobDetail;
    }

    private Trigger createTrigger(JobDetail jobDetail, MisReport misReport) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow();
        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.getKey().getName(), "Mis-Report-Schedules")
            .withDescription(jobDetail.getDescription())
            .startAt(Date.from(misReport.getScheduleDate().toInstant(ZoneOffset.UTC)))
            .withSchedule(simpleScheduleBuilder)
            .build();
        return simpleTrigger;

    }

    @Override
    public Map<String,String> download(Long reportId) throws IOException {
        MisReport misReport = misReportRepository.findById(reportId).orElseThrow(() -> {
            return new CustomParameterizedException("10101", "no report found");
        });
        HashMap fileDetails= new HashMap();
        fileDetails.put("fileName", misReport.getReportPath());
        fileDetails.put("pathReference", "tempExport");
        return fileDetails;
    }



    private static String hashString(Map<String,String> filterMap)
        throws RuntimeException {
        try {
            StringBuffer messageBuffer= new StringBuffer();
            filterMap.forEach((key, value) ->messageBuffer.append(key).append(value) );
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(messageBuffer.toString().getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new RuntimeException(
                "Could not generate hash from String", ex);
        }
    }
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                .substring(1));
        }
        return stringBuffer.toString();
    }

    private Map<String,String> queryParamMap(Map filterMap){
        Map<String,String> queryParamMap= new HashMap<>();
        filterMap.forEach((key, value) -> {
            String[] split = value.toString().split("&&");
            String paramName=null;
            String paramValue=null;
            for (int index=0;index<split.length;index++){
                paramName=split[index].split("=")[0];
                paramValue=split[index].split("=")[1];
                if(!paramName.equalsIgnoreCase("limitSize")&& !paramName.equalsIgnoreCase("startIndex")) {
                    queryParamMap.put(paramName, paramValue);
                }
            }

        });
        return queryParamMap;
    }

    @Override
    public Page<MisReport> search(String query, Pageable pageable) {
        return misReportSearchRepository.search(queryStringQuery(query).defaultOperator(Operator.AND),pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Organization");

        List<MisReport> data = misReportRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            misReportSearchRepository.saveAll(data);
        }

    }
}
