package org.nh.artha.web.rest;

//import com.codahale.metrics.annotation.Timed;

import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.nh.artha.domain.TariffClass;
import org.nh.artha.service.CommonValueSetCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TariffClass.
 */
@RestController
@RequestMapping("/api")
public class TariffClassResource {

    private final Logger log = LoggerFactory.getLogger(TariffClassResource.class);

    private final CommonValueSetCodeService commonValueSetCodeService;


    public TariffClassResource(CommonValueSetCodeService commonValueSetCodeService)
    {
        this.commonValueSetCodeService=commonValueSetCodeService;

    }

    /**
     * GET  /tariff-classes : get all the tariffClasses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tariffClasses in body
     */
    @GetMapping("/tariff-classes")
  //@Timed
    public List<TariffClass> getAllTariffClasses() {
        log.debug("REST request to get all TariffClasses");
        org.springframework.data.domain.Page<TariffClass> tariffClasses = commonValueSetCodeService.findAll(TariffClass.class);
        return tariffClasses.getContent();
    }

    /**
     * GET  /tariff-classes/:id : get the "id" tariffClass.
     *
     * @param id the id of the tariffClass to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tariffClass, or with status 404 (Not Found)
     */
    @GetMapping("/tariff-classes/{id}")
  //@Timed
    public ResponseEntity<TariffClass> getTariffClass(@PathVariable Long id) {
        log.debug("REST request to get TariffClass : {}", id);
        TariffClass tariffClass = commonValueSetCodeService.findOne(TariffClass.class,id);
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
    @GetMapping("/_search/tariff-classes")
  //@Timed
    public ResponseEntity<List<?>> searchTariffClasses(@RequestParam String query,Pageable pageable) throws URISyntaxException {
        log.debug("REST request to search TariffClasses for query {}", query);

        String modifiedQuery = "valueSet.code:" + TariffClass.VALUE_SET_CODE + " " + query;
        log.debug("REST request to search Tariff Class for query {}", modifiedQuery);
        try {
            org.springframework.data.domain.Page<?> page = commonValueSetCodeService.search(modifiedQuery, pageable);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }catch(SearchPhaseExecutionException e){
            log.error("No Index found for {}",e);// nothing to do with the exception hence mode is debug
            org.springframework.data.domain.Page page = new PageImpl(Collections.emptyList(), pageable, 0);
            return new ResponseEntity(page.getContent(),
               new HttpHeaders(),
                HttpStatus.OK);
        }
    }




}
