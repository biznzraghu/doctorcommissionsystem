package org.nh.artha.web.rest;

import org.nh.artha.domain.TransactionApprovalDetails;
import org.nh.artha.service.TransactionApprovalDetailsService;
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
 * REST controller for managing {@link org.nh.artha.domain.TransactionApprovalDetails}.
 */
@RestController
@RequestMapping("/api")
public class TransactionApprovalDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TransactionApprovalDetailsResource.class);

    private static final String ENTITY_NAME = "transactionApprovalDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionApprovalDetailsService transactionApprovalDetailsService;

    public TransactionApprovalDetailsResource(TransactionApprovalDetailsService transactionApprovalDetailsService) {
        this.transactionApprovalDetailsService = transactionApprovalDetailsService;
    }

    /**
     * {@code POST  /transaction-approval-details} : Create a new transactionApprovalDetails.
     *
     * @param transactionApprovalDetails the transactionApprovalDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionApprovalDetails, or with status {@code 400 (Bad Request)} if the transactionApprovalDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-approval-details")
    public ResponseEntity<TransactionApprovalDetails> createTransactionApprovalDetails(@RequestBody TransactionApprovalDetails transactionApprovalDetails) throws URISyntaxException {
        log.debug("REST request to save TransactionApprovalDetails : {}", transactionApprovalDetails);
        if (transactionApprovalDetails.getId() != null) {
            throw new BadRequestAlertException("A new transactionApprovalDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionApprovalDetails result = transactionApprovalDetailsService.save(transactionApprovalDetails);
        return ResponseEntity.created(new URI("/api/transaction-approval-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-approval-details} : Updates an existing transactionApprovalDetails.
     *
     * @param transactionApprovalDetails the transactionApprovalDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionApprovalDetails,
     * or with status {@code 400 (Bad Request)} if the transactionApprovalDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionApprovalDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-approval-details")
    public ResponseEntity<TransactionApprovalDetails> updateTransactionApprovalDetails(@RequestBody TransactionApprovalDetails transactionApprovalDetails) throws URISyntaxException {
        log.debug("REST request to update TransactionApprovalDetails : {}", transactionApprovalDetails);
        if (transactionApprovalDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TransactionApprovalDetails result = transactionApprovalDetailsService.save(transactionApprovalDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionApprovalDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /transaction-approval-details} : get all the transactionApprovalDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionApprovalDetails in body.
     */
    @GetMapping("/transaction-approval-details")
    public ResponseEntity<List<TransactionApprovalDetails>> getAllTransactionApprovalDetails(Pageable pageable) {
        log.debug("REST request to get a page of TransactionApprovalDetails");
        Page<TransactionApprovalDetails> page = transactionApprovalDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-approval-details/:id} : get the "id" transactionApprovalDetails.
     *
     * @param id the id of the transactionApprovalDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionApprovalDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-approval-details/{id}")
    public ResponseEntity<TransactionApprovalDetails> getTransactionApprovalDetails(@PathVariable Long id) {
        log.debug("REST request to get TransactionApprovalDetails : {}", id);
        Optional<TransactionApprovalDetails> transactionApprovalDetails = transactionApprovalDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionApprovalDetails);
    }

    /**
     * {@code DELETE  /transaction-approval-details/:id} : delete the "id" transactionApprovalDetails.
     *
     * @param id the id of the transactionApprovalDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-approval-details/{id}")
    public ResponseEntity<Void> deleteTransactionApprovalDetails(@PathVariable Long id) {
        log.debug("REST request to delete TransactionApprovalDetails : {}", id);
        transactionApprovalDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/transaction-approval-details?query=:query} : search for the transactionApprovalDetails corresponding
     * to the query.
     *
     * @param query the query of the transactionApprovalDetails search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-approval-details")
    public ResponseEntity<List<TransactionApprovalDetails>> searchTransactionApprovalDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TransactionApprovalDetails for query {}", query);
        Page<TransactionApprovalDetails> page = transactionApprovalDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
