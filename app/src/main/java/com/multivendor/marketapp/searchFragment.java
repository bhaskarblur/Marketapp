package com.multivendor.marketapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.multivendor.marketapp.Adapters.nbyshopAdapter;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.databinding.FragmentSearchBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class searchFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSearchBinding shbinding;
    private com.multivendor.marketapp.Adapters.nbyshopAdapter nbyshopAdapter;
    private com.multivendor.marketapp.ViewModel.homefragViewModel categfragViewModel;
    private String selcatname;
    private String mParam1;
    private String mParam2;
    private String lat;
    private String longit;
    private String userid;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;
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
        userid=getActivity().getSharedPreferences("userlogged",0).getString("userid","");
        categfragViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketapp.ViewModel.homefragViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shbinding = FragmentSearchBinding.inflate(inflater, container, false);

        Bundle bundle=getArguments();
        String lat=bundle.getString("lat","");
        String longit=bundle.getString("long","");
        String cityname=bundle.getString("city_name","");
        categfragViewModel.getlocation(userid,lat,longit,cityname);
        categfragViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
            @Override
            public void onChanged(newProductModel.homeprodResult homeprodResult) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (homeprodResult.getAll_products().size() > 0) {
                            loadsearchres();
                            nbyshopAdapter.notifyDataSetChanged();

                        }
                    }
                }, 200);
            }
        });
        viewfunctions();
        loadsearchres();

        return shbinding.getRoot();

    }


    private void loadsearchres() {
        shbinding.searchres.setVisibility(View.INVISIBLE);
        nbyshopAdapter = new nbyshopAdapter(getContext(), categfragViewModel.getnbyshopModel().getValue().getAll_products());
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

        List<newProductModel.ListProductresp> searchedList = new ArrayList<>();
        //searchedList.clear();
        if(categfragViewModel.getnbyshopModel().getValue().getAll_products()!=null) {
            for (newProductModel.ListProductresp model : categfragViewModel.getnbyshopModel().getValue().getAll_products()) {

                if (model.getProduct_name().toString().toLowerCase().contains(query.toLowerCase()) ||
                        model.getProduct_category().toString().toLowerCase().contains(query.toLowerCase())) {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getViewModelStore().clear();
    }
}