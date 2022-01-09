package org.nh.artha.service;

import org.nh.artha.domain.ApplicableConsultant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ApplicableConsultant}.
 */
public interface ApplicableConsultantService {

    /**
     * Save a applicableConsultant.
     *
     * @param applicableConsultant the entity to save.
     * @return the persisted entity.
     */
    ApplicableConsultant save(ApplicableConsultant applicableConsultant);

    /**
     * Get all the applicableConsultants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApplicableConsultant> findAll(Pageable pageable);

    /**
     * Get the "id" applicableConsultant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApplicableConsultant> findOne(Long id);

    /**
     * Delete the "id" applicableConsultant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the applicableConsultant corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApplicableConsultant> search(String query, Pageable pageable);

    List<ApplicableConsultant> saveAll(List<ApplicableConsultant> applicableConsultants);
}
