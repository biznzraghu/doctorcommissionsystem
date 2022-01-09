package org.nh.artha.web.rest;

import org.nh.artha.domain.PackageCodeMapping;
import org.nh.artha.service.PackageCodeMappingService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.PackageCodeMapping}.
 */
@RestController
@RequestMapping("/api")
public class PackageCodeMappingResource {

    private final Logger log = LoggerFactory.getLogger(PackageCodeMappingResource.class);

    private static final String ENTITY_NAME = "packageCodeMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackageCodeMappingService packageCodeMappingService;

    public PackageCodeMappingResource(PackageCodeMappingService packageCodeMappingService) {
        this.packageCodeMappingService = packageCodeMappingService;
    }

    /**
     * {@code POST  /package-code-mappings} : Create a new packageCodeMapping.
     *
     * @param packageCodeMapping the packageCodeMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packageCodeMapping, or with status {@code 400 (Bad Request)} if the packageCodeMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/package-code-mappings")
    public ResponseEntity<PackageCodeMapping> createPackageCodeMapping(@RequestBody PackageCodeMapping packageCodeMapping) throws URISyntaxException {
        log.debug("REST request to save PackageCodeMapping : {}", packageCodeMapping);
        if (packageCodeMapping.getId() != null) {
            throw new BadRequestAlertException("A new packageCodeMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PackageCodeMapping result = packageCodeMappingService.save(packageCodeMapping);
        return ResponseEntity.created(new URI("/api/package-code-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /package-code-mappings} : Updates an existing packageCodeMapping.
     *
     * @param packageCodeMapping the packageCodeMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageCodeMapping,
     * or with status {@code 400 (Bad Request)} if the packageCodeMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packageCodeMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/package-code-mappings")
    public ResponseEntity<PackageCodeMapping> updatePackageCodeMapping(@RequestBody PackageCodeMapping packageCodeMapping) throws URISyntaxException {
        log.debug("REST request to update PackageCodeMapping : {}", packageCodeMapping);
        if (packageCodeMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PackageCodeMapping result = packageCodeMappingService.save(packageCodeMapping);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packageCodeMapping.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /package-code-mappings} : get all the packageCodeMappings.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packageCodeMappings in body.
     */
    @GetMapping("/package-code-mappings")
    public List<PackageCodeMapping> getAllPackageCodeMappings() {
        log.debug("REST request to get all PackageCodeMappings");
        return packageCodeMappingService.findAll();
    }

    /**
     * {@code GET  /package-code-mappings/:id} : get the "id" packageCodeMapping.
     *
     * @param id the id of the packageCodeMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packageCodeMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/package-code-mappings/{id}")
    public ResponseEntity<PackageCodeMapping> getPackageCodeMapping(@PathVariable Long id) {
        log.debug("REST request to get PackageCodeMapping : {}", id);
        Optional<PackageCodeMapping> packageCodeMapping = packageCodeMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(packageCodeMapping);
    }

    /**
     * {@code DELETE  /package-code-mappings/:id} : delete the "id" packageCodeMapping.
     *
     * @param id the id of the packageCodeMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/package-code-mappings/{id}")
    public ResponseEntity<Void> deletePackageCodeMapping(@PathVariable Long id) {
        log.debug("REST request to delete PackageCodeMapping : {}", id);
        packageCodeMappingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/package-code-mappings?query=:query} : search for the packageCodeMapping corresponding
     * to the query.
     *
     * @param query the query of the packageCodeMapping search.
     * @return the result of the search.
     */
    @GetMapping("/_search/package-code-mappings")
    public List<PackageCodeMapping> searchPackageCodeMappings(@RequestParam String query) {
        log.debug("REST request to search PackageCodeMappings for query {}", query);
        return packageCodeMappingService.search(query);
    }
}
