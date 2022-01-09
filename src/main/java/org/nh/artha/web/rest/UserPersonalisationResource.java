package org.nh.artha.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiParam;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserPersonalisation;
import org.nh.artha.repository.UserPersonalisationRepository;
import org.nh.artha.repository.search.UserPersonalisationSearchRepository;
import org.nh.artha.service.UserPersonalisationService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserPersonalisation.
 */
@RestController
@RequestMapping("/api")
public class UserPersonalisationResource {

    private final Logger log = LoggerFactory.getLogger(UserPersonalisationResource.class);

    private static final String ENTITY_NAME = "userPersonalisation";

    private final UserPersonalisationService userPersonalisationService;

    private final UserPersonalisationRepository userPersonalisationRepository;

    private final UserPersonalisationSearchRepository userPersonalisationSearchRepository;

    private final ApplicationProperties applicationProperties;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public UserPersonalisationResource(UserPersonalisationService userPersonalisationService,
                                       UserPersonalisationRepository userPersonalisationRepository,
                                       UserPersonalisationSearchRepository userPersonalisationSearchRepository,
                                       ApplicationProperties applicationProperties) {
        this.userPersonalisationService = userPersonalisationService;
        this.userPersonalisationRepository = userPersonalisationRepository;
        this.userPersonalisationSearchRepository = userPersonalisationSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /user-personalisations : Create a new userPersonalisation.
     *
     * @param userPersonalisation the userPersonalisation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userPersonalisation, or with status 400 (Bad Request) if the userPersonalisation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-personalisations")
    @Timed
    public ResponseEntity<UserPersonalisation> createUserPersonalisation(@Valid @RequestBody UserPersonalisation userPersonalisation) throws URISyntaxException {
        log.debug("REST request to save UserPersonalisation : {}", userPersonalisation);
        if (userPersonalisation.getId() != null) {
            throw new BadRequestAlertException(ENTITY_NAME, "idexists", "A new userPersonalisation cannot already have an ID");
        }
        UserPersonalisation result = userPersonalisationService.save(userPersonalisation);
        return ResponseEntity.created(new URI("/api/user-personalisations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,ENTITY_NAME,result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-personalisations : Updates an existing userPersonalisation.
     *
     * @param userPersonalisation the userPersonalisation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userPersonalisation,
     * or with status 400 (Bad Request) if the userPersonalisation is not valid,
     * or with status 500 (Internal Server Error) if the userPersonalisation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-personalisations")
    @Timed
    public ResponseEntity<UserPersonalisation> updateUserPersonalisation(@Valid @RequestBody UserPersonalisation userPersonalisation) throws URISyntaxException {
        log.debug("REST request to update UserPersonalisation : {}", userPersonalisation);
        if (userPersonalisation.getId() == null) {
            return createUserPersonalisation(userPersonalisation);
        }
        UserPersonalisation result = userPersonalisationService.save(userPersonalisation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,ENTITY_NAME, userPersonalisation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-personalisations : get all the userPersonalisations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userPersonalisations in body
     */
    @GetMapping("/user-personalisations")
    @Timed
    public ResponseEntity<List<UserPersonalisation>> getAllUserPersonalisations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserPersonalisations");
        Page<UserPersonalisation> page = userPersonalisationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-personalisations/:id : get the "id" userPersonalisation.
     *
     * @param id the id of the userPersonalisation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userPersonalisation, or with status 404 (Not Found)
     */
    @GetMapping("/user-personalisations/{id}")
    @Timed
    public ResponseEntity<UserPersonalisation> getUserPersonalisation(@PathVariable Long id) {
        log.debug("REST request to get UserPersonalisation : {}", id);
        UserPersonalisation userPersonalisation = userPersonalisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userPersonalisation));
    }

    /**
     * DELETE  /user-personalisations/:id : delete the "id" userPersonalisation.
     *
     * @param id the id of the userPersonalisation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-personalisations/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserPersonalisation(@PathVariable Long id) {
        log.debug("REST request to delete UserPersonalisation : {}", id);
        userPersonalisationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(applicationName, true,ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-personalisations?query=:query : search for the userPersonalisation corresponding
     * to the query.
     *
     * @param query    the query of the userPersonalisation search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-personalisations")
    @Timed
    public ResponseEntity<List<UserPersonalisation>> searchUserPersonalisations(@RequestParam String query, @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to search for a page of UserPersonalisations for query {}", query);
        Page<UserPersonalisation> page = userPersonalisationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     *
     * @return
     */
    @GetMapping("/_index/user-personalisations")
    @Timed
    public ResponseEntity<Void> indexUserPersonalisations(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on User Personalisations");
        long resultCount = userPersonalisationRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            userPersonalisationService.doIndex(i, pageSize, fromDate, toDate);
        }
        userPersonalisationSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "Elastic index User Personalisations is completed","")).build();
    }

}
