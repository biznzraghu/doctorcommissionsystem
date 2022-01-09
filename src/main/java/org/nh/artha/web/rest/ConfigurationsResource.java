package org.nh.artha.web.rest;

import org.nh.artha.domain.Configurations;
import org.nh.artha.service.ConfigurationsService;
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
 * REST controller for managing {@link org.nh.artha.domain.Configurations}.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationsResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationsResource.class);

    private static final String ENTITY_NAME = "configurations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationsService configurationsService;

    public ConfigurationsResource(ConfigurationsService configurationsService) {
        this.configurationsService = configurationsService;
    }

    /**
     * {@code POST  /configurations} : Create a new configurations.
     *
     * @param configurations the configurations to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configurations, or with status {@code 400 (Bad Request)} if the configurations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configurations")
    public ResponseEntity<Configurations> createConfigurations(@Valid @RequestBody Configurations configurations) throws URISyntaxException {
        log.debug("REST request to save Configurations : {}", configurations);
        if (configurations.getId() != null) {
            throw new BadRequestAlertException("A new configurations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Configurations result = configurationsService.save(configurations);
        return ResponseEntity.created(new URI("/api/configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configurations} : Updates an existing configurations.
     *
     * @param configurations the configurations to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurations,
     * or with status {@code 400 (Bad Request)} if the configurations is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configurations couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configurations")
    public ResponseEntity<Configurations> updateConfigurations(@Valid @RequestBody Configurations configurations) throws URISyntaxException {
        log.debug("REST request to update Configurations : {}", configurations);
        if (configurations.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Configurations result = configurationsService.save(configurations);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurations.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /configurations} : get all the configurations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configurations in body.
     */
    @GetMapping("/configurations")
    public ResponseEntity<List<Configurations>> getAllConfigurations(Pageable pageable) {
        log.debug("REST request to get a page of Configurations");
        Page<Configurations> page = configurationsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configurations/:id} : get the "id" configurations.
     *
     * @param id the id of the configurations to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configurations, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configurations/{id}")
    public ResponseEntity<Configurations> getConfigurations(@PathVariable Long id) {
        log.debug("REST request to get Configurations : {}", id);
        Optional<Configurations> configurations = configurationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configurations);
    }

    /**
     * {@code DELETE  /configurations/:id} : delete the "id" configurations.
     *
     * @param id the id of the configurations to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configurations/{id}")
    public ResponseEntity<Void> deleteConfigurations(@PathVariable Long id) {
        log.debug("REST request to delete Configurations : {}", id);
        configurationsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/configurations?query=:query} : search for the configurations corresponding
     * to the query.
     *
     * @param query the query of the configurations search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/configurations")
    public ResponseEntity<List<Configurations>> searchConfigurations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Configurations for query {}", query);
        Page<Configurations> page = configurationsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
