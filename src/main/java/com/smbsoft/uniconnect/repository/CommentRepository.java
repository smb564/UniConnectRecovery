package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Comment entity.
 */
@SuppressWarnings("unused")
public interface CommentRepository extends MongoRepository<Comment,String> {

}
