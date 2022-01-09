package org.nh.artha.web.rest;

import org.nh.artha.domain.HscConsumptionMaterialReduction;
import org.nh.artha.service.HscConsumptionMaterialReductionService;
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
 * REST controller for managing {@link org.nh.artha.domain.HscConsumptionMaterialReduction}.
 */
@RestController
@RequestMapping("/api")
public class HscConsumptionMaterialReductionResource {

    private final Logger log = LoggerFactory.getLogger(HscConsumptionMaterialReductionResource.class);

    private static final String ENTITY_NAME = "hscConsumptionMaterialReduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HscConsumptionMaterialReductionService hscConsumptionMaterialReductionService;

    public HscConsumptionMaterialReductionResource(HscConsumptionMaterialReductionService hscConsumptionMaterialReductionService) {
        this.hscConsumptionMaterialReductionService = hscConsumptionMaterialReductionService;
    }

    /**
     * {@code POST  /hsc-consumption-material-reductions} : Create a new hscConsumptionMaterialReduction.
     *
     * @param hscConsumptionMaterialReduction the hscConsumptionMaterialReduction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hscConsumptionMaterialReduction, or with status {@code 400 (Bad Request)} if the hscConsumptionMaterialReduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hsc-consumption-material-reductions")
    public ResponseEntity<HscConsumptionMaterialReduction> createHscConsumptionMaterialReduction(@RequestBody HscConsumptionMaterialReduction hscConsumptionMaterialReduction) throws URISyntaxException {
        log.debug("REST request to save HscConsumptionMaterialReduction : {}", hscConsumptionMaterialReduction);
        if (hscConsumptionMaterialReduction.getId() != null) {
            throw new BadRequestAlertException("A new hscConsumptionMaterialReduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HscConsumptionMaterialReduction result = hscConsumptionMaterialReductionService.save(hscConsumptionMaterialReduction);
        return ResponseEntity.created(new URI("/api/hsc-consumption-material-reductions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hsc-consumption-material-reductions} : Updates an existing hscConsumptionMaterialReduction.
     *
     * @param hscConsumptionMaterialReduction the hscConsumptionMaterialReduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hscConsumptionMaterialReduction,
     * or with status {@code 400 (Bad Request)} if the hscConsumptionMaterialReduction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hscConsumptionMaterialReduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hsc-consumption-material-reductions")
    public ResponseEntity<HscConsumptionMaterialReduction> updateHscConsumptionMaterialReduction(@RequestBody HscConsumptionMaterialReduction hscConsumptionMaterialReduction) throws URISyntaxException {
        log.debug("REST request to update HscConsumptionMaterialReduction : {}", hscConsumptionMaterialReduction);
        if (hscConsumptionMaterialReduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HscConsumptionMaterialReduction result = hscConsumptionMaterialReductionService.save(hscConsumptionMaterialReduction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hscConsumptionMaterialReduction.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hsc-consumption-material-reductions} : get all the hscConsumptionMaterialReductions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hscConsumptionMaterialReductions in body.
     */
    @GetMapping("/hsc-consumption-material-reductions")
    public ResponseEntity<List<HscConsumptionMaterialReduction>> getAllHscConsumptionMaterialReductions(Pageable pageable) {
        log.debug("REST request to get a page of HscConsumptionMaterialReductions");
        Page<HscConsumptionMaterialReduction> page = hscConsumptionMaterialReductionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hsc-consumption-material-reductions/:id} : get the "id" hscConsumptionMaterialReduction.
     *
     * @param id the id of the hscConsumptionMaterialReduction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hscConsumptionMaterialReduction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hsc-consumption-material-reductions/{id}")
    public ResponseEntity<HscConsumptionMaterialReduction> getHscConsumptionMaterialReduction(@PathVariable Long id) {
        log.debug("REST request to get HscConsumptionMaterialReduction : {}", id);
        Optional<HscConsumptionMaterialReduction> hscConsumptionMaterialReduction = hscConsumptionMaterialReductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hscConsumptionMaterialReduction);
    }

    /**
     * {@code DELETE  /hsc-consumption-material-reductions/:id} : delete the "id" hscConsumptionMaterialReduction.
     *
     * @param id the id of the hscConsumptionMaterialReduction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hsc-consumption-material-reductions/{id}")
    public ResponseEntity<Void> deleteHscConsumptionMaterialReduction(@PathVariable Long id) {
        log.debug("REST request to delete HscConsumptionMaterialReduction : {}", id);
        hscConsumptionMaterialReductionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/hsc-consumption-material-reductions?query=:query} : search for the hscConsumptionMaterialReduction corresponding
     * to the query.
     *
     * @param query the query of the hscConsumptionMaterialReduction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/hsc-consumption-material-reductions")
    public ResponseEntity<List<HscConsumptionMaterialReduction>> searchHscConsumptionMaterialReductions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HscConsumptionMaterialReductions for query {}", query);
        Page<HscConsumptionMaterialReduction> page = hscConsumptionMaterialReductionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
