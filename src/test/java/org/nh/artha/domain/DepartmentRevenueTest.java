package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class DepartmentRevenueTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartmentRevenue.class);
        DepartmentRevenue departmentRevenue1 = new DepartmentRevenue();
        departmentRevenue1.setId(1L);
        DepartmentRevenue departmentRevenue2 = new DepartmentRevenue();
        departmentRevenue2.setId(departmentRevenue1.getId());
        assertThat(departmentRevenue1).isEqualTo(departmentRevenue2);
        departmentRevenue2.setId(2L);
        assertThat(departmentRevenue1).isNotEqualTo(departmentRevenue2);
        departmentRevenue1.setId(null);
        assertThat(departmentRevenue1).isNotEqualTo(departmentRevenue2);
    }
}
