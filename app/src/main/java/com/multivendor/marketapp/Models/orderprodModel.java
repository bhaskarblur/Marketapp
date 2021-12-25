package com.multivendor.marketapp.Models;

public class orderprodModel {

    private String itemimg;


    public String getBelcategory() {
        return belcategory;
    }

    public void setBelcategory(String belcategory) {
        this.belcategory = belcategory;
    }

    private String belcategory;
    public orderprodModel() {

    }

    public float getProdprice() {
        return prodprice;
    }

    public void setProdprice(float prodprice) {
        this.prodprice = prodprice;
    }

    public Integer getSelquat() {
        return selquat;
    }

    public void setSelquat(Integer selquat) {
        this.selquat = selquat;
    }

    public orderprodModel(String itemimg, String itemname, float prodprice, String selsize, Integer selquat,
                          String belcategory) {
        this.itemimg = itemimg;
        this.itemname = itemname;
        this.selsize=selsize;
        this.selquat=selquat;
        this.prodprice=prodprice;
        this.belcategory=belcategory;
    }

    private String itemname;
    private float prodprice;
    private String selsize;

    public String getSelsize() {
        return selsize;
    }

    public void setSelsize(String selsize) {
        this.selsize = selsize;
    }

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

    private Integer selquat;

}
