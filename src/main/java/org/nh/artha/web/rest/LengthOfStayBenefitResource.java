package org.nh.artha.web.rest;

import org.nh.artha.domain.LengthOfStayBenefit;
import org.nh.artha.service.LengthOfStayBenefitService;
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
 * REST controller for managing {@link org.nh.artha.domain.LengthOfStayBenefit}.
 */
@RestController
@RequestMapping("/api")
public class LengthOfStayBenefitResource {

    private final Logger log = LoggerFactory.getLogger(LengthOfStayBenefitResource.class);

    private static final String ENTITY_NAME = "lengthOfStayBenefit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LengthOfStayBenefitService lengthOfStayBenefitService;

    public LengthOfStayBenefitResource(LengthOfStayBenefitService lengthOfStayBenefitService) {
        this.lengthOfStayBenefitService = lengthOfStayBenefitService;
    }

    /**
     * {@code POST  /length-of-stay-benefits} : Create a new lengthOfStayBenefit.
     *
     * @param lengthOfStayBenefit the lengthOfStayBenefit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lengthOfStayBenefit, or with status {@code 400 (Bad Request)} if the lengthOfStayBenefit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/length-of-stay-benefits")
    public ResponseEntity<LengthOfStayBenefit> createLengthOfStayBenefit(@Valid @RequestBody LengthOfStayBenefit lengthOfStayBenefit) throws URISyntaxException {
        log.debug("REST request to save LengthOfStayBenefit : {}", lengthOfStayBenefit);
        if (lengthOfStayBenefit.getId() != null) {
            throw new BadRequestAlertException("A new lengthOfStayBenefit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LengthOfStayBenefit result = lengthOfStayBenefitService.save(lengthOfStayBenefit);
        return ResponseEntity.created(new URI("/api/length-of-stay-benefits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /length-of-stay-benefits} : Updates an existing lengthOfStayBenefit.
     *
     * @param lengthOfStayBenefit the lengthOfStayBenefit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lengthOfStayBenefit,
     * or with status {@code 400 (Bad Request)} if the lengthOfStayBenefit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lengthOfStayBenefit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/length-of-stay-benefits")
    public ResponseEntity<LengthOfStayBenefit> updateLengthOfStayBenefit(@Valid @RequestBody LengthOfStayBenefit lengthOfStayBenefit) throws URISyntaxException {
        log.debug("REST request to update LengthOfStayBenefit : {}", lengthOfStayBenefit);
        if (lengthOfStayBenefit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LengthOfStayBenefit result = lengthOfStayBenefitService.save(lengthOfStayBenefit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lengthOfStayBenefit.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /length-of-stay-benefits} : get all the lengthOfStayBenefits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lengthOfStayBenefits in body.
     */
    @GetMapping("/length-of-stay-benefits")
    public ResponseEntity<List<LengthOfStayBenefit>> getAllLengthOfStayBenefits(Pageable pageable) {
        log.debug("REST request to get a page of LengthOfStayBenefits");
        Page<LengthOfStayBenefit> page = lengthOfStayBenefitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /length-of-stay-benefits/:id} : get the "id" lengthOfStayBenefit.
     *
     * @param id the id of the lengthOfStayBenefit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lengthOfStayBenefit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/length-of-stay-benefits/{id}")
    public ResponseEntity<LengthOfStayBenefit> getLengthOfStayBenefit(@PathVariable Long id) {
        log.debug("REST request to get LengthOfStayBenefit : {}", id);
        Optional<LengthOfStayBenefit> lengthOfStayBenefit = lengthOfStayBenefitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lengthOfStayBenefit);
    }

    /**
     * {@code DELETE  /length-of-stay-benefits/:id} : delete the "id" lengthOfStayBenefit.
     *
     * @param id the id of the lengthOfStayBenefit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/length-of-stay-benefits/{id}")
    public ResponseEntity<Void> deleteLengthOfStayBenefit(@PathVariable Long id) {
        log.debug("REST request to delete LengthOfStayBenefit : {}", id);
        lengthOfStayBenefitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/length-of-stay-benefits?query=:query} : search for the lengthOfStayBenefit corresponding
     * to the query.
     *
     * @param query the query of the lengthOfStayBenefit search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/length-of-stay-benefits")
    public ResponseEntity<List<LengthOfStayBenefit>> searchLengthOfStayBenefits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LengthOfStayBenefits for query {}", query);
        Page<LengthOfStayBenefit> page = lengthOfStayBenefitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
