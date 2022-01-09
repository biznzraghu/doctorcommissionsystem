package org.nh.artha.repository;

import org.nh.artha.domain.ServiceAnalysisStaging;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Repository
public interface ServiceAnalysisRepository extends JpaRepository< ServiceAnalysisStaging,Long> {

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where visitTypeTariffClassKey = :visitTypeTariffClassKey")
    List<ServiceAnalysisStaging> fetchServiceBasedOnRuleKey(@Param("visitTypeTariffClassKey") String visitTypeTariffClassKey);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where orderingDoctorLogin = :employeeId  and type = :type  and itemType =:itemType and unitCode =:unitCode and orderingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> fetchAllSARDataByTypeKey(@Param("employeeId") String employeeId,@Param("type") String type,@Param("itemType") String itemType,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where renderingDoctorLogin = :employeeId  and type = :type  and itemType =:itemType and unitCode =:unitCode and renderingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> fetchAllSARDataByTypeKeyForRenderingConsultant(@Param("employeeId") String employeeId,@Param("type") String type,@Param("itemType") String itemType,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorlogin = :employeeId  and type = :type  and itemType =:itemType and unitCode =:unitCode and admittingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> fetchAllSARDataByTypeKeyForAdmittingConsultant(@Param("employeeId") String employeeId,@Param("type") String type,@Param("itemType") String itemType,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where  type = :type  and itemType =:itemType and unitCode =:unitCode and executed = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> fetchAllSARDataByTypeKeyForConsultant(@Param("type") String type,@Param("itemType") String itemType,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where orderingDoctorLogin = :employeeId and type = :type and unitCode =:unitCode and orderingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> allServiceAnalysis(@Param("employeeId") String orderingDoctorLogin ,@Param("type") String type,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where renderingDoctorLogin = :employeeId and type = :type and unitCode =:unitCode and renderingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> allServiceAnalysisForRenderingConsultant(@Param("employeeId") String orderingDoctorLogin ,@Param("type") String type,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorlogin = :employeeId and type = :type and unitCode =:unitCode and admittingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> allServiceAnalysisForAdmittingConsultant(@Param("employeeId") String orderingDoctorLogin ,@Param("type") String type,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where  type = :type and unitCode =:unitCode and executed = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> allServiceAnalysisForConsultant(@Param("type") String type,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where orderingDoctorLogin = :employeeId and type = :type  and itemCode = :itemCode and orderingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> servicesByName(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("itemCode") String itemCode,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where renderingDoctorLogin = :employeeId and type = :type  and itemCode = :itemCode and renderingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> servicesByNameForRenderingConsultant(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("itemCode") String itemCode,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorlogin = :employeeId and type = :type  and itemCode = :itemCode and admittingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> servicesByNameForAdmittingConsultant(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("itemCode") String itemCode,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where  type = :type  and itemCode = :itemCode and executed = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> servicesByNameForConsultant(@Param("type") String type,@Param("itemCode") String itemCode,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select sum(netAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and visitType in (:visitType) and departmentRevenueExecuted = false")
    BigDecimal allServiceAnalysisForDepartmentRevenue(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("visitType") List<String> visitType);

    @Transactional
    @Modifying
    @Query(value = "update ServiceAnalysisStaging set departmentRevenueExecuted= true where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and visitType in (:visitType)")
    void updateServiceAnalysisForDepartmentRevenue(@Param("unitCode") String unitCode ,@Param("orderingDepartmentCode") String departmentCode,@Param("visitType") List<String> visitType);

    @Query(value = "select sum(netAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode  and visitType in (:visitType) and employeeId in :employeeList and departmentRevenueExecuted = false")
    BigDecimal allServiceAnalysisForDepartmentRevenueForConsultant(@Param("unitCode") String unitCode ,  @Param("visitType") List<String> visitType,@Param("employeeList")List<Long> employeList);

    @Query(value = "select sum(grossAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode  and visitType in (:visitType) and employeeId in :employeeList and departmentRevenueExecuted = false")
    BigDecimal allGrossServiceAnalysisForDepartmentRevenueForConsultant(@Param("unitCode") String unitCode ,  @Param("visitType") List<String> visitType,@Param("employeeList")List<Long> employeList);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where  unitCode =:unitCode and  employeeId =:employeeId and departmentCode =:departmentCode and itemCode in :itemCodeList")
    List<ServiceAnalysisStaging> filterSarReportForLOSCalculation(@Param("unitCode") String unitCode,@Param("employeeId") Long employeeId,@Param("departmentCode") String departmentCode,@Param("itemCodeList") List<String> itemCodeList);

    @Query(value = "update ServiceAnalysisStaging set lengthOfStayExecuted =true where  unitCode =:unitCode and  employeeId =:employeeId and departmentCode =:departmentCode and itemCode in :itemCodeList")
    List<ServiceAnalysisStaging> updateLosFlag(@Param("unitCode") String unitCode,@Param("employeeId") Long employeeId,@Param("departmentCode") String departmentCode,@Param("itemCodeList") List<String> itemCodeList);


    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where renderingDoctorLogin = :employeeId and type = :type  and itemGroup = :itemGroup and renderingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> serviceGroupForRenderingConsultant(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("itemGroup") String itemGroup,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where orderingDoctorLogin = :employeeId and type = :type  and itemGroup = :itemGroup and orderingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> servicesGroupForOrdering(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("itemGroup") String itemGroup,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorlogin = :employeeId and type = :type  and itemGroup = :itemGroup and admittingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> servicesGroupForAdmitting(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("itemGroup") String itemGroup,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where  type = :type  and itemGroup = :itemGroup and executed = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> servicesGroupForConsultant(@Param("type") String type,@Param("itemGroup") String itemGroup,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);


    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where renderingDoctorLogin = :employeeId and type = :type  and packageCategory = :packageCategory and renderingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> packageCategoryForRenderingConsultant(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("packageCategory") String itemGroup,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where orderingDoctorLogin = :employeeId and type = :type  and packageCategory = :packageCategory and orderingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> packageCategoryForForOrdering(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("packageCategory") String itemGroup,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorlogin = :employeeId and type = :type  and packageCategory = :packageCategory and admittingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> packageCategoryForForAdmitting(@Param("employeeId") String employeeId ,@Param("type") String type,@Param("packageCategory") String packageCategory,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where  type = :type  and packageCategory = :packageCategory and executed = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> packageCategoryForrConsultant(@Param("type") String type,@Param("packageCategory") String itemGroup,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select sum(grossAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and visitType in (:visitType) and departmentRevenueExecuted = false")
    BigDecimal totalGrossAmountForDepartmentRevenue(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("visitType") List<String> visitType);

    @Query(value = "select sum(netAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and itemType = :itemType and visitType in (:visitType) and departmentRevenueExecuted = false")
    BigDecimal allServiceAnalysisForDepartmentRevenueOnInvoiceType(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("itemType") String itemType,@Param("visitType") List<String> visitType);

    @Query(value = "select sum(grossAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and itemType = :itemType and visitType in (:visitType) and departmentRevenueExecuted = false")
    BigDecimal totalGrossAmountForDepartmentRevenueOnInvoiceType(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("itemType") String itemType,@Param("visitType") List<String> visitType);


    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode  and visitType in (:visitType) and employeeId in :employeeList and year= :year and month= :month")
    List<ServiceAnalysisStaging> allServiceAnalysisForDepartmentRevenueForBreakUp(@Param("unitCode") String unitCode ,  @Param("visitType") List<String> visitType,@Param("employeeList")List<Long> employeList,@Param("year") Integer year,
                                                                                  @Param("month") Integer month);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode  and visitType in (:visitType) and employeeId in :employeeList and year= :year and month= :month")
    List<ServiceAnalysisStaging> allGrossServiceAnalysisForDepartmentRevenueForBreakUp(@Param("unitCode") String unitCode ,  @Param("visitType") List<String> visitType,@Param("employeeList")List<Long> employeList,@Param("year") Integer year,
                                                                                       @Param("month") Integer month);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and visitType in (:visitType) and year= :year and month= :month")
    List<ServiceAnalysisStaging> allServiceAnalysisForDepartmentRevenueForBreakUp(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("visitType") List<String> visitType,@Param("year") Integer year,
                                                                                  @Param("month") Integer month);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and itemType = :itemType and visitType in (:visitType) and year= :year and month= :month")
    List<ServiceAnalysisStaging> allServiceAnalysisForDepartmentRevenueOnInvoiceTypeForBreakUp(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("itemType") String itemType,@Param("visitType") List<String> visitType,@Param("year") Integer year,
                                                                                               @Param("month") Integer month);

    @Query(value = "select sum(netAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode  and visitType in (:visitType) and employeeId in :employeeList and departmentRevenueExecuted = true")
    BigDecimal allServiceAnalysisForDepartmentRevenueForConsultantExecuted(@Param("unitCode") String unitCode ,  @Param("visitType") List<String> visitType,@Param("employeeList")List<Long> employeList);

    @Query(value = "select sum(grossAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode  and visitType in (:visitType) and employeeId in :employeeList and departmentRevenueExecuted = true")
    BigDecimal allGrossServiceAnalysisForDepartmentRevenueForConsultantExecuted(@Param("unitCode") String unitCode ,  @Param("visitType") List<String> visitType,@Param("employeeList")List<Long> employeList);

    @Query(value = "select sum(netAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and visitType in (:visitType) and departmentRevenueExecuted = true")
    BigDecimal allServiceAnalysisForDepartmentRevenueExecuted(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("visitType") List<String> visitType);

    @Query(value = "select sum(netAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and itemType = :itemType and visitType in (:visitType) and departmentRevenueExecuted = true")
    BigDecimal allServiceAnalysisForDepartmentRevenueOnInvoiceTypeExecuted(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("itemType") String itemType,@Param("visitType") List<String> visitType);

    @Query(value = "select sum(grossAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and visitType in (:visitType) and departmentRevenueExecuted = true")
    BigDecimal totalGrossAmountForDepartmentRevenueExecuted(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("visitType") List<String> visitType);


    @Query(value = "select sum(grossAmount) from ServiceAnalysisStaging serviceAnalysisStaging where unitCode = :unitCode and orderingDepartmentCode = :orderingDepartmentCode and itemType = :itemType and visitType in (:visitType) and departmentRevenueExecuted = true")
    BigDecimal totalGrossAmountForDepartmentRevenueOnInvoiceTypeExecuted(@Param("unitCode") String unitCode , @Param("orderingDepartmentCode") String orderingDepartmentCode, @Param("itemType") String itemType,@Param("visitType") List<String> visitType);


    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where orderingDoctorLogin = :employeeId and type = :type  and itemCode = :itemCode and orderingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass and referPackageId is not null")
    List<ServiceAnalysisStaging> fetchServiceInsidePackageForOrdering(@Param("employeeId") String employeeNumber,@Param("itemCode") String itemCode,@Param("type") String type,@Param("unitCode") String unitCode, @Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where renderingDoctorLogin  = :employeeId and type = :type  and itemCode = :itemCode and renderingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass and referPackageId is not null")
    List<ServiceAnalysisStaging> fetchServiceInsidePackageForRendering(@Param("employeeId") String employeeNumber,@Param("itemCode") String itemCode,@Param("type") String type,@Param("unitCode") String unitCode, @Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorLogin  = :employeeId and type = :type  and itemCode = :itemCode and admittingDoctorExecuted = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass and referPackageId is not null")
    List<ServiceAnalysisStaging> fetchServiceInsidePackageForAdmitting(@Param("employeeId") String employeeNumber,@Param("itemCode") String itemCode,@Param("type") String type,@Param("unitCode") String unitCode, @Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where  type = :type  and itemCode = :itemCode and executed = false and unitCode =:unitCode and visitType in :visitType and tariffClass in :tariffClass and referPackageId is not null")
    List<ServiceAnalysisStaging> fetchServiceInsidePackageForConsultant(@Param("itemCode") String itemCode,@Param("type") String type,@Param("unitCode") String unitCode, @Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where  id in :ids")
    List<ServiceAnalysisStaging> getByIdList(@Param("ids")List<Long> idList);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where orderingDoctorLogin = :employeeId  and unitCode =:unitCode and orderingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> allServiceAnalysisForInvoiceMinusAllMaterial(@Param("employeeId") String orderingDoctorLogin ,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where renderingDoctorLogin = :employeeId  and unitCode =:unitCode and renderingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> allServiceAnalysisForRenderingInvoiceMinusAllMaterial(@Param("employeeId") String orderingDoctorLogin ,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorlogin = :employeeId and unitCode =:unitCode and admittingDoctorExecuted = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> allServiceAnalysisForAdmittingInvoiceMinusAllMaterial(@Param("employeeId") String orderingDoctorLogin ,@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where unitCode =:unitCode and executed = false and visitType in :visitType and tariffClass in :tariffClass")
    List<ServiceAnalysisStaging> allServiceAnalysisForConsultantInvoiceMinusAllMaterial(@Param("unitCode") String unitCode,@Param("visitType") List<String> visitType,@Param("tariffClass") List<String> tariffClass);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorlogin = :employeeId and  unitCode =:unitCode and admittingDepartmentCode =:admittingDepartmentCode and lengthOfStayExecuted = false and visitType = :visitType and itemCode in :itemCodeList and lengthOfStayExecuted=false")
    Set<ServiceAnalysisStaging> findMrnBasedOnDepartmentAndServiceCode(@Param("employeeId") String orderingDoctorLogin, @Param("unitCode") String unitCode, @Param("admittingDepartmentCode") String code, @Param("visitType") String visitType, @Param("itemCodeList") List<String> itemCodeList);

    @Query(value = "select serviceAnalysisStaging from ServiceAnalysisStaging serviceAnalysisStaging where admittingDoctorlogin = :employeeId and  unitCode =:unitCode and admittingDepartmentCode =:admittingDepartmentCode and visitType = :visitType and itemCode in :itemCodeList and lengthOfStayExecuted=true")
    Set<ServiceAnalysisStaging> findMrnBasedOnDepartmentAndServiceCodeExecuteOne(@Param("employeeId") String orderingDoctorLogin, @Param("unitCode") String unitCode, @Param("admittingDepartmentCode") String code, @Param("visitType") String visitType, @Param("itemCodeList") List<String> itemCodeList);

    List<ServiceAnalysisStaging> findAll(Specification<ServiceAnalysisStaging> allCriteria);
}


