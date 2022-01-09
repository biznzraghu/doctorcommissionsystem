package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.OrganizationType;
import org.nh.artha.domain.ValueSet;
import org.nh.artha.repository.OrganizationTypeRepository;
import org.nh.artha.repository.search.OrganizationTypeSearchRepository;
import org.nh.artha.service.OrganizationTypeService;
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
 * Integration tests for the {@link OrganizationTypeResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class OrganizationTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DEFINITION = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;

    @Autowired
    private OrganizationTypeRepository organizationTypeRepository;

    @Autowired
    private OrganizationTypeService organizationTypeService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.OrganizationTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrganizationTypeSearchRepository mockOrganizationTypeSearchRepository;

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

    private MockMvc restOrganizationTypeMockMvc;

    private OrganizationType organizationType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganizationTypeResource organizationTypeResource = new OrganizationTypeResource(organizationTypeService);
        this.restOrganizationTypeMockMvc = MockMvcBuilders.standaloneSetup(organizationTypeResource)
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
    public static OrganizationType createEntity(EntityManager em) {
        OrganizationType organizationType = new OrganizationType();
        organizationType.code(DEFAULT_CODE);
        organizationType.display(DEFAULT_DISPLAY);
        organizationType.active(DEFAULT_ACTIVE);
        organizationType.definition(DEFAULT_DEFINITION);
        organizationType.source(DEFAULT_SOURCE);
        organizationType.level(DEFAULT_LEVEL);
        organizationType.comments(DEFAULT_COMMENTS);
        organizationType.displayOrder(DEFAULT_DISPLAY_ORDER);
        // Add required entity
        ValueSet valueSet;
        if (TestUtil.findAll(em, ValueSet.class).isEmpty()) {
            valueSet = ValueSetResourceIT.createEntity(em);
            em.persist(valueSet);
            em.flush();
        } else {
            valueSet = TestUtil.findAll(em, ValueSet.class).get(0);
        }
        organizationType.setValueSet(valueSet);
        return organizationType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationType createUpdatedEntity(EntityManager em) {
        OrganizationType organizationType = new OrganizationType();
        organizationType.code(UPDATED_CODE);
        organizationType.display(UPDATED_DISPLAY);
        organizationType.active(UPDATED_ACTIVE);
        organizationType.definition(UPDATED_DEFINITION);
        organizationType.source(UPDATED_SOURCE);
        organizationType.level(UPDATED_LEVEL);
        organizationType.comments(UPDATED_COMMENTS);
        organizationType.displayOrder(UPDATED_DISPLAY_ORDER);
        // Add required entity
        ValueSet valueSet;
        if (TestUtil.findAll(em, ValueSet.class).isEmpty()) {
            valueSet = ValueSetResourceIT.createUpdatedEntity(em);
            em.persist(valueSet);
            em.flush();
        } else {
            valueSet = TestUtil.findAll(em, ValueSet.class).get(0);
        }
        organizationType.setValueSet(valueSet);
        return organizationType;
    }

    @BeforeEach
    public void initTest() {
        organizationType = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizationType() throws Exception {
        int databaseSizeBeforeCreate = organizationTypeRepository.findAll().size();

        // Create the OrganizationType
        restOrganizationTypeMockMvc.perform(post("/api/organization-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationType)))
            .andExpect(status().isCreated());

        // Validate the OrganizationType in the database
        List<OrganizationType> organizationTypeList = organizationTypeRepository.findAll();
        assertThat(organizationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationType testOrganizationType = organizationTypeList.get(organizationTypeList.size() - 1);
        assertThat(testOrganizationType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganizationType.getDisplay()).isEqualTo(DEFAULT_DISPLAY);
        assertThat(testOrganizationType.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testOrganizationType.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
        assertThat(testOrganizationType.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testOrganizationType.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testOrganizationType.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testOrganizationType.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);

        // Validate the OrganizationType in Elasticsearch
        verify(mockOrganizationTypeSearchRepository, times(1)).save(testOrganizationType);
    }

    @Test
    @Transactional
    public void createOrganizationTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationTypeRepository.findAll().size();

        // Create the OrganizationType with an existing ID
        organizationType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationTypeMockMvc.perform(post("/api/organization-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationType)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationType in the database
        List<OrganizationType> organizationTypeList = organizationTypeRepository.findAll();
        assertThat(organizationTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrganizationType in Elasticsearch
        verify(mockOrganizationTypeSearchRepository, times(0)).save(organizationType);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationTypeRepository.findAll().size();
        // set the field null
        organizationType.setCode(null);

        // Create the OrganizationType, which fails.

        restOrganizationTypeMockMvc.perform(post("/api/organization-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationType)))
            .andExpect(status().isBadRequest());

        List<OrganizationType> organizationTypeList = organizationTypeRepository.findAll();
        assertThat(organizationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationTypeRepository.findAll().size();
        // set the field null
        organizationType.setActive(null);

        // Create the OrganizationType, which fails.

        restOrganizationTypeMockMvc.perform(post("/api/organization-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationType)))
            .andExpect(status().isBadRequest());

        List<OrganizationType> organizationTypeList = organizationTypeRepository.findAll();
        assertThat(organizationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizationTypes() throws Exception {
        // Initialize the database
        organizationTypeRepository.saveAndFlush(organizationType);

        // Get all the organizationTypeList
        restOrganizationTypeMockMvc.perform(get("/api/organization-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)));
    }
    
    @Test
    @Transactional
    public void getOrganizationType() throws Exception {
        // Initialize the database
        organizationTypeRepository.saveAndFlush(organizationType);

        // Get the organizationType
        restOrganizationTypeMockMvc.perform(get("/api/organization-types/{id}", organizationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organizationType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.display").value(DEFAULT_DISPLAY))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.definition").value(DEFAULT_DEFINITION))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationType() throws Exception {
        // Get the organizationType
        restOrganizationTypeMockMvc.perform(get("/api/organization-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizationType() throws Exception {
        // Initialize the database
        organizationTypeService.save(organizationType);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOrganizationTypeSearchRepository);

        int databaseSizeBeforeUpdate = organizationTypeRepository.findAll().size();

        // Update the organizationType
        OrganizationType updatedOrganizationType = organizationTypeRepository.findById(organizationType.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationType are not directly saved in db
        em.detach(updatedOrganizationType);
        updatedOrganizationType
            .code(UPDATED_CODE)
            .display(UPDATED_DISPLAY)
            .active(UPDATED_ACTIVE)
            .definition(UPDATED_DEFINITION)
            .source(UPDATED_SOURCE)
            .level(UPDATED_LEVEL)
            .comments(UPDATED_COMMENTS)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restOrganizationTypeMockMvc.perform(put("/api/organization-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationType)))
            .andExpect(status().isOk());

        // Validate the OrganizationType in the database
        List<OrganizationType> organizationTypeList = organizationTypeRepository.findAll();
        assertThat(organizationTypeList).hasSize(databaseSizeBeforeUpdate);
        OrganizationType testOrganizationType = organizationTypeList.get(organizationTypeList.size() - 1);
        assertThat(testOrganizationType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganizationType.getDisplay()).isEqualTo(UPDATED_DISPLAY);
        assertThat(testOrganizationType.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testOrganizationType.getDefinition()).isEqualTo(UPDATED_DEFINITION);
        assertThat(testOrganizationType.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testOrganizationType.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testOrganizationType.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testOrganizationType.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);

        // Validate the OrganizationType in Elasticsearch
        verify(mockOrganizationTypeSearchRepository, times(1)).save(testOrganizationType);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganizationType() throws Exception {
        int databaseSizeBeforeUpdate = organizationTypeRepository.findAll().size();

        // Create the OrganizationType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationTypeMockMvc.perform(put("/api/organization-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationType)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationType in the database
        List<OrganizationType> organizationTypeList = organizationTypeRepository.findAll();
        assertThat(organizationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrganizationType in Elasticsearch
        verify(mockOrganizationTypeSearchRepository, times(0)).save(organizationType);
    }

    @Test
    @Transactional
    public void deleteOrganizationType() throws Exception {
        // Initialize the database
        organizationTypeService.save(organizationType);

        int databaseSizeBeforeDelete = organizationTypeRepository.findAll().size();

        // Delete the organizationType
        restOrganizationTypeMockMvc.perform(delete("/api/organization-types/{id}", organizationType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationType> organizationTypeList = organizationTypeRepository.findAll();
        assertThat(organizationTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrganizationType in Elasticsearch
        verify(mockOrganizationTypeSearchRepository, times(1)).deleteById(organizationType.getId());
    }

    @Test
    @Transactional
    public void searchOrganizationType() throws Exception {
        // Initialize the database
        organizationTypeService.save(organizationType);
        when(mockOrganizationTypeSearchRepository.search(queryStringQuery("id:" + organizationType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(organizationType), PageRequest.of(0, 1), 1));
        // Search the organizationType
        restOrganizationTypeMockMvc.perform(get("/api/_search/organization-types?query=id:" + organizationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)));
    }
}
