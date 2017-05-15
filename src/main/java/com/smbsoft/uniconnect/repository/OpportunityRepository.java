package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.Opportunity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data MongoDB repository for the Opportunity entity.
 */
@SuppressWarnings("unused")
public interface OpportunityRepository extends MongoRepository<Opportunity,String> {
    public Page<Opportunity> findAllByOwnerLogin(Pageable pageable, String ownerLogin);

    public List<Opportunity> findAllByTagsContainsAndTargetsContainingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(List<String> tags, List<List<String>> targets, LocalDate start, LocalDate end);

    public List<Opportunity> findAllByTargetsContainingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(List<List<String>> targets, LocalDate start, LocalDate end);

    public List<Opportunity> findAllByTagsContainsAndStartDateLessThanEqualAndEndDateGreaterThanEqual(List<String> tags, LocalDate start, LocalDate end);

    public List<Opportunity> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate start, LocalDate end);
}
