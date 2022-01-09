package org.nh.artha.service;

import org.nh.artha.domain.UserOrganizationRoleMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserOrganizationRoleMapping}.
 */
public interface UserOrganizationRoleMappingService {

    /**
     * Save a userOrganizationRoleMapping.
     *
     * @param userOrganizationRoleMapping the entity to save.
     * @return the persisted entity.
     */
    UserOrganizationRoleMapping save(UserOrganizationRoleMapping userOrganizationRoleMapping);

    /**
     * Get all the userOrganizationRoleMappings.
     *
     * @return the list of entities.
     */
    List<UserOrganizationRoleMapping> findAll();


    /**
     * Get the "id" userOrganizationRoleMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserOrganizationRoleMapping> findOne(Long id);

    /**
     * Delete the "id" userOrganizationRoleMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the userOrganizationRoleMapping corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<UserOrganizationRoleMapping> search(String query);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);
}
