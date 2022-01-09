package org.nh.artha.web.rest;

import org.nh.artha.domain.ServiceItemException;
import org.nh.artha.service.ServiceItemExceptionService;
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
 * REST controller for managing {@link org.nh.artha.domain.ServiceItemException}.
 */
@RestController
@RequestMapping("/api")
public class ServiceItemExceptionResource {

    private final Logger log = LoggerFactory.getLogger(ServiceItemExceptionResource.class);

    private static final String ENTITY_NAME = "serviceItemException";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceItemExceptionService serviceItemExceptionService;

    public ServiceItemExceptionResource(ServiceItemExceptionService serviceItemExceptionService) {
        this.serviceItemExceptionService = serviceItemExceptionService;
    }

    /**
     * {@code POST  /service-item-exceptions} : Create a new serviceItemException.
     *
     * @param serviceItemException the serviceItemException to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceItemException, or with status {@code 400 (Bad Request)} if the serviceItemException has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-item-exceptions")
    public ResponseEntity<ServiceItemException> createServiceItemException(@RequestBody ServiceItemException serviceItemException) throws URISyntaxException {
        log.debug("REST request to save ServiceItemException : {}", serviceItemException);
        if (serviceItemException.getId() != null) {
            throw new BadRequestAlertException("A new serviceItemException cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceItemException result = serviceItemExceptionService.save(serviceItemException);
        return ResponseEntity.created(new URI("/api/service-item-exceptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-item-exceptions} : Updates an existing serviceItemException.
     *
     * @param serviceItemException the serviceItemException to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceItemException,
     * or with status {@code 400 (Bad Request)} if the serviceItemException is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceItemException couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-item-exceptions")
    public ResponseEntity<ServiceItemException> updateServiceItemException(@RequestBody ServiceItemException serviceItemException) throws URISyntaxException {
        log.debug("REST request to update ServiceItemException : {}", serviceItemException);
        if (serviceItemException.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceItemException result = serviceItemExceptionService.save(serviceItemException);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceItemException.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /service-item-exceptions} : get all the serviceItemExceptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceItemExceptions in body.
     */
    @GetMapping("/service-item-exceptions")
    public ResponseEntity<List<ServiceItemException>> getAllServiceItemExceptions(Pageable pageable) {
        log.debug("REST request to get a page of ServiceItemExceptions");
        Page<ServiceItemException> page = serviceItemExceptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-item-exceptions/:id} : get the "id" serviceItemException.
     *
     * @param id the id of the serviceItemException to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceItemException, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-item-exceptions/{id}")
    public ResponseEntity<ServiceItemException> getServiceItemException(@PathVariable Long id) {
        log.debug("REST request to get ServiceItemException : {}", id);
        Optional<ServiceItemException> serviceItemException = serviceItemExceptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceItemException);
    }

    /**
     * {@code DELETE  /service-item-exceptions/:id} : delete the "id" serviceItemException.
     *
     * @param id the id of the serviceItemException to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-item-exceptions/{id}")
    public ResponseEntity<Void> deleteServiceItemException(@PathVariable Long id) {
        log.debug("REST request to delete ServiceItemException : {}", id);
        serviceItemExceptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/service-item-exceptions?query=:query} : search for the serviceItemException corresponding
     * to the query.
     *
     * @param query the query of the serviceItemException search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/service-item-exceptions")
    public ResponseEntity<List<ServiceItemException>> searchServiceItemExceptions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceItemExceptions for query {}", query);
        Page<ServiceItemException> page = serviceItemExceptionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/save-all/service-item-exceptions")
    public ResponseEntity<List<ServiceItemException>> createServiceItemExceptions(@RequestBody List<ServiceItemException> serviceItemException) throws URISyntaxException {
        log.debug("REST request to save ServiceItemException : {}", serviceItemException);
        List<ServiceItemException> result = serviceItemExceptionService.saveAll(serviceItemException);
        return ResponseEntity.created(new URI("/api/service-item-exceptions/" ))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
            .body(result);
    }
}
