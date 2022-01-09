package org.nh.artha.repository;
import org.nh.artha.domain.PackageMaster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PackageMaster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackageMasterRepository extends JpaRepository<PackageMaster, Long> {

}
