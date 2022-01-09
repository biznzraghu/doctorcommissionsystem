package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.nh.artha.domain.dto.SettingValue;
import org.nh.repository.hibernate.type.JsonBinaryType;
import org.nh.repository.hibernate.type.JsonStringType;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A UserPersonalisation.
 */
@Entity
@Table(name = "user_personalisation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Setting(settingPath = "/es/settings.json")
@Document(indexName = "artha_userpersonalisation")
public class UserPersonalisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_name", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String userName;

    @NotNull
    @Column(name = "module", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String module;

    @NotNull
    @Column(name = "feature", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String feature;

    @NotNull
    @Column(name = "subfeature", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String subFeature;

    @NotNull
    @Column(name = "setting_type", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String settingType;

    @NotNull
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "setting_value", nullable = false)
    private SettingValue settingValue;

    @Column(name = "created_date")
    @Field(type = FieldType.Date)
    private LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public UserPersonalisation userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModule() {
        return module;
    }

    public UserPersonalisation module(String module) {
        this.module = module;
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFeature() {
        return feature;
    }

    public UserPersonalisation feature(String feature) {
        this.feature = feature;
        return this;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getSettingType() {
        return settingType;
    }

    public UserPersonalisation settingType(String settingType) {
        this.settingType = settingType;
        return this;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    public SettingValue getSettingValue() {
        return settingValue;
    }

    public UserPersonalisation settingValue(SettingValue settingValue) {
        this.settingValue = settingValue;
        return this;
    }

    public void setSettingValue(SettingValue settingValue) {
        this.settingValue = settingValue;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public UserPersonalisation createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getSubFeature() {
        return subFeature;
    }

    public void setSubFeature(String subFeature) {
        this.subFeature = subFeature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPersonalisation userPersonalisation = (UserPersonalisation) o;
        if (userPersonalisation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userPersonalisation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserPersonalisation{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", module='" + module + '\'' +
            ", feature='" + feature + '\'' +
            ", subFeature='" + subFeature + '\'' +
            ", settingType='" + settingType + '\'' +
            ", settingValue=" + settingValue +
            ", createdDate=" + createdDate +
            '}';
    }
}
