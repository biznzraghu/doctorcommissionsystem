package org.nh.artha.repository;
import org.nh.artha.domain.GroupType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GroupType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupTypeRepository extends JpaRepository<GroupType, Long> {

}
