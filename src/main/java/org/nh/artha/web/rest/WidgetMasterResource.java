package org.nh.artha.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.WidgetMaster;
import org.nh.artha.repository.WidgetMasterRepository;
import org.nh.artha.repository.search.WidgetMasterSearchRepository;
import org.nh.artha.service.WidgetMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WidgetMaster.
 */
@RestController
@RequestMapping("/api")
public class WidgetMasterResource {

    private final Logger log = LoggerFactory.getLogger(WidgetMasterResource.class);

    private static final String ENTITY_NAME = "widgetMaster";

    private final WidgetMasterService widgetMasterService;
    private final WidgetMasterRepository widgetMasterRepository;
    private final WidgetMasterSearchRepository widgetMasterSearchRepository;
    private final ApplicationProperties applicationProperties;

    public WidgetMasterResource(WidgetMasterService widgetMasterService, WidgetMasterRepository widgetMasterRepository, WidgetMasterSearchRepository widgetMasterSearchRepository, ApplicationProperties applicationProperties) {
        this.widgetMasterService = widgetMasterService;
        this.widgetMasterRepository = widgetMasterRepository;
        this.widgetMasterSearchRepository = widgetMasterSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /widget-masters : Create a new widgetMaster.
     *
     * @param widgetMaster the widgetMaster to create
     * @return the ResponseEntity with status 201 (Created) and with body the new widgetMaster, or with status 400 (Bad Request) if the widgetMaster has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/widget-masters")
    public ResponseEntity<WidgetMaster> createWidgetMaster(@Valid @RequestBody WidgetMaster widgetMaster) throws URISyntaxException {
        log.debug("REST request to save WidgetMaster : {}", widgetMaster);
        if (widgetMaster.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, true,"idexists", "A new dashboard cannot already have an ID",null)).body(null);
        }
        WidgetMaster result = widgetMasterService.save(widgetMaster);
        return ResponseEntity.created(new URI("/api/widget-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, true,result.getId().toString(),null))
            .body(result);

    }

    /**
     * PUT  /widget-masters : Updates an existing widgetMaster.
     *
     * @param widgetMaster the widgetMaster to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated widgetMaster,
     * or with status 400 (Bad Request) if the widgetMaster is not valid,
     * or with status 500 (Internal Server Error) if the widgetMaster couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/widget-masters")
    public ResponseEntity<WidgetMaster> updateWidgetMaster(@Valid @RequestBody WidgetMaster widgetMaster) throws URISyntaxException {
        log.debug("REST request to update WidgetMaster : {}", widgetMaster);
        if (widgetMaster.getId() == null) {
            return createWidgetMaster(widgetMaster);
        }
        WidgetMaster result = widgetMasterService.save(widgetMaster);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, true,widgetMaster.getId().toString(),null))
            .body(result);
    }

    /**
     * GET  /widget-masters : get all the widgetMasters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of widgetMasters in body
     */
    @GetMapping("/widget-masters")
    public ResponseEntity<List<WidgetMaster>> getAllWidgetMasters(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WidgetMasters");
        Page<WidgetMaster> page = widgetMasterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /widget-masters/:id : get the "id" widgetMaster.
     *
     * @param id the id of the widgetMaster to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the widgetMaster, or with status 404 (Not Found)
     */
    @GetMapping("/widget-masters/{id}")
    public ResponseEntity<WidgetMaster> getWidgetMaster(@PathVariable Long id) {
        log.debug("REST request to get WidgetMaster : {}", id);
        WidgetMaster widgetMaster = widgetMasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(widgetMaster));
    }

    /**
     * DELETE  /widget-masters/:id : delete the "id" widgetMaster.
     *
     * @param id the id of the widgetMaster to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/widget-masters/{id}")

    public ResponseEntity<Void> deleteWidgetMaster(@PathVariable Long id) {
        log.debug("REST request to delete WidgetMaster : {}", id);
        widgetMasterService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME,true, id.toString(),null)).build();
    }

    /**
     * SEARCH  /_search/widget-masters?query=:query : search for the widgetMaster corresponding
     * to the query.
     *
     * @param query    the query of the widgetMaster search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/widget-masters")
    public ResponseEntity<List<WidgetMaster>> searchWidgetMasters(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of WidgetMasters for query {}", query);
        Page<WidgetMaster> page = widgetMasterService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/_index/widget-masters")
    public ResponseEntity<Void> indexWidgetMasters(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on widget-masters");
        long resultCount = widgetMasterRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            widgetMasterService.doIndex(i, pageSize, fromDate, toDate);
        }
        widgetMasterSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("artha","Elastic index is completed", "")).build();
    }


    /***
     *
     * @return
     */
    @GetMapping("/dashboard-image-path")
    public ResponseEntity<String> getDashboardImagePath() {
        log.debug("Get dashboard Image Path");
        String imagePath = widgetMasterService.getDashboardImagePath();
        return new ResponseEntity<>(imagePath, HttpStatus.OK);
    }
}
