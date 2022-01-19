package com.multivendor.marketapp.Repositories;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.ApiWork.ApiWork;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Constants.api_baseurl;
import com.multivendor.marketapp.Models.bannermodel;

import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homefragrepo {


    private homefragrepo instance;
    private List<categoriesModel> catlist = new ArrayList<>();
    private MutableLiveData< List<categoriesModel>> catdata=new MutableLiveData<>();
    private MutableLiveData<newProductModel.homeprodResult> nyshopdata=new MutableLiveData<>();
    private MutableLiveData<bannermodel.banneresult> bannerdata=new MutableLiveData<>();
    private Integer pos;
    api_baseurl baseurl=new api_baseurl();
    public homefragrepo getInstance() {
        if(instance==null) {
            instance= new homefragrepo();
        }
        pos=0;
        return instance;
    }


    public MutableLiveData< bannermodel.banneresult> returnbannerdata() {
        getbannerdatafromSource();
        return bannerdata;
    }

    private void getbannerdatafromSource() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<bannermodel.bannerresp> call=apiWork.getbanners();

        call.enqueue(new Callback<bannermodel.bannerresp>() {
            @Override
            public void onResponse(Call<bannermodel.bannerresp> call, Response<bannermodel.bannerresp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                bannermodel.bannerresp data=response.body();
                Log.d("msg",data.getSuccess());

                if(data.getResult()!=null) {

                    bannerdata.setValue(data.getResult());
                }


            }

            @Override
            public void onFailure(Call<bannermodel.bannerresp> call, Throwable t) {

            }
        });


}

    public MutableLiveData< List<categoriesModel>> returncatdata() {
        if(catlist==null) {
            catdata.setValue(null);
        }
        catdata.setValue(catlist);
        return catdata;
    }

    public MutableLiveData<newProductModel.homeprodResult> returnnybyshopdata(String userid,String lat,String longit,String cityname) {
        getnbyshopsdatafromSource(userid,lat,longit,cityname);
        return nyshopdata;
    }

    private void getnbyshopsdatafromSource(String userid,String lat,String longit,String cityname) {
        Log.d("latandlong",lat+","+longit);
        Log.d("city_name",cityname);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork= retrofit.create(ApiWork.class);

        Call<newProductModel.homeprodResp> call=apiWork.getallproducts(userid,lat,longit,cityname);

        call.enqueue(new Callback<newProductModel.homeprodResp>() {
            @Override
            public void onResponse(Call<newProductModel.homeprodResp> call, Response<newProductModel.homeprodResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code1", String.valueOf(response.code()));
                    return;
                }

                newProductModel.homeprodResp storedata = response.body();


                Log.d("message12",storedata.getSuccess());

                if(storedata.getResult().getBest_deal_products()!=null) {

                    nyshopdata.setValue(storedata.getResult());
                    Log.d("sample",
                            storedata.getResult().getAll_categories().get(0).getName());
                }

            }

            @Override
            public void onFailure(Call<newProductModel.homeprodResp> call, Throwable t) {
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

