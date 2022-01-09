package org.nh.artha.service;

import org.nh.artha.domain.UserMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserMaster}.
 */
public interface UserMasterService {

    /**
     * Save a userMaster.
     *
     * @param userMaster the entity to save.
     * @return the persisted entity.
     */
    UserMaster save(UserMaster userMaster);

    /**
     * Get all the userMasters.
     *
     * @return the list of entities.
     */
    List<UserMaster> findAll();


    /**
     * Get the "id" userMaster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserMaster> findOne(Long id);

    /**
     * Delete the "id" userMaster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the userMaster corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<UserMaster> search(String query);

    /**
     * Do elastic index for city.
     *
     */
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    void importUser(MultipartFile file) throws  Exception;

    List<UserMaster> saveAll(List<UserMaster> userMasterList);

    Map<String, String> exportUserMaster(String query, Pageable pageable) throws IOException;
}
