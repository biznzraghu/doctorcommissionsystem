package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ValueSet;
import org.nh.artha.repository.ValueSetRepository;
import org.nh.artha.repository.search.ValueSetSearchRepository;
import org.nh.artha.service.ValueSetService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.nh.security.PrivilegeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.ValueSet}.
 */
@RestController
@RequestMapping("/api")
public class ValueSetResource {

    private final Logger log = LoggerFactory.getLogger(ValueSetResource.class);

    private static final String ENTITY_NAME = "valueSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValueSetService valueSetService;

    private final ValueSetRepository valueSetRepository;

    private final ValueSetSearchRepository valueSetSearchRepository;

    private final ApplicationProperties applicationProperties;

    public ValueSetResource(ValueSetService valueSetService,ValueSetRepository valueSetRepository,ValueSetSearchRepository valueSetSearchRepository,ApplicationProperties applicationProperties) {
        this.valueSetService = valueSetService;
        this.valueSetRepository=valueSetRepository;
        this.valueSetSearchRepository=valueSetSearchRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /value-sets} : Create a new valueSet.
     *
     * @param valueSet the valueSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valueSet, or with status {@code 400 (Bad Request)} if the valueSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/value-sets")
    public ResponseEntity<ValueSet> createValueSet(@Valid @RequestBody ValueSet valueSet) throws URISyntaxException {
        log.debug("REST request to save ValueSet : {}", valueSet);
        if (valueSet.getId() != null) {
            throw new BadRequestAlertException("A new valueSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValueSet result = valueSetService.save(valueSet);
        return ResponseEntity.created(new URI("/api/value-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /value-sets} : Updates an existing valueSet.
     *
     * @param valueSet the valueSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valueSet,
     * or with status {@code 400 (Bad Request)} if the valueSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valueSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/value-sets")
    public ResponseEntity<ValueSet> updateValueSet(@Valid @RequestBody ValueSet valueSet) throws URISyntaxException {
        log.debug("REST request to update ValueSet : {}", valueSet);
        if (valueSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ValueSet result = valueSetService.save(valueSet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valueSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /value-sets} : get all the valueSets.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valueSets in body.
     */
    @GetMapping("/value-sets")
    public ResponseEntity<List<ValueSet>> getAllValueSets(Pageable pageable) {
        log.debug("REST request to get a page of ValueSets");
        Page<ValueSet> page = valueSetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /value-sets/:id} : get the "id" valueSet.
     *
     * @param id the id of the valueSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valueSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/value-sets/{id}")
    public ResponseEntity<ValueSet> getValueSet(@PathVariable Long id) {
        log.debug("REST request to get ValueSet : {}", id);
        Optional<ValueSet> valueSet = valueSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(valueSet);
    }

    /**
     * {@code DELETE  /value-sets/:id} : delete the "id" valueSet.
     *
     * @param id the id of the valueSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/value-sets/{id}")
    public ResponseEntity<Void> deleteValueSet(@PathVariable Long id) {
        log.debug("REST request to delete ValueSet : {}", id);
        valueSetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/value-sets?query=:query} : search for the valueSet corresponding
     * to the query.
     *
     * @param query the query of the valueSet search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/value-sets")
    public ResponseEntity<List<ValueSet>> searchValueSets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ValueSets for query {}", query);
        Page<ValueSet> page = valueSetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/value-sets")
    public ResponseEntity<Void> indexValueSet(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on ValueSets");
        long resultCount = valueSetRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            valueSetService.doIndex(i, pageSize, fromDate, toDate);
        }
        valueSetSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "ValueSet Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }
}
