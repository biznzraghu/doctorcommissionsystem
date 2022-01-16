package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

import org.hibernate.annotations.Cache;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nh.artha.domain.enumeration.ConfigurationLevel;

import org.nh.artha.domain.enumeration.ConfigurationInputType;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A ConfigurationDefination.
 */
@Entity
@Table(name = "configuration_definition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_configurationdefination")
@Setting(settingPath = "/es/settings.json")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ConfigurationDefination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "key", nullable = false)
    private String key;

    @NotNull
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "input_value")
    private List<Map<String,String>> inputValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "allowable_override_level", nullable = false)
    private ConfigurationLevel allowableOverrideLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "allowable_display_level", nullable = false)
    private ConfigurationLevel allowableDisplayLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "input_type", nullable = false)
    private ConfigurationInputType inputType;

    @NotNull
    @Column(name = "validations", nullable = false)
    private String validations;

    @Column(name = "is_mandatory")
    private Boolean isMandatory;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("ConfigurationDefinations")
    private ConfigurationCategory configurationCategory;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public ConfigurationDefination key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ConfigurationDefination displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Map<String,String>> getInputValue() {
        return inputValue;
    }

    public ConfigurationDefination inputValue(List<Map<String,String>> inputValue) {
        this.inputValue = inputValue;
        return this;
    }

    public void setInputValue(List<Map<String,String>> inputValue) {
        this.inputValue = inputValue;
    }

    public ConfigurationLevel getAllowableOverrideLevel() {
        return allowableOverrideLevel;
    }

    public ConfigurationDefination allowableOverrideLevel(ConfigurationLevel allowableOverrideLevel) {
        this.allowableOverrideLevel = allowableOverrideLevel;
        return this;
    }

    public void setAllowableOverrideLevel(ConfigurationLevel allowableOverrideLevel) {
        this.allowableOverrideLevel = allowableOverrideLevel;
    }

    public ConfigurationLevel getAllowableDisplayLevel() {
        return allowableDisplayLevel;
    }

    public ConfigurationDefination allowableDisplayLevel(ConfigurationLevel allowableDisplayLevel) {
        this.allowableDisplayLevel = allowableDisplayLevel;
        return this;
    }

    public void setAllowableDisplayLevel(ConfigurationLevel allowableDisplayLevel) {
        this.allowableDisplayLevel = allowableDisplayLevel;
    }

    public ConfigurationInputType getInputType() {
        return inputType;
    }

    public ConfigurationDefination inputType(ConfigurationInputType inputType) {
        this.inputType = inputType;
        return this;
    }

    public void setInputType(ConfigurationInputType inputType) {
        this.inputType = inputType;
    }

    public String getValidations() {
        return validations;
    }

    public ConfigurationDefination validations(String validations) {
        this.validations = validations;
        return this;
    }

    public void setValidations(String validations) {
        this.validations = validations;
    }

    public Boolean isIsMandatory() {
        return isMandatory;
    }

    public ConfigurationDefination isMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
        return this;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public ConfigurationCategory getConfigurationCategory() {
        return configurationCategory;
    }

    public ConfigurationDefination configurationCategory(ConfigurationCategory ConfigurationCategory) {
        this.configurationCategory = ConfigurationCategory;
        return this;
    }

    public void setConfigurationCategory(ConfigurationCategory ConfigurationCategory) {
        this.configurationCategory = ConfigurationCategory;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigurationDefination)) {
            return false;
        }
        return id != null && id.equals(((ConfigurationDefination) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConfigurationDefination{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", inputValue='" + getInputValue() + "'" +
            ", allowableOverrideLevel='" + getAllowableOverrideLevel() + "'" +
            ", allowableDisplayLevel='" + getAllowableDisplayLevel() + "'" +
            ", inputType='" + getInputType() + "'" +
            ", validations='" + getValidations() + "'" +
            ", isMandatory='" + isIsMandatory() + "'" +
            "}";
    }
}
