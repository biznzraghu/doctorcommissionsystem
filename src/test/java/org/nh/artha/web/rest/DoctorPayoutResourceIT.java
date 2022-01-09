package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.DoctorPayout;
import org.nh.artha.repository.DoctorPayoutRepository;
import org.nh.artha.repository.search.DoctorPayoutSearchRepository;
import org.nh.artha.service.DoctorPayoutService;
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

import org.nh.artha.domain.enumeration.ConsultantType;
/**
 * Integration tests for the {@link DoctorPayoutResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class DoctorPayoutResourceIT {

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Integer DEFAULT_DATE = 1;
    private static final Integer UPDATED_DATE = 2;

    private static final Long DEFAULT_VARIABLE_PAYOUT_ID = 1L;
    private static final Long UPDATED_VARIABLE_PAYOUT_ID = 2L;

    private static final Long DEFAULT_DOCTOR_PAYOUT_INVOICE = 1L;
    private static final Long UPDATED_DOCTOR_PAYOUT_INVOICE = 2L;

    private static final ConsultantType DEFAULT_CONSULTATNT_TYPE = ConsultantType.ADMITTING_CONSULTANT;
    private static final ConsultantType UPDATED_CONSULTATNT_TYPE = ConsultantType.RENDERING_CONSULTANT;

    private static final Long DEFAULT_DEPARTMENT_REVENUE_BENEFIT_ID = 1L;
    private static final Long UPDATED_DEPARTMENT_REVENUE_BENEFIT_ID = 2L;

    private static final Double DEFAULT_DEPARTMENT_REVENUE_BENEFIT_AMOUNT = 1D;
    private static final Double UPDATED_DEPARTMENT_REVENUE_BENEFIT_AMOUNT = 2D;

    private static final Long DEFAULT_LOS_BENEFIET_ID = 1L;
    private static final Long UPDATED_LOS_BENEFIET_ID = 2L;

    private static final Long DEFAULT_SERVICE_ITEM_BENEFIT_AMOUNT = 1L;
    private static final Long UPDATED_SERVICE_ITEM_BENEFIT_AMOUNT = 2L;

    @Autowired
    private DoctorPayoutRepository doctorPayoutRepository;

    @Autowired
    private DoctorPayoutService doctorPayoutService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.DoctorPayoutSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorPayoutSearchRepository mockDoctorPayoutSearchRepository;

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

    private MockMvc restDoctorPayoutMockMvc;

    private DoctorPayout doctorPayout;

    @Autowired
    private ApplicationProperties applicationProperties;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorPayoutResource doctorPayoutResource = new DoctorPayoutResource(doctorPayoutService,doctorPayoutRepository,mockDoctorPayoutSearchRepository,applicationProperties);
        this.restDoctorPayoutMockMvc = MockMvcBuilders.standaloneSetup(doctorPayoutResource)
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
    public static DoctorPayout createEntity(EntityManager em) {
        DoctorPayout doctorPayout = new DoctorPayout()
            .unitCode(DEFAULT_UNIT)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .year(DEFAULT_YEAR)
            .month(DEFAULT_MONTH)
            .date(DEFAULT_DATE)
            .variablePayoutId(DEFAULT_VARIABLE_PAYOUT_ID)
            .doctorPayoutInvoice(DEFAULT_DOCTOR_PAYOUT_INVOICE)
            .consultatntType(DEFAULT_CONSULTATNT_TYPE)
            .departmentRevenueBenefitId(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_ID)
            .departmentRevenueBenefitAmount(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_AMOUNT)
            .losBenefietId(DEFAULT_LOS_BENEFIET_ID)
            .serviceItemBenefitAmount(DEFAULT_SERVICE_ITEM_BENEFIT_AMOUNT);
        return doctorPayout;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DoctorPayout createUpdatedEntity(EntityManager em) {
        DoctorPayout doctorPayout = new DoctorPayout()
            .unitCode(UPDATED_UNIT)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .date(UPDATED_DATE)
            .variablePayoutId(UPDATED_VARIABLE_PAYOUT_ID)
            .doctorPayoutInvoice(UPDATED_DOCTOR_PAYOUT_INVOICE)
            .consultatntType(UPDATED_CONSULTATNT_TYPE)
            .departmentRevenueBenefitId(UPDATED_DEPARTMENT_REVENUE_BENEFIT_ID)
            .departmentRevenueBenefitAmount(UPDATED_DEPARTMENT_REVENUE_BENEFIT_AMOUNT)
            .losBenefietId(UPDATED_LOS_BENEFIET_ID)
            .serviceItemBenefitAmount(UPDATED_SERVICE_ITEM_BENEFIT_AMOUNT);
        return doctorPayout;
    }

    @BeforeEach
    public void initTest() {
        doctorPayout = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorPayout() throws Exception {
        int databaseSizeBeforeCreate = doctorPayoutRepository.findAll().size();

        // Create the DoctorPayout
        restDoctorPayoutMockMvc.perform(post("/api/doctor-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayout)))
            .andExpect(status().isCreated());

        // Validate the DoctorPayout in the database
        List<DoctorPayout> doctorPayoutList = doctorPayoutRepository.findAll();
        assertThat(doctorPayoutList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorPayout testDoctorPayout = doctorPayoutList.get(doctorPayoutList.size() - 1);
        assertThat(testDoctorPayout.getUnitCode()).isEqualTo(DEFAULT_UNIT);
        assertThat(testDoctorPayout.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testDoctorPayout.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testDoctorPayout.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testDoctorPayout.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDoctorPayout.getVariablePayoutId()).isEqualTo(DEFAULT_VARIABLE_PAYOUT_ID);
        assertThat(testDoctorPayout.getDoctorPayoutInvoice()).isEqualTo(DEFAULT_DOCTOR_PAYOUT_INVOICE);
        assertThat(testDoctorPayout.getConsultatntType()).isEqualTo(DEFAULT_CONSULTATNT_TYPE);
        assertThat(testDoctorPayout.getDepartmentRevenueBenefitId()).isEqualTo(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_ID);
        assertThat(testDoctorPayout.getDepartmentRevenueBenefitAmount()).isEqualTo(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_AMOUNT);
        assertThat(testDoctorPayout.getLosBenefietId()).isEqualTo(DEFAULT_LOS_BENEFIET_ID);
        assertThat(testDoctorPayout.getServiceItemBenefitAmount()).isEqualTo(DEFAULT_SERVICE_ITEM_BENEFIT_AMOUNT);

        // Validate the DoctorPayout in Elasticsearch
        verify(mockDoctorPayoutSearchRepository, times(1)).save(testDoctorPayout);
    }

    @Test
    @Transactional
    public void createDoctorPayoutWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorPayoutRepository.findAll().size();

        // Create the DoctorPayout with an existing ID
        doctorPayout.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorPayoutMockMvc.perform(post("/api/doctor-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayout)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorPayout in the database
        List<DoctorPayout> doctorPayoutList = doctorPayoutRepository.findAll();
        assertThat(doctorPayoutList).hasSize(databaseSizeBeforeCreate);

        // Validate the DoctorPayout in Elasticsearch
        verify(mockDoctorPayoutSearchRepository, times(0)).save(doctorPayout);
    }


    @Test
    @Transactional
    public void getAllDoctorPayouts() throws Exception {
        // Initialize the database
        doctorPayoutRepository.saveAndFlush(doctorPayout);

        // Get all the doctorPayoutList
        restDoctorPayoutMockMvc.perform(get("/api/doctor-payouts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].variablePayoutId").value(hasItem(DEFAULT_VARIABLE_PAYOUT_ID.intValue())))
            .andExpect(jsonPath("$.[*].doctorPayoutInvoice").value(hasItem(DEFAULT_DOCTOR_PAYOUT_INVOICE.intValue())))
            .andExpect(jsonPath("$.[*].consultatntType").value(hasItem(DEFAULT_CONSULTATNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].departmentRevenueBenefitId").value(hasItem(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].departmentRevenueBenefitAmount").value(hasItem(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].losBenefietId").value(hasItem(DEFAULT_LOS_BENEFIET_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceItemBenefitAmount").value(hasItem(DEFAULT_SERVICE_ITEM_BENEFIT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getDoctorPayout() throws Exception {
        // Initialize the database
        doctorPayoutRepository.saveAndFlush(doctorPayout);

        // Get the doctorPayout
        restDoctorPayoutMockMvc.perform(get("/api/doctor-payouts/{id}", doctorPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorPayout.getId().intValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.variablePayoutId").value(DEFAULT_VARIABLE_PAYOUT_ID.intValue()))
            .andExpect(jsonPath("$.doctorPayoutInvoice").value(DEFAULT_DOCTOR_PAYOUT_INVOICE.intValue()))
            .andExpect(jsonPath("$.consultatntType").value(DEFAULT_CONSULTATNT_TYPE.toString()))
            .andExpect(jsonPath("$.departmentRevenueBenefitId").value(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_ID.intValue()))
            .andExpect(jsonPath("$.departmentRevenueBenefitAmount").value(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.losBenefietId").value(DEFAULT_LOS_BENEFIET_ID.intValue()))
            .andExpect(jsonPath("$.serviceItemBenefitAmount").value(DEFAULT_SERVICE_ITEM_BENEFIT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDoctorPayout() throws Exception {
        // Get the doctorPayout
        restDoctorPayoutMockMvc.perform(get("/api/doctor-payouts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorPayout() throws Exception {
        // Initialize the database
        doctorPayoutService.save(doctorPayout);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDoctorPayoutSearchRepository);

        int databaseSizeBeforeUpdate = doctorPayoutRepository.findAll().size();

        // Update the doctorPayout
        DoctorPayout updatedDoctorPayout = doctorPayoutRepository.findById(doctorPayout.getId()).get();
        // Disconnect from session so that the updates on updatedDoctorPayout are not directly saved in db
        em.detach(updatedDoctorPayout);
        updatedDoctorPayout
            .unitCode(UPDATED_UNIT)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .date(UPDATED_DATE)
            .variablePayoutId(UPDATED_VARIABLE_PAYOUT_ID)
            .doctorPayoutInvoice(UPDATED_DOCTOR_PAYOUT_INVOICE)
            .consultatntType(UPDATED_CONSULTATNT_TYPE)
            .departmentRevenueBenefitId(UPDATED_DEPARTMENT_REVENUE_BENEFIT_ID)
            .departmentRevenueBenefitAmount(UPDATED_DEPARTMENT_REVENUE_BENEFIT_AMOUNT)
            .losBenefietId(UPDATED_LOS_BENEFIET_ID)
            .serviceItemBenefitAmount(UPDATED_SERVICE_ITEM_BENEFIT_AMOUNT);

        restDoctorPayoutMockMvc.perform(put("/api/doctor-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctorPayout)))
            .andExpect(status().isOk());

        // Validate the DoctorPayout in the database
        List<DoctorPayout> doctorPayoutList = doctorPayoutRepository.findAll();
        assertThat(doctorPayoutList).hasSize(databaseSizeBeforeUpdate);
        DoctorPayout testDoctorPayout = doctorPayoutList.get(doctorPayoutList.size() - 1);
        assertThat(testDoctorPayout.getUnitCode()).isEqualTo(UPDATED_UNIT);
        assertThat(testDoctorPayout.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testDoctorPayout.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testDoctorPayout.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testDoctorPayout.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDoctorPayout.getVariablePayoutId()).isEqualTo(UPDATED_VARIABLE_PAYOUT_ID);
        assertThat(testDoctorPayout.getDoctorPayoutInvoice()).isEqualTo(UPDATED_DOCTOR_PAYOUT_INVOICE);
        assertThat(testDoctorPayout.getConsultatntType()).isEqualTo(UPDATED_CONSULTATNT_TYPE);
        assertThat(testDoctorPayout.getDepartmentRevenueBenefitId()).isEqualTo(UPDATED_DEPARTMENT_REVENUE_BENEFIT_ID);
        assertThat(testDoctorPayout.getDepartmentRevenueBenefitAmount()).isEqualTo(UPDATED_DEPARTMENT_REVENUE_BENEFIT_AMOUNT);
        assertThat(testDoctorPayout.getLosBenefietId()).isEqualTo(UPDATED_LOS_BENEFIET_ID);
        assertThat(testDoctorPayout.getServiceItemBenefitAmount()).isEqualTo(UPDATED_SERVICE_ITEM_BENEFIT_AMOUNT);

        // Validate the DoctorPayout in Elasticsearch
        verify(mockDoctorPayoutSearchRepository, times(1)).save(testDoctorPayout);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorPayout() throws Exception {
        int databaseSizeBeforeUpdate = doctorPayoutRepository.findAll().size();

        // Create the DoctorPayout

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorPayoutMockMvc.perform(put("/api/doctor-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorPayout)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorPayout in the database
        List<DoctorPayout> doctorPayoutList = doctorPayoutRepository.findAll();
        assertThat(doctorPayoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DoctorPayout in Elasticsearch
        verify(mockDoctorPayoutSearchRepository, times(0)).save(doctorPayout);
    }

    @Test
    @Transactional
    public void deleteDoctorPayout() throws Exception {
        // Initialize the database
        doctorPayoutService.save(doctorPayout);

        int databaseSizeBeforeDelete = doctorPayoutRepository.findAll().size();

        // Delete the doctorPayout
        restDoctorPayoutMockMvc.perform(delete("/api/doctor-payouts/{id}", doctorPayout.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DoctorPayout> doctorPayoutList = doctorPayoutRepository.findAll();
        assertThat(doctorPayoutList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DoctorPayout in Elasticsearch
        verify(mockDoctorPayoutSearchRepository, times(1)).deleteById(doctorPayout.getId());
    }

    @Test
    @Transactional
    public void searchDoctorPayout() throws Exception {
        // Initialize the database
        doctorPayoutService.save(doctorPayout);
        when(mockDoctorPayoutSearchRepository.search(queryStringQuery("id:" + doctorPayout.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctorPayout), PageRequest.of(0, 1), 1));
        // Search the doctorPayout
        restDoctorPayoutMockMvc.perform(get("/api/_search/doctor-payouts?query=id:" + doctorPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].variablePayoutId").value(hasItem(DEFAULT_VARIABLE_PAYOUT_ID.intValue())))
            .andExpect(jsonPath("$.[*].doctorPayoutInvoice").value(hasItem(DEFAULT_DOCTOR_PAYOUT_INVOICE.intValue())))
            .andExpect(jsonPath("$.[*].consultatntType").value(hasItem(DEFAULT_CONSULTATNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].departmentRevenueBenefitId").value(hasItem(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].departmentRevenueBenefitAmount").value(hasItem(DEFAULT_DEPARTMENT_REVENUE_BENEFIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].losBenefietId").value(hasItem(DEFAULT_LOS_BENEFIET_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceItemBenefitAmount").value(hasItem(DEFAULT_SERVICE_ITEM_BENEFIT_AMOUNT.intValue())));
    }
}
