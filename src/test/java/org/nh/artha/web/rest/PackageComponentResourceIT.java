package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.PackageComponent;
import org.nh.artha.repository.PackageComponentRepository;
import org.nh.artha.repository.search.PackageComponentSearchRepository;
import org.nh.artha.service.PackageComponentService;
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

import org.nh.artha.domain.enumeration.PackageComponentCategory;
/**
 * Integration tests for the {@link PackageComponentResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class PackageComponentResourceIT {

    private static final PackageComponentCategory DEFAULT_PACKAGE_COMPONENT_CATEGORY = PackageComponentCategory.SERVICE;
    private static final PackageComponentCategory UPDATED_PACKAGE_COMPONENT_CATEGORY = PackageComponentCategory.SERVICE;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_AUTO_ORDER = false;
    private static final Boolean UPDATED_AUTO_ORDER = true;

    private static final Boolean DEFAULT_EXCLUDE = false;
    private static final Boolean UPDATED_EXCLUDE = true;

    private static final Integer DEFAULT_QUANTITY_LIMIT = 1;
    private static final Integer UPDATED_QUANTITY_LIMIT = 2;

    private static final Float DEFAULT_AMOUNT_LIMIT = 1F;
    private static final Float UPDATED_AMOUNT_LIMIT = 2F;

    @Autowired
    private PackageComponentRepository packageComponentRepository;

    @Autowired
    private PackageComponentService packageComponentService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PackageComponentSearchRepositoryMockConfiguration
     */
    @Autowired
    private PackageComponentSearchRepository mockPackageComponentSearchRepository;

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

    private MockMvc restPackageComponentMockMvc;

    private PackageComponent packageComponent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PackageComponentResource packageComponentResource = new PackageComponentResource(packageComponentService);
        this.restPackageComponentMockMvc = MockMvcBuilders.standaloneSetup(packageComponentResource)
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
    public static PackageComponent createEntity(EntityManager em) {
        PackageComponent packageComponent = new PackageComponent()
            .packageComponentCategory(DEFAULT_PACKAGE_COMPONENT_CATEGORY)
            .active(DEFAULT_ACTIVE)
            .autoOrder(DEFAULT_AUTO_ORDER)
            .exclude(DEFAULT_EXCLUDE)
            .quantityLimit(DEFAULT_QUANTITY_LIMIT)
            .amountLimit(DEFAULT_AMOUNT_LIMIT);
        return packageComponent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageComponent createUpdatedEntity(EntityManager em) {
        PackageComponent packageComponent = new PackageComponent()
            .packageComponentCategory(UPDATED_PACKAGE_COMPONENT_CATEGORY)
            .active(UPDATED_ACTIVE)
            .autoOrder(UPDATED_AUTO_ORDER)
            .exclude(UPDATED_EXCLUDE)
            .quantityLimit(UPDATED_QUANTITY_LIMIT)
            .amountLimit(UPDATED_AMOUNT_LIMIT);
        return packageComponent;
    }

    @BeforeEach
    public void initTest() {
        packageComponent = createEntity(em);
    }

    @Test
    @Transactional
    public void createPackageComponent() throws Exception {
        int databaseSizeBeforeCreate = packageComponentRepository.findAll().size();

        // Create the PackageComponent
        restPackageComponentMockMvc.perform(post("/api/package-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageComponent)))
            .andExpect(status().isCreated());

        // Validate the PackageComponent in the database
        List<PackageComponent> packageComponentList = packageComponentRepository.findAll();
        assertThat(packageComponentList).hasSize(databaseSizeBeforeCreate + 1);
        PackageComponent testPackageComponent = packageComponentList.get(packageComponentList.size() - 1);
        assertThat(testPackageComponent.getPackageComponentCategory()).isEqualTo(DEFAULT_PACKAGE_COMPONENT_CATEGORY);
        assertThat(testPackageComponent.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPackageComponent.isAutoOrder()).isEqualTo(DEFAULT_AUTO_ORDER);
        assertThat(testPackageComponent.isExclude()).isEqualTo(DEFAULT_EXCLUDE);
        assertThat(testPackageComponent.getQuantityLimit()).isEqualTo(DEFAULT_QUANTITY_LIMIT);
        assertThat(testPackageComponent.getAmountLimit()).isEqualTo(DEFAULT_AMOUNT_LIMIT);

        // Validate the PackageComponent in Elasticsearch
        verify(mockPackageComponentSearchRepository, times(1)).save(testPackageComponent);
    }

    @Test
    @Transactional
    public void createPackageComponentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packageComponentRepository.findAll().size();

        // Create the PackageComponent with an existing ID
        packageComponent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackageComponentMockMvc.perform(post("/api/package-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageComponent)))
            .andExpect(status().isBadRequest());

        // Validate the PackageComponent in the database
        List<PackageComponent> packageComponentList = packageComponentRepository.findAll();
        assertThat(packageComponentList).hasSize(databaseSizeBeforeCreate);

        // Validate the PackageComponent in Elasticsearch
        verify(mockPackageComponentSearchRepository, times(0)).save(packageComponent);
    }


    @Test
    @Transactional
    public void getAllPackageComponents() throws Exception {
        // Initialize the database
        packageComponentRepository.saveAndFlush(packageComponent);

        // Get all the packageComponentList
        restPackageComponentMockMvc.perform(get("/api/package-components?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageComponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].packageComponentCategory").value(hasItem(DEFAULT_PACKAGE_COMPONENT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].autoOrder").value(hasItem(DEFAULT_AUTO_ORDER.booleanValue())))
            .andExpect(jsonPath("$.[*].exclude").value(hasItem(DEFAULT_EXCLUDE.booleanValue())))
            .andExpect(jsonPath("$.[*].quantityLimit").value(hasItem(DEFAULT_QUANTITY_LIMIT)))
            .andExpect(jsonPath("$.[*].amountLimit").value(hasItem(DEFAULT_AMOUNT_LIMIT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getPackageComponent() throws Exception {
        // Initialize the database
        packageComponentRepository.saveAndFlush(packageComponent);

        // Get the packageComponent
        restPackageComponentMockMvc.perform(get("/api/package-components/{id}", packageComponent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(packageComponent.getId().intValue()))
            .andExpect(jsonPath("$.packageComponentCategory").value(DEFAULT_PACKAGE_COMPONENT_CATEGORY.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.autoOrder").value(DEFAULT_AUTO_ORDER.booleanValue()))
            .andExpect(jsonPath("$.exclude").value(DEFAULT_EXCLUDE.booleanValue()))
            .andExpect(jsonPath("$.quantityLimit").value(DEFAULT_QUANTITY_LIMIT))
            .andExpect(jsonPath("$.amountLimit").value(DEFAULT_AMOUNT_LIMIT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPackageComponent() throws Exception {
        // Get the packageComponent
        restPackageComponentMockMvc.perform(get("/api/package-components/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackageComponent() throws Exception {
        // Initialize the database
        packageComponentService.save(packageComponent);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPackageComponentSearchRepository);

        int databaseSizeBeforeUpdate = packageComponentRepository.findAll().size();

        // Update the packageComponent
        PackageComponent updatedPackageComponent = packageComponentRepository.findById(packageComponent.getId()).get();
        // Disconnect from session so that the updates on updatedPackageComponent are not directly saved in db
        em.detach(updatedPackageComponent);
        updatedPackageComponent
            .packageComponentCategory(UPDATED_PACKAGE_COMPONENT_CATEGORY)
            .active(UPDATED_ACTIVE)
            .autoOrder(UPDATED_AUTO_ORDER)
            .exclude(UPDATED_EXCLUDE)
            .quantityLimit(UPDATED_QUANTITY_LIMIT)
            .amountLimit(UPDATED_AMOUNT_LIMIT);

        restPackageComponentMockMvc.perform(put("/api/package-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPackageComponent)))
            .andExpect(status().isOk());

        // Validate the PackageComponent in the database
        List<PackageComponent> packageComponentList = packageComponentRepository.findAll();
        assertThat(packageComponentList).hasSize(databaseSizeBeforeUpdate);
        PackageComponent testPackageComponent = packageComponentList.get(packageComponentList.size() - 1);
        assertThat(testPackageComponent.getPackageComponentCategory()).isEqualTo(UPDATED_PACKAGE_COMPONENT_CATEGORY);
        assertThat(testPackageComponent.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPackageComponent.isAutoOrder()).isEqualTo(UPDATED_AUTO_ORDER);
        assertThat(testPackageComponent.isExclude()).isEqualTo(UPDATED_EXCLUDE);
        assertThat(testPackageComponent.getQuantityLimit()).isEqualTo(UPDATED_QUANTITY_LIMIT);
        assertThat(testPackageComponent.getAmountLimit()).isEqualTo(UPDATED_AMOUNT_LIMIT);

        // Validate the PackageComponent in Elasticsearch
        verify(mockPackageComponentSearchRepository, times(1)).save(testPackageComponent);
    }

    @Test
    @Transactional
    public void updateNonExistingPackageComponent() throws Exception {
        int databaseSizeBeforeUpdate = packageComponentRepository.findAll().size();

        // Create the PackageComponent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageComponentMockMvc.perform(put("/api/package-components")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageComponent)))
            .andExpect(status().isBadRequest());

        // Validate the PackageComponent in the database
        List<PackageComponent> packageComponentList = packageComponentRepository.findAll();
        assertThat(packageComponentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PackageComponent in Elasticsearch
        verify(mockPackageComponentSearchRepository, times(0)).save(packageComponent);
    }

    @Test
    @Transactional
    public void deletePackageComponent() throws Exception {
        // Initialize the database
        packageComponentService.save(packageComponent);

        int databaseSizeBeforeDelete = packageComponentRepository.findAll().size();

        // Delete the packageComponent
        restPackageComponentMockMvc.perform(delete("/api/package-components/{id}", packageComponent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PackageComponent> packageComponentList = packageComponentRepository.findAll();
        assertThat(packageComponentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PackageComponent in Elasticsearch
        verify(mockPackageComponentSearchRepository, times(1)).deleteById(packageComponent.getId());
    }

    @Test
    @Transactional
    public void searchPackageComponent() throws Exception {
        // Initialize the database
        packageComponentService.save(packageComponent);
        when(mockPackageComponentSearchRepository.search(queryStringQuery("id:" + packageComponent.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(packageComponent), PageRequest.of(0, 1), 1));
        // Search the packageComponent
        restPackageComponentMockMvc.perform(get("/api/_search/package-components?query=id:" + packageComponent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageComponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].packageComponentCategory").value(hasItem(DEFAULT_PACKAGE_COMPONENT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].autoOrder").value(hasItem(DEFAULT_AUTO_ORDER.booleanValue())))
            .andExpect(jsonPath("$.[*].exclude").value(hasItem(DEFAULT_EXCLUDE.booleanValue())))
            .andExpect(jsonPath("$.[*].quantityLimit").value(hasItem(DEFAULT_QUANTITY_LIMIT)))
            .andExpect(jsonPath("$.[*].amountLimit").value(hasItem(DEFAULT_AMOUNT_LIMIT.doubleValue())));
    }
}
