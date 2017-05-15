package com.smbsoft.uniconnect.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.smbsoft.uniconnect.domain.enumeration.CompanyType;

/**
 * A CompanyUser.
 */

@Document(collection = "company_user")
public class CompanyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("company")
    private String company;

    @Field("type")
    private CompanyType type;

    @Field("user_login")
    private String userLogin;

    @Field("user_notifications")
    private List<UserNotification> userNotifications;

    public List<UserNotification> getUserNotifications() {
        return userNotifications;
    }

    public void setUserNotifications(List<UserNotification> userNotifications) {
        this.userNotifications = userNotifications;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public CompanyUser company(String company) {
        this.company = company;
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public CompanyType getType() {
        return type;
    }

    public CompanyUser type(CompanyType type) {
        this.type = type;
        return this;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public CompanyUser userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyUser companyUser = (CompanyUser) o;
        if (companyUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, companyUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyUser{" +
            "id=" + id +
            ", company='" + company + "'" +
            ", type='" + type + "'" +
            ", userLogin='" + userLogin + "'" +
            '}';
    }
}
