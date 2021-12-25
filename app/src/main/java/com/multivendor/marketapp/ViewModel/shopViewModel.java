package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.Repositories.shopRepo;

import java.util.List;

public class shopViewModel extends ViewModel {

    private MutableLiveData<List<userAPIResp.sellerResult>> nbyshopmodel;
    private com.multivendor.marketapp.Repositories.shopRepo mrepo=new shopRepo();

    public void initwork(String userid,String lat,String longit){
        if(nbyshopmodel!=null) {
            return;
        }

        nbyshopmodel=mrepo.returnnybyshopdata(userid,lat,longit);
    }
    public MutableLiveData<List<userAPIResp.sellerResult>> getNbyshopmodel() {
        return nbyshopmodel;
    }
}
