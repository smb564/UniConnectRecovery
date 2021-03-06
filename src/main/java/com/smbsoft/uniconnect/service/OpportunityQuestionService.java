package com.smbsoft.uniconnect.service;

import com.smbsoft.uniconnect.domain.Opportunity;
import com.smbsoft.uniconnect.domain.OpportunityQuestion;
import com.smbsoft.uniconnect.repository.OpportunityQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing OpportunityQuestion.
 */
@Service
public class OpportunityQuestionService {

    private final Logger log = LoggerFactory.getLogger(OpportunityQuestionService.class);

    private final OpportunityQuestionRepository opportunityQuestionRepository;
    private final OpportunityService opportunityService;

    public OpportunityQuestionService(OpportunityQuestionRepository opportunityQuestionRepository, OpportunityService opportunityService) {
        this.opportunityQuestionRepository = opportunityQuestionRepository;
        this.opportunityService = opportunityService;
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

    public List<OpportunityQuestion> findForOpportunity(String id) {
        Opportunity opportunity = opportunityService.findOne(id);

        Optional<List<String>> questionsOptional = Optional.ofNullable(opportunity.getQuestions());

        if(!questionsOptional.isPresent())
            return new ArrayList<>();

        List<OpportunityQuestion> result = new ArrayList<>();

        // Get the questions
        for(String oq : questionsOptional.get()){
            result.add(findOne(oq));
        }

        return result;
    }


    public OpportunityQuestion addQuestion(OpportunityQuestion oq, String id){
        OpportunityQuestion result = save(oq);

        Opportunity opportunity = opportunityService.findOne(id);

        Optional<List<String>> questionsOptional = Optional.ofNullable(opportunity.getQuestions());

        if(!questionsOptional.isPresent())
            opportunity.setQuestions(new ArrayList<>());

        opportunity.getQuestions().add(result.getId());

        opportunityService.save(opportunity);

        return result;
    }

    public void upVote(String questionId, String userId) {
        OpportunityQuestion oq = opportunityQuestionRepository.findOne(questionId);

        Optional<List<String>> votedOptional = Optional.ofNullable(oq.getVotedUsers());

        if(!votedOptional.isPresent()){
            log.debug("No voted list found, so create new list and upvote");
            oq.upVote();
            oq.setVotedUsers(new ArrayList<>());
            oq.getVotedUsers().add(userId);

            opportunityQuestionRepository.save(oq);
            return;
        }

//        for(String user : votedOptional.get()){
//            if (user.equals(userId)){
//                log.debug("Found an already voted member, so down vote");
//                oq.downVote();
//                oq.getVotedUsers().remove(userId);
//                opportunityQuestionRepository.save(oq);
//                return;
//            }
//        }

        if (oq.getVotedUsers().contains(userId)){
            log.debug("Found an already voted member, so down vote");
            oq.downVote();
            oq.getVotedUsers().remove(userId);
            opportunityQuestionRepository.save(oq);
            return;
        }

        // should be new vote
        oq.upVote();
        oq.getVotedUsers().add(userId);
        opportunityQuestionRepository.save(oq);
        log.debug("Found a new member, so upvote");
    }

    public void deleteFromOpportunity(String questionId, String opportunityId) {
        // Delete the pointer of Opportunity
        Opportunity opportunity = opportunityService.findOne(opportunityId);
        opportunity.getQuestions().remove(questionId);
        opportunityService.save(opportunity);

        // now delete the question as normal
        delete(questionId);
    }
}
