package org.nh.artha.repository;

import org.nh.artha.domain.ServiceItemException;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ServiceItemException entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceItemExceptionRepository extends JpaRepository<ServiceItemException, Long> {

}
