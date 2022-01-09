package org.nh.artha.domain.dto;

import org.nh.artha.domain.enumeration.PackageCategory;

import java.time.LocalDate;
import java.util.Objects;

public class PackageMasterDto {
    private Long id;

    private String code;

    private String name;

    private PackageCategory packageCategory;

    private Integer numberOfAllowedVisit;

    private Boolean template;

    private Boolean planValidation;

    private String comments;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PackageCategory getPackageCategory() {
        return packageCategory;
    }

    public void setPackageCategory(PackageCategory packageCategory) {
        this.packageCategory = packageCategory;
    }

    public Integer getNumberOfAllowedVisit() {
        return numberOfAllowedVisit;
    }

    public void setNumberOfAllowedVisit(Integer numberOfAllowedVisit) {
        this.numberOfAllowedVisit = numberOfAllowedVisit;
    }

    public Boolean getTemplate() {
        return template;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

    public Boolean getPlanValidation() {
        return planValidation;
    }

    public void setPlanValidation(Boolean planValidation) {
        this.planValidation = planValidation;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageMasterDto that = (PackageMasterDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            packageCategory == that.packageCategory &&
            Objects.equals(numberOfAllowedVisit, that.numberOfAllowedVisit) &&
            Objects.equals(template, that.template) &&
            Objects.equals(planValidation, that.planValidation) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, packageCategory, numberOfAllowedVisit, template, planValidation, comments, startDate, endDate, active);
    }
}
