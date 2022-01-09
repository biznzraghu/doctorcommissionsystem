package org.nh.artha.service.impl;

import liquibase.util.csv.CSVReader;
import org.nh.artha.service.PlanService;
import org.nh.artha.domain.Plan;
import org.nh.artha.repository.PlanRepository;
import org.nh.artha.repository.search.PlanSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.nh.artha.domain.ExceptionSponsor_.plans;

/**
 * Service Implementation for managing {@link Plan}.
 */
@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private final Logger log = LoggerFactory.getLogger(PlanServiceImpl.class);

    private final PlanRepository planRepository;

    private final PlanSearchRepository planSearchRepository;

    public PlanServiceImpl(PlanRepository planRepository, PlanSearchRepository planSearchRepository) {
        this.planRepository = planRepository;
        this.planSearchRepository = planSearchRepository;
    }

    /**
     * Save a plan.
     *
     * @param plan the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Plan save(Plan plan) {
        log.debug("Request to save Plan : {}", plan);
        Plan result = planRepository.save(plan);
        planSearchRepository.save(result);
        return result;
    }
    @Override
    public List<Plan> saveAll(List<Plan> planList) {
        log.debug("Request to save Plan : {}", planList);
        List<Plan> result = planRepository.saveAll(planList);
        planSearchRepository.saveAll(result);
        return result;
    }

    /**
     * Get all the plans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Plan> findAll(Pageable pageable) {
        log.debug("Request to get all Plans");
        return planRepository.findAll(pageable);
    }

    /**
     * Get one plan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Plan> findOne(Long id) {
        log.debug("Request to get Plan : {}", id);
        return planRepository.findById(id);
    }

    /**
     * Delete the plan by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Plan : {}", id);
        planRepository.deleteById(id);
        planSearchRepository.deleteById(id);
    }

    /**
     * Search for the plan corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Plan> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Plans for query {}", query);
        return planSearchRepository.search(queryStringQuery(query), pageable);    }
    @Override
    public List<Plan> uploadPlanData(MultipartFile file) throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader=new CSVReader(reader);
        List<String[]> organizationArray = csvReader.readAll();

        organizationArray.remove(0);
        Map<String, List<String[]>> planMap = organizationArray.stream().collect(Collectors.groupingBy(strings -> strings[0]));
        List<Plan> planList=new ArrayList<>();
        planMap.forEach((code, plans) -> {
            Plan plan=   createDepartmentFromCsv(plans.get(0));
            if (null!=plan ) planList.add(plan);
        });
        this.saveAll(planList);
        return null;
    }
    private Plan createDepartmentFromCsv (String [] csvRowArray) {
        Plan plan=new Plan();
        try {
            plan.setCode(csvRowArray[0]);
            plan.setName(csvRowArray[1]);
            plan.setActive(csvRowArray[4].equalsIgnoreCase("Active")?true:false);
            plan.setOpAuthorization(csvRowArray[15].equalsIgnoreCase("TRUE")?true:false);
            plan.setIpAuthorization(csvRowArray[16].equalsIgnoreCase("TRUE")?true:false);
            plan.setTemplate(csvRowArray[17].equalsIgnoreCase("TRUE")?true:false);
            plan.setSponsorPayTax(csvRowArray[18].equalsIgnoreCase("TRUE")?true:false);
            plan.setCreatedBy(csvRowArray[8]);
            plan.setModifiedBy(csvRowArray[10]);
            plan.setCreatedDate(LocalDateTime.now());
            plan.setModifiedDate(LocalDateTime.now());
            plan.setContractStartDate(LocalDateTime.now());
            plan.setContractEndDate(LocalDateTime.now());
            plan.setApplicableStartDate(LocalDateTime.now());
            plan.setApplicableEndDate(LocalDateTime.now());
        } catch (Exception e) {
            System.out.println(Arrays.toString(csvRowArray));
            throw new RuntimeException(e);
        }
        return plan;
    };

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on ValueSets");
        List<Plan> data = planRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            planSearchRepository.saveAll(data);
        }

    }
}
