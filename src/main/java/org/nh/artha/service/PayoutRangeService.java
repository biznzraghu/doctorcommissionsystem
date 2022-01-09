package org.nh.artha.service;

import org.nh.artha.domain.PayoutRange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PayoutRange}.
 */
public interface PayoutRangeService {

    /**
     * Save a payoutRange.
     *
     * @param payoutRange the entity to save.
     * @return the persisted entity.
     */
    PayoutRange save(PayoutRange payoutRange);

    /**
     * Get all the payoutRanges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutRange> findAll(Pageable pageable);

    /**
     * Get the "id" payoutRange.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayoutRange> findOne(Long id);

    /**
     * Delete the "id" payoutRange.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the payoutRange corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutRange> search(String query, Pageable pageable);
    List<PayoutRange> saveAll(List<PayoutRange> payoutRanges);
}
