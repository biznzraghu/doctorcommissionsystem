package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Entity;
import javax.persistence.Table;
import static org.nh.artha.domain.ItemGroup.VALUE_SET_CODE;

/**
 * A ItemGroup.
 */
@Entity
@Table(name = "value_set_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "artha_itemgroup")
@Setting(settingPath = "/es/settings.json")
@Where(clause = "value_set_id = (select t.id from value_set t where t.code = '"+VALUE_SET_CODE+"')")
public class ItemGroup extends AbstractValueSetCode {


    private static final long serialVersionUID = 1L;

    public static final String VALUE_SET_CODE = "ITEM_GROUP";
}
