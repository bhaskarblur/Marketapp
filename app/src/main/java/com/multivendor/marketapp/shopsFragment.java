package com.multivendor.marketapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.multivendor.marketapp.Adapters.catalcateAdapter;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.CustomDialogs.nocatDialog;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.databinding.FragmentShopsBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class shopsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentShopsBinding fsbinding;
    private catalcateAdapter catadapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String shopnamerec;
    private com.multivendor.marketapp.ViewModel.shopViewModel shViewModel;
    private String lat;
    private String longit;
    private String sellerlat;
    private String sellerlong;
    private Boolean hascats = false;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;
    
    
    public shopsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment shopsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static shopsFragment newInstance(String param1, String param2) {
        shopsFragment fragment = new shopsFragment();
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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fsbinding = FragmentShopsBinding.inflate(inflater, container, false);
        viewfunctions();
        Bundle bundle = getArguments();
        String shopname = bundle.getString("shopname");
        shopnamerec = shopname;

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        View bottom=getActivity().findViewById(R.id.bottomnav);
        bottom.setVisibility(View.GONE);
        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");
        Call<userAPIResp.getStores> call = logregApiInterface.get_favshop(userid);
        call.enqueue(new Callback<userAPIResp.getStores>() {
            @Override
            public void onResponse(Call<userAPIResp.getStores> call, Response<userAPIResp.getStores> response) {
                if (!response.isSuccessful()) {
                    Log.d("errocode:", String.valueOf(response.code()));
                    return;
                }

                userAPIResp.getStores getStores = response.body();
                if (getStores.getResult() != null) {
                    for (nbyshopsModel shopmodel : getStores.getResult()) {
                        if (shopmodel.getId() != null) {
                            if (shopmodel.getId().toString().contains(shopnamerec)) {
                                fsbinding.heartonbtn.setVisibility(View.VISIBLE);
                                fsbinding.heartoffbtn.setVisibility(View.INVISIBLE);
                            } else {
                                fsbinding.heartonbtn.setVisibility(View.INVISIBLE);
                                fsbinding.heartoffbtn.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }


            @Override
            public void onFailure(Call<userAPIResp.getStores> call, Throwable t) {

            }
        });

        getlatlong();


        return fsbinding.getRoot();
    }

    private void loadmat(double sellat, double sellongit, String shopname) {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        LatLng latLng = new LatLng(sellat, sellongit);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                .title(shopname);
                        sellerlat=String.valueOf(sellat);
                        sellerlong=String.valueOf(sellongit);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        }, 1000);


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
                        shViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketapp.ViewModel.shopViewModel.class);
                        shViewModel.initwork(shopnamerec, lat, longit);
                        Geocoder geocoder = new Geocoder(getActivity()
                                , Locale.getDefault());


                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        shViewModel.getNbyshopmodel().observe(getActivity(), new Observer<List<userAPIResp.sellerResult>>() {
                            @Override
                            public void onChanged(List<userAPIResp.sellerResult> nbyshopsModels) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (nbyshopsModels.size() > 0) {

                                           if(nbyshopsModels.get(0).getImage() != null)
                                           { Picasso.get().load(nbyshopsModels.get(0).getImage()).resize(600, 600)
                                                   .into(fsbinding.shopimg);}
                                           else {
                                               Picasso.get().load(R.drawable.imgsample).fit()
                                                       .into(fsbinding.shopimg);
                                           }

//
                                            fsbinding.shopname.setText(nbyshopsModels.get(0).getStore_name());
                                            fsbinding.shopname2.setText(nbyshopsModels.get(0).getStore_name());
                                            fsbinding.shoplocat.setText(nbyshopsModels.get(0).getAddress());
                                            fsbinding.shoprating.setText(nbyshopsModels.get(0).getRating());
                                            fsbinding.shopdist.setText(nbyshopsModels.get(0).getDistance() + "Km");
                                            fsbinding.shopaboutus.setText(nbyshopsModels.get(0).getAbout());
                                            catadapter = new catalcateAdapter(getContext(), nbyshopsModels.get(0).getCategories());
                                            LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                            llm.setOrientation(RecyclerView.HORIZONTAL);
                                           //  loadmat(location.getLatitude(),location.getLongitude(),nbyshopsModels.get(0).getStore_name());
                                              loadmat(Double.parseDouble(nbyshopsModels.get(0).getLat()),
                                                Double.parseDouble(nbyshopsModels.get(0).getLongit()),nbyshopsModels.get(0).getStore_name());
                                            if (nbyshopsModels.get(0).getCategories() != null) {
                                                if (nbyshopsModels.get(0).getCategories().size() > 0) {
                                                    hascats = true;
                                                } else {
                                                    hascats = false;
                                                }
                                            } else {
                                                hascats = false;
                                            }
                                            fsbinding.shopcatrec.setLayoutManager(llm);
                                            fsbinding.shopcatrec.setAdapter(catadapter);

                                        }


                                    }
                                }, 100);
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
                                Geocoder geocoder = new Geocoder(getActivity()
                                        , Locale.getDefault());

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                shViewModel.getNbyshopmodel().observe(getActivity(), new Observer<List<userAPIResp.sellerResult>>() {
                                    @Override
                                    public void onChanged(List<userAPIResp.sellerResult> nbyshopsModels) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (nbyshopsModels.size() > 0) {

                                                    Picasso.get().load(nbyshopsModels.get(0).getImage()).into(fsbinding.shopimg);
                                                    fsbinding.shopname.setText(nbyshopsModels.get(0).getStore_name());
                                                    fsbinding.shopname2.setText(nbyshopsModels.get(0).getStore_name());
                                                    fsbinding.shoplocat.setText(nbyshopsModels.get(0).getAddress());
                                                    fsbinding.shoprating.setText(nbyshopsModels.get(0).getRating());
                                                    fsbinding.shopdist.setText(nbyshopsModels.get(0).getDistance() + "Km");
                                                    fsbinding.shopaboutus.setText(nbyshopsModels.get(0).getAbout());
                                                    catadapter = new catalcateAdapter(getContext(), nbyshopsModels.get(0).getCategories());
                                                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                                    llm.setOrientation(RecyclerView.HORIZONTAL);
                                                         loadmat(Double.parseDouble(nbyshopsModels.get(0).getLat()),
                                                            Double.parseDouble(nbyshopsModels.get(0).getLongit()),nbyshopsModels.get(0).getStore_name());
                                                    if (nbyshopsModels.get(0).getCategories() != null) {
                                                        if (nbyshopsModels.get(0).getCategories().size() > 0) {
                                                            hascats = true;
                                                        } else {
                                                            hascats = false;
                                                        }
                                                    } else {
                                                        hascats = false;
                                                    }
                                                    fsbinding.shopcatrec.setLayoutManager(llm);
                                                    fsbinding.shopcatrec.setAdapter(catadapter);

                                                }

                                            }
                                        }, 100);
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

    private void viewfunctions() {
        fsbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View bottom=getActivity().findViewById(R.id.bottomnav);
                bottom.setVisibility(View.VISIBLE);
                getParentFragmentManager().popBackStack();
            }
        });
        fsbinding.seecatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hascats.equals(true)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("shopname", shopnamerec);
                    catalogFragment catfrag = new catalogFragment();
                    catfrag.setArguments(bundle);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                    transaction.replace(R.id.mainfragment, catfrag);
                    transaction.addToBackStack("A");
                    transaction.commit();
                } else {
                    nocatDialog nocatDialog = new nocatDialog();
                    nocatDialog.show(getParentFragmentManager(), "nocatDialog");
                    nocatDialog.setCancelable(false);
                }
            }
        });

        fsbinding.directorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("storeid", shopnamerec);
                makequickfragment catfrag = new makequickfragment();
                catfrag.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left);
                transaction.replace(R.id.mainfragment, catfrag);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });
        fsbinding.shopmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "http://maps.google.co.in/maps?q=" + sellerlat+","+sellerlong;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                getActivity().startActivity(intent);
            }
        });

        fsbinding.heartoffbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
                String userid = sharedPreferences.getString("userid", "");
                Call<loginresResponse.verifyforgpass> call = logregApiInterface.add_favshop(userid, shopnamerec);
                call.enqueue(new Callback<loginresResponse.verifyforgpass>() {
                    @Override
                    public void onResponse(Call<loginresResponse.verifyforgpass> call, Response<loginresResponse.verifyforgpass> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        if (!response.isSuccessful()) {
                            Log.d("errocode:", String.valueOf(response.code()));
                            return;
                        }

                        loginresResponse.verifyforgpass data = response.body();
                        Toast.makeText(getContext(), "Added To Favourites!", Toast.LENGTH_SHORT).show();
                        fsbinding.heartoffbtn.setVisibility(View.INVISIBLE);
                        fsbinding.heartonbtn.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<loginresResponse.verifyforgpass> call, Throwable t) {

                    }
                });
            }

        });

        fsbinding.heartonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
                String userid = sharedPreferences.getString("userid", "");
                Call<loginresResponse.verifyforgpass> call = logregApiInterface.add_favshop(userid, shopnamerec);
                call.enqueue(new Callback<loginresResponse.verifyforgpass>() {
                    @Override
                    public void onResponse(Call<loginresResponse.verifyforgpass> call, Response<loginresResponse.verifyforgpass> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        if (!response.isSuccessful()) {
                            Log.d("errocode:", String.valueOf(response.code()));
                            return;
                        }

                        loginresResponse.verifyforgpass data = response.body();
                        Toast.makeText(getContext(), "Removed From Favourites!", Toast.LENGTH_SHORT).show();
                        fsbinding.heartoffbtn.setVisibility(View.VISIBLE);
                        fsbinding.heartonbtn.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<loginresResponse.verifyforgpass> call, Throwable t) {

                    }
                });
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getViewModelStore().clear();
        View bottom=getActivity().findViewById(R.id.bottomnav);
        bottom.setVisibility(View.VISIBLE);
    }
}