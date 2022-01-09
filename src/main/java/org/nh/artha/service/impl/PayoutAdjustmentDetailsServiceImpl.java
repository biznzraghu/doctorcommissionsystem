package org.nh.artha.service.impl;

import org.nh.artha.service.PayoutAdjustmentDetailsService;
import org.nh.artha.domain.PayoutAdjustmentDetails;
import org.nh.artha.repository.PayoutAdjustmentDetailsRepository;
import org.nh.artha.repository.search.PayoutAdjustmentDetailsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PayoutAdjustmentDetails}.
 */
@Service
@Transactional
public class PayoutAdjustmentDetailsServiceImpl implements PayoutAdjustmentDetailsService {

    private final Logger log = LoggerFactory.getLogger(PayoutAdjustmentDetailsServiceImpl.class);

    private final PayoutAdjustmentDetailsRepository payoutAdjustmentDetailsRepository;

    private final PayoutAdjustmentDetailsSearchRepository payoutAdjustmentDetailsSearchRepository;

    public PayoutAdjustmentDetailsServiceImpl(PayoutAdjustmentDetailsRepository payoutAdjustmentDetailsRepository, PayoutAdjustmentDetailsSearchRepository payoutAdjustmentDetailsSearchRepository) {
        this.payoutAdjustmentDetailsRepository = payoutAdjustmentDetailsRepository;
        this.payoutAdjustmentDetailsSearchRepository = payoutAdjustmentDetailsSearchRepository;
    }

    /**
     * Save a payoutAdjustmentDetails.
     *
     * @param payoutAdjustmentDetails the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PayoutAdjustmentDetails save(PayoutAdjustmentDetails payoutAdjustmentDetails) {
        log.debug("Request to save PayoutAdjustmentDetails : {}", payoutAdjustmentDetails);
        PayoutAdjustmentDetails result = payoutAdjustmentDetailsRepository.save(payoutAdjustmentDetails);
        payoutAdjustmentDetailsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the payoutAdjustmentDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayoutAdjustmentDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PayoutAdjustmentDetails");
        return payoutAdjustmentDetailsRepository.findAll(pageable);
    }

    /**
     * Get one payoutAdjustmentDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PayoutAdjustmentDetails> findOne(Long id) {
        log.debug("Request to get PayoutAdjustmentDetails : {}", id);
        return payoutAdjustmentDetailsRepository.findById(id);
    }

    /**
     * Delete the payoutAdjustmentDetails by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PayoutAdjustmentDetails : {}", id);
        payoutAdjustmentDetailsRepository.deleteById(id);
        payoutAdjustmentDetailsSearchRepository.deleteById(id);
    }

    /**
     * Search for the payoutAdjustmentDetails corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayoutAdjustmentDetails> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PayoutAdjustmentDetails for query {}", query);
        return payoutAdjustmentDetailsSearchRepository.search(queryStringQuery(query), pageable);    }
}
