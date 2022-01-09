package org.nh.artha.service;

import org.nh.artha.domain.ValueSetCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ValueSetCode}.
 */
public interface ValueSetCodeService {

    /**
     * Save a valueSetCode.
     *
     * @param valueSetCode the entity to save.
     * @return the persisted entity.
     */
    ValueSetCode save(ValueSetCode valueSetCode);

    /**
     * Get all the valueSetCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValueSetCode> findAll(Pageable pageable);


    /**
     * Get the "id" valueSetCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ValueSetCode> findOne(Long id);

    /**
     * Delete the "id" valueSetCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the valueSetCode corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValueSetCode> search(String query, Pageable pageable);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    List<ValueSetCode> saveAll(List<ValueSetCode> valueSetCodes);
}
