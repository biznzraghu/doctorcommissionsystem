package org.nh.artha.repository;
import org.nh.artha.domain.PackageComponent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PackageComponent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackageComponentRepository extends JpaRepository<PackageComponent, Long> {

}
