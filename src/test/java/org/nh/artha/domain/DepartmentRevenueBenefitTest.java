package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class DepartmentRevenueBenefitTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartmentRevenueBenefit.class);
        DepartmentRevenueBenefit departmentRevenueBenefit1 = new DepartmentRevenueBenefit();
        departmentRevenueBenefit1.setId(1L);
        DepartmentRevenueBenefit departmentRevenueBenefit2 = new DepartmentRevenueBenefit();
        departmentRevenueBenefit2.setId(departmentRevenueBenefit1.getId());
        assertThat(departmentRevenueBenefit1).isEqualTo(departmentRevenueBenefit2);
        departmentRevenueBenefit2.setId(2L);
        assertThat(departmentRevenueBenefit1).isNotEqualTo(departmentRevenueBenefit2);
        departmentRevenueBenefit1.setId(null);
        assertThat(departmentRevenueBenefit1).isNotEqualTo(departmentRevenueBenefit2);
    }
}
