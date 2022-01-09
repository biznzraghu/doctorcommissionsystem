package org.nh.artha.domain.dto;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ValueSetDTO.
 */

@Document(indexName = "valueset", type = "valueset", createIndex = false)
public class ValueSetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String code;

    private String display;

    public ValueSetDTO id(Long id) {
        this.id = id;
        return this;
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

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
