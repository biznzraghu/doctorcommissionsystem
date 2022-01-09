package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ServiceTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceType.class);
        ServiceType serviceType1 = new ServiceType();
        serviceType1.setId(1L);
        ServiceType serviceType2 = new ServiceType();
        serviceType2.setId(serviceType1.getId());
        assertThat(serviceType1).isEqualTo(serviceType2);
        serviceType2.setId(2L);
        assertThat(serviceType1).isNotEqualTo(serviceType2);
        serviceType1.setId(null);
        assertThat(serviceType1).isNotEqualTo(serviceType2);
    }
}
