package org.nh.artha.service;

import org.nh.artha.domain.Privilege;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Interface for managing {@link Privilege}.
 */
public interface PrivilegeService {

    /**
     * Save a privilege.
     *
     * @param privilege the entity to save.
     * @return the persisted entity.
     */
    Privilege save(Privilege privilege);

    /**
     * Get all the privileges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Privilege> findAll(Pageable pageable);


    /**
     * Get the "id" privilege.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Privilege> findOne(Long id);

    /**
     * Delete the "id" privilege.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the privilege corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Privilege> search(String query, Pageable pageable);

    void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate);
}
