package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class DoctorPayoutDepartmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorPayoutDepartment.class);
        DoctorPayoutDepartment doctorPayoutDepartment1 = new DoctorPayoutDepartment();
        doctorPayoutDepartment1.setId(1L);
        DoctorPayoutDepartment doctorPayoutDepartment2 = new DoctorPayoutDepartment();
        doctorPayoutDepartment2.setId(doctorPayoutDepartment1.getId());
        assertThat(doctorPayoutDepartment1).isEqualTo(doctorPayoutDepartment2);
        doctorPayoutDepartment2.setId(2L);
        assertThat(doctorPayoutDepartment1).isNotEqualTo(doctorPayoutDepartment2);
        doctorPayoutDepartment1.setId(null);
        assertThat(doctorPayoutDepartment1).isNotEqualTo(doctorPayoutDepartment2);
    }
}
