package org.nh.artha.repository;
import org.nh.artha.domain.OrganizationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrganizationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, Long> {

}
