package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Plan;
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

import java.util.List;

public class PlanSyncJob implements Job {

    private final Logger log = LoggerFactory.getLogger(PlanSyncJob.class);
    @Autowired
    private NamedParameterJdbcTemplate mdmDbNamedParameterTemplate;
    @Autowired
    private JdbcTemplate mdmDbTemplate;
    @Autowired
    private PlanService planService;
    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long startTime = System.currentTimeMillis();
        log.debug("Request to PlanSyncJob  : {}");
        String planCountQuery = "select count(*) from plan;";
        String query = "SELECT * FROM plan OFFSET :startIndex ROWS   FETCH FIRST 200 ROWS ONLY";
        Long planCount = mdmDbTemplate.queryForObject(planCountQuery, Long.class);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Integer startIndex = 0;
        while (startIndex < planCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<Plan> planList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(Plan.class));
            syncPlanMaster(planList);
            startIndex = startIndex + pageSize;
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken to execute " + planCount + " Records ::: " + (endTime - startTime));//Time Taken to execute 12642 Records ::: 136327

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncPlanMaster(List<Plan> planList) {
        planService.saveAll(planList);

    }

}
