package org.nh.artha.service.impl;

import org.nh.artha.service.HscConsumptionMaterialReductionService;
import org.nh.artha.domain.HscConsumptionMaterialReduction;
import org.nh.artha.repository.HscConsumptionMaterialReductionRepository;
import org.nh.artha.repository.search.HscConsumptionMaterialReductionSearchRepository;
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
 * Service Implementation for managing {@link HscConsumptionMaterialReduction}.
 */
@Service
@Transactional
public class HscConsumptionMaterialReductionServiceImpl implements HscConsumptionMaterialReductionService {

    private final Logger log = LoggerFactory.getLogger(HscConsumptionMaterialReductionServiceImpl.class);

    private final HscConsumptionMaterialReductionRepository hscConsumptionMaterialReductionRepository;

    private final HscConsumptionMaterialReductionSearchRepository hscConsumptionMaterialReductionSearchRepository;

    public HscConsumptionMaterialReductionServiceImpl(HscConsumptionMaterialReductionRepository hscConsumptionMaterialReductionRepository, HscConsumptionMaterialReductionSearchRepository hscConsumptionMaterialReductionSearchRepository) {
        this.hscConsumptionMaterialReductionRepository = hscConsumptionMaterialReductionRepository;
        this.hscConsumptionMaterialReductionSearchRepository = hscConsumptionMaterialReductionSearchRepository;
    }

    /**
     * Save a hscConsumptionMaterialReduction.
     *
     * @param hscConsumptionMaterialReduction the entity to save.
     * @return the persisted entity.
     */
    @Override
    public HscConsumptionMaterialReduction save(HscConsumptionMaterialReduction hscConsumptionMaterialReduction) {
        log.debug("Request to save HscConsumptionMaterialReduction : {}", hscConsumptionMaterialReduction);
        HscConsumptionMaterialReduction result = hscConsumptionMaterialReductionRepository.save(hscConsumptionMaterialReduction);
        hscConsumptionMaterialReductionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the hscConsumptionMaterialReductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HscConsumptionMaterialReduction> findAll(Pageable pageable) {
        log.debug("Request to get all HscConsumptionMaterialReductions");
        return hscConsumptionMaterialReductionRepository.findAll(pageable);
    }

    /**
     * Get one hscConsumptionMaterialReduction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HscConsumptionMaterialReduction> findOne(Long id) {
        log.debug("Request to get HscConsumptionMaterialReduction : {}", id);
        return hscConsumptionMaterialReductionRepository.findById(id);
    }

    /**
     * Delete the hscConsumptionMaterialReduction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HscConsumptionMaterialReduction : {}", id);
        hscConsumptionMaterialReductionRepository.deleteById(id);
        hscConsumptionMaterialReductionSearchRepository.deleteById(id);
    }

    /**
     * Search for the hscConsumptionMaterialReduction corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HscConsumptionMaterialReduction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HscConsumptionMaterialReductions for query {}", query);
        return hscConsumptionMaterialReductionSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<HscConsumptionMaterialReduction> saveAll(List<HscConsumptionMaterialReduction> hscConsumptionMaterialReductions) {
        List<HscConsumptionMaterialReduction> hscConsumptionMaterialReductionList = hscConsumptionMaterialReductionRepository.saveAll(hscConsumptionMaterialReductions);
        return hscConsumptionMaterialReductionList;
    }
}
