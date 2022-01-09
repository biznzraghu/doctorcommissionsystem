package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DoctorPayoutAdjustment.
 */
@Entity
@Table(name = "department_revenue_calculation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DepartmentRevenueCalculation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "dept_code")
    private String deptCode;

    @Column(name = "sum")
    private Double sum;

    @OneToMany(mappedBy = "departmentRevenueCalculation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DepartmentPayoutAdjustment> departmentPayoutAdjustments = new HashSet<>();

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "unit_code")
    private String unitCode;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Set<DepartmentPayoutAdjustment> getDepartmentPayoutAdjustments() {
        return departmentPayoutAdjustments;
    }

    public void setDepartmentPayoutAdjustments(Set<DepartmentPayoutAdjustment> departmentPayoutAdjustments) {
        this.departmentPayoutAdjustments = departmentPayoutAdjustments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentRevenueCalculation)) {
            return false;
        }
        return id != null && id.equals(((DepartmentRevenueCalculation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DoctorPayoutAdjustment{" +
            "id=" + getId() +
            ", deptCode='" + getDeptCode() + "'" +
            ", sum=" + getSum() +
            ", year=" + getYear() +
            "}";
    }
}
