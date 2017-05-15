package com.smbsoft.uniconnect.service;

import com.smbsoft.uniconnect.domain.StudentUser;
import com.smbsoft.uniconnect.domain.UserNotification;
import com.smbsoft.uniconnect.repository.StudentUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing StudentUser.
 */
@Service
public class StudentUserService {

    private final Logger log = LoggerFactory.getLogger(StudentUserService.class);

    private final StudentUserRepository studentUserRepository;

    public StudentUserService(StudentUserRepository studentUserRepository) {
        this.studentUserRepository = studentUserRepository;
    }

    /**
     * Save a student_user.
     *
     * @param studentUser the entity to save
     * @return the persisted entity
     */
    public StudentUser save(StudentUser studentUser) {
        log.debug("Request to save StudentUser : {}", studentUser);
        // Check if previous setting exists
        StudentUser prevUser = studentUserRepository.findOneByUserId(studentUser.getUserId());

        if (prevUser!=null){
            log.debug("Found previous settings, delete student settings");
            studentUserRepository.delete(prevUser.getId());
        }

        StudentUser result = studentUserRepository.save(studentUser);
        return result;
    }

    /**
     *  Get all the student_users.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<StudentUser> findAll(Pageable pageable) {
        log.debug("Request to get all Student_users");
        Page<StudentUser> result = studentUserRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one student_user by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public StudentUser findOne(String id) {
        log.debug("Request to get StudentUser : {}", id);
        StudentUser studentUser = studentUserRepository.findOne(id);
        return studentUser;
    }

    /**
     *  Get one student_user by userLogin.
     *
     *  @param userLogin the id of the current user
     *  @return the entity
     */
    public StudentUser findByUserLogin(String userLogin) {
        log.debug("Request to get StudentUser using user Login : {}", userLogin );
        StudentUser studentUser = studentUserRepository.findOneByUserId(userLogin);

        // If that does not exist in the db, send a null object
        if (studentUser==null)
            return new StudentUser();
        return studentUser;
    }



    /**
     *  Delete the  student_user by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete StudentUser : {}", id);
        studentUserRepository.delete(id);
    }

    public UserNotification addNotification(UserNotification userNotification, String userLogin){
        StudentUser studentUser = studentUserRepository.findOneByUserId(userLogin);

        // Use optional to avoid null pointer exception
        Optional<List<UserNotification>> userNotificationsOptional = Optional.ofNullable(studentUser.getUserNotifications());

        // If not existing create an empty list
        if(!userNotificationsOptional.isPresent())
            studentUser.setUserNotifications(new ArrayList<>());

        // Now add the notification
        studentUser.getUserNotifications().add(userNotification);

        save(studentUser);

        return userNotification;
    }
}
