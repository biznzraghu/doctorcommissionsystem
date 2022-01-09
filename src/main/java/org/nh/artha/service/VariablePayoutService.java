package org.nh.artha.service;

import org.nh.artha.domain.VariablePayout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link VariablePayout}.
 */
public interface VariablePayoutService {

    /**
     * Save a variablePayout.
     *
     * @param variablePayout the entity to save.
     * @return the persisted entity.
     */
    VariablePayout save(VariablePayout variablePayout);

    /**
     * Get all the variablePayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VariablePayout> findAll(Pageable pageable);

    /**
     * Get the "id" variablePayout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VariablePayout> findOne(Long id);

    /**
     * Delete the "id" variablePayout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the variablePayout corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VariablePayout> search(String query, Pageable pageable);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    VariablePayout update(VariablePayout variablePayout);

    VariablePayout saveAndUpdate(VariablePayout variablePayout);

    List<VariablePayout>  getPayouts(String query, Pageable pageable);

    List<Integer> getDistinctVersion(String query) throws IOException;

    Map<String, String> exportVariablePayout(String query,Pageable pageable) throws IOException;
}
