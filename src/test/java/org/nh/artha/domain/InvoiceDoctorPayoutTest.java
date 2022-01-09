package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class InvoiceDoctorPayoutTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDoctorPayout.class);
        InvoiceDoctorPayout invoiceDoctorPayout1 = new InvoiceDoctorPayout();
        invoiceDoctorPayout1.setId(1L);
        InvoiceDoctorPayout invoiceDoctorPayout2 = new InvoiceDoctorPayout();
        invoiceDoctorPayout2.setId(invoiceDoctorPayout1.getId());
        assertThat(invoiceDoctorPayout1).isEqualTo(invoiceDoctorPayout2);
        invoiceDoctorPayout2.setId(2L);
        assertThat(invoiceDoctorPayout1).isNotEqualTo(invoiceDoctorPayout2);
        invoiceDoctorPayout1.setId(null);
        assertThat(invoiceDoctorPayout1).isNotEqualTo(invoiceDoctorPayout2);
    }
}
