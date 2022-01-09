package org.nh.artha.repository;
import org.nh.artha.domain.DoctorPayout;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the DoctorPayout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorPayoutRepository extends JpaRepository<DoctorPayout, Long> {

    @Query("select doctorPayout from DoctorPayout doctorPayout where employeeId = :employeeId and year =:year and month =:month")
    DoctorPayout getDoctorByYearAndMonth(@Param("employeeId") Long employeeId, @Param("year") Integer year , @Param("month") Integer month);

    @Query("select doctorPayout from DoctorPayout doctorPayout where employeeId IN :employeeIds and year =:year and month =:month")
    List<DoctorPayout> getDoctorsListByEmpIds(@Param("employeeIds") List employeeIds,@Param("year") Integer year , @Param("month") Integer month);

    @Query(value = "select count(doctorPayout.id) from doctor_payout doctorPayout where doctorPayout.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from doctor_payout doctorPayout where doctorPayout.iu_datetime between :fromDate AND :toDate order by doctorPayout.iu_datetime", nativeQuery = true)
    List<DoctorPayout> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query("select doctorPayout from DoctorPayout doctorPayout where unitCode =:unitCode and year =:year and month =:month ")
    List<DoctorPayout> getAllDoctorByYearAndMonth( @Param("year") Integer year , @Param("month") Integer month,@Param("unitCode" )String unitCode);

    @Query(value = "select max(id) from doctor_payout  where employee_id =:id",nativeQuery = true)
    public Long getLatestDoctorPayout(@Param("id") Long id);

}
