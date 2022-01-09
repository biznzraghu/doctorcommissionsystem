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
 * A DoctorPayoutLOS.
 */
@Entity
@Table(name = "doctor_payout_los")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_doctorpayoutlos")
@Setting(settingPath = "/es/settings.json")
public class DoctorPayoutLOS implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "doctor_payout_los_id")
    private Long doctorPayoutLosId;

    @Column(name = "amount")
    private Double amount;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("losBenefietIds")
    @JsonIgnore
    @ReadOnlyProperty
    private DoctorPayout doctorPayout;


    @Column(name = "patient_mrn")
    private String patientMrn;

    @Column(name = "admission_date")
    private LocalDateTime admissionDate;

    @Column(name = "discharge_date")
    private LocalDateTime dischargeDateTime;

    @Column(name = "payable_days")
    private Long payableDays;

    @Column(name = "sar_id")
    private Long sarId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorPayoutLosId() {
        return doctorPayoutLosId;
    }

    public DoctorPayoutLOS doctorPayoutLosId(Long doctorPayoutLosId) {
        this.doctorPayoutLosId = doctorPayoutLosId;
        return this;
    }

    public void setDoctorPayoutLosId(Long doctorPayoutLosId) {
        this.doctorPayoutLosId = doctorPayoutLosId;
    }

    public Double getAmount() {
        return amount;
    }

    public DoctorPayoutLOS amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Department getDepartment() {
        return department;
    }

    public DoctorPayoutLOS department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public DoctorPayout getDoctorPayout() {
        return doctorPayout;
    }

    public DoctorPayoutLOS doctorPayout(DoctorPayout doctorPayout) {
        this.doctorPayout = doctorPayout;
        return this;
    }

    public void setDoctorPayout(DoctorPayout doctorPayout) {
        this.doctorPayout = doctorPayout;
    }

    public String getPatientMrn() {
        return patientMrn;
    }

    public void setPatientMrn(String patientMrn) {
        this.patientMrn = patientMrn;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDateTime getDischargeDateTime() {
        return dischargeDateTime;
    }

    public void setDischargeDateTime(LocalDateTime dischargeDateTime) {
        this.dischargeDateTime = dischargeDateTime;
    }

    public Long getPayableDays() {
        return payableDays;
    }

    public void setPayableDays(Long payableDays) {
        this.payableDays = payableDays;
    }

    public Long getSarId() {
        return sarId;
    }

    public void setSarId(Long sarId) {
        this.sarId = sarId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctorPayoutLOS)) {
            return false;
        }
        return id != null && id.equals(((DoctorPayoutLOS) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DoctorPayoutLOS{" +
            "id=" + getId() +
            ", doctorPayoutLosId=" + getDoctorPayoutLosId() +
            ", amount=" + getAmount() +
            "}";
    }
}
