package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ConfigurationDefination;
import org.nh.artha.domain.ConfigurationCategory;
import org.nh.artha.repository.ConfigurationDefinationRepository;
import org.nh.artha.repository.search.ConfigurationDefinationSearchRepository;
import org.nh.artha.service.ConfigurationDefinationService;
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
import java.util.*;

import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nh.artha.domain.enumeration.ConfigurationLevel;
import org.nh.artha.domain.enumeration.ConfigurationLevel;
import org.nh.artha.domain.enumeration.ConfigurationInputType;
/**
 * Integration tests for the {@link ConfigurationDefinationResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ConfigurationDefinationResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final List<Map<String,String>> DEFAULT_INPUT_VALUE = new ArrayList<>();
    private static final List<Map<String,String>> UPDATED_INPUT_VALUE = new ArrayList<>();

    private static final ConfigurationLevel DEFAULT_ALLOWABLE_OVERRIDE_LEVEL = ConfigurationLevel.Global;
    private static final ConfigurationLevel UPDATED_ALLOWABLE_OVERRIDE_LEVEL = ConfigurationLevel.Unit;

    private static final ConfigurationLevel DEFAULT_ALLOWABLE_DISPLAY_LEVEL = ConfigurationLevel.Global;
    private static final ConfigurationLevel UPDATED_ALLOWABLE_DISPLAY_LEVEL = ConfigurationLevel.Unit;

    private static final ConfigurationInputType DEFAULT_INPUT_TYPE = ConfigurationInputType.DropDown;
    private static final ConfigurationInputType UPDATED_INPUT_TYPE = ConfigurationInputType.DropDown;

    private static final String DEFAULT_VALIDATIONS = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MANDATORY = false;
    private static final Boolean UPDATED_IS_MANDATORY = true;

    @Autowired
    private ConfigurationDefinationRepository configurationdefinationRepository;

    @Autowired
    private ConfigurationDefinationService configurationdefinationService;
    @Autowired
    private ConfigurationDefinationSearchRepository mockConfigurationDefinationSearchRepository;

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

    private MockMvc restConfigurationDefinationMockMvc;

    private ConfigurationDefination configurationdefination;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigurationDefinationResource configurationdefinationResource = new ConfigurationDefinationResource(configurationdefinationService);
        this.restConfigurationDefinationMockMvc = MockMvcBuilders.standaloneSetup(configurationdefinationResource)
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
    public static ConfigurationDefination createEntity(EntityManager em) {
        ConfigurationDefination configurationdefination = new ConfigurationDefination()
            .key(DEFAULT_KEY)
            .displayName(DEFAULT_DISPLAY_NAME)
            .inputValue(DEFAULT_INPUT_VALUE)
            .allowableOverrideLevel(DEFAULT_ALLOWABLE_OVERRIDE_LEVEL)
            .allowableDisplayLevel(DEFAULT_ALLOWABLE_DISPLAY_LEVEL)
            .inputType(DEFAULT_INPUT_TYPE)
            .validations(DEFAULT_VALIDATIONS)
            .isMandatory(DEFAULT_IS_MANDATORY);
        // Add required entity
        ConfigurationCategory ConfigurationCategory;
        if (TestUtil.findAll(em, ConfigurationCategory.class).isEmpty()) {
            ConfigurationCategory = ConfigurationCategoryResourceIT.createEntity(em);
            em.persist(ConfigurationCategory);
            em.flush();
        } else {
            ConfigurationCategory = TestUtil.findAll(em, ConfigurationCategory.class).get(0);
        }
        configurationdefination.setConfigurationCategory(ConfigurationCategory);
        // Add required entity
        return configurationdefination;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationDefination createUpdatedEntity(EntityManager em) {
        ConfigurationDefination configurationdefination = new ConfigurationDefination()
            .key(UPDATED_KEY)
            .displayName(UPDATED_DISPLAY_NAME)
            .inputValue(UPDATED_INPUT_VALUE)
            .allowableOverrideLevel(UPDATED_ALLOWABLE_OVERRIDE_LEVEL)
            .allowableDisplayLevel(UPDATED_ALLOWABLE_DISPLAY_LEVEL)
            .inputType(UPDATED_INPUT_TYPE)
            .validations(UPDATED_VALIDATIONS)
            .isMandatory(UPDATED_IS_MANDATORY);
        // Add required entity
        ConfigurationCategory ConfigurationCategory;
        if (TestUtil.findAll(em, ConfigurationCategory.class).isEmpty()) {
            ConfigurationCategory = ConfigurationCategoryResourceIT.createUpdatedEntity(em);
            em.persist(ConfigurationCategory);
            em.flush();
        } else {
            ConfigurationCategory = TestUtil.findAll(em, ConfigurationCategory.class).get(0);
        }
        configurationdefination.setConfigurationCategory(ConfigurationCategory);
        // Add required entity
        return configurationdefination;
    }

    @BeforeEach
    public void initTest() {
        configurationdefination = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigurationDefination() throws Exception {
        int databaseSizeBeforeCreate = configurationdefinationRepository.findAll().size();

        // Create the ConfigurationDefination
        restConfigurationDefinationMockMvc.perform(post("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isCreated());

        // Validate the ConfigurationDefination in the database
        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigurationDefination testConfigurationDefination = configurationdefinationList.get(configurationdefinationList.size() - 1);
        assertThat(testConfigurationDefination.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testConfigurationDefination.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testConfigurationDefination.getInputValue()).isEqualTo(DEFAULT_INPUT_VALUE);
        assertThat(testConfigurationDefination.getAllowableOverrideLevel()).isEqualTo(DEFAULT_ALLOWABLE_OVERRIDE_LEVEL);
        assertThat(testConfigurationDefination.getAllowableDisplayLevel()).isEqualTo(DEFAULT_ALLOWABLE_DISPLAY_LEVEL);
        assertThat(testConfigurationDefination.getInputType()).isEqualTo(DEFAULT_INPUT_TYPE);
        assertThat(testConfigurationDefination.getValidations()).isEqualTo(DEFAULT_VALIDATIONS);
        assertThat(testConfigurationDefination.isIsMandatory()).isEqualTo(DEFAULT_IS_MANDATORY);

        // Validate the ConfigurationDefination in Elasticsearch
        verify(mockConfigurationDefinationSearchRepository, times(1)).save(testConfigurationDefination);
    }

    @Test
    @Transactional
    public void createConfigurationDefinationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configurationdefinationRepository.findAll().size();

        // Create the ConfigurationDefination with an existing ID
        configurationdefination.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationDefinationMockMvc.perform(post("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationDefination in the database
        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfigurationDefination in Elasticsearch
        verify(mockConfigurationDefinationSearchRepository, times(0)).save(configurationdefination);
    }


    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationdefinationRepository.findAll().size();
        // set the field null
        configurationdefination.setKey(null);

        // Create the ConfigurationDefination, which fails.

        restConfigurationDefinationMockMvc.perform(post("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isBadRequest());

        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDisplayNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationdefinationRepository.findAll().size();
        // set the field null
        configurationdefination.setDisplayName(null);

        // Create the ConfigurationDefination, which fails.

        restConfigurationDefinationMockMvc.perform(post("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isBadRequest());

        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAllowableOverrideLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationdefinationRepository.findAll().size();
        // set the field null
        configurationdefination.setAllowableOverrideLevel(null);

        // Create the ConfigurationDefination, which fails.

        restConfigurationDefinationMockMvc.perform(post("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isBadRequest());

        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAllowableDisplayLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationdefinationRepository.findAll().size();
        // set the field null
        configurationdefination.setAllowableDisplayLevel(null);

        // Create the ConfigurationDefination, which fails.

        restConfigurationDefinationMockMvc.perform(post("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isBadRequest());

        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInputTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationdefinationRepository.findAll().size();
        // set the field null
        configurationdefination.setInputType(null);

        // Create the ConfigurationDefination, which fails.

        restConfigurationDefinationMockMvc.perform(post("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isBadRequest());

        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = configurationdefinationRepository.findAll().size();
        // set the field null
        configurationdefination.setValidations(null);

        // Create the ConfigurationDefination, which fails.

        restConfigurationDefinationMockMvc.perform(post("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isBadRequest());

        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigurationDefinations() throws Exception {
        // Initialize the database
        configurationdefinationRepository.saveAndFlush(configurationdefination);

        // Get all the configurationdefinationList
        restConfigurationDefinationMockMvc.perform(get("/api/configurationdefinations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationdefination.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].inputValue").value(hasItem(DEFAULT_INPUT_VALUE)))
            .andExpect(jsonPath("$.[*].allowableOverrideLevel").value(hasItem(DEFAULT_ALLOWABLE_OVERRIDE_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].allowableDisplayLevel").value(hasItem(DEFAULT_ALLOWABLE_DISPLAY_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].inputType").value(hasItem(DEFAULT_INPUT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].validations").value(hasItem(DEFAULT_VALIDATIONS)))
            .andExpect(jsonPath("$.[*].isMandatory").value(hasItem(DEFAULT_IS_MANDATORY.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getConfigurationDefination() throws Exception {
        // Initialize the database
        configurationdefinationRepository.saveAndFlush(configurationdefination);

        // Get the configurationdefination
        restConfigurationDefinationMockMvc.perform(get("/api/configurationdefinations/{id}", configurationdefination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configurationdefination.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME))
            .andExpect(jsonPath("$.inputValue").value(DEFAULT_INPUT_VALUE))
            .andExpect(jsonPath("$.allowableOverrideLevel").value(DEFAULT_ALLOWABLE_OVERRIDE_LEVEL.toString()))
            .andExpect(jsonPath("$.allowableDisplayLevel").value(DEFAULT_ALLOWABLE_DISPLAY_LEVEL.toString()))
            .andExpect(jsonPath("$.inputType").value(DEFAULT_INPUT_TYPE.toString()))
            .andExpect(jsonPath("$.validations").value(DEFAULT_VALIDATIONS))
            .andExpect(jsonPath("$.isMandatory").value(DEFAULT_IS_MANDATORY.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigurationDefination() throws Exception {
        // Get the configurationdefination
        restConfigurationDefinationMockMvc.perform(get("/api/configurationdefinations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigurationDefination() throws Exception {
        // Initialize the database
        configurationdefinationService.save(configurationdefination);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockConfigurationDefinationSearchRepository);

        int databaseSizeBeforeUpdate = configurationdefinationRepository.findAll().size();

        // Update the configurationdefination
        ConfigurationDefination updatedConfigurationDefination = configurationdefinationRepository.findById(configurationdefination.getId()).get();
        // Disconnect from session so that the updates on updatedConfigurationDefination are not directly saved in db
        em.detach(updatedConfigurationDefination);
        updatedConfigurationDefination
            .key(UPDATED_KEY)
            .displayName(UPDATED_DISPLAY_NAME)
            .inputValue(UPDATED_INPUT_VALUE)
            .allowableOverrideLevel(UPDATED_ALLOWABLE_OVERRIDE_LEVEL)
            .allowableDisplayLevel(UPDATED_ALLOWABLE_DISPLAY_LEVEL)
            .inputType(UPDATED_INPUT_TYPE)
            .validations(UPDATED_VALIDATIONS)
            .isMandatory(UPDATED_IS_MANDATORY);

        restConfigurationDefinationMockMvc.perform(put("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigurationDefination)))
            .andExpect(status().isOk());

        // Validate the ConfigurationDefination in the database
        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeUpdate);
        ConfigurationDefination testConfigurationDefination = configurationdefinationList.get(configurationdefinationList.size() - 1);
        assertThat(testConfigurationDefination.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfigurationDefination.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testConfigurationDefination.getInputValue()).isEqualTo(UPDATED_INPUT_VALUE);
        assertThat(testConfigurationDefination.getAllowableOverrideLevel()).isEqualTo(UPDATED_ALLOWABLE_OVERRIDE_LEVEL);
        assertThat(testConfigurationDefination.getAllowableDisplayLevel()).isEqualTo(UPDATED_ALLOWABLE_DISPLAY_LEVEL);
        assertThat(testConfigurationDefination.getInputType()).isEqualTo(UPDATED_INPUT_TYPE);
        assertThat(testConfigurationDefination.getValidations()).isEqualTo(UPDATED_VALIDATIONS);
        assertThat(testConfigurationDefination.isIsMandatory()).isEqualTo(UPDATED_IS_MANDATORY);

        // Validate the ConfigurationDefination in Elasticsearch
        verify(mockConfigurationDefinationSearchRepository, times(1)).save(testConfigurationDefination);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigurationDefination() throws Exception {
        int databaseSizeBeforeUpdate = configurationdefinationRepository.findAll().size();

        // Create the ConfigurationDefination

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigurationDefinationMockMvc.perform(put("/api/configurationdefinations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationdefination)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigurationDefination in the database
        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfigurationDefination in Elasticsearch
        verify(mockConfigurationDefinationSearchRepository, times(0)).save(configurationdefination);
    }

    @Test
    @Transactional
    public void deleteConfigurationDefination() throws Exception {
        // Initialize the database
        configurationdefinationService.save(configurationdefination);

        int databaseSizeBeforeDelete = configurationdefinationRepository.findAll().size();

        // Delete the configurationdefination
        restConfigurationDefinationMockMvc.perform(delete("/api/configurationdefinations/{id}", configurationdefination.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigurationDefination> configurationdefinationList = configurationdefinationRepository.findAll();
        assertThat(configurationdefinationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfigurationDefination in Elasticsearch
        verify(mockConfigurationDefinationSearchRepository, times(1)).deleteById(configurationdefination.getId());
    }

    @Test
    @Transactional
    public void searchConfigurationDefination() throws Exception {
        // Initialize the database
        configurationdefinationService.save(configurationdefination);
        when(mockConfigurationDefinationSearchRepository.search(queryStringQuery("id:" + configurationdefination.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(configurationdefination), PageRequest.of(0, 1), 1));
        // Search the configurationdefination
        restConfigurationDefinationMockMvc.perform(get("/api/_search/configurationdefinations?query=id:" + configurationdefination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationdefination.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].inputValue").value(hasItem(DEFAULT_INPUT_VALUE)))
            .andExpect(jsonPath("$.[*].allowableOverrideLevel").value(hasItem(DEFAULT_ALLOWABLE_OVERRIDE_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].allowableDisplayLevel").value(hasItem(DEFAULT_ALLOWABLE_DISPLAY_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].inputType").value(hasItem(DEFAULT_INPUT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].validations").value(hasItem(DEFAULT_VALIDATIONS)))
            .andExpect(jsonPath("$.[*].isMandatory").value(hasItem(DEFAULT_IS_MANDATORY.booleanValue())));
    }
}
