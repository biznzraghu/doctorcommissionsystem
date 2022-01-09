package org.nh.artha.web.rest;

import com.mchange.util.IteratorUtils;
import liquibase.pro.packaged.R;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Department;
import org.nh.artha.domain.LengthOfStayBenefit;
import org.nh.artha.domain.VariablePayout;
import org.nh.artha.repository.DepartmentRepository;
import org.nh.artha.repository.VariablePayoutRepository;
import org.nh.artha.repository.search.VariablePayoutSearchRepository;
import org.nh.artha.service.VariablePayoutService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.VariablePayout}.
 */
@RestController
@RequestMapping("/api")
public class VariablePayoutResource {

    private final Logger log = LoggerFactory.getLogger(VariablePayoutResource.class);

    private static final String ENTITY_NAME = "variablePayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VariablePayoutService variablePayoutService;

    private final VariablePayoutRepository variablePayoutRepository;

    private final VariablePayoutSearchRepository variablePayoutSearchRepository;

    private final ApplicationProperties applicationProperties;

    public VariablePayoutResource(VariablePayoutService variablePayoutService,VariablePayoutRepository variablePayoutRepository,ApplicationProperties applicationProperties,
                                  VariablePayoutSearchRepository variablePayoutSearchRepository) {
        this.variablePayoutService = variablePayoutService;
        this.variablePayoutRepository=variablePayoutRepository;
        this.applicationProperties =applicationProperties;
        this.variablePayoutSearchRepository=variablePayoutSearchRepository;
    }

    /**
     * {@code POST  /variable-payouts} : Create a new variablePayout.
     *
     * @param variablePayout the variablePayout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new variablePayout, or with status {@code 400 (Bad Request)} if the variablePayout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/variable-payouts")
    public ResponseEntity<VariablePayout> createVariablePayout(@Valid @RequestBody VariablePayout variablePayout) throws URISyntaxException {
        log.debug("REST request to save VariablePayout : {}", variablePayout);
        if (variablePayout.getId() != null) {
            throw new BadRequestAlertException("A new variablePayout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VariablePayout result = variablePayoutService.save(variablePayout);
        return ResponseEntity.created(new URI("/api/variable-payouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /variable-payouts} : Updates an existing variablePayout.
     *
     * @param variablePayout the variablePayout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variablePayout,
     * or with status {@code 400 (Bad Request)} if the variablePayout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the variablePayout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/variable-payouts")
    public ResponseEntity<VariablePayout> updateVariablePayout(@Valid @RequestBody VariablePayout variablePayout) throws URISyntaxException {
        log.debug("REST request to update VariablePayout : {}", variablePayout);
        VariablePayout result = variablePayoutService.update(variablePayout);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, variablePayout.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /variable-payouts} : get all the variablePayouts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of variablePayouts in body.
     */
    @GetMapping("/variable-payouts")
    public ResponseEntity<List<VariablePayout>> getAllVariablePayouts(Pageable pageable) {
        log.debug("REST request to get a page of VariablePayouts");
        Page<VariablePayout> page = variablePayoutService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /variable-payouts/:id} : get the "id" variablePayout.
     *
     * @param id the id of the variablePayout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the variablePayout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/variable-payouts/{id}")
    public ResponseEntity<VariablePayout> getVariablePayout(@PathVariable Long id) {
        log.debug("REST request to get VariablePayout : {}", id);
        Optional<VariablePayout> variablePayout = variablePayoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(variablePayout);
    }

    /**
     * {@code DELETE  /variable-payouts/:id} : delete the "id" variablePayout.
     *
     * @param id the id of the variablePayout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/variable-payouts/{id}")
    public ResponseEntity<Void> deleteVariablePayout(@PathVariable Long id) {
        log.debug("REST request to delete VariablePayout : {}", id);
        variablePayoutService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/variable-payouts?query=:query} : search for the variablePayout corresponding
     * to the query.
     *
     * @param query the query of the variablePayout search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/variable-payouts")
    public ResponseEntity<List<VariablePayout>> searchVariablePayouts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of VariablePayouts for query {}", query);
        Page<VariablePayout> page = variablePayoutService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/_index/variable-payout")
    public ResponseEntity<Void> indexVariablePayout(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Variable Payout");
        long resultCount = variablePayoutRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            variablePayoutService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("artha","ElasticSearch index completed sucessfully","")).build();
    }
    @GetMapping("/payouts/latest-variable-payouts")
    public ResponseEntity<List<VariablePayout>> getPayouts(@RequestParam String query, Pageable pageable) {
        List<VariablePayout> payouts = variablePayoutService.getPayouts(query, pageable);
        PageImpl<VariablePayout> variablePayouts = new PageImpl<>(payouts, pageable, payouts.size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),variablePayouts);
        return ResponseEntity.ok().headers(headers).body(variablePayouts.getContent());
    }

    @DeleteMapping("variablePayout/deletebyQuery")
    public void  deleteAllRecord(@RequestParam(value = "query") String query){
        Iterable<VariablePayout> search = variablePayoutSearchRepository.search(queryStringQuery(query));
        List<VariablePayout> collect = StreamSupport.stream(search.spliterator(), false)
            .collect(Collectors.toList());
        for(int i=0;i<collect.size();i++){
            variablePayoutService.delete(collect.get(i).getId());
        }
    }

    @GetMapping("/variable-payouts/distinct-version")
    public ResponseEntity<List<Integer>> getDistinctVersion(@RequestParam(value = "query") String query) throws IOException {
        List<Integer> uniqueVersionList=variablePayoutService.getDistinctVersion(query);
        return ResponseEntity.ok().body(uniqueVersionList);
    }

    @GetMapping("/variable-payouts/export")
    public ResponseEntity<Map<String,String>> exportVariablePayout(@RequestParam(value = "query") String query,Pageable pageable) throws IOException {
        Map<String,String> fileAndPathLocationMap=variablePayoutService.exportVariablePayout(query,pageable);
        return ResponseEntity.ok().body(fileAndPathLocationMap);
    }

}
