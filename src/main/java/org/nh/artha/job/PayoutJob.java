package org.nh.artha.job;


import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.LengthOfStayCriteria;
import org.nh.artha.domain.enumeration.Type;
import org.nh.artha.repository.*;
import org.nh.artha.repository.search.DoctorPayoutSearchRepository;
import org.nh.artha.service.*;
import org.nh.artha.service.impl.DoctorPayoutServiceImpl;
import org.nh.artha.web.rest.errors.CustomParameterizedException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PayoutJob implements Job {

    private final Logger log = LoggerFactory.getLogger(PayoutJob.class);

    @Autowired
    private StagingService stagingService;

    @Autowired
    private McrStagingRepository mcrStagingRepository;

    @Autowired
    private ServiceAnalysisRepository serviceAnalysisRepository;

    @Autowired
    private VariablePayoutServiceAnalysisLoading variablePayoutServiceAnalysisLoading;

    @Autowired
    private ServiceItemBenefitService serviceItemBenefitService;

    @Autowired
    private ServiceItemBenefitRepository serviceItemBenefitRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private VariablePayoutRepository variablePayoutRepository;

    @Autowired
    private DoctorPayoutRepository doctorPayoutRepository;

    @Autowired
    private DoctorPayoutService doctorPayoutService;

    @Autowired
    private DoctorPayoutSearchRepository doctorPayoutSearchRepository;

    @Autowired
    private LengthOfStayBenefitService lengthOfStayBenefitService;

    @Autowired
    private DoctorPayoutLOSService doctorPayoutLOSService;

    /**
     * execute user producer.
     *
     * @param jobExecutionContext
     */
    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) {
        List<Organization> allOrganization = organizationRepository.findAll();
        for (int i = 0; i < allOrganization.size(); i++) {
            executeSARDataByUnit(allOrganization.get(i).getCode());
        }
    }


    private void executeSARDataByUnit(String unitCode) {
        List<VariablePayout> doctorsBasedOnUnit = variablePayoutServiceAnalysisLoading.getDoctorsBasedOnUnit(unitCode);
        Map<Long, Map<ServiceItemBenefit, List<ServiceAnalysisStaging>>> doctorBySAR = new HashMap<>();
        for (int i = 0; i < doctorsBasedOnUnit.size(); i++) {
            Long employeeId = doctorsBasedOnUnit.get(i).getEmployeeId();
            Long latestApprovedVariablePayoutId = variablePayoutRepository.getLatestApprovedVariablePayoutId(unitCode, employeeId);
            VariablePayout variablePayout = variablePayoutRepository.findById(latestApprovedVariablePayoutId).orElse(null);
            LocalDateTime min = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1,0,0,0).with(TemporalAdjusters.firstDayOfMonth());
            Boolean flag=false;
            if(variablePayout.getCommencementDate().getMonthValue()==min.getMonthValue() && variablePayout.getCommencementDate().getYear()==min.getYear()){
                flag=true;
            } else if(variablePayout.getContractEndDate().getMonthValue()>=min.getMonthValue() && variablePayout.getContractEndDate().getYear()>=min.getYear()){
                flag=true;
            }
            if(variablePayout.getContractEndDate().getMonthValue()==min.getMonthValue() && variablePayout.getContractEndDate().getYear()==min.getYear()){
                flag=true;
            }else if(variablePayout.getContractEndDate().getMonthValue()>=min.getMonthValue() && variablePayout.getContractEndDate().getYear()>=min.getYear()){
                flag=true;
            }
            if(!flag){
                log.debug("CommencementDate is greater than Matching with selected month {} ",variablePayout.getEmployee().getDisplayName());
                continue;
            }
            List<ServiceItemBenefit> serviceItemBenefitByVariablePayoutOrderByPriority = serviceItemBenefitRepository.getServiceItemBenefitByVersion(variablePayout.getVariablePayoutId(), variablePayout.getVersion());
            if(variablePayout.getServiceItemBenefitTemplates()!=null && !variablePayout.getServiceItemBenefitTemplates().isEmpty()){
                List<ServiceItemBenefitTemplate> collect = variablePayout.getServiceItemBenefitTemplates().stream().collect(Collectors.toList());
               collect.parallelStream().forEach(serviceItemBenefitTemplate -> {
                    serviceItemBenefitByVariablePayoutOrderByPriority.addAll(serviceItemBenefitRepository.getRulesByTemplate(serviceItemBenefitTemplate.getId()));
                });
            }
            Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap = serviceItemBenefitByVariablePayoutOrderByPriority.stream().collect(Collectors.groupingBy(ServiceItemBenefit::getType));
            List<InvoiceDoctorPayout> invoiceDoctorPayoutListResult= new ArrayList<>();
            List<InvoiceDoctorPayout> invoiceDoctorPayoutListForService = variablePayoutServiceAnalysisLoading.processSARReportsForServices(serviceItemTypeListMap, employeeId, unitCode, variablePayout,LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),true);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutListForPackage = variablePayoutServiceAnalysisLoading.processSARReportsForPackage(serviceItemTypeListMap, employeeId, unitCode, variablePayout,LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),true);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutListForItem = variablePayoutServiceAnalysisLoading.processMcrReportsForItem(serviceItemTypeListMap, employeeId, unitCode, variablePayout,LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),true);
            List<InvoiceDoctorPayout> invoiceDoctorPayoutListForInvoice=variablePayoutServiceAnalysisLoading.processSARReportsForInvoice(serviceItemTypeListMap,employeeId,unitCode,variablePayout,LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),true);
            DoctorPayout doctorPayout = doctorPayoutRepository.getDoctorByYearAndMonth(employeeId, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
            invoiceDoctorPayoutListResult.addAll(invoiceDoctorPayoutListForService);
            invoiceDoctorPayoutListResult.addAll(invoiceDoctorPayoutListForPackage);
            invoiceDoctorPayoutListResult.addAll(invoiceDoctorPayoutListForItem);
            invoiceDoctorPayoutListResult.addAll(invoiceDoctorPayoutListForInvoice);
            if (doctorPayout == null) {
                doctorPayout = new DoctorPayout();
                doctorPayout.setEmployeeId(employeeId);
                doctorPayout.setMonth(LocalDateTime.now().getMonthValue());
                doctorPayout.setYear(LocalDateTime.now().getYear());
                doctorPayout.setunitCode(unitCode);
                doctorPayout = doctorPayoutService.save(doctorPayout);
            }
            Map<LengthOfStayBenefit, List<ServiceAnalysisStaging>> patientBasedOnLosBenefit = lengthOfStayBenefitService.findPatientBasedOnLosBenefit(variablePayout);
            List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSService.calculateDoctorPayoutLos(patientBasedOnLosBenefit, doctorPayout);
            List<DoctorPayoutAdjustment> doctorPayoutAdjustmentList = variablePayoutServiceAnalysisLoading.calculatePayoutAdjustment(unitCode, doctorPayout);
            doctorPayout.setDoctorPayoutAdjustments(doctorPayoutAdjustmentList.stream().collect(Collectors.toSet()));
            doctorPayout.setDoctorPayoutInvoices(invoiceDoctorPayoutListResult.stream().collect(Collectors.toSet()));
            if(doctorPayoutLOSList!=null && !doctorPayoutLOSList.isEmpty()){
                doctorPayout.setLosBenefietIds(doctorPayoutLOSList.stream().collect(Collectors.toSet()));
            }
            doctorPayoutSearchRepository.save(doctorPayout);
        }

    }
}
