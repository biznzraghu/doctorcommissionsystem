package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.nh.artha.domain.dto.UserDTO;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import org.nh.artha.domain.enumeration.TransactionType;

/**
 * A PayoutAdjustment.
 */
@Entity
@Table(name = "payout_adjustment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_payoutadjustment")
@Setting(settingPath = "/es/settings.json")
public class PayoutAdjustment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_number")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
         //   @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String documentNumber;

    @NotNull
    @Column(name = "unit_code")
    private String unitCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
           // @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private TransactionType type;

    @Column(name = "employee_id")
    private Long employeeId;

    @Type(type = "jsonb")
    @Column(name = "employee_detail" ,columnDefinition = "jsonb" ,nullable = true)
    @Field(type = FieldType.Object)
    private UserDTO employeeDetail;

    @Column(name = "department_id")
    private Long departmentId;

    @Type(type = "jsonb")
    @Column(name = "department" ,columnDefinition = "jsonb" ,nullable = true)
    @Field(type = FieldType.Object)
    private Department department;

    @Column(name = "net_amount", precision = 21, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "created_by_id")
    private Long createdById;

    @Type(type = "jsonb")
    @Column(name = "created_by" ,columnDefinition = "jsonb" ,nullable = true)
    private UserDTO createdBy;

    @Column(name = "created_date_time")
    @MultiField(
        mainField = @Field(type = FieldType.Date),
        otherFields = {
           // @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private LocalDateTime createdDateTime=LocalDateTime.now();

    @Column(name = "status")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
          //  @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String status;

    @Column(name = "modified_by_id")
    private Long modifiedById;

    @Type(type = "jsonb")
    @Column(name = "modified_by" ,columnDefinition = "jsonb" ,nullable = true)
    private UserDTO modifiedBy;

    @Column(name = "modified_date_time")
    private LocalDateTime modifiedDateTime;

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name = "payout_adjustment_id",nullable = false)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PayoutAdjustmentDetails> payoutAdjustmentDetails = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "payout_adjustment_id",nullable = false)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionApprovalDetails> transactionApprovalDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public UserDTO getEmployeeDetail() {
        return employeeDetail;
    }

    public void setEmployeeDetail(UserDTO employeeDetail) {
        this.employeeDetail = employeeDetail;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }


    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Long modifiedById) {
        this.modifiedById = modifiedById;
    }

    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserDTO modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public Set<PayoutAdjustmentDetails> getPayoutAdjustmentDetails() {
        return payoutAdjustmentDetails;
    }

    /*public PayoutAdjustment payoutAdjustmentDetails(Set<PayoutAdjustmentDetails> payoutAdjustmentDetails){
        this.payoutAdjustmentDetails = payoutAdjustmentDetails;
        return this;
    }

    public PayoutAdjustment addPayoutAdjustmentDetails(PayoutAdjustmentDetails payoutAdjustmentDetail){
        this.payoutAdjustmentDetails.add(payoutAdjustmentDetail);
        payoutAdjustmentDetail.setPayoutAdjustment(this);
        return this;
    }

    public PayoutAdjustment removePayoutAdjustmentDetails(PayoutAdjustmentDetails payoutAdjustmentDetail){
        this.payoutAdjustmentDetails.remove(payoutAdjustmentDetail);
        payoutAdjustmentDetail.setPayoutAdjustment(null);
        return this;
    }*/

    public void setPayoutAdjustmentDetails(Set<PayoutAdjustmentDetails> payoutAdjustmentDetails) {
        this.payoutAdjustmentDetails = payoutAdjustmentDetails;
    }

    public Set<TransactionApprovalDetails> getTransactionApprovalDetails() {
        return transactionApprovalDetails;
    }

    public void setTransactionApprovalDetails(Set<TransactionApprovalDetails> transactionApprovalDetails) {
        this.transactionApprovalDetails = transactionApprovalDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayoutAdjustment)) {
            return false;
        }
        return id != null && id.equals(((PayoutAdjustment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PayoutAdjustment{" +
            "id=" + getId() +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", unitCode='" + getUnitCode() + "'" +
            ", type='" + getType() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", departmentId=" + getDepartmentId() +
            ", netAmount=" + getNetAmount() +
            ", createdById=" + getCreatedById() +
            ", createdDateTime='" + getCreatedDateTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", modifiedById='" + getModifiedById() + "'" +
            ", modifiedDateTime='" + getModifiedDateTime() + "'" +
            ", payoutAdjustmentDetails='" + getPayoutAdjustmentDetails() + "'" +
            ", transactionApprovalDetails='" + getTransactionApprovalDetails() + "'" +
            "}";
    }
}
