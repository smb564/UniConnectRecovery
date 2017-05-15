package com.smbsoft.uniconnect.service;

import com.smbsoft.uniconnect.domain.Notification;
import com.smbsoft.uniconnect.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Implementation for managing Notification.
 */
@Service
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Save a notification.
     *
     * @param notification the entity to save
     * @return the persisted entity
     */
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        Notification result = notificationRepository.save(notification);
        return result;
    }

    /**
     *  Get all the notifications.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Notification> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        Page<Notification> result = notificationRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one notification by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Notification findOne(String id) {
        log.debug("Request to get Notification : {}", id);
        Notification notification = notificationRepository.findOne(id);
        return notification;
    }

    /**
     *  Delete the  notification by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.delete(id);
    }

    public List<Notification> findForTarget(List<String> target) {
        String department = target.get(1);
        int semester = Integer.parseInt(target.get(0));

        return notificationRepository.findAllByDepartmentAndAndSemesterAndDateGreaterThanEqual(department, semester, LocalDate.now().minusDays(3));
    }
}
