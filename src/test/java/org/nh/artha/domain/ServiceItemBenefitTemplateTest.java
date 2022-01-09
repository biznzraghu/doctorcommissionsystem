package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ServiceItemBenefitTemplateTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceItemBenefitTemplate.class);
        ServiceItemBenefitTemplate serviceItemBenefitTemplate1 = new ServiceItemBenefitTemplate();
        serviceItemBenefitTemplate1.setId(1L);
        ServiceItemBenefitTemplate serviceItemBenefitTemplate2 = new ServiceItemBenefitTemplate();
        serviceItemBenefitTemplate2.setId(serviceItemBenefitTemplate1.getId());
        assertThat(serviceItemBenefitTemplate1).isEqualTo(serviceItemBenefitTemplate2);
        serviceItemBenefitTemplate2.setId(2L);
        assertThat(serviceItemBenefitTemplate1).isNotEqualTo(serviceItemBenefitTemplate2);
        serviceItemBenefitTemplate1.setId(null);
        assertThat(serviceItemBenefitTemplate1).isNotEqualTo(serviceItemBenefitTemplate2);
    }
}
