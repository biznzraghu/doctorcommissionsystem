package org.nh.artha.domain.dto;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Nirbhay on 3/7/18.
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
          //  @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String login;
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
          //  @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String displayName;
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
           // @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String employeeNo;

    public UserDTO(){}

    public UserDTO(Long id, String login){
        this.id = id;
        this.login = login;
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
        this.login = login;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public int hashCode() {
        return Objects.hash(id, login, displayName, employeeNo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (id != null ? !id.equals(userDTO.id) : userDTO.id != null) return false;
        if (login != null ? !login.equals(userDTO.login) : userDTO.login != null) return false;
        if (displayName != null ? !displayName.equals(userDTO.displayName) : userDTO.displayName != null) return false;
        return employeeNo != null ? employeeNo.equals(userDTO.employeeNo) : userDTO.employeeNo == null;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", displayName='" + displayName + '\'' +
            ", employeeNo='" + employeeNo + '\'' +
            '}';
    }
}
