package org.nh.artha.web.rest;

import org.nh.artha.domain.ServiceCategory;
import org.nh.artha.repository.ServiceCategoryRepository;
import org.nh.artha.repository.search.ServiceCategorySearchRepository;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.ServiceCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ServiceCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ServiceCategoryResource.class);

    private static final String ENTITY_NAME = "serviceCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceCategoryRepository serviceCategoryRepository;

    private final ServiceCategorySearchRepository serviceCategorySearchRepository;

    public ServiceCategoryResource(ServiceCategoryRepository serviceCategoryRepository, ServiceCategorySearchRepository serviceCategorySearchRepository) {
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.serviceCategorySearchRepository = serviceCategorySearchRepository;
    }

    /**
     * {@code POST  /service-categories} : Create a new serviceCategory.
     *
     * @param serviceCategory the serviceCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceCategory, or with status {@code 400 (Bad Request)} if the serviceCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-categories")
    public ResponseEntity<ServiceCategory> createServiceCategory(@RequestBody ServiceCategory serviceCategory) throws URISyntaxException {
        log.debug("REST request to save ServiceCategory : {}", serviceCategory);
        if (serviceCategory.getId() != null) {
            throw new BadRequestAlertException("A new serviceCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceCategory result = serviceCategoryRepository.save(serviceCategory);
        serviceCategorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/service-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-categories} : Updates an existing serviceCategory.
     *
     * @param serviceCategory the serviceCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceCategory,
     * or with status {@code 400 (Bad Request)} if the serviceCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-categories")
    public ResponseEntity<ServiceCategory> updateServiceCategory(@RequestBody ServiceCategory serviceCategory) throws URISyntaxException {
        log.debug("REST request to update ServiceCategory : {}", serviceCategory);
        if (serviceCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceCategory result = serviceCategoryRepository.save(serviceCategory);
        serviceCategorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /service-categories} : get all the serviceCategories.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceCategories in body.
     */
    @GetMapping("/service-categories")
    public List<ServiceCategory> getAllServiceCategories() {
        log.debug("REST request to get all ServiceCategories");
        return serviceCategoryRepository.findAll();
    }

    /**
     * {@code GET  /service-categories/:id} : get the "id" serviceCategory.
     *
     * @param id the id of the serviceCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-categories/{id}")
    public ResponseEntity<ServiceCategory> getServiceCategory(@PathVariable Long id) {
        log.debug("REST request to get ServiceCategory : {}", id);
        Optional<ServiceCategory> serviceCategory = serviceCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceCategory);
    }

    /**
     * {@code DELETE  /service-categories/:id} : delete the "id" serviceCategory.
     *
     * @param id the id of the serviceCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-categories/{id}")
    public ResponseEntity<Void> deleteServiceCategory(@PathVariable Long id) {
        log.debug("REST request to delete ServiceCategory : {}", id);
        serviceCategoryRepository.deleteById(id);
        serviceCategorySearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/service-categories?query=:query} : search for the serviceCategory corresponding
     * to the query.
     *
     * @param query the query of the serviceCategory search.
     * @return the result of the search.
     */
    @GetMapping("/_search/service-categories")
    public List<ServiceCategory> searchServiceCategories(@RequestParam String query) {
        log.debug("REST request to search ServiceCategories for query {}", query);
        return StreamSupport
            .stream(serviceCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
