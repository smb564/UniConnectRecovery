package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.Opportunity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Opportunity entity.
 */
@SuppressWarnings("unused")
public interface OpportunityRepository extends MongoRepository<Opportunity,String> {
    public Page<Opportunity> findAllByOwnerLogin(Pageable pageable, String ownerLogin);

    public List<Opportunity> findAllByTagsContainsAndTargetsContaining(List<String> tags, List<List<String>> targets);

    public List<Opportunity> findAllByTargetsContaining(List<List<String>> targets);

    public List<Opportunity> findAllByTagsContains(List<String> tags);
}
