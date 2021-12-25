package com.multivendor.marketapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multivendor.marketapp.Adapters.nbyshopAdapter;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class searchFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSearchBinding shbinding;
    private com.multivendor.marketapp.Adapters.nbyshopAdapter nbyshopAdapter;
    private com.multivendor.marketapp.ViewModel.categfragViewModel categfragViewModel;
    private String selcatname;
    private String mParam1;
    private String mParam2;

    public searchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        categfragViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketapp.ViewModel.categfragViewModel.class);
        categfragViewModel.initwork(selcatname);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shbinding = FragmentSearchBinding.inflate(inflater, container, false);

        Bundle bundle=getArguments();
        String lat=bundle.getString("lat","");
        String longit=bundle.getString("long","");
        categfragViewModel.getlocation(lat,longit);
        categfragViewModel.getnbyshopmodel().observe(getActivity(), new Observer<List<nbyshopsModel>>() {
            @Override
            public void onChanged(List<nbyshopsModel> nbyshopsModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nbyshopAdapter.notifyDataSetChanged();
                    }
                }, 100);
            }
        });
        viewfunctions();
        loadsearchres();
        return shbinding.getRoot();

    }

    private void loadsearchres() {
        shbinding.searchres.setVisibility(View.INVISIBLE);
        nbyshopAdapter = new nbyshopAdapter(getContext(), categfragViewModel.getnbyshopmodel().getValue());
        LinearLayoutManager glm = new LinearLayoutManager(getContext());
        glm.setOrientation(RecyclerView.VERTICAL);
        shbinding.searchres.setLayoutManager(glm);
        shbinding.searchres.setAdapter(nbyshopAdapter);

        shbinding.catalsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                shbinding.searchres.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shbinding.searchres.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.toString().isEmpty()) {
                    shbinding.searchres.setVisibility(View.VISIBLE);
                    searchfun(s.toString());
                }
                else  {
                    shbinding.searchres.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void searchfun(String query) {

        List<nbyshopsModel> searchedList = new ArrayList<>();
        //searchedList.clear();
        if(categfragViewModel.getnbyshopmodel().getValue()!=null) {
            for (nbyshopsModel model : categfragViewModel.getnbyshopmodel().getValue()) {

                if (model.getCategory().toString().toLowerCase().contains(query.toLowerCase()) ||
                        model.getShopname().toString().toLowerCase().contains(query.toLowerCase())) {

                    searchedList.add(model);
                }

            }
            nbyshopAdapter.searchList(searchedList);
        }
    }

    private void viewfunctions() {
        shbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homefragment home = new homefragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainfragment, home);
                transaction.commit();
            }
        });
    }
}