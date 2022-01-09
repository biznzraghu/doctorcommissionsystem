package org.nh.artha.repository;
import org.nh.artha.domain.Privilege;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the Privilege entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    @Query(value = "select count(privilege.id) from privilege privilege where privilege.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate")LocalDate toDate);

    @Query(value = "select * from privilege privilege where privilege.iu_datetime between :fromDate AND :toDate order by privilege.iu_datetime", nativeQuery = true)
    List<Privilege> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}
