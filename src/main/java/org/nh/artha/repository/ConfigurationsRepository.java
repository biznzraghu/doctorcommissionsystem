package org.nh.artha.repository;
import org.nh.artha.domain.Configurations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Configurations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationsRepository extends JpaRepository<Configurations, Long> {

}
