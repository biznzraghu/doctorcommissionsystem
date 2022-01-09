package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Department;
import org.nh.artha.domain.Organization;
import org.nh.artha.domain.Plan;
import org.nh.artha.domain.dto.DepartmentDTO;
import org.nh.artha.domain.dto.DepartmentMasterDTO;
import org.nh.artha.repository.OrganizationRepository;
import org.nh.artha.service.DepartmentService;
import org.nh.artha.service.PlanService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepartmentJobSync implements Job {

    private final Logger log = LoggerFactory.getLogger(DepartmentJobSync.class);
    @Autowired
    private NamedParameterJdbcTemplate mdmDbNamedParameterTemplate;
    @Autowired
    private JdbcTemplate mdmDbTemplate;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long startTime = System.currentTimeMillis();
        log.debug("Request to departmentSyncJob  : {}");
        /*String departmentCountQuery = "select count(*) from department;";*/
        String query = "select org.code from organization org cross join value_set_code vsc where vsc.id = org.type_id and vsc.code='dept'";
        /*Long departmentCount = mdmDbTemplate.queryForObject(departmentCountQuery, Long.class);*/
        List<String> departmentList = mdmDbTemplate.queryForList(query, String.class);
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        departmentList.forEach(unitDepartmentCode -> {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            String[] splitedCodes = unitDepartmentCode.split("-");
            departmentDTO.setDepartmentCode(splitedCodes[1].trim());
            departmentDTO.setOrganizationCode(splitedCodes[0].trim());
            departmentDTOList.add(departmentDTO);
        });
        Map<String, List<DepartmentDTO>> collect = departmentDTOList.stream().collect(Collectors.groupingBy(DepartmentDTO::getOrganizationCode));
        collect.forEach((organizationCode, departmentDTOS) -> {
            List<Department> departments = new ArrayList<>();
            Organization organizationByUnitCode = organizationRepository.findOrganizationByUnitCode(organizationCode.trim());
            departmentDTOS.forEach(departmentDTO -> {
                String deptSearchQuery = "SELECT * FROM department where code='" + departmentDTO.getDepartmentCode().trim() + "'";
                List<DepartmentMasterDTO> departmentMasterDTOList = (mdmDbTemplate.query(deptSearchQuery, new BeanPropertyRowMapper(DepartmentMasterDTO.class)));
                if (departmentMasterDTOList != null && !departmentMasterDTOList.isEmpty() && departmentMasterDTOList.get(0) != null) {
                    DepartmentMasterDTO departmentMasterDTO = departmentMasterDTOList.get(0);
                    Department department = new Department();
                    department.setCode(organizationByUnitCode.getCode() + "-" + departmentMasterDTO.getCode());
                    department.setName(departmentMasterDTO.getName());
                    department.setActive(departmentMasterDTO.isActive());
                    department.setOrganization(organizationByUnitCode);
                    departments.add(department);
                }

            });
            syncdepartmentMaster(departments);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncdepartmentMaster(List<Department> departmentList) {
        departmentService.saveAll(departmentList);
    }

}
