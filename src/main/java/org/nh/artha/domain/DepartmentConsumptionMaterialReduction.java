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
 * A DepartmentConsumptionMaterialReduction.
 */
@Entity
@Table(name = "department_consumption")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DepartmentConsumptionMaterialReduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @ManyToOne
    @JsonIgnoreProperties("departmentConsumptionMaterialReductions")
    @JsonIgnore
    @ReadOnlyProperty
    private DepartmentPayout departmentPayout;

    public DepartmentPayout getDepartmentPayout() {
        return departmentPayout;
    }

    public DepartmentConsumptionMaterialReduction departmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
        return this;
    }

    public void setDepartmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    /*  public DepartmentPayout getDepartmentPayout() {
            return departmentPayout;
        }

        public void setDepartmentPayout(DepartmentPayout departmentPayout) {
            this.departmentPayout = departmentPayout;
        }
    */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentConsumptionMaterialReduction)) {
            return false;
        }
        return id != null && id.equals(((DepartmentConsumptionMaterialReduction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DepartmentConsumptionMaterialReduction{" +
            "id=" + getId() +
            "}";
    }
}
