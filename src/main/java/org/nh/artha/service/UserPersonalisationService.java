package org.nh.artha.service;

import org.nh.artha.domain.UserPersonalisation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * Service Interface for managing UserPersonalisation.
 */
public interface UserPersonalisationService {

    /**
     * Save a userPersonalisation.
     *
     * @param userPersonalisation the entity to save
     * @return the persisted entity
     */
    UserPersonalisation save(UserPersonalisation userPersonalisation);

    /**
     * Get all the userPersonalisations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserPersonalisation> findAll(Pageable pageable);

    /**
     * Get the "id" userPersonalisation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    UserPersonalisation findOne(Long id);

    /**
     * Delete the "id" userPersonalisation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userPersonalisation corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserPersonalisation> search(String query, Pageable pageable);

    /**
     *
     */
    void deleteIndex();

    /**
     *
     */
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);
}
