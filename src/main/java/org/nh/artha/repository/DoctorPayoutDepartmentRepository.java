package org.nh.artha.repository;

import org.nh.artha.domain.DoctorPayoutDepartment;
import org.nh.artha.domain.UserOrganizationDepartmentMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the DoctorPayoutDepartment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorPayoutDepartmentRepository extends JpaRepository<DoctorPayoutDepartment, Long> {
    @Query(value = "select * from doctor_payout_department  where unit_code=:unitCode and year=:year and month=:month and department_id=:departmentId order by id desc limit 1",nativeQuery = true)
    DoctorPayoutDepartment getDepartmentPayoutByYearMontUnitAndDepartId(@Param("unitCode") String unitCode,@Param("year") Integer year,
                                                                              @Param("month") Integer month,@Param("departmentId") Integer departmentId);

    @Query(value = "select * from doctor_payout_department  where unit_code=:unitCode and year=:year and month=:month and department_id=:departmentId order by id desc",nativeQuery = true)
   List<DoctorPayoutDepartment> getDepartmentPayoutByYearMonthUnitAndDepartIdGroupBy(@Param("unitCode") String unitCode,@Param("year") Integer year,
                                                                        @Param("month") Integer month,@Param("departmentId") Integer departmentId);

    @Query(value = "select * from doctor_payout_department  where unit_code=:unitCode and year=:year and month=:month order by id desc",nativeQuery = true)
    List<DoctorPayoutDepartment> getDepartmentPayoutByYearMonthANDUnitGroupBy(@Param("unitCode") String unitCode,@Param("year") Integer year,
                                                                                      @Param("month") Integer month);

    @Query(value = "select count(doctorPayoutDepartment.id) from doctor_payout_department doctorPayoutDepartment where doctorPayoutDepartment.invoice_date between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from doctor_payout_department doctorPayoutDepartment where doctorPayoutDepartment.invoice_date between :fromDate AND :toDate order by doctorPayoutDepartment.invoice_date", nativeQuery = true)
    List<DoctorPayoutDepartment> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}
