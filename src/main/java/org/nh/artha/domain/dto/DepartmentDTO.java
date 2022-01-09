package org.nh.artha.domain.dto;

import java.io.Serializable;
import java.util.Objects;

public class DepartmentDTO implements Serializable {



    private String departmentCode;

    private String organizationCode;

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentDTO that = (DepartmentDTO) o;
        return Objects.equals(departmentCode, that.departmentCode) &&
            Objects.equals(organizationCode, that.organizationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentCode, organizationCode);
    }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "departmentCode='" + departmentCode + '\'' +
            ", organizationCode='" + organizationCode + '\'' +
            '}';
    }
}
