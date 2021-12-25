package com.multivendor.marketapp.Models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class userAPIResp {

    public class getbanners {
        private String success;

        private List<bannermodel> result;
        private String message;


        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<bannermodel> getResult() {
            return result;
        }

        public void setResult(List<bannermodel> result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public class getStores {

        private String success;
        private List<nbyshopsModel> result;
        private String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<nbyshopsModel> getResult() {
            return result;
        }

        public void setResult(List<nbyshopsModel> result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class sellerinfo {

        private String success;
        private sellerResult result;
        private String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public sellerResult getResult() {
            return result;
        }

        public void setResult(sellerResult result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
    public class sellerResult {
        public int id;
        public String name;
        public String phone;
        public String user_type;
        public String about;
        public String distance;
        public String rating;
        public String getAbout() {
            return about;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public void setAbout(String about) {
            this.about = about;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public void setEmail_verified_at(String email_verified_at) {
            this.email_verified_at = email_verified_at;
        }

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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDelivery_redius() {
            return delivery_redius;
        }

        public void setDelivery_redius(String delivery_redius) {
            this.delivery_redius = delivery_redius;
        }

        public String getMin_order_amount() {
            return min_order_amount;
        }

        public void setMin_order_amount(String min_order_amount) {
            this.min_order_amount = min_order_amount;
        }

        public String getFree_delivery_above() {
            return free_delivery_above;
        }

        public void setFree_delivery_above(String free_delivery_above) {
            this.free_delivery_above = free_delivery_above;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getPan_number() {
            return pan_number;
        }

        public void setPan_number(String pan_number) {
            this.pan_number = pan_number;
        }

        public String getGst_number() {
            return gst_number;
        }

        public void setGst_number(String gst_number) {
            this.gst_number = gst_number;
        }

        public String getUpi_id() {
            return upi_id;
        }

        public void setUpi_id(String upi_id) {
            this.upi_id = upi_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String otp;
        public String store_name;
        public String address;
        public String city;
        public String state;
        public String category;
        public String image;
        public String email;
        public String email_verified_at;
        public String created_at;
        public String updated_at;
        public String user_id;
        public String delivery_redius;
        public String min_order_amount;
        public String free_delivery_above;
        public String delivery_charge;
        public String pan_number;
        public String gst_number;
        public String upi_id;
        public String status;
        @SerializedName("lat")
        public String lat;
        @SerializedName("long")
        public String longit;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLongit() {
            return longit;
        }

        public void setLongit(String longit) {
            this.longit = longit;
        }

        public List<categoriesModel> categories;

        public List<categoriesModel> getCategories() {
            return categories;
        }

        public void setCategories(List<categoriesModel> categories) {
            this.categories = categories;
        }
    }

    public class getproduct_info {
        private String success;
        private listofprods result;
        private String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public listofprods getResult() {
            return result;
        }

        public void setResult(listofprods result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class listofprods {
        public List<productitemModel> products;

        public List<productitemModel> getProducts() {
            return products;
        }

        public void setProducts(List<productitemModel> products) {
            this.products = products;
        }
    }

}
