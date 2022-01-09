package org.nh.artha.service;

import org.nh.artha.domain.DepartmentConsumptionMaterialReduction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DepartmentConsumptionMaterialReduction}.
 */
public interface DepartmentConsumptionMaterialReductionService {

    /**
     * Save a departmentConsumptionMaterialReduction.
     *
     * @param departmentConsumptionMaterialReduction the entity to save.
     * @return the persisted entity.
     */
    DepartmentConsumptionMaterialReduction save(DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction);

    /**
     * Get all the departmentConsumptionMaterialReductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepartmentConsumptionMaterialReduction> findAll(Pageable pageable);

    /**
     * Get the "id" departmentConsumptionMaterialReduction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepartmentConsumptionMaterialReduction> findOne(Long id);

    /**
     * Delete the "id" departmentConsumptionMaterialReduction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the departmentConsumptionMaterialReduction corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepartmentConsumptionMaterialReduction> search(String query, Pageable pageable);

    List<DepartmentConsumptionMaterialReduction> saveAll(List<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductions);
}
