package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ApplicableConsultantTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicableConsultant.class);
        ApplicableConsultant applicableConsultant1 = new ApplicableConsultant();
        applicableConsultant1.setId(1L);
        ApplicableConsultant applicableConsultant2 = new ApplicableConsultant();
        applicableConsultant2.setId(applicableConsultant1.getId());
        assertThat(applicableConsultant1).isEqualTo(applicableConsultant2);
        applicableConsultant2.setId(2L);
        assertThat(applicableConsultant1).isNotEqualTo(applicableConsultant2);
        applicableConsultant1.setId(null);
        assertThat(applicableConsultant1).isNotEqualTo(applicableConsultant2);
    }
}
