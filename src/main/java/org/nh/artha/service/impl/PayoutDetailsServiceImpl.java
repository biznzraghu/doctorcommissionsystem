package org.nh.artha.service.impl;

import org.nh.artha.domain.Department;
import org.nh.artha.service.PayoutDetailsService;
import org.nh.artha.domain.PayoutDetails;
import org.nh.artha.repository.PayoutDetailsRepository;
import org.nh.artha.repository.search.PayoutDetailsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PayoutDetails}.
 */
@Service
@Transactional
public class PayoutDetailsServiceImpl implements PayoutDetailsService {

    private final Logger log = LoggerFactory.getLogger(PayoutDetailsServiceImpl.class);

    private final PayoutDetailsRepository payoutDetailsRepository;

    private final PayoutDetailsSearchRepository payoutDetailsSearchRepository;

    public PayoutDetailsServiceImpl(PayoutDetailsRepository payoutDetailsRepository, PayoutDetailsSearchRepository payoutDetailsSearchRepository) {
        this.payoutDetailsRepository = payoutDetailsRepository;
        this.payoutDetailsSearchRepository = payoutDetailsSearchRepository;
    }

    /**
     * Save a payoutDetails.
     *
     * @param payoutDetails the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PayoutDetails save(PayoutDetails payoutDetails) {
        log.debug("Request to save PayoutDetails : {}", payoutDetails);
        PayoutDetails result = payoutDetailsRepository.save(payoutDetails);
        payoutDetailsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the payoutDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayoutDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PayoutDetails");
        return payoutDetailsRepository.findAll(pageable);
    }


    /**
     * Get one payoutDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PayoutDetails> findOne(Long id) {
        log.debug("Request to get PayoutDetails : {}", id);
        return payoutDetailsRepository.findById(id);
    }

    /**
     * Delete the payoutDetails by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PayoutDetails : {}", id);
        payoutDetailsRepository.deleteById(id);
        payoutDetailsSearchRepository.deleteById(id);
    }

    /**
     * Search for the payoutDetails corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayoutDetails> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PayoutDetails for query {}", query);
        return payoutDetailsSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Department");
        List<PayoutDetails> data = payoutDetailsRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(i, pageSize));
        if (!data.isEmpty()) {
            payoutDetailsSearchRepository.saveAll(data);
        }
    }
}
