package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class UserOrganizationDepartmentMappingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserOrganizationDepartmentMapping.class);
        UserOrganizationDepartmentMapping userOrganizationDepartmentMapping1 = new UserOrganizationDepartmentMapping();
        userOrganizationDepartmentMapping1.setId(1L);
        UserOrganizationDepartmentMapping userOrganizationDepartmentMapping2 = new UserOrganizationDepartmentMapping();
        userOrganizationDepartmentMapping2.setId(userOrganizationDepartmentMapping1.getId());
        assertThat(userOrganizationDepartmentMapping1).isEqualTo(userOrganizationDepartmentMapping2);
        userOrganizationDepartmentMapping2.setId(2L);
        assertThat(userOrganizationDepartmentMapping1).isNotEqualTo(userOrganizationDepartmentMapping2);
        userOrganizationDepartmentMapping1.setId(null);
        assertThat(userOrganizationDepartmentMapping1).isNotEqualTo(userOrganizationDepartmentMapping2);
    }
}
