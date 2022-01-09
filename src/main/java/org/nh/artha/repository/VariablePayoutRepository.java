package org.nh.artha.repository;

import org.nh.artha.domain.UserMaster;
import org.nh.artha.domain.VariablePayout;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the VariablePayout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VariablePayoutRepository extends JpaRepository<VariablePayout, Long> {

    @Query("SELECT max(id) from VariablePayout variablePayout where unitCode  = :unitCode and changeRequestStatus = 'APPROVED' group by variablePayoutId")
    List<Long> getDoctorsBasedOnUnit(@Param("unitCode") String unitCode);

    @Query("SELECT max(id) from VariablePayout variablePayout where employeeId  = :employeeId group by variablePayoutId")
    Long getDoctorByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT variablePayout from VariablePayout variablePayout where employeeId  in :employeeIdList")
    List<VariablePayout> getDoctorListByEmployeeId(@Param("employeeIdList") List<Long> employeeId);

    @Query(value = "select count(variable_payout.id) from variable_payout variable_payout where variable_payout.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from variable_payout variable_payout where variable_payout.iu_datetime between :fromDate AND :toDate order by variable_payout.iu_datetime", nativeQuery = true)
    List<VariablePayout> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query(value = "SELECT * from variable_payout  where starting_version  = :startingVersion order by id desc limit 1",nativeQuery = true)
    VariablePayout getCurrentVersion(@Param("startingVersion") Long startingVersion);

    @Query(value = "SELECT * from variable_payout  where starting_version  = :startingVersion",nativeQuery = true)
    List<VariablePayout> getAllVersion(@Param("startingVersion") Long startingVersion);

    @Query(value = "SELECT * from variable_payout  where variable_payout_id  = :variablePayoutId order by id desc limit 1",nativeQuery = true)
    VariablePayout getPreviousVersion(@Param("variablePayoutId") Long variablePayoutId);

    @Query("SELECT max(id) from VariablePayout variablePayout where employeeId  in :employeeIds and unitCode  = :unitCode and changeRequestStatus = 'APPROVED' group by variablePayoutId")
    List<Long> getLatestVariablePayoutIds(@Param("unitCode") String unitCode,@Param("employeeIds") List<Long> employeeIds);

    @Query("SELECT max(id) from VariablePayout variablePayout where employeeId  =:employeeId and unitCode  = :unitCode and changeRequestStatus = 'APPROVED' group by variablePayoutId")
    Long getLatestApprovedVariablePayoutId(@Param("unitCode") String unitCode,@Param("employeeId") Long employeeId);
}
