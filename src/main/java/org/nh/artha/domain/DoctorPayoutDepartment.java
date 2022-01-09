package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DoctorPayoutDepartment.
 */
@Entity
@Table(name = "doctor_payout_department")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_doctorpayoutdepartment")
@Setting(settingPath = "/es/settings.json")
public class DoctorPayoutDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "doctor_payout_department_id")
    private Long doctorPayoutDepartmentId;

    @Column(name = "amount")
    private Double amount;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @ReadOnlyProperty
    @JsonIgnore
    private DoctorPayout doctorPayout;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "department_payout_id")
    private Long departmentPayoutId;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate=LocalDateTime.now();

    @Column(name = "department_master_id")
    private Long departmentMasterId;

    public Long getDepartmentMasterId() {
        return departmentMasterId;
    }

    public void setDepartmentMasterId(Long departmentMasterId) {
        this.departmentMasterId = departmentMasterId;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public Long getDepartmentPayoutId() {
        return departmentPayoutId;
    }

    public void setDepartmentPayoutId(Long departmentPayoutId) {
        this.departmentPayoutId = departmentPayoutId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorPayoutDepartmentId() {
        return doctorPayoutDepartmentId;
    }

    public DoctorPayoutDepartment doctorPayoutDepartmentId(Long doctorPayoutDepartmentId) {
        this.doctorPayoutDepartmentId = doctorPayoutDepartmentId;
        return this;
    }

    public void setDoctorPayoutDepartmentId(Long doctorPayoutDepartmentId) {
        this.doctorPayoutDepartmentId = doctorPayoutDepartmentId;
    }

    public Double getAmount() {
        return amount;
    }

    public DoctorPayoutDepartment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Department getDepartment() {
        return department;
    }

    public DoctorPayoutDepartment department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public DoctorPayout getDoctorPayout() {
        return doctorPayout;
    }

    public DoctorPayoutDepartment doctorPayout(DoctorPayout doctorPayout) {
        this.doctorPayout = doctorPayout;
        return this;
    }

    public void setDoctorPayout(DoctorPayout doctorPayout) {
        this.doctorPayout = doctorPayout;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctorPayoutDepartment)) {
            return false;
        }
        return id != null && id.equals(((DoctorPayoutDepartment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DoctorPayoutDepartment{" +
            "id=" + getId() +
            ", doctorPayoutDepartmentId=" + getDoctorPayoutDepartmentId() +
            ", amount=" + getAmount() +
            "}";
    }
}
