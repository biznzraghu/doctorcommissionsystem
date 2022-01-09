package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class PayoutRangeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayoutRange.class);
        PayoutRange payoutRange1 = new PayoutRange();
        payoutRange1.setId(1L);
        PayoutRange payoutRange2 = new PayoutRange();
        payoutRange2.setId(payoutRange1.getId());
        assertThat(payoutRange1).isEqualTo(payoutRange2);
        payoutRange2.setId(2L);
        assertThat(payoutRange1).isNotEqualTo(payoutRange2);
        payoutRange1.setId(null);
        assertThat(payoutRange1).isNotEqualTo(payoutRange2);
    }
}
