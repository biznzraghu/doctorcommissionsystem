package org.nh.artha.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import static org.nh.artha.domain.OrganizationType.VALUE_SET_CODE;
import java.io.Serializable;

/**
 * A OrganizationType.
 */
@Entity
@Table(name = "value_set_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "artha_organizationtype")
@Setting(settingPath = "/es/settings.json")
@Where(clause = "value_set_id = (select t.id from value_set t where t.code = '"+VALUE_SET_CODE+"')")
public class OrganizationType extends AbstractValueSetCode implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String VALUE_SET_CODE = "1.25.2.1.228";
}
