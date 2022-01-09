package org.nh.artha.service.impl;


import org.elasticsearch.index.query.Operator;
import org.nh.artha.domain.Department;
import org.nh.artha.domain.Organization;
import org.nh.artha.repository.DepartmentRepository;
import org.nh.artha.repository.OrganizationRepository;
import org.nh.artha.repository.search.DepartmentSearchRepository;
import org.nh.artha.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Department}.
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;

    private final DepartmentSearchRepository departmentSearchRepository;

    private final OrganizationRepository organizationRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentSearchRepository departmentSearchRepository,OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentSearchRepository = departmentSearchRepository;
        this.organizationRepository=organizationRepository;
    }

    /**
     * Save a department.
     *
     * @param department the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Department save(Department department) {
        log.debug("Request to save Department : {}", department);
        Department result = departmentRepository.save(department);
        departmentSearchRepository.save(result);
        return result;
    }
    public List<Department> saveAll(List<Department> department) {
        log.debug("Request to save Department : {}", department);
        List<Department> result = departmentRepository.saveAll(department);
        departmentSearchRepository.saveAll(result);
        return result;
    }

    /**
     * Get all the departments.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Department> findAll() {
        log.debug("Request to get all Departments");
        return departmentRepository.findAll();
    }


    /**
     * Get one department by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Department> findOne(Long id) {
        log.debug("Request to get Department : {}", id);
        return departmentRepository.findById(id);
    }

    /**
     * Delete the department by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Department : {}", id);
        departmentRepository.deleteById(id);
        departmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the department corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Department> search(String query) {
        log.debug("Request to search Departments for query {}", query);
        return StreamSupport
            .stream(departmentSearchRepository.search(queryStringQuery(query).field("id").field("code").field("name").field("active").defaultOperator(Operator.AND)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Department");
        List<Department> data = departmentRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(i, pageSize));
        if (!data.isEmpty()) {
            departmentSearchRepository.saveAll(data);
        }
    }
    @Override
    public List<Department> uploadDepartmentData(List<String[]> organizationArray) throws Exception {
        Map<String, List<String[]>> organizationDepartmentMap = organizationArray.stream().collect(Collectors.groupingBy(strings -> strings[0]));
        organizationDepartmentMap.forEach((unitCode, departments) -> {
            Organization organizationByUnitCode = organizationRepository.findOrganizationByUnitCode(unitCode);
            List<Department> departmentList=new ArrayList<>();
            departments.stream().forEach(department -> {
                Department departmentFromCsv = createDepartmentFromCsv(department, organizationByUnitCode);
                if (departmentFromCsv!=null)
                    departmentList.add(departmentFromCsv);
            });
          this.saveAll(departmentList);
        });
        return null;
    }
    private Department createDepartmentFromCsv (String [] csvRowArray,Organization organization) {
        Department department = null;
        try {
            if (csvRowArray[2] != null && !csvRowArray[2].isEmpty()) {
                department = new Department();
                department.setOrganization(organization);
                department.setCode(organization.getCode());
                department.setName(csvRowArray[2]);
                department.setActive(csvRowArray[5].equalsIgnoreCase("Active") ? true : false);
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(csvRowArray));
            throw new RuntimeException(e);
        }
        return department;
    }
}
