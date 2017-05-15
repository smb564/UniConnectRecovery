package com.smbsoft.uniconnect.domain;

import java.io.Serializable;

/**
 * Created by Supun on 5/14/2017.
 */

public class UserNotification implements Serializable{
    private String notification;

    private boolean viewed;

    public UserNotification(String notification, boolean viewed) {
        this.notification = notification;
        this.viewed = viewed;
    }

    public UserNotification(){
        // Default constructor
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
