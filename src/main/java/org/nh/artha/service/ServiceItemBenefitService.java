package org.nh.artha.service;

import org.nh.artha.domain.ServiceItemBenefit;

import org.nh.artha.domain.ServiceItemBenefitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ServiceItemBenefit}.
 */
public interface ServiceItemBenefitService {

    /**
     * Save a serviceItemBenefit.
     *
     * @param serviceItemBenefit the entity to save.
     * @return the persisted entity.
     */
    ServiceItemBenefit save(ServiceItemBenefit serviceItemBenefit);

    /**
     * Get all the serviceItemBenefits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceItemBenefit> findAll(Pageable pageable);

    /**
     * Get the "id" serviceItemBenefit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceItemBenefit> findOne(Long id);

    /**
     * Delete the "id" serviceItemBenefit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the serviceItemBenefit corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceItemBenefit> search(String query, Pageable pageable);

    List<ServiceItemBenefit> saveAll(List<ServiceItemBenefit> serviceItemBenefit);


    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

    ServiceItemBenefit createNewVersion(ServiceItemBenefit serviceItemBenefit);


    List<ServiceItemBenefit> getServices(Long variablePayoutId,Integer versionNo,Boolean isApproved,Pageable pageable);

    void updateVersion(Long variablePayoutId,Integer version);

    List<ServiceItemBenefit> copyRule(Long fromId, Long toId,Boolean isCopyFromVariablePayout,Long version);

    List<ServiceItemBenefit> copyRuleFromTemplate(ServiceItemBenefitTemplate fromTemplate, Long toTemplateId,Boolean isRemove);
}


