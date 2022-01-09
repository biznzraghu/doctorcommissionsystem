package org.nh.artha.service;

import org.nh.artha.domain.DepartmentRevenueBenefit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DepartmentRevenueBenefit}.
 */
public interface DepartmentRevenueBenefitService {

    /**
     * Save a departmentRevenueBenefit.
     *
     * @param departmentRevenueBenefit the entity to save.
     * @return the persisted entity.
     */
    DepartmentRevenueBenefit save(DepartmentRevenueBenefit departmentRevenueBenefit);

    /**
     * Get all the departmentRevenueBenefits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepartmentRevenueBenefit> findAll(Pageable pageable);

    /**
     * Get the "id" departmentRevenueBenefit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepartmentRevenueBenefit> findOne(Long id);

    /**
     * Delete the "id" departmentRevenueBenefit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the departmentRevenueBenefit corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepartmentRevenueBenefit> search(String query, Pageable pageable);

    List<DepartmentRevenueBenefit> saveAll(List<DepartmentRevenueBenefit> departmentRevenueBenefit);
}
