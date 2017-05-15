package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.StudentUser;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the StudentUser entity.
 */
@SuppressWarnings("unused")
public interface StudentUserRepository extends MongoRepository<StudentUser,String> {

    StudentUser findOneByUserId(String userId);
}
