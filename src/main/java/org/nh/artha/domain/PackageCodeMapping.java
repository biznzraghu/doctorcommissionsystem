package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;

/**
 * A PackageCodeMapping.
 */
@Entity
@Table(name = "package_code_mapping")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_packagecodemapping")
@Setting(settingPath = "/es/settings.json")
public class PackageCodeMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageCodeMappings")
    @JsonIgnore
    private PackageMaster packageMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("packageCodeMappings")
    @JsonIgnore
    private ValueSetCode valueSetCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PackageMaster getPackageMaster() {
        return packageMaster;
    }

    public PackageCodeMapping packageMaster(PackageMaster PackageMaster) {
        this.packageMaster = PackageMaster;
        return this;
    }

    public void setPackageMaster(PackageMaster PackageMaster) {
        this.packageMaster = PackageMaster;
    }

    public ValueSetCode getValueSetCode() {
        return valueSetCode;
    }

    public PackageCodeMapping valueSetCode(ValueSetCode ValueSetCode) {
        this.valueSetCode = ValueSetCode;
        return this;
    }

    public void setValueSetCode(ValueSetCode ValueSetCode) {
        this.valueSetCode = ValueSetCode;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackageCodeMapping)) {
            return false;
        }
        return id != null && id.equals(((PackageCodeMapping) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PackageCodeMapping{" +
            "id=" + getId() +
            "}";
    }
}
