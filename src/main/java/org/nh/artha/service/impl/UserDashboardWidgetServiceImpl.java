package org.nh.artha.service.impl;


import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserDashboardWidget;
import org.nh.artha.domain.WidgetMaster;
import org.nh.artha.repository.UserDashboardRepository;
import org.nh.artha.repository.UserDashboardWidgetRepository;
import org.nh.artha.repository.WidgetMasterRepository;
import org.nh.artha.repository.search.UserDashboardWidgetSearchRepository;
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
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing UserDashboardWidget.
 */
@Service
@Transactional
public class UserDashboardWidgetServiceImpl implements UserDashboardWidgetService {

    private final Logger log = LoggerFactory.getLogger(UserDashboardWidgetServiceImpl.class);

    private final UserDashboardWidgetRepository userDashboardWidgetRepository;

    private final UserDashboardRepository userDashboardRepository;

    private final UserDashboardWidgetSearchRepository userDashboardWidgetSearchRepository;

    private final WidgetMasterRepository widgetMasterRepository;

    private final ApplicationProperties applicationProperties;

    public UserDashboardWidgetServiceImpl(UserDashboardWidgetRepository userDashboardWidgetRepository,
                                          UserDashboardWidgetSearchRepository userDashboardWidgetSearchRepository,
                                          WidgetMasterRepository widgetMasterRepository,
                                          UserDashboardRepository userDashboardRepository,
                                          ApplicationProperties applicationProperties) {
        this.userDashboardWidgetRepository = userDashboardWidgetRepository;
        this.userDashboardWidgetSearchRepository = userDashboardWidgetSearchRepository;
        this.widgetMasterRepository = widgetMasterRepository;
        this.userDashboardRepository = userDashboardRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a userDashboardWidget.
     *
     * @param userDashboardWidget the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    public UserDashboardWidget save(UserDashboardWidget userDashboardWidget) {
        log.debug("Request to save UserDashboardWidget : {}", userDashboardWidget);

        if (userDashboardWidget.getCreatedOn() == null)
            userDashboardWidget.setCreatedOn(LocalDateTime.now());

        if (userDashboardWidget.getUpdatedOn() == null)
            userDashboardWidget.setUpdatedOn(LocalDateTime.now());
        userDashboardWidget.setUserDashboard(userDashboardRepository.findById(userDashboardWidget.getUserDashboard().getId()).get());
        WidgetMaster widgetMaster = widgetMasterRepository.findById(userDashboardWidget.getWidgetMaster().getId()).get();
        widgetMaster.setThumbnail("");
        userDashboardWidget.setWidgetMaster(widgetMaster);
        UserDashboardWidget result = userDashboardWidgetRepository.save(userDashboardWidget);
        userDashboardWidgetSearchRepository.save(result);
        return result;
    }

    /**
     * @param userDashboardWidgets
     * @return
     */
    @Override
    public List<UserDashboardWidget> saveAll(List<UserDashboardWidget> userDashboardWidgets) {
        log.debug("Request to save userDashboardWidgets : {}", userDashboardWidgets);
        for (UserDashboardWidget uDashboardWidget : userDashboardWidgets) {
            if (uDashboardWidget.getCreatedOn() == null)
                uDashboardWidget.setCreatedOn(LocalDateTime.now());

            if (uDashboardWidget.getUpdatedOn() == null)
                uDashboardWidget.setUpdatedOn(LocalDateTime.now());

            uDashboardWidget.setUserDashboard(userDashboardRepository.findById(uDashboardWidget.getUserDashboard().getId()).get());
            WidgetMaster widgetMaster = widgetMasterRepository.findById(uDashboardWidget.getWidgetMaster().getId()).get();
            widgetMaster.setThumbnail("");
            uDashboardWidget.setWidgetMaster(widgetMaster);
            UserDashboardWidget result = userDashboardWidgetRepository.save(uDashboardWidget);
            userDashboardWidgetSearchRepository.save(result);
        }
        List<UserDashboardWidget> udws = userDashboardWidgetRepository.findAllByUserDashboardId(userDashboardWidgets.get(0).getUserDashboard().getId());
        return udws;
    }

    /**
     * Get all the userDashboardWidgets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDashboardWidget> findAll(Pageable pageable) {
        log.debug("Request to get all UserDashboardWidgets");
        return userDashboardWidgetRepository.findAll(pageable);
    }

    /**
     * Get one userDashboardWidget by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserDashboardWidget findOne(Long id) {
        log.debug("Request to get UserDashboardWidget : {}", id);
        return userDashboardWidgetRepository.findById(id).get();
    }

    /**
     * Delete the  userDashboardWidget by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserDashboardWidget : {}", id);
        userDashboardWidgetRepository.deleteById(id);
        userDashboardWidgetSearchRepository.deleteById(id);
    }

    /**
     * Search for the userDashboardWidget corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDashboardWidget> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserDashboardWidgets for query {}", query);
        Page<UserDashboardWidget> result = userDashboardWidgetSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     * Do elastic index for userDashboardWidget.
     */
    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on userDashboardWidget");
        List<UserDashboardWidget> data = userDashboardWidgetRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            userDashboardWidgetSearchRepository.saveAll(data);
        }
    }

    /**
     * Delete entire elastic index of userDashboardWidget.
     */
    @Override
    public void deleteIndex() {
        log.debug("Request to delete elastic index on userDashboardWidget");
        userDashboardWidgetSearchRepository.deleteAll();
    }
}
