package org.nh.artha.service;

import org.nh.artha.domain.InvoiceDoctorPayout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link InvoiceDoctorPayout}.
 */
public interface InvoiceDoctorPayoutService {

    /**
     * Save a invoiceDoctorPayout.
     *
     * @param invoiceDoctorPayout the entity to save.
     * @return the persisted entity.
     */
    InvoiceDoctorPayout save(InvoiceDoctorPayout invoiceDoctorPayout);

    /**
     * Get all the invoiceDoctorPayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceDoctorPayout> findAll(Pageable pageable);


    /**
     * Get the "id" invoiceDoctorPayout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceDoctorPayout> findOne(Long id);

    /**
     * Delete the "id" invoiceDoctorPayout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the invoiceDoctorPayout corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceDoctorPayout> search(String query, Pageable pageable);

    List<InvoiceDoctorPayout> saveAll(List<InvoiceDoctorPayout> invoiceDoctorPayouts);
}
