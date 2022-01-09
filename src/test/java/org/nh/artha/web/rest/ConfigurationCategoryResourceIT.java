package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ConfigurationCategory;
import org.nh.artha.repository.ConfigurationCategoryRepository;
import org.nh.artha.repository.search.ConfigurationCategorySearchRepository;
import org.nh.artha.service.CommonValueSetCodeService;
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
 * Integration tests for the {@link ConfigurationCategoryResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ConfigurationCategoryResourceIT {

    @Autowired
    private CommonValueSetCodeService configurationCategoryRepository;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ConfigurationCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigurationCategorySearchRepository mockConfigurationCategorySearchRepository;

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

    private MockMvc restConfigurationCategoryMockMvc;

    private ConfigurationCategory configurationCategory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigurationCategoryResource configurationCategoryResource = new ConfigurationCategoryResource(configurationCategoryRepository);
        this.restConfigurationCategoryMockMvc = MockMvcBuilders.standaloneSetup(configurationCategoryResource)
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
    public static ConfigurationCategory createEntity(EntityManager em) {
        ConfigurationCategory configurationCategory = new ConfigurationCategory();
        return configurationCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigurationCategory createUpdatedEntity(EntityManager em) {
        ConfigurationCategory configurationCategory = new ConfigurationCategory();
        return configurationCategory;
    }

    @BeforeEach
    public void initTest() {
        configurationCategory = createEntity(em);
    }



    @Test
    @Transactional
    public void getAllConfigurationCategories() throws Exception {
        // Initialize the database
        //configurationCategoryRepository.saveAndFlush(configurationCategory);

        // Get all the configurationCategoryList
        restConfigurationCategoryMockMvc.perform(get("/api/configuration-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configurationCategory.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getConfigurationCategory() throws Exception {
        // Initialize the database


        // Get the configurationCategory
        restConfigurationCategoryMockMvc.perform(get("/api/configuration-categories/{id}", configurationCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configurationCategory.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigurationCategory() throws Exception {
        // Get the configurationCategory
        restConfigurationCategoryMockMvc.perform(get("/api/configuration-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }





}
