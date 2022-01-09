package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.PackageMaster;
import org.nh.artha.repository.PackageMasterRepository;
import org.nh.artha.repository.search.PackageMasterSearchRepository;
import org.nh.artha.service.PackageMasterService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nh.artha.domain.enumeration.PackageCategory;
import org.nh.artha.domain.enumeration.VisitType;
import org.nh.artha.domain.enumeration.durationUnit;
/**
 * Integration tests for the {@link PackageMasterResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class PackageMasterResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final PackageCategory DEFAULT_PACKAGE_CATEGORY = PackageCategory.SURGICAL;
    private static final PackageCategory UPDATED_PACKAGE_CATEGORY = PackageCategory.SURGICAL;

    private static final VisitType DEFAULT_VISIT_TYPE = VisitType.IP;
    private static final VisitType UPDATED_VISIT_TYPE = VisitType.OP;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final durationUnit DEFAULT_DURATION_UNIT = durationUnit.DAYS;
    private static final durationUnit UPDATED_DURATION_UNIT = durationUnit.DAYS;

    private static final Integer DEFAULT_NUMBER_OF_ALLOWED_VISIT = 1;
    private static final Integer UPDATED_NUMBER_OF_ALLOWED_VISIT = 2;

    private static final Boolean DEFAULT_TEMPLATE = false;
    private static final Boolean UPDATED_TEMPLATE = true;

    private static final Boolean DEFAULT_PLAN_VALIDATION = false;
    private static final Boolean UPDATED_PLAN_VALIDATION = true;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

    @Autowired
    private PackageMasterRepository packageMasterRepository;

    @Autowired
    private PackageMasterService packageMasterService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PackageMasterSearchRepositoryMockConfiguration
     */
    @Autowired
    private PackageMasterSearchRepository mockPackageMasterSearchRepository;

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

    private MockMvc restPackageMasterMockMvc;

    private PackageMaster packageMaster;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PackageMasterResource packageMasterResource = new PackageMasterResource(packageMasterService);
        this.restPackageMasterMockMvc = MockMvcBuilders.standaloneSetup(packageMasterResource)
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
    public static PackageMaster createEntity(EntityManager em) {
        PackageMaster packageMaster = new PackageMaster()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .packageCategory(DEFAULT_PACKAGE_CATEGORY)
            .duration(DEFAULT_DURATION)
            .numberOfAllowedVisit(DEFAULT_NUMBER_OF_ALLOWED_VISIT)
            .template(DEFAULT_TEMPLATE)
            .planValidation(DEFAULT_PLAN_VALIDATION)
            .comments(DEFAULT_COMMENTS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .active(DEFAULT_ACTIVE)
            .abbreviation(DEFAULT_ABBREVIATION);
        return packageMaster;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageMaster createUpdatedEntity(EntityManager em) {
        PackageMaster packageMaster = new PackageMaster()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .packageCategory(UPDATED_PACKAGE_CATEGORY)
            .duration(UPDATED_DURATION)
            .numberOfAllowedVisit(UPDATED_NUMBER_OF_ALLOWED_VISIT)
            .template(UPDATED_TEMPLATE)
            .planValidation(UPDATED_PLAN_VALIDATION)
            .comments(UPDATED_COMMENTS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .active(UPDATED_ACTIVE)
            .abbreviation(UPDATED_ABBREVIATION);
        return packageMaster;
    }

    @BeforeEach
    public void initTest() {
        packageMaster = createEntity(em);
    }

    @Test
    @Transactional
    public void createPackageMaster() throws Exception {
        int databaseSizeBeforeCreate = packageMasterRepository.findAll().size();

        // Create the PackageMaster
        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isCreated());

        // Validate the PackageMaster in the database
        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeCreate + 1);
        PackageMaster testPackageMaster = packageMasterList.get(packageMasterList.size() - 1);
        assertThat(testPackageMaster.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPackageMaster.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPackageMaster.getPackageCategory()).isEqualTo(DEFAULT_PACKAGE_CATEGORY);
        assertThat(testPackageMaster.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testPackageMaster.getNumberOfAllowedVisit()).isEqualTo(DEFAULT_NUMBER_OF_ALLOWED_VISIT);
        assertThat(testPackageMaster.isTemplate()).isEqualTo(DEFAULT_TEMPLATE);
        assertThat(testPackageMaster.isPlanValidation()).isEqualTo(DEFAULT_PLAN_VALIDATION);
        assertThat(testPackageMaster.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testPackageMaster.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPackageMaster.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPackageMaster.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPackageMaster.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);

        // Validate the PackageMaster in Elasticsearch
        verify(mockPackageMasterSearchRepository, times(1)).save(testPackageMaster);
    }

    @Test
    @Transactional
    public void createPackageMasterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packageMasterRepository.findAll().size();

        // Create the PackageMaster with an existing ID
        packageMaster.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        // Validate the PackageMaster in the database
        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeCreate);

        // Validate the PackageMaster in Elasticsearch
        verify(mockPackageMasterSearchRepository, times(0)).save(packageMaster);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageMasterRepository.findAll().size();
        // set the field null
        packageMaster.setCode(null);

        // Create the PackageMaster, which fails.

        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageMasterRepository.findAll().size();
        // set the field null
        packageMaster.setName(null);

        // Create the PackageMaster, which fails.

        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPackageCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageMasterRepository.findAll().size();
        // set the field null
        packageMaster.setPackageCategory(null);

        // Create the PackageMaster, which fails.

        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVisitTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageMasterRepository.findAll().size();
        // set the field null

        // Create the PackageMaster, which fails.

        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageMasterRepository.findAll().size();
        // set the field null
        // Create the PackageMaster, which fails.

        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageMasterRepository.findAll().size();
        // set the field null
        packageMaster.setStartDate(null);

        // Create the PackageMaster, which fails.

        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageMasterRepository.findAll().size();
        // set the field null
        packageMaster.setEndDate(null);

        // Create the PackageMaster, which fails.

        restPackageMasterMockMvc.perform(post("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPackageMasters() throws Exception {
        // Initialize the database
        packageMasterRepository.saveAndFlush(packageMaster);

        // Get all the packageMasterList
        restPackageMasterMockMvc.perform(get("/api/package-masters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].packageCategory").value(hasItem(DEFAULT_PACKAGE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].VisitType").value(hasItem(DEFAULT_VISIT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].durationUnit").value(hasItem(DEFAULT_DURATION_UNIT.toString())))
            .andExpect(jsonPath("$.[*].numberOfAllowedVisit").value(hasItem(DEFAULT_NUMBER_OF_ALLOWED_VISIT)))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.booleanValue())))
            .andExpect(jsonPath("$.[*].planValidation").value(hasItem(DEFAULT_PLAN_VALIDATION.booleanValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)));
    }

    @Test
    @Transactional
    public void getPackageMaster() throws Exception {
        // Initialize the database
        packageMasterRepository.saveAndFlush(packageMaster);

        // Get the packageMaster
        restPackageMasterMockMvc.perform(get("/api/package-masters/{id}", packageMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(packageMaster.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.packageCategory").value(DEFAULT_PACKAGE_CATEGORY.toString()))
            .andExpect(jsonPath("$.VisitType").value(DEFAULT_VISIT_TYPE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.durationUnit").value(DEFAULT_DURATION_UNIT.toString()))
            .andExpect(jsonPath("$.numberOfAllowedVisit").value(DEFAULT_NUMBER_OF_ALLOWED_VISIT))
            .andExpect(jsonPath("$.template").value(DEFAULT_TEMPLATE.booleanValue()))
            .andExpect(jsonPath("$.planValidation").value(DEFAULT_PLAN_VALIDATION.booleanValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION));
    }

    @Test
    @Transactional
    public void getNonExistingPackageMaster() throws Exception {
        // Get the packageMaster
        restPackageMasterMockMvc.perform(get("/api/package-masters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackageMaster() throws Exception {
        // Initialize the database
        packageMasterService.save(packageMaster);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPackageMasterSearchRepository);

        int databaseSizeBeforeUpdate = packageMasterRepository.findAll().size();

        // Update the packageMaster
        PackageMaster updatedPackageMaster = packageMasterRepository.findById(packageMaster.getId()).get();
        // Disconnect from session so that the updates on updatedPackageMaster are not directly saved in db
        em.detach(updatedPackageMaster);
        updatedPackageMaster
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .packageCategory(UPDATED_PACKAGE_CATEGORY)
            .duration(UPDATED_DURATION)
            .numberOfAllowedVisit(UPDATED_NUMBER_OF_ALLOWED_VISIT)
            .template(UPDATED_TEMPLATE)
            .planValidation(UPDATED_PLAN_VALIDATION)
            .comments(UPDATED_COMMENTS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .active(UPDATED_ACTIVE)
            .abbreviation(UPDATED_ABBREVIATION);

        restPackageMasterMockMvc.perform(put("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPackageMaster)))
            .andExpect(status().isOk());

        // Validate the PackageMaster in the database
        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeUpdate);
        PackageMaster testPackageMaster = packageMasterList.get(packageMasterList.size() - 1);
        assertThat(testPackageMaster.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPackageMaster.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPackageMaster.getPackageCategory()).isEqualTo(UPDATED_PACKAGE_CATEGORY);
        assertThat(testPackageMaster.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testPackageMaster.getNumberOfAllowedVisit()).isEqualTo(UPDATED_NUMBER_OF_ALLOWED_VISIT);
        assertThat(testPackageMaster.isTemplate()).isEqualTo(UPDATED_TEMPLATE);
        assertThat(testPackageMaster.isPlanValidation()).isEqualTo(UPDATED_PLAN_VALIDATION);
        assertThat(testPackageMaster.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testPackageMaster.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPackageMaster.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPackageMaster.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPackageMaster.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);

        // Validate the PackageMaster in Elasticsearch
        verify(mockPackageMasterSearchRepository, times(1)).save(testPackageMaster);
    }

    @Test
    @Transactional
    public void updateNonExistingPackageMaster() throws Exception {
        int databaseSizeBeforeUpdate = packageMasterRepository.findAll().size();

        // Create the PackageMaster

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageMasterMockMvc.perform(put("/api/package-masters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageMaster)))
            .andExpect(status().isBadRequest());

        // Validate the PackageMaster in the database
        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PackageMaster in Elasticsearch
        verify(mockPackageMasterSearchRepository, times(0)).save(packageMaster);
    }

    @Test
    @Transactional
    public void deletePackageMaster() throws Exception {
        // Initialize the database
        packageMasterService.save(packageMaster);

        int databaseSizeBeforeDelete = packageMasterRepository.findAll().size();

        // Delete the packageMaster
        restPackageMasterMockMvc.perform(delete("/api/package-masters/{id}", packageMaster.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PackageMaster> packageMasterList = packageMasterRepository.findAll();
        assertThat(packageMasterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PackageMaster in Elasticsearch
        verify(mockPackageMasterSearchRepository, times(1)).deleteById(packageMaster.getId());
    }

    @Test
    @Transactional
    public void searchPackageMaster() throws Exception {
        // Initialize the database
        packageMasterService.save(packageMaster);
        when(mockPackageMasterSearchRepository.search(queryStringQuery("id:" + packageMaster.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(packageMaster), PageRequest.of(0, 1), 1));
        // Search the packageMaster
        restPackageMasterMockMvc.perform(get("/api/_search/package-masters?query=id:" + packageMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].packageCategory").value(hasItem(DEFAULT_PACKAGE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].VisitType").value(hasItem(DEFAULT_VISIT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].durationUnit").value(hasItem(DEFAULT_DURATION_UNIT.toString())))
            .andExpect(jsonPath("$.[*].numberOfAllowedVisit").value(hasItem(DEFAULT_NUMBER_OF_ALLOWED_VISIT)))
            .andExpect(jsonPath("$.[*].template").value(hasItem(DEFAULT_TEMPLATE.booleanValue())))
            .andExpect(jsonPath("$.[*].planValidation").value(hasItem(DEFAULT_PLAN_VALIDATION.booleanValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)));
    }
}
