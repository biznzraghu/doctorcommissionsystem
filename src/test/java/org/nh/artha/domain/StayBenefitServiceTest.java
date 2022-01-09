package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class StayBenefitServiceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StayBenefitService.class);
        StayBenefitService stayBenefitService1 = new StayBenefitService();
        stayBenefitService1.setId(1L);
        StayBenefitService stayBenefitService2 = new StayBenefitService();
        stayBenefitService2.setId(stayBenefitService1.getId());
        assertThat(stayBenefitService1).isEqualTo(stayBenefitService2);
        stayBenefitService2.setId(2L);
        assertThat(stayBenefitService1).isNotEqualTo(stayBenefitService2);
        stayBenefitService1.setId(null);
        assertThat(stayBenefitService1).isNotEqualTo(stayBenefitService2);
    }
}
