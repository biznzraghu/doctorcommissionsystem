package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Group;
import org.nh.artha.domain.ServiceGroup;
import org.nh.artha.domain.dto.GroupDTO;
import org.nh.artha.repository.GroupRepository;
import org.nh.artha.repository.ServiceGroupRepository;
import org.nh.artha.repository.search.GroupSearchRepository;
import org.nh.artha.repository.search.ServiceGroupSearchRepository;
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

public class ServiceGroupSyncJob implements Job {

    private final Logger log = LoggerFactory.getLogger(ServiceGroupSyncJob.class);

    @Autowired
    private NamedParameterJdbcTemplate mdmDbNamedParameterTemplate;
    @Autowired
    private JdbcTemplate mdmDbTemplate;

    @Autowired
    private GroupRepository serviceGroupRepository;

    @Autowired
    private GroupSearchRepository serviceGroupSearchRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to ServiceGroupSyncJob  : {}");
        String serviceGroupCountQuery="select count(*) from group_master  where context ='Service_Group'";
        String query="SELECT gp.id as id,gp.code as code,gp.name as name,gp.context as context,gp.modifiable as modifiable,gp.active as active FROM group_master gp  where context ='Service_Group'  OFFSET :startIndex ROWS FETCH FIRST 200 ROWS ONLY";
        Long serviceGroupCount = mdmDbTemplate.queryForObject(serviceGroupCountQuery, Long.class);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Integer startIndex=0;
        while (startIndex<serviceGroupCount){
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<Group> serviceGroupList=mdmDbTemplate.query(replaceQuery,new BeanPropertyRowMapper(Group.class));
            syncServiceGroup(serviceGroupList);
            startIndex=startIndex+pageSize;
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncServiceGroup(List<Group> serviceMastersList) {
        log.debug("Request to syncServiceGroup  : {}", serviceMastersList);
        serviceGroupRepository.saveAll(serviceMastersList);
        serviceGroupSearchRepository.saveAll(serviceMastersList);

    }

}
