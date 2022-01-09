package org.nh.artha.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.nh.artha.domain.enumeration.ConfigurationLevel;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * A Configurations.
 */
@Entity
@Table(name = "configurations", uniqueConstraints = @UniqueConstraint(columnNames = {"applicable_type","applicable_to"}))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_configurations")
@Setting(settingPath = "/es/settings.json")
public class Configurations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicable_type")
    private ConfigurationLevel applicableType;

    @NotNull
    @Column(name = "applicable_to", nullable = false)
    private Long applicableTo;

    @NotNull
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "configuration", nullable = false)
    private Map<String,String> configuration;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "applicable_entity", nullable = false)
    @Field(type = FieldType.Object)
    private Map<String, Object> applicableEntity;

    @Type(type = "jsonb")
    @Column(name = "created_by",columnDefinition = "jsonb")
    private User createdBy;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Type(type = "jsonb")
    @Column(name = "modified_by", columnDefinition = "jsonb")
    private User modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConfigurationLevel getApplicableType() {
        return applicableType;
    }

    public Configurations applicableType(ConfigurationLevel applicableType) {
        this.applicableType = applicableType;
        return this;
    }

    public void setApplicableType(ConfigurationLevel applicableType) {
        this.applicableType = applicableType;
    }

    public Long getApplicableTo() {
        return applicableTo;
    }

    public Configurations applicableTo(Long applicableTo) {
        this.applicableTo = applicableTo;
        return this;
    }

    public void setApplicableTo(Long applicableTo) {
        this.applicableTo = applicableTo;
    }

    public Map<String,String> getConfiguration() {
        return configuration;
    }

    public Configurations configuration(Map<String,String> configuration) {
        this.configuration = configuration;
        return this;
    }

    public void setConfiguration(Map<String,String> configuration) {
        this.configuration = configuration;
    }

    public Map<String, Object> getApplicableEntity() {
        return applicableEntity;
    }

    public Configurations applicableEntity(Map<String, Object> applicableEntity) {
        this.applicableEntity = applicableEntity;
        return this;
    }

    public void setApplicableEntity(Map<String, Object> applicableEntity) {
        this.applicableEntity = applicableEntity;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Configurations createdBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Configurations creationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public Configurations modifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public Configurations modifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Configurations)) {
            return false;
        }
        return id != null && id.equals(((Configurations) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Configurations{" +
            "id=" + getId() +
            ", applicableType='" + getApplicableType() + "'" +
            ", applicableTo=" + getApplicableTo() +
            ", configuration='" + getConfiguration() + "'" +
            ", applicableEntity='" + getApplicableEntity() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
