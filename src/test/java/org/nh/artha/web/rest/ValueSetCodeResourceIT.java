package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ValueSetCode;
import org.nh.artha.domain.ValueSet;
import org.nh.artha.repository.ValueSetCodeRepository;
import org.nh.artha.repository.search.ValueSetCodeSearchRepository;
import org.nh.artha.service.ValueSetCodeService;
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
 * Integration tests for the {@link ValueSetCodeResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ValueSetCodeResourceIT {

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
    private ValueSetCodeRepository valueSetCodeRepository;

    @Autowired
    private ValueSetCodeService valueSetCodeService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ValueSetCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ValueSetCodeSearchRepository mockValueSetCodeSearchRepository;

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

    private MockMvc restValueSetCodeMockMvc;

    private ValueSetCode valueSetCode;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ValueSetCodeResource valueSetCodeResource = new ValueSetCodeResource(valueSetCodeService,valueSetCodeRepository,mockValueSetCodeSearchRepository,applicationProperties);
        this.restValueSetCodeMockMvc = MockMvcBuilders.standaloneSetup(valueSetCodeResource)
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
    public static ValueSetCode createEntity(EntityManager em) {
        ValueSetCode valueSetCode =  new ValueSetCode();
        valueSetCode.code(DEFAULT_CODE);
        valueSetCode.display(DEFAULT_DISPLAY);
        valueSetCode.active(DEFAULT_ACTIVE);
        valueSetCode.definition(DEFAULT_DEFINITION);
        valueSetCode.source(DEFAULT_SOURCE);
        valueSetCode.level(DEFAULT_LEVEL);
        valueSetCode.comments(DEFAULT_COMMENTS);
        valueSetCode.displayOrder(DEFAULT_DISPLAY_ORDER);
        // Add required entity
        ValueSet valueSet;
        if (TestUtil.findAll(em, ValueSet.class).isEmpty()) {
            valueSet = ValueSetResourceIT.createEntity(em);
            em.persist(valueSet);
            em.flush();
        } else {
            valueSet = TestUtil.findAll(em, ValueSet.class).get(0);
        }
        valueSetCode.setValueSet(valueSet);
        return valueSetCode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueSetCode createUpdatedEntity(EntityManager em) {
        ValueSetCode valueSetCode = (ValueSetCode) new ValueSetCode()
            .code(UPDATED_CODE)
            .display(UPDATED_DISPLAY)
            .active(UPDATED_ACTIVE)
            .definition(UPDATED_DEFINITION)
            .source(UPDATED_SOURCE)
            .level(UPDATED_LEVEL)
            .comments(UPDATED_COMMENTS)
            .displayOrder(UPDATED_DISPLAY_ORDER);
        // Add required entity
        ValueSet valueSet;
        if (TestUtil.findAll(em, ValueSet.class).isEmpty()) {
            valueSet = ValueSetResourceIT.createUpdatedEntity(em);
            em.persist(valueSet);
            em.flush();
        } else {
            valueSet = TestUtil.findAll(em, ValueSet.class).get(0);
        }
        valueSetCode.setValueSet(valueSet);
        return valueSetCode;
    }

    @BeforeEach
    public void initTest() {
        valueSetCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createValueSetCode() throws Exception {
        int databaseSizeBeforeCreate = valueSetCodeRepository.findAll().size();

        // Create the ValueSetCode
        restValueSetCodeMockMvc.perform(post("/api/value-set-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSetCode)))
            .andExpect(status().isCreated());

        // Validate the ValueSetCode in the database
        List<ValueSetCode> valueSetCodeList = valueSetCodeRepository.findAll();
        assertThat(valueSetCodeList).hasSize(databaseSizeBeforeCreate + 1);
        ValueSetCode testValueSetCode = valueSetCodeList.get(valueSetCodeList.size() - 1);
        assertThat(testValueSetCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testValueSetCode.getDisplay()).isEqualTo(DEFAULT_DISPLAY);
        assertThat(testValueSetCode.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testValueSetCode.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
        assertThat(testValueSetCode.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testValueSetCode.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testValueSetCode.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testValueSetCode.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);

        // Validate the ValueSetCode in Elasticsearch
        verify(mockValueSetCodeSearchRepository, times(1)).save(testValueSetCode);
    }

    @Test
    @Transactional
    public void createValueSetCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = valueSetCodeRepository.findAll().size();

        // Create the ValueSetCode with an existing ID
        valueSetCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValueSetCodeMockMvc.perform(post("/api/value-set-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSetCode)))
            .andExpect(status().isBadRequest());

        // Validate the ValueSetCode in the database
        List<ValueSetCode> valueSetCodeList = valueSetCodeRepository.findAll();
        assertThat(valueSetCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ValueSetCode in Elasticsearch
        verify(mockValueSetCodeSearchRepository, times(0)).save(valueSetCode);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = valueSetCodeRepository.findAll().size();
        // set the field null
        valueSetCode.setCode(null);

        // Create the ValueSetCode, which fails.

        restValueSetCodeMockMvc.perform(post("/api/value-set-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSetCode)))
            .andExpect(status().isBadRequest());

        List<ValueSetCode> valueSetCodeList = valueSetCodeRepository.findAll();
        assertThat(valueSetCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = valueSetCodeRepository.findAll().size();
        // set the field null
        valueSetCode.setActive(null);

        // Create the ValueSetCode, which fails.

        restValueSetCodeMockMvc.perform(post("/api/value-set-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSetCode)))
            .andExpect(status().isBadRequest());

        List<ValueSetCode> valueSetCodeList = valueSetCodeRepository.findAll();
        assertThat(valueSetCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllValueSetCodes() throws Exception {
        // Initialize the database
        valueSetCodeRepository.saveAndFlush(valueSetCode);

        // Get all the valueSetCodeList
        restValueSetCodeMockMvc.perform(get("/api/value-set-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueSetCode.getId().intValue())))
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
    public void getValueSetCode() throws Exception {
        // Initialize the database
        valueSetCodeRepository.saveAndFlush(valueSetCode);

        // Get the valueSetCode
        restValueSetCodeMockMvc.perform(get("/api/value-set-codes/{id}", valueSetCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(valueSetCode.getId().intValue()))
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
    public void getNonExistingValueSetCode() throws Exception {
        // Get the valueSetCode
        restValueSetCodeMockMvc.perform(get("/api/value-set-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValueSetCode() throws Exception {
        // Initialize the database
        valueSetCodeService.save(valueSetCode);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockValueSetCodeSearchRepository);

        int databaseSizeBeforeUpdate = valueSetCodeRepository.findAll().size();

        // Update the valueSetCode
        ValueSetCode updatedValueSetCode = valueSetCodeRepository.findById(valueSetCode.getId()).get();
        // Disconnect from session so that the updates on updatedValueSetCode are not directly saved in db
        em.detach(updatedValueSetCode);
        updatedValueSetCode
            .code(UPDATED_CODE)
            .display(UPDATED_DISPLAY)
            .active(UPDATED_ACTIVE)
            .definition(UPDATED_DEFINITION)
            .source(UPDATED_SOURCE)
            .level(UPDATED_LEVEL)
            .comments(UPDATED_COMMENTS)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restValueSetCodeMockMvc.perform(put("/api/value-set-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedValueSetCode)))
            .andExpect(status().isOk());

        // Validate the ValueSetCode in the database
        List<ValueSetCode> valueSetCodeList = valueSetCodeRepository.findAll();
        assertThat(valueSetCodeList).hasSize(databaseSizeBeforeUpdate);
        ValueSetCode testValueSetCode = valueSetCodeList.get(valueSetCodeList.size() - 1);
        assertThat(testValueSetCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testValueSetCode.getDisplay()).isEqualTo(UPDATED_DISPLAY);
        assertThat(testValueSetCode.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testValueSetCode.getDefinition()).isEqualTo(UPDATED_DEFINITION);
        assertThat(testValueSetCode.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testValueSetCode.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testValueSetCode.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testValueSetCode.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);

        // Validate the ValueSetCode in Elasticsearch
        verify(mockValueSetCodeSearchRepository, times(1)).save(testValueSetCode);
    }

    @Test
    @Transactional
    public void updateNonExistingValueSetCode() throws Exception {
        int databaseSizeBeforeUpdate = valueSetCodeRepository.findAll().size();

        // Create the ValueSetCode

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValueSetCodeMockMvc.perform(put("/api/value-set-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSetCode)))
            .andExpect(status().isBadRequest());

        // Validate the ValueSetCode in the database
        List<ValueSetCode> valueSetCodeList = valueSetCodeRepository.findAll();
        assertThat(valueSetCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ValueSetCode in Elasticsearch
        verify(mockValueSetCodeSearchRepository, times(0)).save(valueSetCode);
    }

    @Test
    @Transactional
    public void deleteValueSetCode() throws Exception {
        // Initialize the database
        valueSetCodeService.save(valueSetCode);

        int databaseSizeBeforeDelete = valueSetCodeRepository.findAll().size();

        // Delete the valueSetCode
        restValueSetCodeMockMvc.perform(delete("/api/value-set-codes/{id}", valueSetCode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValueSetCode> valueSetCodeList = valueSetCodeRepository.findAll();
        assertThat(valueSetCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ValueSetCode in Elasticsearch
        verify(mockValueSetCodeSearchRepository, times(1)).deleteById(valueSetCode.getId());
    }

    @Test
    @Transactional
    public void searchValueSetCode() throws Exception {
        // Initialize the database
        valueSetCodeService.save(valueSetCode);
        when(mockValueSetCodeSearchRepository.search(queryStringQuery("id:" + valueSetCode.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(valueSetCode), PageRequest.of(0, 1), 1));
        // Search the valueSetCode
        restValueSetCodeMockMvc.perform(get("/api/_search/value-set-codes?query=id:" + valueSetCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueSetCode.getId().intValue())))
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
