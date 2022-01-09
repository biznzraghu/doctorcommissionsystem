package org.nh.artha.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Locale;

@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private boolean activated = false;
    private String employeeNo;
    private String displayName;
    private String jobTitle;

    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login.toLowerCase(Locale.ENGLISH);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getActivated() {
        return this.activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getEmployeeNo() {
        return this.employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            User user = (User)o;
            return this.login.equals(user.login);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.login.hashCode();
    }

    public String toString() {
        return "User{login='" + this.login + '\'' + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName + '\'' + ", email='" + this.email + '\'' + ", activated='" + this.activated + '\'' + ", employeeNo='" + this.employeeNo + '\'' + ", displayName='" + this.displayName + '\'' + ", jobTitle='" + this.jobTitle + '\'' + "}";
    }
}
