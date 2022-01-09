package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.nh.repository.hibernate.type.JsonBinaryType;
import org.nh.repository.hibernate.type.JsonStringType;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * A WidgetMaster.
 */
@Entity
@Table(name = "widget_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Document(indexName = "artha_widgetmaster")
@Setting(settingPath = "/es/settings.json")
public class WidgetMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

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
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "properties", nullable = true)
    private Map<String, Object> properties;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "settings", nullable = true)
    private Map<String, Object> settings;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public WidgetMaster code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public WidgetMaster name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public WidgetMaster title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public WidgetMaster description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public WidgetMaster module(String module) {
        this.module = module;
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Boolean isActive() {
        return active;
    }

    public WidgetMaster active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public WidgetMaster thumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public WidgetMaster properties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public WidgetMaster settings(Map<String, Object> settings) {
        this.settings = settings;
        return this;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WidgetMaster that = (WidgetMaster) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (module != null ? !module.equals(that.module) : that.module != null) return false;
        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (thumbnail != null ? !thumbnail.equals(that.thumbnail) : that.thumbnail != null) return false;
        if (properties != null ? !properties.equals(that.properties) : that.properties != null) return false;
        return settings != null ? settings.equals(that.settings) : that.settings == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (module != null ? module.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (settings != null ? settings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WidgetMaster{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", module='" + module + '\'' +
            ", active=" + active +
            ", thumbnail='" + thumbnail + '\'' +
            ", properties=" + properties +
            ", settings=" + settings +
            '}';
    }
}
