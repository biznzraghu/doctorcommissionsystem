package org.nh.artha.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A HealthcareServiceCenter.
 */
@Entity
@Table(name = "healthcare_service_center")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Document(indexName = "artha_healthcareservicecenter")
@Setting(settingPath = "/es/settings.json")
public class HealthcareServiceCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;


    @Column(name = "code")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
          //  @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String code;


    @Column(name = "name")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
          //  @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword),
        }
    )
    private String name;

    @Column(name = "full_name")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
          //  @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String fullName;


    @Column(name = "active")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
        }
    )
    private Boolean active;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "athma_part_of_id")
    private Long partOfId;

    @Column(name = "organization_code")
    private String organizationCode;

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public Long getPartOfId() {
        return partOfId;
    }

    public void setPartOfId(Long partOfId) {
        this.partOfId = partOfId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public HealthcareServiceCenter code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public HealthcareServiceCenter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public HealthcareServiceCenter active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }



    public String getFullName() { return fullName;  }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getActive() {
        return active;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HealthcareServiceCenter healthcareServiceCenter = (HealthcareServiceCenter) o;
        if (healthcareServiceCenter.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, healthcareServiceCenter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HealthcareServiceCenter{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", fullName='" + fullName + '\'' +
            ", active=" + active +
            '}';
    }
}
