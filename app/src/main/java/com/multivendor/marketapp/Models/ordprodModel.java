package com.multivendor.marketapp.Models;

public class ordprodModel {
  private String prodname;

  public ordprodModel() {

  }

  public ordprodModel(String prodname, Integer prodquat, Integer prodprice) {
    this.prodname = prodname;
    this.prodquat = prodquat;
    this.prodprice = prodprice;
  }

  public String getProdname() {
    return prodname;
  }

  public void setProdname(String prodname) {
    this.prodname = prodname;
  }

  public Integer getProdquat() {
    return prodquat;
  }

  public void setProdquat(Integer prodquat) {
    this.prodquat = prodquat;
  }

  public Integer getProdprice() {
    return prodprice;
  }

  public void setProdprice(Integer prodprice) {
    this.prodprice = prodprice;
  }

  private Integer prodquat;
  private Integer prodprice;
}
