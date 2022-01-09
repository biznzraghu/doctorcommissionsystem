package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;

import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;

/**
 * Integration tests for the {@link DepartmentMasterResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class DepartmentMasterResourceIT {
/*
    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private DepartmentMasterRepository departmentMasterRepository;

    @Autowired
    private DepartmentMasterMapper departmentMasterMapper;

    @Autowired
    private DepartmentMasterService departmentMasterService;

    *//**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.DepartmentMasterSearchRepositoryMockConfiguration
     *//*
    @Autowired
    private DepartmentMasterSearchRepository mockDepartmentMasterSearchRepository;

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

    private MockMvc restDepartmentMasterMockMvc;

    private DepartmentMaster departmentMaster;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepartmentMasterResource departmentMasterResource = new DepartmentMasterResource(departmentMasterService,departmentMasterRepository,mockDepartmentMasterSearchRepository,applicationProperties);
        this.restDepartmentMasterMockMvc = MockMvcBuilders.standaloneSetup(departmentMasterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    *//**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static DepartmentMaster createEntity(EntityManager em) {
        DepartmentMaster departmentMaster = new DepartmentMaster()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE);
        return departmentMaster;
    }
    *//**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static DepartmentMaster createUpdatedEntity(EntityManager em) {
        DepartmentMaster departmentMaster = new DepartmentMaster()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE);
        return departmentMaster;
    }

    @BeforeEach
    public void initTest() {
        departmentMaster = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartmentMaster() throws Exception {
        int databaseSizeBeforeCreate = departmentMasterRepository.findAll().size();

        // Create the DepartmentMaster
        DepartmentMasterDTO departmentMasterDTO = departmentMasterMapper.toDto(departmentMaster);
        restDepartmentMasterMockMvc.perform(post("/api/department-masters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentMasterDTO)))
            .andExpect(status().isCreated());

        // Validate the DepartmentMaster in the database
        List<DepartmentMaster> departmentMasterList = departmentMasterRepository.findAll();
        assertThat(departmentMasterList).hasSize(databaseSizeBeforeCreate + 1);
        DepartmentMaster testDepartmentMaster = departmentMasterList.get(departmentMasterList.size() - 1);
        assertThat(testDepartmentMaster.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDepartmentMaster.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartmentMaster.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the DepartmentMaster in Elasticsearch
        verify(mockDepartmentMasterSearchRepository, times(1)).save(testDepartmentMaster);
    }

    @Test
    @Transactional
    public void createDepartmentMasterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departmentMasterRepository.findAll().size();

        // Create the DepartmentMaster with an existing ID
        departmentMaster.setId(1L);
        DepartmentMasterDTO departmentMasterDTO = departmentMasterMapper.toDto(departmentMaster);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentMasterMockMvc.perform(post("/api/department-masters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentMasterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentMaster in the database
        List<DepartmentMaster> departmentMasterList = departmentMasterRepository.findAll();
        assertThat(departmentMasterList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepartmentMaster in Elasticsearch
        verify(mockDepartmentMasterSearchRepository, times(0)).save(departmentMaster);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentMasterRepository.findAll().size();
        // set the field null
        departmentMaster.setCode(null);

        // Create the DepartmentMaster, which fails.
        DepartmentMasterDTO departmentMasterDTO = departmentMasterMapper.toDto(departmentMaster);

        restDepartmentMasterMockMvc.perform(post("/api/department-masters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentMasterDTO)))
            .andExpect(status().isBadRequest());

        List<DepartmentMaster> departmentMasterList = departmentMasterRepository.findAll();
        assertThat(departmentMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentMasterRepository.findAll().size();
        // set the field null
        departmentMaster.setName(null);

        // Create the DepartmentMaster, which fails.
        DepartmentMasterDTO departmentMasterDTO = departmentMasterMapper.toDto(departmentMaster);

        restDepartmentMasterMockMvc.perform(post("/api/department-masters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentMasterDTO)))
            .andExpect(status().isBadRequest());

        List<DepartmentMaster> departmentMasterList = departmentMasterRepository.findAll();
        assertThat(departmentMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentMasterRepository.findAll().size();
        // set the field null
        departmentMaster.setActive(null);

        // Create the DepartmentMaster, which fails.
        DepartmentMasterDTO departmentMasterDTO = departmentMasterMapper.toDto(departmentMaster);

        restDepartmentMasterMockMvc.perform(post("/api/department-masters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentMasterDTO)))
            .andExpect(status().isBadRequest());

        List<DepartmentMaster> departmentMasterList = departmentMasterRepository.findAll();
        assertThat(departmentMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepartmentMasters() throws Exception {
        // Initialize the database
        departmentMasterRepository.saveAndFlush(departmentMaster);

        // Get all the departmentMasterList
        restDepartmentMasterMockMvc.perform(get("/api/department-masters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getDepartmentMaster() throws Exception {
        // Initialize the database
        departmentMasterRepository.saveAndFlush(departmentMaster);

        // Get the departmentMaster
        restDepartmentMasterMockMvc.perform(get("/api/department-masters/{id}", departmentMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departmentMaster.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDepartmentMaster() throws Exception {
        // Get the departmentMaster
        restDepartmentMasterMockMvc.perform(get("/api/department-masters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartmentMaster() throws Exception {
        // Initialize the database
        departmentMasterRepository.saveAndFlush(departmentMaster);

        int databaseSizeBeforeUpdate = departmentMasterRepository.findAll().size();

        // Update the departmentMaster
        DepartmentMaster updatedDepartmentMaster = departmentMasterRepository.findById(departmentMaster.getId()).get();
        // Disconnect from session so that the updates on updatedDepartmentMaster are not directly saved in db
        em.detach(updatedDepartmentMaster);
        updatedDepartmentMaster
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE);
        DepartmentMasterDTO departmentMasterDTO = departmentMasterMapper.toDto(updatedDepartmentMaster);

        restDepartmentMasterMockMvc.perform(put("/api/department-masters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentMasterDTO)))
            .andExpect(status().isOk());

        // Validate the DepartmentMaster in the database
        List<DepartmentMaster> departmentMasterList = departmentMasterRepository.findAll();
        assertThat(departmentMasterList).hasSize(databaseSizeBeforeUpdate);
        DepartmentMaster testDepartmentMaster = departmentMasterList.get(departmentMasterList.size() - 1);
        assertThat(testDepartmentMaster.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDepartmentMaster.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartmentMaster.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the DepartmentMaster in Elasticsearch
        verify(mockDepartmentMasterSearchRepository, times(1)).save(testDepartmentMaster);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartmentMaster() throws Exception {
        int databaseSizeBeforeUpdate = departmentMasterRepository.findAll().size();

        // Create the DepartmentMaster
        DepartmentMasterDTO departmentMasterDTO = departmentMasterMapper.toDto(departmentMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMasterMockMvc.perform(put("/api/department-masters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentMasterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentMaster in the database
        List<DepartmentMaster> departmentMasterList = departmentMasterRepository.findAll();
        assertThat(departmentMasterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentMaster in Elasticsearch
        verify(mockDepartmentMasterSearchRepository, times(0)).save(departmentMaster);
    }

    @Test
    @Transactional
    public void deleteDepartmentMaster() throws Exception {
        // Initialize the database
        departmentMasterRepository.saveAndFlush(departmentMaster);

        int databaseSizeBeforeDelete = departmentMasterRepository.findAll().size();

        // Delete the departmentMaster
        restDepartmentMasterMockMvc.perform(delete("/api/department-masters/{id}", departmentMaster.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepartmentMaster> departmentMasterList = departmentMasterRepository.findAll();
        assertThat(departmentMasterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepartmentMaster in Elasticsearch
        verify(mockDepartmentMasterSearchRepository, times(1)).deleteById(departmentMaster.getId());
    }

    @Test
    @Transactional
    public void searchDepartmentMaster() throws Exception {
        // Initialize the database
        departmentMasterRepository.saveAndFlush(departmentMaster);
        when(mockDepartmentMasterSearchRepository.search(queryStringQuery("id:" + departmentMaster.getId())))
            .thenReturn(Collections.singletonList(departmentMaster));
        // Search the departmentMaster
        restDepartmentMasterMockMvc.perform(get("/api/_search/department-masters?query=id:" + departmentMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }*/
}
