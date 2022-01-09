package org.nh.artha.web.rest;

import org.nh.artha.domain.ValueSetCodeMapping;
import org.nh.artha.service.ValueSetCodeMappingService;
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
 * REST controller for managing {@link org.nh.artha.domain.ValueSetCodeMapping}.
 */
@RestController
@RequestMapping("/api")
public class ValueSetCodeMappingResource {

    private final Logger log = LoggerFactory.getLogger(ValueSetCodeMappingResource.class);

    private static final String ENTITY_NAME = "valueSetCodeMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValueSetCodeMappingService valueSetCodeMappingService;

    public ValueSetCodeMappingResource(ValueSetCodeMappingService valueSetCodeMappingService) {
        this.valueSetCodeMappingService = valueSetCodeMappingService;
    }

    /**
     * {@code POST  /value-set-code-mappings} : Create a new valueSetCodeMapping.
     *
     * @param valueSetCodeMapping the valueSetCodeMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valueSetCodeMapping, or with status {@code 400 (Bad Request)} if the valueSetCodeMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/value-set-code-mappings")
    public ResponseEntity<ValueSetCodeMapping> createValueSetCodeMapping(@RequestBody ValueSetCodeMapping valueSetCodeMapping) throws URISyntaxException {
        log.debug("REST request to save ValueSetCodeMapping : {}", valueSetCodeMapping);
        if (valueSetCodeMapping.getId() != null) {
            throw new BadRequestAlertException("A new valueSetCodeMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValueSetCodeMapping result = valueSetCodeMappingService.save(valueSetCodeMapping);
        return ResponseEntity.created(new URI("/api/value-set-code-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /value-set-code-mappings} : Updates an existing valueSetCodeMapping.
     *
     * @param valueSetCodeMapping the valueSetCodeMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valueSetCodeMapping,
     * or with status {@code 400 (Bad Request)} if the valueSetCodeMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valueSetCodeMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/value-set-code-mappings")
    public ResponseEntity<ValueSetCodeMapping> updateValueSetCodeMapping(@RequestBody ValueSetCodeMapping valueSetCodeMapping) throws URISyntaxException {
        log.debug("REST request to update ValueSetCodeMapping : {}", valueSetCodeMapping);
        if (valueSetCodeMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ValueSetCodeMapping result = valueSetCodeMappingService.save(valueSetCodeMapping);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valueSetCodeMapping.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /value-set-code-mappings} : get all the valueSetCodeMappings.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valueSetCodeMappings in body.
     */
    @GetMapping("/value-set-code-mappings")
    public List<ValueSetCodeMapping> getAllValueSetCodeMappings() {
        log.debug("REST request to get all ValueSetCodeMappings");
        return valueSetCodeMappingService.findAll();
    }

    /**
     * {@code GET  /value-set-code-mappings/:id} : get the "id" valueSetCodeMapping.
     *
     * @param id the id of the valueSetCodeMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valueSetCodeMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/value-set-code-mappings/{id}")
    public ResponseEntity<ValueSetCodeMapping> getValueSetCodeMapping(@PathVariable Long id) {
        log.debug("REST request to get ValueSetCodeMapping : {}", id);
        Optional<ValueSetCodeMapping> valueSetCodeMapping = valueSetCodeMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(valueSetCodeMapping);
    }

    /**
     * {@code DELETE  /value-set-code-mappings/:id} : delete the "id" valueSetCodeMapping.
     *
     * @param id the id of the valueSetCodeMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/value-set-code-mappings/{id}")
    public ResponseEntity<Void> deleteValueSetCodeMapping(@PathVariable Long id) {
        log.debug("REST request to delete ValueSetCodeMapping : {}", id);
        valueSetCodeMappingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/value-set-code-mappings?query=:query} : search for the valueSetCodeMapping corresponding
     * to the query.
     *
     * @param query the query of the valueSetCodeMapping search.
     * @return the result of the search.
     */
    @GetMapping("/_search/value-set-code-mappings")
    public List<ValueSetCodeMapping> searchValueSetCodeMappings(@RequestParam String query) {
        log.debug("REST request to search ValueSetCodeMappings for query {}", query);
        return valueSetCodeMappingService.search(query);
    }
}
