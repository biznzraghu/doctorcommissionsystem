package org.nh.artha.repository;

import org.nh.artha.domain.UserPersonalisation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the UserPersonalisation entity.
 */
@SuppressWarnings("unused")
public interface UserPersonalisationRepository extends JpaRepository<UserPersonalisation, Long> {

    @Query(value = "select count(userper.id) from user_personalisation userper where userper.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


    @Query(value = "select * from user_personalisation userper where userper.iu_datetime between :fromDate AND :toDate order by userper.iu_datetime", nativeQuery = true)
    List<UserPersonalisation> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}
