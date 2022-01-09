package org.nh.artha.service.impl;

import org.nh.artha.service.InvoiceDoctorPayoutService;
import org.nh.artha.domain.InvoiceDoctorPayout;
import org.nh.artha.repository.InvoiceDoctorPayoutRepository;
import org.nh.artha.repository.search.InvoiceDoctorPayoutSearchRepository;
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
 * Service Implementation for managing {@link InvoiceDoctorPayout}.
 */
@Service
@Transactional
public class InvoiceDoctorPayoutServiceImpl implements InvoiceDoctorPayoutService {

    private final Logger log = LoggerFactory.getLogger(InvoiceDoctorPayoutServiceImpl.class);

    private final InvoiceDoctorPayoutRepository invoiceDoctorPayoutRepository;

    private final InvoiceDoctorPayoutSearchRepository invoiceDoctorPayoutSearchRepository;

    public InvoiceDoctorPayoutServiceImpl(InvoiceDoctorPayoutRepository invoiceDoctorPayoutRepository, InvoiceDoctorPayoutSearchRepository invoiceDoctorPayoutSearchRepository) {
        this.invoiceDoctorPayoutRepository = invoiceDoctorPayoutRepository;
        this.invoiceDoctorPayoutSearchRepository = invoiceDoctorPayoutSearchRepository;
    }

    /**
     * Save a invoiceDoctorPayout.
     *
     * @param invoiceDoctorPayout the entity to save.
     * @return the persisted entity.
     */
    @Override
    public InvoiceDoctorPayout save(InvoiceDoctorPayout invoiceDoctorPayout) {
        log.debug("Request to save InvoiceDoctorPayout : {}", invoiceDoctorPayout);
        InvoiceDoctorPayout result = invoiceDoctorPayoutRepository.save(invoiceDoctorPayout);
        invoiceDoctorPayoutSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the invoiceDoctorPayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDoctorPayout> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceDoctorPayouts");
        return invoiceDoctorPayoutRepository.findAll(pageable);
    }


    /**
     * Get one invoiceDoctorPayout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceDoctorPayout> findOne(Long id) {
        log.debug("Request to get InvoiceDoctorPayout : {}", id);
        return invoiceDoctorPayoutRepository.findById(id);
    }

    /**
     * Delete the invoiceDoctorPayout by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvoiceDoctorPayout : {}", id);
        invoiceDoctorPayoutRepository.deleteById(id);
        invoiceDoctorPayoutSearchRepository.deleteById(id);
    }

    /**
     * Search for the invoiceDoctorPayout corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDoctorPayout> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InvoiceDoctorPayouts for query {}", query);
        return invoiceDoctorPayoutSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<InvoiceDoctorPayout> saveAll(List<InvoiceDoctorPayout> invoiceDoctorPayouts) {
        List<InvoiceDoctorPayout> invoiceDoctorPayoutsList = invoiceDoctorPayoutRepository.saveAll(invoiceDoctorPayouts);
        return invoiceDoctorPayoutsList;

    }
}
