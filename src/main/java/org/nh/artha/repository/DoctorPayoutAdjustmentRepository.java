package org.nh.artha.repository;

import org.nh.artha.domain.DoctorPayoutAdjustment;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DoctorPayoutAdjustment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorPayoutAdjustmentRepository extends JpaRepository<DoctorPayoutAdjustment, Long> {
}
