package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.InvoiceDoctorPayout;
import org.nh.artha.repository.InvoiceDoctorPayoutRepository;
import org.nh.artha.repository.search.InvoiceDoctorPayoutSearchRepository;
import org.nh.artha.service.InvoiceDoctorPayoutService;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InvoiceDoctorPayoutResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class InvoiceDoctorPayoutResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_VARIABLE_PAYOUT_ID = 1L;
    private static final Long UPDATED_VARIABLE_PAYOUT_ID = 2L;

    private static final Long DEFAULT_INVOICE_ID = 1L;
    private static final Long UPDATED_INVOICE_ID = 2L;

    private static final Long DEFAULT_INVOICE_LINE_ITEM_ID = 1L;
    private static final Long UPDATED_INVOICE_LINE_ITEM_ID = 2L;

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;

    private static final Long DEFAULT_SERVICE_BENEFIET_ID = 1L;
    private static final Long UPDATED_SERVICE_BENEFIET_ID = 2L;

    private static final Double DEFAULT_SERVICE_BENEFIET_AMOUNT = 1.0;
    private static final Double UPDATED_SERVICE_BENEFIET_AMOUNT = 2.0;

    private static final String DEFAULT_INVOICE_NUMBER = "1";
    private static final String UPDATED_INVOICE_NUMBER = "2";

    private static final String DEFAULT_MRN = "AAAAAAAAAA";
    private static final String UPDATED_MRN = "BBBBBBBBBB";

    private static final String DEFAULT_VISIT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VISIT_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ORDER_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ISSUE_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_INVOICE_APPROVED_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INVOICE_APPROVED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private InvoiceDoctorPayoutRepository invoiceDoctorPayoutRepository;

    @Autowired
    private InvoiceDoctorPayoutService invoiceDoctorPayoutService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.InvoiceDoctorPayoutSearchRepositoryMockConfiguration
     */
    @Autowired
    private InvoiceDoctorPayoutSearchRepository mockInvoiceDoctorPayoutSearchRepository;

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

    private MockMvc restInvoiceDoctorPayoutMockMvc;

    private InvoiceDoctorPayout invoiceDoctorPayout;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceDoctorPayoutResource invoiceDoctorPayoutResource = new InvoiceDoctorPayoutResource(invoiceDoctorPayoutService);
        this.restInvoiceDoctorPayoutMockMvc = MockMvcBuilders.standaloneSetup(invoiceDoctorPayoutResource)
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
    public static InvoiceDoctorPayout createEntity(EntityManager em) {
        InvoiceDoctorPayout invoiceDoctorPayout = new InvoiceDoctorPayout()
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .variablePayoutId(DEFAULT_VARIABLE_PAYOUT_ID)
            .invoiceId(DEFAULT_INVOICE_ID)
            .invoiceLineItemId(DEFAULT_INVOICE_LINE_ITEM_ID)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .serviceBenefietId(DEFAULT_SERVICE_BENEFIET_ID)
            .serviceBenefietAmount(DEFAULT_SERVICE_BENEFIET_AMOUNT)
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .mrn(DEFAULT_MRN)
            .visitType(DEFAULT_VISIT_TYPE)
            .orderDateTime(DEFAULT_ORDER_DATE_TIME)
            .issueDateTime(DEFAULT_ISSUE_DATE_TIME)
            .invoiceApprovedDateTime(DEFAULT_INVOICE_APPROVED_DATE_TIME);
        return invoiceDoctorPayout;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceDoctorPayout createUpdatedEntity(EntityManager em) {
        InvoiceDoctorPayout invoiceDoctorPayout = new InvoiceDoctorPayout()
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .variablePayoutId(UPDATED_VARIABLE_PAYOUT_ID)
            .invoiceId(UPDATED_INVOICE_ID)
            .invoiceLineItemId(UPDATED_INVOICE_LINE_ITEM_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .serviceBenefietId(UPDATED_SERVICE_BENEFIET_ID)
            .serviceBenefietAmount(UPDATED_SERVICE_BENEFIET_AMOUNT)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .mrn(UPDATED_MRN)
            .visitType(UPDATED_VISIT_TYPE)
            .orderDateTime(UPDATED_ORDER_DATE_TIME)
            .issueDateTime(UPDATED_ISSUE_DATE_TIME)
            .invoiceApprovedDateTime(UPDATED_INVOICE_APPROVED_DATE_TIME);
        return invoiceDoctorPayout;
    }

    @BeforeEach
    public void initTest() {
        invoiceDoctorPayout = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceDoctorPayout() throws Exception {
        int databaseSizeBeforeCreate = invoiceDoctorPayoutRepository.findAll().size();

        // Create the InvoiceDoctorPayout
        restInvoiceDoctorPayoutMockMvc.perform(post("/api/invoice-doctor-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDoctorPayout)))
            .andExpect(status().isCreated());

        // Validate the InvoiceDoctorPayout in the database
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = invoiceDoctorPayoutRepository.findAll();
        assertThat(invoiceDoctorPayoutList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceDoctorPayout testInvoiceDoctorPayout = invoiceDoctorPayoutList.get(invoiceDoctorPayoutList.size() - 1);
        assertThat(testInvoiceDoctorPayout.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInvoiceDoctorPayout.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoiceDoctorPayout.getVariablePayoutId()).isEqualTo(DEFAULT_VARIABLE_PAYOUT_ID);
        assertThat(testInvoiceDoctorPayout.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testInvoiceDoctorPayout.getInvoiceLineItemId()).isEqualTo(DEFAULT_INVOICE_LINE_ITEM_ID);
        assertThat(testInvoiceDoctorPayout.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testInvoiceDoctorPayout.getServiceBenefietId()).isEqualTo(DEFAULT_SERVICE_BENEFIET_ID);
        assertThat(testInvoiceDoctorPayout.getServiceBenefietAmount()).isEqualTo(DEFAULT_SERVICE_BENEFIET_AMOUNT);
        assertThat(testInvoiceDoctorPayout.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoiceDoctorPayout.getMrn()).isEqualTo(DEFAULT_MRN);
        assertThat(testInvoiceDoctorPayout.getVisitType()).isEqualTo(DEFAULT_VISIT_TYPE);
        assertThat(testInvoiceDoctorPayout.getOrderDateTime()).isEqualTo(DEFAULT_ORDER_DATE_TIME);
        assertThat(testInvoiceDoctorPayout.getIssueDateTime()).isEqualTo(DEFAULT_ISSUE_DATE_TIME);
        assertThat(testInvoiceDoctorPayout.getInvoiceApprovedDateTime()).isEqualTo(DEFAULT_INVOICE_APPROVED_DATE_TIME);

        // Validate the InvoiceDoctorPayout in Elasticsearch
        verify(mockInvoiceDoctorPayoutSearchRepository, times(1)).save(testInvoiceDoctorPayout);
    }

    @Test
    @Transactional
    public void createInvoiceDoctorPayoutWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceDoctorPayoutRepository.findAll().size();

        // Create the InvoiceDoctorPayout with an existing ID
        invoiceDoctorPayout.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceDoctorPayoutMockMvc.perform(post("/api/invoice-doctor-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDoctorPayout)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDoctorPayout in the database
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = invoiceDoctorPayoutRepository.findAll();
        assertThat(invoiceDoctorPayoutList).hasSize(databaseSizeBeforeCreate);

        // Validate the InvoiceDoctorPayout in Elasticsearch
        verify(mockInvoiceDoctorPayoutSearchRepository, times(0)).save(invoiceDoctorPayout);
    }


    @Test
    @Transactional
    public void getAllInvoiceDoctorPayouts() throws Exception {
        // Initialize the database
        invoiceDoctorPayoutRepository.saveAndFlush(invoiceDoctorPayout);

        // Get all the invoiceDoctorPayoutList
        restInvoiceDoctorPayoutMockMvc.perform(get("/api/invoice-doctor-payouts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceDoctorPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].variablePayoutId").value(hasItem(DEFAULT_VARIABLE_PAYOUT_ID.intValue())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].invoiceLineItemId").value(hasItem(DEFAULT_INVOICE_LINE_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceBenefietId").value(hasItem(DEFAULT_SERVICE_BENEFIET_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceBenefietAmount").value(hasItem(DEFAULT_SERVICE_BENEFIET_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].mrn").value(hasItem(DEFAULT_MRN)))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE)))
            .andExpect(jsonPath("$.[*].orderDateTime").value(hasItem(DEFAULT_ORDER_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].issueDateTime").value(hasItem(DEFAULT_ISSUE_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].invoiceApprovedDateTime").value(hasItem(DEFAULT_INVOICE_APPROVED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void getInvoiceDoctorPayout() throws Exception {
        // Initialize the database
        invoiceDoctorPayoutRepository.saveAndFlush(invoiceDoctorPayout);

        // Get the invoiceDoctorPayout
        restInvoiceDoctorPayoutMockMvc.perform(get("/api/invoice-doctor-payouts/{id}", invoiceDoctorPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceDoctorPayout.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.variablePayoutId").value(DEFAULT_VARIABLE_PAYOUT_ID.intValue()))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID.intValue()))
            .andExpect(jsonPath("$.invoiceLineItemId").value(DEFAULT_INVOICE_LINE_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.serviceBenefietId").value(DEFAULT_SERVICE_BENEFIET_ID.intValue()))
            .andExpect(jsonPath("$.serviceBenefietAmount").value(DEFAULT_SERVICE_BENEFIET_AMOUNT.intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.mrn").value(DEFAULT_MRN))
            .andExpect(jsonPath("$.visitType").value(DEFAULT_VISIT_TYPE))
            .andExpect(jsonPath("$.orderDateTime").value(DEFAULT_ORDER_DATE_TIME.toString()))
            .andExpect(jsonPath("$.issueDateTime").value(DEFAULT_ISSUE_DATE_TIME.toString()))
            .andExpect(jsonPath("$.invoiceApprovedDateTime").value(DEFAULT_INVOICE_APPROVED_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoiceDoctorPayout() throws Exception {
        // Get the invoiceDoctorPayout
        restInvoiceDoctorPayoutMockMvc.perform(get("/api/invoice-doctor-payouts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceDoctorPayout() throws Exception {
        // Initialize the database
        invoiceDoctorPayoutService.save(invoiceDoctorPayout);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockInvoiceDoctorPayoutSearchRepository);

        int databaseSizeBeforeUpdate = invoiceDoctorPayoutRepository.findAll().size();

        // Update the invoiceDoctorPayout
        InvoiceDoctorPayout updatedInvoiceDoctorPayout = invoiceDoctorPayoutRepository.findById(invoiceDoctorPayout.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceDoctorPayout are not directly saved in db
        em.detach(updatedInvoiceDoctorPayout);
        updatedInvoiceDoctorPayout
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .variablePayoutId(UPDATED_VARIABLE_PAYOUT_ID)
            .invoiceId(UPDATED_INVOICE_ID)
            .invoiceLineItemId(UPDATED_INVOICE_LINE_ITEM_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .serviceBenefietId(UPDATED_SERVICE_BENEFIET_ID)
            .serviceBenefietAmount(UPDATED_SERVICE_BENEFIET_AMOUNT)
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .mrn(UPDATED_MRN)
            .visitType(UPDATED_VISIT_TYPE)
            .orderDateTime(UPDATED_ORDER_DATE_TIME)
            .issueDateTime(UPDATED_ISSUE_DATE_TIME)
            .invoiceApprovedDateTime(UPDATED_INVOICE_APPROVED_DATE_TIME);

        restInvoiceDoctorPayoutMockMvc.perform(put("/api/invoice-doctor-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceDoctorPayout)))
            .andExpect(status().isOk());

        // Validate the InvoiceDoctorPayout in the database
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = invoiceDoctorPayoutRepository.findAll();
        assertThat(invoiceDoctorPayoutList).hasSize(databaseSizeBeforeUpdate);
        InvoiceDoctorPayout testInvoiceDoctorPayout = invoiceDoctorPayoutList.get(invoiceDoctorPayoutList.size() - 1);
        assertThat(testInvoiceDoctorPayout.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInvoiceDoctorPayout.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoiceDoctorPayout.getVariablePayoutId()).isEqualTo(UPDATED_VARIABLE_PAYOUT_ID);
        assertThat(testInvoiceDoctorPayout.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testInvoiceDoctorPayout.getInvoiceLineItemId()).isEqualTo(UPDATED_INVOICE_LINE_ITEM_ID);
        assertThat(testInvoiceDoctorPayout.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testInvoiceDoctorPayout.getServiceBenefietId()).isEqualTo(UPDATED_SERVICE_BENEFIET_ID);
        assertThat(testInvoiceDoctorPayout.getServiceBenefietAmount()).isEqualTo(UPDATED_SERVICE_BENEFIET_AMOUNT);
        assertThat(testInvoiceDoctorPayout.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoiceDoctorPayout.getMrn()).isEqualTo(UPDATED_MRN);
        assertThat(testInvoiceDoctorPayout.getVisitType()).isEqualTo(UPDATED_VISIT_TYPE);
        assertThat(testInvoiceDoctorPayout.getOrderDateTime()).isEqualTo(UPDATED_ORDER_DATE_TIME);
        assertThat(testInvoiceDoctorPayout.getIssueDateTime()).isEqualTo(UPDATED_ISSUE_DATE_TIME);
        assertThat(testInvoiceDoctorPayout.getInvoiceApprovedDateTime()).isEqualTo(UPDATED_INVOICE_APPROVED_DATE_TIME);

        // Validate the InvoiceDoctorPayout in Elasticsearch
        verify(mockInvoiceDoctorPayoutSearchRepository, times(1)).save(testInvoiceDoctorPayout);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceDoctorPayout() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDoctorPayoutRepository.findAll().size();

        // Create the InvoiceDoctorPayout

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceDoctorPayoutMockMvc.perform(put("/api/invoice-doctor-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDoctorPayout)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDoctorPayout in the database
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = invoiceDoctorPayoutRepository.findAll();
        assertThat(invoiceDoctorPayoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InvoiceDoctorPayout in Elasticsearch
        verify(mockInvoiceDoctorPayoutSearchRepository, times(0)).save(invoiceDoctorPayout);
    }

    @Test
    @Transactional
    public void deleteInvoiceDoctorPayout() throws Exception {
        // Initialize the database
        invoiceDoctorPayoutService.save(invoiceDoctorPayout);

        int databaseSizeBeforeDelete = invoiceDoctorPayoutRepository.findAll().size();

        // Delete the invoiceDoctorPayout
        restInvoiceDoctorPayoutMockMvc.perform(delete("/api/invoice-doctor-payouts/{id}", invoiceDoctorPayout.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceDoctorPayout> invoiceDoctorPayoutList = invoiceDoctorPayoutRepository.findAll();
        assertThat(invoiceDoctorPayoutList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InvoiceDoctorPayout in Elasticsearch
        verify(mockInvoiceDoctorPayoutSearchRepository, times(1)).deleteById(invoiceDoctorPayout.getId());
    }

    @Test
    @Transactional
    public void searchInvoiceDoctorPayout() throws Exception {
        // Initialize the database
        invoiceDoctorPayoutService.save(invoiceDoctorPayout);
        when(mockInvoiceDoctorPayoutSearchRepository.search(queryStringQuery("id:" + invoiceDoctorPayout.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(invoiceDoctorPayout), PageRequest.of(0, 1), 1));
        // Search the invoiceDoctorPayout
        restInvoiceDoctorPayoutMockMvc.perform(get("/api/_search/invoice-doctor-payouts?query=id:" + invoiceDoctorPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceDoctorPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].variablePayoutId").value(hasItem(DEFAULT_VARIABLE_PAYOUT_ID.intValue())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].invoiceLineItemId").value(hasItem(DEFAULT_INVOICE_LINE_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceBenefietId").value(hasItem(DEFAULT_SERVICE_BENEFIET_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceBenefietAmount").value(hasItem(DEFAULT_SERVICE_BENEFIET_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].mrn").value(hasItem(DEFAULT_MRN)))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE)))
            .andExpect(jsonPath("$.[*].orderDateTime").value(hasItem(DEFAULT_ORDER_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].issueDateTime").value(hasItem(DEFAULT_ISSUE_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].invoiceApprovedDateTime").value(hasItem(DEFAULT_INVOICE_APPROVED_DATE_TIME.toString())));
    }
}
