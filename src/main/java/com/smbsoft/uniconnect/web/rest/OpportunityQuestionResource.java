package com.smbsoft.uniconnect.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.smbsoft.uniconnect.domain.OpportunityQuestion;
import com.smbsoft.uniconnect.security.SecurityUtils;
import com.smbsoft.uniconnect.service.OpportunityQuestionService;
import com.smbsoft.uniconnect.web.rest.util.HeaderUtil;
import com.smbsoft.uniconnect.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OpportunityQuestion.
 */
@RestController
@RequestMapping("/api")
public class OpportunityQuestionResource {

    private final Logger log = LoggerFactory.getLogger(OpportunityQuestionResource.class);

    private static final String ENTITY_NAME = "opportunityQuestion";

    private final OpportunityQuestionService opportunityQuestionService;

    public OpportunityQuestionResource(OpportunityQuestionService opportunityQuestionService) {
        this.opportunityQuestionService = opportunityQuestionService;
    }

    /**
     * POST  /opportunity-questions : Create a new opportunityQuestion.
     *
     * @param opportunityQuestion the opportunityQuestion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new opportunityQuestion, or with status 400 (Bad Request) if the opportunityQuestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/opportunity-questions")
    @Timed
    public ResponseEntity<OpportunityQuestion> createOpportunityQuestion(@Valid @RequestBody OpportunityQuestion opportunityQuestion) throws URISyntaxException {
        log.debug("REST request to save OpportunityQuestion : {}", opportunityQuestion);
        if (opportunityQuestion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new opportunityQuestion cannot already have an ID")).body(null);
        }

        opportunityQuestion = opportunityQuestion.date(LocalDate.now()).ownerLogin(SecurityUtils.getCurrentUserLogin()).votes(0);
        OpportunityQuestion result = opportunityQuestionService.save(opportunityQuestion);
        return ResponseEntity.created(new URI("/api/opportunity-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /opportunity-questions : Updates an existing opportunityQuestion.
     *
     * @param opportunityQuestion the opportunityQuestion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated opportunityQuestion,
     * or with status 400 (Bad Request) if the opportunityQuestion is not valid,
     * or with status 500 (Internal Server Error) if the opportunityQuestion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/opportunity-questions")
    @Timed
    public ResponseEntity<OpportunityQuestion> updateOpportunityQuestion(@Valid @RequestBody OpportunityQuestion opportunityQuestion) throws URISyntaxException {
        log.debug("REST request to update OpportunityQuestion : {}", opportunityQuestion);
        if (opportunityQuestion.getId() == null) {
            return createOpportunityQuestion(opportunityQuestion);
        }
        OpportunityQuestion result = opportunityQuestionService.save(opportunityQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, opportunityQuestion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /opportunity-questions : get all the opportunityQuestions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of opportunityQuestions in body
     */
    @GetMapping("/opportunity-questions")
    @Timed
    public ResponseEntity<List<OpportunityQuestion>> getAllOpportunityQuestions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of OpportunityQuestions");
        Page<OpportunityQuestion> page = opportunityQuestionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/opportunity-questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /opportunity-questions/:id : get the "id" opportunityQuestion.
     *
     * @param id the id of the opportunityQuestion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the opportunityQuestion, or with status 404 (Not Found)
     */
    @GetMapping("/opportunity-questions/{id}")
    @Timed
    public ResponseEntity<OpportunityQuestion> getOpportunityQuestion(@PathVariable String id) {
        log.debug("REST request to get OpportunityQuestion : {}", id);
        OpportunityQuestion opportunityQuestion = opportunityQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(opportunityQuestion));
    }

    /**
     * DELETE  /opportunity-questions/:id : delete the "id" opportunityQuestion.
     *
     * @param id the id of the opportunityQuestion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/opportunity-questions/{id}")
    @Timed
    public ResponseEntity<Void> deleteOpportunityQuestion(@PathVariable String id) {
        log.debug("REST request to delete OpportunityQuestion : {}", id);
        opportunityQuestionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /opportunity-questions/{id} : get all the opportunityQuestions with opportunity id.
     *
     * @param id the id of the post
     * @return the ResponseEntity with status 200 (OK) and the list of opportunityQuestions in body
     */
    @GetMapping("/opportunity/{id}/questions")
    @Timed
    public ResponseEntity<List<OpportunityQuestion>> getAllOpportunityQuestions(@PathVariable String id) {
        log.debug("REST request to get OpportunityQuestions for Opportunity {}", id);

        List<OpportunityQuestion> result = opportunityQuestionService.findForOpportunity(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }


    /**
     * POST  /opportunity-questions : Create a new opportunityQuestion.
     *
     * @param opportunityQuestion the opportunityQuestion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new opportunityQuestion, or with status 400 (Bad Request) if the opportunityQuestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/opportunity/{id}/questions")
    @Timed
    public ResponseEntity<OpportunityQuestion> createOpportunityQuestionToOpportunity(@Valid @RequestBody OpportunityQuestion opportunityQuestion, @PathVariable String id) throws URISyntaxException {
        log.debug("REST request to save OpportunityQuestion to question: {}", opportunityQuestion);
        if (opportunityQuestion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new opportunityQuestion cannot already have an ID")).body(null);
        }

        opportunityQuestion = opportunityQuestion.date(LocalDate.now()).ownerLogin(SecurityUtils.getCurrentUserLogin()).votes(0);

        OpportunityQuestion result = opportunityQuestionService.addQuestion(opportunityQuestion, id);
        return ResponseEntity.created(new URI("/api/opportunity-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @GetMapping("/questions/{questionId}/user/{userId}/upvote")
    @ResponseStatus(value = HttpStatus.OK)
    public void upvoteQuestion(@PathVariable String questionId, @PathVariable String userId) throws URISyntaxException {
        log.debug("REST request to upvote OpportunityQuestion : {}", questionId);

        opportunityQuestionService.upVote(questionId, userId);
    }

    @DeleteMapping("/questions/{questionId}/opportunity/{opportunityId}")
    @Timed
    public ResponseEntity<Void> deleteQuestionFromOpportunity(@PathVariable String questionId, @PathVariable String opportunityId) {
        log.debug("REST request to delete OpportunityQuestion from Opportunity: {} {}", questionId, opportunityId);
        opportunityQuestionService.deleteFromOpportunity(questionId, opportunityId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, "")).build();
    }
}
