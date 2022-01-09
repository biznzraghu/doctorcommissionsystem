package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Organization;
import org.nh.artha.domain.Role;
import org.nh.artha.domain.UserMaster;
import org.nh.artha.domain.UserOrganizationRoleMapping;
import org.nh.artha.repository.UserOrganizationRoleMappingRepository;
import org.nh.artha.repository.search.UserOrganizationRoleMappingSearchRepository;
import org.nh.artha.service.UserOrganizationRoleMappingService;
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
 * Integration tests for the {@link UserOrganizationRoleMappingResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class UserOrganizationRoleMappingResourceIT {

    private static final Organization DEFAULT_HOSPITAL = null;
    private static final Organization UPDATED_HOSPITAL = null;

    private static final Role DEFAULT_ROLE = null;
    private static final Role UPDATED_ROLE = null;

    private static final UserMaster DEFAULT_USER_ID = null;
    private static final UserMaster UPDATED_USER_ID = null;

    @Autowired
    private UserOrganizationRoleMappingRepository userOrganizationRoleMappingRepository;

    @Autowired
    private UserOrganizationRoleMappingService userOrganizationRoleMappingService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.UserOrganizationRoleMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserOrganizationRoleMappingSearchRepository mockUserOrganizationRoleMappingSearchRepository;

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

    private MockMvc restUserOrganizationRoleMappingMockMvc;

    private UserOrganizationRoleMapping userOrganizationRoleMapping;

    @Autowired
    private ApplicationProperties applicationProperties;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserOrganizationRoleMappingResource userOrganizationRoleMappingResource = new UserOrganizationRoleMappingResource(userOrganizationRoleMappingService,userOrganizationRoleMappingRepository,mockUserOrganizationRoleMappingSearchRepository,applicationProperties);
        this.restUserOrganizationRoleMappingMockMvc = MockMvcBuilders.standaloneSetup(userOrganizationRoleMappingResource)
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
    public static UserOrganizationRoleMapping createEntity(EntityManager em) {
        UserOrganizationRoleMapping userOrganizationRoleMapping = new UserOrganizationRoleMapping()
            .organization(DEFAULT_HOSPITAL)
            .role(DEFAULT_ROLE)
            .user(DEFAULT_USER_ID);
        return userOrganizationRoleMapping;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserOrganizationRoleMapping createUpdatedEntity(EntityManager em) {
        UserOrganizationRoleMapping userOrganizationRoleMapping = new UserOrganizationRoleMapping()
            .organization(UPDATED_HOSPITAL)
            .role(UPDATED_ROLE)
            .user(UPDATED_USER_ID);
        return userOrganizationRoleMapping;
    }

    @BeforeEach
    public void initTest() {
        userOrganizationRoleMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserOrganizationRoleMapping() throws Exception {
        int databaseSizeBeforeCreate = userOrganizationRoleMappingRepository.findAll().size();

        // Create the UserOrganizationRoleMapping
        restUserOrganizationRoleMappingMockMvc.perform(post("/api/user-organization-role-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationRoleMapping)))
            .andExpect(status().isCreated());

        // Validate the UserOrganizationRoleMapping in the database
        List<UserOrganizationRoleMapping> userOrganizationRoleMappingList = userOrganizationRoleMappingRepository.findAll();
        assertThat(userOrganizationRoleMappingList).hasSize(databaseSizeBeforeCreate + 1);
        UserOrganizationRoleMapping testUserOrganizationRoleMapping = userOrganizationRoleMappingList.get(userOrganizationRoleMappingList.size() - 1);
        assertThat(testUserOrganizationRoleMapping.getOrganization()).isEqualTo(DEFAULT_HOSPITAL);
        assertThat(testUserOrganizationRoleMapping.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testUserOrganizationRoleMapping.getUser()).isEqualTo(DEFAULT_USER_ID);

        // Validate the UserOrganizationRoleMapping in Elasticsearch
        verify(mockUserOrganizationRoleMappingSearchRepository, times(1)).save(testUserOrganizationRoleMapping);
    }

    @Test
    @Transactional
    public void createUserOrganizationRoleMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userOrganizationRoleMappingRepository.findAll().size();

        // Create the UserOrganizationRoleMapping with an existing ID
        userOrganizationRoleMapping.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserOrganizationRoleMappingMockMvc.perform(post("/api/user-organization-role-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationRoleMapping)))
            .andExpect(status().isBadRequest());

        // Validate the UserOrganizationRoleMapping in the database
        List<UserOrganizationRoleMapping> userOrganizationRoleMappingList = userOrganizationRoleMappingRepository.findAll();
        assertThat(userOrganizationRoleMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserOrganizationRoleMapping in Elasticsearch
        verify(mockUserOrganizationRoleMappingSearchRepository, times(0)).save(userOrganizationRoleMapping);
    }


    @Test
    @Transactional
    public void checkHospitalIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOrganizationRoleMappingRepository.findAll().size();
        // set the field null
        userOrganizationRoleMapping.setOrganization(null);

        // Create the UserOrganizationRoleMapping, which fails.

        restUserOrganizationRoleMappingMockMvc.perform(post("/api/user-organization-role-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationRoleMapping)))
            .andExpect(status().isBadRequest());

        List<UserOrganizationRoleMapping> userOrganizationRoleMappingList = userOrganizationRoleMappingRepository.findAll();
        assertThat(userOrganizationRoleMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOrganizationRoleMappingRepository.findAll().size();
        // set the field null
        userOrganizationRoleMapping.setRole(null);

        // Create the UserOrganizationRoleMapping, which fails.

        restUserOrganizationRoleMappingMockMvc.perform(post("/api/user-organization-role-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationRoleMapping)))
            .andExpect(status().isBadRequest());

        List<UserOrganizationRoleMapping> userOrganizationRoleMappingList = userOrganizationRoleMappingRepository.findAll();
        assertThat(userOrganizationRoleMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOrganizationRoleMappingRepository.findAll().size();
        // set the field null
        userOrganizationRoleMapping.setUser(null);

        // Create the UserOrganizationRoleMapping, which fails.

        restUserOrganizationRoleMappingMockMvc.perform(post("/api/user-organization-role-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationRoleMapping)))
            .andExpect(status().isBadRequest());

        List<UserOrganizationRoleMapping> userOrganizationRoleMappingList = userOrganizationRoleMappingRepository.findAll();
        assertThat(userOrganizationRoleMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserOrganizationRoleMappings() throws Exception {
        // Initialize the database
        userOrganizationRoleMappingRepository.saveAndFlush(userOrganizationRoleMapping);

        // Get all the userOrganizationRoleMappingList
        restUserOrganizationRoleMappingMockMvc.perform(get("/api/user-organization-role-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOrganizationRoleMapping.getId())))
            .andExpect(jsonPath("$.[*].organization").value(hasItem(DEFAULT_HOSPITAL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    public void getUserOrganizationRoleMapping() throws Exception {
        // Initialize the database
        userOrganizationRoleMappingRepository.saveAndFlush(userOrganizationRoleMapping);

        // Get the userOrganizationRoleMapping
        restUserOrganizationRoleMappingMockMvc.perform(get("/api/user-organization-role-mappings/{id}", userOrganizationRoleMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userOrganizationRoleMapping.getId()))
            .andExpect(jsonPath("$.organization").value(DEFAULT_HOSPITAL))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    public void getNonExistingUserOrganizationRoleMapping() throws Exception {
        // Get the userOrganizationRoleMapping
        restUserOrganizationRoleMappingMockMvc.perform(get("/api/user-organization-role-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserOrganizationRoleMapping() throws Exception {
        // Initialize the database
        userOrganizationRoleMappingService.save(userOrganizationRoleMapping);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockUserOrganizationRoleMappingSearchRepository);

        int databaseSizeBeforeUpdate = userOrganizationRoleMappingRepository.findAll().size();

        // Update the userOrganizationRoleMapping
        UserOrganizationRoleMapping updatedUserOrganizationRoleMapping = userOrganizationRoleMappingRepository.findById(userOrganizationRoleMapping.getId()).get();
        // Disconnect from session so that the updates on updatedUserOrganizationRoleMapping are not directly saved in db
        em.detach(updatedUserOrganizationRoleMapping);
        updatedUserOrganizationRoleMapping
            .organization(UPDATED_HOSPITAL)
            .role(UPDATED_ROLE)
            .user(UPDATED_USER_ID);

        restUserOrganizationRoleMappingMockMvc.perform(put("/api/user-organization-role-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserOrganizationRoleMapping)))
            .andExpect(status().isOk());

        // Validate the UserOrganizationRoleMapping in the database
        List<UserOrganizationRoleMapping> userOrganizationRoleMappingList = userOrganizationRoleMappingRepository.findAll();
        assertThat(userOrganizationRoleMappingList).hasSize(databaseSizeBeforeUpdate);
        UserOrganizationRoleMapping testUserOrganizationRoleMapping = userOrganizationRoleMappingList.get(userOrganizationRoleMappingList.size() - 1);
        assertThat(testUserOrganizationRoleMapping.getOrganization()).isEqualTo(UPDATED_HOSPITAL);
        assertThat(testUserOrganizationRoleMapping.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testUserOrganizationRoleMapping.getUser()).isEqualTo(UPDATED_USER_ID);

        // Validate the UserOrganizationRoleMapping in Elasticsearch
        verify(mockUserOrganizationRoleMappingSearchRepository, times(1)).save(testUserOrganizationRoleMapping);
    }

    @Test
    @Transactional
    public void updateNonExistingUserOrganizationRoleMapping() throws Exception {
        int databaseSizeBeforeUpdate = userOrganizationRoleMappingRepository.findAll().size();

        // Create the UserOrganizationRoleMapping

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserOrganizationRoleMappingMockMvc.perform(put("/api/user-organization-role-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationRoleMapping)))
            .andExpect(status().isBadRequest());

        // Validate the UserOrganizationRoleMapping in the database
        List<UserOrganizationRoleMapping> userOrganizationRoleMappingList = userOrganizationRoleMappingRepository.findAll();
        assertThat(userOrganizationRoleMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserOrganizationRoleMapping in Elasticsearch
        verify(mockUserOrganizationRoleMappingSearchRepository, times(0)).save(userOrganizationRoleMapping);
    }

    @Test
    @Transactional
    public void deleteUserOrganizationRoleMapping() throws Exception {
        // Initialize the database
        userOrganizationRoleMappingService.save(userOrganizationRoleMapping);

        int databaseSizeBeforeDelete = userOrganizationRoleMappingRepository.findAll().size();

        // Delete the userOrganizationRoleMapping
        restUserOrganizationRoleMappingMockMvc.perform(delete("/api/user-organization-role-mappings/{id}", userOrganizationRoleMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserOrganizationRoleMapping> userOrganizationRoleMappingList = userOrganizationRoleMappingRepository.findAll();
        assertThat(userOrganizationRoleMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserOrganizationRoleMapping in Elasticsearch
        verify(mockUserOrganizationRoleMappingSearchRepository, times(1)).deleteById(userOrganizationRoleMapping.getId());
    }

    @Test
    @Transactional
    public void searchUserOrganizationRoleMapping() throws Exception {
        // Initialize the database
        userOrganizationRoleMappingService.save(userOrganizationRoleMapping);
        when(mockUserOrganizationRoleMappingSearchRepository.search(queryStringQuery("id:" + userOrganizationRoleMapping.getId())))
            .thenReturn(Collections.singletonList(userOrganizationRoleMapping));
        // Search the userOrganizationRoleMapping
        restUserOrganizationRoleMappingMockMvc.perform(get("/api/_search/user-organization-role-mappings?query=id:" + userOrganizationRoleMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOrganizationRoleMapping.getId())))
            .andExpect(jsonPath("$.[*].organization").value(hasItem(DEFAULT_HOSPITAL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER_ID)));
    }
}
