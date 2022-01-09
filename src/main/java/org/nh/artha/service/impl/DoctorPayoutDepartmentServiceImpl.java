package org.nh.artha.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.ApplicableInvoices;
import org.nh.artha.domain.enumeration.NetGross;
import org.nh.artha.repository.DoctorPayoutRepository;
import org.nh.artha.repository.UserMasterRepository;
import org.nh.artha.service.DoctorPayoutDepartmentService;
import org.nh.artha.repository.DoctorPayoutDepartmentRepository;
import org.nh.artha.repository.search.DoctorPayoutDepartmentSearchRepository;
import org.nh.artha.util.ExportUtil;
import org.nh.artha.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link DoctorPayoutDepartment}.
 */
@Service
@Transactional
public class DoctorPayoutDepartmentServiceImpl implements DoctorPayoutDepartmentService {

    private final Logger log = LoggerFactory.getLogger(DoctorPayoutDepartmentServiceImpl.class);

    private final DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository;

    private final DoctorPayoutDepartmentSearchRepository doctorPayoutDepartmentSearchRepository;

    private  final ApplicationProperties applicationProperties;

    private final DoctorPayoutRepository doctorPayoutRepository;

    private final UserMasterRepository userMasterRepository;

    public DoctorPayoutDepartmentServiceImpl(DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository, DoctorPayoutDepartmentSearchRepository doctorPayoutDepartmentSearchRepository,ApplicationProperties applicationProperties,DoctorPayoutRepository doctorPayoutRepository,UserMasterRepository userMasterRepository) {
        this.doctorPayoutDepartmentRepository = doctorPayoutDepartmentRepository;
        this.doctorPayoutDepartmentSearchRepository = doctorPayoutDepartmentSearchRepository;
        this.applicationProperties=applicationProperties;
        this.doctorPayoutRepository=doctorPayoutRepository;
        this.userMasterRepository=userMasterRepository;
    }

    /**
     * Save a doctorPayoutDepartment.
     *
     * @param doctorPayoutDepartment the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DoctorPayoutDepartment save(DoctorPayoutDepartment doctorPayoutDepartment) {
        log.debug("Request to save DoctorPayoutDepartment : {}", doctorPayoutDepartment);
        DoctorPayoutDepartment result = doctorPayoutDepartmentRepository.save(doctorPayoutDepartment);
        doctorPayoutDepartmentSearchRepository.save(result);
        return result;
    }
    @Override
    @Transactional
    public List<DoctorPayoutDepartment> saveAll(List<DoctorPayoutDepartment> doctorPayoutDepartments) {
        List<DoctorPayoutDepartment> doctorPayoutDepartmentList = doctorPayoutDepartmentRepository.saveAll(doctorPayoutDepartments);
        doctorPayoutDepartmentSearchRepository.saveAll(doctorPayoutDepartmentList);
        return doctorPayoutDepartmentList;
    }
    /**
     * Get all the doctorPayoutDepartments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorPayoutDepartment> findAll(Pageable pageable) {
        log.debug("Request to get all DoctorPayoutDepartments");
        return doctorPayoutDepartmentRepository.findAll(pageable);
    }


    /**
     * Get one doctorPayoutDepartment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorPayoutDepartment> findOne(Long id) {
        log.debug("Request to get DoctorPayoutDepartment : {}", id);
        return doctorPayoutDepartmentRepository.findById(id);
    }

    /**
     * Delete the doctorPayoutDepartment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DoctorPayoutDepartment : {}", id);
        doctorPayoutDepartmentRepository.deleteById(id);
        doctorPayoutDepartmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctorPayoutDepartment corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorPayoutDepartment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoctorPayoutDepartments for query {}", query);
        return doctorPayoutDepartmentSearchRepository.search(queryStringQuery(query), pageable);    }

    @Override
    public Map<String, String> exportEmployeeWiseSummary(String unitCode, Integer year, Integer month, Integer departmentId) throws Exception{
      //  log.debug("REST request to export to csv for Applicable Consultant for query {}", query);
      //  long totalPages = this.search(query, PageRequest.of(0, 200, pageable.getSort())).getTotalPages();
      //  log.debug("Total page count:{} ", totalPages);
        File file = ExportUtil.getCSVExportFile("employee_wise_summary", applicationProperties.getAthmaBucket().getTempExport());
        FileWriter invoiceFileWriter = new FileWriter(file);
        Map<String, String> fileDetails = new HashMap<>();
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");

        final String[] invoiceFileHeader = {"Employee","Department", "Revenue"};

        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(invoiceFileWriter, csvFileFormat)) {
            csvFilePrinter.printRecord(invoiceFileHeader);
            /*for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {*/
                /*List<DoctorPayoutDepartment> doctorPayoutDepartmentList = search(query, PageRequest.of(pageIndex, 2000, pageable.getSort())).getContent();
                if (doctorPayoutDepartmentList.size()<=0){
                    throw new CustomParameterizedException("10395","Department Revenue Is Not Available For The Selected Department");
                }*/
                Map<DoctorPayout, List<DoctorPayoutDepartment>> doctorDepartmentPayoutList;
                if (null!=departmentId) {
                    doctorDepartmentPayoutList = doctorPayoutDepartmentRepository.getDepartmentPayoutByYearMonthUnitAndDepartIdGroupBy(unitCode, year, month, departmentId).stream().collect(Collectors.groupingBy(DoctorPayoutDepartment::getDoctorPayout));
                }
                else {
                    doctorDepartmentPayoutList = doctorPayoutDepartmentRepository.getDepartmentPayoutByYearMonthANDUnitGroupBy(unitCode, year, month).stream().collect(Collectors.groupingBy(DoctorPayoutDepartment::getDoctorPayout));
                }if (doctorDepartmentPayoutList.size()<=0){
                    throw new CustomParameterizedException("10395","Department Revenue Is Not Available For The Selected Department");
                }
               // Map<Department, List<DoctorPayoutDepartment>> collect = doctorDepartmentPayoutList.stream().collect(Collectors.groupingBy(DoctorPayoutDepartment::getDepartment));
                for (Map.Entry<DoctorPayout, List<DoctorPayoutDepartment>> entry : doctorDepartmentPayoutList.entrySet()) {
                    DoctorPayout doctorPayout = entry.getKey();
                    UserMaster userMaster = userMasterRepository.findById(doctorPayout.getEmployeeId()).orElseThrow(() -> {
                        return  new CustomParameterizedException("1011", "No Employee Found");
                    });
                    List<DoctorPayoutDepartment> doctorPayoutDepartments = entry.getValue();
                    List departmentRevenue = new ArrayList();
                    double totalDepartmentRevenue = 0;
                    String formattedDoctorString="";
                    for (DoctorPayoutDepartment doctorPayoutDepartment : doctorPayoutDepartments) {
                        totalDepartmentRevenue = totalDepartmentRevenue + doctorPayoutDepartment.getAmount();
                        formattedDoctorString = doctorPayoutDepartments.parallelStream().map(doctorPayoutDepartment1 -> {
                            if (userMaster != null) {
                                return doctorPayoutDepartment1.getDepartment().getName();
                            } else {
                                return "";
                            }
                        }).reduce(String::concat).get();
                    }
                        departmentRevenue.add(userMaster.getDisplayName());
                        departmentRevenue.add(formattedDoctorString);
                        departmentRevenue.add(totalDepartmentRevenue);
                        csvFilePrinter.printRecord(departmentRevenue);
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
    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Doctor Payout");
        List<DoctorPayoutDepartment> data = doctorPayoutDepartmentRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            data.forEach(doctorPayout -> {
                doctorPayoutDepartmentSearchRepository.indexWithoutRefresh(doctorPayout);
            });
        }
    }
}
