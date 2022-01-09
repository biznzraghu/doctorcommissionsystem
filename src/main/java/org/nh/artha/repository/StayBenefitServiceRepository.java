package org.nh.artha.repository;

import org.nh.artha.domain.StayBenefitService;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the StayBenefitService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StayBenefitServiceRepository extends JpaRepository<StayBenefitService, Long> {

    @Query(value = "select stayBenefit from  StayBenefitService  stayBenefit where lengthOfStayBenefit.id =:losId")
    List<StayBenefitService> getStayBenifetByLOSId(@Param("losId") Long losId);
}
