package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Privilege;
import org.nh.artha.repository.PrivilegeRepository;
import org.nh.artha.repository.search.PrivilegeSearchRepository;
import org.nh.artha.service.PrivilegeService;
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
 * REST controller for managing {@link org.nh.artha.domain.Privilege}.
 */
@RestController
@RequestMapping("/api")
public class PrivilegeResource {

    private final Logger log = LoggerFactory.getLogger(PrivilegeResource.class);

    private static final String ENTITY_NAME = "privilege";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrivilegeService privilegeService;

    private final PrivilegeRepository privilegeRepository;

    private final PrivilegeSearchRepository privilegeSearchRepository;

    private final ApplicationProperties applicationProperties;

    public PrivilegeResource(PrivilegeService privilegeService,PrivilegeRepository privilegeRepository,PrivilegeSearchRepository privilegeSearchRepository,
                             ApplicationProperties applicationProperties) {
        this.privilegeService = privilegeService;
        this.privilegeRepository=privilegeRepository;
        this.privilegeSearchRepository= privilegeSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * {@code POST  /privileges} : Create a new privilege.
     *
     * @param privilege the privilege to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new privilege, or with status {@code 400 (Bad Request)} if the privilege has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/privileges")
    public ResponseEntity<Privilege> createPrivilege(@Valid @RequestBody Privilege privilege) throws URISyntaxException {
        log.debug("REST request to save Privilege : {}", privilege);
        if (privilege.getId() != null) {
            throw new BadRequestAlertException("A new privilege cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Privilege result = privilegeService.save(privilege);
        return ResponseEntity.created(new URI("/api/privileges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /privileges} : Updates an existing privilege.
     *
     * @param privilege the privilege to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated privilege,
     * or with status {@code 400 (Bad Request)} if the privilege is not valid,
     * or with status {@code 500 (Internal Server Error)} if the privilege couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/privileges")
    public ResponseEntity<Privilege> updatePrivilege(@Valid @RequestBody Privilege privilege) throws URISyntaxException {
        log.debug("REST request to update Privilege : {}", privilege);
        if (privilege.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Privilege result = privilegeService.save(privilege);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, privilege.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /privileges} : get all the privileges.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of privileges in body.
     */
    @GetMapping("/privileges")
    public ResponseEntity<List<Privilege>> getAllPrivileges(Pageable pageable) {
        log.debug("REST request to get a page of Privileges");
        Page<Privilege> page = privilegeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /privileges/:id} : get the "id" privilege.
     *
     * @param id the id of the privilege to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the privilege, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/privileges/{id}")
    public ResponseEntity<Privilege> getPrivilege(@PathVariable Long id) {
        log.debug("REST request to get Privilege : {}", id);
        Optional<Privilege> privilege = privilegeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(privilege);
    }

    /**
     * {@code DELETE  /privileges/:id} : delete the "id" privilege.
     *
     * @param id the id of the privilege to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/privileges/{id}")
    public ResponseEntity<Void> deletePrivilege(@PathVariable Long id) {
        log.debug("REST request to delete Privilege : {}", id);
        privilegeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/privileges?query=:query} : search for the privilege corresponding
     * to the query.
     *
     * @param query the query of the privilege search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/privileges")
    public ResponseEntity<List<Privilege>> searchPrivileges(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Privileges for query {}", query);
        Page<Privilege> page = privilegeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/privilege")
    public ResponseEntity<Void> indexPrivilege(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Privilege");
        long resultCount = privilegeRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            privilegeService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "Privilege Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }
}
