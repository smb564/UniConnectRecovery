package com.smbsoft.uniconnect.web.rest;

import com.smbsoft.uniconnect.UniConnectApp;

import com.smbsoft.uniconnect.domain.Opportunity;
import com.smbsoft.uniconnect.repository.OpportunityRepository;
import com.smbsoft.uniconnect.service.OpportunityService;
import com.smbsoft.uniconnect.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OpportunityResource REST controller.
 *
 * @see OpportunityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniConnectApp.class)
public class OpportunityResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OWNER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_LOGIN = "BBBBBBBBBB";

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private OpportunityService opportunityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restOpportunityMockMvc;

    private Opportunity opportunity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OpportunityResource opportunityResource = new OpportunityResource(opportunityService);
        this.restOpportunityMockMvc = MockMvcBuilders.standaloneSetup(opportunityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunity createEntity() {
        Opportunity opportunity = new Opportunity()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .ownerLogin(DEFAULT_OWNER_LOGIN);
        return opportunity;
    }

    @Before
    public void initTest() {
        opportunityRepository.deleteAll();
        opportunity = createEntity();
    }

    @Test
    public void createOpportunity() throws Exception {
        int databaseSizeBeforeCreate = opportunityRepository.findAll().size();

        // Create the Opportunity
        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isCreated());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate + 1);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOpportunity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOpportunity.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOpportunity.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testOpportunity.getOwnerLogin()).isEqualTo(DEFAULT_OWNER_LOGIN);
    }

    @Test
    public void createOpportunityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = opportunityRepository.findAll().size();

        // Create the Opportunity with an existing ID
        opportunity.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunityRepository.findAll().size();
        // set the field null
        opportunity.setTitle(null);

        // Create the Opportunity, which fails.

        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunityRepository.findAll().size();
        // set the field null
        opportunity.setDescription(null);

        // Create the Opportunity, which fails.

        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunityRepository.findAll().size();
        // set the field null
        opportunity.setStartDate(null);

        // Create the Opportunity, which fails.

        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunityRepository.findAll().size();
        // set the field null
        opportunity.setEndDate(null);

        // Create the Opportunity, which fails.

        restOpportunityMockMvc.perform(post("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isBadRequest());

        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllOpportunities() throws Exception {
        // Initialize the database
        opportunityRepository.save(opportunity);

        // Get all the opportunityList
        restOpportunityMockMvc.perform(get("/api/opportunities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunity.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].ownerLogin").value(hasItem(DEFAULT_OWNER_LOGIN.toString())));
    }

    @Test
    public void getOpportunity() throws Exception {
        // Initialize the database
        opportunityRepository.save(opportunity);

        // Get the opportunity
        restOpportunityMockMvc.perform(get("/api/opportunities/{id}", opportunity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(opportunity.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.ownerLogin").value(DEFAULT_OWNER_LOGIN.toString()));
    }

    @Test
    public void getNonExistingOpportunity() throws Exception {
        // Get the opportunity
        restOpportunityMockMvc.perform(get("/api/opportunities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateOpportunity() throws Exception {
        // Initialize the database
        opportunityService.save(opportunity);

        int databaseSizeBeforeUpdate = opportunityRepository.findAll().size();

        // Update the opportunity
        Opportunity updatedOpportunity = opportunityRepository.findOne(opportunity.getId());
        updatedOpportunity
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .ownerLogin(UPDATED_OWNER_LOGIN);

        restOpportunityMockMvc.perform(put("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOpportunity)))
            .andExpect(status().isOk());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate);
        Opportunity testOpportunity = opportunityList.get(opportunityList.size() - 1);
        assertThat(testOpportunity.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOpportunity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOpportunity.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOpportunity.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOpportunity.getOwnerLogin()).isEqualTo(UPDATED_OWNER_LOGIN);
    }

    @Test
    public void updateNonExistingOpportunity() throws Exception {
        int databaseSizeBeforeUpdate = opportunityRepository.findAll().size();

        // Create the Opportunity

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOpportunityMockMvc.perform(put("/api/opportunities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunity)))
            .andExpect(status().isCreated());

        // Validate the Opportunity in the database
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteOpportunity() throws Exception {
        // Initialize the database
        opportunityService.save(opportunity);

        int databaseSizeBeforeDelete = opportunityRepository.findAll().size();

        // Get the opportunity
        restOpportunityMockMvc.perform(delete("/api/opportunities/{id}", opportunity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Opportunity> opportunityList = opportunityRepository.findAll();
        assertThat(opportunityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opportunity.class);
    }
}
