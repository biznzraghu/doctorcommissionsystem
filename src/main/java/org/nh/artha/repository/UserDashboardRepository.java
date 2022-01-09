package org.nh.artha.repository;


import org.nh.artha.domain.UserDashboard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the UserDashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDashboardRepository extends JpaRepository<UserDashboard, Long> {
    /**
     * @param userName
     * @return
     */
    List<UserDashboard> findAllByUserName(String userName);

    @Query(value = "select count(userdashboard.id) from user_dashboard userdashboard where userdashboard.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


    @Query(value = "select * from user_dashboard userdashboard where userdashboard.iu_datetime between :fromDate AND :toDate order by userdashboard.iu_datetime", nativeQuery = true)
    List<UserDashboard> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}
