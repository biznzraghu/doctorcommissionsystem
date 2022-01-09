package org.nh.artha.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Type;
import org.nh.artha.domain.dto.UserDTO;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A UserOrganizationRoleMapping.
 */
@Entity
@Table(name = "user_organization_role_mapping")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_userorganizationrolemapping")
@Setting(settingPath = "/es/settings.json")
public class UserOrganizationRoleMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @Field(type = FieldType.Object)
    private Role role;

    @ManyToOne(optional = false)
    @NotNull
    @Field(type = FieldType.Object)
    private UserMaster user;

    @ManyToOne(optional = false)
    @NotNull
    @Field(type = FieldType.Object)
    private Organization organization;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb" ,name = "created_by")
    private UserDTO createdBy;

    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "modified_by")
    private UserDTO modifiedBy;

    @Column(name = "modified_datetime")
    private LocalDateTime modifiedDatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public UserOrganizationRoleMapping role(Role role) {
        this.role = role;
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserMaster getUser() {
        return user;
    }

    public UserOrganizationRoleMapping user(UserMaster user) {
        this.user = user;
        return this;
    }

    public void setUser(UserMaster user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public UserOrganizationRoleMapping organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserDTO modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(LocalDateTime modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserOrganizationRoleMapping userOrganizationRoleMapping = (UserOrganizationRoleMapping) o;
        if (userOrganizationRoleMapping.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userOrganizationRoleMapping.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserOrganizationRoleMapping{" +
            "id=" + id +
            '}';
    }
}
