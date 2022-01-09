package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ConfigurationsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Configurations.class);
        Configurations configurations1 = new Configurations();
        configurations1.setId(1L);
        Configurations configurations2 = new Configurations();
        configurations2.setId(configurations1.getId());
        assertThat(configurations1).isEqualTo(configurations2);
        configurations2.setId(2L);
        assertThat(configurations1).isNotEqualTo(configurations2);
        configurations1.setId(null);
        assertThat(configurations1).isNotEqualTo(configurations2);
    }
}
