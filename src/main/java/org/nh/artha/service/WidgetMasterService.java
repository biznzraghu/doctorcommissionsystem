package org.nh.artha.service;


import org.nh.artha.domain.WidgetMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * Service Interface for managing WidgetMaster.
 */
public interface WidgetMasterService {

    /**
     * Save a widgetMaster.
     *
     * @param widgetMaster the entity to save
     * @return the persisted entity
     */
    WidgetMaster save(WidgetMaster widgetMaster);

    /**
     * Get all the widgetMasters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WidgetMaster> findAll(Pageable pageable);

    /**
     * Get the "id" widgetMaster.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WidgetMaster findOne(Long id);

    /**
     * Delete the "id" widgetMaster.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the widgetMaster corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WidgetMaster> search(String query, Pageable pageable);

    /**
     * Do elastic index for widgetMaster.
     */
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    /**
     * Delete elastic index for all the widgetMasters.
     */
    void deleteIndex();

    /**
     *
     * @return
     */
    String getDashboardImagePath();
}
