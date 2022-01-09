package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class UserOrganizationRoleMappingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserOrganizationRoleMapping.class);
        UserOrganizationRoleMapping userOrganizationRoleMapping1 = new UserOrganizationRoleMapping();
        userOrganizationRoleMapping1.setId(1L);
        UserOrganizationRoleMapping userOrganizationRoleMapping2 = new UserOrganizationRoleMapping();
        userOrganizationRoleMapping2.setId(userOrganizationRoleMapping1.getId());
        assertThat(userOrganizationRoleMapping1).isEqualTo(userOrganizationRoleMapping2);
        userOrganizationRoleMapping2.setId(2L);
        assertThat(userOrganizationRoleMapping1).isNotEqualTo(userOrganizationRoleMapping2);
        userOrganizationRoleMapping1.setId(null);
        assertThat(userOrganizationRoleMapping1).isNotEqualTo(userOrganizationRoleMapping2);
    }
}
