package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ConfigurationDefinationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationDefination.class);
        ConfigurationDefination configurationdefination1 = new ConfigurationDefination();
        configurationdefination1.setId(1L);
        ConfigurationDefination configurationdefination2 = new ConfigurationDefination();
        configurationdefination2.setId(configurationdefination1.getId());
        assertThat(configurationdefination1).isEqualTo(configurationdefination2);
        configurationdefination2.setId(2L);
        assertThat(configurationdefination1).isNotEqualTo(configurationdefination2);
        configurationdefination1.setId(null);
        assertThat(configurationdefination1).isNotEqualTo(configurationdefination2);
    }
}
