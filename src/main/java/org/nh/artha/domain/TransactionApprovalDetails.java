package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.nh.artha.domain.dto.UserDTO;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.time.ZonedDateTime;

import org.nh.artha.domain.enumeration.DocumentType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

/**
 * A TransactionApprovalDetails.
 */
@Entity
@Table(name = "transaction_approval_details")
public class TransactionApprovalDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(name = "type")
    private String type;

    @Column(name = "approved_by_id")
    private Long approvedById;

    @Column(name = "approved_by_login")
    private String approvedByLogin;

    @Column(name = "approved_by_employee_no")
    private String approvedByEmployeeNo;

    @Column(name = "approved_by_display_name")
    private String approvedByDisplayName;

    @Column(name = "approved_date_time")
    @MultiField(
        mainField = @Field(type = FieldType.Date),
        otherFields = {
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private LocalDateTime approvedDateTime;

    @Column(name = "approved_by" ,columnDefinition = "jsonb" ,nullable = true)
    @Type(type = "jsonb")
    private UserDTO approvedBy;

    @ManyToOne
    @JsonIgnoreProperties("transactionApprovalDetails")
    @JsonIgnore
    @ReadOnlyProperty
    private DepartmentPayout departmentPayout;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ReadOnlyProperty
    private VariablePayout variablePayout;

    @Column(name = "comments")
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public DepartmentPayout getDepartmentPayout() {
        return departmentPayout;
    }

    public TransactionApprovalDetails departmentPayout(DepartmentPayout departmentPayout) {
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

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long approvedById) {
        this.approvedById = approvedById;
    }

    public String getApprovedByLogin() {
        return approvedByLogin;
    }

    public void setApprovedByLogin(String approvedByLogin) {
        this.approvedByLogin = approvedByLogin;
    }

    public String getApprovedByEmployeeNo() {
        return approvedByEmployeeNo;
    }

    public void setApprovedByEmployeeNo(String approvedByEmployeeNo) {
        this.approvedByEmployeeNo = approvedByEmployeeNo;
    }

    public String getApprovedByDisplayName() {
        return approvedByDisplayName;
    }

    public void setApprovedByDisplayName(String approvedByDisplayName) {
        this.approvedByDisplayName = approvedByDisplayName;
    }

    public LocalDateTime getApprovedDateTime() {
        return approvedDateTime;
    }

    public void setApprovedDateTime(LocalDateTime approvedDateTime) {
        this.approvedDateTime = approvedDateTime;
    }

    public UserDTO getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(UserDTO approvedBy) {
        this.approvedBy = approvedBy;
    }

    public VariablePayout getVariablePayout() {
        return variablePayout;
    }

    public TransactionApprovalDetails variablePayout(VariablePayout variablePayout) {
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
        if (!(o instanceof TransactionApprovalDetails)) {
            return false;
        }
        return id != null && id.equals(((TransactionApprovalDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TransactionApprovalDetails{" +
            "id=" + getId() +
            ", documentType='" + getDocumentType() + "'" +
            ", type='" + getType() + "'" +
            ", approvedById=" + getApprovedById() +
            ", approvedByLogin='" + getApprovedByLogin() + "'" +
            ", approvedByEmployeeNo='" + getApprovedByEmployeeNo() + "'" +
            ", approvedByDisplayName='" + getApprovedByDisplayName() + "'" +
            ", approvedDateTime='" + getApprovedDateTime() + "'" +
            "}";
    }
}
