package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.ApplicableConsultant;
import org.nh.artha.repository.ApplicableConsultantRepository;
import org.nh.artha.repository.search.ApplicableConsultantSearchRepository;
import org.nh.artha.service.ApplicableConsultantService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ApplicableConsultantResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ApplicableConsultantResourceIT {

    private static final Boolean DEFAULT_INCLUDE = false;
    private static final Boolean UPDATED_INCLUDE = true;

    @Autowired
    private ApplicableConsultantRepository applicableConsultantRepository;

    @Autowired
    private ApplicableConsultantService applicableConsultantService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ApplicableConsultantSearchRepositoryMockConfiguration
     */
    @Autowired
    private ApplicableConsultantSearchRepository mockApplicableConsultantSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicableConsultantMockMvc;

    private ApplicableConsultant applicableConsultant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicableConsultant createEntity(EntityManager em) {
        ApplicableConsultant applicableConsultant = new ApplicableConsultant();
        applicableConsultant.setInclude(DEFAULT_INCLUDE);
        return applicableConsultant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicableConsultant createUpdatedEntity(EntityManager em) {
        ApplicableConsultant applicableConsultant = new ApplicableConsultant();
        applicableConsultant.setInclude(UPDATED_INCLUDE);
        return applicableConsultant;
    }

    @BeforeEach
    public void initTest() {
        applicableConsultant = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicableConsultant() throws Exception {
        int databaseSizeBeforeCreate = applicableConsultantRepository.findAll().size();

        // Create the ApplicableConsultant
        restApplicableConsultantMockMvc.perform(post("/api/applicable-consultants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicableConsultant)))
            .andExpect(status().isCreated());

        // Validate the ApplicableConsultant in the database
        List<ApplicableConsultant> applicableConsultantList = applicableConsultantRepository.findAll();
        assertThat(applicableConsultantList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicableConsultant testApplicableConsultant = applicableConsultantList.get(applicableConsultantList.size() - 1);
        assertThat(testApplicableConsultant.isInclude()).isEqualTo(DEFAULT_INCLUDE);

        // Validate the ApplicableConsultant in Elasticsearch
        verify(mockApplicableConsultantSearchRepository, times(1)).save(testApplicableConsultant);
    }

    @Test
    @Transactional
    public void createApplicableConsultantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicableConsultantRepository.findAll().size();

        // Create the ApplicableConsultant with an existing ID
        applicableConsultant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicableConsultantMockMvc.perform(post("/api/applicable-consultants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicableConsultant)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicableConsultant in the database
        List<ApplicableConsultant> applicableConsultantList = applicableConsultantRepository.findAll();
        assertThat(applicableConsultantList).hasSize(databaseSizeBeforeCreate);

        // Validate the ApplicableConsultant in Elasticsearch
        verify(mockApplicableConsultantSearchRepository, times(0)).save(applicableConsultant);
    }


    @Test
    @Transactional
    public void getAllApplicableConsultants() throws Exception {
        // Initialize the database
        applicableConsultantRepository.saveAndFlush(applicableConsultant);

        // Get all the applicableConsultantList
        restApplicableConsultantMockMvc.perform(get("/api/applicable-consultants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicableConsultant.getId().intValue())))
            .andExpect(jsonPath("$.[*].include").value(hasItem(DEFAULT_INCLUDE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getApplicableConsultant() throws Exception {
        // Initialize the database
        applicableConsultantRepository.saveAndFlush(applicableConsultant);

        // Get the applicableConsultant
        restApplicableConsultantMockMvc.perform(get("/api/applicable-consultants/{id}", applicableConsultant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicableConsultant.getId().intValue()))
            .andExpect(jsonPath("$.include").value(DEFAULT_INCLUDE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicableConsultant() throws Exception {
        // Get the applicableConsultant
        restApplicableConsultantMockMvc.perform(get("/api/applicable-consultants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicableConsultant() throws Exception {
        // Initialize the database
        applicableConsultantService.save(applicableConsultant);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockApplicableConsultantSearchRepository);

        int databaseSizeBeforeUpdate = applicableConsultantRepository.findAll().size();

        // Update the applicableConsultant
        ApplicableConsultant updatedApplicableConsultant = applicableConsultantRepository.findById(applicableConsultant.getId()).get();
        // Disconnect from session so that the updates on updatedApplicableConsultant are not directly saved in db
        em.detach(updatedApplicableConsultant);
        updatedApplicableConsultant.setInclude(UPDATED_INCLUDE);

        restApplicableConsultantMockMvc.perform(put("/api/applicable-consultants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplicableConsultant)))
            .andExpect(status().isOk());

        // Validate the ApplicableConsultant in the database
        List<ApplicableConsultant> applicableConsultantList = applicableConsultantRepository.findAll();
        assertThat(applicableConsultantList).hasSize(databaseSizeBeforeUpdate);
        ApplicableConsultant testApplicableConsultant = applicableConsultantList.get(applicableConsultantList.size() - 1);
        assertThat(testApplicableConsultant.isInclude()).isEqualTo(UPDATED_INCLUDE);

        // Validate the ApplicableConsultant in Elasticsearch
        verify(mockApplicableConsultantSearchRepository, times(1)).save(testApplicableConsultant);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicableConsultant() throws Exception {
        int databaseSizeBeforeUpdate = applicableConsultantRepository.findAll().size();

        // Create the ApplicableConsultant

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicableConsultantMockMvc.perform(put("/api/applicable-consultants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(applicableConsultant)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicableConsultant in the database
        List<ApplicableConsultant> applicableConsultantList = applicableConsultantRepository.findAll();
        assertThat(applicableConsultantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ApplicableConsultant in Elasticsearch
        verify(mockApplicableConsultantSearchRepository, times(0)).save(applicableConsultant);
    }

    @Test
    @Transactional
    public void deleteApplicableConsultant() throws Exception {
        // Initialize the database
        applicableConsultantService.save(applicableConsultant);

        int databaseSizeBeforeDelete = applicableConsultantRepository.findAll().size();

        // Delete the applicableConsultant
        restApplicableConsultantMockMvc.perform(delete("/api/applicable-consultants/{id}", applicableConsultant.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicableConsultant> applicableConsultantList = applicableConsultantRepository.findAll();
        assertThat(applicableConsultantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ApplicableConsultant in Elasticsearch
        verify(mockApplicableConsultantSearchRepository, times(1)).deleteById(applicableConsultant.getId());
    }

    @Test
    @Transactional
    public void searchApplicableConsultant() throws Exception {
        // Initialize the database
        applicableConsultantService.save(applicableConsultant);
        when(mockApplicableConsultantSearchRepository.search(queryStringQuery("id:" + applicableConsultant.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(applicableConsultant), PageRequest.of(0, 1), 1));
        // Search the applicableConsultant
        restApplicableConsultantMockMvc.perform(get("/api/_search/applicable-consultants?query=id:" + applicableConsultant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicableConsultant.getId().intValue())))
            .andExpect(jsonPath("$.[*].include").value(hasItem(DEFAULT_INCLUDE.booleanValue())));
    }
}
