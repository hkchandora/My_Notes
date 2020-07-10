package com.himanshu.mynotes.Model;

public class User {
    String uid, name, image, email;

    public User() {
    }

    public User(String uid, String name, String image, String email) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
