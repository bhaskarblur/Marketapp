package com.multivendor.marketapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class productitemModel {

    @SerializedName("image")
    private String itemimg;

    @SerializedName("user_id")
    private String userid;

    private String category_id;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    @SerializedName("variation")
    private List<sizeandquat> sizeandquats;
    @SerializedName("product_name")
    private String itemname;

    private String created_at;
    private String updated_at;
    private String product_id;
    private String sku;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemimg() {
        return itemimg;
    }

    public void setItemimg(String itemimg) {
        this.itemimg = itemimg;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<sizeandquat> getSizeandquats() {
        return sizeandquats;
    }

    public void setSizeandquats(List<sizeandquat> sizeandquats) {
        this.sizeandquats = sizeandquats;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public class sizeandquat {

        private String variation_id;
        private String product_id;
        private String size;

        public String getVariation_id() {
            return variation_id;
        }

        public void setVariation_id(String variation_id) {
            this.variation_id = variation_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSelling_price() {
            return selling_price;
        }

        public void setSelling_price(String selling_price) {
            this.selling_price = selling_price;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        private String price;
        private String selling_price;
        private String qty;
    }
}
