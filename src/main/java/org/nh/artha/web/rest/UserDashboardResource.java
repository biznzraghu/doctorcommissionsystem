package org.nh.artha.web.rest;

//import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserDashboard;
import org.nh.artha.repository.UserDashboardRepository;
import org.nh.artha.repository.search.UserDashboardSearchRepository;
import org.nh.artha.service.UserDashboardService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserDashboard.
 */
@RestController
@RequestMapping("/api")
public class UserDashboardResource {

    private final Logger log = LoggerFactory.getLogger(UserDashboardResource.class);

    private static final String ENTITY_NAME = "userDashboard";

    private final UserDashboardService userDashboardService;
    private final UserDashboardRepository userDashboardRepository;
    private final UserDashboardSearchRepository userDashboardSearchRepository;
    private final ApplicationProperties applicationProperties;

    public UserDashboardResource(UserDashboardService userDashboardService, UserDashboardRepository userDashboardRepository, UserDashboardSearchRepository userDashboardSearchRepository, ApplicationProperties applicationProperties) {
        this.userDashboardService = userDashboardService;
        this.userDashboardRepository = userDashboardRepository;
        this.userDashboardSearchRepository = userDashboardSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /user-dashboards : Create a new userDashboard.
     *
     * @param userDashboard the userDashboard to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userDashboard, or with status 400 (Bad Request) if the userDashboard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-dashboards")
//    @Timed
    public ResponseEntity<UserDashboard> createUserDashboard(@Valid @RequestBody UserDashboard userDashboard) throws URISyntaxException {
        log.debug("REST request to save UserDashboard : {}", userDashboard);
        if (userDashboard.getId() != null) {
            throw new BadRequestAlertException("A new userDashboard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDashboard result = userDashboardService.save(userDashboard);
        HttpHeaders headers =HeaderUtil.createEntityUpdateAlert("artha", true, ENTITY_NAME, "/api/_search/user-dashboards");
        return ResponseEntity.created(new URI("/api/user-dashboards/" + result.getId()))
            .headers(headers)
            .body(result);
    }

    /**
     * @param userDashboards
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/user-dashboards/all")
//    @Timed
    public ResponseEntity<List<UserDashboard>> createUserDashboards(@Valid @RequestBody List<UserDashboard> userDashboards) throws URISyntaxException {
        log.debug("REST request to save UserDashboard : {}", userDashboards);
        if (userDashboards.size() == 0) {
            throw new BadRequestAlertException("Nothing to update", ENTITY_NAME, "idexists");
        }
        List<UserDashboard> result = userDashboardService.saveAll(userDashboards);
        return ResponseEntity.created(new URI("/api/user-dashboards/all/"))
            .headers(HeaderUtil.createEntityUpdateAlert("artha", true, ENTITY_NAME, "/api/_search/user-dashboards"))
            .body(result);
    }

    /**
     * PUT  /user-dashboards : Updates an existing userDashboard.
     *
     * @param userDashboard the userDashboard to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userDashboard,
     * or with status 400 (Bad Request) if the userDashboard is not valid,
     * or with status 500 (Internal Server Error) if the userDashboard couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-dashboards")
//    @Timed
    public ResponseEntity<UserDashboard> updateUserDashboard(@Valid @RequestBody UserDashboard userDashboard) throws URISyntaxException {
        log.debug("REST request to update UserDashboard : {}", userDashboard);
        if (userDashboard.getId() == null) {
            return createUserDashboard(userDashboard);
        }
        UserDashboard result = userDashboardService.save(userDashboard);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("artha", true, ENTITY_NAME, "/api/_search/user-dashboards"))
            .body(result);
    }

    /**
     * GET  /user-dashboards : get all the userDashboards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userDashboards in body
     */
    @GetMapping("/user-dashboards")
//    @Timed
    public ResponseEntity<List<UserDashboard>> getAllUserDashboards(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserDashboards");
        Page<UserDashboard> page = userDashboardService.findAll(pageable);
        HttpHeaders headers =HeaderUtil.createEntityUpdateAlert("artha", true, ENTITY_NAME, "/api/_search/user-dashboards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-dashboards/:id : get the "id" userDashboard.
     *
     * @param id the id of the userDashboard to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userDashboard, or with status 404 (Not Found)
     */
    @GetMapping("/user-dashboards/{id}")
//    @Timed
    public ResponseEntity<UserDashboard> getUserDashboard(@PathVariable Long id) {
        log.debug("REST request to get UserDashboard : {}", id);
        UserDashboard userDashboard = userDashboardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userDashboard));
    }

    /**
     * DELETE  /user-dashboards/:id : delete the "id" userDashboard.
     *
     * @param id the id of the userDashboard to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-dashboards/{id}")
//    @Timed
    public ResponseEntity<Void> deleteUserDashboard(@PathVariable Long id) {
        log.debug("REST request to delete UserDashboard : {}", id);
        userDashboardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("artha", true, ENTITY_NAME, "/api/_search/user-dashboards")).build();
    }

    /**
     * SEARCH  /_search/user-dashboards?query=:query : search for the userDashboard corresponding
     * to the query.
     *
     * @param query    the query of the userDashboard search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-dashboards")
//    @Timed
    public ResponseEntity<List<UserDashboard>> searchUserDashboards(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of UserDashboards for query {}", query);
        Page<UserDashboard> page = userDashboardService.search(query, pageable);
        HttpHeaders headers =HeaderUtil.createEntityUpdateAlert("artha", true, ENTITY_NAME, "/api/_search/user-dashboards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping("/_index/user-dashboards")
//    @Timed
    //@PreAuthorize(PrivilegeConstant.SUPER_USER_PRIVILEGE_EXPRESSION)
    public ResponseEntity<Void> indexUserDashboards(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on user-dashboards");
        long resultCount = userDashboardRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            userDashboardService.doIndex(i, pageSize, fromDate, toDate);
        }
        userDashboardSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("artha", true, ENTITY_NAME, "/api/_search/user-dashboards")).build();
    }

}
