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
 * A HscConsumptionMaterialReduction.
 */
@Entity
@Table(name = "hsc_consumption")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HscConsumptionMaterialReduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hsc_id")
    private Long hscId;

    @Column(name = "hsc_name")
    private String hscName;


    @ManyToOne
    @JsonIgnoreProperties("hscConsumptionMaterialReductions")
    @JsonIgnore
    @ReadOnlyProperty
    private DepartmentPayout departmentPayout;

    public DepartmentPayout getDepartmentPayout() {
        return departmentPayout;
    }

    public HscConsumptionMaterialReduction departmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
        return this;
    }

    public void setDepartmentPayout(DepartmentPayout departmentPayout) {
        this.departmentPayout = departmentPayout;
    }
    public String getHscName() {
        return hscName;
    }

    public void setHscName(String hscName) {
        this.hscName = hscName;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHscId() {
        return hscId;
    }

    public void setHscId(Long hscId) {
        this.hscId = hscId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HscConsumptionMaterialReduction)) {
            return false;
        }
        return id != null && id.equals(((HscConsumptionMaterialReduction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HscConsumptionMaterialReduction{" +
            "id=" + getId() +
            ", hscId=" + getHscId() +
            "}";
    }
}
