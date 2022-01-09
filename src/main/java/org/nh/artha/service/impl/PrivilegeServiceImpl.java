package org.nh.artha.service.impl;

import org.nh.artha.domain.Module;
import org.nh.artha.service.PrivilegeService;
import org.nh.artha.domain.Privilege;
import org.nh.artha.repository.PrivilegeRepository;
import org.nh.artha.repository.search.PrivilegeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Privilege}.
 */
@Service
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {

    private final Logger log = LoggerFactory.getLogger(PrivilegeServiceImpl.class);

    private final PrivilegeRepository privilegeRepository;

    private final PrivilegeSearchRepository privilegeSearchRepository;

    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository, PrivilegeSearchRepository privilegeSearchRepository) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeSearchRepository = privilegeSearchRepository;
    }

    /**
     * Save a privilege.
     *
     * @param privilege the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Privilege save(Privilege privilege) {
        log.debug("Request to save Privilege : {}", privilege);
        Privilege result = privilegeRepository.save(privilege);
        privilegeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the privileges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Privilege> findAll(Pageable pageable) {
        log.debug("Request to get all Privileges");
        return privilegeRepository.findAll(pageable);
    }


    /**
     * Get one privilege by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Privilege> findOne(Long id) {
        log.debug("Request to get Privilege : {}", id);
        return privilegeRepository.findById(id);
    }

    /**
     * Delete the privilege by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Privilege : {}", id);
        privilegeRepository.deleteById(id);
        privilegeSearchRepository.deleteById(id);
    }

    /**
     * Search for the privilege corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Privilege> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Privileges for query {}", query);
        return privilegeSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Privilege");
        List<Privilege> data = privilegeRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(i, pageSize));
        if (!data.isEmpty()) {
            privilegeSearchRepository.saveAll(data);
        }
    }
}
