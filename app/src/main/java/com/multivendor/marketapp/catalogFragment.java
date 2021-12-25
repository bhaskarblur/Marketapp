package com.multivendor.marketapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.multivendor.marketapp.Adapters.catalcateAdapter;
import com.multivendor.marketapp.Adapters.productitemAdapter;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.CustomDialogs.clearcartDialog;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.categoriesModel;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.ViewModel.catalogViewModel;
import com.multivendor.marketapp.databinding.FragmentCatalogBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link catalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class catalogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentCatalogBinding fcbinding;
    private catalogViewModel catViewModel;
    private com.multivendor.marketapp.Adapters.catalcateAdapter catalcateAdapter;
    private com.multivendor.marketapp.Adapters.productitemAdapter productitemAdapter;
    private List<cartModel.cartqtyandsize> qtysizelist = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Boolean shortbol = false;
    private Boolean sameshop = false;
    private String lat;
    private String longit;
    private String storeid;
    private String cartid;
    String shopnamerec = new String();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Integer items=0;
    private Integer amount=0;
    private Boolean animdone = false;
    private Boolean gotdata = false;
    private String filterQuery;
    private String searchQuery;
    public catalogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static catalogFragment newInstance(String param1, String param2) {
        catalogFragment fragment = new catalogFragment();
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
        Bundle bundle = getArguments();
        String shopname = bundle.getString("shopname");
        shopnamerec = shopname;
        catViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(catalogViewModel.class);
        catViewModel.initwork(shopnamerec);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fcbinding = FragmentCatalogBinding.inflate(inflater, container, false);
        loadcategories();
        catViewModel.getCatModel().observe(getActivity(), new Observer<List<categoriesModel>>() {
            @Override
            public void onChanged(List<categoriesModel> categoriesModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        catalcateAdapter.notifyDataSetChanged();
                    }
                }, 100);
            }
        });
        fcbinding.progressBar5.setVisibility(View.VISIBLE);
        fcbinding.catalitems.setVisibility(View.INVISIBLE);
        fcbinding.catalcategrec.setVisibility(View.INVISIBLE);
        fcbinding.allcatcard.setVisibility(View.INVISIBLE);
        catViewModel.getItemModel().observe(getActivity(), new Observer<List<productitemModel>>() {
            @Override
            public void onChanged(List<productitemModel> productitemModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (productitemModels.size() > 0) {
                            fcbinding.progressBar5.setVisibility(View.INVISIBLE);
                            fcbinding.catalitems.setVisibility(View.VISIBLE);
                            fcbinding.catalcategrec.setVisibility(View.VISIBLE);
                            fcbinding.allcatcard.setVisibility(View.VISIBLE);

                            getlatlong();
                            checkcartexists();
                            checksamestore();
                            LoadCart();
                        }
                        else {
                            Toast.makeText(getActivity(), "No Products Available!", Toast.LENGTH_SHORT).show();
                            fcbinding.progressBar5.setVisibility(View.INVISIBLE);
                        }

                    }
                }, 2000);
            }
        });

        viewfunction();
        return fcbinding.getRoot();

    }

    private void viewfunction() {

        fcbinding.cartDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartfragment cartfragment = new cartfragment();
                Bundle bundlenew = new Bundle();
                bundlenew.putString("shopname", shopnamerec);
                cartfragment.setArguments(bundlenew);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, cartfragment);
                transaction.addToBackStack("B");
                transaction.commit();
            }
        });
        fcbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
                //   Bundle bundlenew = new Bundle();
                //  bundlenew.putString("shopname", shopnamerec);
                // shopsFragment shopsFragment = new shopsFragment();
                //  shopsFragment.setArguments(bundlenew);
                //  FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                //  transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right);
                //  transaction.replace(R.id.mainfragment, shopsFragment);
                //    transaction.commit();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getlatlong() {

        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {
                        lat = String.valueOf(location.getLatitude());
                        longit = String.valueOf(location.getLongitude());

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

    public void checkcartexists() {
        SharedPreferences shpref = getActivity().getSharedPreferences("userlogged", 0);
        String userid = shpref.getString("userid", "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
        Call<cartModel.cartResp> call = logregApiInterface.get_cart(userid);
        call.enqueue(new Callback<cartModel.cartResp>() {
            @Override
            public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {

                if (!response.isSuccessful()) {
                    Log.d("errorcode:", String.valueOf(response.code()));
                    return;
                }

                cartModel.cartResp cartResp = response.body();
                if (cartResp.getResult() != null) {
                    shortbol = true;


                } else {
                    shortbol = false;

                }

            }

            @Override
            public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                Log.d("error", t.getMessage().toString());
                shortbol = false;
            }
        });

    }

    public void checksamestore() {
        SharedPreferences shpref = getActivity().getSharedPreferences("userlogged", 0);
        String userid = shpref.getString("userid", "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
        Call<cartModel.cartResp> call = logregApiInterface.get_cart(userid);
        call.enqueue(new Callback<cartModel.cartResp>() {
            @Override
            public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {

                if (!response.isSuccessful()) {
                    Log.d("errorcode:", String.valueOf(response.code()));
                    return;
                }

                cartModel.cartResp cartResp = response.body();

                if (cartResp.getResult() != null) {
                    if (cartResp.getResult().getCart_id() != null) {
                        String cartid = cartResp.getResult().getCart_id();
                    } else {
                        cartid = null;
                    }

                    if (cartResp.getResult().getStore() != null) {
                        storeid = cartResp.getResult().getStore();

                        if (cartResp.getResult().getStore().equals(catViewModel.getCatModel().getValue().get(0).getStore_id())) {
                            sameshop = true;

                        } else {
                            sameshop = false;
                        }
                    } else {
                        storeid = null;
                        sameshop = false;
                    }
                    //  productitemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                Log.d("error", t.getMessage().toString());
                sameshop = false;
            }
        });

    }


    public void LoadCart() {
        SharedPreferences shpref = getActivity().getSharedPreferences("userlogged", 0);
        String userid = shpref.getString("userid", "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        qtysizelist = new ArrayList<>();
        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
        Call<cartModel.cartResp> call = logregApiInterface.get_cart(userid);
        call.enqueue(new Callback<cartModel.cartResp>() {
            @Override
            public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {

                if (!response.isSuccessful()) {
                    Log.d("errorcode:", String.valueOf(response.code()));
                    return;
                }

                cartModel.cartResp cartResp = response.body();

                if (cartResp.getResult() != null) {

                    if (cartResp.getResult().getProducts() != null) {

                        if (cartResp.getResult().getProducts().size() > 0) {
                            if (cartResp.getResult().getStore() != null) {
                                SharedPreferences.Editor editor = shpref.edit();
                                editor.putString("cartshop", cartResp.getResult().getStore());
                                editor.apply();
                                if (cartResp.getResult().getStore().equals(catViewModel.getCatModel().getValue()
                                        .get(0).getStore_id())) {
                                    //checksamestore();
                                    if (animdone.equals(false)) {
                                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_down);
                                        fcbinding.cartDialog.setAnimation(animation);
                                        animdone = true;
                                    }
                                    fcbinding.cartDialog.setVisibility(View.VISIBLE);
                                    fcbinding.ttlitemsTxt.setText(String.valueOf(cartResp
                                            .getResult().getProducts().size()) + " Items");

                                    items = cartResp.getResult().getProducts().size();
                                    amount = Integer.parseInt(cartResp.getResult().getSubtotal());

                                    fcbinding.ttlpriceTxt.setText("₹" + cartResp.getResult().getSubtotal().toString());

                                    for (int i = 0; i < cartResp.getResult().getProducts().size(); i++) {

                                        qtysizelist.add(i, new cartModel.cartqtyandsize(cartResp.getResult().getProducts()
                                                .get(i).getProduct_id(), cartResp.getResult().getProducts()
                                                .get(i).getProduct_name(), cartResp.getResult().getProducts()
                                                .get(i).getVariant_id(), cartResp.getResult().getProducts()
                                                .get(i).getSize(), cartResp.getResult().getProducts()
                                                .get(i).getQty()));


                                    }
                                    loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                                    if(filterQuery!=null) {
                                        filterfun(filterQuery);
                                    }

                                } else {
                                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                                    fcbinding.cartDialog.setAnimation(animation);
                                    fcbinding.cartDialog.setVisibility(View.GONE);
                                    loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                                    // productitemAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if(getContext()!=null) {
                                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                                    fcbinding.cartDialog.setAnimation(animation);
                                    fcbinding.cartDialog.setVisibility(View.GONE);
                                    loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                                    //   productitemAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            if(getContext()!=null) {
                                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                                fcbinding.cartDialog.setAnimation(animation);
                                fcbinding.cartDialog.setVisibility(View.GONE);
                                loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                                //  productitemAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        if(getContext()!=null) {
                            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                            fcbinding.cartDialog.setAnimation(animation);
                            fcbinding.cartDialog.setVisibility(View.GONE);
                            loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                            //productitemAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if(getContext()!=null) {
                        loaditems(null, null);
                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                        fcbinding.cartDialog.setAnimation(animation);
                        fcbinding.cartDialog.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                Log.d("error", t.getMessage().toString());
                sameshop = false;
            }
        });
    }

    private void loaditems(String store_id, String cart_id) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");
        productitemAdapter = new productitemAdapter(getContext(), catViewModel.getItemModel().getValue(),
                qtysizelist, storeid);
        productitemAdapter.setStore_id(store_id);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        fcbinding.catalitems.setLayoutManager(llm);
        fcbinding.catalitems.setAdapter(productitemAdapter);
        fcbinding.catalitems.setHasFixedSize(true);
        fcbinding.catalitems.setNestedScrollingEnabled(false);
        fcbinding.catalitems.setItemViewCacheSize(50);
        fcbinding.catalitems.setDrawingCacheEnabled(true);
        fcbinding.catalitems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        if(getContext()!=null) {
            fcbinding.catalsearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    fcbinding.catalitems.setVisibility(View.INVISIBLE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString() != null && !s.toString().isEmpty()) {
                        fcbinding.catalitems.setVisibility(View.VISIBLE);
                        searchfun(s.toString());
                    } else {
                        fcbinding.catalitems.setVisibility(View.VISIBLE);
                        productitemAdapter.searchList(catViewModel.getItemModel().getValue());
                    }
                }
            });
        }

        productitemAdapter.setonproductsClick(new productitemAdapter.onproductsClick() {
            @Override
            public void onSizeClick(int position, String size_name) {
                // Toast.makeText(getContext(), size_name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onaddClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        items = items + 1;
                        amount = amount + Integer.valueOf(productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                .getSelling_price());

                        fcbinding.ttlitemsTxt.setText(items + " Items");
                        fcbinding.ttlpriceTxt.setText("₹" + String.valueOf(amount));

                    }
                }, 50);
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                if (sameshop.equals(true)) {
                    if (!lat.toString().isEmpty()) {
                        Call<cartModel.cartResp> call = null;
                        if (catViewModel.getCatModel().getValue().get(0).getStore_id().equals(store_id)) {

                            call = logregApiInterface.add_cart(lat, longit, userid,
                                    productitemAdapter.productmodel.get(position).getUserid(),
                                    prod_id, productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos).getVariation_id(), productitemAdapter.productmodel.get(position).getSku(),
                                    productitemAdapter.productmodel.get(position).getItemname(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos).getSize(),
                                    productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                            .getSelling_price(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                            .getSelling_price(), qty, cart_id);

                        } else {

                            call = logregApiInterface.add_cart(lat, longit, userid,
                                    productitemAdapter.productmodel.get(position).getUserid(),
                                    prod_id, productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos).getVariation_id(), productitemAdapter.productmodel.get(position).getSku(),
                                    productitemAdapter.productmodel.get(position).getItemname(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos).getSize(),
                                    productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                            .getSelling_price(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                            .getSelling_price(), qty, null);

                        }

                        call.enqueue(new Callback<cartModel.cartResp>() {
                            @Override
                            public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("errorcode", String.valueOf(response.code()));
                                    return;
                                }

                                cartModel.cartResp resp = response.body();
                                Log.d("msg", resp.getMessage());
                                if (resp.getMessage().equals("Product added successfully")) {
                                    checkcartexists();
                                    checksamestore();
                                    LoadCart();
                                } else {

                                    Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                                Log.d("error", t.getMessage());
                            }
                        });
                    }
                } else if (store_id == null || cart_id == null) {
                    Call<cartModel.cartResp> call;
                    call = logregApiInterface.add_cart(lat, longit, userid,
                            productitemAdapter.productmodel.get(position).getUserid(),
                            prod_id, productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos).getVariation_id(), catViewModel.getItemModel().getValue().get(position).getSku(),
                            productitemAdapter.productmodel.get(position).getItemname(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos).getSize(),
                            productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                    .getSelling_price(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                    .getSelling_price(), qty, null);

                    call.enqueue(new Callback<cartModel.cartResp>() {
                        @Override
                        public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                            if (!response.isSuccessful()) {
                                Log.d("errorcode", String.valueOf(response.code()));
                                return;
                            }

                            cartModel.cartResp resp = response.body();
                            Log.d("msg", resp.getResult().toString());
                            if (resp.getMessage().equals("Product added successfully")) {
                                checkcartexists();
                                checksamestore();
                                LoadCart();
                            } else {

                                Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                            Log.d("error", t.getMessage());
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).
                            setTitle("Cart Already Loaded").
                            setMessage("Your cart contains items from some other shop. Do you want to remove old items?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
                                    String userid = sharedPreferences.getString("userid", "");

                                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                                            .addConverterFactory(GsonConverterFactory.create()).build();

                                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                                    Call<cartModel.cartResp> call = logregApiInterface.clear_cart(userid);

                                    call.enqueue(new Callback<cartModel.cartResp>() {
                                        @Override
                                        public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                                            if (!response.isSuccessful()) {
                                                Log.d("errorcode", String.valueOf(response.code()));
                                                return;
                                            }

                                            cartModel.cartResp resp = response.body();


                                            if (resp.getMessage().equals("Clear Cart successfully")) {

                                                Toast.makeText(getActivity(), "Cart cleared!", Toast.LENGTH_SHORT).show();
                                                Call<cartModel.cartResp> calladd;
                                                calladd = logregApiInterface.add_cart(lat, longit, userid,
                                                        productitemAdapter.productmodel.get(position).getUserid(),
                                                        prod_id, productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos).getVariation_id(), catViewModel.getItemModel().getValue().get(position).getSku(),
                                                        productitemAdapter.productmodel.get(position).getItemname(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos).getSize(),
                                                        productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                                                .getSelling_price(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                                                .getSelling_price(), qty, null);

                                                calladd.enqueue(new Callback<cartModel.cartResp>() {
                                                    @Override
                                                    public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                                                        if (!response.isSuccessful()) {
                                                            Log.d("errorcode", String.valueOf(response.code()));
                                                            return;
                                                        }

                                                        cartModel.cartResp resp = response.body();
                                                        Log.d("msg", resp.getResult().toString());
                                                        if (resp.getMessage().equals("Product added successfully")) {
                                                            checkcartexists();
                                                            checksamestore();
                                                            LoadCart();
                                                        } else {

                                                            Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                                                        Log.d("error", t.getMessage());
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getActivity(), "There was an error!", Toast.LENGTH_SHORT).show();
                                                checkcartexists();
                                                checksamestore();
                                                LoadCart();

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                                            Log.d("error", t.getMessage());
                                            checkcartexists();
                                            checksamestore();
                                            LoadCart();
                                        }
                                    });
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    builder.setCancelable(false);
                    builder.show();
                    //  clearcartDialog clearcartDialog = new clearcartDialog();

                    // clearcartDialog.show(getParentFragmentManager(), "clearcartDialog");
                    // clearcartDialog.setCancelable(false);


                }

            }

            @Override
            public void onplusClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        amount = amount + Integer.valueOf(productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                .getSelling_price());

                        fcbinding.ttlpriceTxt.setText("₹" + String.valueOf(amount));

                    }
                }, 50);
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<cartModel.cartResp> call = logregApiInterface.update_cart(lat, longit, userid
                        , store_id, prod_id, size_id, productitemAdapter.productmodel.get(position).getSku(),
                        productitemAdapter.productmodel.get(position).getItemname(), sizename,
                        productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                .getSelling_price(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                .getSelling_price(), qty, cart_id);

                call.enqueue(new Callback<cartModel.cartResp>() {
                    @Override
                    public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("error code", String.valueOf(response.code()));
                            return;
                        }

                        cartModel.cartResp resp = response.body();
                        Log.d("msg", resp.getMessage().toString());
                        if (resp.getMessage().equals("Product updated successfully ")) {
                            checkcartexists();
                            checksamestore();
                            LoadCart();
                        } else {

                            Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                        // Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onminusClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty) {

                if (Integer.parseInt(qty) > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            amount = amount - Integer.valueOf(productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                    .getSelling_price());

                            fcbinding.ttlpriceTxt.setText("₹" + String.valueOf(amount));

                        }
                    }, 50);
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                    Call<cartModel.cartResp> call = logregApiInterface.update_cart(lat, longit, userid
                            , store_id, prod_id, productitemAdapter.productmodel
                                    .get(position).getSizeandquats().get(sizepos).getVariation_id(), productitemAdapter.productmodel.get(position).getSku(),
                            productitemAdapter.productmodel.get(position).getItemname(), sizename,
                            productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                    .getSelling_price(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                    .getSelling_price(), qty, cart_id);

                    call.enqueue(new Callback<cartModel.cartResp>() {
                        @Override
                        public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                            if (!response.isSuccessful()) {
                                Log.d("error code", String.valueOf(response.code()));
                                return;
                            }

                            cartModel.cartResp resp = response.body();
                            Log.d("msg", resp.getMessage().toString());
                            if (resp.getMessage().equals("Product updated successfully ")) {
                                checkcartexists();
                                checksamestore();
                                LoadCart();
                            } else {

                                Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                            Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items = items - 1;
                            amount = amount - Integer.valueOf(productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                    .getSelling_price());

                            fcbinding.ttlitemsTxt.setText(items + " Items");
                            fcbinding.ttlpriceTxt.setText("₹" + String.valueOf(amount));

                        }
                    }, 50);
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                    Call<cartModel.cartResp> call = logregApiInterface.remove_product(lat, longit, userid
                            , store_id, prod_id, productitemAdapter.productmodel
                                    .get(position).getSizeandquats().get(sizepos).getVariation_id(), productitemAdapter.productmodel.get(position).getSku(),
                            productitemAdapter.productmodel.get(position).getItemname(), sizename,
                            productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                    .getSelling_price(), productitemAdapter.productmodel.get(position).getSizeandquats().get(sizepos)
                                    .getSelling_price(), qty, cart_id);

                    call.enqueue(new Callback<cartModel.cartResp>() {
                        @Override
                        public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                            if (!response.isSuccessful()) {
                                Log.d("error code", String.valueOf(response.code()));
                                return;
                            }

                            cartModel.cartResp resp = response.body();
                            Log.d("msg", resp.getMessage().toString());
                            if (resp.getMessage().equals("Product deleted successfully")) {
                                checkcartexists();
                                checksamestore();
                                LoadCart();
                            } else {

                                Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                            //   Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private void loadcategories() {

        fcbinding.allcatcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterfun("");
                fcbinding.allcatname.setTextColor(Color.parseColor("#0881E3"));
                catalcateAdapter.selectedcategory = "";
                catalcateAdapter.notifyDataSetChanged();
            }
        });

        catalcateAdapter = new catalcateAdapter(getContext(), catViewModel.getCatModel().getValue());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        fcbinding.catalcategrec.setLayoutManager(llm);
        fcbinding.catalcategrec.setAdapter(catalcateAdapter);
        catalcateAdapter.setoncardclicklistener(new allcategoriesAdapter.oncardclicklistener() {
            @Override
            public void oncardclick(int position) {
                fcbinding.allcatname.setTextColor(Color.parseColor("#000000"));
                fcbinding.catalitems.setVisibility(View.VISIBLE);
                String filter = catViewModel.getCatModel().getValue().get(position).getId();
                if (productitemAdapter != null) {
                    filterfun(filter);
                }
            }
        });

    }

    private void filterfun(String query) {
        filterQuery=query;
        Log.d("query", query);
        List<productitemModel> searchedList = new ArrayList<>();
        //searchedList.clear();
        for (productitemModel model : catViewModel.getItemModel().getValue()) {
            if (model.getCategory_id().toString().contains(query)) {
                searchedList.add(model);
            }

        }
        if (productitemAdapter != null) {

            productitemAdapter.searchList(searchedList);
        }
    }

    private void searchfun(String query) {
        Log.d("query", query);
        searchQuery=query;
        List<productitemModel> searchedList = new ArrayList<>();
        //searchedList.clear();
        for (productitemModel model : catViewModel.getItemModel().getValue()) {
            if (model.getItemname().toString().toLowerCase().contains(query.toLowerCase())) {

                searchedList.add(model);
            }

        }
        productitemAdapter.searchList(searchedList);

    }


}



