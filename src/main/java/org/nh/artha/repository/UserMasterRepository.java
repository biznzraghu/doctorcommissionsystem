package org.nh.artha.repository;
import org.nh.artha.domain.UserMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the UserMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Long> {
    @Query(value = "select * from user_master user_master where user_master.employee_number = ?1",nativeQuery = true)
    UserMaster findByEmployeeNo(String employeeNumber);

    @Query(value = "select count(user_master.id) from user_master user_master where user_master.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from user_master user_master where user_master.iu_datetime between :fromDate AND :toDate order by user_master.iu_datetime", nativeQuery = true)
    List<UserMaster> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query(value = "select count(*) from user_master",nativeQuery = true)
    Long getTotalRecordCount();

}
