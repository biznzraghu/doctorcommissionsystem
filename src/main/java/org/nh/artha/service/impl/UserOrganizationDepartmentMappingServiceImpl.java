package org.nh.artha.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.elasticsearch.index.query.Operator;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Preferences;
import org.nh.artha.domain.UserOrganizationDepartmentMapping;
import org.nh.artha.repository.PreferencesRepository;
import org.nh.artha.repository.UserOrganizationDepartmentMappingRepository;
import org.nh.artha.repository.UserRepository;
import org.nh.artha.repository.search.UserOrganizationDepartmentMappingSearchRepository;
import org.nh.artha.service.UserOrganizationDepartmentMappingService;
import org.nh.artha.util.ExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link UserOrganizationDepartmentMapping}.
 */
@Service
@Transactional
public class UserOrganizationDepartmentMappingServiceImpl implements UserOrganizationDepartmentMappingService {

    private final Logger log = LoggerFactory.getLogger(UserOrganizationDepartmentMappingServiceImpl.class);

    private final UserOrganizationDepartmentMappingRepository userOrganizationDepartmentMappingRepository;

    private final UserOrganizationDepartmentMappingSearchRepository userOrganizationDepartmentMappingSearchRepository;

    private final PreferencesRepository preferencesRepository;

    private final UserRepository userRepository;

    private  final ApplicationProperties applicationProperties;

    public UserOrganizationDepartmentMappingServiceImpl(UserOrganizationDepartmentMappingRepository userOrganizationDepartmentMappingRepository, UserOrganizationDepartmentMappingSearchRepository userOrganizationDepartmentMappingSearchRepository,
                                                        UserRepository userRepository,PreferencesRepository preferencesRepository,ApplicationProperties applicationProperties) {
        this.userOrganizationDepartmentMappingRepository = userOrganizationDepartmentMappingRepository;
        this.userOrganizationDepartmentMappingSearchRepository = userOrganizationDepartmentMappingSearchRepository;
        this.userRepository=userRepository;
        this.preferencesRepository=preferencesRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * Save a userOrganizationDepartmentMapping.
     *
     * @param userOrganizationDepartmentMapping the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserOrganizationDepartmentMapping save(UserOrganizationDepartmentMapping userOrganizationDepartmentMapping) {
        log.debug("Request to save UserOrganizationDepartmentMapping : {}", userOrganizationDepartmentMapping);
        UserOrganizationDepartmentMapping result = userOrganizationDepartmentMappingRepository.save(userOrganizationDepartmentMapping);
        userOrganizationDepartmentMappingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userOrganizationDepartmentMappings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserOrganizationDepartmentMapping> findAll(Pageable pageable) {
        log.debug("Request to get all UserOrganizationDepartmentMappings");
        return userOrganizationDepartmentMappingRepository.findAll(pageable);
    }

    /**
     * Get one userOrganizationDepartmentMapping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserOrganizationDepartmentMapping> findOne(Long id) {
        log.debug("Request to get UserOrganizationDepartmentMapping : {}", id);
        return userOrganizationDepartmentMappingRepository.findById(id);
    }

    /**
     * Delete the userOrganizationDepartmentMapping by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserOrganizationDepartmentMapping : {}", id);
        userOrganizationDepartmentMappingRepository.deleteById(id);
        userOrganizationDepartmentMappingSearchRepository.deleteById(id);
    }

    /**
     * Search for the userOrganizationDepartmentMapping corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserOrganizationDepartmentMapping> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserOrganizationDepartmentMappings for query {}", query);
        return userOrganizationDepartmentMappingSearchRepository.search(queryStringQuery(query).defaultOperator(Operator.AND), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on UserOrganizationDepartmentMapping");
        List<UserOrganizationDepartmentMapping> data = userOrganizationDepartmentMappingRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            userOrganizationDepartmentMappingSearchRepository.saveAll(data);
        }
    }

    @Override
    public List<UserOrganizationDepartmentMapping> saveAll(List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList) {
        List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappings=null;
        try {
            if(userOrganizationDepartmentMappingList!=null && !userOrganizationDepartmentMappingList.isEmpty()){
                userOrganizationDepartmentMappings = userOrganizationDepartmentMappingRepository.saveAll(userOrganizationDepartmentMappingList);
            }
            userOrganizationDepartmentMappingSearchRepository.saveAll(userOrganizationDepartmentMappings);
            return userOrganizationDepartmentMappings;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Preferences searchUserBasedOnOrganization(Long unitId) {
        log.debug("Request to searchUserBasedOnOrganization UserOrganizationMappings for query {}", unitId);
        UserOrganizationDepartmentMapping userByOrganizationAndByUserId = userOrganizationDepartmentMappingRepository.getUserByOrganization(unitId);
        Preferences preferences  = preferencesRepository.findOneByUser(userByOrganizationAndByUserId.getUserMaster().getId());
        if(preferences!=null){
            preferences.setOrganization(userByOrganizationAndByUserId.getOrganization());
        }else {
            preferences = new Preferences();
            preferences.setUser(userRepository.findById(userByOrganizationAndByUserId.getUserMaster().getId()).get());
            preferences.setOrganization(userByOrganizationAndByUserId.getOrganization());
        }
        return preferencesRepository.save(preferences);

    }
    @Override
    public Map<String, String> exportApplicableConsultant(@RequestParam String query, Pageable pageable) throws Exception{
        log.debug("REST request to export to csv for Applicable Consultant for query {}", query);
        long totalPages = this.search(query, PageRequest.of(0, 200, pageable.getSort())).getTotalPages();
        log.debug("Total page count:{} ", totalPages);
        File file = ExportUtil.getCSVExportFile("applicable_consultant", applicationProperties.getAthmaBucket().getTempExport());
        FileWriter invoiceFileWriter = new FileWriter(file);
        Map<String, String> fileDetails = new HashMap<>();
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");

        final String[] invoiceFileHeader = {"Employee Code", "Employee Name","Status"};

        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(invoiceFileWriter, csvFileFormat)) {
            csvFilePrinter.printRecord(invoiceFileHeader);
            for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
                Iterator<UserOrganizationDepartmentMapping> iterator = search(query, PageRequest.of(pageIndex, 2000, pageable.getSort())).iterator();
                while (iterator.hasNext()) {
                    UserOrganizationDepartmentMapping userOrganizationDepartmentMapping = iterator.next();

                    List applicableConsultant=new ArrayList();
                    applicableConsultant.add(userOrganizationDepartmentMapping.getUserMaster().getEmployeeNumber());
                    applicableConsultant.add(userOrganizationDepartmentMapping.getUserMaster().getDisplayName());
                    applicableConsultant.add(userOrganizationDepartmentMapping.getUserMaster().getStatus());
                    csvFilePrinter.printRecord(applicableConsultant);

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
