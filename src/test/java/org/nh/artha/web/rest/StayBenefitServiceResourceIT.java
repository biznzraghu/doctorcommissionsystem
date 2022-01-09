package org.nh.artha.web.rest;

import org.nh.artha.ArthaApp;
import org.nh.artha.domain.StayBenefitService;
import org.nh.artha.repository.StayBenefitServiceRepository;
import org.nh.artha.repository.search.StayBenefitServiceSearchRepository;
import org.nh.artha.service.StayBenefitServiceService;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StayBenefitServiceResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class StayBenefitServiceResourceIT {

    private static final Long DEFAULT_STAY_BENEFIT_ID = 1L;
    private static final Long UPDATED_STAY_BENEFIT_ID = 2L;

    private static final Long DEFAULT_SERVICE_ID = 1L;
    private static final Long UPDATED_SERVICE_ID = 2L;

    private static final String DEFAULT_SERVICE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    @Autowired
    private StayBenefitServiceRepository stayBenefitServiceRepository;

    @Autowired
    private StayBenefitServiceService stayBenefitServiceService;

    /**
     * This repository is mocked in the org.nh.artha.repository.search test package.
     *
     * @see org.nh.artha.repository.search.StayBenefitServiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private StayBenefitServiceSearchRepository mockStayBenefitServiceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStayBenefitServiceMockMvc;

    private StayBenefitService stayBenefitService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StayBenefitService createEntity(EntityManager em) {
        StayBenefitService stayBenefitService = new StayBenefitService()
            .stayBenefitId(DEFAULT_STAY_BENEFIT_ID)
            .serviceId(DEFAULT_SERVICE_ID)
            .serviceCode(DEFAULT_SERVICE_CODE)
            .serviceName(DEFAULT_SERVICE_NAME);
        return stayBenefitService;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StayBenefitService createUpdatedEntity(EntityManager em) {
        StayBenefitService stayBenefitService = new StayBenefitService()
            .stayBenefitId(UPDATED_STAY_BENEFIT_ID)
            .serviceId(UPDATED_SERVICE_ID)
            .serviceCode(UPDATED_SERVICE_CODE)
            .serviceName(UPDATED_SERVICE_NAME);
        return stayBenefitService;
    }

    @BeforeEach
    public void initTest() {
        stayBenefitService = createEntity(em);
    }

    @Test
    @Transactional
    public void createStayBenefitService() throws Exception {
        int databaseSizeBeforeCreate = stayBenefitServiceRepository.findAll().size();

        // Create the StayBenefitService
        restStayBenefitServiceMockMvc.perform(post("/api/stay-benefit-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stayBenefitService)))
            .andExpect(status().isCreated());

        // Validate the StayBenefitService in the database
        List<StayBenefitService> stayBenefitServiceList = stayBenefitServiceRepository.findAll();
        assertThat(stayBenefitServiceList).hasSize(databaseSizeBeforeCreate + 1);
        StayBenefitService testStayBenefitService = stayBenefitServiceList.get(stayBenefitServiceList.size() - 1);
        assertThat(testStayBenefitService.getStayBenefitId()).isEqualTo(DEFAULT_STAY_BENEFIT_ID);
        assertThat(testStayBenefitService.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testStayBenefitService.getServiceCode()).isEqualTo(DEFAULT_SERVICE_CODE);
        assertThat(testStayBenefitService.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);

        // Validate the StayBenefitService in Elasticsearch
        verify(mockStayBenefitServiceSearchRepository, times(1)).save(testStayBenefitService);
    }

    @Test
    @Transactional
    public void createStayBenefitServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stayBenefitServiceRepository.findAll().size();

        // Create the StayBenefitService with an existing ID
        stayBenefitService.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStayBenefitServiceMockMvc.perform(post("/api/stay-benefit-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stayBenefitService)))
            .andExpect(status().isBadRequest());

        // Validate the StayBenefitService in the database
        List<StayBenefitService> stayBenefitServiceList = stayBenefitServiceRepository.findAll();
        assertThat(stayBenefitServiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the StayBenefitService in Elasticsearch
        verify(mockStayBenefitServiceSearchRepository, times(0)).save(stayBenefitService);
    }


    @Test
    @Transactional
    public void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = stayBenefitServiceRepository.findAll().size();
        // set the field null
        stayBenefitService.setServiceId(null);

        // Create the StayBenefitService, which fails.

        restStayBenefitServiceMockMvc.perform(post("/api/stay-benefit-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stayBenefitService)))
            .andExpect(status().isBadRequest());

        List<StayBenefitService> stayBenefitServiceList = stayBenefitServiceRepository.findAll();
        assertThat(stayBenefitServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stayBenefitServiceRepository.findAll().size();
        // set the field null
        stayBenefitService.setServiceCode(null);

        // Create the StayBenefitService, which fails.

        restStayBenefitServiceMockMvc.perform(post("/api/stay-benefit-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stayBenefitService)))
            .andExpect(status().isBadRequest());

        List<StayBenefitService> stayBenefitServiceList = stayBenefitServiceRepository.findAll();
        assertThat(stayBenefitServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stayBenefitServiceRepository.findAll().size();
        // set the field null
        stayBenefitService.setServiceName(null);

        // Create the StayBenefitService, which fails.

        restStayBenefitServiceMockMvc.perform(post("/api/stay-benefit-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stayBenefitService)))
            .andExpect(status().isBadRequest());

        List<StayBenefitService> stayBenefitServiceList = stayBenefitServiceRepository.findAll();
        assertThat(stayBenefitServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStayBenefitServices() throws Exception {
        // Initialize the database
        stayBenefitServiceRepository.saveAndFlush(stayBenefitService);

        // Get all the stayBenefitServiceList
        restStayBenefitServiceMockMvc.perform(get("/api/stay-benefit-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stayBenefitService.getId().intValue())))
            .andExpect(jsonPath("$.[*].stayBenefitId").value(hasItem(DEFAULT_STAY_BENEFIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceCode").value(hasItem(DEFAULT_SERVICE_CODE)))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)));
    }
    
    @Test
    @Transactional
    public void getStayBenefitService() throws Exception {
        // Initialize the database
        stayBenefitServiceRepository.saveAndFlush(stayBenefitService);

        // Get the stayBenefitService
        restStayBenefitServiceMockMvc.perform(get("/api/stay-benefit-services/{id}", stayBenefitService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stayBenefitService.getId().intValue()))
            .andExpect(jsonPath("$.stayBenefitId").value(DEFAULT_STAY_BENEFIT_ID.intValue()))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID.intValue()))
            .andExpect(jsonPath("$.serviceCode").value(DEFAULT_SERVICE_CODE))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingStayBenefitService() throws Exception {
        // Get the stayBenefitService
        restStayBenefitServiceMockMvc.perform(get("/api/stay-benefit-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStayBenefitService() throws Exception {
        // Initialize the database
        stayBenefitServiceService.save(stayBenefitService);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockStayBenefitServiceSearchRepository);

        int databaseSizeBeforeUpdate = stayBenefitServiceRepository.findAll().size();

        // Update the stayBenefitService
        StayBenefitService updatedStayBenefitService = stayBenefitServiceRepository.findById(stayBenefitService.getId()).get();
        // Disconnect from session so that the updates on updatedStayBenefitService are not directly saved in db
        em.detach(updatedStayBenefitService);
        updatedStayBenefitService
            .stayBenefitId(UPDATED_STAY_BENEFIT_ID)
            .serviceId(UPDATED_SERVICE_ID)
            .serviceCode(UPDATED_SERVICE_CODE)
            .serviceName(UPDATED_SERVICE_NAME);

        restStayBenefitServiceMockMvc.perform(put("/api/stay-benefit-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStayBenefitService)))
            .andExpect(status().isOk());

        // Validate the StayBenefitService in the database
        List<StayBenefitService> stayBenefitServiceList = stayBenefitServiceRepository.findAll();
        assertThat(stayBenefitServiceList).hasSize(databaseSizeBeforeUpdate);
        StayBenefitService testStayBenefitService = stayBenefitServiceList.get(stayBenefitServiceList.size() - 1);
        assertThat(testStayBenefitService.getStayBenefitId()).isEqualTo(UPDATED_STAY_BENEFIT_ID);
        assertThat(testStayBenefitService.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testStayBenefitService.getServiceCode()).isEqualTo(UPDATED_SERVICE_CODE);
        assertThat(testStayBenefitService.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);

        // Validate the StayBenefitService in Elasticsearch
        verify(mockStayBenefitServiceSearchRepository, times(1)).save(testStayBenefitService);
    }

    @Test
    @Transactional
    public void updateNonExistingStayBenefitService() throws Exception {
        int databaseSizeBeforeUpdate = stayBenefitServiceRepository.findAll().size();

        // Create the StayBenefitService

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStayBenefitServiceMockMvc.perform(put("/api/stay-benefit-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stayBenefitService)))
            .andExpect(status().isBadRequest());

        // Validate the StayBenefitService in the database
        List<StayBenefitService> stayBenefitServiceList = stayBenefitServiceRepository.findAll();
        assertThat(stayBenefitServiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StayBenefitService in Elasticsearch
        verify(mockStayBenefitServiceSearchRepository, times(0)).save(stayBenefitService);
    }

    @Test
    @Transactional
    public void deleteStayBenefitService() throws Exception {
        // Initialize the database
        stayBenefitServiceService.save(stayBenefitService);

        int databaseSizeBeforeDelete = stayBenefitServiceRepository.findAll().size();

        // Delete the stayBenefitService
        restStayBenefitServiceMockMvc.perform(delete("/api/stay-benefit-services/{id}", stayBenefitService.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StayBenefitService> stayBenefitServiceList = stayBenefitServiceRepository.findAll();
        assertThat(stayBenefitServiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StayBenefitService in Elasticsearch
        verify(mockStayBenefitServiceSearchRepository, times(1)).deleteById(stayBenefitService.getId());
    }

    @Test
    @Transactional
    public void searchStayBenefitService() throws Exception {
        // Initialize the database
        stayBenefitServiceService.save(stayBenefitService);
        when(mockStayBenefitServiceSearchRepository.search(queryStringQuery("id:" + stayBenefitService.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stayBenefitService), PageRequest.of(0, 1), 1));
        // Search the stayBenefitService
        restStayBenefitServiceMockMvc.perform(get("/api/_search/stay-benefit-services?query=id:" + stayBenefitService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stayBenefitService.getId().intValue())))
            .andExpect(jsonPath("$.[*].stayBenefitId").value(hasItem(DEFAULT_STAY_BENEFIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].serviceCode").value(hasItem(DEFAULT_SERVICE_CODE)))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)));
    }
}
