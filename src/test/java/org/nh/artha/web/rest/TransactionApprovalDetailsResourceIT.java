package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.TransactionApprovalDetails;
import org.nh.artha.repository.TransactionApprovalDetailsRepository;
import org.nh.artha.repository.search.TransactionApprovalDetailsSearchRepository;
import org.nh.artha.service.TransactionApprovalDetailsService;

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
import java.time.*;
import java.util.Collections;
import java.util.List;

import static org.nh.artha.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nh.artha.domain.enumeration.DocumentType;
/**
 * Integration tests for the {@link TransactionApprovalDetailsResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TransactionApprovalDetailsResourceIT {

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.VARIABLE_PAYOUT;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.DEPARTMENT_PAYOUT;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_APPROVED_BY_ID = 1L;
    private static final Long UPDATED_APPROVED_BY_ID = 2L;

    private static final String DEFAULT_APPROVED_BY_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_APPROVED_BY_EMPLOYEE_NO = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY_EMPLOYEE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_APPROVED_BY_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY_DISPLAY_NAME = "BBBBBBBBBB";

    /*private static final ZonedDateTime DEFAULT_APPROVED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);*/

    private static final LocalDateTime DEFAULT_APPROVED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime UPDATED_APPROVED_DATE_TIME = LocalDateTime.now();


    @Autowired
    private TransactionApprovalDetailsRepository transactionApprovalDetailsRepository;

    @Autowired
    private TransactionApprovalDetailsService transactionApprovalDetailsService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.TransactionApprovalDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionApprovalDetailsSearchRepository mockTransactionApprovalDetailsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionApprovalDetailsMockMvc;

    private TransactionApprovalDetails transactionApprovalDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionApprovalDetails createEntity(EntityManager em) {
        TransactionApprovalDetails transactionApprovalDetails = new TransactionApprovalDetails();
        transactionApprovalDetails.setDocumentType(DEFAULT_DOCUMENT_TYPE);
        transactionApprovalDetails.setType(DEFAULT_TYPE);
        transactionApprovalDetails.setApprovedById(DEFAULT_APPROVED_BY_ID);
        transactionApprovalDetails.setApprovedByLogin(DEFAULT_APPROVED_BY_LOGIN);
        transactionApprovalDetails.setApprovedByEmployeeNo(DEFAULT_APPROVED_BY_EMPLOYEE_NO);
        transactionApprovalDetails.setApprovedByDisplayName(DEFAULT_APPROVED_BY_DISPLAY_NAME);
        transactionApprovalDetails.setApprovedDateTime(DEFAULT_APPROVED_DATE_TIME);
        return transactionApprovalDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionApprovalDetails createUpdatedEntity(EntityManager em) {
        TransactionApprovalDetails transactionApprovalDetails = new TransactionApprovalDetails();
        transactionApprovalDetails.setDocumentType(UPDATED_DOCUMENT_TYPE);
        transactionApprovalDetails.setType(UPDATED_TYPE);
        transactionApprovalDetails.setApprovedById(UPDATED_APPROVED_BY_ID);
        transactionApprovalDetails.setApprovedByLogin(UPDATED_APPROVED_BY_LOGIN);
        transactionApprovalDetails.setApprovedByEmployeeNo(UPDATED_APPROVED_BY_EMPLOYEE_NO);
        transactionApprovalDetails.setApprovedByDisplayName(UPDATED_APPROVED_BY_DISPLAY_NAME);
        transactionApprovalDetails.setApprovedDateTime(UPDATED_APPROVED_DATE_TIME);
        return transactionApprovalDetails;
    }

    @BeforeEach
    public void initTest() {
        transactionApprovalDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionApprovalDetails() throws Exception {
        int databaseSizeBeforeCreate = transactionApprovalDetailsRepository.findAll().size();

        // Create the TransactionApprovalDetails
        restTransactionApprovalDetailsMockMvc.perform(post("/api/transaction-approval-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(transactionApprovalDetails)))
            .andExpect(status().isCreated());

        // Validate the TransactionApprovalDetails in the database
        List<TransactionApprovalDetails> transactionApprovalDetailsList = transactionApprovalDetailsRepository.findAll();
        assertThat(transactionApprovalDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionApprovalDetails testTransactionApprovalDetails = transactionApprovalDetailsList.get(transactionApprovalDetailsList.size() - 1);
        assertThat(testTransactionApprovalDetails.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testTransactionApprovalDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTransactionApprovalDetails.getApprovedById()).isEqualTo(DEFAULT_APPROVED_BY_ID);
        assertThat(testTransactionApprovalDetails.getApprovedByLogin()).isEqualTo(DEFAULT_APPROVED_BY_LOGIN);
        assertThat(testTransactionApprovalDetails.getApprovedByEmployeeNo()).isEqualTo(DEFAULT_APPROVED_BY_EMPLOYEE_NO);
        assertThat(testTransactionApprovalDetails.getApprovedByDisplayName()).isEqualTo(DEFAULT_APPROVED_BY_DISPLAY_NAME);
        assertThat(testTransactionApprovalDetails.getApprovedDateTime()).isEqualTo(DEFAULT_APPROVED_DATE_TIME);

        // Validate the TransactionApprovalDetails in Elasticsearch
        verify(mockTransactionApprovalDetailsSearchRepository, times(1)).save(testTransactionApprovalDetails);
    }

    @Test
    @Transactional
    public void createTransactionApprovalDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionApprovalDetailsRepository.findAll().size();

        // Create the TransactionApprovalDetails with an existing ID
        transactionApprovalDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionApprovalDetailsMockMvc.perform(post("/api/transaction-approval-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(transactionApprovalDetails)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionApprovalDetails in the database
        List<TransactionApprovalDetails> transactionApprovalDetailsList = transactionApprovalDetailsRepository.findAll();
        assertThat(transactionApprovalDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionApprovalDetails in Elasticsearch
        verify(mockTransactionApprovalDetailsSearchRepository, times(0)).save(transactionApprovalDetails);
    }


    @Test
    @Transactional
    public void getAllTransactionApprovalDetails() throws Exception {
        // Initialize the database
        transactionApprovalDetailsRepository.saveAndFlush(transactionApprovalDetails);

        ZoneId zoneId = ZoneId.of( "Asia/Kolkata" );
        ZonedDateTime zdtAtAsia = DEFAULT_APPROVED_DATE_TIME.atZone( zoneId );

        // Get all the transactionApprovalDetailsList
        restTransactionApprovalDetailsMockMvc.perform(get("/api/transaction-approval-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionApprovalDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].approvedById").value(hasItem(DEFAULT_APPROVED_BY_ID.intValue())))
            .andExpect(jsonPath("$.[*].approvedByLogin").value(hasItem(DEFAULT_APPROVED_BY_LOGIN)))
            .andExpect(jsonPath("$.[*].approvedByEmployeeNo").value(hasItem(DEFAULT_APPROVED_BY_EMPLOYEE_NO)))
            .andExpect(jsonPath("$.[*].approvedByDisplayName").value(hasItem(DEFAULT_APPROVED_BY_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].approvedDateTime").value(hasItem(sameInstant(zdtAtAsia))));
    }

    @Test
    @Transactional
    public void getTransactionApprovalDetails() throws Exception {
        // Initialize the database
        transactionApprovalDetailsRepository.saveAndFlush(transactionApprovalDetails);

        ZoneId zoneId = ZoneId.of( "Asia/Kolkata" );
        ZonedDateTime zdtAtAsia = DEFAULT_APPROVED_DATE_TIME.atZone( zoneId );

        // Get the transactionApprovalDetails
        restTransactionApprovalDetailsMockMvc.perform(get("/api/transaction-approval-details/{id}", transactionApprovalDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionApprovalDetails.getId().intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.approvedById").value(DEFAULT_APPROVED_BY_ID.intValue()))
            .andExpect(jsonPath("$.approvedByLogin").value(DEFAULT_APPROVED_BY_LOGIN))
            .andExpect(jsonPath("$.approvedByEmployeeNo").value(DEFAULT_APPROVED_BY_EMPLOYEE_NO))
            .andExpect(jsonPath("$.approvedByDisplayName").value(DEFAULT_APPROVED_BY_DISPLAY_NAME))
            .andExpect(jsonPath("$.approvedDateTime").value(sameInstant(zdtAtAsia)));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionApprovalDetails() throws Exception {
        // Get the transactionApprovalDetails
        restTransactionApprovalDetailsMockMvc.perform(get("/api/transaction-approval-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionApprovalDetails() throws Exception {
        // Initialize the database
        transactionApprovalDetailsService.save(transactionApprovalDetails);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTransactionApprovalDetailsSearchRepository);

        int databaseSizeBeforeUpdate = transactionApprovalDetailsRepository.findAll().size();

        // Update the transactionApprovalDetails
        TransactionApprovalDetails updatedTransactionApprovalDetails = transactionApprovalDetailsRepository.findById(transactionApprovalDetails.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionApprovalDetails are not directly saved in db
        em.detach(updatedTransactionApprovalDetails);
        updatedTransactionApprovalDetails.setDocumentType(UPDATED_DOCUMENT_TYPE);
        updatedTransactionApprovalDetails.setType(UPDATED_TYPE);
        updatedTransactionApprovalDetails.setApprovedById(UPDATED_APPROVED_BY_ID);
        updatedTransactionApprovalDetails.setApprovedByLogin(UPDATED_APPROVED_BY_LOGIN);
        updatedTransactionApprovalDetails.setApprovedByEmployeeNo(UPDATED_APPROVED_BY_EMPLOYEE_NO);
        updatedTransactionApprovalDetails.setApprovedByDisplayName(UPDATED_APPROVED_BY_DISPLAY_NAME);
        updatedTransactionApprovalDetails.setApprovedDateTime(UPDATED_APPROVED_DATE_TIME);

        restTransactionApprovalDetailsMockMvc.perform(put("/api/transaction-approval-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransactionApprovalDetails)))
            .andExpect(status().isOk());

        // Validate the TransactionApprovalDetails in the database
        List<TransactionApprovalDetails> transactionApprovalDetailsList = transactionApprovalDetailsRepository.findAll();
        assertThat(transactionApprovalDetailsList).hasSize(databaseSizeBeforeUpdate);
        TransactionApprovalDetails testTransactionApprovalDetails = transactionApprovalDetailsList.get(transactionApprovalDetailsList.size() - 1);
        assertThat(testTransactionApprovalDetails.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testTransactionApprovalDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTransactionApprovalDetails.getApprovedById()).isEqualTo(UPDATED_APPROVED_BY_ID);
        assertThat(testTransactionApprovalDetails.getApprovedByLogin()).isEqualTo(UPDATED_APPROVED_BY_LOGIN);
        assertThat(testTransactionApprovalDetails.getApprovedByEmployeeNo()).isEqualTo(UPDATED_APPROVED_BY_EMPLOYEE_NO);
        assertThat(testTransactionApprovalDetails.getApprovedByDisplayName()).isEqualTo(UPDATED_APPROVED_BY_DISPLAY_NAME);
        assertThat(testTransactionApprovalDetails.getApprovedDateTime()).isEqualTo(UPDATED_APPROVED_DATE_TIME);

        // Validate the TransactionApprovalDetails in Elasticsearch
        verify(mockTransactionApprovalDetailsSearchRepository, times(1)).save(testTransactionApprovalDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionApprovalDetails() throws Exception {
        int databaseSizeBeforeUpdate = transactionApprovalDetailsRepository.findAll().size();

        // Create the TransactionApprovalDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionApprovalDetailsMockMvc.perform(put("/api/transaction-approval-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(transactionApprovalDetails)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionApprovalDetails in the database
        List<TransactionApprovalDetails> transactionApprovalDetailsList = transactionApprovalDetailsRepository.findAll();
        assertThat(transactionApprovalDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionApprovalDetails in Elasticsearch
        verify(mockTransactionApprovalDetailsSearchRepository, times(0)).save(transactionApprovalDetails);
    }

    @Test
    @Transactional
    public void deleteTransactionApprovalDetails() throws Exception {
        // Initialize the database
        transactionApprovalDetailsService.save(transactionApprovalDetails);

        int databaseSizeBeforeDelete = transactionApprovalDetailsRepository.findAll().size();

        // Delete the transactionApprovalDetails
        restTransactionApprovalDetailsMockMvc.perform(delete("/api/transaction-approval-details/{id}", transactionApprovalDetails.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionApprovalDetails> transactionApprovalDetailsList = transactionApprovalDetailsRepository.findAll();
        assertThat(transactionApprovalDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionApprovalDetails in Elasticsearch
        verify(mockTransactionApprovalDetailsSearchRepository, times(1)).deleteById(transactionApprovalDetails.getId());
    }

    @Test
    @Transactional
    public void searchTransactionApprovalDetails() throws Exception {
        // Initialize the database
        transactionApprovalDetailsService.save(transactionApprovalDetails);
        when(mockTransactionApprovalDetailsSearchRepository.search(queryStringQuery("id:" + transactionApprovalDetails.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionApprovalDetails), PageRequest.of(0, 1), 1));
        // Search the transactionApprovalDetails

        ZoneId zoneId = ZoneId.of( "Asia/Kolkata" );
        ZonedDateTime zdtAtAsia = DEFAULT_APPROVED_DATE_TIME.atZone( zoneId );

        restTransactionApprovalDetailsMockMvc.perform(get("/api/_search/transaction-approval-details?query=id:" + transactionApprovalDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionApprovalDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].approvedById").value(hasItem(DEFAULT_APPROVED_BY_ID.intValue())))
            .andExpect(jsonPath("$.[*].approvedByLogin").value(hasItem(DEFAULT_APPROVED_BY_LOGIN)))
            .andExpect(jsonPath("$.[*].approvedByEmployeeNo").value(hasItem(DEFAULT_APPROVED_BY_EMPLOYEE_NO)))
            .andExpect(jsonPath("$.[*].approvedByDisplayName").value(hasItem(DEFAULT_APPROVED_BY_DISPLAY_NAME)))
            .andExpect(jsonPath("$.[*].approvedDateTime").value(hasItem(sameInstant(zdtAtAsia))));
    }
}
