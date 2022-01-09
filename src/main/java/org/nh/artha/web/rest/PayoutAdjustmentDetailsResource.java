package org.nh.artha.web.rest;

import org.nh.artha.domain.PayoutAdjustmentDetails;
import org.nh.artha.service.PayoutAdjustmentDetailsService;
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
 * REST controller for managing {@link org.nh.artha.domain.PayoutAdjustmentDetails}.
 */
@RestController
@RequestMapping("/api")
public class PayoutAdjustmentDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PayoutAdjustmentDetailsResource.class);

    private static final String ENTITY_NAME = "payoutAdjustmentDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayoutAdjustmentDetailsService payoutAdjustmentDetailsService;

    public PayoutAdjustmentDetailsResource(PayoutAdjustmentDetailsService payoutAdjustmentDetailsService) {
        this.payoutAdjustmentDetailsService = payoutAdjustmentDetailsService;
    }

    /**
     * {@code POST  /payout-adjustment-details} : Create a new payoutAdjustmentDetails.
     *
     * @param payoutAdjustmentDetails the payoutAdjustmentDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payoutAdjustmentDetails, or with status {@code 400 (Bad Request)} if the payoutAdjustmentDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payout-adjustment-details")
    public ResponseEntity<PayoutAdjustmentDetails> createPayoutAdjustmentDetails(@RequestBody PayoutAdjustmentDetails payoutAdjustmentDetails) throws URISyntaxException {
        log.debug("REST request to save PayoutAdjustmentDetails : {}", payoutAdjustmentDetails);
        if (payoutAdjustmentDetails.getId() != null) {
            throw new BadRequestAlertException("A new payoutAdjustmentDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayoutAdjustmentDetails result = payoutAdjustmentDetailsService.save(payoutAdjustmentDetails);
        return ResponseEntity.created(new URI("/api/payout-adjustment-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payout-adjustment-details} : Updates an existing payoutAdjustmentDetails.
     *
     * @param payoutAdjustmentDetails the payoutAdjustmentDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payoutAdjustmentDetails,
     * or with status {@code 400 (Bad Request)} if the payoutAdjustmentDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payoutAdjustmentDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payout-adjustment-details")
    public ResponseEntity<PayoutAdjustmentDetails> updatePayoutAdjustmentDetails(@RequestBody PayoutAdjustmentDetails payoutAdjustmentDetails) throws URISyntaxException {
        log.debug("REST request to update PayoutAdjustmentDetails : {}", payoutAdjustmentDetails);
        if (payoutAdjustmentDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayoutAdjustmentDetails result = payoutAdjustmentDetailsService.save(payoutAdjustmentDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payoutAdjustmentDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payout-adjustment-details} : get all the payoutAdjustmentDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payoutAdjustmentDetails in body.
     */
    @GetMapping("/payout-adjustment-details")
    public ResponseEntity<List<PayoutAdjustmentDetails>> getAllPayoutAdjustmentDetails(Pageable pageable) {
        log.debug("REST request to get a page of PayoutAdjustmentDetails");
        Page<PayoutAdjustmentDetails> page = payoutAdjustmentDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payout-adjustment-details/:id} : get the "id" payoutAdjustmentDetails.
     *
     * @param id the id of the payoutAdjustmentDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payoutAdjustmentDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payout-adjustment-details/{id}")
    public ResponseEntity<PayoutAdjustmentDetails> getPayoutAdjustmentDetails(@PathVariable Long id) {
        log.debug("REST request to get PayoutAdjustmentDetails : {}", id);
        Optional<PayoutAdjustmentDetails> payoutAdjustmentDetails = payoutAdjustmentDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payoutAdjustmentDetails);
    }

    /**
     * {@code DELETE  /payout-adjustment-details/:id} : delete the "id" payoutAdjustmentDetails.
     *
     * @param id the id of the payoutAdjustmentDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payout-adjustment-details/{id}")
    public ResponseEntity<Void> deletePayoutAdjustmentDetails(@PathVariable Long id) {
        log.debug("REST request to delete PayoutAdjustmentDetails : {}", id);
        payoutAdjustmentDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/payout-adjustment-details?query=:query} : search for the payoutAdjustmentDetails corresponding
     * to the query.
     *
     * @param query the query of the payoutAdjustmentDetails search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payout-adjustment-details")
    public ResponseEntity<List<PayoutAdjustmentDetails>> searchPayoutAdjustmentDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PayoutAdjustmentDetails for query {}", query);
        Page<PayoutAdjustmentDetails> page = payoutAdjustmentDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
