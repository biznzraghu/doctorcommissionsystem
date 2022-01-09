package org.nh.artha.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.Beneficiary;
import org.nh.artha.domain.enumeration.PatientCategory;
import org.nh.artha.domain.enumeration.Type;
import org.nh.artha.domain.enumeration.VisitType;
import org.nh.artha.repository.McrStagingRepository;
import org.nh.artha.repository.ServiceAnalysisRepository;
import org.nh.artha.service.CommonValueSetCodeService;
import org.nh.artha.service.ServiceAnalysisStagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceAnalysisStagingServiceImpl implements ServiceAnalysisStagingService {
    private final Logger log = LoggerFactory.getLogger(ServiceAnalysisStagingService.class);

    private final ServiceAnalysisRepository serviceAnalysisRepository;

    private final McrStagingRepository mcrStagingRepository;

    private final ObjectMapper objectMapper;

    private final CommonValueSetCodeService commonValueSetCodeService;

    public ServiceAnalysisStagingServiceImpl(ServiceAnalysisRepository serviceAnalysisRepository,McrStagingRepository mcrStagingRepository,ObjectMapper objectMapper,CommonValueSetCodeService commonValueSetCodeService) {
        this.serviceAnalysisRepository = serviceAnalysisRepository;
        this.mcrStagingRepository=mcrStagingRepository;
        this.objectMapper=objectMapper;
        this.commonValueSetCodeService=commonValueSetCodeService;
    }

    @Override
    @Transactional
    public List<ServiceAnalysisStaging> saveAll(List<ServiceAnalysisStaging> serviceAnalysisStagingList) {
        try {
            log.debug("Request to save serviceAnalysisStagingList {} ", serviceAnalysisStagingList);
            return serviceAnalysisRepository.saveAll(serviceAnalysisStagingList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Execption while updating sar");
        }
    }

    public  List<McrStaging> findByCriteriaOnMcr(ServiceItemBenefit rule, VariablePayout variablePayout, Integer year, Integer month, String empNumber, Boolean throughSchduler,
                                                            String type, List<Long> sarIdList) {
        LocalDateTime minDate = LocalDateTime.of(year, month, 1, 0, 0, 0).with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime maxDate = LocalDateTime.of(year, month, minDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(), 23, 59, 59, 999999999);
        if (variablePayout.getCommencementDate().getYear() == minDate.getYear() && variablePayout.getCommencementDate().getMonthValue() == minDate.getMonthValue()) {
            minDate = variablePayout.getCommencementDate();
        } else if (variablePayout.getContractEndDate().getYear() >= minDate.getYear()) {
            if(variablePayout.getContractEndDate().getYear() == minDate.getYear() && variablePayout.getContractEndDate().getMonthValue() == minDate.getMonthValue()){
                minDate=variablePayout.getContractEndDate();
            }else if(variablePayout.getContractEndDate().getYear() == minDate.getYear() && variablePayout.getContractEndDate().getMonthValue() > minDate.getMonthValue()){
                minDate=minDate;
            }else if(variablePayout.getContractEndDate().getYear() > minDate.getYear()){
                minDate=minDate;
            }

        }
        if (variablePayout.getContractEndDate().getMonthValue() == minDate.getMonthValue() && variablePayout.getContractEndDate().getYear() == minDate.getYear()) {
            maxDate = variablePayout.getContractEndDate();
        } else if (variablePayout.getContractEndDate().getYear() >= minDate.getYear()) {
            if(variablePayout.getContractEndDate().getYear()==minDate.getYear() && variablePayout.getContractEndDate().getMonthValue()==minDate.getMonthValue()){
                maxDate = variablePayout.getContractEndDate();
            }else if(variablePayout.getContractEndDate().getYear()== minDate.getYear() && variablePayout.getContractEndDate().getMonthValue()>minDate.getMonthValue()){
                maxDate=maxDate;
            }else if(variablePayout.getContractEndDate().getYear() > minDate.getYear()){
                maxDate=maxDate;
            }

        }
        LocalDateTime fromDate = minDate;
        LocalDateTime toDate = maxDate;
        log.debug("Request to  findByCriteria {}", rule.getId(), variablePayout.getId(), year, month, throughSchduler, fromDate, toDate);
        return mcrStagingRepository.findAll(new Specification<McrStaging>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<McrStaging> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
                if (rule != null) {
                    if (rule.getBeneficiary().equals(Beneficiary.ORDERING)) {
                        if (throughSchduler) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("orderingDoctorExecuted"), false)));
                        }
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("orderingDoctorLogin"), empNumber)));
                    }
                    if (rule.getBeneficiary().equals(Beneficiary.RENDERING)) {
                        if (throughSchduler) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("renderingDoctorExecuted"), false)));
                        }
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("renderingDoctorLogin"), empNumber)));
                    }
                    if (rule.getBeneficiary().equals(Beneficiary.ADMITTING)) {
                        if (throughSchduler) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("admittingDoctorExecuted"), false)));
                        }
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("admittingDoctorlogin"), empNumber)));
                    }
                    if (rule.getBeneficiary().equals(Beneficiary.CONSULTANT)) {
                        if (throughSchduler) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("executed"), false)));
                        }
                    }
                    if ((rule.getType().equals(Type.ITEM_NAME) || rule.getType().equals(Type.ITEM_NAME)) && rule.getComponents() != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("itemCode"), rule.getComponents().getCode())));
                    }
                    if ((rule.getType().equals(Type.ITEM_GROUP) || rule.getType().equals(Type.ITEM_GROUP)) && rule.getServiceGroup() != null) {
                        List<Group> serviceGroupList1 = rule.getServiceGroup();
                        List<String> serviceGroupList = new ArrayList<>();
                        for (int k = 0; k < serviceGroupList1.size(); k++) {
                            Group group = objectMapper.convertValue(serviceGroupList1.get(k), Group.class);
                            serviceGroupList.add(group.getCode());
                        }
                        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("billingGroupCode"));
                        for (String vs : serviceGroupList) {
                            inClause.value(vs);
                        }
                        predicates.add(criteriaBuilder.and(inClause));
                    }
                    if (rule.getType().equals(Type.ITEM_CATEGORY) && rule.getItemCategory() != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("itemCategoryCode"), rule.getItemCategory().getCode())));

                    }
                    if (rule.getVisitType() != null && !rule.getVisitType().isEmpty()) {
                        List<String> visitType = rule.getVisitType().stream().map(visitType1 -> VisitType.valueOf(visitType1).getDisplay()).collect(Collectors.toList());
                        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("visitType"));
                        for (String vs : visitType) {
                            inClause.value(vs);
                        }
                        predicates.add(criteriaBuilder.and(inClause));
                    }
                    List<ValueSetCode> tariffClass1 = null;
                    List<String> tariffClass = new ArrayList<>();
                    if (rule.getTariffClass() != null) {
                        tariffClass1 = rule.getTariffClass();
                    } else {
                        String modifiedQuery = "valueSet.code:" + TariffClass.VALUE_SET_CODE + " " + "*";
                        List<ValueSetCode> content = (List<ValueSetCode>) commonValueSetCodeService.search(modifiedQuery, PageRequest.of(0, 200)).getContent();
                        tariffClass = content.stream().map(valueSetCode -> valueSetCode.getDisplay()).collect(Collectors.toList());
                    }
                    if (tariffClass1 != null && !tariffClass1.isEmpty()) {
                        for (int k = 0; k < tariffClass1.size(); k++) {
                            ValueSetCode valueSetCode = objectMapper.convertValue(tariffClass1.get(k), ValueSetCode.class);
                            tariffClass.add(valueSetCode.getDisplay());
                        }
                    }
                    if (tariffClass != null && !tariffClass.isEmpty()) {
                        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("tariffClass"));
                        for (String vs : tariffClass) {
                            inClause.value(vs);
                        }
                        predicates.add(criteriaBuilder.and(inClause));
                    }
                    if (!throughSchduler && sarIdList != null && !sarIdList.isEmpty()) {
                        CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(root.get("id"));
                        for (Long vs : sarIdList) {
                            inClause.value(vs);
                        }
                        predicates.add(criteriaBuilder.and(criteriaBuilder.not(inClause)));
                    }
                    if (rule.getPatientCategory() != null && rule.getPatientCategory().equals(PatientCategory.CASH)) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("patientPayable"), 0)));
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("sponsorPayable"), 0)));
                    } else if (rule.getPatientCategory() != null && rule.getPatientCategory().equals(PatientCategory.CREDIT)) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("sponsorPayable"), 0)));
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("patientPayable"), 0)));
                    } else if (rule.getPatientCategory() != null && rule.getPatientCategory().equals(PatientCategory.CASH_CREDIT)) {
                        javax.persistence.criteria.Predicate sponsorAmount ;
                        javax.persistence.criteria.Predicate patientAmount ;
                        sponsorAmount=criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("sponsorPayable"), 0));
                        patientAmount=criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("patientPayable"), 0));
                        predicates.add(criteriaBuilder.or(sponsorAmount,patientAmount));
                    }
                    predicates.add(criteriaBuilder.and(criteriaBuilder.between(root.get("invoiceDate"), fromDate, toDate)));


                }

                javax.persistence.criteria.Predicate predicate = criteriaBuilder.and(predicates.toArray(new javax.persistence.criteria.Predicate[predicates.size()]));
                return predicate;
            }
        });
    }

    public List<ServiceAnalysisStaging> findByCriteriaForSar(ServiceItemBenefit rule, VariablePayout variablePayout, Integer year, Integer month, String empNumber, Boolean throughSchduler,
                                                       String type, List<Long> sarIdList) {
        LocalDateTime minDate = LocalDateTime.of(year, month, 1, 0, 0, 0).with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime maxDate = LocalDateTime.of(year, month, minDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(), 23, 59, 59, 999999999);
        if (variablePayout.getCommencementDate().getYear() == minDate.getYear() && variablePayout.getCommencementDate().getMonthValue() == minDate.getMonthValue()) {
            minDate = variablePayout.getCommencementDate();
        } else if (variablePayout.getContractEndDate().getYear() >= minDate.getYear()) {
            if(variablePayout.getContractEndDate().getYear() == minDate.getYear() && variablePayout.getContractEndDate().getMonthValue() == minDate.getMonthValue()){
               minDate=variablePayout.getContractEndDate();
            }else if(variablePayout.getContractEndDate().getYear() == minDate.getYear() && variablePayout.getContractEndDate().getMonthValue() > minDate.getMonthValue()){
                minDate=minDate;
            }else if(variablePayout.getContractEndDate().getYear() > minDate.getYear()){
                minDate=minDate;
            }

        }
        if (variablePayout.getContractEndDate().getMonthValue() == minDate.getMonthValue() && variablePayout.getContractEndDate().getYear() == minDate.getYear()) {
            maxDate = variablePayout.getContractEndDate();
        } else if (variablePayout.getContractEndDate().getYear() >= minDate.getYear()) {
            if(variablePayout.getContractEndDate().getYear()==minDate.getYear() && variablePayout.getContractEndDate().getMonthValue()==minDate.getMonthValue()){
                maxDate = variablePayout.getContractEndDate();
            }else if(variablePayout.getContractEndDate().getYear()== minDate.getYear() && variablePayout.getContractEndDate().getMonthValue()>minDate.getMonthValue()){
                maxDate=maxDate;
            }else if(variablePayout.getContractEndDate().getYear() > minDate.getYear()){
                maxDate=maxDate;
            }

        }
        LocalDateTime fromDate = minDate;
        LocalDateTime toDate = maxDate;
        log.debug("Request to  findByCriteria {}", rule.getId(), variablePayout.getId(), year, month, throughSchduler, fromDate, toDate);
        return serviceAnalysisRepository.findAll(new Specification<ServiceAnalysisStaging>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<ServiceAnalysisStaging> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
                if (rule != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("invoiceType"), "Invoice")));
                    if (type.equalsIgnoreCase("SERVICE")) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("type"), "SERVICE")));
                    } else if (type.equalsIgnoreCase("PACKAGE")) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("type"), "PACKAGE")));
                    }
                    if (rule.getBeneficiary().equals(Beneficiary.ORDERING)) {
                        if (throughSchduler) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("orderingDoctorExecuted"), false)));
                        }
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("orderingDoctorLogin"), empNumber)));
                    }
                    if (rule.getBeneficiary().equals(Beneficiary.RENDERING)) {
                        if (throughSchduler) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("renderingDoctorExecuted"), false)));
                        }
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("renderingDoctorLogin"), empNumber)));
                    }
                    if (rule.getBeneficiary().equals(Beneficiary.ADMITTING)) {
                        if (throughSchduler) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("admittingDoctorExecuted"), false)));
                        }
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("admittingDoctorlogin"), empNumber)));
                    }
                    if (rule.getBeneficiary().equals(Beneficiary.CONSULTANT)) {
                        if (throughSchduler) {
                            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("executed"), false)));
                        }
                    }
                    if ((rule.getType().equals(Type.SERVICE_NAME) || rule.getType().equals(Type.PACKAGE_NAME)) && rule.getComponents() != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("itemCode"), rule.getComponents().getCode())));
                    }
                    if ((rule.getType().equals(Type.SERVICE_GROUP) || rule.getType().equals(Type.PACKAGE_GROUP)) && rule.getServiceGroup() != null) {
                        List<Group> serviceGroupList1 = rule.getServiceGroup();
                        List<String> serviceGroupList = new ArrayList<>();
                        for (int k = 0; k < serviceGroupList1.size(); k++) {
                            Group group = objectMapper.convertValue(serviceGroupList1.get(k), Group.class);
                            serviceGroupList.add(group.getCode());
                        }
                        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("itemGroup"));
                        for (String vs : serviceGroupList) {
                            inClause.value(vs);
                        }
                        predicates.add(criteriaBuilder.and(inClause));
                    }
                    if (rule.getType().equals(Type.SERVICE_TYPE)) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("itemType"), rule.getServiceType().getDisplay())));
                    }
                    if (rule.getType().equals(Type.SERVICE_INSIDE_PACKAGE) && rule.getComponents() != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("referPackageId"))));
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("itemCode"), rule.getComponents().getCode())));
                    }

                    if (rule.getType().equals(Type.PACKAGE_CATEGORY) && rule.getPackageCategory() != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("packageCategory"), rule.getPackageCategory().getName())));

                    }
                    if (rule.getVisitType() != null && !rule.getVisitType().isEmpty()) {
                        List<String> visitType = rule.getVisitType().stream().map(visitType1 -> VisitType.valueOf(visitType1).getDisplay()).collect(Collectors.toList());
                        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("visitType"));
                        for (String vs : visitType) {
                            inClause.value(vs);
                        }
                        predicates.add(criteriaBuilder.and(inClause));
                    }
                    List<ValueSetCode> tariffClass1 = null;
                    List<String> tariffClass = new ArrayList<>();
                    if (rule.getTariffClass() != null) {
                        tariffClass1 = rule.getTariffClass();
                    } else {
                        String modifiedQuery = "valueSet.code:" + TariffClass.VALUE_SET_CODE + " " + "*";
                        List<ValueSetCode> content = (List<ValueSetCode>) commonValueSetCodeService.search(modifiedQuery, PageRequest.of(0, 200)).getContent();
                        tariffClass = content.stream().map(valueSetCode -> valueSetCode.getDisplay()).collect(Collectors.toList());
                    }
                    if (tariffClass1 != null && !tariffClass1.isEmpty()) {
                        for (int k = 0; k < tariffClass1.size(); k++) {
                            ValueSetCode valueSetCode = objectMapper.convertValue(tariffClass1.get(k), ValueSetCode.class);
                            tariffClass.add(valueSetCode.getDisplay());
                        }
                    }
                    if (tariffClass != null && !tariffClass.isEmpty()) {
                        CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("tariffClass"));
                        for (String vs : tariffClass) {
                            inClause.value(vs);
                        }
                        predicates.add(criteriaBuilder.and(inClause));
                    }
                    if (!throughSchduler && sarIdList != null && !sarIdList.isEmpty()) {
                        CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(root.get("id"));
                        for (Long vs : sarIdList) {
                            inClause.value(vs);
                        }
                        predicates.add(criteriaBuilder.and(criteriaBuilder.not(inClause)));
                    }
                    if (rule.getPatientCategory() != null && rule.getPatientCategory().equals(PatientCategory.CASH)) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("patientAmount"), 0)));
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("sponsorAmount"), 0)));
                    } else if (rule.getPatientCategory() != null && rule.getPatientCategory().equals(PatientCategory.CREDIT)) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("sponsorAmount"), 0)));
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("patientAmount"), 0)));
                    } else if (rule.getPatientCategory() != null && rule.getPatientCategory().equals(PatientCategory.CASH_CREDIT)) {
                        javax.persistence.criteria.Predicate sponsorAmount ;
                        javax.persistence.criteria.Predicate patientAmount ;
                        sponsorAmount=criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("sponsorAmount"), 0));
                        patientAmount=criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("patientAmount"), 0));
                        predicates.add(criteriaBuilder.or(sponsorAmount,patientAmount));
                    }
                    predicates.add(criteriaBuilder.and(criteriaBuilder.between(root.get("invoiceDate"), fromDate, toDate)));


                }

                javax.persistence.criteria.Predicate predicate = criteriaBuilder.and(predicates.toArray(new javax.persistence.criteria.Predicate[predicates.size()]));
                return predicate;
            }
        });
    }


}
