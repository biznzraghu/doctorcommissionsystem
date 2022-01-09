package org.nh.artha.domain.dto;

import java.util.Objects;

public class RuleKeyDTO {
    private String visitType;
    private String type;
    private String typeBeneficiary;
    private String department;
    private String tariffClass;
    private String serviceType;

    public RuleKeyDTO(){};

    public RuleKeyDTO(String... args){
        this.type=args[1];
        this.visitType=args[2];
        this.tariffClass=args[3];
        this.typeBeneficiary=args[4];
        this.department=args[5];
        this.serviceType=args[6];
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeBeneficiary() {
        return typeBeneficiary;
    }

    public void setTypeBeneficiary(String typeBeneficiary) {
        this.typeBeneficiary = typeBeneficiary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTariffClass() {
        return tariffClass;
    }

    public void setTariffClass(String tariffClass) {
        this.tariffClass = tariffClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleKeyDTO that = (RuleKeyDTO) o;
        return Objects.equals(visitType, that.visitType) &&
            Objects.equals(type, that.type) &&
            Objects.equals(typeBeneficiary, that.typeBeneficiary) &&
            Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitType, type, typeBeneficiary, department);
    }

    @Override
    public String toString() {
        return "RuleKeyDTO{" +
            "visitType='" + visitType + '\'' +
            ", type='" + type + '\'' +
            ", typeBeneficiary='" + typeBeneficiary + '\'' +
            ", department='" + department + '\'' +
            '}';
    }
}
