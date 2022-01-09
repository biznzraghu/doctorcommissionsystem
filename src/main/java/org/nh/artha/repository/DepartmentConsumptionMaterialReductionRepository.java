package org.nh.artha.repository;

import org.nh.artha.domain.DepartmentConsumptionMaterialReduction;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DepartmentConsumptionMaterialReduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentConsumptionMaterialReductionRepository extends JpaRepository<DepartmentConsumptionMaterialReduction, Long> {
}
