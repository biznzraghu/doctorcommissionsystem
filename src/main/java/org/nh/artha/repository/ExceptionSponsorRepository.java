package org.nh.artha.repository;

import org.nh.artha.domain.ExceptionSponsor;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ExceptionSponsor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExceptionSponsorRepository extends JpaRepository<ExceptionSponsor, Long> {

}
