package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class DoctorPayoutLOSTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorPayoutLOS.class);
        DoctorPayoutLOS doctorPayoutLOS1 = new DoctorPayoutLOS();
        doctorPayoutLOS1.setId(1L);
        DoctorPayoutLOS doctorPayoutLOS2 = new DoctorPayoutLOS();
        doctorPayoutLOS2.setId(doctorPayoutLOS1.getId());
        assertThat(doctorPayoutLOS1).isEqualTo(doctorPayoutLOS2);
        doctorPayoutLOS2.setId(2L);
        assertThat(doctorPayoutLOS1).isNotEqualTo(doctorPayoutLOS2);
        doctorPayoutLOS1.setId(null);
        assertThat(doctorPayoutLOS1).isNotEqualTo(doctorPayoutLOS2);
    }
}
