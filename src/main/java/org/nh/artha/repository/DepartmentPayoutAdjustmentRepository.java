package org.nh.artha.repository;

import org.nh.artha.domain.DepartmentPayoutAdjustment;
import org.nh.artha.domain.DoctorPayoutAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DoctorPayoutAdjustment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentPayoutAdjustmentRepository extends JpaRepository<DepartmentPayoutAdjustment, Long> {
}
