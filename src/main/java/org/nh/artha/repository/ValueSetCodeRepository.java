package org.nh.artha.repository;
import org.nh.artha.domain.ValueSetCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the ValueSetCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValueSetCodeRepository extends JpaRepository<ValueSetCode, Long> {

    @Query(value = "select count(valuesetcode.id) from value_set_code valuesetcode where valuesetcode.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


    @Query(value = "select * from value_set_code valuesetcode where valuesetcode.iu_datetime between :fromDate AND :toDate order by valuesetcode.iu_datetime", nativeQuery = true)
    List<ValueSetCode> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}
