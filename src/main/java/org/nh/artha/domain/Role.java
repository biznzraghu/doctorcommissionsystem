package org.nh.artha.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.nh.artha.domain.dto.UserDTO;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_role")
@Setting(settingPath = "/es/settings.json")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
           // @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "active", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
         //   @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Boolean active;


    @Type(type = "jsonb")
    @Column(name = "created_by", nullable = false,columnDefinition = "jsonb")
    private UserDTO createdBy;

    @Column(name = "created_datetime", nullable = false)
    private LocalDateTime createdDatetime;

    @Type(type = "jsonb")
    @Column(name = "updated_by",columnDefinition = "jsonb")
    private UserDTO updatedBy;

    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;

    @Column(name = "part_of")
    private String partOf;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_privilege",joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Privilege> privileges = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "role_roles",
        joinColumns = @JoinColumn(name="primary_roleid", referencedColumnName="id"),
        inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName="id"))
    private Set<Role> roles = new HashSet<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Role name(String name) {
        this.name = name;
        return this;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Role description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return active;
    }

    public Role active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public Role createdBy(UserDTO createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public Role createdDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
        return this;
    }

    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public UserDTO getUpdatedBy() {
        return updatedBy;
    }

    public Role updatedBy(UserDTO updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(UserDTO updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedDatetime() {
        return updatedDatetime;
    }

    public Role updatedDatetime(LocalDateTime updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
        return this;
    }

    public void setUpdatedDatetime(LocalDateTime updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    public String getPartOf() {
        return partOf;
    }

    public Role partOf(String partOf) {
        this.partOf = partOf;
        return this;
    }

    public void setPartOf(String partOf) {
        this.partOf = partOf;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public Role privileges(Set<Privilege> privileges) {
        this.privileges = privileges;
        return this;
    }

    public Role addPrivileges(Privilege privilege) {
        this.privileges.add(privilege);
        //privilege.setRole(this);
        return this;
    }

    public Role removePrivileges(Privilege privilege) {
        this.privileges.remove(privilege);
        //privilege.setRole(null);
        return this;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Role roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Role addRoles(Role role) {
        this.roles.add(role);
        return this;
    }

    public Role removeRoles(Role role) {
        this.roles.remove(role);
        return this;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + isActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDatetime='" + getCreatedDatetime() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedDatetime='" + getUpdatedDatetime() + "'" +
            ", partOf='" + getPartOf() + "'" +
            "}";
    }
}
