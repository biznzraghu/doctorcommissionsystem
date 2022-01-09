package org.nh.artha.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.elasticsearch.index.query.Operator;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserMaster;
import org.nh.artha.repository.UserMasterRepository;
import org.nh.artha.repository.search.UserMasterSearchRepository;
import org.nh.artha.service.UserMasterService;
import org.nh.artha.service.UserService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing {@link org.nh.artha.domain.UserMaster}.
 */
@RestController
@RequestMapping("/api")
public class UserMasterResource {

    private final Logger log = LoggerFactory.getLogger(UserMasterResource.class);

    private static final String ENTITY_NAME = "userMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserMasterService userMasterService;
    private final UserMasterRepository userMasterRepository;
    private final UserMasterSearchRepository userMasterSearchRepository;
    private final ApplicationProperties applicationProperties;

    @Autowired
    private UserService userService;

    public UserMasterResource(UserMasterService userMasterService,UserMasterRepository userMasterRepository,UserMasterSearchRepository userMasterSearchRepository,ApplicationProperties applicationProperties) {
        this.userMasterService = userMasterService;
        this.userMasterRepository=userMasterRepository;
        this.userMasterSearchRepository=userMasterSearchRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /user-masters} : Create a new userMaster.
     *
     * @param userMaster the userMaster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userMaster, or with status {@code 400 (Bad Request)} if the userMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-masters")
    public ResponseEntity<UserMaster> createUserMaster(@Valid @RequestBody UserMaster userMaster) throws URISyntaxException {
        log.debug("REST request to save UserMaster : {}", userMaster);
        if (userMaster.getId() != null) {
            throw new BadRequestAlertException("A new userMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserMaster result = userMasterService.save(userMaster);
        return ResponseEntity.created(new URI("/api/user-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-masters} : Updates an existing userMaster.
     *
     * @param userMaster the userMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMaster,
     * or with status {@code 400 (Bad Request)} if the userMaster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-masters")
    public ResponseEntity<UserMaster> updateUserMaster(@Valid @RequestBody UserMaster userMaster) throws URISyntaxException {
        log.debug("REST request to update UserMaster : {}", userMaster);
        if (userMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserMaster result = userMasterService.save(userMaster);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMaster.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-masters} : get all the userMasters.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userMasters in body.
     */
    @GetMapping("/user-masters")
    public List<UserMaster> getAllUserMasters() {
        log.debug("REST request to get all UserMasters");
        return userMasterService.findAll();
    }

    /**
     * {@code GET  /user-masters/:id} : get the "id" userMaster.
     *
     * @param id the id of the userMaster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userMaster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-masters/{id}")
    public ResponseEntity<UserMaster> getUserMaster(@PathVariable Long id) {
        log.debug("REST request to get UserMaster : {}", id);
        Optional<UserMaster> userMaster = userMasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userMaster);
    }

    /**
     * {@code DELETE  /user-masters/:id} : delete the "id" userMaster.
     *
     * @param id the id of the userMaster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-masters/{id}")
    public ResponseEntity<Void> deleteUserMaster(@PathVariable Long id) {
        log.debug("REST request to delete UserMaster : {}", id);
        userMasterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/user-masters?query=:query} : search for the userMaster corresponding
     * to the query.
     *
     * @param query the query of the userMaster search.
     * @return the result of the search.
     */
    @GetMapping("/_search/user-masters")
    public ResponseEntity<List<UserMaster>>  searchUserMasters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search UserMasters for query {}", query);
        Page<UserMaster> search = userMasterSearchRepository.search(queryStringQuery(query).field("firstName").field("employeeNumber").field("displayName").field("lastName").field("status").defaultOperator(Operator.AND), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), search);
        return ResponseEntity.ok().headers(headers).body(search.getContent());
    }



    @GetMapping("/_index/user-master")
    public ResponseEntity<Void> indexUserMaster(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate) {
        log.debug("REST request to do elastic index on Users");
        long resultCount = userMasterRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            userMasterService.doIndex(i, pageSize, fromDate, toDate);
        }
        userMasterSearchRepository.refresh();
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("artha","ElasticSearch index completed sucessfully","")).build();
    }

    @PostMapping("/_import/user-master")
    public void importUsers(@RequestBody MultipartFile multipartFile) throws Exception {
        userMasterService.importUser(multipartFile);
    }

    @GetMapping("/_export/user-master")
    public ResponseEntity<Map<String,String>> exportUserMaster(@RequestParam("query") String query,Pageable pageable) throws IOException {
        Map<String,String> fileNameMap=userMasterService.exportUserMaster(query,pageable);
        return  ResponseEntity.ok().body(fileNameMap);
    }
}
