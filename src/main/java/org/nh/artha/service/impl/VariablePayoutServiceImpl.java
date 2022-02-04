package org.nh.artha.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.ChangeRequestStatus;
import org.nh.artha.repository.DepartmentRepository;
import org.nh.artha.repository.VariablePayoutRepository;
import org.nh.artha.repository.search.UserMasterSearchRepository;
import org.nh.artha.repository.search.VariablePayoutSearchRepository;
import org.nh.artha.service.*;
import org.nh.artha.util.ExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link VariablePayout}.
 */
@Service
@Transactional
public class VariablePayoutServiceImpl implements VariablePayoutService {

    private final Logger log = LoggerFactory.getLogger(VariablePayoutServiceImpl.class);

    private final VariablePayoutRepository variablePayoutRepository;

    private final VariablePayoutSearchRepository variablePayoutSearchRepository;

    private final DepartmentRevenueBenefitService departmentRevenueBenefitService;

    private final LengthOfStayBenefitService lengthOfStayBenefitService;

    private final ServiceItemBenefitService serviceItemBenefitService;

    private final ServiceItemExceptionService serviceItemExceptionService;

    private final ServiceItemBenefitTemplateService serviceItemBenefitTemplateService;

    private final StayBenefitServiceService stayBenefitService;

    private final TransactionApprovalDetailsService transactionApprovalDetailsService;

    private final ArthaSequenceGeneratorService sequenceGeneratorService;

    private final ElasticsearchOperations elasticsearchOperations;

    private final DepartmentRepository departmentRepository;

    private final RestHighLevelClient elasticsearchTemplate;

    private final ApplicationProperties applicationProperties;

    private final UserMasterSearchRepository userMasterSearchRepository;


    public VariablePayoutServiceImpl(VariablePayoutRepository variablePayoutRepository, VariablePayoutSearchRepository variablePayoutSearchRepository,
                                     DepartmentRevenueBenefitService departmentRevenueBenefitService,ArthaSequenceGeneratorService sequenceGeneratorService,
                                     LengthOfStayBenefitService lengthOfStayBenefitService, ServiceItemBenefitService serviceItemBenefitService, ServiceItemExceptionService serviceItemExceptionService,
                                     ServiceItemBenefitTemplateService serviceItemBenefitTemplateService, StayBenefitServiceService stayBenefitService, TransactionApprovalDetailsService transactionApprovalDetailsService,
                                     ElasticsearchOperations elasticsearchOperations,DepartmentRepository departmentRepository,RestHighLevelClient elasticsearchTemplate,ApplicationProperties applicationProperties,
                                     UserMasterSearchRepository userMasterSearchRepository) {
        this.variablePayoutRepository = variablePayoutRepository;
        this.variablePayoutSearchRepository = variablePayoutSearchRepository;
        this.departmentRevenueBenefitService = departmentRevenueBenefitService;
        this.lengthOfStayBenefitService = lengthOfStayBenefitService;
        this.serviceItemBenefitService = serviceItemBenefitService;
        this.serviceItemExceptionService = serviceItemExceptionService;
        this.serviceItemBenefitTemplateService = serviceItemBenefitTemplateService;
        this.stayBenefitService = stayBenefitService;
        this.transactionApprovalDetailsService = transactionApprovalDetailsService;
        this.sequenceGeneratorService=sequenceGeneratorService;
        this.elasticsearchOperations=elasticsearchOperations;
        this.departmentRepository=departmentRepository;
        this.elasticsearchTemplate=elasticsearchTemplate;
        this.applicationProperties=applicationProperties;
        this.userMasterSearchRepository=userMasterSearchRepository;

    }

    /**
     * Save a variablePayout.
     *
     * @param variablePayout the entity to save.
     * @return the persisted entity.
     */
    @Override
    @Transactional
    public VariablePayout save(VariablePayout variablePayout) {
        log.debug("Request to save VariablePayout : {}", variablePayout);
        String substring = "var";
            //sequenceGeneratorService.generateNumber("VariablePayout", "NH", variablePayout);
        if(variablePayout.getId()==null && variablePayout.getVariablePayoutId()==null) {
            variablePayout.setVariablePayoutId(Long.parseLong(substring.substring(substring.length() - 4)));
        }
        variablePayout.setDepartmentRevenueBenefits(variablePayout.getDepartmentRevenueBenefits().stream().map(departmentRevenueBenefit ->
        {
            Department departmentByCode = departmentRepository.findDepartmentByCode(departmentRevenueBenefit.getDepartment().getCode());
            departmentRevenueBenefit.setDepartment(departmentByCode);
            departmentRevenueBenefit.variablePayout(variablePayout);
            return departmentRevenueBenefit;
        }).collect(Collectors.toSet()));
        variablePayout.setServiceItemExceptions(variablePayout.getServiceItemExceptions().stream().map(serviceItemException->serviceItemException.variablePayout(variablePayout)).collect(Collectors.toSet()));
        List<LengthOfStayBenefit> collect = variablePayout.getLengthOfStayBenefits().stream().collect(Collectors.toList());
        Set<LengthOfStayBenefit> lengthOfStayBenefitsSet = new HashSet<>();
        for(int k=0;k<collect.size();k++){
            LengthOfStayBenefit lengthOfStayBenefit = collect.get(k);
            Department departmentByCode = departmentRepository.findDepartmentByCode(lengthOfStayBenefit.getDepartment().getCode());
            lengthOfStayBenefit.setDepartment(departmentByCode);
            List<StayBenefitService> stayBenefitServiceList = lengthOfStayBenefit.getStayBenefitServices().stream().collect(Collectors.toList());
            Set<StayBenefitService> collect1 = stayBenefitServiceList.stream().map(stayBenefitService1 -> stayBenefitService1.lengthOfStayBenefit(lengthOfStayBenefit)).collect(Collectors.toSet());
            lengthOfStayBenefit.setStayBenefitServices(collect1);
            lengthOfStayBenefit.setVariablePayout(variablePayout);
            lengthOfStayBenefitsSet.add(lengthOfStayBenefit);
        }
        variablePayout.setLengthOfStayBenefits(lengthOfStayBenefitsSet);
        VariablePayout result = variablePayoutRepository.save(variablePayout);
        if(variablePayout!=null && (variablePayout.getChangeRequestStatus().equals(ChangeRequestStatus.APPROVED)||variablePayout.getChangeRequestStatus().equals(ChangeRequestStatus.REJECTED))){
            serviceItemBenefitService.updateVersion(variablePayout.getVariablePayoutId(),variablePayout.getVersion());
        }
        variablePayoutSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the variablePayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VariablePayout> findAll(Pageable pageable) {
        log.debug("Request to get all VariablePayouts");
        return variablePayoutSearchRepository.findAll(pageable);
    }

    /**
     * Get one variablePayout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VariablePayout> findOne(Long id) {
        log.debug("Request to get VariablePayout : {}", id);
        return variablePayoutSearchRepository.findById(id);
    }

    /**
     * Delete the variablePayout by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VariablePayout : {}", id);
        variablePayoutRepository.deleteById(id);
        variablePayoutSearchRepository.deleteById(id);
    }

    /**
     * Search for the variablePayout corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VariablePayout> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of VariablePayouts for query {}", query);
        return variablePayoutSearchRepository.search(queryStringQuery(query).field("createdBy.displayName").field("version").field("employee.displayName").field("employee.employeeNo"), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on City");
        List<VariablePayout> data = variablePayoutRepository.findByDateRangeSortById(fromDate, toDate, PageRequest.of(pageNo, pageSize));
        if (!data.isEmpty()) {
            data.stream().forEach(variablePayout -> variablePayoutSearchRepository.indexWithoutRefresh(variablePayout));
        }
    }

    @Override
    public VariablePayout update(VariablePayout variablePayout) {
        log.debug("Request to update VariablePayout : {}", variablePayout);
        VariablePayout payout = null;
        if(variablePayout.getId()!=null){
            payout = this.saveAndUpdate(variablePayout);
            return payout;
        }
        else {
            variablePayout.setVersion(variablePayout.getVersion() + 1);
            payout= this.save(variablePayout);
        }
        return payout;
    }

    @Override
    public VariablePayout saveAndUpdate(VariablePayout variablePayout) {
        VariablePayout save = this.save(variablePayout);
        return save;
    }

    @Override
    public List<VariablePayout> getPayouts(String query, Pageable pageable) {
        log.debug("REST request to search for a page of VariablePayouts for query {}", query);
        Page<VariablePayout> page = search(query, pageable);
        List<VariablePayout> content = page.getContent();
        LinkedHashMap<Long, List<VariablePayout>> listMap = content.stream().collect(Collectors.groupingBy(VariablePayout::getVariablePayoutId, LinkedHashMap::new, Collectors.toList()));
        Set<Map.Entry<Long, List<VariablePayout>>> entries = listMap.entrySet();
        Iterator<Map.Entry<Long, List<VariablePayout>>> iterator = entries.iterator();
        List<VariablePayout> returnResult= new ArrayList<>();
        while (iterator.hasNext()){
            Map.Entry<Long, List<VariablePayout>> next = iterator.next();
            List<VariablePayout> value = next.getValue();
            value.sort(Comparator.comparing(VariablePayout::getId));
            VariablePayout currentVersion=value.get(value.size()-1);
            returnResult.add(currentVersion);
        }
        List<Long> latestVariablePayoutIds = returnResult.stream().map(VariablePayout::getId).collect(Collectors.toList());
        Query searchQuery = new NativeSearchQueryBuilder()
            .withQuery(boolQuery().must(termsQuery("id",latestVariablePayoutIds))).build();
        searchQuery.setPageable(pageable);
        List<VariablePayout> artha_variablepayout = elasticsearchOperations.queryForList(searchQuery, VariablePayout.class, IndexCoordinates.of("artha_variablepayout"));
        return artha_variablepayout;
    }

    private Function<LengthOfStayBenefit, List<StayBenefitService>> createStayBenefietObject = (lengthOfStayBenefiet) -> {
        List<org.nh.artha.domain.StayBenefitService> collect = lengthOfStayBenefiet.getStayBenefitServices().stream().map(stayBenefitService1 ->
            stayBenefitService1.lengthOfStayBenefit(lengthOfStayBenefiet)
        ).collect(Collectors.toList());
        return collect;

    };

    private Function<ServiceItemBenefit, List<ServiceItemException>> createSLineLevelExceptionObject = (itemBenefiet) -> {
        List<ServiceItemException> collect = itemBenefiet.getServiceItemExceptions().stream().map(exception ->
            exception.serviceItemBenefit(itemBenefiet)
        ).collect(Collectors.toList());
        return collect;

    };

    @Override
    public List<Integer> getDistinctVersion(String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest("artha_variablepayout");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryStringQuery(query)).size(0).aggregation(AggregationBuilders.terms("unique_version").field("version.raw").size(1000));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = elasticsearchTemplate.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms terms = aggregations.get("unique_version");
        List<Integer> requestList= new ArrayList<>();
        for (Terms.Bucket bucket : terms.getBuckets()) {
            requestList.add(Integer.parseInt(bucket.getKey().toString()));
        }
        return requestList;
    }

    @Override
    public Map<String, String> exportVariablePayout(String query,Pageable pageable) throws IOException {
        Map<String , String> fileDetails= new HashMap<>();
        long totalPages = this.search(query,pageable).getTotalPages();
        File file = ExportUtil.getCSVExportFile("varaible_payout", applicationProperties.getAthmaBucket().getTempExport());
        fileDetails.put("fileName", file.getName());
        fileDetails.put("pathReference", "tempExport");
        FileWriter variablePayoutFileWriter = new FileWriter(file);
        final String[] variablePayoutFileHeader = {"Employee Code", "Employee Name","Status","Version","Contract End Date","Change Request Status","Min Assured Amount","Max Payout Amount"};
        CSVFormat varaiblePayoutFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator()).withQuoteMode(QuoteMode.MINIMAL);
        try (CSVPrinter csvFilePrinter = new CSVPrinter(variablePayoutFileWriter, varaiblePayoutFileFormat)) {
            csvFilePrinter.printRecord(variablePayoutFileHeader);
            for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
                Iterator<VariablePayout> iterator = search(query, pageable).iterator();
                while (iterator.hasNext()) {
                    VariablePayout variablePayout = iterator.next();
                    List variablePayoutPopulateList=new ArrayList();
                    UserMaster userMaster = userMasterSearchRepository.findById(variablePayout.getEmployeeId()).get();
                    variablePayoutPopulateList.add(userMaster.getEmployeeNumber());
                    variablePayoutPopulateList.add(userMaster.getDisplayName());
                    variablePayoutPopulateList.add(variablePayout.isStatus());
                    variablePayoutPopulateList.add(variablePayout.getVersion());
                    variablePayoutPopulateList.add(variablePayout.getContractEndDate()!=null?"":variablePayout.getContractEndDate());
                    variablePayoutPopulateList.add(variablePayout.getChangeRequestStatus().getDisplay());
                    variablePayoutPopulateList.add(variablePayout.getMinAssuredAmount()!=null?"":variablePayout.getMinAssuredAmount());
                    variablePayoutPopulateList.add(variablePayout.getMaxPayoutAmount()!=null?"":variablePayout.getMaxPayoutAmount());
                    csvFilePrinter.printRecord(variablePayoutPopulateList);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        } finally {
            if (variablePayoutFileWriter != null)
                variablePayoutFileWriter.close();
        }
        return fileDetails;
    }
}
