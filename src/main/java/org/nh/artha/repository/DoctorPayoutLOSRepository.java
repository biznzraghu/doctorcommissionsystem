package org.nh.artha.repository;
import org.nh.artha.domain.DoctorPayoutLOS;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Spring Data  repository for the DoctorPayoutLOS entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorPayoutLOSRepository extends JpaRepository<DoctorPayoutLOS, Long> {
    @Modifying
    @Query(value = "delete from DoctorPayoutLOS doctorPayoutLOS where doctorPayout.id=:doctorPayout")
    @Transactional
    void getLengthOfStayByVariablePayout(@Param("doctorPayout")Long doctorPayout);
}
