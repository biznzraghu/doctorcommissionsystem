package org.nh.artha.repository;

import org.nh.artha.domain.UserOrganizationDepartmentMapping;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the UserOrganizationDepartmentMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserOrganizationDepartmentMappingRepository extends JpaRepository<UserOrganizationDepartmentMapping, Long> {

    @Query(value = "select count(userOrganizationDepartmentMapping.id) from user_org_dept_mapping userOrganizationDepartmentMapping where userOrganizationDepartmentMapping.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


    @Query(value = "select * from user_org_dept_mapping userOrganizationDepartmentMapping where userOrganizationDepartmentMapping.iu_datetime between :fromDate AND :toDate order by userOrganizationDepartmentMapping.iu_datetime", nativeQuery = true)
    List<UserOrganizationDepartmentMapping> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query("select userOrganizationDepartmentMapping from UserOrganizationDepartmentMapping userOrganizationDepartmentMapping where userMaster.employeeNumber = ?1")
    List<UserOrganizationDepartmentMapping> findByPreferredAndUsername(String username);

    @Query("select userOrganizationMapping from UserOrganizationDepartmentMapping userOrganizationMapping where  userOrganizationMapping.userMaster.employeeNumber = ?#{principal.username} and   userOrganizationMapping.organization.id = :organizationId")
    UserOrganizationDepartmentMapping getUserByOrganization(@Param("organizationId") Long organizationId);

    @Query("select userOrganizationMapping from UserOrganizationDepartmentMapping userOrganizationMapping where userOrganizationMapping.userMaster.employeeNumber = ?#{principal.username}")
    List<UserOrganizationDepartmentMapping> findByUserIsCurrentUser();

    @Query("select userDepartmentMapping from UserOrganizationDepartmentMapping userDepartmentMapping where userMaster.id = :userMasterId and organization.id= :organizationId")
    UserOrganizationDepartmentMapping getAllDepartmentByEmployeeid(@Param("userMasterId") Long userMasterId, @Param("organizationId") Long organization);

    @Query("select userDepartmentMapping from UserOrganizationDepartmentMapping userDepartmentMapping where userMaster.employeeNumber = :login and organization.code= :organizationCode")
    UserOrganizationDepartmentMapping filterAllDepartmentByUserOrganization(@Param("login") String login, @Param("organizationCode") String organizationCode);

}
