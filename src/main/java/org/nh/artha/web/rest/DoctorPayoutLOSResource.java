package org.nh.artha.web.rest;

import org.nh.artha.domain.DoctorPayoutLOS;
import org.nh.artha.service.DoctorPayoutLOSService;
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
 * REST controller for managing {@link org.nh.artha.domain.DoctorPayoutLOS}.
 */
@RestController
@RequestMapping("/api")
public class DoctorPayoutLOSResource {

    private final Logger log = LoggerFactory.getLogger(DoctorPayoutLOSResource.class);

    private static final String ENTITY_NAME = "doctorPayoutLOS";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorPayoutLOSService doctorPayoutLOSService;

    public DoctorPayoutLOSResource(DoctorPayoutLOSService doctorPayoutLOSService) {
        this.doctorPayoutLOSService = doctorPayoutLOSService;
    }

    /**
     * {@code POST  /doctor-payout-los} : Create a new doctorPayoutLOS.
     *
     * @param doctorPayoutLOS the doctorPayoutLOS to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorPayoutLOS, or with status {@code 400 (Bad Request)} if the doctorPayoutLOS has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doctor-payout-los")
    public ResponseEntity<DoctorPayoutLOS> createDoctorPayoutLOS(@RequestBody DoctorPayoutLOS doctorPayoutLOS) throws URISyntaxException {
        log.debug("REST request to save DoctorPayoutLOS : {}", doctorPayoutLOS);
        if (doctorPayoutLOS.getId() != null) {
            throw new BadRequestAlertException("A new doctorPayoutLOS cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorPayoutLOS result = doctorPayoutLOSService.save(doctorPayoutLOS);
        return ResponseEntity.created(new URI("/api/doctor-payout-los/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doctor-payout-los} : Updates an existing doctorPayoutLOS.
     *
     * @param doctorPayoutLOS the doctorPayoutLOS to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorPayoutLOS,
     * or with status {@code 400 (Bad Request)} if the doctorPayoutLOS is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctorPayoutLOS couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doctor-payout-los")
    public ResponseEntity<DoctorPayoutLOS> updateDoctorPayoutLOS(@RequestBody DoctorPayoutLOS doctorPayoutLOS) throws URISyntaxException {
        log.debug("REST request to update DoctorPayoutLOS : {}", doctorPayoutLOS);
        if (doctorPayoutLOS.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorPayoutLOS result = doctorPayoutLOSService.save(doctorPayoutLOS);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorPayoutLOS.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /doctor-payout-los} : get all the doctorPayoutLOS.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctorPayoutLOS in body.
     */
    @GetMapping("/doctor-payout-los")
    public ResponseEntity<List<DoctorPayoutLOS>> getAllDoctorPayoutLOS(Pageable pageable) {
        log.debug("REST request to get a page of DoctorPayoutLOS");
        Page<DoctorPayoutLOS> page = doctorPayoutLOSService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doctor-payout-los/:id} : get the "id" doctorPayoutLOS.
     *
     * @param id the id of the doctorPayoutLOS to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorPayoutLOS, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctor-payout-los/{id}")
    public ResponseEntity<DoctorPayoutLOS> getDoctorPayoutLOS(@PathVariable Long id) {
        log.debug("REST request to get DoctorPayoutLOS : {}", id);
        Optional<DoctorPayoutLOS> doctorPayoutLOS = doctorPayoutLOSService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorPayoutLOS);
    }

    /**
     * {@code DELETE  /doctor-payout-los/:id} : delete the "id" doctorPayoutLOS.
     *
     * @param id the id of the doctorPayoutLOS to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doctor-payout-los/{id}")
    public ResponseEntity<Void> deleteDoctorPayoutLOS(@PathVariable Long id) {
        log.debug("REST request to delete DoctorPayoutLOS : {}", id);
        doctorPayoutLOSService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/doctor-payout-los?query=:query} : search for the doctorPayoutLOS corresponding
     * to the query.
     *
     * @param query the query of the doctorPayoutLOS search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/doctor-payout-los")
    public ResponseEntity<List<DoctorPayoutLOS>> searchDoctorPayoutLOS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DoctorPayoutLOS for query {}", query);
        Page<DoctorPayoutLOS> page = doctorPayoutLOSService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
