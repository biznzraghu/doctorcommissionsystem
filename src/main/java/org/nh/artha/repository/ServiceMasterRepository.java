package org.nh.artha.repository;
import org.nh.artha.domain.ServiceMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the ServiceMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceMasterRepository extends JpaRepository<ServiceMaster, Long> {
    @Query(value = "select count(serviceMaster.id) from service_master serviceMaster where serviceMaster.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from service_master serviceMaster where serviceMaster.iu_datetime between :fromDate AND :toDate order by serviceMaster.iu_datetime", nativeQuery = true)
    List<ServiceMaster> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}
