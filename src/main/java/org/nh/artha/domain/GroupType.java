package org.nh.artha.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

import static org.nh.artha.domain.GroupType.VALUE_SET_CODE;

/**
 * A GroupType.
 */
@Entity
@Table(name = "value_set_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "value_set_id = (select t.id from value_set t where t.code = '"+VALUE_SET_CODE+"')")
public class GroupType extends AbstractValueSetCode {

    private static final long serialVersionUID = 1L;

    public static final String VALUE_SET_CODE = "GROUP_TYPE";

}

