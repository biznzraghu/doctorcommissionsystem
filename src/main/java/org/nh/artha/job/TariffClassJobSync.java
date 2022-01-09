package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.AbstractValueSetCode;
import org.nh.artha.domain.ValueSet;
import org.nh.artha.domain.ValueSetCode;
import org.nh.artha.repository.ValueSetCodeRepository;
import org.nh.artha.repository.ValueSetRepository;
import org.nh.artha.repository.search.ValueSetCodeSearchRepository;
import org.nh.artha.repository.search.ValueSetSearchRepository;
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

public class TariffClassJobSync implements Job {

    private final Logger log = LoggerFactory.getLogger(TariffClassJobSync.class);

    @Autowired
    private JdbcTemplate mdmDbTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ValueSetRepository valueSetRepository;

    @Autowired
    private ValueSetSearchRepository valueSetSearchRepository;

    @Autowired
    private ValueSetCodeRepository valueSetCodeRepository;

    @Autowired
    private ValueSetCodeSearchRepository valueSetCodeSearchRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Request to TariffClass  : {}");
        String valueSetQuery="select * from value_set  where code ='TARIFF_CLASS'";
        ValueSet valueSet = (ValueSet)mdmDbTemplate.query(valueSetQuery, new BeanPropertyRowMapper(ValueSet.class)).stream().findFirst().get();
        ValueSet result = saveValueSet(valueSet);
        String valueSetCodeQuery="SELECT * from value_set_code  where value_set_id ="+valueSet.getId();
        List<ValueSetCode> valueSetCode = mdmDbTemplate.query(valueSetCodeQuery, new BeanPropertyRowMapper(ValueSetCode.class));
        List<ValueSetCode> collect = valueSetCode.stream().map(valueSetCode1 -> {
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
        List<ValueSetCode> valueSetCodes = valueSetCodeRepository.saveAll(valueSetCode);
        valueSetCodeSearchRepository.saveAll(valueSetCodes);
    }
}
