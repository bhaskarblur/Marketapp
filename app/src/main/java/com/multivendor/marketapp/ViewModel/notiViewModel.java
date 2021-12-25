package com.multivendor.marketapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketapp.Models.notiModel;
import com.multivendor.marketapp.Repositories.notiRepo;

import java.util.List;

public class notiViewModel extends ViewModel {

    private MutableLiveData<List<notiModel .notiResult>> notiData;
    private notiRepo mRepo=new notiRepo();

    public void initwork(String userid) {
        if(notiData!=null) {
            return;
        }

        notiData=mRepo.returnnotidata(userid);
    }

    public LiveData<List<notiModel.notiResult>> getNotiData() {
        return notiData;
    }
}
