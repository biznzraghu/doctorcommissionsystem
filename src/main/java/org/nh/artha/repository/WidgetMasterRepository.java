package org.nh.artha.repository;


import org.nh.artha.domain.WidgetMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the WidgetMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WidgetMasterRepository extends JpaRepository<WidgetMaster, Long> {

    @Query(value = "select count(widgetmaster.id) from widget_master widgetmaster where widgetmaster.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


    @Query(value = "select * from widget_master widgetmaster where widgetmaster.iu_datetime between :fromDate AND :toDate order by widgetmaster.iu_datetime", nativeQuery = true)
    List<WidgetMaster> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}
