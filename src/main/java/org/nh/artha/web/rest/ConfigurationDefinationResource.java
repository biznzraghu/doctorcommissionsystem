package org.nh.artha.web.rest;

import org.nh.artha.domain.ConfigurationDefination;
import org.nh.artha.service.ConfigurationDefinationService;
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
 * REST controller for managing {@link org.nh.artha.domain.ConfigurationDefination}.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationDefinationResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationDefinationResource.class);

    private static final String ENTITY_NAME = "ConfigurationDefination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationDefinationService ConfigurationDefinationService;

    public ConfigurationDefinationResource(ConfigurationDefinationService ConfigurationDefinationService) {
        this.ConfigurationDefinationService = ConfigurationDefinationService;
    }

    /**
     * {@code POST  /ConfigurationDefinations} : Create a new ConfigurationDefination.
     *
     * @param ConfigurationDefination the ConfigurationDefination to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ConfigurationDefination, or with status {@code 400 (Bad Request)} if the ConfigurationDefination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ConfigurationDefinations")
    public ResponseEntity<ConfigurationDefination> createConfigurationDefination(@Valid @RequestBody ConfigurationDefination ConfigurationDefination) throws URISyntaxException {
        log.debug("REST request to save ConfigurationDefination : {}", ConfigurationDefination);
        if (ConfigurationDefination.getId() != null) {
            throw new BadRequestAlertException("A new ConfigurationDefination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigurationDefination result = ConfigurationDefinationService.save(ConfigurationDefination);
        return ResponseEntity.created(new URI("/api/ConfigurationDefinations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ConfigurationDefinations} : Updates an existing ConfigurationDefination.
     *
     * @param ConfigurationDefination the ConfigurationDefination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ConfigurationDefination,
     * or with status {@code 400 (Bad Request)} if the ConfigurationDefination is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ConfigurationDefination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ConfigurationDefinations")
    public ResponseEntity<ConfigurationDefination> updateConfigurationDefination(@Valid @RequestBody ConfigurationDefination ConfigurationDefination) throws URISyntaxException {
        log.debug("REST request to update ConfigurationDefination : {}", ConfigurationDefination);
        if (ConfigurationDefination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigurationDefination result = ConfigurationDefinationService.save(ConfigurationDefination);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ConfigurationDefination.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ConfigurationDefinations} : get all the ConfigurationDefinations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ConfigurationDefinations in body.
     */
    @GetMapping("/ConfigurationDefinations")
    public ResponseEntity<List<ConfigurationDefination>> getAllConfigurationDefinations(Pageable pageable) {
        log.debug("REST request to get a page of ConfigurationDefinations");
        Page<ConfigurationDefination> page = ConfigurationDefinationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ConfigurationDefinations/:id} : get the "id" ConfigurationDefination.
     *
     * @param id the id of the ConfigurationDefination to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ConfigurationDefination, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ConfigurationDefinations/{id}")
    public ResponseEntity<ConfigurationDefination> getConfigurationDefination(@PathVariable Long id) {
        log.debug("REST request to get ConfigurationDefination : {}", id);
        Optional<ConfigurationDefination> ConfigurationDefination = ConfigurationDefinationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ConfigurationDefination);
    }

    /**
     * {@code DELETE  /ConfigurationDefinations/:id} : delete the "id" ConfigurationDefination.
     *
     * @param id the id of the ConfigurationDefination to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ConfigurationDefinations/{id}")
    public ResponseEntity<Void> deleteConfigurationDefination(@PathVariable Long id) {
        log.debug("REST request to delete ConfigurationDefination : {}", id);
        ConfigurationDefinationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ConfigurationDefinations?query=:query} : search for the ConfigurationDefination corresponding
     * to the query.
     *
     * @param query the query of the ConfigurationDefination search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ConfigurationDefinations")
    public ResponseEntity<List<ConfigurationDefination>> searchConfigurationDefinations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ConfigurationDefinations for query {}", query);
        Page<ConfigurationDefination> page = ConfigurationDefinationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
