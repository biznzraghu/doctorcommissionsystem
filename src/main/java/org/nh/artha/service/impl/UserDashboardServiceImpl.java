package org.nh.artha.service.impl;

import org.apache.commons.compress.utils.Lists;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserDashboard;
import org.nh.artha.domain.UserDashboardWidget;
import org.nh.artha.repository.UserDashboardRepository;
import org.nh.artha.repository.UserDashboardWidgetRepository;
import org.nh.artha.repository.search.UserDashboardSearchRepository;
import org.nh.artha.repository.search.UserDashboardWidgetSearchRepository;
import org.nh.artha.service.UserDashboardService;
import org.nh.artha.service.UserDashboardWidgetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing UserDashboard.
 */
@Service
@Transactional
public class UserDashboardServiceImpl implements UserDashboardService {

    private final Logger log = LoggerFactory.getLogger(UserDashboardServiceImpl.class);

    private final UserDashboardRepository userDashboardRepository;
    private final UserDashboardSearchRepository userDashboardSearchRepository;

    private final UserDashboardWidgetRepository userDashboardWidgetRepository;
    private final UserDashboardWidgetSearchRepository userDashboardWidgetSearchRepository;

    private final UserDashboardWidgetService userDashboardWidgetService;

    private final ApplicationProperties applicationProperties;

    public UserDashboardServiceImpl(UserDashboardRepository userDashboardRepository,
                                    UserDashboardSearchRepository userDashboardSearchRepository,
                                    UserDashboardWidgetRepository userDashboardWidgetRepository,
                                    UserDashboardWidgetSearchRepository userDashboardWidgetSearchRepository,
                                    ApplicationProperties applicationProperties, UserDashboardWidgetService userDashboardWidgetService) {
        this.userDashboardRepository = userDashboardRepository;
        this.userDashboardSearchRepository = userDashboardSearchRepository;
        this.userDashboardWidgetRepository = userDashboardWidgetRepository;
        this.userDashboardWidgetSearchRepository = userDashboardWidgetSearchRepository;
        this.applicationProperties = applicationProperties;
        this.userDashboardWidgetService = userDashboardWidgetService;
    }

    /**
     * Save a userDashboard.
     *
     * @param userDashboard the entity to save
     * @return the persisted entity
     */
    @Override
    public UserDashboard save(UserDashboard userDashboard) {
        log.debug("Request to save UserDashboard : {}", userDashboard);

        if (userDashboard.getCreatedOn() == null)
            userDashboard.setCreatedOn(LocalDateTime.now());

        if (userDashboard.getUpdatedOn() == null)
            userDashboard.setUpdatedOn(LocalDateTime.now());

        UserDashboard result = userDashboardRepository.save(userDashboard);
        userDashboardSearchRepository.save(result);
        return result;
    }


    /**
     * SaveAll a userDashboard.
     *
     * @param userDashboards the entity to save all
     * @return the persisted entity
     */
    @Override
    public List<UserDashboard> saveAll(List<UserDashboard> userDashboards) {
        log.debug("Request to save UserDashboard : {}", userDashboards);
        for (UserDashboard uDashboard : userDashboards) {

            if (uDashboard.getCreatedOn() == null)
                uDashboard.setCreatedOn(LocalDateTime.now());

            if (uDashboard.getUpdatedOn() == null)
                uDashboard.setUpdatedOn(LocalDateTime.now());

            UserDashboard result = userDashboardRepository.save(uDashboard);
            userDashboardSearchRepository.save(result);
        }
        List<UserDashboard> uds = userDashboardRepository.findAllByUserName(userDashboards.get(0).getUserName());
        return uds;
    }

    /**
     * Get all the userDashboards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDashboard> findAll(Pageable pageable) {
        log.debug("Request to get all UserDashboards");
        return userDashboardRepository.findAll(pageable);
    }

    /**
     * Get one userDashboard by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserDashboard findOne(Long id) {
        log.debug("Request to get UserDashboard : {}", id);
        return userDashboardRepository.findById(id).get();
    }

    /**
     * Delete the  userDashboard by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        Iterable<UserDashboardWidget> userDashboardWidgets = userDashboardWidgetSearchRepository.search(queryStringQuery("userDashboard.id:" + id));
        ArrayList<UserDashboardWidget> content = Lists.newArrayList(userDashboardWidgets.iterator());
        UserDashboard userDashboard;
        if(content!=null && !content.isEmpty()){
            userDashboard = content.get(0).getUserDashboard();
            userDashboardWidgetRepository.deleteAll(content);
            userDashboardWidgetSearchRepository.deleteAll(content);
            userDashboardRepository.delete(userDashboard);
            userDashboardSearchRepository.delete(userDashboard);
        }else{
            userDashboardRepository.deleteById(id);
            userDashboardSearchRepository.deleteById(id);
        }
    }

    /**
     * Search for the userDashboard corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDashboard> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserDashboards for query {}", query);
        Page<UserDashboard> result = userDashboardSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     * Do elastic index for userDashboard.
     */
    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on userDashboard");
        List<UserDashboard> data = userDashboardRepository.findByDateRangeSortById(fromDate,toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            userDashboardSearchRepository.saveAll(data);
        }
    }

    /**
     * Delete entire elastic index of userDashboard.
     */
    @Override
    public void deleteIndex() {
        log.debug("Request to delete elastic index on userDashboard");
        userDashboardSearchRepository.deleteAll();
    }
}
