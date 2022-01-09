package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Privilege;
import org.nh.artha.domain.Feature;
import org.nh.artha.repository.PrivilegeRepository;
import org.nh.artha.repository.search.PrivilegeSearchRepository;
import org.nh.artha.service.PrivilegeService;
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
 * Integration tests for the {@link PrivilegeResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class PrivilegeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PrivilegeService privilegeService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PrivilegeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrivilegeSearchRepository mockPrivilegeSearchRepository;

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

    private MockMvc restPrivilegeMockMvc;

    private Privilege privilege;

    @Autowired
    private ApplicationProperties applicationProperties;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrivilegeResource privilegeResource = new PrivilegeResource(privilegeService,privilegeRepository,mockPrivilegeSearchRepository,applicationProperties);
        this.restPrivilegeMockMvc = MockMvcBuilders.standaloneSetup(privilegeResource)
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
    public static Privilege createEntity(EntityManager em) {
        Privilege privilege = new Privilege()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .displayOrder(DEFAULT_DISPLAY_ORDER);
        // Add required entity
        Feature feature;
        if (TestUtil.findAll(em, Feature.class).isEmpty()) {
            feature = FeatureResourceIT.createEntity(em);
            em.persist(feature);
            em.flush();
        } else {
            feature = TestUtil.findAll(em, Feature.class).get(0);
        }
        privilege.setFeature(feature);
        return privilege;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Privilege createUpdatedEntity(EntityManager em) {
        Privilege privilege = new Privilege()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .displayOrder(UPDATED_DISPLAY_ORDER);
        // Add required entity
        Feature feature;
        if (TestUtil.findAll(em, Feature.class).isEmpty()) {
            feature = FeatureResourceIT.createUpdatedEntity(em);
            em.persist(feature);
            em.flush();
        } else {
            feature = TestUtil.findAll(em, Feature.class).get(0);
        }
        privilege.setFeature(feature);
        return privilege;
    }

    @BeforeEach
    public void initTest() {
        privilege = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrivilege() throws Exception {
        int databaseSizeBeforeCreate = privilegeRepository.findAll().size();

        // Create the Privilege
        restPrivilegeMockMvc.perform(post("/api/privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isCreated());

        // Validate the Privilege in the database
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeCreate + 1);
        Privilege testPrivilege = privilegeList.get(privilegeList.size() - 1);
        assertThat(testPrivilege.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPrivilege.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrivilege.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPrivilege.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(1)).save(testPrivilege);
    }

    @Test
    @Transactional
    public void createPrivilegeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = privilegeRepository.findAll().size();

        // Create the Privilege with an existing ID
        privilege.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrivilegeMockMvc.perform(post("/api/privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(0)).save(privilege);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = privilegeRepository.findAll().size();
        // set the field null
        privilege.setCode(null);

        // Create the Privilege, which fails.

        restPrivilegeMockMvc.perform(post("/api/privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isBadRequest());

        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = privilegeRepository.findAll().size();
        // set the field null
        privilege.setName(null);

        // Create the Privilege, which fails.

        restPrivilegeMockMvc.perform(post("/api/privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isBadRequest());

        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = privilegeRepository.findAll().size();
        // set the field null
        privilege.setActive(null);

        // Create the Privilege, which fails.

        restPrivilegeMockMvc.perform(post("/api/privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isBadRequest());

        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrivileges() throws Exception {
        // Initialize the database
        privilegeRepository.saveAndFlush(privilege);

        // Get all the privilegeList
        restPrivilegeMockMvc.perform(get("/api/privileges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privilege.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)));
    }

    @Test
    @Transactional
    public void getPrivilege() throws Exception {
        // Initialize the database
        privilegeRepository.saveAndFlush(privilege);

        // Get the privilege
        restPrivilegeMockMvc.perform(get("/api/privileges/{id}", privilege.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(privilege.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingPrivilege() throws Exception {
        // Get the privilege
        restPrivilegeMockMvc.perform(get("/api/privileges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrivilege() throws Exception {
        // Initialize the database
        privilegeService.save(privilege);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPrivilegeSearchRepository);

        int databaseSizeBeforeUpdate = privilegeRepository.findAll().size();

        // Update the privilege
        Privilege updatedPrivilege = privilegeRepository.findById(privilege.getId()).get();
        // Disconnect from session so that the updates on updatedPrivilege are not directly saved in db
        em.detach(updatedPrivilege);
        updatedPrivilege
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restPrivilegeMockMvc.perform(put("/api/privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrivilege)))
            .andExpect(status().isOk());

        // Validate the Privilege in the database
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeUpdate);
        Privilege testPrivilege = privilegeList.get(privilegeList.size() - 1);
        assertThat(testPrivilege.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPrivilege.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrivilege.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPrivilege.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(1)).save(testPrivilege);
    }

    @Test
    @Transactional
    public void updateNonExistingPrivilege() throws Exception {
        int databaseSizeBeforeUpdate = privilegeRepository.findAll().size();

        // Create the Privilege

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrivilegeMockMvc.perform(put("/api/privileges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privilege)))
            .andExpect(status().isBadRequest());

        // Validate the Privilege in the database
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(0)).save(privilege);
    }

    @Test
    @Transactional
    public void deletePrivilege() throws Exception {
        // Initialize the database
        privilegeService.save(privilege);

        int databaseSizeBeforeDelete = privilegeRepository.findAll().size();

        // Delete the privilege
        restPrivilegeMockMvc.perform(delete("/api/privileges/{id}", privilege.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Privilege> privilegeList = privilegeRepository.findAll();
        assertThat(privilegeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Privilege in Elasticsearch
        verify(mockPrivilegeSearchRepository, times(1)).deleteById(privilege.getId());
    }

    @Test
    @Transactional
    public void searchPrivilege() throws Exception {
        // Initialize the database
        privilegeService.save(privilege);
        when(mockPrivilegeSearchRepository.search(queryStringQuery("id:" + privilege.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(privilege), PageRequest.of(0, 1), 1));
        // Search the privilege
        restPrivilegeMockMvc.perform(get("/api/_search/privileges?query=id:" + privilege.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privilege.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)));
    }
}
