package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Organization;
import org.nh.artha.domain.UserMaster;
import org.nh.artha.domain.UserOrganizationDepartmentMapping;
import org.nh.artha.domain.dto.DepartmentMasterDTO;
import org.nh.artha.domain.dto.UserOrganizationDTO;
import org.nh.artha.repository.OrganizationRepository;
import org.nh.artha.repository.UserMasterRepository;
import org.nh.artha.repository.UserOrganizationDepartmentMappingRepository;
import org.nh.artha.service.OrganizationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserOrganizationDepartmentJobSync implements Job {

    private final Logger log = LoggerFactory.getLogger(UserOrganizationDepartmentJobSync.class);

    @Autowired
    private JdbcTemplate mdmDbTemplate;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private org.nh.artha.service.UserOrganizationDepartmentMappingService UserOrganizationDepartmentMappingService;

    @Autowired
    private UserOrganizationDepartmentMappingRepository userOrganizationDepartmentMappingRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to userOrganizationDepartment Job  : {}");
        String userOrganizationCountQuery = "select count(*) from user_organization_mapping uom inner join organization o on (o.id = uom.organization_id) inner join user_master um on (um.id = uom.user_id) inner join value_set_code vsc on (vsc.id = o.type_id) where vsc.code = 'prov'";
        String query = "select um.login as login, string_agg(o.code, ', ') as organizationCode from user_organization_mapping uom" +
            " inner join organization o on (o.id = uom.organization_id)" +
            " inner join user_master um on (um.id = uom.user_id)" +
            " inner join value_set_code vsc on (vsc.id = o.type_id)" +
            " where vsc.code = 'prov'" +
            " group by um.login OFFSET :startIndex ROWS FETCH FIRST 200 ROWS ONLY";
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Long userOrganizationCount = mdmDbTemplate.queryForObject(userOrganizationCountQuery, Long.class);
        Integer startIndex = 0;
        while (startIndex < userOrganizationCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<UserOrganizationDTO> userOrganizationDTOList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(UserOrganizationDTO.class));
            List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList = new ArrayList<>();
            userOrganizationDTOList.stream().forEach(userOrganizationDTO -> {
                UserMaster byEmployeeNo = userMasterRepository.findByEmployeeNo(userOrganizationDTO.getLogin());
                List<String> splitList = Stream.of(userOrganizationDTO.getOrganizationCode().split(",")).collect(Collectors.toList());
                splitList.forEach(orgCode -> {
                    Organization organizationByUnitCode = organizationRepository.findOrganizationByUnitCode(orgCode.trim());
                    UserOrganizationDepartmentMapping userOrganizationDepartmentMapping = new UserOrganizationDepartmentMapping();
                    userOrganizationDepartmentMapping.setOrganization(organizationByUnitCode);
                    userOrganizationDepartmentMapping.setUserMaster(byEmployeeNo);
                    userOrganizationDepartmentMappingList.add(userOrganizationDepartmentMapping);
                });

            });
            if (userOrganizationDepartmentMappingList != null && !userOrganizationDepartmentMappingList.isEmpty())
                syncUserOrganizationDepartment(userOrganizationDepartmentMappingList);
            startIndex = startIndex + pageSize;
        }
        String deptQuery = "select um.login as login, string_agg(o.code, ', ') as organizationCode from user_organization_mapping uom" +
            " inner join organization o on (o.id = uom.organization_id)" +
            " inner join user_master um on (um.id = uom.user_id)" +
            " inner join value_set_code vsc on (vsc.id = o.type_id)" +
            " where vsc.code = 'dept'" +
            " group by um.login";
        List<UserOrganizationDTO> userOrganizationDTOList = mdmDbTemplate.query(deptQuery, new BeanPropertyRowMapper(UserOrganizationDTO.class));
        userOrganizationDTOList.stream().forEach(userOrganizationDTO -> {
            List<String> splitList = Stream.of(userOrganizationDTO.getOrganizationCode().split(",")).collect(Collectors.toList());
            splitList.forEach(orgCode -> {
                String[] orgDeptDetail = orgCode.split("-");
                String organizationCode = orgDeptDetail[0];
                String departmentCode = orgDeptDetail[1];
                List<DepartmentMasterDTO> department1 = new ArrayList<>();
                UserOrganizationDepartmentMapping savedDepartmentMapping = userOrganizationDepartmentMappingRepository.filterAllDepartmentByUserOrganization(userOrganizationDTO.getLogin().trim(), organizationCode.trim());
                if (savedDepartmentMapping != null) {
                    String deptSearchQuery = "SELECT * FROM department where code='" + departmentCode.trim() + "'";
                    List<DepartmentMasterDTO> departmentMasterDTO = mdmDbTemplate.query(deptSearchQuery, new BeanPropertyRowMapper(DepartmentMasterDTO.class));
                    if (savedDepartmentMapping.getDepartment() != null && !savedDepartmentMapping.getDepartment().isEmpty())
                        department1 = savedDepartmentMapping.getDepartment();
                    if (departmentMasterDTO != null && !departmentMasterDTO.isEmpty() && departmentMasterDTO.get(0) != null) {
                        DepartmentMasterDTO masterDTO = departmentMasterDTO.get(0);
                        masterDTO.setCode(savedDepartmentMapping.getOrganization().getCode() + "-" + masterDTO.getCode());
                        department1.add(masterDTO);
                    }
                    if (department1 != null && !department1.isEmpty()) {
                        savedDepartmentMapping.setDepartment(department1);
                    }
                    UserOrganizationDepartmentMappingService.save(savedDepartmentMapping);
                }
            });

        });

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncUserOrganizationDepartment(List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList) {
        log.debug("Request to syncUserOrganizationDepartment syncUserOrganizationDepartment  : {}", userOrganizationDepartmentMappingList);
        UserOrganizationDepartmentMappingService.saveAll(userOrganizationDepartmentMappingList);

    }
}
