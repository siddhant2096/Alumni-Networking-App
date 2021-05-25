package com.rishav.firebasedemo.Model;

public class User {

    private String name;
    private String email;
    private String username;
    private String bio;
    private String imageurl;
    private String id;
    private String type_user;
private String type_job;
private String batch;
    public User() {
    }

    public User(String name, String email, String username, String bio, String imageurl, String id,String type_user,String type_job,String batch) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.imageurl = imageurl;
        this.id = id;
        this.type_user=type_user;
        this.type_job=type_job;
        this.batch=batch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getType_user(){return type_user;}
    public void setType_user(String type_user){this.type_user=type_user;}
    public String getType_job(){return type_job;}
    public void setType_job(String type_job){this.type_job=type_job;}
    public String  getBatch(){return batch;}
    public void setBatch(String batch){this.batch=batch;}


}
