package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ServiceMaster;
import org.nh.artha.repository.ServiceMasterRepository;
import org.nh.artha.service.ServiceMasterService;
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
import org.springframework.web.multipart.MultipartFile;
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
 * REST controller for managing {@link org.nh.artha.domain.ServiceMaster}.
 */
@RestController
@RequestMapping("/api")
public class ServiceMasterResource {

    private final Logger log = LoggerFactory.getLogger(ServiceMasterResource.class);

    private static final String ENTITY_NAME = "serviceMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceMasterService serviceMasterService;

    private final ServiceMasterRepository serviceMasterRepository;

    private final ApplicationProperties applicationProperties;

    public ServiceMasterResource(ServiceMasterService serviceMasterService,ServiceMasterRepository serviceMasterRepository,ApplicationProperties applicationProperties) {
        this.serviceMasterService = serviceMasterService;
        this.serviceMasterRepository=serviceMasterRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /service-masters} : Create a new serviceMaster.
     *
     * @param serviceMaster the serviceMaster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceMaster, or with status {@code 400 (Bad Request)} if the serviceMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-masters")
    public ResponseEntity<ServiceMaster> createServiceMaster(@Valid @RequestBody ServiceMaster serviceMaster) throws URISyntaxException {
        log.debug("REST request to save ServiceMaster : {}", serviceMaster);
        if (serviceMaster.getId() != null) {
            throw new BadRequestAlertException("A new serviceMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceMaster result = serviceMasterService.save(serviceMaster);
        return ResponseEntity.created(new URI("/api/service-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-masters} : Updates an existing serviceMaster.
     *
     * @param serviceMaster the serviceMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceMaster,
     * or with status {@code 400 (Bad Request)} if the serviceMaster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-masters")
    public ResponseEntity<ServiceMaster> updateServiceMaster(@Valid @RequestBody ServiceMaster serviceMaster) throws URISyntaxException {
        log.debug("REST request to update ServiceMaster : {}", serviceMaster);
        if (serviceMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceMaster result = serviceMasterService.save(serviceMaster);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceMaster.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /service-masters} : get all the serviceMasters.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceMasters in body.
     */
    @GetMapping("/service-masters")
    public ResponseEntity<List<ServiceMaster>> getAllServiceMasters(@RequestParam String query,Pageable pageable) {
        log.debug("REST request to get a page of ServiceMasters");
        Page<ServiceMaster> page = serviceMasterService.searchForAllService(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-masters/:id} : get the "id" serviceMaster.
     *
     * @param id the id of the serviceMaster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceMaster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-masters/{id}")
    public ResponseEntity<ServiceMaster> getServiceMaster(@PathVariable Long id) {
        log.debug("REST request to get ServiceMaster : {}", id);
        Optional<ServiceMaster> serviceMaster = serviceMasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceMaster);
    }

    /**
     * {@code DELETE  /service-masters/:id} : delete the "id" serviceMaster.
     *
     * @param id the id of the serviceMaster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-masters/{id}")
    public ResponseEntity<Void> deleteServiceMaster(@PathVariable Long id) {
        log.debug("REST request to delete ServiceMaster : {}", id);
        serviceMasterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/service-masters?query=:query} : search for the serviceMaster corresponding
     * to the query.
     *
     * @param query the query of the serviceMaster search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/service-masters")
    public ResponseEntity<List<ServiceMaster>> searchServiceMasters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceMasters for query {}", query);
        Page<ServiceMaster> page = serviceMasterService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_search/all-services")
    public ResponseEntity<List<ServiceMaster>> searchAllServices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceMasters for query {}", query);
        Page<ServiceMaster> page = serviceMasterService.searchForAllService(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_search/service-group")
    public ResponseEntity<List<ServiceMaster>> searchForServiceGroup(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceMasters for query {}", query);
        Page<ServiceMaster> page = serviceMasterService.searchForServiceGroup(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_search/service-type")
    public ResponseEntity<List<ServiceMaster>> searchForServiceType(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceMasters for query {}", query);
        Page<ServiceMaster> page = serviceMasterService.searchForServiceType(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/_import/service")
    public void importService(@RequestBody MultipartFile file ) throws  Exception{
        log.debug("REST request to Import ServiceMasters  {}");
        serviceMasterService.importService(file);
    }
    @GetMapping("/_index/service-master")
    public ResponseEntity<Void> doIndex(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate){
        log.debug("REST request to do elastic index on serviceMaster");
        long resultCount = serviceMasterRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            serviceMasterService.doIndex(i, pageSize,fromDate,toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("ARTHA", "Elastic index is completed",fromDate.toString()+" , "+toDate.toString())).build();
    }
}
