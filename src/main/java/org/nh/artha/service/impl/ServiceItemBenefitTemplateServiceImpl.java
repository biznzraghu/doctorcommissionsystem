package org.nh.artha.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.elasticsearch.index.query.Operator;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.*;
import org.nh.artha.service.ServiceItemBenefitService;
import org.nh.artha.service.ServiceItemBenefitTemplateService;
import org.nh.artha.repository.ServiceItemBenefitTemplateRepository;
import org.nh.artha.repository.search.ServiceItemBenefitTemplateSearchRepository;
import org.nh.artha.service.ServiceItemExceptionService;
import org.nh.artha.util.ExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ServiceItemBenefitTemplate}.
 */
@Service
@Transactional
public class ServiceItemBenefitTemplateServiceImpl implements ServiceItemBenefitTemplateService {

    private final Logger log = LoggerFactory.getLogger(ServiceItemBenefitTemplateServiceImpl.class);

    private final ServiceItemBenefitTemplateRepository serviceItemBenefitTemplateRepository;

    private final ServiceItemBenefitTemplateSearchRepository serviceItemBenefitTemplateSearchRepository;

    private final ServiceItemBenefitService serviceItemBenefitService;

    private final ServiceItemExceptionService serviceItemExceptionService;

    private final ApplicationProperties applicationProperties;

    public ServiceItemBenefitTemplateServiceImpl(ServiceItemBenefitTemplateRepository serviceItemBenefitTemplateRepository, ServiceItemBenefitTemplateSearchRepository serviceItemBenefitTemplateSearchRepository,
                                                 ServiceItemBenefitService serviceItemBenefitService,ServiceItemExceptionService serviceItemExceptionService,
                                                 ApplicationProperties applicationProperties) {
        this.serviceItemBenefitTemplateRepository = serviceItemBenefitTemplateRepository;
        this.serviceItemBenefitTemplateSearchRepository = serviceItemBenefitTemplateSearchRepository;
        this.serviceItemBenefitService=serviceItemBenefitService;
        this.serviceItemExceptionService=serviceItemExceptionService;
        this.applicationProperties=applicationProperties;
    }

    /**
     * Save a serviceItemBenefitTemplate.
     *
     * @param serviceItemBenefitTemplate the entity to save.
     * @return the persisted entity.
     */
    @Override
    @Transactional
    public ServiceItemBenefitTemplate save(ServiceItemBenefitTemplate serviceItemBenefitTemplate) {
        log.debug("Request to save ServiceItemBenefitTemplate : {}", serviceItemBenefitTemplate);
        serviceItemBenefitTemplate.setServiceItemExceptions(serviceItemBenefitTemplate.getServiceItemExceptions().stream().map(serviceItemBenefitTemplateException->serviceItemBenefitTemplateException.serviceItemBenefitTemplate(serviceItemBenefitTemplate)).collect(Collectors.toSet()));
        ServiceItemBenefitTemplate result = serviceItemBenefitTemplateRepository.save(serviceItemBenefitTemplate);
        serviceItemBenefitTemplateSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the serviceItemBenefitTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceItemBenefitTemplate> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceItemBenefitTemplates");
        return serviceItemBenefitTemplateRepository.findAll(pageable);
    }

    /**
     * Get all the serviceItemBenefitTemplates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ServiceItemBenefitTemplate> findAllWithEagerRelationships(Pageable pageable) {
        return serviceItemBenefitTemplateRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one serviceItemBenefitTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceItemBenefitTemplate> findOne(Long id) {
        log.debug("Request to get ServiceItemBenefitTemplate : {}", id);
        return serviceItemBenefitTemplateSearchRepository.findById(id);
    }

    /**
     * Delete the serviceItemBenefitTemplate by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceItemBenefitTemplate : {}", id);
        serviceItemBenefitTemplateRepository.deleteById(id);
        serviceItemBenefitTemplateSearchRepository.deleteById(id);
    }

    /**
     * Search for the serviceItemBenefitTemplate corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceItemBenefitTemplate> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceItemBenefitTemplates for query {}", query);
        return serviceItemBenefitTemplateSearchRepository.search(queryStringQuery(query).defaultOperator(Operator.AND), pageable);
    }

    @Override
    public List<ServiceItemBenefitTemplate> saveAll(List<ServiceItemBenefitTemplate> serviceItemBenefitTemplate) {
        List<ServiceItemBenefitTemplate> serviceItemBenefitTemplates = serviceItemBenefitTemplateRepository.saveAll(serviceItemBenefitTemplate);
        return serviceItemBenefitTemplates;
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on ServiceItemBenefitTemplate");
        List<ServiceItemBenefitTemplate> data = serviceItemBenefitTemplateRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            serviceItemBenefitTemplateSearchRepository.saveAll(data);
        }

    }

    @Override
    public Map<String, String> export(String query, Pageable pageable) throws IOException {
        Map<String , String> fileDetails= new HashMap<>();
        long totalPages = this.search(query,pageable).getTotalPages();
        File file = ExportUtil.getCSVExportFile("unit_mapping_export", applicationProperties.getAthmaBucket().getTempExport());
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");
        FileWriter variablePayoutFileWriter = new FileWriter(file);
        final String[] variablePayoutFileHeader = {"Template Name","Organization Name", "Organization Code","Status"};
        CSVFormat varaiblePayoutFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(variablePayoutFileWriter, varaiblePayoutFileFormat)) {
            csvFilePrinter.printRecord(variablePayoutFileHeader);
            for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
                Iterator<ServiceItemBenefitTemplate> iterator = search(query, pageable).iterator();
                while (iterator.hasNext()) {
                    ServiceItemBenefitTemplate next = iterator.next();
                    List<Organization> organizationList = next.getOrganization();
                    if (organizationList != null && !organizationList.isEmpty()) {
                        List templateOrganizationMapping = new ArrayList();
                        for (int i = 0; i < organizationList.size(); i++) {
                            Organization organization = organizationList.get(i);
                            templateOrganizationMapping.add(next.getTemplateName());
                            templateOrganizationMapping.add(organization.getName());
                            templateOrganizationMapping.add(organization.getCode());
                            templateOrganizationMapping.add(organization.isActive());
                            csvFilePrinter.printRecord(templateOrganizationMapping);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        } finally {
            if (variablePayoutFileWriter != null)
                variablePayoutFileWriter.close();
        }
        return fileDetails;
    }
}
