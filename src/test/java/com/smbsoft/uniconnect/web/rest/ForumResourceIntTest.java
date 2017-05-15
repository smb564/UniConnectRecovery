package com.smbsoft.uniconnect.web.rest;

import com.smbsoft.uniconnect.UniConnectApp;

import com.smbsoft.uniconnect.domain.Forum;
import com.smbsoft.uniconnect.repository.ForumRepository;
import com.smbsoft.uniconnect.service.ForumService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ForumResource REST controller.
 *
 * @see ForumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniConnectApp.class)
public class ForumResourceIntTest {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private ForumService forumService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restForumMockMvc;

    private Forum forum;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ForumResource forumResource = new ForumResource(forumService);
        this.restForumMockMvc = MockMvcBuilders.standaloneSetup(forumResource)
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
    public static Forum createEntity() {
        Forum forum = new Forum();
        return forum;
    }

    @Before
    public void initTest() {
        forumRepository.deleteAll();
        forum = createEntity();
    }

    @Test
    public void createForum() throws Exception {
        int databaseSizeBeforeCreate = forumRepository.findAll().size();

        // Create the Forum
        restForumMockMvc.perform(post("/api/forums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(forum)))
            .andExpect(status().isCreated());

        // Validate the Forum in the database
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeCreate + 1);
        Forum testForum = forumList.get(forumList.size() - 1);
    }

    @Test
    public void createForumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = forumRepository.findAll().size();

        // Create the Forum with an existing ID
        forum.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restForumMockMvc.perform(post("/api/forums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(forum)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllForums() throws Exception {
        // Initialize the database
        forumRepository.save(forum);

        // Get all the forumList
        restForumMockMvc.perform(get("/api/forums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forum.getId())));
    }

    @Test
    public void getForum() throws Exception {
        // Initialize the database
        forumRepository.save(forum);

        // Get the forum
        restForumMockMvc.perform(get("/api/forums/{id}", forum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(forum.getId()));
    }

    @Test
    public void getNonExistingForum() throws Exception {
        // Get the forum
        restForumMockMvc.perform(get("/api/forums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateForum() throws Exception {
        // Initialize the database
        forumService.save(forum);

        int databaseSizeBeforeUpdate = forumRepository.findAll().size();

        // Update the forum
        Forum updatedForum = forumRepository.findOne(forum.getId());

        restForumMockMvc.perform(put("/api/forums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedForum)))
            .andExpect(status().isOk());

        // Validate the Forum in the database
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeUpdate);
        Forum testForum = forumList.get(forumList.size() - 1);
    }

    @Test
    public void updateNonExistingForum() throws Exception {
        int databaseSizeBeforeUpdate = forumRepository.findAll().size();

        // Create the Forum

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restForumMockMvc.perform(put("/api/forums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(forum)))
            .andExpect(status().isCreated());

        // Validate the Forum in the database
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteForum() throws Exception {
        // Initialize the database
        forumService.save(forum);

        int databaseSizeBeforeDelete = forumRepository.findAll().size();

        // Get the forum
        restForumMockMvc.perform(delete("/api/forums/{id}", forum.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Forum> forumList = forumRepository.findAll();
        assertThat(forumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Forum.class);
    }
}
