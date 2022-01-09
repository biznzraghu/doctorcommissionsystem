package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.ServiceItemBenefitTemplate;
import org.nh.artha.repository.ServiceItemBenefitTemplateRepository;
import org.nh.artha.repository.search.ServiceItemBenefitTemplateSearchRepository;
import org.nh.artha.service.ServiceItemBenefitTemplateService;
import org.nh.artha.service.impl.ArthaSequenceGeneratorService;
import org.nh.artha.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Integration tests for the {@link ServiceItemBenefitTemplateResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ServiceItemBenefitTemplateResourceIT {

    private static final String DEFAULT_TEMPLATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_NAME = "BBBBBBBBBB";

    @Autowired
    private ServiceItemBenefitTemplateRepository serviceItemBenefitTemplateRepository;

    @Mock
    private ServiceItemBenefitTemplateRepository serviceItemBenefitTemplateRepositoryMock;

    @Mock
    private ServiceItemBenefitTemplateService serviceItemBenefitTemplateServiceMock;

    @Autowired
    private ServiceItemBenefitTemplateService serviceItemBenefitTemplateService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ServiceItemBenefitTemplateSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceItemBenefitTemplateSearchRepository mockServiceItemBenefitTemplateSearchRepository;

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

    private MockMvc restServiceItemBenefitTemplateMockMvc;

    private ServiceItemBenefitTemplate serviceItemBenefitTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private  ArthaSequenceGeneratorService sequenceGeneratorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceItemBenefitTemplateResource serviceItemBenefitTemplateResource = new ServiceItemBenefitTemplateResource(serviceItemBenefitTemplateService,serviceItemBenefitTemplateRepository,mockServiceItemBenefitTemplateSearchRepository,applicationProperties,sequenceGeneratorService);
        this.restServiceItemBenefitTemplateMockMvc = MockMvcBuilders.standaloneSetup(serviceItemBenefitTemplateResource)
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
    public static ServiceItemBenefitTemplate createEntity(EntityManager em) {
        ServiceItemBenefitTemplate serviceItemBenefitTemplate = new ServiceItemBenefitTemplate()
            .templateName(DEFAULT_TEMPLATE_NAME);
        return serviceItemBenefitTemplate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceItemBenefitTemplate createUpdatedEntity(EntityManager em) {
        ServiceItemBenefitTemplate serviceItemBenefitTemplate = new ServiceItemBenefitTemplate()
            .templateName(UPDATED_TEMPLATE_NAME);
        return serviceItemBenefitTemplate;
    }

    @BeforeEach
    public void initTest() {
        serviceItemBenefitTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceItemBenefitTemplate() throws Exception {
        int databaseSizeBeforeCreate = serviceItemBenefitTemplateRepository.findAll().size();

        // Create the ServiceItemBenefitTemplate
        restServiceItemBenefitTemplateMockMvc.perform(post("/api/service-item-benefit-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemBenefitTemplate)))
            .andExpect(status().isCreated());

        // Validate the ServiceItemBenefitTemplate in the database
        List<ServiceItemBenefitTemplate> serviceItemBenefitTemplateList = serviceItemBenefitTemplateRepository.findAll();
        assertThat(serviceItemBenefitTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceItemBenefitTemplate testServiceItemBenefitTemplate = serviceItemBenefitTemplateList.get(serviceItemBenefitTemplateList.size() - 1);
        assertThat(testServiceItemBenefitTemplate.getTemplateName()).isEqualTo(DEFAULT_TEMPLATE_NAME);

        // Validate the ServiceItemBenefitTemplate in Elasticsearch
        verify(mockServiceItemBenefitTemplateSearchRepository, times(1)).save(testServiceItemBenefitTemplate);
    }

    @Test
    @Transactional
    public void createServiceItemBenefitTemplateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceItemBenefitTemplateRepository.findAll().size();

        // Create the ServiceItemBenefitTemplate with an existing ID
        serviceItemBenefitTemplate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceItemBenefitTemplateMockMvc.perform(post("/api/service-item-benefit-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemBenefitTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceItemBenefitTemplate in the database
        List<ServiceItemBenefitTemplate> serviceItemBenefitTemplateList = serviceItemBenefitTemplateRepository.findAll();
        assertThat(serviceItemBenefitTemplateList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceItemBenefitTemplate in Elasticsearch
        verify(mockServiceItemBenefitTemplateSearchRepository, times(0)).save(serviceItemBenefitTemplate);
    }


    @Test
    @Transactional
    public void getAllServiceItemBenefitTemplates() throws Exception {
        // Initialize the database
        serviceItemBenefitTemplateRepository.saveAndFlush(serviceItemBenefitTemplate);

        // Get all the serviceItemBenefitTemplateList
        restServiceItemBenefitTemplateMockMvc.perform(get("/api/service-item-benefit-templates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceItemBenefitTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME)));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllServiceItemBenefitTemplatesWithEagerRelationshipsIsEnabled() throws Exception {
        ServiceItemBenefitTemplateResource serviceItemBenefitTemplateResource = new ServiceItemBenefitTemplateResource(serviceItemBenefitTemplateService,serviceItemBenefitTemplateRepository,mockServiceItemBenefitTemplateSearchRepository,applicationProperties,sequenceGeneratorService);
        when(serviceItemBenefitTemplateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restServiceItemBenefitTemplateMockMvc = MockMvcBuilders.standaloneSetup(serviceItemBenefitTemplateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restServiceItemBenefitTemplateMockMvc.perform(get("/api/service-item-benefit-templates?eagerload=true"))
        .andExpect(status().isOk());

        verify(serviceItemBenefitTemplateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllServiceItemBenefitTemplatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        ServiceItemBenefitTemplateResource serviceItemBenefitTemplateResource = new ServiceItemBenefitTemplateResource(serviceItemBenefitTemplateService,serviceItemBenefitTemplateRepository,mockServiceItemBenefitTemplateSearchRepository,applicationProperties,sequenceGeneratorService);
            when(serviceItemBenefitTemplateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restServiceItemBenefitTemplateMockMvc = MockMvcBuilders.standaloneSetup(serviceItemBenefitTemplateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restServiceItemBenefitTemplateMockMvc.perform(get("/api/service-item-benefit-templates?eagerload=true"))
        .andExpect(status().isOk());

            verify(serviceItemBenefitTemplateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getServiceItemBenefitTemplate() throws Exception {
        // Initialize the database
        serviceItemBenefitTemplateRepository.saveAndFlush(serviceItemBenefitTemplate);

        // Get the serviceItemBenefitTemplate
        restServiceItemBenefitTemplateMockMvc.perform(get("/api/service-item-benefit-templates/{id}", serviceItemBenefitTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceItemBenefitTemplate.getId().intValue()))
            .andExpect(jsonPath("$.templateName").value(DEFAULT_TEMPLATE_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingServiceItemBenefitTemplate() throws Exception {
        // Get the serviceItemBenefitTemplate
        restServiceItemBenefitTemplateMockMvc.perform(get("/api/service-item-benefit-templates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceItemBenefitTemplate() throws Exception {
        // Initialize the database
        serviceItemBenefitTemplateService.save(serviceItemBenefitTemplate);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockServiceItemBenefitTemplateSearchRepository);

        int databaseSizeBeforeUpdate = serviceItemBenefitTemplateRepository.findAll().size();

        // Update the serviceItemBenefitTemplate
        ServiceItemBenefitTemplate updatedServiceItemBenefitTemplate = serviceItemBenefitTemplateRepository.findById(serviceItemBenefitTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedServiceItemBenefitTemplate are not directly saved in db
        em.detach(updatedServiceItemBenefitTemplate);
        updatedServiceItemBenefitTemplate
            .templateName(UPDATED_TEMPLATE_NAME);

        restServiceItemBenefitTemplateMockMvc.perform(put("/api/service-item-benefit-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceItemBenefitTemplate)))
            .andExpect(status().isOk());

        // Validate the ServiceItemBenefitTemplate in the database
        List<ServiceItemBenefitTemplate> serviceItemBenefitTemplateList = serviceItemBenefitTemplateRepository.findAll();
        assertThat(serviceItemBenefitTemplateList).hasSize(databaseSizeBeforeUpdate);
        ServiceItemBenefitTemplate testServiceItemBenefitTemplate = serviceItemBenefitTemplateList.get(serviceItemBenefitTemplateList.size() - 1);
        assertThat(testServiceItemBenefitTemplate.getTemplateName()).isEqualTo(UPDATED_TEMPLATE_NAME);

        // Validate the ServiceItemBenefitTemplate in Elasticsearch
        verify(mockServiceItemBenefitTemplateSearchRepository, times(1)).save(testServiceItemBenefitTemplate);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceItemBenefitTemplate() throws Exception {
        int databaseSizeBeforeUpdate = serviceItemBenefitTemplateRepository.findAll().size();

        // Create the ServiceItemBenefitTemplate

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceItemBenefitTemplateMockMvc.perform(put("/api/service-item-benefit-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemBenefitTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceItemBenefitTemplate in the database
        List<ServiceItemBenefitTemplate> serviceItemBenefitTemplateList = serviceItemBenefitTemplateRepository.findAll();
        assertThat(serviceItemBenefitTemplateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceItemBenefitTemplate in Elasticsearch
        verify(mockServiceItemBenefitTemplateSearchRepository, times(0)).save(serviceItemBenefitTemplate);
    }

    @Test
    @Transactional
    public void deleteServiceItemBenefitTemplate() throws Exception {
        // Initialize the database
        serviceItemBenefitTemplateService.save(serviceItemBenefitTemplate);

        int databaseSizeBeforeDelete = serviceItemBenefitTemplateRepository.findAll().size();

        // Delete the serviceItemBenefitTemplate
        restServiceItemBenefitTemplateMockMvc.perform(delete("/api/service-item-benefit-templates/{id}", serviceItemBenefitTemplate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceItemBenefitTemplate> serviceItemBenefitTemplateList = serviceItemBenefitTemplateRepository.findAll();
        assertThat(serviceItemBenefitTemplateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceItemBenefitTemplate in Elasticsearch
        verify(mockServiceItemBenefitTemplateSearchRepository, times(1)).deleteById(serviceItemBenefitTemplate.getId());
    }

    @Test
    @Transactional
    public void searchServiceItemBenefitTemplate() throws Exception {
        // Initialize the database
        serviceItemBenefitTemplateService.save(serviceItemBenefitTemplate);
        when(mockServiceItemBenefitTemplateSearchRepository.search(queryStringQuery("id:" + serviceItemBenefitTemplate.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(serviceItemBenefitTemplate), PageRequest.of(0, 1), 1));
        // Search the serviceItemBenefitTemplate
        restServiceItemBenefitTemplateMockMvc.perform(get("/api/_search/service-item-benefit-templates?query=id:" + serviceItemBenefitTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceItemBenefitTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME)));
    }
}
