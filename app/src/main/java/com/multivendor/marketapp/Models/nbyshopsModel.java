package com.multivendor.marketapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class nbyshopsModel {

    private String id;
    private String user_id;

    @SerializedName("image")
    private String shopimg;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
    // change this to list of categories
    @SerializedName("categories")
    private List<categoriesModel> shopcat;

    public List<categoriesModel> getShopcat() {
        return shopcat;
    }

    public void setShopcat(List<categoriesModel> shopcat) {
        this.shopcat = shopcat;
    }

    public nbyshopsModel() {
    }


    public String getShoplocation() {
        return shoplocation;
    }

    public void setShoplocation(String shoplocation) {
        this.shoplocation = shoplocation;
    }

    public String getShopdistance() {
        return shopdistance;
    }

    public void setShopdistance(String shopdistance) {
        this.shopdistance = shopdistance;
    }

    public String getShopimg() {
        return shopimg;
    }

    public void setShopimg(String shopimg) {
        this.shopimg = shopimg;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }


    @SerializedName("store_name")
    private String shopname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShopabout() {
        return shopabout;
    }

    public void setShopabout(String shopabout) {
        this.shopabout = shopabout;
    }

    @SerializedName("about")
    private String shopabout;

    private Integer rating;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @SerializedName("address")
    private String shoplocation;
    @SerializedName("distance")
    private String shopdistance;
}
