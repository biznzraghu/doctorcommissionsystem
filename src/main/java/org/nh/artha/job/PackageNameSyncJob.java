package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.PackageMaster;
import org.nh.artha.service.PackageMasterService;
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

public class PackageNameSyncJob implements Job {

    private final Logger log = LoggerFactory.getLogger(PackageNameSyncJob.class);
    @Autowired
    private JdbcTemplate mdmDbTemplate;
    @Autowired
    private PackageMasterService packageMasterService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to ServiceGroupSyncJob  : {}");
        String packageGroupCountQuery = "select count(*) from package_master";
        String query = "SELECT * FROM package_master OFFSET :startIndex ROWS FETCH FIRST 200 ROWS ONLY";
        Long packageGroupCount = mdmDbTemplate.queryForObject(packageGroupCountQuery, Long.class);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(packageGroupCount / pageSize));
        Integer startIndex = 0;
        while (startIndex < packageGroupCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<PackageMaster> packageMasterList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(PackageMaster.class));
            syncPackageMaster(packageMasterList);
            startIndex = startIndex + pageSize;
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncPackageMaster(List<PackageMaster> packageMasterList) {
        log.debug("Request to packageMasterList  : {}", packageMasterList);
        packageMasterService.saveAll(packageMasterList);


    }

}


