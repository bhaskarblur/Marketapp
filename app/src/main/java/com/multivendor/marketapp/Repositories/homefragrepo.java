package com.multivendor.marketapp.Repositories;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.bannermodel;

import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homefragrepo {


    private homefragrepo instance;
    private List<categoriesModel> catlist = new ArrayList<>();
    private MutableLiveData< List<categoriesModel>> catdata=new MutableLiveData<>();
    private List<nbyshopsModel> nyshoplist = new ArrayList<>();
    private MutableLiveData< List<nbyshopsModel>> nyshopdata=new MutableLiveData<>();

    private List<bannermodel> bannerlist = new ArrayList<>();
    private MutableLiveData< List<bannermodel>> bannerdata=new MutableLiveData<>();
    private Integer pos;
    public homefragrepo getInstance() {
        if(instance==null) {
            instance= new homefragrepo();
        }
        pos=0;
        return instance;
    }


    public MutableLiveData< List<bannermodel>> returnbannerdata() {
        getbannerdatafromSource();
        if(bannerlist==null) {
            bannerdata.setValue(null);
        }
        bannerdata.setValue(bannerlist);
        return bannerdata;
    }

    private void getbannerdatafromSource() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<userAPIResp.getbanners> call=logregApiInterface.getbanners();

        call.enqueue(new Callback<userAPIResp.getbanners>() {
            @Override
            public void onResponse(Call<userAPIResp.getbanners> call, Response<userAPIResp.getbanners> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                userAPIResp.getbanners data=response.body();
                Log.d("msg",data.getMessage());

                if(data.getMessage().equals("All Banners.!")) {
                    for(com.multivendor.marketapp.Models.bannermodel bmodel:data.getResult()) {
                        bannerlist.add(bmodel);
                    }
                    bannerdata.setValue(bannerlist);
                }


            }

            @Override
            public void onFailure(Call<userAPIResp.getbanners> call, Throwable t) {

            }
        });


}

    public MutableLiveData< List<categoriesModel>> returncatdata() {
        getcatdatafromSource();
        if(catlist==null) {
            catdata.setValue(null);
        }
        catdata.setValue(catlist);
        return catdata;
    }

    public MutableLiveData<List<nbyshopsModel>> returnnybyshopdata(String lat,String longit) {
        getnbyshopsdatafromSource(lat,longit);
        if(nyshoplist==null) {
            nyshopdata.setValue(null);
        }
        nyshopdata.setValue(nyshoplist);
        return nyshopdata;
    }

    private void getnbyshopsdatafromSource(String lat,String longit) {
        Log.d("latandlong",lat+","+longit);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<userAPIResp.getStores> call=logregApiInterface.getstores(lat,longit);

        call.enqueue(new Callback<userAPIResp.getStores>() {
            @Override
            public void onResponse(Call<userAPIResp.getStores> call, Response<userAPIResp.getStores> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                userAPIResp.getStores storedata = response.body();


                Log.d("message",storedata.getMessage());

                if(storedata.getResult()!=null) {
                    if (storedata.getResult().size() > 5) {

                        for (int i = 0; i < storedata.getResult().size(); i++) {

                            nyshoplist.add(storedata.getResult().get(i));
                        }
                    }

                    else {
                        for (int x = 0; x < storedata.getResult().size(); x++) {

                            nyshoplist.add(storedata.getResult().get(x));
                        }
                        Log.d("here", nyshoplist.get(0).getShopname());
                    }

                    nyshopdata.setValue(nyshoplist);
                }



            }

            @Override
            public void onFailure(Call<userAPIResp.getStores> call, Throwable t) {
                Log.d("errorshops",t.getMessage().toString());
            }
        });

    }

    private void getcatdatafromSource() {

        catlist.add(new categoriesModel(getURLForResource(R.drawable.grocery),"Grocery"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.medicine),"Pharmacy"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.food),"Food"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.cloth),"Clothing"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.furniture),"Furniture"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.toys),"Toys & Gifts"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.cosmetics),"Cosmetics"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.shoe),"Footwear"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.necklace),"Fashion Accessories"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.homeappliances),"Home Appliances"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.gadgets),"Mobiles & Laptops"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.ayurvedic),"Ayurvedic"));
        catlist.add(new categoriesModel(getURLForResource(R.drawable.vegetables),"Vegetable"));

    }

    public String getURLForResource(int resourceID){

       return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/"+resourceID).toString();
    }
}

