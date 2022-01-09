package org.nh.artha.service;

import org.nh.artha.domain.DoctorPayout;
import org.nh.artha.domain.DoctorPayoutLOS;

import org.nh.artha.domain.LengthOfStayBenefit;
import org.nh.artha.domain.ServiceAnalysisStaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link DoctorPayoutLOS}.
 */
public interface DoctorPayoutLOSService {

    /**
     * Save a doctorPayoutLOS.
     *
     * @param doctorPayoutLOS the entity to save.
     * @return the persisted entity.
     */
    DoctorPayoutLOS save(DoctorPayoutLOS doctorPayoutLOS);

    /**
     * Get all the doctorPayoutLOS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoctorPayoutLOS> findAll(Pageable pageable);


    /**
     * Get the "id" doctorPayoutLOS.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DoctorPayoutLOS> findOne(Long id);

    /**
     * Delete the "id" doctorPayoutLOS.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the doctorPayoutLOS corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoctorPayoutLOS> search(String query, Pageable pageable);

    List<DoctorPayoutLOS> calculateDoctorPayoutLos(Map<LengthOfStayBenefit, List<ServiceAnalysisStaging>> lengthOfStayBenefitListMap, DoctorPayout doctorPayout);
}
