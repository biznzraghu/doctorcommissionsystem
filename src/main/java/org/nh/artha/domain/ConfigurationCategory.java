package org.nh.artha.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.springframework.data.elasticsearch.annotations.Setting;

import static org.nh.artha.domain.ConfigurationCategory.VALUE_SET_CODE;

/**
 * A ConfigurationCategory.
 */
@Entity
@Table(name = "value_set_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "artha_configurationcategory")
@Setting(settingPath = "/es/settings.json")
@Where(clause = "value_set_id = (select t.id from value_set t where t.code = '"+VALUE_SET_CODE+"')")
public class ConfigurationCategory extends AbstractValueSetCode {

    private static final long serialVersionUID = 1L;

    public static final String VALUE_SET_CODE = "CONFIGURATION_CATEGORY";
}
