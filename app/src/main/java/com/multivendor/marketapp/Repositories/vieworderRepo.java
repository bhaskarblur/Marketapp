package com.multivendor.marketapp.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.orderprodModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.Models.userAPIResp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class vieworderRepo {

    private vieworderRepo instance;
    private List<orderprodModel> ordprodList=new ArrayList<>();
    private MutableLiveData<List<orderprodModel>> ordprodModel=new MutableLiveData<>();
    public MutableLiveData<cartModel.singlecartResult> getordinfo=new MutableLiveData<>();
    private List<productitemModel> allproductlist=new ArrayList<>();
    private MutableLiveData<List<productitemModel>> allproductModel=new MutableLiveData<>();
    public vieworderRepo getInstance() {
        if(instance==null) {
            instance=new vieworderRepo();
        }
        return instance;
    }

    public MutableLiveData<List<orderprodModel>> returnordprodModel() {
        if(ordprodList==null) {
            ordprodModel.setValue(null);
        }
        ordprodModel.setValue(ordprodList);
        return ordprodModel;
    }

    public MutableLiveData<cartModel.singlecartResult> returnorderdata(String storeid,String orderid) {
        getOrdInfo(storeid,orderid);
        return getordinfo;
    }

    private void getOrdInfo(String storeid,String orderid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://demowebsites.co.in/marketapp/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.singlecartResp> call=logregApiInterface.getsingleorder(storeid,orderid);

        call.enqueue(new Callback<cartModel.singlecartResp>() {
            @Override
            public void onResponse(Call<cartModel.singlecartResp> call, Response<cartModel.singlecartResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errrcode",String.valueOf(response.code()));
                    return;
                }

                cartModel.singlecartResp resp=response.body();
                Log.d("msg",resp.getMessage());
                if(resp.getResult()!=null)  {
                    getordinfo.setValue(resp.getResult());
                }
            }

            @Override
            public void onFailure(Call<cartModel.singlecartResp> call, Throwable t) {
                Log.d("failure",t.getMessage());
            }
        });
    }



    public MutableLiveData<List<productitemModel>> returnallproductitem(String userid) {
        getallproductdatafromSource(userid);
        if(allproductlist==null) {
            allproductModel.setValue(null);
        }
        allproductModel.setValue(allproductlist);
        return allproductModel;
    }
        
    private void getallproductdatafromSource(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface=retrofit.create(LogregApiInterface.class);

        Call<userAPIResp .getproduct_info> call=logregApiInterface.get_historyproducts(userid);

        call.enqueue(new Callback<userAPIResp .getproduct_info>() {
            @Override
            public void onResponse(Call<userAPIResp .getproduct_info> call, Response<userAPIResp .getproduct_info> response) {
                if(!response.isSuccessful()) {
                    Log.d("error code:",String.valueOf(response.code()));
                    return;
                }

                userAPIResp .getproduct_info data=response.body();
                if(data!=null) {
                    if(data.getMessage()!=null) {
                        Log.d("messagehi",data.getMessage());
                    }

                    if(data.getResult()!=null) {
                        for(productitemModel itrdata:data.getResult().getProducts()) {
                            Log.d("data",itrdata.getItemname());
                            allproductlist.add(itrdata);
                        }
                        allproductModel.setValue(allproductlist);
                    }
                }

            }

            @Override
            public void onFailure(Call<userAPIResp .getproduct_info> call, Throwable t) {
                Log.d("Failure",t.getMessage());
            }
        });
    }
}
