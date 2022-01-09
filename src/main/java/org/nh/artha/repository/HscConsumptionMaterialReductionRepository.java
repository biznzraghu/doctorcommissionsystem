package org.nh.artha.repository;

import org.nh.artha.domain.HscConsumptionMaterialReduction;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HscConsumptionMaterialReduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HscConsumptionMaterialReductionRepository extends JpaRepository<HscConsumptionMaterialReduction, Long> {
}
