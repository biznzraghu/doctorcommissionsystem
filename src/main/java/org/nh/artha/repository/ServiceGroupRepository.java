package org.nh.artha.repository;
import org.nh.artha.domain.ServiceGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceGroupRepository extends JpaRepository<ServiceGroup, Long> {

}
