package org.nh.artha.service;

import org.nh.artha.domain.TransactionApprovalDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link TransactionApprovalDetails}.
 */
public interface TransactionApprovalDetailsService {

    /**
     * Save a transactionApprovalDetails.
     *
     * @param transactionApprovalDetails the entity to save.
     * @return the persisted entity.
     */
    TransactionApprovalDetails save(TransactionApprovalDetails transactionApprovalDetails);

    /**
     * Get all the transactionApprovalDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionApprovalDetails> findAll(Pageable pageable);

    /**
     * Get the "id" transactionApprovalDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionApprovalDetails> findOne(Long id);

    /**
     * Delete the "id" transactionApprovalDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the transactionApprovalDetails corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionApprovalDetails> search(String query, Pageable pageable);

    List<TransactionApprovalDetails> saveAll(List<TransactionApprovalDetails> transactionApprovalDetails);
}
