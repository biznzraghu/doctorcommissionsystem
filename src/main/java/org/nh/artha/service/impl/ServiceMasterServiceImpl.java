package org.nh.artha.service.impl;

import liquibase.util.csv.CSVReader;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.nh.artha.domain.Organization;
import org.nh.artha.domain.ServiceType;
import org.nh.artha.service.ServiceMasterService;
import org.nh.artha.domain.ServiceMaster;
import org.nh.artha.repository.ServiceMasterRepository;
import org.nh.artha.repository.search.ServiceMasterSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ServiceMaster}.
 */
@Service
@Transactional
public class ServiceMasterServiceImpl implements ServiceMasterService {

    private final Logger log = LoggerFactory.getLogger(ServiceMasterServiceImpl.class);

    private final ServiceMasterRepository serviceMasterRepository;

    private final ServiceMasterSearchRepository serviceMasterSearchRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public ServiceMasterServiceImpl(ServiceMasterRepository serviceMasterRepository, ServiceMasterSearchRepository serviceMasterSearchRepository) {
        this.serviceMasterRepository = serviceMasterRepository;
        this.serviceMasterSearchRepository = serviceMasterSearchRepository;
    }

    /**
     * Save a serviceMaster.
     *
     * @param serviceMaster the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ServiceMaster save(ServiceMaster serviceMaster) {
        log.debug("Request to save ServiceMaster : {}", serviceMaster);
        ServiceMaster result = serviceMasterRepository.save(serviceMaster);
        serviceMasterSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the serviceMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMaster> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceMasters");
        return serviceMasterRepository.findAll(pageable);
    }


    /**
     * Get one serviceMaster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceMaster> findOne(Long id) {
        log.debug("Request to get ServiceMaster : {}", id);
        return serviceMasterRepository.findById(id);
    }

    /**
     * Delete the serviceMaster by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceMaster : {}", id);
        serviceMasterRepository.deleteById(id);
        serviceMasterSearchRepository.deleteById(id);
    }

    /**
     * Search for the serviceMaster corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMaster> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceMasters for query {}", query);
        return serviceMasterSearchRepository.search(queryStringQuery(query), pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMaster> searchForAllService(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceMasters for query {}", query);
        return serviceMasterSearchRepository.search(queryStringQuery(query).field("name").field("code"), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMaster> searchForServiceGroup(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceMasters for query {}", query);
        return serviceMasterSearchRepository.search(queryStringQuery(query).field("serviceGroup.name"), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMaster> searchForServiceType(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceMasters for query {}", query);
        return serviceMasterSearchRepository.search(queryStringQuery(query).field("serviceType.display"), pageable);
    }

    @Override
    public void importService(MultipartFile file)throws  Exception{
        Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader=new CSVReader(reader);
        List<String[]> serviceMasterArray = csvReader.readAll();
        Map<String, List<String[]>> collect =serviceMasterArray.stream().collect(Collectors.groupingBy(strings -> strings[2]));
        List<ServiceMaster> all= new ArrayList<>();
        collect.forEach((serviceType, list) -> {
            List<ServiceMaster> serviceMasterList = list.stream().map(createServiceMasterFromCsv).collect(Collectors.toList());
            all.addAll(serviceMasterList);
        });
        saveAll(all);
    }

    @Override
    public List<ServiceMaster> saveAll(List<ServiceMaster> serviceMasterList) {
        List<ServiceMaster> serviceMasters = serviceMasterRepository.saveAll(serviceMasterList);
        serviceMasterSearchRepository.saveAll(serviceMasters);
        return serviceMasters;
    }

    private Function<String[] , ServiceMaster> createServiceMasterFromCsv = (String [] csvRowArray) -> {
        ServiceMaster serviceMaster=new ServiceMaster();
        try {
            serviceMaster.setCode(csvRowArray[0]);
            serviceMaster.setName(csvRowArray[1]);
            serviceMaster.setActive(csvRowArray[4].equalsIgnoreCase("Active")?true:false);
            serviceMaster.setProfileService(true);
        } catch (Exception e) {
            System.out.println(Arrays.toString(csvRowArray));
            throw new RuntimeException(e);
        }
        return serviceMaster;
    };


    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Doctor Payout");
        List<ServiceMaster> data = serviceMasterRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            data.forEach(doctorPayout -> {
                serviceMasterSearchRepository.indexWithoutRefresh(doctorPayout);
            });
        }
    }

}
