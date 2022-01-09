package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ServiceMaster;
import org.nh.artha.repository.ServiceMasterRepository;
import org.nh.artha.service.ServiceMasterService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServiceNameSyncJob implements Job {

    private final Logger log = LoggerFactory.getLogger(ServiceNameSyncJob.class);
    @Autowired
    private NamedParameterJdbcTemplate mdmDbNamedParameterTemplate;
    @Autowired
    private JdbcTemplate mdmDbTemplate;
    @Autowired
    private ServiceMasterService serviceMasterService;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private ServiceMasterRepository serviceMasterRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long startTime = System.currentTimeMillis();
        log.debug("Request to ServiceNameSyncJob  : {}");
        String serviceGroupCountQuery = "select count(*) from service_master;";
        String query = "SELECT * FROM service_master OFFSET :startIndex ROWS   FETCH FIRST 200 ROWS ONLY";
        Long serviceGroupCount = mdmDbTemplate.queryForObject(serviceGroupCountQuery, Long.class);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Integer startIndex = 0;
        while (startIndex < serviceGroupCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<ServiceMaster> serviceGroupList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(ServiceMaster.class));
            serviceGroupList=serviceGroupList.stream().filter(serviceNotpresent).collect(Collectors.toList());
            syncServiceGroup(serviceGroupList);
            startIndex = startIndex + pageSize;
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken to execute " + serviceGroupCount + " Records ::: " + (endTime - startTime));//Time Taken to execute 12642 Records ::: 136327

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncServiceGroup(List<ServiceMaster> serviceMastersList) {
        serviceMasterService.saveAll(serviceMastersList);

    }

    Predicate<ServiceMaster> serviceNotpresent = serviceMaster -> {
        ServiceMaster sm = new ServiceMaster();
        sm.setCode(serviceMaster.getCode());
        List<ServiceMaster> serviceMaster1 = serviceMasterRepository.findAll(Example.of(sm));
        if (serviceMaster1 == null || serviceMaster1.isEmpty()) {
            return true;
        } else {
            return false;
        }
    };

}
