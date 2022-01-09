package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.nh.artha.service.dto.UserDTO;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A ServiceItemBenefitTemplate.
 */
@Entity
@Table(name = "service_item_benefit_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_serviceitembenefittemplate")
@Setting(settingPath = "/es/settings.json")
public class ServiceItemBenefitTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "template_name")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String templateName;

    @OneToMany(mappedBy = "planTemplate",fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    @ReadOnlyProperty
    private Set<ServiceItemBenefit> serviceItemBenefits = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_item_benefit_template_variable_payout",
               joinColumns = @JoinColumn(name = "service_item_benefit_template_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "variable_payout_id", referencedColumnName = "id"))
    @JsonIgnore
    @ReadOnlyProperty
    private Set<VariablePayout> variablePayouts = new HashSet<>();

    @Type(type = "jsonb")
    @Column(name = "created_by",columnDefinition = "jsonb")
    @Field(type = FieldType.Object)
    private UserDTO createdBy;

    @Type(type = "jsonb")
    @Column(name = "last_modified_by",columnDefinition = "jsonb")
    @Field(type = FieldType.Object)
    private UserDTO lastModifiedBy;

    @Column(name = "created_date")
    @MultiField(
        mainField = @Field(type = FieldType.Date),
        otherFields = {
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private LocalDateTime createdDate=LocalDateTime.now();

    @Type(type = "jsonb")
    @Column(name = "organization",columnDefinition = "jsonb")
    private List<Organization> organization;

    @Column(name = "code")
    @NotNull
    private String code;

    @OneToMany(mappedBy = "serviceItemBenefitTemplate",fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceItemException> serviceItemExceptions = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public ServiceItemBenefitTemplate templateName(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Set<ServiceItemBenefit> getServiceItemBenefits() {
        return serviceItemBenefits;
    }

    public ServiceItemBenefitTemplate serviceItemBenefits(Set<ServiceItemBenefit> serviceItemBenefits) {
        this.serviceItemBenefits = serviceItemBenefits;
        return this;
    }

    public ServiceItemBenefitTemplate addServiceItemBenefit(ServiceItemBenefit serviceItemBenefit) {
        this.serviceItemBenefits.add(serviceItemBenefit);
        serviceItemBenefit.setPlanTemplate(this);
        return this;
    }

    public ServiceItemBenefitTemplate removeServiceItemBenefit(ServiceItemBenefit serviceItemBenefit) {
        this.serviceItemBenefits.remove(serviceItemBenefit);
        serviceItemBenefit.setPlanTemplate(null);
        return this;
    }

    public void setServiceItemBenefits(Set<ServiceItemBenefit> serviceItemBenefits) {
        this.serviceItemBenefits = serviceItemBenefits;
    }

    public Set<VariablePayout> getVariablePayouts() {
        return variablePayouts;
    }

    public ServiceItemBenefitTemplate variablePayouts(Set<VariablePayout> variablePayouts) {
        this.variablePayouts = variablePayouts;
        return this;
    }

    public ServiceItemBenefitTemplate addVariablePayout(VariablePayout variablePayout) {
        this.variablePayouts.add(variablePayout);
        variablePayout.getServiceItemBenefitTemplates().add(this);
        return this;
    }

    public ServiceItemBenefitTemplate removeVariablePayout(VariablePayout variablePayout) {
        this.variablePayouts.remove(variablePayout);
        variablePayout.getServiceItemBenefitTemplates().remove(this);
        return this;
    }

    public void setVariablePayouts(Set<VariablePayout> variablePayouts) {
        this.variablePayouts = variablePayouts;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public UserDTO getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(UserDTO lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Organization> getOrganization() {
        return organization;
    }

    public void setOrganization(List<Organization> organization) {
        this.organization = organization;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<ServiceItemException> getServiceItemExceptions() {
        return serviceItemExceptions;
    }

    public ServiceItemBenefitTemplate serviceItemExceptions(Set<ServiceItemException> serviceItemExceptions) {
        this.serviceItemExceptions = serviceItemExceptions;
        return this;
    }

    public ServiceItemBenefitTemplate addServiceItemException(ServiceItemException serviceItemException) {
        this.serviceItemExceptions.add(serviceItemException);
        serviceItemException.setServiceItemBenefitTemplate(this);
        return this;
    }

    public ServiceItemBenefitTemplate removeServiceItemException(ServiceItemException serviceItemException) {
        this.serviceItemExceptions.remove(serviceItemException);
        serviceItemException.setServiceItemBenefitTemplate(null);
        return this;
    }

    public void setServiceItemExceptions(Set<ServiceItemException> serviceItemExceptions) {
        this.serviceItemExceptions = serviceItemExceptions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceItemBenefitTemplate)) {
            return false;
        }
        return id != null && id.equals(((ServiceItemBenefitTemplate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServiceItemBenefitTemplate{" +
            "id=" + id +
            ", templateName='" + templateName + '\'' +
            ", serviceItemBenefits=" + serviceItemBenefits +
            ", variablePayouts=" + variablePayouts +
            ", createdBy=" + createdBy +
            ", lastModifiedBy=" + lastModifiedBy +
            ", createdDate=" + createdDate +
            ", organization=" + organization +
            ", code='" + code + '\'' +
            '}';
    }
}
