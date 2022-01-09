package org.nh.artha.service.impl;


import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.WidgetMaster;
import org.nh.artha.repository.WidgetMasterRepository;
import org.nh.artha.repository.search.WidgetMasterSearchRepository;
import org.nh.artha.service.WidgetMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing WidgetMaster.
 */
@Service
@Transactional
public class WidgetMasterServiceImpl implements WidgetMasterService {

    private final Logger log = LoggerFactory.getLogger(WidgetMasterServiceImpl.class);

    private final WidgetMasterRepository widgetMasterRepository;

    private final WidgetMasterSearchRepository widgetMasterSearchRepository;

    private final ApplicationProperties applicationProperties;

    public WidgetMasterServiceImpl(WidgetMasterRepository widgetMasterRepository,
                                   WidgetMasterSearchRepository widgetMasterSearchRepository,
                                   ApplicationProperties applicationProperties) {
        this.widgetMasterRepository = widgetMasterRepository;
        this.widgetMasterSearchRepository = widgetMasterSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a widgetMaster.
     *
     * @param widgetMaster the entity to save
     * @return the persisted entity
     */
    @Override
    public WidgetMaster save(WidgetMaster widgetMaster) {
        log.debug("Request to save WidgetMaster : {}", widgetMaster);
        WidgetMaster result = widgetMasterRepository.save(widgetMaster);
        widgetMasterSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the widgetMasters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WidgetMaster> findAll(Pageable pageable) {
        log.debug("Request to get all WidgetMasters");
        return widgetMasterRepository.findAll(pageable);
    }

    /**
     * Get one widgetMaster by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WidgetMaster findOne(Long id) {
        log.debug("Request to get WidgetMaster : {}", id);
        return widgetMasterRepository.findById(id).get();
    }

    /**
     * Delete the  widgetMaster by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WidgetMaster : {}", id);
        widgetMasterRepository.deleteById(id);
        widgetMasterSearchRepository.deleteById(id);
    }

    /**
     * Search for the widgetMaster corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WidgetMaster> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WidgetMasters for query {}", query);
        Page<WidgetMaster> result = widgetMasterSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     * Do elastic index for widgetMaster.
     */
    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on widgetMaster");
        List<WidgetMaster> data = widgetMasterRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            widgetMasterSearchRepository.saveAll(data);
        }
    }

    /**
     * Delete entire elastic index of widgetMaster.
     */
    @Override
    public void deleteIndex() {
        log.debug("Request to delete elastic index on widgetMaster");
        widgetMasterSearchRepository.deleteAll();
    }

    /***
     *
     * @return
     */
    @Override
    public String getDashboardImagePath() {
        log.debug("Request to get Dashboard Image Path");
       /* String imagePath = applicationProperties.getAthmaBucket().getDashboardImage();*/
        return "";
    }
}
