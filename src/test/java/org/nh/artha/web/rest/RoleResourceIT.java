package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.Role;
import org.nh.artha.domain.dto.UserDTO;
import org.nh.artha.repository.RoleRepository;
import org.nh.artha.repository.search.RoleSearchRepository;
import org.nh.artha.service.RoleService;
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
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.nh.artha.web.rest.TestUtil.sameInstant;
import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RoleResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class RoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final UserDTO DEFAULT_CREATED_BY = null;
    private static final UserDTO UPDATED_CREATED_BY = null;

    private static final LocalDateTime DEFAULT_CREATED_DATETIME = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CREATED_DATETIME = LocalDateTime.now();

    private static final UserDTO DEFAULT_UPDATED_BY = null;
    private static final UserDTO UPDATED_UPDATED_BY = null;

    private static final LocalDateTime DEFAULT_UPDATED_DATETIME = LocalDateTime.now();
    private static final LocalDateTime UPDATED_UPDATED_DATETIME =LocalDateTime.now();

    private static final String DEFAULT_PART_OF = "AAAAAAAAAA";
    private static final String UPDATED_PART_OF = "BBBBBBBBBB";

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Mock
    private RoleService roleServiceMock;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.RoleSearchRepositoryMockConfiguration
     */
    @Autowired
    private RoleSearchRepository mockRoleSearchRepository;

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

    private MockMvc restRoleMockMvc;

    private Role role;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoleResource roleResource = new RoleResource(roleService,roleRepository,mockRoleSearchRepository,applicationProperties);
        this.restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
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
    public static Role createEntity(EntityManager em) {
        Role role = new Role()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDatetime(DEFAULT_CREATED_DATETIME)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedDatetime(DEFAULT_UPDATED_DATETIME)
            .partOf(DEFAULT_PART_OF);
        return role;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role role = new Role()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDatetime(UPDATED_CREATED_DATETIME)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDatetime(UPDATED_UPDATED_DATETIME)
            .partOf(UPDATED_PART_OF);
        return role;
    }

    @BeforeEach
    public void initTest() {
        role = createEntity(em);
    }

    @Test
    @Transactional
    public void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // Create the Role
        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRole.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testRole.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRole.getCreatedDatetime()).isEqualTo(DEFAULT_CREATED_DATETIME);
        assertThat(testRole.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testRole.getUpdatedDatetime()).isEqualTo(DEFAULT_UPDATED_DATETIME);
        assertThat(testRole.getPartOf()).isEqualTo(DEFAULT_PART_OF);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(1)).save(testRole);
    }

    @Test
    @Transactional
    public void createRoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // Create the Role with an existing ID
        role.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(0)).save(role);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().size();
        // set the field null
        role.setName(null);

        // Create the Role, which fails.

        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().size();
        // set the field null
        role.setActive(null);

        // Create the Role, which fails.

        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().size();
        // set the field null
        role.setCreatedBy(null);

        // Create the Role, which fails.

        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedDatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = roleRepository.findAll().size();
        // set the field null
        role.setCreatedDatetime(null);

        // Create the Role, which fails.

        restRoleMockMvc.perform(post("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoles() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roleList
        restRoleMockMvc.perform(get("/api/roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            //.andExpect(jsonPath("$.[*].createdDatetime").value(hasItem(sameInstant(DEFAULT_CREATED_DATETIME))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            //.andExpect(jsonPath("$.[*].updatedDatetime").value(hasItem(sameInstant(DEFAULT_UPDATED_DATETIME))))
            .andExpect(jsonPath("$.[*].partOf").value(hasItem(DEFAULT_PART_OF)));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllRolesWithEagerRelationshipsIsEnabled() throws Exception {
        RoleResource roleResource = new RoleResource(roleServiceMock,roleRepository,mockRoleSearchRepository,applicationProperties);
        //when(roleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRoleMockMvc.perform(get("/api/roles?eagerload=true"))
        .andExpect(status().isOk());

        //verify(roleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        RoleResource roleResource = new RoleResource(roleServiceMock,roleRepository,mockRoleSearchRepository,applicationProperties);
            //when(roleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRoleMockMvc.perform(get("/api/roles?eagerload=true"))
        .andExpect(status().isOk());

            //verify(roleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            //.andExpect(jsonPath("$.createdDatetime").value(sameInstant(DEFAULT_CREATED_DATETIME)))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            //.andExpect(jsonPath("$.updatedDatetime").value(sameInstant(DEFAULT_UPDATED_DATETIME)))
            .andExpect(jsonPath("$.partOf").value(DEFAULT_PART_OF));
    }

    @Test
    @Transactional
    public void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRole() throws Exception {
        // Initialize the database
        roleService.save(role);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRoleSearchRepository);

        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).get();
        // Disconnect from session so that the updates on updatedRole are not directly saved in db
        em.detach(updatedRole);
        updatedRole
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDatetime(UPDATED_CREATED_DATETIME)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDatetime(UPDATED_UPDATED_DATETIME)
            .partOf(UPDATED_PART_OF);

        restRoleMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRole)))
            .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roleList.get(roleList.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRole.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testRole.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRole.getCreatedDatetime()).isEqualTo(UPDATED_CREATED_DATETIME);
        assertThat(testRole.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testRole.getUpdatedDatetime()).isEqualTo(UPDATED_UPDATED_DATETIME);
        assertThat(testRole.getPartOf()).isEqualTo(UPDATED_PART_OF);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(1)).save(testRole);
    }

    @Test
    @Transactional
    public void updateNonExistingRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Create the Role

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleMockMvc.perform(put("/api/roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(role)))
            .andExpect(status().isBadRequest());

        // Validate the Role in the database
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(0)).save(role);
    }

    @Test
    @Transactional
    public void deleteRole() throws Exception {
        // Initialize the database
        roleService.save(role);

        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Delete the role
        restRoleMockMvc.perform(delete("/api/roles/{id}", role.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Role in Elasticsearch
        verify(mockRoleSearchRepository, times(1)).deleteById(role.getId());
    }

    @Test
    @Transactional
    public void searchRole() throws Exception {
        // Initialize the database
        roleService.save(role);
        when(mockRoleSearchRepository.search(queryStringQuery("id:" + role.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(role), PageRequest.of(0, 1), 1));
        // Search the role
        restRoleMockMvc.perform(get("/api/_search/roles?query=id:" + role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            //.andExpect(jsonPath("$.[*].createdDatetime").value(hasItem(sameInstant(DEFAULT_CREATED_DATETIME))))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            //.andExpect(jsonPath("$.[*].updatedDatetime").value(hasItem(sameInstant(DEFAULT_UPDATED_DATETIME))))
            .andExpect(jsonPath("$.[*].partOf").value(hasItem(DEFAULT_PART_OF)));
    }
}
