package com.smbsoft.uniconnect.web.rest;

import com.smbsoft.uniconnect.UniConnectApp;

import com.smbsoft.uniconnect.domain.CompanyUser;
import com.smbsoft.uniconnect.repository.CompanyUserRepository;
import com.smbsoft.uniconnect.service.CompanyUserService;
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

import com.smbsoft.uniconnect.domain.enumeration.CompanyType;
/**
 * Test class for the CompanyUserResource REST controller.
 *
 * @see CompanyUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniConnectApp.class)
public class CompanyUserResourceIntTest {

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final CompanyType DEFAULT_TYPE = CompanyType.Company;
    private static final CompanyType UPDATED_TYPE = CompanyType.Research;

    private static final String DEFAULT_USER_LOGIN = "User";
    private static final String UPDATED_USER_LOGIN = "NEW User";

    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restCompanyUserMockMvc;

    private CompanyUser companyUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyUserResource companyUserResource = new CompanyUserResource(companyUserService);
        this.restCompanyUserMockMvc = MockMvcBuilders.standaloneSetup(companyUserResource)
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
    public static CompanyUser createEntity() {
        CompanyUser companyUser = new CompanyUser()
            .company(DEFAULT_COMPANY)
            .type(DEFAULT_TYPE)
            .userLogin(DEFAULT_USER_LOGIN);
        return companyUser;
    }

    @Before
    public void initTest() {
        companyUserRepository.deleteAll();
        companyUser = createEntity();
    }

    @Test
    public void createCompanyUser() throws Exception {
        int databaseSizeBeforeCreate = companyUserRepository.findAll().size();

        // Create the CompanyUser
        restCompanyUserMockMvc.perform(post("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isCreated());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCompanyUser.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    public void createCompanyUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyUserRepository.findAll().size();

        // Create the CompanyUser with an existing ID
        companyUser.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyUserMockMvc.perform(post("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkCompanyIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyUserRepository.findAll().size();
        // set the field null
        companyUser.setCompany(null);

        // Create the CompanyUser, which fails.

        restCompanyUserMockMvc.perform(post("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isBadRequest());

        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCompanyUsers() throws Exception {
        // Initialize the database
        companyUserRepository.save(companyUser);

        // Get all the companyUserList
        restCompanyUserMockMvc.perform(get("/api/company-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyUser.getId())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN.toString())));
    }

    @Test
    public void getCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.save(companyUser);

        // Get the companyUser
        restCompanyUserMockMvc.perform(get("/api/company-users/{id}", companyUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyUser.getId()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN.toString()));
    }

    @Test
    public void getNonExistingCompanyUser() throws Exception {
        // Get the companyUser
        restCompanyUserMockMvc.perform(get("/api/company-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCompanyUser() throws Exception {
        // Initialize the database
        companyUserService.save(companyUser);

        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Update the companyUser
        CompanyUser updatedCompanyUser = companyUserRepository.findOne(companyUser.getId());
        updatedCompanyUser
            .company(UPDATED_COMPANY)
            .type(UPDATED_TYPE)
            .userLogin(UPDATED_USER_LOGIN);

        restCompanyUserMockMvc.perform(put("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyUser)))
            .andExpect(status().isOk());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCompanyUser.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    public void updateNonExistingCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Create the CompanyUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyUserMockMvc.perform(put("/api/company-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isCreated());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteCompanyUser() throws Exception {
        // Initialize the database
        companyUserService.save(companyUser);

        int databaseSizeBeforeDelete = companyUserRepository.findAll().size();

        // Get the companyUser
        restCompanyUserMockMvc.perform(delete("/api/company-users/{id}", companyUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyUser.class);
    }
}
