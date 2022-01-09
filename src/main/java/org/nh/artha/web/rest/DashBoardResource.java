package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.service.DashBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);

    private final ApplicationProperties applicationProperties;

    private final DashBoardService dashBoardService;

    public DashBoardResource(ApplicationProperties applicationProperties, DashBoardService dashBoardService) {
        this.applicationProperties = applicationProperties;
        this.dashBoardService = dashBoardService;
    }

    @GetMapping("/doctor-wise-revenue-trend")
    public Map<String, Object> getDoctorWiseRevenueTrend(@RequestParam("unitCode") String unitCode, @RequestParam("format") String format,
                                                         @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                         @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws IOException, ParseException {
        log.debug("Request for getDoctorWiseRevenueTrend {}", unitCode, format, fromDate, toDate);
        return dashBoardService.getDoctorWiseRevenueTrend(unitCode, format, fromDate, toDate);

    }

    @GetMapping("/department-wise-revenue-trend")
    public Map<String, Object> getDepartmentWiseRevenueTrend(@RequestParam("unitCode") String unitCode, @RequestParam("format") String format,
                                                         @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                                         @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) throws IOException, ParseException {
        log.debug("Request for getDoctorWiseRevenueTrend {}", unitCode, format, fromDate, toDate);
        return dashBoardService.getDepartmentWiseRevenueTrend(unitCode, format, fromDate, toDate);

    }
}
