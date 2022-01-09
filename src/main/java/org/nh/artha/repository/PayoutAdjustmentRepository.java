package org.nh.artha.repository;

import org.nh.artha.domain.PayoutAdjustment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data  repository for the PayoutAdjustment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayoutAdjustmentRepository extends JpaRepository<PayoutAdjustment, Long> {

    @Query(value = "select count(payoutAdjustment.id) from payout_adjustment payoutAdjustment where payoutAdjustment.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from payout_adjustment payoutAdjustment where payoutAdjustment.iu_datetime between :fromDate AND :toDate order by payoutAdjustment.iu_datetime", nativeQuery = true)
    List<PayoutAdjustment> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query(value = "select * from payout_adjustment  where created_date_time BETWEEN :createdDateFrom and  :createdDateTo AND employee_id=:employee_id AND status=:status AND unit_code=:unit_code order by id desc limit 1",nativeQuery = true)
    PayoutAdjustment getLatestEmployeePayout(@Param("createdDateFrom") LocalDateTime createdDateFrom, @Param("createdDateTo") LocalDateTime createdDateTo, @Param("employee_id") Long employee_id, @Param("unit_code") String unit_code , @Param("status") String status);

    @Query(value = "select * from payout_adjustment  where created_date_time BETWEEN :createdDateFrom and  :createdDateTo AND department_id=:department_id AND status=:status AND unit_code=:unit_code order by id desc limit 1",nativeQuery = true)
    PayoutAdjustment getLatestDepartmentPayout(@Param("createdDateFrom") LocalDateTime createdDateFrom, @Param("createdDateTo") LocalDateTime createdDateTo, @Param("department_id") Long department_id, @Param("unit_code") String unit_code , @Param("status") String status);
}
