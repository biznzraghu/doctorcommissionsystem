package org.nh.artha.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import org.nh.artha.domain.enumeration.Status;

import org.nh.artha.domain.enumeration.VisitType;

import org.nh.artha.domain.enumeration.DiscountType;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A PayoutDetails.
 */
@Entity
@Table(name = "payout_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_payoutdetails")
@Setting(settingPath = "/es/settings.json")
public class PayoutDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "commencement_date", nullable = false)
    private LocalDateTime commencementDate;

    @NotNull
    @Column(name = "contract_end_date", nullable = false)
    private LocalDateTime contractEndDate;

    @Column(name = "minimum_assured_amount")
    private Double minimumAssuredAmount;

    @Column(name = "minimum_assured_validity")
    private Double minimumAssuredValidity;

    @Column(name = "maximum_payout_amount")
    private Double maximumPayoutAmount;

    @Column(name = "upload_contract")
    private String uploadContract;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type", nullable = false)
    private VisitType visitType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "discount", nullable = false)
    private DiscountType discount;

    @Column(name = "payout_range")
    private String payoutRange;

    @Column(name = "applicable_invoices")
    private String applicableInvoices;

    @Column(name = "material_reduction")
    private String materialReduction;

    @Column(name = "exception")
    private String exception;

    @Column(name = "comments")
    private String comments;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private UserMaster userDetails;

    @OneToMany(mappedBy = "id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DepartmentRevenue> departmentalRevenues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDateTime commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDateTime getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDateTime contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Double getMinimumAssuredAmount() {
        return minimumAssuredAmount;
    }

    public void setMinimumAssuredAmount(Double minimumAssuredAmount) {
        this.minimumAssuredAmount = minimumAssuredAmount;
    }

    public Double getMinimumAssuredValidity() {
        return minimumAssuredValidity;
    }

    public void setMinimumAssuredValidity(Double minimumAssuredValidity) {
        this.minimumAssuredValidity = minimumAssuredValidity;
    }

    public Double getMaximumPayoutAmount() {
        return maximumPayoutAmount;
    }

    public void setMaximumPayoutAmount(Double maximumPayoutAmount) {
        this.maximumPayoutAmount = maximumPayoutAmount;
    }

    public String getUploadContract() {
        return uploadContract;
    }

    public void setUploadContract(String uploadContract) {
        this.uploadContract = uploadContract;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public DiscountType getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountType discount) {
        this.discount = discount;
    }

    public String getPayoutRange() {
        return payoutRange;
    }

    public void setPayoutRange(String payoutRange) {
        this.payoutRange = payoutRange;
    }

    public String getApplicableInvoices() {
        return applicableInvoices;
    }

    public void setApplicableInvoices(String applicableInvoices) {
        this.applicableInvoices = applicableInvoices;
    }

    public String getMaterialReduction() {
        return materialReduction;
    }

    public void setMaterialReduction(String materialReduction) {
        this.materialReduction = materialReduction;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public UserMaster getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserMaster userMaster) {
        this.userDetails = userMaster;
    }

    public Set<DepartmentRevenue> getDepartmentalRevenues() {
        return departmentalRevenues;
    }

    public void setDepartmentalRevenues(Set<DepartmentRevenue> departmentRevenues) {
        this.departmentalRevenues = departmentRevenues;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayoutDetails)) {
            return false;
        }
        return id != null && id.equals(((PayoutDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PayoutDetails{" +
            "id=" + getId() +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", contractEndDate='" + getContractEndDate() + "'" +
            ", minimumAssuredAmount=" + getMinimumAssuredAmount() +
            ", minimumAssuredValidity=" + getMinimumAssuredValidity() +
            ", maximumPayoutAmount=" + getMaximumPayoutAmount() +
            ", uploadContract='" + getUploadContract() + "'" +
            ", status='" + getStatus() + "'" +
            ", visitType='" + getVisitType() + "'" +
            ", discount='" + getDiscount() + "'" +
            ", payoutRange='" + getPayoutRange() + "'" +
            ", applicableInvoices='" + getApplicableInvoices() + "'" +
            ", materialReduction='" + getMaterialReduction() + "'" +
            ", exception='" + getException() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
