package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.DoctorPayoutDepartment;
import org.nh.artha.repository.DoctorPayoutDepartmentRepository;
import org.nh.artha.service.DoctorPayoutDepartmentService;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.DoctorPayoutDepartment}.
 */
@RestController
@RequestMapping("/api")
public class DoctorPayoutDepartmentResource {

    private final Logger log = LoggerFactory.getLogger(DoctorPayoutDepartmentResource.class);

    private static final String ENTITY_NAME = "doctorPayoutDepartment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorPayoutDepartmentService doctorPayoutDepartmentService;
    private final ApplicationProperties applicationProperties;
    private final DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository;

    public DoctorPayoutDepartmentResource(DoctorPayoutDepartmentService doctorPayoutDepartmentService,ApplicationProperties applicationProperties,DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository) {
        this.doctorPayoutDepartmentService = doctorPayoutDepartmentService;
        this.applicationProperties=applicationProperties;
        this.doctorPayoutDepartmentRepository=doctorPayoutDepartmentRepository;
    }

    /**
     * {@code POST  /doctor-payout-departments} : Create a new doctorPayoutDepartment.
     *
     * @param doctorPayoutDepartment the doctorPayoutDepartment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorPayoutDepartment, or with status {@code 400 (Bad Request)} if the doctorPayoutDepartment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doctor-payout-departments")
    public ResponseEntity<DoctorPayoutDepartment> createDoctorPayoutDepartment(@RequestBody DoctorPayoutDepartment doctorPayoutDepartment) throws URISyntaxException {
        log.debug("REST request to save DoctorPayoutDepartment : {}", doctorPayoutDepartment);
        if (doctorPayoutDepartment.getId() != null) {
            throw new BadRequestAlertException("A new doctorPayoutDepartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorPayoutDepartment result = doctorPayoutDepartmentService.save(doctorPayoutDepartment);
        return ResponseEntity.created(new URI("/api/doctor-payout-departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doctor-payout-departments} : Updates an existing doctorPayoutDepartment.
     *
     * @param doctorPayoutDepartment the doctorPayoutDepartment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorPayoutDepartment,
     * or with status {@code 400 (Bad Request)} if the doctorPayoutDepartment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctorPayoutDepartment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doctor-payout-departments")
    public ResponseEntity<DoctorPayoutDepartment> updateDoctorPayoutDepartment(@RequestBody DoctorPayoutDepartment doctorPayoutDepartment) throws URISyntaxException {
        log.debug("REST request to update DoctorPayoutDepartment : {}", doctorPayoutDepartment);
        if (doctorPayoutDepartment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorPayoutDepartment result = doctorPayoutDepartmentService.save(doctorPayoutDepartment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorPayoutDepartment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /doctor-payout-departments} : get all the doctorPayoutDepartments.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctorPayoutDepartments in body.
     */
    @GetMapping("/doctor-payout-departments")
    public ResponseEntity<List<DoctorPayoutDepartment>> getAllDoctorPayoutDepartments(Pageable pageable) {
        log.debug("REST request to get a page of DoctorPayoutDepartments");
        Page<DoctorPayoutDepartment> page = doctorPayoutDepartmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doctor-payout-departments/:id} : get the "id" doctorPayoutDepartment.
     *
     * @param id the id of the doctorPayoutDepartment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorPayoutDepartment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctor-payout-departments/{id}")
    public ResponseEntity<DoctorPayoutDepartment> getDoctorPayoutDepartment(@PathVariable Long id) {
        log.debug("REST request to get DoctorPayoutDepartment : {}", id);
        Optional<DoctorPayoutDepartment> doctorPayoutDepartment = doctorPayoutDepartmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorPayoutDepartment);
    }

    /**
     * {@code DELETE  /doctor-payout-departments/:id} : delete the "id" doctorPayoutDepartment.
     *
     * @param id the id of the doctorPayoutDepartment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doctor-payout-departments/{id}")
    public ResponseEntity<Void> deleteDoctorPayoutDepartment(@PathVariable Long id) {
        log.debug("REST request to delete DoctorPayoutDepartment : {}", id);
        doctorPayoutDepartmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/doctor-payout-departments?query=:query} : search for the doctorPayoutDepartment corresponding
     * to the query.
     *
     * @param query the query of the doctorPayoutDepartment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/doctor-payout-departments")
    public ResponseEntity<List<DoctorPayoutDepartment>> searchDoctorPayoutDepartments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DoctorPayoutDepartments for query {}", query);
        Page<DoctorPayoutDepartment> page = doctorPayoutDepartmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @GetMapping("/_export/employee-wise-summary")
    public Map<String, String> exportEmployeeWiseSummary(@RequestParam("unitCode") String unitCode,@RequestParam("month")Integer month,@RequestParam("year") Integer year,@RequestParam (required = false)Integer departmentId) throws Exception {

       // log.debug("REST request to export applicable consultant for query {}", query);
        return doctorPayoutDepartmentService.exportEmployeeWiseSummary(unitCode,year,month,departmentId);
    }
    @GetMapping("/_index/doctor-payout-department")
    public ResponseEntity<Void> doIndex(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate){
        log.debug("REST request to do elastic index on DoctorPayout");
        long resultCount = doctorPayoutDepartmentRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            doctorPayoutDepartmentService.doIndex(i, pageSize,fromDate,toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("ARTHA", "Elastic index is completed",fromDate.toString()+" , "+toDate.toString())).build();
    }
}
