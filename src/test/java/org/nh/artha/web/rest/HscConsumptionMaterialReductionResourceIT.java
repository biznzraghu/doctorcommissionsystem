package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.HscConsumptionMaterialReduction;
import org.nh.artha.repository.HscConsumptionMaterialReductionRepository;
import org.nh.artha.repository.search.HscConsumptionMaterialReductionSearchRepository;
import org.nh.artha.service.HscConsumptionMaterialReductionService;

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
 * Integration tests for the {@link HscConsumptionMaterialReductionResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HscConsumptionMaterialReductionResourceIT {

    private static final Long DEFAULT_HSC_ID = 1L;
    private static final Long UPDATED_HSC_ID = 2L;

    @Autowired
    private HscConsumptionMaterialReductionRepository hscConsumptionMaterialReductionRepository;

    @Autowired
    private HscConsumptionMaterialReductionService hscConsumptionMaterialReductionService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.HscConsumptionMaterialReductionSearchRepositoryMockConfiguration
     */
    @Autowired
    private HscConsumptionMaterialReductionSearchRepository mockHscConsumptionMaterialReductionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHscConsumptionMaterialReductionMockMvc;

    private HscConsumptionMaterialReduction hscConsumptionMaterialReduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HscConsumptionMaterialReduction createEntity(EntityManager em) {
        HscConsumptionMaterialReduction hscConsumptionMaterialReduction = new HscConsumptionMaterialReduction();
        hscConsumptionMaterialReduction.setHscId(DEFAULT_HSC_ID);
        return hscConsumptionMaterialReduction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HscConsumptionMaterialReduction createUpdatedEntity(EntityManager em) {
        HscConsumptionMaterialReduction hscConsumptionMaterialReduction = new HscConsumptionMaterialReduction();
        hscConsumptionMaterialReduction.setHscId(UPDATED_HSC_ID);
        return hscConsumptionMaterialReduction;
    }

    @BeforeEach
    public void initTest() {
        hscConsumptionMaterialReduction = createEntity(em);
    }

    @Test
    @Transactional
    public void createHscConsumptionMaterialReduction() throws Exception {
        int databaseSizeBeforeCreate = hscConsumptionMaterialReductionRepository.findAll().size();

        // Create the HscConsumptionMaterialReduction
        restHscConsumptionMaterialReductionMockMvc.perform(post("/api/hsc-consumption-material-reductions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hscConsumptionMaterialReduction)))
            .andExpect(status().isCreated());

        // Validate the HscConsumptionMaterialReduction in the database
        List<HscConsumptionMaterialReduction> hscConsumptionMaterialReductionList = hscConsumptionMaterialReductionRepository.findAll();
        assertThat(hscConsumptionMaterialReductionList).hasSize(databaseSizeBeforeCreate + 1);
        HscConsumptionMaterialReduction testHscConsumptionMaterialReduction = hscConsumptionMaterialReductionList.get(hscConsumptionMaterialReductionList.size() - 1);
        assertThat(testHscConsumptionMaterialReduction.getHscId()).isEqualTo(DEFAULT_HSC_ID);

        // Validate the HscConsumptionMaterialReduction in Elasticsearch
        verify(mockHscConsumptionMaterialReductionSearchRepository, times(1)).save(testHscConsumptionMaterialReduction);
    }

    @Test
    @Transactional
    public void createHscConsumptionMaterialReductionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hscConsumptionMaterialReductionRepository.findAll().size();

        // Create the HscConsumptionMaterialReduction with an existing ID
        hscConsumptionMaterialReduction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHscConsumptionMaterialReductionMockMvc.perform(post("/api/hsc-consumption-material-reductions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hscConsumptionMaterialReduction)))
            .andExpect(status().isBadRequest());

        // Validate the HscConsumptionMaterialReduction in the database
        List<HscConsumptionMaterialReduction> hscConsumptionMaterialReductionList = hscConsumptionMaterialReductionRepository.findAll();
        assertThat(hscConsumptionMaterialReductionList).hasSize(databaseSizeBeforeCreate);

        // Validate the HscConsumptionMaterialReduction in Elasticsearch
        verify(mockHscConsumptionMaterialReductionSearchRepository, times(0)).save(hscConsumptionMaterialReduction);
    }


    @Test
    @Transactional
    public void getAllHscConsumptionMaterialReductions() throws Exception {
        // Initialize the database
        hscConsumptionMaterialReductionRepository.saveAndFlush(hscConsumptionMaterialReduction);

        // Get all the hscConsumptionMaterialReductionList
        restHscConsumptionMaterialReductionMockMvc.perform(get("/api/hsc-consumption-material-reductions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hscConsumptionMaterialReduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].hscId").value(hasItem(DEFAULT_HSC_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getHscConsumptionMaterialReduction() throws Exception {
        // Initialize the database
        hscConsumptionMaterialReductionRepository.saveAndFlush(hscConsumptionMaterialReduction);

        // Get the hscConsumptionMaterialReduction
        restHscConsumptionMaterialReductionMockMvc.perform(get("/api/hsc-consumption-material-reductions/{id}", hscConsumptionMaterialReduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hscConsumptionMaterialReduction.getId().intValue()))
            .andExpect(jsonPath("$.hscId").value(DEFAULT_HSC_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHscConsumptionMaterialReduction() throws Exception {
        // Get the hscConsumptionMaterialReduction
        restHscConsumptionMaterialReductionMockMvc.perform(get("/api/hsc-consumption-material-reductions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHscConsumptionMaterialReduction() throws Exception {
        // Initialize the database
        hscConsumptionMaterialReductionService.save(hscConsumptionMaterialReduction);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockHscConsumptionMaterialReductionSearchRepository);

        int databaseSizeBeforeUpdate = hscConsumptionMaterialReductionRepository.findAll().size();

        // Update the hscConsumptionMaterialReduction
        HscConsumptionMaterialReduction updatedHscConsumptionMaterialReduction = hscConsumptionMaterialReductionRepository.findById(hscConsumptionMaterialReduction.getId()).get();
        // Disconnect from session so that the updates on updatedHscConsumptionMaterialReduction are not directly saved in db
        em.detach(updatedHscConsumptionMaterialReduction);
        updatedHscConsumptionMaterialReduction.setHscId(UPDATED_HSC_ID);

        restHscConsumptionMaterialReductionMockMvc.perform(put("/api/hsc-consumption-material-reductions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHscConsumptionMaterialReduction)))
            .andExpect(status().isOk());

        // Validate the HscConsumptionMaterialReduction in the database
        List<HscConsumptionMaterialReduction> hscConsumptionMaterialReductionList = hscConsumptionMaterialReductionRepository.findAll();
        assertThat(hscConsumptionMaterialReductionList).hasSize(databaseSizeBeforeUpdate);
        HscConsumptionMaterialReduction testHscConsumptionMaterialReduction = hscConsumptionMaterialReductionList.get(hscConsumptionMaterialReductionList.size() - 1);
        assertThat(testHscConsumptionMaterialReduction.getHscId()).isEqualTo(UPDATED_HSC_ID);

        // Validate the HscConsumptionMaterialReduction in Elasticsearch
        verify(mockHscConsumptionMaterialReductionSearchRepository, times(1)).save(testHscConsumptionMaterialReduction);
    }

    @Test
    @Transactional
    public void updateNonExistingHscConsumptionMaterialReduction() throws Exception {
        int databaseSizeBeforeUpdate = hscConsumptionMaterialReductionRepository.findAll().size();

        // Create the HscConsumptionMaterialReduction

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHscConsumptionMaterialReductionMockMvc.perform(put("/api/hsc-consumption-material-reductions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(hscConsumptionMaterialReduction)))
            .andExpect(status().isBadRequest());

        // Validate the HscConsumptionMaterialReduction in the database
        List<HscConsumptionMaterialReduction> hscConsumptionMaterialReductionList = hscConsumptionMaterialReductionRepository.findAll();
        assertThat(hscConsumptionMaterialReductionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HscConsumptionMaterialReduction in Elasticsearch
        verify(mockHscConsumptionMaterialReductionSearchRepository, times(0)).save(hscConsumptionMaterialReduction);
    }

    @Test
    @Transactional
    public void deleteHscConsumptionMaterialReduction() throws Exception {
        // Initialize the database
        hscConsumptionMaterialReductionService.save(hscConsumptionMaterialReduction);

        int databaseSizeBeforeDelete = hscConsumptionMaterialReductionRepository.findAll().size();

        // Delete the hscConsumptionMaterialReduction
        restHscConsumptionMaterialReductionMockMvc.perform(delete("/api/hsc-consumption-material-reductions/{id}", hscConsumptionMaterialReduction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HscConsumptionMaterialReduction> hscConsumptionMaterialReductionList = hscConsumptionMaterialReductionRepository.findAll();
        assertThat(hscConsumptionMaterialReductionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HscConsumptionMaterialReduction in Elasticsearch
        verify(mockHscConsumptionMaterialReductionSearchRepository, times(1)).deleteById(hscConsumptionMaterialReduction.getId());
    }

    @Test
    @Transactional
    public void searchHscConsumptionMaterialReduction() throws Exception {
        // Initialize the database
        hscConsumptionMaterialReductionService.save(hscConsumptionMaterialReduction);
        when(mockHscConsumptionMaterialReductionSearchRepository.search(queryStringQuery("id:" + hscConsumptionMaterialReduction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(hscConsumptionMaterialReduction), PageRequest.of(0, 1), 1));
        // Search the hscConsumptionMaterialReduction
        restHscConsumptionMaterialReductionMockMvc.perform(get("/api/_search/hsc-consumption-material-reductions?query=id:" + hscConsumptionMaterialReduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hscConsumptionMaterialReduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].hscId").value(hasItem(DEFAULT_HSC_ID.intValue())));
    }
}
