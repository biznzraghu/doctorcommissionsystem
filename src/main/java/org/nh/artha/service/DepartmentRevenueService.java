package org.nh.artha.service;

import org.nh.artha.domain.DepartmentRevenue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DepartmentRevenue}.
 */
public interface DepartmentRevenueService {

    /**
     * Save a departmentRevenue.
     *
     * @param departmentRevenue the entity to save.
     * @return the persisted entity.
     */
    DepartmentRevenue save(DepartmentRevenue departmentRevenue);

    /**
     * Get all the departmentRevenues.
     *
     * @return the list of entities.
     */
    List<DepartmentRevenue> findAll();


    /**
     * Get the "id" departmentRevenue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepartmentRevenue> findOne(Long id);

    /**
     * Delete the "id" departmentRevenue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the departmentRevenue corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<DepartmentRevenue> search(String query);
    void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate);
}
