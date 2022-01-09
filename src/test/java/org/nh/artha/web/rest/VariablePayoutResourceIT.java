package org.nh.artha.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.VariablePayout;
import org.nh.artha.domain.dto.UserDTO;
import org.nh.artha.domain.enumeration.ChangeRequestStatus;
import org.nh.artha.repository.VariablePayoutRepository;
import org.nh.artha.repository.search.VariablePayoutSearchRepository;
import org.nh.artha.service.VariablePayoutService;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.nh.artha.web.rest.TestUtil.createFormattingConversionService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Integration tests for the {@link VariablePayoutResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class VariablePayoutResourceIT {

    private static final String DEFAULT_UNIT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final UserDTO DEFAULT_CREATED_BY = new UserDTO(1l, "AAAAAAAAAA");
    private static final UserDTO UPDATED_CREATED_BY = new UserDTO(2l,"BBBBBBBBBB");

    private static final LocalDateTime DEFAULT_CREATED_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CREATED_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final ChangeRequestStatus DEFAULT_CHANGE_REQUEST_STATUS = ChangeRequestStatus.DRAFT;
    private static final ChangeRequestStatus UPDATED_CHANGE_REQUEST_STATUS = ChangeRequestStatus.PENDING_APPROVAL;

    private static final Long DEFAULT_STARTING_VERSION = 1l;
    private static final Long UPDATED_STARTING_VERSION = 2l;

    private static final Long DEFAULT_CURRENT_VERSION = 1l;
    private static final Long UPDATED_CURRENT_VERSION = 2l;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final LocalDateTime DEFAULT_COMMENCEMENT_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_COMMENCEMENT_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final LocalDateTime DEFAULT_CONTRACT_END_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CONTRACT_END_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_MIN_ASSURED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MIN_ASSURED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MAX_PAYOUT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_PAYOUT_AMOUNT = new BigDecimal(2);

    private static final LocalDateTime DEFAULT_MIN_ASSURED_VALIDITY_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_MIN_ASSURED_VALIDITY_DATE = LocalDateTime.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String DEFAULT_UPLOAD_CONTRACT = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_CONTRACT = "BBBBBBBBBB";

    @Autowired
    private VariablePayoutRepository variablePayoutRepository;

    @Autowired
    private VariablePayoutService variablePayoutService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.VariablePayoutSearchRepositoryMockConfiguration
     */
    @Autowired
    private VariablePayoutSearchRepository mockVariablePayoutSearchRepository;

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

    private MockMvc restVariablePayoutMockMvc;

    @Autowired
    private ApplicationProperties applicationProperties;

    private VariablePayout variablePayout;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VariablePayoutResource variablePayoutResource = new VariablePayoutResource(variablePayoutService,variablePayoutRepository,applicationProperties,mockVariablePayoutSearchRepository);
        this.restVariablePayoutMockMvc = MockMvcBuilders.standaloneSetup(variablePayoutResource)
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
    public static VariablePayout createEntity(EntityManager em) {
        VariablePayout variablePayout = new VariablePayout()
            .unitCode(DEFAULT_UNIT_CODE)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .version(DEFAULT_VERSION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .changeRequestStatus(DEFAULT_CHANGE_REQUEST_STATUS)
            .startingVersion(DEFAULT_STARTING_VERSION)
            .currentVersion(DEFAULT_CURRENT_VERSION)
            .remarks(DEFAULT_REMARKS)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE)
            .contractEndDate(DEFAULT_CONTRACT_END_DATE)
            .minAssuredAmount(DEFAULT_MIN_ASSURED_AMOUNT)
            .maxPayoutAmount(DEFAULT_MAX_PAYOUT_AMOUNT)
            .minAssuredValidityDate(DEFAULT_MIN_ASSURED_VALIDITY_DATE)
            .status(DEFAULT_STATUS)
            .uploadContract(DEFAULT_UPLOAD_CONTRACT);
        return variablePayout;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VariablePayout createUpdatedEntity(EntityManager em) {
        VariablePayout variablePayout = new VariablePayout()
            .unitCode(UPDATED_UNIT_CODE)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .changeRequestStatus(UPDATED_CHANGE_REQUEST_STATUS)
            .startingVersion(UPDATED_STARTING_VERSION)
            .currentVersion(UPDATED_CURRENT_VERSION)
            .remarks(UPDATED_REMARKS)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .minAssuredAmount(UPDATED_MIN_ASSURED_AMOUNT)
            .maxPayoutAmount(UPDATED_MAX_PAYOUT_AMOUNT)
            .minAssuredValidityDate(UPDATED_MIN_ASSURED_VALIDITY_DATE)
            .status(UPDATED_STATUS)
            .uploadContract(UPDATED_UPLOAD_CONTRACT);
        return variablePayout;
    }

    @BeforeEach
    public void initTest() {
        variablePayout = createEntity(em);
    }

    @Test
    @Transactional
    public void createVariablePayout() throws Exception {
        int databaseSizeBeforeCreate = variablePayoutRepository.findAll().size();

        // Create the VariablePayout
        restVariablePayoutMockMvc.perform(post("/api/variable-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variablePayout)))
            .andExpect(status().isCreated());

        // Validate the VariablePayout in the database
        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAll();
        assertThat(variablePayoutList).hasSize(databaseSizeBeforeCreate + 1);
        VariablePayout testVariablePayout = variablePayoutList.get(variablePayoutList.size() - 1);
        assertThat(testVariablePayout.getUnitCode()).isEqualTo(DEFAULT_UNIT_CODE);
        assertThat(testVariablePayout.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testVariablePayout.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testVariablePayout.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testVariablePayout.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testVariablePayout.getChangeRequestStatus()).isEqualTo(DEFAULT_CHANGE_REQUEST_STATUS);
        assertThat(testVariablePayout.getStartingVersion()).isEqualTo(DEFAULT_STARTING_VERSION);
        assertThat(testVariablePayout.getCurrentVersion()).isEqualTo(DEFAULT_CURRENT_VERSION);
        assertThat(testVariablePayout.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testVariablePayout.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testVariablePayout.getContractEndDate()).isEqualTo(DEFAULT_CONTRACT_END_DATE);
        assertThat(testVariablePayout.getMinAssuredAmount()).isEqualTo(DEFAULT_MIN_ASSURED_AMOUNT);
        assertThat(testVariablePayout.getMaxPayoutAmount()).isEqualTo(DEFAULT_MAX_PAYOUT_AMOUNT);
        assertThat(testVariablePayout.getMinAssuredValidityDate()).isEqualTo(DEFAULT_MIN_ASSURED_VALIDITY_DATE);
        assertThat(testVariablePayout.isStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVariablePayout.getUploadContract()).isEqualTo(DEFAULT_UPLOAD_CONTRACT);

        // Validate the VariablePayout in Elasticsearch
        verify(mockVariablePayoutSearchRepository, times(1)).save(testVariablePayout);
    }

    @Test
    @Transactional
    public void createVariablePayoutWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = variablePayoutRepository.findAll().size();

        // Create the VariablePayout with an existing ID
        variablePayout.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVariablePayoutMockMvc.perform(post("/api/variable-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variablePayout)))
            .andExpect(status().isBadRequest());

        // Validate the VariablePayout in the database
        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAll();
        assertThat(variablePayoutList).hasSize(databaseSizeBeforeCreate);

        // Validate the VariablePayout in Elasticsearch
        verify(mockVariablePayoutSearchRepository, times(0)).save(variablePayout);
    }


    @Test
    @Transactional
    public void checkEmployeeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = variablePayoutRepository.findAll().size();
        // set the field null
        variablePayout.setEmployeeId(null);

        // Create the VariablePayout, which fails.

        restVariablePayoutMockMvc.perform(post("/api/variable-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variablePayout)))
            .andExpect(status().isBadRequest());

        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAll();
        assertThat(variablePayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCommencementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = variablePayoutRepository.findAll().size();
        // set the field null
        variablePayout.setCommencementDate(null);

        // Create the VariablePayout, which fails.

        restVariablePayoutMockMvc.perform(post("/api/variable-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variablePayout)))
            .andExpect(status().isBadRequest());

        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAll();
        assertThat(variablePayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContractEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = variablePayoutRepository.findAll().size();
        // set the field null
        variablePayout.setContractEndDate(null);

        // Create the VariablePayout, which fails.

        restVariablePayoutMockMvc.perform(post("/api/variable-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variablePayout)))
            .andExpect(status().isBadRequest());

        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAll();
        assertThat(variablePayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVariablePayouts() throws Exception {
        // Initialize the database
        variablePayoutRepository.saveAndFlush(variablePayout);

        // Get all the variablePayoutList
        restVariablePayoutMockMvc.perform(get("/api/variable-payouts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(variablePayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitCode").value(hasItem(DEFAULT_UNIT_CODE)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].changeRequestStatus").value(hasItem(DEFAULT_CHANGE_REQUEST_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startingVersion").value(hasItem(DEFAULT_STARTING_VERSION)))
            .andExpect(jsonPath("$.[*].currentVersion").value(hasItem(DEFAULT_CURRENT_VERSION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractEndDate").value(hasItem(DEFAULT_CONTRACT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].minAssuredAmount").value(hasItem(DEFAULT_MIN_ASSURED_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].maxPayoutAmount").value(hasItem(DEFAULT_MAX_PAYOUT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].minAssuredValidityDate").value(hasItem(DEFAULT_MIN_ASSURED_VALIDITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadContract").value(hasItem(DEFAULT_UPLOAD_CONTRACT)));
    }

    @Test
    @Transactional
    public void getVariablePayout() throws Exception {
        // Initialize the database
        variablePayoutRepository.saveAndFlush(variablePayout);

        // Get the variablePayout
        restVariablePayoutMockMvc.perform(get("/api/variable-payouts/{id}", variablePayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(variablePayout.getId().intValue()))
            .andExpect(jsonPath("$.unitCode").value(DEFAULT_UNIT_CODE))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.changeRequestStatus").value(DEFAULT_CHANGE_REQUEST_STATUS.toString()))
            .andExpect(jsonPath("$.startingVersion").value(DEFAULT_STARTING_VERSION))
            .andExpect(jsonPath("$.currentVersion").value(DEFAULT_CURRENT_VERSION))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE.toString()))
            .andExpect(jsonPath("$.contractEndDate").value(DEFAULT_CONTRACT_END_DATE.toString()))
            .andExpect(jsonPath("$.minAssuredAmount").value(DEFAULT_MIN_ASSURED_AMOUNT.intValue()))
            .andExpect(jsonPath("$.maxPayoutAmount").value(DEFAULT_MAX_PAYOUT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.minAssuredValidityDate").value(DEFAULT_MIN_ASSURED_VALIDITY_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.uploadContract").value(DEFAULT_UPLOAD_CONTRACT));
    }

    @Test
    @Transactional
    public void getNonExistingVariablePayout() throws Exception {
        // Get the variablePayout
        restVariablePayoutMockMvc.perform(get("/api/variable-payouts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVariablePayout() throws Exception {
        // Initialize the database
        variablePayoutService.save(variablePayout);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockVariablePayoutSearchRepository);

        int databaseSizeBeforeUpdate = variablePayoutRepository.findAll().size();

        // Update the variablePayout
        VariablePayout updatedVariablePayout = variablePayoutRepository.findById(variablePayout.getId()).get();
        // Disconnect from session so that the updates on updatedVariablePayout are not directly saved in db
        em.detach(updatedVariablePayout);
        updatedVariablePayout
            .unitCode(UPDATED_UNIT_CODE)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .version(UPDATED_VERSION)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .changeRequestStatus(UPDATED_CHANGE_REQUEST_STATUS)
            .startingVersion(UPDATED_STARTING_VERSION)
            .currentVersion(UPDATED_CURRENT_VERSION)
            .remarks(UPDATED_REMARKS)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .contractEndDate(UPDATED_CONTRACT_END_DATE)
            .minAssuredAmount(UPDATED_MIN_ASSURED_AMOUNT)
            .maxPayoutAmount(UPDATED_MAX_PAYOUT_AMOUNT)
            .minAssuredValidityDate(UPDATED_MIN_ASSURED_VALIDITY_DATE)
            .status(UPDATED_STATUS)
            .uploadContract(UPDATED_UPLOAD_CONTRACT);

        restVariablePayoutMockMvc.perform(put("/api/variable-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVariablePayout)))
            .andExpect(status().isOk());

        // Validate the VariablePayout in the database
        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAll();
        assertThat(variablePayoutList).hasSize(databaseSizeBeforeUpdate);
        VariablePayout testVariablePayout = variablePayoutList.get(variablePayoutList.size() - 1);
        assertThat(testVariablePayout.getUnitCode()).isEqualTo(UPDATED_UNIT_CODE);
        assertThat(testVariablePayout.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testVariablePayout.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testVariablePayout.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVariablePayout.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testVariablePayout.getChangeRequestStatus()).isEqualTo(UPDATED_CHANGE_REQUEST_STATUS);
        assertThat(testVariablePayout.getStartingVersion()).isEqualTo(UPDATED_STARTING_VERSION);
        assertThat(testVariablePayout.getCurrentVersion()).isEqualTo(UPDATED_CURRENT_VERSION);
        assertThat(testVariablePayout.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testVariablePayout.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testVariablePayout.getContractEndDate()).isEqualTo(UPDATED_CONTRACT_END_DATE);
        assertThat(testVariablePayout.getMinAssuredAmount()).isEqualTo(UPDATED_MIN_ASSURED_AMOUNT);
        assertThat(testVariablePayout.getMaxPayoutAmount()).isEqualTo(UPDATED_MAX_PAYOUT_AMOUNT);
        assertThat(testVariablePayout.getMinAssuredValidityDate()).isEqualTo(UPDATED_MIN_ASSURED_VALIDITY_DATE);
        assertThat(testVariablePayout.isStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVariablePayout.getUploadContract()).isEqualTo(UPDATED_UPLOAD_CONTRACT);

        // Validate the VariablePayout in Elasticsearch
        verify(mockVariablePayoutSearchRepository, times(1)).save(testVariablePayout);
    }

    @Test
    @Transactional
    public void updateNonExistingVariablePayout() throws Exception {
        int databaseSizeBeforeUpdate = variablePayoutRepository.findAll().size();

        // Create the VariablePayout

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVariablePayoutMockMvc.perform(put("/api/variable-payouts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(variablePayout)))
            .andExpect(status().isBadRequest());

        // Validate the VariablePayout in the database
        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAll();
        assertThat(variablePayoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VariablePayout in Elasticsearch
        verify(mockVariablePayoutSearchRepository, times(0)).save(variablePayout);
    }

    @Test
    @Transactional
    public void deleteVariablePayout() throws Exception {
        // Initialize the database
        variablePayoutService.save(variablePayout);

        int databaseSizeBeforeDelete = variablePayoutRepository.findAll().size();

        // Delete the variablePayout
        restVariablePayoutMockMvc.perform(delete("/api/variable-payouts/{id}", variablePayout.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VariablePayout> variablePayoutList = variablePayoutRepository.findAll();
        assertThat(variablePayoutList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the VariablePayout in Elasticsearch
        verify(mockVariablePayoutSearchRepository, times(1)).deleteById(variablePayout.getId());
    }

    @Test
    @Transactional
    public void searchVariablePayout() throws Exception {
        // Initialize the database
        variablePayoutService.save(variablePayout);
        when(mockVariablePayoutSearchRepository.search(queryStringQuery("id:" + variablePayout.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(variablePayout), PageRequest.of(0, 1), 1));
        // Search the variablePayout
        restVariablePayoutMockMvc.perform(get("/api/_search/variable-payouts?query=id:" + variablePayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(variablePayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitCode").value(hasItem(DEFAULT_UNIT_CODE)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].changeRequestStatus").value(hasItem(DEFAULT_CHANGE_REQUEST_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startingVersion").value(hasItem(DEFAULT_STARTING_VERSION)))
            .andExpect(jsonPath("$.[*].currentVersion").value(hasItem(DEFAULT_CURRENT_VERSION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].contractEndDate").value(hasItem(DEFAULT_CONTRACT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].minAssuredAmount").value(hasItem(DEFAULT_MIN_ASSURED_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].maxPayoutAmount").value(hasItem(DEFAULT_MAX_PAYOUT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].minAssuredValidityDate").value(hasItem(DEFAULT_MIN_ASSURED_VALIDITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadContract").value(hasItem(DEFAULT_UPLOAD_CONTRACT)));
    }
}
