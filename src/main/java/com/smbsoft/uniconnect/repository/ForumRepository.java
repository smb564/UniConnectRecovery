package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.Forum;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Forum entity.
 */
@SuppressWarnings("unused")
public interface ForumRepository extends MongoRepository<Forum,String> {

}
