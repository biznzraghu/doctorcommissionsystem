package org.nh.artha.service;

import org.nh.artha.domain.ValueSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Interface for managing {@link ValueSet}.
 */
public interface ValueSetService {

    /**
     * Save a valueSet.
     *
     * @param valueSet the entity to save.
     * @return the persisted entity.
     */
    ValueSet save(ValueSet valueSet);

    /**
     * Get all the valueSets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValueSet> findAll(Pageable pageable);


    /**
     * Get the "id" valueSet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ValueSet> findOne(Long id);

    /**
     * Delete the "id" valueSet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the valueSet corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValueSet> search(String query, Pageable pageable);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);
}
