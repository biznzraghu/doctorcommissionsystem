package org.nh.artha.service.impl;

import org.nh.artha.service.PackageCodeMappingService;
import org.nh.artha.domain.PackageCodeMapping;
import org.nh.artha.repository.PackageCodeMappingRepository;
import org.nh.artha.repository.search.PackageCodeMappingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PackageCodeMapping}.
 */
@Service
@Transactional
public class PackageCodeMappingServiceImpl implements PackageCodeMappingService {

    private final Logger log = LoggerFactory.getLogger(PackageCodeMappingServiceImpl.class);

    private final PackageCodeMappingRepository packageCodeMappingRepository;

    private final PackageCodeMappingSearchRepository packageCodeMappingSearchRepository;

    public PackageCodeMappingServiceImpl(PackageCodeMappingRepository packageCodeMappingRepository, PackageCodeMappingSearchRepository packageCodeMappingSearchRepository) {
        this.packageCodeMappingRepository = packageCodeMappingRepository;
        this.packageCodeMappingSearchRepository = packageCodeMappingSearchRepository;
    }

    /**
     * Save a packageCodeMapping.
     *
     * @param packageCodeMapping the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PackageCodeMapping save(PackageCodeMapping packageCodeMapping) {
        log.debug("Request to save PackageCodeMapping : {}", packageCodeMapping);
        PackageCodeMapping result = packageCodeMappingRepository.save(packageCodeMapping);
        packageCodeMappingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the packageCodeMappings.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PackageCodeMapping> findAll() {
        log.debug("Request to get all PackageCodeMappings");
        return packageCodeMappingRepository.findAll();
    }


    /**
     * Get one packageCodeMapping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PackageCodeMapping> findOne(Long id) {
        log.debug("Request to get PackageCodeMapping : {}", id);
        return packageCodeMappingRepository.findById(id);
    }

    /**
     * Delete the packageCodeMapping by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PackageCodeMapping : {}", id);
        packageCodeMappingRepository.deleteById(id);
        packageCodeMappingSearchRepository.deleteById(id);
    }

    /**
     * Search for the packageCodeMapping corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PackageCodeMapping> search(String query) {
        log.debug("Request to search PackageCodeMappings for query {}", query);
        return StreamSupport
            .stream(packageCodeMappingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
