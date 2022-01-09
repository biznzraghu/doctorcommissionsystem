package org.nh.artha.web.rest;

import org.nh.artha.domain.PackageComponent;
import org.nh.artha.service.PackageComponentService;
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
 * REST controller for managing {@link org.nh.artha.domain.PackageComponent}.
 */
@RestController
@RequestMapping("/api")
public class PackageComponentResource {

    private final Logger log = LoggerFactory.getLogger(PackageComponentResource.class);

    private static final String ENTITY_NAME = "packageComponent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackageComponentService packageComponentService;

    public PackageComponentResource(PackageComponentService packageComponentService) {
        this.packageComponentService = packageComponentService;
    }

    /**
     * {@code POST  /package-components} : Create a new packageComponent.
     *
     * @param packageComponent the packageComponent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packageComponent, or with status {@code 400 (Bad Request)} if the packageComponent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/package-components")
    public ResponseEntity<PackageComponent> createPackageComponent(@RequestBody PackageComponent packageComponent) throws URISyntaxException {
        log.debug("REST request to save PackageComponent : {}", packageComponent);
        if (packageComponent.getId() != null) {
            throw new BadRequestAlertException("A new packageComponent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PackageComponent result = packageComponentService.save(packageComponent);
        return ResponseEntity.created(new URI("/api/package-components/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /package-components} : Updates an existing packageComponent.
     *
     * @param packageComponent the packageComponent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageComponent,
     * or with status {@code 400 (Bad Request)} if the packageComponent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packageComponent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/package-components")
    public ResponseEntity<PackageComponent> updatePackageComponent(@RequestBody PackageComponent packageComponent) throws URISyntaxException {
        log.debug("REST request to update PackageComponent : {}", packageComponent);
        if (packageComponent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PackageComponent result = packageComponentService.save(packageComponent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packageComponent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /package-components} : get all the packageComponents.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packageComponents in body.
     */
    @GetMapping("/package-components")
    public ResponseEntity<List<PackageComponent>> getAllPackageComponents(Pageable pageable) {
        log.debug("REST request to get a page of PackageComponents");
        Page<PackageComponent> page = packageComponentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /package-components/:id} : get the "id" packageComponent.
     *
     * @param id the id of the packageComponent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packageComponent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/package-components/{id}")
    public ResponseEntity<PackageComponent> getPackageComponent(@PathVariable Long id) {
        log.debug("REST request to get PackageComponent : {}", id);
        Optional<PackageComponent> packageComponent = packageComponentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(packageComponent);
    }

    /**
     * {@code DELETE  /package-components/:id} : delete the "id" packageComponent.
     *
     * @param id the id of the packageComponent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/package-components/{id}")
    public ResponseEntity<Void> deletePackageComponent(@PathVariable Long id) {
        log.debug("REST request to delete PackageComponent : {}", id);
        packageComponentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/package-components?query=:query} : search for the packageComponent corresponding
     * to the query.
     *
     * @param query the query of the packageComponent search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/package-components")
    public ResponseEntity<List<PackageComponent>> searchPackageComponents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PackageComponents for query {}", query);
        Page<PackageComponent> page = packageComponentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
