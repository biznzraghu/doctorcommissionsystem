package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A Plan.
 */
@Entity
@Table(name = "plan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_plan")
@Setting(settingPath = "/es/settings.json")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "template")
    private Boolean template;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "contract_start_date")
    private LocalDateTime contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDateTime contractEndDate;

    @Column(name = "applicable_start_date")
    private LocalDateTime applicableStartDate;

    @Column(name = "applicable_end_date")
    private LocalDateTime applicableEndDate;

    @Column(name = "op_authorization")
    private Boolean opAuthorization;

    @Column(name = "ip_authorization")
    private Boolean ipAuthorization;

    @Column(name = "sponsor_pay_tax")
    private Boolean sponsorPayTax;

    @Type(type = "jsonb")
    @Column(name = "sponsor_details")
    private Organization sponsor;



   /* @ManyToOne(cascade = {CascadeType.ALL})
    @JsonIgnoreProperties("plans")
    private Organization sponsor;
    */
    /*@ManyToOne(cascade = {CascadeType.ALL})
    @JsonIgnoreProperties("plans")
    private Plan planTemplate;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JsonIgnoreProperties("plans")
    private Plan partOf;*/

   /* @ManyToOne
    @JsonIgnoreProperties("plans")
    private ExceptionSponsor exceptionSponsor;*/

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

    public Plan code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Plan name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public Plan active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isTemplate() {
        return template;
    }

    public Plan template(Boolean template) {
        this.template = template;
        return this;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Plan createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Plan createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public Plan modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public Plan modifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public LocalDateTime getContractStartDate() {
        return contractStartDate;
    }

    public Plan contractStartDate(LocalDateTime contractStartDate) {
        this.contractStartDate = contractStartDate;
        return this;
    }

    public void setContractStartDate(LocalDateTime contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public LocalDateTime getContractEndDate() {
        return contractEndDate;
    }

    public Plan contractEndDate(LocalDateTime contractEndDate) {
        this.contractEndDate = contractEndDate;
        return this;
    }

    public void setContractEndDate(LocalDateTime contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public LocalDateTime getApplicableStartDate() {
        return applicableStartDate;
    }

    public Plan applicableStartDate(LocalDateTime applicableStartDate) {
        this.applicableStartDate = applicableStartDate;
        return this;
    }

    public void setApplicableStartDate(LocalDateTime applicableStartDate) {
        this.applicableStartDate = applicableStartDate;
    }

    public LocalDateTime getApplicableEndDate() {
        return applicableEndDate;
    }

    public Plan applicableEndDate(LocalDateTime applicableEndDate) {
        this.applicableEndDate = applicableEndDate;
        return this;
    }

    public void setApplicableEndDate(LocalDateTime applicableEndDate) {
        this.applicableEndDate = applicableEndDate;
    }

    public Boolean isOpAuthorization() {
        return opAuthorization;
    }

    public Plan opAuthorization(Boolean opAuthorization) {
        this.opAuthorization = opAuthorization;
        return this;
    }

    public void setOpAuthorization(Boolean opAuthorization) {
        this.opAuthorization = opAuthorization;
    }

    public Boolean isIpAuthorization() {
        return ipAuthorization;
    }

    public Plan ipAuthorization(Boolean ipAuthorization) {
        this.ipAuthorization = ipAuthorization;
        return this;
    }

    public void setIpAuthorization(Boolean ipAuthorization) {
        this.ipAuthorization = ipAuthorization;
    }

    public Boolean isSponsorPayTax() {
        return sponsorPayTax;
    }

    public Plan sponsorPayTax(Boolean sponsorPayTax) {
        this.sponsorPayTax = sponsorPayTax;
        return this;
    }

    public void setSponsorPayTax(Boolean sponsorPayTax) {
        this.sponsorPayTax = sponsorPayTax;
    }

    public Organization getSponsor() {
        return sponsor;
    }

    public Plan sponsor(Organization Organization) {
        this.sponsor = Organization;
        return this;
    }

    public void setSponsor(Organization Organization) {
        this.sponsor = Organization;
    }

   /* public Plan getPlanTemplate() {
        return planTemplate;
    }

    public Plan planTemplate(Plan Plan) {
        this.planTemplate = Plan;
        return this;
    }

    public void setPlanTemplate(Plan Plan) {
        this.planTemplate = Plan;
    }

    public Plan getPartOf() {
        return partOf;
    }

    public Plan partOf(Plan Plan) {
        this.partOf = Plan;
        return this;
    }

    public void setPartOf(Plan Plan) {
        this.partOf = Plan;
    }*/

    /*public ExceptionSponsor getExceptionSponsor() {
        return exceptionSponsor;
    }

    public Plan exceptionSponsor(ExceptionSponsor exceptionSponsor) {
        this.exceptionSponsor = exceptionSponsor;
        return this;
    }

    public void setExceptionSponsor(ExceptionSponsor exceptionSponsor) {
        this.exceptionSponsor = exceptionSponsor;
    }*/
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plan)) {
            return false;
        }
        return id != null && id.equals(((Plan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Plan{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", active='" + isActive() + "'" +
            ", template='" + isTemplate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", contractStartDate='" + getContractStartDate() + "'" +
            ", contractEndDate='" + getContractEndDate() + "'" +
            ", applicableStartDate='" + getApplicableStartDate() + "'" +
            ", applicableEndDate='" + getApplicableEndDate() + "'" +
            ", opAuthorization='" + isOpAuthorization() + "'" +
            ", ipAuthorization='" + isIpAuthorization() + "'" +
            ", sponsorPayTax='" + isSponsorPayTax() + "'" +
            "}";
    }
}
