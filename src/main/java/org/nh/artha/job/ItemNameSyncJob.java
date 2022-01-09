package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Item;
import org.nh.artha.service.ItemService;
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

public class ItemNameSyncJob implements Job {
    private final Logger log = LoggerFactory.getLogger(PackageNameSyncJob.class);

    @Autowired
    private JdbcTemplate mdmDbTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ItemService itemService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to ItemNameSync  : {}");
        String userQueryCountQuery="select count(*) from item";
        String query="SELECT * FROM item OFFSET :startIndex ROWS FETCH FIRST 200 ROWS ONLY";
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Long userMasterCount = mdmDbTemplate.queryForObject(userQueryCountQuery, Long.class);
        Integer startIndex=0;
        while (startIndex<userMasterCount){
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<Item> itemList=mdmDbTemplate.query(replaceQuery,new BeanPropertyRowMapper(Item.class));
            syncItemMaster(itemList);
            startIndex=startIndex+pageSize;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncItemMaster(List<Item> itemList){
        log.debug("Request to ItemNameSync syncItemMaster  : {}",itemList);
        itemService.saveAll(itemList);
    }

}
