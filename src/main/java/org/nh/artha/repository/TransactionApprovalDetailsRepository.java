package org.nh.artha.repository;

import org.nh.artha.domain.TransactionApprovalDetails;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TransactionApprovalDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionApprovalDetailsRepository extends JpaRepository<TransactionApprovalDetails, Long> {
}
