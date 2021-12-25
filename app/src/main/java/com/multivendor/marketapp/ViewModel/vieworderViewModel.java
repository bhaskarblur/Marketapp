package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.orderprodModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.Repositories.vieworderRepo;

import java.util.List;

public class vieworderViewModel extends ViewModel {

    private MutableLiveData<List<orderprodModel>> ordprodModel;
    public MutableLiveData<cartModel.singlecartResult> getordinfo;
    private MutableLiveData<List<productitemModel>> allproductModel;
    private vieworderRepo mRepo=new vieworderRepo();

    public void initwork(String userid,String storeid,String orderid) {
        if(ordprodModel!=null) {
            return;
        }
        ordprodModel=mRepo.getInstance().returnordprodModel();
        getordinfo=mRepo.getInstance().returnorderdata(userid,orderid);
        allproductModel=mRepo.getInstance().returnallproductitem(storeid);
    }

    public LiveData<List<productitemModel>> getAllproductModel() {
        return allproductModel;
    }

    public LiveData<cartModel.singlecartResult> getGetordinfo() {
        return getordinfo;
    }

    public LiveData<List<orderprodModel>> getOrdprodModel() {
        return ordprodModel;
    }
}
