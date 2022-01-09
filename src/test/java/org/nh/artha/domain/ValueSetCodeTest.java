package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ValueSetCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValueSetCode.class);
        ValueSetCode valueSetCode1 = new ValueSetCode();
        valueSetCode1.setId(1L);
        ValueSetCode valueSetCode2 = new ValueSetCode();
        valueSetCode2.setId(valueSetCode1.getId());
        assertThat(valueSetCode1).isEqualTo(valueSetCode2);
        valueSetCode2.setId(2L);
        assertThat(valueSetCode1).isNotEqualTo(valueSetCode2);
        valueSetCode1.setId(null);
        assertThat(valueSetCode1).isNotEqualTo(valueSetCode2);
    }
}
