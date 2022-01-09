package org.nh.artha.service.impl;

import org.nh.artha.service.OrganizationTypeService;
import org.nh.artha.domain.OrganizationType;
import org.nh.artha.repository.OrganizationTypeRepository;
import org.nh.artha.repository.search.OrganizationTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link OrganizationType}.
 */
@Service
@Transactional
public class OrganizationTypeServiceImpl implements OrganizationTypeService {

    private final Logger log = LoggerFactory.getLogger(OrganizationTypeServiceImpl.class);

    private final OrganizationTypeRepository organizationTypeRepository;

    private final OrganizationTypeSearchRepository organizationTypeSearchRepository;

    public OrganizationTypeServiceImpl(OrganizationTypeRepository organizationTypeRepository, OrganizationTypeSearchRepository organizationTypeSearchRepository) {
        this.organizationTypeRepository = organizationTypeRepository;
        this.organizationTypeSearchRepository = organizationTypeSearchRepository;
    }

    /**
     * Save a organizationType.
     *
     * @param organizationType the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OrganizationType save(OrganizationType organizationType) {
        log.debug("Request to save OrganizationType : {}", organizationType);
        OrganizationType result = organizationTypeRepository.save(organizationType);
        organizationTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the organizationTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationType> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationTypes");
        return organizationTypeRepository.findAll(pageable);
    }


    /**
     * Get one organizationType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationType> findOne(Long id) {
        log.debug("Request to get OrganizationType : {}", id);
        return organizationTypeRepository.findById(id);
    }

    /**
     * Delete the organizationType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganizationType : {}", id);
        organizationTypeRepository.deleteById(id);
        organizationTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the organizationType corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrganizationTypes for query {}", query);
        return organizationTypeSearchRepository.search(queryStringQuery(query), pageable);    }
}
