package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ServiceItemBenefitTemplate;
import org.nh.artha.repository.ServiceItemBenefitTemplateRepository;
import org.nh.artha.repository.search.ServiceItemBenefitTemplateSearchRepository;
import org.nh.artha.service.ServiceItemBenefitTemplateService;
import org.nh.artha.service.impl.ArthaSequenceGeneratorService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.ServiceItemBenefitTemplate}.
 */
@RestController
@RequestMapping("/api")
public class ServiceItemBenefitTemplateResource {

    private final Logger log = LoggerFactory.getLogger(ServiceItemBenefitTemplateResource.class);

    private static final String ENTITY_NAME = "serviceItemBenefitTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceItemBenefitTemplateService serviceItemBenefitTemplateService;

    private final ServiceItemBenefitTemplateRepository serviceItemBenefitTemplateRepository;

    private final ServiceItemBenefitTemplateSearchRepository serviceItemBenefitTemplateSearchRepository;

    private final ApplicationProperties applicationProperties;

    private final ArthaSequenceGeneratorService sequenceGeneratorService;

    public ServiceItemBenefitTemplateResource(ServiceItemBenefitTemplateService serviceItemBenefitTemplateService,
                                              ServiceItemBenefitTemplateRepository serviceItemBenefitTemplateRepository,
                                              ServiceItemBenefitTemplateSearchRepository serviceItemBenefitTemplateSearchRepository,
                                              ApplicationProperties applicationProperties,ArthaSequenceGeneratorService sequenceGeneratorService) {
        this.serviceItemBenefitTemplateService = serviceItemBenefitTemplateService;
        this.serviceItemBenefitTemplateRepository=serviceItemBenefitTemplateRepository;
        this.serviceItemBenefitTemplateSearchRepository= serviceItemBenefitTemplateSearchRepository;
        this.applicationProperties = applicationProperties;
        this.sequenceGeneratorService=sequenceGeneratorService;
    }

    /**
     * {@code POST  /service-item-benefit-templates} : Create a new serviceItemBenefitTemplate.
     *
     * @param serviceItemBenefitTemplate the serviceItemBenefitTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceItemBenefitTemplate, or with status {@code 400 (Bad Request)} if the serviceItemBenefitTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-item-benefit-templates")
    public ResponseEntity<ServiceItemBenefitTemplate> createServiceItemBenefitTemplate(@RequestBody ServiceItemBenefitTemplate serviceItemBenefitTemplate) throws URISyntaxException {
        log.debug("REST request to save ServiceItemBenefitTemplate : {}", serviceItemBenefitTemplate);
        if (serviceItemBenefitTemplate.getId() != null) {
            throw new BadRequestAlertException("A new serviceItemBenefitTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String templateCode = "";
            //sequenceGeneratorService.generateNumber("templateCode", "NH", serviceItemBenefitTemplate);
        serviceItemBenefitTemplate.setCode(templateCode);
        ServiceItemBenefitTemplate result = serviceItemBenefitTemplateService.save(serviceItemBenefitTemplate);
        return ResponseEntity.created(new URI("/api/service-item-benefit-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-item-benefit-templates} : Updates an existing serviceItemBenefitTemplate.
     *
     * @param serviceItemBenefitTemplate the serviceItemBenefitTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceItemBenefitTemplate,
     * or with status {@code 400 (Bad Request)} if the serviceItemBenefitTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceItemBenefitTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-item-benefit-templates")
    public ResponseEntity<ServiceItemBenefitTemplate> updateServiceItemBenefitTemplate(@RequestBody ServiceItemBenefitTemplate serviceItemBenefitTemplate) throws URISyntaxException {
        log.debug("REST request to update ServiceItemBenefitTemplate : {}", serviceItemBenefitTemplate);
        if (serviceItemBenefitTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceItemBenefitTemplate result = serviceItemBenefitTemplateService.save(serviceItemBenefitTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceItemBenefitTemplate.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /service-item-benefit-templates} : get all the serviceItemBenefitTemplates.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceItemBenefitTemplates in body.
     */
    @GetMapping("/service-item-benefit-templates")
    public ResponseEntity<List<ServiceItemBenefitTemplate>> getAllServiceItemBenefitTemplates(Pageable pageable) {
        log.debug("REST request to get a page of ServiceItemBenefitTemplates");
        Page<ServiceItemBenefitTemplate> page = serviceItemBenefitTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-item-benefit-templates/:id} : get the "id" serviceItemBenefitTemplate.
     *
     * @param id the id of the serviceItemBenefitTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceItemBenefitTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-item-benefit-templates/{id}")
    public ResponseEntity<ServiceItemBenefitTemplate> getServiceItemBenefitTemplate(@PathVariable Long id) {
        log.debug("REST request to get ServiceItemBenefitTemplate : {}", id);
        Optional<ServiceItemBenefitTemplate> serviceItemBenefitTemplate = serviceItemBenefitTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceItemBenefitTemplate);
    }

    /**
     * {@code DELETE  /service-item-benefit-templates/:id} : delete the "id" serviceItemBenefitTemplate.
     *
     * @param id the id of the serviceItemBenefitTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-item-benefit-templates/{id}")
    public ResponseEntity<Void> deleteServiceItemBenefitTemplate(@PathVariable Long id) {
        log.debug("REST request to delete ServiceItemBenefitTemplate : {}", id);
        serviceItemBenefitTemplateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/service-item-benefit-templates?query=:query} : search for the serviceItemBenefitTemplate corresponding
     * to the query.
     *
     * @param query the query of the serviceItemBenefitTemplate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/service-item-benefit-templates")
    public ResponseEntity<List<ServiceItemBenefitTemplate>> searchServiceItemBenefitTemplates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceItemBenefitTemplates for query {}", query);
        Page<ServiceItemBenefitTemplate> page = serviceItemBenefitTemplateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/service-item-benefit-templates")
    public ResponseEntity<Void> indexServiceItemBenefitTemplate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on ServiceItemBenefitTemplate");
        long resultCount = serviceItemBenefitTemplateRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            serviceItemBenefitTemplateService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "ServiceItemBenefitTemplate Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }

    @DeleteMapping("template/deletebyQuery")
    public void  deleteAllRecord(@RequestParam(value = "query") String query){
        Iterable<ServiceItemBenefitTemplate> search = serviceItemBenefitTemplateService.search(query, PageRequest.of(0,99999));
        List<ServiceItemBenefitTemplate> collect = StreamSupport.stream(search.spliterator(), false)
            .collect(Collectors.toList());
        for(int i=0;i<collect.size();i++){
            serviceItemBenefitTemplateService.delete(collect.get(i).getId());
        }
    }

    @GetMapping("template/export/unit-mapping")
    public ResponseEntity<Map<String,String>> exportUnitMapping(@RequestParam("query") String query,Pageable pageable) throws IOException {
       Map<String,String> exportDetailMap= serviceItemBenefitTemplateService.export(query,pageable);
       return ResponseEntity.ok().body(exportDetailMap);
    }
}
