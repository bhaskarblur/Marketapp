package com.multivendor.marketapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.Models.userAPIResp;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class catalRepo {
    private catalRepo instance;
    private List<categoriesModel> catlist= new ArrayList<>();
    private MutableLiveData<List<categoriesModel>> catdata=new MutableLiveData<>();
    private List<productitemModel> itemlist= new ArrayList<>();
    private MutableLiveData<List<productitemModel>> itemdata=new MutableLiveData<>();
    public catalRepo getInstance() {
        if(instance==null){
            instance=new catalRepo();
        }
        return instance;
    }

    public MutableLiveData<List<categoriesModel>> returncatdata(String user_id) {
        getcatdatafromSource(user_id,"","");
        if(catlist==null) {
            catdata.setValue(null);
        }
        catdata.setValue(catlist);
        return catdata;
    }

    public MutableLiveData<List<productitemModel>> returnitemdata(String user_id) {
        getitemdatafromSource(user_id);
        if(itemlist==null) {
            itemdata.setValue(null);
        }
        itemdata.setValue(itemlist);
        return itemdata;
    }

    private void getitemdatafromSource(String user_id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface=retrofit.create(LogregApiInterface.class);

        Call<userAPIResp.getproduct_info> call=logregApiInterface.get_products(user_id);

        call.enqueue(new Callback<userAPIResp.getproduct_info>() {
            @Override
            public void onResponse(Call<userAPIResp.getproduct_info> call, Response<userAPIResp.getproduct_info> response) {
                if(!response.isSuccessful()) {
                    Log.d("error code:",String.valueOf(response.code()));
                    return;
                }

                userAPIResp.getproduct_info data=response.body();
                if(data!=null) {
                    if(data.getMessage()!=null) {
                        Log.d("messagehi",data.getMessage());
                    }

                    if(data.getMessage().equals("Get All Products")) {
                        for(productitemModel itrdata:data.getResult().getProducts()) {
                            itemlist.add(itrdata);
                        }
                        itemdata.setValue(itemlist);
                    }
                }

            }

            @Override
            public void onFailure(Call<userAPIResp.getproduct_info> call, Throwable t) {

                Log.d("Failure",t.getMessage());
            }
        });
    }

    private void getcatdatafromSource(String user_id,String lat,String longit) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface=retrofit.create(LogregApiInterface.class);

        Call<userAPIResp.sellerinfo> call=logregApiInterface.get_shopinfo(user_id,"","");
        call.enqueue(new Callback<userAPIResp.sellerinfo>() {
            @Override
            public void onResponse(Call<userAPIResp.sellerinfo> call, Response<userAPIResp.sellerinfo> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                userAPIResp.sellerinfo storedata =response.body();
                Log.d("message",storedata.getMessage());
                if(storedata.getResult()!=null) {
                    for (int i = 0; i < storedata.getResult().getCategories().size(); i++) {
                        catlist.add(storedata.getResult().getCategories().get(i));
                    }
                    catdata.setValue(catlist);
                }
            }

            @Override
            public void onFailure(Call<userAPIResp.sellerinfo> call, Throwable t) {
                Log.d("errorshops",t.getMessage().toString());
            }
        });

    }

}
