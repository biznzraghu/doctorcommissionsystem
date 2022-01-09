package org.nh.artha.repository;
import org.nh.artha.domain.Feature;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the Feature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {


    @Query(value = "select m.name as modulename, f.type as featuretype, f.name as featurename,m.display_order as moduledisplayorder, f.display_order as featuredisplayorder, m.id as moduleid, f.menue_link as menulink from module m inner join feature f on f.module_id = m.id and f.active = true inner join privilege p on p.feature_id = f.id and p.active = true where m.id = ?1 group by m.name,f.type,f.name,m.display_order, f.display_order, m.id, f.menue_link order by m.display_order, f.display_order, f.name", nativeQuery = true)
    List findFeatureMenusForModule(Long moduleId);

    @Query(value = "select m.name as modulename, f.type as featuretype, f.name as featurename,m.display_order as moduledisplayorder, f.display_order as featuredisplayorder, m.id as moduleid, f.menue_link as menulink from module m inner join feature f on f.module_id = m.id and f.active = true inner join privilege p on p.feature_id = f.id and p.active = true group by m.name,f.type,f.name,m.display_order, f.display_order, m.id, f.menue_link order by m.display_order, f.display_order, f.name", nativeQuery = true)
    List findFeatureMenusForAllModule();

    @Query(value = "select count(feature.id) from feature feature where feature.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate")LocalDate toDate);

    @Query(value = "select * from feature feature where feature.iu_datetime between :fromDate AND :toDate order by feature.iu_datetime", nativeQuery = true)
    List<Feature> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}
