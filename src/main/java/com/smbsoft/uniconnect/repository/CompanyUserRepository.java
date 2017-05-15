package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.CompanyUser;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the CompanyUser entity.
 */
@SuppressWarnings("unused")
public interface CompanyUserRepository extends MongoRepository<CompanyUser,String> {

    CompanyUser findOneByUserLogin(String userLogin);
}
