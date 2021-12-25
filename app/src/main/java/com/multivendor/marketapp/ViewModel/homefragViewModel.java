package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Repositories.homefragrepo;
import com.multivendor.marketapp.Models.bannermodel;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class homefragViewModel extends ViewModel {
    private MutableLiveData<List<categoriesModel>> catmodel;
    private MutableLiveData<List<bannermodel>> bannerModel;

    private MutableLiveData<List<com.multivendor.marketapp.Models.nbyshopsModel>> nbyshopmodel;
    private com.multivendor.marketapp.Repositories.homefragrepo mrepo= new homefragrepo();


    public void getlocation(String lat,String longit) {
        if(nbyshopmodel!=null) {
            return;
        }
        nbyshopmodel=mrepo.getInstance().returnnybyshopdata(lat,longit);
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
    public LiveData<List<com.multivendor.marketapp.Models.nbyshopsModel>> getnbyshopModel() {
        return nbyshopmodel;
    }

    public MutableLiveData<List<bannermodel>> getBannerModel() {
        return bannerModel;
    }

    public LiveData<List<categoriesModel>> getcatmodel() {
        return catmodel;
    }

}
