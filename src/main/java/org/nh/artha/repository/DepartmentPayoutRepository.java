package org.nh.artha.repository;

import org.nh.artha.domain.DepartmentPayout;

import org.nh.artha.domain.DocumentId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the DepartmentPayout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentPayoutRepository extends JpaRepository<DepartmentPayout, Long> {

    @Query(value = "select nextval('department_payout_id_seq')", nativeQuery = true)
    Long getId();

    @Modifying
    @Query("update DepartmentPayout deptPayout set deptPayout.latest=false where deptPayout.id=:id and deptPayout.latest=true")
    void updateLatest(@Param("id") Long id);

    @Modifying
    @Query("delete from DepartmentPayout deptPayout where deptPayout.id=:id")
    void delete(@Param("id") Long id);

    @Query("select deptPayout from DepartmentPayout deptPayout where deptPayout.id=:id")
    DepartmentPayout findOne(@Param("id") Long id);

    @Query(value = "select deptPayout from DepartmentPayout deptPayout where unitCode = :unitCode and departmentId = :departmentId")
    DepartmentPayout departmentPayoutByUnitAndDepartment(@Param("unitCode") String unitCode ,@Param("departmentId") Long departmentId);

    @Query(value = "select max(id) from department_payout deptPayout where unit_code = :unitCode and change_request_status = 'APPROVED' group by starting_version",nativeQuery = true)
    List<Long> fetchAllDepartmentBasedOnUnit(@Param("unitCode") String unitCode);

    @Query(value = "select count(departmentPayout.id) from department_payout departmentPayout where departmentPayout.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from department_payout departmentPayout where departmentPayout.iu_datetime between :fromDate AND :toDate order by departmentPayout.iu_datetime", nativeQuery = true)
    List<DepartmentPayout> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query(value = "SELECT * from department_payout  where starting_version  = :startingVersion order by id desc limit 1",nativeQuery = true)
    DepartmentPayout getCurrentVersion(@Param("startingVersion") Long startingVersion);

    @Query(value = "SELECT * from department_payout  where starting_version  = :startingVersion order by id desc",nativeQuery = true)
    List<DepartmentPayout> getAllDepartmentPayoutOnCurrentVersion(@Param("startingVersion") Long startingVersion);

    @Query(value = "select max(dept.id) from department_payout  dept CROSS JOIN applicable_consultant apt WHERE dept.id = apt.department_payout_id and dept.unit_code =:unitCode and apt.user_master_id=:userId and dept.change_request_status='APPROVED' group by dept.starting_version",nativeQuery = true)
    List<Long> getDepartmentPayoutConfigBasedOnEmployeeAndUnit(@Param("unitCode") String unitCode,@Param("userId") Long userId);

    @Query("SELECT max(id) from DepartmentPayout departmentPayout where departmentId  =:departmentId and unitCode  = :unitCode and changeRequestStatus = 'APPROVED' group by id")
    Long getLatestApprovedDepartmentPayoutId(@Param("unitCode") String unitCode,@Param("departmentId") Long departmentId);
}
