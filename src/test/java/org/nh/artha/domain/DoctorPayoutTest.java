package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class DoctorPayoutTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorPayout.class);
        DoctorPayout doctorPayout1 = new DoctorPayout();
        doctorPayout1.setId(1L);
        DoctorPayout doctorPayout2 = new DoctorPayout();
        doctorPayout2.setId(doctorPayout1.getId());
        assertThat(doctorPayout1).isEqualTo(doctorPayout2);
        doctorPayout2.setId(2L);
        assertThat(doctorPayout1).isNotEqualTo(doctorPayout2);
        doctorPayout1.setId(null);
        assertThat(doctorPayout1).isNotEqualTo(doctorPayout2);
    }
}
