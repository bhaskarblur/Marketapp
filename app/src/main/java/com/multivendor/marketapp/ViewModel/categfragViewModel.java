package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.Repositories.catefragRepo;

import java.util.List;

public class categfragViewModel extends ViewModel {
    private MutableLiveData<List<categoriesModel>> catModel;
    private com.multivendor.marketapp.Repositories.catefragRepo mrepo = new catefragRepo();
    private MutableLiveData<newProductModel.homeprodResult> nbyshopmodel;


    public void initwork(String selcat,String lat, String longit) {
        if (catModel != null) {
            return;
        }
        if(nbyshopmodel!=null) {
            return;
        }
        catModel = mrepo.getInstance().returncatdata();
        nbyshopmodel = mrepo.getInstance().returnshopdata(selcat, lat, longit);
    }

    public LiveData<List<categoriesModel>> getcatModel() {
        return catModel;
    }

    public LiveData<newProductModel.homeprodResult> getnbyshopmodel() {
        return nbyshopmodel;
    }


}
