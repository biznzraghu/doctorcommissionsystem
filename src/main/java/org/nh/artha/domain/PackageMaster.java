package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.nh.artha.domain.enumeration.VisitType;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

import org.nh.artha.domain.enumeration.PackageCategory;

import org.nh.artha.domain.enumeration.durationUnit;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A PackageMaster.
 */
@Entity
@Table(name = "package_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_packagemaster")
@Setting(settingPath = "/es/settings.json")
public class PackageMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;


    @Enumerated(EnumType.STRING)
    @Column(name = "package_category", nullable = false)
    private PackageCategory packageCategory;

    /*@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type", nullable = false)
    private VisitType visitType;*/

    @Column(name = "duration")
    private Integer duration;

    /*@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "duration_unit", nullable = false)
    private durationUnit durationUnit;*/

    @Column(name = "number_of_allowed_visit")
    private Integer numberOfAllowedVisit;

    @Column(name = "template")
    private Boolean template;

    @Column(name = "plan_validation")
    private Boolean planValidation;

    @Column(name = "comments")
    private String comments;


    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "abbreviation")
    private String abbreviation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageMasters")
    private Group serviceGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageMasters")
    private Group billingGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageMasters")
    private ServiceMaster mainProcedure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageMasters")
    private PackageMaster partOf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageMasters")
    private Group financialGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public PackageMaster code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public PackageMaster name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PackageCategory getPackageCategory() {
        return packageCategory;
    }

    public PackageMaster packageCategory(PackageCategory packageCategory) {
        this.packageCategory = packageCategory;
        return this;
    }

    public void setPackageCategory(PackageCategory packageCategory) {
        this.packageCategory = packageCategory;
    }

   /* public VisitType getVisitType() {
        return visitType;
    }

    public PackageMaster visitType(VisitType visitType) {
        this.visitType = visitType;
        return this;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }*/

    public Integer getDuration() {
        return duration;
    }

    public PackageMaster duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /*public durationUnit getDurationUnit() {
        return durationUnit;
    }

    public PackageMaster durationUnit(durationUnit durationUnit) {
        this.durationUnit = durationUnit;
        return this;
    }

    public void setDurationUnit(durationUnit durationUnit) {
        this.durationUnit = durationUnit;
    }*/

    public Integer getNumberOfAllowedVisit() {
        return numberOfAllowedVisit;
    }

    public PackageMaster numberOfAllowedVisit(Integer numberOfAllowedVisit) {
        this.numberOfAllowedVisit = numberOfAllowedVisit;
        return this;
    }

    public void setNumberOfAllowedVisit(Integer numberOfAllowedVisit) {
        this.numberOfAllowedVisit = numberOfAllowedVisit;
    }

    public Boolean isTemplate() {
        return template;
    }

    public PackageMaster template(Boolean template) {
        this.template = template;
        return this;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

    public Boolean isPlanValidation() {
        return planValidation;
    }

    public PackageMaster planValidation(Boolean planValidation) {
        this.planValidation = planValidation;
        return this;
    }

    public void setPlanValidation(Boolean planValidation) {
        this.planValidation = planValidation;
    }

    public String getComments() {
        return comments;
    }

    public PackageMaster comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public PackageMaster startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PackageMaster endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean isActive() {
        return active;
    }

    public PackageMaster active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public PackageMaster abbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Group getServiceGroup() {
        return serviceGroup;
    }

    public PackageMaster serviceGroup(Group group) {
        this.serviceGroup = group;
        return this;
    }

    public void setServiceGroup(Group group) {
        this.serviceGroup = group;
    }

    public Group getBillingGroup() {
        return billingGroup;
    }

    public PackageMaster billingGroup(Group group) {
        this.billingGroup = group;
        return this;
    }

    public void setBillingGroup(Group group) {
        this.billingGroup = group;
    }

    public ServiceMaster getMainProcedure() {
        return mainProcedure;
    }

    public PackageMaster mainProcedure(ServiceMaster ServiceMaster) {
        this.mainProcedure = ServiceMaster;
        return this;
    }

    public void setMainProcedure(ServiceMaster ServiceMaster) {
        this.mainProcedure = ServiceMaster;
    }

    public PackageMaster getPartOf() {
        return partOf;
    }

    public PackageMaster partOf(PackageMaster PackageMaster) {
        this.partOf = PackageMaster;
        return this;
    }

    public void setPartOf(PackageMaster PackageMaster) {
        this.partOf = PackageMaster;
    }

    public Group getFinancialGroup() {
        return financialGroup;
    }

    public PackageMaster financialGroup(Group group) {
        this.financialGroup = group;
        return this;
    }

    public void setFinancialGroup(Group group) {
        this.financialGroup = group;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackageMaster)) {
            return false;
        }
        return id != null && id.equals(((PackageMaster) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PackageMaster{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", packageCategory='" + getPackageCategory() + "'" +
            ", duration=" + getDuration() +
            ", numberOfAllowedVisit=" + getNumberOfAllowedVisit() +
            ", template='" + isTemplate() + "'" +
            ", planValidation='" + isPlanValidation() + "'" +
            ", comments='" + getComments() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", active='" + isActive() + "'" +
            ", abbreviation='" + getAbbreviation() + "'" +
            "}";
    }
}
