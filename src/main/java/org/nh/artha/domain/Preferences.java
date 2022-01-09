package org.nh.artha.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    @ShallowReference
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    @ShallowReference
    private Organization organization;

    @NotNull
    @Column(name = "validate_timezone", nullable = false)
    private Boolean validateTimeZone = Boolean.TRUE;

    @PrePersist
    public void assignId() {
        this.id = this.user.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Preferences user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Boolean getValidateTimeZone() {
        return validateTimeZone;
    }

    public void setValidateTimeZone(Boolean validateTimeZone) {
        this.validateTimeZone = validateTimeZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Preferences preferences = (Preferences) o;
        if (preferences.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, preferences.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + id +
            '}';
    }
}
