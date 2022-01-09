package org.nh.artha.service.impl;

import org.nh.artha.service.TransactionApprovalDetailsService;
import org.nh.artha.domain.TransactionApprovalDetails;
import org.nh.artha.repository.TransactionApprovalDetailsRepository;
import org.nh.artha.repository.search.TransactionApprovalDetailsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TransactionApprovalDetails}.
 */
@Service
@Transactional
public class TransactionApprovalDetailsServiceImpl implements TransactionApprovalDetailsService {

    private final Logger log = LoggerFactory.getLogger(TransactionApprovalDetailsServiceImpl.class);

    private final TransactionApprovalDetailsRepository transactionApprovalDetailsRepository;

    private final TransactionApprovalDetailsSearchRepository transactionApprovalDetailsSearchRepository;

    public TransactionApprovalDetailsServiceImpl(TransactionApprovalDetailsRepository transactionApprovalDetailsRepository, TransactionApprovalDetailsSearchRepository transactionApprovalDetailsSearchRepository) {
        this.transactionApprovalDetailsRepository = transactionApprovalDetailsRepository;
        this.transactionApprovalDetailsSearchRepository = transactionApprovalDetailsSearchRepository;
    }

    /**
     * Save a transactionApprovalDetails.
     *
     * @param transactionApprovalDetails the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TransactionApprovalDetails save(TransactionApprovalDetails transactionApprovalDetails) {
        log.debug("Request to save TransactionApprovalDetails : {}", transactionApprovalDetails);
        TransactionApprovalDetails result = transactionApprovalDetailsRepository.save(transactionApprovalDetails);
        transactionApprovalDetailsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the transactionApprovalDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionApprovalDetails> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionApprovalDetails");
        return transactionApprovalDetailsRepository.findAll(pageable);
    }

    /**
     * Get one transactionApprovalDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionApprovalDetails> findOne(Long id) {
        log.debug("Request to get TransactionApprovalDetails : {}", id);
        return transactionApprovalDetailsRepository.findById(id);
    }

    /**
     * Delete the transactionApprovalDetails by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionApprovalDetails : {}", id);
        transactionApprovalDetailsRepository.deleteById(id);
    }

    /**
     * Search for the transactionApprovalDetails corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionApprovalDetails> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransactionApprovalDetails for query {}", query);
        return transactionApprovalDetailsSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<TransactionApprovalDetails> saveAll(List<TransactionApprovalDetails> transactionApprovalDetails) {
        List<TransactionApprovalDetails> transactionApprovalDetailsList = transactionApprovalDetailsRepository.saveAll(transactionApprovalDetails);
        return transactionApprovalDetailsList;
    }
}
