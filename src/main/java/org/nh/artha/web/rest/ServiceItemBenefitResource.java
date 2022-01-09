package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ServiceItemBenefit;
import org.nh.artha.domain.ServiceItemBenefitTemplate;
import org.nh.artha.domain.VariablePayout;
import org.nh.artha.repository.ServiceItemBenefitRepository;
import org.nh.artha.repository.search.ServiceItemBenefitSearchRepository;
import org.nh.artha.service.ServiceItemBenefitService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.ServiceItemBenefit}.
 */
@RestController
@RequestMapping("/api")
public class ServiceItemBenefitResource {

    private final Logger log = LoggerFactory.getLogger(ServiceItemBenefitResource.class);

    private static final String ENTITY_NAME = "serviceItemBenefit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceItemBenefitService serviceItemBenefitService;

    private final ServiceItemBenefitRepository serviceItemBenefitRepository;

    private final ServiceItemBenefitSearchRepository serviceItemBenefitSearchRepository;

    private final ApplicationProperties applicationProperties;

    public ServiceItemBenefitResource(ServiceItemBenefitService serviceItemBenefitService, ServiceItemBenefitRepository serviceItemBenefitRepository,
                                      ServiceItemBenefitSearchRepository serviceItemBenefitSearchRepository,ApplicationProperties applicationProperties) {
        this.serviceItemBenefitService = serviceItemBenefitService;
        this.serviceItemBenefitRepository=serviceItemBenefitRepository;
        this.serviceItemBenefitSearchRepository=serviceItemBenefitSearchRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /service-item-benefits} : Create a new serviceItemBenefit.
     *
     * @param serviceItemBenefit the serviceItemBenefit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceItemBenefit, or with status {@code 400 (Bad Request)} if the serviceItemBenefit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-item-benefits")
    public ResponseEntity<ServiceItemBenefit> createServiceItemBenefit(@Valid @RequestBody ServiceItemBenefit serviceItemBenefit) throws URISyntaxException {
        log.debug("REST request to save ServiceItemBenefit : {}", serviceItemBenefit);
        if (serviceItemBenefit.getId() != null) {
            throw new BadRequestAlertException("A new serviceItemBenefit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceItemBenefit result = serviceItemBenefitService.createNewVersion(serviceItemBenefit);
        return ResponseEntity.created(new URI("/api/service-item-benefits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-item-benefits} : Updates an existing serviceItemBenefit.
     *
     * @param serviceItemBenefit the serviceItemBenefit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceItemBenefit,
     * or with status {@code 400 (Bad Request)} if the serviceItemBenefit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceItemBenefit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-item-benefits")
    public ResponseEntity<ServiceItemBenefit> updateServiceItemBenefit(@Valid @RequestBody ServiceItemBenefit serviceItemBenefit) throws URISyntaxException {
        log.debug("REST request to update ServiceItemBenefit : {}", serviceItemBenefit);
        if (serviceItemBenefit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceItemBenefit result = serviceItemBenefitService.createNewVersion(serviceItemBenefit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceItemBenefit.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /service-item-benefits} : get all the serviceItemBenefits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceItemBenefits in body.
     */
    @GetMapping("/service-item-benefits")
    public ResponseEntity<List<ServiceItemBenefit>> getAllServiceItemBenefits(Pageable pageable) {
        log.debug("REST request to get a page of ServiceItemBenefits");
        Page<ServiceItemBenefit> page = serviceItemBenefitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-item-benefits/:id} : get the "id" serviceItemBenefit.
     *
     * @param id the id of the serviceItemBenefit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceItemBenefit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-item-benefits/{id}")
    public ResponseEntity<ServiceItemBenefit> getServiceItemBenefit(@PathVariable Long id) {
        log.debug("REST request to get ServiceItemBenefit : {}", id);
        Optional<ServiceItemBenefit> serviceItemBenefit = serviceItemBenefitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceItemBenefit);
    }

    /**
     * {@code DELETE  /service-item-benefits/:id} : delete the "id" serviceItemBenefit.
     *
     * @param id the id of the serviceItemBenefit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-item-benefits/{id}")
    public ResponseEntity<Void> deleteServiceItemBenefit(@PathVariable Long id) {
        log.debug("REST request to delete ServiceItemBenefit : {}", id);
        serviceItemBenefitService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/service-item-benefits?query=:query} : search for the serviceItemBenefit corresponding
     * to the query.
     *
     * @param query the query of the serviceItemBenefit search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/service-item-benefits")
    public ResponseEntity<List<ServiceItemBenefit>> searchServiceItemBenefits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceItemBenefits for query {}", query);
        Page<ServiceItemBenefit> page = serviceItemBenefitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/service-item-benefits")
    public ResponseEntity<Void> doIndex(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate){
        log.debug("REST request to do elastic index on service-item-benefits");
        long resultCount = serviceItemBenefitRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            serviceItemBenefitService.doIndex(i, pageSize,fromDate,toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("ARTHA", "Elastic index is completed",fromDate.toString()+" , "+toDate.toString())).build();
    }

    @GetMapping("/service-item-benefits-by-variablePayout")
    public ResponseEntity<List<ServiceItemBenefit>> getServiceItemBenefietByVersion(@RequestParam("variablePayoutId") Long variablePayoutd,@RequestParam("version") Integer version,@RequestParam(value = "isApproved",defaultValue = "false",required = false) Boolean isApporved, Pageable pageable) {
        List<ServiceItemBenefit> page = serviceItemBenefitService.getServices(variablePayoutd,version,isApporved, pageable);
        PageImpl<ServiceItemBenefit> serviceItemBenefits = new PageImpl<>(page, pageable, page.size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), serviceItemBenefits);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @DeleteMapping("rules/deletebyQuery")
    public void  deleteAllRecord(@RequestParam(value = "query") String query){
        Iterable<ServiceItemBenefit> search = serviceItemBenefitService.search(query, PageRequest.of(0,99999));
        List<ServiceItemBenefit> collect = StreamSupport.stream(search.spliterator(), false)
            .collect(Collectors.toList());
        for(int i=0;i<collect.size();i++){
            serviceItemBenefitService.delete(collect.get(i).getId());
        }
    }

    @PostMapping("variablePayout/copyRules")
    public List<ServiceItemBenefit> copyRules(@Valid @RequestBody VariablePayout variablePayout,@RequestParam(value = "isApproved",defaultValue = "false",required = false) Boolean isApproved,
                                        @RequestParam(value = "toEmployee") Long employeeId,@RequestParam("version") Long version) {
        log.debug("REST request to copy rules",variablePayout,employeeId );
        Long variablePayoutId = variablePayout.getId();
        List<ServiceItemBenefit> serviceItemBenefits = serviceItemBenefitService.copyRule(variablePayoutId, employeeId, isApproved, version);
        return serviceItemBenefits;
    }

    @PostMapping("template/copyRules")
    public List<ServiceItemBenefit> copyRulesFromTemplate(@Valid @RequestBody ServiceItemBenefitTemplate fromTemplate,
                                              @RequestParam(value = "toTemplate") Long templateId,@RequestParam(value = "remove",required = false,defaultValue = "false") Boolean isRemove) {
        log.debug("REST request to copy rules from template",fromTemplate,templateId );
        List<ServiceItemBenefit> serviceItemBenefits = serviceItemBenefitService.copyRuleFromTemplate(fromTemplate,templateId,isRemove);
        return serviceItemBenefits;
    }

}
