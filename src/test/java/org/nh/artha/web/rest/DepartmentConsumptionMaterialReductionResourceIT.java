package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.DepartmentConsumptionMaterialReduction;
import org.nh.artha.repository.DepartmentConsumptionMaterialReductionRepository;
import org.nh.artha.repository.search.DepartmentConsumptionMaterialReductionSearchRepository;
import org.nh.artha.service.DepartmentConsumptionMaterialReductionService;

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
 * Integration tests for the {@link DepartmentConsumptionMaterialReductionResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DepartmentConsumptionMaterialReductionResourceIT {

    @Autowired
    private DepartmentConsumptionMaterialReductionRepository departmentConsumptionMaterialReductionRepository;

    @Autowired
    private DepartmentConsumptionMaterialReductionService departmentConsumptionMaterialReductionService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.DepartmentConsumptionMaterialReductionSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepartmentConsumptionMaterialReductionSearchRepository mockDepartmentConsumptionMaterialReductionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentConsumptionMaterialReductionMockMvc;

    private DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentConsumptionMaterialReduction createEntity(EntityManager em) {
        DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction = new DepartmentConsumptionMaterialReduction();
        return departmentConsumptionMaterialReduction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentConsumptionMaterialReduction createUpdatedEntity(EntityManager em) {
        DepartmentConsumptionMaterialReduction departmentConsumptionMaterialReduction = new DepartmentConsumptionMaterialReduction();
        return departmentConsumptionMaterialReduction;
    }

    @BeforeEach
    public void initTest() {
        departmentConsumptionMaterialReduction = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartmentConsumptionMaterialReduction() throws Exception {
        int databaseSizeBeforeCreate = departmentConsumptionMaterialReductionRepository.findAll().size();

        // Create the DepartmentConsumptionMaterialReduction
        restDepartmentConsumptionMaterialReductionMockMvc.perform(post("/api/department-consumption-material-reductions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentConsumptionMaterialReduction)))
            .andExpect(status().isCreated());

        // Validate the DepartmentConsumptionMaterialReduction in the database
        List<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductionList = departmentConsumptionMaterialReductionRepository.findAll();
        assertThat(departmentConsumptionMaterialReductionList).hasSize(databaseSizeBeforeCreate + 1);
        DepartmentConsumptionMaterialReduction testDepartmentConsumptionMaterialReduction = departmentConsumptionMaterialReductionList.get(departmentConsumptionMaterialReductionList.size() - 1);

        // Validate the DepartmentConsumptionMaterialReduction in Elasticsearch
        verify(mockDepartmentConsumptionMaterialReductionSearchRepository, times(1)).save(testDepartmentConsumptionMaterialReduction);
    }

    @Test
    @Transactional
    public void createDepartmentConsumptionMaterialReductionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departmentConsumptionMaterialReductionRepository.findAll().size();

        // Create the DepartmentConsumptionMaterialReduction with an existing ID
        departmentConsumptionMaterialReduction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentConsumptionMaterialReductionMockMvc.perform(post("/api/department-consumption-material-reductions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentConsumptionMaterialReduction)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentConsumptionMaterialReduction in the database
        List<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductionList = departmentConsumptionMaterialReductionRepository.findAll();
        assertThat(departmentConsumptionMaterialReductionList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepartmentConsumptionMaterialReduction in Elasticsearch
        verify(mockDepartmentConsumptionMaterialReductionSearchRepository, times(0)).save(departmentConsumptionMaterialReduction);
    }


    @Test
    @Transactional
    public void getAllDepartmentConsumptionMaterialReductions() throws Exception {
        // Initialize the database
        departmentConsumptionMaterialReductionRepository.saveAndFlush(departmentConsumptionMaterialReduction);

        // Get all the departmentConsumptionMaterialReductionList
        restDepartmentConsumptionMaterialReductionMockMvc.perform(get("/api/department-consumption-material-reductions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentConsumptionMaterialReduction.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getDepartmentConsumptionMaterialReduction() throws Exception {
        // Initialize the database
        departmentConsumptionMaterialReductionRepository.saveAndFlush(departmentConsumptionMaterialReduction);

        // Get the departmentConsumptionMaterialReduction
        restDepartmentConsumptionMaterialReductionMockMvc.perform(get("/api/department-consumption-material-reductions/{id}", departmentConsumptionMaterialReduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departmentConsumptionMaterialReduction.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDepartmentConsumptionMaterialReduction() throws Exception {
        // Get the departmentConsumptionMaterialReduction
        restDepartmentConsumptionMaterialReductionMockMvc.perform(get("/api/department-consumption-material-reductions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartmentConsumptionMaterialReduction() throws Exception {
        // Initialize the database
        departmentConsumptionMaterialReductionService.save(departmentConsumptionMaterialReduction);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDepartmentConsumptionMaterialReductionSearchRepository);

        int databaseSizeBeforeUpdate = departmentConsumptionMaterialReductionRepository.findAll().size();

        // Update the departmentConsumptionMaterialReduction
        DepartmentConsumptionMaterialReduction updatedDepartmentConsumptionMaterialReduction = departmentConsumptionMaterialReductionRepository.findById(departmentConsumptionMaterialReduction.getId()).get();
        // Disconnect from session so that the updates on updatedDepartmentConsumptionMaterialReduction are not directly saved in db
        em.detach(updatedDepartmentConsumptionMaterialReduction);

        restDepartmentConsumptionMaterialReductionMockMvc.perform(put("/api/department-consumption-material-reductions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDepartmentConsumptionMaterialReduction)))
            .andExpect(status().isOk());

        // Validate the DepartmentConsumptionMaterialReduction in the database
        List<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductionList = departmentConsumptionMaterialReductionRepository.findAll();
        assertThat(departmentConsumptionMaterialReductionList).hasSize(databaseSizeBeforeUpdate);
        DepartmentConsumptionMaterialReduction testDepartmentConsumptionMaterialReduction = departmentConsumptionMaterialReductionList.get(departmentConsumptionMaterialReductionList.size() - 1);

        // Validate the DepartmentConsumptionMaterialReduction in Elasticsearch
        verify(mockDepartmentConsumptionMaterialReductionSearchRepository, times(1)).save(testDepartmentConsumptionMaterialReduction);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartmentConsumptionMaterialReduction() throws Exception {
        int databaseSizeBeforeUpdate = departmentConsumptionMaterialReductionRepository.findAll().size();

        // Create the DepartmentConsumptionMaterialReduction

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentConsumptionMaterialReductionMockMvc.perform(put("/api/department-consumption-material-reductions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentConsumptionMaterialReduction)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentConsumptionMaterialReduction in the database
        List<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductionList = departmentConsumptionMaterialReductionRepository.findAll();
        assertThat(departmentConsumptionMaterialReductionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentConsumptionMaterialReduction in Elasticsearch
        verify(mockDepartmentConsumptionMaterialReductionSearchRepository, times(0)).save(departmentConsumptionMaterialReduction);
    }

    @Test
    @Transactional
    public void deleteDepartmentConsumptionMaterialReduction() throws Exception {
        // Initialize the database
        departmentConsumptionMaterialReductionService.save(departmentConsumptionMaterialReduction);

        int databaseSizeBeforeDelete = departmentConsumptionMaterialReductionRepository.findAll().size();

        // Delete the departmentConsumptionMaterialReduction
        restDepartmentConsumptionMaterialReductionMockMvc.perform(delete("/api/department-consumption-material-reductions/{id}", departmentConsumptionMaterialReduction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepartmentConsumptionMaterialReduction> departmentConsumptionMaterialReductionList = departmentConsumptionMaterialReductionRepository.findAll();
        assertThat(departmentConsumptionMaterialReductionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepartmentConsumptionMaterialReduction in Elasticsearch
        verify(mockDepartmentConsumptionMaterialReductionSearchRepository, times(1)).deleteById(departmentConsumptionMaterialReduction.getId());
    }

    @Test
    @Transactional
    public void searchDepartmentConsumptionMaterialReduction() throws Exception {
        // Initialize the database
        departmentConsumptionMaterialReductionService.save(departmentConsumptionMaterialReduction);
        when(mockDepartmentConsumptionMaterialReductionSearchRepository.search(queryStringQuery("id:" + departmentConsumptionMaterialReduction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(departmentConsumptionMaterialReduction), PageRequest.of(0, 1), 1));
        // Search the departmentConsumptionMaterialReduction
        restDepartmentConsumptionMaterialReductionMockMvc.perform(get("/api/_search/department-consumption-material-reductions?query=id:" + departmentConsumptionMaterialReduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentConsumptionMaterialReduction.getId().intValue())));
    }
}
