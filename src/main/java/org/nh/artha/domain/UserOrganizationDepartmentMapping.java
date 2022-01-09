package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.nh.artha.domain.dto.DepartmentMasterDTO;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.util.List;

/**
 * A UserOrganizationDepartmentMapping.
 */
@Entity
@Table(name = "user_org_dept_mapping")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_userorganizationdepartmentmapping")
@Setting(settingPath = "/es/settings.json")
public class UserOrganizationDepartmentMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("userOrganizationDepartmentMappings")
    private UserMaster userMaster;

    @Type(type = "jsonb")
    @Column(name = "department",columnDefinition = "jsonb")
    private List<DepartmentMasterDTO> department;

    @ManyToOne
    @JsonIgnoreProperties("userOrganizationDepartmentMappings")
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserMaster getUserMaster() {
        return userMaster;
    }

    public UserOrganizationDepartmentMapping userMaster(UserMaster userMaster) {
        this.userMaster = userMaster;
        return this;
    }

    public void setUserMaster(UserMaster userMaster) {
        this.userMaster = userMaster;
    }

    public List<DepartmentMasterDTO> getDepartment() {
        return department;
    }

    public UserOrganizationDepartmentMapping department(List<DepartmentMasterDTO> department) {
        this.department = department;
        return this;
    }

    public void setDepartment(List<DepartmentMasterDTO> department) {
        this.department = department;
    }

    public Organization getOrganization() {
        return organization;
    }

    public UserOrganizationDepartmentMapping organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserOrganizationDepartmentMapping)) {
            return false;
        }
        return id != null && id.equals(((UserOrganizationDepartmentMapping) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserOrganizationDepartmentMapping{" +
            "id=" + getId() +
            "}";
    }
}
