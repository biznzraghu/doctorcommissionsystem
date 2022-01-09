package org.nh.artha.repository;

import org.nh.artha.domain.InvoiceListStaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvoiceListRepository extends JpaRepository<InvoiceListStaging,Long> {


}

