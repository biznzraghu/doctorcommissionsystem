package org.nh.artha.service;

import org.nh.artha.domain.ValueSetCodeMapping;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ValueSetCodeMapping}.
 */
public interface ValueSetCodeMappingService {

    /**
     * Save a valueSetCodeMapping.
     *
     * @param valueSetCodeMapping the entity to save.
     * @return the persisted entity.
     */
    ValueSetCodeMapping save(ValueSetCodeMapping valueSetCodeMapping);

    /**
     * Get all the valueSetCodeMappings.
     *
     * @return the list of entities.
     */
    List<ValueSetCodeMapping> findAll();


    /**
     * Get the "id" valueSetCodeMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ValueSetCodeMapping> findOne(Long id);

    /**
     * Delete the "id" valueSetCodeMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the valueSetCodeMapping corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<ValueSetCodeMapping> search(String query);
}
