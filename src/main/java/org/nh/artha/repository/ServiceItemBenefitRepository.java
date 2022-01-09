package org.nh.artha.repository;

import org.nh.artha.domain.ServiceItemBenefit;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the ServiceItemBenefit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceItemBenefitRepository extends JpaRepository<ServiceItemBenefit, Long> {

    @Query(value = "select * from service_item_benefit serviceItemBenefit where variable_payout_id = :variable_payout_id order by priority_order desc",nativeQuery = true)
    public List<ServiceItemBenefit> getServiceItemBenefitByVaraiblePayout(@Param("variable_payout_id") Long variablePayoutId);

    @Query(value = "select count(serviceItemBenefit.id) from service_item_benefit serviceItemBenefit where serviceItemBenefit.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from service_item_benefit serviceItemBenefit where serviceItemBenefit.iu_datetime between :fromDate AND :toDate order by serviceItemBenefit.iu_datetime", nativeQuery = true)
    List<ServiceItemBenefit> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query(value = "select * from service_item_benefit serviceItemBenefit where variable_payout_base_id =:variable_payout_base_id and  :version between valid_from and valid_to",nativeQuery = true)
    public List<ServiceItemBenefit> getServiceItemBenefitByVersion(@Param("variable_payout_base_id") Long variablePayoutId,@Param("version") Integer version);

    @Query(value = "DELETE FROM  service_item_benefit where id =:id",nativeQuery = true)
    @Modifying
    void deleteOne(@Param("id") Long id);


    @Transactional
    @Modifying
    @Query(value = "update  service_item_benefit set valid_to=:version  where variable_payout_base_id =:variable_payout_base_id and latest= true",nativeQuery = true)
    void update(@Param("variable_payout_base_id") Long variablePayoutId,@Param("version") Integer version);

    @Query(value = "select * from service_item_benefit serviceItemBenefit where variable_payout_base_id =:variable_payout_base_id and latest = true",nativeQuery = true)
    List<ServiceItemBenefit> byLatest(@Param("variable_payout_base_id")  Long variablePayoutId);

    @Query(value = "SELECT * from service_item_benefit  where plan_template_id =:templateId",nativeQuery = true)
    List<ServiceItemBenefit> getRulesByTemplate(@Param("templateId") Long templateId);
}
