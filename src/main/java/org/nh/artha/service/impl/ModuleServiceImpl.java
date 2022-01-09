package org.nh.artha.service.impl;

import org.nh.artha.service.ModuleService;
import org.nh.artha.domain.Module;
import org.nh.artha.repository.ModuleRepository;
import org.nh.artha.repository.search.ModuleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Module}.
 */
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);

    private final ModuleRepository moduleRepository;

    private final ModuleSearchRepository moduleSearchRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository, ModuleSearchRepository moduleSearchRepository) {
        this.moduleRepository = moduleRepository;
        this.moduleSearchRepository = moduleSearchRepository;
    }

    /**
     * Save a module.
     *
     * @param module the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Module save(Module module) {
        log.debug("Request to save Module : {}", module);
        Module result = moduleRepository.save(module);
        moduleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the modules.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Module> findAll() {
        log.debug("Request to get all Modules");
        return moduleRepository.findAll();
    }

    /**
     * Get one module by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Module> findOne(Long id) {
        log.debug("Request to get Module : {}", id);
        return moduleRepository.findById(id);
    }

    /**
     * Delete the module by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Module : {}", id);
        moduleRepository.deleteById(id);
        moduleSearchRepository.deleteById(id);
    }

    /**
     * Search for the module corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Module> search(String query) {
        log.debug("Request to search Modules for query {}", query);
        return StreamSupport
            .stream(moduleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public void doIndex(int i, int pageSize, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to do elastic index on Module");
        List<Module> data = moduleRepository.findByDateRangeSortById(fromDate, toDate,  PageRequest.of(i, pageSize));
        if (!data.isEmpty()) {
            moduleSearchRepository.saveAll(data);
        }
    }

}
