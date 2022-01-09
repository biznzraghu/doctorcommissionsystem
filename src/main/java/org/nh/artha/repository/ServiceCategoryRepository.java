package org.nh.artha.repository;
import org.nh.artha.domain.ServiceCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

}
