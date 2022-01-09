package org.nh.artha.web.rest;

import org.nh.artha.domain.ConfigurationCategory;
import org.nh.artha.repository.ConfigurationCategoryRepository;
import org.nh.artha.repository.search.ConfigurationCategorySearchRepository;
import org.nh.artha.service.CommonValueSetCodeService;
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
 * REST controller for managing {@link org.nh.artha.domain.ConfigurationCategory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConfigurationCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationCategoryResource.class);

    private static final String ENTITY_NAME = "configurationCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    //private final ConfigurationCategoryRepository configurationCategoryRepository;
    private final CommonValueSetCodeService commonValueSetCodeService;

    public ConfigurationCategoryResource(CommonValueSetCodeService commonValueSetCodeService) {
        this.commonValueSetCodeService = commonValueSetCodeService;
    }


    /**
     * {@code GET  /configuration-categories} : get all the configurationCategories.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configurationCategories in body.
     */
    @GetMapping("/configuration-categories")
    public ResponseEntity<List<ConfigurationCategory>> getAllConfigurationCategories(Pageable pageable) {
        log.debug("REST request to get a page of ConfigurationCategories");
        Page<ConfigurationCategory> page = commonValueSetCodeService.findAll(ConfigurationCategory.class);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configuration-categories/:id} : get the "id" configurationCategory.
     *
     * @param id the id of the configurationCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configurationCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configuration-categories/{id}")
    public ResponseEntity<ConfigurationCategory> getConfigurationCategory(@PathVariable Long id) {
        log.debug("REST request to get ConfigurationCategory : {}", id);
        ConfigurationCategory configurationCategory = commonValueSetCodeService.findOne(ConfigurationCategory.class,id);
        return Optional.ofNullable(configurationCategory).map(result->new ResponseEntity<>(result,HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.OK));
    }


    @GetMapping("/_search/configuration-categories")
    public ResponseEntity<List<?>> searchConfigurationCategories(@RequestParam String query, Pageable pageable) {
        String modifiedQuery = "valueSet.code.raw:" + ConfigurationCategory.VALUE_SET_CODE + " " + query;
        log.debug("REST request to search for a page of ConfigurationCategories for query {}", modifiedQuery);
        Page<?> page = commonValueSetCodeService.search(modifiedQuery, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
