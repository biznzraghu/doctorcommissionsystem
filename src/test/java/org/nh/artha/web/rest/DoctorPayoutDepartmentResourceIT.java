package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.DoctorPayoutDepartment;
import org.nh.artha.repository.DoctorPayoutDepartmentRepository;
import org.nh.artha.repository.search.DoctorPayoutDepartmentSearchRepository;
import org.nh.artha.service.DoctorPayoutDepartmentService;
import org.nh.artha.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DoctorPayoutDepartmentResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class DoctorPayoutDepartmentResourceIT {

    private static final Long DEFAULT_DOCTOR_PAYOUT_DEPARTMENT_ID = 1L;
    private static final Long UPDATED_DOCTOR_PAYOUT_DEPARTMENT_ID = 2L;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private DoctorPayoutDepartmentRepository doctorPayoutDepartmentRepository;

    @Autowired
    private DoctorPayoutDepartmentService doctorPayoutDepartmentService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.DoctorPayoutDepartmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorPayoutDepartmentSearchRepository mockDoctorPayoutDepartmentSearchRepository;

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

    @Autowired
    private ApplicationProperties applicationProperties;

    private MockMvc restDoctorPayoutDepartmentMockMvc;

    private DoctorPayoutDepartment doctorPayoutDepartment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorPayoutDepartmentResource doctorPayoutDepartmentResource = new DoctorPayoutDepartmentResource(doctorPayoutDepartmentService,applicationProperties,doctorPayoutDepartmentRepository);
        this.restDoctorPayoutDepartmentMockMvc = MockMvcBuilders.standaloneSetup(doctorPayoutDepartmentResource)
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
    public static DoctorPayoutDepartment createEntity(EntityManager em) {
        DoctorPayoutDepartment doctorPayoutDepartment = new DoctorPayoutDepartment()
            .doctorPayoutDepartmentId(DEFAULT_DOCTOR_PAYOUT_DEPARTMENT_ID)
            .amount(DEFAULT_AMOUNT);
        return doctorPayoutDepartment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorPayoutDepartment createUpdatedEntity(EntityManager em) {
        DoctorPayoutDepartment doctorPayoutDepartment = new DoctorPayoutDepartment()
            .doctorPayoutDepartmentId(UPDATED_DOCTOR_PAYOUT_DEPARTMENT_ID)
            .amount(UPDATED_AMOUNT);
        return doctorPayoutDepartment;
    }

    @BeforeEach
    public void initTest() {
        doctorPayoutDepartment = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorPayoutDepartment() throws Exception {
        int databaseSizeBeforeCreate = doctorPayoutDepartmentRepository.findAll().size();

        // Create the DoctorPayoutDepartment
        restDoctorPayoutDepartmentMockMvc.perform(post("/api/doctor-payout-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayoutDepartment)))
            .andExpect(status().isCreated());

        // Validate the DoctorPayoutDepartment in the database
        List<DoctorPayoutDepartment> doctorPayoutDepartmentList = doctorPayoutDepartmentRepository.findAll();
        assertThat(doctorPayoutDepartmentList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorPayoutDepartment testDoctorPayoutDepartment = doctorPayoutDepartmentList.get(doctorPayoutDepartmentList.size() - 1);
        assertThat(testDoctorPayoutDepartment.getDoctorPayoutDepartmentId()).isEqualTo(DEFAULT_DOCTOR_PAYOUT_DEPARTMENT_ID);
        assertThat(testDoctorPayoutDepartment.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the DoctorPayoutDepartment in Elasticsearch
        verify(mockDoctorPayoutDepartmentSearchRepository, times(1)).save(testDoctorPayoutDepartment);
    }

    @Test
    @Transactional
    public void createDoctorPayoutDepartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorPayoutDepartmentRepository.findAll().size();

        // Create the DoctorPayoutDepartment with an existing ID
        doctorPayoutDepartment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorPayoutDepartmentMockMvc.perform(post("/api/doctor-payout-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayoutDepartment)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorPayoutDepartment in the database
        List<DoctorPayoutDepartment> doctorPayoutDepartmentList = doctorPayoutDepartmentRepository.findAll();
        assertThat(doctorPayoutDepartmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the DoctorPayoutDepartment in Elasticsearch
        verify(mockDoctorPayoutDepartmentSearchRepository, times(0)).save(doctorPayoutDepartment);
    }


    @Test
    @Transactional
    public void getAllDoctorPayoutDepartments() throws Exception {
        // Initialize the database
        doctorPayoutDepartmentRepository.saveAndFlush(doctorPayoutDepartment);

        // Get all the doctorPayoutDepartmentList
        restDoctorPayoutDepartmentMockMvc.perform(get("/api/doctor-payout-departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPayoutDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].doctorPayoutDepartmentId").value(hasItem(DEFAULT_DOCTOR_PAYOUT_DEPARTMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getDoctorPayoutDepartment() throws Exception {
        // Initialize the database
        doctorPayoutDepartmentRepository.saveAndFlush(doctorPayoutDepartment);

        // Get the doctorPayoutDepartment
        restDoctorPayoutDepartmentMockMvc.perform(get("/api/doctor-payout-departments/{id}", doctorPayoutDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorPayoutDepartment.getId().intValue()))
            .andExpect(jsonPath("$.doctorPayoutDepartmentId").value(DEFAULT_DOCTOR_PAYOUT_DEPARTMENT_ID.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDoctorPayoutDepartment() throws Exception {
        // Get the doctorPayoutDepartment
        restDoctorPayoutDepartmentMockMvc.perform(get("/api/doctor-payout-departments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorPayoutDepartment() throws Exception {
        // Initialize the database
        doctorPayoutDepartmentService.save(doctorPayoutDepartment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDoctorPayoutDepartmentSearchRepository);

        int databaseSizeBeforeUpdate = doctorPayoutDepartmentRepository.findAll().size();

        // Update the doctorPayoutDepartment
        DoctorPayoutDepartment updatedDoctorPayoutDepartment = doctorPayoutDepartmentRepository.findById(doctorPayoutDepartment.getId()).get();
        // Disconnect from session so that the updates on updatedDoctorPayoutDepartment are not directly saved in db
        em.detach(updatedDoctorPayoutDepartment);
        updatedDoctorPayoutDepartment
            .doctorPayoutDepartmentId(UPDATED_DOCTOR_PAYOUT_DEPARTMENT_ID)
            .amount(UPDATED_AMOUNT);

        restDoctorPayoutDepartmentMockMvc.perform(put("/api/doctor-payout-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctorPayoutDepartment)))
            .andExpect(status().isOk());

        // Validate the DoctorPayoutDepartment in the database
        List<DoctorPayoutDepartment> doctorPayoutDepartmentList = doctorPayoutDepartmentRepository.findAll();
        assertThat(doctorPayoutDepartmentList).hasSize(databaseSizeBeforeUpdate);
        DoctorPayoutDepartment testDoctorPayoutDepartment = doctorPayoutDepartmentList.get(doctorPayoutDepartmentList.size() - 1);
        assertThat(testDoctorPayoutDepartment.getDoctorPayoutDepartmentId()).isEqualTo(UPDATED_DOCTOR_PAYOUT_DEPARTMENT_ID);
        assertThat(testDoctorPayoutDepartment.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the DoctorPayoutDepartment in Elasticsearch
        verify(mockDoctorPayoutDepartmentSearchRepository, times(1)).save(testDoctorPayoutDepartment);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorPayoutDepartment() throws Exception {
        int databaseSizeBeforeUpdate = doctorPayoutDepartmentRepository.findAll().size();

        // Create the DoctorPayoutDepartment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorPayoutDepartmentMockMvc.perform(put("/api/doctor-payout-departments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayoutDepartment)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorPayoutDepartment in the database
        List<DoctorPayoutDepartment> doctorPayoutDepartmentList = doctorPayoutDepartmentRepository.findAll();
        assertThat(doctorPayoutDepartmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DoctorPayoutDepartment in Elasticsearch
        verify(mockDoctorPayoutDepartmentSearchRepository, times(0)).save(doctorPayoutDepartment);
    }

    @Test
    @Transactional
    public void deleteDoctorPayoutDepartment() throws Exception {
        // Initialize the database
        doctorPayoutDepartmentService.save(doctorPayoutDepartment);

        int databaseSizeBeforeDelete = doctorPayoutDepartmentRepository.findAll().size();

        // Delete the doctorPayoutDepartment
        restDoctorPayoutDepartmentMockMvc.perform(delete("/api/doctor-payout-departments/{id}", doctorPayoutDepartment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DoctorPayoutDepartment> doctorPayoutDepartmentList = doctorPayoutDepartmentRepository.findAll();
        assertThat(doctorPayoutDepartmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DoctorPayoutDepartment in Elasticsearch
        verify(mockDoctorPayoutDepartmentSearchRepository, times(1)).deleteById(doctorPayoutDepartment.getId());
    }

    @Test
    @Transactional
    public void searchDoctorPayoutDepartment() throws Exception {
        // Initialize the database
        doctorPayoutDepartmentService.save(doctorPayoutDepartment);
        when(mockDoctorPayoutDepartmentSearchRepository.search(queryStringQuery("id:" + doctorPayoutDepartment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctorPayoutDepartment), PageRequest.of(0, 1), 1));
        // Search the doctorPayoutDepartment
        restDoctorPayoutDepartmentMockMvc.perform(get("/api/_search/doctor-payout-departments?query=id:" + doctorPayoutDepartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPayoutDepartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].doctorPayoutDepartmentId").value(hasItem(DEFAULT_DOCTOR_PAYOUT_DEPARTMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
}
