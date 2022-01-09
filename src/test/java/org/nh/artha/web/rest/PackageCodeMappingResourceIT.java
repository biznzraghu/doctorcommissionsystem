package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.PackageCodeMapping;
import org.nh.artha.repository.PackageCodeMappingRepository;
import org.nh.artha.repository.search.PackageCodeMappingSearchRepository;
import org.nh.artha.service.PackageCodeMappingService;
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
 * Integration tests for the {@link PackageCodeMappingResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class PackageCodeMappingResourceIT {

    @Autowired
    private PackageCodeMappingRepository packageCodeMappingRepository;

    @Autowired
    private PackageCodeMappingService packageCodeMappingService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PackageCodeMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private PackageCodeMappingSearchRepository mockPackageCodeMappingSearchRepository;

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

    private MockMvc restPackageCodeMappingMockMvc;

    private PackageCodeMapping packageCodeMapping;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PackageCodeMappingResource packageCodeMappingResource = new PackageCodeMappingResource(packageCodeMappingService);
        this.restPackageCodeMappingMockMvc = MockMvcBuilders.standaloneSetup(packageCodeMappingResource)
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
    public static PackageCodeMapping createEntity(EntityManager em) {
        PackageCodeMapping packageCodeMapping = new PackageCodeMapping();
        return packageCodeMapping;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageCodeMapping createUpdatedEntity(EntityManager em) {
        PackageCodeMapping packageCodeMapping = new PackageCodeMapping();
        return packageCodeMapping;
    }

    @BeforeEach
    public void initTest() {
        packageCodeMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createPackageCodeMapping() throws Exception {
        int databaseSizeBeforeCreate = packageCodeMappingRepository.findAll().size();

        // Create the PackageCodeMapping
        restPackageCodeMappingMockMvc.perform(post("/api/package-code-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageCodeMapping)))
            .andExpect(status().isCreated());

        // Validate the PackageCodeMapping in the database
        List<PackageCodeMapping> packageCodeMappingList = packageCodeMappingRepository.findAll();
        assertThat(packageCodeMappingList).hasSize(databaseSizeBeforeCreate + 1);
        PackageCodeMapping testPackageCodeMapping = packageCodeMappingList.get(packageCodeMappingList.size() - 1);

        // Validate the PackageCodeMapping in Elasticsearch
        verify(mockPackageCodeMappingSearchRepository, times(1)).save(testPackageCodeMapping);
    }

    @Test
    @Transactional
    public void createPackageCodeMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packageCodeMappingRepository.findAll().size();

        // Create the PackageCodeMapping with an existing ID
        packageCodeMapping.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackageCodeMappingMockMvc.perform(post("/api/package-code-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageCodeMapping)))
            .andExpect(status().isBadRequest());

        // Validate the PackageCodeMapping in the database
        List<PackageCodeMapping> packageCodeMappingList = packageCodeMappingRepository.findAll();
        assertThat(packageCodeMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the PackageCodeMapping in Elasticsearch
        verify(mockPackageCodeMappingSearchRepository, times(0)).save(packageCodeMapping);
    }


    @Test
    @Transactional
    public void getAllPackageCodeMappings() throws Exception {
        // Initialize the database
        packageCodeMappingRepository.saveAndFlush(packageCodeMapping);

        // Get all the packageCodeMappingList
        restPackageCodeMappingMockMvc.perform(get("/api/package-code-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageCodeMapping.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getPackageCodeMapping() throws Exception {
        // Initialize the database
        packageCodeMappingRepository.saveAndFlush(packageCodeMapping);

        // Get the packageCodeMapping
        restPackageCodeMappingMockMvc.perform(get("/api/package-code-mappings/{id}", packageCodeMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(packageCodeMapping.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPackageCodeMapping() throws Exception {
        // Get the packageCodeMapping
        restPackageCodeMappingMockMvc.perform(get("/api/package-code-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackageCodeMapping() throws Exception {
        // Initialize the database
        packageCodeMappingService.save(packageCodeMapping);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPackageCodeMappingSearchRepository);

        int databaseSizeBeforeUpdate = packageCodeMappingRepository.findAll().size();

        // Update the packageCodeMapping
        PackageCodeMapping updatedPackageCodeMapping = packageCodeMappingRepository.findById(packageCodeMapping.getId()).get();
        // Disconnect from session so that the updates on updatedPackageCodeMapping are not directly saved in db
        em.detach(updatedPackageCodeMapping);

        restPackageCodeMappingMockMvc.perform(put("/api/package-code-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPackageCodeMapping)))
            .andExpect(status().isOk());

        // Validate the PackageCodeMapping in the database
        List<PackageCodeMapping> packageCodeMappingList = packageCodeMappingRepository.findAll();
        assertThat(packageCodeMappingList).hasSize(databaseSizeBeforeUpdate);
        PackageCodeMapping testPackageCodeMapping = packageCodeMappingList.get(packageCodeMappingList.size() - 1);

        // Validate the PackageCodeMapping in Elasticsearch
        verify(mockPackageCodeMappingSearchRepository, times(1)).save(testPackageCodeMapping);
    }

    @Test
    @Transactional
    public void updateNonExistingPackageCodeMapping() throws Exception {
        int databaseSizeBeforeUpdate = packageCodeMappingRepository.findAll().size();

        // Create the PackageCodeMapping

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageCodeMappingMockMvc.perform(put("/api/package-code-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageCodeMapping)))
            .andExpect(status().isBadRequest());

        // Validate the PackageCodeMapping in the database
        List<PackageCodeMapping> packageCodeMappingList = packageCodeMappingRepository.findAll();
        assertThat(packageCodeMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PackageCodeMapping in Elasticsearch
        verify(mockPackageCodeMappingSearchRepository, times(0)).save(packageCodeMapping);
    }

    @Test
    @Transactional
    public void deletePackageCodeMapping() throws Exception {
        // Initialize the database
        packageCodeMappingService.save(packageCodeMapping);

        int databaseSizeBeforeDelete = packageCodeMappingRepository.findAll().size();

        // Delete the packageCodeMapping
        restPackageCodeMappingMockMvc.perform(delete("/api/package-code-mappings/{id}", packageCodeMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PackageCodeMapping> packageCodeMappingList = packageCodeMappingRepository.findAll();
        assertThat(packageCodeMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PackageCodeMapping in Elasticsearch
        verify(mockPackageCodeMappingSearchRepository, times(1)).deleteById(packageCodeMapping.getId());
    }

    @Test
    @Transactional
    public void searchPackageCodeMapping() throws Exception {
        // Initialize the database
        packageCodeMappingService.save(packageCodeMapping);
        when(mockPackageCodeMappingSearchRepository.search(queryStringQuery("id:" + packageCodeMapping.getId())))
            .thenReturn(Collections.singletonList(packageCodeMapping));
        // Search the packageCodeMapping
        restPackageCodeMappingMockMvc.perform(get("/api/_search/package-code-mappings?query=id:" + packageCodeMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageCodeMapping.getId().intValue())));
    }
}
