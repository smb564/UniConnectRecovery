package com.smbsoft.uniconnect.web.rest;

import com.smbsoft.uniconnect.UniConnectApp;

import com.smbsoft.uniconnect.domain.StudentUser;
import com.smbsoft.uniconnect.repository.StudentUserRepository;
import com.smbsoft.uniconnect.service.StudentUserService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StudentUserResource REST controller.
 *
 * @see StudentUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UniConnectApp.class)
public class StudentUserResourceIntTest {

    private static final Boolean DEFAULT_GRADUATE = false;
    private static final Boolean UPDATED_GRADUATE = true;

    private static final Integer DEFAULT_GRADUATE_YEAR = 2000;
    private static final Integer UPDATED_GRADUATE_YEAR = 2002;

    private static final Integer DEFAULT_CURRENT_SEMESTER = 1;
    private static final Integer UPDATED_CURRENT_SEMESTER = 2;

    private static final String[] DEFAULT_INTEREST = {"interest1", "interest2"};
    private static final String[] UPDATED_INTEREST = {"interest1", "interest2", "interest3"};

    private static final String DEFAULT_USER_ID = "user-3";

    @Autowired
    private StudentUserRepository studentUserRepository;

    @Autowired
    private StudentUserService studentUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restStudentUserMockMvc;

    private StudentUser studentUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentUserResource studentUserResource = new StudentUserResource(studentUserService);
        this.restStudentUserMockMvc = MockMvcBuilders.standaloneSetup(studentUserResource)
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
    public static StudentUser createEntity() {
        StudentUser studentUser = new StudentUser()
            .graduate(DEFAULT_GRADUATE)
            .graduateYear(DEFAULT_GRADUATE_YEAR)
            .currentSemester(DEFAULT_CURRENT_SEMESTER)
            .interests(DEFAULT_INTEREST)
            .userID(DEFAULT_USER_ID);
        return studentUser;
    }

    @Before
    public void initTest() {
        studentUserRepository.deleteAll();
        studentUser = createEntity();
    }

    @Test
    public void createStudentUser() throws Exception {
        int databaseSizeBeforeCreate = studentUserRepository.findAll().size();
        System.out.println(studentUser);

        System.out.println(new String(TestUtil.convertObjectToJsonBytes(studentUser)));

        // Create the StudentUser
        restStudentUserMockMvc.perform(post("/api/student-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentUser)))
            .andExpect(status().isCreated());

        // Validate the StudentUser in the database
        List<StudentUser> studentUserList = studentUserRepository.findAll();

        //assertThat(studentUserList).hasSize(databaseSizeBeforeCreate + 1);
        StudentUser testStudentUser = studentUserList.get(studentUserList.size() - 1);
        assertThat(testStudentUser.isGraduate()).isEqualTo(DEFAULT_GRADUATE);
        assertThat(testStudentUser.getGraduateYear()).isEqualTo(DEFAULT_GRADUATE_YEAR);
        assertThat(testStudentUser.getCurrentSemester()).isEqualTo(DEFAULT_CURRENT_SEMESTER);
        // Assert interests are currect
        assertThat(testStudentUser.getInterests()).isEqualTo(DEFAULT_INTEREST);
    }

    @Test
    public void createStudentUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentUserRepository.findAll().size();

        // Create the StudentUser with an existing ID
        studentUser.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentUserMockMvc.perform(post("/api/student-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentUser)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StudentUser> studentUserList = studentUserRepository.findAll();
        assertThat(studentUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllStudentUsers() throws Exception {
        // Initialize the database
        studentUserRepository.save(studentUser);

        // Get all the student_userList
        ResultActions ra = restStudentUserMockMvc.perform(get("/api/student-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentUser.getId())))
            .andExpect(jsonPath("$.[*].graduate").value(hasItem(DEFAULT_GRADUATE.booleanValue())))
            .andExpect(jsonPath("$.[*].graduateYear").value(hasItem(DEFAULT_GRADUATE_YEAR)))
            .andExpect(jsonPath("$.[*].currentSemester").value(hasItem(DEFAULT_CURRENT_SEMESTER)));

        for(int i = 0; i < DEFAULT_INTEREST.length ; i++){
            ra.andExpect(jsonPath("$.[*].interests["+i+"]").value(hasItem(DEFAULT_INTEREST[i])));
        }

    }

    @Test
    public void getStudentUser() throws Exception {
        // Initialize the database
        studentUserRepository.save(studentUser);

        // Get the studentUser
        ResultActions ra = restStudentUserMockMvc.perform(get("/api/student-users/{id}", studentUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studentUser.getId()))
            .andExpect(jsonPath("$.graduate").value(DEFAULT_GRADUATE.booleanValue()))
            .andExpect(jsonPath("$.graduateYear").value(DEFAULT_GRADUATE_YEAR))
            .andExpect(jsonPath("$.currentSemester").value(DEFAULT_CURRENT_SEMESTER));

        for(int i = 0; i < DEFAULT_INTEREST.length ; i++){
            ra.andExpect(jsonPath("$.interests["+i+"]").value(DEFAULT_INTEREST[i]));
        }


    }

    @Test
    public void getNonExistingStudentUser() throws Exception {
        // Get the studentUser
        restStudentUserMockMvc.perform(get("/api/student-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStudentUser() throws Exception {
        // Initialize the database
        studentUserService.save(studentUser);

        int databaseSizeBeforeUpdate = studentUserRepository.findAll().size();

        // Update the studentUser
        StudentUser updatedStudentUser = studentUserRepository.findOne(studentUser.getId());
        updatedStudentUser
            .graduate(UPDATED_GRADUATE)
            .graduateYear(UPDATED_GRADUATE_YEAR)
            .currentSemester(UPDATED_CURRENT_SEMESTER)
            .interests(UPDATED_INTEREST);

        restStudentUserMockMvc.perform(put("/api/student-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudentUser)))
            .andExpect(status().isOk());

        // Validate the StudentUser in the database
        List<StudentUser> studentUserList = studentUserRepository.findAll();
        assertThat(studentUserList).hasSize(databaseSizeBeforeUpdate);
        StudentUser testStudentUser = studentUserList.get(studentUserList.size() - 1);
        assertThat(testStudentUser.isGraduate()).isEqualTo(UPDATED_GRADUATE);
        assertThat(testStudentUser.getGraduateYear()).isEqualTo(UPDATED_GRADUATE_YEAR);
        assertThat(testStudentUser.getCurrentSemester()).isEqualTo(UPDATED_CURRENT_SEMESTER);
        assertThat(testStudentUser.getInterests()).isEqualTo(UPDATED_INTEREST);
    }

    @Test
    public void updateNonExistingStudent_user() throws Exception {
        int databaseSizeBeforeUpdate = studentUserRepository.findAll().size();

        // Create the StudentUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentUserMockMvc.perform(put("/api/student-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentUser)))
            .andExpect(status().isCreated());

        // Validate the StudentUser in the database
        List<StudentUser> studentUserList = studentUserRepository.findAll();
        assertThat(studentUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteStudentUser() throws Exception {
        // Initialize the database
        studentUserService.save(studentUser);

        int databaseSizeBeforeDelete = studentUserRepository.findAll().size();

        // Get the studentUser
        restStudentUserMockMvc.perform(delete("/api/student-users/{id}", studentUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StudentUser> student_userList = studentUserRepository.findAll();
        assertThat(student_userList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentUser.class);
    }
}
