package org.nh.artha.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the UserOrganizationMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserOrganizationMappingRepository {

    /*@Query("select userOrganizationMapping from UserOrganizationMapping userOrganizationMapping where userOrganizationMapping.user.login = ?#{principal.username}")
    List<UserOrganizationMapping> findByUserIsCurrentUser();

    @Query("select userOrganizationMapping from UserOrganizationMapping userOrganizationMapping where userOrganizationMapping.preferred = true and userOrganizationMapping.user.login = ?1")
    List<UserOrganizationMapping> findByPreferredAndUsername(String username);

    @Query("select userOrganizationMapping from UserOrganizationMapping userOrganizationMapping where  userOrganizationMapping.user.login = ?#{principal.username} and   userOrganizationMapping.organization.id = :organizationId")
    UserOrganizationMapping getUserByOrganizationAndByUserId(@Param("organizationId") Long organizationId);

    @Query(value = "select count(userOrganizationMapping.id) from user_organization_mapping userOrganizationMapping where userOrganizationMapping.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate")LocalDate toDate);

    @Query(value = "select * from user_organization_mapping userOrganizationMapping where userOrganizationMapping.iu_datetime between :fromDate AND :toDate order by userOrganizationMapping.iu_datetime", nativeQuery = true)
    List<UserOrganizationMapping> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);*/
}
