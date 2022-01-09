package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.nh.artha.domain.enumeration.LengthOfStayCriteria;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A LengthOfStayBenefit.
 */
@Entity
@Table(name = "length_of_stay_benefit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_lengthofstaybenefit")
@Setting(settingPath = "/es/settings.json")
public class LengthOfStayBenefit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "length_of_stay_criteria")
    private LengthOfStayCriteria lengthOfStayCriteria;

    @NotNull
    @Column(name = "days", nullable = false)
    private Integer days;

    @Column(name = "per_day_payout_amount", precision = 21, scale = 2)
    private BigDecimal perDayPayoutAmount;

    @NotNull
    @Column(name = "starting_version", nullable = false)
    private Integer startingVersion;

    @NotNull
    @Column(name = "current_version", nullable = false)
    private Integer currentVersion;

    @OneToOne
    @JoinColumn(unique = true)
    private Department department;

    @OneToMany(mappedBy = "lengthOfStayBenefit",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StayBenefitService> stayBenefitServices = new HashSet<>();

    @ManyToOne
    @ReadOnlyProperty
    @JsonIgnoreProperties("lengthOfStayBenefits")
    @JsonIgnore
    private VariablePayout variablePayout;

    @Column(name = "executed")
    private Boolean executed=Boolean.FALSE;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LengthOfStayCriteria getLengthOfStayCriteria() {
        return lengthOfStayCriteria;
    }

    public LengthOfStayBenefit lengthOfStayCriteria(LengthOfStayCriteria lengthOfStayCriteria) {
        this.lengthOfStayCriteria = lengthOfStayCriteria;
        return this;
    }

    public void setLengthOfStayCriteria(LengthOfStayCriteria lengthOfStayCriteria) {
        this.lengthOfStayCriteria = lengthOfStayCriteria;
    }

    public Integer getDays() {
        return days;
    }

    public LengthOfStayBenefit days(Integer days) {
        this.days = days;
        return this;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public BigDecimal getPerDayPayoutAmount() {
        return perDayPayoutAmount;
    }

    public LengthOfStayBenefit perDayPayoutAmount(BigDecimal perDayPayoutAmount) {
        this.perDayPayoutAmount = perDayPayoutAmount;
        return this;
    }

    public void setPerDayPayoutAmount(BigDecimal perDayPayoutAmount) {
        this.perDayPayoutAmount = perDayPayoutAmount;
    }

    public Integer getStartingVersion() {
        return startingVersion;
    }

    public LengthOfStayBenefit startingVersion(Integer startingVersion) {
        this.startingVersion = startingVersion;
        return this;
    }

    public void setStartingVersion(Integer startingVersion) {
        this.startingVersion = startingVersion;
    }

    public Integer getCurrentVersion() {
        return currentVersion;
    }

    public LengthOfStayBenefit currentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
        return this;
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Department getDepartment() {
        return department;
    }

    public LengthOfStayBenefit department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<StayBenefitService> getStayBenefitServices() {
        return stayBenefitServices;
    }

    public LengthOfStayBenefit stayBenefitServices(Set<StayBenefitService> stayBenefitServices) {
        this.stayBenefitServices = stayBenefitServices;
        return this;
    }

    public LengthOfStayBenefit addStayBenefitServices(StayBenefitService stayBenefitService) {
        this.stayBenefitServices.add(stayBenefitService);
        stayBenefitService.setLengthOfStayBenefit(this);
        return this;
    }

    public LengthOfStayBenefit removeStayBenefitServices(StayBenefitService stayBenefitService) {
        this.stayBenefitServices.remove(stayBenefitService);
        stayBenefitService.setLengthOfStayBenefit(null);
        return this;
    }

    public void setStayBenefitServices(Set<StayBenefitService> stayBenefitServices) {
        this.stayBenefitServices = stayBenefitServices;
    }

    public VariablePayout getVariablePayout() {
        return variablePayout;
    }

    public LengthOfStayBenefit variablePayout(VariablePayout variablePayout) {
        this.variablePayout = variablePayout;
        return this;
    }

    public void setVariablePayout(VariablePayout variablePayout) {
        this.variablePayout = variablePayout;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LengthOfStayBenefit)) {
            return false;
        }
        return id != null && id.equals(((LengthOfStayBenefit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LengthOfStayBenefit{" +
            "id=" + getId() +
            ", lengthOfStayCriteria='" + getLengthOfStayCriteria() + "'" +
            ", days=" + getDays() +
            ", perDayPayoutAmount=" + getPerDayPayoutAmount() +
            ", startingVersion=" + getStartingVersion() +
            ", currentVersion=" + getCurrentVersion() +
            "}";
    }
}
