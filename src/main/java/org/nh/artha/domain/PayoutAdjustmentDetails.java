package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.Type;
import org.nh.artha.domain.dto.DepartmentMasterDTO;
import org.nh.artha.domain.dto.UserDTO;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

/**
 * A PayoutAdjustmentDetails.
 */
@Entity
@Table(name = "payout_adjustment_details")
public class PayoutAdjustmentDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "year")
    private String year;

    @Column(name = "month")
    private String month;

    @Column(name = "sign")
    private String sign;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "contra_employee_id")
    private Long contraEmployeeId;

    @Column(name = "contra_employee_detail" ,columnDefinition = "jsonb" ,nullable = true)
    @Type(type = "jsonb")
    private UserDTO contraEmployeeDetail;

    @Column(name = "executed")
    private Boolean executed = Boolean.FALSE;

    @Column(name = "contra_department" ,columnDefinition = "jsonb" ,nullable = true)
    @Type(type = "jsonb")
    private DepartmentMasterDTO contraDepartment;

    @Column(name = "department_executed")
    private Boolean departmentExecuted = Boolean.FALSE;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getContraEmployeeId() {
        return contraEmployeeId;
    }

    public void setContraEmployeeId(Long contraEmployeeId) {
        this.contraEmployeeId = contraEmployeeId;
    }

    public UserDTO getContraEmployeeDetail() {
        return contraEmployeeDetail;
    }

    public void setContraEmployeeDetail(UserDTO contraEmployeeDetail) {
        this.contraEmployeeDetail = contraEmployeeDetail;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    public DepartmentMasterDTO getContraDepartment() {
        return contraDepartment;
    }

    public Boolean getDepartmentExecuted() {
        return departmentExecuted;
    }

    public void setDepartmentExecuted(Boolean departmentExecuted) {
        this.departmentExecuted = departmentExecuted;
    }

    public void setContraDepartment(DepartmentMasterDTO contraDepartment) {
        this.contraDepartment = contraDepartment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PayoutAdjustmentDetails)) {
            return false;
        }
        return id != null && id.equals(((PayoutAdjustmentDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PayoutAdjustmentDetails{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", year='" + getYear() + "'" +
            ", month='" + getMonth() + "'" +
            ", sign='" + getSign() + "'" +
            ", amount=" + getAmount() +
            ", contraEmployeeId=" + getContraEmployeeId() +
            "}";
    }
}
