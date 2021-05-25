package com.rishav.firebasedemo.Model;

public class Post {

    private String description;
    private String imageurl;
    private String postid;
    private String publisher;
private String isJob;
private String isEvent;
    public Post() {
    }

    public Post(String description, String imageurl, String postid, String publisher,String isJob,String isEvent) {
        this.description = description;
        this.imageurl = imageurl;
        this.postid = postid;
        this.publisher = publisher;
        this.isJob=isJob;
        this.isEvent=isEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsJob(){return isJob;}

    public void setIsJob(String isJob){this.isJob=isJob;}

    public String getIsEvent(){return isEvent;}
    public void setIsEvent(String isEvent){this.isEvent=isEvent;}
}
