package com.multivendor.marketapp.ApiWork;


import com.multivendor.marketapp.Models.AuthResponse;

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

}
