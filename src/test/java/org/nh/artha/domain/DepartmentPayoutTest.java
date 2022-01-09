package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class DepartmentPayoutTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartmentPayout.class);
        DepartmentPayout departmentPayout1 = new DepartmentPayout();
        departmentPayout1.setId(1L);
        DepartmentPayout departmentPayout2 = new DepartmentPayout();
        departmentPayout2.setId(departmentPayout1.getId());
        assertThat(departmentPayout1).isEqualTo(departmentPayout2);
        departmentPayout2.setId(2L);
        assertThat(departmentPayout1).isNotEqualTo(departmentPayout2);
        departmentPayout1.setId(null);
        assertThat(departmentPayout1).isNotEqualTo(departmentPayout2);
    }
}
