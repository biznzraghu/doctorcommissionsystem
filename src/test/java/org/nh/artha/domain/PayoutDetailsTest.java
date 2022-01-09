package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class PayoutDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayoutDetails.class);
        PayoutDetails payoutDetails1 = new PayoutDetails();
        payoutDetails1.setId(1L);
        PayoutDetails payoutDetails2 = new PayoutDetails();
        payoutDetails2.setId(payoutDetails1.getId());
        assertThat(payoutDetails1).isEqualTo(payoutDetails2);
        payoutDetails2.setId(2L);
        assertThat(payoutDetails1).isNotEqualTo(payoutDetails2);
        payoutDetails1.setId(null);
        assertThat(payoutDetails1).isNotEqualTo(payoutDetails2);
    }
}
