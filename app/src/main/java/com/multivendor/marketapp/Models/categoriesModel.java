package com.multivendor.marketapp.Models;

public class categoriesModel {
    private String image;
    private String name;
    private String id;
    private String store_id;

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public categoriesModel() {

    }
    public categoriesModel(String image, String name) {
        this.image=image;
        this.name = name;
    }


}
