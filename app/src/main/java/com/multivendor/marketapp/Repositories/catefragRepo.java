package com.multivendor.marketapp.Repositories;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.ApiWork.ApiWork;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Constants.api_baseurl;
import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class catefragRepo {

    private catefragRepo instance;
    private List<categoriesModel> catlist = new ArrayList<>();
    private MutableLiveData<List<categoriesModel>> catdata = new MutableLiveData<>();
    private MutableLiveData<newProductModel.homeprodResult> shopdata = new MutableLiveData<>();

    private String selcategoryname;
    private Integer selcategorypos;

    public catefragRepo getInstance() {
        if (instance == null) {
            instance = new catefragRepo();
        }
        return instance;
    }

    public MutableLiveData<List<categoriesModel>> returncatdata() {
        //getcatdatafromSource();
        if (catlist == null) {
            catdata.setValue(null);
        }
        catdata.setValue(catlist);
        return catdata;
    }

    public MutableLiveData<newProductModel.homeprodResult> returnshopdata(String selcategoryname,String lat,String longit) {
        getnbyshopsdatafromSource(selcategoryname,lat,longit);
        return shopdata;
    }

    private void getnbyshopsdatafromSource(String selcategoryname,String lat,String longit) {
        api_baseurl baseurl= new api_baseurl();
        Log.d("latandlong",lat+","+longit);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<newProductModel.homeprodResp> call = apiWork.getcategoryproducts(selcategoryname,lat,longit);

        call.enqueue(new Callback<newProductModel.homeprodResp>() {
            @Override
            public void onResponse(Call<newProductModel.homeprodResp> call, Response<newProductModel.homeprodResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                newProductModel.homeprodResp storedata = response.body();
                Log.d("message", storedata.getSuccess());
                if(storedata.getResult()!=null) {
                    shopdata.setValue(storedata.getResult());
                }

            }

            @Override
            public void onFailure(Call<newProductModel.homeprodResp> call, Throwable t) {
                Log.d("errorshops", t.getMessage().toString());
                shopdata.setValue(null);
            }
        });
    }

//    private void getcatdatafromSource() {
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/2716/2716362.png","Grocery"));
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/3058/3058995.png","Vegetable"));
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/3187/3187880.png","Food"));
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/4625/4625809.png","Pharmacy"));
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/1642/1642802.png","Clothing"));
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/680/680246.png","Electronics"));
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/2598/2598741.png","Furniture"));
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/3501/3501241.png","Beauty"));
////        catlist.add(new categoriesModel("https://cdn-icons-png.flaticon.com/512/1021/1021743.png","Footwear"));
//
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.grocery),"Grocery"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.medicine),"Pharmacy"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.food),"Food"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.cloth),"Clothing"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.furniture),"Furniture"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.toys),"Toys & Gifts"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.cosmetics),"Cosmetics"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.shoe),"Footwear"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.necklace),"Fashion Accessories"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.homeappliances),"Home Appliances"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.gadgets),"Mobiles & Laptops"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.ayurvedic),"Ayurvedic"));
//        catlist.add(new categoriesModel(getURLForResource(R.drawable.vegetables),"Vegetable"));
//
//    }

    public String getURLForResource(int resourceID){

        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/"+resourceID).toString();
    }
    }


