package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Type;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.nh.artha.domain.dto.GroupMemberDto;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.List;

import org.nh.artha.domain.enumeration.Context;

/**
 * A Group.
 */
@Entity
@Table(name = "group_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_group")
@Setting(settingPath = "/es/settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "active", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Boolean active;


    @Column(name = "actual", nullable = false)
    private Boolean actual;

    @Enumerated(EnumType.STRING)
    @Column(name = "context")
    private Context context;


    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "members", nullable = false)
    @Field(type = FieldType.Nested, includeInParent = true )
    private List<GroupMemberDto> members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("groupMasters")
    private GroupType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("groupMasters")
    private Organization partOf;

    @Type(type = "jsonb")
    @Column(name = "created_by",columnDefinition = "jsonb")
    @ShallowReference
    private User createdBy;

    @Column(name = "modifiable", nullable = false)
    private Boolean modifiable = Boolean.TRUE;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Group name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Group code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isActive() {
        return active;
    }

    public Group active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isActual() {
        return actual;
    }

    public Group actual(Boolean actual) {
        this.actual = actual;
        return this;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public Context getContext() {
        return context;
    }

    public Group context(Context context) {
        this.context = context;
        return this;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<GroupMemberDto> getMembers() {
        return members;
    }

    public Group members(List<GroupMemberDto> members) {
        this.members = members;
        return this;
    }

    public void setMembers(List<GroupMemberDto> members) {
        this.members = members;
    }

    public GroupType getType() {
        return type;
    }

    public Group type(GroupType GroupType) {
        this.type = GroupType;
        return this;
    }

    public void setType(GroupType GroupType) {
        this.type = GroupType;
    }

    public Organization getPartOf() {
        return partOf;
    }

    public Group partOf(Organization Organization) {
        this.partOf = Organization;
        return this;
    }

    public void setPartOf(Organization Organization) {
        this.partOf = Organization;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getActual() {
        return actual;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getModifiable() {
        return modifiable;
    }

    public void setModifiable(Boolean modifiable) {
        this.modifiable = modifiable;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return id != null && id.equals(((Group) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", active='" + isActive() + "'" +
            ", actual='" + isActual() + "'" +
            ", context='" + getContext() + "'" +
            ", members='" + getMembers() + "'" +
            "}";
    }
}
