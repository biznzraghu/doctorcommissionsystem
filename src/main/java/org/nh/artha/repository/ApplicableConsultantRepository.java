package org.nh.artha.repository;

import org.nh.artha.domain.ApplicableConsultant;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ApplicableConsultant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicableConsultantRepository extends JpaRepository<ApplicableConsultant, Long> {
}
