package org.nh.artha.service.impl;

import org.nh.artha.service.ExceptionSponsorService;
import org.nh.artha.domain.ExceptionSponsor;
import org.nh.artha.repository.ExceptionSponsorRepository;
import org.nh.artha.repository.search.ExceptionSponsorSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ExceptionSponsor}.
 */
@Service
@Transactional
public class ExceptionSponsorServiceImpl implements ExceptionSponsorService {

    private final Logger log = LoggerFactory.getLogger(ExceptionSponsorServiceImpl.class);

    private final ExceptionSponsorRepository exceptionSponsorRepository;

    private final ExceptionSponsorSearchRepository exceptionSponsorSearchRepository;

    public ExceptionSponsorServiceImpl(ExceptionSponsorRepository exceptionSponsorRepository, ExceptionSponsorSearchRepository exceptionSponsorSearchRepository) {
        this.exceptionSponsorRepository = exceptionSponsorRepository;
        this.exceptionSponsorSearchRepository = exceptionSponsorSearchRepository;
    }

    /**
     * Save a exceptionSponsor.
     *
     * @param exceptionSponsor the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ExceptionSponsor save(ExceptionSponsor exceptionSponsor) {
        log.debug("Request to save ExceptionSponsor : {}", exceptionSponsor);
        ExceptionSponsor result = exceptionSponsorRepository.save(exceptionSponsor);
        exceptionSponsorSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the exceptionSponsors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExceptionSponsor> findAll(Pageable pageable) {
        log.debug("Request to get all ExceptionSponsors");
        return exceptionSponsorRepository.findAll(pageable);
    }

    /**
     * Get one exceptionSponsor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ExceptionSponsor> findOne(Long id) {
        log.debug("Request to get ExceptionSponsor : {}", id);
        return exceptionSponsorRepository.findById(id);
    }

    /**
     * Delete the exceptionSponsor by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExceptionSponsor : {}", id);
        exceptionSponsorRepository.deleteById(id);
        exceptionSponsorSearchRepository.deleteById(id);
    }

    /**
     * Search for the exceptionSponsor corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExceptionSponsor> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExceptionSponsors for query {}", query);
        return exceptionSponsorSearchRepository.search(queryStringQuery(query), pageable);    }
}
