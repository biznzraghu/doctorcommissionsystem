package org.nh.artha.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserDashboardWidget;
import org.nh.artha.repository.UserDashboardWidgetRepository;
import org.nh.artha.repository.search.UserDashboardWidgetSearchRepository;
import org.nh.artha.service.UserDashboardWidgetService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing UserDashboardWidget.
 */
@RestController
@RequestMapping("/api")
public class UserDashboardWidgetResource {

    private final Logger log = LoggerFactory.getLogger(UserDashboardWidgetResource.class);

    private static final String ENTITY_NAME = "userDashboardWidget";

    private final UserDashboardWidgetService userDashboardWidgetService;
    private final UserDashboardWidgetRepository userDashboardWidgetRepository;
    private final UserDashboardWidgetSearchRepository userDashboardWidgetSearchRepository;
    private final ApplicationProperties applicationProperties;
    private  static final String   applicationName="artha";

    public UserDashboardWidgetResource(UserDashboardWidgetService userDashboardWidgetService, UserDashboardWidgetRepository userDashboardWidgetRepository, UserDashboardWidgetSearchRepository userDashboardWidgetSearchRepository, ApplicationProperties applicationProperties) {
        this.userDashboardWidgetService = userDashboardWidgetService;
        this.userDashboardWidgetRepository = userDashboardWidgetRepository;
        this.userDashboardWidgetSearchRepository = userDashboardWidgetSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /user-dashboard-widgets : Create a new userDashboardWidget.
     *
     * @param userDashboardWidget the userDashboardWidget to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userDashboardWidget, or with status 400 (Bad Request) if the userDashboardWidget has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-dashboard-widgets")
    public ResponseEntity<UserDashboardWidget> createUserDashboardWidget(@Valid @RequestBody UserDashboardWidget userDashboardWidget) throws URISyntaxException {
        log.debug("REST request to save UserDashboardWidget : {}", userDashboardWidget);
        if (userDashboardWidget.getId() != null) {
            throw new BadRequestAlertException("A new userDashboardWidget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDashboardWidget result = userDashboardWidgetService.save(userDashboardWidget);
        return ResponseEntity.created(new URI("/api/user-dashboard-widgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * @param UserDashboardWidgets
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/user-dashboard-widgets/all")
    public ResponseEntity<List<UserDashboardWidget>> createUserDashboardWidgets(@Valid @RequestBody List<UserDashboardWidget> UserDashboardWidgets) throws URISyntaxException {
        log.debug("REST request to save UserDashboardWidget : {}", UserDashboardWidgets);
        if (UserDashboardWidgets.size() == 0) {
            throw new BadRequestAlertException("Nothing to update", ENTITY_NAME, "idexists");
        }
        List<UserDashboardWidget> result = userDashboardWidgetService.saveAll(UserDashboardWidgets);
        return ResponseEntity.created(new URI("/api/user-dashboard-widgets/all/"))
            .body(result);
    }

    /**
     * PUT  /user-dashboard-widgets : Updates an existing userDashboardWidget.
     *
     * @param userDashboardWidget the userDashboardWidget to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userDashboardWidget,
     * or with status 400 (Bad Request) if the userDashboardWidget is not valid,
     * or with status 500 (Internal Server Error) if the userDashboardWidget couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-dashboard-widgets")
    public ResponseEntity<UserDashboardWidget> updateUserDashboardWidget(@Valid @RequestBody UserDashboardWidget userDashboardWidget) throws URISyntaxException {
        log.debug("REST request to update UserDashboardWidget : {}", userDashboardWidget);
        if (userDashboardWidget.getId() == null) {
            return createUserDashboardWidget(userDashboardWidget);
        }
        UserDashboardWidget result = userDashboardWidgetService.save(userDashboardWidget);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-dashboard-widgets : get all the userDashboardWidgets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userDashboardWidgets in body
     */
    @GetMapping("/user-dashboard-widgets")
    public ResponseEntity<List<UserDashboardWidget>> getAllUserDashboardWidgets(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserDashboardWidgets");
        Page<UserDashboardWidget> page = userDashboardWidgetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-dashboard-widgets/:id : get the "id" userDashboardWidget.
     *
     * @param id the id of the userDashboardWidget to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userDashboardWidget, or with status 404 (Not Found)
     */
    @GetMapping("/user-dashboard-widgets/{id}")
    public ResponseEntity<UserDashboardWidget> getUserDashboardWidget(@PathVariable Long id) {
        log.debug("REST request to get UserDashboardWidget : {}", id);
        UserDashboardWidget userDashboardWidget = userDashboardWidgetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userDashboardWidget));
    }

    /**
     * DELETE  /user-dashboard-widgets/:id : delete the "id" userDashboardWidget.
     *
     * @param id the id of the userDashboardWidget to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-dashboard-widgets/{id}")
    public ResponseEntity<Void> deleteUserDashboardWidget(@PathVariable Long id) {
        log.debug("REST request to delete UserDashboardWidget : {}", id);
        userDashboardWidgetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-dashboard-widgets?query=:query : search for the userDashboardWidget corresponding
     * to the query.
     *
     * @param query    the query of the userDashboardWidget search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-dashboard-widgets")
    public ResponseEntity<List<UserDashboardWidget>> searchUserDashboardWidgets(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of UserDashboardWidgets for query {}", query);
        Page<UserDashboardWidget> page = userDashboardWidgetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * @return
     */
    @GetMapping("/_index/user-dashboard-widgets")
    public ResponseEntity<Void> indexUserDashboardWidgets(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on user-dashboard-widgets");
        long resultCount = userDashboardWidgetRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            userDashboardWidgetService.doIndex(i, pageSize, fromDate, toDate);
        }
        userDashboardWidgetSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("artha","Elastic index is completed", null)).build();
    }
}
