package org.nh.artha.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.ApplicableInvoices;
import org.nh.artha.domain.enumeration.NetGross;
import org.nh.artha.repository.DoctorPayoutDepartmentRepository;
import org.nh.artha.repository.ServiceAnalysisRepository;
import org.nh.artha.repository.search.DepartmentSearchRepository;
import org.nh.artha.service.*;
import org.nh.artha.repository.DepartmentPayoutRepository;
import org.nh.artha.repository.search.DepartmentPayoutSearchRepository;
import org.nh.artha.util.ExportUtil;
import org.nh.artha.web.rest.errors.BadRequestAlertException;
import org.nh.artha.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link DepartmentPayout}.
 */
@Service
@Transactional
public class DepartmentPayoutServiceImpl implements DepartmentPayoutService {

    private final Logger log = LoggerFactory.getLogger(DepartmentPayoutServiceImpl.class);

    private final DepartmentPayoutRepository departmentPayoutRepository;

    private final DepartmentPayoutSearchRepository departmentPayoutSearchRepository;

    private final PayoutRangeService payoutRangeService;

    private final ApplicableConsultantService applicableConsultantService;

    private final DepartmentConsumptionMaterialReductionService departmentConsumptionMaterialReductionService;

    private final HscConsumptionMaterialReductionService hscConsumptionMaterialReductionService;

    private final ServiceItemExceptionService serviceItemExceptionService;

    private final TransactionApprovalDetailsService transactionApprovalDetailsService;

    private final ElasticsearchOperations elasticsearchOperations;

    private final ArthaSequenceGeneratorService sequenceGeneratorService;

    private final DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository;

    private final ServiceAnalysisRepository serviceAnalysisRepository;

    private final DepartmentSearchRepository departmentSearchRepository;

    private final ApplicationProperties applicationProperties;

    private final RestHighLevelClient elasticsearchTemplate;


    public DepartmentPayoutServiceImpl(DepartmentPayoutRepository departmentPayoutRepository, DepartmentPayoutSearchRepository departmentPayoutSearchRepository, PayoutRangeService payoutRangeService,
                                       ApplicableConsultantService applicableConsultantService, DepartmentConsumptionMaterialReductionService departmentConsumptionMaterialReductionService,
                                       HscConsumptionMaterialReductionService hscConsumptionMaterialReductionService, ServiceItemExceptionService serviceItemExceptionService, TransactionApprovalDetailsService transactionApprovalDetailsService, ElasticsearchOperations elasticsearchOperations, ArthaSequenceGeneratorService sequenceGeneratorService,
                                       DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository,
                                       ServiceAnalysisRepository serviceAnalysisRepository,
                                       DepartmentSearchRepository departmentSearchRepository, ApplicationProperties applicationProperties,RestHighLevelClient elasticsearchTemplate) {
        this.departmentPayoutRepository = departmentPayoutRepository;
        this.departmentPayoutSearchRepository = departmentPayoutSearchRepository;
        this.payoutRangeService = payoutRangeService;
        this.applicableConsultantService = applicableConsultantService;
        this.departmentConsumptionMaterialReductionService = departmentConsumptionMaterialReductionService;
        this.hscConsumptionMaterialReductionService = hscConsumptionMaterialReductionService;
        this.serviceItemExceptionService = serviceItemExceptionService;
        this.transactionApprovalDetailsService = transactionApprovalDetailsService;
        this.elasticsearchOperations = elasticsearchOperations;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.doctorPayoutDepartmentRepository = doctorPayoutDepartmentRepository;
        this.serviceAnalysisRepository = serviceAnalysisRepository;
        this.departmentSearchRepository = departmentSearchRepository;
        this.applicationProperties = applicationProperties;
        this.elasticsearchTemplate=elasticsearchTemplate;
    }

    /**
     * Save a departmentPayout.
     *
     * @param departmentPayout the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DepartmentPayout save(DepartmentPayout departmentPayout) {
        log.debug("Request to save DepartmentPayout : {}", departmentPayout);
        if (departmentPayout.getId() == null && departmentPayout.getStartingVersion() == null) {
            String substring = "dep";
                //sequenceGeneratorService.generateNumber("DepartmentPayout", "NH", departmentPayout);
            departmentPayout.setStartingVersion(Long.parseLong(substring.substring(substring.length() - 4)));
        }
        if (departmentPayout.getPayoutRanges() != null && !departmentPayout.getPayoutRanges().isEmpty()) {
            Set<PayoutRange> payoutRanges = departmentPayout.getPayoutRanges().stream().map(payoutRange -> payoutRange.departmentPayout(departmentPayout)).collect(Collectors.toSet());
            departmentPayout.setPayoutRanges(payoutRanges);
        }
        if (departmentPayout.getApplicableConsultants() != null && !departmentPayout.getApplicableConsultants().isEmpty()) {
            Set<ApplicableConsultant> applicableConsultants = departmentPayout.getApplicableConsultants().stream().map(applicableConsultant -> applicableConsultant.departmentPayout(departmentPayout)).collect(Collectors.toSet());
            departmentPayout.setApplicableConsultants(applicableConsultants);
        }
        if (departmentPayout.getDepartmentConsumptionMaterialReductions() != null && !departmentPayout.getDepartmentConsumptionMaterialReductions().isEmpty()) {
            Set<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductions = departmentPayout.getDepartmentConsumptionMaterialReductions().stream().map(departmentConsumptionMaterialReduction -> departmentConsumptionMaterialReduction.departmentPayout(departmentPayout)).collect(Collectors.toSet());
            departmentPayout.setDepartmentConsumptionMaterialReductions(departmentConsumptionMaterialReductions);
        }
        if (departmentPayout.getHscConsumptionMaterialReductions() != null && !departmentPayout.getHscConsumptionMaterialReductions().isEmpty()) {
            Set<HscConsumptionMaterialReduction> hscConsumptionMaterialReductions = departmentPayout.getHscConsumptionMaterialReductions().stream().map(hscConsumptionMaterialReduction -> hscConsumptionMaterialReduction.departmentPayout(departmentPayout)).collect(Collectors.toSet());
            departmentPayout.setHscConsumptionMaterialReductions(hscConsumptionMaterialReductions);
        }
        if (departmentPayout.getServiceItemExceptions() != null && !departmentPayout.getServiceItemExceptions().isEmpty()) {
            Set<ServiceItemException> serviceItemExceptions = departmentPayout.getServiceItemExceptions().stream().map(serviceItemException -> serviceItemException.departmentPayout(departmentPayout)).collect(Collectors.toSet());
            departmentPayout.setServiceItemExceptions(serviceItemExceptions);
        }
        if (departmentPayout.getTransactionApprovalDetails() != null && !departmentPayout.getTransactionApprovalDetails().isEmpty()) {
            Set<TransactionApprovalDetails> transactionApprovalDetails = departmentPayout.getTransactionApprovalDetails().stream().map(transactionApprovalDetail -> transactionApprovalDetail.departmentPayout(departmentPayout)).collect(Collectors.toSet());
            transactionApprovalDetails.stream().forEach(transactionApprovalDetail -> {
                if (null == transactionApprovalDetail.getApprovedDateTime())
                    transactionApprovalDetail.setApprovedDateTime(LocalDateTime.now());
            });
            departmentPayout.setTransactionApprovalDetails(transactionApprovalDetails);
        }
        DepartmentPayout result = departmentPayoutRepository.save(departmentPayout);
        departmentPayoutSearchRepository.save(result);
        return result;
    }

    @Override
    public List<DepartmentPayout> saveAll(List<DepartmentPayout> departmentPayouts) {
        List<DepartmentPayout> departmentPayoutList = departmentPayoutRepository.saveAll(departmentPayouts);
        departmentPayoutSearchRepository.saveAll(departmentPayoutList);
        return departmentPayoutList;
    }

    /**
     * Get all the departmentPayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentPayout> findAll(Pageable pageable) {
        log.debug("Request to get all DepartmentPayouts");
        return departmentPayoutRepository.findAll(pageable);
    }

    /**
     * Get one departmentPayout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public DepartmentPayout findOne(Long id) {
        log.debug("Request to get DepartmentPayout : {}", id);
        DepartmentPayout departmentPayoutOptional = departmentPayoutRepository.findOne(id);
        return departmentPayoutOptional;

        /*if( departmentPayoutOptional.isPresent()) {
            DepartmentPayout departmentPayout= departmentPayoutOptional.get();
            departmentPayout.getApplicableConsultants();
           *//* departmentPayout.getDepartmentConsumptionMaterialReductions();
            departmentPayout.getHscConsumptionMaterialReductions();*//*
            return departmentPayout;
        }else
        {
            return null;
        }*/
    }

    /**
     * Delete the departmentPayout by id.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepartmentPayout : {}", id);
        DepartmentPayout departmentPayout = this.findOne(id);
        departmentPayoutRepository.deleteById(id);
        departmentPayoutSearchRepository.deleteById(id);

    }

    /**
     * Search for the departmentPayout corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentPayout> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepartmentPayouts for query {}", query);
        return departmentPayoutSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Doctor Payout");
        List<DepartmentPayout> data = departmentPayoutRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            data.forEach(doctorPayout -> {
                departmentPayoutSearchRepository.indexWithoutRefresh(doctorPayout);
            });
        }
    }

    @Override
    public DepartmentPayout saveAndUpdate(DepartmentPayout departmentPayout) {
        DepartmentPayout save = this.save(departmentPayout);
        return save;
    }

    @Override
    public DepartmentPayout update(DepartmentPayout departmentPayout) {
        log.debug("Request to save DepartmentPayout : {}", departmentPayout);
        DepartmentPayout deptPayout = null;
        if (departmentPayout.getId() != null) {
            deptPayout = this.saveAndUpdate(departmentPayout);
            return deptPayout;
        } else {
            departmentPayout.setVersion(departmentPayout.getVersion() + 1);
            deptPayout = this.save(departmentPayout);
        }
        return deptPayout;
    }

    @Override
    public List<DepartmentPayout> getDepartmentPayouts(String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepartmentPayout for query {}", query);
        Page<DepartmentPayout> page = search(query, pageable);
        List<DepartmentPayout> content = page.getContent();
        LinkedHashMap<Long, List<DepartmentPayout>> listMap = content.stream().collect(Collectors.groupingBy(DepartmentPayout::getStartingVersion, LinkedHashMap::new, Collectors.toList()));
        Set<Map.Entry<Long, List<DepartmentPayout>>> entries = listMap.entrySet();
        Iterator<Map.Entry<Long, List<DepartmentPayout>>> iterator = entries.iterator();
        List<DepartmentPayout> returnResult = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<DepartmentPayout>> next = iterator.next();
            List<DepartmentPayout> value = next.getValue();
            value.sort(Comparator.comparing(DepartmentPayout::getId));
            DepartmentPayout currentVersion = value.get(value.size() - 1);
            returnResult.add(currentVersion);
        }
        List<Long> latestDepartmentPayoutIds = returnResult.stream().map(DepartmentPayout::getId).collect(Collectors.toList());
        Query searchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQuery().must(termsQuery("id", latestDepartmentPayoutIds))).build();
        searchQuery.setPageable(pageable);
        List<DepartmentPayout> artha_departmentpayout = elasticsearchOperations.queryForList(searchQuery, DepartmentPayout.class, IndexCoordinates.of("artha_departmentpayout"));
        return artha_departmentpayout;
    }

    @Override
    public Map<String, String> exportDepartmentPayoutBreakUp(String unitCode, Integer year, Integer month, Integer departmentId) throws IOException {
        Long latestApprovedDepartmentPayoutId = departmentPayoutRepository.getLatestApprovedDepartmentPayoutId(unitCode,Long.valueOf(departmentId));
      //DoctorPayoutDepartment doctorPayoutDepartment = doctorPayoutDepartmentRepository.getDepartmentPayoutByYearMontUnitAndDepartId(unitCode, year, month, departmentId);
        if (latestApprovedDepartmentPayoutId != null) {
            DepartmentPayout departmentPayout = departmentPayoutRepository.findById(latestApprovedDepartmentPayoutId).orElseThrow(() -> {
                return new CustomParameterizedException("1011", "No Department Payout Available for the selected department");
            });
            Department department = departmentSearchRepository.findById(departmentPayout.getDepartmentId()).orElse(null);
            List<Long> collect = departmentPayout.getApplicableConsultants().stream().map(applicableConsultant -> applicableConsultant.getUserMasterId()).collect(Collectors.toList());
            List<ServiceAnalysisStaging> serviceAnalysisStagingList = null;
            if (department != null) {

                if (departmentPayout.getCustomDepartment()) {
                    if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                        serviceAnalysisStagingList = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenueForBreakUp(departmentPayout.getUnitCode(), Arrays.asList(departmentPayout.getVisitType().split(",")), collect,year,month);
                    }
                    if (departmentPayout.getNetGross().equals(NetGross.GROSS)) {
                        serviceAnalysisStagingList = serviceAnalysisRepository.allGrossServiceAnalysisForDepartmentRevenueForBreakUp(departmentPayout.getUnitCode(), Arrays.asList(departmentPayout.getVisitType().split(",")), collect,year,month);
                    }
                } else {

                    if (departmentPayout.getApplicableInvoice().equals(ApplicableInvoices.ALL_INVOICES)) {
                        serviceAnalysisStagingList = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenueForBreakUp(departmentPayout.getUnitCode(), department.getCode(), Arrays.asList(departmentPayout.getVisitType().split(",")),year,month);
                    } else {
                        serviceAnalysisStagingList = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenueOnInvoiceTypeForBreakUp(departmentPayout.getUnitCode(), department.getCode(), departmentPayout.getApplicableInvoice().getName(), Arrays.asList(departmentPayout.getVisitType().split(",")),year,month);
                    }

                }

                File file = ExportUtil.getCSVExportFile("department_payout_breakup", applicationProperties.getAthmaBucket().getTempExport());
                FileWriter invoiceFileWriter = new FileWriter(file);
                Map<String, String> fileDetails = new HashMap<>();
                fileDetails.put("fileName", file.getName());
                fileDetails.put("pathReference", "tempExport");

                final String[] invoiceFileHeader = {"MRN", "Patient Name", "Visit Type", "Visit No", "Consultant", "Dept", "Service/Item Name", "Ordering Dat", "Billed Service/Item Amount Gross", "Billed Service/Item Amount Net", "Consumption HSC", "Item Cost", "Revenue"};
                CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
                try (CSVPrinter csvFilePrinter = new CSVPrinter(invoiceFileWriter, csvFileFormat)) {
                    csvFilePrinter.printRecord(invoiceFileHeader);
                    for (int pageIndex = 0; pageIndex < serviceAnalysisStagingList.size(); pageIndex++) {
                        ServiceAnalysisStaging serviceAnalysisStaging = serviceAnalysisStagingList.get(pageIndex);
                        List<String> populateList = new ArrayList<>();
                        populateList.add(serviceAnalysisStaging.getPatientMrn());
                        populateList.add(serviceAnalysisStaging.getPatientName());
                        populateList.add(serviceAnalysisStaging.getVisitType());
                        populateList.add(serviceAnalysisStaging.getVisitNo());
                        populateList.add(serviceAnalysisStaging.getOrderingDoctorName());
                        populateList.add(department.getName());
                        populateList.add(serviceAnalysisStaging.getItemName());
                        populateList.add(serviceAnalysisStaging.getOrderedDate().toString());
                        populateList.add(String.valueOf(serviceAnalysisStaging.getGrossAmount()));
                        populateList.add(String.valueOf(serviceAnalysisStaging.getNetAmount()));
                        populateList.add("");//set consumption HSC data
                        populateList.add("");//set item cost  data
                        if(departmentPayout.getNetGross().equals(NetGross.GROSS)){
                            populateList.add(String.valueOf(serviceAnalysisStaging.getGrossAmount()));
                        }else {
                            populateList.add(String.valueOf(serviceAnalysisStaging.getNetAmount()));
                        }
                        csvFilePrinter.printRecord(populateList);
                    }
                } finally {
                    if (invoiceFileWriter != null) {
                        invoiceFileWriter.close();
                        return fileDetails;
                    }

                }

            }

        }
        else {
            throw new CustomParameterizedException("1011", "No Department Payout Available for the selected department");
        }
        return null;
    }

    @Override
    public Map<String, String> exportDepartmentRevenueSummary(String unitCode, Integer year, Integer month, Integer departmentId) throws Exception {
        File file = ExportUtil.getCSVExportFile("department_wise_revenue", applicationProperties.getAthmaBucket().getTempExport());
        FileWriter invoiceFileWriter = new FileWriter(file);
        Map<String, String> fileDetails = new HashMap<>();
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");

        final String[] invoiceFileHeader = {"Department", "Revenue "};
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(invoiceFileWriter, csvFileFormat)) {
            csvFilePrinter.printRecord(invoiceFileHeader);
            Map<Department, List<DoctorPayoutDepartment>> doctorDepartmentPayoutList;
            if (null!=departmentId) {
                 doctorDepartmentPayoutList = doctorPayoutDepartmentRepository.getDepartmentPayoutByYearMonthUnitAndDepartIdGroupBy(unitCode, year, month, departmentId).stream().collect(Collectors.groupingBy(DoctorPayoutDepartment::getDepartment));
            }
            else {
                doctorDepartmentPayoutList = doctorPayoutDepartmentRepository.getDepartmentPayoutByYearMonthANDUnitGroupBy(unitCode, year, month).stream().collect(Collectors.groupingBy(DoctorPayoutDepartment::getDepartment));
            }if (doctorDepartmentPayoutList.size()<=0){
                throw new CustomParameterizedException("10395","Department Revenue Is Not Available For The Selected Department");
            }
            for (Map.Entry<Department, List<DoctorPayoutDepartment>> entry : doctorDepartmentPayoutList.entrySet()) {
                List<DoctorPayoutDepartment> doctorPayoutDepartments = entry.getValue();
                BigDecimal totalDepartmentRevenue = new BigDecimal(0);
                for (DoctorPayoutDepartment doctorPayoutDepartment : doctorPayoutDepartments) {
                    DepartmentPayout departmentPayout = departmentPayoutRepository.findById(doctorPayoutDepartment.getDepartmentPayoutId()).orElseThrow(() -> {
                        return  new CustomParameterizedException("1011", "No DepartmentPayout Found");
                    });
                    Department department = departmentSearchRepository.findById(departmentPayout.getDepartmentId()).orElse(null);
                    List<Long> collect = departmentPayout.getApplicableConsultants().stream().map(applicableConsultant -> applicableConsultant.getUserMasterId()).collect(Collectors.toList());
                    if (department != null) {

                        if (departmentPayout.getCustomDepartment()) {
                            if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                                totalDepartmentRevenue = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenueForConsultantExecuted(departmentPayout.getUnitCode(), Arrays.asList(departmentPayout.getVisitType().split(",")), collect);
                            }
                            if (departmentPayout.getNetGross().equals(NetGross.GROSS)) {
                                totalDepartmentRevenue = serviceAnalysisRepository.allGrossServiceAnalysisForDepartmentRevenueForConsultantExecuted(departmentPayout.getUnitCode(), Arrays.asList(departmentPayout.getVisitType().split(",")), collect);
                            }
                        } else {
                            if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                                if (departmentPayout.getApplicableInvoice().equals(ApplicableInvoices.ALL_INVOICES)) {
                                    totalDepartmentRevenue = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenueExecuted(departmentPayout.getUnitCode(), department.getCode(), Arrays.asList(departmentPayout.getVisitType().split(",")));
                                }
                                else{
                                    totalDepartmentRevenue = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenueOnInvoiceTypeExecuted(departmentPayout.getUnitCode(), department.getCode(),departmentPayout.getApplicableInvoice().getName(), Arrays.asList(departmentPayout.getVisitType().split(",")));
                                }
                            }
                            if (departmentPayout.getNetGross().equals(NetGross.GROSS)) {
                                if (departmentPayout.getApplicableInvoice().equals(ApplicableInvoices.ALL_INVOICES)) {
                                    totalDepartmentRevenue = serviceAnalysisRepository.totalGrossAmountForDepartmentRevenueExecuted(departmentPayout.getUnitCode(), department.getCode(), Arrays.asList(departmentPayout.getVisitType().split(",")));
                                }
                                else{
                                    totalDepartmentRevenue = serviceAnalysisRepository.totalGrossAmountForDepartmentRevenueOnInvoiceTypeExecuted(departmentPayout.getUnitCode(), department.getCode(),departmentPayout.getApplicableInvoice().getName(), Arrays.asList(departmentPayout.getVisitType().split(",")));
                                }
                            }

                        }
                        List departmentRevenue = new ArrayList();
                        departmentRevenue.add(department.getName());
                        departmentRevenue.add(totalDepartmentRevenue);
                        csvFilePrinter.printRecord(departmentRevenue);
                    }
                    else {
                        throw new CustomParameterizedException("1010", "No Department Payout created please run department revenue job to calculate");
                    }
                    break;
                }

            }

        }
        catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        } finally {
            if (invoiceFileWriter != null)
                invoiceFileWriter.close();
        }
        return fileDetails;
    }

    @Override
    public List<Integer> getDistinctVersion(String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest("artha_departmentpayout");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryStringQuery(query)).size(0).aggregation(AggregationBuilders.terms("unique_version").field("version.raw").size(1000));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = elasticsearchTemplate.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get("unique_version");
        List<Integer> requestList= new ArrayList<>();
        for (Terms.Bucket bucket : terms.getBuckets()) {
            requestList.add(Integer.parseInt(bucket.getKey().toString()));
        }
        return requestList;
    }
    @Override
    public Map<String, String> exportDepartmentPayout(@RequestParam String query, Pageable pageable,Boolean latest) throws Exception{
        log.debug("REST request to export to csv for department payout list for query {}", query);
        long totalPages = this.search(query, PageRequest.of(0, 200, pageable.getSort())).getTotalPages();
        log.debug("Total page count:{} ", totalPages);
        File file = ExportUtil.getCSVExportFile("department_payout", applicationProperties.getAthmaBucket().getTempExport());
        FileWriter invoiceFileWriter = new FileWriter(file);
        Map<String, String> fileDetails = new HashMap<>();
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");

        final String[] invoiceFileHeader = {"Department", "Created By","Created Date","Version","Status"};

        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(invoiceFileWriter, csvFileFormat)) {
            csvFilePrinter.printRecord(invoiceFileHeader);
            for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
                Iterator<DepartmentPayout> iterator;
                if (latest){
                     iterator = this.getDepartmentPayouts(query, pageable).iterator();
                }else {
                     iterator = search(query, PageRequest.of(pageIndex, 2000, pageable.getSort())).iterator();
                }
                while (iterator.hasNext()) {
                    DepartmentPayout departmentPayout = iterator.next();

                    List departmentPayoutList=new ArrayList();
                   departmentPayoutList.add(departmentPayout.getDepartmentName());
                    departmentPayoutList.add(departmentPayout.getCreatedBy().getDisplayName());
                    departmentPayoutList.add(departmentPayout.getCreatedDate());
                    departmentPayoutList.add(departmentPayout.getVersion());
                    if (latest && null!=departmentPayout.getStatus()) {
                        departmentPayoutList.add(departmentPayout.getStatus()? "Active":"Inactive");
                    }
                    else{
                        departmentPayoutList.add(departmentPayout.getChangeRequestStatus().name().replaceAll("_", " "));
                    }
                    csvFilePrinter.printRecord(departmentPayoutList);

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
