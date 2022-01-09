package org.nh.artha.service;

import org.nh.artha.domain.Module;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Module}.
 */
public interface ModuleService {

    /**
     * Save a module.
     *
     * @param module the entity to save.
     * @return the persisted entity.
     */
    Module save(Module module);

    /**
     * Get all the modules.
     *
     * @return the list of entities.
     */
    List<Module> findAll();

    /**
     * Get the "id" module.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Module> findOne(Long id);

    /**
     * Delete the "id" module.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the module corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<Module> search(String query);

    void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate);
}
