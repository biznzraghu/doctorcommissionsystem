package org.nh.artha.service.impl;

import org.elasticsearch.index.query.Operator;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserPersonalisation;
import org.nh.artha.repository.UserPersonalisationRepository;
import org.nh.artha.repository.search.UserPersonalisationSearchRepository;
import org.nh.artha.service.UserPersonalisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing UserPersonalisation.
 */
@Service
@Transactional
public class UserPersonalisationServiceImpl implements UserPersonalisationService {

    private final Logger log = LoggerFactory.getLogger(UserPersonalisationServiceImpl.class);

    private final UserPersonalisationRepository userPersonalisationRepository;

    private final UserPersonalisationSearchRepository userPersonalisationSearchRepository;

    private final ApplicationProperties applicationProperties;

    public UserPersonalisationServiceImpl(UserPersonalisationRepository userPersonalisationRepository,
                                          UserPersonalisationSearchRepository userPersonalisationSearchRepository,
                                          ApplicationProperties applicationProperties) {
        this.userPersonalisationRepository = userPersonalisationRepository;
        this.userPersonalisationSearchRepository = userPersonalisationSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a userPersonalisation.
     *
     * @param userPersonalisation the entity to save
     * @return the persisted entity
     */
    @Override
    public UserPersonalisation save(UserPersonalisation userPersonalisation) {
        log.debug("Request to save UserPersonalisation : {}", userPersonalisation);
        userPersonalisation.setCreatedDate(LocalDateTime.now());
        UserPersonalisation result = userPersonalisationRepository.save(userPersonalisation);
        userPersonalisationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userPersonalisations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserPersonalisation> findAll(Pageable pageable) {
        log.debug("Request to get all UserPersonalisations");
        Page<UserPersonalisation> result = userPersonalisationRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one userPersonalisation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserPersonalisation findOne(Long id) {
        log.debug("Request to get UserPersonalisation : {}", id);
        UserPersonalisation userPersonalisation = userPersonalisationRepository.findById(id).get();
        return userPersonalisation;
    }

    /**
     * Delete the  userPersonalisation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserPersonalisation : {}", id);
        userPersonalisationRepository.deleteById(id);
        userPersonalisationSearchRepository.deleteById(id);
    }

    /**
     * Search for the userPersonalisation corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserPersonalisation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserPersonalisations for query {}", query);
        Page<UserPersonalisation> result = userPersonalisationSearchRepository.search(queryStringQuery(query)
                .defaultOperator(Operator.AND)
            , pageable);
        return result;
    }

    /***
     *
     */
    @Override
    @Transactional(readOnly = true)
    public void deleteIndex() {
        log.debug("Request to delete elastic index of UserPersonalisations");
        userPersonalisationSearchRepository.deleteAll();
    }

    /**
     * @param pageNo
     * @param pageSize
     */
    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on UserPersonalisations");
        List<UserPersonalisation> data = userPersonalisationRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            userPersonalisationSearchRepository.saveAll(data);
        }

    }
}
