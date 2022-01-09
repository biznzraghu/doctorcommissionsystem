package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Feature;
import org.nh.artha.repository.FeatureRepository;
import org.nh.artha.repository.search.FeatureSearchRepository;
import org.nh.artha.service.FeatureService;
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

import org.nh.artha.domain.enumeration.FeatureType;
/**
 * Integration tests for the {@link FeatureResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class FeatureResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;

    private static final FeatureType DEFAULT_TYPE = FeatureType.MASTER;
    private static final FeatureType UPDATED_TYPE = FeatureType.TRANSACTION;

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private FeatureService featureService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.FeatureSearchRepositoryMockConfiguration
     */
    @Autowired
    private FeatureSearchRepository mockFeatureSearchRepository;

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

    private MockMvc restFeatureMockMvc;

    private Feature feature;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FeatureResource featureResource = new FeatureResource(featureService,featureRepository,mockFeatureSearchRepository,applicationProperties);
        this.restFeatureMockMvc = MockMvcBuilders.standaloneSetup(featureResource)
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
    public static Feature createEntity(EntityManager em) {
        Feature feature = new Feature()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .displayOrder(DEFAULT_DISPLAY_ORDER)
            .type(DEFAULT_TYPE);
        return feature;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feature createUpdatedEntity(EntityManager em) {
        Feature feature = new Feature()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .type(UPDATED_TYPE);
        return feature;
    }

    @BeforeEach
    public void initTest() {
        feature = createEntity(em);
    }

    @Test
    @Transactional
    public void createFeature() throws Exception {
        int databaseSizeBeforeCreate = featureRepository.findAll().size();

        // Create the Feature
        restFeatureMockMvc.perform(post("/api/features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feature)))
            .andExpect(status().isCreated());

        // Validate the Feature in the database
        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeCreate + 1);
        Feature testFeature = featureList.get(featureList.size() - 1);
        assertThat(testFeature.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFeature.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFeature.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testFeature.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
        assertThat(testFeature.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Feature in Elasticsearch
        verify(mockFeatureSearchRepository, times(1)).save(testFeature);
    }

    @Test
    @Transactional
    public void createFeatureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = featureRepository.findAll().size();

        // Create the Feature with an existing ID
        feature.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeatureMockMvc.perform(post("/api/features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feature)))
            .andExpect(status().isBadRequest());

        // Validate the Feature in the database
        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeCreate);

        // Validate the Feature in Elasticsearch
        verify(mockFeatureSearchRepository, times(0)).save(feature);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = featureRepository.findAll().size();
        // set the field null
        feature.setCode(null);

        // Create the Feature, which fails.

        restFeatureMockMvc.perform(post("/api/features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feature)))
            .andExpect(status().isBadRequest());

        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = featureRepository.findAll().size();
        // set the field null
        feature.setName(null);

        // Create the Feature, which fails.

        restFeatureMockMvc.perform(post("/api/features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feature)))
            .andExpect(status().isBadRequest());

        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = featureRepository.findAll().size();
        // set the field null
        feature.setActive(null);

        // Create the Feature, which fails.

        restFeatureMockMvc.perform(post("/api/features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feature)))
            .andExpect(status().isBadRequest());

        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = featureRepository.findAll().size();
        // set the field null
        feature.setType(null);

        // Create the Feature, which fails.

        restFeatureMockMvc.perform(post("/api/features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feature)))
            .andExpect(status().isBadRequest());

        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFeatures() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);

        // Get all the featureList
        restFeatureMockMvc.perform(get("/api/features?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feature.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getFeature() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);

        // Get the feature
        restFeatureMockMvc.perform(get("/api/features/{id}", feature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(feature.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFeature() throws Exception {
        // Get the feature
        restFeatureMockMvc.perform(get("/api/features/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeature() throws Exception {
        // Initialize the database
        featureService.save(feature);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFeatureSearchRepository);

        int databaseSizeBeforeUpdate = featureRepository.findAll().size();

        // Update the feature
        Feature updatedFeature = featureRepository.findById(feature.getId()).get();
        // Disconnect from session so that the updates on updatedFeature are not directly saved in db
        em.detach(updatedFeature);
        updatedFeature
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .type(UPDATED_TYPE);

        restFeatureMockMvc.perform(put("/api/features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFeature)))
            .andExpect(status().isOk());

        // Validate the Feature in the database
        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeUpdate);
        Feature testFeature = featureList.get(featureList.size() - 1);
        assertThat(testFeature.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFeature.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFeature.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testFeature.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testFeature.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Feature in Elasticsearch
        verify(mockFeatureSearchRepository, times(1)).save(testFeature);
    }

    @Test
    @Transactional
    public void updateNonExistingFeature() throws Exception {
        int databaseSizeBeforeUpdate = featureRepository.findAll().size();

        // Create the Feature

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeatureMockMvc.perform(put("/api/features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feature)))
            .andExpect(status().isBadRequest());

        // Validate the Feature in the database
        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Feature in Elasticsearch
        verify(mockFeatureSearchRepository, times(0)).save(feature);
    }

    @Test
    @Transactional
    public void deleteFeature() throws Exception {
        // Initialize the database
        featureService.save(feature);

        int databaseSizeBeforeDelete = featureRepository.findAll().size();

        // Delete the feature
        restFeatureMockMvc.perform(delete("/api/features/{id}", feature.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Feature> featureList = featureRepository.findAll();
        assertThat(featureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Feature in Elasticsearch
        verify(mockFeatureSearchRepository, times(1)).deleteById(feature.getId());
    }

    @Test
    @Transactional
    public void searchFeature() throws Exception {
        // Initialize the database
        featureService.save(feature);
        when(mockFeatureSearchRepository.search(queryStringQuery("id:" + feature.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(feature), PageRequest.of(0, 1), 1));
        // Search the feature
        restFeatureMockMvc.perform(get("/api/_search/features?query=id:" + feature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feature.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
}
