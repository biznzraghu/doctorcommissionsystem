package org.nh.artha.service.impl;

import org.elasticsearch.index.query.Operator;
import org.nh.artha.domain.AbstractValueSetCode;
import org.nh.artha.repository.CommonValueSetCodeRepository;
import org.nh.artha.repository.search.ValueSetCodeSearchRepository;
import org.nh.artha.service.CommonValueSetCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
@Service
@Transactional
public class CommonValueSetCodeServiceImpl implements CommonValueSetCodeService {
    private final Logger log = LoggerFactory.getLogger(CommonValueSetCodeServiceImpl.class);
    private final ValueSetCodeSearchRepository valueSetCodeSearchRepository;
    private final CommonValueSetCodeRepository valueSetCodeRepository;
    public CommonValueSetCodeServiceImpl(ValueSetCodeSearchRepository valueSetCodeSearchRepository,CommonValueSetCodeRepository valueSetCodeRepository)
    {
        this.valueSetCodeSearchRepository=valueSetCodeSearchRepository;
        this.valueSetCodeRepository=valueSetCodeRepository;
    }

    @Override
    public <T> Page<T> findAll(Class<T> tClass) {
        log.debug("Request to get all ValueSetCodes");
        List<T> result = valueSetCodeRepository.findAll(tClass);
        return new PageImpl<>(result);
    }

    @Override
    public <T> T findOne(Class<T> tClass, Long id) {
        return valueSetCodeRepository.findOne(tClass,id);
    }

    @Override
    public <T extends AbstractValueSetCode> Page<? extends AbstractValueSetCode> search(String query, Pageable pageable) {
        return valueSetCodeSearchRepository.search(queryStringQuery(query).field("code").field("display").field("active").defaultOperator(Operator.AND),pageable);
    }
}
