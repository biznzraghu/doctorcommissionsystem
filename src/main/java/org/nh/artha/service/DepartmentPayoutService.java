package org.nh.artha.service;

import org.nh.artha.domain.DepartmentPayout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link DepartmentPayout}.
 */
public interface DepartmentPayoutService {

    /**
     * Save a departmentPayout.
     *
     * @param departmentPayout the entity to save.
     * @return the persisted entity.
     */
    DepartmentPayout save(DepartmentPayout departmentPayout);

    /**
     * Get all the departmentPayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepartmentPayout> findAll(Pageable pageable);

    /**
     * Get the "id" departmentPayout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    DepartmentPayout findOne(Long id);

    /**
     * Delete the "id" departmentPayout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the departmentPayout corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepartmentPayout> search(String query, Pageable pageable);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    DepartmentPayout update(DepartmentPayout departmentPayout);

    List<DepartmentPayout> saveAll(List<DepartmentPayout> departmentPayouts);

    DepartmentPayout saveAndUpdate(DepartmentPayout variablePayout);

    List<DepartmentPayout>  getDepartmentPayouts(String query, Pageable pageable);

    Map<String, String> exportDepartmentPayoutBreakUp(String unitCode, Integer year, Integer month,Integer departmentId) throws IOException;

    Map<String, String> exportDepartmentRevenueSummary(String unitCode, Integer year, Integer month,Integer departmentId) throws Exception;

    List<Integer> getDistinctVersion(String query) throws IOException;
    Map<String,String> exportDepartmentPayout(String query, Pageable pageable,Boolean latest) throws Exception;
}
