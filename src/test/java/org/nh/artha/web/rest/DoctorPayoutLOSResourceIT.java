package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.DoctorPayoutLOS;
import org.nh.artha.repository.DoctorPayoutLOSRepository;
import org.nh.artha.repository.search.DoctorPayoutLOSSearchRepository;
import org.nh.artha.service.DoctorPayoutLOSService;
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
 * Integration tests for the {@link DoctorPayoutLOSResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class DoctorPayoutLOSResourceIT {

    private static final Long DEFAULT_DOCTOR_PAYOUT_LOS_ID = 1L;
    private static final Long UPDATED_DOCTOR_PAYOUT_LOS_ID = 2L;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private DoctorPayoutLOSRepository doctorPayoutLOSRepository;

    @Autowired
    private DoctorPayoutLOSService doctorPayoutLOSService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.DoctorPayoutLOSSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorPayoutLOSSearchRepository mockDoctorPayoutLOSSearchRepository;

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

    private MockMvc restDoctorPayoutLOSMockMvc;

    private DoctorPayoutLOS doctorPayoutLOS;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorPayoutLOSResource doctorPayoutLOSResource = new DoctorPayoutLOSResource(doctorPayoutLOSService);
        this.restDoctorPayoutLOSMockMvc = MockMvcBuilders.standaloneSetup(doctorPayoutLOSResource)
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
    public static DoctorPayoutLOS createEntity(EntityManager em) {
        DoctorPayoutLOS doctorPayoutLOS = new DoctorPayoutLOS()
            .doctorPayoutLosId(DEFAULT_DOCTOR_PAYOUT_LOS_ID)
            .amount(DEFAULT_AMOUNT);
        return doctorPayoutLOS;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorPayoutLOS createUpdatedEntity(EntityManager em) {
        DoctorPayoutLOS doctorPayoutLOS = new DoctorPayoutLOS()
            .doctorPayoutLosId(UPDATED_DOCTOR_PAYOUT_LOS_ID)
            .amount(UPDATED_AMOUNT);
        return doctorPayoutLOS;
    }

    @BeforeEach
    public void initTest() {
        doctorPayoutLOS = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorPayoutLOS() throws Exception {
        int databaseSizeBeforeCreate = doctorPayoutLOSRepository.findAll().size();

        // Create the DoctorPayoutLOS
        restDoctorPayoutLOSMockMvc.perform(post("/api/doctor-payout-los")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayoutLOS)))
            .andExpect(status().isCreated());

        // Validate the DoctorPayoutLOS in the database
        List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSRepository.findAll();
        assertThat(doctorPayoutLOSList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorPayoutLOS testDoctorPayoutLOS = doctorPayoutLOSList.get(doctorPayoutLOSList.size() - 1);
        assertThat(testDoctorPayoutLOS.getDoctorPayoutLosId()).isEqualTo(DEFAULT_DOCTOR_PAYOUT_LOS_ID);
        assertThat(testDoctorPayoutLOS.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the DoctorPayoutLOS in Elasticsearch
        verify(mockDoctorPayoutLOSSearchRepository, times(1)).save(testDoctorPayoutLOS);
    }

    @Test
    @Transactional
    public void createDoctorPayoutLOSWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorPayoutLOSRepository.findAll().size();

        // Create the DoctorPayoutLOS with an existing ID
        doctorPayoutLOS.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorPayoutLOSMockMvc.perform(post("/api/doctor-payout-los")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayoutLOS)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorPayoutLOS in the database
        List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSRepository.findAll();
        assertThat(doctorPayoutLOSList).hasSize(databaseSizeBeforeCreate);

        // Validate the DoctorPayoutLOS in Elasticsearch
        verify(mockDoctorPayoutLOSSearchRepository, times(0)).save(doctorPayoutLOS);
    }


    @Test
    @Transactional
    public void getAllDoctorPayoutLOS() throws Exception {
        // Initialize the database
        doctorPayoutLOSRepository.saveAndFlush(doctorPayoutLOS);

        // Get all the doctorPayoutLOSList
        restDoctorPayoutLOSMockMvc.perform(get("/api/doctor-payout-los?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPayoutLOS.getId().intValue())))
            .andExpect(jsonPath("$.[*].doctorPayoutLosId").value(hasItem(DEFAULT_DOCTOR_PAYOUT_LOS_ID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getDoctorPayoutLOS() throws Exception {
        // Initialize the database
        doctorPayoutLOSRepository.saveAndFlush(doctorPayoutLOS);

        // Get the doctorPayoutLOS
        restDoctorPayoutLOSMockMvc.perform(get("/api/doctor-payout-los/{id}", doctorPayoutLOS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorPayoutLOS.getId().intValue()))
            .andExpect(jsonPath("$.doctorPayoutLosId").value(DEFAULT_DOCTOR_PAYOUT_LOS_ID.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDoctorPayoutLOS() throws Exception {
        // Get the doctorPayoutLOS
        restDoctorPayoutLOSMockMvc.perform(get("/api/doctor-payout-los/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorPayoutLOS() throws Exception {
        // Initialize the database
        doctorPayoutLOSService.save(doctorPayoutLOS);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDoctorPayoutLOSSearchRepository);

        int databaseSizeBeforeUpdate = doctorPayoutLOSRepository.findAll().size();

        // Update the doctorPayoutLOS
        DoctorPayoutLOS updatedDoctorPayoutLOS = doctorPayoutLOSRepository.findById(doctorPayoutLOS.getId()).get();
        // Disconnect from session so that the updates on updatedDoctorPayoutLOS are not directly saved in db
        em.detach(updatedDoctorPayoutLOS);
        updatedDoctorPayoutLOS
            .doctorPayoutLosId(UPDATED_DOCTOR_PAYOUT_LOS_ID)
            .amount(UPDATED_AMOUNT);

        restDoctorPayoutLOSMockMvc.perform(put("/api/doctor-payout-los")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctorPayoutLOS)))
            .andExpect(status().isOk());

        // Validate the DoctorPayoutLOS in the database
        List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSRepository.findAll();
        assertThat(doctorPayoutLOSList).hasSize(databaseSizeBeforeUpdate);
        DoctorPayoutLOS testDoctorPayoutLOS = doctorPayoutLOSList.get(doctorPayoutLOSList.size() - 1);
        assertThat(testDoctorPayoutLOS.getDoctorPayoutLosId()).isEqualTo(UPDATED_DOCTOR_PAYOUT_LOS_ID);
        assertThat(testDoctorPayoutLOS.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the DoctorPayoutLOS in Elasticsearch
        verify(mockDoctorPayoutLOSSearchRepository, times(1)).save(testDoctorPayoutLOS);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorPayoutLOS() throws Exception {
        int databaseSizeBeforeUpdate = doctorPayoutLOSRepository.findAll().size();

        // Create the DoctorPayoutLOS

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorPayoutLOSMockMvc.perform(put("/api/doctor-payout-los")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayoutLOS)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorPayoutLOS in the database
        List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSRepository.findAll();
        assertThat(doctorPayoutLOSList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DoctorPayoutLOS in Elasticsearch
        verify(mockDoctorPayoutLOSSearchRepository, times(0)).save(doctorPayoutLOS);
    }

    @Test
    @Transactional
    public void deleteDoctorPayoutLOS() throws Exception {
        // Initialize the database
        doctorPayoutLOSService.save(doctorPayoutLOS);

        int databaseSizeBeforeDelete = doctorPayoutLOSRepository.findAll().size();

        // Delete the doctorPayoutLOS
        restDoctorPayoutLOSMockMvc.perform(delete("/api/doctor-payout-los/{id}", doctorPayoutLOS.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DoctorPayoutLOS> doctorPayoutLOSList = doctorPayoutLOSRepository.findAll();
        assertThat(doctorPayoutLOSList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DoctorPayoutLOS in Elasticsearch
        verify(mockDoctorPayoutLOSSearchRepository, times(1)).deleteById(doctorPayoutLOS.getId());
    }

    @Test
    @Transactional
    public void searchDoctorPayoutLOS() throws Exception {
        // Initialize the database
        doctorPayoutLOSService.save(doctorPayoutLOS);
        when(mockDoctorPayoutLOSSearchRepository.search(queryStringQuery("id:" + doctorPayoutLOS.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctorPayoutLOS), PageRequest.of(0, 1), 1));
        // Search the doctorPayoutLOS
        restDoctorPayoutLOSMockMvc.perform(get("/api/_search/doctor-payout-los?query=id:" + doctorPayoutLOS.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPayoutLOS.getId().intValue())))
            .andExpect(jsonPath("$.[*].doctorPayoutLosId").value(hasItem(DEFAULT_DOCTOR_PAYOUT_LOS_ID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
}
