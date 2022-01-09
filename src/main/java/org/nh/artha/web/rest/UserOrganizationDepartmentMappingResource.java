package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserOrganizationDepartmentMapping;
import org.nh.artha.repository.UserOrganizationDepartmentMappingRepository;
import org.nh.artha.repository.search.UserOrganizationDepartmentMappingSearchRepository;
import org.nh.artha.service.UserOrganizationDepartmentMappingService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.UserOrganizationDepartmentMapping}.
 */
@RestController
@RequestMapping("/api")
public class UserOrganizationDepartmentMappingResource {

    private final Logger log = LoggerFactory.getLogger(UserOrganizationDepartmentMappingResource.class);

    private static final String ENTITY_NAME = "userOrganizationDepartmentMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserOrganizationDepartmentMappingService userOrganizationDepartmentMappingService;

    private final UserOrganizationDepartmentMappingRepository userOrganizationDepartmentMappingRepository;

    private final UserOrganizationDepartmentMappingSearchRepository userOrganizationDepartmentMappingSearchRepository;

    private final ApplicationProperties applicationProperties;

    public UserOrganizationDepartmentMappingResource(UserOrganizationDepartmentMappingService userOrganizationDepartmentMappingService,
                                                     UserOrganizationDepartmentMappingRepository userOrganizationDepartmentMappingRepository,
                                                     UserOrganizationDepartmentMappingSearchRepository userOrganizationDepartmentMappingSearchRepository,
                                                     ApplicationProperties applicationProperties) {
        this.userOrganizationDepartmentMappingService = userOrganizationDepartmentMappingService;
        this.userOrganizationDepartmentMappingRepository=userOrganizationDepartmentMappingRepository;
        this.userOrganizationDepartmentMappingSearchRepository=userOrganizationDepartmentMappingSearchRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /user-organization-department-mappings} : Create a new userOrganizationDepartmentMapping.
     *
     * @param userOrganizationDepartmentMapping the userOrganizationDepartmentMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userOrganizationDepartmentMapping, or with status {@code 400 (Bad Request)} if the userOrganizationDepartmentMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-organization-department-mappings")
    public ResponseEntity<UserOrganizationDepartmentMapping> createUserOrganizationDepartmentMapping(@RequestBody UserOrganizationDepartmentMapping userOrganizationDepartmentMapping) throws URISyntaxException {
        log.debug("REST request to save UserOrganizationDepartmentMapping : {}", userOrganizationDepartmentMapping);
        if (userOrganizationDepartmentMapping.getId() != null) {
            throw new BadRequestAlertException("A new userOrganizationDepartmentMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserOrganizationDepartmentMapping result = userOrganizationDepartmentMappingService.save(userOrganizationDepartmentMapping);
        return ResponseEntity.created(new URI("/api/user-organization-department-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-organization-department-mappings} : Updates an existing userOrganizationDepartmentMapping.
     *
     * @param userOrganizationDepartmentMapping the userOrganizationDepartmentMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userOrganizationDepartmentMapping,
     * or with status {@code 400 (Bad Request)} if the userOrganizationDepartmentMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userOrganizationDepartmentMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-organization-department-mappings")
    public ResponseEntity<UserOrganizationDepartmentMapping> updateUserOrganizationDepartmentMapping(@RequestBody UserOrganizationDepartmentMapping userOrganizationDepartmentMapping) throws URISyntaxException {
        log.debug("REST request to update UserOrganizationDepartmentMapping : {}", userOrganizationDepartmentMapping);
        if (userOrganizationDepartmentMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserOrganizationDepartmentMapping result = userOrganizationDepartmentMappingService.save(userOrganizationDepartmentMapping);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userOrganizationDepartmentMapping.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-organization-department-mappings} : get all the userOrganizationDepartmentMappings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userOrganizationDepartmentMappings in body.
     */
    @GetMapping("/user-organization-department-mappings")
    public ResponseEntity<List<UserOrganizationDepartmentMapping>> getAllUserOrganizationDepartmentMappings(Pageable pageable) {
        log.debug("REST request to get a page of UserOrganizationDepartmentMappings");
        Page<UserOrganizationDepartmentMapping> page = userOrganizationDepartmentMappingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-organization-department-mappings/:id} : get the "id" userOrganizationDepartmentMapping.
     *
     * @param id the id of the userOrganizationDepartmentMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userOrganizationDepartmentMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-organization-department-mappings/{id}")
    public ResponseEntity<UserOrganizationDepartmentMapping> getUserOrganizationDepartmentMapping(@PathVariable Long id) {
        log.debug("REST request to get UserOrganizationDepartmentMapping : {}", id);
        Optional<UserOrganizationDepartmentMapping> userOrganizationDepartmentMapping = userOrganizationDepartmentMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userOrganizationDepartmentMapping);
    }

    /**
     * {@code DELETE  /user-organization-department-mappings/:id} : delete the "id" userOrganizationDepartmentMapping.
     *
     * @param id the id of the userOrganizationDepartmentMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-organization-department-mappings/{id}")
    public ResponseEntity<Void> deleteUserOrganizationDepartmentMapping(@PathVariable Long id) {
        log.debug("REST request to delete UserOrganizationDepartmentMapping : {}", id);
        userOrganizationDepartmentMappingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/user-organization-department-mappings?query=:query} : search for the userOrganizationDepartmentMapping corresponding
     * to the query.
     *
     * @param query the query of the userOrganizationDepartmentMapping search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/user-organization-department-mappings")
    public ResponseEntity<List<UserOrganizationDepartmentMapping>> searchUserOrganizationDepartmentMappings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserOrganizationDepartmentMappings for query {}", query);
        Page<UserOrganizationDepartmentMapping> page = userOrganizationDepartmentMappingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/user-organization-department")
    public ResponseEntity<Void> indexUserOrganizationDepartment(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on UserOrganizationDepartmentMapping");
        long resultCount = userOrganizationDepartmentMappingRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            userOrganizationDepartmentMappingService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "UserOrganizationDepartmentMapping Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }


    @PostMapping("/multiple/save/user-organization-department-mappings")
    public ResponseEntity<List<UserOrganizationDepartmentMapping>> multipleUserOrganizationDepartmentMapping(@RequestBody List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMapping) throws URISyntaxException {
        log.debug("REST request to save UserOrganizationDepartmentMapping : {}", userOrganizationDepartmentMapping);
        List<UserOrganizationDepartmentMapping> result = userOrganizationDepartmentMappingService.saveAll(userOrganizationDepartmentMapping);
        return ResponseEntity.created(new URI("/api/user-organization-department-mappings/" +""))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
            .body(result);
    }
    @GetMapping("/_export/applicable-consultant")
    public Map<String, String> exportApplicableConsultant(@RequestParam String query, Pageable pageable) throws Exception {

        log.debug("REST request to export applicable consultant for query {}", query);
        return userOrganizationDepartmentMappingService.exportApplicableConsultant(query, pageable);
    }
}
