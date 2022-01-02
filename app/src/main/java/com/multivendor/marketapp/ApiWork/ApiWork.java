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
    @POST("user_login")
    Call<AuthResponse.SendOtp> sendotp(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("user_otp")
    Call<AuthResponse.VerifyOtp> login(@Field("mobile") String mobile,
    @Field("otp") String otp,@Field("device_token") String token);

    @GET("banner")
    Call<bannermodel.bannerresp> getbanners();

    @FormUrlEncoded
    @POST("all_products")
    Call<newProductModel .homeprodResp> getallproducts(@Field("user_id") String userid,@Field("latitude") String lat,
                                                       @Field("longitude") String longit,@Field("city")
                                                       String cityname);
    @FormUrlEncoded
    @POST("category_products")
    Call<newProductModel .homeprodResp> getcategoryproducts(@Field("category_name") String catname,@Field("latitude") String lat,
                                                       @Field("longitude") String longit,@Field("city")
                                                                    String cityname);

    @FormUrlEncoded
    @POST("product_detail")
    Call<newProductModel.productdetailResp> getproduct_details(@Field("user_id") String userid,
                                                               @Field("product_id") String productid);


    @FormUrlEncoded
    @POST("add_to_favourite")
    Call<AuthResponse.VerifyOtp> addfavourite(@Field("user_id") String userid,
                                              @Field("product_id") String productid);

    @FormUrlEncoded
    @POST("remove_favourite")
    Call<AuthResponse.VerifyOtp> removefavourite(@Field("user_id") String userid,
                                              @Field("product_id") String productid);

    @FormUrlEncoded
    @POST("apply_coupon")
    Call<newProductModel.couponResp> applycoupon(@Field("user_id") String userid,
                                                 @Field("coupon_code") String coupon,
                                                 @Field("cart_id") String cartid);

}

