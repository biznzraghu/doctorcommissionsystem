package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class OrganizationTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationType.class);
        OrganizationType organizationType1 = new OrganizationType();
        organizationType1.setId(1L);
        OrganizationType organizationType2 = new OrganizationType();
        organizationType2.setId(organizationType1.getId());
        assertThat(organizationType1).isEqualTo(organizationType2);
        organizationType2.setId(2L);
        assertThat(organizationType1).isNotEqualTo(organizationType2);
        organizationType1.setId(null);
        assertThat(organizationType1).isNotEqualTo(organizationType2);
    }
}
