package org.nh.artha.repository;
import org.nh.artha.domain.ServiceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {

}
