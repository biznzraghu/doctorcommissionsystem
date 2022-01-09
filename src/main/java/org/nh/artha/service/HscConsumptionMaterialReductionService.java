package org.nh.artha.service;

import org.nh.artha.domain.HscConsumptionMaterialReduction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link HscConsumptionMaterialReduction}.
 */
public interface HscConsumptionMaterialReductionService {

    /**
     * Save a hscConsumptionMaterialReduction.
     *
     * @param hscConsumptionMaterialReduction the entity to save.
     * @return the persisted entity.
     */
    HscConsumptionMaterialReduction save(HscConsumptionMaterialReduction hscConsumptionMaterialReduction);

    /**
     * Get all the hscConsumptionMaterialReductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HscConsumptionMaterialReduction> findAll(Pageable pageable);

    /**
     * Get the "id" hscConsumptionMaterialReduction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HscConsumptionMaterialReduction> findOne(Long id);

    /**
     * Delete the "id" hscConsumptionMaterialReduction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the hscConsumptionMaterialReduction corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HscConsumptionMaterialReduction> search(String query, Pageable pageable);

    List<HscConsumptionMaterialReduction> saveAll(List<HscConsumptionMaterialReduction> hscConsumptionMaterialReductions);
}
