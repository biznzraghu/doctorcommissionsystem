package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.util.Objects;

/**
 * A StayBenefitService.
 */
@Entity
@Table(name = "stay_benefit_service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_staybenefitservice")
@Setting(settingPath = "/es/settings.json")
public class StayBenefitService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "stay_benefit_id")
    private Long stayBenefitId;

    @NotNull
    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @NotNull
    @Column(name = "service_code", nullable = false)
    private String serviceCode;

    @NotNull
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @ManyToOne
    @JsonIgnoreProperties("stayBenefitServices")
    @ReadOnlyProperty
    @JsonIgnore
    private LengthOfStayBenefit lengthOfStayBenefit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStayBenefitId() {
        return stayBenefitId;
    }

    public StayBenefitService stayBenefitId(Long stayBenefitId) {
        this.stayBenefitId = stayBenefitId;
        return this;
    }

    public void setStayBenefitId(Long stayBenefitId) {
        this.stayBenefitId = stayBenefitId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public StayBenefitService serviceId(Long serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public StayBenefitService serviceCode(String serviceCode) {
        this.serviceCode = serviceCode;
        return this;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public StayBenefitService serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public LengthOfStayBenefit getLengthOfStayBenefit() {
        return lengthOfStayBenefit;
    }

    public StayBenefitService lengthOfStayBenefit(LengthOfStayBenefit lengthOfStayBenefit) {
        this.lengthOfStayBenefit = lengthOfStayBenefit;
        return this;
    }

    public void setLengthOfStayBenefit(LengthOfStayBenefit lengthOfStayBenefit) {
        this.lengthOfStayBenefit = lengthOfStayBenefit;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StayBenefitService)) {
            return false;
        }
        return id != null && id.equals(((StayBenefitService) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StayBenefitService{" +
            "id=" + getId() +
            ", stayBenefitId=" + getStayBenefitId() +
            ", serviceId=" + getServiceId() +
            ", serviceCode='" + getServiceCode() + "'" +
            ", serviceName='" + getServiceName() + "'" +
            "}";
    }
}
