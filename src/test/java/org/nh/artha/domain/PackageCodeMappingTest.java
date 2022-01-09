package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class PackageCodeMappingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackageCodeMapping.class);
        PackageCodeMapping packageCodeMapping1 = new PackageCodeMapping();
        packageCodeMapping1.setId(1L);
        PackageCodeMapping packageCodeMapping2 = new PackageCodeMapping();
        packageCodeMapping2.setId(packageCodeMapping1.getId());
        assertThat(packageCodeMapping1).isEqualTo(packageCodeMapping2);
        packageCodeMapping2.setId(2L);
        assertThat(packageCodeMapping1).isNotEqualTo(packageCodeMapping2);
        packageCodeMapping1.setId(null);
        assertThat(packageCodeMapping1).isNotEqualTo(packageCodeMapping2);
    }
}
