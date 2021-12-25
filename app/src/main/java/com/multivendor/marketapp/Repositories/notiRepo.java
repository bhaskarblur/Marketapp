package com.multivendor.marketapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.notiModel;
import com.multivendor.marketapp.Models.userAPIResp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class notiRepo {

    public notiRepo instance;
    private MutableLiveData<List<notiModel.notiResult>> notiData=new MutableLiveData<>();
    private List<notiModel.notiResult> notiList=new ArrayList<>();

    public notiRepo getInstance() {
        if(instance==null) {
            instance=new notiRepo();
        }
        return instance;
    }

    public MutableLiveData<List<notiModel.notiResult>> returnnotidata(String userid) {
        getordernotis(userid);
        getquickordnotis(userid);
        if(notiList==null) {
            notiData.setValue(null);
        }
        notiData.setValue(notiList);
        return notiData;
    }

    private void getordernotis(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<notiModel.notiResp> call=logregApiInterface.getAllnotis(userid,"order");

        call.enqueue(new Callback<notiModel.notiResp>() {
            @Override
            public void onResponse(Call<notiModel.notiResp> call, Response<notiModel.notiResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("Errorcode",String.valueOf(response.code()));
                    return;
                }

                notiModel.notiResp resp=response.body();

                if(resp.getResult()!=null) {
                    for(notiModel.notiResult noti:resp.getResult()) {
                        notiList.add(noti);
                    }
                    notiData.setValue(notiList);
                }
            }

            @Override
            public void onFailure(Call<notiModel.notiResp> call, Throwable throwable) {
                Log.d("Failure",throwable.getMessage());
            }
        });

    }

    private void getquickordnotis(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<notiModel.notiResp> call=logregApiInterface.getAllnotis(userid,"quick_order");

        call.enqueue(new Callback<notiModel.notiResp>() {
            @Override
            public void onResponse(Call<notiModel.notiResp> call, Response<notiModel.notiResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("Errorcode",String.valueOf(response.code()));
                    return;
                }

                notiModel.notiResp resp=response.body();

                if(resp.getResult()!=null) {
                    for(notiModel.notiResult noti:resp.getResult()) {
                        notiList.add(noti);
                    }
                    notiData.setValue(notiList);
                }
            }

            @Override
            public void onFailure(Call<notiModel.notiResp> call, Throwable throwable) {
                Log.d("Failure",throwable.getMessage());
            }
        });
    }
}
