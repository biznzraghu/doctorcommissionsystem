package org.nh.artha.repository;

import org.nh.artha.domain.PayoutRange;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PayoutRange entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayoutRangeRepository extends JpaRepository<PayoutRange, Long> {
}
