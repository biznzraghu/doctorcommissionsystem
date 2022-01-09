package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.DepartmentRevenue;
import org.nh.artha.repository.DepartmentRevenueRepository;
import org.nh.artha.repository.search.DepartmentRevenueSearchRepository;
import org.nh.artha.service.DepartmentRevenueService;
import org.nh.artha.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nh.artha.domain.enumeration.Status;
import org.nh.artha.domain.enumeration.RequestStatus;
/**
 * Integration tests for the {@link DepartmentRevenueResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class DepartmentRevenueResourceIT {

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_CREATED_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CREATED_DATE = LocalDateTime.now();

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    private static final String DEFAULT_APPROVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY = "BBBBBBBBBB";

    private static final RequestStatus DEFAULT_REQUEST_STATUS = RequestStatus.DRAFT;
    private static final RequestStatus UPDATED_REQUEST_STATUS = RequestStatus.PENDING_APPROVAL;

    @Autowired
    private DepartmentRevenueRepository departmentRevenueRepository;

    @Autowired
    private DepartmentRevenueService departmentRevenueService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.DepartmentRevenueSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepartmentRevenueSearchRepository mockDepartmentRevenueSearchRepository;
    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDepartmentRevenueMockMvc;

    private DepartmentRevenue departmentRevenue;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepartmentRevenueResource departmentRevenueResource = new DepartmentRevenueResource(departmentRevenueService,departmentRevenueRepository,mockDepartmentRevenueSearchRepository,applicationProperties);
        this.restDepartmentRevenueMockMvc = MockMvcBuilders.standaloneSetup(departmentRevenueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentRevenue createEntity(EntityManager em) {
        DepartmentRevenue departmentRevenue = new DepartmentRevenue()
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .version(DEFAULT_VERSION)
            .status(DEFAULT_STATUS)
            .approvedBy(DEFAULT_APPROVED_BY)
            .requestStatus(DEFAULT_REQUEST_STATUS);
        return departmentRevenue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentRevenue createUpdatedEntity(EntityManager em) {
        DepartmentRevenue departmentRevenue = new DepartmentRevenue()
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .version(UPDATED_VERSION)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .requestStatus(UPDATED_REQUEST_STATUS);
        return departmentRevenue;
    }

    @BeforeEach
    public void initTest() {
        departmentRevenue = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartmentRevenue() throws Exception {
        int databaseSizeBeforeCreate = departmentRevenueRepository.findAll().size();

        // Create the DepartmentRevenue
        restDepartmentRevenueMockMvc.perform(post("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isCreated());

        // Validate the DepartmentRevenue in the database
        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeCreate + 1);
        DepartmentRevenue testDepartmentRevenue = departmentRevenueList.get(departmentRevenueList.size() - 1);
        assertThat(testDepartmentRevenue.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDepartmentRevenue.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDepartmentRevenue.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testDepartmentRevenue.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDepartmentRevenue.getApprovedBy()).isEqualTo(DEFAULT_APPROVED_BY);
        assertThat(testDepartmentRevenue.getRequestStatus()).isEqualTo(DEFAULT_REQUEST_STATUS);

        // Validate the DepartmentRevenue in Elasticsearch
        verify(mockDepartmentRevenueSearchRepository, times(1)).save(testDepartmentRevenue);
    }

    @Test
    @Transactional
    public void createDepartmentRevenueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departmentRevenueRepository.findAll().size();

        // Create the DepartmentRevenue with an existing ID
        departmentRevenue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentRevenueMockMvc.perform(post("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentRevenue in the database
        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepartmentRevenue in Elasticsearch
        verify(mockDepartmentRevenueSearchRepository, times(0)).save(departmentRevenue);
    }


    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueRepository.findAll().size();
        // set the field null
        departmentRevenue.setCreatedBy(null);

        // Create the DepartmentRevenue, which fails.

        restDepartmentRevenueMockMvc.perform(post("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueRepository.findAll().size();
        // set the field null
        departmentRevenue.setCreatedDate(null);

        // Create the DepartmentRevenue, which fails.

        restDepartmentRevenueMockMvc.perform(post("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueRepository.findAll().size();
        // set the field null
        departmentRevenue.setVersion(null);

        // Create the DepartmentRevenue, which fails.

        restDepartmentRevenueMockMvc.perform(post("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueRepository.findAll().size();
        // set the field null
        departmentRevenue.setStatus(null);

        // Create the DepartmentRevenue, which fails.

        restDepartmentRevenueMockMvc.perform(post("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApprovedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueRepository.findAll().size();
        // set the field null
        departmentRevenue.setApprovedBy(null);

        // Create the DepartmentRevenue, which fails.

        restDepartmentRevenueMockMvc.perform(post("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequestStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueRepository.findAll().size();
        // set the field null
        departmentRevenue.setRequestStatus(null);

        // Create the DepartmentRevenue, which fails.

        restDepartmentRevenueMockMvc.perform(post("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepartmentRevenues() throws Exception {
        // Initialize the database
        departmentRevenueRepository.saveAndFlush(departmentRevenue);

        // Get all the departmentRevenueList
        restDepartmentRevenueMockMvc.perform(get("/api/department-revenues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentRevenue.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)))
            .andExpect(jsonPath("$.[*].requestStatus").value(hasItem(DEFAULT_REQUEST_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getDepartmentRevenue() throws Exception {
        // Initialize the database
        departmentRevenueRepository.saveAndFlush(departmentRevenue);

        // Get the departmentRevenue
        restDepartmentRevenueMockMvc.perform(get("/api/department-revenues/{id}", departmentRevenue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(departmentRevenue.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.approvedBy").value(DEFAULT_APPROVED_BY))
            .andExpect(jsonPath("$.requestStatus").value(DEFAULT_REQUEST_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDepartmentRevenue() throws Exception {
        // Get the departmentRevenue
        restDepartmentRevenueMockMvc.perform(get("/api/department-revenues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartmentRevenue() throws Exception {
        // Initialize the database
        departmentRevenueService.save(departmentRevenue);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDepartmentRevenueSearchRepository);

        int databaseSizeBeforeUpdate = departmentRevenueRepository.findAll().size();

        // Update the departmentRevenue
        DepartmentRevenue updatedDepartmentRevenue = departmentRevenueRepository.findById(departmentRevenue.getId()).get();
        // Disconnect from session so that the updates on updatedDepartmentRevenue are not directly saved in db
        em.detach(updatedDepartmentRevenue);
        updatedDepartmentRevenue
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .version(UPDATED_VERSION)
            .status(UPDATED_STATUS)
            .approvedBy(UPDATED_APPROVED_BY)
            .requestStatus(UPDATED_REQUEST_STATUS);

        restDepartmentRevenueMockMvc.perform(put("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDepartmentRevenue)))
            .andExpect(status().isOk());

        // Validate the DepartmentRevenue in the database
        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeUpdate);
        DepartmentRevenue testDepartmentRevenue = departmentRevenueList.get(departmentRevenueList.size() - 1);
        assertThat(testDepartmentRevenue.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDepartmentRevenue.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDepartmentRevenue.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testDepartmentRevenue.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDepartmentRevenue.getApprovedBy()).isEqualTo(UPDATED_APPROVED_BY);
        assertThat(testDepartmentRevenue.getRequestStatus()).isEqualTo(UPDATED_REQUEST_STATUS);

        // Validate the DepartmentRevenue in Elasticsearch
        verify(mockDepartmentRevenueSearchRepository, times(1)).save(testDepartmentRevenue);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartmentRevenue() throws Exception {
        int databaseSizeBeforeUpdate = departmentRevenueRepository.findAll().size();

        // Create the DepartmentRevenue

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentRevenueMockMvc.perform(put("/api/department-revenues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenue)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentRevenue in the database
        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentRevenue in Elasticsearch
        verify(mockDepartmentRevenueSearchRepository, times(0)).save(departmentRevenue);
    }

    @Test
    @Transactional
    public void deleteDepartmentRevenue() throws Exception {
        // Initialize the database
        departmentRevenueService.save(departmentRevenue);

        int databaseSizeBeforeDelete = departmentRevenueRepository.findAll().size();

        // Delete the departmentRevenue
        restDepartmentRevenueMockMvc.perform(delete("/api/department-revenues/{id}", departmentRevenue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepartmentRevenue> departmentRevenueList = departmentRevenueRepository.findAll();
        assertThat(departmentRevenueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepartmentRevenue in Elasticsearch
        verify(mockDepartmentRevenueSearchRepository, times(1)).deleteById(departmentRevenue.getId());
    }

    @Test
    @Transactional
    public void searchDepartmentRevenue() throws Exception {
        // Initialize the database
        departmentRevenueService.save(departmentRevenue);
        when(mockDepartmentRevenueSearchRepository.search(queryStringQuery("id:" + departmentRevenue.getId())))
            .thenReturn(Collections.singletonList(departmentRevenue));
        // Search the departmentRevenue
        restDepartmentRevenueMockMvc.perform(get("/api/_search/department-revenues?query=id:" + departmentRevenue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentRevenue.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)))
            .andExpect(jsonPath("$.[*].requestStatus").value(hasItem(DEFAULT_REQUEST_STATUS.toString())));
    }
}
