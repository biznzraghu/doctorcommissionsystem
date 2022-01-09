package org.nh.artha.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nh.artha.domain.*;
import org.nh.artha.domain.dto.CodeNameDto;
import org.nh.artha.domain.enumeration.*;
import org.nh.artha.repository.*;
import org.nh.artha.repository.search.DepartmentSearchRepository;
import org.nh.artha.repository.search.DoctorPayoutSearchRepository;
import org.nh.artha.repository.search.PayoutAdjustmentSearchRepository;
import org.nh.artha.repository.search.UserMasterSearchRepository;
import org.nh.artha.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class VariablePayoutServiceAnalysisLoadingImpl implements VariablePayoutServiceAnalysisLoading {

    private final Logger log = LoggerFactory.getLogger(VariablePayoutServiceAnalysisLoading.class);

    private final ServiceAnalysisRepository serviceAnalysisRepository;

    private final VariablePayoutRepository variablePayoutRepository;

    private final McrStagingRepository mcrStagingRepository;

    private final DoctorPayoutService doctorPayoutService;

    private final InvoiceDoctorPayoutService invoiceDoctorPayoutService;

    private final DoctorPayoutRepository doctorPayoutRepository;

    private final DepartmentRepository departmentRepository;

    private final DoctorPayoutSearchRepository doctorPayoutSearchRepository;

    private final PayoutAdjustmentService payoutAdjustmentService;

    private final UserMasterSearchRepository userMasterSearchRepository;

    private final UserMasterRepository userMasterRepository;

    private final PayoutAdjustmentRepository payoutAdjustmentRepository;

    private final PayoutAdjustmentSearchRepository payoutAdjustmentSearchRepository;

    private final PayoutAdjustmentDetailsRepository payoutAdjustmentDetailsRepository;

    private final DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository;

    private final ObjectMapper objectMapper;

    private final DoctorPayoutLOSRepository doctorPayoutLOSRepository;

    private final StayBenefitServiceRepository stayBenefitServiceRepository;

    private final DoctorPayoutAdjustmentRepository doctorPayoutAdjustmentRepository;

    private final DepartmentSearchRepository departmentSearchRepository;

    private final DoctorPayoutDepartmentService doctorPayoutDepartmentService;
    private final DepartmentRevenueCalculationRepository departmentRevenueCalculationRepository;
    private final DepartmentPayoutAdjustmentRepository departmentPayoutAdjustmentRepository;

    private final ServiceItemBenefitRepository serviceItemBenefitRepository;

    private final InvoiceDoctorPayoutRepository invoiceDoctorPayoutRepository;

    private final CommonValueSetCodeService commonValueSetCodeService;

    private final ServiceAnalysisStagingService serviceAnalysisStagingService;

    public VariablePayoutServiceAnalysisLoadingImpl(ServiceAnalysisRepository serviceAnalysisRepository, VariablePayoutRepository variablePayoutRepository,
                                                    McrStagingRepository mcrStagingRepository, DoctorPayoutService doctorPayoutService, InvoiceDoctorPayoutService invoiceDoctorPayoutService,
                                                    DoctorPayoutRepository doctorPayoutRepository, DepartmentRepository departmentRepository, ObjectMapper objectMapper,
                                                    DoctorPayoutSearchRepository doctorPayoutSearchRepository,
                                                    PayoutAdjustmentService payoutAdjustmentService, UserMasterSearchRepository userMasterSearchRepository,
                                                    PayoutAdjustmentRepository payoutAdjustmentRepository,
                                                    PayoutAdjustmentSearchRepository payoutAdjustmentSearchRepository,
                                                    UserMasterRepository userMasterRepository, PayoutAdjustmentDetailsRepository payoutAdjustmentDetailsRepository,
                                                    DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository,
                                                    DoctorPayoutLOSRepository doctorPayoutLOSRepository, StayBenefitServiceRepository stayBenefitServiceRepository,
                                                    DoctorPayoutAdjustmentRepository doctorPayoutAdjustmentRepository, DepartmentSearchRepository departmentSearchRepository, DoctorPayoutDepartmentService doctorPayoutDepartmentService,
                                                    DepartmentRevenueCalculationRepository departmentRevenueCalculationRepository, DepartmentPayoutAdjustmentRepository departmentPayoutAdjustmentRepository, ServiceItemBenefitRepository serviceItemBenefitRepository, InvoiceDoctorPayoutRepository invoiceDoctorPayoutRepository,
                                                    CommonValueSetCodeService commonValueSetCodeService, ServiceAnalysisStagingService serviceAnalysisStagingService) {
        this.serviceAnalysisRepository = serviceAnalysisRepository;
        this.variablePayoutRepository = variablePayoutRepository;
        this.mcrStagingRepository = mcrStagingRepository;
        this.doctorPayoutService = doctorPayoutService;
        this.invoiceDoctorPayoutService = invoiceDoctorPayoutService;
        this.doctorPayoutRepository = doctorPayoutRepository;
        this.departmentRepository = departmentRepository;
        this.objectMapper = objectMapper;
        this.doctorPayoutSearchRepository = doctorPayoutSearchRepository;
        this.payoutAdjustmentService = payoutAdjustmentService;
        this.userMasterSearchRepository = userMasterSearchRepository;
        this.payoutAdjustmentRepository = payoutAdjustmentRepository;
        this.payoutAdjustmentSearchRepository = payoutAdjustmentSearchRepository;
        this.userMasterRepository = userMasterRepository;
        this.payoutAdjustmentDetailsRepository = payoutAdjustmentDetailsRepository;
        this.doctorPayoutDepartmentRepository = doctorPayoutDepartmentRepository;
        this.doctorPayoutLOSRepository = doctorPayoutLOSRepository;
        this.stayBenefitServiceRepository = stayBenefitServiceRepository;
        this.doctorPayoutAdjustmentRepository = doctorPayoutAdjustmentRepository;
        this.departmentSearchRepository = departmentSearchRepository;
        this.doctorPayoutDepartmentService = doctorPayoutDepartmentService;
        this.departmentRevenueCalculationRepository = departmentRevenueCalculationRepository;
        this.departmentPayoutAdjustmentRepository = departmentPayoutAdjustmentRepository;
        this.serviceItemBenefitRepository = serviceItemBenefitRepository;
        this.invoiceDoctorPayoutRepository = invoiceDoctorPayoutRepository;
        this.commonValueSetCodeService = commonValueSetCodeService;
        this.serviceAnalysisStagingService = serviceAnalysisStagingService;
    }

    @Override
    public List<ServiceAnalysisStaging> fetchAllServiceAnalysisBasedOnRule(String ruleKey) {
        List<ServiceAnalysisStaging> serviceAnalysisStagings = serviceAnalysisRepository.fetchServiceBasedOnRuleKey(ruleKey);
        return serviceAnalysisStagings;
    }

    @Override
    public List<VariablePayout> getDoctorsBasedOnUnit(String unitCode) {
        List<Long> doctorBasedOnUnit = variablePayoutRepository.getDoctorsBasedOnUnit(unitCode);
        return variablePayoutRepository.findAllById(doctorBasedOnUnit);
    }

    @Override
    public List<InvoiceDoctorPayout> saveVariablePayoutData(Map<Long, Map<ServiceItemBenefit, List<ServiceAnalysisStaging>>> data, String unitCode, Integer year, Integer month) {
        List<InvoiceDoctorPayout> invoiceDoctorPayoutListReturn = new ArrayList<>();
        data.forEach((employeeId, serviceAnalysisStagingList) -> {
            DoctorPayout doctorPayout = doctorPayoutRepository.getDoctorByYearAndMonth(employeeId, year == null ? LocalDateTime.now().getYear() : year, month == null ? LocalDateTime.now().getMonthValue() : month);
            Long latestApprovedVariablePayoutId = variablePayoutRepository.getLatestApprovedVariablePayoutId(unitCode, employeeId);//year month empid
            if (doctorPayout == null) {
                doctorPayout = new DoctorPayout();
                doctorPayout.setEmployeeId(employeeId);
                doctorPayout.setMonth(month == null ? LocalDateTime.now().getMonthValue() : month);
                doctorPayout.setYear(year == null ? LocalDateTime.now().getYear() : year);
                doctorPayout.setunitCode(unitCode);

                doctorPayout = doctorPayoutService.save(doctorPayout);
            }
            DoctorPayout doctorPayoutResult = doctorPayout;
            List<InvoiceDoctorPayout> invoiceDoctorPayoutList = createInvoiceBaseOnSARData(serviceAnalysisStagingList);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutWithDoctorPayoutAsRefernce = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> {
                return invoiceDoctorPayout.doctorPayout(doctorPayoutResult);
            }).collect(Collectors.toList());
            Double sum = 0d;
            if (doctorPayout.getServiceItemBenefitAmount() != null) {
                sum += invoiceDoctorPayoutWithDoctorPayoutAsRefernce.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getServiceBenefietAmount()).collect(Collectors.toList()).stream().reduce(0d, Double::sum);
            } else {
                sum = invoiceDoctorPayoutWithDoctorPayoutAsRefernce.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getServiceBenefietAmount()).collect(Collectors.toList()).stream().reduce(0d, Double::sum);
            }
            doctorPayoutResult.setServiceItemBenefitAmount(sum.longValue());
            log.debug("Save Variable Payout Data For Service And Package ::{} " + latestApprovedVariablePayoutId);
            doctorPayoutResult.setVariablePayoutId(latestApprovedVariablePayoutId);
            doctorPayoutService.save(doctorPayoutResult);
            invoiceDoctorPayoutListReturn.addAll(invoiceDoctorPayoutService.saveAll(invoiceDoctorPayoutWithDoctorPayoutAsRefernce));

//            calculatePayoutAdjustment(unitCode, save);

        });
        return invoiceDoctorPayoutListReturn;
    }

    private List<InvoiceDoctorPayout> createInvoiceBaseOnSARData(Map<ServiceItemBenefit, List<ServiceAnalysisStaging>> serviceAnalysisStagingList) {
        Map<String, Department> typeDepartmentCodeMap = new HashMap<>();
        Set<Map.Entry<ServiceItemBenefit, List<ServiceAnalysisStaging>>> entries = serviceAnalysisStagingList.entrySet();
        Iterator<Map.Entry<ServiceItemBenefit, List<ServiceAnalysisStaging>>> iterator = entries.iterator();
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<ServiceItemBenefit, List<ServiceAnalysisStaging>> next = iterator.next();
            ServiceItemBenefit key = next.getKey();
            List<ServiceAnalysisStaging> value = next.getValue();
            for (int i = 0; i < value.size(); i++) {
                ServiceAnalysisStaging serviceAnalysisStagings = value.get(i);
                InvoiceDoctorPayout invoiceDoctorPayout = new InvoiceDoctorPayout();
                invoiceDoctorPayout.setName(serviceAnalysisStagings.getItemName());
                invoiceDoctorPayout.setInvoiceNumber(serviceAnalysisStagings.getInvoiceNumber());
                invoiceDoctorPayout.setVisitType(serviceAnalysisStagings.getVisitType());
                invoiceDoctorPayout.setMrn(serviceAnalysisStagings.getPatientMrn());
                invoiceDoctorPayout.setEmployeeId(serviceAnalysisStagings.getEmployeeId());
                invoiceDoctorPayout.setVisitType(serviceAnalysisStagings.getVisitType());
                invoiceDoctorPayout.setIssueDateTime(serviceAnalysisStagings.getInvoiceDate().toInstant(ZoneOffset.UTC));
                invoiceDoctorPayout.setOrderDateTime(serviceAnalysisStagings.getOrderedDate().toInstant(ZoneOffset.UTC));
                invoiceDoctorPayout.setSarId(serviceAnalysisStagings.getId());
                invoiceDoctorPayout.setInvoiceDate(serviceAnalysisStagings.getInvoiceDate());
                invoiceDoctorPayout.setRuleId(key.getId());//set rule id;
                double percentageValue = 0.0;
                if (key.getApplicableOn() != null && key.getApplicableOn().equals(ApplicableOn.GROSS)) {
                    if (key != null && key.getAmount().contains("%")) {
                        double parseDouble = Double.parseDouble(key.getAmount().replaceAll("%", "").trim());
                        percentageValue = (serviceAnalysisStagings.getGrossAmount() * parseDouble) / 100;
                    } else if (key != null) {
                        percentageValue = Double.parseDouble(key.getAmount());
                    }
                    if (percentageValue > serviceAnalysisStagings.getGrossAmount()) {
                        percentageValue = serviceAnalysisStagings.getGrossAmount();
                    }
                    invoiceDoctorPayout.setServiceBenefietAmount(percentageValue);
                } else {
                    if (key != null && key.getAmount().contains("%")) {
                        double parseDouble = Double.parseDouble(key.getAmount().replaceAll("%", "").trim());
                        percentageValue = (serviceAnalysisStagings.getNetAmount() * parseDouble) / 100;
                    } else if (key != null) {
                        percentageValue = Double.parseDouble(key.getAmount());
                    }
                    if (percentageValue > serviceAnalysisStagings.getNetAmount()) {
                        percentageValue = serviceAnalysisStagings.getNetAmount();
                    }
                    invoiceDoctorPayout.setServiceBenefietAmount(percentageValue);
                }
                if (key.getBeneficiary().equals(Beneficiary.ORDERING)) {
                    Department departmentByCode = null;
                    if (typeDepartmentCodeMap.containsKey(serviceAnalysisStagings.getOrderingDepartmentCode())) {
                        departmentByCode = typeDepartmentCodeMap.get(serviceAnalysisStagings.getOrderingDepartmentCode());
                    } else {
                        departmentByCode = departmentRepository.findDepartmentByCode(serviceAnalysisStagings.getOrderingDepartmentCode());
                        typeDepartmentCodeMap.put(serviceAnalysisStagings.getOrderingDepartmentCode(), departmentByCode);
                    }
                    invoiceDoctorPayout.setDepartment(departmentByCode);
                }
                if (key.getBeneficiary().equals(Beneficiary.ADMITTING)) {
                    Department departmentByCode = null;
                    if (typeDepartmentCodeMap.containsKey(serviceAnalysisStagings.getAdmittingDepartmentCode())) {
                        departmentByCode = typeDepartmentCodeMap.get(serviceAnalysisStagings.getAdmittingDepartmentCode());
                    } else {
                        departmentByCode = departmentRepository.findDepartmentByCode(serviceAnalysisStagings.getAdmittingDepartmentCode());
                        typeDepartmentCodeMap.put(serviceAnalysisStagings.getAdmittingDepartmentCode(), departmentByCode);
                    }
                    invoiceDoctorPayout.setDepartment(departmentByCode);
                }
                if (key.getBeneficiary().equals(Beneficiary.RENDERING)) {
                    Department departmentByCode = null;
                    if (typeDepartmentCodeMap.containsKey(serviceAnalysisStagings.getRenderingDepartmentCode())) {
                        departmentByCode = typeDepartmentCodeMap.get(serviceAnalysisStagings.getRenderingDepartmentCode());
                    } else {
                        departmentByCode = departmentRepository.findDepartmentByCode(serviceAnalysisStagings.getRenderingDepartmentCode());
                        typeDepartmentCodeMap.put(serviceAnalysisStagings.getRenderingDepartmentCode(), departmentByCode);
                    }
                    invoiceDoctorPayout.setDepartment(departmentByCode);
                }
                invoiceDoctorPayoutList.add(invoiceDoctorPayout);

            }
        }
        return invoiceDoctorPayoutList;

    }

    @Override
    public List<InvoiceDoctorPayout> processSARReportsForServices(Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap, Long employeeId, String unitCode, VariablePayout variablePayout, Integer year, Integer month, Boolean throughSchduler) {
        List<ServiceItemBenefit> serviceItemBenefitList = null;
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = new ArrayList<>();
        if (serviceItemTypeListMap.containsKey(Type.SERVICE_NAME) && serviceItemTypeListMap.get(Type.SERVICE_NAME) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.SERVICE_NAME);
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.SERVICE_NAME, unitCode, variablePayout, year, month, throughSchduler, null));
        }
        if (serviceItemTypeListMap.containsKey(Type.SERVICE_GROUP) && serviceItemTypeListMap.get(Type.SERVICE_GROUP) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.SERVICE_GROUP);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.SERVICE_GROUP, unitCode, variablePayout, year, month, throughSchduler, sarIdList));
        }
        if (serviceItemTypeListMap.containsKey(Type.SERVICE_TYPE) && serviceItemTypeListMap.get(Type.SERVICE_TYPE) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.SERVICE_TYPE);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.SERVICE_TYPE, unitCode, variablePayout, year, month, throughSchduler, sarIdList));

        }
        if (serviceItemTypeListMap.containsKey(Type.ALL_SERVICES) && serviceItemTypeListMap.get(Type.ALL_SERVICES) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.ALL_SERVICES);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.ALL_SERVICES, unitCode, variablePayout, year, month, throughSchduler, sarIdList));

        }
        if (serviceItemTypeListMap.containsKey(Type.SERVICE_INSIDE_PACKAGE) && serviceItemTypeListMap.get(Type.SERVICE_INSIDE_PACKAGE) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.SERVICE_INSIDE_PACKAGE);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.SERVICE_INSIDE_PACKAGE, unitCode, variablePayout, year, month, throughSchduler, sarIdList));

        }
        return invoiceDoctorPayoutList;
    }

    @Override
    public List<InvoiceDoctorPayout> processSARReportsForPackage(Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap, Long employeeId, String unitCode, VariablePayout variablePayout, Integer year, Integer month,
                                                                 Boolean throughSchduler) {
        List<ServiceItemBenefit> serviceItemBenefitList = null;
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = new ArrayList<>();
        List<ServiceAnalysisStaging> allMatchRuleServiceItemBenefitList = new ArrayList<>();
        if (serviceItemTypeListMap.containsKey(Type.PACKAGE_MINUS_MATERIAL_COST) && serviceItemTypeListMap.get(Type.PACKAGE_MINUS_MATERIAL_COST) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.PACKAGE_MINUS_MATERIAL_COST);
            allMatchRuleServiceItemBenefitList.addAll(fetchAllServiceAnalysisBasedOnRule(""));
        }
        if (serviceItemTypeListMap.containsKey(Type.PACKAGE_NAME) && serviceItemTypeListMap.get(Type.PACKAGE_NAME) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.PACKAGE_NAME);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.PACKAGE_NAME, unitCode, variablePayout, year, month, throughSchduler, sarIdList));
        }
        if (serviceItemTypeListMap.containsKey(Type.PACKAGE_GROUP) && serviceItemTypeListMap.get(Type.PACKAGE_GROUP) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.PACKAGE_GROUP);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.PACKAGE_GROUP, unitCode, variablePayout, year, month, throughSchduler, sarIdList));
        }
        if (serviceItemTypeListMap.containsKey(Type.PACKAGE_CATEGORY) && serviceItemTypeListMap.get(Type.PACKAGE_CATEGORY) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.PACKAGE_CATEGORY);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.PACKAGE_CATEGORY, unitCode, variablePayout, year, month, throughSchduler, sarIdList));
        }
        if (serviceItemTypeListMap.containsKey(Type.ALL_PACKAGES) && serviceItemTypeListMap.get(Type.ALL_PACKAGES) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.ALL_PACKAGES);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.ALL_PACKAGES, unitCode, variablePayout, year, month, throughSchduler, sarIdList));
        }
        return invoiceDoctorPayoutList;
    }

    @Override
    public List<InvoiceDoctorPayout> processSARReportsForInvoice(Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap, Long employeeId, String unitCode, VariablePayout variablePayout, Integer year, Integer month, Boolean throughSchduler) {
        List<ServiceItemBenefit> serviceItemBenefitList = null;
        Map<ServiceItemBenefit, List<McrStaging>> allMatchRuleServiceItemBenefitListMap = new HashMap<>();
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = new ArrayList<>();
        if (serviceItemTypeListMap.containsKey(Type.INVOICE) && serviceItemTypeListMap.get(Type.INVOICE) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.INVOICE);
            List<Long> sarIdList = null;
            if (!throughSchduler)
                sarIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getSARDetails(serviceItemBenefitList, employeeId, Type.INVOICE, unitCode, variablePayout, year, month, throughSchduler, sarIdList));
        }
        return invoiceDoctorPayoutList;
    }

    @Override
    public List<InvoiceDoctorPayout> processMcrReportsForItem(Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap, Long employeeId, String unitCode, VariablePayout variablePayout, Integer year, Integer month,
                                                              Boolean throughSchduler) {
        List<ServiceItemBenefit> serviceItemBenefitList = null;
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = new ArrayList<>();
        if (serviceItemTypeListMap.containsKey(Type.ITEM_NAME) && serviceItemTypeListMap.get(Type.ITEM_NAME) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.ITEM_NAME);
            List<Long> mcrIdList = null;
            if (!throughSchduler)
                mcrIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getMcrList(serviceItemBenefitList, employeeId, Type.ITEM_NAME, unitCode, variablePayout, year, month,throughSchduler,mcrIdList));
        }
        if (serviceItemTypeListMap.containsKey(Type.ITEM_GROUP) && serviceItemTypeListMap.get(Type.ITEM_GROUP) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.ITEM_GROUP);
            List<Long> mcrIdList = null;
            if (!throughSchduler)
                mcrIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getMcrList(serviceItemBenefitList, employeeId, Type.ITEM_NAME, unitCode, variablePayout, year, month,throughSchduler,mcrIdList));
        }
        if (serviceItemTypeListMap.containsKey(Type.ITEM_CATEGORY) && serviceItemTypeListMap.get(Type.ITEM_CATEGORY) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.ITEM_CATEGORY);
            List<Long> mcrIdList = null;
            if (!throughSchduler)
                mcrIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getMcrList(serviceItemBenefitList, employeeId, Type.ITEM_NAME, unitCode, variablePayout, year, month,throughSchduler,mcrIdList));
        }
        if (serviceItemTypeListMap.containsKey(Type.ALL_ITEMS) && serviceItemTypeListMap.get(Type.ALL_ITEMS) != null) {
            serviceItemBenefitList = serviceItemTypeListMap.get(Type.ALL_ITEMS);
            List<Long> mcrIdList = null;
            if (!throughSchduler)
                mcrIdList = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getSarId()).collect(Collectors.toList());
            invoiceDoctorPayoutList.addAll(getMcrList(serviceItemBenefitList, employeeId, Type.ITEM_NAME, unitCode, variablePayout, year, month,throughSchduler,mcrIdList));
        }
        return invoiceDoctorPayoutList;
    }


    private List<InvoiceDoctorPayout> createInvoiceBaseOnMCRData(Map<ServiceItemBenefit, List<McrStaging>> serviceAnalysisStagingList) {
        Map<String, Department> typeDepartmentCodeMap = new HashMap<>();
        Set<Map.Entry<ServiceItemBenefit, List<McrStaging>>> entries = serviceAnalysisStagingList.entrySet();
        Iterator<Map.Entry<ServiceItemBenefit, List<McrStaging>>> iterator = entries.iterator();
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<ServiceItemBenefit, List<McrStaging>> next = iterator.next();
            ServiceItemBenefit key = next.getKey();
            List<McrStaging> value = next.getValue();
            for (int i = 0; i < value.size(); i++) {
                McrStaging mcrStaging = value.get(i);
                InvoiceDoctorPayout invoiceDoctorPayout = new InvoiceDoctorPayout();
                invoiceDoctorPayout.setName(mcrStaging.getItemName());
                invoiceDoctorPayout.setInvoiceNumber(mcrStaging.getInvoiceNumber());
                invoiceDoctorPayout.setVisitType(mcrStaging.getVisitType());
                invoiceDoctorPayout.setMrn(mcrStaging.getMrn());
                invoiceDoctorPayout.setEmployeeId(mcrStaging.getEmployeeId());
                invoiceDoctorPayout.setVisitType(mcrStaging.getVisitType());
                invoiceDoctorPayout.setIssueDateTime(mcrStaging.getInvoiceDate().toInstant(ZoneOffset.UTC));
                invoiceDoctorPayout.setOrderDateTime(mcrStaging.getOrderDateTime() != null ? mcrStaging.getOrderDateTime().toInstant(ZoneOffset.UTC) : null);
                double percentageValue = 0.0;
                if (key.getMaterialAmount() != null && key.getMaterialAmount().equals(MaterialAmount.COST)) {
                    if (key != null && key.getAmount().contains("%")) {
                        double parseDouble = Double.parseDouble(key.getAmount().replaceAll("%", "").trim());
                        percentageValue = (mcrStaging.getTotalCost() * parseDouble) / 100;
                    } else {
                        percentageValue = Double.parseDouble(key.getAmount());
                    }
                    invoiceDoctorPayout.setServiceBenefietAmount(percentageValue);
                } else if (key.getMaterialAmount() != null && key.getMaterialAmount().equals(MaterialAmount.SALE)) {
                    if (key != null && key.getAmount().contains("%")) {
                        double parseDouble = Double.parseDouble(key.getAmount().replaceAll("%", "").trim());
                        percentageValue = (mcrStaging.getTotalSaleRate() * parseDouble) / 100;
                    } else {
                        percentageValue = Double.parseDouble(key.getAmount());
                    }
                    invoiceDoctorPayout.setServiceBenefietAmount(percentageValue);
                }
                Department department = null;
                if (key.getBeneficiary().equals(Beneficiary.ORDERING)) {
                    Department departmentByCode = null;
                    if (typeDepartmentCodeMap.containsKey(mcrStaging.getOrderingDepartmentCode())) {
                        departmentByCode = typeDepartmentCodeMap.get(mcrStaging.getOrderingDepartmentCode());
                    } else {
                        departmentByCode = departmentRepository.findDepartmentByCode(mcrStaging.getOrderingDepartmentCode());
                        typeDepartmentCodeMap.put(mcrStaging.getOrderingDepartmentCode(), departmentByCode);
                    }
                    invoiceDoctorPayout.setDepartment(departmentByCode);
                }
                if (key.getBeneficiary().equals(Beneficiary.ADMITTING)) {
                    Department departmentByCode = null;
                    if (typeDepartmentCodeMap.containsKey(mcrStaging.getAdmittingDepartmentCode())) {
                        departmentByCode = typeDepartmentCodeMap.get(mcrStaging.getAdmittingDepartmentCode());
                    } else {
                        departmentByCode = departmentRepository.findDepartmentByCode(mcrStaging.getAdmittingDepartmentCode());
                        typeDepartmentCodeMap.put(mcrStaging.getAdmittingDepartmentCode(), departmentByCode);
                    }
                    invoiceDoctorPayout.setDepartment(departmentByCode);
                }
                invoiceDoctorPayout.setDepartment(department);
                invoiceDoctorPayoutList.add(invoiceDoctorPayout);

            }
        }
        return invoiceDoctorPayoutList;

    }

    private List<InvoiceDoctorPayout> getMcrList(List<ServiceItemBenefit> serviceItemBenefitList, Long employeeId, Type type, String unitCode, VariablePayout variablePayout, Integer year, Integer month,
                                                 Boolean throughScheduler,List<Long> sarIdList) {
        int size = serviceItemBenefitList.size();
        Map<ServiceItemBenefit, List<McrStaging>> resultMap = new HashMap<>();
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = null;
        for (int i = 0; i < size; i++) {
            ServiceItemBenefit serviceItemBenefit = serviceItemBenefitList.get(i);
            List<ServiceItemException> serviceItemExceptionList = serviceItemBenefit.getServiceItemExceptions().stream().collect(Collectors.toList());
            log.debug("Request to execute rule : {}", serviceItemBenefit);
            List<String> planNameList = null;
            if (serviceItemBenefit.getExceptionSponsor() != null && serviceItemBenefit.getExceptionSponsor().getPlans() != null && !serviceItemBenefit.getExceptionSponsor().getPlans().isEmpty()) {
                List<Plan> plans = serviceItemBenefit.getExceptionSponsor().getPlans();
                planNameList = new ArrayList<>();
                for (int planIndex = 0; planIndex < plans.size(); planIndex++) {
                    Plan plan = objectMapper.convertValue(plans.get(i), Plan.class);
                    planNameList.add(plan.getName());
                }
            }
            UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
            List<String> finalPlanNameList = planNameList;
            List<McrStaging> mcrStagingList = new ArrayList<>();
            if (type.equals(Type.ITEM_NAME)) {
                mcrStagingList.addAll(serviceAnalysisStagingService.findByCriteriaOnMcr(serviceItemBenefit,variablePayout,year,month,userMaster.getEmployeeNumber(),throughScheduler,null,sarIdList));
            } else if (type.equals(Type.ITEM_GROUP)) {
                mcrStagingList.addAll(serviceAnalysisStagingService.findByCriteriaOnMcr(serviceItemBenefit,variablePayout,year,month,userMaster.getEmployeeNumber(),throughScheduler,null,sarIdList));
            } else if (type.equals(Type.ITEM_CATEGORY)) {
                mcrStagingList.addAll(serviceAnalysisStagingService.findByCriteriaOnMcr(serviceItemBenefit,variablePayout,year,month,userMaster.getEmployeeNumber(),throughScheduler,null,sarIdList));
            } else if (type.equals(Type.ALL_ITEMS)) {
                mcrStagingList.addAll(serviceAnalysisStagingService.findByCriteriaOnMcr(serviceItemBenefit,variablePayout,year,month,userMaster.getEmployeeNumber(),throughScheduler,null,sarIdList));
            }
            if (serviceItemBenefit.getExceptionSponsor() != null && !serviceItemBenefit.getExceptionSponsor().isApplicable()) {
                if (finalPlanNameList != null && !finalPlanNameList.isEmpty()) {
                    Predicate<McrStaging> mcrStagingExcludingPlanPredicate = serviceAnalysisStaging1 -> !finalPlanNameList.contains(serviceAnalysisStaging1.getPlanName());
                    mcrStagingList = mcrStagingList.stream().filter(mcrStagingExcludingPlanPredicate).collect(Collectors.toList());
                }
            }
            if (serviceItemBenefit.getExceptionSponsor() != null && serviceItemBenefit.getExceptionSponsor().isApplicable()) {
                if (finalPlanNameList != null && !finalPlanNameList.isEmpty()) {
                    Predicate<McrStaging> mcrStagingIncludingPlanPredicate = serviceAnalysisStaging1 -> finalPlanNameList.contains(serviceAnalysisStaging1.getPlanName());
                    mcrStagingList = mcrStagingList.stream().filter(mcrStagingIncludingPlanPredicate).collect(Collectors.toList());
                }
            }
            if ((serviceItemExceptionList != null && !serviceItemExceptionList.isEmpty()) ||
                (variablePayout != null && variablePayout.getServiceItemExceptions() != null && !variablePayout.getServiceItemExceptions().isEmpty())
                || (variablePayout != null && variablePayout.getServiceItemBenefitTemplates() != null && !variablePayout.getServiceItemBenefitTemplates().isEmpty())) {
                List<ServiceItemException> itemExceptions = new ArrayList<>();
                List<CodeNameDto> itemNameDtoList = new ArrayList<>();
                List<CodeNameDto> planNameDtoList = new ArrayList<>();
                if (variablePayout != null && variablePayout.getServiceItemExceptions() != null && !variablePayout.getServiceItemExceptions().isEmpty()) {
                    itemExceptions.addAll(variablePayout.getServiceItemExceptions().stream().collect(Collectors.toList()));
                }
                if (serviceItemExceptionList != null && !serviceItemExceptionList.isEmpty()) {
                    itemExceptions.addAll(serviceItemBenefit.getServiceItemExceptions().stream().collect(Collectors.toList()));
                }
                if (variablePayout != null && variablePayout.getServiceItemBenefitTemplates() != null && !variablePayout.getServiceItemBenefitTemplates().isEmpty()) {
                    List<ServiceItemBenefitTemplate> serviceItemBenefitTemplatesList = variablePayout.getServiceItemBenefitTemplates().stream().collect(Collectors.toList());
                    if (serviceItemBenefitTemplatesList != null && !serviceItemBenefitTemplatesList.isEmpty()) {
                        for (int templateExceptionIndex = 0; templateExceptionIndex < serviceItemBenefitTemplatesList.size(); templateExceptionIndex++) {
                            ServiceItemBenefitTemplate serviceItemBenefitTemplate = serviceItemBenefitTemplatesList.get(templateExceptionIndex);
                            if (serviceItemBenefitTemplate.getServiceItemExceptions() != null && !serviceItemBenefitTemplate.getServiceItemExceptions().isEmpty()) {
                                itemExceptions.addAll(serviceItemBenefitTemplate.getServiceItemExceptions().stream().collect(Collectors.toList()));
                            }
                        }
                    }
                }
                for (int exceptionIndex = 0; exceptionIndex < itemExceptions.size(); exceptionIndex++) {
                    ServiceItemException exception = itemExceptions.get(exceptionIndex);
                    if (exception.getExceptionType().equals(ExceptionType.Plan)) {
                        planNameDtoList.add(exception.getValue());
                    } else if (exception.getExceptionType().equals(ExceptionType.ItemWithGeneric)) {
                        itemNameDtoList.add(exception.getValue());
                    } else if (exception.getExceptionType().equals(ExceptionType.ItemWithBrand)) {
                        itemNameDtoList.add(exception.getValue());
                    }
                }
                if (itemNameDtoList != null && !itemNameDtoList.isEmpty()) {
                    Predicate<McrStaging> sarLineLevelItemExceptionPredicate = mcrStaging -> !itemNameDtoList.contains(mcrStaging.getItemCode());
                    mcrStagingList = mcrStagingList.stream().filter(sarLineLevelItemExceptionPredicate).collect(Collectors.toList());
                }
                if (planNameDtoList != null && !planNameDtoList.isEmpty()) {
                    Predicate<McrStaging> sarLineLevelPlanExceptionPredicate = mcrStaging -> !planNameDtoList.contains(mcrStaging.getPlanCode());
                    mcrStagingList = mcrStagingList.stream().filter(sarLineLevelPlanExceptionPredicate).collect(Collectors.toList());
                }
            }
            List<McrStaging> collect = mcrStagingList.stream().map(serviceAnalysisStaging -> serviceAnalysisStaging.executed(true)).collect(Collectors.toList());
            if (collect != null && !collect.isEmpty())
                log.debug("matched rules size : {}", collect.size());
            List<McrStaging> mcrStagingList1 = updateMcr(collect);
            resultMap.put(serviceItemBenefit, mcrStagingList1);
            Map<Long, Map<ServiceItemBenefit, List<McrStaging>>> doctorBySAR = new HashMap<>();
            doctorBySAR.put(employeeId, resultMap);
            invoiceDoctorPayoutList = saveVariablePayoutDataForMcr(doctorBySAR, unitCode, year, month);
        }
        return invoiceDoctorPayoutList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private List<McrStaging> updateMcr(List<McrStaging> mcrStagingrList) {
        return mcrStagingRepository.saveAll(mcrStagingrList);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<InvoiceDoctorPayout> getSARDetails(List<ServiceItemBenefit> serviceItemBenefitList, Long employeeId, Type type, String unitCode, VariablePayout variablePayout, Integer year, Integer month,
                                                   Boolean throughSchduler, List<Long> sarIdList) {
        int size = serviceItemBenefitList.size();
        String packageOrServiceType = "";
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Map<ServiceItemBenefit, List<ServiceAnalysisStaging>> resultMap = new HashMap<>();
            ServiceItemBenefit serviceItemBenefit = serviceItemBenefitList.get(i);
            log.debug("Request to execute rule : {} ", serviceItemBenefit);
            List<ServiceItemException> serviceItemExceptionList = serviceItemBenefit.getServiceItemExceptions().stream().collect(Collectors.toList());
            List<String> planNameList = null;
            if (serviceItemBenefit.getExceptionSponsor() != null && serviceItemBenefit.getExceptionSponsor().getPlans() != null && !serviceItemBenefit.getExceptionSponsor().getPlans().isEmpty()) {
                List<Plan> plans = serviceItemBenefit.getExceptionSponsor().getPlans();
                planNameList = new ArrayList<>();
                for (int planIndex = 0; planIndex < plans.size(); planIndex++) {
                    Plan plan = objectMapper.convertValue(plans.get(i), Plan.class);
                    planNameList.add(plan.getCode());
                }
            }
            List<String> finalPlanNameList = planNameList;
            List<ServiceAnalysisStaging> analysisStagingList = new ArrayList<>();
            if (type.equals(Type.PACKAGE_NAME) || type.equals(Type.SERVICE_NAME)) {
                if (type.equals(Type.PACKAGE_NAME)) {
                    packageOrServiceType = "PACKAGE";
                } else if (type.equals(Type.SERVICE_NAME)) {
                    packageOrServiceType = "SERVICE";
                }
                UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
                String employeeNumber = userMaster.getEmployeeNumber();
                analysisStagingList.addAll(serviceAnalysisStagingService.findByCriteriaForSar(serviceItemBenefit, variablePayout, year, month, employeeNumber, throughSchduler, packageOrServiceType, sarIdList));
            }
            if (type.equals(Type.SERVICE_GROUP) || type.equals(Type.PACKAGE_GROUP)) {
                if (type.equals(Type.PACKAGE_GROUP)) {
                    packageOrServiceType = "PACKAGE";
                } else if (type.equals(Type.SERVICE_GROUP)) {
                    packageOrServiceType = "SERVICE";
                }
                UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
                String employeeNumber = userMaster.getEmployeeNumber();
                analysisStagingList.addAll(serviceAnalysisStagingService.findByCriteriaForSar(serviceItemBenefit, variablePayout, year, month, employeeNumber, throughSchduler, packageOrServiceType, sarIdList));
            }
            if (type.equals(Type.SERVICE_TYPE)) {
                UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
                String employeeNumber = userMaster.getEmployeeNumber();
                if (type.equals(Type.SERVICE_TYPE)) {
                    packageOrServiceType = "SERVICE";
                }
                analysisStagingList.addAll(serviceAnalysisStagingService.findByCriteriaForSar(serviceItemBenefit, variablePayout, year, month, employeeNumber, throughSchduler, packageOrServiceType, sarIdList));
            }
            if (type.equals(Type.SERVICE_INSIDE_PACKAGE)) {
                UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
                String employeeNumber = userMaster.getEmployeeNumber();
                if (type.equals(Type.SERVICE_INSIDE_PACKAGE)) {
                    packageOrServiceType = "SERVICE";
                }
                analysisStagingList.addAll(serviceAnalysisStagingService.findByCriteriaForSar(serviceItemBenefit, variablePayout, year, month, employeeNumber, throughSchduler, packageOrServiceType, sarIdList));
            }
            if (type.equals(Type.PACKAGE_CATEGORY)) {
                UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
                String employeeNumber = userMaster.getEmployeeNumber();
                if (type.equals(Type.PACKAGE_CATEGORY)) {
                    packageOrServiceType = "PACKAGE";
                }
                analysisStagingList.addAll(serviceAnalysisStagingService.findByCriteriaForSar(serviceItemBenefit, variablePayout, year, month, employeeNumber, throughSchduler, packageOrServiceType, sarIdList));

            }
            if (type.equals(Type.ALL_SERVICES) || type.equals(Type.ALL_PACKAGES)) {
                UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
                String employeeNumber = userMaster.getEmployeeNumber();
                if (type.equals(Type.ALL_SERVICES)) {
                    packageOrServiceType = "SERVICE";
                } else if (type.equals(Type.ALL_PACKAGES)) {
                    packageOrServiceType = "PACKAGE";
                }
                analysisStagingList.addAll(serviceAnalysisStagingService.findByCriteriaForSar(serviceItemBenefit, variablePayout, year, month, employeeNumber, throughSchduler, packageOrServiceType, sarIdList));

            }
            if (type.equals(Type.INVOICE)) {
                UserMaster userMaster = userMasterRepository.findById(employeeId).orElse(null);
                String employeeNumber = userMaster.getEmployeeNumber();
                analysisStagingList.addAll(serviceAnalysisStagingService.findByCriteriaForSar(serviceItemBenefit, variablePayout, year, month, employeeNumber, throughSchduler, packageOrServiceType, sarIdList));
            }
            if (serviceItemBenefit.getExceptionSponsor() != null && serviceItemBenefit.getExceptionSponsor().isApplicable()) {
                if (finalPlanNameList != null && !finalPlanNameList.isEmpty()) {
                    Predicate<ServiceAnalysisStaging> serviceAnalysisStagingIncludingPlanPredicate = serviceAnalysisStaging1 -> finalPlanNameList.contains(serviceAnalysisStaging1.getPlanCode());
                    analysisStagingList = analysisStagingList.stream().filter(serviceAnalysisStagingIncludingPlanPredicate).collect(Collectors.toList());
                }
            }
            if (serviceItemBenefit.getDepartment() != null && !serviceItemBenefit.getDepartment().isEmpty()) {
                List<Department> department = serviceItemBenefit.getDepartment();
                Predicate<ServiceAnalysisStaging> serviceAnalysisStagingIncludingDepartmentPredicate = null;
                if (serviceItemBenefit.getBeneficiary().equals(Beneficiary.ORDERING)) {
                    serviceAnalysisStagingIncludingDepartmentPredicate = serviceAnalysisStaging1 -> department.contains(serviceAnalysisStaging1.getOrderingDepartmentCode());
                }
                if (serviceItemBenefit.getBeneficiary().equals(Beneficiary.RENDERING)) {
                    serviceAnalysisStagingIncludingDepartmentPredicate = serviceAnalysisStaging1 -> department.contains(serviceAnalysisStaging1.getRenderingDepartmentCode());
                }
                if (serviceItemBenefit.getBeneficiary().equals(Beneficiary.ADMITTING)) {
                    serviceAnalysisStagingIncludingDepartmentPredicate = serviceAnalysisStaging1 -> department.contains(serviceAnalysisStaging1.getAdmittingDepartmentCode());
                }
                if (serviceAnalysisStagingIncludingDepartmentPredicate != null) {
                    analysisStagingList = analysisStagingList.stream().filter(serviceAnalysisStagingIncludingDepartmentPredicate).collect(Collectors.toList());
                }
            }

            if ((serviceItemExceptionList != null && !serviceItemExceptionList.isEmpty()) ||
                (variablePayout != null && variablePayout.getServiceItemExceptions() != null && !variablePayout.getServiceItemExceptions().isEmpty())
                || (variablePayout != null && variablePayout.getServiceItemBenefitTemplates() != null && !variablePayout.getServiceItemBenefitTemplates().isEmpty())) {
                List<ServiceItemException> itemExceptions = new ArrayList<>();
                List<CodeNameDto> serviceNameDtoList = new ArrayList<>();
                List<CodeNameDto> planNameDtoList = new ArrayList<>();
                List<CodeNameDto> serviceGroupDtoList = new ArrayList<>();
                if (variablePayout != null && variablePayout.getServiceItemExceptions() != null && !variablePayout.getServiceItemExceptions().isEmpty()) {
                    itemExceptions.addAll(variablePayout.getServiceItemExceptions().stream().collect(Collectors.toList()));
                }
                if (serviceItemExceptionList != null && !serviceItemExceptionList.isEmpty()) {
                    itemExceptions.addAll(serviceItemBenefit.getServiceItemExceptions().stream().collect(Collectors.toList()));
                }
                if (variablePayout != null && variablePayout.getServiceItemBenefitTemplates() != null && !variablePayout.getServiceItemBenefitTemplates().isEmpty()) {
                    List<ServiceItemBenefitTemplate> serviceItemBenefitTemplatesList = variablePayout.getServiceItemBenefitTemplates().stream().collect(Collectors.toList());
                    if (serviceItemBenefitTemplatesList != null && !serviceItemBenefitTemplatesList.isEmpty()) {
                        for (int templateExceptionIndex = 0; templateExceptionIndex < serviceItemBenefitTemplatesList.size(); templateExceptionIndex++) {
                            ServiceItemBenefitTemplate serviceItemBenefitTemplate = serviceItemBenefitTemplatesList.get(templateExceptionIndex);
                            if (serviceItemBenefitTemplate.getServiceItemExceptions() != null && !serviceItemBenefitTemplate.getServiceItemExceptions().isEmpty()) {
                                itemExceptions.addAll(serviceItemBenefitTemplate.getServiceItemExceptions().stream().collect(Collectors.toList()));
                            }
                        }
                    }
                }
                for (int exceptionIndex = 0; exceptionIndex < itemExceptions.size(); exceptionIndex++) {
                    ServiceItemException exception = itemExceptions.get(exceptionIndex);
                    if (exception.getExceptionType().equals(ExceptionType.Plan)) {
                        planNameDtoList.add(exception.getValue());
                    } else if (exception.getExceptionType().equals(ExceptionType.Service)) {
                        serviceNameDtoList.add(exception.getValue());
                    } else if (exception.getExceptionType().equals(ExceptionType.ServiceGroup)) {
                        serviceGroupDtoList.add(exception.getValue());
                    }
                }
                if (serviceNameDtoList != null && !serviceNameDtoList.isEmpty()) {
                    Predicate<ServiceAnalysisStaging> sarLineLevelServiceExceptionPredicate = serviceAnalysisStaging1 -> !serviceNameDtoList.contains(serviceAnalysisStaging1.getItemName());
                    analysisStagingList = analysisStagingList.stream().filter(sarLineLevelServiceExceptionPredicate).collect(Collectors.toList());
                }
                if (serviceGroupDtoList != null && !serviceGroupDtoList.isEmpty()) {
                    Predicate<ServiceAnalysisStaging> sarLineLevelServiceGroupExceptionPredicate = serviceAnalysisStaging1 -> !serviceGroupDtoList.contains(serviceAnalysisStaging1.getItemGroup());
                    analysisStagingList = analysisStagingList.stream().filter(sarLineLevelServiceGroupExceptionPredicate).collect(Collectors.toList());
                }
                if (planNameDtoList != null && !planNameDtoList.isEmpty()) {
                    Predicate<ServiceAnalysisStaging> sarLineLevelPlanExceptionPredicate = serviceAnalysisStaging1 -> !serviceGroupDtoList.contains(serviceAnalysisStaging1.getPlanCode());
                    analysisStagingList = analysisStagingList.stream().filter(sarLineLevelPlanExceptionPredicate).collect(Collectors.toList());
                }
            }
            List<ServiceAnalysisStaging> collect = analysisStagingList.stream().map(serviceAnalysisStaging -> {
                if (serviceItemBenefit.getBeneficiary().equals(Beneficiary.ORDERING)) {
                    return serviceAnalysisStaging.orderingDoctorExecuted(true);

                } else if (serviceItemBenefit.getBeneficiary().equals(Beneficiary.ADMITTING)) {
                    return serviceAnalysisStaging.admittingDoctorExecuted(true);

                } else if (serviceItemBenefit.getBeneficiary().equals(Beneficiary.RENDERING)) {
                    return serviceAnalysisStaging.renderingDoctorExecuted(true);
                } else {
                    return serviceAnalysisStaging.executed(true);
                }
            }).collect(Collectors.toList());
            List<ServiceAnalysisStaging> updateSar = serviceAnalysisStagingService.saveAll(collect);
            if (updateSar != null & !updateSar.isEmpty() && serviceItemBenefit.getMinQuantity() != null && serviceItemBenefit.getMaxQuantity() != null) {
                updateSar = updateSar.subList(serviceItemBenefit.getMinQuantity() - 1, serviceItemBenefit.getMaxQuantity() + 1 > analysisStagingList.size() ? analysisStagingList.size() : serviceItemBenefit.getMaxQuantity() + 1);
            }
            resultMap.put(serviceItemBenefit, updateSar);
            Map<Long, Map<ServiceItemBenefit, List<ServiceAnalysisStaging>>> doctorBySAR = new HashMap<>();
            doctorBySAR.put(employeeId, resultMap);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutList1 = saveVariablePayoutData(doctorBySAR, unitCode, year, month);
            invoiceDoctorPayoutList.addAll(invoiceDoctorPayoutList1);
        }
        return invoiceDoctorPayoutList;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<DoctorPayoutAdjustment> calculatePayoutAdjustment(String unitCode, DoctorPayout doctorPayoutResult) {
        LocalDate localDate = LocalDate.of(doctorPayoutResult.getYear(), doctorPayoutResult.getMonth(), 1);
        LocalDate localDateTo = LocalDate.of(doctorPayoutResult.getYear(), doctorPayoutResult.getMonth(), 25);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
        LocalDateTime localDateTime1 = LocalDateTime.of(localDateTo, LocalTime.of(23, 59, 59));
        List<DoctorPayoutAdjustment> doctorPayoutAdjustmentList = new ArrayList<>();
        PayoutAdjustment latestEmployeePayoutAdjustment = payoutAdjustmentRepository.getLatestEmployeePayout(localDateTime, localDateTime1, doctorPayoutResult.getEmployeeId(), doctorPayoutResult.getUnitCode(), "APPROVED");
        if (latestEmployeePayoutAdjustment != null) {
            List<PayoutAdjustmentDetails> payoutAdjustmentDetailsList = latestEmployeePayoutAdjustment.getPayoutAdjustmentDetails().stream().collect(Collectors.toList());
            payoutAdjustmentDetailsList.stream().filter(payoutAdjustmentDetails -> payoutAdjustmentDetails.getExecuted() != null).filter(payoutAdjustmentDetails -> !payoutAdjustmentDetails.getExecuted()).forEach(payoutAdjustmentDetails -> {
                if (payoutAdjustmentDetails.getSign().equalsIgnoreCase("POSITIVE")) {
                    if (payoutAdjustmentDetails.getContraEmployeeId() != null) {
                        UserMaster byEmployeeNo = userMasterRepository.findByEmployeeNo(payoutAdjustmentDetails.getContraEmployeeId().toString());
                        DoctorPayout doctorByYearAndMonth = doctorPayoutRepository.getDoctorByYearAndMonth(byEmployeeNo.getId(), localDate.getYear(), localDate.getDayOfMonth());
                        if (doctorByYearAndMonth == null) {
                            DoctorPayout doctorPayout = new DoctorPayout();
                            doctorPayout.setEmployeeId(payoutAdjustmentDetails.getContraEmployeeId());
                            doctorPayout.setMonth(LocalDateTime.now().getMonthValue());
                            doctorPayout.setYear(LocalDateTime.now().getYear());
                            doctorPayout.setunitCode(unitCode);
                            Double adjustedValue = 0 - payoutAdjustmentDetails.getAmount().doubleValue();
                            doctorPayout.setServiceItemBenefitAmount(adjustedValue.longValue());
                            doctorByYearAndMonth = doctorPayoutService.save(doctorPayout);

                        } else {
                            Double adjustedValue = doctorByYearAndMonth.getServiceItemBenefitAmount() - payoutAdjustmentDetails.getAmount().doubleValue();
                            doctorByYearAndMonth.setServiceItemBenefitAmount(adjustedValue.longValue());
                        }
                        DoctorPayoutAdjustment doctorPayoutAdjustment = new DoctorPayoutAdjustment();
                        doctorPayoutAdjustment.setAdjustmentConfigurationId(payoutAdjustmentDetails.getId());
                        doctorPayoutAdjustment.setAmount(payoutAdjustmentDetails.getAmount().doubleValue());
                        doctorPayoutAdjustment.setDescription(payoutAdjustmentDetails.getDescription());
                        doctorPayoutAdjustment.setDoctorPayout(doctorPayoutResult);
                        doctorPayoutService.save(doctorByYearAndMonth);
                        doctorPayoutAdjustmentList.add(doctorPayoutAdjustmentRepository.save(doctorPayoutAdjustment));
                    } else {
                        Double adjustedValue = doctorPayoutResult.getServiceItemBenefitAmount() + payoutAdjustmentDetails.getAmount().doubleValue();
                        doctorPayoutResult.setServiceItemBenefitAmount(adjustedValue.longValue());
                        DoctorPayoutAdjustment doctorPayoutAdjustment = new DoctorPayoutAdjustment();
                        doctorPayoutAdjustment.setAdjustmentConfigurationId(payoutAdjustmentDetails.getId());
                        doctorPayoutAdjustment.setAmount(payoutAdjustmentDetails.getAmount().doubleValue());
                        doctorPayoutAdjustment.setDescription(payoutAdjustmentDetails.getDescription());
                        doctorPayoutAdjustment.setDoctorPayout(doctorPayoutResult);
                        doctorPayoutService.save(doctorPayoutResult);
                        doctorPayoutAdjustmentList.add(doctorPayoutAdjustmentRepository.save(doctorPayoutAdjustment));
                    }

                } else if (payoutAdjustmentDetails.getSign().equalsIgnoreCase("NEGATIVE")) {
                    if (payoutAdjustmentDetails.getContraEmployeeId() != null) {
                        DoctorPayout doctorByYearAndMonth = doctorPayoutRepository.getDoctorByYearAndMonth(payoutAdjustmentDetails.getContraEmployeeId(), localDate.getYear(), localDate.getDayOfMonth());
                        if (doctorByYearAndMonth == null) {
                            DoctorPayout doctorPayout = new DoctorPayout();
                            doctorPayout.setEmployeeId(payoutAdjustmentDetails.getContraEmployeeId());
                            doctorPayout.setMonth(LocalDateTime.now().getMonthValue());
                            doctorPayout.setYear(LocalDateTime.now().getYear());
                            doctorPayout.setunitCode(unitCode);
                            Double adjustedValue = 0 + payoutAdjustmentDetails.getAmount().doubleValue();
                            doctorPayout.setServiceItemBenefitAmount(adjustedValue.longValue());
                            doctorByYearAndMonth = doctorPayoutService.save(doctorPayout);

                        } else {
                            Double adjustedValue = doctorByYearAndMonth.getServiceItemBenefitAmount() + payoutAdjustmentDetails.getAmount().doubleValue();
                            doctorByYearAndMonth.setServiceItemBenefitAmount(adjustedValue.longValue());
                        }
                        DoctorPayoutAdjustment doctorPayoutAdjustment = new DoctorPayoutAdjustment();
                        doctorPayoutAdjustment.setAdjustmentConfigurationId(payoutAdjustmentDetails.getId());
                        doctorPayoutAdjustment.setAmount(payoutAdjustmentDetails.getAmount().doubleValue());
                        doctorPayoutAdjustment.setDescription(payoutAdjustmentDetails.getDescription());
                        doctorPayoutAdjustment.setDoctorPayout(doctorByYearAndMonth);
                        doctorPayoutService.save(doctorByYearAndMonth);
                        doctorPayoutAdjustmentList.add(doctorPayoutAdjustmentRepository.save(doctorPayoutAdjustment));
                    } else {
                        Double adjustedValue = doctorPayoutResult.getServiceItemBenefitAmount() - payoutAdjustmentDetails.getAmount().doubleValue();
                        doctorPayoutResult.setServiceItemBenefitAmount(adjustedValue.longValue());
                        DoctorPayoutAdjustment doctorPayoutAdjustment = new DoctorPayoutAdjustment();
                        doctorPayoutAdjustment.setAdjustmentConfigurationId(payoutAdjustmentDetails.getId());
                        doctorPayoutAdjustment.setAmount(payoutAdjustmentDetails.getAmount().doubleValue());
                        doctorPayoutAdjustment.setDescription(payoutAdjustmentDetails.getDescription());
                        doctorPayoutAdjustment.setDoctorPayout(doctorPayoutResult);
                        doctorPayoutService.save(doctorPayoutResult);
                        doctorPayoutAdjustmentList.add(doctorPayoutAdjustmentRepository.save(doctorPayoutAdjustment));
                    }


                }
                payoutAdjustmentDetails.setExecuted(true);
                payoutAdjustmentDetailsRepository.save(payoutAdjustmentDetails);
            });
        }
        return doctorPayoutAdjustmentList;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void calculateDepartmentPayoutAdjustment(String unitCode, DepartmentRevenueCalculation departmentRevenueCalculation) {
        LocalDate localDate = LocalDate.of(departmentRevenueCalculation.getYear(), departmentRevenueCalculation.getMonth(), 1);
        LocalDate localDateTo = LocalDate.of(departmentRevenueCalculation.getYear(), departmentRevenueCalculation.getMonth(), 25);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
        LocalDateTime localDateTime1 = LocalDateTime.of(localDateTo, LocalTime.of(23, 59, 59));
        PayoutAdjustment latestEmployeePayoutAdjustment = payoutAdjustmentRepository.getLatestDepartmentPayout(localDateTime, localDateTime1, departmentRevenueCalculation.getDepartmentId(), departmentRevenueCalculation.getUnitCode(), "APPROVED");
        if (latestEmployeePayoutAdjustment != null) {
            List<PayoutAdjustmentDetails> payoutAdjustmentDetailsList = latestEmployeePayoutAdjustment.getPayoutAdjustmentDetails().stream().collect(Collectors.toList());
            payoutAdjustmentDetailsList.stream().filter(payoutAdjustmentDetails -> payoutAdjustmentDetails.getDepartmentExecuted() != null).filter(payoutAdjustmentDetails -> !payoutAdjustmentDetails.getDepartmentExecuted()).forEach(payoutAdjustmentDetails -> {
                if (payoutAdjustmentDetails.getSign().equalsIgnoreCase("POSITIVE")) {
                    if (payoutAdjustmentDetails.getContraDepartment() != null) {
                        DepartmentRevenueCalculation deptRevByYearAndMonth = departmentRevenueCalculationRepository.getDeptRevCalByYearAndMonth(payoutAdjustmentDetails.getContraDepartment().getId(), localDate.getYear(), localDate.getDayOfMonth());
                        if (deptRevByYearAndMonth == null) {
                            DepartmentRevenueCalculation deptRevCal = new DepartmentRevenueCalculation();
                            deptRevCal.setYear(LocalDateTime.now().getYear());
                            deptRevCal.setMonth(LocalDateTime.now().getMonthValue());
                            deptRevCal.setDeptCode(payoutAdjustmentDetails.getContraDepartment().getCode());
                            deptRevCal.setDepartmentId(payoutAdjustmentDetails.getContraDepartment().getId());
                            deptRevCal.setUnitCode(unitCode);
                            Double adjustedValue = 0 - payoutAdjustmentDetails.getAmount().doubleValue();
                            deptRevCal.setSum(adjustedValue);
                            deptRevByYearAndMonth = departmentRevenueCalculationRepository.save(deptRevCal);
                        } else {
                            Double adjustedValue = deptRevByYearAndMonth.getSum() - payoutAdjustmentDetails.getAmount().doubleValue();
                            deptRevByYearAndMonth.setSum(adjustedValue);
                        }
                        DepartmentPayoutAdjustment departmentPayoutAdjustment = new DepartmentPayoutAdjustment();
                        departmentPayoutAdjustment.setAdjustmentConfigurationId(payoutAdjustmentDetails.getId());
                        departmentPayoutAdjustment.setAmount(payoutAdjustmentDetails.getAmount().doubleValue());
                        departmentPayoutAdjustment.setDescription(payoutAdjustmentDetails.getDescription());
                        departmentPayoutAdjustment.setDepartmentRevenueCalculation(departmentRevenueCalculation);
                        departmentRevenueCalculationRepository.save(deptRevByYearAndMonth);
                        departmentPayoutAdjustmentRepository.save(departmentPayoutAdjustment);
                    } else {
                        Double adjustedValue = departmentRevenueCalculation.getSum() + payoutAdjustmentDetails.getAmount().doubleValue();
                        departmentRevenueCalculation.setSum(adjustedValue);
                        DepartmentPayoutAdjustment departmentPayoutAdjustment = new DepartmentPayoutAdjustment();
                        departmentPayoutAdjustment.setAdjustmentConfigurationId(payoutAdjustmentDetails.getId());
                        departmentPayoutAdjustment.setAmount(payoutAdjustmentDetails.getAmount().doubleValue());
                        departmentPayoutAdjustment.setDescription(payoutAdjustmentDetails.getDescription());
                        departmentPayoutAdjustment.setDepartmentRevenueCalculation(departmentRevenueCalculation);
                        departmentRevenueCalculationRepository.save(departmentRevenueCalculation);
                        departmentPayoutAdjustmentRepository.save(departmentPayoutAdjustment);
                    }

                } else if (payoutAdjustmentDetails.getSign().equalsIgnoreCase("NEGATIVE")) {
                    if (payoutAdjustmentDetails.getContraDepartment() != null) {
                        DepartmentRevenueCalculation deptRevByYearAndMonth = departmentRevenueCalculationRepository.getDeptRevCalByYearAndMonth(payoutAdjustmentDetails.getContraDepartment().getId(), localDate.getYear(), localDate.getDayOfMonth());
                        if (deptRevByYearAndMonth == null) {
                            DepartmentRevenueCalculation deptRevCal = new DepartmentRevenueCalculation();
                            deptRevCal.setYear(LocalDateTime.now().getYear());
                            deptRevCal.setMonth(LocalDateTime.now().getMonthValue());
                            deptRevCal.setDeptCode(payoutAdjustmentDetails.getContraDepartment().getCode());
                            deptRevCal.setDepartmentId(payoutAdjustmentDetails.getContraDepartment().getId());
                            deptRevCal.setUnitCode(unitCode);
                            Double adjustedValue = 0 + payoutAdjustmentDetails.getAmount().doubleValue();
                            deptRevCal.setSum(adjustedValue);
                            deptRevByYearAndMonth = departmentRevenueCalculationRepository.save(deptRevCal);
                        } else {
                            Double adjustedValue = deptRevByYearAndMonth.getSum() + payoutAdjustmentDetails.getAmount().doubleValue();
                            deptRevByYearAndMonth.setSum(adjustedValue);
                        }
                        DepartmentPayoutAdjustment departmentPayoutAdjustment = new DepartmentPayoutAdjustment();
                        departmentPayoutAdjustment.setAdjustmentConfigurationId(payoutAdjustmentDetails.getId());
                        departmentPayoutAdjustment.setAmount(payoutAdjustmentDetails.getAmount().doubleValue());
                        departmentPayoutAdjustment.setDescription(payoutAdjustmentDetails.getDescription());
                        departmentPayoutAdjustment.setDepartmentRevenueCalculation(departmentRevenueCalculation);
                        departmentRevenueCalculationRepository.save(deptRevByYearAndMonth);
                        departmentPayoutAdjustmentRepository.save(departmentPayoutAdjustment);
                    } else {
                        Double adjustedValue = departmentRevenueCalculation.getSum() - payoutAdjustmentDetails.getAmount().doubleValue();
                        departmentRevenueCalculation.setSum(adjustedValue);
                        DepartmentPayoutAdjustment departmentPayoutAdjustment = new DepartmentPayoutAdjustment();
                        departmentPayoutAdjustment.setAdjustmentConfigurationId(payoutAdjustmentDetails.getId());
                        departmentPayoutAdjustment.setAmount(payoutAdjustmentDetails.getAmount().doubleValue());
                        departmentPayoutAdjustment.setDescription(payoutAdjustmentDetails.getDescription());
                        departmentPayoutAdjustment.setDepartmentRevenueCalculation(departmentRevenueCalculation);
                        departmentRevenueCalculationRepository.save(departmentRevenueCalculation);
                        departmentPayoutAdjustmentRepository.save(departmentPayoutAdjustment);
                    }
                }
                payoutAdjustmentDetails.setDepartmentExecuted(true);
                payoutAdjustmentDetailsRepository.save(payoutAdjustmentDetails);
            });
        }

    }

    @Override
    @Transactional
    public void executeDepartmentRevenueCalculation(DepartmentPayout departmentPayout) {
        //departmentPayout based on unit code
        //SAR report query based on unit and depot based on config
        log.debug("Request to  executeDepartmentRevenueCalculation", departmentPayout);
        List<Long> collect = departmentPayout.getApplicableConsultants().stream().map(applicableConsultant -> applicableConsultant.getUserMasterId()).collect(Collectors.toList());
        BigDecimal totalDepartmentRevenue = new BigDecimal(0);
        Department department = departmentSearchRepository.findById(departmentPayout.getDepartmentId()).orElse(null);

        if (department != null) {
            if (departmentPayout.getCustomDepartment()) {
                if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                    totalDepartmentRevenue = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenueForConsultant(departmentPayout.getUnitCode(), Arrays.asList(departmentPayout.getVisitType().split(",")), collect);
                }
                if (departmentPayout.getNetGross().equals(NetGross.GROSS)) {
                    totalDepartmentRevenue = serviceAnalysisRepository.allGrossServiceAnalysisForDepartmentRevenueForConsultant(departmentPayout.getUnitCode(), Arrays.asList(departmentPayout.getVisitType().split(",")), collect);
                }
            } else {
                if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                    if (departmentPayout.getApplicableInvoice().equals(ApplicableInvoices.ALL_INVOICES)) {
                        totalDepartmentRevenue = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenue(departmentPayout.getUnitCode(), department.getCode(), Arrays.asList(departmentPayout.getVisitType().split(",")));
                    } else {
                        totalDepartmentRevenue = serviceAnalysisRepository.allServiceAnalysisForDepartmentRevenueOnInvoiceType(departmentPayout.getUnitCode(), department.getCode(), departmentPayout.getApplicableInvoice().getName(), Arrays.asList(departmentPayout.getVisitType().split(",")));
                    }
                }
                if (departmentPayout.getNetGross().equals(NetGross.GROSS)) {
                    if (departmentPayout.getApplicableInvoice().equals(ApplicableInvoices.ALL_INVOICES)) {
                        totalDepartmentRevenue = serviceAnalysisRepository.totalGrossAmountForDepartmentRevenue(departmentPayout.getUnitCode(), department.getCode(), Arrays.asList(departmentPayout.getVisitType().split(",")));
                    } else {
                        totalDepartmentRevenue = serviceAnalysisRepository.totalGrossAmountForDepartmentRevenueOnInvoiceType(departmentPayout.getUnitCode(), department.getCode(), departmentPayout.getApplicableInvoice().getName(), Arrays.asList(departmentPayout.getVisitType().split(",")));
                    }
                }
            }
            if (totalDepartmentRevenue != null) {
                BigDecimal material = new BigDecimal(0);
                if (departmentPayout.isAllMaterials()) {
                    if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                        material = mcrStagingRepository.calcualateNetAmounttMaterailReduction(department.getCode(), departmentPayout.getUnitCode(), "Material", Arrays.asList(departmentPayout.getVisitType().split(",")));
                        mcrStagingRepository.updateDepartmentMcr(departmentPayout.getUnitCode(), "Material", Arrays.asList(departmentPayout.getVisitType().split(",")));
                    } else if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                        material = mcrStagingRepository.calcualateGrossAmounttMaterailReduction(department.getCode(), departmentPayout.getUnitCode(), "Material", Arrays.asList(departmentPayout.getVisitType().split(",")));
                        mcrStagingRepository.updateDepartmentMcr(departmentPayout.getUnitCode(), "Material", Arrays.asList(departmentPayout.getVisitType().split(",")));
                    }
                }
                if (departmentPayout.isDrugs()) {
                    if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                        material = mcrStagingRepository.calcualateNetAmounttMaterailReduction(department.getCode(), departmentPayout.getUnitCode(), "Drug", Arrays.asList(departmentPayout.getVisitType().split(",")));
                    } else if (departmentPayout.getNetGross().equals(NetGross.NET)) {
                        material = mcrStagingRepository.calcualateGrossAmounttMaterailReduction(department.getCode(), departmentPayout.getUnitCode(), "Drug", Arrays.asList(departmentPayout.getVisitType().split(",")));
                    }

                    mcrStagingRepository.updateDepartmentMcr(departmentPayout.getUnitCode(), "Drug", Arrays.asList(departmentPayout.getVisitType().split(",")));
                }
                if (departmentPayout.isConsumables()) {
                }
                if (departmentPayout.isDeptConsumption()) {
                    //calculate mcr for specific department
                }
                double value = totalDepartmentRevenue.doubleValue() - material.doubleValue();
                DepartmentRevenueCalculation departmentRevenueCalculation = departmentRevenueCalculationRepository.getDeptRevCalByYearAndMonth(department.getId(), LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
                if (null == departmentRevenueCalculation) {
                    departmentRevenueCalculation = new DepartmentRevenueCalculation();
                    departmentRevenueCalculation.setYear(LocalDateTime.now().getYear());
                    departmentRevenueCalculation.setMonth(LocalDateTime.now().getMonthValue());
                    departmentRevenueCalculation.setDeptCode(department.getCode());
                    departmentRevenueCalculation.setDepartmentId(department.getId());
                    departmentRevenueCalculation.setUnitCode(departmentPayout.getUnitCode());
                    departmentRevenueCalculation.setSum(totalDepartmentRevenue.doubleValue());
                    departmentRevenueCalculation = departmentRevenueCalculationRepository.save(departmentRevenueCalculation);
                }
                this.calculateDepartmentPayoutAdjustment(departmentPayout.getUnitCode(), departmentRevenueCalculation);
                material = new BigDecimal(value);
                calculateDepartmentPayout(departmentPayout, material);
                serviceAnalysisRepository.updateServiceAnalysisForDepartmentRevenue(departmentPayout.getUnitCode(), department.getCode(), Arrays.asList(departmentPayout.getVisitType().split(",")));
            }
        } else {
            log.debug("DEPARTMENT NOT FOUND FOR :::: {} " + departmentPayout.getDepartmentId() + " ::::: AND Unit Code ::: {}" + departmentPayout.getUnitCode());
        }

    }


    private void calculateDepartmentPayout(DepartmentPayout departmentPayoutByUnitAndDepartment, BigDecimal totalDepartmentRevenue) {
        log.debug("Request to  calculateDepartmentPayout", departmentPayoutByUnitAndDepartment, departmentPayoutByUnitAndDepartment);
        List<Long> applicableConsultantIdsList = departmentPayoutByUnitAndDepartment.getApplicableConsultants().stream().map(applicableConsultant -> applicableConsultant.getUserMasterId()).collect(Collectors.toList());
        List<DoctorPayout> doctorsListByEmpIds = doctorPayoutRepository.getDoctorsListByEmpIds(applicableConsultantIdsList, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
        List<Long> availableDoctorPayoutEmIdsList = doctorsListByEmpIds.stream().map(doctorPayout -> doctorPayout.getEmployeeId()).collect(Collectors.toList());
        List<Long> collect1 = applicableConsultantIdsList.stream().filter(empIds -> !availableDoctorPayoutEmIdsList.contains(empIds)).collect(Collectors.toList());
        List<DoctorPayout> createDoctorPayoutList = new ArrayList<>();
        Set<PayoutRange> payoutRanges = departmentPayoutByUnitAndDepartment.getPayoutRanges();
        PayoutRange payoutRangesAfterFiltering = payoutRanges.stream().
            filter(payoutRange -> payoutRange.getFromAmount().doubleValue() < totalDepartmentRevenue.doubleValue() && payoutRange.getToAmount().doubleValue() > totalDepartmentRevenue.doubleValue()).findFirst().orElse(null);
        Float percentage = 0.0f;
        if (payoutRangesAfterFiltering != null && payoutRangesAfterFiltering.getPercentage() != 0.0) {
            percentage = payoutRangesAfterFiltering.getPercentage();
        }
        Double totalDepartmentCalculatedValue = (totalDepartmentRevenue.doubleValue() * percentage) / 100;
        for (int createDoctorPayoutIndex = 0; createDoctorPayoutIndex < collect1.size(); createDoctorPayoutIndex++) {
            DoctorPayout createDoctorPayout = new DoctorPayout();
            createDoctorPayout.setunitCode(departmentPayoutByUnitAndDepartment.getUnitCode());
            createDoctorPayout.setYear(LocalDateTime.now().getYear());
            createDoctorPayout.setMonth(LocalDateTime.now().getMonthValue());
            createDoctorPayout.setDate(LocalDateTime.now().getDayOfMonth());
            createDoctorPayout.setEmployeeId(collect1.get(createDoctorPayoutIndex));
            createDoctorPayoutList.add(createDoctorPayout);
        }
        if (!createDoctorPayoutList.isEmpty()) {
            doctorsListByEmpIds.addAll(doctorPayoutService.saveAll(createDoctorPayoutList));
        }
        //Calculate Department Revenue Based on Payout range
        //and then set department revenue to each consultant/

        List<DoctorPayoutDepartment> collect = doctorsListByEmpIds.stream().map(doctorPayout -> saveDoctorPayoutDepartment(doctorPayout, departmentPayoutByUnitAndDepartment, totalDepartmentCalculatedValue)).filter(doctorPayoutDepartment -> doctorPayoutDepartment != null).collect(Collectors.toList());
        doctorPayoutDepartmentService.saveAll(collect);

    }


    private DoctorPayoutDepartment saveDoctorPayoutDepartment(DoctorPayout doctorPayout, DepartmentPayout departmentPayout
        , Double departmentRevenueAmount) {
        log.debug("Request to  saveDoctorPayoutDepartment", departmentPayout, departmentPayout, departmentRevenueAmount);
        List<Long> applicableConsultantIds = departmentPayout.getApplicableConsultants().stream().map(applicableConsultant -> applicableConsultant.getUserMasterId()).collect(Collectors.toList());
        List<Long> doctorsBasedOnUnit = variablePayoutRepository.getLatestVariablePayoutIds(departmentPayout.getUnitCode(), applicableConsultantIds);
        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAllById(doctorsBasedOnUnit);
        DoctorPayoutDepartment doctorPayoutDepartment = null;
        for (int i = 0; i < variablePayoutList.size(); i++) {
            VariablePayout variablePayout = variablePayoutList.get(i);
            List<DepartmentRevenueBenefit> departmentRevenueBenefitList = variablePayout.getDepartmentRevenueBenefits().stream().collect(Collectors.toList());
            DepartmentRevenueBenefit departmentRevenueBenefit = departmentRevenueBenefitList.stream().filter(departmentRevenueBenefi -> departmentRevenueBenefi.getDepartment().getId() == departmentPayout.getDepartmentId()).findFirst().orElse(null);
            if (departmentRevenueBenefit != null) {
                if (departmentRevenueBenefit.getRevenueBenefitType().equals(RevenueBenefitType.FIXED)) {
                    Float payoutPercentage = departmentRevenueBenefit.getPayoutPercentage();
                    departmentRevenueAmount = (departmentRevenueAmount * payoutPercentage) / 100;
                    doctorPayoutDepartment = new DoctorPayoutDepartment();
                    doctorPayoutDepartment.setYear(LocalDateTime.now().getYear());
                    doctorPayoutDepartment.setMonth(LocalDateTime.now().getMonthValue());
                    doctorPayoutDepartment.setDepartmentPayoutId(departmentPayout.getId());
                    doctorPayoutDepartment.setUnitCode(departmentPayout.getUnitCode());
                    doctorPayoutDepartment.setDoctorPayout(doctorPayout);
                    if (departmentRevenueAmount < departmentRevenueBenefit.getUpperLimit().doubleValue()) {
                        doctorPayoutDepartment.setAmount(departmentRevenueAmount);
                    } else {
                        doctorPayoutDepartment.setAmount(departmentRevenueBenefit.getUpperLimit().doubleValue());
                    }
                    doctorPayoutDepartment.setDoctorPayout(doctorPayout);
                    Department byId = departmentRepository.findById(departmentPayout.getDepartmentId()).orElse(null);
                    if (byId != null) {
                        doctorPayoutDepartment.setDepartment(byId);
                        doctorPayoutDepartment.setDepartmentMasterId(departmentPayout.getDepartmentId());
                    }
                } else {
                    Double averageAmount = departmentRevenueAmount / applicableConsultantIds.size();
                    doctorPayoutDepartment = new DoctorPayoutDepartment();
                    doctorPayoutDepartment.setYear(LocalDateTime.now().getYear());
                    doctorPayoutDepartment.setMonth(LocalDateTime.now().getMonthValue());
                    doctorPayoutDepartment.setDepartmentPayoutId(departmentPayout.getId());
                    doctorPayoutDepartment.setUnitCode(departmentPayout.getUnitCode());
                    doctorPayoutDepartment.setDoctorPayout(doctorPayout);
                    if (averageAmount < departmentRevenueBenefit.getUpperLimit().doubleValue()) {
                        doctorPayoutDepartment.setAmount(averageAmount);
                    } else {
                        doctorPayoutDepartment.setAmount(departmentRevenueBenefit.getUpperLimit().doubleValue());
                    }
                    doctorPayoutDepartment.setDoctorPayout(doctorPayout);
                    Department byId = departmentRepository.findById(departmentPayout.getDepartmentId()).orElse(null);
                    if (byId != null) {
                        doctorPayoutDepartment.setDepartment(byId);
                        doctorPayoutDepartment.setDepartmentMasterId(departmentPayout.getDepartmentId());
                    }
                }

            }
        }
        return doctorPayoutDepartment;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<InvoiceDoctorPayout> saveVariablePayoutDataForMcr(Map<Long, Map<ServiceItemBenefit, List<McrStaging>>> data, String unitCode, Integer year, Integer month) {
        List<InvoiceDoctorPayout> invoiceDoctorPayouts = new ArrayList<>();
        data.forEach((employeeId, serviceAnalysisStagingList) -> {
            DoctorPayout doctorPayout = doctorPayoutRepository.getDoctorByYearAndMonth(employeeId, year == null ? LocalDateTime.now().getYear() : year, month == null ? LocalDateTime.now().getMonthValue() : month);
            Long latestApprovedVariablePayoutId = variablePayoutRepository.getLatestApprovedVariablePayoutId(unitCode, employeeId);
            if (doctorPayout == null) {
                doctorPayout = new DoctorPayout();
                doctorPayout.setEmployeeId(employeeId);
                doctorPayout.setMonth(month == null ? LocalDateTime.now().getMonthValue() : month);
                doctorPayout.setYear(year == null ? LocalDateTime.now().getYear() : year);
                doctorPayout.setunitCode(unitCode);
                doctorPayout = doctorPayoutService.save(doctorPayout);
            }
            DoctorPayout doctorPayoutResult = doctorPayout;
            List<InvoiceDoctorPayout> invoiceDoctorPayoutList = createInvoiceBaseOnMCRData(serviceAnalysisStagingList);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutWithDoctorPayoutAsRefernce = invoiceDoctorPayoutList.stream().map(invoiceDoctorPayout -> {
                return invoiceDoctorPayout.doctorPayout(doctorPayoutResult);
            }).collect(Collectors.toList());
            invoiceDoctorPayouts.addAll(invoiceDoctorPayoutService.saveAll(invoiceDoctorPayoutWithDoctorPayoutAsRefernce));
            Double sum = 0d;
            if (doctorPayout.getServiceItemBenefitAmount() != null) {
                sum += invoiceDoctorPayoutWithDoctorPayoutAsRefernce.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getServiceBenefietAmount()).collect(Collectors.toList()).stream().reduce(0d, Double::sum);
            } else {
                sum = invoiceDoctorPayoutWithDoctorPayoutAsRefernce.stream().map(invoiceDoctorPayout -> invoiceDoctorPayout.getServiceBenefietAmount()).collect(Collectors.toList()).stream().reduce(0d, Double::sum);
            }
            doctorPayoutResult.setServiceItemBenefitAmount(sum.longValue());
            log.debug("Varaible Payout Saving for Item :: {} " + latestApprovedVariablePayoutId);
            doctorPayoutResult.setVariablePayoutId(latestApprovedVariablePayoutId);
            DoctorPayout save = doctorPayoutService.save(doctorPayoutResult);
            invoiceDoctorPayoutService.saveAll(invoiceDoctorPayoutWithDoctorPayoutAsRefernce);
//            calculatePayoutAdjustment(unitCode, save);

        });
        return invoiceDoctorPayouts;
    }

    @Override
    public DoctorPayout executeVariablePayoutByYearMonthAndID(String unitCode, Integer year, Integer month, Long employeeId, Long variablePayoutId, DoctorPayout doctorPayout) {
        VariablePayout latestVariablePayout = variablePayoutRepository.findById(variablePayoutId).orElse(null);
        List<ServiceItemBenefit> serviceItemBenefitByVariablePayoutOrderByPriority = serviceItemBenefitRepository.getServiceItemBenefitByVersion(latestVariablePayout.getVariablePayoutId(), latestVariablePayout.getVersion());
        if (latestVariablePayout.getServiceItemBenefitTemplates() != null && !latestVariablePayout.getServiceItemBenefitTemplates().isEmpty()) {
            List<ServiceItemBenefitTemplate> collect = latestVariablePayout.getServiceItemBenefitTemplates().stream().collect(Collectors.toList());
            collect.parallelStream().forEach(serviceItemBenefitTemplate -> {
                serviceItemBenefitByVariablePayoutOrderByPriority.addAll(serviceItemBenefitRepository.getRulesByTemplate(serviceItemBenefitTemplate.getId()));
            });
        }
        deleteOldInvoiceData(doctorPayout);
        List<InvoiceDoctorPayout> invoiceDoctorPayoutListResult = new ArrayList<>();
        Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap = serviceItemBenefitByVariablePayoutOrderByPriority.stream().collect(Collectors.groupingBy(ServiceItemBenefit::getType));
        List<InvoiceDoctorPayout> invoiceDoctorPayoutListForService = processSARReportsForServices(serviceItemTypeListMap, employeeId, unitCode, latestVariablePayout, year, month, false);
        List<InvoiceDoctorPayout> invoiceDoctorPayoutListForPackage = processSARReportsForPackage(serviceItemTypeListMap, employeeId, unitCode, latestVariablePayout, year, month, false);
        List<InvoiceDoctorPayout> invoiceDoctorPayoutListForItem = processMcrReportsForItem(serviceItemTypeListMap, employeeId, unitCode, latestVariablePayout, year, month,false);
        List<DoctorPayoutAdjustment> doctorPayoutAdjustmentList = calculatePayoutAdjustment(unitCode, doctorPayout);
        invoiceDoctorPayoutListResult.addAll(invoiceDoctorPayoutListForService);
        invoiceDoctorPayoutListResult.addAll(invoiceDoctorPayoutListForPackage);
        invoiceDoctorPayoutListResult.addAll(invoiceDoctorPayoutListForItem);
        doctorPayout.setDoctorPayoutAdjustments(doctorPayoutAdjustmentList.stream().collect(Collectors.toSet()));
        doctorPayout.setDoctorPayoutInvoices(invoiceDoctorPayoutListResult.stream().collect(Collectors.toSet()));
        return doctorPayout;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteOldInvoiceData(DoctorPayout doctorPayout) {
        log.debug("Delete Old Invoice Data with reference of doctorPayout {}", doctorPayout.getId());
        invoiceDoctorPayoutRepository.deleteAllInvoiceByDoctorPayout(doctorPayout.getId());
        doctorPayout.setServiceItemBenefitAmount(0l);
        doctorPayout.setDoctorPayoutInvoices(null);
        doctorPayoutService.save(doctorPayout);
    }

}
