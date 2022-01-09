package org.nh.artha.service.impl;

import org.elasticsearch.index.query.Operator;
import org.nh.artha.service.GroupService;
import org.nh.artha.domain.Group;
import org.nh.artha.repository.GroupRepository;
import org.nh.artha.repository.search.GroupSearchRepository;
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
 * Service Implementation for managing {@link Group}.
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    private final GroupSearchRepository groupSearchRepository;

    public GroupServiceImpl(GroupRepository groupRepository, GroupSearchRepository groupSearchRepository) {
        this.groupRepository = groupRepository;
        this.groupSearchRepository = groupSearchRepository;
    }

    /**
     * Save a group.
     *
     * @param group the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Group save(Group group) {
        log.debug("Request to save Group : {}", group);
        Group result = groupRepository.save(group);
        groupSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Group> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        return groupRepository.findAll(pageable);
    }


    /**
     * Get one group by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Group> findOne(Long id) {
        log.debug("Request to get Group : {}", id);
        return groupRepository.findById(id);
    }

    /**
     * Delete the group by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
        groupSearchRepository.deleteById(id);
    }

    /**
     * Search for the group corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Group> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Groups for query {}", query);
        return groupSearchRepository.search(queryStringQuery(query).field("name").field("active")
            .defaultOperator(Operator.AND), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Group");
        List<Group> data = groupRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            groupSearchRepository.saveAll(data);
        }
    }
}
