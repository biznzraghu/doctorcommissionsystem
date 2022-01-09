package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserMaster;
import org.nh.artha.service.UserMasterService;
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

public class UserSyncJob implements Job {
    @Autowired
    private JdbcTemplate mdmDbTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserMasterService userMasterService;

    private final Logger log = LoggerFactory.getLogger(PackageNameSyncJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to User  execute : {}");
        String userQueryCountQuery = "select count(*) from user_master";
        String query = "SELECT usm.id as id, usm.login as employeeNumber , usm.first_name as firstName, usm.last_name as lastName, usm.display_name as displayName, usm.status as status, usm.designation as designation FROM user_master usm OFFSET :startIndex ROWS FETCH FIRST 200 ROWS ONLY";
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Long userMasterCount = mdmDbTemplate.queryForObject(userQueryCountQuery, Long.class);
        Integer startIndex = 0;
        while (startIndex < userMasterCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<UserMaster> userMasterList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(UserMaster.class));
            syncUserMaster(userMasterList);
            startIndex = startIndex + pageSize;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncUserMaster(List<UserMaster> userMasterList) {
        log.debug("Request to syncUserMaster  : {}", userMasterList);
        userMasterService.saveAll(userMasterList);

    }

}
