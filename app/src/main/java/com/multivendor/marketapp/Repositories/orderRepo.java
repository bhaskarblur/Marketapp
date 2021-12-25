package com.multivendor.marketapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.ordersModel;
import com.multivendor.marketapp.Models.quickorderModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class orderRepo {
    private orderRepo instance;
    private List<quickorderModel.quickordResult> quickordList = new ArrayList<>();
    private MutableLiveData<List<quickorderModel.quickordResult>> quickordModel = new MutableLiveData<>();
    private List<cartModel.singlecartResult> orderlist=new ArrayList<>();
    private MutableLiveData<List<cartModel.singlecartResult>> orderData=new MutableLiveData<>();
;
    public orderRepo getInstance() {
        if(instance==null) {
            instance=new orderRepo();
        }
        return instance;
    }
      public MutableLiveData<List<cartModel.singlecartResult>> returnorderModel(String userid) {
        getorderdatafromSource(userid);
        if(orderlist==null) {
            orderData.setValue(null);
        }
        orderData.setValue(orderlist);
        return orderData;
      }


    private void getorderdatafromSource(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.multcartResp> call=logregApiInterface.getorders(userid);

        call.enqueue(new Callback<cartModel.multcartResp>() {
            @Override
            public void onResponse(Call<cartModel.multcartResp> call, Response<cartModel.multcartResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode:",String.valueOf(response.code()));
                    return;
                }

                cartModel.multcartResp resp=response.body();
                if(resp.getResult()!=null) {

                    for(cartModel.singlecartResult data:resp.getResult()) {
                        orderlist.add(data);
                    }
                    orderData.setValue(orderlist);
                }
            }

            @Override
            public void onFailure(Call<cartModel.multcartResp> call, Throwable t) {
                Log.d("failure:",t.getMessage());
            }
        });

    }



    public MutableLiveData<List<quickorderModel.quickordResult>> returnquickModel(String storeid) {
        getquickordFromQuick(storeid);
        if(quickordList!=null) {
            quickordModel.setValue(null);
        }
        quickordModel.setValue(quickordList);
        return quickordModel;
    }

    private void getquickordFromQuick(String storeid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<quickorderModel.quickordResp> call = logregApiInterface.get_quickorders(storeid);

        call.enqueue(new Callback<quickorderModel.quickordResp>() {
            @Override
            public void onResponse(Call<quickorderModel.quickordResp> call, Response<quickorderModel.quickordResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode",String.valueOf(response.code()));
                    return;
                }

                quickorderModel.quickordResp resp=response.body();
                if(resp.getResult()!=null) {
                    for(quickorderModel.quickordResult quickdata:resp.getResult()) {
                        quickordList.add(quickdata);
                    }
                    quickordModel.setValue(quickordList);
                }
            }

            @Override
            public void onFailure(Call<quickorderModel.quickordResp> call, Throwable throwable) {
                Log.d("failurequick:", throwable.getMessage());
            }
        });

    }


}
