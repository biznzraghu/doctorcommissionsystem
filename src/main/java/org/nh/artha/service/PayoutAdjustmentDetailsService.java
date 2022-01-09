package org.nh.artha.service;

import org.nh.artha.domain.PayoutAdjustmentDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link PayoutAdjustmentDetails}.
 */
public interface PayoutAdjustmentDetailsService {

    /**
     * Save a payoutAdjustmentDetails.
     *
     * @param payoutAdjustmentDetails the entity to save.
     * @return the persisted entity.
     */
    PayoutAdjustmentDetails save(PayoutAdjustmentDetails payoutAdjustmentDetails);

    /**
     * Get all the payoutAdjustmentDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutAdjustmentDetails> findAll(Pageable pageable);

    /**
     * Get the "id" payoutAdjustmentDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayoutAdjustmentDetails> findOne(Long id);

    /**
     * Delete the "id" payoutAdjustmentDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the payoutAdjustmentDetails corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutAdjustmentDetails> search(String query, Pageable pageable);
}
