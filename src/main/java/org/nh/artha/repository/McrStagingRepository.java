package org.nh.artha.repository;

import org.nh.artha.domain.McrStaging;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


/**
 * Spring Data  repository for the McrStaging entity.
 */
@SuppressWarnings("unused")
@Repository
public interface McrStagingRepository extends JpaRepository<McrStaging, Long> {

    @Query("select mcrStaging from McrStaging mcrStaging where visitType = :visitType and invoiceNumber = :invoiceNumber")
    List<McrStaging> getMcrDataBasedOnRuleKey(@Param("visitType") String visitType , @Param("invoiceNumber") String invoiceNumber);


    @Query("select mcrStaging from McrStaging mcrStaging where typeEmployeeDepartmentKey = :typeEmployeeDepartmentKey and departmentId in :departmentIdList and invoicePayoutCreated =false")
    List<McrStaging> getMcrDataByTypeEmployeeDepartmentKey(@Param("typeEmployeeDepartmentKey") String typeEmployeeDepartmentKey , @Param("departmentIdList") List<Long> departmentIdList);

    @Modifying
    @Transactional
    @Query("update McrStaging set invoicePayoutCreated='t' where typeEmployeeDepartmentKey = :typeEmployeeDepartmentKey and departmentId in :departmentIdList")
    void updateMatchedMcr(@Param("typeEmployeeDepartmentKey") String typeEmployeeDepartmentKey , @Param("departmentIdList") List<Long> departmentIdList);

    @Query(value = "select mcrStaging from McrStaging mcrStaging  where employeeId = :employeeId and visitTypeDeptAmtMaterial in :visitTypeDeptAmtMaterial and unitCode =:unitCode and executed = false")
    List<McrStaging> allMcrItem(@Param("employeeId") Long employeeId, @Param("visitTypeDeptAmtMaterial") List<String> visitTypeTariffClassKey,@Param("unitCode") String unitCode);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where orderingDoctorLogin = :employeeId  and unitCode =:unitCode and executed = false and visitType in :visitType")
    List<McrStaging> allMcrForOrderingDoctorLogin(@Param("employeeId") String orderingDoctorLogin ,  @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where admittingDoctorlogin = :employeeId  and unitCode =:unitCode and executed = false and visitType in :visitType")
    List<McrStaging> allMcrForAdmittingConsultant(@Param("employeeId") String orderingDoctorLogin , @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where unitCode =:unitCode and executed = false and visitType in :visitType")
    List<McrStaging> allMcrForConsultant(@Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where orderingDoctorLogin = :employeeId   and itemCode = :itemCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemNameByNameForOrderingDoctorLogin(@Param("employeeId") String employeeId ,  @Param("itemCode") String itemCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where itemCode = :itemCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemNameByNameForConsultant(@Param("itemCode") String itemCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where admittingDoctorlogin = :employeeId   and itemCode = :itemCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemByNameForAdmittingConsultant(@Param("employeeId") String employeeId ,  @Param("itemCode") String itemCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);


    @Query(value = "select mcrStaging from McrStaging mcrStaging where orderingDoctorLogin = :employeeId   and billingGroupCode = :billingGroupCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemGroupByNameForOrderingDoctorLogin(@Param("employeeId") String employeeId , @Param("billingGroupCode") String billingGroupCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where admittingDoctorlogin = :employeeId   and billingGroupCode = :billingGroupCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemGroupForAdmittingConsultant(@Param("employeeId") String employeeId ,  @Param("billingGroupCode") String billingGroupCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where  billingGroupCode = :billingGroupCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemGroupForConsultant(  @Param("billingGroupCode") String billingGroupCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);


    @Query(value = "select mcrStaging from McrStaging mcrStaging where orderingDoctorLogin = :employeeId   and itemCategoryCode = :itemCategoryCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemCategoryForOrderingDoctorLogin(@Param("employeeId") String employeeId , @Param("itemCategoryCode") String itemCategoryCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where admittingDoctorlogin = :employeeId   and itemCategoryCode = :itemCategoryCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemCategoryAdmittingConsultant(@Param("employeeId") String employeeId ,  @Param("itemCategoryCode") String billingGroupCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select mcrStaging from McrStaging mcrStaging where itemCategoryCode = :itemCategoryCode and executed = false and unitCode =:unitCode and visitType in :visitType")
    List<McrStaging> itemCategoryConsultant(  @Param("itemCategoryCode") String billingGroupCode, @Param("unitCode") String unitCode, @Param("visitType") List<String> visitType);

    @Query(value = "select sum(item_net_amount) from mcr_staging mcr where mcr.ordering_department_code =:deptCode and mcr.unit_code =:unitCode and mcr.item_type_name=:itemTypeName and visit_type in :visitType and department_executed='false'",nativeQuery = true)
    BigDecimal calcualateNetAmounttMaterailReduction(@Param("deptCode") String deptCode,@Param("unitCode") String unitCode,@Param("itemTypeName") String itemTypeName, @Param("visitType") List<String> visitType);

    @Query(value = "select sum(item_gross_amount) from mcr_staging mcr where mcr.ordering_department_code =:deptCode and mcr.unit_code =:unitCode and mcr.item_type_name=:itemTypeName and visit_type in :visitType and department_executed",nativeQuery = true)
    BigDecimal calcualateGrossAmounttMaterailReduction(@Param("deptCode") String deptCode,@Param("unitCode") String unitCode, @Param("itemTypeName") String itemTypeName, @Param("visitType") List<String> visitType);
    //item_type_name

    @Modifying
    @Query(value = "update mcr_staging mcr set department_executed='true'  where mcr.unit_code =:unitCode and mcr.item_type_name=:itemTypeName and visit_type in :visitType",nativeQuery = true)
    void updateDepartmentMcr(@Param("unitCode") String unitCode, @Param("itemTypeName") String itemTypeName, @Param("visitType") List<String> visitType);

    List<McrStaging> findAll(Specification<McrStaging> mcrStagingSpecification);
}
