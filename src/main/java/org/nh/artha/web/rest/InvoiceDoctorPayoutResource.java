package org.nh.artha.web.rest;

import org.nh.artha.domain.InvoiceDoctorPayout;
import org.nh.artha.service.InvoiceDoctorPayoutService;
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
 * REST controller for managing {@link org.nh.artha.domain.InvoiceDoctorPayout}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceDoctorPayoutResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceDoctorPayoutResource.class);

    private static final String ENTITY_NAME = "invoiceDoctorPayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceDoctorPayoutService invoiceDoctorPayoutService;

    public InvoiceDoctorPayoutResource(InvoiceDoctorPayoutService invoiceDoctorPayoutService) {
        this.invoiceDoctorPayoutService = invoiceDoctorPayoutService;
    }

    /**
     * {@code POST  /invoice-doctor-payouts} : Create a new invoiceDoctorPayout.
     *
     * @param invoiceDoctorPayout the invoiceDoctorPayout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceDoctorPayout, or with status {@code 400 (Bad Request)} if the invoiceDoctorPayout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-doctor-payouts")
    public ResponseEntity<InvoiceDoctorPayout> createInvoiceDoctorPayout(@RequestBody InvoiceDoctorPayout invoiceDoctorPayout) throws URISyntaxException {
        log.debug("REST request to save InvoiceDoctorPayout : {}", invoiceDoctorPayout);
        if (invoiceDoctorPayout.getId() != null) {
            throw new BadRequestAlertException("A new invoiceDoctorPayout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceDoctorPayout result = invoiceDoctorPayoutService.save(invoiceDoctorPayout);
        return ResponseEntity.created(new URI("/api/invoice-doctor-payouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-doctor-payouts} : Updates an existing invoiceDoctorPayout.
     *
     * @param invoiceDoctorPayout the invoiceDoctorPayout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceDoctorPayout,
     * or with status {@code 400 (Bad Request)} if the invoiceDoctorPayout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceDoctorPayout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-doctor-payouts")
    public ResponseEntity<InvoiceDoctorPayout> updateInvoiceDoctorPayout(@RequestBody InvoiceDoctorPayout invoiceDoctorPayout) throws URISyntaxException {
        log.debug("REST request to update InvoiceDoctorPayout : {}", invoiceDoctorPayout);
        if (invoiceDoctorPayout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InvoiceDoctorPayout result = invoiceDoctorPayoutService.save(invoiceDoctorPayout);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceDoctorPayout.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /invoice-doctor-payouts} : get all the invoiceDoctorPayouts.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceDoctorPayouts in body.
     */
    @GetMapping("/invoice-doctor-payouts")
    public ResponseEntity<List<InvoiceDoctorPayout>> getAllInvoiceDoctorPayouts(Pageable pageable) {
        log.debug("REST request to get a page of InvoiceDoctorPayouts");
        Page<InvoiceDoctorPayout> page = invoiceDoctorPayoutService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-doctor-payouts/:id} : get the "id" invoiceDoctorPayout.
     *
     * @param id the id of the invoiceDoctorPayout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceDoctorPayout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-doctor-payouts/{id}")
    public ResponseEntity<InvoiceDoctorPayout> getInvoiceDoctorPayout(@PathVariable Long id) {
        log.debug("REST request to get InvoiceDoctorPayout : {}", id);
        Optional<InvoiceDoctorPayout> invoiceDoctorPayout = invoiceDoctorPayoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceDoctorPayout);
    }

    /**
     * {@code DELETE  /invoice-doctor-payouts/:id} : delete the "id" invoiceDoctorPayout.
     *
     * @param id the id of the invoiceDoctorPayout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-doctor-payouts/{id}")
    public ResponseEntity<Void> deleteInvoiceDoctorPayout(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceDoctorPayout : {}", id);
        invoiceDoctorPayoutService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/invoice-doctor-payouts?query=:query} : search for the invoiceDoctorPayout corresponding
     * to the query.
     *
     * @param query the query of the invoiceDoctorPayout search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/invoice-doctor-payouts")
    public ResponseEntity<List<InvoiceDoctorPayout>> searchInvoiceDoctorPayouts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InvoiceDoctorPayouts for query {}", query);
        Page<InvoiceDoctorPayout> page = invoiceDoctorPayoutService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
