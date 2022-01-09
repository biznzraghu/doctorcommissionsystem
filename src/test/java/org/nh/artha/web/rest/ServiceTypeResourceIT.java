package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ServiceType;
import org.nh.artha.repository.ServiceTypeRepository;
import org.nh.artha.repository.search.ServiceTypeSearchRepository;
import org.nh.artha.service.CommonValueSetCodeService;
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
 * Integration tests for the {@link ServiceTypeResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ServiceTypeResourceIT {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ServiceTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceTypeSearchRepository mockServiceTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private CommonValueSetCodeService commonValueSetCodeService;

    @Autowired
    private Validator validator;

    private MockMvc restServiceTypeMockMvc;

    private ServiceType serviceType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceTypeResource serviceTypeResource = new ServiceTypeResource(commonValueSetCodeService);
        this.restServiceTypeMockMvc = MockMvcBuilders.standaloneSetup(serviceTypeResource)
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
    public static ServiceType createEntity(EntityManager em) {
        ServiceType serviceType = new ServiceType();
        return serviceType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceType createUpdatedEntity(EntityManager em) {
        ServiceType serviceType = new ServiceType();
        return serviceType;
    }

    @BeforeEach
    public void initTest() {
        serviceType = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceType() throws Exception {
        int databaseSizeBeforeCreate = serviceTypeRepository.findAll().size();

        // Create the ServiceType
        restServiceTypeMockMvc.perform(post("/api/service-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceType)))
            .andExpect(status().isCreated());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceType testServiceType = serviceTypeList.get(serviceTypeList.size() - 1);

        // Validate the ServiceType in Elasticsearch
        verify(mockServiceTypeSearchRepository, times(1)).save(testServiceType);
    }

    @Test
    @Transactional
    public void createServiceTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceTypeRepository.findAll().size();

        // Create the ServiceType with an existing ID
        serviceType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceTypeMockMvc.perform(post("/api/service-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceType)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceType in Elasticsearch
        verify(mockServiceTypeSearchRepository, times(0)).save(serviceType);
    }


    @Test
    @Transactional
    public void getAllServiceTypes() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);

        // Get all the serviceTypeList
        restServiceTypeMockMvc.perform(get("/api/service-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceType.getId().intValue())));
    }

    @Test
    @Transactional
    public void getServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);

        // Get the serviceType
        restServiceTypeMockMvc.perform(get("/api/service-types/{id}", serviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceType.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceType() throws Exception {
        // Get the serviceType
        restServiceTypeMockMvc.perform(get("/api/service-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);

        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().size();

        // Update the serviceType
        ServiceType updatedServiceType = serviceTypeRepository.findById(serviceType.getId()).get();
        // Disconnect from session so that the updates on updatedServiceType are not directly saved in db
        em.detach(updatedServiceType);

        restServiceTypeMockMvc.perform(put("/api/service-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceType)))
            .andExpect(status().isOk());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);
        ServiceType testServiceType = serviceTypeList.get(serviceTypeList.size() - 1);

        // Validate the ServiceType in Elasticsearch
        verify(mockServiceTypeSearchRepository, times(1)).save(testServiceType);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceType() throws Exception {
        int databaseSizeBeforeUpdate = serviceTypeRepository.findAll().size();

        // Create the ServiceType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceTypeMockMvc.perform(put("/api/service-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceType)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceType in the database
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceType in Elasticsearch
        verify(mockServiceTypeSearchRepository, times(0)).save(serviceType);
    }

    @Test
    @Transactional
    public void deleteServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);

        int databaseSizeBeforeDelete = serviceTypeRepository.findAll().size();

        // Delete the serviceType
        restServiceTypeMockMvc.perform(delete("/api/service-types/{id}", serviceType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        assertThat(serviceTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceType in Elasticsearch
        verify(mockServiceTypeSearchRepository, times(1)).deleteById(serviceType.getId());
    }

    @Test
    @Transactional
    public void searchServiceType() throws Exception {
        // Initialize the database
        serviceTypeRepository.saveAndFlush(serviceType);
        when(mockServiceTypeSearchRepository.search(queryStringQuery("id:" + serviceType.getId())))
            .thenReturn(Collections.singletonList(serviceType));
        // Search the serviceType
        restServiceTypeMockMvc.perform(get("/api/_search/service-types?query=id:" + serviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceType.getId().intValue())));
    }
}
