package org.nh.artha.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.nh.artha.domain.OrganizationType;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String code;
    private String name;
    private Boolean active;
    private OrganizationType type;
    private Organization partOf;
    private String fullName;

    public Organization() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Organization code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Organization getPartOf() {
        return this.partOf;
    }

    public Organization partOf(Organization partOf) {
        this.partOf = partOf;
        return this;
    }

    public void setPartOf(Organization partOf) {
        this.partOf = partOf;
    }

    public String getName() {
        return this.name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return this.active;
    }

    public Organization active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public OrganizationType getType() {
        return this.type;
    }

    public Organization type(OrganizationType type) {
        this.type = type;
        return this;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Organization organization = (Organization)o;
            return organization.id != null && this.id != null ? Objects.equals(this.id, organization.id) : false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    public String toString() {
        return "Organization{id=" + this.id + ", code='" + this.code + "', name='" + this.name + "', active='" + this.active + "', type='" + this.type + "', partOf='" + this.partOf + "', fullName='" + this.fullName + "'" + '}';
    }
}
