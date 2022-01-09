package org.nh.artha.service;

import org.nh.artha.domain.PackageCodeMapping;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PackageCodeMapping}.
 */
public interface PackageCodeMappingService {

    /**
     * Save a packageCodeMapping.
     *
     * @param packageCodeMapping the entity to save.
     * @return the persisted entity.
     */
    PackageCodeMapping save(PackageCodeMapping packageCodeMapping);

    /**
     * Get all the packageCodeMappings.
     *
     * @return the list of entities.
     */
    List<PackageCodeMapping> findAll();


    /**
     * Get the "id" packageCodeMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PackageCodeMapping> findOne(Long id);

    /**
     * Delete the "id" packageCodeMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the packageCodeMapping corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<PackageCodeMapping> search(String query);
}
