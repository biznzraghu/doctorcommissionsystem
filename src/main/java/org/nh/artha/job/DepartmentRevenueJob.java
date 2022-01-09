package org.nh.artha.job;


import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.Type;
import org.nh.artha.repository.*;
import org.nh.artha.service.DoctorPayoutService;
import org.nh.artha.service.ServiceItemBenefitService;
import org.nh.artha.service.StagingService;
import org.nh.artha.service.VariablePayoutServiceAnalysisLoading;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DepartmentRevenueJob implements Job {

    @Autowired
    private DepartmentPayoutRepository departmentPayoutRepository;

    @Autowired
    private ServiceAnalysisRepository serviceAnalysisRepository;

    @Autowired
    private DoctorPayoutRepository doctorPayoutRepository;

    @Autowired
    private DoctorPayoutService doctorPayoutService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository;

    @Autowired
    private VariablePayoutRepository variablePayoutRepository;

    @Autowired
    private VariablePayoutServiceAnalysisLoading variablePayoutServiceAnalysisLoading;

    /**
     * execute user producer.
     *
     * @param jobExecutionContext
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
            List<Organization> allOrganization = organizationRepository.findAll();
            allOrganization.forEach(organization -> {
                List<Long> departmentPayoutIdsList = departmentPayoutRepository.fetchAllDepartmentBasedOnUnit(organization.getCode());
                List<DepartmentPayout> departmentPayoutList = departmentPayoutRepository.findAllById(departmentPayoutIdsList);
                departmentPayoutList.forEach(departmentPayout -> {
                    variablePayoutServiceAnalysisLoading.executeDepartmentRevenueCalculation(departmentPayout);
                });
            });

    }


}
