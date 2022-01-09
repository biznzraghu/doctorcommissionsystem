package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Group;
import org.nh.artha.repository.GroupRepository;
import org.nh.artha.repository.search.GroupSearchRepository;
import org.nh.artha.service.GroupService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.nh.security.PrivilegeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * REST controller for managing {@link org.nh.artha.domain.Group}.
 */
@RestController
@RequestMapping("/api")
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "group";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupService groupService;

    private final GroupRepository groupRepository;

    private final GroupSearchRepository groupSearchRepository;

    private final ApplicationProperties applicationProperties;

    public GroupResource(GroupService groupService,GroupRepository groupRepository,GroupSearchRepository groupSearchRepository,ApplicationProperties applicationProperties) {
        this.groupService = groupService;
        this.groupRepository=groupRepository;
        this.groupSearchRepository=groupSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * {@code POST  /groups} : Create a new group.
     *
     * @param group the group to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new group, or with status {@code 400 (Bad Request)} if the group has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to save Group : {}", group);
        if (group.getId() != null) {
            throw new BadRequestAlertException("A new group cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Group result = groupService.save(group);
        return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /groups} : Updates an existing group.
     *
     * @param group the group to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated group,
     * or with status {@code 400 (Bad Request)} if the group is not valid,
     * or with status {@code 500 (Internal Server Error)} if the group couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/groups")
    public ResponseEntity<Group> updateGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to update Group : {}", group);
        if (group.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Group result = groupService.save(group);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, group.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /groups} : get all the groups.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
     */
    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getAllGroups(Pageable pageable) {
        log.debug("REST request to get a page of Groups");
        Page<Group> page = groupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /groups/:id} : get the "id" group.
     *
     * @param id the id of the group to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the group, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/groups/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable Long id) {
        log.debug("REST request to get Group : {}", id);
        Optional<Group> group = groupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(group);
    }

    /**
     * {@code DELETE  /groups/:id} : delete the "id" group.
     *
     * @param id the id of the group to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        log.debug("REST request to delete Group : {}", id);
        groupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/groups?query=:query} : search for the group corresponding
     * to the query.
     *
     * @param query the query of the group search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/groups")
    public ResponseEntity<List<Group>> searchGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Groups for query {}", query);
        Page<Group> page = groupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/groups")
    public ResponseEntity<Void> indexGroups(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Groups");
        long resultCount = groupRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            groupService.doIndex(i, pageSize, fromDate, toDate);
        }
        groupSearchRepository.refresh();

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "Group Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }
}
