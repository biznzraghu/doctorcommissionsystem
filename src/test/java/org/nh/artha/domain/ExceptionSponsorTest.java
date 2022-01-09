package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class ExceptionSponsorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExceptionSponsor.class);
        ExceptionSponsor exceptionSponsor1 = new ExceptionSponsor();
        exceptionSponsor1.setId(1L);
        ExceptionSponsor exceptionSponsor2 = new ExceptionSponsor();
        exceptionSponsor2.setId(exceptionSponsor1.getId());
        assertThat(exceptionSponsor1).isEqualTo(exceptionSponsor2);
        exceptionSponsor2.setId(2L);
        assertThat(exceptionSponsor1).isNotEqualTo(exceptionSponsor2);
        exceptionSponsor1.setId(null);
        assertThat(exceptionSponsor1).isNotEqualTo(exceptionSponsor2);
    }
}
