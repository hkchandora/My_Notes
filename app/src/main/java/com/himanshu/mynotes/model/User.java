package com.himanshu.mynotes.model;

public class User {
    String name, photoUrl, emailId;

    public User() {
    }

    public User(String name, String photoUrl, String emailId) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}