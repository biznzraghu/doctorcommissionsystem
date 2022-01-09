package org.nh.artha.web.rest;


import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.Type;
import org.nh.artha.repository.*;
import org.nh.artha.repository.search.DoctorPayoutSearchRepository;
import org.nh.artha.repository.search.HealthcareServiceCentreSearchRepository;
import org.nh.artha.repository.search.ItemCategorySearchRepository;
import org.nh.artha.service.*;
import org.nh.artha.util.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@RestController
@RequestMapping("/api")
@Transactional
public class StagingResource {
    private final Logger log = LoggerFactory.getLogger(StagingResource.class);
    private StagingService stagingService;
    private final McrStagingRepository mcrStagingRepository;
    private final ServiceAnalysisRepository serviceAnalysisRepository;
    private final VariablePayoutServiceAnalysisLoading variablePayoutServiceAnalysisLoading;
    private final ServiceItemBenefitService serviceItemBenefitService;
    private final ServiceItemBenefitRepository serviceItemBenefitRepository;
    private final DepartmentPayoutRepository departmentPayoutRepository;
    @Autowired
    private DoctorPayoutRepository doctorPayoutRepository;
    @Autowired
    private VariablePayoutRepository variablePayoutRepository;
    @Autowired
    private DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository;
    @Autowired
    private DoctorPayoutService doctorPayoutService;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DoctorPayoutSearchRepository doctorPayoutSearchRepository;

    @Autowired
    private NamedParameterJdbcTemplate mdmDbNamedParameterTemplate;
    @Autowired
    private JdbcTemplate mdmDbTemplate;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private ItemCategorySearchRepository itemCategorySearchRepository;
    @Autowired
    private HealthcareServiceCentreSearchRepository healthcareServiceCentreSearchRepository;
    @Autowired
    private HealthcareServiceCentreRepository healthcareServiceCentreRepository;

    @Autowired
    private UserMasterService userMasterService;
    @Autowired
    private ServiceMasterRepository serviceMasterRepository;


    StagingResource(StagingService stagingService, McrStagingRepository mcrStagingRepository, ServiceAnalysisRepository serviceAnalysisRepository, VariablePayoutServiceAnalysisLoading variablePayoutServiceAnalysisLoading,
                    ServiceItemBenefitService serviceItemBenefitService, ServiceItemBenefitRepository serviceItemBenefitRepository, DepartmentPayoutRepository departmentPayoutRepository) {
        this.stagingService = stagingService;
        this.mcrStagingRepository = mcrStagingRepository;
        this.serviceAnalysisRepository = serviceAnalysisRepository;
        this.variablePayoutServiceAnalysisLoading = variablePayoutServiceAnalysisLoading;
        this.serviceItemBenefitService = serviceItemBenefitService;
        this.serviceItemBenefitRepository = serviceItemBenefitRepository;
        this.departmentPayoutRepository = departmentPayoutRepository;
    }

    @PostMapping("/mcr/stg-upload")
    public String mcrUploadFile(@RequestBody MultipartFile multipartFile) throws IOException {
        log.debug("In MCR STAGING UPLOAD{}" + multipartFile.getOriginalFilename());
        String result = stagingService.uploadFile(multipartFile, "MCR_STAGING");
        return result;
    }

    @PostMapping("/serviceAnalysis/stg-upload")
    public String serviceAnalysisUploadFile(@RequestBody MultipartFile multipartFile) throws IOException {
        log.debug("In MCR STAGING UPLOAD{}" + multipartFile.getOriginalFilename());
        String result = stagingService.uploadFile(multipartFile, "SERVICE_ANALYSIS_STAGING");
        return result;
    }

    @GetMapping("/testVariablePayout/unitcode")
    public Boolean testVariablePayout(@RequestParam("unitcode") String unitCode, @RequestParam(required = false) Long variablePayoutID) {
        List<VariablePayout> doctorsBasedOnUnit = variablePayoutServiceAnalysisLoading.getDoctorsBasedOnUnit(unitCode);
        if(variablePayoutID!=null) {
            List<VariablePayout> collect1 = doctorsBasedOnUnit.stream().filter(variablePayout -> variablePayout.getId().equals(variablePayoutID)).collect(Collectors.toList());
            doctorsBasedOnUnit=collect1;
        }
        Map<Long, Map<ServiceItemBenefit, List<ServiceAnalysisStaging>>> doctorBySAR = new HashMap<>();
        for (int i = 0; i < doctorsBasedOnUnit.size(); i++) {
            Long employeeId = doctorsBasedOnUnit.get(i).getEmployeeId();
            Long latestApprovedVariablePayoutId = variablePayoutRepository.getLatestApprovedVariablePayoutId(unitCode, employeeId);
            VariablePayout variablePayout = variablePayoutRepository.findById(latestApprovedVariablePayoutId).orElse(null);
            List<ServiceItemBenefit> serviceItemBenefitByVariablePayoutOrderByPriority = serviceItemBenefitRepository.getServiceItemBenefitByVersion(variablePayout.getVariablePayoutId(), variablePayout.getVersion());
            if(variablePayout.getServiceItemBenefitTemplates()!=null && !variablePayout.getServiceItemBenefitTemplates().isEmpty()){
                List<ServiceItemBenefitTemplate> collect = variablePayout.getServiceItemBenefitTemplates().stream().collect(Collectors.toList());
                collect.parallelStream().forEach(serviceItemBenefitTemplate -> {
                    serviceItemBenefitByVariablePayoutOrderByPriority.addAll(serviceItemBenefitRepository.getRulesByTemplate(serviceItemBenefitTemplate.getId()));
                });
            }
            Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap = serviceItemBenefitByVariablePayoutOrderByPriority.stream().collect(Collectors.groupingBy(ServiceItemBenefit::getType));
            List<InvoiceDoctorPayout> invoiceDoctorPayoutList23= new ArrayList<>();
            List<InvoiceDoctorPayout> invoiceDoctorPayoutList = variablePayoutServiceAnalysisLoading.processSARReportsForServices(serviceItemTypeListMap, employeeId, unitCode, variablePayout,LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),false);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutList1 = variablePayoutServiceAnalysisLoading.processSARReportsForPackage(serviceItemTypeListMap, employeeId, unitCode, variablePayout,LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),false);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutList2 = variablePayoutServiceAnalysisLoading.processMcrReportsForItem(serviceItemTypeListMap, employeeId, unitCode, variablePayout,LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),true);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutList3=variablePayoutServiceAnalysisLoading.processSARReportsForInvoice(serviceItemTypeListMap,employeeId,unitCode,variablePayout,LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),false);
            invoiceDoctorPayoutList23.addAll(invoiceDoctorPayoutList);
            invoiceDoctorPayoutList23.addAll(invoiceDoctorPayoutList1);
            invoiceDoctorPayoutList23.addAll(invoiceDoctorPayoutList2);
            DoctorPayout doctorPayout = doctorPayoutRepository.getDoctorByYearAndMonth(employeeId, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
            if (doctorPayout == null) {
                doctorPayout = new DoctorPayout();
                doctorPayout.setEmployeeId(employeeId);
                doctorPayout.setMonth(LocalDateTime.now().getMonthValue());
                doctorPayout.setYear(LocalDateTime.now().getYear());
                doctorPayout.setunitCode(unitCode);
                doctorPayout = doctorPayoutService.save(doctorPayout);
            }
            List<DoctorPayoutAdjustment> doctorPayoutAdjustmentList = variablePayoutServiceAnalysisLoading.calculatePayoutAdjustment(unitCode, doctorPayout);
            doctorPayout.setDoctorPayoutInvoices(invoiceDoctorPayoutList23.stream().collect(Collectors.toSet()));
            doctorPayoutSearchRepository.save(doctorPayout);
        }
        return null;
    }

    @GetMapping("/save/ServiceItemBenefit")
    public void setPrio() {
        List<ServiceItemBenefit> all = serviceItemBenefitService.findAll(PageRequest.of(0, 100)).getContent();
        for (int i = 0; i < all.size(); i++) {
            serviceItemBenefitService.save(all.get(i));
        }
    }

    @GetMapping("/save/setServiceAnalysis")
    public void setServiceAnalysis() {
        List<ServiceAnalysisStaging> all = serviceAnalysisRepository.findAll();
        KeyGenerator keyGenerator = new KeyGenerator();
        for (int i = 0; i < all.size(); i++) {
            ServiceAnalysisStaging serviceAnalysisStaging = all.get(i);
            String groupkey = keyGenerator.createKey(serviceAnalysisStaging.getItemGroup());
            String typekey = keyGenerator.createKey(serviceAnalysisStaging.getItemType());
            String visitTypeTariffClassKey = keyGenerator.createVisitTypeTariffClassKey(serviceAnalysisStaging.getVisitType(), serviceAnalysisStaging.getTariffClass());
            serviceAnalysisStaging.setGroupKey(groupkey);
            serviceAnalysisStaging.setTypeKey(typekey);
            serviceAnalysisStaging.setVisitTypeTariffClassKey(visitTypeTariffClassKey);
            serviceAnalysisRepository.save(serviceAnalysisStaging);
        }
    }

    @GetMapping("/executeDepartmentPayout/unit/unitcode")
    public void excecuteDepartmentPayoutJobBasedOnUnit(String unitCode) {
        List<Long> departmentPayoutList = departmentPayoutRepository.fetchAllDepartmentBasedOnUnit(unitCode);
        departmentPayoutRepository.findAllById(departmentPayoutList).forEach(departmentPayout -> {
            variablePayoutServiceAnalysisLoading.executeDepartmentRevenueCalculation(departmentPayout);
        });
    }

    @GetMapping("/loadServiceMaster")
    public List<ServiceMaster> loadServiceMasterDataFromMdm() {
        String serviceGroupCountQuery = "select count(*) from service_master";
        String query = "SELECT * FROM service_master OFFSET :startIndex ROWS   FETCH FIRST 10 ROWS ONLY";
        Long serviceGroupCount = mdmDbTemplate.queryForObject(serviceGroupCountQuery, Long.class);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(serviceGroupCount / pageSize));
        for (Integer i = 0; i <= lastPageNumber; i++) {
            String replaceQuery = query.replace(":startIndex", i.toString());
            List<ServiceMaster> serviceGroupList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(ServiceMaster.class));
            syncServiceGroup(serviceGroupList);
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean syncServiceGroup(List<ServiceMaster> serviceMastersList) {
        try {
            serviceMastersList.toString();
//            serviceMasterService.saveAll(serviceMastersList);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/loadServiceGroup")
    public List<ServiceMaster> loadServiceServiceGroupMdm() {
        long startTime = System.currentTimeMillis();
        log.debug("Request to ServiceNameSyncJob  : {}");
        String serviceGroupCountQuery = "select count(*) from service_master;";
        String query = "SELECT * FROM service_master OFFSET :startIndex ROWS   FETCH FIRST 200 ROWS ONLY";
        Long serviceGroupCount = mdmDbTemplate.queryForObject(serviceGroupCountQuery, Long.class);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Integer startIndex = 0;
        Predicate<ServiceMaster> serviceNotpresent= serviceMaster -> {
            {
                ServiceMaster sm = new ServiceMaster();
                sm.setCode(serviceMaster.getCode());
                List<ServiceMaster> serviceMaster1 = serviceMasterRepository.findAll(Example.of(sm));
                if(serviceMaster1==null || serviceMaster1.isEmpty()){
                    return true;
                }else{
                    return false;
                }

            }
        };
        Integer sum=0;
        while (startIndex < serviceGroupCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<ServiceMaster> serviceGroupList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(ServiceMaster.class));
            serviceGroupList=serviceGroupList.stream().filter(serviceNotpresent).collect(Collectors.toList());
            sum+=serviceGroupList.size();
            startIndex = startIndex + pageSize;
        }
        System.out.println("totalCount "+serviceGroupCount+" Sum "+sum);
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncItemMaster(List<ItemCategory> itemCategoryList) {
        List<ItemCategory> itemCategoryList1 = itemCategoryRepository.saveAll(itemCategoryList);
        itemCategorySearchRepository.saveAll(itemCategoryList1);

    }
    @GetMapping("/_search/all-hsc")
    public ResponseEntity<List<HealthcareServiceCenter>> searchAllHsc(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceMasters for query {}", query);
        Page<HealthcareServiceCenter> page =healthcareServiceCentreSearchRepository.search(queryStringQuery(query), PageRequest.of(0, 5000));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @GetMapping("/_index/healthcare-department-centre")
    public ResponseEntity<Void> indexHsc(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Variable Payout");
        long resultCount = healthcareServiceCentreRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            this.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("artha","ElasticSearch index completed sucessfully","")).build();
    }
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on City");
        List<HealthcareServiceCenter> data = healthcareServiceCentreRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            data.stream().forEach(healthcareServiceCenter -> healthcareServiceCentreSearchRepository.indexWithoutRefresh(healthcareServiceCenter));
        }
    }
}


