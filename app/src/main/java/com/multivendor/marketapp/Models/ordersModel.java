package com.multivendor.marketapp.Models;

import java.util.ArrayList;

public class ordersModel {


    public ordersModel() {

    }
    public ordersModel(String ordertickid, String orderdate, String orderstats,ArrayList<String> orderitemdetails, Integer amount, String orderseller, String orderlocat) {
        this.ordertickid = ordertickid;
        this.orderdate = orderdate;
        this.orderstats=orderstats;
        this.orderitemdetails = orderitemdetails;
        Amount = amount;
        this.orderseller = orderseller;
        this.orderlocat = orderlocat;

    }

    private String ordertickid;

    public String getOrdertickid() {
        return ordertickid;
    }

    public void setOrdertickid(String ordertickid) {
        this.ordertickid = ordertickid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public ArrayList<String> getOrderitemdetails() {
        return orderitemdetails;
    }

    public void setOrderitemdetails(ArrayList<String> orderitemdetails) {
        this.orderitemdetails = orderitemdetails;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public String getOrderseller() {
        return orderseller;
    }

    public void setOrderseller(String orderseller) {
        this.orderseller = orderseller;
    }

    public String getOrderlocat() {
        return orderlocat;
    }

    public void setOrderlocat(String orderlocat) {
        this.orderlocat = orderlocat;
    }

    private String orderdate;

    public String getOrderstats() {
        return orderstats;
    }

    public void setOrderstats(String orderstats) {
        this.orderstats = orderstats;
    }


    private String orderstats;
    private ArrayList<String> orderitemdetails;
    private Integer Amount;
    private String orderseller;
    private String orderlocat;

}
