package org.nh.artha.service;

import org.nh.artha.domain.Feature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Feature}.
 */
public interface FeatureService {

    /**
     * Save a feature.
     *
     * @param feature the entity to save.
     * @return the persisted entity.
     */
    Feature save(Feature feature);

    /**
     * Get all the features.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Feature> findAll(Pageable pageable);


    /**
     * Get the "id" feature.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Feature> findOne(Long id);

    /**
     * Delete the "id" feature.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the feature corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Feature> search(String query, Pageable pageable);

    /***
     *
     * @param moduleId
     * @return
     */
    List findFeatureMenusForModule(Long moduleId);

    /***
     *
     * @return
     */
    List findFeatureMenusForAllModule();

    void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate);
}
