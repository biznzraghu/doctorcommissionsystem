package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.nh.artha.domain.dto.CodeNameDto;
import org.springframework.data.annotation.ReadOnlyProperty;
import java.io.Serializable;

import org.nh.artha.domain.enumeration.Level;

import org.nh.artha.domain.enumeration.ExceptionType;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A ServiceItemException.
 */
@Entity
@Table(name = "service_item_exception")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_serviceitemexception")
@Setting(settingPath = "/es/settings.json")
public class ServiceItemException implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(name = "exception_type")
    private ExceptionType exceptionType;

    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb",name = "value")
    private CodeNameDto value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("serviceItemExceptions")
    @JsonIgnore
    @ReadOnlyProperty
    private ServiceItemBenefit serviceItemBenefit;

    @ManyToOne
    @JsonIgnoreProperties("serviceItemExceptions")
    @JsonIgnore
    @ReadOnlyProperty
    private VariablePayout variablePayout;

    @ManyToOne
    @JsonIgnoreProperties("serviceItemExceptions")
    @JsonIgnore
    @ReadOnlyProperty
    private DepartmentPayout departmentPayout;

    @ManyToOne
    @JsonIgnoreProperties("serviceItemExceptions")
    @JsonIgnore
    @ReadOnlyProperty
    private ServiceItemBenefitTemplate serviceItemBenefitTemplate;

    public DepartmentPayout getDepartmentPayout() {
        return departmentPayout;
    }

    public ServiceItemException departmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
        return this;
    }

    public void setDepartmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Level getLevel() {
        return level;
    }

    public ServiceItemException level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public ServiceItemException exceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
        return this;
    }

    public void setExceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public CodeNameDto getValue() {
        return value;
    }

    public ServiceItemException value(CodeNameDto value) {
        this.value = value;
        return this;
    }

    public void setValue(CodeNameDto value) {
        this.value = value;
    }

    public ServiceItemBenefit getServiceItemBenefit() {
        return serviceItemBenefit;
    }

    public ServiceItemException serviceItemBenefit(ServiceItemBenefit serviceItemBenefit) {
        this.serviceItemBenefit = serviceItemBenefit;
        return this;
    }


    public VariablePayout getVariablePayout() {
        return variablePayout;
    }

    public ServiceItemException variablePayout(VariablePayout variablePayout) {
        this.variablePayout = variablePayout;
        return this;
    }

    public void setVariablePayout(VariablePayout variablePayout) {
        this.variablePayout = variablePayout;
    }

    public ServiceItemBenefitTemplate getServiceItemBenefitTemplate() {
        return serviceItemBenefitTemplate;
    }

    public ServiceItemException serviceItemBenefitTemplate(ServiceItemBenefitTemplate serviceItemBenefitTemplate) {
        this.serviceItemBenefitTemplate = serviceItemBenefitTemplate;
        return this;
    }

    public void setServiceItemBenefitTemplate(ServiceItemBenefitTemplate serviceItemBenefitTemplate) {
        this.serviceItemBenefitTemplate = serviceItemBenefitTemplate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceItemException)) {
            return false;
        }
        return id != null && id.equals(((ServiceItemException) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServiceItemException{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", exceptionType='" + getExceptionType() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
