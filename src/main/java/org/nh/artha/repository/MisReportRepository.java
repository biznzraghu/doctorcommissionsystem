package org.nh.artha.repository;

import org.nh.artha.domain.MisReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the MisReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MisReportRepository extends JpaRepository<MisReport, Long> {

    @Query(value ="select misReport from MisReport misReport where misReport.hashValue = :hashValue ORDER BY misReport.scheduleDate DESC")
    List<MisReport> filterReportByHashValue(@Param("hashValue") String hashValue);

    @Query(value = "select count(mis.id) from mis_report mis where mis.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from mis_report mis where mis.iu_datetime between :fromDate AND :toDate order by mis.iu_datetime", nativeQuery = true)
    List<MisReport> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}
