package org.nh.artha.service;

import org.nh.artha.domain.Configurations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Configurations}.
 */
public interface ConfigurationsService {

    /**
     * Save a configurations.
     *
     * @param configurations the entity to save.
     * @return the persisted entity.
     */
    Configurations save(Configurations configurations);

    /**
     * Get all the configurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Configurations> findAll(Pageable pageable);


    /**
     * Get the "id" configurations.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Configurations> findOne(Long id);

    /**
     * Delete the "id" configurations.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the configurations corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Configurations> search(String query, Pageable pageable);
}
