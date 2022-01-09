package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ServiceItemBenefitTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceItemBenefit.class);
        ServiceItemBenefit serviceItemBenefit1 = new ServiceItemBenefit();
        serviceItemBenefit1.setId(1L);
        ServiceItemBenefit serviceItemBenefit2 = new ServiceItemBenefit();
        serviceItemBenefit2.setId(serviceItemBenefit1.getId());
        assertThat(serviceItemBenefit1).isEqualTo(serviceItemBenefit2);
        serviceItemBenefit2.setId(2L);
        assertThat(serviceItemBenefit1).isNotEqualTo(serviceItemBenefit2);
        serviceItemBenefit1.setId(null);
        assertThat(serviceItemBenefit1).isNotEqualTo(serviceItemBenefit2);
    }
}
