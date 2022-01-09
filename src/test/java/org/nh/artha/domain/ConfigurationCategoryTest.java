package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ConfigurationCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationCategory.class);
        ConfigurationCategory configurationCategory1 = new ConfigurationCategory();
        configurationCategory1.setId(1L);
        ConfigurationCategory configurationCategory2 = new ConfigurationCategory();
        configurationCategory2.setId(configurationCategory1.getId());
        assertThat(configurationCategory1).isEqualTo(configurationCategory2);
        configurationCategory2.setId(2L);
        assertThat(configurationCategory1).isNotEqualTo(configurationCategory2);
        configurationCategory1.setId(null);
        assertThat(configurationCategory1).isNotEqualTo(configurationCategory2);
    }
}
