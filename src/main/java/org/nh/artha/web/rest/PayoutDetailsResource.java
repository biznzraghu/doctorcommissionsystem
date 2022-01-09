package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.PayoutDetails;
import org.nh.artha.repository.PayoutDetailsRepository;
import org.nh.artha.repository.search.PayoutDetailsSearchRepository;
import org.nh.artha.service.PayoutDetailsService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing {@link org.nh.artha.domain.PayoutDetails}.
 */
@RestController
@RequestMapping("/api")
public class PayoutDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PayoutDetailsResource.class);

    private static final String ENTITY_NAME = "payoutDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayoutDetailsService payoutDetailsService;

    private final PayoutDetailsRepository payoutDetailsRepository;

    private final PayoutDetailsSearchRepository payoutDetailsSearchRepository;

    private final ApplicationProperties applicationProperties;

    public PayoutDetailsResource(PayoutDetailsService payoutDetailsService,PayoutDetailsRepository payoutDetailsRepository,PayoutDetailsSearchRepository payoutDetailsSearchRepository,
                                 ApplicationProperties applicationProperties) {
        this.payoutDetailsService = payoutDetailsService;
        this.payoutDetailsRepository = payoutDetailsRepository;
        this.payoutDetailsSearchRepository = payoutDetailsSearchRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /payout-details} : Create a new payoutDetails.
     *
     * @param payoutDetails the payoutDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payoutDetails, or with status {@code 400 (Bad Request)} if the payoutDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payout-details")
    public ResponseEntity<PayoutDetails> createPayoutDetails(@Valid @RequestBody PayoutDetails payoutDetails) throws URISyntaxException {
        log.debug("REST request to save PayoutDetails : {}", payoutDetails);
        if (payoutDetails.getId() != null) {
            throw new BadRequestAlertException("A new payoutDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayoutDetails result = payoutDetailsService.save(payoutDetails);
        return ResponseEntity.created(new URI("/api/payout-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payout-details} : Updates an existing payoutDetails.
     *
     * @param payoutDetails the payoutDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payoutDetails,
     * or with status {@code 400 (Bad Request)} if the payoutDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payoutDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payout-details")
    public ResponseEntity<PayoutDetails> updatePayoutDetails(@Valid @RequestBody PayoutDetails payoutDetails) throws URISyntaxException {
        log.debug("REST request to update PayoutDetails : {}", payoutDetails);
        if (payoutDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayoutDetails result = payoutDetailsService.save(payoutDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payoutDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payout-details} : get all the payoutDetails.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payoutDetails in body.
     */
    @GetMapping("/payout-details")
    public ResponseEntity<List<PayoutDetails>> getAllPayoutDetails(Pageable pageable) {
        log.debug("REST request to get a page of PayoutDetails");
        Page<PayoutDetails> page = payoutDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payout-details/:id} : get the "id" payoutDetails.
     *
     * @param id the id of the payoutDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payoutDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payout-details/{id}")
    public ResponseEntity<PayoutDetails> getPayoutDetails(@PathVariable Long id) {
        log.debug("REST request to get PayoutDetails : {}", id);
        Optional<PayoutDetails> payoutDetails = payoutDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payoutDetails);
    }

    /**
     * {@code DELETE  /payout-details/:id} : delete the "id" payoutDetails.
     *
     * @param id the id of the payoutDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payout-details/{id}")
    public ResponseEntity<Void> deletePayoutDetails(@PathVariable Long id) {
        log.debug("REST request to delete PayoutDetails : {}", id);
        payoutDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/payout-details?query=:query} : search for the payoutDetails corresponding
     * to the query.
     *
     * @param query the query of the payoutDetails search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payout-details")
    public ResponseEntity<List<PayoutDetails>> searchPayoutDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PayoutDetails for query {}", query);
        Page<PayoutDetails> page = payoutDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/index/payoutdetails")
    public ResponseEntity<Void> indexPayoutDetails(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on payoutdetails");
        long resultCount = payoutDetailsRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            payoutDetailsService.doIndex(i, pageSize, fromDate, toDate);
        }
        payoutDetailsSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Department Index in Artha", "payoutdetails Index completetd ",fromDate.toString()+" "+toDate.toString())).build();
    }


}
