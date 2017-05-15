package com.smbsoft.uniconnect.service;

import com.smbsoft.uniconnect.domain.Opportunity;
import com.smbsoft.uniconnect.domain.OpportunityFilter;
import com.smbsoft.uniconnect.repository.OpportunityRepository;
import com.smbsoft.uniconnect.security.AuthoritiesConstants;
import com.smbsoft.uniconnect.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.time.LocalDate;
import java.util.*;

/**
 * Service Implementation for managing Opportunity.
 */
@Service
public class OpportunityService {

    private final Logger log = LoggerFactory.getLogger(OpportunityService.class);

    private final OpportunityRepository opportunityRepository;

    public OpportunityService(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    /**
     * Save a opportunity.
     *
     * @param opportunity the entity to save
     * @return the persisted entity
     */
    public Opportunity save(Opportunity opportunity) {
        log.debug("Request to save Opportunity : {}", opportunity);
        Opportunity result = opportunityRepository.save(opportunity);
        return result;
    }

    /**
     *  Get all the opportunities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Opportunity> findAll(Pageable pageable) {
        log.debug("Request to get all Opportunities");
        Page<Opportunity> result = opportunityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one opportunity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Opportunity findOne(String id) {
        log.debug("Request to get Opportunity : {}", id);
        Opportunity opportunity = opportunityRepository.findOne(id);
        return opportunity;
    }

    /**
     *  Delete the  opportunity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Opportunity : {}", id);
        opportunityRepository.delete(id);
    }

    /**
     *  Get all the opportunities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities belong to company admin, send all the entries for admin
     */
    public Page<Opportunity> findAllForUser(Pageable pageable) {
        log.debug("Request to get all Opportunities");

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            return opportunityRepository.findAll(pageable);
        }else{
            if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)){
                return opportunityRepository.findAll(pageable);
            }
            return opportunityRepository.findAllByOwnerLogin(pageable, SecurityUtils.getCurrentUserLogin());
        }
    }

    public List<Opportunity> findForFilter(OpportunityFilter opportunityFilter) {
        // Need to filter based on the requirements
        // First check whether no filter, then send all
        if (opportunityFilter.getTags().isEmpty() && opportunityFilter.getTargets().isEmpty()){
            // Empty return all
            return opportunityRepository.findAllByStartDateAndEndDateBetween(LocalDate.now());
        }

        // Else filter based on the opportunityFilter
        //HashSet to avoid duplicate entries
        Set<Opportunity> results = new HashSet<>();

        // if no tags
        if (opportunityFilter.getTags().isEmpty()){
            for(List<String> target : opportunityFilter.getTargets()){
                List<List<String>> temp = new ArrayList<>();
                temp.add(target);
                List<Opportunity> tempResult = opportunityRepository.findAllByTargetsContainingAndStartDateAndEndDateBetween(temp, LocalDate.now());
                log.debug(tempResult.size() + "");
                for(Opportunity opportunity : tempResult){
                    results.add(opportunity);
                }
            }

            // Add the content of the hash set to a list
            List<Opportunity> output = new ArrayList<>();

            for(Opportunity o : results){
                output.add(o);
            }
        }

        // if no targets, filter only by tags
        if (opportunityFilter.getTargets().isEmpty()){
            return opportunityRepository.findAllByTagsContainsAndStartDateAndEndDateBetween(opportunityFilter.getTags(), LocalDate.now());
        }

        for(List<String> target : opportunityFilter.getTargets()){
            List<List<String>> temp = new ArrayList<>();
            temp.add(target);
            List<Opportunity> tempResult = opportunityRepository.findAllByTagsContainsAndTargetsContainingAndStartDateAndEndDateBetween(opportunityFilter.getTags() , temp, LocalDate.now());
            log.debug(tempResult.size() + "");
            for(Opportunity opportunity : tempResult){
                results.add(opportunity);
            }
        }

        // Add the content of the hash set to a list
        List<Opportunity> output = new ArrayList<>();

        for(Opportunity o : results){
            output.add(o);
        }

        return output;
    }

}
