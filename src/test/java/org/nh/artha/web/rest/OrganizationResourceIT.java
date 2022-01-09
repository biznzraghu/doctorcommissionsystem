package org.nh.artha.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Organization;
import org.nh.artha.domain.OrganizationType;
import org.nh.artha.repository.OrganizationRepository;
import org.nh.artha.repository.search.OrganizationSearchRepository;
import org.nh.artha.service.DepartmentService;
import org.nh.artha.service.OrganizationService;
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
import java.time.LocalDate;
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
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class OrganizationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STARTED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_CLINICAL = false;
    private static final Boolean UPDATED_CLINICAL = true;

    private static final String DEFAULT_LICENSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationService organizationService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.OrganizationSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrganizationSearchRepository mockOrganizationSearchRepository;

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
    private  ApplicationProperties applicationProperties;

    @Autowired
    private  OrganizationSearchRepository organizationSearchRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;


    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    final OrganizationResource organizationResource = new OrganizationResource(organizationService,organizationRepository,applicationProperties,organizationSearchRepository,departmentService,objectMapper);
        this.restOrganizationMockMvc = MockMvcBuilders.standaloneSetup(organizationResource)
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
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization()
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .code(DEFAULT_CODE)
            .startedOn(DEFAULT_STARTED_ON)
            .clinical(DEFAULT_CLINICAL)
            .licenseNumber(DEFAULT_LICENSE_NUMBER)
            .website(DEFAULT_WEBSITE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        OrganizationType organizationType;
        if (TestUtil.findAll(em, OrganizationType.class).isEmpty()) {
            organizationType = OrganizationTypeResourceIT.createEntity(em);
            em.persist(organizationType);
            em.flush();
        } else {
            organizationType = TestUtil.findAll(em, OrganizationType.class).get(0);
        }
        organization.setType(organizationType);
        return organization;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity(EntityManager em) {
        Organization organization = new Organization()
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .code(UPDATED_CODE)
            .startedOn(UPDATED_STARTED_ON)
            .clinical(UPDATED_CLINICAL)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        OrganizationType organizationType;
        if (TestUtil.findAll(em, OrganizationType.class).isEmpty()) {
            organizationType = OrganizationTypeResourceIT.createUpdatedEntity(em);
            em.persist(organizationType);
            em.flush();
        } else {
            organizationType = TestUtil.findAll(em, OrganizationType.class).get(0);
        }
        organization.setType(organizationType);
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganization.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testOrganization.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrganization.getStartedOn()).isEqualTo(DEFAULT_STARTED_ON);
        assertThat(testOrganization.isClinical()).isEqualTo(DEFAULT_CLINICAL);
        assertThat(testOrganization.getLicenseNumber()).isEqualTo(DEFAULT_LICENSE_NUMBER);
        assertThat(testOrganization.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testOrganization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(1)).save(testOrganization);
    }

    @Test
    @Transactional
    public void createOrganizationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization with an existing ID
        organization.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(0)).save(organization);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setName(null);

        // Create the Organization, which fails.

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setActive(null);

        // Create the Organization, which fails.

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setCode(null);

        // Create the Organization, which fails.

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].startedOn").value(hasItem(DEFAULT_STARTED_ON.toString())))
            .andExpect(jsonPath("$.[*].clinical").value(hasItem(DEFAULT_CLINICAL.booleanValue())))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.startedOn").value(DEFAULT_STARTED_ON.toString()))
            .andExpect(jsonPath("$.clinical").value(DEFAULT_CLINICAL.booleanValue()))
            .andExpect(jsonPath("$.licenseNumber").value(DEFAULT_LICENSE_NUMBER))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganization() throws Exception {
        // Initialize the database
        organizationService.save(organization);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOrganizationSearchRepository);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).get();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .code(UPDATED_CODE)
            .startedOn(UPDATED_STARTED_ON)
            .clinical(UPDATED_CLINICAL)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION);

        restOrganizationMockMvc.perform(put("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrganization)))
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testOrganization.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrganization.getStartedOn()).isEqualTo(UPDATED_STARTED_ON);
        assertThat(testOrganization.isClinical()).isEqualTo(UPDATED_CLINICAL);
        assertThat(testOrganization.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
        assertThat(testOrganization.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(1)).save(testOrganization);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Create the Organization

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc.perform(put("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(0)).save(organization);
    }

    @Test
    @Transactional
    public void deleteOrganization() throws Exception {
        // Initialize the database
        organizationService.save(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Delete the organization
        restOrganizationMockMvc.perform(delete("/api/organizations/{id}", organization.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(1)).deleteById(organization.getId());
    }

    @Test
    @Transactional
    public void searchOrganization() throws Exception {
        // Initialize the database
        organizationService.save(organization);
        when(mockOrganizationSearchRepository.search(queryStringQuery("id:" + organization.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(organization), PageRequest.of(0, 1), 1));
        // Search the organization
        restOrganizationMockMvc.perform(get("/api/_search/organizations?query=id:" + organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].startedOn").value(hasItem(DEFAULT_STARTED_ON.toString())))
            .andExpect(jsonPath("$.[*].clinical").value(hasItem(DEFAULT_CLINICAL.booleanValue())))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
