package org.nh.artha.repository;
import org.nh.artha.domain.Department;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(value = "select count(department.id) from department department where department.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate")LocalDate toDate);

    @Query(value = "select * from department department where department.iu_datetime between :fromDate AND :toDate order by department.iu_datetime", nativeQuery = true)
    List<Department> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query(value = "select  department from Department department where code = :code")
    Department findDepartmentByCode(@Param("code") String code);

}
