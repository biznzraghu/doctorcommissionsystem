package org.nh.artha.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Department;
import org.nh.artha.domain.Organization;
import org.nh.artha.domain.dto.SponsorDto;
import org.nh.artha.repository.OrganizationRepository;
import org.nh.artha.repository.search.OrganizationSearchRepository;
import org.nh.artha.service.DepartmentService;
import org.nh.artha.service.OrganizationService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.Organization}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    private static final String ENTITY_NAME = "organization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationService organizationService;

    private final OrganizationRepository organizationRepository;

    private final ApplicationProperties applicationProperties;

    private final OrganizationSearchRepository organizationSearchRepository;

    private final DepartmentService departmentService;

    private final ObjectMapper objectMapper;




    public OrganizationResource(OrganizationService organizationService,OrganizationRepository organizationRepository,ApplicationProperties applicationProperties,OrganizationSearchRepository organizationSearchRepository,DepartmentService departmentService,ObjectMapper objectMapper) {
        this.organizationService = organizationService;
        this.organizationRepository=organizationRepository;
        this.applicationProperties=applicationProperties;
        this.organizationSearchRepository=organizationSearchRepository;
        this.departmentService=departmentService;
        this.objectMapper=objectMapper;
    }

    /**
     * {@code POST  /organizations} : Create a new organization.
     *
     * @param organization the organization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organization, or with status {@code 400 (Bad Request)} if the organization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organizations")
    public ResponseEntity<Organization> createOrganization(@Valid @RequestBody Organization organization) throws URISyntaxException {
        log.debug("REST request to save Organization : {}", organization);
        if (organization.getId() != null) {
            throw new BadRequestAlertException("A new organization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Organization result = organizationService.save(organization);
        return ResponseEntity.created(new URI("/api/organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organizations} : Updates an existing organization.
     *
     * @param organization the organization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organization,
     * or with status {@code 400 (Bad Request)} if the organization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organizations")
    public ResponseEntity<Organization> updateOrganization(@Valid @RequestBody Organization organization) throws URISyntaxException {
        log.debug("REST request to update Organization : {}", organization);
        if (organization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Organization result = organizationService.save(organization);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organization.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organizations} : get all the organizations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizations in body.
     */
    @GetMapping("/organizations")
    public ResponseEntity<List<Organization>> getAllOrganizations(Pageable pageable) {
        log.debug("REST request to get a page of Organizations");
        Page<Organization> page = organizationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organizations/:id} : get the "id" organization.
     *
     * @param id the id of the organization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organizations/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long id) {
        log.debug("REST request to get Organization : {}", id);
        Optional<Organization> organization = organizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organization);
    }

    /**
     * {@code DELETE  /organizations/:id} : delete the "id" organization.
     *
     * @param id the id of the organization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organizations/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        log.debug("REST request to delete Organization : {}", id);
        organizationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/organizations?query=:query} : search for the organization corresponding
     * to the query.
     *
     * @param query the query of the organization search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/organizations")
    public ResponseEntity<List<Organization>> searchOrganizations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Organizations for query {}", query);
        Page<Organization> page = organizationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @GetMapping("/_index/organizations")
    public ResponseEntity<Void> indexOrganizations(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Organizations");
        long resultCount = organizationRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            organizationService.doIndex(i, pageSize, fromDate, toDate);
        }
        organizationSearchRepository.refresh();
        return new ResponseEntity("Successfully organization indexed" , new HttpHeaders(), HttpStatus.OK);    }

    @PostMapping("/_import/organization-masters")
    public ResponseEntity<List<Organization>> importOrganization(@RequestBody MultipartFile file) throws Exception {
        log.debug("REST request to import packageMaster ");
        List<Organization> result = organizationService.uploadOrganizationData(file);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }
    @GetMapping("/_search/all-sponsors")
    public ResponseEntity<List<SponsorDto>> searchAllSponsors(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sponsors for query {}", query);
        List<SponsorDto> sponsorDTOList=new ArrayList<>();
        List<Department> departmentList = departmentService.search(query);
        departmentList.forEach(department -> {
            SponsorDto sponsorDTO = objectMapper.convertValue(department, SponsorDto.class);
            sponsorDTOList.add(sponsorDTO);
        });
        Page<Organization> page = organizationService.searchForAllSponsors(query, PageRequest.of(0,99));
        List<Organization> content = page.getContent();
        content.forEach(organization -> {
            SponsorDto sponsorDTO = objectMapper.convertValue(organization, SponsorDto.class);
            sponsorDTOList.add(sponsorDTO);
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(sponsorDTOList);
    }
    @GetMapping("/_export/unit")
    public Map<String, String> exportUnit(@RequestParam String query, Pageable pageable) throws Exception {
        log.debug("REST request to Export for a Organization for query {}", query,pageable);
        return organizationService.exportUnit(query, pageable);
    }
}
