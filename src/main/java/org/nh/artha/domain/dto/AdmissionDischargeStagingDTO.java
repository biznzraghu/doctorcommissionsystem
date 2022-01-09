package org.nh.artha.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class AdmissionDischargeStagingDTO implements Serializable {
    private Long id;

    private String patientMrn;

    private String patientName;

    private Long admissionId;

    private String admissionNumber;

    private LocalDateTime admissionDate;

    private LocalDateTime dischargeDate;

    private String unitCode;

    private String unitName;

    private Long sarId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientMrn() {
        return patientMrn;
    }

    public void setPatientMrn(String patientMrn) {
        this.patientMrn = patientMrn;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Long admissionId) {
        this.admissionId = admissionId;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDateTime getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDateTime dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getSarId() {
        return sarId;
    }

    public void setSarId(Long sarId) {
        this.sarId = sarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdmissionDischargeStagingDTO that = (AdmissionDischargeStagingDTO) o;
        return
            Objects.equals(patientMrn, that.patientMrn);
    }

    @Override
    public int hashCode() {
        return Objects.hash( patientMrn);
    }
}
