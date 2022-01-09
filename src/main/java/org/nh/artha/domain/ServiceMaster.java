package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;

/**
 * A ServiceMaster.
 */
@Entity
@Table(name = "service_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_servicemaster")
@Setting(settingPath = "/es/settings.json")
public class ServiceMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "min_re_order_duration")
    private String minReOrderDuration;

    @Column(name = "service_duration")
    private String serviceDuration;

    @Column(name = "auto_process")
    private Boolean autoProcess;

    @Column(name = "individually_orderable")
    private Boolean individuallyOrderable;

    @Column(name = "consent_required")
    private Boolean consentRequired;

    @Column(name = "insurance_exempted")
    private Boolean insuranceExempted;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "profile_service", nullable = false)
    private Boolean profileService;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnoreProperties("serviceMasters")
    private ServiceType serviceType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnoreProperties("serviceMasters")
    private ServiceCategory category;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIgnoreProperties("ServiceMasters")
    private Group serviceGroup;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ServiceMaster name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public ServiceMaster code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public ServiceMaster shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMinReOrderDuration() {
        return minReOrderDuration;
    }

    public ServiceMaster minReOrderDuration(String minReOrderDuration) {
        this.minReOrderDuration = minReOrderDuration;
        return this;
    }

    public void setMinReOrderDuration(String minReOrderDuration) {
        this.minReOrderDuration = minReOrderDuration;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public ServiceMaster serviceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
        return this;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public Boolean isAutoProcess() {
        return autoProcess;
    }

    public ServiceMaster autoProcess(Boolean autoProcess) {
        this.autoProcess = autoProcess;
        return this;
    }

    public void setAutoProcess(Boolean autoProcess) {
        this.autoProcess = autoProcess;
    }

    public Boolean isIndividuallyOrderable() {
        return individuallyOrderable;
    }

    public ServiceMaster individuallyOrderable(Boolean individuallyOrderable) {
        this.individuallyOrderable = individuallyOrderable;
        return this;
    }

    public void setIndividuallyOrderable(Boolean individuallyOrderable) {
        this.individuallyOrderable = individuallyOrderable;
    }

    public Boolean isConsentRequired() {
        return consentRequired;
    }

    public ServiceMaster consentRequired(Boolean consentRequired) {
        this.consentRequired = consentRequired;
        return this;
    }

    public void setConsentRequired(Boolean consentRequired) {
        this.consentRequired = consentRequired;
    }

    public Boolean isInsuranceExempted() {
        return insuranceExempted;
    }

    public ServiceMaster insuranceExempted(Boolean insuranceExempted) {
        this.insuranceExempted = insuranceExempted;
        return this;
    }

    public void setInsuranceExempted(Boolean insuranceExempted) {
        this.insuranceExempted = insuranceExempted;
    }

    public Boolean isActive() {
        return active;
    }

    public ServiceMaster active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isProfileService() {
        return profileService;
    }

    public ServiceMaster profileService(Boolean profileService) {
        this.profileService = profileService;
        return this;
    }

    public void setProfileService(Boolean profileService) {
        this.profileService = profileService;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public ServiceMaster serviceType(ServiceType ServiceType) {
        this.serviceType = ServiceType;
        return this;
    }

    public void setServiceType(ServiceType ServiceType) {
        this.serviceType = ServiceType;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public ServiceMaster category(ServiceCategory ServiceCategory) {
        this.category = ServiceCategory;
        return this;
    }

    public void setCategory(ServiceCategory ServiceCategory) {
        this.category = ServiceCategory;
    }

    public Group getServiceGroup() {
        return serviceGroup;
    }

    public ServiceMaster serviceGroup(Group Group) {
        this.serviceGroup = Group;
        return this;
    }

    public void setServiceGroup(Group Group) {
        this.serviceGroup = Group;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceMaster)) {
            return false;
        }
        return id != null && id.equals(((ServiceMaster) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServiceMaster{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", minReOrderDuration='" + getMinReOrderDuration() + "'" +
            ", serviceDuration='" + getServiceDuration() + "'" +
            ", autoProcess='" + isAutoProcess() + "'" +
            ", individuallyOrderable='" + isIndividuallyOrderable() + "'" +
            ", consentRequired='" + isConsentRequired() + "'" +
            ", insuranceExempted='" + isInsuranceExempted() + "'" +
            ", active='" + isActive() + "'" +
            ", profileService='" + isProfileService() + "'" +
            "}";
    }
}
