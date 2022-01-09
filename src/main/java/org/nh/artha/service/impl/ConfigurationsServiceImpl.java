package org.nh.artha.service.impl;

import org.nh.artha.service.ConfigurationsService;
import org.nh.artha.domain.Configurations;
import org.nh.artha.repository.ConfigurationsRepository;
import org.nh.artha.repository.search.ConfigurationsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Configurations}.
 */
@Service
@Transactional
public class ConfigurationsServiceImpl implements ConfigurationsService {

    private final Logger log = LoggerFactory.getLogger(ConfigurationsServiceImpl.class);

    private final ConfigurationsRepository configurationsRepository;

    private final ConfigurationsSearchRepository configurationsSearchRepository;

    public ConfigurationsServiceImpl(ConfigurationsRepository configurationsRepository, ConfigurationsSearchRepository configurationsSearchRepository) {
        this.configurationsRepository = configurationsRepository;
        this.configurationsSearchRepository = configurationsSearchRepository;
    }

    /**
     * Save a configurations.
     *
     * @param configurations the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Configurations save(Configurations configurations) {
        log.debug("Request to save Configurations : {}", configurations);
        Configurations result = configurationsRepository.save(configurations);
        configurationsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the configurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Configurations> findAll(Pageable pageable) {
        log.debug("Request to get all Configurations");
        return configurationsRepository.findAll(pageable);
    }


    /**
     * Get one configurations by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Configurations> findOne(Long id) {
        log.debug("Request to get Configurations : {}", id);
        return configurationsRepository.findById(id);
    }

    /**
     * Delete the configurations by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Configurations : {}", id);
        configurationsRepository.deleteById(id);
        configurationsSearchRepository.deleteById(id);
    }

    /**
     * Search for the configurations corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Configurations> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Configurations for query {}", query);
        return configurationsSearchRepository.search(queryStringQuery(query), pageable);    }
}
