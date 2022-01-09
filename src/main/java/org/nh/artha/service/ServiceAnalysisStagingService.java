package org.nh.artha.service;

import org.nh.artha.domain.McrStaging;
import org.nh.artha.domain.ServiceAnalysisStaging;
import org.nh.artha.domain.ServiceItemBenefit;
import org.nh.artha.domain.VariablePayout;

import java.util.List;
import java.util.function.Predicate;

public interface ServiceAnalysisStagingService {
    List<ServiceAnalysisStaging> saveAll(List<ServiceAnalysisStaging> serviceAnalysisStagingList);

     default Predicate<ServiceAnalysisStaging> removeSar(List<ServiceAnalysisStaging> serviceAnalysisStagingList) {
        Predicate<ServiceAnalysisStaging> uniqueSarPredicate = serviceAnalysisStaging1 -> !serviceAnalysisStagingList.contains(serviceAnalysisStaging1.getId());
        return uniqueSarPredicate;

    }

    List<ServiceAnalysisStaging> findByCriteriaForSar(ServiceItemBenefit rule, VariablePayout variablePayout, Integer year, Integer month, String empNumber, Boolean throughSchduler,
                                                      String type, List<Long> sarIdList);

    List<McrStaging> findByCriteriaOnMcr(ServiceItemBenefit rule, VariablePayout variablePayout, Integer year, Integer month, String empNumber, Boolean throughSchduler,
                                          String type, List<Long> sarIdList);

}
