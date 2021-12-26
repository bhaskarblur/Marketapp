package com.multivendor.marketapp.Models;

import java.util.List;

public class newProductModel {

    public class homeprodResp {
        public String success;
        public homeprodResult result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public homeprodResult getResult() {
            return result;
        }

        public void setResult(homeprodResult result) {
            this.result = result;
        }
    }

    public class homeprodResult {

        public List<ListProductresp> all_products;
        public List<ListProductresp> deal_day_products;
        public List<ListProductresp> top_sell_products;
        public List<ListProductresp> best_deal_products;
        public List<categoriesModel> all_categories;
        public String cart_items;

        public String getCart_items() {
            return cart_items;
        }

        public void setCart_items(String cart_items) {
            this.cart_items = cart_items;
        }

        public List<ListProductresp> getAll_products() {
            return all_products;
        }

        public void setAll_products(List<ListProductresp> all_products) {
            this.all_products = all_products;
        }

        public List<ListProductresp> getDeal_day_products() {
            return deal_day_products;
        }

        public void setDeal_day_products(List<ListProductresp> deal_day_products) {
            this.deal_day_products = deal_day_products;
        }

        public List<ListProductresp> getTop_sell_products() {
            return top_sell_products;
        }

        public void setTop_sell_products(List<ListProductresp> top_sell_products) {
            this.top_sell_products = top_sell_products;
        }

        public List<ListProductresp> getBest_deal_products() {
            return best_deal_products;
        }

        public void setBest_deal_products(List<ListProductresp> best_deal_products) {
            this.best_deal_products = best_deal_products;
        }

        public List<categoriesModel> getAll_categories() {
            return all_categories;
        }

        public void setAll_categories(List<categoriesModel> all_categories) {
            this.all_categories = all_categories;
        }
    }
    public class ListProductresp {
        public String product_id;
        public String product_name;
        public String product_image;
        public String product_description;
        public String product_price;
        public String product_cut_price;
        public String discount_rate;

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_image() {
            return product_image;
        }

        public void setProduct_image(String product_image) {
            this.product_image = product_image;
        }

        public String getProduct_description() {
            return product_description;
        }

        public void setProduct_description(String product_description) {
            this.product_description = product_description;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public String getProduct_cut_price() {
            return product_cut_price;
        }

        public void setProduct_cut_price(String product_cut_price) {
            this.product_cut_price = product_cut_price;
        }

        public String getDiscount_rate() {
            return discount_rate;
        }

        public void setDiscount_rate(String discount_rate) {
            this.discount_rate = discount_rate;
        }
    }
}
