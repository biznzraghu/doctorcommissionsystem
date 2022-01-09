package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.Configurations;
import org.nh.artha.domain.User;
import org.nh.artha.repository.ConfigurationsRepository;
import org.nh.artha.repository.search.ConfigurationsSearchRepository;
import org.nh.artha.service.ConfigurationsService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nh.artha.domain.enumeration.ConfigurationLevel;
/**
 * Integration tests for the {@link ConfigurationsResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ConfigurationsResourceIT {

    private static final ConfigurationLevel DEFAULT_APPLICABLE_TYPE = ConfigurationLevel.Global;
    private static final ConfigurationLevel UPDATED_APPLICABLE_TYPE = ConfigurationLevel.Unit;

    private static final Long DEFAULT_APPLICABLE_TO = 1L;
    private static final Long UPDATED_APPLICABLE_TO = 2L;

    private static final Map<String,String> DEFAULT_CONFIGURATION = new HashMap<>();
    private static final Map<String,String> UPDATED_CONFIGURATION = new HashMap<>();

    private static final Map<String, Object> DEFAULT_APPLICABLE_ENTITY = new HashMap<>();
    private static final Map<String, Object> UPDATED_APPLICABLE_ENTITY = new HashMap<>();

    private static final User DEFAULT_CREATED_BY = new User();
    private static final User UPDATED_CREATED_BY =new User();

    private static final LocalDateTime DEFAULT_CREATION_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CREATION_DATE = LocalDateTime.now();

    private static final User DEFAULT_MODIFIED_BY = new User();
    private static final User UPDATED_MODIFIED_BY = new User();

    private static final LocalDateTime DEFAULT_MODIFIED_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_MODIFIED_DATE = LocalDateTime.now();

    @Autowired
    private ConfigurationsRepository configurationsRepository;

    @Autowired
    private ConfigurationsService configurationsService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ConfigurationsSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigurationsSearchRepository mockConfigurationsSearchRepository;

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

    private MockMvc restConfigurationsMockMvc;

    private Configurations configurations;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigurationsResource configurationsResource = new ConfigurationsResource(configurationsService);
        this.restConfigurationsMockMvc = MockMvcBuilders.standaloneSetup(configurationsResource)
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
    public static Configurations createEntity(EntityManager em) {
        Configurations configurations = new Configurations()
            .applicableType(DEFAULT_APPLICABLE_TYPE)
            .applicableTo(DEFAULT_APPLICABLE_TO)
            .configuration(DEFAULT_CONFIGURATION)
            .applicableEntity(DEFAULT_APPLICABLE_ENTITY)
            .createdBy(DEFAULT_CREATED_BY)
            .creationDate(DEFAULT_CREATION_DATE)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return configurations;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Configurations createUpdatedEntity(EntityManager em) {
        Configurations configurations = new Configurations()
            .applicableType(UPDATED_APPLICABLE_TYPE)
            .applicableTo(UPDATED_APPLICABLE_TO)
            .configuration(UPDATED_CONFIGURATION)
            .applicableEntity(UPDATED_APPLICABLE_ENTITY)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return configurations;
    }

    @BeforeEach
    public void initTest() {
        configurations = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigurations() throws Exception {
        int databaseSizeBeforeCreate = configurationsRepository.findAll().size();

        // Create the Configurations
        restConfigurationsMockMvc.perform(post("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurations)))
            .andExpect(status().isCreated());

        // Validate the Configurations in the database
        List<Configurations> configurationsList = configurationsRepository.findAll();
        assertThat(configurationsList).hasSize(databaseSizeBeforeCreate + 1);
        Configurations testConfigurations = configurationsList.get(configurationsList.size() - 1);
        assertThat(testConfigurations.getApplicableType()).isEqualTo(DEFAULT_APPLICABLE_TYPE);
        assertThat(testConfigurations.getApplicableTo()).isEqualTo(DEFAULT_APPLICABLE_TO);
        assertThat(testConfigurations.getConfiguration()).isEqualTo(DEFAULT_CONFIGURATION);
        assertThat(testConfigurations.getApplicableEntity()).isEqualTo(DEFAULT_APPLICABLE_ENTITY);
        assertThat(testConfigurations.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testConfigurations.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testConfigurations.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testConfigurations.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);

        // Validate the Configurations in Elasticsearch
        verify(mockConfigurationsSearchRepository, times(1)).save(testConfigurations);
    }

    @Test
    @Transactional
    public void createConfigurationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configurationsRepository.findAll().size();

        // Create the Configurations with an existing ID
        configurations.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationsMockMvc.perform(post("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurations)))
            .andExpect(status().isBadRequest());

        // Validate the Configurations in the database
        List<Configurations> configurationsList = configurationsRepository.findAll();
        assertThat(configurationsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Configurations in Elasticsearch
        verify(mockConfigurationsSearchRepository, times(0)).save(configurations);
    }


    @Test
    @Transactional
    public void checkApplicableToIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationsRepository.findAll().size();
        // set the field null
        configurations.setApplicableTo(null);

        // Create the Configurations, which fails.

        restConfigurationsMockMvc.perform(post("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurations)))
            .andExpect(status().isBadRequest());

        List<Configurations> configurationsList = configurationsRepository.findAll();
        assertThat(configurationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfigurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationsRepository.findAll().size();
        // set the field null
        configurations.setConfiguration(null);

        // Create the Configurations, which fails.

        restConfigurationsMockMvc.perform(post("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurations)))
            .andExpect(status().isBadRequest());

        List<Configurations> configurationsList = configurationsRepository.findAll();
        assertThat(configurationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigurations() throws Exception {
        // Initialize the database
        configurationsRepository.saveAndFlush(configurations);

        // Get all the configurationsList
        restConfigurationsMockMvc.perform(get("/api/configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurations.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicableType").value(hasItem(DEFAULT_APPLICABLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].applicableTo").value(hasItem(DEFAULT_APPLICABLE_TO.intValue())))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].applicableEntity").value(hasItem(DEFAULT_APPLICABLE_ENTITY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getConfigurations() throws Exception {
        // Initialize the database
        configurationsRepository.saveAndFlush(configurations);

        // Get the configurations
        restConfigurationsMockMvc.perform(get("/api/configurations/{id}", configurations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configurations.getId().intValue()))
            .andExpect(jsonPath("$.applicableType").value(DEFAULT_APPLICABLE_TYPE.toString()))
            .andExpect(jsonPath("$.applicableTo").value(DEFAULT_APPLICABLE_TO.intValue()))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION))
            .andExpect(jsonPath("$.applicableEntity").value(DEFAULT_APPLICABLE_ENTITY))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigurations() throws Exception {
        // Get the configurations
        restConfigurationsMockMvc.perform(get("/api/configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigurations() throws Exception {
        // Initialize the database
        configurationsService.save(configurations);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockConfigurationsSearchRepository);

        int databaseSizeBeforeUpdate = configurationsRepository.findAll().size();

        // Update the configurations
        Configurations updatedConfigurations = configurationsRepository.findById(configurations.getId()).get();
        // Disconnect from session so that the updates on updatedConfigurations are not directly saved in db
        em.detach(updatedConfigurations);
        updatedConfigurations
            .applicableType(UPDATED_APPLICABLE_TYPE)
            .applicableTo(UPDATED_APPLICABLE_TO)
            .configuration(UPDATED_CONFIGURATION)
            .applicableEntity(UPDATED_APPLICABLE_ENTITY)
            .createdBy(UPDATED_CREATED_BY)
            .creationDate(UPDATED_CREATION_DATE)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restConfigurationsMockMvc.perform(put("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigurations)))
            .andExpect(status().isOk());

        // Validate the Configurations in the database
        List<Configurations> configurationsList = configurationsRepository.findAll();
        assertThat(configurationsList).hasSize(databaseSizeBeforeUpdate);
        Configurations testConfigurations = configurationsList.get(configurationsList.size() - 1);
        assertThat(testConfigurations.getApplicableType()).isEqualTo(UPDATED_APPLICABLE_TYPE);
        assertThat(testConfigurations.getApplicableTo()).isEqualTo(UPDATED_APPLICABLE_TO);
        assertThat(testConfigurations.getConfiguration()).isEqualTo(UPDATED_CONFIGURATION);
        assertThat(testConfigurations.getApplicableEntity()).isEqualTo(UPDATED_APPLICABLE_ENTITY);
        assertThat(testConfigurations.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testConfigurations.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testConfigurations.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testConfigurations.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);

        // Validate the Configurations in Elasticsearch
        verify(mockConfigurationsSearchRepository, times(1)).save(testConfigurations);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigurations() throws Exception {
        int databaseSizeBeforeUpdate = configurationsRepository.findAll().size();

        // Create the Configurations

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationsMockMvc.perform(put("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurations)))
            .andExpect(status().isBadRequest());

        // Validate the Configurations in the database
        List<Configurations> configurationsList = configurationsRepository.findAll();
        assertThat(configurationsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Configurations in Elasticsearch
        verify(mockConfigurationsSearchRepository, times(0)).save(configurations);
    }

    @Test
    @Transactional
    public void deleteConfigurations() throws Exception {
        // Initialize the database
        configurationsService.save(configurations);

        int databaseSizeBeforeDelete = configurationsRepository.findAll().size();

        // Delete the configurations
        restConfigurationsMockMvc.perform(delete("/api/configurations/{id}", configurations.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Configurations> configurationsList = configurationsRepository.findAll();
        assertThat(configurationsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Configurations in Elasticsearch
        verify(mockConfigurationsSearchRepository, times(1)).deleteById(configurations.getId());
    }

    @Test
    @Transactional
    public void searchConfigurations() throws Exception {
        // Initialize the database
        configurationsService.save(configurations);
        when(mockConfigurationsSearchRepository.search(queryStringQuery("id:" + configurations.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(configurations), PageRequest.of(0, 1), 1));
        // Search the configurations
        restConfigurationsMockMvc.perform(get("/api/_search/configurations?query=id:" + configurations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurations.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicableType").value(hasItem(DEFAULT_APPLICABLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].applicableTo").value(hasItem(DEFAULT_APPLICABLE_TO.intValue())))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].applicableEntity").value(hasItem(DEFAULT_APPLICABLE_ENTITY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }
}
