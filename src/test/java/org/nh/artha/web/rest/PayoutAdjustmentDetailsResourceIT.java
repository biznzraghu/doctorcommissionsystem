package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.PayoutAdjustmentDetails;
import org.nh.artha.repository.PayoutAdjustmentDetailsRepository;
import org.nh.artha.repository.search.PayoutAdjustmentDetailsSearchRepository;
import org.nh.artha.service.PayoutAdjustmentDetailsService;

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
 * Integration tests for the {@link PayoutAdjustmentDetailsResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PayoutAdjustmentDetailsResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    private static final String DEFAULT_SIGN = "AAAAAAAAAA";
    private static final String UPDATED_SIGN = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Long DEFAULT_CONTRA_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_CONTRA_EMPLOYEE_ID = 2L;

    @Autowired
    private PayoutAdjustmentDetailsRepository payoutAdjustmentDetailsRepository;

    @Autowired
    private PayoutAdjustmentDetailsService payoutAdjustmentDetailsService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PayoutAdjustmentDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private PayoutAdjustmentDetailsSearchRepository mockPayoutAdjustmentDetailsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayoutAdjustmentDetailsMockMvc;

    private PayoutAdjustmentDetails payoutAdjustmentDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutAdjustmentDetails createEntity(EntityManager em) {
        PayoutAdjustmentDetails payoutAdjustmentDetails = new PayoutAdjustmentDetails();
        payoutAdjustmentDetails.setDescription(DEFAULT_DESCRIPTION);
        payoutAdjustmentDetails.setYear(DEFAULT_YEAR);
        payoutAdjustmentDetails.setMonth(DEFAULT_MONTH);
        payoutAdjustmentDetails.setSign(DEFAULT_SIGN);
        payoutAdjustmentDetails.setAmount(DEFAULT_AMOUNT);
        payoutAdjustmentDetails.setContraEmployeeId(DEFAULT_CONTRA_EMPLOYEE_ID);
        return payoutAdjustmentDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutAdjustmentDetails createUpdatedEntity(EntityManager em) {
        PayoutAdjustmentDetails payoutAdjustmentDetails = new PayoutAdjustmentDetails();
        payoutAdjustmentDetails.setDescription(UPDATED_DESCRIPTION);
        payoutAdjustmentDetails.setYear(UPDATED_YEAR);
        payoutAdjustmentDetails.setMonth(UPDATED_MONTH);
        payoutAdjustmentDetails.setSign(UPDATED_SIGN);
        payoutAdjustmentDetails.setAmount(UPDATED_AMOUNT);
        payoutAdjustmentDetails.setContraEmployeeId(UPDATED_CONTRA_EMPLOYEE_ID);
        return payoutAdjustmentDetails;
    }

    @BeforeEach
    public void initTest() {
        payoutAdjustmentDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayoutAdjustmentDetails() throws Exception {
        int databaseSizeBeforeCreate = payoutAdjustmentDetailsRepository.findAll().size();

        // Create the PayoutAdjustmentDetails
        restPayoutAdjustmentDetailsMockMvc.perform(post("/api/payout-adjustment-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutAdjustmentDetails)))
            .andExpect(status().isCreated());

        // Validate the PayoutAdjustmentDetails in the database
        List<PayoutAdjustmentDetails> payoutAdjustmentDetailsList = payoutAdjustmentDetailsRepository.findAll();
        assertThat(payoutAdjustmentDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PayoutAdjustmentDetails testPayoutAdjustmentDetails = payoutAdjustmentDetailsList.get(payoutAdjustmentDetailsList.size() - 1);
        assertThat(testPayoutAdjustmentDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPayoutAdjustmentDetails.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPayoutAdjustmentDetails.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testPayoutAdjustmentDetails.getSign()).isEqualTo(DEFAULT_SIGN);
        assertThat(testPayoutAdjustmentDetails.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayoutAdjustmentDetails.getContraEmployeeId()).isEqualTo(DEFAULT_CONTRA_EMPLOYEE_ID);

        // Validate the PayoutAdjustmentDetails in Elasticsearch
        verify(mockPayoutAdjustmentDetailsSearchRepository, times(1)).save(testPayoutAdjustmentDetails);
    }

    @Test
    @Transactional
    public void createPayoutAdjustmentDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payoutAdjustmentDetailsRepository.findAll().size();

        // Create the PayoutAdjustmentDetails with an existing ID
        payoutAdjustmentDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayoutAdjustmentDetailsMockMvc.perform(post("/api/payout-adjustment-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutAdjustmentDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PayoutAdjustmentDetails in the database
        List<PayoutAdjustmentDetails> payoutAdjustmentDetailsList = payoutAdjustmentDetailsRepository.findAll();
        assertThat(payoutAdjustmentDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the PayoutAdjustmentDetails in Elasticsearch
        verify(mockPayoutAdjustmentDetailsSearchRepository, times(0)).save(payoutAdjustmentDetails);
    }


    @Test
    @Transactional
    public void getAllPayoutAdjustmentDetails() throws Exception {
        // Initialize the database
        payoutAdjustmentDetailsRepository.saveAndFlush(payoutAdjustmentDetails);

        // Get all the payoutAdjustmentDetailsList
        restPayoutAdjustmentDetailsMockMvc.perform(get("/api/payout-adjustment-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutAdjustmentDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].sign").value(hasItem(DEFAULT_SIGN)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].contraEmployeeId").value(hasItem(DEFAULT_CONTRA_EMPLOYEE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getPayoutAdjustmentDetails() throws Exception {
        // Initialize the database
        payoutAdjustmentDetailsRepository.saveAndFlush(payoutAdjustmentDetails);

        // Get the payoutAdjustmentDetails
        restPayoutAdjustmentDetailsMockMvc.perform(get("/api/payout-adjustment-details/{id}", payoutAdjustmentDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payoutAdjustmentDetails.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.sign").value(DEFAULT_SIGN))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.contraEmployeeId").value(DEFAULT_CONTRA_EMPLOYEE_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPayoutAdjustmentDetails() throws Exception {
        // Get the payoutAdjustmentDetails
        restPayoutAdjustmentDetailsMockMvc.perform(get("/api/payout-adjustment-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayoutAdjustmentDetails() throws Exception {
        // Initialize the database
        payoutAdjustmentDetailsService.save(payoutAdjustmentDetails);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPayoutAdjustmentDetailsSearchRepository);

        int databaseSizeBeforeUpdate = payoutAdjustmentDetailsRepository.findAll().size();

        // Update the payoutAdjustmentDetails
        PayoutAdjustmentDetails updatedPayoutAdjustmentDetails = payoutAdjustmentDetailsRepository.findById(payoutAdjustmentDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPayoutAdjustmentDetails are not directly saved in db
        em.detach(updatedPayoutAdjustmentDetails);
        updatedPayoutAdjustmentDetails.setDescription(UPDATED_DESCRIPTION);
        updatedPayoutAdjustmentDetails.setYear(UPDATED_YEAR);
        updatedPayoutAdjustmentDetails.setMonth(UPDATED_MONTH);
        updatedPayoutAdjustmentDetails.setSign(UPDATED_SIGN);
        updatedPayoutAdjustmentDetails.setAmount(UPDATED_AMOUNT);
        updatedPayoutAdjustmentDetails.setContraEmployeeId(UPDATED_CONTRA_EMPLOYEE_ID);

        restPayoutAdjustmentDetailsMockMvc.perform(put("/api/payout-adjustment-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayoutAdjustmentDetails)))
            .andExpect(status().isOk());

        // Validate the PayoutAdjustmentDetails in the database
        List<PayoutAdjustmentDetails> payoutAdjustmentDetailsList = payoutAdjustmentDetailsRepository.findAll();
        assertThat(payoutAdjustmentDetailsList).hasSize(databaseSizeBeforeUpdate);
        PayoutAdjustmentDetails testPayoutAdjustmentDetails = payoutAdjustmentDetailsList.get(payoutAdjustmentDetailsList.size() - 1);
        assertThat(testPayoutAdjustmentDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayoutAdjustmentDetails.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPayoutAdjustmentDetails.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testPayoutAdjustmentDetails.getSign()).isEqualTo(UPDATED_SIGN);
        assertThat(testPayoutAdjustmentDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayoutAdjustmentDetails.getContraEmployeeId()).isEqualTo(UPDATED_CONTRA_EMPLOYEE_ID);

        // Validate the PayoutAdjustmentDetails in Elasticsearch
        verify(mockPayoutAdjustmentDetailsSearchRepository, times(1)).save(testPayoutAdjustmentDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingPayoutAdjustmentDetails() throws Exception {
        int databaseSizeBeforeUpdate = payoutAdjustmentDetailsRepository.findAll().size();

        // Create the PayoutAdjustmentDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayoutAdjustmentDetailsMockMvc.perform(put("/api/payout-adjustment-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutAdjustmentDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PayoutAdjustmentDetails in the database
        List<PayoutAdjustmentDetails> payoutAdjustmentDetailsList = payoutAdjustmentDetailsRepository.findAll();
        assertThat(payoutAdjustmentDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PayoutAdjustmentDetails in Elasticsearch
        verify(mockPayoutAdjustmentDetailsSearchRepository, times(0)).save(payoutAdjustmentDetails);
    }

    @Test
    @Transactional
    public void deletePayoutAdjustmentDetails() throws Exception {
        // Initialize the database
        payoutAdjustmentDetailsService.save(payoutAdjustmentDetails);

        int databaseSizeBeforeDelete = payoutAdjustmentDetailsRepository.findAll().size();

        // Delete the payoutAdjustmentDetails
        restPayoutAdjustmentDetailsMockMvc.perform(delete("/api/payout-adjustment-details/{id}", payoutAdjustmentDetails.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayoutAdjustmentDetails> payoutAdjustmentDetailsList = payoutAdjustmentDetailsRepository.findAll();
        assertThat(payoutAdjustmentDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PayoutAdjustmentDetails in Elasticsearch
        verify(mockPayoutAdjustmentDetailsSearchRepository, times(1)).deleteById(payoutAdjustmentDetails.getId());
    }

    @Test
    @Transactional
    public void searchPayoutAdjustmentDetails() throws Exception {
        // Initialize the database
        payoutAdjustmentDetailsService.save(payoutAdjustmentDetails);
        when(mockPayoutAdjustmentDetailsSearchRepository.search(queryStringQuery("id:" + payoutAdjustmentDetails.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payoutAdjustmentDetails), PageRequest.of(0, 1), 1));
        // Search the payoutAdjustmentDetails
        restPayoutAdjustmentDetailsMockMvc.perform(get("/api/_search/payout-adjustment-details?query=id:" + payoutAdjustmentDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutAdjustmentDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].sign").value(hasItem(DEFAULT_SIGN)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].contraEmployeeId").value(hasItem(DEFAULT_CONTRA_EMPLOYEE_ID.intValue())));
    }
}
