package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * A InvoiceDoctorPayout.
 */
@Entity
@Table(name = "invoice_doctor_payout")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_invoicedoctorpayout")
@Setting(settingPath = "/es/settings.json")
public class InvoiceDoctorPayout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "variable_payout_id")
    private Long variablePayoutId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "invoice_line_item_id")
    private Long invoiceLineItemId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "service_benefiet_id")
    private Long serviceBenefietId;

    @Column(name = "service_benefiet_amount")
    private Double serviceBenefietAmount;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "mrn")
    private String mrn;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "order_date_time")
    private Instant orderDateTime;

    @Column(name = "issue_date_time")
    private Instant issueDateTime;

    @Column(name = "invoice_approved_date_time")
    private Instant invoiceApprovedDateTime;

    @Column(name = "sar_id")
    private Long sarId;

    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate=LocalDateTime.now();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @ReadOnlyProperty
    @JsonIgnore
    private DoctorPayout doctorPayout;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public InvoiceDoctorPayout name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public InvoiceDoctorPayout status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVariablePayoutId() {
        return variablePayoutId;
    }

    public InvoiceDoctorPayout variablePayoutId(Long variablePayoutId) {
        this.variablePayoutId = variablePayoutId;
        return this;
    }

    public void setVariablePayoutId(Long variablePayoutId) {
        this.variablePayoutId = variablePayoutId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public InvoiceDoctorPayout invoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceLineItemId() {
        return invoiceLineItemId;
    }

    public InvoiceDoctorPayout invoiceLineItemId(Long invoiceLineItemId) {
        this.invoiceLineItemId = invoiceLineItemId;
        return this;
    }

    public void setInvoiceLineItemId(Long invoiceLineItemId) {
        this.invoiceLineItemId = invoiceLineItemId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public InvoiceDoctorPayout employeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getServiceBenefietId() {
        return serviceBenefietId;
    }

    public InvoiceDoctorPayout serviceBenefietId(Long serviceBenefietId) {
        this.serviceBenefietId = serviceBenefietId;
        return this;
    }

    public void setServiceBenefietId(Long serviceBenefietId) {
        this.serviceBenefietId = serviceBenefietId;
    }

    public Double getServiceBenefietAmount() {
        return serviceBenefietAmount;
    }

    public InvoiceDoctorPayout serviceBenefietAmount(Double serviceBenefietAmount) {
        this.serviceBenefietAmount = serviceBenefietAmount;
        return this;
    }

    public void setServiceBenefietAmount(Double serviceBenefietAmount) {
        this.serviceBenefietAmount = serviceBenefietAmount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public InvoiceDoctorPayout invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getMrn() {
        return mrn;
    }

    public InvoiceDoctorPayout mrn(String mrn) {
        this.mrn = mrn;
        return this;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getVisitType() {
        return visitType;
    }

    public InvoiceDoctorPayout visitType(String visitType) {
        this.visitType = visitType;
        return this;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Instant getOrderDateTime() {
        return orderDateTime;
    }

    public InvoiceDoctorPayout orderDateTime(Instant orderDateTime) {
        this.orderDateTime = orderDateTime;
        return this;
    }

    public void setOrderDateTime(Instant orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public Instant getIssueDateTime() {
        return issueDateTime;
    }

    public InvoiceDoctorPayout issueDateTime(Instant issueDateTime) {
        this.issueDateTime = issueDateTime;
        return this;
    }

    public void setIssueDateTime(Instant issueDateTime) {
        this.issueDateTime = issueDateTime;
    }

    public Instant getInvoiceApprovedDateTime() {
        return invoiceApprovedDateTime;
    }

    public InvoiceDoctorPayout invoiceApprovedDateTime(Instant invoiceApprovedDateTime) {
        this.invoiceApprovedDateTime = invoiceApprovedDateTime;
        return this;
    }

    public void setInvoiceApprovedDateTime(Instant invoiceApprovedDateTime) {
        this.invoiceApprovedDateTime = invoiceApprovedDateTime;
    }

    public DoctorPayout getDoctorPayout() {
        return doctorPayout;
    }

    public InvoiceDoctorPayout doctorPayout(DoctorPayout doctorPayout) {
        this.doctorPayout = doctorPayout;
        return this;
    }

    public void setDoctorPayout(DoctorPayout doctorPayout) {
        this.doctorPayout = doctorPayout;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Long getSarId() {
        return sarId;
    }

    public void setSarId(Long sarId) {
        this.sarId = sarId;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDoctorPayout)) {
            return false;
        }
        return id != null && id.equals(((InvoiceDoctorPayout) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "InvoiceDoctorPayout{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", variablePayoutId=" + getVariablePayoutId() +
            ", invoiceId=" + getInvoiceId() +
            ", invoiceLineItemId=" + getInvoiceLineItemId() +
            ", employeeId=" + getEmployeeId() +
            ", serviceBenefietId=" + getServiceBenefietId() +
            ", serviceBenefietAmount=" + getServiceBenefietAmount() +
            ", invoiceNumber=" + getInvoiceNumber() +
            ", mrn='" + getMrn() + "'" +
            ", visitType='" + getVisitType() + "'" +
            ", orderDateTime='" + getOrderDateTime() + "'" +
            ", issueDateTime='" + getIssueDateTime() + "'" +
            ", invoiceApprovedDateTime='" + getInvoiceApprovedDateTime() + "'" +
            "}";
    }
}
