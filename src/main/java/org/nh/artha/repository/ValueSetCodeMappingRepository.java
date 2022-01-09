package org.nh.artha.repository;
import org.nh.artha.domain.ValueSetCodeMapping;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ValueSetCodeMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValueSetCodeMappingRepository extends JpaRepository<ValueSetCodeMapping, Long> {

}
