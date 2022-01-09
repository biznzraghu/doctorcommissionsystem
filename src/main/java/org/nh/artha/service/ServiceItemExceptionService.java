package org.nh.artha.service;

import org.nh.artha.domain.ServiceItemException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ServiceItemException}.
 */
public interface ServiceItemExceptionService {

    /**
     * Save a serviceItemException.
     *
     * @param serviceItemException the entity to save.
     * @return the persisted entity.
     */
    ServiceItemException save(ServiceItemException serviceItemException);

    /**
     * Get all the serviceItemExceptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceItemException> findAll(Pageable pageable);

    /**
     * Get the "id" serviceItemException.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceItemException> findOne(Long id);

    /**
     * Delete the "id" serviceItemException.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the serviceItemException corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceItemException> search(String query, Pageable pageable);

    List<ServiceItemException> saveAll(List<ServiceItemException> serviceItemException);
}
