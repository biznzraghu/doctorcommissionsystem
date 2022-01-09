package org.nh.artha.repository;

import org.nh.artha.domain.DepartmentPayoutAdjustment;
import org.nh.artha.domain.DepartmentRevenueCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the DoctorPayoutAdjustment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRevenueCalculationRepository extends JpaRepository<DepartmentRevenueCalculation, Long> {

    @Query(value = "select departmentRevCal from DepartmentRevenueCalculation departmentRevCal where deptCode = :deptCode")
    List<DepartmentRevenueCalculation> allDeptRevCal(@Param("deptCode") String deptCode);

    @Query("select departmentRevCal from DepartmentRevenueCalculation departmentRevCal where departmentId = :departmentId and year =:year and month =:month")
    DepartmentRevenueCalculation getDeptRevCalByYearAndMonth(@Param("departmentId") Long departmentId, @Param("year") Integer year , @Param("month") Integer month);

}
