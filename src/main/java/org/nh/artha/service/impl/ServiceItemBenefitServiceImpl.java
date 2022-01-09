package org.nh.artha.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.Type;
import org.nh.artha.repository.ServiceItemBenefitRepository;
import org.nh.artha.repository.VariablePayoutRepository;
import org.nh.artha.repository.search.ServiceItemBenefitSearchRepository;
import org.nh.artha.service.*;
import org.nh.artha.web.rest.errors.BadRequestAlertException;
import org.nh.artha.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link ServiceItemBenefit}.
 */
@Service
@Transactional
public class ServiceItemBenefitServiceImpl implements ServiceItemBenefitService {

    private final Logger log = LoggerFactory.getLogger(ServiceItemBenefitServiceImpl.class);

    private final ServiceItemBenefitRepository serviceItemBenefitRepository;

    private final ServiceItemBenefitSearchRepository serviceItemBenefitSearchRepository;

    private final ServiceItemExceptionService serviceItemExceptionService;

    private final UserMasterService userMasterService;

    @Autowired
    @Lazy
    private VariablePayoutService variablePayoutService;

    private final VariablePayoutRepository variablePayoutRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    @Lazy
    private ServiceItemBenefitTemplateService serviceItemBenefitTemplateService;

    public ServiceItemBenefitServiceImpl(ServiceItemBenefitRepository serviceItemBenefitRepository, ServiceItemBenefitSearchRepository serviceItemBenefitSearchRepository,
                                         ServiceItemExceptionService serviceItemExceptionService,ObjectMapper objectMapper,
                                         VariablePayoutRepository variablePayoutRepository,UserMasterService userMasterService) {
        this.serviceItemBenefitRepository = serviceItemBenefitRepository;
        this.serviceItemBenefitSearchRepository = serviceItemBenefitSearchRepository;
        this.serviceItemExceptionService = serviceItemExceptionService;
        this.variablePayoutRepository = variablePayoutRepository;
        this.userMasterService =userMasterService;
        this.objectMapper=objectMapper;
    }

    /**
     * Save a serviceItemBenefit.
     *
     * @param serviceItemBenefit the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ServiceItemBenefit save(ServiceItemBenefit serviceItemBenefit) {
        log.debug("Request to save ServiceItemBenefit : {}", serviceItemBenefit);
        serviceItemBenefit.setServiceItemExceptions(serviceItemBenefit.getServiceItemExceptions().stream().map(serviceItemBenefitException -> serviceItemBenefitException.serviceItemBenefit(serviceItemBenefit)).collect(Collectors.toSet()));
        ServiceItemBenefit result = serviceItemBenefitRepository.save(serviceItemBenefit);
        serviceItemBenefitSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the serviceItemBenefits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceItemBenefit> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceItemBenefits");
        return serviceItemBenefitRepository.findAll(pageable);
    }

    /**
     * Get one serviceItemBenefit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceItemBenefit> findOne(Long id) {
        log.debug("Request to get ServiceItemBenefit : {}", id);
        return serviceItemBenefitSearchRepository.findById(id);
    }

    /**
     * Delete the serviceItemBenefit by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceItemBenefit : {}", id);
        ServiceItemBenefit serviceItemBenefit = serviceItemBenefitRepository.findById(id).get();
        if(serviceItemBenefit.getValidFrom()==serviceItemBenefit.getValidTo()){
            serviceItemBenefitRepository.deleteOne(serviceItemBenefit.getId());
            serviceItemBenefitSearchRepository.deleteById(id);
        }else {
            serviceItemBenefit.setLatest(false);
            save(serviceItemBenefit);
        }
    }

    /**
     * Search for the serviceItemBenefit corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceItemBenefit> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceItemBenefits for query {}", query);
        return serviceItemBenefitSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public List<ServiceItemBenefit> saveAll(List<ServiceItemBenefit> serviceItemBenefit) {
        log.debug("Request to save ServiceItemBenefit : {}", serviceItemBenefit);
        List<ServiceItemBenefit> serviceItemBenefits = serviceItemBenefitRepository.saveAll(serviceItemBenefit);
        serviceItemBenefitSearchRepository.saveAll(serviceItemBenefits);
        return serviceItemBenefits;
    }

    private static Integer createPriorityOrder(Type type) {
        List<String> ordering = new ArrayList<>();
        for (int i = 0; i < Type.values().length; i++) {
            ordering.add(Type.values()[i].name());
        }
        Map<String, Integer> priorityMap = IntStream.range(0, ordering.size()).boxed()
            .collect(Collectors.toMap(ordering::get, i -> i));
        return priorityMap.get(type.name());
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on service-item-benefits");
        List<ServiceItemBenefit> data = serviceItemBenefitRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            data.forEach(serviceItemBenefit -> {
                serviceItemBenefitSearchRepository.indexWithoutRefresh(serviceItemBenefit);
            });
        }
    }

    @Transactional
    public ServiceItemBenefit createNewVersion(ServiceItemBenefit serviceItemBenefit) {//validFrom :: null variable_payout_id:88050 validto:null
        if (serviceItemBenefit.getId() == null) {
            serviceItemBenefit.setValidFrom(serviceItemBenefit.getValidTo());//They Need to Send Valid from UI vesrion of varaible Payout
        } else {
            ServiceItemBenefit serviceItemBenefit1 = serviceItemBenefitRepository.findById(serviceItemBenefit.getId()).orElse(null);
            serviceItemBenefit1.setExceptionSponsor(serviceItemBenefit.getExceptionSponsor());
            serviceItemBenefit1.setServiceItemExceptions(serviceItemBenefit.getServiceItemExceptions().stream().map(serviceItemBenefitException -> serviceItemBenefitException.serviceItemBenefit(serviceItemBenefit1)).collect(Collectors.toSet()));
            if (serviceItemBenefit.getPlanTemplate() == null && serviceItemBenefit.getValidTo() > serviceItemBenefit1.getValidTo()) {
                serviceItemBenefit1.setLatest(false);
                save(serviceItemBenefit1);
                serviceItemBenefit.setId(null);
                serviceItemBenefit.setValidFrom(serviceItemBenefit.getValidTo());
            }
        }
        return save(serviceItemBenefit);
    }

    public List<ServiceItemBenefit> getServices(Long variablePayoutId, Integer versionNo, Boolean isApproved, Pageable pageable) {
        if (!isApproved) {
            return serviceItemBenefitRepository.byLatest(variablePayoutId);
        } else {
            return serviceItemBenefitRepository.getServiceItemBenefitByVersion(variablePayoutId, versionNo);
        }
    }

    public void updateVersion(Long variablePayoutId, Integer version) {
        serviceItemBenefitRepository.update(variablePayoutId, version);
    }

    @Override
    public List<ServiceItemBenefit> copyRule(Long fromId, Long toId, Boolean isApproved, Long version) {
        List<ServiceItemBenefit> serviceItemBenefits=null;
        log.debug("Reuest To save Copy Rules ::");
        Long doctorByEmployeeId = variablePayoutRepository.getDoctorByEmployeeId(toId);
        if(doctorByEmployeeId==null){
            UserMaster userMaster = userMasterService.findOne(toId).orElse(null);
            throw new BadRequestAlertException("No Variable Payout is created for " , userMaster.getEmployeeNumber(),"ERR002");
        }
        VariablePayout variablePayout = variablePayoutRepository.findById(doctorByEmployeeId).orElse(null);
        if (variablePayout == null) {
            UserMaster userMaster = userMasterService.findOne(toId).orElse(null);
            throw new BadRequestAlertException("No Variable Payout is created for " , userMaster.getEmployeeNumber(),"ERR002");
        } else {
            log.debug("In Save Method");
            List<ServiceItemBenefit> services = getServices(variablePayoutRepository.findById(fromId).get().getVariablePayoutId(), version.intValue(), isApproved, PageRequest.of(0, 99999));
            List<ServiceItemBenefit> collect = services.parallelStream().map(serviceItemBenefit -> {
                ServiceItemBenefit serviceItemBenefitNew = null;
                try {
                    String valueAsString = objectMapper.writeValueAsString(serviceItemBenefit);
                    serviceItemBenefitNew = objectMapper.readValue(valueAsString, ServiceItemBenefit.class);
                    serviceItemBenefitNew.setId(null);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                Set<ServiceItemException> serviceItemExceptionSet = serviceItemBenefit.getServiceItemExceptions().stream().map(serviceItemException -> {
                    serviceItemException.setId(null);
                    return serviceItemException;
                }).collect(Collectors.toSet());
                ExceptionSponsor exceptionSponsor = serviceItemBenefit.getExceptionSponsor();
                if(exceptionSponsor!=null) {
                    exceptionSponsor.setId(null);
                }
                serviceItemBenefitNew.setVariablePayoutBaseId(variablePayout.getVariablePayoutId().intValue());
                serviceItemBenefitNew.setServiceItemExceptions(serviceItemExceptionSet);
                serviceItemBenefitNew.setExceptionSponsor(exceptionSponsor);
                serviceItemBenefitNew.setVariablePayout(variablePayout);
                return serviceItemBenefitNew;
            }).collect(Collectors.toList());
           serviceItemBenefits = saveAll(collect);
        }
        return serviceItemBenefits;

    }

    @Override
    public List<ServiceItemBenefit> copyRuleFromTemplate(ServiceItemBenefitTemplate fromTemplate, Long toTemplateId,Boolean isRemove) {
        log.debug("Request to copy template rules : {}", fromTemplate.getId(),toTemplateId,isRemove);
        List<ServiceItemBenefit> serviceItemBenefitList=null;
        Page<ServiceItemBenefit> serviceItemBenefits = search("planTemplate.id:" + fromTemplate.getId(), PageRequest.of(0, 99999));
        List<ServiceItemBenefit> content = serviceItemBenefits.getContent();
        ServiceItemBenefitTemplate serviceItemBenefitTemplate = serviceItemBenefitTemplateService.findOne(toTemplateId).orElse(null);
        if(isRemove){
            Page<ServiceItemBenefit> deleteRulesFromTemplate = search("planTemplate.id:" + toTemplateId, PageRequest.of(0, 99999));
            if(deleteRulesFromTemplate!=null && !deleteRulesFromTemplate.isEmpty()) {
                deleteRulesFromTemplate.stream().forEach(serviceItemBenefit -> serviceItemBenefitRepository.deleteOne(serviceItemBenefit.getId()));
            }
        }
        if(serviceItemBenefitTemplate==null){
            throw  new CustomParameterizedException("1012","No Template found for "+serviceItemBenefitTemplate.getTemplateName(),"template");
        }
        if (content != null && !content.isEmpty()) {
            List<ServiceItemBenefit> collect = content.parallelStream().map(serviceItemBenefit -> {
                ServiceItemBenefit serviceItemBenefitNew = null;
                try {
                    String valueAsString = objectMapper.writeValueAsString(serviceItemBenefit);
                    serviceItemBenefitNew = objectMapper.readValue(valueAsString, ServiceItemBenefit.class);
                    serviceItemBenefitNew.setId(null);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                Set<ServiceItemException> serviceItemExceptionSet = serviceItemBenefit.getServiceItemExceptions().stream().map(serviceItemException -> {
                    serviceItemException.setId(null);
                    return serviceItemException;
                }).collect(Collectors.toSet());
                ExceptionSponsor exceptionSponsor = serviceItemBenefit.getExceptionSponsor();
                if (exceptionSponsor != null) {
                    exceptionSponsor.setId(null);
                }
                serviceItemBenefitNew.setPlanTemplate(serviceItemBenefitTemplate);
                serviceItemBenefitNew.setServiceItemExceptions(serviceItemExceptionSet);
                serviceItemBenefitNew.setExceptionSponsor(exceptionSponsor);
                return serviceItemBenefitNew;
            }).collect(Collectors.toList());
            serviceItemBenefitList = saveAll(collect);
        } else {
            throw new CustomParameterizedException("1013","No Rules is preset to copy from ",fromTemplate.getTemplateName());
        }
        return serviceItemBenefitList;
    }
}
