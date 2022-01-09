package org.nh.artha.service;

import org.nh.artha.domain.OrganizationType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link OrganizationType}.
 */
public interface OrganizationTypeService {

    /**
     * Save a organizationType.
     *
     * @param organizationType the entity to save.
     * @return the persisted entity.
     */
    OrganizationType save(OrganizationType organizationType);

    /**
     * Get all the organizationTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganizationType> findAll(Pageable pageable);


    /**
     * Get the "id" organizationType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationType> findOne(Long id);

    /**
     * Delete the "id" organizationType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the organizationType corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrganizationType> search(String query, Pageable pageable);
}
