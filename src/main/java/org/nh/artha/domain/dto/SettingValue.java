package org.nh.artha.domain.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class SettingValue implements Serializable {
    private String name;
    private LinkedHashMap<Object,Object> value;

    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue( LinkedHashMap<Object,Object> value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettingValue that = (SettingValue) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SettingValue{" +
            "name='" + name + '\'' +
            ", value=" + value +
            '}';
    }
}
