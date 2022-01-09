package org.nh.artha.repository;
import org.nh.artha.domain.Role;
import org.nh.artha.domain.UserOrganizationRoleMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the UserOrganizationRoleMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserOrganizationRoleMappingRepository extends JpaRepository<UserOrganizationRoleMapping, Long> {

    @Query("select r from UserOrganizationRoleMapping uorm, Role r where r.active = true and r.id = uorm.role.id and uorm.organization.id = ?1 and uorm.user.id= ?2")
    List<Role> findAllRolesByHospitalAndUserId(Long hospital, Long userId);

    @Query(value = "select count(user_organization_role_mapping.id) from user_organization_role_mapping user_organization_role_mapping where user_organization_role_mapping.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from user_organization_role_mapping user_organization_role_mapping where user_organization_role_mapping.iu_datetime between :fromDate AND :toDate order by user_organization_role_mapping.iu_datetime", nativeQuery = true)
    List<UserOrganizationRoleMapping> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
}
