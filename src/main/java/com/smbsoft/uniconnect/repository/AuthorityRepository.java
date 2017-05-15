package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.Authority;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
