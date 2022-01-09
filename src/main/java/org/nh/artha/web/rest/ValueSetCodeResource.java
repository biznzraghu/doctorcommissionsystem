package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ValueSetCode;
import org.nh.artha.repository.ValueSetCodeRepository;
import org.nh.artha.repository.search.ValueSetCodeSearchRepository;
import org.nh.artha.service.ValueSetCodeService;
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
 * REST controller for managing {@link org.nh.artha.domain.ValueSetCode}.
 */
@RestController
@RequestMapping("/api")
public class ValueSetCodeResource {

    private final Logger log = LoggerFactory.getLogger(ValueSetCodeResource.class);

    private static final String ENTITY_NAME = "valueSetCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValueSetCodeService valueSetCodeService;

    private final ValueSetCodeRepository valueSetCodeRepository;

    private final ValueSetCodeSearchRepository valueSetCodeSearchRepository;

    private final ApplicationProperties applicationProperties;

    public ValueSetCodeResource(ValueSetCodeService valueSetCodeService,ValueSetCodeRepository valueSetCodeRepository,ValueSetCodeSearchRepository valueSetCodeSearchRepository,ApplicationProperties applicationProperties) {
        this.valueSetCodeService = valueSetCodeService;
        this.valueSetCodeRepository=valueSetCodeRepository;
        this.valueSetCodeSearchRepository=valueSetCodeSearchRepository;
        this.applicationProperties= applicationProperties;
    }

    /**
     * {@code POST  /value-set-codes} : Create a new valueSetCode.
     *
     * @param valueSetCode the valueSetCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valueSetCode, or with status {@code 400 (Bad Request)} if the valueSetCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/value-set-codes")
    public ResponseEntity<ValueSetCode> createValueSetCode(@Valid @RequestBody ValueSetCode valueSetCode) throws URISyntaxException {
        log.debug("REST request to save ValueSetCode : {}", valueSetCode);
        if (valueSetCode.getId() != null) {
            throw new BadRequestAlertException("A new valueSetCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValueSetCode result = valueSetCodeService.save(valueSetCode);
        return ResponseEntity.created(new URI("/api/value-set-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /value-set-codes} : Updates an existing valueSetCode.
     *
     * @param valueSetCode the valueSetCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valueSetCode,
     * or with status {@code 400 (Bad Request)} if the valueSetCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valueSetCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/value-set-codes")
    public ResponseEntity<ValueSetCode> updateValueSetCode(@Valid @RequestBody ValueSetCode valueSetCode) throws URISyntaxException {
        log.debug("REST request to update ValueSetCode : {}", valueSetCode);
        if (valueSetCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ValueSetCode result = valueSetCodeService.save(valueSetCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valueSetCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /value-set-codes} : get all the valueSetCodes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valueSetCodes in body.
     */
    @GetMapping("/value-set-codes")
    public ResponseEntity<List<ValueSetCode>> getAllValueSetCodes(Pageable pageable) {
        log.debug("REST request to get a page of ValueSetCodes");
        Page<ValueSetCode> page = valueSetCodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /value-set-codes/:id} : get the "id" valueSetCode.
     *
     * @param id the id of the valueSetCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valueSetCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/value-set-codes/{id}")
    public ResponseEntity<ValueSetCode> getValueSetCode(@PathVariable Long id) {
        log.debug("REST request to get ValueSetCode : {}", id);
        Optional<ValueSetCode> valueSetCode = valueSetCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(valueSetCode);
    }

    /**
     * {@code DELETE  /value-set-codes/:id} : delete the "id" valueSetCode.
     *
     * @param id the id of the valueSetCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/value-set-codes/{id}")
    public ResponseEntity<Void> deleteValueSetCode(@PathVariable Long id) {
        log.debug("REST request to delete ValueSetCode : {}", id);
        valueSetCodeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/value-set-codes?query=:query} : search for the valueSetCode corresponding
     * to the query.
     *
     * @param query the query of the valueSetCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/value-set-codes")
    public ResponseEntity<List<ValueSetCode>> searchValueSetCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ValueSetCodes for query {}", query);
        Page<ValueSetCode> page = valueSetCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/value-set-codes")
    public ResponseEntity<Void> indexValueSetCodes(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on ValueSetCodes");
        long resultCount = valueSetCodeRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            valueSetCodeService.doIndex(i, pageSize, fromDate, toDate);
        }
        valueSetCodeSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "ValueSetCode Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }
}
