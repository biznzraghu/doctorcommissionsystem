package org.nh.artha.service.impl;


import liquibase.util.csv.CSVReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.elasticsearch.index.query.Operator;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Organization;
import org.nh.artha.repository.OrganizationRepository;
import org.nh.artha.repository.search.OrganizationSearchRepository;
import org.nh.artha.service.DepartmentService;
import org.nh.artha.service.OrganizationService;
import org.nh.artha.util.ExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Organization}.
 */
@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationSearchRepository organizationSearchRepository;

    private final DepartmentService departmentSevice;

    private final ApplicationProperties applicationProperties;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationSearchRepository organizationSearchRepository,DepartmentService departmentSevice,
                                   ApplicationProperties applicationProperties) {
        this.organizationRepository = organizationRepository;
        this.organizationSearchRepository = organizationSearchRepository;
        this.departmentSevice=departmentSevice;
        this.applicationProperties=applicationProperties;
    }

    /**
     * Save a organization.
     *
     * @param organization the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Organization save(Organization organization) {
        log.debug("Request to save Organization : {}", organization);
        Organization result = organizationRepository.save(organization);
        organizationSearchRepository.save(result);
        return result;
    }
    public List<Organization> saveAll(List<Organization> organization) {
        log.debug("Request to save Organization : {}", organization);
        List<Organization> result = organizationRepository.saveAll(organization);
        organizationSearchRepository.saveAll(result);
        return result;
    }

    /**
     * Get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Organization> findAll(Pageable pageable) {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll(pageable);
    }


    /**
     * Get one organization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id);
    }

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
        organizationSearchRepository.deleteById(id);
    }

    /**
     * Search for the organization corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Organization> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Organizations for query {}", query);
        return organizationSearchRepository.search(queryStringQuery(query).field("name").field("active").field("code")
            .defaultOperator(Operator.AND), pageable);    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Organization");

        List<Organization> data = organizationRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            organizationSearchRepository.saveAll(data);
        }

    }
    @Override
    public List<Organization> uploadOrganizationData(MultipartFile file) throws Exception {
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader=new CSVReader(reader);
        List<String[]> organizationArray = csvReader.readAll();
        organizationArray.remove(0);
        /*List<Organization> organizationList = organizationArray.stream().map(createOrganizationFromCsv).distinct().collect(Collectors.toList());
        List<Organization> organizationSavedList = this.saveAll(organizationList);*/
        departmentSevice.uploadDepartmentData(organizationArray);
        return null;
    }
    private Function<String[] , Organization> createOrganizationFromCsv = (String [] csvRowArray) -> {
        Organization organization=new Organization();
        try {
         organization.setCode(csvRowArray[0]);
         organization.setName(csvRowArray[1]);
         organization.setActive(csvRowArray[5].equalsIgnoreCase("Active")?true:false);
        } catch (Exception e) {
            System.out.println(Arrays.toString(csvRowArray));
            throw new RuntimeException(e);
        }
        return organization;
    };

    @Override
    @Transactional(readOnly = true)
    public Page<Organization> searchForAllSponsors(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sponsors for query {}", query);
        return organizationSearchRepository.search(queryStringQuery(query).field("name"), pageable);
    }

    @Override
    public Map<String, String> exportUnit(String query, Pageable pageable) throws IOException {
        log.debug("REST request to Export for a Organization for query {}", query);
        long totalPages = this.search(query, PageRequest.of(0, 200, pageable.getSort())).getTotalPages();
        File file = ExportUtil.getCSVExportFile("unit", applicationProperties.getAthmaBucket().getTempExport());
        FileWriter invoiceFileWriter = new FileWriter(file);
        Map<String, String> fileDetails = new HashMap<>();
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");
        final String[] organizationHeader = {"Name", "Code","Status"};
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(invoiceFileWriter, csvFileFormat)) {
            csvFilePrinter.printRecord(organizationHeader);
            for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
                Iterator<Organization> iterator = search(query, PageRequest.of(pageIndex, 2000, pageable.getSort())).iterator();
                while (iterator.hasNext()) {
                    Organization organization = iterator.next();
                    List organizationList=new ArrayList();
                    organizationList.add(organization.getName());
                    organizationList.add(organization.getCode());
                    organizationList.add(organization.isActive());
                    csvFilePrinter.printRecord(organizationList);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        } finally {
            if (invoiceFileWriter != null)
                invoiceFileWriter.close();
        }
        return fileDetails;

    }
}
