package org.nh.artha.repository;
import org.nh.artha.domain.ValueSet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the ValueSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValueSetRepository extends JpaRepository<ValueSet, Long> {

    @Query(value = "select count(valueset.id) from value_set valueset where valueset.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


    @Query(value = "select * from value_set valueset where valueset.iu_datetime between :fromDate AND :toDate order by valueset.iu_datetime", nativeQuery = true)
    List<ValueSet> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}
