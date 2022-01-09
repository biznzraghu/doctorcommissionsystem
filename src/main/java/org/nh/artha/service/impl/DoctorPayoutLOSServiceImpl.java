package org.nh.artha.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nh.artha.domain.*;
import org.nh.artha.domain.dto.AdmissionDischargeStagingDTO;
import org.nh.artha.domain.enumeration.LengthOfStayCriteria;
import org.nh.artha.repository.AdmissionDischargeStagingRepository;
import org.nh.artha.repository.LengthOfStayBenefitRepository;
import org.nh.artha.repository.ServiceAnalysisRepository;
import org.nh.artha.service.DoctorPayoutLOSService;
import org.nh.artha.repository.DoctorPayoutLOSRepository;
import org.nh.artha.repository.search.DoctorPayoutLOSSearchRepository;
import org.nh.artha.service.LengthOfStayBenefitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link DoctorPayoutLOS}.
 */
@Service
@Transactional
public class DoctorPayoutLOSServiceImpl implements DoctorPayoutLOSService {

    private final Logger log = LoggerFactory.getLogger(DoctorPayoutLOSServiceImpl.class);

    private final DoctorPayoutLOSRepository doctorPayoutLOSRepository;

    private final DoctorPayoutLOSSearchRepository doctorPayoutLOSSearchRepository;

    private final AdmissionDischargeStagingRepository admissionDischargeStagingRepository;

    private final ObjectMapper objectMapper;

    private final ServiceAnalysisRepository serviceAnalysisRepository;

    private final LengthOfStayBenefitRepository lengthOfStayBenefitRepository;

    public DoctorPayoutLOSServiceImpl(DoctorPayoutLOSRepository doctorPayoutLOSRepository, DoctorPayoutLOSSearchRepository doctorPayoutLOSSearchRepository,
                                      AdmissionDischargeStagingRepository admissionDischargeStagingRepository,ObjectMapper objectMapper,ServiceAnalysisRepository serviceAnalysisRepository,
                                      LengthOfStayBenefitRepository lengthOfStayBenefitRepository) {
        this.doctorPayoutLOSRepository = doctorPayoutLOSRepository;
        this.doctorPayoutLOSSearchRepository = doctorPayoutLOSSearchRepository;
        this.admissionDischargeStagingRepository=admissionDischargeStagingRepository;
        this.objectMapper=objectMapper;
        this.serviceAnalysisRepository=serviceAnalysisRepository;
        this.lengthOfStayBenefitRepository=lengthOfStayBenefitRepository;
    }

    /**
     * Save a doctorPayoutLOS.
     *
     * @param doctorPayoutLOS the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DoctorPayoutLOS save(DoctorPayoutLOS doctorPayoutLOS) {
        log.debug("Request to save DoctorPayoutLOS : {}", doctorPayoutLOS);
        DoctorPayoutLOS result = doctorPayoutLOSRepository.save(doctorPayoutLOS);
        doctorPayoutLOSSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the doctorPayoutLOS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorPayoutLOS> findAll(Pageable pageable) {
        log.debug("Request to get all DoctorPayoutLOS");
        return doctorPayoutLOSRepository.findAll(pageable);
    }


    /**
     * Get one doctorPayoutLOS by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorPayoutLOS> findOne(Long id) {
        log.debug("Request to get DoctorPayoutLOS : {}", id);
        return doctorPayoutLOSRepository.findById(id);
    }

    /**
     * Delete the doctorPayoutLOS by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DoctorPayoutLOS : {}", id);
        doctorPayoutLOSRepository.deleteById(id);
        doctorPayoutLOSSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctorPayoutLOS corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorPayoutLOS> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoctorPayoutLOS for query {}", query);
        return doctorPayoutLOSSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<DoctorPayoutLOS> calculateDoctorPayoutLos(Map<LengthOfStayBenefit, List<ServiceAnalysisStaging>> patientBasedOnLosBenefit, DoctorPayout finalDoctorPayout) {
        Predicate<ServiceAnalysisStaging> mrnPredicate= stagingSar -> {
            {
                AdmissionDischargeStaging admissionDischargeStaging = new AdmissionDischargeStaging();
                admissionDischargeStaging.setPatientMrn(stagingSar.getPatientMrn());
                AdmissionDischargeStaging admission = admissionDischargeStagingRepository.findOne(Example.of(admissionDischargeStaging)).orElse(null);
                return admission!=null;
            }
        };
        List<DoctorPayoutLOS> doctorPayoutLOSFinalList= new ArrayList<>();
        patientBasedOnLosBenefit.forEach((lengthOfStayBenefit, mrnList) -> {
            List<AdmissionDischargeStagingDTO> collect = mrnList.stream().filter(mrnPredicate).map(stagingSar -> {
                AdmissionDischargeStaging admissionDischargeStaging = new AdmissionDischargeStaging();
                admissionDischargeStaging.setPatientMrn(stagingSar.getPatientMrn());
                AdmissionDischargeStaging admission = admissionDischargeStagingRepository.findOne(Example.of(admissionDischargeStaging)).get();
                AdmissionDischargeStagingDTO admissionDischargeStagingDTO = objectMapper.convertValue(admission, AdmissionDischargeStagingDTO.class);
                admissionDischargeStagingDTO.setSarId(stagingSar.getId());
                return admissionDischargeStagingDTO;
            }).collect(Collectors.toSet()).stream().collect(Collectors.toList());
            if(lengthOfStayBenefit.getLengthOfStayCriteria().equals(LengthOfStayCriteria.FIXED)){
                List<DoctorPayoutLOS> doctorPayoutLOSList = collect.stream().map(admissionDischargeStaging -> {
                    Period between = Period.between(admissionDischargeStaging.getAdmissionDate().toLocalDate(), admissionDischargeStaging.getDischargeDate().toLocalDate());
                    if (between.getDays() < lengthOfStayBenefit.getDays()) {
                        Long payableDays = Long.valueOf(lengthOfStayBenefit.getDays() - between.getDays());
                        Double sum=lengthOfStayBenefit.getPerDayPayoutAmount().doubleValue()*(payableDays);
                        DoctorPayoutLOS doctorPayoutLOS = new DoctorPayoutLOS();
                        doctorPayoutLOS.setPatientMrn(admissionDischargeStaging.getPatientMrn());
                        doctorPayoutLOS.setAdmissionDate(admissionDischargeStaging.getDischargeDate());
                        doctorPayoutLOS.setDischargeDateTime(admissionDischargeStaging.getAdmissionDate());
                        doctorPayoutLOS.setAmount(sum);
                        doctorPayoutLOS.setPayableDays(payableDays);
                        doctorPayoutLOS.setDoctorPayout(finalDoctorPayout);
                        doctorPayoutLOS.setDoctorPayoutLosId(lengthOfStayBenefit.getId());
                        doctorPayoutLOS.setDepartment(lengthOfStayBenefit.getDepartment());
                        doctorPayoutLOS.setSarId(admissionDischargeStaging.getSarId());
                        return doctorPayoutLOS;
                    } else {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
                doctorPayoutLOSFinalList.addAll(doctorPayoutLOSList);
            }else if(lengthOfStayBenefit.getLengthOfStayCriteria().equals(LengthOfStayCriteria.AVERAGE)){
                Double average = collect.stream().map(admissionDischargeStaging -> {
                    Period between = Period.between(admissionDischargeStaging.getDischargeDate().toLocalDate(), admissionDischargeStaging.getAdmissionDate().toLocalDate());
                    return between.getDays();
                }).mapToInt((x) -> x).summaryStatistics().getAverage();
                if(average<lengthOfStayBenefit.getDays()){
                    Double sum=(lengthOfStayBenefit.getDays()-average)*lengthOfStayBenefit.getPerDayPayoutAmount().doubleValue();
                    DoctorPayoutLOS doctorPayoutLOS = new DoctorPayoutLOS();
                        /*doctorPayoutLOS.setPatientMrn(admissionDischargeStaging.getPatientMrn());
                        doctorPayoutLOS.setDischargeDateTime(admissionDischargeStaging.getDischargeDate());
                        doctorPayoutLOS.setDischargeDateTime(admissionDischargeStaging.getAdmissionDate());*/
                    doctorPayoutLOS.setAmount(sum);
                    doctorPayoutLOS.setDoctorPayout(finalDoctorPayout);
                    doctorPayoutLOSFinalList.add(doctorPayoutLOS);
                }
            }

        });
        List<DoctorPayoutLOS> doctorPayoutLOSList = saveAll(doctorPayoutLOSFinalList);
        patientBasedOnLosBenefit.forEach((lengthOfStayBenefit, serviceAnalysisStagings) -> {
            List<ServiceAnalysisStaging> collect = serviceAnalysisStagings.parallelStream().map(serviceAnalysisStaging -> {
                serviceAnalysisStaging.setLengthOfStayExecuted(true);
                return serviceAnalysisStaging;
            }).collect(Collectors.toList());
            serviceAnalysisRepository.saveAll(collect);
            lengthOfStayBenefit.setExecuted(true);
            lengthOfStayBenefitRepository.save(lengthOfStayBenefit);
        });
        return doctorPayoutLOSList;
    }

    private List<DoctorPayoutLOS> saveAll(List<DoctorPayoutLOS> doctorPayoutLOS){
        List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSRepository.saveAll(doctorPayoutLOS);
        return doctorPayoutLOSList;
    }
}
