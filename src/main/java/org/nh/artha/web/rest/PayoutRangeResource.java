package org.nh.artha.web.rest;

import org.nh.artha.domain.PayoutRange;
import org.nh.artha.service.PayoutRangeService;
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
 * REST controller for managing {@link org.nh.artha.domain.PayoutRange}.
 */
@RestController
@RequestMapping("/api")
public class PayoutRangeResource {

    private final Logger log = LoggerFactory.getLogger(PayoutRangeResource.class);

    private static final String ENTITY_NAME = "payoutRange";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayoutRangeService payoutRangeService;

    public PayoutRangeResource(PayoutRangeService payoutRangeService) {
        this.payoutRangeService = payoutRangeService;
    }

    /**
     * {@code POST  /payout-ranges} : Create a new payoutRange.
     *
     * @param payoutRange the payoutRange to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payoutRange, or with status {@code 400 (Bad Request)} if the payoutRange has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payout-ranges")
    public ResponseEntity<PayoutRange> createPayoutRange(@RequestBody PayoutRange payoutRange) throws URISyntaxException {
        log.debug("REST request to save PayoutRange : {}", payoutRange);
        if (payoutRange.getId() != null) {
            throw new BadRequestAlertException("A new payoutRange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayoutRange result = payoutRangeService.save(payoutRange);
        return ResponseEntity.created(new URI("/api/payout-ranges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payout-ranges} : Updates an existing payoutRange.
     *
     * @param payoutRange the payoutRange to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payoutRange,
     * or with status {@code 400 (Bad Request)} if the payoutRange is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payoutRange couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payout-ranges")
    public ResponseEntity<PayoutRange> updatePayoutRange(@RequestBody PayoutRange payoutRange) throws URISyntaxException {
        log.debug("REST request to update PayoutRange : {}", payoutRange);
        if (payoutRange.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayoutRange result = payoutRangeService.save(payoutRange);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payoutRange.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payout-ranges} : get all the payoutRanges.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payoutRanges in body.
     */
    @GetMapping("/payout-ranges")
    public ResponseEntity<List<PayoutRange>> getAllPayoutRanges(Pageable pageable) {
        log.debug("REST request to get a page of PayoutRanges");
        Page<PayoutRange> page = payoutRangeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payout-ranges/:id} : get the "id" payoutRange.
     *
     * @param id the id of the payoutRange to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payoutRange, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payout-ranges/{id}")
    public ResponseEntity<PayoutRange> getPayoutRange(@PathVariable Long id) {
        log.debug("REST request to get PayoutRange : {}", id);
        Optional<PayoutRange> payoutRange = payoutRangeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payoutRange);
    }

    /**
     * {@code DELETE  /payout-ranges/:id} : delete the "id" payoutRange.
     *
     * @param id the id of the payoutRange to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payout-ranges/{id}")
    public ResponseEntity<Void> deletePayoutRange(@PathVariable Long id) {
        log.debug("REST request to delete PayoutRange : {}", id);
        payoutRangeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/payout-ranges?query=:query} : search for the payoutRange corresponding
     * to the query.
     *
     * @param query the query of the payoutRange search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payout-ranges")
    public ResponseEntity<List<PayoutRange>> searchPayoutRanges(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PayoutRanges for query {}", query);
        Page<PayoutRange> page = payoutRangeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
