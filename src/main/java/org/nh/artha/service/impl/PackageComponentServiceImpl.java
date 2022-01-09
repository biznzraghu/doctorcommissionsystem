package org.nh.artha.service.impl;

import org.nh.artha.service.PackageComponentService;
import org.nh.artha.domain.PackageComponent;
import org.nh.artha.repository.PackageComponentRepository;
import org.nh.artha.repository.search.PackageComponentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PackageComponent}.
 */
@Service
@Transactional
public class PackageComponentServiceImpl implements PackageComponentService {

    private final Logger log = LoggerFactory.getLogger(PackageComponentServiceImpl.class);

    private final PackageComponentRepository packageComponentRepository;

    private final PackageComponentSearchRepository packageComponentSearchRepository;

    public PackageComponentServiceImpl(PackageComponentRepository packageComponentRepository, PackageComponentSearchRepository packageComponentSearchRepository) {
        this.packageComponentRepository = packageComponentRepository;
        this.packageComponentSearchRepository = packageComponentSearchRepository;
    }

    /**
     * Save a packageComponent.
     *
     * @param packageComponent the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PackageComponent save(PackageComponent packageComponent) {
        log.debug("Request to save PackageComponent : {}", packageComponent);
        PackageComponent result = packageComponentRepository.save(packageComponent);
        packageComponentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the packageComponents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PackageComponent> findAll(Pageable pageable) {
        log.debug("Request to get all PackageComponents");
        return packageComponentRepository.findAll(pageable);
    }


    /**
     * Get one packageComponent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PackageComponent> findOne(Long id) {
        log.debug("Request to get PackageComponent : {}", id);
        return packageComponentRepository.findById(id);
    }

    /**
     * Delete the packageComponent by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PackageComponent : {}", id);
        packageComponentRepository.deleteById(id);
        packageComponentSearchRepository.deleteById(id);
    }

    /**
     * Search for the packageComponent corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PackageComponent> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PackageComponents for query {}", query);
        return packageComponentSearchRepository.search(queryStringQuery(query), pageable);    }
}
