package org.nh.artha.service;

import org.nh.artha.domain.Preferences;
import org.nh.artha.domain.UserOrganizationDepartmentMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserOrganizationDepartmentMapping}.
 */
public interface UserOrganizationDepartmentMappingService {

    /**
     * Save a userOrganizationDepartmentMapping.
     *
     * @param userOrganizationDepartmentMapping the entity to save.
     * @return the persisted entity.
     */
    UserOrganizationDepartmentMapping save(UserOrganizationDepartmentMapping userOrganizationDepartmentMapping);

    /**
     * Get all the userOrganizationDepartmentMappings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserOrganizationDepartmentMapping> findAll(Pageable pageable);

    /**
     * Get the "id" userOrganizationDepartmentMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserOrganizationDepartmentMapping> findOne(Long id);

    /**
     * Delete the "id" userOrganizationDepartmentMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the userOrganizationDepartmentMapping corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserOrganizationDepartmentMapping> search(String query, Pageable pageable);


    /**
     * doIndex for the userOrganizationDepartmentMapping corresponding to the query.
     *
     * @param pageNo
     * @param pageSize
     * @param fromDate
     * @param toDate
     */
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);


    /**
     * Save a userOrganizationDepartmentMapping.
     *
     * @param userOrganizationDepartmentMappingList
     * @return
     */
    List<UserOrganizationDepartmentMapping> saveAll(List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList);


    Preferences searchUserBasedOnOrganization(Long unitId);

    Map<String,String> exportApplicableConsultant(String query, Pageable pageable) throws Exception;
}
