package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.Opportunity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Opportunity entity.
 */
@SuppressWarnings("unused")
public interface OpportunityRepository extends MongoRepository<Opportunity,String> {
    public Page<Opportunity> findAllByOwnerLogin(Pageable pageable, String ownerLogin);
}
