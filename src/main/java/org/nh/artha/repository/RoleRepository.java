package org.nh.artha.repository;

import org.nh.artha.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data  repository for the Role entity.
 */
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("select distinct role from Role role left join fetch role.roles")
    List<Role> findAllWithEagerRelationships();

    @Query("select role from Role role left join fetch role.roles where role.id =:id")
    Role findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select r from Role r, UserOrganizationRoleMapping urom where r.active = true and urom.role = r.id and urom.organization.id = ?2 and urom.user.id = ?1")
    List<Role> getRolesOfUserInAnOrganization(Long userId, Long organizationId);

    @Query(value = "select m.name as modulename, f.type as featuretype, f.name as featurename,m.display_order as moduledisplayorder, f.display_order as featuredisplayorder, m.id as moduleid, f.menue_link as menulink from module m inner join feature f on f.module_id = m.id inner join privilege p on p.feature_id = f.id where m.active = true and f.active = true and p.active = true and p.id in (select rp.privilege_id from role r inner join role_privilege rp on r.id = rp.role_id where r.id in (?1)) group by m.name,f.type,f.name,m.display_order, f.display_order, m.id, f.menue_link order by m.display_order desc", nativeQuery = true)
    List getAllMenusForRoles(Set<Long> roles);

    @Query(value = "select count(role.id) from role role where role.iu_datetime between :fromDate AND :toDate", nativeQuery = true)
    long getTotalRecord(@Param("fromDate") LocalDate fromDate, @Param("toDate")LocalDate toDate);

    @Query(value = "select * from role role where role.iu_datetime between :fromDate AND :toDate order by role.iu_datetime", nativeQuery = true)
    List<Role> findByDateRangeSortById(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

}
