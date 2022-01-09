package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ItemCategory;
import org.nh.artha.repository.ItemCategoryRepository;
import org.nh.artha.repository.search.ItemCategorySearchRepository;
import org.nh.artha.service.ItemCategoryService;
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
 * Integration tests for the {@link ItemCategoryResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ItemCategoryResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_GROUP = false;
    private static final Boolean UPDATED_GROUP = true;

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private ItemCategoryService itemCategoryService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ItemCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemCategorySearchRepository mockItemCategorySearchRepository;

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

    private MockMvc restItemCategoryMockMvc;

    private ItemCategory itemCategory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemCategoryResource itemCategoryResource = new ItemCategoryResource(itemCategoryService);
        this.restItemCategoryMockMvc = MockMvcBuilders.standaloneSetup(itemCategoryResource)
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
    public static ItemCategory createEntity(EntityManager em) {
        ItemCategory itemCategory = new ItemCategory()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE)
            .group(DEFAULT_GROUP);
        return itemCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemCategory createUpdatedEntity(EntityManager em) {
        ItemCategory itemCategory = new ItemCategory()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .group(UPDATED_GROUP);
        return itemCategory;
    }

    @BeforeEach
    public void initTest() {
        itemCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemCategory() throws Exception {
        int databaseSizeBeforeCreate = itemCategoryRepository.findAll().size();

        // Create the ItemCategory
        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isCreated());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ItemCategory testItemCategory = itemCategoryList.get(itemCategoryList.size() - 1);
        assertThat(testItemCategory.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testItemCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItemCategory.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testItemCategory.isGroup()).isEqualTo(DEFAULT_GROUP);

        // Validate the ItemCategory in Elasticsearch
        verify(mockItemCategorySearchRepository, times(1)).save(testItemCategory);
    }

    @Test
    @Transactional
    public void createItemCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemCategoryRepository.findAll().size();

        // Create the ItemCategory with an existing ID
        itemCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the ItemCategory in Elasticsearch
        verify(mockItemCategorySearchRepository, times(0)).save(itemCategory);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCategoryRepository.findAll().size();
        // set the field null
        itemCategory.setCode(null);

        // Create the ItemCategory, which fails.

        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isBadRequest());

        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCategoryRepository.findAll().size();
        // set the field null
        itemCategory.setDescription(null);

        // Create the ItemCategory, which fails.

        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isBadRequest());

        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCategoryRepository.findAll().size();
        // set the field null
        itemCategory.setActive(null);

        // Create the ItemCategory, which fails.

        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isBadRequest());

        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCategoryRepository.findAll().size();
        // set the field null
        itemCategory.setGroup(null);

        // Create the ItemCategory, which fails.

        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isBadRequest());

        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemCategories() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        // Get all the itemCategoryList
        restItemCategoryMockMvc.perform(get("/api/item-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getItemCategory() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        // Get the itemCategory
        restItemCategoryMockMvc.perform(get("/api/item-categories/{id}", itemCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemCategory.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItemCategory() throws Exception {
        // Get the itemCategory
        restItemCategoryMockMvc.perform(get("/api/item-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemCategory() throws Exception {
        // Initialize the database
        itemCategoryService.save(itemCategory);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockItemCategorySearchRepository);

        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();

        // Update the itemCategory
        ItemCategory updatedItemCategory = itemCategoryRepository.findById(itemCategory.getId()).get();
        // Disconnect from session so that the updates on updatedItemCategory are not directly saved in db
        em.detach(updatedItemCategory);
        updatedItemCategory
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .group(UPDATED_GROUP);

        restItemCategoryMockMvc.perform(put("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemCategory)))
            .andExpect(status().isOk());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
        ItemCategory testItemCategory = itemCategoryList.get(itemCategoryList.size() - 1);
        assertThat(testItemCategory.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItemCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItemCategory.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testItemCategory.isGroup()).isEqualTo(UPDATED_GROUP);

        // Validate the ItemCategory in Elasticsearch
        verify(mockItemCategorySearchRepository, times(1)).save(testItemCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingItemCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();

        // Create the ItemCategory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemCategoryMockMvc.perform(put("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ItemCategory in Elasticsearch
        verify(mockItemCategorySearchRepository, times(0)).save(itemCategory);
    }

    @Test
    @Transactional
    public void deleteItemCategory() throws Exception {
        // Initialize the database
        itemCategoryService.save(itemCategory);

        int databaseSizeBeforeDelete = itemCategoryRepository.findAll().size();

        // Delete the itemCategory
        restItemCategoryMockMvc.perform(delete("/api/item-categories/{id}", itemCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ItemCategory in Elasticsearch
        verify(mockItemCategorySearchRepository, times(1)).deleteById(itemCategory.getId());
    }

    @Test
    @Transactional
    public void searchItemCategory() throws Exception {
        // Initialize the database
        itemCategoryService.save(itemCategory);
        when(mockItemCategorySearchRepository.search(queryStringQuery("id:" + itemCategory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(itemCategory), PageRequest.of(0, 1), 1));
        // Search the itemCategory
        restItemCategoryMockMvc.perform(get("/api/_search/item-categories?query=id:" + itemCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.booleanValue())));
    }
}
