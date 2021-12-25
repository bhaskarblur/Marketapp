package com.multivendor.marketapp.ApiWork;

import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.Models.notiModel;
import com.multivendor.marketapp.Models.quickorderModel;
import com.multivendor.marketapp.Models.userAPIResp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LogregApiInterface {


    @FormUrlEncoded
    @POST("send-otp")
    Call<loginresResponse.sendotp> sendOTP(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("validate-otp")
    Call<loginresResponse.register> signin(@Field("name") String name,
                         @Field("phone") String phone, @Field("otp") String OTP
                         ,@Field("password") String password, @Field("user_type") String usetype);

    @FormUrlEncoded
    @POST("login")
    Call<loginresResponse.login> login(@Field("phone") String phone,@Field("password") String password,
                                       @Field("user_type") String user_type,@Field("device_token") String device_token);


    @FormUrlEncoded
    @POST("forgot-password-sendotp")
     Call<loginresResponse.forgotpass> doforgotpass(@Field("phone")String phone);

    @FormUrlEncoded
    @POST("save-forgot-password")
    Call<loginresResponse.verifyforgpass> verifyforgpass( @Field("phone") String phone, @Field("otp") String OTP
            ,@Field("new_password") String password);


    @GET("banner")
    Call<userAPIResp.getbanners> getbanners();

    @FormUrlEncoded
    @POST("get-user-profile")
    Call<loginresResponse.login> getprofile(@Field("user_id")String userid);

    @FormUrlEncoded
    @POST("user-change-password")
    Call<loginresResponse.verifyforgpass> changepass(@Field("user_id")String userid,@Field("password")String password);


    @FormUrlEncoded
    @POST("user-update-profile")
    Call<loginresResponse.login> updateprofile(@Field("user_id")String userid, @Field("name")String name,
                                                     @Field("address")String address,@Field("image")String image);


    @FormUrlEncoded
    @POST("user-update-profile")
    Call<loginresResponse.login> noimgprofileupdate(@Field("user_id")String userid, @Field("name")String name,
                                               @Field("address")String address);
    @FormUrlEncoded
    @POST("get-all-store")
    Call<userAPIResp.getStores> getstores(@Field("lat") String lat,@Field("long")String longit);

    @FormUrlEncoded
    @POST("my-store")
    Call<userAPIResp.sellerinfo> get_shopinfo(@Field("user_id")String userid,@Field("lat")String lat,
                                              @Field("long") String longit);

    @FormUrlEncoded
    @POST("user-side-get-products")
    Call<userAPIResp.getproduct_info> get_products(@Field("user_id")String userid);

    @FormUrlEncoded
    @POST("my-products")
    Call<userAPIResp.getproduct_info> get_historyproducts(@Field("user_id")String userid);

    @FormUrlEncoded
    @POST("add-wishlist")
    Call<loginresResponse.verifyforgpass> add_favshop(@Field("user_id")String userid,
                                                      @Field("store_id")String storeid);

    @FormUrlEncoded
    @POST("get-wishlist")
    Call<userAPIResp.getStores> get_favshop(@Field("user_id")String userid);

    @FormUrlEncoded
    @POST("add-order")
    Call<cartModel.cartResp> add_cart(@Field("lat") String lat,@Field("long") String longit,
                                      @Field("user_id")String user_id,@Field("store_id") String store_id,
                                      @Field("product_id")String prod_id,@Field("variant_id") String variant_id,
                                      @Field("sku")String sku,@Field("product_name") String prod_name,
                                      @Field("size") String sizename, @Field("price") String price,
                                      @Field("final_price") String finprice,
                                      @Field("qty_ordered") String qty_ord,@Field("cart_id") String cart_id);

    @FormUrlEncoded
    @POST("update-order")
    Call<cartModel.cartResp> update_cart(@Field("lat") String lat,@Field("long") String longit,
                                         @Field("user_id")String user_id,@Field("store_id") String store_id,
                                         @Field("product_id")String prod_id,@Field("variant_id") String variant_id,
                                         @Field("sku")String sku,@Field("product_name") String prod_name,
                                         @Field("size") String sizename, @Field("price") String price,
                                         @Field("final_price") String finprice,
                                         @Field("qty_ordered") String qty_ord,
                                         @Field("cart_id") String cart_id);

    @FormUrlEncoded
    @POST("delete-order")
    Call<cartModel.cartResp> remove_product(@Field("lat") String lat,@Field("long") String longit,
                                            @Field("user_id")String user_id,@Field("store_id") String store_id,
                                            @Field("product_id")String prod_id,@Field("variant_id") String variant_id,
                                            @Field("sku")String sku,@Field("product_name") String prod_name,
                                            @Field("size") String sizename, @Field("price") String price,
                                            @Field("qty_ordered") String qty_ord,
                                            @Field("final_price") String finprice,
                                            @Field("cart_id") String cart_id);

    @FormUrlEncoded
    @POST("get-cart")
    Call<cartModel.cartResp> get_cart(@Field("user_id") String userid);

    @FormUrlEncoded
    @POST("clear-cart")
    Call<cartModel.cartResp> clear_cart(@Field("user_id") String userid);


    @FormUrlEncoded
    @POST("get-store")
    Call<userAPIResp.sellerinfo> get_paymentstore(@Field("store_id") String storeid,@Field("cart_id")
                                          String cartid);


    @FormUrlEncoded
    @POST("order-confirmation")
    Call<cartModel.cartResp> confirm_order(@Field("user_id") String userid,@Field("store_id")String storeid,
                                           @Field("cart_id")String cartid,@Field("total_price") String amount,
                                           @Field("customer_name") String name, @Field("customer_phone") String phone,
                                           @Field("customer_address") String address, @Field("payment_method") String pay_meth,
                                           @Field("payment_status") String pay_stat,  @Field("transaction_id")
                                                   String transid,@Field("delivery_instruction")String delvinst,
                                           @Field("lat") String lat,@Field("long") String longit);

    @FormUrlEncoded
    @POST("user-get-orders")
    Call<cartModel.multcartResp> getorders(@Field("user_id") String storeid);
    @FormUrlEncoded
    @POST("user-find-single-order")
    Call<cartModel.singlecartResp> getsingleorder(@Field("user_id") String storeid,
                                                  @Field("order_id") String order_id);


    @FormUrlEncoded
    @POST("add-rating")
    Call<loginresResponse.forgotpass> giveratings(@Field("user_id") String userid,
                                                  @Field("store_id") String storeid,
                                                  @Field("rating") String rating,
                                                  @Field("review") String reviews);

    @FormUrlEncoded
    @POST("re-order")
    Call<cartModel.cartResp> re_order(@Field("user_id") String userid,
                                      @Field("cart_id") String cartid);

    @FormUrlEncoded
    @POST("user-get-quick-orders")
    Call<quickorderModel.quickordResp> get_quickorders(@Field("user_id") String storeid);

    @FormUrlEncoded
    @POST("add-quick-order")
    Call<quickorderModel.AddquickordResp>add_quickorders(@Field("store_id") String storeid,
                                                      @Field("user_id") String userid,
                                                      @Field("product_name") String prod_name,
                                                      @Field("qty") String qty,
                                                      @Field("image") String image,
                                                         @Field("customer_name") String name,
                                                         @Field("customer_address") String address,
                                                         @Field("customer_phone") String phone,
                                                         @Field("lat") String lat,@Field("long") String longit);

    @FormUrlEncoded
    @POST("user-get-single-quick-order")
    Call<quickorderModel.singlequickordResp> get_singleQuick(@Field("user_id") String userid,
                                                             @Field("quick_id") String quickid);

    @FormUrlEncoded
    @POST("user-final-update-quick-order")
    Call<quickorderModel.singlequickordResp> user_quickFinal(@Field("user_id") String userid,
                                                             @Field("quick_id") String quickid,
                                                             @Field("user_status") String status);


    @FormUrlEncoded
    @POST("user-notification-list")
    Call<notiModel .notiResp> getAllnotis(@Field("user_id") String userid,@Field("noti_type") String notitype);

    @FormUrlEncoded
    @POST("update-user-notification-list")
    Call<notiModel .notiupdateResp> updatenotis(@Field("user_id") String userid,@Field("noti_id") String notiid,
                                                @Field("noti_type") String notitype);
}
