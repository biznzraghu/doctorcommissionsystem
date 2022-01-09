package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.*;
import org.nh.artha.domain.dto.CodeNameDto;
import org.nh.artha.domain.dto.RuleKeyDTO;
import org.nh.artha.domain.enumeration.*;
import org.nh.artha.repository.ServiceItemBenefitRepository;
import org.nh.artha.repository.search.ServiceItemBenefitSearchRepository;
import org.nh.artha.service.ServiceItemBenefitService;
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
 * Integration tests for the {@link ServiceItemBenefitResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class ServiceItemBenefitResourceIT {

    private static final Type DEFAULT_TYPE = Type.ALL_SERVICES;
    private static final Type UPDATED_TYPE = Type.SERVICE_TYPE;

    private static final RuleKeyDTO DEFAULT_RULE_KEY = null;
    private static final RuleKeyDTO UPDATED_RULE_KEY = null;

    private static final List<Group> DEFAULT_SERVICE_GROUP = null;
    private static final List<Group> UPDATED_SERVICE_GROUP = null;

    private static final ServiceType DEFAULT_SERVICE_TYPE = null;
    private static final ServiceType UPDATED_SERVICE_TYPE = null;

    private static final ItemCategory DEFAULT_ITEM_CATEGORY = null;
    private static final ItemCategory UPDATED_ITEM_CATEGORY = null;

    private static final ItemGroup DEFAULT_ITEM_GROUP = null;
    private static final ItemGroup UPDATED_ITEM_GROUP = null;

    private static final PackageCategory DEFAULT_PACKAGE_CATEGORY = PackageCategory.SURGICAL;
    private static final PackageCategory UPDATED_PACKAGE_CATEGORY = PackageCategory.SURGICAL;

    private static final List<String> DEFAULT_VISIT_TYPE = null;
    private static final List<String> UPDATED_VISIT_TYPE = null;

    private static final CodeNameDto DEFAULT_COMPONENTS = null;
    private static final CodeNameDto UPDATED_COMPONENTS = null;

    private static final Beneficiary DEFAULT_BENEFICIARY = Beneficiary.ORDERING;
    private static final Beneficiary UPDATED_BENEFICIARY = Beneficiary.RENDERING;

    private static final PaymentMode DEFAULT_PAYMENT_MODE = PaymentMode.PERCENTAGE;
    private static final PaymentMode UPDATED_PAYMENT_MODE = PaymentMode.AMOUNT;

    private static final BigDecimal DEFAULT_PAYMENT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_VALUE = new BigDecimal(2);

    private static final Integer DEFAULT_MIN_QUANTITY = 1;
    private static final Integer UPDATED_MIN_QUANTITY = 2;

    private static final Integer DEFAULT_MAX_QUANTITY = 1;
    private static final Integer UPDATED_MAX_QUANTITY = 2;

    private static final String DEFAULT_AMOUNT = "0";
    private static final String UPDATED_AMOUNT = "1";

    private static final ApplicableOn DEFAULT_APPLICABLE_ON = ApplicableOn.NET;
    private static final ApplicableOn UPDATED_APPLICABLE_ON = ApplicableOn.GROSS;

    private static final PatientCategory DEFAULT_PATIENT_CATEGORY = PatientCategory.CASH;
    private static final PatientCategory UPDATED_PATIENT_CATEGORY = PatientCategory.CREDIT;

    private static final List<ValueSetCode> DEFAULT_TARIFF_CLASS = null;
    private static final List<ValueSetCode> UPDATED_TARIFF_CLASS = null;

    private static final MaterialAmount DEFAULT_MATERIAL_AMOUNT = MaterialAmount.COST;
    private static final MaterialAmount UPDATED_MATERIAL_AMOUNT = MaterialAmount.SALE;

    private static final List<Department> DEFAULT_DEPARTMENT = null;
    private static final List<Department> UPDATED_DEPARTMENT = null;

    private static final Long DEFAULT_APPLICABLE_SPONSOR = 1L;
    private static final Long UPDATED_APPLICABLE_SPONSOR = 2L;

    private static final Long DEFAULT_EXEMPTED_SPONSOR = 1L;
    private static final Long UPDATED_EXEMPTED_SPONSOR = 2L;

    private static final Long DEFAULT_SERVICE_EXCEPTION = 1L;
    private static final Long UPDATED_SERVICE_EXCEPTION = 2L;

    private static final Long DEFAULT_ITEM_EXCEPTION = 1L;
    private static final Long UPDATED_ITEM_EXCEPTION = 2L;

    private static final Boolean DEFAULT_ON_DEATH_INCENTIVE = false;
    private static final Boolean UPDATED_ON_DEATH_INCENTIVE = true;

    @Autowired
    private ServiceItemBenefitRepository serviceItemBenefitRepository;

    @Autowired
    private ServiceItemBenefitService serviceItemBenefitService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.ServiceItemBenefitSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServiceItemBenefitSearchRepository mockServiceItemBenefitSearchRepository;

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

    @Autowired
    private ApplicationProperties applicationProperties;

    private MockMvc restServiceItemBenefitMockMvc;

    private ServiceItemBenefit serviceItemBenefit;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceItemBenefitResource serviceItemBenefitResource = new ServiceItemBenefitResource(serviceItemBenefitService,serviceItemBenefitRepository,mockServiceItemBenefitSearchRepository,applicationProperties);
        this.restServiceItemBenefitMockMvc = MockMvcBuilders.standaloneSetup(serviceItemBenefitResource)
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
    public static ServiceItemBenefit createEntity(EntityManager em) {
        ServiceItemBenefit serviceItemBenefit = new ServiceItemBenefit()
            .type(DEFAULT_TYPE)
            .rule_key(DEFAULT_RULE_KEY)
            .serviceGroup(DEFAULT_SERVICE_GROUP)
            .serviceType(DEFAULT_SERVICE_TYPE)
            .itemCategory(DEFAULT_ITEM_CATEGORY)
            .itemGroup(DEFAULT_ITEM_GROUP)
            .packageCategory(DEFAULT_PACKAGE_CATEGORY)
            .visitType(DEFAULT_VISIT_TYPE)
            .components(DEFAULT_COMPONENTS)
            .beneficiary(DEFAULT_BENEFICIARY)
            .paymentMode(DEFAULT_PAYMENT_MODE)
            .paymentValue(DEFAULT_PAYMENT_VALUE)
            .minQuantity(DEFAULT_MIN_QUANTITY)
            .maxQuantity(DEFAULT_MAX_QUANTITY)
            .amount(DEFAULT_AMOUNT)
            .applicableOn(DEFAULT_APPLICABLE_ON)
            .patientCategory(DEFAULT_PATIENT_CATEGORY)
            .tariffClass(DEFAULT_TARIFF_CLASS)
            .materialAmount(DEFAULT_MATERIAL_AMOUNT)
            .department(DEFAULT_DEPARTMENT)
            .applicableSponsor(DEFAULT_APPLICABLE_SPONSOR)
            .exemptedSponsor(DEFAULT_EXEMPTED_SPONSOR)
            .serviceException(DEFAULT_SERVICE_EXCEPTION)
            .itemException(DEFAULT_ITEM_EXCEPTION)
            .onDeathIncentive(DEFAULT_ON_DEATH_INCENTIVE);
        return serviceItemBenefit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceItemBenefit createUpdatedEntity(EntityManager em) {
        ServiceItemBenefit serviceItemBenefit = new ServiceItemBenefit()
            .type(UPDATED_TYPE)
            .rule_key(UPDATED_RULE_KEY)
            .serviceGroup(UPDATED_SERVICE_GROUP)
            .serviceType(UPDATED_SERVICE_TYPE)
            .itemCategory(UPDATED_ITEM_CATEGORY)
            .itemGroup(UPDATED_ITEM_GROUP)
            .packageCategory(UPDATED_PACKAGE_CATEGORY)
            .visitType(UPDATED_VISIT_TYPE)
            .components(UPDATED_COMPONENTS)
            .beneficiary(UPDATED_BENEFICIARY)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .paymentValue(UPDATED_PAYMENT_VALUE)
            .minQuantity(UPDATED_MIN_QUANTITY)
            .maxQuantity(UPDATED_MAX_QUANTITY)
            .amount(UPDATED_AMOUNT)
            .applicableOn(UPDATED_APPLICABLE_ON)
            .patientCategory(UPDATED_PATIENT_CATEGORY)
            .tariffClass(UPDATED_TARIFF_CLASS)
            .materialAmount(UPDATED_MATERIAL_AMOUNT)
            .department(UPDATED_DEPARTMENT)
            .applicableSponsor(UPDATED_APPLICABLE_SPONSOR)
            .exemptedSponsor(UPDATED_EXEMPTED_SPONSOR)
            .serviceException(UPDATED_SERVICE_EXCEPTION)
            .itemException(UPDATED_ITEM_EXCEPTION)
            .onDeathIncentive(UPDATED_ON_DEATH_INCENTIVE);
        return serviceItemBenefit;
    }

    @BeforeEach
    public void initTest() {
        serviceItemBenefit = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceItemBenefit() throws Exception {
        int databaseSizeBeforeCreate = serviceItemBenefitRepository.findAll().size();

        // Create the ServiceItemBenefit
        restServiceItemBenefitMockMvc.perform(post("/api/service-item-benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemBenefit)))
            .andExpect(status().isCreated());

        // Validate the ServiceItemBenefit in the database
        List<ServiceItemBenefit> serviceItemBenefitList = serviceItemBenefitRepository.findAll();
        assertThat(serviceItemBenefitList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceItemBenefit testServiceItemBenefit = serviceItemBenefitList.get(serviceItemBenefitList.size() - 1);
        assertThat(testServiceItemBenefit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testServiceItemBenefit.getRule_key()).isEqualTo(DEFAULT_RULE_KEY);
        assertThat(testServiceItemBenefit.getServiceGroup()).isEqualTo(DEFAULT_SERVICE_GROUP);
        assertThat(testServiceItemBenefit.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testServiceItemBenefit.getItemCategory()).isEqualTo(DEFAULT_ITEM_CATEGORY);
        assertThat(testServiceItemBenefit.getItemGroup()).isEqualTo(DEFAULT_ITEM_GROUP);
        assertThat(testServiceItemBenefit.getPackageCategory()).isEqualTo(DEFAULT_PACKAGE_CATEGORY);
        assertThat(testServiceItemBenefit.getVisitType()).isEqualTo(DEFAULT_VISIT_TYPE);
        assertThat(testServiceItemBenefit.getComponents()).isEqualTo(DEFAULT_COMPONENTS);
        assertThat(testServiceItemBenefit.getBeneficiary()).isEqualTo(DEFAULT_BENEFICIARY);
        assertThat(testServiceItemBenefit.getPaymentMode()).isEqualTo(DEFAULT_PAYMENT_MODE);
        assertThat(testServiceItemBenefit.getPaymentValue()).isEqualTo(DEFAULT_PAYMENT_VALUE);
        assertThat(testServiceItemBenefit.getMinQuantity()).isEqualTo(DEFAULT_MIN_QUANTITY);
        assertThat(testServiceItemBenefit.getMaxQuantity()).isEqualTo(DEFAULT_MAX_QUANTITY);
        assertThat(testServiceItemBenefit.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testServiceItemBenefit.getApplicableOn()).isEqualTo(DEFAULT_APPLICABLE_ON);
        assertThat(testServiceItemBenefit.getPatientCategory()).isEqualTo(DEFAULT_PATIENT_CATEGORY);
        assertThat(testServiceItemBenefit.getTariffClass()).isEqualTo(DEFAULT_TARIFF_CLASS);
        assertThat(testServiceItemBenefit.getMaterialAmount()).isEqualTo(DEFAULT_MATERIAL_AMOUNT);
        assertThat(testServiceItemBenefit.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testServiceItemBenefit.getApplicableSponsor()).isEqualTo(DEFAULT_APPLICABLE_SPONSOR);
        assertThat(testServiceItemBenefit.getExemptedSponsor()).isEqualTo(DEFAULT_EXEMPTED_SPONSOR);
        assertThat(testServiceItemBenefit.getServiceException()).isEqualTo(DEFAULT_SERVICE_EXCEPTION);
        assertThat(testServiceItemBenefit.getItemException()).isEqualTo(DEFAULT_ITEM_EXCEPTION);
        assertThat(testServiceItemBenefit.isOnDeathIncentive()).isEqualTo(DEFAULT_ON_DEATH_INCENTIVE);

        // Validate the ServiceItemBenefit in Elasticsearch
        verify(mockServiceItemBenefitSearchRepository, times(1)).save(testServiceItemBenefit);
    }

    @Test
    @Transactional
    public void createServiceItemBenefitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceItemBenefitRepository.findAll().size();

        // Create the ServiceItemBenefit with an existing ID
        serviceItemBenefit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceItemBenefitMockMvc.perform(post("/api/service-item-benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemBenefit)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceItemBenefit in the database
        List<ServiceItemBenefit> serviceItemBenefitList = serviceItemBenefitRepository.findAll();
        assertThat(serviceItemBenefitList).hasSize(databaseSizeBeforeCreate);

        // Validate the ServiceItemBenefit in Elasticsearch
        verify(mockServiceItemBenefitSearchRepository, times(0)).save(serviceItemBenefit);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceItemBenefitRepository.findAll().size();
        // set the field null
        serviceItemBenefit.setType(null);

        // Create the ServiceItemBenefit, which fails.

        restServiceItemBenefitMockMvc.perform(post("/api/service-item-benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemBenefit)))
            .andExpect(status().isBadRequest());

        List<ServiceItemBenefit> serviceItemBenefitList = serviceItemBenefitRepository.findAll();
        assertThat(serviceItemBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBeneficiaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceItemBenefitRepository.findAll().size();
        // set the field null
        serviceItemBenefit.setBeneficiary(null);

        // Create the ServiceItemBenefit, which fails.

        restServiceItemBenefitMockMvc.perform(post("/api/service-item-benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemBenefit)))
            .andExpect(status().isBadRequest());

        List<ServiceItemBenefit> serviceItemBenefitList = serviceItemBenefitRepository.findAll();
        assertThat(serviceItemBenefitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceItemBenefits() throws Exception {
        // Initialize the database
        serviceItemBenefitRepository.saveAndFlush(serviceItemBenefit);

        // Get all the serviceItemBenefitList
        restServiceItemBenefitMockMvc.perform(get("/api/service-item-benefits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceItemBenefit.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rule_key").value(hasItem(DEFAULT_RULE_KEY)))
            .andExpect(jsonPath("$.[*].serviceGroup").value(hasItem(DEFAULT_SERVICE_GROUP)))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].itemCategory").value(hasItem(DEFAULT_ITEM_CATEGORY)))
            .andExpect(jsonPath("$.[*].itemGroup").value(hasItem(DEFAULT_ITEM_GROUP)))
            .andExpect(jsonPath("$.[*].packageCategory").value(hasItem(DEFAULT_PACKAGE_CATEGORY)))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE)))
            .andExpect(jsonPath("$.[*].components").value(hasItem(DEFAULT_COMPONENTS)))
            .andExpect(jsonPath("$.[*].beneficiary").value(hasItem(DEFAULT_BENEFICIARY.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].paymentValue").value(hasItem(DEFAULT_PAYMENT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].minQuantity").value(hasItem(DEFAULT_MIN_QUANTITY)))
            .andExpect(jsonPath("$.[*].maxQuantity").value(hasItem(DEFAULT_MAX_QUANTITY)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].applicableOn").value(hasItem(DEFAULT_APPLICABLE_ON.toString())))
            .andExpect(jsonPath("$.[*].patientCategory").value(hasItem(DEFAULT_PATIENT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tariffClass").value(hasItem(DEFAULT_TARIFF_CLASS)))
            .andExpect(jsonPath("$.[*].materialAmount").value(hasItem(DEFAULT_MATERIAL_AMOUNT.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].applicableSponsor").value(hasItem(DEFAULT_APPLICABLE_SPONSOR.intValue())))
            .andExpect(jsonPath("$.[*].exemptedSponsor").value(hasItem(DEFAULT_EXEMPTED_SPONSOR.intValue())))
            .andExpect(jsonPath("$.[*].serviceException").value(hasItem(DEFAULT_SERVICE_EXCEPTION.intValue())))
            .andExpect(jsonPath("$.[*].itemException").value(hasItem(DEFAULT_ITEM_EXCEPTION.intValue())))
            .andExpect(jsonPath("$.[*].onDeathIncentive").value(hasItem(DEFAULT_ON_DEATH_INCENTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getServiceItemBenefit() throws Exception {
        // Initialize the database
        serviceItemBenefitRepository.saveAndFlush(serviceItemBenefit);

        // Get the serviceItemBenefit
        restServiceItemBenefitMockMvc.perform(get("/api/service-item-benefits/{id}", serviceItemBenefit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceItemBenefit.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.rule_key").value(DEFAULT_RULE_KEY))
            .andExpect(jsonPath("$.serviceGroup").value(DEFAULT_SERVICE_GROUP))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE))
            .andExpect(jsonPath("$.itemCategory").value(DEFAULT_ITEM_CATEGORY))
            .andExpect(jsonPath("$.itemGroup").value(DEFAULT_ITEM_GROUP))
            .andExpect(jsonPath("$.packageCategory").value(DEFAULT_PACKAGE_CATEGORY))
            .andExpect(jsonPath("$.visitType").value(DEFAULT_VISIT_TYPE))
            .andExpect(jsonPath("$.components").value(DEFAULT_COMPONENTS))
            .andExpect(jsonPath("$.beneficiary").value(DEFAULT_BENEFICIARY.toString()))
            .andExpect(jsonPath("$.paymentMode").value(DEFAULT_PAYMENT_MODE.toString()))
            .andExpect(jsonPath("$.paymentValue").value(DEFAULT_PAYMENT_VALUE.intValue()))
            .andExpect(jsonPath("$.minQuantity").value(DEFAULT_MIN_QUANTITY))
            .andExpect(jsonPath("$.maxQuantity").value(DEFAULT_MAX_QUANTITY))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.applicableOn").value(DEFAULT_APPLICABLE_ON.toString()))
            .andExpect(jsonPath("$.patientCategory").value(DEFAULT_PATIENT_CATEGORY.toString()))
            .andExpect(jsonPath("$.tariffClass").value(DEFAULT_TARIFF_CLASS))
            .andExpect(jsonPath("$.materialAmount").value(DEFAULT_MATERIAL_AMOUNT.toString()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.applicableSponsor").value(DEFAULT_APPLICABLE_SPONSOR.intValue()))
            .andExpect(jsonPath("$.exemptedSponsor").value(DEFAULT_EXEMPTED_SPONSOR.intValue()))
            .andExpect(jsonPath("$.serviceException").value(DEFAULT_SERVICE_EXCEPTION.intValue()))
            .andExpect(jsonPath("$.itemException").value(DEFAULT_ITEM_EXCEPTION.intValue()))
            .andExpect(jsonPath("$.onDeathIncentive").value(DEFAULT_ON_DEATH_INCENTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceItemBenefit() throws Exception {
        // Get the serviceItemBenefit
        restServiceItemBenefitMockMvc.perform(get("/api/service-item-benefits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceItemBenefit() throws Exception {
        // Initialize the database
        serviceItemBenefitService.save(serviceItemBenefit);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockServiceItemBenefitSearchRepository);

        int databaseSizeBeforeUpdate = serviceItemBenefitRepository.findAll().size();

        // Update the serviceItemBenefit
        ServiceItemBenefit updatedServiceItemBenefit = serviceItemBenefitRepository.findById(serviceItemBenefit.getId()).get();
        // Disconnect from session so that the updates on updatedServiceItemBenefit are not directly saved in db
        em.detach(updatedServiceItemBenefit);
        updatedServiceItemBenefit
            .type(UPDATED_TYPE)
            .rule_key(UPDATED_RULE_KEY)
            .serviceGroup(UPDATED_SERVICE_GROUP)
            .serviceType(UPDATED_SERVICE_TYPE)
            .itemCategory(UPDATED_ITEM_CATEGORY)
            .itemGroup(UPDATED_ITEM_GROUP)
            .packageCategory(UPDATED_PACKAGE_CATEGORY)
            .visitType(UPDATED_VISIT_TYPE)
            .components(UPDATED_COMPONENTS)
            .beneficiary(UPDATED_BENEFICIARY)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .paymentValue(UPDATED_PAYMENT_VALUE)
            .minQuantity(UPDATED_MIN_QUANTITY)
            .maxQuantity(UPDATED_MAX_QUANTITY)
            .amount(UPDATED_AMOUNT)
            .applicableOn(UPDATED_APPLICABLE_ON)
            .patientCategory(UPDATED_PATIENT_CATEGORY)
            .tariffClass(UPDATED_TARIFF_CLASS)
            .materialAmount(UPDATED_MATERIAL_AMOUNT)
            .department(UPDATED_DEPARTMENT)
            .applicableSponsor(UPDATED_APPLICABLE_SPONSOR)
            .exemptedSponsor(UPDATED_EXEMPTED_SPONSOR)
            .serviceException(UPDATED_SERVICE_EXCEPTION)
            .itemException(UPDATED_ITEM_EXCEPTION)
            .onDeathIncentive(UPDATED_ON_DEATH_INCENTIVE);

        restServiceItemBenefitMockMvc.perform(put("/api/service-item-benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceItemBenefit)))
            .andExpect(status().isOk());

        // Validate the ServiceItemBenefit in the database
        List<ServiceItemBenefit> serviceItemBenefitList = serviceItemBenefitRepository.findAll();
        assertThat(serviceItemBenefitList).hasSize(databaseSizeBeforeUpdate);
        ServiceItemBenefit testServiceItemBenefit = serviceItemBenefitList.get(serviceItemBenefitList.size() - 1);
        assertThat(testServiceItemBenefit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testServiceItemBenefit.getRule_key()).isEqualTo(UPDATED_RULE_KEY);
        assertThat(testServiceItemBenefit.getServiceGroup()).isEqualTo(UPDATED_SERVICE_GROUP);
        assertThat(testServiceItemBenefit.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);
        assertThat(testServiceItemBenefit.getItemCategory()).isEqualTo(UPDATED_ITEM_CATEGORY);
        assertThat(testServiceItemBenefit.getItemGroup()).isEqualTo(UPDATED_ITEM_GROUP);
        assertThat(testServiceItemBenefit.getPackageCategory()).isEqualTo(UPDATED_PACKAGE_CATEGORY);
        assertThat(testServiceItemBenefit.getVisitType()).isEqualTo(UPDATED_VISIT_TYPE);
        assertThat(testServiceItemBenefit.getComponents()).isEqualTo(UPDATED_COMPONENTS);
        assertThat(testServiceItemBenefit.getBeneficiary()).isEqualTo(UPDATED_BENEFICIARY);
        assertThat(testServiceItemBenefit.getPaymentMode()).isEqualTo(UPDATED_PAYMENT_MODE);
        assertThat(testServiceItemBenefit.getPaymentValue()).isEqualTo(UPDATED_PAYMENT_VALUE);
        assertThat(testServiceItemBenefit.getMinQuantity()).isEqualTo(UPDATED_MIN_QUANTITY);
        assertThat(testServiceItemBenefit.getMaxQuantity()).isEqualTo(UPDATED_MAX_QUANTITY);
        assertThat(testServiceItemBenefit.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testServiceItemBenefit.getApplicableOn()).isEqualTo(UPDATED_APPLICABLE_ON);
        assertThat(testServiceItemBenefit.getPatientCategory()).isEqualTo(UPDATED_PATIENT_CATEGORY);
        assertThat(testServiceItemBenefit.getTariffClass()).isEqualTo(UPDATED_TARIFF_CLASS);
        assertThat(testServiceItemBenefit.getMaterialAmount()).isEqualTo(UPDATED_MATERIAL_AMOUNT);
        assertThat(testServiceItemBenefit.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testServiceItemBenefit.getApplicableSponsor()).isEqualTo(UPDATED_APPLICABLE_SPONSOR);
        assertThat(testServiceItemBenefit.getExemptedSponsor()).isEqualTo(UPDATED_EXEMPTED_SPONSOR);
        assertThat(testServiceItemBenefit.getServiceException()).isEqualTo(UPDATED_SERVICE_EXCEPTION);
        assertThat(testServiceItemBenefit.getItemException()).isEqualTo(UPDATED_ITEM_EXCEPTION);
        assertThat(testServiceItemBenefit.isOnDeathIncentive()).isEqualTo(UPDATED_ON_DEATH_INCENTIVE);

        // Validate the ServiceItemBenefit in Elasticsearch
        verify(mockServiceItemBenefitSearchRepository, times(1)).save(testServiceItemBenefit);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceItemBenefit() throws Exception {
        int databaseSizeBeforeUpdate = serviceItemBenefitRepository.findAll().size();

        // Create the ServiceItemBenefit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceItemBenefitMockMvc.perform(put("/api/service-item-benefits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceItemBenefit)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceItemBenefit in the database
        List<ServiceItemBenefit> serviceItemBenefitList = serviceItemBenefitRepository.findAll();
        assertThat(serviceItemBenefitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ServiceItemBenefit in Elasticsearch
        verify(mockServiceItemBenefitSearchRepository, times(0)).save(serviceItemBenefit);
    }

    @Test
    @Transactional
    public void deleteServiceItemBenefit() throws Exception {
        // Initialize the database
        serviceItemBenefitService.save(serviceItemBenefit);

        int databaseSizeBeforeDelete = serviceItemBenefitRepository.findAll().size();

        // Delete the serviceItemBenefit
        restServiceItemBenefitMockMvc.perform(delete("/api/service-item-benefits/{id}", serviceItemBenefit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceItemBenefit> serviceItemBenefitList = serviceItemBenefitRepository.findAll();
        assertThat(serviceItemBenefitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ServiceItemBenefit in Elasticsearch
        verify(mockServiceItemBenefitSearchRepository, times(1)).deleteById(serviceItemBenefit.getId());
    }

    @Test
    @Transactional
    public void searchServiceItemBenefit() throws Exception {
        // Initialize the database
        serviceItemBenefitService.save(serviceItemBenefit);
        when(mockServiceItemBenefitSearchRepository.search(queryStringQuery("id:" + serviceItemBenefit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(serviceItemBenefit), PageRequest.of(0, 1), 1));
        // Search the serviceItemBenefit
        restServiceItemBenefitMockMvc.perform(get("/api/_search/service-item-benefits?query=id:" + serviceItemBenefit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceItemBenefit.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rule_key").value(hasItem(DEFAULT_RULE_KEY)))
            .andExpect(jsonPath("$.[*].serviceGroup").value(hasItem(DEFAULT_SERVICE_GROUP)))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].itemCategory").value(hasItem(DEFAULT_ITEM_CATEGORY)))
            .andExpect(jsonPath("$.[*].itemGroup").value(hasItem(DEFAULT_ITEM_GROUP)))
            .andExpect(jsonPath("$.[*].packageCategory").value(hasItem(DEFAULT_PACKAGE_CATEGORY)))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE)))
            .andExpect(jsonPath("$.[*].components").value(hasItem(DEFAULT_COMPONENTS)))
            .andExpect(jsonPath("$.[*].beneficiary").value(hasItem(DEFAULT_BENEFICIARY.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE.toString())))
            .andExpect(jsonPath("$.[*].paymentValue").value(hasItem(DEFAULT_PAYMENT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].minQuantity").value(hasItem(DEFAULT_MIN_QUANTITY)))
            .andExpect(jsonPath("$.[*].maxQuantity").value(hasItem(DEFAULT_MAX_QUANTITY)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].applicableOn").value(hasItem(DEFAULT_APPLICABLE_ON.toString())))
            .andExpect(jsonPath("$.[*].patientCategory").value(hasItem(DEFAULT_PATIENT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tariffClass").value(hasItem(DEFAULT_TARIFF_CLASS)))
            .andExpect(jsonPath("$.[*].materialAmount").value(hasItem(DEFAULT_MATERIAL_AMOUNT.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].applicableSponsor").value(hasItem(DEFAULT_APPLICABLE_SPONSOR.intValue())))
            .andExpect(jsonPath("$.[*].exemptedSponsor").value(hasItem(DEFAULT_EXEMPTED_SPONSOR.intValue())))
            .andExpect(jsonPath("$.[*].serviceException").value(hasItem(DEFAULT_SERVICE_EXCEPTION.intValue())))
            .andExpect(jsonPath("$.[*].itemException").value(hasItem(DEFAULT_ITEM_EXCEPTION.intValue())))
            .andExpect(jsonPath("$.[*].onDeathIncentive").value(hasItem(DEFAULT_ON_DEATH_INCENTIVE.booleanValue())));
    }
}
