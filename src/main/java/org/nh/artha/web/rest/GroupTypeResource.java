package org.nh.artha.web.rest;

import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.nh.artha.domain.GroupType;
import org.nh.artha.repository.GroupTypeRepository;
import org.nh.artha.repository.search.GroupTypeSearchRepository;
import org.nh.artha.service.CommonValueSetCodeService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.nh.seqgen.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
 * REST controller for managing {@link org.nh.artha.domain.GroupType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GroupTypeResource {

    private final Logger log = LoggerFactory.getLogger(GroupTypeResource.class);

    private final CommonValueSetCodeService commonValueSetCodeService;

    public GroupTypeResource(CommonValueSetCodeService commonValueSetCodeService) {
        this.commonValueSetCodeService = commonValueSetCodeService;
    }

    /**
     * GET  /group-types : get all the groupTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of groupTypes in body
     */
    @GetMapping("/group-types")
    //@Timed
    public List<GroupType> getAllGroupTypes() {
        log.debug("REST request to get all GroupTypes");
        Page<GroupType> groupTypes = commonValueSetCodeService.findAll(GroupType.class);
        return groupTypes.getContent();
    }

    /**
     * GET  /group-types/:id : get the "id" groupType.
     *
     * @param id the id of the groupType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupType, or with status 404 (Not Found)
     */
    @GetMapping("/group-types/{id}")
    //@Timed
    public ResponseEntity<GroupType> getGroupType(@PathVariable Long id) {
        log.debug("REST request to get GroupType : {}", id);
        GroupType groupType = commonValueSetCodeService.findOne(GroupType.class, id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groupType));
    }

    /**
     * SEARCH  /_search/group-types?query=:query : search for the groupType corresponding
     * to the query.
     *
     * @param query the query of the groupType search
     * @return the result of the search
     */
    @GetMapping("/_search/group-types")
    //@Timed
    public ResponseEntity<List<?>> searchGroupTypes(@RequestParam String query, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to search GroupTypes for query {}", query);
        String modifiedQuery = "valueSet.code:" + GroupType.VALUE_SET_CODE + " " + query;
        log.debug("REST request to search for a page of GroupTypes for query {}", modifiedQuery);
        try {
            Page<?> page = commonValueSetCodeService.search(modifiedQuery, pageable);
            HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/group-types");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }catch(SearchPhaseExecutionException e){
            log.error("No Index found for {}",e);// nothing to do with the exception hence mode is debug
            Page page = new PageImpl(Collections.emptyList(), pageable, 0);
            return new ResponseEntity(page.getContent(),
                PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/group-types"),
                HttpStatus.OK);
        }
    }
}
