package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class UserMasterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMaster.class);
        UserMaster userMaster1 = new UserMaster();
        userMaster1.setId(1L);
        UserMaster userMaster2 = new UserMaster();
        userMaster2.setId(userMaster1.getId());
        assertThat(userMaster1).isEqualTo(userMaster2);
        userMaster2.setId(2L);
        assertThat(userMaster1).isNotEqualTo(userMaster2);
        userMaster1.setId(null);
        assertThat(userMaster1).isNotEqualTo(userMaster2);
    }
}
