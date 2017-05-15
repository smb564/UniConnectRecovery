package com.smbsoft.uniconnect.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.smbsoft.uniconnect.domain.StudentUser;
import com.smbsoft.uniconnect.domain.UserNotification;
import com.smbsoft.uniconnect.security.SecurityUtils;
import com.smbsoft.uniconnect.service.StudentUserService;
import com.smbsoft.uniconnect.web.rest.util.HeaderUtil;
import com.smbsoft.uniconnect.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StudentUser.
 */
@RestController
@RequestMapping("/api")
public class StudentUserResource {

    private final Logger log = LoggerFactory.getLogger(StudentUserResource.class);

    private static final String ENTITY_NAME = "student_user";

    private final StudentUserService studentUserService;

    public StudentUserResource(StudentUserService studentUserService) {
        this.studentUserService = studentUserService;
    }

    /**
     * POST  /student-users : Create a new student_user.
     *
     * @param studentUser the student_user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new student_user, or with status 400 (Bad Request) if the student_user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-users")
    @Timed
    public ResponseEntity<StudentUser> createStudentUser(@Valid @RequestBody StudentUser studentUser) throws URISyntaxException {
        log.debug("REST request to save StudentUser : {}", studentUser);
        if (studentUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new student_user cannot already have an ID")).body(null);
        }

        // Set the userId to current user login
        StudentUser result = studentUserService.save(studentUser.userID(SecurityUtils.getCurrentUserLogin()));

        return ResponseEntity.created(new URI("/api/student-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /student-users : Updates an existing student_user.
     *
     * @param studentUser the student_user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated student_user,
     * or with status 400 (Bad Request) if the student_user is not valid,
     * or with status 500 (Internal Server Error) if the student_user couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/student-users")
    @Timed
    public ResponseEntity<StudentUser> updateStudentUser(@Valid @RequestBody StudentUser studentUser) throws URISyntaxException, AccessDeniedException {
        log.debug("REST request to update StudentUser : {}", studentUser);
        if (studentUser.getId() == null) {
            return createStudentUser(studentUser);
        }

        StudentUser result = studentUserService.save(studentUser.userID(SecurityUtils.getCurrentUserLogin()));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studentUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /student-users : get all the student_users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of student_users in body
     */
    @GetMapping("/student-users")
    @Timed
    public ResponseEntity<List<StudentUser>> getAllStudentUsers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Student_users");
        Page<StudentUser> page = studentUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /student-users/:id : get the "id" student_user.
     *
     * @param id the id of the student_user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the student_user, or with status 404 (Not Found)
     */
    @GetMapping("/student-users/{id}")
    @Timed
    public ResponseEntity<StudentUser> getStudentUser(@PathVariable String id) {
        log.debug("REST request to get StudentUser : {}", id);
        StudentUser studentUser = studentUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentUser));
    }

    /**
     * GET  /student-users/me : get Student User for the current logged in user
     *
     * @return the ResponseEntity with status 200 (OK) and with body the student_user, or with status 404 (Not Found)
     */
    @GetMapping("/student-users/me")
    @Timed
    public ResponseEntity<StudentUser> getStudentUserForLogin() {
        log.debug("REST request to get StudentUser for login : {}", SecurityUtils.getCurrentUserLogin());
        StudentUser studentUser = studentUserService.findByUserLogin(SecurityUtils.getCurrentUserLogin());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentUser));
    }



    /**
     * DELETE  /student-users/:id : delete the "id" student_user.
     *
     * @param id the id of the student_user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/student-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudentUser(@PathVariable String id) {
        log.debug("REST request to delete StudentUser : {}", id);
        studentUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /student-users/{id}/notification : Add notifications to student
     *
     * @param userNotification the notification to add
     * @param login ; login of the student_user
     * @return the ResponseEntity with status 201 (Created) and with body the new student_user, or with status 400 (Bad Request) if the student_user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-users/{login}/notification")
    @Timed
    public ResponseEntity<UserNotification> addNotifcation(@Valid @RequestBody UserNotification userNotification, @PathVariable String login) throws URISyntaxException {
        log.debug("REST request to add notification to StudentUser : {} , {}", userNotification, login);

        UserNotification result = studentUserService.addNotification(userNotification, login);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

}
