package org.nh.artha.service;

import org.nh.artha.domain.ServiceMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ServiceMaster}.
 */
public interface ServiceMasterService {

    /**
     * Save a serviceMaster.
     *
     * @param serviceMaster the entity to save.
     * @return the persisted entity.
     */
    ServiceMaster save(ServiceMaster serviceMaster);

    /**
     * Get all the serviceMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceMaster> findAll(Pageable pageable);


    /**
     * Get the "id" serviceMaster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceMaster> findOne(Long id);

    /**
     * Delete the "id" serviceMaster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the serviceMaster corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceMaster> search(String query, Pageable pageable);

    Page<ServiceMaster> searchForAllService(String query, Pageable pageable);

    Page<ServiceMaster> searchForServiceGroup(String query, Pageable pageable);

    Page<ServiceMaster> searchForServiceType(String query, Pageable pageable);

    void importService(MultipartFile file) throws  Exception;

    List<ServiceMaster> saveAll(List<ServiceMaster> serviceMasterList);
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);
}
