package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "department_hsc_consumption_staging")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DepartmentHscConsumptionStaging {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "patient_mrn")
    private String patientMrn;


    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "consumption_department_name")
    private String consumptionDepartmentName;

    @Column(name = "consumption_department_code")
    private String consumptionDepartmentCode;

    @Column(name = "consumption_hsc_name")
    private String consumptionHscName;

    @Column(name = "consumption_hsc_code")
    private String consumptionHscCode;

    @Column(name = "issued_hsc_name")
    private String issuedHscName;

    @Column(name = "issued_hsc_code")
    private String issuedHscCode;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "approver_name")
    private String approverName;

    @Column(name = "approver_code")
    private String approverCode;

    @Column(name = "issued_by_name")
    private String issuedByName;

    @Column(name = "issued_by_code")
    private String issuedByCode;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "batch_number")
    private String batchNumber;

    @Column(name = "uom")
    private String uom;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "executed")
    private Boolean executed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentHscConsumptionStaging)) {
            return false;
        }
        return id != null && id.equals(((DepartmentHscConsumptionStaging) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DepartmentHscConsumptionStaging{" +
            "id=" + id +
            ", documentNumber='" + documentNumber + '\'' +
            ", patientMrn='" + patientMrn + '\'' +
            ", patientName='" + patientName + '\'' +
            ", unitName='" + unitName + '\'' +
            ", unitCode='" + unitCode + '\'' +
            ", createdDate='" + createdDate + '\'' +
            ", consumptionDepartmentName='" + consumptionDepartmentName + '\'' +
            ", consumptionDepartmentCode='" + consumptionDepartmentCode + '\'' +
            ", consumptionHscName=" + consumptionHscName +
            ", consumptionHscCode='" + consumptionHscCode + '\'' +
            ", issuedHscName=" + issuedHscName +
            ", issuedHscCode=" + issuedHscCode +
            ", approvedDate='" + approvedDate + '\'' +
            ", approverName='" + approverName + '\'' +
            ", approverCode='" + approverCode + '\'' +
            ", issuedByName='" + issuedByName + '\'' +
            ", issuedByCode='" + issuedByCode + '\'' +
            ", itemName='" + itemName + '\'' +
            ", itemCode='" + itemCode + '\'' +
            ", batchNumber='" + batchNumber + '\'' +
            ", uom='" + uom + '\'' +
            ", quantity='" + quantity + '\'' +
            ", cost='" + cost + '\'' +
            ", totalCost='" + totalCost + '\'' +
            '}';
    }
}
