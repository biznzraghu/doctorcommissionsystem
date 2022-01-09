package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.MisReport;
import org.nh.artha.domain.enumeration.MisReportStatus;
import org.nh.artha.repository.MisReportRepository;
import org.nh.artha.repository.search.MisReportSearchRepository;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import  org.nh.artha.service.DoctorPayoutService;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MisReportTriggerJob extends QuartzJobBean {
    private final Logger log = LoggerFactory.getLogger(MisReportTriggerJob.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MisReportRepository reportRepository;

    @Autowired
    private MisReportSearchRepository misReportSearchRepository;

    @Autowired
    private DoctorPayoutService doctorPayoutService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        MisReport misReport = null;
        try {
            Long misReportId = Long.parseLong(jobExecutionContext.getMergedJobDataMap().get("misReportId").toString());
            String filterCriteria=null;
            String[] queryValue=null;
            if(jobExecutionContext.getMergedJobDataMap().containsKey("filterCriteria")) {
                filterCriteria = (String) jobExecutionContext.getMergedJobDataMap().get("filterCriteria");
                queryValue = filterCriteria.split("&&");
            }
            Map<String, String> queryCriStringMap = new HashMap<>();
            if (queryValue!=null) {
                for (int i = 0; i < queryValue.length; i++) {
                    String key = queryValue[i].split("=")[0];
                    String value = queryValue[i].split("=")[1];
                    queryCriStringMap.put(key, value);
                }
            }
            misReport = reportRepository.findById(misReportId).get();
            misReport.setStatus(MisReportStatus.IN_PROGRESS);
            Map<String, String> returnMap = doctorPayoutService.exportItemServiceWisePayoutBreakUp(queryCriStringMap.get("unit"), Integer.valueOf(queryCriStringMap.get("year")), Integer.valueOf(queryCriStringMap.get("month")), misReport);
            misReport.setReportPath(returnMap.get("fileName"));
            misReport.setStatus(MisReportStatus.COMPLETED);

        } catch (Exception ex) {
            Throwable th = ex;
            log.error("Error while running the scheduler in MIS", ex);
            misReport.setStatus(MisReportStatus.FAILED);
            misReport.setError("Error while running the scheduler in MIS casued by { "+th.getMessage()+"}");
        } finally {
            MisReport saveAndFlush = reportRepository.saveAndFlush(misReport);
            misReportSearchRepository.save(saveAndFlush);
        }

    }
}
