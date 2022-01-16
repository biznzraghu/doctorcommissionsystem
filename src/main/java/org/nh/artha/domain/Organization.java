package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_organization")
@Setting(settingPath = "/es/settings.json")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
           // @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String name;

    @NotNull
    @Column(name = "active", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
           // @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Boolean active;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "started_on")
    private LocalDate startedOn;

    @Column(name = "clinical")
    private Boolean clinical;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "website")
    private String website;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("organizations")
    @JsonIgnore
    private OrganizationType type;

    @ManyToOne
    @JsonIgnoreProperties("organizations")
    @JsonIgnore
    private Organization partOf;

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

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public Organization active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public Organization code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getStartedOn() {
        return startedOn;
    }

    public Organization startedOn(LocalDate startedOn) {
        this.startedOn = startedOn;
        return this;
    }

    public void setStartedOn(LocalDate startedOn) {
        this.startedOn = startedOn;
    }

    public Boolean isClinical() {
        return clinical;
    }

    public Organization clinical(Boolean clinical) {
        this.clinical = clinical;
        return this;
    }

    public void setClinical(Boolean clinical) {
        this.clinical = clinical;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public Organization licenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
        return this;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getWebsite() {
        return website;
    }

    public Organization website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public Organization description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrganizationType getType() {
        return type;
    }

    public Organization type(OrganizationType organizationType) {
        this.type = organizationType;
        return this;
    }

    public void setType(OrganizationType organizationType) {
        this.type = organizationType;
    }

    public Organization getPartOf() {
        return partOf;
    }

    public Organization partOf(Organization organization) {
        this.partOf = organization;
        return this;
    }

    public void setPartOf(Organization organization) {
        this.partOf = organization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return code != null && code.equals(((Organization) o).code);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + isActive() + "'" +
            ", code='" + getCode() + "'" +
            ", startedOn='" + getStartedOn() + "'" +
            ", clinical='" + isClinical() + "'" +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            ", website='" + getWebsite() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
