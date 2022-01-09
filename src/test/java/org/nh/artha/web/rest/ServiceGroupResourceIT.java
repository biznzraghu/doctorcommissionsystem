package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ServiceGroup;
import org.nh.artha.repository.ServiceGroupRepository;
import org.nh.artha.repository.search.ServiceGroupSearchRepository;
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
 * Integration tests for the {@link ServiceGroupResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ServiceGroupResourceIT {

    @Autowired
    private ServiceGroupRepository serviceGroupRepository;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ServiceGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceGroupSearchRepository mockServiceGroupSearchRepository;

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

    private MockMvc restServiceGroupMockMvc;

    private ServiceGroup serviceGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceGroupResource serviceGroupResource = new ServiceGroupResource(serviceGroupRepository, mockServiceGroupSearchRepository);
        this.restServiceGroupMockMvc = MockMvcBuilders.standaloneSetup(serviceGroupResource)
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
    public static ServiceGroup createEntity(EntityManager em) {
        ServiceGroup serviceGroup = new ServiceGroup();
        return serviceGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceGroup createUpdatedEntity(EntityManager em) {
        ServiceGroup serviceGroup = new ServiceGroup();
        return serviceGroup;
    }

    @BeforeEach
    public void initTest() {
        serviceGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceGroup() throws Exception {
        int databaseSizeBeforeCreate = serviceGroupRepository.findAll().size();

        // Create the ServiceGroup
        restServiceGroupMockMvc.perform(post("/api/service-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceGroup)))
            .andExpect(status().isCreated());

        // Validate the ServiceGroup in the database
        List<ServiceGroup> serviceGroupList = serviceGroupRepository.findAll();
        assertThat(serviceGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceGroup testServiceGroup = serviceGroupList.get(serviceGroupList.size() - 1);

        // Validate the ServiceGroup in Elasticsearch
        verify(mockServiceGroupSearchRepository, times(1)).save(testServiceGroup);
    }

    @Test
    @Transactional
    public void createServiceGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceGroupRepository.findAll().size();

        // Create the ServiceGroup with an existing ID
        serviceGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceGroupMockMvc.perform(post("/api/service-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceGroup in the database
        List<ServiceGroup> serviceGroupList = serviceGroupRepository.findAll();
        assertThat(serviceGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceGroup in Elasticsearch
        verify(mockServiceGroupSearchRepository, times(0)).save(serviceGroup);
    }


    @Test
    @Transactional
    public void getAllServiceGroups() throws Exception {
        // Initialize the database
        serviceGroupRepository.saveAndFlush(serviceGroup);

        // Get all the serviceGroupList
        restServiceGroupMockMvc.perform(get("/api/service-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceGroup.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getServiceGroup() throws Exception {
        // Initialize the database
        serviceGroupRepository.saveAndFlush(serviceGroup);

        // Get the serviceGroup
        restServiceGroupMockMvc.perform(get("/api/service-groups/{id}", serviceGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceGroup.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceGroup() throws Exception {
        // Get the serviceGroup
        restServiceGroupMockMvc.perform(get("/api/service-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceGroup() throws Exception {
        // Initialize the database
        serviceGroupRepository.saveAndFlush(serviceGroup);

        int databaseSizeBeforeUpdate = serviceGroupRepository.findAll().size();

        // Update the serviceGroup
        ServiceGroup updatedServiceGroup = serviceGroupRepository.findById(serviceGroup.getId()).get();
        // Disconnect from session so that the updates on updatedServiceGroup are not directly saved in db
        em.detach(updatedServiceGroup);

        restServiceGroupMockMvc.perform(put("/api/service-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceGroup)))
            .andExpect(status().isOk());

        // Validate the ServiceGroup in the database
        List<ServiceGroup> serviceGroupList = serviceGroupRepository.findAll();
        assertThat(serviceGroupList).hasSize(databaseSizeBeforeUpdate);
        ServiceGroup testServiceGroup = serviceGroupList.get(serviceGroupList.size() - 1);

        // Validate the ServiceGroup in Elasticsearch
        verify(mockServiceGroupSearchRepository, times(1)).save(testServiceGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceGroup() throws Exception {
        int databaseSizeBeforeUpdate = serviceGroupRepository.findAll().size();

        // Create the ServiceGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceGroupMockMvc.perform(put("/api/service-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceGroup in the database
        List<ServiceGroup> serviceGroupList = serviceGroupRepository.findAll();
        assertThat(serviceGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceGroup in Elasticsearch
        verify(mockServiceGroupSearchRepository, times(0)).save(serviceGroup);
    }

    @Test
    @Transactional
    public void deleteServiceGroup() throws Exception {
        // Initialize the database
        serviceGroupRepository.saveAndFlush(serviceGroup);

        int databaseSizeBeforeDelete = serviceGroupRepository.findAll().size();

        // Delete the serviceGroup
        restServiceGroupMockMvc.perform(delete("/api/service-groups/{id}", serviceGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceGroup> serviceGroupList = serviceGroupRepository.findAll();
        assertThat(serviceGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceGroup in Elasticsearch
        verify(mockServiceGroupSearchRepository, times(1)).deleteById(serviceGroup.getId());
    }

    @Test
    @Transactional
    public void searchServiceGroup() throws Exception {
        // Initialize the database
        serviceGroupRepository.saveAndFlush(serviceGroup);
        when(mockServiceGroupSearchRepository.search(queryStringQuery("id:" + serviceGroup.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(serviceGroup), PageRequest.of(0, 1), 1));
        // Search the serviceGroup
        restServiceGroupMockMvc.perform(get("/api/_search/service-groups?query=id:" + serviceGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceGroup.getId().intValue())));
    }
}
