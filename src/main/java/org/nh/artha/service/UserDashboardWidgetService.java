package org.nh.artha.service;


import org.nh.artha.domain.UserDashboardWidget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing UserDashboardWidget.
 */
public interface UserDashboardWidgetService {

    /**
     * Save a userDashboardWidget.
     *
     * @param userDashboardWidget the entity to save
     * @return the persisted entity
     */
    UserDashboardWidget save(UserDashboardWidget userDashboardWidget);

    /**
     * @param userDashboardWidgets
     * @return
     */

    List<UserDashboardWidget> saveAll(List<UserDashboardWidget> userDashboardWidgets);

    /**
     * Get all the userDashboardWidgets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserDashboardWidget> findAll(Pageable pageable);

    /**
     * Get the "id" userDashboardWidget.
     *
     * @param id the id of the entity
     * @return the entity
     */
    UserDashboardWidget findOne(Long id);

    /**
     * Delete the "id" userDashboardWidget.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userDashboardWidget corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserDashboardWidget> search(String query, Pageable pageable);

    /**
     * Do elastic index for userDashboardWidget.
     */
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    /**
     * Delete elastic index for all the userDashboardWidget.
     */
    void deleteIndex();
}
