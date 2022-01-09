package org.nh.artha.service.impl;

import org.nh.artha.service.ConfigurationDefinationService;
import org.nh.artha.domain.ConfigurationDefination;
import org.nh.artha.repository.ConfigurationDefinationRepository;
import org.nh.artha.repository.search.ConfigurationDefinationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ConfigurationDefination}.
 */
@Service
@Transactional
public class ConfigurationDefinationServiceImpl implements ConfigurationDefinationService {

    private final Logger log = LoggerFactory.getLogger(ConfigurationDefinationServiceImpl.class);

    private final ConfigurationDefinationRepository ConfigurationDefinationRepository;

    private final ConfigurationDefinationSearchRepository ConfigurationDefinationSearchRepository;

    public ConfigurationDefinationServiceImpl(ConfigurationDefinationRepository ConfigurationDefinationRepository, ConfigurationDefinationSearchRepository ConfigurationDefinationSearchRepository) {
        this.ConfigurationDefinationRepository = ConfigurationDefinationRepository;
        this.ConfigurationDefinationSearchRepository = ConfigurationDefinationSearchRepository;
    }

    /**
     * Save a ConfigurationDefination.
     *
     * @param ConfigurationDefination the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ConfigurationDefination save(ConfigurationDefination ConfigurationDefination) {
        log.debug("Request to save ConfigurationDefination : {}", ConfigurationDefination);
        ConfigurationDefination result = ConfigurationDefinationRepository.save(ConfigurationDefination);
        ConfigurationDefinationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ConfigurationDefinations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigurationDefination> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigurationDefinations");
        return ConfigurationDefinationRepository.findAll(pageable);
    }


    /**
     * Get one ConfigurationDefination by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigurationDefination> findOne(Long id) {
        log.debug("Request to get ConfigurationDefination : {}", id);
        return ConfigurationDefinationRepository.findById(id);
    }

    /**
     * Delete the ConfigurationDefination by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConfigurationDefination : {}", id);
        ConfigurationDefinationRepository.deleteById(id);
        ConfigurationDefinationSearchRepository.deleteById(id);
    }

    /**
     * Search for the ConfigurationDefination corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConfigurationDefination> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ConfigurationDefinations for query {}", query);
        return ConfigurationDefinationSearchRepository.search(queryStringQuery(query), pageable);    }
}
