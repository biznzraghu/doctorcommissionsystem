package org.nh.artha.repository;
import org.nh.artha.domain.ServiceItemBenefitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ServiceItemBenefitTemplate entity.
 */
@Repository
public interface ServiceItemBenefitTemplateRepository extends JpaRepository<ServiceItemBenefitTemplate, Long> {

    @Query(value = "select distinct serviceItemBenefitTemplate from ServiceItemBenefitTemplate serviceItemBenefitTemplate left join fetch serviceItemBenefitTemplate.variablePayouts",
        countQuery = "select count(distinct serviceItemBenefitTemplate) from ServiceItemBenefitTemplate serviceItemBenefitTemplate")
    Page<ServiceItemBenefitTemplate> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct serviceItemBenefitTemplate from ServiceItemBenefitTemplate serviceItemBenefitTemplate left join fetch serviceItemBenefitTemplate.variablePayouts")
    List<ServiceItemBenefitTemplate> findAllWithEagerRelationships();

    @Query("select serviceItemBenefitTemplate from ServiceItemBenefitTemplate serviceItemBenefitTemplate left join fetch serviceItemBenefitTemplate.variablePayouts where serviceItemBenefitTemplate.id =:id")
    Optional<ServiceItemBenefitTemplate> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value = "select count(serviceitembenefittemplate.id) from service_item_benefit_template serviceitembenefittemplate where serviceitembenefittemplate.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


    @Query(value = "select * from service_item_benefit_template serviceitembenefittemplate where serviceitembenefittemplate.iu_datetime between :fromDate AND :toDate order by serviceitembenefittemplate.iu_datetime", nativeQuery = true)
    List<ServiceItemBenefitTemplate> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}
