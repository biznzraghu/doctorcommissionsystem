package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserOrganizationDepartmentMapping;
import org.nh.artha.repository.UserOrganizationDepartmentMappingRepository;
import org.nh.artha.repository.search.UserOrganizationDepartmentMappingSearchRepository;
import org.nh.artha.service.UserOrganizationDepartmentMappingService;
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
 * Integration tests for the {@link UserOrganizationDepartmentMappingResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class UserOrganizationDepartmentMappingResourceIT {

    @Autowired
    private UserOrganizationDepartmentMappingRepository userOrganizationDepartmentMappingRepository;

    @Autowired
    private UserOrganizationDepartmentMappingService userOrganizationDepartmentMappingService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.UserOrganizationDepartmentMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserOrganizationDepartmentMappingSearchRepository mockUserOrganizationDepartmentMappingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private Validator validator;

    private MockMvc restUserOrganizationDepartmentMappingMockMvc;

    private UserOrganizationDepartmentMapping userOrganizationDepartmentMapping;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserOrganizationDepartmentMappingResource userOrganizationDepartmentMappingResource = new UserOrganizationDepartmentMappingResource(userOrganizationDepartmentMappingService,userOrganizationDepartmentMappingRepository,mockUserOrganizationDepartmentMappingSearchRepository,applicationProperties);
        this.restUserOrganizationDepartmentMappingMockMvc = MockMvcBuilders.standaloneSetup(userOrganizationDepartmentMappingResource)
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
    public static UserOrganizationDepartmentMapping createEntity(EntityManager em) {
        UserOrganizationDepartmentMapping userOrganizationDepartmentMapping = new UserOrganizationDepartmentMapping();
        return userOrganizationDepartmentMapping;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserOrganizationDepartmentMapping createUpdatedEntity(EntityManager em) {
        UserOrganizationDepartmentMapping userOrganizationDepartmentMapping = new UserOrganizationDepartmentMapping();
        return userOrganizationDepartmentMapping;
    }

    @BeforeEach
    public void initTest() {
        userOrganizationDepartmentMapping = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserOrganizationDepartmentMapping() throws Exception {
        int databaseSizeBeforeCreate = userOrganizationDepartmentMappingRepository.findAll().size();

        // Create the UserOrganizationDepartmentMapping
        restUserOrganizationDepartmentMappingMockMvc.perform(post("/api/user-organization-department-mappings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationDepartmentMapping)))
            .andExpect(status().isCreated());

        // Validate the UserOrganizationDepartmentMapping in the database
        List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList = userOrganizationDepartmentMappingRepository.findAll();
        assertThat(userOrganizationDepartmentMappingList).hasSize(databaseSizeBeforeCreate + 1);
        UserOrganizationDepartmentMapping testUserOrganizationDepartmentMapping = userOrganizationDepartmentMappingList.get(userOrganizationDepartmentMappingList.size() - 1);

        // Validate the UserOrganizationDepartmentMapping in Elasticsearch
        verify(mockUserOrganizationDepartmentMappingSearchRepository, times(1)).save(testUserOrganizationDepartmentMapping);
    }

    @Test
    @Transactional
    public void createUserOrganizationDepartmentMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userOrganizationDepartmentMappingRepository.findAll().size();

        // Create the UserOrganizationDepartmentMapping with an existing ID
        userOrganizationDepartmentMapping.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserOrganizationDepartmentMappingMockMvc.perform(post("/api/user-organization-department-mappings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationDepartmentMapping)))
            .andExpect(status().isBadRequest());

        // Validate the UserOrganizationDepartmentMapping in the database
        List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList = userOrganizationDepartmentMappingRepository.findAll();
        assertThat(userOrganizationDepartmentMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserOrganizationDepartmentMapping in Elasticsearch
        verify(mockUserOrganizationDepartmentMappingSearchRepository, times(0)).save(userOrganizationDepartmentMapping);
    }


    @Test
    @Transactional
    public void getAllUserOrganizationDepartmentMappings() throws Exception {
        // Initialize the database
        userOrganizationDepartmentMappingRepository.saveAndFlush(userOrganizationDepartmentMapping);

        // Get all the userOrganizationDepartmentMappingList
        restUserOrganizationDepartmentMappingMockMvc.perform(get("/api/user-organization-department-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOrganizationDepartmentMapping.getId().intValue())));
    }

    @Test
    @Transactional
    public void getUserOrganizationDepartmentMapping() throws Exception {
        // Initialize the database
        userOrganizationDepartmentMappingRepository.saveAndFlush(userOrganizationDepartmentMapping);

        // Get the userOrganizationDepartmentMapping
        restUserOrganizationDepartmentMappingMockMvc.perform(get("/api/user-organization-department-mappings/{id}", userOrganizationDepartmentMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userOrganizationDepartmentMapping.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserOrganizationDepartmentMapping() throws Exception {
        // Get the userOrganizationDepartmentMapping
        restUserOrganizationDepartmentMappingMockMvc.perform(get("/api/user-organization-department-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserOrganizationDepartmentMapping() throws Exception {
        // Initialize the database
        userOrganizationDepartmentMappingService.save(userOrganizationDepartmentMapping);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockUserOrganizationDepartmentMappingSearchRepository);

        int databaseSizeBeforeUpdate = userOrganizationDepartmentMappingRepository.findAll().size();

        // Update the userOrganizationDepartmentMapping
        UserOrganizationDepartmentMapping updatedUserOrganizationDepartmentMapping = userOrganizationDepartmentMappingRepository.findById(userOrganizationDepartmentMapping.getId()).get();
        // Disconnect from session so that the updates on updatedUserOrganizationDepartmentMapping are not directly saved in db
        em.detach(updatedUserOrganizationDepartmentMapping);

        restUserOrganizationDepartmentMappingMockMvc.perform(put("/api/user-organization-department-mappings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserOrganizationDepartmentMapping)))
            .andExpect(status().isOk());

        // Validate the UserOrganizationDepartmentMapping in the database
        List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList = userOrganizationDepartmentMappingRepository.findAll();
        assertThat(userOrganizationDepartmentMappingList).hasSize(databaseSizeBeforeUpdate);
        UserOrganizationDepartmentMapping testUserOrganizationDepartmentMapping = userOrganizationDepartmentMappingList.get(userOrganizationDepartmentMappingList.size() - 1);

        // Validate the UserOrganizationDepartmentMapping in Elasticsearch
        verify(mockUserOrganizationDepartmentMappingSearchRepository, times(1)).save(testUserOrganizationDepartmentMapping);
    }

    @Test
    @Transactional
    public void updateNonExistingUserOrganizationDepartmentMapping() throws Exception {
        int databaseSizeBeforeUpdate = userOrganizationDepartmentMappingRepository.findAll().size();

        // Create the UserOrganizationDepartmentMapping

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserOrganizationDepartmentMappingMockMvc.perform(put("/api/user-organization-department-mappings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userOrganizationDepartmentMapping)))
            .andExpect(status().isBadRequest());

        // Validate the UserOrganizationDepartmentMapping in the database
        List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList = userOrganizationDepartmentMappingRepository.findAll();
        assertThat(userOrganizationDepartmentMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserOrganizationDepartmentMapping in Elasticsearch
        verify(mockUserOrganizationDepartmentMappingSearchRepository, times(0)).save(userOrganizationDepartmentMapping);
    }

    @Test
    @Transactional
    public void deleteUserOrganizationDepartmentMapping() throws Exception {
        // Initialize the database
        userOrganizationDepartmentMappingService.save(userOrganizationDepartmentMapping);

        int databaseSizeBeforeDelete = userOrganizationDepartmentMappingRepository.findAll().size();

        // Delete the userOrganizationDepartmentMapping
        restUserOrganizationDepartmentMappingMockMvc.perform(delete("/api/user-organization-department-mappings/{id}", userOrganizationDepartmentMapping.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserOrganizationDepartmentMapping> userOrganizationDepartmentMappingList = userOrganizationDepartmentMappingRepository.findAll();
        assertThat(userOrganizationDepartmentMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserOrganizationDepartmentMapping in Elasticsearch
        verify(mockUserOrganizationDepartmentMappingSearchRepository, times(1)).deleteById(userOrganizationDepartmentMapping.getId());
    }

    @Test
    @Transactional
    public void searchUserOrganizationDepartmentMapping() throws Exception {
        // Initialize the database
        userOrganizationDepartmentMappingService.save(userOrganizationDepartmentMapping);
        when(mockUserOrganizationDepartmentMappingSearchRepository.search(queryStringQuery("id:" + userOrganizationDepartmentMapping.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(userOrganizationDepartmentMapping), PageRequest.of(0, 1), 1));
        // Search the userOrganizationDepartmentMapping
        restUserOrganizationDepartmentMappingMockMvc.perform(get("/api/_search/user-organization-department-mappings?query=id:" + userOrganizationDepartmentMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOrganizationDepartmentMapping.getId().intValue())));
    }
}
