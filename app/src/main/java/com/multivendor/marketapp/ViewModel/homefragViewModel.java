package com.multivendor.marketapp.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.Repositories.homefragrepo;
import com.multivendor.marketapp.Models.bannermodel;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class homefragViewModel extends ViewModel {
    private MutableLiveData<List<categoriesModel>> catmodel;
    private MutableLiveData<bannermodel.banneresult> bannerModel;

    private MutableLiveData<newProductModel.homeprodResult> nbyshopmodel;
    private com.multivendor.marketapp.Repositories.homefragrepo mrepo= new homefragrepo();


    public void getlocation(String userid,String lat,String longit,String cityname) {
        if(nbyshopmodel!=null) {
            return;
        }
        nbyshopmodel=mrepo.getInstance().returnnybyshopdata(userid,lat,longit,cityname);
    }
    public void initwork() {
        if(catmodel!=null) {
            return;
        }
        if(nbyshopmodel!=null) {
            return;
        }
        catmodel=mrepo.getInstance().returncatdata();
        bannerModel=mrepo.getInstance().returnbannerdata();

    }
    public LiveData<newProductModel.homeprodResult> getnbyshopModel() {
        return nbyshopmodel;
    }

    public MutableLiveData<bannermodel.banneresult> getBannerModel() {
        return bannerModel;
    }

    public LiveData<List<categoriesModel>> getcatmodel() {
        return catmodel;
    }

}
