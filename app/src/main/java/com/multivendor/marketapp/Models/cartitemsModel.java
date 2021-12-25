package com.multivendor.marketapp.Models;

import java.util.List;

public class cartitemsModel {

    private Integer itemtotalprice;
    private String shopname;

    public cartitemsModel() {

    }

    public cartitemsModel(Integer itemtotalprice, String shopname, String itemimg, String belcategory, String itemname, float smallitemprice, float bigitemprice, String smallsize, String bigsize, Integer availquat, Integer selquat, String shoplocat) {
        this.itemname = itemname;
        this.smallitemprice = smallitemprice;
        this.bigitemprice = bigitemprice;
        this.smallsize = smallsize;
        this.bigsize = bigsize;
        this.availquat = availquat;
        this.belcategory = belcategory;
        this.selquat = selquat;
        this.shoplocat = shoplocat;
        this.itemtotalprice = itemtotalprice;
        this.shopname = shopname;
        this.itemimg = itemimg;


    }

    private String itemimg;

    public Integer getSelquat() {
        return selquat;
    }

    public void setSelquat(Integer selquat) {
        this.selquat = selquat;
    }

    public String getBelcategory() {
        return belcategory;
    }

    public void setBelcategory(String belcategory) {
        this.belcategory = belcategory;
    }

    private String belcategory;


    private String itemname;
    private float smallitemprice;

    public float getBigitemprice() {
        return bigitemprice;
    }

    public void setBigitemprice(float bigitemprice) {
        this.bigitemprice = bigitemprice;
    }

    private float bigitemprice;

    public String getItemimg() {
        return itemimg;
    }

    public void setItemimg(String itemimg) {
        this.itemimg = itemimg;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public float getsmallItemprice() {
        return smallitemprice;
    }

    public void setsmallItemprice(float smallitemprice) {
        this.smallitemprice=smallitemprice;
    }

    public Integer getAvailquat() {
        return availquat;
    }

    public void setAvailquat(Integer availquat) {
        this.availquat = availquat;
    }
    private String smallsize;


    public String getSmallsize() {
        return smallsize;
    }

    public void setSmallsize(String smallsize) {
        this.smallsize = smallsize;
    }

    public String getBigsize() {
        return bigsize;
    }

    public void setBigsize(String bigsize) {
        this.bigsize = bigsize;
    }

    private String bigsize;
    private Integer availquat;
    private Integer selquat;

    public Integer getItemtotalprice() {
        return itemtotalprice;
    }

    public void setItemtotalprice(Integer itemtotalprice) {
        this.itemtotalprice = itemtotalprice;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }


    public String getShoplocat() {
        return shoplocat;
    }

    public void setShoplocat(String shoplocat) {
        this.shoplocat = shoplocat;
    }

    private String shoplocat;

}
