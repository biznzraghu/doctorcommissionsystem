package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class PayoutAdjustmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayoutAdjustment.class);
        PayoutAdjustment payoutAdjustment1 = new PayoutAdjustment();
        payoutAdjustment1.setId(1L);
        PayoutAdjustment payoutAdjustment2 = new PayoutAdjustment();
        payoutAdjustment2.setId(payoutAdjustment1.getId());
        assertThat(payoutAdjustment1).isEqualTo(payoutAdjustment2);
        payoutAdjustment2.setId(2L);
        assertThat(payoutAdjustment1).isNotEqualTo(payoutAdjustment2);
        payoutAdjustment1.setId(null);
        assertThat(payoutAdjustment1).isNotEqualTo(payoutAdjustment2);
    }
}
