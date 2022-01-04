package com.multivendor.marketapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

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
import com.multivendor.marketapp.Adapters.bannerAdapter;
import com.multivendor.marketapp.Adapters.productImageAdapter;
import com.multivendor.marketapp.Adapters.productitemAdapter;
import com.multivendor.marketapp.Adapters.reviewAdapter;
import com.multivendor.marketapp.Adapters.showszAdapter;
import com.multivendor.marketapp.ApiWork.ApiWork;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Constants.api_baseurl;
import com.multivendor.marketapp.Models.AuthResponse;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.databinding.FragmentFragmentnewProductBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class fragmentnewProduct extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private com.multivendor.marketapp.Adapters.reviewAdapter reviewAdapter;
    private showszAdapter sizeadapter;
    private String selected_size;
    private String product_id;
    private FragmentFragmentnewProductBinding binding;
    private List<newProductModel.reviewResult> reviewList = new ArrayList<>();
    private List<newProductModel.sizeandquat> sizeList = new ArrayList<>();
    api_baseurl baseurl = new api_baseurl();
    private List<newProductModel.productImage> imageList = new ArrayList<>();
    private String userid;
    private String cartid;
    private com.multivendor.marketapp.Adapters.productImageAdapter imagesAdapter;
    private List<newProductModel.productCartresp> inCartList = new ArrayList<>();
    private String lat;
    private String longit;
    private String prod_name;
    private String selectedsizename;
    private String selectedprice;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public fragmentnewProduct() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static fragmentnewProduct newInstance(String param1, String param2) {
        fragmentnewProduct fragment = new fragmentnewProduct();
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
        product_id = bundle.getString("product_id");
        userid = getActivity().getSharedPreferences("userlogged", 0).getString("userid", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFragmentnewProductBinding.inflate(inflater, container, false);
        View bottom=getActivity().findViewById(R.id.bottomnav);
        bottom.setVisibility(View.GONE);
        Managefuncs();
        viewfuncs();
        getlatlong();
        loadData();
        return binding.getRoot();
    }

    private void viewfuncs() {
        binding.onbprog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imagebanner.setCurrentItem(0, true);
                binding.onbprog.getBackground().setTint(Color.parseColor("#0881E3"));
                binding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        binding.onbprog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imagebanner.setCurrentItem(1, true);
                binding.onbprog2.getBackground().setTint(Color.parseColor("#0881E3"));
                binding.onbprog.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        binding.onbprog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imagebanner.setCurrentItem(2, true);
                binding.onbprog3.getBackground().setTint(Color.parseColor("#0881E3"));
                binding.onbprog.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        binding.onbprog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imagebanner.setCurrentItem(3, true);
                binding.onbprog4.getBackground().setTint(Color.parseColor("#0881E3"));
                binding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View bottom=getActivity().findViewById(R.id.bottomnav);
                bottom.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        binding.heartofficon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to favourite
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ApiWork apiWork = retrofit.create(ApiWork.class);

                Call<AuthResponse.VerifyOtp> call = apiWork.addfavourite(userid, product_id);

                call.enqueue(new Callback<AuthResponse.VerifyOtp>() {
                    @Override
                    public void onResponse(Call<AuthResponse.VerifyOtp> call, Response<AuthResponse.VerifyOtp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        if (response.body().getCode().equals("200")) {
                            Toast.makeText(getContext(), "Added To Favourites", Toast.LENGTH_SHORT).show();
                            binding.heartofficon.setVisibility(View.INVISIBLE);
                            binding.heartonicon.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse.VerifyOtp> call, Throwable t) {
                        Log.d("Failure", t.getMessage());
                    }
                });

            }
        });

        binding.heartonicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove from favourite;
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ApiWork apiWork = retrofit.create(ApiWork.class);

                Call<AuthResponse.VerifyOtp> call = apiWork.removefavourite(userid, product_id);

                call.enqueue(new Callback<AuthResponse.VerifyOtp>() {
                    @Override
                    public void onResponse(Call<AuthResponse.VerifyOtp> call, Response<AuthResponse.VerifyOtp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        if (response.body().getCode().equals("200")) {
                            Toast.makeText(getContext(), "Removed From Favourites", Toast.LENGTH_SHORT).show();
                            binding.heartofficon.setVisibility(View.VISIBLE);
                            binding.heartonicon.setVisibility(View.INVISIBLE);

                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse.VerifyOtp> call, Throwable t) {
                        Log.d("Failure", t.getMessage());
                    }
                });

            }

        });

        binding.carticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartfragment notiFragment = new cartfragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, notiFragment);
                transaction.addToBackStack("B");
                transaction.commit();
            }
        });

        binding.addctLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<cartModel.cartResp> call = null;
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                call = logregApiInterface.add_cart(lat, longit, userid, "1", product_id,
                        selected_size, "", prod_name, selectedsizename, selectedprice, selected_size,
                        String.valueOf(Integer.valueOf(binding.qtytxt.getText().toString()) + 1), null);

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
                            cartid=resp.getResult().getCart_id();
                            binding.qtytxt.setText("1");
                            binding.qtytxt.setVisibility(View.VISIBLE);
                            binding.minusLay.setVisibility(View.VISIBLE);
                            binding.plusLay.setVisibility(View.VISIBLE);
                            binding.addctLay.setVisibility(View.INVISIBLE);
                            refreshCart();
                        } else {

                            Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                        Log.d("Failure",t.getMessage());
                    }
                });
            }
        });

        binding.plusLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<cartModel.cartResp> call = null;
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                call = logregApiInterface.update_cart(lat, longit, userid, "1", product_id,
                        selected_size, "", prod_name, selectedsizename, selectedprice, selected_size,
                        String.valueOf(Integer.valueOf(binding.qtytxt.getText().toString()) + 1), cartid);

                call.enqueue(new Callback<cartModel.cartResp>() {
                    @Override
                    public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        cartModel.cartResp resp = response.body();
                        Log.d("msg", resp.getMessage());
                        if (resp.getMessage().equals("Product updated successfully")) {
                            cartid=resp.getResult().getCart_id();
                            binding.qtytxt.setText( String.valueOf(Integer.valueOf(binding.qtytxt.getText().toString()) + 1));
                            refreshCart();
                        } else {

                            Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                        Log.d("Failure",t.getMessage());
                    }
                });
            }
        });

        binding.minusLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<cartModel.cartResp> call = null;
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                call = logregApiInterface.update_cart(lat, longit, userid, "1", product_id,
                        selected_size, "", prod_name, selectedsizename, selectedprice, selected_size,
                        String.valueOf(Integer.valueOf(binding.qtytxt.getText().toString()) - 1), cartid);

                call.enqueue(new Callback<cartModel.cartResp>() {
                    @Override
                    public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        cartModel.cartResp resp = response.body();
                        Log.d("msg", resp.getMessage());
                        if (resp.getMessage().equals("Product updated successfully")) {
                            cartid=resp.getResult().getCart_id();
                            if(Integer.valueOf(binding.qtytxt.getText().toString())>1) {
                                binding.qtytxt.setText(String.valueOf(Integer.valueOf(binding.qtytxt.getText().toString()) - 1));

                            }
                            else {
                                binding.qtytxt.setText("0");
                                binding.qtytxt.setVisibility(View.INVISIBLE);
                                binding.minusLay.setVisibility(View.INVISIBLE);
                                binding.plusLay.setVisibility(View.INVISIBLE);
                                binding.addctLay.setVisibility(View.VISIBLE);
                            }
                            refreshCart();
                        } else {

                            Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                        Log.d("Failure",t.getMessage());
                    }
                });
            }
        });
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
                    Geocoder geocoder = null;
                    if (getContext() != null) {
                        geocoder = new Geocoder(getActivity()
                                , Locale.getDefault());
                    }
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
                                Location location = task.getResult();
                                Geocoder geocoder = null;
                                if (getContext() != null) {
                                    geocoder = new Geocoder(getActivity()
                                            , Locale.getDefault());
                                }
                                if (location != null) {
                                    lat = String.valueOf(location.getLatitude());
                                    longit = String.valueOf(location.getLongitude());


                                }
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

    private void Managefuncs() {


        reviewAdapter = new reviewAdapter(getContext(), reviewList);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        llm2.setOrientation(RecyclerView.VERTICAL);
        binding.reviewrec.setLayoutManager(llm2);
        binding.reviewrec.setAdapter(reviewAdapter);

    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<newProductModel.productdetailResp> call = apiWork.getproduct_details(userid, product_id);

        call.enqueue(new Callback<newProductModel.productdetailResp>() {
            @Override
            public void onResponse(Call<newProductModel.productdetailResp> call, Response<newProductModel.productdetailResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                newProductModel.productdetailResp productdata = response.body();


                Log.d("message", productdata.getSuccess());

                if (productdata.getResult() != null) {
                    if (productdata.getResult().getProduct_images().size() > 0) {
                        imageList = productdata.getResult().getProduct_images();
                        imagesAdapter = new productImageAdapter(getActivity(), imageList);
                        binding.imagebanner.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                        binding.imagebanner.setAdapter(imagesAdapter);
                        binding.imagebanner.setCurrentItem(0);

                        if (productdata.getResult().getProduct_images().size() < 4) {
                            binding.onbprog4.setVisibility(View.INVISIBLE);
                        }
                        if (productdata.getResult().getProduct_images().size() < 3) {
                            binding.onbprog3.setVisibility(View.INVISIBLE);
                            binding.onbprog4.setVisibility(View.INVISIBLE);
                        }
                        if (productdata.getResult().getProduct_images().size() < 2) {
                            binding.onbprog2.setVisibility(View.INVISIBLE);
                            binding.onbprog3.setVisibility(View.INVISIBLE);
                            binding.onbprog4.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (productdata.getResult().getProduct_variants().size() > 0) {
                        Log.d("variant",productdata.getResult().getProduct_variants().get(0).getSize());
                        sizeList = productdata.getResult().getProduct_variants();
                        selected_size = sizeList.get(0).getVariation_id().toString();
                        selectedsizename = sizeList.get(0).getSize().toString();
                        selectedprice = sizeList.get(0).getSelling_price();
                        sizeadapter = new showszAdapter(getContext(), sizeList);
                        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
                        llm1.setOrientation(RecyclerView.HORIZONTAL);
                        binding.sizerec.setLayoutManager(llm1);
                        binding.sizerec.setAdapter(sizeadapter);
                        sizeadapter.setonbtnclickListener(new showszAdapter.onbtnclick() {
                            @Override
                            public void onCLICK(int position, String id) {
                                selected_size = id.toString();
                                selectedprice = sizeList.get(position).getSelling_price();
                                selectedsizename = sizeList.get(position).getSize();
                                binding.prodprice.setText("Rs "+selectedprice);
                                binding.cutprice.setText("Rs "+sizeList.get(position).getPrice());
                                if (inCartList.size() > 0) {
                                    for (int i = 0; i < inCartList.size(); i++) {
                                        if (inCartList.get(i).getVariant_id().equals(selected_size)) {
                                            binding.addctLay.setVisibility(View.INVISIBLE);
                                            binding.qtytxt.setVisibility(View.VISIBLE);
                                            binding.qtytxt.setText(inCartList.get(i).getQuantity().toString());
                                            binding.plusLay.setVisibility(View.VISIBLE);
                                            binding.minusLay.setVisibility(View.VISIBLE);
                                            return;
                                        } else {
                                            binding.addctLay.setVisibility(View.VISIBLE);
                                            binding.qtytxt.setText("0");
                                            binding.plusLay.setVisibility(View.INVISIBLE);
                                            binding.minusLay.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                } else {
                                    binding.addctLay.setVisibility(View.VISIBLE);
                                    binding.plusLay.setVisibility(View.INVISIBLE);
                                    binding.qtytxt.setText("0");
                                    binding.qtytxt.setVisibility(View.INVISIBLE);
                                    binding.minusLay.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }
                    if(productdata.getResult().getProduct_reviews()!=null) {
                        if (productdata.getResult().getProduct_reviews().size() > 0) {
                            reviewList = productdata.getResult().getProduct_reviews();
                            reviewAdapter.notifyDataSetChanged();
                        }
                    }
                    if (productdata.getResult().getIn_cart().size() > 0) {
                        inCartList = productdata.getResult().getIn_cart();
                    }
                    prod_name = productdata.getResult().getProduct_name();
                    cartid = productdata.getResult().getCart_id();
                    binding.productname.setText(productdata.getResult().getProduct_name().toString());
                    binding.productdesc.setText(productdata.getResult().getProduct_description().toString());
                    binding.prodprice.setText("Rs "+productdata.getResult().getProduct_price().toString());
                    binding.cutprice.setText("Rs "+productdata.getResult().getProduct_cut_price().toString());
                    binding.cutprice.setPaintFlags(binding.cutprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.disctxt.setText(productdata.getResult().getDiscount_rate());
                    if (productdata.getResult().getIn_favourites().equals("yes")) {
                        binding.heartonicon.setVisibility(View.VISIBLE);
                        binding.heartofficon.setVisibility(View.INVISIBLE);
                    } else {
                        binding.heartonicon.setVisibility(View.INVISIBLE);
                        binding.heartofficon.setVisibility(View.VISIBLE);
                    }
                    if (productdata.getResult().getIn_cart() != null) {
                        for (int i = 0; i < productdata.getResult().getIn_cart().size(); i++) {
                            if (productdata.getResult().getIn_cart().get(i).getVariant_id().equals(selected_size)) {
                                binding.addctLay.setVisibility(View.INVISIBLE);
                                binding.qtytxt.setVisibility(View.VISIBLE);
                                binding.qtytxt.setText(productdata.getResult().getIn_cart().get(i).getQuantity().toString());
                                binding.plusLay.setVisibility(View.VISIBLE);
                                binding.minusLay.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        binding.addctLay.setVisibility(View.VISIBLE);
                        binding.qtytxt.setText("0");
                        binding.qtytxt.setVisibility(View.INVISIBLE);
                        binding.plusLay.setVisibility(View.INVISIBLE);
                        binding.minusLay.setVisibility(View.INVISIBLE);
                    }

                }


            }

            @Override
            public void onFailure(Call<newProductModel.productdetailResp> call, Throwable t) {
                Log.d("errorDetail", t.getMessage().toString());
            }
        });

    }

    private void refreshCart() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<newProductModel.productdetailResp> call = apiWork.getproduct_details(userid, product_id);

        call.enqueue(new Callback<newProductModel.productdetailResp>() {
            @Override
            public void onResponse(Call<newProductModel.productdetailResp> call, Response<newProductModel.productdetailResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                newProductModel.productdetailResp productdata = response.body();


                Log.d("message", productdata.getSuccess());

                if (productdata.getResult() != null) {
                    if (productdata.getResult().getIn_cart().size() > 0) {
                        inCartList = productdata.getResult().getIn_cart();
                    }
                    cartid = productdata.getResult().getCart_id();
                    if (productdata.getResult().getIn_cart() != null) {
                        for (int i = 0; i < productdata.getResult().getIn_cart().size(); i++) {
                            if (productdata.getResult().getIn_cart().get(i).getVariant_id().equals(selected_size)) {
                                binding.addctLay.setVisibility(View.INVISIBLE);
                                binding.qtytxt.setVisibility(View.VISIBLE);
                                binding.qtytxt.setText(productdata.getResult().getIn_cart().get(i).getQuantity().toString());
                                binding.plusLay.setVisibility(View.VISIBLE);
                                binding.minusLay.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        binding.addctLay.setVisibility(View.VISIBLE);
                        binding.qtytxt.setText("0");
                        binding.qtytxt.setVisibility(View.INVISIBLE);
                        binding.plusLay.setVisibility(View.INVISIBLE);
                        binding.minusLay.setVisibility(View.INVISIBLE);
                    }

                }


            }

            @Override
            public void onFailure(Call<newProductModel.productdetailResp> call, Throwable t) {
                Log.d("errorDetail", t.getMessage().toString());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View bottom=getActivity().findViewById(R.id.bottomnav);
        bottom.setVisibility(View.VISIBLE);
    }
}