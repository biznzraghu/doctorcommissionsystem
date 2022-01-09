package org.nh.artha.domain;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.nh.artha.domain.dto.UserDTO;
import org.nh.artha.domain.enumeration.ChangeRequestStatus;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.nh.artha.domain.enumeration.NetGross;

import org.nh.artha.domain.enumeration.ApplicableInvoices;

import org.nh.artha.domain.enumeration.OnCostSale;

/**
 * A DepartmentPayout.
 */
@Entity
@Table(name = "department_payout")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_departmentpayout")
@Setting(settingPath = "/es/settings.json")
public class DepartmentPayout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "jsonb")
    @Column(name = "created_by",columnDefinition = "jsonb")
    private UserDTO createdBy;

    @Column(name = "created_date")
    @MultiField(
        mainField = @Field(type = FieldType.Date),
        otherFields = {
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private LocalDateTime createdDate=LocalDateTime.now();



    @Column(name = "version")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Integer version=1;

    @Column(name = "status")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Boolean status = Boolean.FALSE;

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

    @Column(name = "latest")
    private Boolean latest=Boolean.TRUE;

    @Column(name = "visit_type")
    private String visitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "net_gross")
    private NetGross netGross;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicable_invoice")
    private ApplicableInvoices applicableInvoice;

    @Enumerated(EnumType.STRING)
    @Column(name = "on_cost_sale")
    private OnCostSale onCostSale;

    @Column(name = "all_materials")
    private Boolean allMaterials=Boolean.FALSE;

    @Column(name = "drugs")
    private Boolean drugs=Boolean.FALSE;

    @Column(name = "implants")
    private Boolean implants=Boolean.FALSE;

    @Column(name = "consumables")
    private Boolean consumables=Boolean.FALSE;

    @Column(name = "dept_consumption")
    private Boolean deptConsumption=Boolean.FALSE;

    @Column(name = "hsc_consumption")
    private Boolean hscConsumption=Boolean.FALSE;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "unit_code")
    private String unitCode;

    @OneToMany(mappedBy = "departmentPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PayoutRange> payoutRanges = new HashSet<>();

    @OneToMany(mappedBy = "departmentPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ApplicableConsultant> applicableConsultants = new HashSet<>();


    @OneToMany(mappedBy = "departmentPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductions = new HashSet<>();

    @OneToMany(mappedBy = "departmentPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<HscConsumptionMaterialReduction> hscConsumptionMaterialReductions = new HashSet<>();

    @OneToMany(mappedBy = "departmentPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceItemException> serviceItemExceptions = new HashSet<>();

    @OneToMany(mappedBy = "departmentPayout",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TransactionApprovalDetails> transactionApprovalDetails = new HashSet<>();

    @Column(name = "department_name")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String departmentName;

    @Column(name = "starting_version")
    private Long startingVersion;

    @Column(name = "current_version")
    private Long currentVersion;

    @Column(name = "custom_department")
    private Boolean customDepartment=Boolean.FALSE;

    @Column(name = "active")
    private Boolean active=Boolean.TRUE;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getCustomDepartment() {
        return customDepartment;
    }

    public void setCustomDepartment(Boolean customDepartment) {
        this.customDepartment = customDepartment;
    }

    public Long getStartingVersion() {
        return startingVersion;
    }

    public DepartmentPayout startingVersion(Long startingVersion) {
        this.startingVersion = startingVersion;
        return this;
    }

    public void setStartingVersion(Long startingVersion) {
        this.startingVersion = startingVersion;
    }

    public Long getCurrentVersion() {
        return currentVersion;
    }

    public DepartmentPayout currentVersion(Long currentVersion) {
        this.currentVersion = currentVersion;
        return this;
    }

    public void setCurrentVersion(Long currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Set<TransactionApprovalDetails> getTransactionApprovalDetails() {
        return transactionApprovalDetails;
    }

    public void setTransactionApprovalDetails(Set<TransactionApprovalDetails> transactionApprovalDetails) {
        this.transactionApprovalDetails = transactionApprovalDetails;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /*@Transient
        private Boolean newFlag=true;
        @Override
        public boolean isNew() {
            if(newFlag){
                newFlag = false;
                return true;
            }
            return newFlag;
        }*/
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ChangeRequestStatus getChangeRequestStatus() {
        return changeRequestStatus;
    }

    public void setChangeRequestStatus(ChangeRequestStatus changeRequestStatus) {
        this.changeRequestStatus = changeRequestStatus;
    }

    public Boolean isLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public NetGross getNetGross() {
        return netGross;
    }

    public void setNetGross(NetGross netGross) {
        this.netGross = netGross;
    }

    public ApplicableInvoices getApplicableInvoice() {
        return applicableInvoice;
    }

    public void setApplicableInvoice(ApplicableInvoices applicableInvoice) {
        this.applicableInvoice = applicableInvoice;
    }

    public OnCostSale getOnCostSale() {
        return onCostSale;
    }

    public void setOnCostSale(OnCostSale onCostSale) {
        this.onCostSale = onCostSale;
    }

    public Boolean isAllMaterials() {
        return allMaterials;
    }

    public void setAllMaterials(Boolean allMaterials) {
        this.allMaterials = allMaterials;
    }

    public Boolean isDrugs() {
        return drugs;
    }

    public void setDrugs(Boolean drugs) {
        this.drugs = drugs;
    }

    public Boolean isImplants() {
        return implants;
    }

    public void setImplants(Boolean implants) {
        this.implants = implants;
    }

    public Boolean isConsumables() {
        return consumables;
    }

    public void setConsumables(Boolean consumables) {
        this.consumables = consumables;
    }

    public Boolean isDeptConsumption() {
        return deptConsumption;
    }

    public void setDeptConsumption(Boolean deptConsumption) {
        this.deptConsumption = deptConsumption;
    }

    public Boolean isHscConsumption() {
        return hscConsumption;
    }

    public void setHscConsumption(Boolean hscConsumption) {
        this.hscConsumption = hscConsumption;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Set<PayoutRange> getPayoutRanges() {

        return payoutRanges;
    }
    public void setPayoutRanges(Set<PayoutRange> payoutRanges) {

        this.payoutRanges=payoutRanges;
    }
    public Set<ApplicableConsultant> getApplicableConsultants() {
        return applicableConsultants;
    }

    public void setApplicableConsultants(Set<ApplicableConsultant> applicableConsultants) {
        this.applicableConsultants = applicableConsultants;
    }

    public Set<DepartmentConsumptionMaterialReduction> getDepartmentConsumptionMaterialReductions() {
        return departmentConsumptionMaterialReductions;
    }

    public void setDepartmentConsumptionMaterialReductions(Set<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductions) {
        this.departmentConsumptionMaterialReductions = departmentConsumptionMaterialReductions;
    }

    public Set<HscConsumptionMaterialReduction> getHscConsumptionMaterialReductions() {
        return hscConsumptionMaterialReductions;
    }

    public void setHscConsumptionMaterialReductions(Set<HscConsumptionMaterialReduction> hscConsumptionMaterialReductions) {
        this.hscConsumptionMaterialReductions=hscConsumptionMaterialReductions;
    }

    public Set<ServiceItemException> getServiceItemExceptions() {
        return serviceItemExceptions;
    }

    public DepartmentPayout serviceItemExceptions(Set<ServiceItemException> serviceItemExceptions) {
        this.serviceItemExceptions = serviceItemExceptions;
        return this;
    }


    public DepartmentPayout removeServiceItemException(ServiceItemException serviceItemException) {
        this.serviceItemExceptions.remove(serviceItemException);
        return this;
    }

    public void setServiceItemExceptions(Set<ServiceItemException> serviceItemExceptions) {
        this.serviceItemExceptions = serviceItemExceptions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        /*if (this == o) {
            return true;
        }*/
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DepartmentPayout departmentPayout = (DepartmentPayout) o;
        if(departmentPayout.id == null || id == null || departmentPayout.version == null || version ==null) {
            return false;
        }
        return Objects.equals(id, departmentPayout.id) && Objects.equals(version,departmentPayout.version);    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DepartmentPayout{" +
            "id=" + getId() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", version=" + getVersion() +
            ", status='" + getStatus() + "'" +
            ", changeRequestStatus='" + getChangeRequestStatus() + "'" +
            ", latest='" + isLatest() + "'" +
            ", visitType='" + getVisitType() + "'" +
            ", netGross='" + getNetGross() + "'" +
            ", applicableInvoice='" + getApplicableInvoice() + "'" +
            ", onCostSale='" + getOnCostSale() + "'" +
            ", allMaterials='" + isAllMaterials() + "'" +
            ", drugs='" + isDrugs() + "'" +
            ", implants='" + isImplants() + "'" +
            ", consumables='" + isConsumables() + "'" +
            ", deptConsumption='" + isDeptConsumption() + "'" +
            ", hscConsumption='" + isHscConsumption() + "'" +
            ", payoutRanges='" + getPayoutRanges() + "'" +
            ", applicableConsultants='" + getApplicableConsultants() + "'" +
            ", departmentConsumptionMaterialReductions='" + getDepartmentConsumptionMaterialReductions() + "'" +
            ", hscConsumptionMaterialReductions='" + getHscConsumptionMaterialReductions() + "'" +
            "}";
    }
}
