package com.multivendor.marketapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.userAPIResp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class shopRepo {

    private shopRepo instance;
    private List<userAPIResp.sellerResult> nyshoplist = new ArrayList<>();
    private MutableLiveData< List<userAPIResp.sellerResult>> nyshopdata=new MutableLiveData<>();


    public shopRepo getInstance() {
        if(instance==null) {
            instance=new shopRepo();
        }
        return instance;
    }

    public MutableLiveData<List<userAPIResp.sellerResult>> returnnybyshopdata(String userid,String lat,String longit) {

        getnbyshopsdatafromSource(userid,lat,longit);
        if(nyshoplist==null) {
            nyshopdata.setValue(null);
        }
        nyshopdata.setValue(nyshoplist);
        return nyshopdata;
    }



    private void getnbyshopsdatafromSource(String userid,String lat,String longit) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface=retrofit.create(LogregApiInterface.class);

        Call<userAPIResp.sellerinfo> call=logregApiInterface.get_shopinfo(userid,lat,longit);
        call.enqueue(new Callback<userAPIResp.sellerinfo>() {
            @Override
            public void onResponse(Call<userAPIResp.sellerinfo> call, Response<userAPIResp.sellerinfo> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                userAPIResp.sellerinfo storedata =response.body();
                Log.d("message",storedata.getMessage());
                nyshoplist.add(storedata.getResult());
                nyshopdata.setValue(nyshoplist);
            }

            @Override
            public void onFailure(Call<userAPIResp.sellerinfo> call, Throwable t) {
                Log.d("errorshops",t.getMessage().toString());
            }
        });

    }

}
