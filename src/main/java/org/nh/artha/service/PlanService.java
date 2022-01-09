package org.nh.artha.service;

import org.nh.artha.domain.Plan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Plan}.
 */
public interface PlanService {

    /**
     * Save a plan.
     *
     * @param plan the entity to save.
     * @return the persisted entity.
     */
    Plan save(Plan plan);

    /**
     * Get all the plans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Plan> findAll(Pageable pageable);

    /**
     * Get the "id" plan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Plan> findOne(Long id);

    /**
     * Delete the "id" plan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the plan corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Plan> search(String query, Pageable pageable);
    List<Plan> uploadPlanData(MultipartFile file) throws Exception;
    List<Plan> saveAll(List<Plan> planList);
    void doIndex(int pageNo, int pageSize, LocalDate fromDate, LocalDate toDate);

}
