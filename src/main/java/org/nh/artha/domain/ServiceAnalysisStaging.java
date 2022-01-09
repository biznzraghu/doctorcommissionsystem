package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "service_analysis_staging")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceAnalysisStaging {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;


    @Column(name = "invoice_type", nullable = false)
    private String invoiceType;


    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;


    @Column(name = "admission_date", nullable = false)
    private LocalDateTime admissionDate;


    @Column(name = "admitted_for", nullable = false)
    private String admittedFor;


    @Column(name = "visit_no", nullable = false)
    private String visitNo;


    @Column(name = "patient_mrn", nullable = false)
    private String patientMrn;


    @Column(name = "patient_name", nullable = false)
    private String patientName;


    @Column(name = "patient_gender", nullable = false)
    private String patientGender;


    @Column(name = "patient_age", nullable = false)
    private Integer patientAge;


    @Column(name = "visit_type", nullable = false)
    private String visitType;


    @Column(name = "year", nullable = false)
    private Integer year;


    @Column(name = "month", nullable = false)
    private Integer month;


    @Column(name = "ordering_department_name", nullable = false)
    private String orderingDepartmentName;


    @Column(name = "ordering_doctor_name", nullable = false)
    private String orderingDoctorName;


    @Column(name = "rendering_department_name", nullable = false)
    private String renderingDepartmentName;


    @Column(name = "rendering_doctor_name", nullable = false)
    private String renderingDoctorName;


    @Column(name = "type", nullable = false)
    private String type;


    @Column(name = "item_code", nullable = false)
    private String itemCode;


    @Column(name = "item_name", nullable = false)
    private String itemName;


    @Column(name = "item_group", nullable = false)
    private String itemGroup;


    @Column(name = "patient_type", nullable = false)
    private String patientType;


    @Column(name = "sponsor_ref_name", nullable = false)
    private String sponsorRefName;


    @Column(name = "plan_name", nullable = false)
    private String planName;


    @Column(name = "tariff_class", nullable = false)
    private String tariffClass;


    @Column(name = "gross_amount", nullable = false)
    private Double grossAmount;

    @Column(name = "tax_amount", nullable = false)
    private Double taxAmount;


    @Column(name = "discretionary_concession", nullable = false)
    private Double discretionaryConcession;


    @Column(name = "non_discretionary_concession", nullable = false)
    private Double nonDiscretionaryConcession;


    @Column(name = "net_amount", nullable = false)
    private Double netAmount;


    @Column(name = "patient_amount", nullable = false)
    private Double patientAmount;


    @Column(name = "sponsor_amount", nullable = false)
    private Double sponsorAmount;


    @Column(name = "remarks", nullable = false)
    private String remarks;


    @Column(name = "cancellation_discount_approver_name", nullable = false)
    private String cancellationDiscountApproverName;


    @Column(name = "billed_by", nullable = false)
    private String billedBy;


    @Column(name = "is_profile", nullable = false)
    private String isProfile;


    @Column(name = "quantity", nullable = false)
    private Double quantity;


    @Column(name = "item_type", nullable = false)
    private String itemType;


    @Column(name = "invoice_status", nullable = false)
    private String invoiceStatus;


    @Column(name = "settlement_status", nullable = false)
    private String settlementStatus;


    @Column(name = "approved_date", nullable = false)
    private LocalDateTime approvedDate;


    @Column(name = "discharge_date_time", nullable = false)
    private LocalDateTime dischargeDateTime;


    @Column(name = "billing_group_name", nullable = false)
    private String billingGroupName;


    @Column(name = "service_request_date_time", nullable = false)
    private LocalDateTime serviceRequestDateTime;


    @Column(name = "requested_by", nullable = false)
    private String requestedBy;


    @Column(name = "service_rendering_date_time", nullable = false)
    private String serviceRenderingDateTime;


    @Column(name = "rendered_by", nullable = false)
    private String renderedBy;


    @Column(name = "requesting_service_patient_ward", nullable = false)
    private String requestingServicePatientWard;


    @Column(name = "rendering_service_patient_ward", nullable = false)
    private String renderingServicePatientWard;


    @Column(name = "hospital_unit_rate", nullable = false)
    private Double hospitalUnitRate;


    @Column(name = "plan_unit_rate", nullable = false)
    private Double planUnitRate;


    @Column(name = "addon_parameter_value", nullable = false)
    private String addonParameterValue;


    @Column(name = "total_mrp", nullable = false)
    private Double totalMrp;


    @Column(name = "revenue_department", nullable = false)
    private String revenueDepartment;


    @Column(name = "revenue_doctor", nullable = false)
    private String revenueDoctor;


    @Column(name = "finance_group_name", nullable = false)
    private String financeGroupName;


    @Column(name = "original_invoice_no", nullable = false)
    private String originalInvoiceNo;


    @Column(name = "addon_parameter", nullable = false)
    private String addonParameter;


    @Column(name = "ordered_date ", nullable = false)
    private LocalDateTime orderedDate;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "executed")
    private Boolean executed;

    @Column(name = "visit_type_tariff_class_key")
    private String visitTypeTariffClassKey;

    @Column(name = "group_key")
    private String groupKey;

    @Column(name = "type_key")
    private String typeKey;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "dept_revenue_executed")
    private Boolean departmentRevenueExecuted;

    @Column(name = "length_of_stay_executed")
    private Boolean lengthOfStayExecuted = Boolean.FALSE;

    @Column(name = "ordering_doctor_login")
    private String orderingDoctorLogin;

    @Column(name = "rendering_doctor_login")
    private String renderingDoctorLogin;

    @Column(name = "rendering_department_code")
    private String renderingDepartmentCode;

    @Column(name = "admitting_doctor_login")
    private String admittingDoctorlogin;

    @Column(name = "admitting_department_code")
    private String admittingDepartmentCode;

    @Column(name ="package_category")
    private String packageCategory;

    @Column(name = "ordering_department_code")
    private String orderingDepartmentCode;

    @Column(name = "plan_code")
    private String planCode;

    @Column(name = "ordering_doctor_executed")
    private Boolean orderingDoctorExecuted;

    @Column(name = "admitting_doctor_executed")
    private Boolean admittingDoctorExecuted;

    @Column(name = "rendering_doctor_executed")
    private Boolean renderingDoctorExecuted;

    @Column(name = "refer_package_id")
    private Long referPackageId;

    public ServiceAnalysisStaging(){

    }

    public ServiceAnalysisStaging(String invoiceNumber,  String invoiceType,  String invoiceDate,  String admissionDate,
                                  String admittedFor,  String visitNo,  String patientMrn,  String patientName,  String patientGender,
                                  String patientAge,  String visitType,  String year,  String month,  String orderingDepartmentName,
                                  String orderingDoctorName,  String renderingDepartmentName,  String renderingDoctorName,  String type,
                                  String itemCode,  String itemName,  String itemGroup,  String patientType,  String sponsorRefName,
                                  String planName,  String tariffClass,  String grossAmount,  String taxAmount,
                                  String discretionaryConcession,  String nonDiscretionaryConcession,  String netAmount,  String patientAmount,
                                  String sponsorAmount,  String remarks,  String cancellationDiscountApproverName,  String billedBy,
                                  String isProfile,  String quantity,  String itemType,  String invoiceStatus,  String settlementStatus,
                                  String approvedDate,  String dischargeDateTime,  String billingGroupName,
                                  String serviceRequestDateTime,  String requestedBy,  String serviceRenderingDateTime,
                                  String renderedBy,  String requestingServicePatientWard,  String renderingServicePatientWard,
                                  String hospitalUnitRate,  String planUnitRate,  String addonParameterValue,  String totalMrp,
                                  String revenueDepartment,  String revenueDoctor,  String financeGroupName,  String originalInvoiceNo,
                                  String addonParameter,  String orderedDate,String employeeId,String unitCode,String departmentCode) {
        this.invoiceNumber = checkStringNullCondition(invoiceNumber);
        this.invoiceType = checkStringNullCondition(invoiceType);
        this.invoiceDate = (invoiceDate!=null && !invoiceDate.isEmpty() && !invoiceDate.trim().equalsIgnoreCase("null"))?LocalDateTime.parse(invoiceDate.trim(), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss")):null;
        this.admissionDate = (admissionDate!=null && !admissionDate.isEmpty() && !admissionDate.trim().equalsIgnoreCase("null"))?LocalDateTime.parse(admissionDate.trim(), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss")):null;
        this.admittedFor = checkStringNullCondition(admittedFor);
        this.visitNo = checkStringNullCondition(visitNo);
        this.patientMrn = checkStringNullCondition(patientMrn);
        this.patientName = checkStringNullCondition(patientName);
        this.patientGender = checkStringNullCondition(patientGender);
        this.patientAge = checkStringNullCondition(patientAge)!=null?Integer.parseInt(patientAge):null;
        this.visitType = checkStringNullCondition(visitType);
        this.year = checkStringNullCondition(year)!=null?Integer.parseInt(year):null;
        this.month = checkStringNullCondition(month)!=null?Integer.parseInt(month):null;
        this.orderingDepartmentName = checkStringNullCondition(orderingDepartmentName);
        this.orderingDoctorName = checkStringNullCondition(orderingDoctorName);
        this.renderingDepartmentName = checkStringNullCondition(renderingDepartmentName);
        this.renderingDoctorName = checkStringNullCondition(renderingDoctorName);
        this.type = checkStringNullCondition(type);
        this.itemCode = checkStringNullCondition(itemCode);
        this.itemName = checkStringNullCondition(itemName);
        this.itemGroup = checkStringNullCondition(itemGroup);
        this.patientType = checkStringNullCondition(patientType);
        this.sponsorRefName = checkStringNullCondition(sponsorRefName);
        this.planName = checkStringNullCondition(planName);
        this.tariffClass = checkStringNullCondition(tariffClass);
        this.grossAmount = checkStringNullCondition(grossAmount)!=null?Double.parseDouble(grossAmount):null;
        this.taxAmount = checkStringNullCondition(taxAmount)!=null?Double.parseDouble(checkStringNullCondition(taxAmount)):null;
        this.discretionaryConcession = checkStringNullCondition(discretionaryConcession)!=null?Double.parseDouble(checkStringNullCondition(discretionaryConcession)):null;
        this.nonDiscretionaryConcession = checkStringNullCondition(nonDiscretionaryConcession)!=null?Double.parseDouble(nonDiscretionaryConcession):null;
        this.netAmount = checkStringNullCondition(netAmount)!=null?Double.parseDouble(netAmount):null;
        this.patientAmount = checkStringNullCondition(patientAmount)!=null?Double.parseDouble(patientAmount):null;
        this.sponsorAmount = checkStringNullCondition(sponsorAmount)!=null?Double.parseDouble(sponsorAmount):null;
        this.remarks = checkStringNullCondition(remarks);
        this.cancellationDiscountApproverName = checkStringNullCondition(cancellationDiscountApproverName);
        this.billedBy = checkStringNullCondition(billedBy);
        this.isProfile = checkStringNullCondition(isProfile);
        this.quantity = checkStringNullCondition(quantity)!=null?Double.parseDouble(quantity):null;
        this.itemType = checkStringNullCondition(itemType);
        this.invoiceStatus = checkStringNullCondition(invoiceStatus);
        this.settlementStatus = checkStringNullCondition(settlementStatus);
        this.approvedDate = (approvedDate != null && !approvedDate.isEmpty() && !approvedDate.trim().equalsIgnoreCase("null")) ? LocalDateTime.parse(approvedDate.trim(), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss")) : null;
        this.dischargeDateTime = (dischargeDateTime != null && !dischargeDateTime.isEmpty() && !dischargeDateTime.trim().equalsIgnoreCase("null")) ? LocalDateTime.parse(dischargeDateTime.trim(), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss")) : null;
        this.billingGroupName = checkStringNullCondition(billingGroupName);
        this.serviceRequestDateTime = (serviceRequestDateTime != null && !serviceRequestDateTime.isEmpty() && !serviceRequestDateTime.trim().equalsIgnoreCase("null")) ? LocalDateTime.parse(serviceRequestDateTime.trim(), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss")) : null;
        this.requestedBy = checkStringNullCondition(requestedBy);
        this.serviceRenderingDateTime = checkStringNullCondition(serviceRenderingDateTime);
        this.renderedBy = checkStringNullCondition(renderedBy);
        this.requestingServicePatientWard = checkStringNullCondition(requestingServicePatientWard);
        this.renderingServicePatientWard = checkStringNullCondition(renderingServicePatientWard);
        this.hospitalUnitRate = checkStringNullCondition(hospitalUnitRate)!=null?Double.parseDouble(hospitalUnitRate):null;
        this.planUnitRate = checkStringNullCondition(planUnitRate)!=null?Double.parseDouble(planUnitRate):null;
        this.addonParameterValue = checkStringNullCondition(addonParameterValue);
        this.totalMrp = checkStringNullCondition(totalMrp)!=null?Double.parseDouble(totalMrp):null;
        this.revenueDepartment = checkStringNullCondition(revenueDepartment);
        this.revenueDoctor = checkStringNullCondition(revenueDoctor);
        this.financeGroupName = checkStringNullCondition(financeGroupName);
        this.originalInvoiceNo = checkStringNullCondition(originalInvoiceNo);
        this.addonParameter = checkStringNullCondition(addonParameter);
        this.orderedDate = (orderedDate != null && !orderedDate.isEmpty() && !orderedDate.trim().equalsIgnoreCase("null")) ? LocalDateTime.parse(orderedDate.trim(), DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss")) : null;
        this.employeeId = Long.parseLong(employeeId);
        this.unitCode=checkStringNullCondition(unitCode);
        this.departmentCode=checkStringNullCondition(departmentCode);
    }

  //  public ServiceAnalysisStaging(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10, String s11, String s12, String s13, String s14, String s15, String s16, String s17, String s18, String s19, String s20, String s21, String s22, String s23, String s24, String s25, String s26, String s27, String s28, String s29, String s30, String s31, String s32, String s33, String s34, String s35, String s36, String s37, String s38, String s39, String s40, String s41, String s42, String s43, String s44, String s45, String s46, String s47, String s48, String s49, String s50, String s51, String s52, String s53, String s54, String s55, String s56, String s57, String s58, String s59, String s60, String s61, String s62) {
 //   }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getAdmittedFor() {
        return admittedFor;
    }

    public void setAdmittedFor(String admittedFor) {
        this.admittedFor = admittedFor;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getPatientMrn() {
        return patientMrn;
    }

    public void setPatientMrn(String patientMrn) {
        this.patientMrn = patientMrn;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getOrderingDepartmentName() {
        return orderingDepartmentName;
    }

    public void setOrderingDepartmentName(String orderingDepartmentName) {
        this.orderingDepartmentName = orderingDepartmentName;
    }

    public String getOrderingDoctorName() {
        return orderingDoctorName;
    }

    public void setOrderingDoctorName(String orderingDoctorName) {
        this.orderingDoctorName = orderingDoctorName;
    }

    public String getRenderingDepartmentName() {
        return renderingDepartmentName;
    }

    public void setRenderingDepartmentName(String renderingDepartmentName) {
        this.renderingDepartmentName = renderingDepartmentName;
    }

    public String getRenderingDoctorName() {
        return renderingDoctorName;
    }

    public void setRenderingDoctorName(String renderingDoctorName) {
        this.renderingDoctorName = renderingDoctorName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getSponsorRefName() {
        return sponsorRefName;
    }

    public void setSponsorRefName(String sponsorRefName) {
        this.sponsorRefName = sponsorRefName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getTariffClass() {
        return tariffClass;
    }

    public void setTariffClass(String tariffClass) {
        this.tariffClass = tariffClass;
    }

    public Double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getDiscretionaryConcession() {
        return discretionaryConcession;
    }

    public void setDiscretionaryConcession(Double discretionaryConcession) {
        this.discretionaryConcession = discretionaryConcession;
    }

    public Double getNonDiscretionaryConcession() {
        return nonDiscretionaryConcession;
    }

    public void setNonDiscretionaryConcession(Double nonDiscretionaryConcession) {
        this.nonDiscretionaryConcession = nonDiscretionaryConcession;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public Double getPatientAmount() {
        return patientAmount;
    }

    public void setPatientAmount(Double patientAmount) {
        this.patientAmount = patientAmount;
    }

    public Double getSponsorAmount() {
        return sponsorAmount;
    }

    public void setSponsorAmount(Double sponsorAmount) {
        this.sponsorAmount = sponsorAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCancellationDiscountApproverName() {
        return cancellationDiscountApproverName;
    }

    public void setCancellationDiscountApproverName(String cancellationDiscountApproverName) {
        this.cancellationDiscountApproverName = cancellationDiscountApproverName;
    }

    public String getBilledBy() {
        return billedBy;
    }

    public void setBilledBy(String billedBy) {
        this.billedBy = billedBy;
    }

    public String getIsProfile() {
        return isProfile;
    }

    public void setIsProfile(String isProfile) {
        this.isProfile = isProfile;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public LocalDateTime getDischargeDateTime() {
        return dischargeDateTime;
    }

    public void setDischargeDateTime(LocalDateTime dischargeDateTime) {
        this.dischargeDateTime = dischargeDateTime;
    }

    public String getBillingGroupName() {
        return billingGroupName;
    }

    public void setBillingGroupName(String billingGroupName) {
        this.billingGroupName = billingGroupName;
    }

    public LocalDateTime getServiceRequestDateTime() {
        return serviceRequestDateTime;
    }

    public void setServiceRequestDateTime(LocalDateTime serviceRequestDateTime) {
        this.serviceRequestDateTime = serviceRequestDateTime;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getServiceRenderingDateTime() {
        return serviceRenderingDateTime;
    }

    public void setServiceRenderingDateTime(String serviceRenderingDateTime) {
        this.serviceRenderingDateTime = serviceRenderingDateTime;
    }

    public String getRenderedBy() {
        return renderedBy;
    }

    public void setRenderedBy(String renderedBy) {
        this.renderedBy = renderedBy;
    }

    public String getRequestingServicePatientWard() {
        return requestingServicePatientWard;
    }

    public void setRequestingServicePatientWard(String requestingServicePatientWard) {
        this.requestingServicePatientWard = requestingServicePatientWard;
    }

    public String getRenderingServicePatientWard() {
        return renderingServicePatientWard;
    }

    public void setRenderingServicePatientWard(String renderingServicePatientWard) {
        this.renderingServicePatientWard = renderingServicePatientWard;
    }

    public Double getHospitalUnitRate() {
        return hospitalUnitRate;
    }

    public void setHospitalUnitRate(Double hospitalUnitRate) {
        this.hospitalUnitRate = hospitalUnitRate;
    }

    public Double getPlanUnitRate() {
        return planUnitRate;
    }

    public void setPlanUnitRate(Double planUnitRate) {
        this.planUnitRate = planUnitRate;
    }

    public String getAddonParameterValue() {
        return addonParameterValue;
    }

    public void setAddonParameterValue(String addonParameterValue) {
        this.addonParameterValue = addonParameterValue;
    }

    public Double getTotalMrp() {
        return totalMrp;
    }

    public void setTotalMrp(Double totalMrp) {
        this.totalMrp = totalMrp;
    }

    public String getRevenueDepartment() {
        return revenueDepartment;
    }

    public void setRevenueDepartment(String revenueDepartment) {
        this.revenueDepartment = revenueDepartment;
    }

    public String getRevenueDoctor() {
        return revenueDoctor;
    }

    public void setRevenueDoctor(String revenueDoctor) {
        this.revenueDoctor = revenueDoctor;
    }

    public String getFinanceGroupName() {
        return financeGroupName;
    }

    public void setFinanceGroupName(String financeGroupName) {
        this.financeGroupName = financeGroupName;
    }

    public String getOriginalInvoiceNo() {
        return originalInvoiceNo;
    }

    public void setOriginalInvoiceNo(String originalInvoiceNo) {
        this.originalInvoiceNo = originalInvoiceNo;
    }

    public String getAddonParameter() {
        return addonParameter;
    }

    public void setAddonParameter(String addonParameter) {
        this.addonParameter = addonParameter;
    }

    public LocalDateTime getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(LocalDateTime orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean isExecuted() {
        return executed;
    }
    public ServiceAnalysisStaging executed(Boolean executed){
        this.executed=executed;
        return this;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    public String getVisitTypeTariffClassKey() {
        return visitTypeTariffClassKey;
    }

    public void setVisitTypeTariffClassKey(String visitTypeTariffClassKey) {
        StringBuilder key=new StringBuilder();
        if(null!= this.getVisitType()) {
            key.append(this.visitType);
        }else{
            key.append("-X");
        }
        if(null!=this.getTariffClass() ) {
            key.append(this.tariffClass);
        }else{
            key.append("-X");
        }
        this.visitTypeTariffClassKey = visitTypeTariffClassKey;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Boolean getDepartmentRevenueExecuted() {
        return departmentRevenueExecuted;
    }

    public void setDepartmentRevenueExecuted(Boolean departmentRevenueExecuted) {
        this.departmentRevenueExecuted = departmentRevenueExecuted;
    }

    public Boolean getLengthOfStayExecuted() {
        return lengthOfStayExecuted;
    }

    public void setLengthOfStayExecuted(Boolean lengthOfStayExecuted) {
        this.lengthOfStayExecuted = lengthOfStayExecuted;
    }

    public String getOrderingDoctorLogin() {
        return orderingDoctorLogin;
    }

    public void setOrderingDoctorLogin(String orderingDoctorLogin) {
        this.orderingDoctorLogin = orderingDoctorLogin;
    }

    public String getRenderingDoctorLogin() {
        return renderingDoctorLogin;
    }

    public void setRenderingDoctorLogin(String renderingDoctorLogin) {
        this.renderingDoctorLogin = renderingDoctorLogin;
    }

    public String getRenderingDepartmentCode() {
        return renderingDepartmentCode;
    }

    public void setRenderingDepartmentCode(String renderingDepartmentCode) {
        this.renderingDepartmentCode = renderingDepartmentCode;
    }

    public String getAdmittingDoctorlogin() {
        return admittingDoctorlogin;
    }

    public void setAdmittingDoctorlogin(String admittingDoctorlogin) {
        this.admittingDoctorlogin = admittingDoctorlogin;
    }

    public String getAdmittingDepartmentCode() {
        return admittingDepartmentCode;
    }

    public void setAdmittingDepartmentCode(String admittingDepartmentCode) {
        this.admittingDepartmentCode = admittingDepartmentCode;
    }

    public String getPackageCategory() {
        return packageCategory;
    }

    public void setPackageCategory(String packageCategory) {
        this.packageCategory = packageCategory;
    }

    public String getOrderingDepartmentCode() {
        return orderingDepartmentCode;
    }

    public void setOrderingDepartmentCode(String orderingDepartmentCode) {
        this.orderingDepartmentCode = orderingDepartmentCode;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public Boolean getOrderingDoctorExecuted() {
        return orderingDoctorExecuted;
    }

    public void setOrderingDoctorExecuted(Boolean orderingDoctorExecuted) {
        this.orderingDoctorExecuted = orderingDoctorExecuted;
    }

    public Boolean getAdmittingDoctorExecuted() {
        return admittingDoctorExecuted;
    }

    public void setAdmittingDoctorExecuted(Boolean admittingDoctorExecuted) {
        this.admittingDoctorExecuted = admittingDoctorExecuted;
    }

    public Boolean getRenderingDoctorExecuted() {
        return renderingDoctorExecuted;
    }

    public void setRenderingDoctorExecuted(Boolean renderingDoctorExecuted) {
        this.renderingDoctorExecuted = renderingDoctorExecuted;
    }

    public ServiceAnalysisStaging renderingDoctorExecuted(Boolean renderingDoctorExecuted){
        this.renderingDoctorExecuted=renderingDoctorExecuted;
        return this;
    }

    public ServiceAnalysisStaging admittingDoctorExecuted(Boolean admittingDoctorExecuted){
        this.admittingDoctorExecuted=admittingDoctorExecuted;
        return this;
    }

    public ServiceAnalysisStaging orderingDoctorExecuted(Boolean orderingDoctorExecuted){
        this.orderingDoctorExecuted=orderingDoctorExecuted;
        return this;
    }

    public Long getReferPackageId() {
        return referPackageId;
    }

    public void setReferPackageId(Long referPackageId) {
        this.referPackageId = referPackageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceAnalysisStaging that = (ServiceAnalysisStaging) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(invoiceType, that.invoiceType) &&
            Objects.equals(invoiceDate, that.invoiceDate) &&
            Objects.equals(admissionDate, that.admissionDate) &&
            Objects.equals(admittedFor, that.admittedFor) &&
            Objects.equals(visitNo, that.visitNo) &&
            Objects.equals(patientMrn, that.patientMrn) &&
            Objects.equals(patientName, that.patientName) &&
            Objects.equals(patientGender, that.patientGender) &&
            Objects.equals(patientAge, that.patientAge) &&
            Objects.equals(visitType, that.visitType) &&
            Objects.equals(year, that.year) &&
            Objects.equals(month, that.month) &&
            Objects.equals(orderingDepartmentName, that.orderingDepartmentName) &&
            Objects.equals(orderingDoctorName, that.orderingDoctorName) &&
            Objects.equals(renderingDepartmentName, that.renderingDepartmentName) &&
            Objects.equals(renderingDoctorName, that.renderingDoctorName) &&
            Objects.equals(type, that.type) &&
            Objects.equals(itemCode, that.itemCode) &&
            Objects.equals(itemName, that.itemName) &&
            Objects.equals(itemGroup, that.itemGroup) &&
            Objects.equals(patientType, that.patientType) &&
            Objects.equals(sponsorRefName, that.sponsorRefName) &&
            Objects.equals(planName, that.planName) &&
            Objects.equals(tariffClass, that.tariffClass) &&
            Objects.equals(grossAmount, that.grossAmount) &&
            Objects.equals(taxAmount, that.taxAmount) &&
            Objects.equals(discretionaryConcession, that.discretionaryConcession) &&
            Objects.equals(nonDiscretionaryConcession, that.nonDiscretionaryConcession) &&
            Objects.equals(netAmount, that.netAmount) &&
            Objects.equals(patientAmount, that.patientAmount) &&
            Objects.equals(sponsorAmount, that.sponsorAmount) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(cancellationDiscountApproverName, that.cancellationDiscountApproverName) &&
            Objects.equals(billedBy, that.billedBy) &&
            Objects.equals(isProfile, that.isProfile) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(itemType, that.itemType) &&
            Objects.equals(invoiceStatus, that.invoiceStatus) &&
            Objects.equals(settlementStatus, that.settlementStatus) &&
            Objects.equals(approvedDate, that.approvedDate) &&
            Objects.equals(dischargeDateTime, that.dischargeDateTime) &&
            Objects.equals(billingGroupName, that.billingGroupName) &&
            Objects.equals(serviceRequestDateTime, that.serviceRequestDateTime) &&
            Objects.equals(requestedBy, that.requestedBy) &&
            Objects.equals(serviceRenderingDateTime, that.serviceRenderingDateTime) &&
            Objects.equals(renderedBy, that.renderedBy) &&
            Objects.equals(requestingServicePatientWard, that.requestingServicePatientWard) &&
            Objects.equals(renderingServicePatientWard, that.renderingServicePatientWard) &&
            Objects.equals(hospitalUnitRate, that.hospitalUnitRate) &&
            Objects.equals(planUnitRate, that.planUnitRate) &&
            Objects.equals(addonParameterValue, that.addonParameterValue) &&
            Objects.equals(totalMrp, that.totalMrp) &&
            Objects.equals(revenueDepartment, that.revenueDepartment) &&
            Objects.equals(revenueDoctor, that.revenueDoctor) &&
            Objects.equals(financeGroupName, that.financeGroupName) &&
            Objects.equals(originalInvoiceNo, that.originalInvoiceNo) &&
            Objects.equals(addonParameter, that.addonParameter) &&
            Objects.equals(orderedDate, that.orderedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, invoiceNumber, invoiceType, invoiceDate, admissionDate, admittedFor, visitNo, patientMrn, patientName, patientGender, patientAge, visitType, year, month, orderingDepartmentName, orderingDoctorName, renderingDepartmentName, renderingDoctorName, type, itemCode, itemName, itemGroup, patientType, sponsorRefName, planName, tariffClass, grossAmount, taxAmount, discretionaryConcession, nonDiscretionaryConcession, netAmount, patientAmount, sponsorAmount, remarks, cancellationDiscountApproverName, billedBy, isProfile, quantity, itemType, invoiceStatus, settlementStatus, approvedDate, dischargeDateTime, billingGroupName, serviceRequestDateTime, requestedBy, serviceRenderingDateTime, renderedBy, requestingServicePatientWard, renderingServicePatientWard, hospitalUnitRate, planUnitRate, addonParameterValue, totalMrp, revenueDepartment, revenueDoctor, financeGroupName, originalInvoiceNo, addonParameter, orderedDate);
    }

    @Override
    public String toString() {
        return "ServiceAnalysisStaging{" +
            "id=" + id +
            ", invoiceNumber='" + invoiceNumber + '\'' +
            ", invoiceType='" + invoiceType + '\'' +
            ", invoiceDate=" + invoiceDate +
            ", admissionDate=" + admissionDate +
            ", admittedFor='" + admittedFor + '\'' +
            ", visitNo='" + visitNo + '\'' +
            ", patientMrn='" + patientMrn + '\'' +
            ", patientName='" + patientName + '\'' +
            ", patientGender='" + patientGender + '\'' +
            ", patientAge=" + patientAge +
            ", visitType='" + visitType + '\'' +
            ", year=" + year +
            ", month=" + month +
            ", orderingDepartmentName='" + orderingDepartmentName + '\'' +
            ", orderingDoctorName='" + orderingDoctorName + '\'' +
            ", renderingDepartmentName='" + renderingDepartmentName + '\'' +
            ", renderingDoctorName='" + renderingDoctorName + '\'' +
            ", type='" + type + '\'' +
            ", itemCode='" + itemCode + '\'' +
            ", itemName='" + itemName + '\'' +
            ", itemGroup='" + itemGroup + '\'' +
            ", patientType='" + patientType + '\'' +
            ", sponsorRefName='" + sponsorRefName + '\'' +
            ", planName='" + planName + '\'' +
            ", tariffClass='" + tariffClass + '\'' +
            ", grossAmount=" + grossAmount +
            ", taxAmount=" + taxAmount +
            ", discretionaryConcession=" + discretionaryConcession +
            ", nonDiscretionaryConcession=" + nonDiscretionaryConcession +
            ", netAmount=" + netAmount +
            ", patientAmount=" + patientAmount +
            ", sponsorAmount=" + sponsorAmount +
            ", remarks='" + remarks + '\'' +
            ", cancellationDiscountApproverName='" + cancellationDiscountApproverName + '\'' +
            ", billedBy='" + billedBy + '\'' +
            ", isProfile='" + isProfile + '\'' +
            ", quantity='" + quantity + '\'' +
            ", itemType='" + itemType + '\'' +
            ", invoiceStatus='" + invoiceStatus + '\'' +
            ", settlementStatus='" + settlementStatus + '\'' +
            ", approvedDate=" + approvedDate +
            ", dischargeDateTime=" + dischargeDateTime +
            ", billingGroupName='" + billingGroupName + '\'' +
            ", serviceRequestDateTime=" + serviceRequestDateTime +
            ", requestedBy='" + requestedBy + '\'' +
            ", serviceRenderingDateTime='" + serviceRenderingDateTime + '\'' +
            ", renderedBy='" + renderedBy + '\'' +
            ", requestingServicePatientWard='" + requestingServicePatientWard + '\'' +
            ", renderingServicePatientWard='" + renderingServicePatientWard + '\'' +
            ", hospitalUnitRate=" + hospitalUnitRate +
            ", planUnitRate=" + planUnitRate +
            ", addonParameterValue='" + addonParameterValue + '\'' +
            ", totalMrp=" + totalMrp +
            ", revenueDepartment='" + revenueDepartment + '\'' +
            ", revenueDoctor='" + revenueDoctor + '\'' +
            ", financeGroupName='" + financeGroupName + '\'' +
            ", originalInvoiceNo='" + originalInvoiceNo + '\'' +
            ", addonParameter='" + addonParameter + '\'' +
            ", orderedDate=" + orderedDate +
            '}';
    }

    private  static String checkStringNullCondition(String arg1){
        if(arg1!=null && !arg1.isEmpty() && !arg1.equalsIgnoreCase("null")){
            return arg1;
        }else{

            return null;
        }

    }
}
