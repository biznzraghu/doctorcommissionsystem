package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ValueSetCodeMapping;
import org.nh.artha.repository.ValueSetCodeMappingRepository;
import org.nh.artha.repository.search.ValueSetCodeMappingSearchRepository;
import org.nh.artha.service.ValueSetCodeMappingService;
import org.nh.artha.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Integration tests for the {@link ValueSetCodeMappingResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ValueSetCodeMappingResourceIT {

    @Autowired
    private ValueSetCodeMappingRepository valueSetCodeMappingRepository;

    @Autowired
    private ValueSetCodeMappingService valueSetCodeMappingService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ValueSetCodeMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private ValueSetCodeMappingSearchRepository mockValueSetCodeMappingSearchRepository;

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

    private MockMvc restValueSetCodeMappingMockMvc;

    private ValueSetCodeMapping valueSetCodeMapping;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ValueSetCodeMappingResource valueSetCodeMappingResource = new ValueSetCodeMappingResource(valueSetCodeMappingService);
        this.restValueSetCodeMappingMockMvc = MockMvcBuilders.standaloneSetup(valueSetCodeMappingResource)
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
    public static ValueSetCodeMapping createEntity(EntityManager em) {
        ValueSetCodeMapping valueSetCodeMapping = new ValueSetCodeMapping();
        return valueSetCodeMapping;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValueSetCodeMapping createUpdatedEntity(EntityManager em) {
        ValueSetCodeMapping valueSetCodeMapping = new ValueSetCodeMapping();
        return valueSetCodeMapping;
    }

    @BeforeEach
    public void initTest() {
        valueSetCodeMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createValueSetCodeMapping() throws Exception {
        int databaseSizeBeforeCreate = valueSetCodeMappingRepository.findAll().size();

        // Create the ValueSetCodeMapping
        restValueSetCodeMappingMockMvc.perform(post("/api/value-set-code-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSetCodeMapping)))
            .andExpect(status().isCreated());

        // Validate the ValueSetCodeMapping in the database
        List<ValueSetCodeMapping> valueSetCodeMappingList = valueSetCodeMappingRepository.findAll();
        assertThat(valueSetCodeMappingList).hasSize(databaseSizeBeforeCreate + 1);
        ValueSetCodeMapping testValueSetCodeMapping = valueSetCodeMappingList.get(valueSetCodeMappingList.size() - 1);

        // Validate the ValueSetCodeMapping in Elasticsearch
        verify(mockValueSetCodeMappingSearchRepository, times(1)).save(testValueSetCodeMapping);
    }

    @Test
    @Transactional
    public void createValueSetCodeMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = valueSetCodeMappingRepository.findAll().size();

        // Create the ValueSetCodeMapping with an existing ID
        valueSetCodeMapping.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValueSetCodeMappingMockMvc.perform(post("/api/value-set-code-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSetCodeMapping)))
            .andExpect(status().isBadRequest());

        // Validate the ValueSetCodeMapping in the database
        List<ValueSetCodeMapping> valueSetCodeMappingList = valueSetCodeMappingRepository.findAll();
        assertThat(valueSetCodeMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the ValueSetCodeMapping in Elasticsearch
        verify(mockValueSetCodeMappingSearchRepository, times(0)).save(valueSetCodeMapping);
    }


    @Test
    @Transactional
    public void getAllValueSetCodeMappings() throws Exception {
        // Initialize the database
        valueSetCodeMappingRepository.saveAndFlush(valueSetCodeMapping);

        // Get all the valueSetCodeMappingList
        restValueSetCodeMappingMockMvc.perform(get("/api/value-set-code-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueSetCodeMapping.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getValueSetCodeMapping() throws Exception {
        // Initialize the database
        valueSetCodeMappingRepository.saveAndFlush(valueSetCodeMapping);

        // Get the valueSetCodeMapping
        restValueSetCodeMappingMockMvc.perform(get("/api/value-set-code-mappings/{id}", valueSetCodeMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(valueSetCodeMapping.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingValueSetCodeMapping() throws Exception {
        // Get the valueSetCodeMapping
        restValueSetCodeMappingMockMvc.perform(get("/api/value-set-code-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValueSetCodeMapping() throws Exception {
        // Initialize the database
        valueSetCodeMappingService.save(valueSetCodeMapping);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockValueSetCodeMappingSearchRepository);

        int databaseSizeBeforeUpdate = valueSetCodeMappingRepository.findAll().size();

        // Update the valueSetCodeMapping
        ValueSetCodeMapping updatedValueSetCodeMapping = valueSetCodeMappingRepository.findById(valueSetCodeMapping.getId()).get();
        // Disconnect from session so that the updates on updatedValueSetCodeMapping are not directly saved in db
        em.detach(updatedValueSetCodeMapping);

        restValueSetCodeMappingMockMvc.perform(put("/api/value-set-code-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedValueSetCodeMapping)))
            .andExpect(status().isOk());

        // Validate the ValueSetCodeMapping in the database
        List<ValueSetCodeMapping> valueSetCodeMappingList = valueSetCodeMappingRepository.findAll();
        assertThat(valueSetCodeMappingList).hasSize(databaseSizeBeforeUpdate);
        ValueSetCodeMapping testValueSetCodeMapping = valueSetCodeMappingList.get(valueSetCodeMappingList.size() - 1);

        // Validate the ValueSetCodeMapping in Elasticsearch
        verify(mockValueSetCodeMappingSearchRepository, times(1)).save(testValueSetCodeMapping);
    }

    @Test
    @Transactional
    public void updateNonExistingValueSetCodeMapping() throws Exception {
        int databaseSizeBeforeUpdate = valueSetCodeMappingRepository.findAll().size();

        // Create the ValueSetCodeMapping

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValueSetCodeMappingMockMvc.perform(put("/api/value-set-code-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valueSetCodeMapping)))
            .andExpect(status().isBadRequest());

        // Validate the ValueSetCodeMapping in the database
        List<ValueSetCodeMapping> valueSetCodeMappingList = valueSetCodeMappingRepository.findAll();
        assertThat(valueSetCodeMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ValueSetCodeMapping in Elasticsearch
        verify(mockValueSetCodeMappingSearchRepository, times(0)).save(valueSetCodeMapping);
    }

    @Test
    @Transactional
    public void deleteValueSetCodeMapping() throws Exception {
        // Initialize the database
        valueSetCodeMappingService.save(valueSetCodeMapping);

        int databaseSizeBeforeDelete = valueSetCodeMappingRepository.findAll().size();

        // Delete the valueSetCodeMapping
        restValueSetCodeMappingMockMvc.perform(delete("/api/value-set-code-mappings/{id}", valueSetCodeMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValueSetCodeMapping> valueSetCodeMappingList = valueSetCodeMappingRepository.findAll();
        assertThat(valueSetCodeMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ValueSetCodeMapping in Elasticsearch
        verify(mockValueSetCodeMappingSearchRepository, times(1)).deleteById(valueSetCodeMapping.getId());
    }

    @Test
    @Transactional
    public void searchValueSetCodeMapping() throws Exception {
        // Initialize the database
        valueSetCodeMappingService.save(valueSetCodeMapping);
        when(mockValueSetCodeMappingSearchRepository.search(queryStringQuery("id:" + valueSetCodeMapping.getId())))
            .thenReturn(Collections.singletonList(valueSetCodeMapping));
        // Search the valueSetCodeMapping
        restValueSetCodeMappingMockMvc.perform(get("/api/_search/value-set-code-mappings?query=id:" + valueSetCodeMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valueSetCodeMapping.getId().intValue())));
    }
}
