package org.nh.artha.service.impl;

import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.nh.artha.service.DashBoardService;
import org.nh.artha.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;


@Service
@Transactional
public class DashBoardServiceImp implements DashBoardService {
    private final RestHighLevelClient restHighLevelClient;

    private final Logger log = LoggerFactory.getLogger(DashBoardServiceImp.class);

    private final UserMasterServiceImpl userMasterService;

    private final DepartmentService departmentService;

    DashBoardServiceImp(RestHighLevelClient restHighLevelClient, UserMasterServiceImpl userMasterService,DepartmentService departmentService) {
        this.restHighLevelClient = restHighLevelClient;
        this.userMasterService = userMasterService;
        this.departmentService=departmentService;
    }

    @Override
    public Map<String, Object> getDoctorWiseRevenueTrend(String unitCode, String format, Date fromDate, Date toDate) throws IOException, ParseException {
        log.debug("Request for getDoctorWiseRevenueTrend IMPL", unitCode, format, fromDate, toDate);
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateStr = sm.format(fromDate);
        String toDateStr = sm.format(toDate);
        DateHistogramInterval dateHistogramInterval = null;
        if (format.equalsIgnoreCase("MONTH")) {
            dateHistogramInterval = DateHistogramInterval.MONTH;
        } else if (format.equalsIgnoreCase("WEEK")) {
            dateHistogramInterval = DateHistogramInterval.WEEK;
        } else if (format.equalsIgnoreCase("DAY")) {
            dateHistogramInterval = DateHistogramInterval.DAY;
        }
        MultiSearchRequest request = new MultiSearchRequest();
        SearchRequest invoiceSearchRequest = new SearchRequest("artha_doctorpayout");
        SearchSourceBuilder searchQuery = new SearchSourceBuilder();
        searchQuery.size(0)
            .query(queryStringQuery("unitCode:" + unitCode
                + " doctorPayoutInvoices.invoiceDate:[" + fromDateStr + " TO " + toDateStr + "] ").defaultOperator(Operator.AND))
            .aggregation(AggregationBuilders.dateHistogram("byFormat")
                .field("doctorPayoutInvoices.invoiceDate").calendarInterval(dateHistogramInterval).format("yyyy-MM-dd")
                .minDocCount(0).extendedBounds(new ExtendedBounds(fromDateStr, toDateStr)).subAggregation(AggregationBuilders.terms("byDoctor").field("employeeId").subAggregation(AggregationBuilders.sum("calculated").field("doctorPayoutInvoices.serviceBenefietAmount").missing(0))));
        invoiceSearchRequest.source(searchQuery);
        request.add(invoiceSearchRequest);
        MultiSearchResponse mSearchCollectionResponseForTerm = restHighLevelClient.msearch(request, RequestOptions.DEFAULT);
        Aggregations invoiceAggregations = mSearchCollectionResponseForTerm.getResponses()[0].getResponse().getAggregations();

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> doctorRevenueList = new ArrayList<>();
        ArrayList<String> doctorNameList = new ArrayList<>();
        Histogram aggs = invoiceAggregations.get("byFormat");
        for (Histogram.Bucket bucket : aggs.getBuckets()) {
            String dateKey = bucket.getKeyAsString();
            String convertedDateKey = "";
            if (format.equalsIgnoreCase("MONTH")) {
                LocalDate givenDate = LocalDate.parse(dateKey);
                convertedDateKey = givenDate.getMonth().toString().substring(0, 3) + " " + givenDate.getYear();
            } else if (format.equalsIgnoreCase("WEEK")) {
                SimpleDateFormat sfResult = new SimpleDateFormat("dd/MM");
                Date dateTmp = sm.parse(dateKey);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateTmp);
                cal.add(Calendar.DATE, 6);
                convertedDateKey = sfResult.format(dateTmp) + " to " + sfResult.format(cal.getTime());
            } else if (format.equalsIgnoreCase("DAY")) {
                SimpleDateFormat sfDayResult = new SimpleDateFormat("dd-MM-yyyy");
                convertedDateKey = sfDayResult.format(sm.parse(dateKey));
            }
            keys.add(convertedDateKey);
            if (bucket.getDocCount() > 0) {
                StringBuffer doctorNameBuffer= new StringBuffer();
                StringBuffer revenueBuffer= new StringBuffer();
                Aggregations agg = bucket.getAggregations();
                Terms byDoctor = agg.get("byDoctor");
                byDoctor.getBuckets().stream().forEach(innerBucket -> {
                    doctorNameBuffer.append(userMasterService.findOne((Long) ((Terms.Bucket) innerBucket).getKeyAsNumber()).orElse(null).getDisplayName()+",");
                    double calculated = ((ParsedSum) (innerBucket.getAggregations().get("calculated"))).getValue();
                    revenueBuffer.append(calculated).append(",");

                });
                doctorNameList.add(doctorNameBuffer.toString());
                doctorRevenueList.add(revenueBuffer.toString());
            }else{
                doctorNameList.add("");
                doctorRevenueList.add("");
            }

        }
        result.put("key", keys);
        result.put("doctorName", doctorNameList);
        result.put("doctorRevenue", doctorRevenueList);

        return result;
    }
    @Override
    public Map<String, Object> getDepartmentWiseRevenueTrend(String unitCode, String format, Date fromDate, Date toDate) throws IOException, ParseException {
        log.debug("Request for getDoctorWiseRevenueTrend IMPL", unitCode, format, fromDate, toDate);
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateStr = sm.format(fromDate);
        String toDateStr = sm.format(toDate);
        DateHistogramInterval dateHistogramInterval = null;
        if (format.equalsIgnoreCase("MONTH")) {
            dateHistogramInterval = DateHistogramInterval.MONTH;
        } else if (format.equalsIgnoreCase("WEEK")) {
            dateHistogramInterval = DateHistogramInterval.WEEK;
        } else if (format.equalsIgnoreCase("DAY")) {
            dateHistogramInterval = DateHistogramInterval.DAY;
        }
        MultiSearchRequest request = new MultiSearchRequest();
        SearchRequest invoiceSearchRequest = new SearchRequest("artha_doctorpayoutdepartment");
        SearchSourceBuilder searchQuery = new SearchSourceBuilder();
        searchQuery.size(0)
            .query(queryStringQuery("unitCode:" + unitCode
                + " invoiceDate:[" + fromDateStr + " TO " + toDateStr + "] ").defaultOperator(Operator.AND))
            .aggregation(AggregationBuilders.dateHistogram("byFormat")
                .field("invoiceDate").calendarInterval(dateHistogramInterval).format("yyyy-MM-dd")
                .minDocCount(0).extendedBounds(new ExtendedBounds(fromDateStr, toDateStr)).subAggregation(AggregationBuilders.terms("byDepartment").field("departmentMasterId").subAggregation(AggregationBuilders.sum("calculated").field("amount").missing(0))));
        invoiceSearchRequest.source(searchQuery);
        request.add(invoiceSearchRequest);
        MultiSearchResponse mSearchCollectionResponseForTerm = restHighLevelClient.msearch(request, RequestOptions.DEFAULT);
        Aggregations invoiceAggregations = mSearchCollectionResponseForTerm.getResponses()[0].getResponse().getAggregations();

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> departmentRevenueList = new ArrayList<>();
        ArrayList<String> departmentNameList = new ArrayList<>();
        Histogram aggs = invoiceAggregations.get("byFormat");
        for (Histogram.Bucket bucket : aggs.getBuckets()) {
            String dateKey = bucket.getKeyAsString();
            String convertedDateKey = "";
            if (format.equalsIgnoreCase("MONTH")) {
                LocalDate givenDate = LocalDate.parse(dateKey);
                convertedDateKey = givenDate.getMonth().toString().substring(0, 3) + " " + givenDate.getYear();
            } else if (format.equalsIgnoreCase("WEEK")) {
                SimpleDateFormat sfResult = new SimpleDateFormat("dd/MM");
                Date dateTmp = sm.parse(dateKey);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateTmp);
                cal.add(Calendar.DATE, 6);
                convertedDateKey = sfResult.format(dateTmp) + " to " + sfResult.format(cal.getTime());
            } else if (format.equalsIgnoreCase("DAY")) {
                SimpleDateFormat sfDayResult = new SimpleDateFormat("dd-MM-yyyy");
                convertedDateKey = sfDayResult.format(sm.parse(dateKey));
            }
            keys.add(convertedDateKey);
            if (bucket.getDocCount() > 0) {
                StringBuffer departmentNameBuffer= new StringBuffer();
                StringBuffer revenueBuffer= new StringBuffer();
                Aggregations agg = bucket.getAggregations();
                Terms byDepartment = agg.get("byDepartment");
                byDepartment.getBuckets().stream().forEach(innerBucket -> {
                    departmentNameBuffer.append(departmentService.findOne((Long) ((Terms.Bucket) innerBucket).getKeyAsNumber()).orElse(null).getName()+",");
                    double calculated = ((ParsedSum) (innerBucket.getAggregations().get("calculated"))).getValue();
                    revenueBuffer.append(calculated).append(",");

                });
                departmentNameList.add(departmentNameBuffer.toString());
                departmentRevenueList.add(revenueBuffer.toString());
            }else{
                departmentNameList.add("");
                departmentRevenueList.add("");
            }

        }
        result.put("key", keys);
        result.put("departmentName", departmentNameList);
        result.put("departmentRevenue", departmentRevenueList);

        return result;
    }
}
