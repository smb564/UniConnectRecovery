package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.Notification;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data MongoDB repository for the Notification entity.
 */
@SuppressWarnings("unused")
public interface NotificationRepository extends MongoRepository<Notification,String> {
    public List<Notification> findAllByDepartmentAndAndSemesterAndDateGreaterThanEqual(String department, int semester, LocalDate date);

}
