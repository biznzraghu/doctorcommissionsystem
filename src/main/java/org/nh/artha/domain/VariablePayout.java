package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.nh.artha.domain.dto.UserDTO;
import org.nh.artha.domain.enumeration.ChangeRequestStatus;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A VariablePayout.
 */
@Entity
@Table(name = "variable_payout")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_variablepayout")
@Setting(settingPath = "/es/settings.json")
public class VariablePayout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "unit_code")
    private String unitCode;

    @NotNull
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "version")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Integer version;


    @Column(name = "variable_payout_id")
    private Long variablePayoutId;

    @Type(type = "jsonb")
    @Column(name = "created_by",columnDefinition = "jsonb")
    @Field(type = FieldType.Object)
    private UserDTO createdBy;

    @Column(name = "created_date")
    @MultiField(
        mainField = @Field(type = FieldType.Date),
        otherFields = {
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private LocalDateTime createdDate= LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "change_request_status")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private ChangeRequestStatus changeRequestStatus;

    @Column(name = "starting_version")
    private Long startingVersion;

    @Column(name = "current_version")
    private Long currentVersion;

    @Column(name = "remarks")
    private String remarks;

    @NotNull
    @Column(name = "commencement_date", nullable = false)
    private LocalDateTime commencementDate;

    @NotNull
    @Column(name = "contract_end_date", nullable = false)
    private LocalDateTime contractEndDate;

    @Column(name = "min_assured_amount", precision = 21, scale = 2)
    private BigDecimal minAssuredAmount;

    @Column(name = "max_payout_amount", precision = 21, scale = 2)
    private BigDecimal maxPayoutAmount;

    @Column(name = "min_assured_validity_date")
    private LocalDateTime minAssuredValidityDate;

    @Column(name = "status")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Boolean status;

    @Column(name = "upload_contract")
    private String uploadContract;

    @OneToMany(mappedBy = "variablePayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Field(type = FieldType.Object)
    private Set<DepartmentRevenueBenefit> departmentRevenueBenefits = new HashSet<>();

    @OneToMany(mappedBy = "variablePayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LengthOfStayBenefit> lengthOfStayBenefits = new HashSet<>();

    @OneToMany(mappedBy = "variablePayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    @ReadOnlyProperty
    private Set<ServiceItemBenefit> serviceItemBenefits = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_item_benefit_template_variable_payout",//variable_payout_id
        joinColumns = @JoinColumn(name="variable_payout_id", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="service_item_benefit_template_id", referencedColumnName="id"))
    private Set<ServiceItemBenefitTemplate> serviceItemBenefitTemplates = new HashSet<>();

    @OneToMany(mappedBy = "variablePayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceItemException> serviceItemExceptions = new HashSet<>();

    @Type(type = "jsonb")
    @Column(name = "approved_by",columnDefinition = "jsonb")
    @Field(type = FieldType.Object)
    private UserDTO approvedBy;

    @OneToMany(mappedBy = "variablePayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionApprovalDetails> transactionApprovalDetails = new HashSet<>();

    @Type(type = "jsonb")
    @Column(name = "employee",columnDefinition = "jsonb")
    @Field(type = FieldType.Object)
    private UserDTO employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public VariablePayout unitCode(String unitCode) {
        this.unitCode = unitCode;
        return this;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public VariablePayout employeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getVersion() {
        return version;
    }

    public VariablePayout version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public VariablePayout createdBy(UserDTO createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public VariablePayout createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ChangeRequestStatus getChangeRequestStatus() {
        return changeRequestStatus;
    }

    public VariablePayout changeRequestStatus(ChangeRequestStatus changeRequestStatus) {
        this.changeRequestStatus = changeRequestStatus;
        return this;
    }

    public void setChangeRequestStatus(ChangeRequestStatus changeRequestStatus) {
        this.changeRequestStatus = changeRequestStatus;
    }

    public Long getStartingVersion() {
        return startingVersion;
    }

    public VariablePayout startingVersion(Long startingVersion) {
        this.startingVersion = startingVersion;
        return this;
    }

    public void setStartingVersion(Long startingVersion) {
        this.startingVersion = startingVersion;
    }

    public Long getCurrentVersion() {
        return currentVersion;
    }

    public VariablePayout currentVersion(Long currentVersion) {
        this.currentVersion = currentVersion;
        return this;
    }

    public void setCurrentVersion(Long currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getRemarks() {
        return remarks;
    }

    public VariablePayout remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCommencementDate() {
        return commencementDate;
    }

    public VariablePayout commencementDate(LocalDateTime commencementDate) {
        this.commencementDate = commencementDate;
        return this;
    }

    public void setCommencementDate(LocalDateTime commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDateTime getContractEndDate() {
        return contractEndDate;
    }

    public VariablePayout contractEndDate(LocalDateTime contractEndDate) {
        this.contractEndDate = contractEndDate;
        return this;
    }

    public void setContractEndDate(LocalDateTime contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public BigDecimal getMinAssuredAmount() {
        return minAssuredAmount;
    }

    public VariablePayout minAssuredAmount(BigDecimal minAssuredAmount) {
        this.minAssuredAmount = minAssuredAmount;
        return this;
    }

    public void setMinAssuredAmount(BigDecimal minAssuredAmount) {
        this.minAssuredAmount = minAssuredAmount;
    }

    public BigDecimal getMaxPayoutAmount() {
        return maxPayoutAmount;
    }

    public VariablePayout maxPayoutAmount(BigDecimal maxPayoutAmount) {
        this.maxPayoutAmount = maxPayoutAmount;
        return this;
    }

    public void setMaxPayoutAmount(BigDecimal maxPayoutAmount) {
        this.maxPayoutAmount = maxPayoutAmount;
    }

    public LocalDateTime getMinAssuredValidityDate() {
        return minAssuredValidityDate;
    }

    public VariablePayout minAssuredValidityDate(LocalDateTime minAssuredValidityDate) {
        this.minAssuredValidityDate = minAssuredValidityDate;
        return this;
    }

    public void setMinAssuredValidityDate(LocalDateTime minAssuredValidityDate) {
        this.minAssuredValidityDate = minAssuredValidityDate;
    }

    public Boolean isStatus() {
        return status;
    }

    public VariablePayout status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUploadContract() {
        return uploadContract;
    }

    public VariablePayout uploadContract(String uploadContract) {
        this.uploadContract = uploadContract;
        return this;
    }

    public void setUploadContract(String uploadContract) {
        this.uploadContract = uploadContract;
    }

    public Set<DepartmentRevenueBenefit> getDepartmentRevenueBenefits() {
        return departmentRevenueBenefits;
    }

    public VariablePayout departmentRevenueBenefits(Set<DepartmentRevenueBenefit> departmentRevenueBenefits) {
        this.departmentRevenueBenefits = departmentRevenueBenefits;
        return this;
    }

    public VariablePayout addDepartmentRevenueBenefits(DepartmentRevenueBenefit departmentRevenueBenefit) {
        this.departmentRevenueBenefits.add(departmentRevenueBenefit);
        departmentRevenueBenefit.setVariablePayout(this);
        return this;
    }

    public VariablePayout removeDepartmentRevenueBenefits(DepartmentRevenueBenefit departmentRevenueBenefit) {
        this.departmentRevenueBenefits.remove(departmentRevenueBenefit);
        departmentRevenueBenefit.setVariablePayout(null);
        return this;
    }

    public void setDepartmentRevenueBenefits(Set<DepartmentRevenueBenefit> departmentRevenueBenefits) {
        this.departmentRevenueBenefits = departmentRevenueBenefits;
    }

    public Set<LengthOfStayBenefit> getLengthOfStayBenefits() {
        return lengthOfStayBenefits;
    }

    public VariablePayout lengthOfStayBenefits(Set<LengthOfStayBenefit> lengthOfStayBenefits) {
        this.lengthOfStayBenefits = lengthOfStayBenefits;
        return this;
    }

    public VariablePayout addLengthOfStayBenefits(LengthOfStayBenefit lengthOfStayBenefit) {
        this.lengthOfStayBenefits.add(lengthOfStayBenefit);
        lengthOfStayBenefit.setVariablePayout(this);
        return this;
    }

    public VariablePayout removeLengthOfStayBenefits(LengthOfStayBenefit lengthOfStayBenefit) {
        this.lengthOfStayBenefits.remove(lengthOfStayBenefit);
        lengthOfStayBenefit.setVariablePayout(null);
        return this;
    }

    public void setLengthOfStayBenefits(Set<LengthOfStayBenefit> lengthOfStayBenefits) {
        this.lengthOfStayBenefits = lengthOfStayBenefits;
    }

    public Set<ServiceItemBenefit> getServiceItemBenefits() {
        return serviceItemBenefits;
    }

    public VariablePayout serviceItemBenefits(Set<ServiceItemBenefit> serviceItemBenefits) {
        this.serviceItemBenefits = serviceItemBenefits;
        return this;
    }

    public VariablePayout addServiceItemBenefits(ServiceItemBenefit serviceItemBenefit) {
        this.serviceItemBenefits.add(serviceItemBenefit);
        serviceItemBenefit.setVariablePayout(this);
        return this;
    }

    public VariablePayout removeServiceItemBenefits(ServiceItemBenefit serviceItemBenefit) {
        this.serviceItemBenefits.remove(serviceItemBenefit);
        serviceItemBenefit.setVariablePayout(null);
        return this;
    }

    public void setServiceItemBenefits(Set<ServiceItemBenefit> serviceItemBenefits) {
        this.serviceItemBenefits = serviceItemBenefits;
    }

    public Set<ServiceItemBenefitTemplate> getServiceItemBenefitTemplates() {
        return serviceItemBenefitTemplates;
    }

    public VariablePayout serviceItemBenefitTemplates(Set<ServiceItemBenefitTemplate> serviceItemBenefitTemplates) {
        this.serviceItemBenefitTemplates = serviceItemBenefitTemplates;
        return this;
    }

    public VariablePayout addServiceItemBenefitTemplate(ServiceItemBenefitTemplate serviceItemBenefitTemplate) {
        this.serviceItemBenefitTemplates.add(serviceItemBenefitTemplate);
        //serviceItemBenefitTemplate.getVariablePayouts().add(this);
        return this;
    }

    public VariablePayout removeServiceItemBenefitTemplate(ServiceItemBenefitTemplate serviceItemBenefitTemplate) {
        this.serviceItemBenefitTemplates.remove(serviceItemBenefitTemplate);
        //serviceItemBenefitTemplate.getVariablePayouts().remove(this);
        return this;
    }

    public void setServiceItemBenefitTemplates(Set<ServiceItemBenefitTemplate> serviceItemBenefitTemplates) {
        this.serviceItemBenefitTemplates = serviceItemBenefitTemplates;
    }

    public Set<ServiceItemException> getServiceItemExceptions() {
        return serviceItemExceptions;
    }

    public VariablePayout serviceItemExceptions(Set<ServiceItemException> serviceItemExceptions) {
        this.serviceItemExceptions = serviceItemExceptions;
        return this;
    }

    public VariablePayout addServiceItemException(ServiceItemException serviceItemException) {
        this.serviceItemExceptions.add(serviceItemException);
        serviceItemException.setVariablePayout(this);
        return this;
    }

    public VariablePayout removeServiceItemException(ServiceItemException serviceItemException) {
        this.serviceItemExceptions.remove(serviceItemException);
        serviceItemException.setVariablePayout(null);
        return this;
    }

    public void setServiceItemExceptions(Set<ServiceItemException> serviceItemExceptions) {
        this.serviceItemExceptions = serviceItemExceptions;
    }

    public UserDTO getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(UserDTO approvedBy) {
        this.approvedBy = approvedBy;
    }


    public Set<TransactionApprovalDetails> getTransactionApprovalDetails() {
        return transactionApprovalDetails;
    }

    public VariablePayout transactionApprovalDetails(Set<TransactionApprovalDetails> transactionApprovalDetails) {
        this.transactionApprovalDetails = transactionApprovalDetails;
        return this;
    }

    public VariablePayout addTransactionApprovalDetails(TransactionApprovalDetails transactionApprovalDetails) {
        this.transactionApprovalDetails.add(transactionApprovalDetails);
        transactionApprovalDetails.setVariablePayout(this);
        return this;
    }

    public VariablePayout removeTransactionApprovalDetails(TransactionApprovalDetails transactionApprovalDetails) {
        this.transactionApprovalDetails.remove(transactionApprovalDetails);
        transactionApprovalDetails.setVariablePayout(null);
        return this;
    }

    public void setTransactionApprovalDetails(Set<TransactionApprovalDetails> transactionApprovalDetails) {
        this.transactionApprovalDetails = transactionApprovalDetails;
    }

    public Long getVariablePayoutId() {
        return variablePayoutId;
    }

    public void setVariablePayoutId(Long variablePayoutId) {
        this.variablePayoutId = variablePayoutId;
    }

    public UserDTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserDTO employee) {
        this.employee = employee;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VariablePayout)) {
            return false;
        }
        return id != null && id.equals(((VariablePayout) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "VariablePayout{" +
            "id=" + getId() +
            ", unitCode='" + getUnitCode() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", version=" + getVersion() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", changeRequestStatus='" + getChangeRequestStatus() + "'" +
            ", startingVersion=" + getStartingVersion() +
            ", currentVersion=" + getCurrentVersion() +
            ", remarks='" + getRemarks() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", contractEndDate='" + getContractEndDate() + "'" +
            ", minAssuredAmount=" + getMinAssuredAmount() +
            ", maxPayoutAmount=" + getMaxPayoutAmount() +
            ", minAssuredValidityDate='" + getMinAssuredValidityDate() + "'" +
            ", status='" + isStatus() + "'" +
            ", uploadContract='" + getUploadContract() + "'" +
            "}";
    }
}
