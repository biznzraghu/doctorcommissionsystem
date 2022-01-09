package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A DoctorPayoutAdjustment.
 */
@Entity
@Table(name = "department_payout_adjustment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "doctorpayoutadjustment",createIndex = false)
public class DepartmentPayoutAdjustment implements Serializable {

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
    private DepartmentRevenueCalculation departmentRevenueCalculation;

    @Column(name = "executed")
    private Boolean executed ;

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

    public DepartmentPayoutAdjustment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public DepartmentPayoutAdjustment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getAdjustmentConfigurationId() {
        return adjustmentConfigurationId;
    }

    public DepartmentPayoutAdjustment adjustmentConfigurationId(Long adjustmentConfigurationId) {
        this.adjustmentConfigurationId = adjustmentConfigurationId;
        return this;
    }

    public void setAdjustmentConfigurationId(Long adjustmentConfigurationId) {
        this.adjustmentConfigurationId = adjustmentConfigurationId;
    }

    public DepartmentRevenueCalculation getDepartmentRevenueCalculation() {
        return departmentRevenueCalculation;
    }
    public DepartmentPayoutAdjustment departmentRevenueCalculation(DepartmentRevenueCalculation departmentRevenueCalculation) {
        this.departmentRevenueCalculation = departmentRevenueCalculation;
        return this;
    }
    public void setDepartmentRevenueCalculation(DepartmentRevenueCalculation departmentRevenueCalculation) {
        this.departmentRevenueCalculation = departmentRevenueCalculation;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentPayoutAdjustment)) {
            return false;
        }
        return id != null && id.equals(((DepartmentPayoutAdjustment) o).id);
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
