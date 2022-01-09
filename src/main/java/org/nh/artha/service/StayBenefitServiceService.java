package org.nh.artha.service;

import org.nh.artha.domain.StayBenefitService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link StayBenefitService}.
 */
public interface StayBenefitServiceService {

    /**
     * Save a stayBenefitService.
     *
     * @param stayBenefitService the entity to save.
     * @return the persisted entity.
     */
    StayBenefitService save(StayBenefitService stayBenefitService);

    /**
     * Get all the stayBenefitServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StayBenefitService> findAll(Pageable pageable);

    /**
     * Get the "id" stayBenefitService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StayBenefitService> findOne(Long id);

    /**
     * Delete the "id" stayBenefitService.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the stayBenefitService corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StayBenefitService> search(String query, Pageable pageable);

    List<StayBenefitService> saveAll(List<StayBenefitService> stayBenefitService);
}
