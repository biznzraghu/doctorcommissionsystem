package org.nh.artha.repository;
import org.nh.artha.domain.ConfigurationDefination;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConfigurationDefination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationDefinationRepository extends JpaRepository<ConfigurationDefination, Long> {

}
