package org.nh.artha.web.rest;

import org.nh.artha.domain.PackageMaster;
import org.nh.artha.service.PackageMasterService;
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
import org.springframework.web.multipart.MultipartFile;
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
 * REST controller for managing {@link org.nh.artha.domain.PackageMaster}.
 */
@RestController
@RequestMapping("/api")
public class PackageMasterResource {

    private final Logger log = LoggerFactory.getLogger(PackageMasterResource.class);

    private static final String ENTITY_NAME = "packageMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackageMasterService packageMasterService;

    public PackageMasterResource(PackageMasterService packageMasterService) {
        this.packageMasterService = packageMasterService;
    }

    /**
     * {@code POST  /package-masters} : Create a new packageMaster.
     *
     * @param packageMaster the packageMaster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packageMaster, or with status {@code 400 (Bad Request)} if the packageMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/package-masters")
    public ResponseEntity<PackageMaster> createPackageMaster(@Valid @RequestBody PackageMaster packageMaster) throws URISyntaxException {
        log.debug("REST request to save PackageMaster : {}", packageMaster);
        if (packageMaster.getId() != null) {
            throw new BadRequestAlertException("A new packageMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PackageMaster result = packageMasterService.save(packageMaster);
        return ResponseEntity.created(new URI("/api/package-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /package-masters} : Updates an existing packageMaster.
     *
     * @param packageMaster the packageMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageMaster,
     * or with status {@code 400 (Bad Request)} if the packageMaster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packageMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/package-masters")
    public ResponseEntity<PackageMaster> updatePackageMaster(@Valid @RequestBody PackageMaster packageMaster) throws URISyntaxException {
        log.debug("REST request to update PackageMaster : {}", packageMaster);
        if (packageMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PackageMaster result = packageMasterService.save(packageMaster);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packageMaster.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /package-masters} : get all the packageMasters.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packageMasters in body.
     */
    @GetMapping("/package-masters")
    public ResponseEntity<List<PackageMaster>> getAllPackageMasters(Pageable pageable) {
        log.debug("REST request to get a page of PackageMasters");
        Page<PackageMaster> page = packageMasterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /package-masters/:id} : get the "id" packageMaster.
     *
     * @param id the id of the packageMaster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packageMaster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/package-masters/{id}")
    public ResponseEntity<PackageMaster> getPackageMaster(@PathVariable Long id) {
        log.debug("REST request to get PackageMaster : {}", id);
        Optional<PackageMaster> packageMaster = packageMasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(packageMaster);
    }

    /**
     * {@code DELETE  /package-masters/:id} : delete the "id" packageMaster.
     *
     * @param id the id of the packageMaster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/package-masters/{id}")
    public ResponseEntity<Void> deletePackageMaster(@PathVariable Long id) {
        log.debug("REST request to delete PackageMaster : {}", id);
        packageMasterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/package-masters?query=:query} : search for the packageMaster corresponding
     * to the query.
     *
     * @param query the query of the packageMaster search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/package-masters")
    public ResponseEntity<List<PackageMaster>> searchPackageMasters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PackageMasters for query {}", query);
        Page<PackageMaster> page = packageMasterService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/_import/package")
    public void importPackage(@RequestBody MultipartFile file ) throws  Exception{
        log.debug("REST request to Import ServiceMasters  {}");
        packageMasterService.importPackage(file);
    }
}
