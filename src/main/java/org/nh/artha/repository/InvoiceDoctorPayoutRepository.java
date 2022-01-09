package org.nh.artha.repository;
import org.nh.artha.domain.InvoiceDoctorPayout;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Spring Data  repository for the InvoiceDoctorPayout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceDoctorPayoutRepository extends JpaRepository<InvoiceDoctorPayout, Long> {

    @Query(value = "select invoiceDoctorPayout from InvoiceDoctorPayout invoiceDoctorPayout where  doctorPayout.id in :doctorPayoutList")
    List<InvoiceDoctorPayout> getAllInvoiceByDoctorPayout(@Param("doctorPayoutList") List<Long> doctorPayoutList);

    @Query(value = "select count(*) from InvoiceDoctorPayout invoiceDoctorPayout where  doctorPayout.id in :doctorPayoutList")
    Integer getAllInvoiceByDoctorPayoutCount(@Param("doctorPayoutList") List<Long> doctorPayoutList);

    @Query(value = "delete from InvoiceDoctorPayout invoiceDoctorPayout where  doctorPayout.id = :doctorPayoutId")
    @Modifying
    @Transactional
    void deleteAllInvoiceByDoctorPayout(@Param("doctorPayoutId") Long doctorPayoutId);

}
