package org.nh.artha.web.rest;

import org.nh.artha.domain.ApplicableConsultant;
import org.nh.artha.service.ApplicableConsultantService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.ApplicableConsultant}.
 */
@RestController
@RequestMapping("/api")
public class ApplicableConsultantResource {

    private final Logger log = LoggerFactory.getLogger(ApplicableConsultantResource.class);

    private static final String ENTITY_NAME = "applicableConsultant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicableConsultantService applicableConsultantService;

    public ApplicableConsultantResource(ApplicableConsultantService applicableConsultantService) {
        this.applicableConsultantService = applicableConsultantService;
    }

    /**
     * {@code POST  /applicable-consultants} : Create a new applicableConsultant.
     *
     * @param applicableConsultant the applicableConsultant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicableConsultant, or with status {@code 400 (Bad Request)} if the applicableConsultant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/applicable-consultants")
    public ResponseEntity<ApplicableConsultant> createApplicableConsultant(@RequestBody ApplicableConsultant applicableConsultant) throws URISyntaxException {
        log.debug("REST request to save ApplicableConsultant : {}", applicableConsultant);
        if (applicableConsultant.getId() != null) {
            throw new BadRequestAlertException("A new applicableConsultant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicableConsultant result = applicableConsultantService.save(applicableConsultant);
        return ResponseEntity.created(new URI("/api/applicable-consultants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /applicable-consultants} : Updates an existing applicableConsultant.
     *
     * @param applicableConsultant the applicableConsultant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicableConsultant,
     * or with status {@code 400 (Bad Request)} if the applicableConsultant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicableConsultant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/applicable-consultants")
    public ResponseEntity<ApplicableConsultant> updateApplicableConsultant(@RequestBody ApplicableConsultant applicableConsultant) throws URISyntaxException {
        log.debug("REST request to update ApplicableConsultant : {}", applicableConsultant);
        if (applicableConsultant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApplicableConsultant result = applicableConsultantService.save(applicableConsultant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicableConsultant.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /applicable-consultants} : get all the applicableConsultants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicableConsultants in body.
     */
    @GetMapping("/applicable-consultants")
    public ResponseEntity<List<ApplicableConsultant>> getAllApplicableConsultants(Pageable pageable) {
        log.debug("REST request to get a page of ApplicableConsultants");
        Page<ApplicableConsultant> page = applicableConsultantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /applicable-consultants/:id} : get the "id" applicableConsultant.
     *
     * @param id the id of the applicableConsultant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicableConsultant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/applicable-consultants/{id}")
    public ResponseEntity<ApplicableConsultant> getApplicableConsultant(@PathVariable Long id) {
        log.debug("REST request to get ApplicableConsultant : {}", id);
        Optional<ApplicableConsultant> applicableConsultant = applicableConsultantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicableConsultant);
    }

    /**
     * {@code DELETE  /applicable-consultants/:id} : delete the "id" applicableConsultant.
     *
     * @param id the id of the applicableConsultant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/applicable-consultants/{id}")
    public ResponseEntity<Void> deleteApplicableConsultant(@PathVariable Long id) {
        log.debug("REST request to delete ApplicableConsultant : {}", id);
        applicableConsultantService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/applicable-consultants?query=:query} : search for the applicableConsultant corresponding
     * to the query.
     *
     * @param query the query of the applicableConsultant search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/applicable-consultants")
    public ResponseEntity<List<ApplicableConsultant>> searchApplicableConsultants(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ApplicableConsultants for query {}", query);
        Page<ApplicableConsultant> page = applicableConsultantService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
