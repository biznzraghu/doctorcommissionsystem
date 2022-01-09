package org.nh.artha.service.impl;
import org.nh.artha.service.DepartmentRevenueService;
import org.nh.artha.domain.DepartmentRevenue;
import org.nh.artha.repository.DepartmentRevenueRepository;
import org.nh.artha.repository.search.DepartmentRevenueSearchRepository;
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
 * Service Implementation for managing {@link DepartmentRevenue}.
 */
@Service
@Transactional
public class DepartmentRevenueServiceImpl implements DepartmentRevenueService {

    private final Logger log = LoggerFactory.getLogger(DepartmentRevenueServiceImpl.class);

    private final DepartmentRevenueRepository departmentRevenueRepository;

    private final DepartmentRevenueSearchRepository departmentRevenueSearchRepository;

    public DepartmentRevenueServiceImpl(DepartmentRevenueRepository departmentRevenueRepository, DepartmentRevenueSearchRepository departmentRevenueSearchRepository) {
        this.departmentRevenueRepository = departmentRevenueRepository;
        this.departmentRevenueSearchRepository = departmentRevenueSearchRepository;
    }

    /**
     * Save a departmentRevenue.
     *
     * @param departmentRevenue the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DepartmentRevenue save(DepartmentRevenue departmentRevenue) {
        log.debug("Request to save DepartmentRevenue : {}", departmentRevenue);
        DepartmentRevenue result = departmentRevenueRepository.save(departmentRevenue);
        departmentRevenueSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the departmentRevenues.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentRevenue> findAll() {
        log.debug("Request to get all DepartmentRevenues");
        return departmentRevenueRepository.findAll();
    }


    /**
     * Get one departmentRevenue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentRevenue> findOne(Long id) {
        log.debug("Request to get DepartmentRevenue : {}", id);
        return departmentRevenueRepository.findById(id);
    }

    /**
     * Delete the departmentRevenue by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepartmentRevenue : {}", id);
        departmentRevenueRepository.deleteById(id);
        departmentRevenueSearchRepository.deleteById(id);
    }

    /**
     * Search for the departmentRevenue corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentRevenue> search(String query) {
        log.debug("Request to search DepartmentRevenues for query {}", query);
        return StreamSupport
            .stream(departmentRevenueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Department");
        List<DepartmentRevenue> data = departmentRevenueRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(i, pageSize));
        if (!data.isEmpty()) {
            departmentRevenueRepository.saveAll(data);
        }
    }
}
