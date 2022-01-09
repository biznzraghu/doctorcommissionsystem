package org.nh.artha.service.impl;

import org.nh.artha.service.ApplicableConsultantService;
import org.nh.artha.domain.ApplicableConsultant;
import org.nh.artha.repository.ApplicableConsultantRepository;
import org.nh.artha.repository.search.ApplicableConsultantSearchRepository;
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
 * Service Implementation for managing {@link ApplicableConsultant}.
 */
@Service
@Transactional
public class ApplicableConsultantServiceImpl implements ApplicableConsultantService {

    private final Logger log = LoggerFactory.getLogger(ApplicableConsultantServiceImpl.class);

    private final ApplicableConsultantRepository applicableConsultantRepository;

    private final ApplicableConsultantSearchRepository applicableConsultantSearchRepository;

    public ApplicableConsultantServiceImpl(ApplicableConsultantRepository applicableConsultantRepository, ApplicableConsultantSearchRepository applicableConsultantSearchRepository) {
        this.applicableConsultantRepository = applicableConsultantRepository;
        this.applicableConsultantSearchRepository = applicableConsultantSearchRepository;
    }

    /**
     * Save a applicableConsultant.
     *
     * @param applicableConsultant the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ApplicableConsultant save(ApplicableConsultant applicableConsultant) {
        log.debug("Request to save ApplicableConsultant : {}", applicableConsultant);
        ApplicableConsultant result = applicableConsultantRepository.save(applicableConsultant);
        applicableConsultantSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the applicableConsultants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicableConsultant> findAll(Pageable pageable) {
        log.debug("Request to get all ApplicableConsultants");
        return applicableConsultantRepository.findAll(pageable);
    }

    /**
     * Get one applicableConsultant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicableConsultant> findOne(Long id) {
        log.debug("Request to get ApplicableConsultant : {}", id);
        return applicableConsultantRepository.findById(id);
    }

    /**
     * Delete the applicableConsultant by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicableConsultant : {}", id);
        applicableConsultantRepository.deleteById(id);
        applicableConsultantSearchRepository.deleteById(id);
    }

    /**
     * Search for the applicableConsultant corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicableConsultant> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ApplicableConsultants for query {}", query);
        return applicableConsultantSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public List<ApplicableConsultant> saveAll(List<ApplicableConsultant> applicableConsultants) {
        List<ApplicableConsultant> applicableConsultantList = applicableConsultantRepository.saveAll(applicableConsultants);
        return applicableConsultantList;
    }
}
