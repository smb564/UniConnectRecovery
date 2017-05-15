package com.smbsoft.uniconnect.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.smbsoft.uniconnect.domain.CompanyUser;
import com.smbsoft.uniconnect.domain.UserNotification;
import com.smbsoft.uniconnect.security.SecurityUtils;
import com.smbsoft.uniconnect.service.CompanyUserService;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CompanyUser.
 */
@RestController
@RequestMapping("/api")
public class CompanyUserResource {

    private final Logger log = LoggerFactory.getLogger(CompanyUserResource.class);

    private static final String ENTITY_NAME = "companyUser";

    private final CompanyUserService companyUserService;

    public CompanyUserResource(CompanyUserService companyUserService) {
        this.companyUserService = companyUserService;
    }

    /**
     * POST  /company-users : Create a new companyUser.
     *
     * @param companyUser the companyUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyUser, or with status 400 (Bad Request) if the companyUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-users")
    @Timed
    public ResponseEntity<CompanyUser> createCompanyUser(@Valid @RequestBody CompanyUser companyUser) throws URISyntaxException {
        log.debug("REST request to save CompanyUser : {}", companyUser);
        if (companyUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new companyUser cannot already have an ID")).body(null);
        }

        // Use the current user as the userLogin
        CompanyUser result = companyUserService.save(companyUser.userLogin(SecurityUtils.getCurrentUserLogin()));
        return ResponseEntity.created(new URI("/api/company-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-users : Updates an existing companyUser.
     *
     * @param companyUser the companyUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyUser,
     * or with status 400 (Bad Request) if the companyUser is not valid,
     * or with status 500 (Internal Server Error) if the companyUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-users")
    @Timed
    public ResponseEntity<CompanyUser> updateCompanyUser(@Valid @RequestBody CompanyUser companyUser) throws URISyntaxException {
        log.debug("REST request to update CompanyUser : {}", companyUser);
        if (companyUser.getId() == null) {
            return createCompanyUser(companyUser);
        }

        // Make userLogin as the current user
        CompanyUser result = companyUserService.save(companyUser.userLogin(SecurityUtils.getCurrentUserLogin()));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-users : get all the companyUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companyUsers in body
     */
    @GetMapping("/company-users")
    @Timed
    public ResponseEntity<List<CompanyUser>> getAllCompanyUsers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CompanyUsers");
        Page<CompanyUser> page = companyUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-users/:id : get the "id" companyUser.
     *
     * @param id the id of the companyUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyUser, or with status 404 (Not Found)
     */
    @GetMapping("/company-users/{id}")
    @Timed
    public ResponseEntity<CompanyUser> getCompanyUser(@PathVariable String id) {
        log.debug("REST request to get CompanyUser : {}", id);
        CompanyUser companyUser = companyUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(companyUser));
    }

    /**
     * GET  /company-users/me get the company user for the logged in user
     *
     * @return the ResponseEntity with status 200 (OK) and with body the companyUser, or with status 404 (Not Found)
     */
    @GetMapping("/company-users/me")
    @Timed
    public ResponseEntity<CompanyUser> getCompanyUserByLogin() {
        log.debug("REST request to get CompanyUser for login : {}", SecurityUtils.getCurrentUserLogin());
        CompanyUser companyUser = companyUserService.findByUserLogin(SecurityUtils.getCurrentUserLogin());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(companyUser));
    }

    /**
     * DELETE  /company-users/:id : delete the "id" companyUser.
     *
     * @param id the id of the companyUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyUser(@PathVariable String id) {
        log.debug("REST request to delete CompanyUser : {}", id);
        companyUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /company-users/{login}/notification : Add notifications to student
     *
     * @param userNotification the notification to add
     * @param login ; login of the company_user
     * @return the ResponseEntity with status 201 (Created) and with body the new student_user, or with status 400 (Bad Request) if the student_user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-users/{login}/notification")
    @Timed
    public ResponseEntity<UserNotification> addNotifcation(@Valid @RequestBody UserNotification userNotification, @PathVariable String login) throws URISyntaxException {
        log.debug("REST request to add notification to StudentUser : {} , {}", userNotification, login);

        UserNotification result = companyUserService.addNotification(userNotification, login);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

}
