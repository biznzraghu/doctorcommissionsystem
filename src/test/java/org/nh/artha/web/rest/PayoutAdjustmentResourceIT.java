package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.PayoutAdjustment;
import org.nh.artha.repository.PayoutAdjustmentRepository;
import org.nh.artha.repository.search.PayoutAdjustmentSearchRepository;
import org.nh.artha.service.PayoutAdjustmentService;

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

import org.nh.artha.domain.enumeration.TransactionType;
/**
 * Integration tests for the {@link PayoutAdjustmentResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PayoutAdjustmentResourceIT {

    private static final String DEFAULT_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_CODE = "BBBBBBBBBB";

    private static final TransactionType DEFAULT_TYPE = TransactionType.EMPLOYEE;
    private static final TransactionType UPDATED_TYPE = TransactionType.DEPARTMENT;

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;

    private static final Long DEFAULT_DEPARTMENT_ID = 1L;
    private static final Long UPDATED_DEPARTMENT_ID = 2L;

    private static final BigDecimal DEFAULT_NET_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_NET_AMOUNT = new BigDecimal(2);

   /* private static final ZonedDateTime DEFAULT_CREATED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);*/

    private static final LocalDateTime DEFAULT_CREATED_DATE_TIME = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CREATED_DATE_TIME = LocalDateTime.now();

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private PayoutAdjustmentRepository payoutAdjustmentRepository;

    @Autowired
    private PayoutAdjustmentService payoutAdjustmentService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PayoutAdjustmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private PayoutAdjustmentSearchRepository mockPayoutAdjustmentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayoutAdjustmentMockMvc;

    private PayoutAdjustment payoutAdjustment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutAdjustment createEntity(EntityManager em) {
        PayoutAdjustment payoutAdjustment = new PayoutAdjustment();
        payoutAdjustment.setDocumentNumber(DEFAULT_DOCUMENT_NUMBER);
        payoutAdjustment.setUnitCode(DEFAULT_UNIT_CODE);
        payoutAdjustment.setType(DEFAULT_TYPE);
        payoutAdjustment.setEmployeeId(DEFAULT_EMPLOYEE_ID);
        payoutAdjustment.setDepartmentId(DEFAULT_DEPARTMENT_ID);
        payoutAdjustment.setNetAmount(DEFAULT_NET_AMOUNT);
        payoutAdjustment.setCreatedDateTime(DEFAULT_CREATED_DATE_TIME);
        payoutAdjustment.setStatus(DEFAULT_STATUS);
        return payoutAdjustment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutAdjustment createUpdatedEntity(EntityManager em) {
        PayoutAdjustment payoutAdjustment = new PayoutAdjustment();
        payoutAdjustment.setDocumentNumber(UPDATED_DOCUMENT_NUMBER);
        payoutAdjustment.setUnitCode(UPDATED_UNIT_CODE);
        payoutAdjustment.setType(UPDATED_TYPE);
        payoutAdjustment.setEmployeeId(UPDATED_EMPLOYEE_ID);
        payoutAdjustment.setDepartmentId(UPDATED_DEPARTMENT_ID);
        payoutAdjustment.setNetAmount(UPDATED_NET_AMOUNT);
        payoutAdjustment.setCreatedDateTime(UPDATED_CREATED_DATE_TIME);
        payoutAdjustment.setStatus(UPDATED_STATUS);
        return payoutAdjustment;
    }

    @BeforeEach
    public void initTest() {
        payoutAdjustment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayoutAdjustment() throws Exception {
        int databaseSizeBeforeCreate = payoutAdjustmentRepository.findAll().size();

        // Create the PayoutAdjustment
        restPayoutAdjustmentMockMvc.perform(post("/api/payout-adjustments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutAdjustment)))
            .andExpect(status().isCreated());

        // Validate the PayoutAdjustment in the database
        List<PayoutAdjustment> payoutAdjustmentList = payoutAdjustmentRepository.findAll();
        assertThat(payoutAdjustmentList).hasSize(databaseSizeBeforeCreate + 1);
        PayoutAdjustment testPayoutAdjustment = payoutAdjustmentList.get(payoutAdjustmentList.size() - 1);
        assertThat(testPayoutAdjustment.getDocumentNumber()).isEqualTo(DEFAULT_DOCUMENT_NUMBER);
        assertThat(testPayoutAdjustment.getUnitCode()).isEqualTo(DEFAULT_UNIT_CODE);
        assertThat(testPayoutAdjustment.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPayoutAdjustment.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testPayoutAdjustment.getDepartmentId()).isEqualTo(DEFAULT_DEPARTMENT_ID);
        assertThat(testPayoutAdjustment.getNetAmount()).isEqualTo(DEFAULT_NET_AMOUNT);
        assertThat(testPayoutAdjustment.getCreatedDateTime()).isEqualTo(DEFAULT_CREATED_DATE_TIME);
        assertThat(testPayoutAdjustment.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the PayoutAdjustment in Elasticsearch
        verify(mockPayoutAdjustmentSearchRepository, times(1)).save(testPayoutAdjustment);
    }

    @Test
    @Transactional
    public void createPayoutAdjustmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payoutAdjustmentRepository.findAll().size();

        // Create the PayoutAdjustment with an existing ID
        payoutAdjustment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayoutAdjustmentMockMvc.perform(post("/api/payout-adjustments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutAdjustment)))
            .andExpect(status().isBadRequest());

        // Validate the PayoutAdjustment in the database
        List<PayoutAdjustment> payoutAdjustmentList = payoutAdjustmentRepository.findAll();
        assertThat(payoutAdjustmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the PayoutAdjustment in Elasticsearch
        verify(mockPayoutAdjustmentSearchRepository, times(0)).save(payoutAdjustment);
    }


    @Test
    @Transactional
    public void getAllPayoutAdjustments() throws Exception {
        // Initialize the database
        payoutAdjustmentRepository.saveAndFlush(payoutAdjustment);

        ZoneId zoneId = ZoneId.of( "Asia/Kolkata" );
        ZonedDateTime zdtAtAsia = DEFAULT_CREATED_DATE_TIME.atZone( zoneId );

        // Get all the payoutAdjustmentList
        restPayoutAdjustmentMockMvc.perform(get("/api/payout-adjustments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutAdjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].unitCode").value(hasItem(DEFAULT_UNIT_CODE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].departmentId").value(hasItem(DEFAULT_DEPARTMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].netAmount").value(hasItem(DEFAULT_NET_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdDateTime").value(hasItem(sameInstant(zdtAtAsia))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getPayoutAdjustment() throws Exception {
        // Initialize the database
        payoutAdjustmentRepository.saveAndFlush(payoutAdjustment);

        ZoneId zoneId = ZoneId.of( "Asia/Kolkata" );
        ZonedDateTime zdtAtAsia = DEFAULT_CREATED_DATE_TIME.atZone( zoneId );

        // Get the payoutAdjustment
        restPayoutAdjustmentMockMvc.perform(get("/api/payout-adjustments/{id}", payoutAdjustment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payoutAdjustment.getId().intValue()))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.unitCode").value(DEFAULT_UNIT_CODE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.departmentId").value(DEFAULT_DEPARTMENT_ID.intValue()))
            .andExpect(jsonPath("$.netAmount").value(DEFAULT_NET_AMOUNT.intValue()))
            .andExpect(jsonPath("$.createdDateTime").value(sameInstant(zdtAtAsia)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingPayoutAdjustment() throws Exception {
        // Get the payoutAdjustment
        restPayoutAdjustmentMockMvc.perform(get("/api/payout-adjustments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayoutAdjustment() throws Exception {
        // Initialize the database
        payoutAdjustmentService.save(payoutAdjustment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPayoutAdjustmentSearchRepository);

        int databaseSizeBeforeUpdate = payoutAdjustmentRepository.findAll().size();

        // Update the payoutAdjustment
        PayoutAdjustment updatedPayoutAdjustment = payoutAdjustmentRepository.findById(payoutAdjustment.getId()).get();
        // Disconnect from session so that the updates on updatedPayoutAdjustment are not directly saved in db
        em.detach(updatedPayoutAdjustment);
        updatedPayoutAdjustment.setDocumentNumber(UPDATED_DOCUMENT_NUMBER);
        updatedPayoutAdjustment.setUnitCode(UPDATED_UNIT_CODE);
        updatedPayoutAdjustment.setType(UPDATED_TYPE);
        updatedPayoutAdjustment.setEmployeeId(UPDATED_EMPLOYEE_ID);
        updatedPayoutAdjustment.setDepartmentId(UPDATED_DEPARTMENT_ID);
        updatedPayoutAdjustment.setNetAmount(UPDATED_NET_AMOUNT);
        updatedPayoutAdjustment.setCreatedDateTime(UPDATED_CREATED_DATE_TIME);
        updatedPayoutAdjustment.setStatus(UPDATED_STATUS);

        restPayoutAdjustmentMockMvc.perform(put("/api/payout-adjustments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayoutAdjustment)))
            .andExpect(status().isOk());

        // Validate the PayoutAdjustment in the database
        List<PayoutAdjustment> payoutAdjustmentList = payoutAdjustmentRepository.findAll();
        assertThat(payoutAdjustmentList).hasSize(databaseSizeBeforeUpdate);
        PayoutAdjustment testPayoutAdjustment = payoutAdjustmentList.get(payoutAdjustmentList.size() - 1);
        assertThat(testPayoutAdjustment.getDocumentNumber()).isEqualTo(UPDATED_DOCUMENT_NUMBER);
        assertThat(testPayoutAdjustment.getUnitCode()).isEqualTo(UPDATED_UNIT_CODE);
        assertThat(testPayoutAdjustment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPayoutAdjustment.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testPayoutAdjustment.getDepartmentId()).isEqualTo(UPDATED_DEPARTMENT_ID);
        assertThat(testPayoutAdjustment.getNetAmount()).isEqualTo(UPDATED_NET_AMOUNT);
        assertThat(testPayoutAdjustment.getCreatedDateTime()).isEqualTo(UPDATED_CREATED_DATE_TIME);
        assertThat(testPayoutAdjustment.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the PayoutAdjustment in Elasticsearch
        verify(mockPayoutAdjustmentSearchRepository, times(1)).save(testPayoutAdjustment);
    }

    @Test
    @Transactional
    public void updateNonExistingPayoutAdjustment() throws Exception {
        int databaseSizeBeforeUpdate = payoutAdjustmentRepository.findAll().size();

        // Create the PayoutAdjustment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayoutAdjustmentMockMvc.perform(put("/api/payout-adjustments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(payoutAdjustment)))
            .andExpect(status().isBadRequest());

        // Validate the PayoutAdjustment in the database
        List<PayoutAdjustment> payoutAdjustmentList = payoutAdjustmentRepository.findAll();
        assertThat(payoutAdjustmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PayoutAdjustment in Elasticsearch
        verify(mockPayoutAdjustmentSearchRepository, times(0)).save(payoutAdjustment);
    }

    @Test
    @Transactional
    public void deletePayoutAdjustment() throws Exception {
        // Initialize the database
        payoutAdjustmentService.save(payoutAdjustment);

        int databaseSizeBeforeDelete = payoutAdjustmentRepository.findAll().size();

        // Delete the payoutAdjustment
        restPayoutAdjustmentMockMvc.perform(delete("/api/payout-adjustments/{id}", payoutAdjustment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayoutAdjustment> payoutAdjustmentList = payoutAdjustmentRepository.findAll();
        assertThat(payoutAdjustmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PayoutAdjustment in Elasticsearch
        verify(mockPayoutAdjustmentSearchRepository, times(1)).deleteById(payoutAdjustment.getId());
    }

    @Test
    @Transactional
    public void searchPayoutAdjustment() throws Exception {
        // Initialize the database
        payoutAdjustmentService.save(payoutAdjustment);
        when(mockPayoutAdjustmentSearchRepository.search(queryStringQuery("id:" + payoutAdjustment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payoutAdjustment), PageRequest.of(0, 1), 1));
        // Search the payoutAdjustment

        ZoneId zoneId = ZoneId.of( "Asia/Kolkata" );
        ZonedDateTime zdtAtAsia = DEFAULT_CREATED_DATE_TIME.atZone( zoneId );

        restPayoutAdjustmentMockMvc.perform(get("/api/_search/payout-adjustments?query=id:" + payoutAdjustment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutAdjustment.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].unitCode").value(hasItem(DEFAULT_UNIT_CODE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].departmentId").value(hasItem(DEFAULT_DEPARTMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].netAmount").value(hasItem(DEFAULT_NET_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].createdDateTime").value(hasItem(sameInstant(zdtAtAsia))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
}
