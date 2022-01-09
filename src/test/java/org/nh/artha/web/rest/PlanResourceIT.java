package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Plan;
import org.nh.artha.repository.PlanRepository;
import org.nh.artha.repository.search.PlanSearchRepository;
import org.nh.artha.service.PlanService;
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

/**
 * Integration tests for the {@link PlanResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class PlanResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_TEMPLATE = false;
    private static final Boolean UPDATED_TEMPLATE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_CREATED_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CREATED_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_MODIFIED_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_MODIFIED_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final LocalDateTime DEFAULT_CONTRACT_START_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CONTRACT_START_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final LocalDateTime DEFAULT_CONTRACT_END_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CONTRACT_END_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final LocalDateTime DEFAULT_APPLICABLE_START_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_APPLICABLE_START_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final LocalDateTime DEFAULT_APPLICABLE_END_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_APPLICABLE_END_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_OP_AUTHORIZATION = false;
    private static final Boolean UPDATED_OP_AUTHORIZATION = true;

    private static final Boolean DEFAULT_IP_AUTHORIZATION = false;
    private static final Boolean UPDATED_IP_AUTHORIZATION = true;

    private static final Boolean DEFAULT_SPONSOR_PAY_TAX = false;
    private static final Boolean UPDATED_SPONSOR_PAY_TAX = true;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanService planService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PlanSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanSearchRepository mockPlanSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPlanMockMvc;

    private Plan plan;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanResource planResource = new PlanResource(planService,planRepository,mockPlanSearchRepository,applicationProperties);
        this.restPlanMockMvc = MockMvcBuilders.standaloneSetup(planResource)
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
    public static Plan createEntity(EntityManager em) {
        Plan plan = new Plan()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .template(DEFAULT_TEMPLATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .contractStartDate(DEFAULT_CONTRACT_START_DATE)
            .contractEndDate(DEFAULT_CONTRACT_END_DATE)
            .applicableStartDate(DEFAULT_APPLICABLE_START_DATE)
            .applicableEndDate(DEFAULT_APPLICABLE_END_DATE)
            .opAuthorization(DEFAULT_OP_AUTHORIZATION)
            .ipAuthorization(DEFAULT_IP_AUTHORIZATION)
            .sponsorPayTax(DEFAULT_SPONSOR_PAY_TAX);
        return plan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plan createUpdatedEntity(EntityManager em) {
        Plan plan = new Plan()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .template(UPDATED_TEMPLATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .applicableStartDate(UPDATED_APPLICABLE_START_DATE)
            .applicableEndDate(UPDATED_APPLICABLE_END_DATE)
            .opAuthorization(UPDATED_OP_AUTHORIZATION)
            .ipAuthorization(UPDATED_IP_AUTHORIZATION)
            .sponsorPayTax(UPDATED_SPONSOR_PAY_TAX);
        return plan;
    }

    @BeforeEach
    public void initTest() {
        plan = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlan() throws Exception {
        int databaseSizeBeforeCreate = planRepository.findAll().size();

        // Create the Plan
        restPlanMockMvc.perform(post("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plan)))
            .andExpect(status().isCreated());

        // Validate the Plan in the database
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeCreate + 1);
        Plan testPlan = planList.get(planList.size() - 1);
        assertThat(testPlan.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlan.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPlan.isTemplate()).isEqualTo(DEFAULT_TEMPLATE);
        assertThat(testPlan.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPlan.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPlan.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testPlan.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testPlan.getContractStartDate()).isEqualTo(DEFAULT_CONTRACT_START_DATE);
        assertThat(testPlan.getContractEndDate()).isEqualTo(DEFAULT_CONTRACT_END_DATE);
        assertThat(testPlan.getApplicableStartDate()).isEqualTo(DEFAULT_APPLICABLE_START_DATE);
        assertThat(testPlan.getApplicableEndDate()).isEqualTo(DEFAULT_APPLICABLE_END_DATE);
        assertThat(testPlan.isOpAuthorization()).isEqualTo(DEFAULT_OP_AUTHORIZATION);
        assertThat(testPlan.isIpAuthorization()).isEqualTo(DEFAULT_IP_AUTHORIZATION);
        assertThat(testPlan.isSponsorPayTax()).isEqualTo(DEFAULT_SPONSOR_PAY_TAX);

        // Validate the Plan in Elasticsearch
        verify(mockPlanSearchRepository, times(1)).save(testPlan);
    }

    @Test
    @Transactional
    public void createPlanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planRepository.findAll().size();

        // Create the Plan with an existing ID
        plan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanMockMvc.perform(post("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plan)))
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeCreate);

        // Validate the Plan in Elasticsearch
        verify(mockPlanSearchRepository, times(0)).save(plan);
    }


    @Test
    @Transactional
    public void getAllPlans() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList
        restPlanMockMvc.perform(get("/api/plans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plan.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractStartDate").value(hasItem(DEFAULT_CONTRACT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractEndDate").value(hasItem(DEFAULT_CONTRACT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].applicableStartDate").value(hasItem(DEFAULT_APPLICABLE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].applicableEndDate").value(hasItem(DEFAULT_APPLICABLE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].opAuthorization").value(hasItem(DEFAULT_OP_AUTHORIZATION.booleanValue())))
            .andExpect(jsonPath("$.[*].ipAuthorization").value(hasItem(DEFAULT_IP_AUTHORIZATION.booleanValue())))
            .andExpect(jsonPath("$.[*].sponsorPayTax").value(hasItem(DEFAULT_SPONSOR_PAY_TAX.booleanValue())));
    }

    @Test
    @Transactional
    public void getPlan() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get the plan
        restPlanMockMvc.perform(get("/api/plans/{id}", plan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plan.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.contractStartDate").value(DEFAULT_CONTRACT_START_DATE.toString()))
            .andExpect(jsonPath("$.contractEndDate").value(DEFAULT_CONTRACT_END_DATE.toString()))
            .andExpect(jsonPath("$.applicableStartDate").value(DEFAULT_APPLICABLE_START_DATE.toString()))
            .andExpect(jsonPath("$.applicableEndDate").value(DEFAULT_APPLICABLE_END_DATE.toString()))
            .andExpect(jsonPath("$.opAuthorization").value(DEFAULT_OP_AUTHORIZATION.booleanValue()))
            .andExpect(jsonPath("$.ipAuthorization").value(DEFAULT_IP_AUTHORIZATION.booleanValue()))
            .andExpect(jsonPath("$.sponsorPayTax").value(DEFAULT_SPONSOR_PAY_TAX.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlan() throws Exception {
        // Get the plan
        restPlanMockMvc.perform(get("/api/plans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlan() throws Exception {
        // Initialize the database
        planService.save(plan);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPlanSearchRepository);

        int databaseSizeBeforeUpdate = planRepository.findAll().size();

        // Update the plan
        Plan updatedPlan = planRepository.findById(plan.getId()).get();
        // Disconnect from session so that the updates on updatedPlan are not directly saved in db
        em.detach(updatedPlan);
        updatedPlan
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .template(UPDATED_TEMPLATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .contractStartDate(UPDATED_CONTRACT_START_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .applicableStartDate(UPDATED_APPLICABLE_START_DATE)
            .applicableEndDate(UPDATED_APPLICABLE_END_DATE)
            .opAuthorization(UPDATED_OP_AUTHORIZATION)
            .ipAuthorization(UPDATED_IP_AUTHORIZATION)
            .sponsorPayTax(UPDATED_SPONSOR_PAY_TAX);

        restPlanMockMvc.perform(put("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlan)))
            .andExpect(status().isOk());

        // Validate the Plan in the database
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeUpdate);
        Plan testPlan = planList.get(planList.size() - 1);
        assertThat(testPlan.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlan.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPlan.isTemplate()).isEqualTo(UPDATED_TEMPLATE);
        assertThat(testPlan.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPlan.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPlan.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testPlan.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testPlan.getContractStartDate()).isEqualTo(UPDATED_CONTRACT_START_DATE);
        assertThat(testPlan.getContractEndDate()).isEqualTo(UPDATED_CONTRACT_END_DATE);
        assertThat(testPlan.getApplicableStartDate()).isEqualTo(UPDATED_APPLICABLE_START_DATE);
        assertThat(testPlan.getApplicableEndDate()).isEqualTo(UPDATED_APPLICABLE_END_DATE);
        assertThat(testPlan.isOpAuthorization()).isEqualTo(UPDATED_OP_AUTHORIZATION);
        assertThat(testPlan.isIpAuthorization()).isEqualTo(UPDATED_IP_AUTHORIZATION);
        assertThat(testPlan.isSponsorPayTax()).isEqualTo(UPDATED_SPONSOR_PAY_TAX);

        // Validate the Plan in Elasticsearch
        verify(mockPlanSearchRepository, times(1)).save(testPlan);
    }

    @Test
    @Transactional
    public void updateNonExistingPlan() throws Exception {
        int databaseSizeBeforeUpdate = planRepository.findAll().size();

        // Create the Plan

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanMockMvc.perform(put("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plan)))
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Plan in Elasticsearch
        verify(mockPlanSearchRepository, times(0)).save(plan);
    }

    @Test
    @Transactional
    public void deletePlan() throws Exception {
        // Initialize the database
        planService.save(plan);

        int databaseSizeBeforeDelete = planRepository.findAll().size();

        // Delete the plan
        restPlanMockMvc.perform(delete("/api/plans/{id}", plan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Plan in Elasticsearch
        verify(mockPlanSearchRepository, times(1)).deleteById(plan.getId());
    }

    @Test
    @Transactional
    public void searchPlan() throws Exception {
        // Initialize the database
        planService.save(plan);
        when(mockPlanSearchRepository.search(queryStringQuery("id:" + plan.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(plan), PageRequest.of(0, 1), 1));
        // Search the plan
        restPlanMockMvc.perform(get("/api/_search/plans?query=id:" + plan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plan.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractStartDate").value(hasItem(DEFAULT_CONTRACT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractEndDate").value(hasItem(DEFAULT_CONTRACT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].applicableStartDate").value(hasItem(DEFAULT_APPLICABLE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].applicableEndDate").value(hasItem(DEFAULT_APPLICABLE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].opAuthorization").value(hasItem(DEFAULT_OP_AUTHORIZATION.booleanValue())))
            .andExpect(jsonPath("$.[*].ipAuthorization").value(hasItem(DEFAULT_IP_AUTHORIZATION.booleanValue())))
            .andExpect(jsonPath("$.[*].sponsorPayTax").value(hasItem(DEFAULT_SPONSOR_PAY_TAX.booleanValue())));
    }
}
