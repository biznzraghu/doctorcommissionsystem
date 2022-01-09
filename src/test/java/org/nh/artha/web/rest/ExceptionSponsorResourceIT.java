package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ExceptionSponsor;
import org.nh.artha.domain.SponsorType;
import org.nh.artha.repository.ExceptionSponsorRepository;
import org.nh.artha.repository.search.ExceptionSponsorSearchRepository;
import org.nh.artha.service.ExceptionSponsorService;
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

/**
 * Integration tests for the {@link ExceptionSponsorResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ExceptionSponsorResourceIT {

    private static final Boolean DEFAULT_APPLICABLE = false;
    private static final Boolean UPDATED_APPLICABLE = true;

    public static final List<SponsorType> DEFAULT_SPONSOR_TYPE = new ArrayList<>();
    public static final List<SponsorType> UPDATED_SPONSOR_TYPE = new ArrayList<>();

    @Autowired
    private ExceptionSponsorRepository exceptionSponsorRepository;

    @Autowired
    private ExceptionSponsorService exceptionSponsorService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ExceptionSponsorSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExceptionSponsorSearchRepository mockExceptionSponsorSearchRepository;

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

    private MockMvc restExceptionSponsorMockMvc;

    private ExceptionSponsor exceptionSponsor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExceptionSponsorResource exceptionSponsorResource = new ExceptionSponsorResource(exceptionSponsorService);
        this.restExceptionSponsorMockMvc = MockMvcBuilders.standaloneSetup(exceptionSponsorResource)
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
    public static ExceptionSponsor createEntity(EntityManager em) {
        ExceptionSponsor exceptionSponsor = new ExceptionSponsor()
            .applicable(DEFAULT_APPLICABLE);
           // .sponsorType(DEFAULT_SPONSOR_TYPE);
        return exceptionSponsor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExceptionSponsor createUpdatedEntity(EntityManager em) {
        ExceptionSponsor exceptionSponsor = new ExceptionSponsor()
            .applicable(UPDATED_APPLICABLE);
        //    .sponsorType(UPDATED_SPONSOR_TYPE);
        return exceptionSponsor;
    }

    @BeforeEach
    public void initTest() {
        exceptionSponsor = createEntity(em);
    }

    @Test
    @Transactional
    public void createExceptionSponsor() throws Exception {
        int databaseSizeBeforeCreate = exceptionSponsorRepository.findAll().size();

        // Create the ExceptionSponsor
        restExceptionSponsorMockMvc.perform(post("/api/exception-sponsors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exceptionSponsor)))
            .andExpect(status().isCreated());

        // Validate the ExceptionSponsor in the database
        List<ExceptionSponsor> exceptionSponsorList = exceptionSponsorRepository.findAll();
        assertThat(exceptionSponsorList).hasSize(databaseSizeBeforeCreate + 1);
        ExceptionSponsor testExceptionSponsor = exceptionSponsorList.get(exceptionSponsorList.size() - 1);
        assertThat(testExceptionSponsor.isApplicable()).isEqualTo(DEFAULT_APPLICABLE);
        assertThat(testExceptionSponsor.getSponsorType()).isEqualTo(DEFAULT_SPONSOR_TYPE);

        // Validate the ExceptionSponsor in Elasticsearch
        verify(mockExceptionSponsorSearchRepository, times(1)).save(testExceptionSponsor);
    }

    @Test
    @Transactional
    public void createExceptionSponsorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exceptionSponsorRepository.findAll().size();

        // Create the ExceptionSponsor with an existing ID
        exceptionSponsor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExceptionSponsorMockMvc.perform(post("/api/exception-sponsors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exceptionSponsor)))
            .andExpect(status().isBadRequest());

        // Validate the ExceptionSponsor in the database
        List<ExceptionSponsor> exceptionSponsorList = exceptionSponsorRepository.findAll();
        assertThat(exceptionSponsorList).hasSize(databaseSizeBeforeCreate);

        // Validate the ExceptionSponsor in Elasticsearch
        verify(mockExceptionSponsorSearchRepository, times(0)).save(exceptionSponsor);
    }


    @Test
    @Transactional
    public void getAllExceptionSponsors() throws Exception {
        // Initialize the database
        exceptionSponsorRepository.saveAndFlush(exceptionSponsor);

        // Get all the exceptionSponsorList
        restExceptionSponsorMockMvc.perform(get("/api/exception-sponsors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exceptionSponsor.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicable").value(hasItem(DEFAULT_APPLICABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].sponsorType").value(hasItem(DEFAULT_SPONSOR_TYPE)));
    }

    @Test
    @Transactional
    public void getExceptionSponsor() throws Exception {
        // Initialize the database
        exceptionSponsorRepository.saveAndFlush(exceptionSponsor);

        // Get the exceptionSponsor
        restExceptionSponsorMockMvc.perform(get("/api/exception-sponsors/{id}", exceptionSponsor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exceptionSponsor.getId().intValue()))
            .andExpect(jsonPath("$.applicable").value(DEFAULT_APPLICABLE.booleanValue()))
            .andExpect(jsonPath("$.sponsorType").value(DEFAULT_SPONSOR_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingExceptionSponsor() throws Exception {
        // Get the exceptionSponsor
        restExceptionSponsorMockMvc.perform(get("/api/exception-sponsors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExceptionSponsor() throws Exception {
        // Initialize the database
        exceptionSponsorService.save(exceptionSponsor);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockExceptionSponsorSearchRepository);

        int databaseSizeBeforeUpdate = exceptionSponsorRepository.findAll().size();

        // Update the exceptionSponsor
        ExceptionSponsor updatedExceptionSponsor = exceptionSponsorRepository.findById(exceptionSponsor.getId()).get();
        // Disconnect from session so that the updates on updatedExceptionSponsor are not directly saved in db
        em.detach(updatedExceptionSponsor);
        updatedExceptionSponsor
            .applicable(UPDATED_APPLICABLE);
            //.sponsorType(UPDATED_SPONSOR_TYPE);

        restExceptionSponsorMockMvc.perform(put("/api/exception-sponsors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExceptionSponsor)))
            .andExpect(status().isOk());

        // Validate the ExceptionSponsor in the database
        List<ExceptionSponsor> exceptionSponsorList = exceptionSponsorRepository.findAll();
        assertThat(exceptionSponsorList).hasSize(databaseSizeBeforeUpdate);
        ExceptionSponsor testExceptionSponsor = exceptionSponsorList.get(exceptionSponsorList.size() - 1);
        assertThat(testExceptionSponsor.isApplicable()).isEqualTo(UPDATED_APPLICABLE);
        assertThat(testExceptionSponsor.getSponsorType()).isEqualTo(UPDATED_SPONSOR_TYPE);

        // Validate the ExceptionSponsor in Elasticsearch
        verify(mockExceptionSponsorSearchRepository, times(1)).save(testExceptionSponsor);
    }

    @Test
    @Transactional
    public void updateNonExistingExceptionSponsor() throws Exception {
        int databaseSizeBeforeUpdate = exceptionSponsorRepository.findAll().size();

        // Create the ExceptionSponsor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExceptionSponsorMockMvc.perform(put("/api/exception-sponsors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exceptionSponsor)))
            .andExpect(status().isBadRequest());

        // Validate the ExceptionSponsor in the database
        List<ExceptionSponsor> exceptionSponsorList = exceptionSponsorRepository.findAll();
        assertThat(exceptionSponsorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExceptionSponsor in Elasticsearch
        verify(mockExceptionSponsorSearchRepository, times(0)).save(exceptionSponsor);
    }

    @Test
    @Transactional
    public void deleteExceptionSponsor() throws Exception {
        // Initialize the database
        exceptionSponsorService.save(exceptionSponsor);

        int databaseSizeBeforeDelete = exceptionSponsorRepository.findAll().size();

        // Delete the exceptionSponsor
        restExceptionSponsorMockMvc.perform(delete("/api/exception-sponsors/{id}", exceptionSponsor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExceptionSponsor> exceptionSponsorList = exceptionSponsorRepository.findAll();
        assertThat(exceptionSponsorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ExceptionSponsor in Elasticsearch
        verify(mockExceptionSponsorSearchRepository, times(1)).deleteById(exceptionSponsor.getId());
    }

    @Test
    @Transactional
    public void searchExceptionSponsor() throws Exception {
        // Initialize the database
        exceptionSponsorService.save(exceptionSponsor);
        when(mockExceptionSponsorSearchRepository.search(queryStringQuery("id:" + exceptionSponsor.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(exceptionSponsor), PageRequest.of(0, 1), 1));
        // Search the exceptionSponsor
        restExceptionSponsorMockMvc.perform(get("/api/_search/exception-sponsors?query=id:" + exceptionSponsor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exceptionSponsor.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicable").value(hasItem(DEFAULT_APPLICABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].sponsorType").value(hasItem(DEFAULT_SPONSOR_TYPE)));
    }
}
