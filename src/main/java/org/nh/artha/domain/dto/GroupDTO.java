package org.nh.artha.domain.dto;

import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.List;


@Document(indexName = "group", type = "group", createIndex = false)
public class GroupDTO implements Serializable {

    private Long id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.Keyword),
                   // @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
            }
    )
    private String name;
    private String code;
    private Boolean active;
    private String context;
    private OrganizationDTO partOf;
    private GroupTypeDTO type;

    public GroupTypeDTO getType() {
        return type;
    }

    public void setType(GroupTypeDTO type) {
        this.type = type;
    }

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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public OrganizationDTO getPartOf() {
        return partOf;
    }

    public void setPartOf(OrganizationDTO partOf) {
        this.partOf = partOf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupDTO groupDTO = (GroupDTO) o;

        if (id != null ? !id.equals(groupDTO.id) : groupDTO.id != null) return false;
        if (name != null ? !name.equals(groupDTO.name) : groupDTO.name != null) return false;
        if (code != null ? !code.equals(groupDTO.code) : groupDTO.code != null) return false;
        if (active != null ? !active.equals(groupDTO.active) : groupDTO.active != null) return false;
        if (context != groupDTO.context) return false;
        return partOf != null ? partOf.equals(groupDTO.partOf) : groupDTO.partOf == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (partOf != null ? partOf.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", active=" + active +
                ", context=" + context +
                ", partOf=" + partOf +
                '}';
    }
}


