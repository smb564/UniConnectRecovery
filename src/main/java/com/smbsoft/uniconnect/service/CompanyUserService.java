package com.smbsoft.uniconnect.service;

import com.smbsoft.uniconnect.domain.CompanyUser;
import com.smbsoft.uniconnect.domain.UserNotification;
import com.smbsoft.uniconnect.repository.CompanyUserRepository;
import com.smbsoft.uniconnect.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing CompanyUser.
 */
@Service
public class CompanyUserService {

    private final Logger log = LoggerFactory.getLogger(CompanyUserService.class);

    private final CompanyUserRepository companyUserRepository;

    public CompanyUserService(CompanyUserRepository companyUserRepository) {
        this.companyUserRepository = companyUserRepository;
    }

    /**
     * Save a companyUser.
     *
     * @param companyUser the entity to save
     * @return the persisted entity
     */
    public CompanyUser save(CompanyUser companyUser) {
        log.debug("Request to save CompanyUser : {}", companyUser);

        // Should have one to one mapping, hence delete any previous entry under same userLogin
        // Get previous object
        CompanyUser prevUser = companyUserRepository.findOneByUserLogin(SecurityUtils.getCurrentUserLogin());
        if (prevUser != null){
            // Delete the object
            companyUserRepository.delete(prevUser.getId());
        }

        CompanyUser result = companyUserRepository.save(companyUser);
        return result;
    }

    /**
     *  Get all the companyUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<CompanyUser> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyUsers");
        Page<CompanyUser> result = companyUserRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one companyUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public CompanyUser findOne(String id) {
        log.debug("Request to get CompanyUser : {}", id);
        CompanyUser companyUser = companyUserRepository.findOne(id);
        return companyUser;
    }

    /**
     *  Get one companyUser by userLogin
     *
     *  @param userLogin the userLogin field of the entity
     *  @return the entity
     */
    public CompanyUser findByUserLogin(String userLogin) {
        log.debug("Request to get CompanyUser for userLogin : {}", userLogin);
        CompanyUser companyUser = companyUserRepository.findOneByUserLogin(userLogin);

        if (companyUser == null)
            return new CompanyUser();

        return companyUser;
    }


    /**
     *  Delete the  companyUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete CompanyUser : {}", id);
        companyUserRepository.delete(id);
    }

    public UserNotification addNotification(UserNotification userNotification, String login) {
        CompanyUser companyUser = companyUserRepository.findOneByUserLogin(login);

        // Use optional to avoid null pointer exception
        Optional<List<UserNotification>> userNotificationsOptional = Optional.ofNullable(companyUser.getUserNotifications());

        // If not existing create an empty list
        if(!userNotificationsOptional.isPresent())
            companyUser.setUserNotifications(new ArrayList<>());

        // Now add the notification
        companyUser.getUserNotifications().add(userNotification);

        save(companyUser);

        return userNotification;
    }
}
