package com.multivendor.marketapp.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketapp.Models.cartitemsModel;
import com.multivendor.marketapp.Models.productitemModel;

import java.util.ArrayList;
import java.util.List;

public class cartRepo {
    private cartRepo instance;
    private List<cartitemsModel> cartitemlist=new ArrayList<>();
    private MutableLiveData<List<cartitemsModel>> cartitemdata=new MutableLiveData<>();

    public cartRepo getInstance() {
        if(instance==null) {
            instance=new cartRepo();
        }
        return instance;
    }

    public MutableLiveData<List<cartitemsModel>> returncartitemModel() {
        getcartitemDatafromSource();
        if(cartitemlist==null) {
            cartitemdata.setValue(null);
        }
        cartitemdata.setValue(cartitemlist);
        return cartitemdata;
    }

    private void getcartitemDatafromSource() {



        cartitemlist.add(new cartitemsModel(80,"Deepu Store","https://www.bigbasket.com/media/uploads/p/xxl/40202281_4-lays-potato-chips-american-style-cream-onion-flavour-best-quality-crunchy.jpg","Grocery ","Lays Chips Yellow",
                20,40,"200gm","600gm",400,4,"randomlocat"));


        cartitemlist.add(new cartitemsModel(80,"Deepu Store","https://www.bigbasket.com/media/uploads/p/xxl/40202281_4-lays-potato-chips-american-style-cream-onion-flavour-best-quality-crunchy.jpg","Grocery ","Lays Chips Yellow",
                20,40,"200gm","600gm",400,4,"randomlocat"));



        cartitemlist.add(new cartitemsModel(80,"Deepu Store","https://www.bigbasket.com/media/uploads/p/xxl/40202281_4-lays-potato-chips-american-style-cream-onion-flavour-best-quality-crunchy.jpg","Grocery ","Lays Chips Yellow",
                20,40,"200gm","600gm",400,4,"randomlocat"));


        cartitemlist.add(new cartitemsModel(80,"Deepu Store","https://www.bigbasket.com/media/uploads/p/xxl/40202281_4-lays-potato-chips-american-style-cream-onion-flavour-best-quality-crunchy.jpg","Grocery ","Lays Chips Yellow",
                20,40,"200gm","600gm",400,4,"randomlocat"));

        cartitemlist.add(new cartitemsModel(80,"Deepu Store","https://www.bigbasket.com/media/uploads/p/xxl/40202281_4-lays-potato-chips-american-style-cream-onion-flavour-best-quality-crunchy.jpg","Grocery ","Lays Chips Yellow",
                20,40,"200gm","600gm",400,4,"randomlocat"));

        cartitemlist.add(new cartitemsModel(80,"Deepu Store","https://www.bigbasket.com/media/uploads/p/xxl/40202281_4-lays-potato-chips-american-style-cream-onion-flavour-best-quality-crunchy.jpg","Grocery ","Lays Chips Yellow",
                20,40,"200gm","600gm",400,4,"randomlocat"));

    }


}
