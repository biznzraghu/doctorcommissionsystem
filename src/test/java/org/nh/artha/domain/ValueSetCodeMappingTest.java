package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ValueSetCodeMappingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValueSetCodeMapping.class);
        ValueSetCodeMapping valueSetCodeMapping1 = new ValueSetCodeMapping();
        valueSetCodeMapping1.setId(1L);
        ValueSetCodeMapping valueSetCodeMapping2 = new ValueSetCodeMapping();
        valueSetCodeMapping2.setId(valueSetCodeMapping1.getId());
        assertThat(valueSetCodeMapping1).isEqualTo(valueSetCodeMapping2);
        valueSetCodeMapping2.setId(2L);
        assertThat(valueSetCodeMapping1).isNotEqualTo(valueSetCodeMapping2);
        valueSetCodeMapping1.setId(null);
        assertThat(valueSetCodeMapping1).isNotEqualTo(valueSetCodeMapping2);
    }
}
