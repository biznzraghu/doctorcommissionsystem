package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Organization;
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

import java.util.List;

public class OrganizationJobSync implements Job {

    private final Logger log = LoggerFactory.getLogger(OrganizationJobSync.class);

    @Autowired
    private JdbcTemplate mdmDbTemplate;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to organization Job  : {}");
        String userQueryCountQuery = "select count(*) from organization organizati0_ cross join value_set_code vsc  where  organizati0_.type_id = vsc.id and  vsc.code='prov'";
        String query = "select organizati0_.id as id, organizati0_.active as active, organizati0_.clinical as clinical, organizati0_.code as code, " +
            "organizati0_.description as description, organizati0_.license_number as licenseNumber, organizati0_.name as name, " +
            "organizati0_.started_on as startedOn, organizati0_.website as website from organization organizati0_ " +
            "cross join value_set_code vsc  where  organizati0_.type_id = vsc.id and  vsc.code='prov' OFFSET :startIndex ROWS FETCH FIRST 200 ROWS ONLY";
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Long userMasterCount = mdmDbTemplate.queryForObject(userQueryCountQuery, Long.class);
        Integer startIndex = 0;
        while (startIndex < userMasterCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<Organization> organizationList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(Organization.class));
            syncOrganization(organizationList);
            startIndex = startIndex + pageSize;

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncOrganization(List<Organization> organizationList) {
        log.debug("Request to OrganizationJobSync syncOrganization  : {}", organizationList);
        organizationService.saveAll(organizationList);

    }
}
