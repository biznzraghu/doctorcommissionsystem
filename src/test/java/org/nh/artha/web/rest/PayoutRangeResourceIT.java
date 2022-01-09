package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.PayoutRange;
import org.nh.artha.repository.PayoutRangeRepository;
import org.nh.artha.repository.search.PayoutRangeSearchRepository;
import org.nh.artha.service.PayoutRangeService;

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

/**
 * Integration tests for the {@link PayoutRangeResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PayoutRangeResourceIT {

    private static final BigDecimal DEFAULT_FROM_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_FROM_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TO_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TO_AMOUNT = new BigDecimal(2);

    private static final Float DEFAULT_PERCENTAGE = 1F;
    private static final Float UPDATED_PERCENTAGE = 2F;

    @Autowired
    private PayoutRangeRepository payoutRangeRepository;

    @Autowired
    private PayoutRangeService payoutRangeService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PayoutRangeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PayoutRangeSearchRepository mockPayoutRangeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayoutRangeMockMvc;

    private PayoutRange payoutRange;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutRange createEntity(EntityManager em) {
        PayoutRange payoutRange = new PayoutRange();
        payoutRange.setFromAmount(DEFAULT_FROM_AMOUNT);
        payoutRange.setToAmount(DEFAULT_TO_AMOUNT);
        payoutRange.setPercentage(DEFAULT_PERCENTAGE);
        return payoutRange;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutRange createUpdatedEntity(EntityManager em) {
        PayoutRange payoutRange = new PayoutRange();
        payoutRange.setFromAmount(UPDATED_FROM_AMOUNT);
        payoutRange.setToAmount(UPDATED_TO_AMOUNT);
        payoutRange.setPercentage(UPDATED_PERCENTAGE);
        return payoutRange;
    }

    @BeforeEach
    public void initTest() {
        payoutRange = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayoutRange() throws Exception {
        int databaseSizeBeforeCreate = payoutRangeRepository.findAll().size();

        // Create the PayoutRange
        restPayoutRangeMockMvc.perform(post("/api/payout-ranges")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutRange)))
            .andExpect(status().isCreated());

        // Validate the PayoutRange in the database
        List<PayoutRange> payoutRangeList = payoutRangeRepository.findAll();
        assertThat(payoutRangeList).hasSize(databaseSizeBeforeCreate + 1);
        PayoutRange testPayoutRange = payoutRangeList.get(payoutRangeList.size() - 1);
        assertThat(testPayoutRange.getFromAmount()).isEqualTo(DEFAULT_FROM_AMOUNT);
        assertThat(testPayoutRange.getToAmount()).isEqualTo(DEFAULT_TO_AMOUNT);
        assertThat(testPayoutRange.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);

        // Validate the PayoutRange in Elasticsearch
        verify(mockPayoutRangeSearchRepository, times(1)).save(testPayoutRange);
    }

    @Test
    @Transactional
    public void createPayoutRangeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payoutRangeRepository.findAll().size();

        // Create the PayoutRange with an existing ID
        payoutRange.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayoutRangeMockMvc.perform(post("/api/payout-ranges")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutRange)))
            .andExpect(status().isBadRequest());

        // Validate the PayoutRange in the database
        List<PayoutRange> payoutRangeList = payoutRangeRepository.findAll();
        assertThat(payoutRangeList).hasSize(databaseSizeBeforeCreate);

        // Validate the PayoutRange in Elasticsearch
        verify(mockPayoutRangeSearchRepository, times(0)).save(payoutRange);
    }


    @Test
    @Transactional
    public void getAllPayoutRanges() throws Exception {
        // Initialize the database
        payoutRangeRepository.saveAndFlush(payoutRange);

        // Get all the payoutRangeList
        restPayoutRangeMockMvc.perform(get("/api/payout-ranges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutRange.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromAmount").value(hasItem(DEFAULT_FROM_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].toAmount").value(hasItem(DEFAULT_TO_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getPayoutRange() throws Exception {
        // Initialize the database
        payoutRangeRepository.saveAndFlush(payoutRange);

        // Get the payoutRange
        restPayoutRangeMockMvc.perform(get("/api/payout-ranges/{id}", payoutRange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payoutRange.getId().intValue()))
            .andExpect(jsonPath("$.fromAmount").value(DEFAULT_FROM_AMOUNT.intValue()))
            .andExpect(jsonPath("$.toAmount").value(DEFAULT_TO_AMOUNT.intValue()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPayoutRange() throws Exception {
        // Get the payoutRange
        restPayoutRangeMockMvc.perform(get("/api/payout-ranges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayoutRange() throws Exception {
        // Initialize the database
        payoutRangeService.save(payoutRange);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPayoutRangeSearchRepository);

        int databaseSizeBeforeUpdate = payoutRangeRepository.findAll().size();

        // Update the payoutRange
        PayoutRange updatedPayoutRange = payoutRangeRepository.findById(payoutRange.getId()).get();
        // Disconnect from session so that the updates on updatedPayoutRange are not directly saved in db
        em.detach(updatedPayoutRange);
        updatedPayoutRange.setFromAmount(UPDATED_FROM_AMOUNT);
        updatedPayoutRange.setToAmount(UPDATED_TO_AMOUNT);
        updatedPayoutRange.setPercentage(UPDATED_PERCENTAGE);

        restPayoutRangeMockMvc.perform(put("/api/payout-ranges")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayoutRange)))
            .andExpect(status().isOk());

        // Validate the PayoutRange in the database
        List<PayoutRange> payoutRangeList = payoutRangeRepository.findAll();
        assertThat(payoutRangeList).hasSize(databaseSizeBeforeUpdate);
        PayoutRange testPayoutRange = payoutRangeList.get(payoutRangeList.size() - 1);
        assertThat(testPayoutRange.getFromAmount()).isEqualTo(UPDATED_FROM_AMOUNT);
        assertThat(testPayoutRange.getToAmount()).isEqualTo(UPDATED_TO_AMOUNT);
        assertThat(testPayoutRange.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);

        // Validate the PayoutRange in Elasticsearch
        verify(mockPayoutRangeSearchRepository, times(1)).save(testPayoutRange);
    }

    @Test
    @Transactional
    public void updateNonExistingPayoutRange() throws Exception {
        int databaseSizeBeforeUpdate = payoutRangeRepository.findAll().size();

        // Create the PayoutRange

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayoutRangeMockMvc.perform(put("/api/payout-ranges")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutRange)))
            .andExpect(status().isBadRequest());

        // Validate the PayoutRange in the database
        List<PayoutRange> payoutRangeList = payoutRangeRepository.findAll();
        assertThat(payoutRangeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PayoutRange in Elasticsearch
        verify(mockPayoutRangeSearchRepository, times(0)).save(payoutRange);
    }

    @Test
    @Transactional
    public void deletePayoutRange() throws Exception {
        // Initialize the database
        payoutRangeService.save(payoutRange);

        int databaseSizeBeforeDelete = payoutRangeRepository.findAll().size();

        // Delete the payoutRange
        restPayoutRangeMockMvc.perform(delete("/api/payout-ranges/{id}", payoutRange.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayoutRange> payoutRangeList = payoutRangeRepository.findAll();
        assertThat(payoutRangeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PayoutRange in Elasticsearch
        verify(mockPayoutRangeSearchRepository, times(1)).deleteById(payoutRange.getId());
    }

    @Test
    @Transactional
    public void searchPayoutRange() throws Exception {
        // Initialize the database
        payoutRangeService.save(payoutRange);
        when(mockPayoutRangeSearchRepository.search(queryStringQuery("id:" + payoutRange.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payoutRange), PageRequest.of(0, 1), 1));
        // Search the payoutRange
        restPayoutRangeMockMvc.perform(get("/api/_search/payout-ranges?query=id:" + payoutRange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutRange.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromAmount").value(hasItem(DEFAULT_FROM_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].toAmount").value(hasItem(DEFAULT_TO_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())));
    }
}
