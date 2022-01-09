package org.nh.artha.web.rest;

import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.nh.artha.domain.ServiceType;
import org.nh.artha.repository.ServiceTypeRepository;
import org.nh.artha.repository.search.ServiceTypeSearchRepository;
import org.nh.artha.service.CommonValueSetCodeService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.nh.seqgen.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.ServiceType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ServiceTypeResource {

    private final Logger log = LoggerFactory.getLogger(TariffClassResource.class);

    private final CommonValueSetCodeService commonValueSetCodeService;


    public ServiceTypeResource(CommonValueSetCodeService commonValueSetCodeService)
    {
        this.commonValueSetCodeService=commonValueSetCodeService;

    }

    /**
     * GET  /tariff-classes : get all the tariffClasses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tariffClasses in body
     */
    @GetMapping("/service-types")
    //@Timed
    public List<ServiceType> getAllTariffClasses() {
        log.debug("REST request to get all TariffClasses");
        org.springframework.data.domain.Page<ServiceType> tariffClasses = commonValueSetCodeService.findAll(ServiceType.class);
        return tariffClasses.getContent();
    }

    /**
     * GET  /tariff-classes/:id : get the "id" tariffClass.
     *
     * @param id the id of the tariffClass to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tariffClass, or with status 404 (Not Found)
     */
    @GetMapping("/service-types/{id}")
    //@Timed
    public ResponseEntity<ServiceType> getTariffClass(@PathVariable Long id) {
        log.debug("REST request to get TariffClass : {}", id);
        ServiceType tariffClass = commonValueSetCodeService.findOne(ServiceType.class,id);
        return Optional.ofNullable(tariffClass)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * SEARCH  /_search/tariff-classes?query=:query : search for the tariffClass corresponding
     * to the query.
     *
     * @param query the query of the tariffClass search
     * @return the result of the search
     */
    @GetMapping("/_search/service-types")
    //@Timed
    public ResponseEntity<List<?>> searchTariffClasses(@RequestParam String query, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to search TariffClasses for query {}", query);

        String modifiedQuery = "valueSet.code:" + ServiceType.VALUE_SET_CODE + " " + query;
        log.debug("REST request to search Tariff Class for query {}", modifiedQuery);
        try {
            org.springframework.data.domain.Page<?> page = commonValueSetCodeService.search(modifiedQuery, pageable);
            HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tariff-classes");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }catch(SearchPhaseExecutionException | URISyntaxException e){
            log.error("No Index found for {}",e);// nothing to do with the exception hence mode is debug
            org.springframework.data.domain.Page page = new PageImpl(Collections.emptyList(), pageable, 0);
            return new ResponseEntity(page.getContent(),
                PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tariff-classes"),
                HttpStatus.OK);
        }
    }

}
