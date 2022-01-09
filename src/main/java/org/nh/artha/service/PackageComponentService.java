package org.nh.artha.service;

import org.nh.artha.domain.PackageComponent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link PackageComponent}.
 */
public interface PackageComponentService {

    /**
     * Save a packageComponent.
     *
     * @param packageComponent the entity to save.
     * @return the persisted entity.
     */
    PackageComponent save(PackageComponent packageComponent);

    /**
     * Get all the packageComponents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PackageComponent> findAll(Pageable pageable);


    /**
     * Get the "id" packageComponent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PackageComponent> findOne(Long id);

    /**
     * Delete the "id" packageComponent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the packageComponent corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PackageComponent> search(String query, Pageable pageable);
}
