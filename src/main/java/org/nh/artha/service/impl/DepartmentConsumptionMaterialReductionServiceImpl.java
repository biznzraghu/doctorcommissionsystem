package org.nh.artha.service.impl;

import org.nh.artha.service.DepartmentConsumptionMaterialReductionService;
import org.nh.artha.domain.DepartmentConsumptionMaterialReduction;
import org.nh.artha.repository.DepartmentConsumptionMaterialReductionRepository;
import org.nh.artha.repository.search.DepartmentConsumptionMaterialReductionSearchRepository;
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
 * Service Implementation for managing {@link DepartmentConsumptionMaterialReduction}.
 */
@Service
@Transactional
public class DepartmentConsumptionMaterialReductionServiceImpl implements DepartmentConsumptionMaterialReductionService {

    private final Logger log = LoggerFactory.getLogger(DepartmentConsumptionMaterialReductionServiceImpl.class);

    private final DepartmentConsumptionMaterialReductionRepository departmentConsumptionMaterialReductionRepository;

    private final DepartmentConsumptionMaterialReductionSearchRepository departmentConsumptionMaterialReductionSearchRepository;

    public DepartmentConsumptionMaterialReductionServiceImpl(DepartmentConsumptionMaterialReductionRepository departmentConsumptionMaterialReductionRepository, DepartmentConsumptionMaterialReductionSearchRepository departmentConsumptionMaterialReductionSearchRepository) {
        this.departmentConsumptionMaterialReductionRepository = departmentConsumptionMaterialReductionRepository;
        this.departmentConsumptionMaterialReductionSearchRepository = departmentConsumptionMaterialReductionSearchRepository;
    }

    /**
     * Save a departmentConsumptionMaterialReduction.
     *
     * @param departmentConsumptionMaterialReduction the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DepartmentConsumptionMaterialReduction save(DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction) {
        log.debug("Request to save DepartmentConsumptionMaterialReduction : {}", departmentConsumptionMaterialReduction);
        DepartmentConsumptionMaterialReduction result = departmentConsumptionMaterialReductionRepository.save(departmentConsumptionMaterialReduction);
        departmentConsumptionMaterialReductionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the departmentConsumptionMaterialReductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentConsumptionMaterialReduction> findAll(Pageable pageable) {
        log.debug("Request to get all DepartmentConsumptionMaterialReductions");
        return departmentConsumptionMaterialReductionRepository.findAll(pageable);
    }

    /**
     * Get one departmentConsumptionMaterialReduction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentConsumptionMaterialReduction> findOne(Long id) {
        log.debug("Request to get DepartmentConsumptionMaterialReduction : {}", id);
        return departmentConsumptionMaterialReductionRepository.findById(id);
    }

    /**
     * Delete the departmentConsumptionMaterialReduction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepartmentConsumptionMaterialReduction : {}", id);
        departmentConsumptionMaterialReductionRepository.deleteById(id);
        departmentConsumptionMaterialReductionSearchRepository.deleteById(id);
    }

    /**
     * Search for the departmentConsumptionMaterialReduction corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentConsumptionMaterialReduction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepartmentConsumptionMaterialReductions for query {}", query);
        return departmentConsumptionMaterialReductionSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<DepartmentConsumptionMaterialReduction> saveAll(List<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductions) {
        List<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductionList = departmentConsumptionMaterialReductionRepository.saveAll(departmentConsumptionMaterialReductions);
        return departmentConsumptionMaterialReductionList;
    }
}
