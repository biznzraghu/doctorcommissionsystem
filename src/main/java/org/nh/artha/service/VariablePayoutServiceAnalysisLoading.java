package org.nh.artha.service;

import org.nh.artha.domain.*;
import org.nh.artha.domain.enumeration.Type;

import java.util.List;
import java.util.Map;

public interface VariablePayoutServiceAnalysisLoading {

    List<ServiceAnalysisStaging> fetchAllServiceAnalysisBasedOnRule(String ruleKey);

    List<VariablePayout> getDoctorsBasedOnUnit(String unitCode);

    List<InvoiceDoctorPayout> saveVariablePayoutData(Map<Long, Map<ServiceItemBenefit, List<ServiceAnalysisStaging>>> data,String unitCode,Integer year,Integer month);

    List<InvoiceDoctorPayout> processSARReportsForServices(Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap,Long employeeId,String unitCode,VariablePayout variablePayout,Integer year,Integer month,
                                                           Boolean throughSchduler);

    List<InvoiceDoctorPayout> processSARReportsForPackage(Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap, Long employeeId,String unitCode,VariablePayout variablePayout,Integer year,Integer month,
                                                          Boolean throughSchduler);

    List<InvoiceDoctorPayout> processMcrReportsForItem(Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap, Long employeeId, String unitCode, VariablePayout variablePayout, Integer year, Integer month,
                                                       Boolean thoughScheduler);

    List<InvoiceDoctorPayout> processSARReportsForInvoice(Map<Type, List<ServiceItemBenefit>> serviceItemTypeListMap, Long employeeId,String unitCode,VariablePayout variablePayout,Integer year,Integer month,
                                                          Boolean throughSchduler);

    void executeDepartmentRevenueCalculation(DepartmentPayout departmentPayout);

    List<InvoiceDoctorPayout> saveVariablePayoutDataForMcr(Map<Long, Map<ServiceItemBenefit, List<McrStaging>>> data,String unitCode,Integer year,Integer month);

    List<DoctorPayoutAdjustment> calculatePayoutAdjustment(String unitCode, DoctorPayout doctorPayoutResult);

    DoctorPayout executeVariablePayoutByYearMonthAndID(String unitCode,Integer year, Integer month,Long employeeId, Long variablePayoutId,DoctorPayout doctorPayout);


}
