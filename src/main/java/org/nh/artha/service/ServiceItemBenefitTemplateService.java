package org.nh.artha.service;

import org.nh.artha.domain.ServiceItemBenefitTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link ServiceItemBenefitTemplate}.
 */
public interface ServiceItemBenefitTemplateService {

    /**
     * Save a serviceItemBenefitTemplate.
     *
     * @param serviceItemBenefitTemplate the entity to save.
     * @return the persisted entity.
     */
    ServiceItemBenefitTemplate save(ServiceItemBenefitTemplate serviceItemBenefitTemplate);

    /**
     * Get all the serviceItemBenefitTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceItemBenefitTemplate> findAll(Pageable pageable);

    /**
     * Get all the serviceItemBenefitTemplates with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ServiceItemBenefitTemplate> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" serviceItemBenefitTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceItemBenefitTemplate> findOne(Long id);

    /**
     * Delete the "id" serviceItemBenefitTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the serviceItemBenefitTemplate corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceItemBenefitTemplate> search(String query, Pageable pageable);

    List<ServiceItemBenefitTemplate> saveAll(List<ServiceItemBenefitTemplate> serviceItemBenefitTemplate);

    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    Map<String, String> export(String query, Pageable pageable) throws IOException;
}
