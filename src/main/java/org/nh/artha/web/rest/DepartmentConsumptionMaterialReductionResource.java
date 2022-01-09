package org.nh.artha.web.rest;

import org.nh.artha.domain.DepartmentConsumptionMaterialReduction;
import org.nh.artha.service.DepartmentConsumptionMaterialReductionService;
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
 * REST controller for managing {@link org.nh.artha.domain.DepartmentConsumptionMaterialReduction}.
 */
@RestController
@RequestMapping("/api")
public class DepartmentConsumptionMaterialReductionResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentConsumptionMaterialReductionResource.class);

    private static final String ENTITY_NAME = "departmentConsumptionMaterialReduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentConsumptionMaterialReductionService departmentConsumptionMaterialReductionService;

    public DepartmentConsumptionMaterialReductionResource(DepartmentConsumptionMaterialReductionService departmentConsumptionMaterialReductionService) {
        this.departmentConsumptionMaterialReductionService = departmentConsumptionMaterialReductionService;
    }

    /**
     * {@code POST  /department-consumption-material-reductions} : Create a new departmentConsumptionMaterialReduction.
     *
     * @param departmentConsumptionMaterialReduction the departmentConsumptionMaterialReduction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentConsumptionMaterialReduction, or with status {@code 400 (Bad Request)} if the departmentConsumptionMaterialReduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/department-consumption-material-reductions")
    public ResponseEntity<DepartmentConsumptionMaterialReduction> createDepartmentConsumptionMaterialReduction(@RequestBody DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction) throws URISyntaxException {
        log.debug("REST request to save DepartmentConsumptionMaterialReduction : {}", departmentConsumptionMaterialReduction);
        if (departmentConsumptionMaterialReduction.getId() != null) {
            throw new BadRequestAlertException("A new departmentConsumptionMaterialReduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentConsumptionMaterialReduction result = departmentConsumptionMaterialReductionService.save(departmentConsumptionMaterialReduction);
        return ResponseEntity.created(new URI("/api/department-consumption-material-reductions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /department-consumption-material-reductions} : Updates an existing departmentConsumptionMaterialReduction.
     *
     * @param departmentConsumptionMaterialReduction the departmentConsumptionMaterialReduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentConsumptionMaterialReduction,
     * or with status {@code 400 (Bad Request)} if the departmentConsumptionMaterialReduction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentConsumptionMaterialReduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department-consumption-material-reductions")
    public ResponseEntity<DepartmentConsumptionMaterialReduction> updateDepartmentConsumptionMaterialReduction(@RequestBody DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction) throws URISyntaxException {
        log.debug("REST request to update DepartmentConsumptionMaterialReduction : {}", departmentConsumptionMaterialReduction);
        if (departmentConsumptionMaterialReduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartmentConsumptionMaterialReduction result = departmentConsumptionMaterialReductionService.save(departmentConsumptionMaterialReduction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentConsumptionMaterialReduction.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /department-consumption-material-reductions} : get all the departmentConsumptionMaterialReductions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departmentConsumptionMaterialReductions in body.
     */
    @GetMapping("/department-consumption-material-reductions")
    public ResponseEntity<List<DepartmentConsumptionMaterialReduction>> getAllDepartmentConsumptionMaterialReductions(Pageable pageable) {
        log.debug("REST request to get a page of DepartmentConsumptionMaterialReductions");
        Page<DepartmentConsumptionMaterialReduction> page = departmentConsumptionMaterialReductionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /department-consumption-material-reductions/:id} : get the "id" departmentConsumptionMaterialReduction.
     *
     * @param id the id of the departmentConsumptionMaterialReduction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentConsumptionMaterialReduction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/department-consumption-material-reductions/{id}")
    public ResponseEntity<DepartmentConsumptionMaterialReduction> getDepartmentConsumptionMaterialReduction(@PathVariable Long id) {
        log.debug("REST request to get DepartmentConsumptionMaterialReduction : {}", id);
        Optional<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReduction = departmentConsumptionMaterialReductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentConsumptionMaterialReduction);
    }

    /**
     * {@code DELETE  /department-consumption-material-reductions/:id} : delete the "id" departmentConsumptionMaterialReduction.
     *
     * @param id the id of the departmentConsumptionMaterialReduction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/department-consumption-material-reductions/{id}")
    public ResponseEntity<Void> deleteDepartmentConsumptionMaterialReduction(@PathVariable Long id) {
        log.debug("REST request to delete DepartmentConsumptionMaterialReduction : {}", id);
        departmentConsumptionMaterialReductionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/department-consumption-material-reductions?query=:query} : search for the departmentConsumptionMaterialReduction corresponding
     * to the query.
     *
     * @param query the query of the departmentConsumptionMaterialReduction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/department-consumption-material-reductions")
    public ResponseEntity<List<DepartmentConsumptionMaterialReduction>> searchDepartmentConsumptionMaterialReductions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepartmentConsumptionMaterialReductions for query {}", query);
        Page<DepartmentConsumptionMaterialReduction> page = departmentConsumptionMaterialReductionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
