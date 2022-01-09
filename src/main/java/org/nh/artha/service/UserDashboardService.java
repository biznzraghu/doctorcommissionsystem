package org.nh.artha.service;


import org.nh.artha.domain.UserDashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing UserDashboard.
 */
public interface UserDashboardService {

    /**
     * Save a userDashboard.
     *
     * @param userDashboard the entity to save
     * @return the persisted entity
     */
    UserDashboard save(UserDashboard userDashboard);


    /**
     * SaveAll a userDashboard.
     *
     * @param userDashboards the entity to save
     * @return the persisted entity
     */
    List<UserDashboard> saveAll(List<UserDashboard> userDashboards);

    /**
     * Get all the userDashboards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserDashboard> findAll(Pageable pageable);

    /**
     * Get the "id" userDashboard.
     *
     * @param id the id of the entity
     * @return the entity
     */
    UserDashboard findOne(Long id);

    /**
     * Delete the "id" userDashboard.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userDashboard corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserDashboard> search(String query, Pageable pageable);


    /**
     * Do elastic index for userDashboardWidget.
     */
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    /**
     * Delete elastic index for all the userDashboardWidget.
     */
    void deleteIndex();
}
