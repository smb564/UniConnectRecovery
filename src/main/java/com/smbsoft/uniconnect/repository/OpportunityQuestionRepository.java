package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.OpportunityQuestion;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the OpportunityQuestion entity.
 */
@SuppressWarnings("unused")
public interface OpportunityQuestionRepository extends MongoRepository<OpportunityQuestion,String> {

}
