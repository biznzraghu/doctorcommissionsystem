package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.PayoutDetails;
import org.nh.artha.repository.PayoutDetailsRepository;
import org.nh.artha.repository.search.PayoutDetailsSearchRepository;
import org.nh.artha.service.PayoutDetailsService;
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
import java.time.*;
import java.util.Collections;
import java.util.List;

import static org.nh.artha.web.rest.TestUtil.sameInstant;
import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nh.artha.domain.enumeration.Status;
import org.nh.artha.domain.enumeration.VisitType;
import org.nh.artha.domain.enumeration.DiscountType;
/**
 * Integration tests for the {@link PayoutDetailsResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class PayoutDetailsResourceIT {

    private static final LocalDateTime DEFAULT_COMMENCEMENT_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_COMMENCEMENT_DATE = LocalDateTime.now();

    private static final LocalDateTime DEFAULT_CONTRACT_END_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CONTRACT_END_DATE = LocalDateTime.now();

    private static final Double DEFAULT_MINIMUM_ASSURED_AMOUNT = 1D;
    private static final Double UPDATED_MINIMUM_ASSURED_AMOUNT = 2D;

    private static final Double DEFAULT_MINIMUM_ASSURED_VALIDITY = 1D;
    private static final Double UPDATED_MINIMUM_ASSURED_VALIDITY = 2D;

    private static final Double DEFAULT_MAXIMUM_PAYOUT_AMOUNT = 1D;
    private static final Double UPDATED_MAXIMUM_PAYOUT_AMOUNT = 2D;

    private static final String DEFAULT_UPLOAD_CONTRACT = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_CONTRACT = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    private static final VisitType DEFAULT_VISIT_TYPE = VisitType.OP;
    private static final VisitType UPDATED_VISIT_TYPE = VisitType.IP;

    private static final DiscountType DEFAULT_DISCOUNT = DiscountType.NET;
    private static final DiscountType UPDATED_DISCOUNT = DiscountType.GROSS;

    private static final String DEFAULT_PAYOUT_RANGE = "AAAAAAAAAA";
    private static final String UPDATED_PAYOUT_RANGE = "BBBBBBBBBB";

    private static final String DEFAULT_APPLICABLE_INVOICES = "AAAAAAAAAA";
    private static final String UPDATED_APPLICABLE_INVOICES = "BBBBBBBBBB";

    private static final String DEFAULT_MATERIAL_REDUCTION = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL_REDUCTION = "BBBBBBBBBB";

    private static final String DEFAULT_EXCEPTION = "AAAAAAAAAA";
    private static final String UPDATED_EXCEPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private PayoutDetailsRepository payoutDetailsRepository;

    @Autowired
    private PayoutDetailsService payoutDetailsService;

    @Autowired
    private PayoutDetailsSearchRepository payoutDetailsSearchRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.PayoutDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private PayoutDetailsSearchRepository mockPayoutDetailsSearchRepository;

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

    private MockMvc restPayoutDetailsMockMvc;

    private PayoutDetails payoutDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PayoutDetailsResource payoutDetailsResource = new PayoutDetailsResource(payoutDetailsService,payoutDetailsRepository,payoutDetailsSearchRepository,applicationProperties);
        this.restPayoutDetailsMockMvc = MockMvcBuilders.standaloneSetup(payoutDetailsResource)
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
    public static PayoutDetails createEntity(EntityManager em) {
        PayoutDetails payoutDetails = new PayoutDetails();
        payoutDetails.setCommencementDate(DEFAULT_COMMENCEMENT_DATE);
        payoutDetails.setContractEndDate(DEFAULT_CONTRACT_END_DATE);
        payoutDetails.setMinimumAssuredAmount(DEFAULT_MINIMUM_ASSURED_AMOUNT);
        payoutDetails.setMinimumAssuredValidity(DEFAULT_MINIMUM_ASSURED_VALIDITY);
        payoutDetails.setMaximumPayoutAmount(DEFAULT_MAXIMUM_PAYOUT_AMOUNT);
        payoutDetails.setUploadContract(DEFAULT_UPLOAD_CONTRACT);
        payoutDetails.setStatus(DEFAULT_STATUS);
        payoutDetails.setVisitType(DEFAULT_VISIT_TYPE);
        payoutDetails.setDiscount(DEFAULT_DISCOUNT);
        payoutDetails.setPayoutRange(DEFAULT_PAYOUT_RANGE);
        payoutDetails.setApplicableInvoices(DEFAULT_APPLICABLE_INVOICES);
        payoutDetails.setMaterialReduction(DEFAULT_MATERIAL_REDUCTION);
        payoutDetails.setException(DEFAULT_EXCEPTION);
        payoutDetails.setComments(DEFAULT_COMMENTS);
        return payoutDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutDetails createUpdatedEntity(EntityManager em) {
        PayoutDetails payoutDetails = new PayoutDetails();
        payoutDetails.setCommencementDate(UPDATED_COMMENCEMENT_DATE);
        payoutDetails.setContractEndDate(UPDATED_CONTRACT_END_DATE);
        payoutDetails.setMinimumAssuredAmount(UPDATED_MINIMUM_ASSURED_AMOUNT);
        payoutDetails.setMinimumAssuredValidity(UPDATED_MINIMUM_ASSURED_VALIDITY);
        payoutDetails.setMaximumPayoutAmount(UPDATED_MAXIMUM_PAYOUT_AMOUNT);
        payoutDetails.setUploadContract(UPDATED_UPLOAD_CONTRACT);
        payoutDetails.setStatus(UPDATED_STATUS);
        payoutDetails.setVisitType(UPDATED_VISIT_TYPE);
        payoutDetails.setDiscount(UPDATED_DISCOUNT);
        payoutDetails.setPayoutRange(UPDATED_PAYOUT_RANGE);
        payoutDetails.setApplicableInvoices(UPDATED_APPLICABLE_INVOICES);
        payoutDetails.setMaterialReduction(UPDATED_MATERIAL_REDUCTION);
        payoutDetails.setException(UPDATED_EXCEPTION);
        payoutDetails.setComments(UPDATED_COMMENTS);
        return payoutDetails;
    }

    @BeforeEach
    public void initTest() {
        payoutDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayoutDetails() throws Exception {
        int databaseSizeBeforeCreate = payoutDetailsRepository.findAll().size();

        // Create the PayoutDetails
        restPayoutDetailsMockMvc.perform(post("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payoutDetails)))
            .andExpect(status().isCreated());

        // Validate the PayoutDetails in the database
        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PayoutDetails testPayoutDetails = payoutDetailsList.get(payoutDetailsList.size() - 1);
        assertThat(testPayoutDetails.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testPayoutDetails.getContractEndDate()).isEqualTo(DEFAULT_CONTRACT_END_DATE);
        assertThat(testPayoutDetails.getMinimumAssuredAmount()).isEqualTo(DEFAULT_MINIMUM_ASSURED_AMOUNT);
        assertThat(testPayoutDetails.getMinimumAssuredValidity()).isEqualTo(DEFAULT_MINIMUM_ASSURED_VALIDITY);
        assertThat(testPayoutDetails.getMaximumPayoutAmount()).isEqualTo(DEFAULT_MAXIMUM_PAYOUT_AMOUNT);
        assertThat(testPayoutDetails.getUploadContract()).isEqualTo(DEFAULT_UPLOAD_CONTRACT);
        assertThat(testPayoutDetails.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPayoutDetails.getVisitType()).isEqualTo(DEFAULT_VISIT_TYPE);
        assertThat(testPayoutDetails.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testPayoutDetails.getPayoutRange()).isEqualTo(DEFAULT_PAYOUT_RANGE);
        assertThat(testPayoutDetails.getApplicableInvoices()).isEqualTo(DEFAULT_APPLICABLE_INVOICES);
        assertThat(testPayoutDetails.getMaterialReduction()).isEqualTo(DEFAULT_MATERIAL_REDUCTION);
        assertThat(testPayoutDetails.getException()).isEqualTo(DEFAULT_EXCEPTION);
        assertThat(testPayoutDetails.getComments()).isEqualTo(DEFAULT_COMMENTS);

        // Validate the PayoutDetails in Elasticsearch
        verify(mockPayoutDetailsSearchRepository, times(1)).save(testPayoutDetails);
    }

    @Test
    @Transactional
    public void createPayoutDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payoutDetailsRepository.findAll().size();

        // Create the PayoutDetails with an existing ID
        payoutDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayoutDetailsMockMvc.perform(post("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payoutDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PayoutDetails in the database
        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the PayoutDetails in Elasticsearch
        verify(mockPayoutDetailsSearchRepository, times(0)).save(payoutDetails);
    }


    @Test
    @Transactional
    public void checkCommencementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutDetailsRepository.findAll().size();
        // set the field null
        payoutDetails.setCommencementDate(null);

        // Create the PayoutDetails, which fails.

        restPayoutDetailsMockMvc.perform(post("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payoutDetails)))
            .andExpect(status().isBadRequest());

        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContractEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutDetailsRepository.findAll().size();
        // set the field null
        payoutDetails.setContractEndDate(null);

        // Create the PayoutDetails, which fails.

        restPayoutDetailsMockMvc.perform(post("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payoutDetails)))
            .andExpect(status().isBadRequest());

        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutDetailsRepository.findAll().size();
        // set the field null
        payoutDetails.setStatus(null);

        // Create the PayoutDetails, which fails.

        restPayoutDetailsMockMvc.perform(post("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payoutDetails)))
            .andExpect(status().isBadRequest());

        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVisitTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutDetailsRepository.findAll().size();
        // set the field null
        payoutDetails.setVisitType(null);

        // Create the PayoutDetails, which fails.

        restPayoutDetailsMockMvc.perform(post("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payoutDetails)))
            .andExpect(status().isBadRequest());

        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiscountIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutDetailsRepository.findAll().size();
        // set the field null
        payoutDetails.setDiscount(null);

        // Create the PayoutDetails, which fails.

        restPayoutDetailsMockMvc.perform(post("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payoutDetails)))
            .andExpect(status().isBadRequest());

        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayoutDetails() throws Exception {
        // Initialize the database
        payoutDetailsRepository.saveAndFlush(payoutDetails);

        // Get all the payoutDetailsList
        restPayoutDetailsMockMvc.perform(get("/api/payout-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimumAssuredAmount").value(hasItem(DEFAULT_MINIMUM_ASSURED_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumAssuredValidity").value(hasItem(DEFAULT_MINIMUM_ASSURED_VALIDITY.doubleValue())))
            .andExpect(jsonPath("$.[*].maximumPayoutAmount").value(hasItem(DEFAULT_MAXIMUM_PAYOUT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadContract").value(hasItem(DEFAULT_UPLOAD_CONTRACT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.toString())))
            .andExpect(jsonPath("$.[*].payoutRange").value(hasItem(DEFAULT_PAYOUT_RANGE)))
            .andExpect(jsonPath("$.[*].applicableInvoices").value(hasItem(DEFAULT_APPLICABLE_INVOICES)))
            .andExpect(jsonPath("$.[*].materialReduction").value(hasItem(DEFAULT_MATERIAL_REDUCTION)))
            .andExpect(jsonPath("$.[*].exception").value(hasItem(DEFAULT_EXCEPTION)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    public void getPayoutDetails() throws Exception {
        // Initialize the database
        payoutDetailsRepository.saveAndFlush(payoutDetails);

        // Get the payoutDetails
        restPayoutDetailsMockMvc.perform(get("/api/payout-details/{id}", payoutDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payoutDetails.getId().intValue()))
            .andExpect(jsonPath("$.minimumAssuredAmount").value(DEFAULT_MINIMUM_ASSURED_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.minimumAssuredValidity").value(DEFAULT_MINIMUM_ASSURED_VALIDITY.doubleValue()))
            .andExpect(jsonPath("$.maximumPayoutAmount").value(DEFAULT_MAXIMUM_PAYOUT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.uploadContract").value(DEFAULT_UPLOAD_CONTRACT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.visitType").value(DEFAULT_VISIT_TYPE.toString()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.toString()))
            .andExpect(jsonPath("$.payoutRange").value(DEFAULT_PAYOUT_RANGE))
            .andExpect(jsonPath("$.applicableInvoices").value(DEFAULT_APPLICABLE_INVOICES))
            .andExpect(jsonPath("$.materialReduction").value(DEFAULT_MATERIAL_REDUCTION))
            .andExpect(jsonPath("$.exception").value(DEFAULT_EXCEPTION))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    public void getNonExistingPayoutDetails() throws Exception {
        // Get the payoutDetails
        restPayoutDetailsMockMvc.perform(get("/api/payout-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayoutDetails() throws Exception {
        // Initialize the database
        payoutDetailsService.save(payoutDetails);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPayoutDetailsSearchRepository);

        int databaseSizeBeforeUpdate = payoutDetailsRepository.findAll().size();

        // Update the payoutDetails
        PayoutDetails updatedPayoutDetails = payoutDetailsRepository.findById(payoutDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPayoutDetails are not directly saved in db
        em.detach(updatedPayoutDetails);
        updatedPayoutDetails.setCommencementDate(UPDATED_COMMENCEMENT_DATE);
        updatedPayoutDetails.setContractEndDate(UPDATED_CONTRACT_END_DATE);
        updatedPayoutDetails.setMinimumAssuredAmount(UPDATED_MINIMUM_ASSURED_AMOUNT);
        updatedPayoutDetails.setMinimumAssuredValidity(UPDATED_MINIMUM_ASSURED_VALIDITY);
        updatedPayoutDetails.setMaximumPayoutAmount(UPDATED_MAXIMUM_PAYOUT_AMOUNT);
        updatedPayoutDetails.setUploadContract(UPDATED_UPLOAD_CONTRACT);
        updatedPayoutDetails.setStatus(UPDATED_STATUS);
        updatedPayoutDetails.setVisitType(UPDATED_VISIT_TYPE);
        updatedPayoutDetails.setDiscount(UPDATED_DISCOUNT);
        updatedPayoutDetails.setPayoutRange(UPDATED_PAYOUT_RANGE);
        updatedPayoutDetails.setApplicableInvoices(UPDATED_APPLICABLE_INVOICES);
        updatedPayoutDetails.setMaterialReduction(UPDATED_MATERIAL_REDUCTION);
        updatedPayoutDetails.setException(UPDATED_EXCEPTION);
        updatedPayoutDetails.setComments(UPDATED_COMMENTS);

        restPayoutDetailsMockMvc.perform(put("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayoutDetails)))
            .andExpect(status().isOk());

        // Validate the PayoutDetails in the database
        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeUpdate);
        PayoutDetails testPayoutDetails = payoutDetailsList.get(payoutDetailsList.size() - 1);
        assertThat(testPayoutDetails.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testPayoutDetails.getContractEndDate()).isEqualTo(UPDATED_CONTRACT_END_DATE);
        assertThat(testPayoutDetails.getMinimumAssuredAmount()).isEqualTo(UPDATED_MINIMUM_ASSURED_AMOUNT);
        assertThat(testPayoutDetails.getMinimumAssuredValidity()).isEqualTo(UPDATED_MINIMUM_ASSURED_VALIDITY);
        assertThat(testPayoutDetails.getMaximumPayoutAmount()).isEqualTo(UPDATED_MAXIMUM_PAYOUT_AMOUNT);
        assertThat(testPayoutDetails.getUploadContract()).isEqualTo(UPDATED_UPLOAD_CONTRACT);
        assertThat(testPayoutDetails.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPayoutDetails.getVisitType()).isEqualTo(UPDATED_VISIT_TYPE);
        assertThat(testPayoutDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPayoutDetails.getPayoutRange()).isEqualTo(UPDATED_PAYOUT_RANGE);
        assertThat(testPayoutDetails.getApplicableInvoices()).isEqualTo(UPDATED_APPLICABLE_INVOICES);
        assertThat(testPayoutDetails.getMaterialReduction()).isEqualTo(UPDATED_MATERIAL_REDUCTION);
        assertThat(testPayoutDetails.getException()).isEqualTo(UPDATED_EXCEPTION);
        assertThat(testPayoutDetails.getComments()).isEqualTo(UPDATED_COMMENTS);

        // Validate the PayoutDetails in Elasticsearch
        verify(mockPayoutDetailsSearchRepository, times(1)).save(testPayoutDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingPayoutDetails() throws Exception {
        int databaseSizeBeforeUpdate = payoutDetailsRepository.findAll().size();

        // Create the PayoutDetails

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayoutDetailsMockMvc.perform(put("/api/payout-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payoutDetails)))
            .andExpect(status().isBadRequest());

        // Validate the PayoutDetails in the database
        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PayoutDetails in Elasticsearch
        verify(mockPayoutDetailsSearchRepository, times(0)).save(payoutDetails);
    }

    @Test
    @Transactional
    public void deletePayoutDetails() throws Exception {
        // Initialize the database
        payoutDetailsService.save(payoutDetails);

        int databaseSizeBeforeDelete = payoutDetailsRepository.findAll().size();

        // Delete the payoutDetails
        restPayoutDetailsMockMvc.perform(delete("/api/payout-details/{id}", payoutDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayoutDetails> payoutDetailsList = payoutDetailsRepository.findAll();
        assertThat(payoutDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PayoutDetails in Elasticsearch
        verify(mockPayoutDetailsSearchRepository, times(1)).deleteById(payoutDetails.getId());
    }

    @Test
    @Transactional
    public void searchPayoutDetails() throws Exception {
        // Initialize the database
        payoutDetailsService.save(payoutDetails);
        when(mockPayoutDetailsSearchRepository.search(queryStringQuery("id:" + payoutDetails.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payoutDetails), PageRequest.of(0, 1), 1));
        // Search the payoutDetails
        restPayoutDetailsMockMvc.perform(get("/api/_search/payout-details?query=id:" + payoutDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payoutDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].minimumAssuredAmount").value(hasItem(DEFAULT_MINIMUM_ASSURED_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumAssuredValidity").value(hasItem(DEFAULT_MINIMUM_ASSURED_VALIDITY.doubleValue())))
            .andExpect(jsonPath("$.[*].maximumPayoutAmount").value(hasItem(DEFAULT_MAXIMUM_PAYOUT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadContract").value(hasItem(DEFAULT_UPLOAD_CONTRACT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.toString())))
            .andExpect(jsonPath("$.[*].payoutRange").value(hasItem(DEFAULT_PAYOUT_RANGE)))
            .andExpect(jsonPath("$.[*].applicableInvoices").value(hasItem(DEFAULT_APPLICABLE_INVOICES)))
            .andExpect(jsonPath("$.[*].materialReduction").value(hasItem(DEFAULT_MATERIAL_REDUCTION)))
            .andExpect(jsonPath("$.[*].exception").value(hasItem(DEFAULT_EXCEPTION)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }
}
