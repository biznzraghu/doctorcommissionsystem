package org.nh.artha.domain.dto;

import org.nh.artha.domain.enumeration.ConfigurationLevel;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * A Configuration.
 */
@Document(indexName = "configuration", createIndex = false)
public class Configuration implements Serializable {

    private ConfigurationLevel applicableType;
    private Long applicableTo;
    private String applicableCode;
    private String key;
    private String value;
    private Integer level;

    public ConfigurationLevel getApplicableType() {
        return applicableType;
    }

    public void setApplicableType(ConfigurationLevel applicableType) {
        this.applicableType = applicableType;
    }

    public Long getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(Long applicableTo) {
        this.applicableTo = applicableTo;
    }

    public String getApplicableCode() {
        return applicableCode;
    }

    public void setApplicableCode(String applicableCode) {
        this.applicableCode = applicableCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Configuration applicableTo(Long applicableTo) {
        this.applicableTo = applicableTo;
        return this;
    }

    public Configuration applicableType(ConfigurationLevel applicableType) {
        this.applicableType = applicableType;
        return this;
    }

    public Configuration key(String key) {
        this.key = key;
        return this;
    }

    public Configuration value(String value) {
        this.value = value;
        return this;
    }

    public Configuration applicableCode(String applicableCode) {
        this.applicableCode = applicableCode;
        return this;
    }

    public Configuration level(Integer level) {
        this.level = level;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (applicableType != that.applicableType) return false;
        if (applicableTo != null ? !applicableTo.equals(that.applicableTo) : that.applicableTo != null) return false;
        if (applicableCode != null ? !applicableCode.equals(that.applicableCode) : that.applicableCode != null)
            return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return level != null ? level.equals(that.level) : that.level == null;
    }

    @Override
    public int hashCode() {
        int result = applicableType != null ? applicableType.hashCode() : 0;
        result = 31 * result + (applicableTo != null ? applicableTo.hashCode() : 0);
        result = 31 * result + (applicableCode != null ? applicableCode.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Configuration{" +
            "applicableType=" + applicableType +
            ", applicableTo=" + applicableTo +
            ", applicableCode='" + applicableCode + '\'' +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            ", level=" + level +
            '}';
    }
}
