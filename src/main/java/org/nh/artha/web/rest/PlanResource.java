package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Plan;
import org.nh.artha.repository.PlanRepository;
import org.nh.artha.repository.search.PlanSearchRepository;
import org.nh.artha.service.PlanService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.Plan}.
 */
@RestController
@RequestMapping("/api")
public class PlanResource {

    private final Logger log = LoggerFactory.getLogger(PlanResource.class);

    private static final String ENTITY_NAME = "plan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanService planService;

    private final PlanRepository planRepository;

    private final PlanSearchRepository planSearchRepository;

    private final ApplicationProperties applicationProperties;

    public PlanResource(PlanService planService,PlanRepository planRepository,PlanSearchRepository planSearchRepository,ApplicationProperties applicationProperties) {
        this.planRepository=planRepository;
        this.planSearchRepository=planSearchRepository;
        this.planService = planService;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /plans} : Create a new plan.
     *
     * @param plan the plan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plan, or with status {@code 400 (Bad Request)} if the plan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plans")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) throws URISyntaxException {
        log.debug("REST request to save Plan : {}", plan);
        if (plan.getId() != null) {
            throw new BadRequestAlertException("A new plan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Plan result = planService.save(plan);
        return ResponseEntity.created(new URI("/api/plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plans} : Updates an existing plan.
     *
     * @param plan the plan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plan,
     * or with status {@code 400 (Bad Request)} if the plan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plans")
    public ResponseEntity<Plan> updatePlan(@RequestBody Plan plan) throws URISyntaxException {
        log.debug("REST request to update Plan : {}", plan);
        if (plan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Plan result = planService.save(plan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plan.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plans} : get all the plans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plans in body.
     */
    @GetMapping("/plans")
    public ResponseEntity<List<Plan>> getAllPlans(Pageable pageable) {
        log.debug("REST request to get a page of Plans");
        Page<Plan> page = planService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plans/:id} : get the "id" plan.
     *
     * @param id the id of the plan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plans/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable Long id) {
        log.debug("REST request to get Plan : {}", id);
        Optional<Plan> plan = planService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plan);
    }

    /**
     * {@code DELETE  /plans/:id} : delete the "id" plan.
     *
     * @param id the id of the plan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plans/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        log.debug("REST request to delete Plan : {}", id);
        planService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/plans?query=:query} : search for the plan corresponding
     * to the query.
     *
     * @param query the query of the plan search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/plans")
    public ResponseEntity<List<Plan>> searchPlans(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Plans for query {}", query);
        Page<Plan> page = planService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @PostMapping("/_import/plan-master")
    public ResponseEntity<List<Plan>> importPlan(@RequestBody MultipartFile file) throws Exception {
        log.debug("REST request to import packageMaster ");
        List<Plan> result = planService.uploadPlanData(file);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/_index/plan")
    public ResponseEntity<Void> indexPlan(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on ValueSets");
        long resultCount = planRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            planService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "ValueSet Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }
}
