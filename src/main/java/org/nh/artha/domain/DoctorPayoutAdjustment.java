package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DoctorPayoutAdjustment.
 */
@Entity
@Table(name = "doctor_payout_adjustment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "doctorpayoutadjustment",createIndex = false)
public class DoctorPayoutAdjustment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "adjustment_configuration_id")
    private Long adjustmentConfigurationId;

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

    public String getDescription() {
        return description;
    }

    public DoctorPayoutAdjustment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public DoctorPayoutAdjustment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getAdjustmentConfigurationId() {
        return adjustmentConfigurationId;
    }

    public DoctorPayoutAdjustment adjustmentConfigurationId(Long adjustmentConfigurationId) {
        this.adjustmentConfigurationId = adjustmentConfigurationId;
        return this;
    }

    public void setAdjustmentConfigurationId(Long adjustmentConfigurationId) {
        this.adjustmentConfigurationId = adjustmentConfigurationId;
    }

    public DoctorPayout getDoctorPayout() {
        return doctorPayout;
    }

    public DoctorPayoutAdjustment doctorPayout(DoctorPayout doctorPayout) {
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
        if (!(o instanceof DoctorPayoutAdjustment)) {
            return false;
        }
        return id != null && id.equals(((DoctorPayoutAdjustment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DoctorPayoutAdjustment{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", adjustmentConfigurationId=" + getAdjustmentConfigurationId() +
            "}";
    }
}
