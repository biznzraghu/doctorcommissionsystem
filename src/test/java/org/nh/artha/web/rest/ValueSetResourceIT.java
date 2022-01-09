package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ValueSet;
import org.nh.artha.repository.ValueSetRepository;
import org.nh.artha.repository.search.ValueSetSearchRepository;
import org.nh.artha.service.ValueSetService;
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
 * Integration tests for the {@link ValueSetResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ValueSetResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DEFINITION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_DEFINING_URL = "AAAAAAAAAA";
    private static final String UPDATED_DEFINING_URL = "BBBBBBBBBB";

    private static final String DEFAULT_OID = "AAAAAAAAAA";
    private static final String UPDATED_OID = "BBBBBBBBBB";

    private static final String DEFAULT_SYSTEM_URL = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SYSTEM_OID = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM_OID = "BBBBBBBBBB";

    @Autowired
    private ValueSetRepository valueSetRepository;

    @Autowired
    private ValueSetService valueSetService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ValueSetSearchRepositoryMockConfiguration
     */
    @Autowired
    private ValueSetSearchRepository mockValueSetSearchRepository;

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

    private MockMvc restValueSetMockMvc;

    private ValueSet valueSet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ValueSetResource valueSetResource = new ValueSetResource(valueSetService,valueSetRepository,mockValueSetSearchRepository,applicationProperties);
        this.restValueSetMockMvc = MockMvcBuilders.standaloneSetup(valueSetResource)
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
    public static ValueSet createEntity(EntityManager em) {
        ValueSet valueSet = new ValueSet()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .definition(DEFAULT_DEFINITION)
            .active(DEFAULT_ACTIVE)
            .source(DEFAULT_SOURCE)
            .definingURL(DEFAULT_DEFINING_URL)
            .oid(DEFAULT_OID)
            .systemURL(DEFAULT_SYSTEM_URL)
            .systemOID(DEFAULT_SYSTEM_OID);
        return valueSet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueSet createUpdatedEntity(EntityManager em) {
        ValueSet valueSet = new ValueSet()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .definition(UPDATED_DEFINITION)
            .active(UPDATED_ACTIVE)
            .source(UPDATED_SOURCE)
            .definingURL(UPDATED_DEFINING_URL)
            .oid(UPDATED_OID)
            .systemURL(UPDATED_SYSTEM_URL)
            .systemOID(UPDATED_SYSTEM_OID);
        return valueSet;
    }

    @BeforeEach
    public void initTest() {
        valueSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createValueSet() throws Exception {
        int databaseSizeBeforeCreate = valueSetRepository.findAll().size();

        // Create the ValueSet
        restValueSetMockMvc.perform(post("/api/value-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSet)))
            .andExpect(status().isCreated());

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll();
        assertThat(valueSetList).hasSize(databaseSizeBeforeCreate + 1);
        ValueSet testValueSet = valueSetList.get(valueSetList.size() - 1);
        assertThat(testValueSet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testValueSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testValueSet.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
        assertThat(testValueSet.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testValueSet.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testValueSet.getDefiningURL()).isEqualTo(DEFAULT_DEFINING_URL);
        assertThat(testValueSet.getOid()).isEqualTo(DEFAULT_OID);
        assertThat(testValueSet.getSystemURL()).isEqualTo(DEFAULT_SYSTEM_URL);
        assertThat(testValueSet.getSystemOID()).isEqualTo(DEFAULT_SYSTEM_OID);

        // Validate the ValueSet in Elasticsearch
        verify(mockValueSetSearchRepository, times(1)).save(testValueSet);
    }

    @Test
    @Transactional
    public void createValueSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = valueSetRepository.findAll().size();

        // Create the ValueSet with an existing ID
        valueSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValueSetMockMvc.perform(post("/api/value-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSet)))
            .andExpect(status().isBadRequest());

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll();
        assertThat(valueSetList).hasSize(databaseSizeBeforeCreate);

        // Validate the ValueSet in Elasticsearch
        verify(mockValueSetSearchRepository, times(0)).save(valueSet);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = valueSetRepository.findAll().size();
        // set the field null
        valueSet.setCode(null);

        // Create the ValueSet, which fails.

        restValueSetMockMvc.perform(post("/api/value-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSet)))
            .andExpect(status().isBadRequest());

        List<ValueSet> valueSetList = valueSetRepository.findAll();
        assertThat(valueSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = valueSetRepository.findAll().size();
        // set the field null
        valueSet.setName(null);

        // Create the ValueSet, which fails.

        restValueSetMockMvc.perform(post("/api/value-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSet)))
            .andExpect(status().isBadRequest());

        List<ValueSet> valueSetList = valueSetRepository.findAll();
        assertThat(valueSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = valueSetRepository.findAll().size();
        // set the field null
        valueSet.setActive(null);

        // Create the ValueSet, which fails.

        restValueSetMockMvc.perform(post("/api/value-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSet)))
            .andExpect(status().isBadRequest());

        List<ValueSet> valueSetList = valueSetRepository.findAll();
        assertThat(valueSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllValueSets() throws Exception {
        // Initialize the database
        valueSetRepository.saveAndFlush(valueSet);

        // Get all the valueSetList
        restValueSetMockMvc.perform(get("/api/value-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].definingURL").value(hasItem(DEFAULT_DEFINING_URL)))
            .andExpect(jsonPath("$.[*].oid").value(hasItem(DEFAULT_OID)))
            .andExpect(jsonPath("$.[*].systemURL").value(hasItem(DEFAULT_SYSTEM_URL)))
            .andExpect(jsonPath("$.[*].systemOID").value(hasItem(DEFAULT_SYSTEM_OID)));
    }

    @Test
    @Transactional
    public void getValueSet() throws Exception {
        // Initialize the database
        valueSetRepository.saveAndFlush(valueSet);

        // Get the valueSet
        restValueSetMockMvc.perform(get("/api/value-sets/{id}", valueSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(valueSet.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.definition").value(DEFAULT_DEFINITION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.definingURL").value(DEFAULT_DEFINING_URL))
            .andExpect(jsonPath("$.oid").value(DEFAULT_OID))
            .andExpect(jsonPath("$.systemURL").value(DEFAULT_SYSTEM_URL))
            .andExpect(jsonPath("$.systemOID").value(DEFAULT_SYSTEM_OID));
    }

    @Test
    @Transactional
    public void getNonExistingValueSet() throws Exception {
        // Get the valueSet
        restValueSetMockMvc.perform(get("/api/value-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValueSet() throws Exception {
        // Initialize the database
        valueSetService.save(valueSet);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockValueSetSearchRepository);

        int databaseSizeBeforeUpdate = valueSetRepository.findAll().size();

        // Update the valueSet
        ValueSet updatedValueSet = valueSetRepository.findById(valueSet.getId()).get();
        // Disconnect from session so that the updates on updatedValueSet are not directly saved in db
        em.detach(updatedValueSet);
        updatedValueSet
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .definition(UPDATED_DEFINITION)
            .active(UPDATED_ACTIVE)
            .source(UPDATED_SOURCE)
            .definingURL(UPDATED_DEFINING_URL)
            .oid(UPDATED_OID)
            .systemURL(UPDATED_SYSTEM_URL)
            .systemOID(UPDATED_SYSTEM_OID);

        restValueSetMockMvc.perform(put("/api/value-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedValueSet)))
            .andExpect(status().isOk());

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);
        ValueSet testValueSet = valueSetList.get(valueSetList.size() - 1);
        assertThat(testValueSet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testValueSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValueSet.getDefinition()).isEqualTo(UPDATED_DEFINITION);
        assertThat(testValueSet.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testValueSet.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testValueSet.getDefiningURL()).isEqualTo(UPDATED_DEFINING_URL);
        assertThat(testValueSet.getOid()).isEqualTo(UPDATED_OID);
        assertThat(testValueSet.getSystemURL()).isEqualTo(UPDATED_SYSTEM_URL);
        assertThat(testValueSet.getSystemOID()).isEqualTo(UPDATED_SYSTEM_OID);

        // Validate the ValueSet in Elasticsearch
        verify(mockValueSetSearchRepository, times(1)).save(testValueSet);
    }

    @Test
    @Transactional
    public void updateNonExistingValueSet() throws Exception {
        int databaseSizeBeforeUpdate = valueSetRepository.findAll().size();

        // Create the ValueSet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValueSetMockMvc.perform(put("/api/value-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSet)))
            .andExpect(status().isBadRequest());

        // Validate the ValueSet in the database
        List<ValueSet> valueSetList = valueSetRepository.findAll();
        assertThat(valueSetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ValueSet in Elasticsearch
        verify(mockValueSetSearchRepository, times(0)).save(valueSet);
    }

    @Test
    @Transactional
    public void deleteValueSet() throws Exception {
        // Initialize the database
        valueSetService.save(valueSet);

        int databaseSizeBeforeDelete = valueSetRepository.findAll().size();

        // Delete the valueSet
        restValueSetMockMvc.perform(delete("/api/value-sets/{id}", valueSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValueSet> valueSetList = valueSetRepository.findAll();
        assertThat(valueSetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ValueSet in Elasticsearch
        verify(mockValueSetSearchRepository, times(1)).deleteById(valueSet.getId());
    }

    @Test
    @Transactional
    public void searchValueSet() throws Exception {
        // Initialize the database
        valueSetService.save(valueSet);
        when(mockValueSetSearchRepository.search(queryStringQuery("id:" + valueSet.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(valueSet), PageRequest.of(0, 1), 1));
        // Search the valueSet
        restValueSetMockMvc.perform(get("/api/_search/value-sets?query=id:" + valueSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].definingURL").value(hasItem(DEFAULT_DEFINING_URL)))
            .andExpect(jsonPath("$.[*].oid").value(hasItem(DEFAULT_OID)))
            .andExpect(jsonPath("$.[*].systemURL").value(hasItem(DEFAULT_SYSTEM_URL)))
            .andExpect(jsonPath("$.[*].systemOID").value(hasItem(DEFAULT_SYSTEM_OID)));
    }
}
