package org.nh.artha.repository;

import org.nh.artha.domain.PayoutDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the PayoutDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayoutDetailsRepository extends JpaRepository<PayoutDetails, Long> {

    @Query(value = "select count(Payout_details.id) from payout_details Payout_details where Payout_details.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from payout_details Payout_details where Payout_details.iu_datetime between :fromDate AND :toDate order by Payout_details.iu_datetime", nativeQuery = true)
    List<PayoutDetails> findByDateRangeSortById(@Param("fromDate")LocalDate fromDate, @Param("toDate")LocalDate toDate, Pageable pageable);
}
