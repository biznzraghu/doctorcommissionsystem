package org.nh.artha.service;

import org.nh.artha.domain.PackageMaster;

import org.nh.artha.domain.ServiceMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PackageMaster}.
 */
public interface PackageMasterService {

    /**
     * Save a packageMaster.
     *
     * @param packageMaster the entity to save.
     * @return the persisted entity.
     */
    PackageMaster save(PackageMaster packageMaster);

    /**
     * Get all the packageMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PackageMaster> findAll(Pageable pageable);


    /**
     * Get the "id" packageMaster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PackageMaster> findOne(Long id);

    /**
     * Delete the "id" packageMaster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the packageMaster corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PackageMaster> search(String query, Pageable pageable);


    void importPackage(MultipartFile file) throws  Exception;

    List<PackageMaster> saveAll(List<PackageMaster> packageMasterList);
}
