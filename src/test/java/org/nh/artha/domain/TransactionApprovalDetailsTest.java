package org.nh.artha.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.nh.artha.web.rest.TestUtil;

public class TransactionApprovalDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionApprovalDetails.class);
        TransactionApprovalDetails transactionApprovalDetails1 = new TransactionApprovalDetails();
        transactionApprovalDetails1.setId(1L);
        TransactionApprovalDetails transactionApprovalDetails2 = new TransactionApprovalDetails();
        transactionApprovalDetails2.setId(transactionApprovalDetails1.getId());
        assertThat(transactionApprovalDetails1).isEqualTo(transactionApprovalDetails2);
        transactionApprovalDetails2.setId(2L);
        assertThat(transactionApprovalDetails1).isNotEqualTo(transactionApprovalDetails2);
        transactionApprovalDetails1.setId(null);
        assertThat(transactionApprovalDetails1).isNotEqualTo(transactionApprovalDetails2);
    }
}
