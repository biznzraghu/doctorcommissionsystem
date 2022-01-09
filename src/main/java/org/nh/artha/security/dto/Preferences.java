package org.nh.artha.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class Preferences implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private User user;
    private Organization organization;

    public Preferences() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public Preferences user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public Preferences organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Preferences preferences = (Preferences) o;
            return preferences.id != null && this.id != null ? Objects.equals(this.id, preferences.id) : false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    public String toString() {
        return "Preferences{id=" + this.id + '}';
    }
}
