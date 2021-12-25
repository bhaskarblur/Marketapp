package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Repositories.catefragRepo;

import java.util.List;

public class categfragViewModel extends ViewModel {
    private MutableLiveData<List<categoriesModel>> catModel;
    private com.multivendor.marketapp.Repositories.catefragRepo mrepo = new catefragRepo();
    private MutableLiveData<List<com.multivendor.marketapp.Models.nbyshopsModel>> nbyshopmodel;
    String selcat;
    String selcatname;
    String lat;
    String longit;

    public void getlocation(String lat, String longit) {
        if(nbyshopmodel!=null) {
            return;
        }
        this.lat=lat;
        this.longit=longit;
        nbyshopmodel = mrepo.getInstance().returnshopdata(selcat, lat, longit);
    }

    public void initwork(String selcat) {
        this.selcat = selcat;
        if (catModel != null) {
            return;
        }

        catModel = mrepo.getInstance().returncatdata();

    }

    public LiveData<List<categoriesModel>> getcatModel() {
        return catModel;
    }

    public LiveData<List<com.multivendor.marketapp.Models.nbyshopsModel>> getnbyshopmodel() {
        return nbyshopmodel;
    }

    public void changeData(String catposselcat, String selcatname) {
        this.selcat = catposselcat;
        this.selcatname = selcatname;
        nbyshopmodel.setValue(mrepo.returnshopdata(selcatname, lat, longit).getValue());

    }
}
