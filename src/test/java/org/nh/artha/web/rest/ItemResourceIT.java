package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.Item;
import org.nh.artha.domain.ItemType;
import org.nh.artha.domain.ItemGroup;
import org.nh.artha.domain.ItemCategory;
import org.nh.artha.repository.ItemRepository;
import org.nh.artha.repository.search.ItemSearchRepository;
import org.nh.artha.service.ItemService;
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

import org.nh.artha.domain.enumeration.FSNType;
import org.nh.artha.domain.enumeration.VEDCategory;
/**
 * Integration tests for the {@link ItemResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ItemResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BATCH_TRACKED = false;
    private static final Boolean UPDATED_BATCH_TRACKED = true;

    private static final Boolean DEFAULT_EXPIRY_DATE_REQUIRED = false;
    private static final Boolean UPDATED_EXPIRY_DATE_REQUIRED = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final FSNType DEFAULT_FSN_TYPE = FSNType.Fast;
    private static final FSNType UPDATED_FSN_TYPE = FSNType.Slow;

    private static final VEDCategory DEFAULT_VED_CATEGORY = VEDCategory.Vital;
    private static final VEDCategory UPDATED_VED_CATEGORY = VEDCategory.Essential;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemSearchRepository mockItemSearchRepository;

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

    private MockMvc restItemMockMvc;

    private Item item;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemResource itemResource = new ItemResource(itemService);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource)
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
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .batchTracked(DEFAULT_BATCH_TRACKED)
            .expiryDateRequired(DEFAULT_EXPIRY_DATE_REQUIRED)
            .active(DEFAULT_ACTIVE)
            .remarks(DEFAULT_REMARKS)
            .fsnType(DEFAULT_FSN_TYPE)
            .vedCategory(DEFAULT_VED_CATEGORY);
        // Add required entity
        ItemType ItemType;
        if (TestUtil.findAll(em, ItemType.class).isEmpty()) {
            //ItemType = ItemTypeResourceIT.createEntity(em);
           // em.persist(ItemType);
            em.flush();
        } else {
            ItemType = TestUtil.findAll(em, ItemType.class).get(0);
        }
        //item.setType(ItemType);
        // Add required entity
        ItemGroup ItemGroup;
        if (TestUtil.findAll(em, ItemGroup.class).isEmpty()) {
            //ItemGroup = ItemGroupResourceIT.createEntity(em);
            //em.persist(ItemGroup);
            em.flush();
        } else {
            ItemGroup = TestUtil.findAll(em, ItemGroup.class).get(0);
        }
        //item.setGroup(ItemGroup);
        // Add required entity
        ItemCategory ItemCategory;
        if (TestUtil.findAll(em, ItemCategory.class).isEmpty()) {
            ItemCategory = ItemCategoryResourceIT.createEntity(em);
            em.persist(ItemCategory);
            em.flush();
        } else {
            ItemCategory = TestUtil.findAll(em, ItemCategory.class).get(0);
        }

        return item;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .batchTracked(UPDATED_BATCH_TRACKED)
            .expiryDateRequired(UPDATED_EXPIRY_DATE_REQUIRED)
            .active(UPDATED_ACTIVE)
            .remarks(UPDATED_REMARKS)
            .fsnType(UPDATED_FSN_TYPE)
            .vedCategory(UPDATED_VED_CATEGORY);
        // Add required entity
        ItemType ItemType;
        if (TestUtil.findAll(em, ItemType.class).isEmpty()) {
            //ItemType = ItemTypeResourceIT.createUpdatedEntity(em);
            //em.persist(ItemType);
            em.flush();
        } else {
            ItemType = TestUtil.findAll(em, ItemType.class).get(0);
        }
        //item.setType(ItemType);
        // Add required entity
        ItemGroup ItemGroup;
        if (TestUtil.findAll(em, ItemGroup.class).isEmpty()) {
            //ItemGroup = ItemGroupResourceIT.createUpdatedEntity(em);
           // em.persist(ItemGroup);
            em.flush();
        } else {
            ItemGroup = TestUtil.findAll(em, ItemGroup.class).get(0);
        }
        //item.setGroup(ItemGroup);
        // Add required entity
        ItemCategory ItemCategory;
        if (TestUtil.findAll(em, ItemCategory.class).isEmpty()) {
            ItemCategory = ItemCategoryResourceIT.createUpdatedEntity(em);
            em.persist(ItemCategory);
            em.flush();
        } else {
            ItemCategory = TestUtil.findAll(em, ItemCategory.class).get(0);
        }

        return item;
    }

    @BeforeEach
    public void initTest() {
        item = createEntity(em);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item
        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItem.isBatchTracked()).isEqualTo(DEFAULT_BATCH_TRACKED);
        assertThat(testItem.isExpiryDateRequired()).isEqualTo(DEFAULT_EXPIRY_DATE_REQUIRED);
        assertThat(testItem.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testItem.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testItem.getFsnType()).isEqualTo(DEFAULT_FSN_TYPE);
        assertThat(testItem.getVedCategory()).isEqualTo(DEFAULT_VED_CATEGORY);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(1)).save(testItem);
    }

    @Test
    @Transactional
    public void createItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item with an existing ID
        item.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(0)).save(item);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setCode(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setName(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBatchTrackedIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setBatchTracked(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiryDateRequiredIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setExpiryDateRequired(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setActive(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc.perform(get("/api/items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].batchTracked").value(hasItem(DEFAULT_BATCH_TRACKED.booleanValue())))
            .andExpect(jsonPath("$.[*].expiryDateRequired").value(hasItem(DEFAULT_EXPIRY_DATE_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].fsnType").value(hasItem(DEFAULT_FSN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vedCategory").value(hasItem(DEFAULT_VED_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.batchTracked").value(DEFAULT_BATCH_TRACKED.booleanValue()))
            .andExpect(jsonPath("$.expiryDateRequired").value(DEFAULT_EXPIRY_DATE_REQUIRED.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.fsnType").value(DEFAULT_FSN_TYPE.toString()))
            .andExpect(jsonPath("$.vedCategory").value(DEFAULT_VED_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemService.save(item);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockItemSearchRepository);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .batchTracked(UPDATED_BATCH_TRACKED)
            .expiryDateRequired(UPDATED_EXPIRY_DATE_REQUIRED)
            .active(UPDATED_ACTIVE)
            .remarks(UPDATED_REMARKS)
            .fsnType(UPDATED_FSN_TYPE)
            .vedCategory(UPDATED_VED_CATEGORY);

        restItemMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItem)))
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.isBatchTracked()).isEqualTo(UPDATED_BATCH_TRACKED);
        assertThat(testItem.isExpiryDateRequired()).isEqualTo(UPDATED_EXPIRY_DATE_REQUIRED);
        assertThat(testItem.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testItem.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testItem.getFsnType()).isEqualTo(UPDATED_FSN_TYPE);
        assertThat(testItem.getVedCategory()).isEqualTo(UPDATED_VED_CATEGORY);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(1)).save(testItem);
    }

    @Test
    @Transactional
    public void updateNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Create the Item

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc.perform(put("/api/items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(item)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(0)).save(item);
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemService.save(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Item in Elasticsearch
        verify(mockItemSearchRepository, times(1)).deleteById(item.getId());
    }

    @Test
    @Transactional
    public void searchItem() throws Exception {
        // Initialize the database
        itemService.save(item);
        when(mockItemSearchRepository.search(queryStringQuery("id:" + item.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(item), PageRequest.of(0, 1), 1));
        // Search the item
        restItemMockMvc.perform(get("/api/_search/items?query=id:" + item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].batchTracked").value(hasItem(DEFAULT_BATCH_TRACKED.booleanValue())))
            .andExpect(jsonPath("$.[*].expiryDateRequired").value(hasItem(DEFAULT_EXPIRY_DATE_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].fsnType").value(hasItem(DEFAULT_FSN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vedCategory").value(hasItem(DEFAULT_VED_CATEGORY.toString())));
    }
}
