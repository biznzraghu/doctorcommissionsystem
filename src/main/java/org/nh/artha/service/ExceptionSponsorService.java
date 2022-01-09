package org.nh.artha.service;

import org.nh.artha.domain.ExceptionSponsor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ExceptionSponsor}.
 */
public interface ExceptionSponsorService {

    /**
     * Save a exceptionSponsor.
     *
     * @param exceptionSponsor the entity to save.
     * @return the persisted entity.
     */
    ExceptionSponsor save(ExceptionSponsor exceptionSponsor);

    /**
     * Get all the exceptionSponsors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExceptionSponsor> findAll(Pageable pageable);

    /**
     * Get the "id" exceptionSponsor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExceptionSponsor> findOne(Long id);

    /**
     * Delete the "id" exceptionSponsor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the exceptionSponsor corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExceptionSponsor> search(String query, Pageable pageable);
}
