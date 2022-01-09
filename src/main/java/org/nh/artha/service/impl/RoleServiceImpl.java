package org.nh.artha.service.impl;

import org.elasticsearch.index.query.Operator;
import org.nh.artha.domain.Privilege;
import org.nh.artha.domain.Role;
import org.nh.artha.repository.RoleRepository;
import org.nh.artha.repository.search.RoleSearchRepository;
import org.nh.artha.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Role}.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final RoleSearchRepository roleSearchRepository;

    public RoleServiceImpl(RoleRepository roleRepository, RoleSearchRepository roleSearchRepository) {
        this.roleRepository = roleRepository;
        this.roleSearchRepository = roleSearchRepository;
    }

    /**
     * Save a role.
     *
     * @param role the entity to save
     * @return the persisted entity
     */
    @Override
    public Role save(Role role) {
        log.debug("Request to save Role : {}", role);
        Role result = roleRepository.save(role);
        roleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the roles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        log.debug("Request to get all Roles");
        List<Role> result = roleRepository.findAllWithEagerRelationships();

        return result;
    }

    /**
     * Get one role by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Role findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        Role role = roleRepository.findOneWithEagerRelationships(id);
        return role;
    }

    /**
     * Delete the  role by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        roleRepository.deleteById(id);
    }

    /**
     * Get all child role ids for the id
     *
     * @param id role id
     * @return set of role ids
     */
    @Override
    public Set<Long> getAllChildRoleIdsForRole(Long id) {
        log.debug("Request to get all child role ids of Role : {}", id);
        Role role = roleRepository.findById(id).get();
        return populateChildRoleIds(role, new HashSet<>());
    }

    /**
     * Get all menus for user organization roles mapped
     *
     * @param userId
     * @param organizationId
     * @return list of module, feature details
     */
    @Override
    public List getAllMenusForUserInAnOrganization(Long userId, Long organizationId) {
        List<Role> roles = roleRepository.getRolesOfUserInAnOrganization(userId, organizationId);
        Set<Long> roleIds = new HashSet<>();
        for (Role role : roles) {
            roleIds.add(role.getId());
            populateChildRoleIds(role, roleIds);
        }
        return roleIds.isEmpty() ? Collections.emptyList() : roleRepository.getAllMenusForRoles(roleIds);
    }

    private Set<Long> populateChildRoleIds(Role role, Set<Long> roleIds) {
        if (role.getRoles() != null) {
            for (Role childRole : role.getRoles()) {
                roleIds.add(childRole.getId());
                populateChildRoleIds(childRole, roleIds);
            }
        }
        return roleIds;
    }

    /**
     * Get all privileges for role hierarchy for the id
     *
     * @param id role id
     * @return set of privileges
     */
    @Override
    public Set<Privilege> getAllPrivilegesForRoleHierarchy(Long id) {
        log.debug("Request to get all privileges of Role hierarchy: {}", id);
        Role role = roleRepository.findById(id).get();
        Set<Long> childRoleIds = populateChildRoleIds(role, new HashSet<>());
        Set<Privilege> privileges = addPrivilegesIfPresent(role, new HashSet<>());
        childRoleIds.remove(role.getId());
        for (Long childRoleId : childRoleIds) {
            addPrivilegesIfPresent(roleRepository.findById(childRoleId).get(), privileges);
        }
        return privileges;
    }

    private Set<Privilege> addPrivilegesIfPresent(Role role, Set<Privilege> privileges) {
        if (Optional.ofNullable(role.getPrivileges()).isPresent()) {
            privileges.addAll(role.getPrivileges());
        }
        return privileges;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Role> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Roles for query {}", query);
        return roleSearchRepository.search(queryStringQuery(query).field("name").field("active")
            .defaultOperator(Operator.AND), pageable);
    }

    @Override
    public void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Role");
        List<Role> data = roleRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(i, pageSize));
        if (!data.isEmpty()) {
            roleSearchRepository.saveAll(data);
        }
    }

}
