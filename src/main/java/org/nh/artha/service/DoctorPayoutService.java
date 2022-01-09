package org.nh.artha.service;

import org.nh.artha.domain.DoctorPayout;

import org.nh.artha.domain.MisReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link DoctorPayout}.
 */
public interface DoctorPayoutService {

    /**
     * Save a doctorPayout.
     *
     * @param doctorPayout the entity to save.
     * @return the persisted entity.
     */
    DoctorPayout save(DoctorPayout doctorPayout);

    /**
     * Get all the doctorPayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoctorPayout> findAll(Pageable pageable);


    /**
     * Get the "id" doctorPayout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DoctorPayout> findOne(Long id);

    /**
     * Delete the "id" doctorPayout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the doctorPayout corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoctorPayout> search(String query, Pageable pageable);


    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    Map<String,Object> exportDoctorPayoutBreakUpSummary(String query, Pageable pageable)  throws  Exception;

    Map<String,Object> exportDoctorPayoutBreakup(String query, Pageable pageable)  throws  Exception;

    List<DoctorPayout> saveAll(List<DoctorPayout> doctorPayout);

    Map<String, String> exportItemServiceWisePayoutBreakUp(String unitCode, Integer year, Integer month, MisReport misReport) throws IOException;
}
