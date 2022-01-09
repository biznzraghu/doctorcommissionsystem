package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class PackageComponentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackageComponent.class);
        PackageComponent packageComponent1 = new PackageComponent();
        packageComponent1.setId(1L);
        PackageComponent packageComponent2 = new PackageComponent();
        packageComponent2.setId(packageComponent1.getId());
        assertThat(packageComponent1).isEqualTo(packageComponent2);
        packageComponent2.setId(2L);
        assertThat(packageComponent1).isNotEqualTo(packageComponent2);
        packageComponent1.setId(null);
        assertThat(packageComponent1).isNotEqualTo(packageComponent2);
    }
}
