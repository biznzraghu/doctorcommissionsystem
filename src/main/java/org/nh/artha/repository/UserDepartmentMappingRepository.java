package org.nh.artha.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the UserDepartmentMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDepartmentMappingRepository /*extends JpaRepository<UserDepartmentMapping, Long>*/ {

   /* @Query("select userDepartmentMapping from UserDepartmentMapping userDepartmentMapping where usermaster.id = :usermaster")
    List<UserDepartmentMapping> getAllDepartmentByEmployeeid(@Param("usermaster") Long usermaster);*/
}
