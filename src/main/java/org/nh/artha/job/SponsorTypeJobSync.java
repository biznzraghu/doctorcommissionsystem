package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ValueSet;
import org.nh.artha.domain.ValueSetCode;
import org.nh.artha.repository.ValueSetCodeRepository;
import org.nh.artha.repository.ValueSetRepository;
import org.nh.artha.repository.search.ValueSetCodeSearchRepository;
import org.nh.artha.repository.search.ValueSetSearchRepository;
import org.nh.artha.service.ValueSetCodeService;
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
import java.util.stream.Collectors;

public class SponsorTypeJobSync implements Job {
    private final Logger log = LoggerFactory.getLogger(SponsorTypeJobSync.class);

    @Autowired
    private JdbcTemplate mdmDbTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ValueSetRepository valueSetRepository;

    @Autowired
    private ValueSetSearchRepository valueSetSearchRepository;

    @Autowired
    private ValueSetCodeService valueSetCodeService;

    @Autowired
    private ValueSetCodeSearchRepository valueSetCodeSearchRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to TariffClass  : {}");
        String valueSetQuery="select * from value_set  where code ='1.25.2.1.228'";
        ValueSet valueSet = (ValueSet)mdmDbTemplate.query(valueSetQuery, new BeanPropertyRowMapper(ValueSet.class)).stream().findFirst().get();
        String valueSetCodeQuery="SELECT * from value_set_code  where value_set_id ="+valueSet.getId();
        List<ValueSetCode> valueSetCode = mdmDbTemplate.query(valueSetCodeQuery, new BeanPropertyRowMapper(ValueSetCode.class));
        valueSet.setId(null);
        ValueSet result = saveValueSet(valueSet);
        List<ValueSetCode> collect = valueSetCode.stream().map(valueSetCode1 -> {
            valueSetCode1.setId(null);
            valueSetCode1.setValueSet(result);
            return valueSetCode1;
        }).collect(Collectors.toList());
        saveValueSetCode(collect);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ValueSet saveValueSet(ValueSet valueSet){
        ValueSet save = valueSetRepository.save(valueSet);
        valueSetSearchRepository.save(save);
        return save;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveValueSetCode(List<ValueSetCode> valueSetCode){
        valueSetCodeService.saveAll(valueSetCode);
    }
}
