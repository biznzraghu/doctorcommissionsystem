package org.nh.artha.service;

import org.nh.artha.domain.McrStaging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link McrStaging}.
 */
public interface McrStagingService {

    /**
     * Save a mcrStaging.
     *
     * @param mcrStaging the entity to save.
     * @return the persisted entity.
     */
    McrStaging save(McrStaging mcrStaging);

    /**
     * Get all the mcrStagings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<McrStaging> findAll(Pageable pageable);


    /**
     * Get the "id" mcrStaging.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<McrStaging> findOne(Long id);

    /**
     * Delete the "id" mcrStaging.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


}
