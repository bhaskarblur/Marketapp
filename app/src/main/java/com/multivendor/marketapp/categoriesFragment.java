package com.multivendor.marketapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.multivendor.marketapp.Adapters.allcategoriesAdapter;
import com.multivendor.marketapp.Adapters.nbyshopAdapter;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.databinding.CategoriesLayBinding;
import com.multivendor.marketapp.databinding.FragmentCartfragmentBinding;
import com.multivendor.marketapp.databinding.FragmentCategoriesBinding;

import java.util.ArrayList;
import java.util.List;


public class categoriesFragment extends Fragment {

    private FragmentCategoriesBinding cfbidning;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private com.multivendor.marketapp.Adapters.allcategoriesAdapter allcategoriesAdapter;
    private com.multivendor.marketapp.Adapters.nbyshopAdapter nbyshopAdapter;
    private com.multivendor.marketapp.ViewModel.categfragViewModel categfragViewModel;
    private String selcatname;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String lat;
    private String longit;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public categoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment categoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static categoriesFragment newInstance(String param1, String param2) {
        categoriesFragment fragment = new categoriesFragment();
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
        Bundle bundle=getArguments();
        selcatname=bundle.getString("selectedCategoryname");
        categfragViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketapp.ViewModel.categfragViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cfbidning = FragmentCategoriesBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        String selcategory = bundle.getString("selectedCategory");
        selcatname = bundle.getString("selectedCategoryname");
        cfbidning.textView.setText(selcatname);
        getlatlong();
        viewfunctions();

        return cfbidning.getRoot();
    }

    @SuppressLint("MissingPermission")
    private void getlatlong() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
            fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                }
            }, Looper.getMainLooper());
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {
                        lat = String.valueOf(location.getLatitude());
                        longit = String.valueOf(location.getLongitude());
                        categfragViewModel.initwork( selcatname,lat, longit);
                        categfragViewModel.getnbyshopmodel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                            @Override
                            public void onChanged(newProductModel.homeprodResult homeprodResult) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadshoprec();
                                        nbyshopAdapter.notifyDataSetChanged();
                                        cfbidning.shoprec.setVisibility(View.VISIBLE);
                                        cfbidning.progressBar3.setVisibility(View.INVISIBLE);
                                    }
                                }, 500);
                            }

                        });


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
                                categfragViewModel.initwork(selcatname, lat, longit);
                                categfragViewModel.getnbyshopmodel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                                    @Override
                                    public void onChanged(newProductModel.homeprodResult homeprodResult) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadshoprec();
                                                nbyshopAdapter.notifyDataSetChanged();
                                                cfbidning.shoprec.setVisibility(View.VISIBLE);
                                                cfbidning.progressBar3.setVisibility(View.INVISIBLE);
                                            }
                                        }, 500);
                                    }

                                });

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


    }

    private void loadshoprec() {

        nbyshopAdapter = new nbyshopAdapter(getContext(), categfragViewModel.getnbyshopmodel().getValue().getAll_products());
        GridLayoutManager glm = new GridLayoutManager(getContext(),2);
        glm.setOrientation(RecyclerView.VERTICAL);
        cfbidning.shoprec.setLayoutManager(glm);
        cfbidning.shoprec.setAdapter(nbyshopAdapter);
    }

    private void searchfun(String query) {
        cfbidning.shoprec.setVisibility(View.VISIBLE);
        cfbidning.sharebtn.setVisibility(View.GONE);
        cfbidning.sharetxt.setVisibility(View.GONE);
        cfbidning.sharetxt2.setVisibility(View.GONE);
        List<newProductModel.ListProductresp> searchedList = new ArrayList<>();
        //searchedList.clear();
        for (newProductModel.ListProductresp model : categfragViewModel.getnbyshopmodel().getValue().getAll_products()) {
            if (model.getProduct_name().toString().toLowerCase().contains(query.toLowerCase())) {
                searchedList.add(model);
            }
        }
        nbyshopAdapter.searchList(searchedList);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(nbyshopAdapter.getItemCount()<1) {
                    cfbidning.sharebtn.setVisibility(View.VISIBLE);
                    cfbidning.sharetxt.setVisibility(View.VISIBLE);
                    cfbidning.sharetxt2.setVisibility(View.VISIBLE);
                }
            }
        },1001);

    }

    private void viewfunctions() {
        cfbidning.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homefragment homeFragment = new homefragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right);
                transaction.replace(R.id.mainfragment, homeFragment);
                transaction.commit();
            }
        });

        cfbidning.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharemsg = new Intent(Intent.ACTION_VIEW);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Market App");
                    String shareMessage= "Install MarketApp for Seller Now. And take your business online." +
                            " Start receiving the orders online from your city.\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.multivendor.marketsellerapp";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cfbidning.searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("lat", lat);
                bundle.putString("long", longit);
                searchFragment searchFragment = new searchFragment();
                searchFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, searchFragment);
                transaction.addToBackStack("A");
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