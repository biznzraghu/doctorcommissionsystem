package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserOrganizationRoleMapping;
import org.nh.artha.repository.UserOrganizationRoleMappingRepository;
import org.nh.artha.repository.search.UserOrganizationRoleMappingSearchRepository;
import org.nh.artha.service.UserOrganizationRoleMappingService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
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
 * REST controller for managing {@link org.nh.artha.domain.UserOrganizationRoleMapping}.
 */
@RestController
@RequestMapping("/api")
public class UserOrganizationRoleMappingResource {

    private final Logger log = LoggerFactory.getLogger(UserOrganizationRoleMappingResource.class);

    private static final String ENTITY_NAME = "userOrganizationRoleMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserOrganizationRoleMappingService userOrganizationRoleMappingService;

    private final UserOrganizationRoleMappingRepository userOrganizationRoleMappingRepository;

    private final UserOrganizationRoleMappingSearchRepository userOrganizationRoleMappingSearchRepository;

    private final ApplicationProperties applicationProperties;

    public UserOrganizationRoleMappingResource(UserOrganizationRoleMappingService userOrganizationRoleMappingService,UserOrganizationRoleMappingRepository userOrganizationRoleMappingRepository,
                                               UserOrganizationRoleMappingSearchRepository userOrganizationRoleMappingSearchRepository,
                                               ApplicationProperties applicationProperties) {
        this.userOrganizationRoleMappingService = userOrganizationRoleMappingService;
        this.userOrganizationRoleMappingRepository=userOrganizationRoleMappingRepository;
        this.userOrganizationRoleMappingSearchRepository=userOrganizationRoleMappingSearchRepository;
        this.applicationProperties =applicationProperties;
    }

    /**
     * {@code POST  /user-organization-role-mappings} : Create a new userOrganizationRoleMapping.
     *
     * @param userOrganizationRoleMapping the userOrganizationRoleMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userOrganizationRoleMapping, or with status {@code 400 (Bad Request)} if the userOrganizationRoleMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-organization-role-mappings")
    public ResponseEntity<UserOrganizationRoleMapping> createUserOrganizationRoleMapping(@Valid @RequestBody UserOrganizationRoleMapping userOrganizationRoleMapping) throws URISyntaxException {
        log.debug("REST request to save UserOrganizationRoleMapping : {}", userOrganizationRoleMapping);
        if (userOrganizationRoleMapping.getId() != null) {
            throw new BadRequestAlertException("A new userOrganizationRoleMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserOrganizationRoleMapping result = userOrganizationRoleMappingService.save(userOrganizationRoleMapping);
        return ResponseEntity.created(new URI("/api/user-organization-role-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-organization-role-mappings} : Updates an existing userOrganizationRoleMapping.
     *
     * @param userOrganizationRoleMapping the userOrganizationRoleMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userOrganizationRoleMapping,
     * or with status {@code 400 (Bad Request)} if the userOrganizationRoleMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userOrganizationRoleMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-organization-role-mappings")
    public ResponseEntity<UserOrganizationRoleMapping> updateUserOrganizationRoleMapping(@Valid @RequestBody UserOrganizationRoleMapping userOrganizationRoleMapping) throws URISyntaxException {
        log.debug("REST request to update UserOrganizationRoleMapping : {}", userOrganizationRoleMapping);
        if (userOrganizationRoleMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserOrganizationRoleMapping result = userOrganizationRoleMappingService.save(userOrganizationRoleMapping);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userOrganizationRoleMapping.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-organization-role-mappings} : get all the userOrganizationRoleMappings.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userOrganizationRoleMappings in body.
     */
    @GetMapping("/user-organization-role-mappings")
    public List<UserOrganizationRoleMapping> getAllUserOrganizationRoleMappings() {
        log.debug("REST request to get all UserOrganizationRoleMappings");
        return userOrganizationRoleMappingService.findAll();
    }

    /**
     * {@code GET  /user-organization-role-mappings/:id} : get the "id" userOrganizationRoleMapping.
     *
     * @param id the id of the userOrganizationRoleMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userOrganizationRoleMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-organization-role-mappings/{id}")
    public ResponseEntity<UserOrganizationRoleMapping> getUserOrganizationRoleMapping(@PathVariable Long id) {
        log.debug("REST request to get UserOrganizationRoleMapping : {}", id);
        Optional<UserOrganizationRoleMapping> userOrganizationRoleMapping = userOrganizationRoleMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userOrganizationRoleMapping);
    }

    /**
     * {@code DELETE  /user-organization-role-mappings/:id} : delete the "id" userOrganizationRoleMapping.
     *
     * @param id the id of the userOrganizationRoleMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-organization-role-mappings/{id}")
    public ResponseEntity<Void> deleteUserOrganizationRoleMapping(@PathVariable Long id) {
        log.debug("REST request to delete UserOrganizationRoleMapping : {}", id);
        userOrganizationRoleMappingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/user-organization-role-mappings?query=:query} : search for the userOrganizationRoleMapping corresponding
     * to the query.
     *
     * @param query the query of the userOrganizationRoleMapping search.
     * @return the result of the search.
     */
    @GetMapping("/_search/user-organization-role-mappings")
    public List<UserOrganizationRoleMapping> searchUserOrganizationRoleMappings(@RequestParam String query) {
        log.debug("REST request to search UserOrganizationRoleMappings for query {}", query);
        return userOrganizationRoleMappingService.search(query);
    }

    @GetMapping("/_index/user-organization-role")
    public ResponseEntity<Void> indexUserOrganizationDepartment(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on UserOrganizationRoleMapping");
        long resultCount = userOrganizationRoleMappingRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            userOrganizationRoleMappingService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "UserOrganizationRoleMapping Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }
}
