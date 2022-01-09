package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class LengthOfStayBenefitTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LengthOfStayBenefit.class);
        LengthOfStayBenefit lengthOfStayBenefit1 = new LengthOfStayBenefit();
        lengthOfStayBenefit1.setId(1L);
        LengthOfStayBenefit lengthOfStayBenefit2 = new LengthOfStayBenefit();
        lengthOfStayBenefit2.setId(lengthOfStayBenefit1.getId());
        assertThat(lengthOfStayBenefit1).isEqualTo(lengthOfStayBenefit2);
        lengthOfStayBenefit2.setId(2L);
        assertThat(lengthOfStayBenefit1).isNotEqualTo(lengthOfStayBenefit2);
        lengthOfStayBenefit1.setId(null);
        assertThat(lengthOfStayBenefit1).isNotEqualTo(lengthOfStayBenefit2);
    }
}
