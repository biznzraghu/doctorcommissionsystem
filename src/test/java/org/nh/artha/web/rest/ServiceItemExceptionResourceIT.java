package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ServiceItemException;
import org.nh.artha.repository.ServiceItemExceptionRepository;
import org.nh.artha.repository.search.ServiceItemExceptionSearchRepository;
import org.nh.artha.service.ServiceItemExceptionService;
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

import org.nh.artha.domain.enumeration.Level;
import org.nh.artha.domain.enumeration.ExceptionType;
/**
 * Integration tests for the {@link ServiceItemExceptionResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ServiceItemExceptionResourceIT {

    private static final Level DEFAULT_LEVEL = Level.LineLevel;
    private static final Level UPDATED_LEVEL = Level.DocumentLevel;

    private static final ExceptionType DEFAULT_EXCEPTION_TYPE = ExceptionType.SponsorType;
    private static final ExceptionType UPDATED_EXCEPTION_TYPE = ExceptionType.Plan;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private ServiceItemExceptionRepository serviceItemExceptionRepository;

    @Autowired
    private ServiceItemExceptionService serviceItemExceptionService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ServiceItemExceptionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceItemExceptionSearchRepository mockServiceItemExceptionSearchRepository;

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

    private MockMvc restServiceItemExceptionMockMvc;

    private ServiceItemException serviceItemException;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceItemExceptionResource serviceItemExceptionResource = new ServiceItemExceptionResource(serviceItemExceptionService);
        this.restServiceItemExceptionMockMvc = MockMvcBuilders.standaloneSetup(serviceItemExceptionResource)
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
    public static ServiceItemException createEntity(EntityManager em) {
        ServiceItemException serviceItemException = new ServiceItemException()
            .level(DEFAULT_LEVEL)
            .exceptionType(DEFAULT_EXCEPTION_TYPE);
        return serviceItemException;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceItemException createUpdatedEntity(EntityManager em) {
        ServiceItemException serviceItemException = new ServiceItemException()
            .level(UPDATED_LEVEL)
            .exceptionType(UPDATED_EXCEPTION_TYPE);
        return serviceItemException;
    }

    @BeforeEach
    public void initTest() {
        serviceItemException = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceItemException() throws Exception {
        int databaseSizeBeforeCreate = serviceItemExceptionRepository.findAll().size();

        // Create the ServiceItemException
        restServiceItemExceptionMockMvc.perform(post("/api/service-item-exceptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemException)))
            .andExpect(status().isCreated());

        // Validate the ServiceItemException in the database
        List<ServiceItemException> serviceItemExceptionList = serviceItemExceptionRepository.findAll();
        assertThat(serviceItemExceptionList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceItemException testServiceItemException = serviceItemExceptionList.get(serviceItemExceptionList.size() - 1);
        assertThat(testServiceItemException.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testServiceItemException.getExceptionType()).isEqualTo(DEFAULT_EXCEPTION_TYPE);
        assertThat(testServiceItemException.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the ServiceItemException in Elasticsearch
        verify(mockServiceItemExceptionSearchRepository, times(1)).save(testServiceItemException);
    }

    @Test
    @Transactional
    public void createServiceItemExceptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceItemExceptionRepository.findAll().size();

        // Create the ServiceItemException with an existing ID
        serviceItemException.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceItemExceptionMockMvc.perform(post("/api/service-item-exceptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemException)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceItemException in the database
        List<ServiceItemException> serviceItemExceptionList = serviceItemExceptionRepository.findAll();
        assertThat(serviceItemExceptionList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceItemException in Elasticsearch
        verify(mockServiceItemExceptionSearchRepository, times(0)).save(serviceItemException);
    }


    @Test
    @Transactional
    public void getAllServiceItemExceptions() throws Exception {
        // Initialize the database
        serviceItemExceptionRepository.saveAndFlush(serviceItemException);

        // Get all the serviceItemExceptionList
        restServiceItemExceptionMockMvc.perform(get("/api/service-item-exceptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceItemException.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].exceptionType").value(hasItem(DEFAULT_EXCEPTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    public void getServiceItemException() throws Exception {
        // Initialize the database
        serviceItemExceptionRepository.saveAndFlush(serviceItemException);

        // Get the serviceItemException
        restServiceItemExceptionMockMvc.perform(get("/api/service-item-exceptions/{id}", serviceItemException.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceItemException.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.exceptionType").value(DEFAULT_EXCEPTION_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    public void getNonExistingServiceItemException() throws Exception {
        // Get the serviceItemException
        restServiceItemExceptionMockMvc.perform(get("/api/service-item-exceptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceItemException() throws Exception {
        // Initialize the database
        serviceItemExceptionService.save(serviceItemException);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockServiceItemExceptionSearchRepository);

        int databaseSizeBeforeUpdate = serviceItemExceptionRepository.findAll().size();

        // Update the serviceItemException
        ServiceItemException updatedServiceItemException = serviceItemExceptionRepository.findById(serviceItemException.getId()).get();
        // Disconnect from session so that the updates on updatedServiceItemException are not directly saved in db
        em.detach(updatedServiceItemException);
        updatedServiceItemException
            .level(UPDATED_LEVEL)
            .exceptionType(UPDATED_EXCEPTION_TYPE);

        restServiceItemExceptionMockMvc.perform(put("/api/service-item-exceptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceItemException)))
            .andExpect(status().isOk());

        // Validate the ServiceItemException in the database
        List<ServiceItemException> serviceItemExceptionList = serviceItemExceptionRepository.findAll();
        assertThat(serviceItemExceptionList).hasSize(databaseSizeBeforeUpdate);
        ServiceItemException testServiceItemException = serviceItemExceptionList.get(serviceItemExceptionList.size() - 1);
        assertThat(testServiceItemException.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testServiceItemException.getExceptionType()).isEqualTo(UPDATED_EXCEPTION_TYPE);
        assertThat(testServiceItemException.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the ServiceItemException in Elasticsearch
        verify(mockServiceItemExceptionSearchRepository, times(1)).save(testServiceItemException);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceItemException() throws Exception {
        int databaseSizeBeforeUpdate = serviceItemExceptionRepository.findAll().size();

        // Create the ServiceItemException

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceItemExceptionMockMvc.perform(put("/api/service-item-exceptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemException)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceItemException in the database
        List<ServiceItemException> serviceItemExceptionList = serviceItemExceptionRepository.findAll();
        assertThat(serviceItemExceptionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceItemException in Elasticsearch
        verify(mockServiceItemExceptionSearchRepository, times(0)).save(serviceItemException);
    }

    @Test
    @Transactional
    public void deleteServiceItemException() throws Exception {
        // Initialize the database
        serviceItemExceptionService.save(serviceItemException);

        int databaseSizeBeforeDelete = serviceItemExceptionRepository.findAll().size();

        // Delete the serviceItemException
        restServiceItemExceptionMockMvc.perform(delete("/api/service-item-exceptions/{id}", serviceItemException.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceItemException> serviceItemExceptionList = serviceItemExceptionRepository.findAll();
        assertThat(serviceItemExceptionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceItemException in Elasticsearch
        verify(mockServiceItemExceptionSearchRepository, times(1)).deleteById(serviceItemException.getId());
    }

    @Test
    @Transactional
    public void searchServiceItemException() throws Exception {
        // Initialize the database
        serviceItemExceptionService.save(serviceItemException);
        when(mockServiceItemExceptionSearchRepository.search(queryStringQuery("id:" + serviceItemException.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(serviceItemException), PageRequest.of(0, 1), 1));
        // Search the serviceItemException
        restServiceItemExceptionMockMvc.perform(get("/api/_search/service-item-exceptions?query=id:" + serviceItemException.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceItemException.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].exceptionType").value(hasItem(DEFAULT_EXCEPTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
