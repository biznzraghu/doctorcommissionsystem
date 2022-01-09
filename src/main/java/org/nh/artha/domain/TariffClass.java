package org.nh.artha.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

import static org.nh.artha.domain.TariffClass.VALUE_SET_CODE;

/**
 * A TariffClass.
 */
@Entity
@Table(name = "value_set_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "artha_tariffclass", createIndex = false)
@Setting(settingPath = "/es/settings.json")
@Where(clause = "value_set_id = (select t.id from value_set t where t.code = '"+VALUE_SET_CODE+"')")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TariffClass extends AbstractValueSetCode implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String VALUE_SET_CODE = "TARIFF_CLASS";
}
