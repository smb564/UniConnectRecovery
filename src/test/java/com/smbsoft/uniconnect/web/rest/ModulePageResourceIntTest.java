package com.smbsoft.uniconnect.web.rest;

import com.smbsoft.uniconnect.UniConnectApp;

import com.smbsoft.uniconnect.domain.ModulePage;
import com.smbsoft.uniconnect.repository.ModulePageRepository;
import com.smbsoft.uniconnect.service.ModulePageService;
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
 * Test class for the ModulePageResource REST controller.
 *
 * @see ModulePageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniConnectApp.class)
public class ModulePageResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MODULE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_CODE = "BBBBBBBBBB";

    @Autowired
    private ModulePageRepository modulePageRepository;

    @Autowired
    private ModulePageService modulePageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restModulePageMockMvc;

    private ModulePage modulePage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModulePageResource modulePageResource = new ModulePageResource(modulePageService);
        this.restModulePageMockMvc = MockMvcBuilders.standaloneSetup(modulePageResource)
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
    public static ModulePage createEntity() {
        ModulePage modulePage = new ModulePage()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .moduleCode(DEFAULT_MODULE_CODE);
        return modulePage;
    }

    @Before
    public void initTest() {
        modulePageRepository.deleteAll();
        modulePage = createEntity();
    }

    @Test
    public void createModulePage() throws Exception {
        int databaseSizeBeforeCreate = modulePageRepository.findAll().size();

        // Create the ModulePage
        restModulePageMockMvc.perform(post("/api/module-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modulePage)))
            .andExpect(status().isCreated());

        // Validate the ModulePage in the database
        List<ModulePage> modulePageList = modulePageRepository.findAll();
        assertThat(modulePageList).hasSize(databaseSizeBeforeCreate + 1);
        ModulePage testModulePage = modulePageList.get(modulePageList.size() - 1);
        assertThat(testModulePage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testModulePage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testModulePage.getModuleCode()).isEqualTo(DEFAULT_MODULE_CODE);
    }

    @Test
    public void createModulePageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modulePageRepository.findAll().size();

        // Create the ModulePage with an existing ID
        modulePage.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restModulePageMockMvc.perform(post("/api/module-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modulePage)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ModulePage> modulePageList = modulePageRepository.findAll();
        assertThat(modulePageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkModuleCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modulePageRepository.findAll().size();
        // set the field null
        modulePage.setModuleCode(null);

        // Create the ModulePage, which fails.

        restModulePageMockMvc.perform(post("/api/module-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modulePage)))
            .andExpect(status().isBadRequest());

        List<ModulePage> modulePageList = modulePageRepository.findAll();
        assertThat(modulePageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllModulePages() throws Exception {
        // Initialize the database
        modulePageRepository.save(modulePage);

        // Get all the modulePageList
        restModulePageMockMvc.perform(get("/api/module-pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modulePage.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].moduleCode").value(hasItem(DEFAULT_MODULE_CODE.toString())));
    }

    @Test
    public void getModulePage() throws Exception {
        // Initialize the database
        modulePageRepository.save(modulePage);

        // Get the modulePage
        restModulePageMockMvc.perform(get("/api/module-pages/{id}", modulePage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modulePage.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.moduleCode").value(DEFAULT_MODULE_CODE.toString()));
    }

    @Test
    public void getNonExistingModulePage() throws Exception {
        // Get the modulePage
        restModulePageMockMvc.perform(get("/api/module-pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateModulePage() throws Exception {
        // Initialize the database
        modulePageService.save(modulePage);

        int databaseSizeBeforeUpdate = modulePageRepository.findAll().size();

        // Update the modulePage
        ModulePage updatedModulePage = modulePageRepository.findOne(modulePage.getId());
        updatedModulePage
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .moduleCode(UPDATED_MODULE_CODE);

        restModulePageMockMvc.perform(put("/api/module-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModulePage)))
            .andExpect(status().isOk());

        // Validate the ModulePage in the database
        List<ModulePage> modulePageList = modulePageRepository.findAll();
        assertThat(modulePageList).hasSize(databaseSizeBeforeUpdate);
        ModulePage testModulePage = modulePageList.get(modulePageList.size() - 1);
        assertThat(testModulePage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testModulePage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testModulePage.getModuleCode()).isEqualTo(UPDATED_MODULE_CODE);
    }

    @Test
    public void updateNonExistingModulePage() throws Exception {
        int databaseSizeBeforeUpdate = modulePageRepository.findAll().size();

        // Create the ModulePage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModulePageMockMvc.perform(put("/api/module-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modulePage)))
            .andExpect(status().isCreated());

        // Validate the ModulePage in the database
        List<ModulePage> modulePageList = modulePageRepository.findAll();
        assertThat(modulePageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteModulePage() throws Exception {
        // Initialize the database
        modulePageService.save(modulePage);

        int databaseSizeBeforeDelete = modulePageRepository.findAll().size();

        // Get the modulePage
        restModulePageMockMvc.perform(delete("/api/module-pages/{id}", modulePage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ModulePage> modulePageList = modulePageRepository.findAll();
        assertThat(modulePageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModulePage.class);
    }
}
