package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nh.artha.domain.enumeration.PatientGender;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A McrStaging.
 */
@Entity
@Table(name = "mcr_staging")
@Document(indexName = "artha_mcrstaging",createIndex = false)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class McrStaging implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @NotNull
    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "visit_no")
    private String visitNo;

    @Column(name = "mrn")
    private String mrn;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "patient_type")
    private String patientType;


    @Column(name = "patient_gender", nullable = false)
    private String patientGender;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "doctor")
    private String doctor;

    @Column(name = "ordering_department")
    private String orderingDepartment;

    @Column(name = "ordering_hsc_name")
    private String orderingHscName;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    @Column(name = "issuing_store")
    private String issuingStore;

    @Column(name = "issue_date_time")
    private LocalDateTime issueDateTime;

    @Column(name = "item_type")
    private String itemType;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "batch")
    private String batch;

    @Column(name = "expiry")
    private String expiry;

    @Column(name = "unit_cost")
    private Double unitCost;

    @Column(name = "unit_mrp")
    private Double unitMrp;

    @Column(name = "unit_sale_rate")
    private Double unitSaleRate;

    @Column(name = "unit_tax")
    private Double unitTax;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "uom")
    private String uom;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "purchase_unit_tax")
    private Double purchaseUnitTax;

    @Column(name = "cost_with_purchase_tax")
    private Double costWithPurchaseTax;

    @Column(name = "purchase_tax_amount")
    private Double purchaseTaxAmount;

    @Column(name = "total_mrp")
    private Double totalMrp;

    @Column(name = "total_sale_rate")
    private Double totalSaleRate;

    @Column(name = "total_tax")
    private Double totalTax;

    @Column(name = "discretionary_concession")
    private Double discretionaryConcession;

    @Column(name = "non_discretionary_concession")
    private Double nonDiscretionaryConcession;

    @Column(name = "item_net_amount")
    private Double itemNetAmount;

    @Column(name = "in_package")
    private String inPackage;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "patient_payable")
    private Double patientPayable;

    @Column(name = "sponsor_payable")
    private Double sponsorPayable;

    @Column(name = "sponsor_ref_name")
    private String sponsorRefName;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "invoice_status")
    private String invoiceStatus;

    @Column(name = "invoice_approved_date_time")
    private LocalDateTime invoiceApprovedDateTime;

    @Column(name = "billing_group")
    private String billingGroup;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "visit_type_dept_amt_material")
    private String visitTypeDeptAmtMaterial;

    @Column(name = "invoice_payout_created")
    private Boolean invoicePayoutCreated=Boolean.FALSE;

    @Column(name = "executed")
    private Boolean executed=Boolean.FALSE;

    @Column(name = "ordering_department_code")
    private String orderingDepartmentCode;

    @Column(name = "ordering_doctor_login")
    private String orderingDoctorLogin;

    @Column(name = "admitting_doctor_login")
    private String admittingDoctorlogin;

    @Column(name = "admitting_department_code")
    private String admittingDepartmentCode;

    @Column(name = "billing_group_code")
    private String billingGroupCode;

    @Column(name = "item_category_code")
    private String itemCategoryCode;

    @Column(name = "plan_code")
    private String planCode;

    @Column(name = "ordering_doctor_executed")
    private Boolean orderingDoctorExecuted;

    @Column(name = "admitting_doctor_executed")
    private Boolean admittingDoctorExecuted;

    @Column(name = "rendering_doctor_executed")
    private Boolean renderingDoctorExecuted;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public McrStaging(){

    }

    public McrStaging(@NotNull String invoiceNumber, @NotNull String invoiceDate, String visitType, String visitNo,
                      String mrn, String patientName, String patientType, @NotNull String patientGender,
                      String year, String month, String doctor, String orderingDepartment, String orderingHscName,
                      String orderDateTime, String issuingStore, String issueDateTime, String itemType,
                      String itemCode, String itemName, String batch, String expiry, String unitCost, String unitMrp,
                      String unitSaleRate, String unitTax, String quantity, String uom, String totalCost, String purchaseUnitTax,
                      String costWithPurchaseTax, String purchaseTaxAmount, String totalMrp, String totalSaleRate, String totalTax,
                      String discretionaryConcession, String nonDiscretionaryConcession, String itemNetAmount, String inPackage,
                      String packageName, Double patientPayable, Double sponsorPayable, String sponsorRefName, String planName,
                      String invoiceStatus, String invoiceApprovedDateTime, String billingGroup,String employeeId,String departmentId,String unitCode) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = (invoiceDate!=null && !invoiceDate.isEmpty() && !invoiceDate.trim().equalsIgnoreCase("null"))?LocalDateTime.parse(invoiceDate.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")):null;
        this.visitType = visitType;
        this.visitNo = visitNo;
        this.mrn = mrn;
        this.patientName = patientName;
        this.patientType = patientType;
        this.patientGender =patientGender;
        this.year = Integer.parseInt(year);
        this.month = Integer.parseInt(month);
        this.doctor = doctor;
        this.orderingDepartment = orderingDepartment;
        this.orderingHscName = orderingHscName;
        this.orderDateTime = (orderDateTime!=null && !orderDateTime.isEmpty() && !orderDateTime.trim().equalsIgnoreCase("null"))?LocalDateTime.parse(orderDateTime.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")):null;
        this.issuingStore = issuingStore;
        this.issueDateTime = (issueDateTime!=null && !issueDateTime.isEmpty() && !issueDateTime.trim().equalsIgnoreCase("null"))?LocalDateTime.parse(issueDateTime.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")):null;
        this.itemType = itemType;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.batch = batch;
        this.expiry = expiry;
        this.unitCost = Double.parseDouble(unitCost);
        this.unitMrp = Double.parseDouble(unitMrp);
        this.unitSaleRate = Double.parseDouble(unitSaleRate);
        this.unitTax = Double.parseDouble(unitTax);
        this.quantity = Double.parseDouble(quantity);
        this.uom = uom;
        this.totalCost = Double.parseDouble(totalCost);
        this.purchaseUnitTax = Double.parseDouble(purchaseUnitTax);
        this.costWithPurchaseTax = Double.parseDouble(costWithPurchaseTax);
        this.purchaseTaxAmount = Double.parseDouble(purchaseTaxAmount);
        this.totalMrp = Double.parseDouble(totalMrp);
        this.totalSaleRate = Double.parseDouble(totalSaleRate);
        this.totalTax = Double.parseDouble(totalTax);
        this.discretionaryConcession = Double.parseDouble(discretionaryConcession);
        this.nonDiscretionaryConcession = Double.parseDouble(nonDiscretionaryConcession);
        this.itemNetAmount = Double.parseDouble(itemNetAmount);
        this.inPackage = inPackage;
        this.packageName = packageName;
        this.patientPayable = patientPayable;
        this.sponsorPayable = sponsorPayable;
        this.sponsorRefName = sponsorRefName;
        this.planName = planName;
        this.invoiceStatus = invoiceStatus;
        this.invoiceApprovedDateTime = (invoiceApprovedDateTime!=null && !invoiceApprovedDateTime.isEmpty() && !invoiceApprovedDateTime.trim().equalsIgnoreCase("null")) && invoiceApprovedDateTime.trim().equalsIgnoreCase("null")?LocalDateTime.parse(invoiceApprovedDateTime.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")):null;
        this.billingGroup = billingGroup;
        this.employeeId=Long.parseLong(employeeId);
        this.departmentCode=departmentId;
        this.unitCode=unitCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public McrStaging invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public McrStaging invoiceDate(String invoiceDate) {
        this.invoiceDate = LocalDateTime.parse(invoiceDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        return this;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = LocalDateTime.now();
    }

    public String getVisitType() {
        return visitType;
    }

    public McrStaging visitType(String visitType) {
        this.visitType = visitType;
        return this;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public McrStaging visitNo(String visitNo) {
        this.visitNo = visitNo;
        return this;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getMrn() {
        return mrn;
    }

    public McrStaging mrn(String mrn) {
        this.mrn = mrn;
        return this;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getPatientName() {
        return patientName;
    }

    public McrStaging patientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientType() {
        return patientType;
    }

    public McrStaging patientType(String patientType) {
        this.patientType = patientType;
        return this;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public McrStaging patientGender(String patientGender) {
        this.patientGender = patientGender;
        return this;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public Integer getYear() {
        return year;
    }

    public McrStaging year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public McrStaging month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getDoctor() {
        return doctor;
    }

    public McrStaging doctor(String doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getOrderingDepartment() {
        return orderingDepartment;
    }

    public McrStaging orderingDepartment(String orderingDepartment) {
        this.orderingDepartment = orderingDepartment;
        return this;
    }

    public void setOrderingDepartment(String orderingDepartment) {
        this.orderingDepartment = orderingDepartment;
    }

    public String getOrderingHscName() {
        return orderingHscName;
    }

    public McrStaging orderingHscName(String orderingHscName) {
        this.orderingHscName = orderingHscName;
        return this;
    }

    public void setOrderingHscName(String orderingHscName) {
        this.orderingHscName = orderingHscName;
    }


    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public McrStaging orderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
        return this;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = LocalDateTime.now();
    }

    public String getIssuingStore() {
        return issuingStore;
    }

    public McrStaging issuingStore(String issuingStore) {
        this.issuingStore = issuingStore;
        return this;
    }

    public void setIssuingStore(String issuingStore) {
        this.issuingStore = issuingStore;
    }

    public LocalDateTime getIssueDateTime() {
        return issueDateTime;
    }

    public McrStaging issueDateTime(LocalDateTime issueDateTime) {
        this.issueDateTime = issueDateTime;
        return this;
    }

    public void setIssueDateTime(String issueDateTime) {
        this.issueDateTime = LocalDateTime.now();
    }

    public String getItemType() {
        return itemType;
    }

    public McrStaging itemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McrStaging itemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public McrStaging itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBatch() {
        return batch;
    }

    public McrStaging batch(String batch) {
        this.batch = batch;
        return this;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getExpiry() {
        return expiry;
    }

    public McrStaging expiry(String expiry) {
        this.expiry = expiry;
        return this;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public McrStaging unitCost(Double unitCost) {
        this.unitCost = unitCost;
        return this;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public Double getUnitMrp() {
        return unitMrp;
    }

    public McrStaging unitMrp(Double unitMrp) {
        this.unitMrp = unitMrp;
        return this;
    }

    public void setUnitMrp(Double unitMrp) {
        this.unitMrp = unitMrp;
    }

    public Double getUnitSaleRate() {
        return unitSaleRate;
    }

    public McrStaging unitSaleRate(Double unitSaleRate) {
        this.unitSaleRate = unitSaleRate;
        return this;
    }

    public void setUnitSaleRate(Double unitSaleRate) {
        this.unitSaleRate = unitSaleRate;
    }

    public Double getUnitTax() {
        return unitTax;
    }

    public McrStaging unitTax(Double unitTax) {
        this.unitTax = unitTax;
        return this;
    }

    public void setUnitTax(Double unitTax) {
        this.unitTax = unitTax;
    }

    public Double getQuantity() {
        return quantity;
    }

    public McrStaging quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public McrStaging uom(String uom) {
        this.uom = uom;
        return this;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public McrStaging totalCost(Double totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getPurchaseUnitTax() {
        return purchaseUnitTax;
    }

    public McrStaging purchaseUnitTax(Double purchaseUnitTax) {
        this.purchaseUnitTax = purchaseUnitTax;
        return this;
    }

    public void setPurchaseUnitTax(Double purchaseUnitTax) {
        this.purchaseUnitTax = purchaseUnitTax;
    }

    public Double getCostWithPurchaseTax() {
        return costWithPurchaseTax;
    }

    public McrStaging costWithPurchaseTax(Double costWithPurchaseTax) {
        this.costWithPurchaseTax = costWithPurchaseTax;
        return this;
    }

    public void setCostWithPurchaseTax(Double costWithPurchaseTax) {
        this.costWithPurchaseTax = costWithPurchaseTax;
    }

    public Double getTotalMrp() {
        return totalMrp;
    }

    public McrStaging totalMrp(Double totalMrp) {
        this.totalMrp = totalMrp;
        return this;
    }

    public void setTotalMrp(Double totalMrp) {
        this.totalMrp = totalMrp;
    }

    public Double getTotalSaleRate() {
        return totalSaleRate;
    }

    public McrStaging totalSaleRate(Double totalSaleRate) {
        this.totalSaleRate = totalSaleRate;
        return this;
    }

    public void setTotalSaleRate(Double totalSaleRate) {
        this.totalSaleRate = totalSaleRate;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public McrStaging totalTax(Double totalTax) {
        this.totalTax = totalTax;
        return this;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getDiscretionaryConcession() {
        return discretionaryConcession;
    }

    public McrStaging discretionaryConcession(Double discretionaryConcession) {
        this.discretionaryConcession = discretionaryConcession;
        return this;
    }

    public void setDiscretionaryConcession(Double discretionaryConcession) {
        this.discretionaryConcession = discretionaryConcession;
    }

    public Double getNonDiscretionaryConcession() {
        return nonDiscretionaryConcession;
    }

    public McrStaging nonDiscretionaryConcession(Double nonDiscretionaryConcession) {
        this.nonDiscretionaryConcession = nonDiscretionaryConcession;
        return this;
    }

    public void setNonDiscretionaryConcession(Double nonDiscretionaryConcession) {
        this.nonDiscretionaryConcession = nonDiscretionaryConcession;
    }

    public Double getItemNetAmount() {
        return itemNetAmount;
    }

    public McrStaging itemNetAmount(Double itemNetAmount) {
        this.itemNetAmount = itemNetAmount;
        return this;
    }

    public void setItemNetAmount(Double itemNetAmount) {
        this.itemNetAmount = itemNetAmount;
    }

    public String getInPackage() {
        return inPackage;
    }

    public McrStaging inPackage(String inPackage) {
        this.inPackage = inPackage;
        return this;
    }

    public void setInPackage(String inPackage) {
        this.inPackage = inPackage;
    }

    public String getPackageName() {
        return packageName;
    }

    public McrStaging packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Double getPatientPayable() {
        return patientPayable;
    }

    public McrStaging patientPayable(Double patientPayable) {
        this.patientPayable = patientPayable;
        return this;
    }

    public void setPatientPayable(Double patientPayable) {
        this.patientPayable = patientPayable;
    }

    public Double getSponsorPayable() {
        return sponsorPayable;
    }

    public McrStaging sponsorPayable(Double sponsorPayable) {
        this.sponsorPayable = sponsorPayable;
        return this;
    }

    public void setSponsorPayable(Double sponsorPayable) {
        this.sponsorPayable = sponsorPayable;
    }

    public String getSponsorRefName() {
        return sponsorRefName;
    }

    public McrStaging sponsorRefName(String sponsorRefName) {
        this.sponsorRefName = sponsorRefName;
        return this;
    }

    public void setSponsorRefName(String sponsorRefName) {
        this.sponsorRefName = sponsorRefName;
    }

    public String getPlanName() {
        return planName;
    }

    public McrStaging planName(String planName) {
        this.planName = planName;
        return this;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public McrStaging invoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
        return this;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public LocalDateTime getInvoiceApprovedDateTime() {
        return invoiceApprovedDateTime;
    }

    public McrStaging invoiceApprovedDateTime(LocalDateTime invoiceApprovedDateTime) {
        this.invoiceApprovedDateTime = invoiceApprovedDateTime;
        return this;
    }

    public void setInvoiceApprovedDateTime(String invoiceApprovedDateTime) {
        this.invoiceApprovedDateTime =LocalDateTime.now();
    }

    public String getBillingGroup() {
        return billingGroup;
    }

    public McrStaging billingGroup(String billingGroup) {
        this.billingGroup = billingGroup;
        return this;
    }

    public void setBillingGroup(String billingGroup) {
        this.billingGroup = billingGroup;
    }

    public Double getPurchaseTaxAmount() {
        return purchaseTaxAmount;
    }

    public void setPurchaseTaxAmount(Double purchaseTaxAmount) {
        this.purchaseTaxAmount = purchaseTaxAmount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public void setIssueDateTime(LocalDateTime issueDateTime) {
        this.issueDateTime = issueDateTime;
    }

    public void setInvoiceApprovedDateTime(LocalDateTime invoiceApprovedDateTime) {
        this.invoiceApprovedDateTime = invoiceApprovedDateTime;
    }

    public McrStaging departmentCode(String departmentCode) {
        this.departmentCode=departmentCode;
        return this;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    public McrStaging executed(Boolean executed){
        this.executed=executed;
        return this;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getVisitTypeDeptAmtMaterial() {
        return visitTypeDeptAmtMaterial;
    }

    public void setVisitTypeDeptAmtMaterial(String visitTypeDeptAmtMaterial) {
        this.visitTypeDeptAmtMaterial = visitTypeDeptAmtMaterial;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getInvoicePayoutCreated() {
        return invoicePayoutCreated;
    }

    public void setInvoicePayoutCreated(Boolean invoicePayoutCreated) {
        this.invoicePayoutCreated = invoicePayoutCreated;
    }

    public String getBillingGroupCode() {
        return billingGroupCode;
    }

    public void setBillingGroupCode(String billingGroupCode) {
        this.billingGroupCode = billingGroupCode;
    }

    public String getOrderingDepartmentCode() {
        return orderingDepartmentCode;
    }

    public void setOrderingDepartmentCode(String orderingDepartmentCode) {
        this.orderingDepartmentCode = orderingDepartmentCode;
    }

    public String getOrderingDoctorLogin() {
        return orderingDoctorLogin;
    }

    public void setOrderingDoctorLogin(String orderingDoctorLogin) {
        this.orderingDoctorLogin = orderingDoctorLogin;
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

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof McrStaging)) {
            return false;
        }
        return id != null && id.equals(((McrStaging) o).id);
    }


    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "McrStaging{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", visitType='" + getVisitType() + "'" +
            ", visitNo='" + getVisitNo() + "'" +
            ", mrn='" + getMrn() + "'" +
            ", patientName='" + getPatientName() + "'" +
            ", patientType='" + getPatientType() + "'" +
            ", patientType='" + getPatientType() + "'" +
            ", patientGender='" + getPatientGender() + "'" +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            ", doctor='" + getDoctor() + "'" +
            ", orderingDepartment='" + getOrderingDepartment() + "'" +
            ", orderingHscName='" + getOrderingHscName() + "'" +
            ", orderDateTime='" + getOrderDateTime() + "'" +
            ", issuingStore='" + getIssuingStore() + "'" +
            ", issueDateTime='" + getIssueDateTime() + "'" +
            ", itemType='" + getItemType() + "'" +
            ", itemCode='" + getItemCode() + "'" +
            ", itemName='" + getItemName() + "'" +
            ", batch='" + getBatch() + "'" +
            ", expiry='" + getExpiry() + "'" +
            ", unitCost=" + getUnitCost() +
            ", unitMrp=" + getUnitMrp() +
            ", unitSaleRate=" + getUnitSaleRate() +
            ", unitTax=" + getUnitTax() +
            ", quantity=" + getQuantity() +
            ", uom='" + getUom() + "'" +
            ", totalCost=" + getTotalCost() +
            ", purchaseUnitTax=" + getPurchaseUnitTax() +
            ", costWithPurchaseTax=" + getCostWithPurchaseTax() +
            ", totalMrp=" + getTotalMrp() +
            ", totalSaleRate=" + getTotalSaleRate() +
            ", totalTax=" + getTotalTax() +
            ", discretionaryConcession=" + getDiscretionaryConcession() +
            ", nonDiscretionaryConcession=" + getNonDiscretionaryConcession() +
            ", itemNetAmount=" + getItemNetAmount() +
            ", inPackage='" + getInPackage() + "'" +
            ", packageName='" + getPackageName() + "'" +
            ", patientPayable='" + getPatientPayable() + "'" +
            ", sponsorPayable='" + getSponsorPayable() + "'" +
            ", sponsorRefName='" + getSponsorRefName() + "'" +
            ", planName='" + getPlanName() + "'" +
            ", invoiceStatus='" + getInvoiceStatus() + "'" +
            ", invoiceApproveDateTime='" + getInvoiceApprovedDateTime() + "'" +
            ", billingGroup='" + getBillingGroup() + "'" +
            "}";
    }
}
