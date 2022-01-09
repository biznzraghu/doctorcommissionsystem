package org.nh.artha.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Role;
import org.nh.artha.domain.dto.UserDTO;
import org.nh.artha.repository.RoleRepository;
import org.nh.artha.repository.search.RoleSearchRepository;
import org.nh.artha.security.UserPreferencesUtils;
import org.nh.artha.security.dto.User;
import org.nh.artha.service.RoleService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.nh.artha.domain.Role}.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    private final RoleSearchRepository roleSearchRepository;

    private final ApplicationProperties applicationProperties;

    @Autowired
    private ObjectMapper objectMapper;

    public RoleResource(RoleService roleService,RoleRepository roleRepository,RoleSearchRepository roleSearchRepository,
                        ApplicationProperties applicationProperties) {
        this.roleService = roleService;
        this.roleRepository=roleRepository;
        this.roleSearchRepository=roleSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * PUT  /roles : Updates an existing role.
     *
     * @param role the role to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated role,
     * or with status 400 (Bad Request) if the role is not valid,
     * or with status 500 (Internal Server Error) if the role couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to save Role : {}", role);
        if (role.getId() != null) {
            throw new BadRequestAlertException("A new Role cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = UserPreferencesUtils.getCurrentUserPreferences().getUser();
        role.setCreatedDatetime(LocalDateTime.now());
        role.setCreatedBy(objectMapper.convertValue(user,UserDTO.class));
        Role result = roleService.save(role);
        return ResponseEntity.created(new URI("/api/roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to update Role : {}", role);
        if (role.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Role result = roleService.save(role);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, role.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roles : get all the roles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles")
    //@Timed
    public List<Role> getAllRoles() {
        log.debug("REST request to get all Roles");
        return roleService.findAll();
    }

    /**
     * GET  /roles/:id : get the "id" role.
     *
     * @param id the id of the role to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the role, or with status 404 (Not Found)
     */
    @GetMapping("/roles/{id}")
    //@Timed
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Role role = roleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(role));
    }

    /**
     * DELETE  /roles/:id : delete the "id" role.
     *
     * @param id the id of the role to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roles/{id}")
    //@Timed
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/roles?query=:query} : search for the role corresponding
     * to the query.
     *
     * @param query the query of the role search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/roles")
    public ResponseEntity<List<Role>> searchRoles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Roles for query {}", query);
        Page<Role> page = roleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/role")
    public ResponseEntity<Void> indexRole(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Role");
        long resultCount = roleRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            roleService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "Role Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }
}
