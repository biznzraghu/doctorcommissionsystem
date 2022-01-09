package org.nh.artha.repository;

import org.nh.artha.domain.HealthcareServiceCenter;
import org.nh.artha.domain.Plan;
import org.nh.artha.domain.ServiceAnalysisStaging;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the Plan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HealthcareServiceCentreRepository extends JpaRepository<HealthcareServiceCenter, Long> {

    @Query(value = "select hsc from HealthcareServiceCenter hsc where organizationCode = :organizationCode  and active = true")
    List<HealthcareServiceCenter> hscByCode( @Param("organizationCode") String organizationCode);

    @Query(value = "select count(healthcare_service_center.id) from healthcare_service_center healthcare_service_center where healthcare_service_center.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from healthcare_service_center healthcare_service_center where healthcare_service_center.iu_datetime between :fromDate AND :toDate order by healthcare_service_center.iu_datetime", nativeQuery = true)
    List<HealthcareServiceCenter> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}
