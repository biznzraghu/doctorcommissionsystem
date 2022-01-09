package org.nh.artha.service.impl;

import org.elasticsearch.index.query.Operator;
import org.nh.artha.service.ValueSetService;
import org.nh.artha.domain.ValueSet;
import org.nh.artha.repository.ValueSetRepository;
import org.nh.artha.repository.search.ValueSetSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ValueSet}.
 */
@Service
@Transactional
public class ValueSetServiceImpl implements ValueSetService {

    private final Logger log = LoggerFactory.getLogger(ValueSetServiceImpl.class);

    private final ValueSetRepository valueSetRepository;

    private final ValueSetSearchRepository valueSetSearchRepository;

    public ValueSetServiceImpl(ValueSetRepository valueSetRepository, ValueSetSearchRepository valueSetSearchRepository) {
        this.valueSetRepository = valueSetRepository;
        this.valueSetSearchRepository = valueSetSearchRepository;
    }

    /**
     * Save a valueSet.
     *
     * @param valueSet the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ValueSet save(ValueSet valueSet) {
        log.debug("Request to save ValueSet : {}", valueSet);
        ValueSet result = valueSetRepository.save(valueSet);
        valueSetSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the valueSets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ValueSet> findAll(Pageable pageable) {
        log.debug("Request to get all ValueSets");
        return valueSetRepository.findAll(pageable);
    }


    /**
     * Get one valueSet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ValueSet> findOne(Long id) {
        log.debug("Request to get ValueSet : {}", id);
        return valueSetRepository.findById(id);
    }

    /**
     * Delete the valueSet by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ValueSet : {}", id);
        valueSetRepository.deleteById(id);
        valueSetSearchRepository.deleteById(id);
    }

    /**
     * Search for the valueSet corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ValueSet> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ValueSets for query {}", query);
        return valueSetSearchRepository.search(queryStringQuery(query).field("code").field("name").field("active")
            .defaultOperator(Operator.AND), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on ValueSets");
        List<ValueSet> data = valueSetRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            valueSetSearchRepository.saveAll(data);
        }

    }
}
