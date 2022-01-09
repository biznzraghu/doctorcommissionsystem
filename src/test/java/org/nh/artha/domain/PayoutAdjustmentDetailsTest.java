package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class PayoutAdjustmentDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayoutAdjustmentDetails.class);
        PayoutAdjustmentDetails payoutAdjustmentDetails1 = new PayoutAdjustmentDetails();
        payoutAdjustmentDetails1.setId(1L);
        PayoutAdjustmentDetails payoutAdjustmentDetails2 = new PayoutAdjustmentDetails();
        payoutAdjustmentDetails2.setId(payoutAdjustmentDetails1.getId());
        assertThat(payoutAdjustmentDetails1).isEqualTo(payoutAdjustmentDetails2);
        payoutAdjustmentDetails2.setId(2L);
        assertThat(payoutAdjustmentDetails1).isNotEqualTo(payoutAdjustmentDetails2);
        payoutAdjustmentDetails1.setId(null);
        assertThat(payoutAdjustmentDetails1).isNotEqualTo(payoutAdjustmentDetails2);
    }
}
