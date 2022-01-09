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
 * A ValueSetCodeMapping.
 */
@Entity
@Table(name = "value_set_code_mapping")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_valuesetcodemapping")
@Setting(settingPath = "/es/settings.json")
public class ValueSetCodeMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("valueSetCodeMappings")
    @JsonIgnore
    private ValueSetCode valueSetCode;

    @ManyToOne
    @JsonIgnoreProperties("valueSetCodeMappings")
    @JsonIgnore
    private ValueSetCode standardCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ValueSetCode getValueSetCode() {
        return valueSetCode;
    }

    public ValueSetCodeMapping valueSetCode(ValueSetCode ValueSetCode) {
        this.valueSetCode = ValueSetCode;
        return this;
    }

    public void setValueSetCode(ValueSetCode ValueSetCode) {
        this.valueSetCode = ValueSetCode;
    }

    public ValueSetCode getStandardCode() {
        return standardCode;
    }

    public ValueSetCodeMapping standardCode(ValueSetCode ValueSetCode) {
        this.standardCode = ValueSetCode;
        return this;
    }

    public void setStandardCode(ValueSetCode ValueSetCode) {
        this.standardCode = ValueSetCode;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValueSetCodeMapping)) {
            return false;
        }
        return id != null && id.equals(((ValueSetCodeMapping) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ValueSetCodeMapping{" +
            "id=" + getId() +
            "}";
    }
}
