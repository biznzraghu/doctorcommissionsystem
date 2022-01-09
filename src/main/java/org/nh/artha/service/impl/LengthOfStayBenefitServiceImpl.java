package org.nh.artha.service.impl;

import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.LengthOfStayCriteria;
import org.nh.artha.repository.AdmissionDischargeStagingRepository;
import org.nh.artha.repository.ServiceAnalysisRepository;
import org.nh.artha.repository.UserMasterRepository;
import org.nh.artha.service.LengthOfStayBenefitService;
import org.nh.artha.repository.LengthOfStayBenefitRepository;
import org.nh.artha.repository.search.LengthOfStayBenefitSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link LengthOfStayBenefit}.
 */
@Service
@Transactional
public class LengthOfStayBenefitServiceImpl implements LengthOfStayBenefitService {

    private final Logger log = LoggerFactory.getLogger(LengthOfStayBenefitServiceImpl.class);

    private final LengthOfStayBenefitRepository lengthOfStayBenefitRepository;

    private final LengthOfStayBenefitSearchRepository lengthOfStayBenefitSearchRepository;

    private final ServiceAnalysisRepository serviceAnalysisRepository;

    private final UserMasterRepository userMasterRepository;

    private AdmissionDischargeStagingRepository admissionDischargeStagingRepository;

    public LengthOfStayBenefitServiceImpl(LengthOfStayBenefitRepository lengthOfStayBenefitRepository, LengthOfStayBenefitSearchRepository lengthOfStayBenefitSearchRepository,
                                          ServiceAnalysisRepository serviceAnalysisRepository, UserMasterRepository userMasterRepository,
                                          AdmissionDischargeStagingRepository admissionDischargeStagingRepository) {
        this.lengthOfStayBenefitRepository = lengthOfStayBenefitRepository;
        this.lengthOfStayBenefitSearchRepository = lengthOfStayBenefitSearchRepository;
        this.serviceAnalysisRepository = serviceAnalysisRepository;
        this.userMasterRepository = userMasterRepository;
        this.admissionDischargeStagingRepository = admissionDischargeStagingRepository;
    }

    /**
     * Save a lengthOfStayBenefit.
     *
     * @param lengthOfStayBenefit the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LengthOfStayBenefit save(LengthOfStayBenefit lengthOfStayBenefit) {
        log.debug("Request to save LengthOfStayBenefit : {}", lengthOfStayBenefit);
        LengthOfStayBenefit result = lengthOfStayBenefitRepository.save(lengthOfStayBenefit);
        lengthOfStayBenefitSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the lengthOfStayBenefits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LengthOfStayBenefit> findAll(Pageable pageable) {
        log.debug("Request to get all LengthOfStayBenefits");
        return lengthOfStayBenefitRepository.findAll(pageable);
    }

    /**
     * Get one lengthOfStayBenefit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LengthOfStayBenefit> findOne(Long id) {
        log.debug("Request to get LengthOfStayBenefit : {}", id);
        return lengthOfStayBenefitRepository.findById(id);
    }

    /**
     * Delete the lengthOfStayBenefit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LengthOfStayBenefit : {}", id);
        lengthOfStayBenefitRepository.deleteById(id);
        //lengthOfStayBenefitSearchRepository.deleteById(id);
    }

    /**
     * Search for the lengthOfStayBenefit corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LengthOfStayBenefit> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LengthOfStayBenefits for query {}", query);
        return lengthOfStayBenefitSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public List<LengthOfStayBenefit> saveAll(List<LengthOfStayBenefit> lengthOfStayBenefit) {
        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.saveAll(lengthOfStayBenefit);
        return lengthOfStayBenefitList;
    }

    @Override
    public Map<LengthOfStayBenefit, List<ServiceAnalysisStaging>> findPatientBasedOnLosBenefit(VariablePayout variablePayout) {
        Map<LengthOfStayBenefit, List<ServiceAnalysisStaging>> returnMap = new HashMap<>();
        ArrayList<LengthOfStayBenefit> lengthOfStayBenefitList = new ArrayList<>(variablePayout.getLengthOfStayBenefits());
        UserMaster userMaster = userMasterRepository.findById(variablePayout.getEmployeeId()).orElse(null);
        if (lengthOfStayBenefitList != null && !lengthOfStayBenefitList.isEmpty()) {
            lengthOfStayBenefitList.stream().forEach(lengthOfStayBenefit -> {
                Department department = lengthOfStayBenefit.getDepartment();
                List<String> stayBenefitServiceCode = lengthOfStayBenefit.getStayBenefitServices().stream().map(stayBenefitService -> stayBenefitService.getServiceCode()).collect(Collectors.toList());
                List<ServiceAnalysisStaging> matchMrnBasedOnStayBenefietServiceCriteria = serviceAnalysisRepository.findMrnBasedOnDepartmentAndServiceCode(userMaster.getEmployeeNumber(), variablePayout.getUnitCode(), department.getCode(), "IP", stayBenefitServiceCode).stream().collect(Collectors.toList());
                //serviceAnalysisRepository.updateMrnBasedOnDepartmentAndServiceCode(userMaster.getEmployeeNumber(), variablePayout.getUnitCode(), department.getCode(), "IP", stayBenefitServiceCode);
                if (matchMrnBasedOnStayBenefietServiceCriteria != null && !matchMrnBasedOnStayBenefietServiceCriteria.isEmpty()) {
                    returnMap.put(lengthOfStayBenefit, matchMrnBasedOnStayBenefietServiceCriteria);
                }
                lengthOfStayBenefit.setCurrentVersion(variablePayout.getVersion());
                save(lengthOfStayBenefit);
            });
        }

        return returnMap;
    }


}
