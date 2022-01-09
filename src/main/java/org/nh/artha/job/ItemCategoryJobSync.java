package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ItemCategory;
import org.nh.artha.repository.ItemCategoryRepository;
import org.nh.artha.repository.search.ItemCategorySearchRepository;
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

public class ItemCategoryJobSync implements Job {
    private final Logger log = LoggerFactory.getLogger(ItemCategoryJobSync.class);
    @Autowired
    private JdbcTemplate mdmDbTemplate;
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private ItemCategorySearchRepository itemCategorySearchRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to ItemCategoryJobSync  : {}");
        String planCountQuery = "select count(*) from item_category;";
        String query = "SELECT * FROM item_category OFFSET :startIndex ROWS  FETCH FIRST 200 ROWS ONLY";
        Long planCount = mdmDbTemplate.queryForObject(planCountQuery, Long.class);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        Integer startIndex = 0;
        while (startIndex < planCount) {
            String replaceQuery = query.replace(":startIndex", startIndex.toString());
            List<ItemCategory> itemCategoryList = mdmDbTemplate.query(replaceQuery, new BeanPropertyRowMapper(ItemCategory.class));
            syncItemMaster(itemCategoryList);
            startIndex = startIndex + pageSize;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncItemMaster(List<ItemCategory> itemCategoryList) {
        List<ItemCategory> itemCategoryList1 = itemCategoryRepository.saveAll(itemCategoryList);
        itemCategorySearchRepository.saveAll(itemCategoryList1);

    }
}
