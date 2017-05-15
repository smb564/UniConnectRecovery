package com.smbsoft.uniconnect.service;

import com.smbsoft.uniconnect.domain.OpportunityQuestion;
import com.smbsoft.uniconnect.repository.OpportunityQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing OpportunityQuestion.
 */
@Service
public class OpportunityQuestionService {

    private final Logger log = LoggerFactory.getLogger(OpportunityQuestionService.class);
    
    private final OpportunityQuestionRepository opportunityQuestionRepository;

    public OpportunityQuestionService(OpportunityQuestionRepository opportunityQuestionRepository) {
        this.opportunityQuestionRepository = opportunityQuestionRepository;
    }

    /**
     * Save a opportunityQuestion.
     *
     * @param opportunityQuestion the entity to save
     * @return the persisted entity
     */
    public OpportunityQuestion save(OpportunityQuestion opportunityQuestion) {
        log.debug("Request to save OpportunityQuestion : {}", opportunityQuestion);
        OpportunityQuestion result = opportunityQuestionRepository.save(opportunityQuestion);
        return result;
    }

    /**
     *  Get all the opportunityQuestions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<OpportunityQuestion> findAll(Pageable pageable) {
        log.debug("Request to get all OpportunityQuestions");
        Page<OpportunityQuestion> result = opportunityQuestionRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one opportunityQuestion by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public OpportunityQuestion findOne(String id) {
        log.debug("Request to get OpportunityQuestion : {}", id);
        OpportunityQuestion opportunityQuestion = opportunityQuestionRepository.findOne(id);
        return opportunityQuestion;
    }

    /**
     *  Delete the  opportunityQuestion by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete OpportunityQuestion : {}", id);
        opportunityQuestionRepository.delete(id);
    }
}
