package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class GroupTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupType.class);
        GroupType groupType1 = new GroupType();
        groupType1.setId(1L);
        GroupType groupType2 = new GroupType();
        groupType2.setId(groupType1.getId());
        assertThat(groupType1).isEqualTo(groupType2);
        groupType2.setId(2L);
        assertThat(groupType1).isNotEqualTo(groupType2);
        groupType1.setId(null);
        assertThat(groupType1).isNotEqualTo(groupType2);
    }
}
