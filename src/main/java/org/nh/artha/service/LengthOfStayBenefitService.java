package org.nh.artha.service;

import org.nh.artha.domain.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link LengthOfStayBenefit}.
 */
public interface LengthOfStayBenefitService {

    /**
     * Save a lengthOfStayBenefit.
     *
     * @param lengthOfStayBenefit the entity to save.
     * @return the persisted entity.
     */
    LengthOfStayBenefit save(LengthOfStayBenefit lengthOfStayBenefit);

    /**
     * Get all the lengthOfStayBenefits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LengthOfStayBenefit> findAll(Pageable pageable);

    /**
     * Get the "id" lengthOfStayBenefit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LengthOfStayBenefit> findOne(Long id);

    /**
     * Delete the "id" lengthOfStayBenefit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the lengthOfStayBenefit corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LengthOfStayBenefit> search(String query, Pageable pageable);

    List<LengthOfStayBenefit> saveAll(List<LengthOfStayBenefit> lengthOfStayBenefit);

    Map<LengthOfStayBenefit,List<ServiceAnalysisStaging>> findPatientBasedOnLosBenefit(VariablePayout variablePayout);


}
