package org.nh.artha.repository;

import org.nh.artha.domain.LengthOfStayBenefit;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the LengthOfStayBenefit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LengthOfStayBenefitRepository extends JpaRepository<LengthOfStayBenefit, Long> {

    @Query(value = "select lengthOfStayBenefit  from LengthOfStayBenefit lengthOfStayBenefit where variablePayout.id=:variablePayoutId")
    List<LengthOfStayBenefit> getLengthOfStayByVariablePayout(@Param("variablePayoutId")Long variablePayoutId);
}
