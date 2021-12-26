package com.multivendor.marketapp.ApiWork;


import com.multivendor.marketapp.Models.AuthResponse;
import com.multivendor.marketapp.Models.bannermodel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.Models.userAPIResp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiWork {

    @FormUrlEncoded
    @POST("Login")
    Call<AuthResponse.SendOtp> sendotp(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("otp")
    Call<AuthResponse.VerifyOtp> login(@Field("mobile") String mobile,
    @Field("otp") String otp,@Field("device_token") String token);

    @GET("banner")
    Call<bannermodel.bannerresp> getbanners();

    @FormUrlEncoded
    @POST("home_products")
    Call<newProductModel .homeprodResp> getallproducts(@Field("user_id") String userid,@Field("latitude") String lat,
                                                       @Field("longitude") String longit);
    @FormUrlEncoded
    @POST("category_products")
    Call<newProductModel .homeprodResp> getcategoryproducts(@Field("category_name") String catname,@Field("latitude") String lat,
                                                       @Field("longitude") String longit);
}
