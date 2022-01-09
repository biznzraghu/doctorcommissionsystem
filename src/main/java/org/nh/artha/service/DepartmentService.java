package org.nh.artha.service;

import org.nh.artha.domain.Department;
import org.nh.artha.domain.Organization;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Department}.
 */
public interface DepartmentService {

    /**
     * Save a department.
     *
     * @param department the entity to save.
     * @return the persisted entity.
     */
    Department save(Department department);

    /**
     * Get all the departments.
     *
     * @return the list of entities.
     */
    List<Department> findAll();


    /**
     * Get the "id" department.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Department> findOne(Long id);

    /**
     * Delete the "id" department.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the department corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<Department> search(String query);

    void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate);

    List<Department> uploadDepartmentData(List<String[]> organizationArray) throws Exception;

    List<Department> saveAll(List<Department> departmentList);
}
