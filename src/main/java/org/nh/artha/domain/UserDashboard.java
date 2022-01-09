package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A UserDashboard.
 */
@Entity
@Table(name = "user_dashboard")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "artha_userdashboard")
@Setting(settingPath = "/es/settings.json")
public class UserDashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @NotNull
    @Column(name = "dashboard_name", nullable = false)
    private String dashboardName;

    @Column(name = "dashboard_order")
    @Field(type = FieldType.Integer)
    private Integer dashboardOrder;

    @Column(name = "created_on", nullable = true)
    private LocalDateTime createdOn;

    @Column(name = "updated_on", nullable = true)
    private LocalDateTime updatedOn;

    /*@OneToMany(mappedBy = "userDashboard", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private Set<UserDashboardWidget> userDashboardWidgets = new HashSet<>();
*/
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public UserDashboard userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public UserDashboard dashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
        return this;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public Integer getDashboardOrder() {
        return dashboardOrder;
    }

    public UserDashboard dashboardOrder(Integer dashboardOrder) {
        this.dashboardOrder = dashboardOrder;
        return this;
    }

    public void setDashboardOrder(Integer dashboardOrder) {
        this.dashboardOrder = dashboardOrder;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public UserDashboard createdOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public UserDashboard updatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    /*public Set<UserDashboardWidget> getUserDashboardWidgets() {
        return userDashboardWidgets;
    }

    public UserDashboard userDashboardWidgets(Set<UserDashboardWidget> userDashboardWidgets) {
        this.userDashboardWidgets = userDashboardWidgets;
        return this;
    }

    public UserDashboard addUserDashboardWidget(UserDashboardWidget userDashboardWidget) {
        this.userDashboardWidgets.add(userDashboardWidget);
        userDashboardWidget.setUserDashboard(this);
        return this;
    }

    public UserDashboard removeUserDashboardWidget(UserDashboardWidget userDashboardWidget) {
        this.userDashboardWidgets.remove(userDashboardWidget);
        userDashboardWidget.setUserDashboard(null);
        return this;
    }

    public void setUserDashboardWidgets(Set<UserDashboardWidget> userDashboardWidgets) {
        this.userDashboardWidgets = userDashboardWidgets;
    }*/
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDashboard userDashboard = (UserDashboard) o;
        if (userDashboard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userDashboard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserDashboard{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", dashboardName='" + getDashboardName() + "'" +
            ", dashboardOrder='" + getDashboardOrder() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
