package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ServiceGroupTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceGroup.class);
        ServiceGroup serviceGroup1 = new ServiceGroup();
        serviceGroup1.setId(1L);
        ServiceGroup serviceGroup2 = new ServiceGroup();
        serviceGroup2.setId(serviceGroup1.getId());
        assertThat(serviceGroup1).isEqualTo(serviceGroup2);
        serviceGroup2.setId(2L);
        assertThat(serviceGroup1).isNotEqualTo(serviceGroup2);
        serviceGroup1.setId(null);
        assertThat(serviceGroup1).isNotEqualTo(serviceGroup2);
    }
}
