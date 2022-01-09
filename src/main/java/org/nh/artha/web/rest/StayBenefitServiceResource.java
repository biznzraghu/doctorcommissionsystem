package org.nh.artha.web.rest;

import org.nh.artha.domain.StayBenefitService;
import org.nh.artha.service.StayBenefitServiceService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.StayBenefitService}.
 */
@RestController
@RequestMapping("/api")
public class StayBenefitServiceResource {

    private final Logger log = LoggerFactory.getLogger(StayBenefitServiceResource.class);

    private static final String ENTITY_NAME = "stayBenefitService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StayBenefitServiceService stayBenefitServiceService;

    public StayBenefitServiceResource(StayBenefitServiceService stayBenefitServiceService) {
        this.stayBenefitServiceService = stayBenefitServiceService;
    }

    /**
     * {@code POST  /stay-benefit-services} : Create a new stayBenefitService.
     *
     * @param stayBenefitService the stayBenefitService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stayBenefitService, or with status {@code 400 (Bad Request)} if the stayBenefitService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stay-benefit-services")
    public ResponseEntity<StayBenefitService> createStayBenefitService(@Valid @RequestBody StayBenefitService stayBenefitService) throws URISyntaxException {
        log.debug("REST request to save StayBenefitService : {}", stayBenefitService);
        if (stayBenefitService.getId() != null) {
            throw new BadRequestAlertException("A new stayBenefitService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StayBenefitService result = stayBenefitServiceService.save(stayBenefitService);
        return ResponseEntity.created(new URI("/api/stay-benefit-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stay-benefit-services} : Updates an existing stayBenefitService.
     *
     * @param stayBenefitService the stayBenefitService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stayBenefitService,
     * or with status {@code 400 (Bad Request)} if the stayBenefitService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stayBenefitService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stay-benefit-services")
    public ResponseEntity<StayBenefitService> updateStayBenefitService(@Valid @RequestBody StayBenefitService stayBenefitService) throws URISyntaxException {
        log.debug("REST request to update StayBenefitService : {}", stayBenefitService);
        if (stayBenefitService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StayBenefitService result = stayBenefitServiceService.save(stayBenefitService);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stayBenefitService.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stay-benefit-services} : get all the stayBenefitServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stayBenefitServices in body.
     */
    @GetMapping("/stay-benefit-services")
    public ResponseEntity<List<StayBenefitService>> getAllStayBenefitServices(Pageable pageable) {
        log.debug("REST request to get a page of StayBenefitServices");
        Page<StayBenefitService> page = stayBenefitServiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stay-benefit-services/:id} : get the "id" stayBenefitService.
     *
     * @param id the id of the stayBenefitService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stayBenefitService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stay-benefit-services/{id}")
    public ResponseEntity<StayBenefitService> getStayBenefitService(@PathVariable Long id) {
        log.debug("REST request to get StayBenefitService : {}", id);
        Optional<StayBenefitService> stayBenefitService = stayBenefitServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stayBenefitService);
    }

    /**
     * {@code DELETE  /stay-benefit-services/:id} : delete the "id" stayBenefitService.
     *
     * @param id the id of the stayBenefitService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stay-benefit-services/{id}")
    public ResponseEntity<Void> deleteStayBenefitService(@PathVariable Long id) {
        log.debug("REST request to delete StayBenefitService : {}", id);
        stayBenefitServiceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/stay-benefit-services?query=:query} : search for the stayBenefitService corresponding
     * to the query.
     *
     * @param query the query of the stayBenefitService search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/stay-benefit-services")
    public ResponseEntity<List<StayBenefitService>> searchStayBenefitServices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StayBenefitServices for query {}", query);
        Page<StayBenefitService> page = stayBenefitServiceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
