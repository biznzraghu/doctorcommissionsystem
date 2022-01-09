package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Feature;
import org.nh.artha.repository.FeatureRepository;
import org.nh.artha.repository.search.FeatureSearchRepository;
import org.nh.artha.service.FeatureService;
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
 * REST controller for managing {@link org.nh.artha.domain.Feature}.
 */
@RestController
@RequestMapping("/api")
public class FeatureResource {

    private final Logger log = LoggerFactory.getLogger(FeatureResource.class);

    private static final String ENTITY_NAME = "feature";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeatureService featureService;

    private final FeatureRepository featureRepository;

    private final FeatureSearchRepository featureSearchRepository;

    private final ApplicationProperties applicationProperties;

    public FeatureResource(FeatureService featureService,FeatureRepository featureRepository,FeatureSearchRepository featureSearchRepository,
                           ApplicationProperties applicationProperties) {
        this.featureService = featureService;
        this.featureRepository = featureRepository;
        this.featureSearchRepository =featureSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * {@code POST  /features} : Create a new feature.
     *
     * @param feature the feature to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feature, or with status {@code 400 (Bad Request)} if the feature has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/features")
    public ResponseEntity<Feature> createFeature(@Valid @RequestBody Feature feature) throws URISyntaxException {
        log.debug("REST request to save Feature : {}", feature);
        if (feature.getId() != null) {
            throw new BadRequestAlertException("A new feature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Feature result = featureService.save(feature);
        return ResponseEntity.created(new URI("/api/features/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /features} : Updates an existing feature.
     *
     * @param feature the feature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feature,
     * or with status {@code 400 (Bad Request)} if the feature is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/features")
    public ResponseEntity<Feature> updateFeature(@Valid @RequestBody Feature feature) throws URISyntaxException {
        log.debug("REST request to update Feature : {}", feature);
        if (feature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Feature result = featureService.save(feature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feature.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /features} : get all the features.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of features in body.
     */
    @GetMapping("/features")
    public ResponseEntity<List<Feature>> getAllFeatures(Pageable pageable) {
        log.debug("REST request to get a page of Features");
        Page<Feature> page = featureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /features/:id} : get the "id" feature.
     *
     * @param id the id of the feature to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feature, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/features/{id}")
    public ResponseEntity<Feature> getFeature(@PathVariable Long id) {
        log.debug("REST request to get Feature : {}", id);
        Optional<Feature> feature = featureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feature);
    }

    /**
     * {@code DELETE  /features/:id} : delete the "id" feature.
     *
     * @param id the id of the feature to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/features/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        log.debug("REST request to delete Feature : {}", id);
        featureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/features?query=:query} : search for the feature corresponding
     * to the query.
     *
     * @param query the query of the feature search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/features")
    public ResponseEntity<List<Feature>> searchFeatures(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Features for query {}", query);
        Page<Feature> page = featureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/index/feature")
    public ResponseEntity<Void> indexFeature(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Feature");
        long resultCount = featureRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            featureService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "Feature Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }

}
