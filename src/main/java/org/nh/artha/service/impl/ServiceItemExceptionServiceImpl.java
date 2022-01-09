package org.nh.artha.service.impl;

import org.nh.artha.service.ServiceItemExceptionService;
import org.nh.artha.domain.ServiceItemException;
import org.nh.artha.repository.ServiceItemExceptionRepository;
import org.nh.artha.repository.search.ServiceItemExceptionSearchRepository;
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
 * Service Implementation for managing {@link ServiceItemException}.
 */
@Service
@Transactional
public class ServiceItemExceptionServiceImpl implements ServiceItemExceptionService {

    private final Logger log = LoggerFactory.getLogger(ServiceItemExceptionServiceImpl.class);

    private final ServiceItemExceptionRepository serviceItemExceptionRepository;

    private final ServiceItemExceptionSearchRepository serviceItemExceptionSearchRepository;

    public ServiceItemExceptionServiceImpl(ServiceItemExceptionRepository serviceItemExceptionRepository, ServiceItemExceptionSearchRepository serviceItemExceptionSearchRepository) {
        this.serviceItemExceptionRepository = serviceItemExceptionRepository;
        this.serviceItemExceptionSearchRepository = serviceItemExceptionSearchRepository;
    }

    /**
     * Save a serviceItemException.
     *
     * @param serviceItemException the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ServiceItemException save(ServiceItemException serviceItemException) {
        log.debug("Request to save ServiceItemException : {}", serviceItemException);
        ServiceItemException result = serviceItemExceptionRepository.save(serviceItemException);
        serviceItemExceptionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the serviceItemExceptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceItemException> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceItemExceptions");
        return serviceItemExceptionRepository.findAll(pageable);
    }

    /**
     * Get one serviceItemException by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceItemException> findOne(Long id) {
        log.debug("Request to get ServiceItemException : {}", id);
        return serviceItemExceptionRepository.findById(id);
    }

    /**
     * Delete the serviceItemException by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceItemException : {}", id);
        serviceItemExceptionRepository.deleteById(id);
        serviceItemExceptionSearchRepository.deleteById(id);
    }

    /**
     * Search for the serviceItemException corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceItemException> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceItemExceptions for query {}", query);
        return serviceItemExceptionSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public List<ServiceItemException> saveAll(List<ServiceItemException> serviceItemException) {
        log.debug("Request to save ServiceItemException : {}", serviceItemException);
        List<ServiceItemException> result = serviceItemExceptionRepository.saveAll(serviceItemException);
        serviceItemExceptionSearchRepository.saveAll(result);
        return result;
    }
}
