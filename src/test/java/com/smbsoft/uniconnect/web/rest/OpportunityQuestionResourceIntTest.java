package com.smbsoft.uniconnect.web.rest;

import com.smbsoft.uniconnect.UniConnectApp;

import com.smbsoft.uniconnect.domain.OpportunityQuestion;
import com.smbsoft.uniconnect.repository.OpportunityQuestionRepository;
import com.smbsoft.uniconnect.service.OpportunityQuestionService;
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
 * Test class for the OpportunityQuestionResource REST controller.
 *
 * @see OpportunityQuestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniConnectApp.class)
public class OpportunityQuestionResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VOTES = 1;
    private static final Integer UPDATED_VOTES = 2;

    private static final String DEFAULT_OWNER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    @Autowired
    private OpportunityQuestionRepository opportunityQuestionRepository;

    @Autowired
    private OpportunityQuestionService opportunityQuestionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restOpportunityQuestionMockMvc;

    private OpportunityQuestion opportunityQuestion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OpportunityQuestionResource opportunityQuestionResource = new OpportunityQuestionResource(opportunityQuestionService);
        this.restOpportunityQuestionMockMvc = MockMvcBuilders.standaloneSetup(opportunityQuestionResource)
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
    public static OpportunityQuestion createEntity() {
        OpportunityQuestion opportunityQuestion = new OpportunityQuestion()
            .description(DEFAULT_DESCRIPTION)
            .date(DEFAULT_DATE)
            .votes(DEFAULT_VOTES)
            .ownerLogin(DEFAULT_OWNER_LOGIN)
            .answer(DEFAULT_ANSWER);
        return opportunityQuestion;
    }

    @Before
    public void initTest() {
        opportunityQuestionRepository.deleteAll();
        opportunityQuestion = createEntity();
    }

    @Test
    public void createOpportunityQuestion() throws Exception {
        int databaseSizeBeforeCreate = opportunityQuestionRepository.findAll().size();

        // Create the OpportunityQuestion
        restOpportunityQuestionMockMvc.perform(post("/api/opportunity-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunityQuestion)))
            .andExpect(status().isCreated());

        // Validate the OpportunityQuestion in the database
        List<OpportunityQuestion> opportunityQuestionList = opportunityQuestionRepository.findAll();
        assertThat(opportunityQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        OpportunityQuestion testOpportunityQuestion = opportunityQuestionList.get(opportunityQuestionList.size() - 1);
        assertThat(testOpportunityQuestion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOpportunityQuestion.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOpportunityQuestion.getVotes()).isEqualTo(DEFAULT_VOTES);
        assertThat(testOpportunityQuestion.getOwnerLogin()).isEqualTo(DEFAULT_OWNER_LOGIN);
        assertThat(testOpportunityQuestion.getAnswer()).isEqualTo(DEFAULT_ANSWER);
    }

    @Test
    public void createOpportunityQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = opportunityQuestionRepository.findAll().size();

        // Create the OpportunityQuestion with an existing ID
        opportunityQuestion.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpportunityQuestionMockMvc.perform(post("/api/opportunity-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunityQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OpportunityQuestion> opportunityQuestionList = opportunityQuestionRepository.findAll();
        assertThat(opportunityQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunityQuestionRepository.findAll().size();
        // set the field null
        opportunityQuestion.setDescription(null);

        // Create the OpportunityQuestion, which fails.

        restOpportunityQuestionMockMvc.perform(post("/api/opportunity-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunityQuestion)))
            .andExpect(status().isBadRequest());

        List<OpportunityQuestion> opportunityQuestionList = opportunityQuestionRepository.findAll();
        assertThat(opportunityQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllOpportunityQuestions() throws Exception {
        // Initialize the database
        opportunityQuestionRepository.save(opportunityQuestion);

        // Get all the opportunityQuestionList
        restOpportunityQuestionMockMvc.perform(get("/api/opportunity-questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunityQuestion.getId())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].votes").value(hasItem(DEFAULT_VOTES)))
            .andExpect(jsonPath("$.[*].ownerLogin").value(hasItem(DEFAULT_OWNER_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())));
    }

    @Test
    public void getOpportunityQuestion() throws Exception {
        // Initialize the database
        opportunityQuestionRepository.save(opportunityQuestion);

        // Get the opportunityQuestion
        restOpportunityQuestionMockMvc.perform(get("/api/opportunity-questions/{id}", opportunityQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(opportunityQuestion.getId()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.votes").value(DEFAULT_VOTES))
            .andExpect(jsonPath("$.ownerLogin").value(DEFAULT_OWNER_LOGIN.toString()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()));
    }

    @Test
    public void getNonExistingOpportunityQuestion() throws Exception {
        // Get the opportunityQuestion
        restOpportunityQuestionMockMvc.perform(get("/api/opportunity-questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateOpportunityQuestion() throws Exception {
        // Initialize the database
        opportunityQuestionService.save(opportunityQuestion);

        int databaseSizeBeforeUpdate = opportunityQuestionRepository.findAll().size();

        // Update the opportunityQuestion
        OpportunityQuestion updatedOpportunityQuestion = opportunityQuestionRepository.findOne(opportunityQuestion.getId());
        updatedOpportunityQuestion
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .votes(UPDATED_VOTES)
            .ownerLogin(UPDATED_OWNER_LOGIN)
            .answer(UPDATED_ANSWER);

        restOpportunityQuestionMockMvc.perform(put("/api/opportunity-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOpportunityQuestion)))
            .andExpect(status().isOk());

        // Validate the OpportunityQuestion in the database
        List<OpportunityQuestion> opportunityQuestionList = opportunityQuestionRepository.findAll();
        assertThat(opportunityQuestionList).hasSize(databaseSizeBeforeUpdate);
        OpportunityQuestion testOpportunityQuestion = opportunityQuestionList.get(opportunityQuestionList.size() - 1);
        assertThat(testOpportunityQuestion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOpportunityQuestion.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOpportunityQuestion.getVotes()).isEqualTo(UPDATED_VOTES);
        assertThat(testOpportunityQuestion.getOwnerLogin()).isEqualTo(UPDATED_OWNER_LOGIN);
        assertThat(testOpportunityQuestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    public void updateNonExistingOpportunityQuestion() throws Exception {
        int databaseSizeBeforeUpdate = opportunityQuestionRepository.findAll().size();

        // Create the OpportunityQuestion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOpportunityQuestionMockMvc.perform(put("/api/opportunity-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opportunityQuestion)))
            .andExpect(status().isCreated());

        // Validate the OpportunityQuestion in the database
        List<OpportunityQuestion> opportunityQuestionList = opportunityQuestionRepository.findAll();
        assertThat(opportunityQuestionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteOpportunityQuestion() throws Exception {
        // Initialize the database
        opportunityQuestionService.save(opportunityQuestion);

        int databaseSizeBeforeDelete = opportunityQuestionRepository.findAll().size();

        // Get the opportunityQuestion
        restOpportunityQuestionMockMvc.perform(delete("/api/opportunity-questions/{id}", opportunityQuestion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OpportunityQuestion> opportunityQuestionList = opportunityQuestionRepository.findAll();
        assertThat(opportunityQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpportunityQuestion.class);
    }
}
