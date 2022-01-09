package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.nh.repository.hibernate.type.JsonBinaryType;
import org.nh.repository.hibernate.type.JsonStringType;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * A UserDashboardWidget.
 */
@Entity
@Table(name = "user_dashboard_widget")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Document(indexName = "artha_userdashboardwidget")
@Setting(settingPath = "/es/settings.json")
public class UserDashboardWidget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "custom_properties", nullable = true)
    private Map<String, Object> customProperties;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "custom_settings", nullable = true)
    private Map<String, Object> customSettings;

    @Column(name = "created_on", nullable = true)
    private LocalDateTime createdOn;

    @Column(name = "updated_on", nullable = true)
    private LocalDateTime updatedOn;

    @NotNull
    @ManyToOne(optional = false)
    private UserDashboard userDashboard;

    @NotNull
    @ManyToOne(optional = false)
    private WidgetMaster widgetMaster;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    public UserDashboardWidget customProperties(Map<String, Object> customProperties) {
        this.customProperties = customProperties;
        return this;
    }

    public void setCustomProperties(Map<String, Object> customProperties) {
        this.customProperties = customProperties;
    }

    public Map<String, Object> getCustomSettings() {
        return customSettings;
    }

    public UserDashboardWidget customSettings(Map<String, Object> customSettings) {
        this.customSettings = customSettings;
        return this;
    }

    public void setCustomSettings(Map<String, Object> customSettings) {
        this.customSettings = customSettings;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public UserDashboardWidget createdOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public UserDashboardWidget updatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public UserDashboard getUserDashboard() {
        return userDashboard;
    }

    public UserDashboardWidget userDashboard(UserDashboard UserDashboard) {
        this.userDashboard = UserDashboard;
        return this;
    }

    public void setUserDashboard(UserDashboard UserDashboard) {
        this.userDashboard = UserDashboard;
    }

    public WidgetMaster getWidgetMaster() {
        return widgetMaster;
    }

    public UserDashboardWidget widgetMaster(WidgetMaster WidgetMaster) {
        this.widgetMaster = WidgetMaster;
        return this;
    }

    public void setWidgetMaster(WidgetMaster WidgetMaster) {
        this.widgetMaster = WidgetMaster;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDashboardWidget that = (UserDashboardWidget) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (customProperties != null ? !customProperties.equals(that.customProperties) : that.customProperties != null)
            return false;
        if (customSettings != null ? !customSettings.equals(that.customSettings) : that.customSettings != null)
            return false;
        if (createdOn != null ? !createdOn.equals(that.createdOn) : that.createdOn != null) return false;
        if (updatedOn != null ? !updatedOn.equals(that.updatedOn) : that.updatedOn != null) return false;
        if (userDashboard != null ? !userDashboard.equals(that.userDashboard) : that.userDashboard != null)
            return false;
        return widgetMaster != null ? widgetMaster.equals(that.widgetMaster) : that.widgetMaster == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (customProperties != null ? customProperties.hashCode() : 0);
        result = 31 * result + (customSettings != null ? customSettings.hashCode() : 0);
        result = 31 * result + (createdOn != null ? createdOn.hashCode() : 0);
        result = 31 * result + (updatedOn != null ? updatedOn.hashCode() : 0);
        result = 31 * result + (userDashboard != null ? userDashboard.hashCode() : 0);
        result = 31 * result + (widgetMaster != null ? widgetMaster.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserDashboardWidget{" +
            "id=" + id +
            ", customProperties=" + customProperties +
            ", customSettings=" + customSettings +
            ", createdOn=" + createdOn +
            ", updatedOn=" + updatedOn +
            ", userDashboard=" + userDashboard +
            ", widgetMaster=" + widgetMaster +
            '}';
    }
}
