package org.nh.artha.service;

import org.nh.artha.domain.Privilege;
import org.nh.artha.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing {@link Role}.
 */
public interface RoleService {

    /**
     * Save a role.
     *
     * @param role the entity to save
     * @return the persisted entity
     */
    Role save(Role role);

    /**
     *  Get all the roles.
     *
     *  @return the list of entities
     */
    List<Role> findAll();

    /**
     *  Get the "id" role.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Role findOne(Long id);

    /**
     *  Delete the "id" role.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get all child role ids for the id
     *
     * @param id role id
     * @return set of role ids
     */
    Set<Long> getAllChildRoleIdsForRole(Long id);

    /**
     * Get all menus for user organization roles mapped
     *
     * @param userId
     * @param organizationId
     * @return list of module, feature details
     */
    List getAllMenusForUserInAnOrganization(Long userId, Long organizationId);

    /**
     * Get all privileges for role hierarchy for the id
     *
     * @param id role id
     * @return set of privileges
     */
    Set<Privilege> getAllPrivilegesForRoleHierarchy(Long id);

    Page<Role> search(String query, Pageable pageable);

    void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate);
}
