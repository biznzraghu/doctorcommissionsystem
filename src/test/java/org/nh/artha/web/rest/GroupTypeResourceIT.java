package org.nh.artha.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.nh.artha.ArthaApp;
import org.nh.artha.domain.GroupType;
import org.nh.artha.service.CommonValueSetCodeService;
import org.nh.artha.service.ValueSetCodeService;
import org.nh.artha.web.rest.errors.ExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GroupTypeResource} REST controller.
 */
@SpringBootTest(classes = ArthaApp.class)
public class GroupTypeResourceIT {

    private static GroupType groupType;
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @Autowired
    private CommonValueSetCodeService commonValueSetCodeService;
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    @Autowired
    private EntityManager em;
    @Autowired
    private ExceptionTranslator exceptionTranslator;
    @Autowired
    private ValueSetCodeService valueSetCodeService;
    private MockMvc restGroupTypeMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupTypeResource groupTypeResource = new GroupTypeResource(commonValueSetCodeService);
        this.restGroupTypeMockMvc = MockMvcBuilders.standaloneSetup(groupTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        groupType = commonValueSetCodeService.findAll(GroupType.class).iterator().next();
        //valueSetCodeService.doIndex(0,200, LocalDate.now(), LocalDate.now());
    }

    @org.junit.Test
    @Transactional
    public void getAllGroupTypes() throws Exception {
        // Get all the groupTypes
        restGroupTypeMockMvc.perform(get("/api/group-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupType.getId().intValue())));
    }

    @org.junit.Test
    @Transactional
    public void getGroupType() throws Exception {
        // Get the groupType
        restGroupTypeMockMvc.perform(get("/api/group-types/{id}", groupType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groupType.getId().intValue()));
    }

    @org.junit.Test
    @Transactional
    public void getNonExistingGroupType() throws Exception {
        // Get the groupType
        restGroupTypeMockMvc.perform(get("/api/group-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @org.junit.Test
    @Transactional
    public void searchGroupType() throws Exception {
        // Search the groupType
        restGroupTypeMockMvc.perform(get("/api/_search/group-types?query=id:" + groupType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupType.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupType.class);
    }

    public static GroupType getExistingGroupType(EntityManager em) {
        List<GroupType> groupTypes = em.createQuery("from " + GroupType.class.getName()).getResultList();

        GroupType groupTypeDup = groupTypes.get(0);

        return groupTypeDup;

    }
}
