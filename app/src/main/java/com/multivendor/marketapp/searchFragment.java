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
        categfragViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketapp.ViewModel.homefragViewModel.class);
        categfragViewModel.getlocation(lat,longit);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shbinding = FragmentSearchBinding.inflate(inflater, container, false);

        Bundle bundle=getArguments();
        String lat=bundle.getString("lat","");
        String longit=bundle.getString("long","");
        viewfunctions();
        loadsearchres();
        return shbinding.getRoot();

    }
    @SuppressLint("MissingPermission")
    private void getlatlong() {
        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        if(getContext()!=null) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                    Location location = task.getResult();
                                    if (location != null) {

                                        lat = String.valueOf(location.getLatitude());
                                        longit = String.valueOf(location.getLongitude());
                                        categfragViewModel.getlocation(lat, longit);
                                        Geocoder geocoder = null;
                                        if(getContext()!=null) {
                                            geocoder = new Geocoder(getActivity()
                                                    , Locale.getDefault());
                                        }
                                        try {
                                            if(geocoder!=null) {
                                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

//                                                hmbinding.locattext.setText(addresses.get(0).getLocality());
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if(categfragViewModel.getnbyshopModel()!=null) {
                                            categfragViewModel.getnbyshopModel().observe(getActivity(), new Observer<List<nbyshopsModel>>() {
                                                @Override
                                                public void onChanged(List<nbyshopsModel> nbyshopsModels) {
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (nbyshopsModels.size() > 0) {
                                                                nbyshopAdapter.notifyDataSetChanged();

                                                            }
                                                        }
                                                    }, 200);
                                                }
                                            });
                                           loadsearchres();
                                        }
                                    } else {

                                        LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                                .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                                        LocationCallback locationCallback = new LocationCallback() {
                                            @Override
                                            public void onLocationResult(LocationResult locationResult) {
                                                super.onLocationResult(locationResult);
                                                Location location1 = locationResult.getLastLocation();
                                                lat = String.valueOf(location1.getLatitude());
                                                longit = String.valueOf(location1.getLongitude());
                                                categfragViewModel.getlocation(lat, longit);
                                                Geocoder geocoder = null;
                                                if(getContext()!=null) {
                                                    geocoder = new Geocoder(getActivity()
                                                            , Locale.getDefault());
                                                }
                                                try {
                                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

//                                                    hmbinding.locattext.setText(addresses.get(0).getLocality());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                categfragViewModel.getnbyshopModel().observe(getActivity(), new Observer<List<nbyshopsModel>>() {
                                                    @Override
                                                    public void onChanged(List<nbyshopsModel> nbyshopsModels) {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (nbyshopsModels.size() > 0) {
                                                                    nbyshopAdapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        }, 200);
                                                    }
                                                });
                                                loadsearchres();
                                            }
                                        };
                                    }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, Looper.getMainLooper());
                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }


            }
        }

    }

    private void loadsearchres() {
        shbinding.searchres.setVisibility(View.INVISIBLE);
        nbyshopAdapter = new nbyshopAdapter(getContext(), categfragViewModel.getnbyshopModel().getValue());
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
        if(categfragViewModel.getnbyshopModel().getValue()!=null) {
            for (nbyshopsModel model : categfragViewModel.getnbyshopModel().getValue()) {

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