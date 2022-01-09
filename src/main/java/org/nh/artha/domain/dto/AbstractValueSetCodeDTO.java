package org.nh.artha.domain.dto;

import java.io.Serializable;
import java.util.Objects;


public abstract class AbstractValueSetCodeDTO implements Serializable {

    private Long id;

    private String code;

    private String display;

     private Boolean active;

    private String definition;

    private String source;

    private String level;

    private String comments;

    private Integer displayOrder;

    private ValueSetDTO valueSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public AbstractValueSetCodeDTO code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public AbstractValueSetCodeDTO display(String display) {
        this.display = display;
        return this;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Boolean isActive() {
        return active;
    }

    public AbstractValueSetCodeDTO active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDefinition() {
        return definition;
    }

    public AbstractValueSetCodeDTO definition(String definition) {
        this.definition = definition;
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getSource() {
        return source;
    }

    public AbstractValueSetCodeDTO source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLevel() {
        return level;
    }

    public AbstractValueSetCodeDTO level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getComments() {
        return comments;
    }

    public AbstractValueSetCodeDTO comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public AbstractValueSetCodeDTO displayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ValueSetDTO getValueSet() {
        return valueSet;
    }

    public AbstractValueSetCodeDTO valueSet(ValueSetDTO valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public void setValueSet(ValueSetDTO valueSet) {
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
        AbstractValueSetCodeDTO valueSetCode = (AbstractValueSetCodeDTO) o;
        if(valueSetCode.id == null || id == null) {
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
        return this.getClass().getSimpleName()+"{" +
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
