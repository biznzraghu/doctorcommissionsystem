package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ServiceCategory;
import org.nh.artha.repository.ServiceCategoryRepository;
import org.nh.artha.repository.search.ServiceCategorySearchRepository;
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
 * Integration tests for the {@link ServiceCategoryResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ServiceCategoryResourceIT {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ServiceCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceCategorySearchRepository mockServiceCategorySearchRepository;

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

    private MockMvc restServiceCategoryMockMvc;

    private ServiceCategory serviceCategory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceCategoryResource serviceCategoryResource = new ServiceCategoryResource(serviceCategoryRepository, mockServiceCategorySearchRepository);
        this.restServiceCategoryMockMvc = MockMvcBuilders.standaloneSetup(serviceCategoryResource)
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
    public static ServiceCategory createEntity(EntityManager em) {
        ServiceCategory serviceCategory = new ServiceCategory();
        return serviceCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceCategory createUpdatedEntity(EntityManager em) {
        ServiceCategory serviceCategory = new ServiceCategory();
        return serviceCategory;
    }

    @BeforeEach
    public void initTest() {
        serviceCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceCategory() throws Exception {
        int databaseSizeBeforeCreate = serviceCategoryRepository.findAll().size();

        // Create the ServiceCategory
        restServiceCategoryMockMvc.perform(post("/api/service-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceCategory)))
            .andExpect(status().isCreated());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);

        // Validate the ServiceCategory in Elasticsearch
        verify(mockServiceCategorySearchRepository, times(1)).save(testServiceCategory);
    }

    @Test
    @Transactional
    public void createServiceCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceCategoryRepository.findAll().size();

        // Create the ServiceCategory with an existing ID
        serviceCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceCategoryMockMvc.perform(post("/api/service-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceCategory in Elasticsearch
        verify(mockServiceCategorySearchRepository, times(0)).save(serviceCategory);
    }


    @Test
    @Transactional
    public void getAllServiceCategories() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        // Get all the serviceCategoryList
        restServiceCategoryMockMvc.perform(get("/api/service-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCategory.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        // Get the serviceCategory
        restServiceCategoryMockMvc.perform(get("/api/service-categories/{id}", serviceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceCategory.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceCategory() throws Exception {
        // Get the serviceCategory
        restServiceCategoryMockMvc.perform(get("/api/service-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Update the serviceCategory
        ServiceCategory updatedServiceCategory = serviceCategoryRepository.findById(serviceCategory.getId()).get();
        // Disconnect from session so that the updates on updatedServiceCategory are not directly saved in db
        em.detach(updatedServiceCategory);

        restServiceCategoryMockMvc.perform(put("/api/service-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceCategory)))
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);

        // Validate the ServiceCategory in Elasticsearch
        verify(mockServiceCategorySearchRepository, times(1)).save(testServiceCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Create the ServiceCategory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc.perform(put("/api/service-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceCategory in Elasticsearch
        verify(mockServiceCategorySearchRepository, times(0)).save(serviceCategory);
    }

    @Test
    @Transactional
    public void deleteServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        int databaseSizeBeforeDelete = serviceCategoryRepository.findAll().size();

        // Delete the serviceCategory
        restServiceCategoryMockMvc.perform(delete("/api/service-categories/{id}", serviceCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceCategory in Elasticsearch
        verify(mockServiceCategorySearchRepository, times(1)).deleteById(serviceCategory.getId());
    }

    @Test
    @Transactional
    public void searchServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);
        when(mockServiceCategorySearchRepository.search(queryStringQuery("id:" + serviceCategory.getId())))
            .thenReturn(Collections.singletonList(serviceCategory));
        // Search the serviceCategory
        restServiceCategoryMockMvc.perform(get("/api/_search/service-categories?query=id:" + serviceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCategory.getId().intValue())));
    }
}
