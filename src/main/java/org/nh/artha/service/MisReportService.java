package org.nh.artha.service;


import org.nh.artha.domain.MisReport;
import org.nh.artha.domain.dto.MisDowloadFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Service Interface for managing MisReport.
 */
public interface MisReportService {

    /**
     * Save a misReport.
     *
     * @param misReport the entity to save
     * @return the persisted entity
     */
    MisReport save(MisReport misReport, Map filterMap);

    /**
     *  Get all the misReports.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MisReport> findAll(Pageable pageable);

    /**
     *  Get the "id" misReport.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MisReport findOne(Long id);

    /**
     *  Delete the "id" misReport.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * download the misreport
     *
     */
    Map<String,String> download(Long reportId) throws IOException;


    Page<MisReport> search(String query, Pageable pageable);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);
}
