package org.nh.artha.service.impl;

import org.nh.artha.service.ValueSetCodeMappingService;
import org.nh.artha.domain.ValueSetCodeMapping;
import org.nh.artha.repository.ValueSetCodeMappingRepository;
import org.nh.artha.repository.search.ValueSetCodeMappingSearchRepository;
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
 * Service Implementation for managing {@link ValueSetCodeMapping}.
 */
@Service
@Transactional
public class ValueSetCodeMappingServiceImpl implements ValueSetCodeMappingService {

    private final Logger log = LoggerFactory.getLogger(ValueSetCodeMappingServiceImpl.class);

    private final ValueSetCodeMappingRepository valueSetCodeMappingRepository;

    private final ValueSetCodeMappingSearchRepository valueSetCodeMappingSearchRepository;

    public ValueSetCodeMappingServiceImpl(ValueSetCodeMappingRepository valueSetCodeMappingRepository, ValueSetCodeMappingSearchRepository valueSetCodeMappingSearchRepository) {
        this.valueSetCodeMappingRepository = valueSetCodeMappingRepository;
        this.valueSetCodeMappingSearchRepository = valueSetCodeMappingSearchRepository;
    }

    /**
     * Save a valueSetCodeMapping.
     *
     * @param valueSetCodeMapping the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ValueSetCodeMapping save(ValueSetCodeMapping valueSetCodeMapping) {
        log.debug("Request to save ValueSetCodeMapping : {}", valueSetCodeMapping);
        ValueSetCodeMapping result = valueSetCodeMappingRepository.save(valueSetCodeMapping);
        valueSetCodeMappingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the valueSetCodeMappings.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ValueSetCodeMapping> findAll() {
        log.debug("Request to get all ValueSetCodeMappings");
        return valueSetCodeMappingRepository.findAll();
    }


    /**
     * Get one valueSetCodeMapping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ValueSetCodeMapping> findOne(Long id) {
        log.debug("Request to get ValueSetCodeMapping : {}", id);
        return valueSetCodeMappingRepository.findById(id);
    }

    /**
     * Delete the valueSetCodeMapping by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ValueSetCodeMapping : {}", id);
        valueSetCodeMappingRepository.deleteById(id);
        valueSetCodeMappingSearchRepository.deleteById(id);
    }

    /**
     * Search for the valueSetCodeMapping corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ValueSetCodeMapping> search(String query) {
        log.debug("Request to search ValueSetCodeMappings for query {}", query);
        return StreamSupport
            .stream(valueSetCodeMappingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
