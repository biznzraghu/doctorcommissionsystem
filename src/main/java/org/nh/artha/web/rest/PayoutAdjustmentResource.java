package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.PayoutAdjustment;
import org.nh.artha.repository.PayoutAdjustmentRepository;
import org.nh.artha.service.PayoutAdjustmentService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.PayoutAdjustment}.
 */
@RestController
@RequestMapping("/api")
public class PayoutAdjustmentResource {

    private final Logger log = LoggerFactory.getLogger(PayoutAdjustmentResource.class);

    private static final String ENTITY_NAME = "payoutAdjustment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayoutAdjustmentService payoutAdjustmentService;

    private final PayoutAdjustmentRepository payoutAdjustmentRepository;

    private final ApplicationProperties applicationProperties;

    public PayoutAdjustmentResource(PayoutAdjustmentService payoutAdjustmentService,PayoutAdjustmentRepository payoutAdjustmentRepository,
                                    ApplicationProperties applicationProperties) {
        this.payoutAdjustmentService = payoutAdjustmentService;
        this.payoutAdjustmentRepository=payoutAdjustmentRepository;
        this.applicationProperties =applicationProperties;
    }

    /**
     * {@code POST  /payout-adjustments} : Create a new payoutAdjustment.
     *
     * @param payoutAdjustment the payoutAdjustment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payoutAdjustment, or with status {@code 400 (Bad Request)} if the payoutAdjustment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payout-adjustments")
    public ResponseEntity<PayoutAdjustment> createPayoutAdjustment(@RequestBody PayoutAdjustment payoutAdjustment) throws URISyntaxException {
        log.debug("REST request to save PayoutAdjustment : {}", payoutAdjustment);
        if (payoutAdjustment.getId() != null) {
            throw new BadRequestAlertException("A new payoutAdjustment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayoutAdjustment result = payoutAdjustmentService.save(payoutAdjustment);
        return ResponseEntity.created(new URI("/api/payout-adjustments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payout-adjustments} : Updates an existing payoutAdjustment.
     *
     * @param payoutAdjustment the payoutAdjustment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payoutAdjustment,
     * or with status {@code 400 (Bad Request)} if the payoutAdjustment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payoutAdjustment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payout-adjustments")
    public ResponseEntity<PayoutAdjustment> updatePayoutAdjustment(@RequestBody PayoutAdjustment payoutAdjustment) throws URISyntaxException {
        log.debug("REST request to update PayoutAdjustment : {}", payoutAdjustment);
        if (payoutAdjustment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayoutAdjustment result = payoutAdjustmentService.save(payoutAdjustment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payoutAdjustment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payout-adjustments} : get all the payoutAdjustments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payoutAdjustments in body.
     */
    @GetMapping("/payout-adjustments")
    public ResponseEntity<List<PayoutAdjustment>> getAllPayoutAdjustments(Pageable pageable) {
        log.debug("REST request to get a page of PayoutAdjustments");
        Page<PayoutAdjustment> page = payoutAdjustmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payout-adjustments/:id} : get the "id" payoutAdjustment.
     *
     * @param id the id of the payoutAdjustment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payoutAdjustment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payout-adjustments/{id}")
    public ResponseEntity<PayoutAdjustment> getPayoutAdjustment(@PathVariable Long id) {
        log.debug("REST request to get PayoutAdjustment : {}", id);
        Optional<PayoutAdjustment> payoutAdjustment = payoutAdjustmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payoutAdjustment);
    }

    /**
     * {@code DELETE  /payout-adjustments/:id} : delete the "id" payoutAdjustment.
     *
     * @param id the id of the payoutAdjustment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payout-adjustments/{id}")
    public ResponseEntity<Void> deletePayoutAdjustment(@PathVariable Long id) {
        log.debug("REST request to delete PayoutAdjustment : {}", id);
        payoutAdjustmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/payout-adjustments?query=:query} : search for the payoutAdjustment corresponding
     * to the query.
     *
     * @param query the query of the payoutAdjustment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payout-adjustments")
    public ResponseEntity<List<PayoutAdjustment>> searchPayoutAdjustments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PayoutAdjustments for query {}", query);
        Page<PayoutAdjustment> page = payoutAdjustmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/payout-adjustment")
    public ResponseEntity<Void> doIndex(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate){
        log.debug("REST request to do elastic index on Payout Adjustment",fromDate,toDate);
        long resultCount = payoutAdjustmentRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            payoutAdjustmentService.doIndex(i, pageSize,fromDate,toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("ARTHA", "Elastic index is completed",fromDate.toString()+" , "+toDate.toString())).build();
    }

    @DeleteMapping("payoutadjustment/deletebyQuery")
    public void  deleteAllRecord(@RequestParam(value = "query") String query){
        Iterable<PayoutAdjustment> search = payoutAdjustmentService.search(query, PageRequest.of(0,99999));
        List<PayoutAdjustment> collect = StreamSupport.stream(search.spliterator(), false)
            .collect(Collectors.toList());
        for(int i=0;i<collect.size();i++){
            payoutAdjustmentService.delete(collect.get(i).getId());
        }
    }
}
