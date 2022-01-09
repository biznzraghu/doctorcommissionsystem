package org.nh.artha.service.impl;

import org.elasticsearch.index.query.Operator;
import org.nh.artha.domain.Feature;
import org.nh.artha.repository.FeatureRepository;
import org.nh.artha.repository.search.FeatureSearchRepository;
import org.nh.artha.service.FeatureService;
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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Feature}.
 */
@Service
@Transactional
public class FeatureServiceImpl implements FeatureService {

    private final Logger log = LoggerFactory.getLogger(FeatureServiceImpl.class);

    private final FeatureRepository featureRepository;

    private final FeatureSearchRepository featureSearchRepository;

    public FeatureServiceImpl(FeatureRepository featureRepository, FeatureSearchRepository featureSearchRepository) {
        this.featureRepository = featureRepository;
        this.featureSearchRepository = featureSearchRepository;
    }

    /**
     * Save a feature.
     *
     * @param feature the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Feature save(Feature feature) {
        log.debug("Request to save Feature : {}", feature);
        Feature result = featureRepository.save(feature);
        featureSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the features.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Feature> findAll(Pageable pageable) {
        log.debug("Request to get all Features");
        return featureRepository.findAll(pageable);
    }


    /**
     * Get one feature by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Feature> findOne(Long id) {
        log.debug("Request to get Feature : {}", id);
        return featureRepository.findById(id);
    }

    /**
     * Delete the feature by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Feature : {}", id);
        featureRepository.deleteById(id);
        featureSearchRepository.deleteById(id);
    }

    /**
     * Search for the feature corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Feature> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Features for query {}", query);
        return featureSearchRepository.search(queryStringQuery(query).defaultOperator(Operator.AND), pageable);
    }

    /***
     *
     * @param moduleId
     * @return
     */

    @Override
    public List findFeatureMenusForModule(Long moduleId) {
        return featureRepository.findFeatureMenusForModule(moduleId);
    }

    /****
     *
     * @return
     */

    @Override
    public List findFeatureMenusForAllModule() {
        return featureRepository.findFeatureMenusForAllModule();
    }

    @Override
    public void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Feature");
        List<Feature> data = featureRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(i, pageSize));
        if (!data.isEmpty()) {
            featureSearchRepository.saveAll(data);
        }
    }
}
