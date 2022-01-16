package org.nh.artha.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

/**
 * A ValueSet.
 */
@Entity
@Table(name = "value_set")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_valueset")
@Setting(settingPath = "/es/settings.json")
public class ValueSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
          //  @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
         //   @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String name;

    @Column(name = "definition")
    private String definition;

    @NotNull
    @Column(name = "active", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
          //  @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Boolean active;

    @Column(name = "source")
    private String source;

    @Column(name = "defining_url")
    private String definingURL;

    @Column(name = "oid")
    private String oid;

    @Column(name = "system_url")
    private String systemURL;

    @Column(name = "system_oid")
    private String systemOID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public ValueSet code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public ValueSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public ValueSet definition(String definition) {
        this.definition = definition;
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Boolean isActive() {
        return active;
    }

    public ValueSet active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSource() {
        return source;
    }

    public ValueSet source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDefiningURL() {
        return definingURL;
    }

    public ValueSet definingURL(String definingURL) {
        this.definingURL = definingURL;
        return this;
    }

    public void setDefiningURL(String definingURL) {
        this.definingURL = definingURL;
    }

    public String getOid() {
        return oid;
    }

    public ValueSet oid(String oid) {
        this.oid = oid;
        return this;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSystemURL() {
        return systemURL;
    }

    public ValueSet systemURL(String systemURL) {
        this.systemURL = systemURL;
        return this;
    }

    public void setSystemURL(String systemURL) {
        this.systemURL = systemURL;
    }

    public String getSystemOID() {
        return systemOID;
    }

    public ValueSet systemOID(String systemOID) {
        this.systemOID = systemOID;
        return this;
    }

    public void setSystemOID(String systemOID) {
        this.systemOID = systemOID;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValueSet)) {
            return false;
        }
        return id != null && id.equals(((ValueSet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ValueSet{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", definition='" + getDefinition() + "'" +
            ", active='" + isActive() + "'" +
            ", source='" + getSource() + "'" +
            ", definingURL='" + getDefiningURL() + "'" +
            ", oid='" + getOid() + "'" +
            ", systemURL='" + getSystemURL() + "'" +
            ", systemOID='" + getSystemOID() + "'" +
            "}";
    }
}
