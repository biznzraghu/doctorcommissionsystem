package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class PrivilegeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Privilege.class);
        Privilege privilege1 = new Privilege();
        privilege1.setId(1L);
        Privilege privilege2 = new Privilege();
        privilege2.setId(privilege1.getId());
        assertThat(privilege1).isEqualTo(privilege2);
        privilege2.setId(2L);
        assertThat(privilege1).isNotEqualTo(privilege2);
        privilege1.setId(null);
        assertThat(privilege1).isNotEqualTo(privilege2);
    }
}
