package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Department;
import org.nh.artha.domain.UserMaster;
import org.nh.artha.repository.UserMasterRepository;
import org.nh.artha.repository.search.UserMasterSearchRepository;
import org.nh.artha.service.UserMasterService;
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

import org.nh.artha.domain.enumeration.Status;
/**
 * Integration tests for the {@link UserMasterResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class UserMasterResourceIT {

    private static final String DEFAULT_EMPLOYEE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final List<Department> DEFAULT_DEPARTMENT_NAME = null;
    private static final List<Department> UPDATED_DEPARTMENT_NAME = null;

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private UserMasterService userMasterService;
    @Autowired
    private UserMasterSearchRepository userMasterSearchRepository;
    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.UserMasterSearchRepositoryMockConfiguration
     */
    @Autowired
    private UserMasterSearchRepository mockUserMasterSearchRepository;

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

    private MockMvc restUserMasterMockMvc;

    private UserMaster userMaster;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserMasterResource userMasterResource = new UserMasterResource(userMasterService,userMasterRepository,userMasterSearchRepository,applicationProperties);
        this.restUserMasterMockMvc = MockMvcBuilders.standaloneSetup(userMasterResource)
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
    public static UserMaster createEntity(EntityManager em) {
        UserMaster userMaster = new UserMaster()
            .employeeNumber(DEFAULT_EMPLOYEE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .displayName(DEFAULT_DISPLAY_NAME)
            //.departmentName(DEFAULT_DEPARTMENT_NAME)
            .status(DEFAULT_STATUS);
        return userMaster;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMaster createUpdatedEntity(EntityManager em) {
        UserMaster userMaster = new UserMaster()
            .employeeNumber(UPDATED_EMPLOYEE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            //.departmentName(UPDATED_DEPARTMENT_NAME)
            .status(UPDATED_STATUS);
        return userMaster;
    }

    @BeforeEach
    public void initTest() {
        userMaster = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserMaster() throws Exception {
        int databaseSizeBeforeCreate = userMasterRepository.findAll().size();

        // Create the UserMaster
        restUserMasterMockMvc.perform(post("/api/user-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMaster)))
            .andExpect(status().isCreated());

        // Validate the UserMaster in the database
        List<UserMaster> userMasterList = userMasterRepository.findAll();
        assertThat(userMasterList).hasSize(databaseSizeBeforeCreate + 1);
        UserMaster testUserMaster = userMasterList.get(userMasterList.size() - 1);
        assertThat(testUserMaster.getEmployeeNumber()).isEqualTo(DEFAULT_EMPLOYEE_NUMBER);
        assertThat(testUserMaster.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserMaster.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserMaster.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testUserMaster.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the UserMaster in Elasticsearch
        verify(mockUserMasterSearchRepository, times(1)).save(testUserMaster);
    }

    @Test
    @Transactional
    public void createUserMasterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userMasterRepository.findAll().size();

        // Create the UserMaster with an existing ID
        userMaster.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMasterMockMvc.perform(post("/api/user-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMaster)))
            .andExpect(status().isBadRequest());

        // Validate the UserMaster in the database
        List<UserMaster> userMasterList = userMasterRepository.findAll();
        assertThat(userMasterList).hasSize(databaseSizeBeforeCreate);

        // Validate the UserMaster in Elasticsearch
        verify(mockUserMasterSearchRepository, times(0)).save(userMaster);
    }


    @Test
    @Transactional
    public void checkEmployeeNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMasterRepository.findAll().size();
        // set the field null
        userMaster.setEmployeeNumber(null);

        // Create the UserMaster, which fails.

        restUserMasterMockMvc.perform(post("/api/user-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMaster)))
            .andExpect(status().isBadRequest());

        List<UserMaster> userMasterList = userMasterRepository.findAll();
        assertThat(userMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMasterRepository.findAll().size();
        // set the field null
        userMaster.setFirstName(null);

        // Create the UserMaster, which fails.

        restUserMasterMockMvc.perform(post("/api/user-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMaster)))
            .andExpect(status().isBadRequest());

        List<UserMaster> userMasterList = userMasterRepository.findAll();
        assertThat(userMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userMasterRepository.findAll().size();
        // set the field null
        userMaster.setStatus(null);

        // Create the UserMaster, which fails.

        restUserMasterMockMvc.perform(post("/api/user-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMaster)))
            .andExpect(status().isBadRequest());

        List<UserMaster> userMasterList = userMasterRepository.findAll();
        assertThat(userMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserMasters() throws Exception {
        // Initialize the database
        userMasterRepository.saveAndFlush(userMaster);

        // Get all the userMasterList
        restUserMasterMockMvc.perform(get("/api/user-masters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeNumber").value(hasItem(DEFAULT_EMPLOYEE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getUserMaster() throws Exception {
        // Initialize the database
        userMasterRepository.saveAndFlush(userMaster);

        // Get the userMaster
        restUserMasterMockMvc.perform(get("/api/user-masters/{id}", userMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userMaster.getId().intValue()))
            .andExpect(jsonPath("$.employeeNumber").value(DEFAULT_EMPLOYEE_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserMaster() throws Exception {
        // Get the userMaster
        restUserMasterMockMvc.perform(get("/api/user-masters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserMaster() throws Exception {
        // Initialize the database
        userMasterService.save(userMaster);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockUserMasterSearchRepository);

        int databaseSizeBeforeUpdate = userMasterRepository.findAll().size();

        // Update the userMaster
        UserMaster updatedUserMaster = userMasterRepository.findById(userMaster.getId()).get();
        // Disconnect from session so that the updates on updatedUserMaster are not directly saved in db
        em.detach(updatedUserMaster);
        updatedUserMaster
            .employeeNumber(UPDATED_EMPLOYEE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            //.department(UPDATED_DEPARTMENT_NAME)
            .status(UPDATED_STATUS);

        restUserMasterMockMvc.perform(put("/api/user-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserMaster)))
            .andExpect(status().isOk());

        // Validate the UserMaster in the database
        List<UserMaster> userMasterList = userMasterRepository.findAll();
        assertThat(userMasterList).hasSize(databaseSizeBeforeUpdate);
        UserMaster testUserMaster = userMasterList.get(userMasterList.size() - 1);
        assertThat(testUserMaster.getEmployeeNumber()).isEqualTo(UPDATED_EMPLOYEE_NUMBER);
        assertThat(testUserMaster.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserMaster.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserMaster.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testUserMaster.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the UserMaster in Elasticsearch
        verify(mockUserMasterSearchRepository, times(1)).save(testUserMaster);
    }

    @Test
    @Transactional
    public void updateNonExistingUserMaster() throws Exception {
        int databaseSizeBeforeUpdate = userMasterRepository.findAll().size();

        // Create the UserMaster

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMasterMockMvc.perform(put("/api/user-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userMaster)))
            .andExpect(status().isBadRequest());

        // Validate the UserMaster in the database
        List<UserMaster> userMasterList = userMasterRepository.findAll();
        assertThat(userMasterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UserMaster in Elasticsearch
        verify(mockUserMasterSearchRepository, times(0)).save(userMaster);
    }

    @Test
    @Transactional
    public void deleteUserMaster() throws Exception {
        // Initialize the database
        userMasterService.save(userMaster);

        int databaseSizeBeforeDelete = userMasterRepository.findAll().size();

        // Delete the userMaster
        restUserMasterMockMvc.perform(delete("/api/user-masters/{id}", userMaster.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserMaster> userMasterList = userMasterRepository.findAll();
        assertThat(userMasterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UserMaster in Elasticsearch
        verify(mockUserMasterSearchRepository, times(1)).deleteById(userMaster.getId());
    }

    @Test
    @Transactional
    public void searchUserMaster() throws Exception {
        // Initialize the database
        userMasterService.save(userMaster);
        when(mockUserMasterSearchRepository.search(queryStringQuery("id:" + userMaster.getId())))
            .thenReturn(Collections.singletonList(userMaster));
        // Search the userMaster
        restUserMasterMockMvc.perform(get("/api/_search/user-masters?query=id:" + userMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeNumber").value(hasItem(DEFAULT_EMPLOYEE_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
