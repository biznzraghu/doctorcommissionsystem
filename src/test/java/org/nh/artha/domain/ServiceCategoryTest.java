package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ServiceCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceCategory.class);
        ServiceCategory serviceCategory1 = new ServiceCategory();
        serviceCategory1.setId(1L);
        ServiceCategory serviceCategory2 = new ServiceCategory();
        serviceCategory2.setId(serviceCategory1.getId());
        assertThat(serviceCategory1).isEqualTo(serviceCategory2);
        serviceCategory2.setId(2L);
        assertThat(serviceCategory1).isNotEqualTo(serviceCategory2);
        serviceCategory1.setId(null);
        assertThat(serviceCategory1).isNotEqualTo(serviceCategory2);
    }
}
