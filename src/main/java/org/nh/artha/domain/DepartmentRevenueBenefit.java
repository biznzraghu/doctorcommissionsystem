package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nh.artha.domain.enumeration.RevenueBenefitType;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DepartmentRevenueBenefit.
 */
@Entity
@Table(name = "department_revenue_benefit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_departmentrevenuebenefit")
@Setting(settingPath = "/es/settings.json")
public class DepartmentRevenueBenefit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "revenue_benefit_type", nullable = false)
    private RevenueBenefitType revenueBenefitType;

    @Column(name = "payout_percentage")
    private Float payoutPercentage;

    @Column(name = "upper_limit", precision = 21, scale = 2)
    private BigDecimal upperLimit;

    @NotNull
    @Column(name = "starting_version", nullable = false)
    private Integer startingVersion;

    @NotNull
    @Column(name = "current_version", nullable = false)
    private Integer currentVersion;

    @OneToOne
    @JoinColumn
    @Field(type = FieldType.Object)
    private Department department;

    @ManyToOne
    @ReadOnlyProperty
    @JsonIgnoreProperties("departmentRevenueBenefits")
    @JsonIgnore
    private VariablePayout variablePayout;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RevenueBenefitType getRevenueBenefitType() {
        return revenueBenefitType;
    }

    public DepartmentRevenueBenefit revenueBenefitType(RevenueBenefitType revenueBenefitType) {
        this.revenueBenefitType = revenueBenefitType;
        return this;
    }

    public void setRevenueBenefitType(RevenueBenefitType revenueBenefitType) {
        this.revenueBenefitType = revenueBenefitType;
    }

    public Float getPayoutPercentage() {
        return payoutPercentage;
    }

    public DepartmentRevenueBenefit payoutPercentage(Float payoutPercentage) {
        this.payoutPercentage = payoutPercentage;
        return this;
    }

    public void setPayoutPercentage(Float payoutPercentage) {
        this.payoutPercentage = payoutPercentage;
    }

    public BigDecimal getUpperLimit() {
        return upperLimit;
    }

    public DepartmentRevenueBenefit upperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
        return this;
    }

    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Integer getStartingVersion() {
        return startingVersion;
    }

    public DepartmentRevenueBenefit startingVersion(Integer startingVersion) {
        this.startingVersion = startingVersion;
        return this;
    }

    public void setStartingVersion(Integer startingVersion) {
        this.startingVersion = startingVersion;
    }

    public Integer getCurrentVersion() {
        return currentVersion;
    }

    public DepartmentRevenueBenefit currentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
        return this;
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Department getDepartment() {
        return department;
    }

    public DepartmentRevenueBenefit department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public VariablePayout getVariablePayout() {
        return variablePayout;
    }

    public DepartmentRevenueBenefit variablePayout(VariablePayout variablePayout) {
        this.variablePayout = variablePayout;
        return this;
    }

    public void setVariablePayout(VariablePayout variablePayout) {
        this.variablePayout = variablePayout;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentRevenueBenefit)) {
            return false;
        }
        return id != null && id.equals(((DepartmentRevenueBenefit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DepartmentRevenueBenefit{" +
            "id=" + getId() +
            ", revenueBenefitType='" + getRevenueBenefitType() + "'" +
            ", payoutPercentage=" + getPayoutPercentage() +
            ", upperLimit=" + getUpperLimit() +
            ", startingVersion=" + getStartingVersion() +
            ", currentVersion=" + getCurrentVersion() +
            "}";
    }
}
