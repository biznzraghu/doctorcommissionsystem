package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ApplicableConsultant.
 */
@Entity
@Table(name = "applicable_consultant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApplicableConsultant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "include")
    private Boolean include;

    @Column(name = "user_master_id")
    private Long userMasterId;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "employee_code")
    private String employeeCode;

    @ManyToOne
    @JsonIgnoreProperties("applicableConsultants")
    @JsonIgnore
    @ReadOnlyProperty
    private DepartmentPayout departmentPayout;

    public DepartmentPayout getDepartmentPayout() {
        return departmentPayout;
    }

    public ApplicableConsultant departmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
        return this;
    }

    public void setDepartmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isInclude() {
        return include;
    }

    public void setInclude(Boolean include) {
        this.include = include;
    }

    public Long getUserMasterId() {
        return userMasterId;
    }

    public void setUserMasterId(Long userMasterId) {
        this.userMasterId = userMasterId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicableConsultant)) {
            return false;
        }
        return id != null && id.equals(((ApplicableConsultant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ApplicableConsultant{" +
            "id=" + getId() +
            ", include='" + isInclude() + "'" +
            "}";
    }
}
