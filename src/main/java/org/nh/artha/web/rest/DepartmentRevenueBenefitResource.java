package org.nh.artha.web.rest;

import org.nh.artha.domain.DepartmentRevenueBenefit;
import org.nh.artha.service.DepartmentRevenueBenefitService;
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
 * REST controller for managing {@link org.nh.artha.domain.DepartmentRevenueBenefit}.
 */
@RestController
@RequestMapping("/api")
public class DepartmentRevenueBenefitResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentRevenueBenefitResource.class);

    private static final String ENTITY_NAME = "departmentRevenueBenefit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentRevenueBenefitService departmentRevenueBenefitService;

    public DepartmentRevenueBenefitResource(DepartmentRevenueBenefitService departmentRevenueBenefitService) {
        this.departmentRevenueBenefitService = departmentRevenueBenefitService;
    }

    /**
     * {@code POST  /department-revenue-benefits} : Create a new departmentRevenueBenefit.
     *
     * @param departmentRevenueBenefit the departmentRevenueBenefit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentRevenueBenefit, or with status {@code 400 (Bad Request)} if the departmentRevenueBenefit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/department-revenue-benefits")
    public ResponseEntity<DepartmentRevenueBenefit> createDepartmentRevenueBenefit(@Valid @RequestBody DepartmentRevenueBenefit departmentRevenueBenefit) throws URISyntaxException {
        log.debug("REST request to save DepartmentRevenueBenefit : {}", departmentRevenueBenefit);
        if (departmentRevenueBenefit.getId() != null) {
            throw new BadRequestAlertException("A new departmentRevenueBenefit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentRevenueBenefit result = departmentRevenueBenefitService.save(departmentRevenueBenefit);
        return ResponseEntity.created(new URI("/api/department-revenue-benefits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /department-revenue-benefits} : Updates an existing departmentRevenueBenefit.
     *
     * @param departmentRevenueBenefit the departmentRevenueBenefit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentRevenueBenefit,
     * or with status {@code 400 (Bad Request)} if the departmentRevenueBenefit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentRevenueBenefit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department-revenue-benefits")
    public ResponseEntity<DepartmentRevenueBenefit> updateDepartmentRevenueBenefit(@Valid @RequestBody DepartmentRevenueBenefit departmentRevenueBenefit) throws URISyntaxException {
        log.debug("REST request to update DepartmentRevenueBenefit : {}", departmentRevenueBenefit);
        if (departmentRevenueBenefit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepartmentRevenueBenefit result = departmentRevenueBenefitService.save(departmentRevenueBenefit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentRevenueBenefit.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /department-revenue-benefits} : get all the departmentRevenueBenefits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departmentRevenueBenefits in body.
     */
    @GetMapping("/department-revenue-benefits")
    public ResponseEntity<List<DepartmentRevenueBenefit>> getAllDepartmentRevenueBenefits(Pageable pageable) {
        log.debug("REST request to get a page of DepartmentRevenueBenefits");
        Page<DepartmentRevenueBenefit> page = departmentRevenueBenefitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /department-revenue-benefits/:id} : get the "id" departmentRevenueBenefit.
     *
     * @param id the id of the departmentRevenueBenefit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentRevenueBenefit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/department-revenue-benefits/{id}")
    public ResponseEntity<DepartmentRevenueBenefit> getDepartmentRevenueBenefit(@PathVariable Long id) {
        log.debug("REST request to get DepartmentRevenueBenefit : {}", id);
        Optional<DepartmentRevenueBenefit> departmentRevenueBenefit = departmentRevenueBenefitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentRevenueBenefit);
    }

    /**
     * {@code DELETE  /department-revenue-benefits/:id} : delete the "id" departmentRevenueBenefit.
     *
     * @param id the id of the departmentRevenueBenefit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/department-revenue-benefits/{id}")
    public ResponseEntity<Void> deleteDepartmentRevenueBenefit(@PathVariable Long id) {
        log.debug("REST request to delete DepartmentRevenueBenefit : {}", id);
        departmentRevenueBenefitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/department-revenue-benefits?query=:query} : search for the departmentRevenueBenefit corresponding
     * to the query.
     *
     * @param query the query of the departmentRevenueBenefit search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/department-revenue-benefits")
    public ResponseEntity<List<DepartmentRevenueBenefit>> searchDepartmentRevenueBenefits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepartmentRevenueBenefits for query {}", query);
        Page<DepartmentRevenueBenefit> page = departmentRevenueBenefitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
