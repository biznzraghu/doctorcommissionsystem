package org.nh.artha.service;

import org.nh.artha.domain.Organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link Organization}.
 */
public interface OrganizationService {

    /**
     * Save a organization.
     *
     * @param organization the entity to save.
     * @return the persisted entity.
     */
    Organization save(Organization organization);

    /**
     * Get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Organization> findAll(Pageable pageable);


    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Organization> findOne(Long id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the organization corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Organization> search(String query, Pageable pageable);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    List<Organization> uploadOrganizationData(MultipartFile file) throws Exception;

    Page<Organization> searchForAllSponsors(String query, Pageable pageable);

    List<Organization> saveAll(List<Organization> organization);

    Map<String, String> exportUnit(String query, Pageable pageable) throws IOException;
}
