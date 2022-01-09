package org.nh.artha.service;

import org.nh.artha.domain.Group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Interface for managing {@link Group}.
 */
public interface GroupService {

    /**
     * Save a group.
     *
     * @param group the entity to save.
     * @return the persisted entity.
     */
    Group save(Group group);

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Group> findAll(Pageable pageable);


    /**
     * Get the "id" group.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Group> findOne(Long id);

    /**
     * Delete the "id" group.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the group corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Group> search(String query, Pageable pageable);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);
}
