package org.nh.artha.service;

import org.nh.artha.domain.PayoutDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Interface for managing {@link PayoutDetails}.
 */
public interface PayoutDetailsService {

    /**
     * Save a payoutDetails.
     *
     * @param payoutDetails the entity to save.
     * @return the persisted entity.
     */
    PayoutDetails save(PayoutDetails payoutDetails);

    /**
     * Get all the payoutDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutDetails> findAll(Pageable pageable);


    /**
     * Get the "id" payoutDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayoutDetails> findOne(Long id);

    /**
     * Delete the "id" payoutDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the payoutDetails corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PayoutDetails> search(String query, Pageable pageable);

    void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate);
}
