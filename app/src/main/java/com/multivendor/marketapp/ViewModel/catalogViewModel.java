package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.Repositories.catalRepo;
import com.multivendor.marketapp.Repositories.homefragrepo;

import java.util.ArrayList;
import java.util.List;

public class catalogViewModel extends ViewModel {

    private MutableLiveData<List<categoriesModel>> catModel;
    private MutableLiveData<List<productitemModel>> itemModel;
    private MutableLiveData<newProductModel.homeprodResult> nbyshopmodel;
    private catalRepo catalRepo=new catalRepo();
    private com.multivendor.marketapp.Repositories.homefragrepo mrepo= new homefragrepo();
    public void getlocation(String userid,String lat,String longit,String cityname) {
        if(nbyshopmodel!=null) {
            return;
        }
        nbyshopmodel=mrepo.getInstance().returnnybyshopdata(userid,lat,longit,cityname);
    }
    public void initwork(String user_id){
        if(catModel!=null) {
            return;
        }
        if(itemModel!=null) {
            return;
        }
        catModel=catalRepo.getInstance().returncatdata(user_id);
        itemModel=catalRepo.getInstance().returnitemdata(user_id);
    }
    public LiveData<newProductModel.homeprodResult> getnbyshopModel() {
        return nbyshopmodel;
    }
    public LiveData<List<categoriesModel>> getCatModel() {
        return catModel;
    }

    public LiveData<List<productitemModel>> getItemModel() {
        return itemModel;
    }
}
