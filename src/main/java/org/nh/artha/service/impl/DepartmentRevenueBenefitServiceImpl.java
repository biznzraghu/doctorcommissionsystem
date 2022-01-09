package org.nh.artha.service.impl;

import org.nh.artha.service.DepartmentRevenueBenefitService;
import org.nh.artha.domain.DepartmentRevenueBenefit;
import org.nh.artha.repository.DepartmentRevenueBenefitRepository;
import org.nh.artha.repository.search.DepartmentRevenueBenefitSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link DepartmentRevenueBenefit}.
 */
@Service
@Transactional
public class DepartmentRevenueBenefitServiceImpl implements DepartmentRevenueBenefitService {

    private final Logger log = LoggerFactory.getLogger(DepartmentRevenueBenefitServiceImpl.class);

    private final DepartmentRevenueBenefitRepository departmentRevenueBenefitRepository;

    private final DepartmentRevenueBenefitSearchRepository departmentRevenueBenefitSearchRepository;

    public DepartmentRevenueBenefitServiceImpl(DepartmentRevenueBenefitRepository departmentRevenueBenefitRepository, DepartmentRevenueBenefitSearchRepository departmentRevenueBenefitSearchRepository) {
        this.departmentRevenueBenefitRepository = departmentRevenueBenefitRepository;
        this.departmentRevenueBenefitSearchRepository = departmentRevenueBenefitSearchRepository;
    }

    /**
     * Save a departmentRevenueBenefit.
     *
     * @param departmentRevenueBenefit the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DepartmentRevenueBenefit save(DepartmentRevenueBenefit departmentRevenueBenefit) {
        log.debug("Request to save DepartmentRevenueBenefit : {}", departmentRevenueBenefit);
        DepartmentRevenueBenefit result = departmentRevenueBenefitRepository.save(departmentRevenueBenefit);
        departmentRevenueBenefitSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the departmentRevenueBenefits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentRevenueBenefit> findAll(Pageable pageable) {
        log.debug("Request to get all DepartmentRevenueBenefits");
        return departmentRevenueBenefitRepository.findAll(pageable);
    }

    /**
     * Get one departmentRevenueBenefit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentRevenueBenefit> findOne(Long id) {
        log.debug("Request to get DepartmentRevenueBenefit : {}", id);
        return departmentRevenueBenefitRepository.findById(id);
    }

    /**
     * Delete the departmentRevenueBenefit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepartmentRevenueBenefit : {}", id);
        departmentRevenueBenefitRepository.deleteById(id);
        //departmentRevenueBenefitSearchRepository.deleteById(id);
    }

    /**
     * Search for the departmentRevenueBenefit corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentRevenueBenefit> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepartmentRevenueBenefits for query {}", query);
        return departmentRevenueBenefitSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<DepartmentRevenueBenefit> saveAll(List<DepartmentRevenueBenefit> departmentRevenueBenefit) {
        List<DepartmentRevenueBenefit> departmentRevenueBenefits = departmentRevenueBenefitRepository.saveAll(departmentRevenueBenefit);
        return departmentRevenueBenefits;
    }
}
