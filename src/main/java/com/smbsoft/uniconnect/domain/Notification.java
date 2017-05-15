package com.smbsoft.uniconnect.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Notification.
 */

@Document(collection = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Min(value = 1)
    @Max(value = 8)
    @Field("semester")
    private Integer semester;

    @NotNull
    @Field("department")
    private String department;

    @Field("notification")
    private String notification;

    @Field("date")
    private LocalDate date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSemester() {
        return semester;
    }

    public Notification semester(Integer semester) {
        this.semester = semester;
        return this;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public Notification department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getNotification() {
        return notification;
    }

    public Notification notification(String notification) {
        this.notification = notification;
        return this;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public LocalDate getDate() {
        return date;
    }

    public Notification date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification notification = (Notification) o;
        if (notification.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + id +
            ", semester='" + semester + "'" +
            ", department='" + department + "'" +
            ", notification='" + notification + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
