package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class VariablePayoutTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VariablePayout.class);
        VariablePayout variablePayout1 = new VariablePayout();
        variablePayout1.setId(1L);
        VariablePayout variablePayout2 = new VariablePayout();
        variablePayout2.setId(variablePayout1.getId());
        assertThat(variablePayout1).isEqualTo(variablePayout2);
        variablePayout2.setId(2L);
        assertThat(variablePayout1).isNotEqualTo(variablePayout2);
        variablePayout1.setId(null);
        assertThat(variablePayout1).isNotEqualTo(variablePayout2);
    }
}
