package org.nh.artha.domain.dto;

import java.io.Serializable;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Nirbhay
 */
@Document(indexName = "organization", type="organization", createIndex = false)
public class OrganizationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.Keyword),
                    @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
            }
    )
    private String name;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.Keyword),
                    @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
            }
    )
    private String code;
    private Boolean active;
    @JsonIgnoreProperties("partOf")
    private OrganizationDTO partOf;

    private String shortName;

    public OrganizationDTO() {
    }

    public OrganizationDTO(Long id, String name, String code, Boolean active, OrganizationDTO partOf) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.active = active;
        this.partOf = partOf;
    }

    public OrganizationDTO getPartOf() {return partOf;}

    public void setPartOf(OrganizationDTO partOf) {this.partOf = partOf;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationDTO that = (OrganizationDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return active != null ? active.equals(that.active) : that.active == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrganizationDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", active=" + active +
                ", partOf=" + partOf +
                ", shortName=" + shortName +
                '}';
    }
}
