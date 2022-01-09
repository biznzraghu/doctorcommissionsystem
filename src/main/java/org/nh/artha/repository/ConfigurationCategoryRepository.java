package org.nh.artha.repository;
import org.nh.artha.domain.ConfigurationCategory;
import org.nh.artha.domain.ValueSetCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConfigurationCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationCategoryRepository extends JpaRepository<ValueSetCode, Long> {

}
