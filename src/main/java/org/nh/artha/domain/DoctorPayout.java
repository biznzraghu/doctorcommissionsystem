package org.nh.artha.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.nh.artha.domain.enumeration.ConsultantType;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A DoctorPayout.
 */
@Entity
@Table(name = "doctor_payout")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_doctorpayout")
@Setting(settingPath = "/es/settings.json")
public class DoctorPayout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "date")
    private Integer date;

    @Column(name = "variable_payout_id")
    private Long variablePayoutId;

    @Column(name = "doctor_payout_invoice")
    private Long doctorPayoutInvoice;

    @Enumerated(EnumType.STRING)
    @Column(name = "consultatnt_type")
    private ConsultantType consultatntType;

    @Column(name = "department_revenue_benefit_id")
    private Long departmentRevenueBenefitId;

    @Column(name = "department_revenue_benefit_amount")
    private Double departmentRevenueBenefitAmount;

    @Column(name = "los_benefiet_id")
    private Long losBenefietId;

    @Column(name = "service_item_benefit_amount")
    private Long serviceItemBenefitAmount;

    @OneToMany(mappedBy = "doctorPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DoctorPayoutLOS> losBenefietIds = new HashSet<>();

    @OneToMany(mappedBy = "doctorPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvoiceDoctorPayout> doctorPayoutInvoices = new HashSet<>();

    @OneToMany(mappedBy = "doctorPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DoctorPayoutDepartment> departmentRevenueBenefitIds = new HashSet<>();

    @OneToMany(mappedBy = "doctorPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DoctorPayoutAdjustment> doctorPayoutAdjustments = new HashSet<>();

    @Column(name = "exported_report_path")
    private String exportedReportPath;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public DoctorPayout unitCode(String unitCode) {
        this.unitCode = unitCode;
        return this;
    }

    public void setunitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public DoctorPayout employeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getYear() {
        return year;
    }

    public DoctorPayout year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public DoctorPayout month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDate() {
        return date;
    }

    public DoctorPayout date(Integer date) {
        this.date = date;
        return this;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Long getVariablePayoutId() {
        return variablePayoutId;
    }

    public DoctorPayout variablePayoutId(Long variablePayoutId) {
        this.variablePayoutId = variablePayoutId;
        return this;
    }

    public void setVariablePayoutId(Long variablePayoutId) {
        this.variablePayoutId = variablePayoutId;
    }

    public Long getDoctorPayoutInvoice() {
        return doctorPayoutInvoice;
    }

    public DoctorPayout doctorPayoutInvoice(Long doctorPayoutInvoice) {
        this.doctorPayoutInvoice = doctorPayoutInvoice;
        return this;
    }

    public void setDoctorPayoutInvoice(Long doctorPayoutInvoice) {
        this.doctorPayoutInvoice = doctorPayoutInvoice;
    }

    public ConsultantType getConsultatntType() {
        return consultatntType;
    }

    public DoctorPayout consultatntType(ConsultantType consultatntType) {
        this.consultatntType = consultatntType;
        return this;
    }

    public void setConsultatntType(ConsultantType consultatntType) {
        this.consultatntType = consultatntType;
    }

    public Long getDepartmentRevenueBenefitId() {
        return departmentRevenueBenefitId;
    }

    public DoctorPayout departmentRevenueBenefitId(Long departmentRevenueBenefitId) {
        this.departmentRevenueBenefitId = departmentRevenueBenefitId;
        return this;
    }

    public void setDepartmentRevenueBenefitId(Long departmentRevenueBenefitId) {
        this.departmentRevenueBenefitId = departmentRevenueBenefitId;
    }

    public Double getDepartmentRevenueBenefitAmount() {
        return departmentRevenueBenefitAmount;
    }

    public DoctorPayout departmentRevenueBenefitAmount(Double departmentRevenueBenefitAmount) {
        this.departmentRevenueBenefitAmount = departmentRevenueBenefitAmount;
        return this;
    }

    public void setDepartmentRevenueBenefitAmount(Double departmentRevenueBenefitAmount) {
        this.departmentRevenueBenefitAmount = departmentRevenueBenefitAmount;
    }

    public Long getLosBenefietId() {
        return losBenefietId;
    }

    public DoctorPayout losBenefietId(Long losBenefietId) {
        this.losBenefietId = losBenefietId;
        return this;
    }

    public void setLosBenefietId(Long losBenefietId) {
        this.losBenefietId = losBenefietId;
    }

    public Long getServiceItemBenefitAmount() {
        return serviceItemBenefitAmount;
    }

    public DoctorPayout serviceItemBenefitAmount(Long serviceItemBenefitAmount) {
        this.serviceItemBenefitAmount = serviceItemBenefitAmount;
        return this;
    }

    public void setServiceItemBenefitAmount(Long serviceItemBenefitAmount) {
        this.serviceItemBenefitAmount = serviceItemBenefitAmount;
    }

    public Set<DoctorPayoutLOS> getLosBenefietIds() {
        return losBenefietIds;
    }

    public DoctorPayout losBenefietIds(Set<DoctorPayoutLOS> doctorPayoutLOS) {
        this.losBenefietIds = doctorPayoutLOS;
        return this;
    }

    public DoctorPayout addLosBenefietId(DoctorPayoutLOS doctorPayoutLOS) {
        this.losBenefietIds.add(doctorPayoutLOS);
        doctorPayoutLOS.setDoctorPayout(this);
        return this;
    }

    public DoctorPayout removeLosBenefietId(DoctorPayoutLOS doctorPayoutLOS) {
        this.losBenefietIds.remove(doctorPayoutLOS);
        doctorPayoutLOS.setDoctorPayout(null);
        return this;
    }

    public void setLosBenefietIds(Set<DoctorPayoutLOS> doctorPayoutLOS) {
        this.losBenefietIds = doctorPayoutLOS;
    }

    public Set<InvoiceDoctorPayout> getDoctorPayoutInvoices() {
        return doctorPayoutInvoices;
    }

    public DoctorPayout doctorPayoutInvoices(Set<InvoiceDoctorPayout> invoiceDoctorPayouts) {
        this.doctorPayoutInvoices = invoiceDoctorPayouts;
        return this;
    }

    public DoctorPayout addDoctorPayoutInvoice(InvoiceDoctorPayout invoiceDoctorPayout) {
        this.doctorPayoutInvoices.add(invoiceDoctorPayout);
        invoiceDoctorPayout.setDoctorPayout(this);
        return this;
    }

    public DoctorPayout removeDoctorPayoutInvoice(InvoiceDoctorPayout invoiceDoctorPayout) {
        this.doctorPayoutInvoices.remove(invoiceDoctorPayout);
        invoiceDoctorPayout.setDoctorPayout(null);
        return this;
    }

    public void setDoctorPayoutInvoices(Set<InvoiceDoctorPayout> invoiceDoctorPayouts) {
        this.doctorPayoutInvoices = invoiceDoctorPayouts;
    }

    public Set<DoctorPayoutDepartment> getDepartmentRevenueBenefitIds() {
        return departmentRevenueBenefitIds;
    }

    public DoctorPayout departmentRevenueBenefitIds(Set<DoctorPayoutDepartment> doctorPayoutDepartments) {
        this.departmentRevenueBenefitIds = doctorPayoutDepartments;
        return this;
    }

    public DoctorPayout addDepartmentRevenueBenefitId(DoctorPayoutDepartment doctorPayoutDepartment) {
        this.departmentRevenueBenefitIds.add(doctorPayoutDepartment);
        doctorPayoutDepartment.setDoctorPayout(this);
        return this;
    }

    public DoctorPayout removeDepartmentRevenueBenefitId(DoctorPayoutDepartment doctorPayoutDepartment) {
        this.departmentRevenueBenefitIds.remove(doctorPayoutDepartment);
        doctorPayoutDepartment.setDoctorPayout(null);
        return this;
    }

    public void setDepartmentRevenueBenefitIds(Set<DoctorPayoutDepartment> doctorPayoutDepartments) {
        this.departmentRevenueBenefitIds = doctorPayoutDepartments;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getExportedReportPath() {
        return exportedReportPath;
    }

    public void setExportedReportPath(String exportedReportPath) {
        this.exportedReportPath = exportedReportPath;
    }

    public Set<DoctorPayoutAdjustment> getDoctorPayoutAdjustments() {
        return doctorPayoutAdjustments;
    }

    public void setDoctorPayoutAdjustments(Set<DoctorPayoutAdjustment> doctorPayoutAdjustments) {
        this.doctorPayoutAdjustments = doctorPayoutAdjustments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctorPayout)) {
            return false;
        }
        return id != null && id.equals(((DoctorPayout) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DoctorPayout{" +
            "id=" + getId() +
            ", unitCode='" + getUnitCode() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            ", date=" + getDate() +
            ", variablePayoutId=" + getVariablePayoutId() +
            ", doctorPayoutInvoice=" + getDoctorPayoutInvoice() +
            ", consultatntType='" + getConsultatntType() + "'" +
            ", departmentRevenueBenefitId=" + getDepartmentRevenueBenefitId() +
            ", departmentRevenueBenefitAmount=" + getDepartmentRevenueBenefitAmount() +
            ", losBenefietId=" + getLosBenefietId() +
            ", serviceItemBenefitAmount=" + getServiceItemBenefitAmount() +
            "}";
    }
}
