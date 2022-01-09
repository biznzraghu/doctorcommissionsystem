package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.HealthcareServiceCenter;
import org.nh.artha.domain.Organization;
import org.nh.artha.repository.HealthcareServiceCentreRepository;
import org.nh.artha.repository.search.HealthcareServiceCentreSearchRepository;
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

public class HealthcareServiceCentreSyncJob implements Job {

    private final Logger log = LoggerFactory.getLogger(HealthcareServiceCentreSyncJob.class);
    @Autowired
    private NamedParameterJdbcTemplate mdmDbNamedParameterTemplate;
    @Autowired
    private JdbcTemplate mdmDbTemplate;
    @Autowired
    private HealthcareServiceCentreSearchRepository healthcareServiceCentreSearchRepository;

    @Autowired
    private HealthcareServiceCentreRepository healthcareServiceCentreRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to hscSyncJob  : {}");
        String hscCountQuery = "select count(*) from healthcare_service_center;";
        String query = "SELECT * FROM healthcare_service_center OFFSET :startIndex ROWS   FETCH FIRST 200 ROWS ONLY";
        Long hscCount = mdmDbTemplate.queryForObject(hscCountQuery, Long.class);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Integer startIndex = 0;
        while (startIndex < hscCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<HealthcareServiceCenter> hscList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(HealthcareServiceCenter.class));
            List<HealthcareServiceCenter> healthcareServiceCenterList = new ArrayList<>();
            Map<Long, List<HealthcareServiceCenter>> collect = hscList.stream().collect(Collectors.groupingBy(HealthcareServiceCenter::getPartOfId));
            collect.forEach((organizationid, healthcareServiceCenters) -> {
                String organizationSearchQuery = "SELECT * FROM organization where id="+ organizationid ;
                List<org.nh.artha.security.dto.Organization> organizationDTOList = (mdmDbTemplate.query(organizationSearchQuery, new BeanPropertyRowMapper(org.nh.artha.security.dto.Organization.class)));
                if (null!=organizationDTOList && !organizationDTOList.isEmpty() && null!=organizationDTOList.get(0)){
                    healthcareServiceCenters.forEach(healthcareServiceCenter -> {
                        healthcareServiceCenter.setOrganizationCode(organizationDTOList.get(0).getCode());
                        healthcareServiceCenterList.add(healthcareServiceCenter);
                    });
                }
            });
            syncHSC(healthcareServiceCenterList);
            startIndex = startIndex + pageSize;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncHSC(List<HealthcareServiceCenter> hscList) {
        healthcareServiceCentreRepository.saveAll(hscList);
        healthcareServiceCentreSearchRepository.saveAll(hscList);
    }

}
