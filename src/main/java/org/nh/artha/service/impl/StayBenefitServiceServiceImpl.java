package org.nh.artha.service.impl;

import org.nh.artha.service.StayBenefitServiceService;
import org.nh.artha.domain.StayBenefitService;
import org.nh.artha.repository.StayBenefitServiceRepository;
import org.nh.artha.repository.search.StayBenefitServiceSearchRepository;
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
 * Service Implementation for managing {@link StayBenefitService}.
 */
@Service
@Transactional
public class StayBenefitServiceServiceImpl implements StayBenefitServiceService {

    private final Logger log = LoggerFactory.getLogger(StayBenefitServiceServiceImpl.class);

    private final StayBenefitServiceRepository stayBenefitServiceRepository;

    private final StayBenefitServiceSearchRepository stayBenefitServiceSearchRepository;

    public StayBenefitServiceServiceImpl(StayBenefitServiceRepository stayBenefitServiceRepository, StayBenefitServiceSearchRepository stayBenefitServiceSearchRepository) {
        this.stayBenefitServiceRepository = stayBenefitServiceRepository;
        this.stayBenefitServiceSearchRepository = stayBenefitServiceSearchRepository;
    }

    /**
     * Save a stayBenefitService.
     *
     * @param stayBenefitService the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StayBenefitService save(StayBenefitService stayBenefitService) {
        log.debug("Request to save StayBenefitService : {}", stayBenefitService);
        StayBenefitService result = stayBenefitServiceRepository.save(stayBenefitService);
        stayBenefitServiceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the stayBenefitServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StayBenefitService> findAll(Pageable pageable) {
        log.debug("Request to get all StayBenefitServices");
        return stayBenefitServiceRepository.findAll(pageable);
    }

    /**
     * Get one stayBenefitService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StayBenefitService> findOne(Long id) {
        log.debug("Request to get StayBenefitService : {}", id);
        return stayBenefitServiceRepository.findById(id);
    }

    /**
     * Delete the stayBenefitService by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StayBenefitService : {}", id);
        stayBenefitServiceRepository.deleteById(id);
        stayBenefitServiceSearchRepository.deleteById(id);
    }

    /**
     * Search for the stayBenefitService corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StayBenefitService> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StayBenefitServices for query {}", query);
        return stayBenefitServiceSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public List<StayBenefitService> saveAll(List<StayBenefitService> stayBenefitService) {
        log.debug("Request to save StayBenefitService : {}", stayBenefitService);
        List<StayBenefitService> result = stayBenefitServiceRepository.saveAll(stayBenefitService);
        stayBenefitServiceSearchRepository.saveAll(result);
        return result;
    }
}
