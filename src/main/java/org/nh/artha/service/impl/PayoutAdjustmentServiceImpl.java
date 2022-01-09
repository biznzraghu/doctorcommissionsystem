package org.nh.artha.service.impl;

import org.nh.artha.domain.PayoutAdjustment;
import org.nh.artha.repository.PayoutAdjustmentRepository;
import org.nh.artha.repository.search.PayoutAdjustmentSearchRepository;
import org.nh.artha.service.PayoutAdjustmentService;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link PayoutAdjustment}.
 */
@Service
@Transactional
public class PayoutAdjustmentServiceImpl implements PayoutAdjustmentService {

    private final Logger log = LoggerFactory.getLogger(PayoutAdjustmentServiceImpl.class);

    private final PayoutAdjustmentRepository payoutAdjustmentRepository;

    private final PayoutAdjustmentSearchRepository payoutAdjustmentSearchRepository;

    private final ArthaSequenceGeneratorService sequenceGeneratorService;

    public PayoutAdjustmentServiceImpl(PayoutAdjustmentRepository payoutAdjustmentRepository, PayoutAdjustmentSearchRepository payoutAdjustmentSearchRepository, ArthaSequenceGeneratorService sequenceGeneratorService) {
        this.payoutAdjustmentRepository = payoutAdjustmentRepository;
        this.payoutAdjustmentSearchRepository = payoutAdjustmentSearchRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    /**
     * Save a payoutAdjustment.
     *
     * @param payoutAdjustment the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PayoutAdjustment save(PayoutAdjustment payoutAdjustment) {
        log.debug("Request to save PayoutAdjustment : {}", payoutAdjustment);
        if (payoutAdjustment.getDocumentNumber() == null || payoutAdjustment.getDocumentNumber().isEmpty()) {
            payoutAdjustment.setDocumentNumber(sequenceGeneratorService.generateNumber("PayoutAdjustmentNumber","NH",payoutAdjustment));
        }
        PayoutAdjustment result = payoutAdjustmentRepository.save(payoutAdjustment);
        payoutAdjustmentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the payoutAdjustments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayoutAdjustment> findAll(Pageable pageable) {
        log.debug("Request to get all PayoutAdjustments");
        return payoutAdjustmentRepository.findAll(pageable);
    }

    /**
     * Get one payoutAdjustment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PayoutAdjustment> findOne(Long id) {
        log.debug("Request to get PayoutAdjustment : {}", id);
        return payoutAdjustmentRepository.findById(id);
    }

    /**
     * Delete the payoutAdjustment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PayoutAdjustment : {}", id);
        payoutAdjustmentRepository.deleteById(id);
        payoutAdjustmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the payoutAdjustment corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayoutAdjustment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PayoutAdjustments for query {}", query);
        return payoutAdjustmentSearchRepository.search(queryStringQuery(query).field("documentNumber").field("employeeDetail.displayName"), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Payout Adjustment");
        List<PayoutAdjustment> data = payoutAdjustmentRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            data.forEach(payoutAdjustment -> {
                payoutAdjustmentSearchRepository.indexWithoutRefresh(payoutAdjustment);
            });
        }
    }
}
