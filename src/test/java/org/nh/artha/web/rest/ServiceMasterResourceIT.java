package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ServiceMaster;
import org.nh.artha.domain.ServiceType;
import org.nh.artha.domain.ServiceCategory;
import org.nh.artha.domain.Group;
import org.nh.artha.repository.ServiceMasterRepository;
import org.nh.artha.repository.search.ServiceMasterSearchRepository;
import org.nh.artha.service.ServiceMasterService;
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
 * Integration tests for the {@link ServiceMasterResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ServiceMasterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIN_RE_ORDER_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_MIN_RE_ORDER_DURATION = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_DURATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AUTO_PROCESS = false;
    private static final Boolean UPDATED_AUTO_PROCESS = true;

    private static final Boolean DEFAULT_INDIVIDUALLY_ORDERABLE = false;
    private static final Boolean UPDATED_INDIVIDUALLY_ORDERABLE = true;

    private static final Boolean DEFAULT_CONSENT_REQUIRED = false;
    private static final Boolean UPDATED_CONSENT_REQUIRED = true;

    private static final Boolean DEFAULT_INSURANCE_EXEMPTED = false;
    private static final Boolean UPDATED_INSURANCE_EXEMPTED = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_PROFILE_SERVICE = false;
    private static final Boolean UPDATED_PROFILE_SERVICE = true;

    @Autowired
    private ServiceMasterRepository serviceMasterRepository;

    @Autowired
    private ServiceMasterService serviceMasterService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ServiceMasterSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceMasterSearchRepository mockServiceMasterSearchRepository;

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

    private MockMvc restServiceMasterMockMvc;

    private ServiceMaster serviceMaster;

   /* @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceMasterResource serviceMasterResource = new ServiceMasterResource(serviceMasterService);
        this.restServiceMasterMockMvc = MockMvcBuilders.standaloneSetup(serviceMasterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }*/

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceMaster createEntity(EntityManager em) {
        ServiceMaster serviceMaster = new ServiceMaster()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .shortName(DEFAULT_SHORT_NAME)
            .minReOrderDuration(DEFAULT_MIN_RE_ORDER_DURATION)
            .serviceDuration(DEFAULT_SERVICE_DURATION)
            .autoProcess(DEFAULT_AUTO_PROCESS)
            .individuallyOrderable(DEFAULT_INDIVIDUALLY_ORDERABLE)
            .consentRequired(DEFAULT_CONSENT_REQUIRED)
            .insuranceExempted(DEFAULT_INSURANCE_EXEMPTED)
            .active(DEFAULT_ACTIVE)
            .profileService(DEFAULT_PROFILE_SERVICE);
        // Add required entity
        ServiceType ServiceType;
        if (TestUtil.findAll(em, ServiceType.class).isEmpty()) {
            ServiceType = ServiceTypeResourceIT.createEntity(em);
            em.persist(ServiceType);
            em.flush();
        } else {
            ServiceType = TestUtil.findAll(em, ServiceType.class).get(0);
        }
        serviceMaster.setServiceType(ServiceType);
        // Add required entity
        ServiceCategory ServiceCategory;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            ServiceCategory = ServiceCategoryResourceIT.createEntity(em);
            em.persist(ServiceCategory);
            em.flush();
        } else {
            ServiceCategory = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        serviceMaster.setCategory(ServiceCategory);
        // Add required entity
        Group Group;
        if (TestUtil.findAll(em, Group.class).isEmpty()) {
            Group = GroupResourceIT.createEntity(em);
            em.persist(Group);
            em.flush();
        } else {
            Group = TestUtil.findAll(em, Group.class).get(0);
        }
        serviceMaster.setServiceGroup(Group);
        return serviceMaster;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceMaster createUpdatedEntity(EntityManager em) {
        ServiceMaster serviceMaster = new ServiceMaster()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .shortName(UPDATED_SHORT_NAME)
            .minReOrderDuration(UPDATED_MIN_RE_ORDER_DURATION)
            .serviceDuration(UPDATED_SERVICE_DURATION)
            .autoProcess(UPDATED_AUTO_PROCESS)
            .individuallyOrderable(UPDATED_INDIVIDUALLY_ORDERABLE)
            .consentRequired(UPDATED_CONSENT_REQUIRED)
            .insuranceExempted(UPDATED_INSURANCE_EXEMPTED)
            .active(UPDATED_ACTIVE)
            .profileService(UPDATED_PROFILE_SERVICE);
        // Add required entity
        ServiceType ServiceType;
        if (TestUtil.findAll(em, ServiceType.class).isEmpty()) {
            ServiceType = ServiceTypeResourceIT.createUpdatedEntity(em);
            em.persist(ServiceType);
            em.flush();
        } else {
            ServiceType = TestUtil.findAll(em, ServiceType.class).get(0);
        }
        serviceMaster.setServiceType(ServiceType);
        // Add required entity
        ServiceCategory ServiceCategory;
        if (TestUtil.findAll(em, ServiceCategory.class).isEmpty()) {
            ServiceCategory = ServiceCategoryResourceIT.createUpdatedEntity(em);
            em.persist(ServiceCategory);
            em.flush();
        } else {
            ServiceCategory = TestUtil.findAll(em, ServiceCategory.class).get(0);
        }
        serviceMaster.setCategory(ServiceCategory);
        // Add required entity
        Group Group;
        if (TestUtil.findAll(em, Group.class).isEmpty()) {
            Group = GroupResourceIT.createUpdatedEntity(em);
            em.persist(Group);
            em.flush();
        } else {
            Group = TestUtil.findAll(em, Group.class).get(0);
        }
        serviceMaster.setServiceGroup(Group);
        return serviceMaster;
    }

    @BeforeEach
    public void initTest() {
        serviceMaster = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceMaster() throws Exception {
        int databaseSizeBeforeCreate = serviceMasterRepository.findAll().size();

        // Create the ServiceMaster
        restServiceMasterMockMvc.perform(post("/api/service-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMaster)))
            .andExpect(status().isCreated());

        // Validate the ServiceMaster in the database
        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceMaster testServiceMaster = serviceMasterList.get(serviceMasterList.size() - 1);
        assertThat(testServiceMaster.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServiceMaster.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testServiceMaster.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testServiceMaster.getMinReOrderDuration()).isEqualTo(DEFAULT_MIN_RE_ORDER_DURATION);
        assertThat(testServiceMaster.getServiceDuration()).isEqualTo(DEFAULT_SERVICE_DURATION);
        assertThat(testServiceMaster.isAutoProcess()).isEqualTo(DEFAULT_AUTO_PROCESS);
        assertThat(testServiceMaster.isIndividuallyOrderable()).isEqualTo(DEFAULT_INDIVIDUALLY_ORDERABLE);
        assertThat(testServiceMaster.isConsentRequired()).isEqualTo(DEFAULT_CONSENT_REQUIRED);
        assertThat(testServiceMaster.isInsuranceExempted()).isEqualTo(DEFAULT_INSURANCE_EXEMPTED);
        assertThat(testServiceMaster.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testServiceMaster.isProfileService()).isEqualTo(DEFAULT_PROFILE_SERVICE);

        // Validate the ServiceMaster in Elasticsearch
        verify(mockServiceMasterSearchRepository, times(1)).save(testServiceMaster);
    }

    @Test
    @Transactional
    public void createServiceMasterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceMasterRepository.findAll().size();

        // Create the ServiceMaster with an existing ID
        serviceMaster.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceMasterMockMvc.perform(post("/api/service-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMaster)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceMaster in the database
        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceMaster in Elasticsearch
        verify(mockServiceMasterSearchRepository, times(0)).save(serviceMaster);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceMasterRepository.findAll().size();
        // set the field null
        serviceMaster.setName(null);

        // Create the ServiceMaster, which fails.

        restServiceMasterMockMvc.perform(post("/api/service-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMaster)))
            .andExpect(status().isBadRequest());

        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceMasterRepository.findAll().size();
        // set the field null
        serviceMaster.setCode(null);

        // Create the ServiceMaster, which fails.

        restServiceMasterMockMvc.perform(post("/api/service-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMaster)))
            .andExpect(status().isBadRequest());

        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceMasterRepository.findAll().size();
        // set the field null
        serviceMaster.setActive(null);

        // Create the ServiceMaster, which fails.

        restServiceMasterMockMvc.perform(post("/api/service-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMaster)))
            .andExpect(status().isBadRequest());

        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProfileServiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceMasterRepository.findAll().size();
        // set the field null
        serviceMaster.setProfileService(null);

        // Create the ServiceMaster, which fails.

        restServiceMasterMockMvc.perform(post("/api/service-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMaster)))
            .andExpect(status().isBadRequest());

        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceMasters() throws Exception {
        // Initialize the database
        serviceMasterRepository.saveAndFlush(serviceMaster);

        // Get all the serviceMasterList
        restServiceMasterMockMvc.perform(get("/api/service-masters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].minReOrderDuration").value(hasItem(DEFAULT_MIN_RE_ORDER_DURATION)))
            .andExpect(jsonPath("$.[*].serviceDuration").value(hasItem(DEFAULT_SERVICE_DURATION)))
            .andExpect(jsonPath("$.[*].autoProcess").value(hasItem(DEFAULT_AUTO_PROCESS.booleanValue())))
            .andExpect(jsonPath("$.[*].individuallyOrderable").value(hasItem(DEFAULT_INDIVIDUALLY_ORDERABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].consentRequired").value(hasItem(DEFAULT_CONSENT_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].insuranceExempted").value(hasItem(DEFAULT_INSURANCE_EXEMPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].profileService").value(hasItem(DEFAULT_PROFILE_SERVICE.booleanValue())));
    }

    @Test
    @Transactional
    public void getServiceMaster() throws Exception {
        // Initialize the database
        serviceMasterRepository.saveAndFlush(serviceMaster);

        // Get the serviceMaster
        restServiceMasterMockMvc.perform(get("/api/service-masters/{id}", serviceMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceMaster.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.minReOrderDuration").value(DEFAULT_MIN_RE_ORDER_DURATION))
            .andExpect(jsonPath("$.serviceDuration").value(DEFAULT_SERVICE_DURATION))
            .andExpect(jsonPath("$.autoProcess").value(DEFAULT_AUTO_PROCESS.booleanValue()))
            .andExpect(jsonPath("$.individuallyOrderable").value(DEFAULT_INDIVIDUALLY_ORDERABLE.booleanValue()))
            .andExpect(jsonPath("$.consentRequired").value(DEFAULT_CONSENT_REQUIRED.booleanValue()))
            .andExpect(jsonPath("$.insuranceExempted").value(DEFAULT_INSURANCE_EXEMPTED.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.profileService").value(DEFAULT_PROFILE_SERVICE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceMaster() throws Exception {
        // Get the serviceMaster
        restServiceMasterMockMvc.perform(get("/api/service-masters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceMaster() throws Exception {
        // Initialize the database
        serviceMasterService.save(serviceMaster);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockServiceMasterSearchRepository);

        int databaseSizeBeforeUpdate = serviceMasterRepository.findAll().size();

        // Update the serviceMaster
        ServiceMaster updatedServiceMaster = serviceMasterRepository.findById(serviceMaster.getId()).get();
        // Disconnect from session so that the updates on updatedServiceMaster are not directly saved in db
        em.detach(updatedServiceMaster);
        updatedServiceMaster
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .shortName(UPDATED_SHORT_NAME)
            .minReOrderDuration(UPDATED_MIN_RE_ORDER_DURATION)
            .serviceDuration(UPDATED_SERVICE_DURATION)
            .autoProcess(UPDATED_AUTO_PROCESS)
            .individuallyOrderable(UPDATED_INDIVIDUALLY_ORDERABLE)
            .consentRequired(UPDATED_CONSENT_REQUIRED)
            .insuranceExempted(UPDATED_INSURANCE_EXEMPTED)
            .active(UPDATED_ACTIVE)
            .profileService(UPDATED_PROFILE_SERVICE);

        restServiceMasterMockMvc.perform(put("/api/service-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceMaster)))
            .andExpect(status().isOk());

        // Validate the ServiceMaster in the database
        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeUpdate);
        ServiceMaster testServiceMaster = serviceMasterList.get(serviceMasterList.size() - 1);
        assertThat(testServiceMaster.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServiceMaster.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testServiceMaster.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testServiceMaster.getMinReOrderDuration()).isEqualTo(UPDATED_MIN_RE_ORDER_DURATION);
        assertThat(testServiceMaster.getServiceDuration()).isEqualTo(UPDATED_SERVICE_DURATION);
        assertThat(testServiceMaster.isAutoProcess()).isEqualTo(UPDATED_AUTO_PROCESS);
        assertThat(testServiceMaster.isIndividuallyOrderable()).isEqualTo(UPDATED_INDIVIDUALLY_ORDERABLE);
        assertThat(testServiceMaster.isConsentRequired()).isEqualTo(UPDATED_CONSENT_REQUIRED);
        assertThat(testServiceMaster.isInsuranceExempted()).isEqualTo(UPDATED_INSURANCE_EXEMPTED);
        assertThat(testServiceMaster.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testServiceMaster.isProfileService()).isEqualTo(UPDATED_PROFILE_SERVICE);

        // Validate the ServiceMaster in Elasticsearch
        verify(mockServiceMasterSearchRepository, times(1)).save(testServiceMaster);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceMaster() throws Exception {
        int databaseSizeBeforeUpdate = serviceMasterRepository.findAll().size();

        // Create the ServiceMaster

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceMasterMockMvc.perform(put("/api/service-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMaster)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceMaster in the database
        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceMaster in Elasticsearch
        verify(mockServiceMasterSearchRepository, times(0)).save(serviceMaster);
    }

    @Test
    @Transactional
    public void deleteServiceMaster() throws Exception {
        // Initialize the database
        serviceMasterService.save(serviceMaster);

        int databaseSizeBeforeDelete = serviceMasterRepository.findAll().size();

        // Delete the serviceMaster
        restServiceMasterMockMvc.perform(delete("/api/service-masters/{id}", serviceMaster.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceMaster> serviceMasterList = serviceMasterRepository.findAll();
        assertThat(serviceMasterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceMaster in Elasticsearch
        verify(mockServiceMasterSearchRepository, times(1)).deleteById(serviceMaster.getId());
    }

    @Test
    @Transactional
    public void searchServiceMaster() throws Exception {
        // Initialize the database
        serviceMasterService.save(serviceMaster);
        when(mockServiceMasterSearchRepository.search(queryStringQuery("id:" + serviceMaster.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(serviceMaster), PageRequest.of(0, 1), 1));
        // Search the serviceMaster
        restServiceMasterMockMvc.perform(get("/api/_search/service-masters?query=id:" + serviceMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].minReOrderDuration").value(hasItem(DEFAULT_MIN_RE_ORDER_DURATION)))
            .andExpect(jsonPath("$.[*].serviceDuration").value(hasItem(DEFAULT_SERVICE_DURATION)))
            .andExpect(jsonPath("$.[*].autoProcess").value(hasItem(DEFAULT_AUTO_PROCESS.booleanValue())))
            .andExpect(jsonPath("$.[*].individuallyOrderable").value(hasItem(DEFAULT_INDIVIDUALLY_ORDERABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].consentRequired").value(hasItem(DEFAULT_CONSENT_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].insuranceExempted").value(hasItem(DEFAULT_INSURANCE_EXEMPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].profileService").value(hasItem(DEFAULT_PROFILE_SERVICE.booleanValue())));
    }
}
