package org.nh.artha.repository;

import org.nh.artha.domain.AdmissionDischargeStaging;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AdmissionDischargeStaging entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdmissionDischargeStagingRepository extends JpaRepository<AdmissionDischargeStaging, Long> {

}
