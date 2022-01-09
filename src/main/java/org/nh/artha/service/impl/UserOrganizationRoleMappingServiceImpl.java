package org.nh.artha.service.impl;

import org.nh.artha.domain.ValueSetCode;
import org.nh.artha.service.UserOrganizationRoleMappingService;
import org.nh.artha.domain.UserOrganizationRoleMapping;
import org.nh.artha.repository.UserOrganizationRoleMappingRepository;
import org.nh.artha.repository.search.UserOrganizationRoleMappingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link UserOrganizationRoleMapping}.
 */
@Service
@Transactional
public class UserOrganizationRoleMappingServiceImpl implements UserOrganizationRoleMappingService {

    private final Logger log = LoggerFactory.getLogger(UserOrganizationRoleMappingServiceImpl.class);

    private final UserOrganizationRoleMappingRepository userOrganizationRoleMappingRepository;

    private final UserOrganizationRoleMappingSearchRepository userOrganizationRoleMappingSearchRepository;

    public UserOrganizationRoleMappingServiceImpl(UserOrganizationRoleMappingRepository userOrganizationRoleMappingRepository, UserOrganizationRoleMappingSearchRepository userOrganizationRoleMappingSearchRepository) {
        this.userOrganizationRoleMappingRepository = userOrganizationRoleMappingRepository;
        this.userOrganizationRoleMappingSearchRepository = userOrganizationRoleMappingSearchRepository;
    }

    /**
     * Save a userOrganizationRoleMapping.
     *
     * @param userOrganizationRoleMapping the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserOrganizationRoleMapping save(UserOrganizationRoleMapping userOrganizationRoleMapping) {
        log.debug("Request to save UserOrganizationRoleMapping : {}", userOrganizationRoleMapping);
        UserOrganizationRoleMapping result = userOrganizationRoleMappingRepository.save(userOrganizationRoleMapping);
        userOrganizationRoleMappingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the userOrganizationRoleMappings.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserOrganizationRoleMapping> findAll() {
        log.debug("Request to get all UserOrganizationRoleMappings");
        return userOrganizationRoleMappingRepository.findAll();
    }


    /**
     * Get one userOrganizationRoleMapping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserOrganizationRoleMapping> findOne(Long id) {
        log.debug("Request to get UserOrganizationRoleMapping : {}", id);
        return userOrganizationRoleMappingRepository.findById(id);
    }

    /**
     * Delete the userOrganizationRoleMapping by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserOrganizationRoleMapping : {}", id);
        userOrganizationRoleMappingRepository.deleteById(id);
        userOrganizationRoleMappingSearchRepository.deleteById(id);
    }

    /**
     * Search for the userOrganizationRoleMapping corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserOrganizationRoleMapping> search(String query) {
        log.debug("Request to search UserOrganizationRoleMappings for query {}", query);
        return StreamSupport
            .stream(userOrganizationRoleMappingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on ValueSetCodes");
        List<UserOrganizationRoleMapping> data = userOrganizationRoleMappingRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            userOrganizationRoleMappingSearchRepository.saveAll(data);
        }
    }
}
