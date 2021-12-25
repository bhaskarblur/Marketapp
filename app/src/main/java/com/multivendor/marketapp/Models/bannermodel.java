package com.multivendor.marketapp.Models;

public class bannermodel {

    private String id;
    private String image;

    public bannermodel() {

    }

    public bannermodel(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
