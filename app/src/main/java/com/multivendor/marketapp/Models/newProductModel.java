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
        public String product_category;

        public String getProduct_category() {
            return product_category;
        }

        public void setProduct_category(String product_category) {
            this.product_category = product_category;
        }

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

    public class reviewResult {
        public String user_image;
        public String user_name;
        public String rating;
        public String review;

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
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

    public class productdetailResp {
        public String success;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public productdetailResult getResult() {
            return result;
        }

        public void setResult(productdetailResult result) {
            this.result = result;
        }

        public productdetailResult result;
    }

    public class productdetailResult {
        public String product_id;
        public List<productImage> product_images;
        public String product_name;
        public String product_description;
        public String product_price;
        public String product_cut_price;
        public String discount_rate;
        public String product_category;
        public List<sizeandquat> product_variants;
        public List<reviewResult> product_reviews;
        public List<productCartresp> in_cart;

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public List<productImage> getProduct_images() {
            return product_images;
        }

        public void setProduct_images(List<productImage> product_images) {
            this.product_images = product_images;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
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

        public String getProduct_category() {
            return product_category;
        }

        public void setProduct_category(String product_category) {
            this.product_category = product_category;
        }

        public List<sizeandquat> getProduct_variants() {
            return product_variants;
        }

        public void setProduct_variants(List<sizeandquat> product_variants) {
            this.product_variants = product_variants;
        }

        public List<reviewResult> getProduct_reviews() {
            return product_reviews;
        }

        public void setProduct_reviews(List<reviewResult> product_reviews) {
            this.product_reviews = product_reviews;
        }

        public List<productCartresp> getIn_cart() {
            return in_cart;
        }

        public void setIn_cart(List<productCartresp> in_cart) {
            this.in_cart = in_cart;
        }
    }

    public class productImage {
        public String image;
        public String id;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public  class productCartresp {

        public String variant_id;
        public String variant_name;
        public String quantity;

        public String getVariant_id() {
            return variant_id;
        }

        public void setVariant_id(String variant_id) {
            this.variant_id = variant_id;
        }

        public String getVariant_name() {
            return variant_name;
        }

        public void setVariant_name(String variant_name) {
            this.variant_name = variant_name;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }
    }
}
