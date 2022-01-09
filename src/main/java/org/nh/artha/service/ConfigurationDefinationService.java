package org.nh.artha.service;

import org.nh.artha.domain.ConfigurationDefination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ConfigurationDefination}.
 */
public interface ConfigurationDefinationService {

    /**
     * Save a ConfigurationDefination.
     *
     * @param ConfigurationDefination the entity to save.
     * @return the persisted entity.
     */
    ConfigurationDefination save(ConfigurationDefination ConfigurationDefination);

    /**
     * Get all the ConfigurationDefinations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigurationDefination> findAll(Pageable pageable);


    /**
     * Get the "id" ConfigurationDefination.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigurationDefination> findOne(Long id);

    /**
     * Delete the "id" ConfigurationDefination.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ConfigurationDefination corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConfigurationDefination> search(String query, Pageable pageable);
}
