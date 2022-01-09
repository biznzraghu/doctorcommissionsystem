package org.nh.artha.repository;
import org.nh.artha.domain.PackageCodeMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PackageCodeMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackageCodeMappingRepository extends JpaRepository<PackageCodeMapping, Long> {

}
