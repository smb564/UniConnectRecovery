package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.ModulePage;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ModulePage entity.
 */
@SuppressWarnings("unused")
public interface ModulePageRepository extends MongoRepository<ModulePage,String> {

}
