package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ValueSetTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValueSet.class);
        ValueSet valueSet1 = new ValueSet();
        valueSet1.setId(1L);
        ValueSet valueSet2 = new ValueSet();
        valueSet2.setId(valueSet1.getId());
        assertThat(valueSet1).isEqualTo(valueSet2);
        valueSet2.setId(2L);
        assertThat(valueSet1).isNotEqualTo(valueSet2);
        valueSet1.setId(null);
        assertThat(valueSet1).isNotEqualTo(valueSet2);
    }
}
