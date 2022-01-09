package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;

/**
 * A ValueSetCode.
 */
@Entity
@Table(name = "value_set_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_valuesetcode")
@Setting(settingPath = "/es/settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValueSetCode extends AbstractValueSetCode implements Serializable{

    private static final long serialVersionUID = 1L;

    public ValueSetCode() {
    }

    public ValueSetCode(Long id) {
        super(id);
    }
}
