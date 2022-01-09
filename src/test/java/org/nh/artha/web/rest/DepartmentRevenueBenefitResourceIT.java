package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.DepartmentRevenueBenefit;
import org.nh.artha.repository.DepartmentRevenueBenefitRepository;
import org.nh.artha.repository.search.DepartmentRevenueBenefitSearchRepository;
import org.nh.artha.service.DepartmentRevenueBenefitService;

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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nh.artha.domain.enumeration.RevenueBenefitType;
/**
 * Integration tests for the {@link DepartmentRevenueBenefitResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DepartmentRevenueBenefitResourceIT {

    private static final RevenueBenefitType DEFAULT_REVENUE_BENEFIT_TYPE = RevenueBenefitType.FIXED;
    private static final RevenueBenefitType UPDATED_REVENUE_BENEFIT_TYPE = RevenueBenefitType.CONTRIBUTION_BASED;

    private static final Float DEFAULT_PAYOUT_PERCENTAGE = 1F;
    private static final Float UPDATED_PAYOUT_PERCENTAGE = 2F;

    private static final BigDecimal DEFAULT_UPPER_LIMIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_UPPER_LIMIT = new BigDecimal(2);

    private static final Integer DEFAULT_STARTING_VERSION = 1;
    private static final Integer UPDATED_STARTING_VERSION = 2;

    private static final Integer DEFAULT_CURRENT_VERSION = 1;
    private static final Integer UPDATED_CURRENT_VERSION = 2;

    @Autowired
    private DepartmentRevenueBenefitRepository departmentRevenueBenefitRepository;

    @Autowired
    private DepartmentRevenueBenefitService departmentRevenueBenefitService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.DepartmentRevenueBenefitSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepartmentRevenueBenefitSearchRepository mockDepartmentRevenueBenefitSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentRevenueBenefitMockMvc;

    private DepartmentRevenueBenefit departmentRevenueBenefit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentRevenueBenefit createEntity(EntityManager em) {
        DepartmentRevenueBenefit departmentRevenueBenefit = new DepartmentRevenueBenefit()
            .revenueBenefitType(DEFAULT_REVENUE_BENEFIT_TYPE)
            .payoutPercentage(DEFAULT_PAYOUT_PERCENTAGE)
            .upperLimit(DEFAULT_UPPER_LIMIT)
            .startingVersion(DEFAULT_STARTING_VERSION)
            .currentVersion(DEFAULT_CURRENT_VERSION);
        return departmentRevenueBenefit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentRevenueBenefit createUpdatedEntity(EntityManager em) {
        DepartmentRevenueBenefit departmentRevenueBenefit = new DepartmentRevenueBenefit()
            .revenueBenefitType(UPDATED_REVENUE_BENEFIT_TYPE)
            .payoutPercentage(UPDATED_PAYOUT_PERCENTAGE)
            .upperLimit(UPDATED_UPPER_LIMIT)
            .startingVersion(UPDATED_STARTING_VERSION)
            .currentVersion(UPDATED_CURRENT_VERSION);
        return departmentRevenueBenefit;
    }

    @BeforeEach
    public void initTest() {
        departmentRevenueBenefit = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartmentRevenueBenefit() throws Exception {
        int databaseSizeBeforeCreate = departmentRevenueBenefitRepository.findAll().size();

        // Create the DepartmentRevenueBenefit
        restDepartmentRevenueBenefitMockMvc.perform(post("/api/department-revenue-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenueBenefit)))
            .andExpect(status().isCreated());

        // Validate the DepartmentRevenueBenefit in the database
        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeCreate + 1);
        DepartmentRevenueBenefit testDepartmentRevenueBenefit = departmentRevenueBenefitList.get(departmentRevenueBenefitList.size() - 1);
        assertThat(testDepartmentRevenueBenefit.getRevenueBenefitType()).isEqualTo(DEFAULT_REVENUE_BENEFIT_TYPE);
        assertThat(testDepartmentRevenueBenefit.getPayoutPercentage()).isEqualTo(DEFAULT_PAYOUT_PERCENTAGE);
        assertThat(testDepartmentRevenueBenefit.getUpperLimit()).isEqualTo(DEFAULT_UPPER_LIMIT);
        assertThat(testDepartmentRevenueBenefit.getStartingVersion()).isEqualTo(DEFAULT_STARTING_VERSION);
        assertThat(testDepartmentRevenueBenefit.getCurrentVersion()).isEqualTo(DEFAULT_CURRENT_VERSION);

        // Validate the DepartmentRevenueBenefit in Elasticsearch
        verify(mockDepartmentRevenueBenefitSearchRepository, times(1)).save(testDepartmentRevenueBenefit);
    }

    @Test
    @Transactional
    public void createDepartmentRevenueBenefitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departmentRevenueBenefitRepository.findAll().size();

        // Create the DepartmentRevenueBenefit with an existing ID
        departmentRevenueBenefit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentRevenueBenefitMockMvc.perform(post("/api/department-revenue-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenueBenefit)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentRevenueBenefit in the database
        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepartmentRevenueBenefit in Elasticsearch
        verify(mockDepartmentRevenueBenefitSearchRepository, times(0)).save(departmentRevenueBenefit);
    }


    @Test
    @Transactional
    public void checkRevenueBenefitTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueBenefitRepository.findAll().size();
        // set the field null
        departmentRevenueBenefit.setRevenueBenefitType(null);

        // Create the DepartmentRevenueBenefit, which fails.

        restDepartmentRevenueBenefitMockMvc.perform(post("/api/department-revenue-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenueBenefit)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPayoutPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueBenefitRepository.findAll().size();
        // set the field null
        departmentRevenueBenefit.setPayoutPercentage(null);

        // Create the DepartmentRevenueBenefit, which fails.

        restDepartmentRevenueBenefitMockMvc.perform(post("/api/department-revenue-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenueBenefit)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartingVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueBenefitRepository.findAll().size();
        // set the field null
        departmentRevenueBenefit.setStartingVersion(null);

        // Create the DepartmentRevenueBenefit, which fails.

        restDepartmentRevenueBenefitMockMvc.perform(post("/api/department-revenue-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenueBenefit)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRevenueBenefitRepository.findAll().size();
        // set the field null
        departmentRevenueBenefit.setCurrentVersion(null);

        // Create the DepartmentRevenueBenefit, which fails.

        restDepartmentRevenueBenefitMockMvc.perform(post("/api/department-revenue-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenueBenefit)))
            .andExpect(status().isBadRequest());

        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepartmentRevenueBenefits() throws Exception {
        // Initialize the database
        departmentRevenueBenefitRepository.saveAndFlush(departmentRevenueBenefit);

        // Get all the departmentRevenueBenefitList
        restDepartmentRevenueBenefitMockMvc.perform(get("/api/department-revenue-benefits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentRevenueBenefit.getId().intValue())))
            .andExpect(jsonPath("$.[*].revenueBenefitType").value(hasItem(DEFAULT_REVENUE_BENEFIT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].payoutPercentage").value(hasItem(DEFAULT_PAYOUT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].upperLimit").value(hasItem(DEFAULT_UPPER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].startingVersion").value(hasItem(DEFAULT_STARTING_VERSION)))
            .andExpect(jsonPath("$.[*].currentVersion").value(hasItem(DEFAULT_CURRENT_VERSION)));
    }
    
    @Test
    @Transactional
    public void getDepartmentRevenueBenefit() throws Exception {
        // Initialize the database
        departmentRevenueBenefitRepository.saveAndFlush(departmentRevenueBenefit);

        // Get the departmentRevenueBenefit
        restDepartmentRevenueBenefitMockMvc.perform(get("/api/department-revenue-benefits/{id}", departmentRevenueBenefit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departmentRevenueBenefit.getId().intValue()))
            .andExpect(jsonPath("$.revenueBenefitType").value(DEFAULT_REVENUE_BENEFIT_TYPE.toString()))
            .andExpect(jsonPath("$.payoutPercentage").value(DEFAULT_PAYOUT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.upperLimit").value(DEFAULT_UPPER_LIMIT.intValue()))
            .andExpect(jsonPath("$.startingVersion").value(DEFAULT_STARTING_VERSION))
            .andExpect(jsonPath("$.currentVersion").value(DEFAULT_CURRENT_VERSION));
    }

    @Test
    @Transactional
    public void getNonExistingDepartmentRevenueBenefit() throws Exception {
        // Get the departmentRevenueBenefit
        restDepartmentRevenueBenefitMockMvc.perform(get("/api/department-revenue-benefits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartmentRevenueBenefit() throws Exception {
        // Initialize the database
        departmentRevenueBenefitService.save(departmentRevenueBenefit);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDepartmentRevenueBenefitSearchRepository);

        int databaseSizeBeforeUpdate = departmentRevenueBenefitRepository.findAll().size();

        // Update the departmentRevenueBenefit
        DepartmentRevenueBenefit updatedDepartmentRevenueBenefit = departmentRevenueBenefitRepository.findById(departmentRevenueBenefit.getId()).get();
        // Disconnect from session so that the updates on updatedDepartmentRevenueBenefit are not directly saved in db
        em.detach(updatedDepartmentRevenueBenefit);
        updatedDepartmentRevenueBenefit
            .revenueBenefitType(UPDATED_REVENUE_BENEFIT_TYPE)
            .payoutPercentage(UPDATED_PAYOUT_PERCENTAGE)
            .upperLimit(UPDATED_UPPER_LIMIT)
            .startingVersion(UPDATED_STARTING_VERSION)
            .currentVersion(UPDATED_CURRENT_VERSION);

        restDepartmentRevenueBenefitMockMvc.perform(put("/api/department-revenue-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDepartmentRevenueBenefit)))
            .andExpect(status().isOk());

        // Validate the DepartmentRevenueBenefit in the database
        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeUpdate);
        DepartmentRevenueBenefit testDepartmentRevenueBenefit = departmentRevenueBenefitList.get(departmentRevenueBenefitList.size() - 1);
        assertThat(testDepartmentRevenueBenefit.getRevenueBenefitType()).isEqualTo(UPDATED_REVENUE_BENEFIT_TYPE);
        assertThat(testDepartmentRevenueBenefit.getPayoutPercentage()).isEqualTo(UPDATED_PAYOUT_PERCENTAGE);
        assertThat(testDepartmentRevenueBenefit.getUpperLimit()).isEqualTo(UPDATED_UPPER_LIMIT);
        assertThat(testDepartmentRevenueBenefit.getStartingVersion()).isEqualTo(UPDATED_STARTING_VERSION);
        assertThat(testDepartmentRevenueBenefit.getCurrentVersion()).isEqualTo(UPDATED_CURRENT_VERSION);

        // Validate the DepartmentRevenueBenefit in Elasticsearch
        verify(mockDepartmentRevenueBenefitSearchRepository, times(1)).save(testDepartmentRevenueBenefit);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartmentRevenueBenefit() throws Exception {
        int databaseSizeBeforeUpdate = departmentRevenueBenefitRepository.findAll().size();

        // Create the DepartmentRevenueBenefit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentRevenueBenefitMockMvc.perform(put("/api/department-revenue-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentRevenueBenefit)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentRevenueBenefit in the database
        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentRevenueBenefit in Elasticsearch
        verify(mockDepartmentRevenueBenefitSearchRepository, times(0)).save(departmentRevenueBenefit);
    }

    @Test
    @Transactional
    public void deleteDepartmentRevenueBenefit() throws Exception {
        // Initialize the database
        departmentRevenueBenefitService.save(departmentRevenueBenefit);

        int databaseSizeBeforeDelete = departmentRevenueBenefitRepository.findAll().size();

        // Delete the departmentRevenueBenefit
        restDepartmentRevenueBenefitMockMvc.perform(delete("/api/department-revenue-benefits/{id}", departmentRevenueBenefit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepartmentRevenueBenefit> departmentRevenueBenefitList = departmentRevenueBenefitRepository.findAll();
        assertThat(departmentRevenueBenefitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepartmentRevenueBenefit in Elasticsearch
        verify(mockDepartmentRevenueBenefitSearchRepository, times(1)).deleteById(departmentRevenueBenefit.getId());
    }

    @Test
    @Transactional
    public void searchDepartmentRevenueBenefit() throws Exception {
        // Initialize the database
        departmentRevenueBenefitService.save(departmentRevenueBenefit);
        when(mockDepartmentRevenueBenefitSearchRepository.search(queryStringQuery("id:" + departmentRevenueBenefit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(departmentRevenueBenefit), PageRequest.of(0, 1), 1));
        // Search the departmentRevenueBenefit
        restDepartmentRevenueBenefitMockMvc.perform(get("/api/_search/department-revenue-benefits?query=id:" + departmentRevenueBenefit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentRevenueBenefit.getId().intValue())))
            .andExpect(jsonPath("$.[*].revenueBenefitType").value(hasItem(DEFAULT_REVENUE_BENEFIT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].payoutPercentage").value(hasItem(DEFAULT_PAYOUT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].upperLimit").value(hasItem(DEFAULT_UPPER_LIMIT.intValue())))
            .andExpect(jsonPath("$.[*].startingVersion").value(hasItem(DEFAULT_STARTING_VERSION)))
            .andExpect(jsonPath("$.[*].currentVersion").value(hasItem(DEFAULT_CURRENT_VERSION)));
    }
}
