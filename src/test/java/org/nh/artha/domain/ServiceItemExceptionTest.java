package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ServiceItemExceptionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceItemException.class);
        ServiceItemException serviceItemException1 = new ServiceItemException();
        serviceItemException1.setId(1L);
        ServiceItemException serviceItemException2 = new ServiceItemException();
        serviceItemException2.setId(serviceItemException1.getId());
        assertThat(serviceItemException1).isEqualTo(serviceItemException2);
        serviceItemException2.setId(2L);
        assertThat(serviceItemException1).isNotEqualTo(serviceItemException2);
        serviceItemException1.setId(null);
        assertThat(serviceItemException1).isNotEqualTo(serviceItemException2);
    }
}
