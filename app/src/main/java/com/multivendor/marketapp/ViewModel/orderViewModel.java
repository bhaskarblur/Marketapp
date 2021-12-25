package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.quickorderModel;
import com.multivendor.marketapp.Repositories.orderRepo;

import java.util.List;

public class orderViewModel extends ViewModel {

    private MutableLiveData<List<cartModel.singlecartResult>> orderModel;
    private com.multivendor.marketapp.Repositories.orderRepo mrepo=new orderRepo();
    private MutableLiveData<List<quickorderModel.quickordResult>> quickordModel;

    public void initwork(String userid) {
        if(orderModel!=null) {
            return;
        }

        orderModel=mrepo.getInstance().returnorderModel(userid);
        quickordModel=mrepo.getInstance().returnquickModel(userid);
    }

    public MutableLiveData<List<cartModel.singlecartResult>> getOrderModel() {
        return orderModel;
    }

    public MutableLiveData<List<quickorderModel.quickordResult>> getQuickordModel() {
        return quickordModel;
    }
}
