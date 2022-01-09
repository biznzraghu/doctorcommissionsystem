package org.nh.artha.repository;

import org.nh.artha.domain.DepartmentHscConsumptionStaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentHscConsumptionRepository extends JpaRepository<DepartmentHscConsumptionStaging,Long> {


}

