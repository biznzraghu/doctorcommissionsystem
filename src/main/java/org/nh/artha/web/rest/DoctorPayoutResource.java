package org.nh.artha.web.rest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.DoctorPayout;
import org.nh.artha.domain.MisReport;
import org.nh.artha.domain.VariablePayout;
import org.nh.artha.domain.dto.MisDowloadFile;
import org.nh.artha.repository.DoctorPayoutRepository;
import org.nh.artha.repository.search.DoctorPayoutSearchRepository;
import org.nh.artha.service.DoctorPayoutService;
import org.nh.artha.service.MisReportService;
import org.nh.artha.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link org.nh.artha.domain.DoctorPayout}.
 */
@RestController
@RequestMapping("/api")
public class DoctorPayoutResource {

    private final Logger log = LoggerFactory.getLogger(DoctorPayoutResource.class);

    private static final String ENTITY_NAME = "doctorPayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorPayoutService doctorPayoutService;

    private final DoctorPayoutRepository doctorPayoutRepository;

    private final DoctorPayoutSearchRepository doctorPayoutSearchRepository;

    private final ApplicationProperties applicationProperties;

    @Autowired
    private MisReportService misReportService;

    public DoctorPayoutResource(DoctorPayoutService doctorPayoutService,DoctorPayoutRepository doctorPayoutRepository,DoctorPayoutSearchRepository doctorPayoutSearchRepository,
                                ApplicationProperties applicationProperties) {
        this.doctorPayoutService = doctorPayoutService;
        this.doctorPayoutRepository=doctorPayoutRepository;
        this.doctorPayoutSearchRepository= doctorPayoutSearchRepository;
        this.applicationProperties=applicationProperties;
    }

    /**
     * {@code POST  /doctor-payouts} : Create a new doctorPayout.
     *
     * @param doctorPayout the doctorPayout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctorPayout, or with status {@code 400 (Bad Request)} if the doctorPayout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doctor-payouts")
    public ResponseEntity<DoctorPayout> createDoctorPayout(@RequestBody DoctorPayout doctorPayout) throws URISyntaxException {
        log.debug("REST request to save DoctorPayout : {}", doctorPayout);
        if (doctorPayout.getId() != null) {
            throw new BadRequestAlertException("A new doctorPayout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorPayout result = doctorPayoutService.save(doctorPayout);
        return ResponseEntity.created(new URI("/api/doctor-payouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doctor-payouts} : Updates an existing doctorPayout.
     *
     * @param doctorPayout the doctorPayout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorPayout,
     * or with status {@code 400 (Bad Request)} if the doctorPayout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctorPayout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doctor-payouts")
    public ResponseEntity<DoctorPayout> updateDoctorPayout(@RequestBody DoctorPayout doctorPayout) throws URISyntaxException {
        log.debug("REST request to update DoctorPayout : {}", doctorPayout);
        if (doctorPayout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorPayout result = doctorPayoutService.save(doctorPayout);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorPayout.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /doctor-payouts} : get all the doctorPayouts.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctorPayouts in body.
     */
    @GetMapping("/doctor-payouts")
    public ResponseEntity<List<DoctorPayout>> getAllDoctorPayouts(Pageable pageable) {
        log.debug("REST request to get a page of DoctorPayouts");
        Page<DoctorPayout> page = doctorPayoutService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doctor-payouts/:id} : get the "id" doctorPayout.
     *
     * @param id the id of the doctorPayout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorPayout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctor-payouts/{id}")
    public ResponseEntity<DoctorPayout> getDoctorPayout(@PathVariable Long id) {
        log.debug("REST request to get DoctorPayout : {}", id);
        Optional<DoctorPayout> doctorPayout = doctorPayoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorPayout);
    }

    /**
     * {@code DELETE  /doctor-payouts/:id} : delete the "id" doctorPayout.
     *
     * @param id the id of the doctorPayout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doctor-payouts/{id}")
    public ResponseEntity<Void> deleteDoctorPayout(@PathVariable Long id) {
        log.debug("REST request to delete DoctorPayout : {}", id);
        doctorPayoutService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/doctor-payouts?query=:query} : search for the doctorPayout corresponding
     * to the query.
     *
     * @param query the query of the doctorPayout search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/doctor-payouts")
    public ResponseEntity<List<DoctorPayout>> searchDoctorPayouts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DoctorPayouts for query {}", query);
        Page<DoctorPayout> page = doctorPayoutService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_index/doctor-payout")
    public ResponseEntity<Void> doIndex(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate fromDate,
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate toDate){
        log.debug("REST request to do elastic index on DoctorPayout");
        long resultCount = doctorPayoutRepository.getTotalRecord(fromDate, toDate);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            doctorPayoutService.doIndex(i, pageSize,fromDate,toDate);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("ARTHA", "Elastic index is completed",fromDate.toString()+" , "+toDate.toString())).build();
    }


    @GetMapping("/_export/variable-payout-breakup-summary")
    public  ResponseEntity<byte[]> exportDoctorPayoutBreakUpSummary(@RequestParam String query, Pageable pageable) throws Exception{
        Map<String, Object> nameAndPathMap = doctorPayoutService.exportDoctorPayoutBreakUpSummary(query, pageable);
        byte[] paths = FileUtils.readFileToByteArray(new File(nameAndPathMap.get("path").toString()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "attachment; filename=" + nameAndPathMap.get("name"));
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentLength(paths.length);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<byte[]>(paths, headers, HttpStatus.OK);
    }

    @GetMapping("/_export/variable-payout-breakup")
    public  ResponseEntity<byte[]> exportVariablePayoutBreakup(@RequestParam String query, Pageable pageable) throws Exception{
        Map<String, Object> nameAndPathMap = doctorPayoutService.exportDoctorPayoutBreakup(query, pageable);
        byte[] paths = FileUtils.readFileToByteArray(new File(nameAndPathMap.get("path").toString()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "attachment; filename=" + nameAndPathMap.get("name"));
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentLength(paths.length);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<byte[]>(paths, headers, HttpStatus.OK);
    }

    @PostMapping("/_schedule/item-service-wise-payout-breakup")
    public MisReport exportItemServiceWisePayoutBreakUp(@RequestBody MisReport misReport, @RequestParam("unit") String unitCode, @RequestParam("year") Integer year, @RequestParam("month") Integer month) throws Exception{
        Map<String,String> queryMap= new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("unit="+unitCode+"&&"+"year="+year+"&&"+"month="+month);
        queryMap.put("filterCriteria",stringBuffer.toString());
        return  misReportService.save(misReport,queryMap);
    }

    @GetMapping("/mis-report")
    public ResponseEntity<List<MisReport>> getMisReport(String query,Pageable pageable){
        Page<MisReport> page = misReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/download/mis-report/{id}")
    public Map<String,String> downloadReport(@PathVariable Long id) throws IOException {
         return misReportService.download(id);

    }

}
