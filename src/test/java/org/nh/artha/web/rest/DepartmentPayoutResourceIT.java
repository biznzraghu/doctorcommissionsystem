package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.DepartmentPayout;
import org.nh.artha.domain.dto.UserDTO;
import org.nh.artha.domain.enumeration.ChangeRequestStatus;
import org.nh.artha.repository.DepartmentPayoutRepository;
import org.nh.artha.repository.search.DepartmentPayoutSearchRepository;
import org.nh.artha.service.DepartmentPayoutService;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nh.artha.domain.enumeration.NetGross;
import org.nh.artha.domain.enumeration.ApplicableInvoices;
import org.nh.artha.domain.enumeration.OnCostSale;
/**
 * Integration tests for the {@link DepartmentPayoutResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DepartmentPayoutResourceIT {

    private static final UserDTO DEFAULT_CREATED_BY = new UserDTO();
    private static final UserDTO UPDATED_CREATED_BY = new UserDTO();

    private static final LocalDateTime DEFAULT_CREATED_DATE = LocalDateTime.now();
    private static final LocalDateTime UPDATED_CREATED_DATE = LocalDateTime.now();

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Boolean DEFAULT_STATUS = Boolean.FALSE;
    private static final Boolean UPDATED_STATUS = Boolean.TRUE;

    private static final ChangeRequestStatus DEFAULT_CHANGE_REQUEST_STATUS = ChangeRequestStatus.DRAFT;
    private static final ChangeRequestStatus UPDATED_CHANGE_REQUEST_STATUS = ChangeRequestStatus.PENDING_APPROVAL;

    private static final Boolean DEFAULT_LATEST = false;
    private static final Boolean UPDATED_LATEST = true;

    private static final String DEFAULT_VISIT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VISIT_TYPE = "BBBBBBBBBB";

    private static final NetGross DEFAULT_NET_GROSS = NetGross.NET;
    private static final NetGross UPDATED_NET_GROSS = NetGross.GROSS;

    private static final ApplicableInvoices DEFAULT_APPLICABLE_INVOICE = ApplicableInvoices.ALL_INVOICES;
    private static final ApplicableInvoices UPDATED_APPLICABLE_INVOICE = ApplicableInvoices.INVOICES_WITH_SURGERY;

    private static final OnCostSale DEFAULT_ON_COST_SALE = OnCostSale.SALE;
    private static final OnCostSale UPDATED_ON_COST_SALE = OnCostSale.COST;

    private static final Boolean DEFAULT_ALL_MATERIALS = false;
    private static final Boolean UPDATED_ALL_MATERIALS = true;

    private static final Boolean DEFAULT_DRUGS = false;
    private static final Boolean UPDATED_DRUGS = true;

    private static final Boolean DEFAULT_IMPLANTS = false;
    private static final Boolean UPDATED_IMPLANTS = true;

    private static final Boolean DEFAULT_CONSUMABLES = false;
    private static final Boolean UPDATED_CONSUMABLES = true;

    private static final Boolean DEFAULT_DEPT_CONSUMPTION = false;
    private static final Boolean UPDATED_DEPT_CONSUMPTION = true;

    private static final Boolean DEFAULT_HSC_CONSUMPTION = false;
    private static final Boolean UPDATED_HSC_CONSUMPTION = true;

    @Autowired
    private DepartmentPayoutRepository departmentPayoutRepository;

    @Autowired
    private DepartmentPayoutService departmentPayoutService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.DepartmentPayoutSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepartmentPayoutSearchRepository mockDepartmentPayoutSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentPayoutMockMvc;

    private DepartmentPayout departmentPayout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentPayout createEntity(EntityManager em) {
        DepartmentPayout departmentPayout = new DepartmentPayout();
        departmentPayout.setCreatedBy(DEFAULT_CREATED_BY);
        departmentPayout.setCreatedDate(DEFAULT_CREATED_DATE);
        departmentPayout.setVersion(DEFAULT_VERSION);
        departmentPayout.setStatus(DEFAULT_STATUS);
        departmentPayout.setChangeRequestStatus(DEFAULT_CHANGE_REQUEST_STATUS);
        departmentPayout.setLatest(DEFAULT_LATEST);
        departmentPayout.setVisitType(DEFAULT_VISIT_TYPE);
        departmentPayout.setNetGross(DEFAULT_NET_GROSS);
        departmentPayout.setApplicableInvoice(DEFAULT_APPLICABLE_INVOICE);
        departmentPayout.setOnCostSale(DEFAULT_ON_COST_SALE);
        departmentPayout.setAllMaterials(DEFAULT_ALL_MATERIALS);
        departmentPayout.setDrugs(DEFAULT_DRUGS);
        departmentPayout.setImplants(DEFAULT_IMPLANTS);
        departmentPayout.setConsumables(DEFAULT_CONSUMABLES);
        departmentPayout.setDeptConsumption(DEFAULT_DEPT_CONSUMPTION);
        departmentPayout.setHscConsumption(DEFAULT_HSC_CONSUMPTION);
        return departmentPayout;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentPayout createUpdatedEntity(EntityManager em) {
        DepartmentPayout departmentPayout = new DepartmentPayout();
        departmentPayout.setCreatedBy(UPDATED_CREATED_BY);
        departmentPayout.setCreatedDate(UPDATED_CREATED_DATE);
        departmentPayout.setVersion(UPDATED_VERSION);
        departmentPayout.setStatus(UPDATED_STATUS);
        departmentPayout.setChangeRequestStatus(UPDATED_CHANGE_REQUEST_STATUS);
        departmentPayout.setLatest(UPDATED_LATEST);
        departmentPayout.setVisitType(UPDATED_VISIT_TYPE);
        departmentPayout.setNetGross(UPDATED_NET_GROSS);
        departmentPayout.setApplicableInvoice(UPDATED_APPLICABLE_INVOICE);
        departmentPayout.setOnCostSale(UPDATED_ON_COST_SALE);
        departmentPayout.setAllMaterials(UPDATED_ALL_MATERIALS);
        departmentPayout.setDrugs(UPDATED_DRUGS);
        departmentPayout.setImplants(UPDATED_IMPLANTS);
        departmentPayout.setConsumables(UPDATED_CONSUMABLES);
        departmentPayout.setDeptConsumption(UPDATED_DEPT_CONSUMPTION);
        departmentPayout.setHscConsumption(UPDATED_HSC_CONSUMPTION);
        return departmentPayout;
    }

    @BeforeEach
    public void initTest() {
        departmentPayout = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartmentPayout() throws Exception {
        int databaseSizeBeforeCreate = departmentPayoutRepository.findAll().size();

        // Create the DepartmentPayout
        restDepartmentPayoutMockMvc.perform(post("/api/department-payouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentPayout)))
            .andExpect(status().isCreated());

        // Validate the DepartmentPayout in the database
        List<DepartmentPayout> departmentPayoutList = departmentPayoutRepository.findAll();
        assertThat(departmentPayoutList).hasSize(databaseSizeBeforeCreate + 1);
        DepartmentPayout testDepartmentPayout = departmentPayoutList.get(departmentPayoutList.size() - 1);
        assertThat(testDepartmentPayout.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDepartmentPayout.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDepartmentPayout.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testDepartmentPayout.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDepartmentPayout.getChangeRequestStatus()).isEqualTo(DEFAULT_CHANGE_REQUEST_STATUS);
        assertThat(testDepartmentPayout.isLatest()).isEqualTo(DEFAULT_LATEST);
        assertThat(testDepartmentPayout.getVisitType()).isEqualTo(DEFAULT_VISIT_TYPE);
        assertThat(testDepartmentPayout.getNetGross()).isEqualTo(DEFAULT_NET_GROSS);
        assertThat(testDepartmentPayout.getApplicableInvoice()).isEqualTo(DEFAULT_APPLICABLE_INVOICE);
        assertThat(testDepartmentPayout.getOnCostSale()).isEqualTo(DEFAULT_ON_COST_SALE);
        assertThat(testDepartmentPayout.isAllMaterials()).isEqualTo(DEFAULT_ALL_MATERIALS);
        assertThat(testDepartmentPayout.isDrugs()).isEqualTo(DEFAULT_DRUGS);
        assertThat(testDepartmentPayout.isImplants()).isEqualTo(DEFAULT_IMPLANTS);
        assertThat(testDepartmentPayout.isConsumables()).isEqualTo(DEFAULT_CONSUMABLES);
        assertThat(testDepartmentPayout.isDeptConsumption()).isEqualTo(DEFAULT_DEPT_CONSUMPTION);
        assertThat(testDepartmentPayout.isHscConsumption()).isEqualTo(DEFAULT_HSC_CONSUMPTION);

        // Validate the DepartmentPayout in Elasticsearch
        verify(mockDepartmentPayoutSearchRepository, times(1)).save(testDepartmentPayout);
    }

    @Test
    @Transactional
    public void createDepartmentPayoutWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departmentPayoutRepository.findAll().size();

        // Create the DepartmentPayout with an existing ID
        departmentPayout.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentPayoutMockMvc.perform(post("/api/department-payouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentPayout)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentPayout in the database
        List<DepartmentPayout> departmentPayoutList = departmentPayoutRepository.findAll();
        assertThat(departmentPayoutList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepartmentPayout in Elasticsearch
        verify(mockDepartmentPayoutSearchRepository, times(0)).save(departmentPayout);
    }


    @Test
    @Transactional
    public void getAllDepartmentPayouts() throws Exception {
        // Initialize the database
        departmentPayoutRepository.saveAndFlush(departmentPayout);

        // Get all the departmentPayoutList
        restDepartmentPayoutMockMvc.perform(get("/api/department-payouts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].changeRequestStatus").value(hasItem(DEFAULT_CHANGE_REQUEST_STATUS)))
            .andExpect(jsonPath("$.[*].latest").value(hasItem(DEFAULT_LATEST.booleanValue())))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE)))
            .andExpect(jsonPath("$.[*].netGross").value(hasItem(DEFAULT_NET_GROSS.toString())))
            .andExpect(jsonPath("$.[*].applicableInvoice").value(hasItem(DEFAULT_APPLICABLE_INVOICE.toString())))
            .andExpect(jsonPath("$.[*].onCostSale").value(hasItem(DEFAULT_ON_COST_SALE.toString())))
            .andExpect(jsonPath("$.[*].allMaterials").value(hasItem(DEFAULT_ALL_MATERIALS.booleanValue())))
            .andExpect(jsonPath("$.[*].drugs").value(hasItem(DEFAULT_DRUGS.booleanValue())))
            .andExpect(jsonPath("$.[*].implants").value(hasItem(DEFAULT_IMPLANTS.booleanValue())))
            .andExpect(jsonPath("$.[*].consumables").value(hasItem(DEFAULT_CONSUMABLES.booleanValue())))
            .andExpect(jsonPath("$.[*].deptConsumption").value(hasItem(DEFAULT_DEPT_CONSUMPTION.booleanValue())))
            .andExpect(jsonPath("$.[*].hscConsumption").value(hasItem(DEFAULT_HSC_CONSUMPTION.booleanValue())));
    }

    @Test
    @Transactional
    public void getDepartmentPayout() throws Exception {
        // Initialize the database
        departmentPayoutRepository.saveAndFlush(departmentPayout);

        // Get the departmentPayout
        restDepartmentPayoutMockMvc.perform(get("/api/department-payouts/{id}", departmentPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departmentPayout.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.changeRequestStatus").value(DEFAULT_CHANGE_REQUEST_STATUS))
            .andExpect(jsonPath("$.latest").value(DEFAULT_LATEST.booleanValue()))
            .andExpect(jsonPath("$.visitType").value(DEFAULT_VISIT_TYPE))
            .andExpect(jsonPath("$.netGross").value(DEFAULT_NET_GROSS.toString()))
            .andExpect(jsonPath("$.applicableInvoice").value(DEFAULT_APPLICABLE_INVOICE.toString()))
            .andExpect(jsonPath("$.onCostSale").value(DEFAULT_ON_COST_SALE.toString()))
            .andExpect(jsonPath("$.allMaterials").value(DEFAULT_ALL_MATERIALS.booleanValue()))
            .andExpect(jsonPath("$.drugs").value(DEFAULT_DRUGS.booleanValue()))
            .andExpect(jsonPath("$.implants").value(DEFAULT_IMPLANTS.booleanValue()))
            .andExpect(jsonPath("$.consumables").value(DEFAULT_CONSUMABLES.booleanValue()))
            .andExpect(jsonPath("$.deptConsumption").value(DEFAULT_DEPT_CONSUMPTION.booleanValue()))
            .andExpect(jsonPath("$.hscConsumption").value(DEFAULT_HSC_CONSUMPTION.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDepartmentPayout() throws Exception {
        // Get the departmentPayout
        restDepartmentPayoutMockMvc.perform(get("/api/department-payouts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartmentPayout() throws Exception {
        // Initialize the database
        departmentPayoutService.save(departmentPayout);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDepartmentPayoutSearchRepository);

        int databaseSizeBeforeUpdate = departmentPayoutRepository.findAll().size();

        // Update the departmentPayout
        DepartmentPayout updatedDepartmentPayout = departmentPayoutRepository.findOne(departmentPayout.getId());
        // Disconnect from session so that the updates on updatedDepartmentPayout are not directly saved in db
        em.detach(updatedDepartmentPayout);
        updatedDepartmentPayout.setCreatedBy(UPDATED_CREATED_BY);
        updatedDepartmentPayout.setCreatedDate(UPDATED_CREATED_DATE);
        updatedDepartmentPayout.setVersion(UPDATED_VERSION);
        updatedDepartmentPayout.setStatus(UPDATED_STATUS);
        updatedDepartmentPayout.setChangeRequestStatus(UPDATED_CHANGE_REQUEST_STATUS);
        updatedDepartmentPayout.setLatest(UPDATED_LATEST);
        updatedDepartmentPayout.setVisitType(UPDATED_VISIT_TYPE);
        updatedDepartmentPayout.setNetGross(UPDATED_NET_GROSS);
        updatedDepartmentPayout.setApplicableInvoice(UPDATED_APPLICABLE_INVOICE);
        updatedDepartmentPayout.setOnCostSale(UPDATED_ON_COST_SALE);
        updatedDepartmentPayout.setAllMaterials(UPDATED_ALL_MATERIALS);
        updatedDepartmentPayout.setDrugs(UPDATED_DRUGS);
        updatedDepartmentPayout.setImplants(UPDATED_IMPLANTS);
        updatedDepartmentPayout.setConsumables(UPDATED_CONSUMABLES);
        updatedDepartmentPayout.setDeptConsumption(UPDATED_DEPT_CONSUMPTION);
        updatedDepartmentPayout.setHscConsumption(UPDATED_HSC_CONSUMPTION);

        restDepartmentPayoutMockMvc.perform(put("/api/department-payouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDepartmentPayout)))
            .andExpect(status().isOk());

        // Validate the DepartmentPayout in the database
        List<DepartmentPayout> departmentPayoutList = departmentPayoutRepository.findAll();
        assertThat(departmentPayoutList).hasSize(databaseSizeBeforeUpdate);
        DepartmentPayout testDepartmentPayout = departmentPayoutList.get(departmentPayoutList.size() - 1);
        assertThat(testDepartmentPayout.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDepartmentPayout.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDepartmentPayout.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testDepartmentPayout.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDepartmentPayout.getChangeRequestStatus()).isEqualTo(UPDATED_CHANGE_REQUEST_STATUS);
        assertThat(testDepartmentPayout.isLatest()).isEqualTo(UPDATED_LATEST);
        assertThat(testDepartmentPayout.getVisitType()).isEqualTo(UPDATED_VISIT_TYPE);
        assertThat(testDepartmentPayout.getNetGross()).isEqualTo(UPDATED_NET_GROSS);
        assertThat(testDepartmentPayout.getApplicableInvoice()).isEqualTo(UPDATED_APPLICABLE_INVOICE);
        assertThat(testDepartmentPayout.getOnCostSale()).isEqualTo(UPDATED_ON_COST_SALE);
        assertThat(testDepartmentPayout.isAllMaterials()).isEqualTo(UPDATED_ALL_MATERIALS);
        assertThat(testDepartmentPayout.isDrugs()).isEqualTo(UPDATED_DRUGS);
        assertThat(testDepartmentPayout.isImplants()).isEqualTo(UPDATED_IMPLANTS);
        assertThat(testDepartmentPayout.isConsumables()).isEqualTo(UPDATED_CONSUMABLES);
        assertThat(testDepartmentPayout.isDeptConsumption()).isEqualTo(UPDATED_DEPT_CONSUMPTION);
        assertThat(testDepartmentPayout.isHscConsumption()).isEqualTo(UPDATED_HSC_CONSUMPTION);

        // Validate the DepartmentPayout in Elasticsearch
        verify(mockDepartmentPayoutSearchRepository, times(1)).save(testDepartmentPayout);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartmentPayout() throws Exception {
        int databaseSizeBeforeUpdate = departmentPayoutRepository.findAll().size();

        // Create the DepartmentPayout

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentPayoutMockMvc.perform(put("/api/department-payouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentPayout)))
            .andExpect(status().isBadRequest());

        // Validate the DepartmentPayout in the database
        List<DepartmentPayout> departmentPayoutList = departmentPayoutRepository.findAll();
        assertThat(departmentPayoutList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepartmentPayout in Elasticsearch
        verify(mockDepartmentPayoutSearchRepository, times(0)).save(departmentPayout);
    }

    @Test
    @Transactional
    public void deleteDepartmentPayout() throws Exception {
        // Initialize the database
        departmentPayoutService.save(departmentPayout);

        int databaseSizeBeforeDelete = departmentPayoutRepository.findAll().size();

        // Delete the departmentPayout
        restDepartmentPayoutMockMvc.perform(delete("/api/department-payouts/{id}", departmentPayout.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepartmentPayout> departmentPayoutList = departmentPayoutRepository.findAll();
        assertThat(departmentPayoutList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepartmentPayout in Elasticsearch
        verify(mockDepartmentPayoutSearchRepository, times(1)).deleteById(departmentPayout.getId());
    }

    @Test
    @Transactional
    public void searchDepartmentPayout() throws Exception {
        // Initialize the database
        departmentPayoutService.save(departmentPayout);
        when(mockDepartmentPayoutSearchRepository.search(queryStringQuery("id:" + departmentPayout.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(departmentPayout), PageRequest.of(0, 1), 1));
        // Search the departmentPayout
        restDepartmentPayoutMockMvc.perform(get("/api/_search/department-payouts?query=id:" + departmentPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].changeRequestStatus").value(hasItem(DEFAULT_CHANGE_REQUEST_STATUS)))
            .andExpect(jsonPath("$.[*].latest").value(hasItem(DEFAULT_LATEST.booleanValue())))
            .andExpect(jsonPath("$.[*].visitType").value(hasItem(DEFAULT_VISIT_TYPE)))
            .andExpect(jsonPath("$.[*].netGross").value(hasItem(DEFAULT_NET_GROSS.toString())))
            .andExpect(jsonPath("$.[*].applicableInvoice").value(hasItem(DEFAULT_APPLICABLE_INVOICE.toString())))
            .andExpect(jsonPath("$.[*].onCostSale").value(hasItem(DEFAULT_ON_COST_SALE.toString())))
            .andExpect(jsonPath("$.[*].allMaterials").value(hasItem(DEFAULT_ALL_MATERIALS.booleanValue())))
            .andExpect(jsonPath("$.[*].drugs").value(hasItem(DEFAULT_DRUGS.booleanValue())))
            .andExpect(jsonPath("$.[*].implants").value(hasItem(DEFAULT_IMPLANTS.booleanValue())))
            .andExpect(jsonPath("$.[*].consumables").value(hasItem(DEFAULT_CONSUMABLES.booleanValue())))
            .andExpect(jsonPath("$.[*].deptConsumption").value(hasItem(DEFAULT_DEPT_CONSUMPTION.booleanValue())))
            .andExpect(jsonPath("$.[*].hscConsumption").value(hasItem(DEFAULT_HSC_CONSUMPTION.booleanValue())));
    }
}
