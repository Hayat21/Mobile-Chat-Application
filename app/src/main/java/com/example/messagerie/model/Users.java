package com.example.messagerie.model;

public class Users {
    public String username;
    public String id;
    public String imageURL;
    public String status;
    public String email;
    public Users(){

    }

    public Users(String username, String image, String status, String email) {
        this.username = username;
        this.imageURL = image;
        this.status = status;
        this.email = email;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = username;
    }

    public String getImage() {
        return imageURL;
    }

    public void setImage(String image) {
        this.imageURL = image;
    }

    public String getStatus() {
        return status;
    }
    public String getId() {
        return id;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return email;
    }

    public void setThumb_image(String thumb_image) {
        this.email = thumb_image;
    }
    }

