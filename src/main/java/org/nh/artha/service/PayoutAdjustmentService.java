package org.nh.artha.service;

import org.nh.artha.domain.PayoutAdjustment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Interface for managing {@link PayoutAdjustment}.
 */
public interface PayoutAdjustmentService {

    /**
     * Save a payoutAdjustment.
     *
     * @param payoutAdjustment the entity to save.
     * @return the persisted entity.
     */
    PayoutAdjustment save(PayoutAdjustment payoutAdjustment);

    /**
     * Get all the payoutAdjustments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutAdjustment> findAll(Pageable pageable);

    /**
     * Get the "id" payoutAdjustment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayoutAdjustment> findOne(Long id);

    /**
     * Delete the "id" payoutAdjustment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the payoutAdjustment corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutAdjustment> search(String query, Pageable pageable);

    void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate);
}
