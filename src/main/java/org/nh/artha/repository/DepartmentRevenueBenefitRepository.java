package org.nh.artha.repository;

import org.nh.artha.domain.DepartmentRevenueBenefit;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DepartmentRevenueBenefit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRevenueBenefitRepository extends JpaRepository<DepartmentRevenueBenefit, Long> {
}
