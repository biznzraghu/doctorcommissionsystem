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
@Table(name = "invoice_list_staging")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvoiceListStaging {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "patient_mrn")
    private String patientMrn;


    @Column(name = "patient_name")
    private String patientName;


    @Column(name = "patient_gender")
    private String patientGender;


    @Column(name = "patient_age")
    private Integer patientAge;


    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "invoice_number")
    private String invoiceNumber;


    @Column(name = "invoice_type")
    private String invoiceType;


    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "approver_name")
    private String approverName;

    @Column(name = "approver_code")
    private String approverCode;

    @Column(name = "gross_amount")
    private Double grossAmount;

    @Column(name = "total_discount_amount")
    private Double totalDiscountAmount;

    @Column(name = "net_invoice_amount")
    private Double netInvoiceAmount;

    @Column(name = "total_tax_amount")
    private Double totalTaxAmount;

    @Column(name = "invoice_amount")
    private Double invoiceAmount;

    @Column(name = "patient_payable")
    private Double patientPayable;

    @Column(name = "sponsor_payable")
    private Double sponsorPayable;

    @Column(name = "patient_discretionary_discount")
    private Double patientDiscretionaryDiscount;

    @Column(name = "patient_non_discretionary_discount")
    private Double patientNonDiscretionaryDiscount;

    @Column(name = "patient_tax_amount")
    private Double patientTaxAmount;

    @Column(name = "patient_paid_amount")
    private Double patientPaidAmount;

    @Column(name = "settlement_status")
    private String settlementStatus;

    @Column(name = "sponsor_discount")
    private Double sponsorDiscount;

    @Column(name = "sponsor_tax_amount")
    private Double sponsorTaxAmount;

    @Column(name = "invoice_amount_services")
    private Double invoiceAmountServices;

    @Column(name = "invoice_amount_pharmacy")
    private Double invoiceAmountPharmacy;

    @Column(name = "tax_amount_services")
    private Double taxAmountServices;

    @Column(name = "tax_amount_pharmacy")
    private Double taxAmountPharmacy;

    @Column(name = "discount_amount_services")
    private Double discountAmountServices;

    @Column(name = "discount_amount_pharmacy")
    private Double discountAmountPharmacy;

    @Column(name = "billed_by_name")
    private String billedByName;

    @Column(name = "billed_by_code")
    private String billedByCode;

    @Column(name = "hsc_name")
    private String hscName;

    @Column(name = "hsc_code")
    private String hscCode;

    @Column(name = "consultant_name")
    private String consultantName;

    @Column(name = "consultant_code")
    private String consultantCode;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "plan_code")
    private String planCode;

    @Column(name = "sponsor_name")
    private String sponsorName;

    @Column(name = "sponsor_code")
    private String sponsorCode;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "executed")
    private Boolean executed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getApproverCode() {
        return approverCode;
    }

    public void setApproverCode(String approverCode) {
        this.approverCode = approverCode;
    }

    public Double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public Double getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(Double totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public Double getNetInvoiceAmount() {
        return netInvoiceAmount;
    }

    public void setNetInvoiceAmount(Double netInvoiceAmount) {
        this.netInvoiceAmount = netInvoiceAmount;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Double getPatientPayable() {
        return patientPayable;
    }

    public void setPatientPayable(Double patientPayable) {
        this.patientPayable = patientPayable;
    }

    public Double getSponsorPayable() {
        return sponsorPayable;
    }

    public void setSponsorPayable(Double sponsorPayable) {
        this.sponsorPayable = sponsorPayable;
    }

    public Double getPatientDiscretionaryDiscount() {
        return patientDiscretionaryDiscount;
    }

    public void setPatientDiscretionaryDiscount(Double patientDiscretionaryDiscount) {
        this.patientDiscretionaryDiscount = patientDiscretionaryDiscount;
    }

    public Double getPatientNonDiscretionaryDiscount() {
        return patientNonDiscretionaryDiscount;
    }

    public void setPatientNonDiscretionaryDiscount(Double patientNonDiscretionaryDiscount) {
        this.patientNonDiscretionaryDiscount = patientNonDiscretionaryDiscount;
    }

    public Double getPatientTaxAmount() {
        return patientTaxAmount;
    }

    public void setPatientTaxAmount(Double patientTaxAmount) {
        this.patientTaxAmount = patientTaxAmount;
    }

    public Double getPatientPaidAmount() {
        return patientPaidAmount;
    }

    public void setPatientPaidAmount(Double patientPaidAmount) {
        this.patientPaidAmount = patientPaidAmount;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public Double getSponsorDiscount() {
        return sponsorDiscount;
    }

    public void setSponsorDiscount(Double sponsorDiscount) {
        this.sponsorDiscount = sponsorDiscount;
    }

    public Double getSponsorTaxAmount() {
        return sponsorTaxAmount;
    }

    public void setSponsorTaxAmount(Double sponsorTaxAmount) {
        this.sponsorTaxAmount = sponsorTaxAmount;
    }

    public Double getInvoiceAmountServices() {
        return invoiceAmountServices;
    }

    public void setInvoiceAmountServices(Double invoiceAmountServices) {
        this.invoiceAmountServices = invoiceAmountServices;
    }

    public Double getInvoiceAmountPharmacy() {
        return invoiceAmountPharmacy;
    }

    public void setInvoiceAmountPharmacy(Double invoiceAmountPharmacy) {
        this.invoiceAmountPharmacy = invoiceAmountPharmacy;
    }

    public Double getTaxAmountServices() {
        return taxAmountServices;
    }

    public void setTaxAmountServices(Double taxAmountServices) {
        this.taxAmountServices = taxAmountServices;
    }

    public Double getTaxAmountPharmacy() {
        return taxAmountPharmacy;
    }

    public void setTaxAmountPharmacy(Double taxAmountPharmacy) {
        this.taxAmountPharmacy = taxAmountPharmacy;
    }

    public Double getDiscountAmountServices() {
        return discountAmountServices;
    }

    public void setDiscountAmountServices(Double discountAmountServices) {
        this.discountAmountServices = discountAmountServices;
    }

    public Double getDiscountAmountPharmacy() {
        return discountAmountPharmacy;
    }

    public void setDiscountAmountPharmacy(Double discountAmountPharmacy) {
        this.discountAmountPharmacy = discountAmountPharmacy;
    }

    public String getBilledByName() {
        return billedByName;
    }

    public void setBilledByName(String billedByName) {
        this.billedByName = billedByName;
    }

    public String getBilledByCode() {
        return billedByCode;
    }

    public void setBilledByCode(String billedByCode) {
        this.billedByCode = billedByCode;
    }

    public String getHscName() {
        return hscName;
    }

    public void setHscName(String hscName) {
        this.hscName = hscName;
    }

    public String getHscCode() {
        return hscCode;
    }

    public void setHscCode(String hscCode) {
        this.hscCode = hscCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceListStaging that = (InvoiceListStaging) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(patientMrn, that.patientMrn) &&
            Objects.equals(patientName, that.patientName) &&
            Objects.equals(patientGender, that.patientGender) &&
            Objects.equals(patientAge, that.patientAge) &&
            Objects.equals(visitType, that.visitType) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(invoiceType, that.invoiceType) &&
            Objects.equals(invoiceDate, that.invoiceDate) &&
            Objects.equals(approvedDate, that.approvedDate) &&
            Objects.equals(approverName, that.approverName) &&
            Objects.equals(approverCode, that.approverCode) &&
            Objects.equals(grossAmount, that.grossAmount) &&
            Objects.equals(totalDiscountAmount, that.totalDiscountAmount) &&
            Objects.equals(netInvoiceAmount, that.netInvoiceAmount) &&
            Objects.equals(totalTaxAmount, that.totalTaxAmount) &&
            Objects.equals(invoiceAmount, that.invoiceAmount) &&
            Objects.equals(patientPayable, that.patientPayable) &&
            Objects.equals(sponsorPayable, that.sponsorPayable) &&
            Objects.equals(patientDiscretionaryDiscount, that.patientDiscretionaryDiscount) &&
            Objects.equals(patientNonDiscretionaryDiscount, that.patientNonDiscretionaryDiscount) &&
            Objects.equals(patientTaxAmount, that.patientTaxAmount) &&
            Objects.equals(patientPaidAmount, that.patientPaidAmount) &&
            Objects.equals(settlementStatus, that.settlementStatus) &&
            Objects.equals(sponsorDiscount, that.sponsorDiscount) &&
            Objects.equals(sponsorTaxAmount, that.sponsorTaxAmount) &&
            Objects.equals(invoiceAmountServices, that.invoiceAmountServices) &&
            Objects.equals(invoiceAmountPharmacy, that.invoiceAmountPharmacy) &&
            Objects.equals(taxAmountServices, that.taxAmountServices) &&
            Objects.equals(taxAmountPharmacy, that.taxAmountPharmacy) &&
            Objects.equals(discountAmountServices, that.discountAmountServices) &&
            Objects.equals(discountAmountPharmacy, that.discountAmountPharmacy) &&
            Objects.equals(billedByName, that.billedByName) &&
            Objects.equals(billedByCode, that.billedByCode) &&
            Objects.equals(hscName, that.hscName) &&
            Objects.equals(hscCode, that.hscCode) &&
            Objects.equals(consultantName, that.consultantName) &&
            Objects.equals(consultantCode, that.consultantCode) &&
            Objects.equals(paymentMode, that.paymentMode) &&
            Objects.equals(planName, that.planName) &&
            Objects.equals(planCode, that.planCode) &&
            Objects.equals(sponsorName, that.sponsorName) &&
            Objects.equals(sponsorCode, that.sponsorCode) &&
            Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientMrn, patientName, patientGender, patientAge, visitType, invoiceNumber, invoiceType, invoiceDate, approvedDate, approverName, approverCode, grossAmount, totalDiscountAmount, netInvoiceAmount, totalTaxAmount, invoiceAmount, patientPayable, sponsorPayable, patientDiscretionaryDiscount, patientNonDiscretionaryDiscount, patientTaxAmount, patientPaidAmount, settlementStatus, sponsorDiscount, sponsorTaxAmount, invoiceAmountServices, invoiceAmountPharmacy, taxAmountServices, taxAmountPharmacy, discountAmountServices, discountAmountPharmacy, billedByName, billedByCode, hscName, hscCode, consultantName, consultantCode, paymentMode, planName, planCode, sponsorName, sponsorCode, remarks);
    }

    @Override
    public String toString() {
        return "InvoiceListStaging{" +
            "id=" + id +
            ", patientMrn='" + patientMrn + '\'' +
            ", patientName='" + patientName + '\'' +
            ", patientGender=" + patientGender +
            ", patientAge=" + patientAge +
            ", visitType='" + visitType + '\'' +
            ", invoiceNumber='" + invoiceNumber + '\'' +
            ", invoiceType='" + invoiceType + '\'' +
            ", invoiceDate='" + invoiceDate + '\'' +
            ", approvedDate='" + approvedDate + '\'' +
            ", approverName=" + approverName +
            ", approverCode='" + approverCode + '\'' +
            ", grossAmount=" + grossAmount +
            ", totalDiscountAmount=" + totalDiscountAmount +
            ", netInvoiceAmount='" + netInvoiceAmount + '\'' +
            ", totalTaxAmount='" + totalTaxAmount + '\'' +
            ", invoiceAmount='" + invoiceAmount + '\'' +
            ", patientPayable='" + patientPayable + '\'' +
            ", sponsorPayable='" + sponsorPayable + '\'' +
            ", patientDiscretionaryDiscount='" + patientDiscretionaryDiscount + '\'' +
            ", patientNonDiscretionaryDiscount='" + patientNonDiscretionaryDiscount + '\'' +
            ", patientTaxAmount='" + patientTaxAmount + '\'' +
            ", patientPaidAmount='" + patientPaidAmount + '\'' +
            ", settlementStatus='" + settlementStatus + '\'' +
            ", sponsorDiscount='" + sponsorDiscount + '\'' +
            ", sponsorTaxAmount='" + sponsorTaxAmount + '\'' +
            ", invoiceAmountServices=" + invoiceAmountServices +
            ", invoiceAmountPharmacy=" + invoiceAmountPharmacy +
            ", taxAmountServices=" + taxAmountServices +
            ", taxAmountPharmacy=" + taxAmountPharmacy +
            ", discountAmountServices=" + discountAmountServices +
            ", discountAmountPharmacy=" + discountAmountPharmacy +
            ", billedByName=" + billedByName +
            ", billedByCode='" + billedByCode + '\'' +
            ", hscName='" + hscName + '\'' +
            ", hscCode='" + hscCode + '\'' +
            ", consultantName='" + consultantName + '\'' +
            ", consultantCode='" + consultantCode + '\'' +
            ", paymentMode='" + paymentMode + '\'' +
            ", planName='" + planName + '\'' +
            ", planCode='" + planCode + '\'' +
            ", sponsorName=" + sponsorName +
            ", sponsorCode=" + sponsorCode +
            ", remarks='" + remarks + '\'' +
            '}';
    }
}
