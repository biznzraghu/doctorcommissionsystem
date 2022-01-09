package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import org.nh.artha.domain.dto.UserDTO;
import org.nh.artha.domain.enumeration.MisReportStatus;
import org.nh.repository.hibernate.type.JsonBinaryType;
import org.nh.repository.hibernate.type.JsonStringType;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * A MisReport.
 */
@Entity
@Table(name = "mis_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Document(indexName = "artha_misreport")
public class MisReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_date")
    private LocalDateTime scheduleDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Type(type = "jsonb")
    @Column(name = "created_by", columnDefinition = "jsonb")
    private UserDTO createdBy;

    @Type(type = "jsonb")
    @Column(name = "query_params",columnDefinition = "jsonb")
    private Map<String,String> queryParams;

    @Column(name = "error")
    private String error;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MisReportStatus status;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "is_duplicate")
    private Boolean isDuplicate = Boolean.FALSE;

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    @Column(name = "hash_value",nullable = false,unique = true)
    private String hashValue;

    @Column(name = "report_path")
    private String reportPath;

    @Column
    private String unitCode;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getScheduleDate() {
        return scheduleDate;
    }

    public MisReport scheduleDate(LocalDateTime scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }

    public void setScheduleDate(LocalDateTime scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public MisReport createdDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public  Map<String,String> getQueryParams() {
        return queryParams;
    }

    public MisReport queryParams( Map<String,String> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public void setQueryParams( Map<String,String> queryParams) {
        this.queryParams = queryParams;
    }

    public String getError() {
        return error;
    }

    public MisReport error(String error) {
        this.error = error;
        return this;
    }

    public void setError(String error) {
        this.error = error;
    }

    public MisReportStatus getStatus() {
        return status;
    }

    public MisReport status(MisReportStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(MisReportStatus status) {
        this.status = status;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getDuplicate() {
        return isDuplicate;
    }

    public void setDuplicate(Boolean duplicate) {
        isDuplicate = duplicate;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MisReport)) {
            return false;
        }
        return id != null && id.equals(((MisReport) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MisReport{" +
            "id=" + getId() +
            ", scheduleDate='" + getScheduleDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", queryParams='" + getQueryParams() + "'" +
            ", error='" + getError() + "'" +
            ", status='" + getStatus() + "'" +
            ", status='" + getHashValue() + "'" +
            "}";
    }
}
