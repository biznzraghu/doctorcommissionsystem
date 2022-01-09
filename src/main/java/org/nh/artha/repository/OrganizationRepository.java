package org.nh.artha.repository;
import org.nh.artha.domain.Organization;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query("select organization from Organization organization where code = :unitCode")
    Organization findOrganizationByUnitCode(@Param("unitCode") String unitCode);

    @Query(value = "select count(org.id) from organization org where org.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from organization org where org.iu_datetime between :fromDate AND :toDate order by org.iu_datetime", nativeQuery = true)
    List<Organization> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);


}
