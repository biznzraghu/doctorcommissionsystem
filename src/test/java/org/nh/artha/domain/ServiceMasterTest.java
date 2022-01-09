package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ServiceMasterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceMaster.class);
        ServiceMaster serviceMaster1 = new ServiceMaster();
        serviceMaster1.setId(1L);
        ServiceMaster serviceMaster2 = new ServiceMaster();
        serviceMaster2.setId(serviceMaster1.getId());
        assertThat(serviceMaster1).isEqualTo(serviceMaster2);
        serviceMaster2.setId(2L);
        assertThat(serviceMaster1).isNotEqualTo(serviceMaster2);
        serviceMaster1.setId(null);
        assertThat(serviceMaster1).isNotEqualTo(serviceMaster2);
    }
}
