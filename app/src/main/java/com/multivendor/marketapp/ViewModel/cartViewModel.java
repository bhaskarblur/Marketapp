package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketapp.Models.cartitemsModel;
import com.multivendor.marketapp.Repositories.cartRepo;

import java.util.List;

public class cartViewModel extends ViewModel {

    private MutableLiveData<List<cartitemsModel>> cartitemModel;
    private com.multivendor.marketapp.Repositories.cartRepo mrepo=new cartRepo();

    public void initwork() {
        if(cartitemModel!=null) {
            return;
        }
        cartitemModel=mrepo.getInstance().returncartitemModel();

    }
    public LiveData<List<cartitemsModel>> getcartitemModel() {
        return cartitemModel;
    }
}
