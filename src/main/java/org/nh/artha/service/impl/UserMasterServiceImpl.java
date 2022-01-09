package org.nh.artha.service.impl;

import liquibase.util.csv.CSVReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.elasticsearch.index.query.Operator;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.VariablePayout;
import org.nh.artha.domain.enumeration.Status;
import org.nh.artha.service.UserMasterService;
import org.nh.artha.domain.UserMaster;
import org.nh.artha.repository.UserMasterRepository;
import org.nh.artha.repository.search.UserMasterSearchRepository;
import org.nh.artha.util.ExportUtil;
import org.postgresql.util.LruCache;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link UserMaster}.
 */
@Service
@Transactional
public class UserMasterServiceImpl implements UserMasterService {

    private final Logger log = LoggerFactory.getLogger(UserMasterServiceImpl.class);

    private final UserMasterRepository userMasterRepository;

    private final UserMasterSearchRepository userMasterSearchRepository;

    private final ApplicationProperties applicationProperties;

    public UserMasterServiceImpl(UserMasterRepository userMasterRepository, UserMasterSearchRepository userMasterSearchRepository,ApplicationProperties applicationProperties) {
        this.userMasterRepository = userMasterRepository;
        this.userMasterSearchRepository = userMasterSearchRepository;
        this.applicationProperties =applicationProperties;
    }

    /**
     * Save a userMaster.
     *
     * @param userMaster the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserMaster save(UserMaster userMaster) {
        log.debug("Request to save UserMaster : {}", userMaster);
        UserMaster result = userMasterRepository.save(userMaster);
        userMasterSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userMasters.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserMaster> findAll() {
        log.debug("Request to get all UserMasters");
        return userMasterRepository.findAll();
    }


    /**
     * Get one userMaster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserMaster> findOne(Long id) {
        log.debug("Request to get UserMaster : {}", id);
        return userMasterRepository.findById(id);
    }

    /**
     * Delete the userMaster by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserMaster : {}", id);
        userMasterRepository.deleteById(id);
        userMasterSearchRepository.deleteById(id);
    }

    /**
     * Search for the userMaster corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserMaster> search(String query) {
        log.debug("Request to search UserMasters for query {}", query);
        return StreamSupport
            .stream(userMasterSearchRepository.search(queryStringQuery(query).field("firstName").field("employeeNumber").field("displayName").field("lastName").field("status").defaultOperator(Operator.AND)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public Page<UserMaster> search(String query, Pageable pageable) {
        return userMasterSearchRepository.search(queryStringQuery(query),pageable);
    }


    /**
     * Do elastic index for city.
     */
    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on City");
        List<UserMaster> data = userMasterRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            userMasterSearchRepository.saveAll(data);
        }
    }

    @Override
    public void importUser(MultipartFile file) throws  Exception{
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> importedUserArray = csvReader.readAll();
        importedUserArray.remove(0);
        List<UserMaster> userMasterList = importedUserArray.stream().map(csvRowArray -> {
            UserMaster userMaster = new UserMaster();
            userMaster.employeeNumber(csvRowArray[4])
                .displayName(csvRowArray[1]).
                firstName(csvRowArray[2]).lastName(csvRowArray[3]).
                status(csvRowArray[7].equalsIgnoreCase("Active") ? Status.ACTIVE : Status.INACTIVE);
            return userMaster;

        }).collect(Collectors.toList());
        this.saveAll(userMasterList);
    }

    @Override
    public List<UserMaster> saveAll(List<UserMaster> userMasterList) {
        List<UserMaster> result = userMasterRepository.saveAll(userMasterList);
        userMasterSearchRepository.saveAll(result);
        return result;
    }

    @Override
    public Map<String, String> exportUserMaster(String query, Pageable pageable) throws IOException {
        Map<String , String> fileDetails= new HashMap<>();
        long totalPages = this.search(query,pageable).getTotalPages();
        File file = ExportUtil.getCSVExportFile("user_master_export", applicationProperties.getAthmaBucket().getTempExport());
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");
        FileWriter variablePayoutFileWriter = new FileWriter(file);
        final String[] userMasterFileHeader = {"Employee Number", "Display Name","First Name","Last Name","Designation","Gender",};
        CSVFormat userMasterHeaderFile = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(variablePayoutFileWriter, userMasterHeaderFile)) {
            csvFilePrinter.printRecord(userMasterFileHeader);
            for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
                Iterator<UserMaster> iterator = search(query,pageable).getContent().listIterator();
                while (iterator.hasNext()) {
                    UserMaster userMaster = iterator.next();
                    List userMasterListPopulateList=new ArrayList();
                    userMasterListPopulateList.add(userMaster.getEmployeeNumber());
                    userMasterListPopulateList.add(userMaster.getDisplayName());
                    userMasterListPopulateList.add(userMaster.getFirstName());
                    userMasterListPopulateList.add(userMaster.getLastName());
                    userMasterListPopulateList.add(userMaster.getDesignation()!=null?"":userMaster.getDesignation());
                    userMasterListPopulateList.add(userMaster.getGender()!=null ?userMaster.getGender().name():"");
                    csvFilePrinter.printRecord(userMasterListPopulateList);
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
