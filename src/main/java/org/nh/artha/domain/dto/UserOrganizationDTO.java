package org.nh.artha.domain.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class UserOrganizationDTO implements Serializable {



    private String login;

    private String organizationCode;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserOrganizationDTO userOrganizationDTO = (UserOrganizationDTO) o;
        if (userOrganizationDTO.getLogin() == null || getLogin() == null) {
            return false;
        }
        return Objects.equals(getLogin(), userOrganizationDTO.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLogin());
    }

    @Override
    public String toString() {
        return "UserOrganizationDTO{" +
            "id=" + getLogin() +
          /*  ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", active='" + isActive() + "'" +*/
            "}";
    }
}
