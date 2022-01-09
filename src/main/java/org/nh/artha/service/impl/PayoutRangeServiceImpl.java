package org.nh.artha.service.impl;

import org.nh.artha.service.PayoutRangeService;
import org.nh.artha.domain.PayoutRange;
import org.nh.artha.repository.PayoutRangeRepository;
import org.nh.artha.repository.search.PayoutRangeSearchRepository;
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
 * Service Implementation for managing {@link PayoutRange}.
 */
@Service
@Transactional
public class PayoutRangeServiceImpl implements PayoutRangeService {

    private final Logger log = LoggerFactory.getLogger(PayoutRangeServiceImpl.class);

    private final PayoutRangeRepository payoutRangeRepository;

    private final PayoutRangeSearchRepository payoutRangeSearchRepository;

    public PayoutRangeServiceImpl(PayoutRangeRepository payoutRangeRepository, PayoutRangeSearchRepository payoutRangeSearchRepository) {
        this.payoutRangeRepository = payoutRangeRepository;
        this.payoutRangeSearchRepository = payoutRangeSearchRepository;
    }

    /**
     * Save a payoutRange.
     *
     * @param payoutRange the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PayoutRange save(PayoutRange payoutRange) {
        log.debug("Request to save PayoutRange : {}", payoutRange);
        PayoutRange result = payoutRangeRepository.save(payoutRange);
        payoutRangeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the payoutRanges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayoutRange> findAll(Pageable pageable) {
        log.debug("Request to get all PayoutRanges");
        return payoutRangeRepository.findAll(pageable);
    }

    /**
     * Get one payoutRange by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PayoutRange> findOne(Long id) {
        log.debug("Request to get PayoutRange : {}", id);
        return payoutRangeRepository.findById(id);
    }

    /**
     * Delete the payoutRange by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PayoutRange : {}", id);
        payoutRangeRepository.deleteById(id);
        payoutRangeSearchRepository.deleteById(id);
    }

    /**
     * Search for the payoutRange corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayoutRange> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PayoutRanges for query {}", query);
        return payoutRangeSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<PayoutRange> saveAll(List<PayoutRange> payoutRanges) {
        List<PayoutRange> payoutRangeList = payoutRangeRepository.saveAll(payoutRanges);
        return payoutRangeList;
    }

}
