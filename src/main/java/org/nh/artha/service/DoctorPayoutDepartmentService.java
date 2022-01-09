package org.nh.artha.service;

import org.nh.artha.domain.DoctorPayoutDepartment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link DoctorPayoutDepartment}.
 */
public interface DoctorPayoutDepartmentService {

    /**
     * Save a doctorPayoutDepartment.
     *
     * @param doctorPayoutDepartment the entity to save.
     * @return the persisted entity.
     */
    DoctorPayoutDepartment save(DoctorPayoutDepartment doctorPayoutDepartment);

    /**
     * Get all the doctorPayoutDepartments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoctorPayoutDepartment> findAll(Pageable pageable);


    /**
     * Get the "id" doctorPayoutDepartment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DoctorPayoutDepartment> findOne(Long id);

    /**
     * Delete the "id" doctorPayoutDepartment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the doctorPayoutDepartment corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoctorPayoutDepartment> search(String query, Pageable pageable);
    List<DoctorPayoutDepartment> saveAll(List<DoctorPayoutDepartment> doctorPayoutDepartments);
    Map<String,String> exportEmployeeWiseSummary(String unitCode, Integer year, Integer month,Integer departmentId) throws Exception;
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);
}
