package org.nh.artha.repository;
import org.nh.artha.domain.Department;
import org.nh.artha.domain.DepartmentRevenue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the DepartmentRevenue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRevenueRepository extends JpaRepository<DepartmentRevenue, Long> {
    @Query(value = "select count(department_revenue.id) from department_revenue department_revenue where department_revenue.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate")LocalDate toDate);


    @Query(value = "select * from department_revenue department_revenue where department_revenue.iu_datetime between :fromDate AND :toDate order by department_revenue.iu_datetime", nativeQuery = true)
    List<DepartmentRevenue> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}
