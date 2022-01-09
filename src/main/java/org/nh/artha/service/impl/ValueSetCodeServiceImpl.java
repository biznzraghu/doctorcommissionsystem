package org.nh.artha.service.impl;

import org.nh.artha.service.ValueSetCodeService;
import org.nh.artha.domain.ValueSetCode;
import org.nh.artha.repository.ValueSetCodeRepository;
import org.nh.artha.repository.search.ValueSetCodeSearchRepository;
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
 * Service Implementation for managing {@link ValueSetCode}.
 */
@Service
@Transactional
public class ValueSetCodeServiceImpl implements ValueSetCodeService {

    private final Logger log = LoggerFactory.getLogger(ValueSetCodeServiceImpl.class);

    private final ValueSetCodeRepository valueSetCodeRepository;

    private final ValueSetCodeSearchRepository valueSetCodeSearchRepository;

    public ValueSetCodeServiceImpl(ValueSetCodeRepository valueSetCodeRepository, ValueSetCodeSearchRepository valueSetCodeSearchRepository) {
        this.valueSetCodeRepository = valueSetCodeRepository;
        this.valueSetCodeSearchRepository = valueSetCodeSearchRepository;
    }

    /**
     * Save a valueSetCode.
     *
     * @param valueSetCode the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ValueSetCode save(ValueSetCode valueSetCode) {
        log.debug("Request to save ValueSetCode : {}", valueSetCode);
        ValueSetCode result = valueSetCodeRepository.save(valueSetCode);
        valueSetCodeSearchRepository.save(result);
        return result;
    }
    @Override
    public List<ValueSetCode> saveAll(List<ValueSetCode> valueSetCodeList) {
        List<ValueSetCode> valueSetCodes = valueSetCodeRepository.saveAll(valueSetCodeList);
        valueSetCodeSearchRepository.saveAll(valueSetCodes);
        return valueSetCodes;
    }

    /**
     * Get all the valueSetCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ValueSetCode> findAll(Pageable pageable) {
        log.debug("Request to get all ValueSetCodes");
        return valueSetCodeRepository.findAll(pageable);
    }


    /**
     * Get one valueSetCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ValueSetCode> findOne(Long id) {
        log.debug("Request to get ValueSetCode : {}", id);
        return valueSetCodeRepository.findById(id);
    }

    /**
     * Delete the valueSetCode by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ValueSetCode : {}", id);
        valueSetCodeRepository.deleteById(id);
        valueSetCodeSearchRepository.deleteById(id);
    }

    /**
     * Search for the valueSetCode corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ValueSetCode> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ValueSetCodes for query {}", query);
        return valueSetCodeSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on ValueSetCodes");
        List<ValueSetCode> data = valueSetCodeRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            valueSetCodeSearchRepository.saveAll(data);
        }
    }
}
