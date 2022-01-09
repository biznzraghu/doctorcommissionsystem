package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * A AdmissionDischargeStaging.
 */
@Entity
@Table(name = "admission_discharge_staging")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AdmissionDischargeStaging implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "patient_mrn")
    private String patientMrn;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "visit_code")
    private String visitCode;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "visit_number")
    private String visitNumber;

    @Column(name = "admission_id")
    private Long admissionId;

    @Column(name = "admission_number")
    private String admissionNumber;

    @Column(name = "admission_date")
    private LocalDateTime admissionDate;

    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    @Column(name = "tariff_class_code")
    private String tariffClassCode;

    @Column(name = "tariff_class_display")
    private String tariffClassDisplay;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "admitting_consultant_id")
    private Long admittingConsultantId;

    @Column(name = "admitting_consultant_display_name")
    private String admittingConsultantDisplayName;

    @Column(name = "admitting_department_id")
    private Long admittingDepartmentId;

    @Column(name = "admitting_department_code")
    private String admittingDepartmentCode;

    @Column(name = "admitting_department_name")
    private String admittingDepartmentName;

    @Column(name = "patient_status")
    private String patientStatus;

    @Column(name = "ward_code")
    private String wardCode;

    @Column(name = "ward_name")
    private String wardName;

    @Column(name = "bed_number")
    private String bedNumber;

    @Column(name = "mark_discharge_date")
    private LocalDateTime markDischargeDate;

    @Column(name = "discharge_date")
    private LocalDateTime dischargeDate;

    @Column(name = "discharge_reason_code")
    private String dischargeReasonCode;

    @Column(name = "discharge_reason_display")
    private String dischargeReasonDisplay;

    @Column(name = "admitting_user_id")
    private Long admittingUserId;

    @Column(name = "admitting_user_login")
    private String admittingUserLogin;

    @Column(name = "admitting_user_display_name")
    private String admittingUserDisplayName;

    @Column(name = "discharging_user_id")
    private Long dischargingUserId;

    @Column(name = "discharging_user_login")
    private String dischargingUserLogin;

    @Column(name = "discharging_user_display_name")
    private String dischargingUserDisplayName;

    @Column(name = "patient_type_code")
    private String patientTypeCode;

    @Column(name = "patient_type_name")
    private String patientTypeName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public AdmissionDischargeStaging documentId(Long documentId) {
        this.documentId = documentId;
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getPatientMrn() {
        return patientMrn;
    }

    public AdmissionDischargeStaging patientMrn(String patientMrn) {
        this.patientMrn = patientMrn;
        return this;
    }

    public void setPatientMrn(String patientMrn) {
        this.patientMrn = patientMrn;
    }

    public String getPatientName() {
        return patientName;
    }

    public AdmissionDischargeStaging patientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getVisitCode() {
        return visitCode;
    }

    public AdmissionDischargeStaging visitCode(String visitCode) {
        this.visitCode = visitCode;
        return this;
    }

    public void setVisitCode(String visitCode) {
        this.visitCode = visitCode;
    }

    public String getVisitType() {
        return visitType;
    }

    public AdmissionDischargeStaging visitType(String visitType) {
        this.visitType = visitType;
        return this;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public AdmissionDischargeStaging visitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
        return this;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public Long getAdmissionId() {
        return admissionId;
    }

    public AdmissionDischargeStaging admissionId(Long admissionId) {
        this.admissionId = admissionId;
        return this;
    }

    public void setAdmissionId(Long admissionId) {
        this.admissionId = admissionId;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public AdmissionDischargeStaging admissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
        return this;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public AdmissionDischargeStaging admissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
        return this;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public AdmissionDischargeStaging receivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
        return this;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getTariffClassCode() {
        return tariffClassCode;
    }

    public AdmissionDischargeStaging tariffClassCode(String tariffClassCode) {
        this.tariffClassCode = tariffClassCode;
        return this;
    }

    public void setTariffClassCode(String tariffClassCode) {
        this.tariffClassCode = tariffClassCode;
    }

    public String getTariffClassDisplay() {
        return tariffClassDisplay;
    }

    public AdmissionDischargeStaging tariffClassDisplay(String tariffClassDisplay) {
        this.tariffClassDisplay = tariffClassDisplay;
        return this;
    }

    public void setTariffClassDisplay(String tariffClassDisplay) {
        this.tariffClassDisplay = tariffClassDisplay;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public AdmissionDischargeStaging categoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public AdmissionDischargeStaging categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public AdmissionDischargeStaging unitCode(String unitCode) {
        this.unitCode = unitCode;
        return this;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public AdmissionDischargeStaging unitName(String unitName) {
        this.unitName = unitName;
        return this;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getAdmittingConsultantId() {
        return admittingConsultantId;
    }

    public AdmissionDischargeStaging admittingConsultantId(Long admittingConsultantId) {
        this.admittingConsultantId = admittingConsultantId;
        return this;
    }

    public void setAdmittingConsultantId(Long admittingConsultantId) {
        this.admittingConsultantId = admittingConsultantId;
    }

    public String getAdmittingConsultantDisplayName() {
        return admittingConsultantDisplayName;
    }

    public AdmissionDischargeStaging admittingConsultantDisplayName(String admittingConsultantDisplayName) {
        this.admittingConsultantDisplayName = admittingConsultantDisplayName;
        return this;
    }

    public void setAdmittingConsultantDisplayName(String admittingConsultantDisplayName) {
        this.admittingConsultantDisplayName = admittingConsultantDisplayName;
    }

    public Long getAdmittingDepartmentId() {
        return admittingDepartmentId;
    }

    public AdmissionDischargeStaging admittingDepartmentId(Long admittingDepartmentId) {
        this.admittingDepartmentId = admittingDepartmentId;
        return this;
    }

    public void setAdmittingDepartmentId(Long admittingDepartmentId) {
        this.admittingDepartmentId = admittingDepartmentId;
    }

    public String getAdmittingDepartmentCode() {
        return admittingDepartmentCode;
    }

    public AdmissionDischargeStaging admittingDepartmentCode(String admittingDepartmentCode) {
        this.admittingDepartmentCode = admittingDepartmentCode;
        return this;
    }

    public void setAdmittingDepartmentCode(String admittingDepartmentCode) {
        this.admittingDepartmentCode = admittingDepartmentCode;
    }

    public String getAdmittingDepartmentName() {
        return admittingDepartmentName;
    }

    public AdmissionDischargeStaging admittingDepartmentName(String admittingDepartmentName) {
        this.admittingDepartmentName = admittingDepartmentName;
        return this;
    }

    public void setAdmittingDepartmentName(String admittingDepartmentName) {
        this.admittingDepartmentName = admittingDepartmentName;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public AdmissionDischargeStaging patientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
        return this;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getWardCode() {
        return wardCode;
    }

    public AdmissionDischargeStaging wardCode(String wardCode) {
        this.wardCode = wardCode;
        return this;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getWardName() {
        return wardName;
    }

    public AdmissionDischargeStaging wardName(String wardName) {
        this.wardName = wardName;
        return this;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public AdmissionDischargeStaging bedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
        return this;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public LocalDateTime getMarkDischargeDate() {
        return markDischargeDate;
    }

    public AdmissionDischargeStaging markDischargeDate(LocalDateTime markDischargeDate) {
        this.markDischargeDate = markDischargeDate;
        return this;
    }

    public void setMarkDischargeDate(LocalDateTime markDischargeDate) {
        this.markDischargeDate = markDischargeDate;
    }

    public LocalDateTime getDischargeDate() {
        return dischargeDate;
    }

    public AdmissionDischargeStaging dischargeDate(LocalDateTime dischargeDate) {
        this.dischargeDate = dischargeDate;
        return this;
    }

    public void setDischargeDate(LocalDateTime dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getDischargeReasonCode() {
        return dischargeReasonCode;
    }

    public AdmissionDischargeStaging dischargeReasonCode(String dischargeReasonCode) {
        this.dischargeReasonCode = dischargeReasonCode;
        return this;
    }

    public void setDischargeReasonCode(String dischargeReasonCode) {
        this.dischargeReasonCode = dischargeReasonCode;
    }

    public String getDischargeReasonDisplay() {
        return dischargeReasonDisplay;
    }

    public AdmissionDischargeStaging dischargeReasonDisplay(String dischargeReasonDisplay) {
        this.dischargeReasonDisplay = dischargeReasonDisplay;
        return this;
    }

    public void setDischargeReasonDisplay(String dischargeReasonDisplay) {
        this.dischargeReasonDisplay = dischargeReasonDisplay;
    }

    public Long getAdmittingUserId() {
        return admittingUserId;
    }

    public AdmissionDischargeStaging admittingUserId(Long admittingUserId) {
        this.admittingUserId = admittingUserId;
        return this;
    }

    public void setAdmittingUserId(Long admittingUserId) {
        this.admittingUserId = admittingUserId;
    }

    public String getAdmittingUserLogin() {
        return admittingUserLogin;
    }

    public AdmissionDischargeStaging admittingUserLogin(String admittingUserLogin) {
        this.admittingUserLogin = admittingUserLogin;
        return this;
    }

    public void setAdmittingUserLogin(String admittingUserLogin) {
        this.admittingUserLogin = admittingUserLogin;
    }

    public String getAdmittingUserDisplayName() {
        return admittingUserDisplayName;
    }

    public AdmissionDischargeStaging admittingUserDisplayName(String admittingUserDisplayName) {
        this.admittingUserDisplayName = admittingUserDisplayName;
        return this;
    }

    public void setAdmittingUserDisplayName(String admittingUserDisplayName) {
        this.admittingUserDisplayName = admittingUserDisplayName;
    }

    public Long getDischargingUserId() {
        return dischargingUserId;
    }

    public AdmissionDischargeStaging dischargingUserId(Long dischargingUserId) {
        this.dischargingUserId = dischargingUserId;
        return this;
    }

    public void setDischargingUserId(Long dischargingUserId) {
        this.dischargingUserId = dischargingUserId;
    }

    public String getDischargingUserLogin() {
        return dischargingUserLogin;
    }

    public AdmissionDischargeStaging dischargingUserLogin(String dischargingUserLogin) {
        this.dischargingUserLogin = dischargingUserLogin;
        return this;
    }

    public void setDischargingUserLogin(String dischargingUserLogin) {
        this.dischargingUserLogin = dischargingUserLogin;
    }

    public String getDischargingUserDisplayName() {
        return dischargingUserDisplayName;
    }

    public AdmissionDischargeStaging dischargingUserDisplayName(String dischargingUserDisplayName) {
        this.dischargingUserDisplayName = dischargingUserDisplayName;
        return this;
    }

    public void setDischargingUserDisplayName(String dischargingUserDisplayName) {
        this.dischargingUserDisplayName = dischargingUserDisplayName;
    }

    public String getPatientTypeCode() {
        return patientTypeCode;
    }

    public AdmissionDischargeStaging patientTypeCode(String patientTypeCode) {
        this.patientTypeCode = patientTypeCode;
        return this;
    }

    public void setPatientTypeCode(String patientTypeCode) {
        this.patientTypeCode = patientTypeCode;
    }

    public String getPatientTypeName() {
        return patientTypeName;
    }

    public AdmissionDischargeStaging patientTypeName(String patientTypeName) {
        this.patientTypeName = patientTypeName;
        return this;
    }

    public void setPatientTypeName(String patientTypeName) {
        this.patientTypeName = patientTypeName;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdmissionDischargeStaging)) {
            return false;
        }
        return id != null && id.equals(((AdmissionDischargeStaging) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AdmissionDischargeStaging{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", patientMrn='" + getPatientMrn() + "'" +
            ", patientName='" + getPatientName() + "'" +
            ", visitCode='" + getVisitCode() + "'" +
            ", visitType='" + getVisitType() + "'" +
            ", visitNumber='" + getVisitNumber() + "'" +
            ", admissionId=" + getAdmissionId() +
            ", admissionNumber='" + getAdmissionNumber() + "'" +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", tariffClassCode='" + getTariffClassCode() + "'" +
            ", tariffClassDisplay='" + getTariffClassDisplay() + "'" +
            ", categoryCode='" + getCategoryCode() + "'" +
            ", categoryName='" + getCategoryName() + "'" +
            ", unitCode='" + getUnitCode() + "'" +
            ", unitName='" + getUnitName() + "'" +
            ", admittingConsultantId=" + getAdmittingConsultantId() +
            ", admittingConsultantDisplayName='" + getAdmittingConsultantDisplayName() + "'" +
            ", admittingDepartmentId=" + getAdmittingDepartmentId() +
            ", admittingDepartmentCode='" + getAdmittingDepartmentCode() + "'" +
            ", admittingDepartmentName='" + getAdmittingDepartmentName() + "'" +
            ", patientStatus='" + getPatientStatus() + "'" +
            ", wardCode='" + getWardCode() + "'" +
            ", wardName='" + getWardName() + "'" +
            ", bedNumber='" + getBedNumber() + "'" +
            ", markDischargeDate='" + getMarkDischargeDate() + "'" +
            ", dischargeDate='" + getDischargeDate() + "'" +
            ", dischargeReasonCode='" + getDischargeReasonCode() + "'" +
            ", dischargeReasonDisplay='" + getDischargeReasonDisplay() + "'" +
            ", admittingUserId=" + getAdmittingUserId() +
            ", admittingUserLogin='" + getAdmittingUserLogin() + "'" +
            ", admittingUserDisplayName='" + getAdmittingUserDisplayName() + "'" +
            ", dischargingUserId=" + getDischargingUserId() +
            ", dischargingUserLogin='" + getDischargingUserLogin() + "'" +
            ", dischargingUserDisplayName='" + getDischargingUserDisplayName() + "'" +
            ", patientTypeCode='" + getPatientTypeCode() + "'" +
            ", patientTypeName='" + getPatientTypeName() + "'" +
            "}";
    }
}
