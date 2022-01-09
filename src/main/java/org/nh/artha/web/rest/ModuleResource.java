package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Module;
import org.nh.artha.repository.ModuleRepository;
import org.nh.artha.repository.search.ModuleSearchRepository;
import org.nh.artha.service.ModuleService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.Module}.
 */
@RestController
@RequestMapping("/api")
public class ModuleResource {

    private final Logger log = LoggerFactory.getLogger(ModuleResource.class);

    private static final String ENTITY_NAME = "module";

    private final ModuleSearchRepository moduleSearchRepository;

    private final ModuleRepository moduleRepository;

    private final ApplicationProperties applicationProperties;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModuleService moduleService;

    public ModuleResource(ModuleService moduleService,ModuleSearchRepository moduleSearchRepository,ModuleRepository moduleRepository,
                          ApplicationProperties applicationProperties) {
        this.moduleService = moduleService;
        this.moduleRepository= moduleRepository;
        this.moduleSearchRepository = moduleSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * {@code POST  /modules} : Create a new module.
     *
     * @param module the module to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new module, or with status {@code 400 (Bad Request)} if the module has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/modules")
    public ResponseEntity<Module> createModule(@Valid @RequestBody Module module) throws URISyntaxException {
        log.debug("REST request to save Module : {}", module);
        if (module.getId() != null) {
            throw new BadRequestAlertException("A new module cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Module result = moduleService.save(module);
        return ResponseEntity.created(new URI("/api/modules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /modules} : Updates an existing module.
     *
     * @param module the module to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated module,
     * or with status {@code 400 (Bad Request)} if the module is not valid,
     * or with status {@code 500 (Internal Server Error)} if the module couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/modules")
    public ResponseEntity<Module> updateModule(@Valid @RequestBody Module module) throws URISyntaxException {
        log.debug("REST request to update Module : {}", module);
        if (module.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Module result = moduleService.save(module);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, module.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /modules} : get all the modules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modules in body.
     */
    @GetMapping("/modules")
    public List<Module> getAllModules() {
        log.debug("REST request to get all Modules");
        return moduleService.findAll();
    }

    /**
     * {@code GET  /modules/:id} : get the "id" module.
     *
     * @param id the id of the module to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the module, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/modules/{id}")
    public ResponseEntity<Module> getModule(@PathVariable Long id) {
        log.debug("REST request to get Module : {}", id);
        Optional<Module> module = moduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(module);
    }

    /**
     * {@code DELETE  /modules/:id} : delete the "id" module.
     *
     * @param id the id of the module to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/modules/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        log.debug("REST request to delete Module : {}", id);
        moduleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/modules?query=:query} : search for the module corresponding
     * to the query.
     *
     * @param query the query of the module search.
     * @return the result of the search.
     */
    @GetMapping("/_search/modules")
    public List<Module> searchModules(@RequestParam String query) {
        log.debug("REST request to search Modules for query {}", query);
        return moduleService.search(query);
    }

    @GetMapping("/_index/module")
    public ResponseEntity<Void> indexModule(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Module");
        long resultCount = moduleRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            moduleService.doIndex(i, pageSize, fromDate, toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Artha", "Module Index completed ",fromDate.toString()+" "+toDate.toString())).build();
    }
}
