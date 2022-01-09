package org.nh.artha.web.rest;

import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.DepartmentPayout;
import org.nh.artha.domain.PayoutAdjustment;
import org.nh.artha.repository.DepartmentPayoutRepository;
import org.nh.artha.repository.search.DepartmentPayoutSearchRepository;
import org.nh.artha.service.DepartmentPayoutService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.DepartmentPayout}.
 */
@RestController
@RequestMapping("/api")
public class DepartmentPayoutResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentPayoutResource.class);

    private static final String ENTITY_NAME = "departmentPayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentPayoutService departmentPayoutService;

    private final DepartmentPayoutRepository departmentPayoutRepository;

    private final ApplicationProperties applicationProperties;

    private final ElasticsearchOperations elasticsearchTemplate;

    private final DepartmentPayoutSearchRepository departmentPayoutSearchRepository;


    public DepartmentPayoutResource(DepartmentPayoutService departmentPayoutService,DepartmentPayoutRepository departmentPayoutRepository,
                                    ApplicationProperties applicationProperties,ElasticsearchOperations elasticsearchTemplate,DepartmentPayoutSearchRepository departmentPayoutSearchRepository) {
        this.departmentPayoutService = departmentPayoutService;
        this.departmentPayoutRepository=departmentPayoutRepository;
        this.applicationProperties = applicationProperties;
        this.elasticsearchTemplate=elasticsearchTemplate;
        this.departmentPayoutSearchRepository=departmentPayoutSearchRepository;
    }

    /**
     * {@code POST  /department-payouts} : Create a new departmentPayout.
     *
     * @param departmentPayout the departmentPayout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentPayout, or with status {@code 400 (Bad Request)} if the departmentPayout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/department-payouts")
    public ResponseEntity<DepartmentPayout> createDepartmentPayout(@RequestBody DepartmentPayout departmentPayout) throws URISyntaxException {
        log.debug("REST request to save DepartmentPayout : {}", departmentPayout);
        if (departmentPayout.getId() != null) {
            throw new BadRequestAlertException("A new departmentPayout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentPayout result = departmentPayoutService.save(departmentPayout);
        return ResponseEntity.created(new URI("/api/department-payouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /department-payouts} : Updates an existing departmentPayout.
     *
     * @param departmentPayout the departmentPayout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentPayout,
     * or with status {@code 400 (Bad Request)} if the departmentPayout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentPayout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department-payouts")
    public ResponseEntity<DepartmentPayout> updateDepartmentPayout(@RequestBody DepartmentPayout departmentPayout) throws URISyntaxException {
        log.debug("REST request to update DepartmentPayout : {}", departmentPayout);
        DepartmentPayout result = departmentPayoutService.update(departmentPayout);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentPayout.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /department-payouts} : get all the departmentPayouts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departmentPayouts in body.
     */
    @GetMapping("/department-payouts")
    public ResponseEntity<List<DepartmentPayout>> getAllDepartmentPayouts(Pageable pageable) {
        log.debug("REST request to get a page of DepartmentPayouts");
        Page<DepartmentPayout> page = departmentPayoutService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /department-payouts/:id} : get the "id" departmentPayout.
     *
     * @param id the id of the departmentPayout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentPayout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/department-payouts/{id}")
    public ResponseEntity<DepartmentPayout> getDepartmentPayout(@PathVariable Long id) {
        log.debug("REST request to get DepartmentPayout : {}", id);
        DepartmentPayout departmentPayout = departmentPayoutService.findOne(id);
        return new ResponseEntity<>(departmentPayout, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /department-payouts/:id} : delete the "id" departmentPayout.
     *
     * @param id the id of the departmentPayout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/department-payouts/{id}")
    public ResponseEntity<Void> deleteDepartmentPayout(@PathVariable Long id) {
        log.debug("REST request to delete DepartmentPayout : {}", id);
        departmentPayoutService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/department-payouts?query=:query} : search for the departmentPayout corresponding
     * to the query.
     *
     * @param query the query of the departmentPayout search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/department-payouts")
    public ResponseEntity<List<DepartmentPayout>> searchDepartmentPayouts(@RequestParam String query, Pageable pageable) {
        String finalQuery=query.concat(" AND active:true");
        log.debug("REST request to search for a page of DepartmentPayouts for query {}", finalQuery);
        Page<DepartmentPayout> page = departmentPayoutService.search(finalQuery, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/department-payout")
    public ResponseEntity<Void> doIndex(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate){
        log.debug("REST request to do elastic index on DoctorPayout");
        long resultCount = departmentPayoutRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            departmentPayoutService.doIndex(i, pageSize,fromDate,toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("ARTHA", "Elastic index is completed",fromDate.toString()+" , "+toDate.toString())).build();
    }
    @GetMapping("/payouts/latest-department-payouts")
    public ResponseEntity<List<DepartmentPayout>> getPayouts(@RequestParam String query, Pageable pageable) {
        String finalQuery=query.concat(" AND active:true");
        log.debug("REST request to search for a page of VariablePayouts for query {}", finalQuery);
        List<DepartmentPayout> payouts = departmentPayoutService.getDepartmentPayouts(finalQuery, pageable);
        PageImpl<DepartmentPayout> departmentPayouts = new PageImpl<>(payouts, pageable, payouts.size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),departmentPayouts);
        return ResponseEntity.ok().headers(headers).body(departmentPayouts.getContent());
    }

    @DeleteMapping("departmentpayout/deletebyQuery")
    public void  deleteAllRecord(@RequestParam(value = "query") String query){
        Iterable<DepartmentPayout> search = departmentPayoutService.search(query, PageRequest.of(0,99999));
        List<DepartmentPayout> collect = StreamSupport.stream(search.spliterator(), false)
            .collect(Collectors.toList());
        for(int i=0;i<collect.size();i++){
            departmentPayoutService.delete(collect.get(i).getId());
        }
    }

    @GetMapping("/export/department-payout-breakup")
    public Map<String, String> exportDepartmentPayoutBreakUp(@RequestParam("unitCode") String unitCode,@RequestParam("month")Integer month,@RequestParam("year") Integer year,@RequestParam("departmentId") Integer departmentId) throws IOException {
        return  departmentPayoutService.exportDepartmentPayoutBreakUp(unitCode,year,month,departmentId);
    }
    @GetMapping("/export/department-revenue-summary")
    public Map<String, String> exportDepartmentRevenueSummary(@RequestParam("unitCode") String unitCode,@RequestParam("month")Integer month,@RequestParam("year") Integer year,@RequestParam (required = false)Integer departmentId) throws Exception {
        return  departmentPayoutService.exportDepartmentRevenueSummary(unitCode,year,month,departmentId);
    }
    @GetMapping("/department-payouts/distinct-version")
    public ResponseEntity<List<Integer>> getDistinctVersion(@RequestParam(value = "query") String query) throws IOException {
        List<Integer> uniqueVersionList=departmentPayoutService.getDistinctVersion(query);
        return ResponseEntity.ok().body(uniqueVersionList);
    }
    @GetMapping("/_export/department-payout-list")
    public Map<String, String> exportDepartmentPayout(@RequestParam String query, Pageable pageable,Boolean latest) throws Exception {

        log.debug("REST request to export department payout list for query {}", query);
        return departmentPayoutService.exportDepartmentPayout(query, pageable,latest);
    }
}
