package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractValueSetCode implements Serializable {

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
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String code;

    @Column(name = "display")
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Keyword),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private String display;

    @NotNull
    @Column(name = "active", nullable = false)
    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "word_analyzer"),
        otherFields = {
            @InnerField(suffix = "raw", type = FieldType.Boolean),
            @InnerField(suffix = "sort", type = FieldType.ICU_Collation_Keyword)
        }
    )
    private Boolean active;

    @Column(name = "definition")
    private String definition;

    @Column(name = "source")
    private String source;

    @Column(name = "level")
    private String level;

    @Column(name = "comments")
    private String comments;

    @Column(name = "display_order")
    private Integer displayOrder;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "value_set_id")
    @Field(type = FieldType.Object)
    @ShallowReference
    private ValueSet valueSet;

    public AbstractValueSetCode() {
    }

    public AbstractValueSetCode(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public AbstractValueSetCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public AbstractValueSetCode display(String display) {
        this.display = display;
        return this;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Boolean isActive() {
        return active;
    }

    public AbstractValueSetCode active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDefinition() {
        return definition;
    }

    public AbstractValueSetCode definition(String definition) {
        this.definition = definition;
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getSource() {
        return source;
    }

    public AbstractValueSetCode source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLevel() {
        return level;
    }

    public AbstractValueSetCode level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getComments() {
        return comments;
    }

    public AbstractValueSetCode comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public AbstractValueSetCode displayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ValueSet getValueSet() {
        return valueSet;
    }

    public AbstractValueSetCode valueSet(ValueSet valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public void setValueSet(ValueSet valueSet) {
        this.valueSet = valueSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractValueSetCode valueSetCode = (AbstractValueSetCode) o;
        if (valueSetCode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, valueSetCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", display='" + display + "'" +
            ", active='" + active + "'" +
            ", definition='" + definition + "'" +
            ", source='" + source + "'" +
            ", level='" + level + "'" +
            ", comments='" + comments + "'" +
            ", displayOrder='" + displayOrder + "'" +
            '}';
    }
}
