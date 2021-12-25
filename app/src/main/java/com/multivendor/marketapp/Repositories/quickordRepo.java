package com.multivendor.marketapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.ordprodModel;
import com.multivendor.marketapp.Models.quickorderModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class quickordRepo {

    private quickordRepo instance;
    private List<quickorderModel.quick_products> prodList = new ArrayList<>();
    private List<String> prodimgList = new ArrayList<>();
    private MutableLiveData<List<String>> prodimgModel = new MutableLiveData<>();
    private MutableLiveData<quickorderModel.quickordResult> wholedata=new MutableLiveData<>();
    private MutableLiveData<List<quickorderModel.quick_products>> prodModel = new MutableLiveData<>();


    public quickordRepo getInstance() {
        if (instance == null) {
            instance = new quickordRepo();

        }
        return instance;
    }


    public MutableLiveData<quickorderModel.quickordResult> returnwholedata() {
        return wholedata;
    }
    public MutableLiveData<List<quickorderModel.quick_products>> returnprodModel(String storeid, String quickordid) {
        getprodDataFromSource(storeid, quickordid);
        if (prodList == null) {
            prodModel.setValue(null);
        }
        prodModel.setValue(prodList);
        return prodModel;
    }

    public MutableLiveData<List<String>> returnprodimg() {
        if (prodimgList == null) {
            prodimgModel.setValue(null);
        }
        prodimgModel.setValue(prodimgList);
        return prodimgModel;
    }


    private void getprodDataFromSource(String storeid, String quickordid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<quickorderModel.singlequickordResp> call = logregApiInterface.get_singleQuick(storeid, quickordid);

        call.enqueue(new Callback<quickorderModel.singlequickordResp>() {
            @Override
            public void onResponse(Call<quickorderModel.singlequickordResp> call, Response<quickorderModel.singlequickordResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("errorcode", String.valueOf(response.code()));
                    return;
                }

                quickorderModel.singlequickordResp resp = response.body();
                if (resp.getResult() != null) {
                    wholedata.setValue(resp.getResult());
                    for(quickorderModel.quick_products products:resp.getResult().getProducts()) {
                        Log.d("hehe","hehe");
                        prodList.add(products);
                    }
                    for(int i=0;i<resp.getResult().getImages().size();i++) {
                        Log.d("hehe1","hehe1");
                        prodimgList.add(resp.getResult().getImages().get(i).getImage());
                    }
                    prodimgModel.setValue(prodimgList);
                    prodModel.setValue(prodList);
                }
            }

            @Override
            public void onFailure(Call<quickorderModel.singlequickordResp> call, Throwable throwable) {
                Log.d("failure:", throwable.getMessage());
            }
        });

    }
}
