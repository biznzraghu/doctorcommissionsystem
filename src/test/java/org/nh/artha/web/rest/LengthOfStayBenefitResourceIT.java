package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.LengthOfStayBenefit;
import org.nh.artha.repository.LengthOfStayBenefitRepository;
import org.nh.artha.repository.search.LengthOfStayBenefitSearchRepository;
import org.nh.artha.service.LengthOfStayBenefitService;

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

import org.nh.artha.domain.enumeration.LengthOfStayCriteria;
/**
 * Integration tests for the {@link LengthOfStayBenefitResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LengthOfStayBenefitResourceIT {

    private static final LengthOfStayCriteria DEFAULT_LENGTH_OF_STAY_CRITERIA = LengthOfStayCriteria.FIXED;
    private static final LengthOfStayCriteria UPDATED_LENGTH_OF_STAY_CRITERIA = LengthOfStayCriteria.AVERAGE;

    private static final Integer DEFAULT_DAYS = 1;
    private static final Integer UPDATED_DAYS = 2;

    private static final BigDecimal DEFAULT_PER_DAY_PAYOUT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PER_DAY_PAYOUT_AMOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_STARTING_VERSION = 1;
    private static final Integer UPDATED_STARTING_VERSION = 2;

    private static final Integer DEFAULT_CURRENT_VERSION = 1;
    private static final Integer UPDATED_CURRENT_VERSION = 2;

    @Autowired
    private LengthOfStayBenefitRepository lengthOfStayBenefitRepository;

    @Autowired
    private LengthOfStayBenefitService lengthOfStayBenefitService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.LengthOfStayBenefitSearchRepositoryMockConfiguration
     */
    @Autowired
    private LengthOfStayBenefitSearchRepository mockLengthOfStayBenefitSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLengthOfStayBenefitMockMvc;

    private LengthOfStayBenefit lengthOfStayBenefit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LengthOfStayBenefit createEntity(EntityManager em) {
        LengthOfStayBenefit lengthOfStayBenefit = new LengthOfStayBenefit()
            .lengthOfStayCriteria(DEFAULT_LENGTH_OF_STAY_CRITERIA)
            .days(DEFAULT_DAYS)
            .perDayPayoutAmount(DEFAULT_PER_DAY_PAYOUT_AMOUNT)
            .startingVersion(DEFAULT_STARTING_VERSION)
            .currentVersion(DEFAULT_CURRENT_VERSION);
        return lengthOfStayBenefit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LengthOfStayBenefit createUpdatedEntity(EntityManager em) {
        LengthOfStayBenefit lengthOfStayBenefit = new LengthOfStayBenefit()
            .lengthOfStayCriteria(UPDATED_LENGTH_OF_STAY_CRITERIA)
            .days(UPDATED_DAYS)
            .perDayPayoutAmount(UPDATED_PER_DAY_PAYOUT_AMOUNT)
            .startingVersion(UPDATED_STARTING_VERSION)
            .currentVersion(UPDATED_CURRENT_VERSION);
        return lengthOfStayBenefit;
    }

    @BeforeEach
    public void initTest() {
        lengthOfStayBenefit = createEntity(em);
    }

    @Test
    @Transactional
    public void createLengthOfStayBenefit() throws Exception {
        int databaseSizeBeforeCreate = lengthOfStayBenefitRepository.findAll().size();

        // Create the LengthOfStayBenefit
        restLengthOfStayBenefitMockMvc.perform(post("/api/length-of-stay-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lengthOfStayBenefit)))
            .andExpect(status().isCreated());

        // Validate the LengthOfStayBenefit in the database
        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.findAll();
        assertThat(lengthOfStayBenefitList).hasSize(databaseSizeBeforeCreate + 1);
        LengthOfStayBenefit testLengthOfStayBenefit = lengthOfStayBenefitList.get(lengthOfStayBenefitList.size() - 1);
        assertThat(testLengthOfStayBenefit.getLengthOfStayCriteria()).isEqualTo(DEFAULT_LENGTH_OF_STAY_CRITERIA);
        assertThat(testLengthOfStayBenefit.getDays()).isEqualTo(DEFAULT_DAYS);
        assertThat(testLengthOfStayBenefit.getPerDayPayoutAmount()).isEqualTo(DEFAULT_PER_DAY_PAYOUT_AMOUNT);
        assertThat(testLengthOfStayBenefit.getStartingVersion()).isEqualTo(DEFAULT_STARTING_VERSION);
        assertThat(testLengthOfStayBenefit.getCurrentVersion()).isEqualTo(DEFAULT_CURRENT_VERSION);

        // Validate the LengthOfStayBenefit in Elasticsearch
        verify(mockLengthOfStayBenefitSearchRepository, times(1)).save(testLengthOfStayBenefit);
    }

    @Test
    @Transactional
    public void createLengthOfStayBenefitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lengthOfStayBenefitRepository.findAll().size();

        // Create the LengthOfStayBenefit with an existing ID
        lengthOfStayBenefit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLengthOfStayBenefitMockMvc.perform(post("/api/length-of-stay-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lengthOfStayBenefit)))
            .andExpect(status().isBadRequest());

        // Validate the LengthOfStayBenefit in the database
        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.findAll();
        assertThat(lengthOfStayBenefitList).hasSize(databaseSizeBeforeCreate);

        // Validate the LengthOfStayBenefit in Elasticsearch
        verify(mockLengthOfStayBenefitSearchRepository, times(0)).save(lengthOfStayBenefit);
    }


    @Test
    @Transactional
    public void checkDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = lengthOfStayBenefitRepository.findAll().size();
        // set the field null
        lengthOfStayBenefit.setDays(null);

        // Create the LengthOfStayBenefit, which fails.

        restLengthOfStayBenefitMockMvc.perform(post("/api/length-of-stay-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lengthOfStayBenefit)))
            .andExpect(status().isBadRequest());

        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.findAll();
        assertThat(lengthOfStayBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartingVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = lengthOfStayBenefitRepository.findAll().size();
        // set the field null
        lengthOfStayBenefit.setStartingVersion(null);

        // Create the LengthOfStayBenefit, which fails.

        restLengthOfStayBenefitMockMvc.perform(post("/api/length-of-stay-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lengthOfStayBenefit)))
            .andExpect(status().isBadRequest());

        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.findAll();
        assertThat(lengthOfStayBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = lengthOfStayBenefitRepository.findAll().size();
        // set the field null
        lengthOfStayBenefit.setCurrentVersion(null);

        // Create the LengthOfStayBenefit, which fails.

        restLengthOfStayBenefitMockMvc.perform(post("/api/length-of-stay-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lengthOfStayBenefit)))
            .andExpect(status().isBadRequest());

        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.findAll();
        assertThat(lengthOfStayBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLengthOfStayBenefits() throws Exception {
        // Initialize the database
        lengthOfStayBenefitRepository.saveAndFlush(lengthOfStayBenefit);

        // Get all the lengthOfStayBenefitList
        restLengthOfStayBenefitMockMvc.perform(get("/api/length-of-stay-benefits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lengthOfStayBenefit.getId().intValue())))
            .andExpect(jsonPath("$.[*].lengthOfStayCriteria").value(hasItem(DEFAULT_LENGTH_OF_STAY_CRITERIA.toString())))
            .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS)))
            .andExpect(jsonPath("$.[*].perDayPayoutAmount").value(hasItem(DEFAULT_PER_DAY_PAYOUT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].startingVersion").value(hasItem(DEFAULT_STARTING_VERSION)))
            .andExpect(jsonPath("$.[*].currentVersion").value(hasItem(DEFAULT_CURRENT_VERSION)));
    }
    
    @Test
    @Transactional
    public void getLengthOfStayBenefit() throws Exception {
        // Initialize the database
        lengthOfStayBenefitRepository.saveAndFlush(lengthOfStayBenefit);

        // Get the lengthOfStayBenefit
        restLengthOfStayBenefitMockMvc.perform(get("/api/length-of-stay-benefits/{id}", lengthOfStayBenefit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lengthOfStayBenefit.getId().intValue()))
            .andExpect(jsonPath("$.lengthOfStayCriteria").value(DEFAULT_LENGTH_OF_STAY_CRITERIA.toString()))
            .andExpect(jsonPath("$.days").value(DEFAULT_DAYS))
            .andExpect(jsonPath("$.perDayPayoutAmount").value(DEFAULT_PER_DAY_PAYOUT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.startingVersion").value(DEFAULT_STARTING_VERSION))
            .andExpect(jsonPath("$.currentVersion").value(DEFAULT_CURRENT_VERSION));
    }

    @Test
    @Transactional
    public void getNonExistingLengthOfStayBenefit() throws Exception {
        // Get the lengthOfStayBenefit
        restLengthOfStayBenefitMockMvc.perform(get("/api/length-of-stay-benefits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLengthOfStayBenefit() throws Exception {
        // Initialize the database
        lengthOfStayBenefitService.save(lengthOfStayBenefit);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockLengthOfStayBenefitSearchRepository);

        int databaseSizeBeforeUpdate = lengthOfStayBenefitRepository.findAll().size();

        // Update the lengthOfStayBenefit
        LengthOfStayBenefit updatedLengthOfStayBenefit = lengthOfStayBenefitRepository.findById(lengthOfStayBenefit.getId()).get();
        // Disconnect from session so that the updates on updatedLengthOfStayBenefit are not directly saved in db
        em.detach(updatedLengthOfStayBenefit);
        updatedLengthOfStayBenefit
            .lengthOfStayCriteria(UPDATED_LENGTH_OF_STAY_CRITERIA)
            .days(UPDATED_DAYS)
            .perDayPayoutAmount(UPDATED_PER_DAY_PAYOUT_AMOUNT)
            .startingVersion(UPDATED_STARTING_VERSION)
            .currentVersion(UPDATED_CURRENT_VERSION);

        restLengthOfStayBenefitMockMvc.perform(put("/api/length-of-stay-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLengthOfStayBenefit)))
            .andExpect(status().isOk());

        // Validate the LengthOfStayBenefit in the database
        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.findAll();
        assertThat(lengthOfStayBenefitList).hasSize(databaseSizeBeforeUpdate);
        LengthOfStayBenefit testLengthOfStayBenefit = lengthOfStayBenefitList.get(lengthOfStayBenefitList.size() - 1);
        assertThat(testLengthOfStayBenefit.getLengthOfStayCriteria()).isEqualTo(UPDATED_LENGTH_OF_STAY_CRITERIA);
        assertThat(testLengthOfStayBenefit.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testLengthOfStayBenefit.getPerDayPayoutAmount()).isEqualTo(UPDATED_PER_DAY_PAYOUT_AMOUNT);
        assertThat(testLengthOfStayBenefit.getStartingVersion()).isEqualTo(UPDATED_STARTING_VERSION);
        assertThat(testLengthOfStayBenefit.getCurrentVersion()).isEqualTo(UPDATED_CURRENT_VERSION);

        // Validate the LengthOfStayBenefit in Elasticsearch
        verify(mockLengthOfStayBenefitSearchRepository, times(1)).save(testLengthOfStayBenefit);
    }

    @Test
    @Transactional
    public void updateNonExistingLengthOfStayBenefit() throws Exception {
        int databaseSizeBeforeUpdate = lengthOfStayBenefitRepository.findAll().size();

        // Create the LengthOfStayBenefit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLengthOfStayBenefitMockMvc.perform(put("/api/length-of-stay-benefits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lengthOfStayBenefit)))
            .andExpect(status().isBadRequest());

        // Validate the LengthOfStayBenefit in the database
        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.findAll();
        assertThat(lengthOfStayBenefitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LengthOfStayBenefit in Elasticsearch
        verify(mockLengthOfStayBenefitSearchRepository, times(0)).save(lengthOfStayBenefit);
    }

    @Test
    @Transactional
    public void deleteLengthOfStayBenefit() throws Exception {
        // Initialize the database
        lengthOfStayBenefitService.save(lengthOfStayBenefit);

        int databaseSizeBeforeDelete = lengthOfStayBenefitRepository.findAll().size();

        // Delete the lengthOfStayBenefit
        restLengthOfStayBenefitMockMvc.perform(delete("/api/length-of-stay-benefits/{id}", lengthOfStayBenefit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LengthOfStayBenefit> lengthOfStayBenefitList = lengthOfStayBenefitRepository.findAll();
        assertThat(lengthOfStayBenefitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LengthOfStayBenefit in Elasticsearch
        verify(mockLengthOfStayBenefitSearchRepository, times(1)).deleteById(lengthOfStayBenefit.getId());
    }

    @Test
    @Transactional
    public void searchLengthOfStayBenefit() throws Exception {
        // Initialize the database
        lengthOfStayBenefitService.save(lengthOfStayBenefit);
        when(mockLengthOfStayBenefitSearchRepository.search(queryStringQuery("id:" + lengthOfStayBenefit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(lengthOfStayBenefit), PageRequest.of(0, 1), 1));
        // Search the lengthOfStayBenefit
        restLengthOfStayBenefitMockMvc.perform(get("/api/_search/length-of-stay-benefits?query=id:" + lengthOfStayBenefit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lengthOfStayBenefit.getId().intValue())))
            .andExpect(jsonPath("$.[*].lengthOfStayCriteria").value(hasItem(DEFAULT_LENGTH_OF_STAY_CRITERIA.toString())))
            .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS)))
            .andExpect(jsonPath("$.[*].perDayPayoutAmount").value(hasItem(DEFAULT_PER_DAY_PAYOUT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].startingVersion").value(hasItem(DEFAULT_STARTING_VERSION)))
            .andExpect(jsonPath("$.[*].currentVersion").value(hasItem(DEFAULT_CURRENT_VERSION)));
    }
}
