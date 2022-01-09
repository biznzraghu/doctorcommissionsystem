package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class PackageMasterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackageMaster.class);
        PackageMaster packageMaster1 = new PackageMaster();
        packageMaster1.setId(1L);
        PackageMaster packageMaster2 = new PackageMaster();
        packageMaster2.setId(packageMaster1.getId());
        assertThat(packageMaster1).isEqualTo(packageMaster2);
        packageMaster2.setId(2L);
        assertThat(packageMaster1).isNotEqualTo(packageMaster2);
        packageMaster1.setId(null);
        assertThat(packageMaster1).isNotEqualTo(packageMaster2);
    }
}
