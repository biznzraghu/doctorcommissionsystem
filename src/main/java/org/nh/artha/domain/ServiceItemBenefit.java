package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nh.artha.domain.dto.CodeNameDto;
import org.nh.artha.domain.dto.RuleKeyDTO;
import org.nh.artha.domain.enumeration.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A ServiceItemBenefit.
 */
@Entity
@Table(name = "service_item_benefit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_serviceitembenefit")
@Setting(settingPath = "/es/settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceItemBenefit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "rule_key")
    @JsonIgnore
    private RuleKeyDTO rule_key;

    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "service_group")
    @Field(type = FieldType.Object)
    private List<Group> serviceGroup;

    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "service_type")
    private ServiceType serviceType;

    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "item_category")
    private ItemCategory itemCategory;

    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "item_group")
    private ItemGroup itemGroup;


    @Enumerated(EnumType.STRING)
    @Column(name = "package_category")
    private PackageCategory packageCategory;


    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(name = "visit_type",columnDefinition = "jsonb")
    private List<String> visitType;

    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb",name = "components")
    private CodeNameDto components;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "beneficiary", nullable = false)
    private Beneficiary beneficiary;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    @Column(name = "payment_value", precision = 21, scale = 2)
    private BigDecimal paymentValue;

    @Column(name = "min_quantity")
    private Integer minQuantity;

    @Column(name = "max_quantity")
    private Integer maxQuantity;

    @Column(name = "amount")
    private String amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicable_on")
    private ApplicableOn applicableOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "patient_category")
    private PatientCategory patientCategory;


    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(name = "tariff_class",columnDefinition = "jsonb")
    private List<ValueSetCode> tariffClass;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_amount")
    private MaterialAmount materialAmount;

    @Column(name = "variable_payout_base_id")
    private Integer variablePayoutBaseId;

    @Column(name = "valid_from")
    private Integer validFrom;

    @Column(name = "valid_to")
    private Integer validTo;


    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(name = "department",columnDefinition = "jsonb")
    private List<Department> department;

    @Column(name = "applicable_sponsor")
    private Long applicableSponsor;

    @Column(name = "exempted_sponsor")
    private Long exemptedSponsor;

    @Column(name = "service_exception")
    private Long serviceException;

    @Column(name = "item_exception")
    private Long itemException;

    @Column(name = "on_death_incentive")
    private Boolean onDeathIncentive;

    @Column(name = "latest")
    private Boolean latest=Boolean.TRUE;

    @ManyToOne
    private VariablePayout variablePayout;

    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceItemBenefitTemplate planTemplate;



    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "service_master")
    private ServiceMaster serviceMaster;

    @org.hibernate.annotations.Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "package_master")
    private PackageMaster packageMaster;

    @OneToMany(mappedBy = "serviceItemBenefit",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceItemException> serviceItemExceptions = new HashSet<>();

    @Column(name = "template_value_display")
    private String templateValueDisplay;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private ExceptionSponsor exceptionSponsor;

    @Column(name = "invoice_value")
    private InvoiceValue invoiceValue;

    public ExceptionSponsor getExceptionSponsor() {
        return exceptionSponsor;
    }

    public void setExceptionSponsor(ExceptionSponsor exceptionSponsor) {
        this.exceptionSponsor = exceptionSponsor;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public ServiceItemBenefit type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public RuleKeyDTO getRule_key() {
        return rule_key;
    }

    public ServiceItemBenefit rule_key(RuleKeyDTO rule_key) {
        this.rule_key = rule_key;
        return this;
    }

    public void setRule_key(RuleKeyDTO rule_key) {
        this.rule_key = rule_key;
    }

    public List<Group> getServiceGroup() {
        return serviceGroup;
    }

    public ServiceItemBenefit serviceGroup(List<Group> serviceGroup) {
        this.serviceGroup = serviceGroup;
        return this;
    }

    public void setServiceGroup(List<Group> serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public ServiceItemBenefit serviceType(ServiceType serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public ServiceItemBenefit itemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
        return this;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public ServiceItemBenefit itemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
        return this;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public PackageCategory getPackageCategory() {
        return packageCategory;
    }

    public ServiceItemBenefit packageCategory(PackageCategory packageCategory) {
        this.packageCategory = packageCategory;
        return this;
    }

    public void setPackageCategory(PackageCategory packageCategory) {
        this.packageCategory = packageCategory;
    }

    public List<String> getVisitType() {
        return visitType;
    }

    public ServiceItemBenefit visitType(List<String> visitType) {
        this.visitType = visitType;
        return this;
    }

    public void setVisitType(List<String> visitType) {
        this.visitType = visitType;
    }

    public CodeNameDto getComponents() {
        return components;
    }

    public ServiceItemBenefit components(CodeNameDto components) {
        this.components = components;
        return this;
    }

    public void setComponents(CodeNameDto components) {
        this.components = components;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public ServiceItemBenefit beneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
        return this;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public ServiceItemBenefit paymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
        return this;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public BigDecimal getPaymentValue() {
        return paymentValue;
    }

    public ServiceItemBenefit paymentValue(BigDecimal paymentValue) {
        this.paymentValue = paymentValue;
        return this;
    }

    public void setPaymentValue(BigDecimal paymentValue) {
        this.paymentValue = paymentValue;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public ServiceItemBenefit minQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
        return this;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public ServiceItemBenefit maxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getAmount() {
        return amount;
    }

    public ServiceItemBenefit amount(String amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ApplicableOn getApplicableOn() {
        return applicableOn;
    }

    public ServiceItemBenefit applicableOn(ApplicableOn applicableOn) {
        this.applicableOn = applicableOn;
        return this;
    }

    public void setApplicableOn(ApplicableOn applicableOn) {
        this.applicableOn = applicableOn;
    }

    public PatientCategory getPatientCategory() {
        return patientCategory;
    }

    public ServiceItemBenefit patientCategory(PatientCategory patientCategory) {
        this.patientCategory = patientCategory;
        return this;
    }

    public void setPatientCategory(PatientCategory patientCategory) {
        this.patientCategory = patientCategory;
    }

    public List<ValueSetCode> getTariffClass() {
        return tariffClass;
    }

    public ServiceItemBenefit tariffClass(List<ValueSetCode> tariffClass) {
        this.tariffClass = tariffClass;
        return this;
    }

    public void setTariffClass(List<ValueSetCode> tariffClass) {
        this.tariffClass = tariffClass;
    }

    public MaterialAmount getMaterialAmount() {
        return materialAmount;
    }

    public ServiceItemBenefit materialAmount(MaterialAmount materialAmount) {
        this.materialAmount = materialAmount;
        return this;
    }

    public void setMaterialAmount(MaterialAmount materialAmount) {
        this.materialAmount = materialAmount;
    }

    public List<Department> getDepartment() {
        return department;
    }

    public ServiceItemBenefit department(List<Department> department) {
        this.department = department;
        return this;
    }

    public void setDepartment(List<Department> department) {
        this.department = department;
    }

    public Long getApplicableSponsor() {
        return applicableSponsor;
    }

    public ServiceItemBenefit applicableSponsor(Long applicableSponsor) {
        this.applicableSponsor = applicableSponsor;
        return this;
    }

    public void setApplicableSponsor(Long applicableSponsor) {
        this.applicableSponsor = applicableSponsor;
    }

    public Long getExemptedSponsor() {
        return exemptedSponsor;
    }

    public ServiceItemBenefit exemptedSponsor(Long exemptedSponsor) {
        this.exemptedSponsor = exemptedSponsor;
        return this;
    }

    public void setExemptedSponsor(Long exemptedSponsor) {
        this.exemptedSponsor = exemptedSponsor;
    }

    public Long getServiceException() {
        return serviceException;
    }

    public ServiceItemBenefit serviceException(Long serviceException) {
        this.serviceException = serviceException;
        return this;
    }

    public void setServiceException(Long serviceException) {
        this.serviceException = serviceException;
    }

    public Long getItemException() {
        return itemException;
    }

    public ServiceItemBenefit itemException(Long itemException) {
        this.itemException = itemException;
        return this;
    }

    public void setItemException(Long itemException) {
        this.itemException = itemException;
    }

    public Boolean isOnDeathIncentive() {
        return onDeathIncentive;
    }

    public ServiceItemBenefit onDeathIncentive(Boolean onDeathIncentive) {
        this.onDeathIncentive = onDeathIncentive;
        return this;
    }

    public void setOnDeathIncentive(Boolean onDeathIncentive) {
        this.onDeathIncentive = onDeathIncentive;
    }

    public VariablePayout getVariablePayout() {
        return variablePayout;
    }

    public ServiceItemBenefit variablePayout(VariablePayout variablePayout) {
        this.variablePayout = variablePayout;
        return this;
    }

    public void setVariablePayout(VariablePayout variablePayout) {
        this.variablePayout = variablePayout;
    }

    public ServiceItemBenefitTemplate getPlanTemplate() {
        return planTemplate;
    }

    public ServiceItemBenefit planTemplate(ServiceItemBenefitTemplate serviceItemBenefitTemplate) {
        this.planTemplate = serviceItemBenefitTemplate;
        return this;
    }

    public void setPlanTemplate(ServiceItemBenefitTemplate serviceItemBenefitTemplate) {
        this.planTemplate = serviceItemBenefitTemplate;
    }

    public ServiceMaster getServiceMaster() {
        return serviceMaster;
    }

    public void setServiceMaster(ServiceMaster serviceMaster) {
        this.serviceMaster = serviceMaster;
    }

    public PackageMaster getPackageMaster() {
        return packageMaster;
    }

    public void setPackageMaster(PackageMaster packageMaster) {
        this.packageMaster = packageMaster;
    }

    public Set<ServiceItemException> getServiceItemExceptions() {
        return serviceItemExceptions;
    }

    public ServiceItemBenefit serviceItemExceptions(Set<ServiceItemException> serviceItemExceptions) {
        this.serviceItemExceptions = serviceItemExceptions;
        return this;
    }



    public void setServiceItemExceptions(Set<ServiceItemException> serviceItemExceptions) {
        this.serviceItemExceptions = serviceItemExceptions;
    }

    public String getTemplateValueDisplay() {
        return templateValueDisplay;
    }

    public void setTemplateValueDisplay(String templateValueDisplay) {
        this.templateValueDisplay = templateValueDisplay;
    }

    public Integer getVariablePayoutBaseId() {
        return variablePayoutBaseId;
    }

    public void setVariablePayoutBaseId(Integer variablePayoutBaseId) {
        this.variablePayoutBaseId = variablePayoutBaseId;
    }

    public Integer getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Integer validFrom) {
        this.validFrom = validFrom;
    }

    public Integer getValidTo() {
        return validTo;
    }

    public void setValidTo(Integer validTo) {
        this.validTo = validTo;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

    public InvoiceValue getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(InvoiceValue invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public String getVisitTypeTariffClassKey(String tariffClass,String visitType) {
        StringBuilder key = new StringBuilder();
        if (null != visitType) {
            key.append(visitType+"-");
        } else {
            key.append("-X");
        }
        if (null != tariffClass) {
            key.append(tariffClass);
        } else {
            key.append("-X");
        }
        return key.toString();
    }
    public String getTypeKey() {
        StringBuilder key = new StringBuilder();
        if (null != this.getServiceType()) {
            if (null != this.getServiceType())
                key.append(this.getServiceType().getDisplay());
        } else {
            key.append("-X");
        }
        return key.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceItemBenefit)) {
            return false;
        }
        return id != null && id.equals(((ServiceItemBenefit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServiceItemBenefit{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", rule_key='" + getRule_key() + "'" +
            ", serviceGroup='" + getServiceGroup() + "'" +
            ", serviceType='" + getServiceType() + "'" +
            ", itemCategory='" + getItemCategory() + "'" +
            ", itemGroup='" + getItemGroup() + "'" +
            ", packageCategory='" + getPackageCategory() + "'" +
            ", visitType='" + getVisitType() + "'" +
            ", components='" + getComponents() + "'" +
            ", beneficiary='" + getBeneficiary() + "'" +
            ", paymentMode='" + getPaymentMode() + "'" +
            ", paymentValue=" + getPaymentValue() +
            ", minQuantity=" + getMinQuantity() +
            ", maxQuantity=" + getMaxQuantity() +
            ", amount=" + getAmount() +
            ", applicableOn='" + getApplicableOn() + "'" +
            ", patientCategory='" + getPatientCategory() + "'" +
            ", tariffClass='" + getTariffClass() + "'" +
            ", materialAmount='" + getMaterialAmount() + "'" +
            ", department=" + getDepartment() +
            ", applicableSponsor=" + getApplicableSponsor() +
            ", exemptedSponsor=" + getExemptedSponsor() +
            ", serviceException=" + getServiceException() +
            ", itemException=" + getItemException() +
            ", onDeathIncentive='" + isOnDeathIncentive() + "'" +
            "}";
    }
}
