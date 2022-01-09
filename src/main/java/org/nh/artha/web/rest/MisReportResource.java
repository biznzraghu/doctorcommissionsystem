package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.repository.MisReportRepository;
import org.nh.artha.repository.search.MisReportSearchRepository;
import org.nh.artha.service.MisReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class MisReportResource {
    private final Logger log = LoggerFactory.getLogger(MisReportResource.class);

    private final MisReportSearchRepository misReportSearchRepository;

    private final MisReportRepository misReportRepository;

    private final ApplicationProperties applicationProperties;

    private final MisReportService misReportService;

    public MisReportResource(MisReportSearchRepository misReportSearchRepository,MisReportRepository misReportRepository,
                             ApplicationProperties applicationProperties, MisReportService misReportService){
        this.misReportRepository=misReportRepository;
        this.misReportSearchRepository=misReportSearchRepository;
        this.applicationProperties=applicationProperties;
        this.misReportService=misReportService;
    }



    @GetMapping("/_index/misreport")
    public ResponseEntity<Void> indexMisReport(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Mis Report");
        long resultCount = misReportRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            misReportService.doIndex(i, pageSize, fromDate, toDate);
        }
        return new ResponseEntity("Successfully MisReport indexed" , new HttpHeaders(), HttpStatus.OK);    }

}
