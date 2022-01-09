package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.DepartmentRevenue;
import org.nh.artha.repository.DepartmentRevenueRepository;
import org.nh.artha.repository.search.DepartmentRevenueSearchRepository;
import org.nh.artha.service.DepartmentRevenueService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.DepartmentRevenue}.
 */
@RestController
@RequestMapping("/api")
public class DepartmentRevenueResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentRevenueResource.class);

    private static final String ENTITY_NAME = "departmentRevenue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentRevenueService departmentRevenueService;
    private final DepartmentRevenueRepository departmentRevenueRepository;
    private final DepartmentRevenueSearchRepository departmentRevenueSearchRepository;
    private final ApplicationProperties applicationProperties;

    public DepartmentRevenueResource(DepartmentRevenueService departmentRevenueService,DepartmentRevenueRepository departmentRevenueRepository,DepartmentRevenueSearchRepository departmentRevenueSearchRepository,ApplicationProperties applicationProperties) {
        this.departmentRevenueService = departmentRevenueService;
        this.departmentRevenueRepository=departmentRevenueRepository;
        this.departmentRevenueSearchRepository=departmentRevenueSearchRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /department-revenues} : Create a new departmentRevenue.
     *
     * @param departmentRevenue the departmentRevenue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentRevenue, or with status {@code 400 (Bad Request)} if the departmentRevenue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/department-revenues")
    public ResponseEntity<DepartmentRevenue> createDepartmentRevenue(@Valid @RequestBody DepartmentRevenue departmentRevenue) throws URISyntaxException {
        log.debug("REST request to save DepartmentRevenue : {}", departmentRevenue);
        if (departmentRevenue.getId() != null) {
            throw new BadRequestAlertException("A new departmentRevenue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentRevenue result = departmentRevenueService.save(departmentRevenue);
        return ResponseEntity.created(new URI("/api/department-revenues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /department-revenues} : Updates an existing departmentRevenue.
     *
     * @param departmentRevenue the departmentRevenue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentRevenue,
     * or with status {@code 400 (Bad Request)} if the departmentRevenue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentRevenue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department-revenues")
    public ResponseEntity<DepartmentRevenue> updateDepartmentRevenue(@Valid @RequestBody DepartmentRevenue departmentRevenue) throws URISyntaxException {
        log.debug("REST request to update DepartmentRevenue : {}", departmentRevenue);
        if (departmentRevenue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartmentRevenue result = departmentRevenueService.save(departmentRevenue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentRevenue.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /department-revenues} : get all the departmentRevenues.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departmentRevenues in body.
     */
    @GetMapping("/department-revenues")
    public List<DepartmentRevenue> getAllDepartmentRevenues() {
        log.debug("REST request to get all DepartmentRevenues");
        return departmentRevenueService.findAll();
    }

    /**
     * {@code GET  /department-revenues/:id} : get the "id" departmentRevenue.
     *
     * @param id the id of the departmentRevenue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentRevenue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/department-revenues/{id}")
    public ResponseEntity<DepartmentRevenue> getDepartmentRevenue(@PathVariable Long id) {
        log.debug("REST request to get DepartmentRevenue : {}", id);
        Optional<DepartmentRevenue> departmentRevenue = departmentRevenueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentRevenue);
    }

    /**
     * {@code DELETE  /department-revenues/:id} : delete the "id" departmentRevenue.
     *
     * @param id the id of the departmentRevenue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/department-revenues/{id}")
    public ResponseEntity<Void> deleteDepartmentRevenue(@PathVariable Long id) {
        log.debug("REST request to delete DepartmentRevenue : {}", id);
        departmentRevenueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/department-revenues?query=:query} : search for the departmentRevenue corresponding
     * to the query.
     *
     * @param query the query of the departmentRevenue search.
     * @return the result of the search.
     */
    @GetMapping("/_search/department-revenues")
    public List<DepartmentRevenue> searchDepartmentRevenues(@RequestParam String query) {
        log.debug("REST request to search DepartmentRevenues for query {}", query);
        return departmentRevenueService.search(query);
    }


    @GetMapping("/index/department-revenues")
    public ResponseEntity<Void> indexDepartmentRevenues(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on department-revenues");
        long resultCount = departmentRevenueRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            departmentRevenueService.doIndex(i, pageSize, fromDate, toDate);
        }
        departmentRevenueSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("DepartmentRevenues Index in Artha", "DepartmentRevenues Index completetd ",fromDate.toString()+" "+toDate.toString())).build();
    }

}
