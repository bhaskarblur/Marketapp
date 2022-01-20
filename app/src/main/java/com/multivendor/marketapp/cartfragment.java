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
import android.os.Bundle;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.multivendor.marketapp.Adapters.cartproditemAdapter;
import com.multivendor.marketapp.Adapters.productitemAdapter;
import com.multivendor.marketapp.ApiWork.ApiWork;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Constants.api_baseurl;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.ViewModel.catalogViewModel;
import com.multivendor.marketapp.databinding.FragmentCartfragmentBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cartfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cartfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentCartfragmentBinding cfbinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private catalogViewModel catViewModel;
    private com.multivendor.marketapp.Adapters.cartproditemAdapter productitemAdapter;
    private List<cartModel.cartqtyandsize> qtysizelist = new ArrayList<>();
    private Boolean shortbol = false;
    private Boolean sameshop = false;
    private String lat;
    private String longit;
    private String storeid;
    private String cartid;
    private String userid_id;
    private Boolean promo_applied=false;
    private List<newProductModel.ListProductresp> cartitems = new ArrayList<>();
    String shopnamerec = new String();
    private String username;
    private String useraddress;
    private String usernumber;
    private Integer items;
    private Integer amount;
    private Boolean gotdata = false;
    api_baseurl baseurl=new api_baseurl();
    private FusedLocationProviderClient fusedLocationProviderClient;

    public cartfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cartfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static cartfragment newInstance(String param1, String param2) {
        cartfragment fragment = new cartfragment();
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        String cartshop = sharedPreferences.getString("cartshop", "");
        userid_id=sharedPreferences.getString("userid","");
        catViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(catalogViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cfbinding = FragmentCartfragmentBinding.inflate(inflater, container, false);
        getlatlong();

        viewfuncs();
        loaduserdetails();

        return cfbinding.getRoot();
    }

    private void viewfuncs() {

        cfbinding.picklocat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlatlong();
            }
        });
        cfbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        cfbinding.placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), paymentactivity.class);
                intent.putExtra("store_id", storeid);
                intent.putExtra("cart_id", cartid);
                intent.putExtra("amount", cfbinding.cartgrandtotal.getText().toString().substring(1).trim());
                intent.putExtra("username", username);
                intent.putExtra("usernumber", usernumber);
                intent.putExtra("address", useraddress);
                intent.putExtra("deliveryinstr", cfbinding.cartdesc.getText().toString());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        cfbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getParentFragmentManager().popBackStack();
            }
        });

        cfbinding.userinfochange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openchangeDialog();
            }
        });
        cfbinding.useraddresschange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openchangeDialog();
            }
        });

        cfbinding.couponApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cfbinding.couponVox.getText().toString().isEmpty() &&
                        cfbinding.couponVox.getText().toString() != null) {
                    String userid=getActivity().getSharedPreferences("userlogged",0)
                            .getString("userid","");
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    ApiWork apiWork = retrofit.create(ApiWork.class);
                    Call<newProductModel.couponResp> call = apiWork.applycoupon(userid,cfbinding
                    .couponVox.getText().toString(),cartid);
                    call.enqueue(new Callback<newProductModel.couponResp>() {
                        @Override
                        public void onResponse(Call<newProductModel.couponResp> call, Response<newProductModel.couponResp> response) {
                            if (!response.isSuccessful()) {
                                Log.d("error", String.valueOf(response.code()));
                                return;
                            }

                            newProductModel.couponResp resp = response.body();

                            if (resp.getMessage().equals("correct")) {
                                Toast.makeText(getContext(), "Coupon Code Applied!", Toast.LENGTH_SHORT).show();
                                cfbinding.cartgrandtotal.setText("₹ "+resp.getTotal_price());
                                cfbinding.discountapplied.setText("- ₹"+resp.getDiscount());
                                cfbinding.discountapplied.setVisibility(View.VISIBLE);
                                cfbinding.textView23.setVisibility(View.VISIBLE);
                                filldetails();
                                promo_applied=true;
                            }
                            else {
                                Toast.makeText(getContext(), "Incorrect Coupon Code!", Toast.LENGTH_SHORT).show();
                                cfbinding.discountapplied.setVisibility(View.GONE);
                                cfbinding.textView23.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<newProductModel.couponResp> call, Throwable t) {
                            Log.d("Failure",t.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "Please enter coupon code.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openchangeDialog() {
        cfbinding.newnametxt.setText(username);
        cfbinding.newaddrtxt.setText(useraddress);
        cfbinding.newnumtxt.setText(usernumber);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_down);
        cfbinding.detchangeDialog.setAnimation(animation);
        cfbinding.detchangeDialog.setVisibility(View.VISIBLE);

        cfbinding.picklocat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        cfbinding.savelocat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                cfbinding.detchangeDialog.setAnimation(animation);
                cfbinding.detchangeDialog.setVisibility(View.INVISIBLE);
                username = cfbinding.newnametxt.getText().toString();
                useraddress = cfbinding.newaddrtxt.getText().toString();
                usernumber = cfbinding.newnumtxt.getText().toString();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getlatlong() {
        String usercity=getActivity().getSharedPreferences("userlogged",0).getString("usercity","");
        catViewModel.getlocation(userid_id,lat,longit,"Ludhiana");
        if(catViewModel.getnbyshopModel()!=null) {
            catViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                @Override
                public void onChanged(newProductModel.homeprodResult productitemModels) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (productitemModels.getAll_products().size() > 0) {
                                Log.d("hellomf", "hellomf");
                                checkcartexists();
                                LoadCart();
                            } else {
                                cfbinding.progressBar7.setVisibility(View.INVISIBLE);
                                cfbinding.emptycarttext.setVisibility(View.VISIBLE);
                            }
                        }
                    }, 2000);
                }
            });
        }
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
                        loadmat(Double.parseDouble(lat), Double.parseDouble(longit), "Your Location");
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {

                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude()
                                    , location.getLongitude(), 1);

                            cfbinding.newaddrtxt.setText(addresses.get(0).getAddressLine(0));
//
                        } catch (IOException e) {
                            e.printStackTrace();
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
                                loadmat(Double.parseDouble(lat), Double.parseDouble(longit), "Your Location");
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                try {

                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude()
                                            , location.getLongitude(), 1);

                                    cfbinding.newaddrtxt.setText(addresses.get(0).getAddressLine(0));
//                                    catViewModel.getlocation(userid_id,lat,longit,addresses.get(0).getLocality().toString());
//                                    if(catViewModel.getnbyshopModel()!=null) {
//                                        catViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
//                                            @Override
//                                            public void onChanged(newProductModel.homeprodResult productitemModels) {
//                                                Log.d("hello1", "hello1");
//                                                new Handler().postDelayed(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        if (productitemModels.getAll_products().size() > 0) {
//                                                            Log.d("hello", "hello");
//                                                            checkcartexists();
//                                                            LoadCart();
//                                                        } else {
//                                                            cfbinding.progressBar7.setVisibility(View.INVISIBLE);
//                                                            cfbinding.emptycarttext.setVisibility(View.VISIBLE);
//                                                        }
//                                                    }
//                                                }, 2000);
//                                            }
//                                        });
//                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
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

    private void loaduserdetails() {
        SharedPreferences shpref = getActivity().getSharedPreferences("userlogged", 0);
        String userid = shpref.getString("userid", "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
        Call<loginresResponse.login> call = logregApiInterface.getprofile(userid);
        call.enqueue(new Callback<loginresResponse.login>() {
            @Override
            public void onResponse(Call<loginresResponse.login> call, Response<loginresResponse.login> response) {
                if (!response.isSuccessful()) {
                    Log.d("error", String.valueOf(response.code()));
                    return;
                }

                loginresResponse.login resp = response.body();

                if (resp.getmessage().equals("User profile.!")) {
                    username = resp.getResult().getName();
                    useraddress = resp.getResult().getAddress();
                    usernumber = resp.getResult().getPhone();
                    filldetails();

                }
            }

            @Override
            public void onFailure(Call<loginresResponse.login> call, Throwable t) {

            }
        });
    }

    private void filldetails() {
        cfbinding.usermobileTxt.setText(usernumber);
        cfbinding.usernameTxt.setText(username);
        cfbinding.useraddressTxt.setText(useraddress);
    }

    public void LoadCart() {
        SharedPreferences shpref = getActivity().getSharedPreferences("userlogged", 0);
        String userid = shpref.getString("userid", "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
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
                Log.d("msg",response.raw().toString());
                if (cartResp.getResult() != null) {
                    if (cartResp.getResult().getProducts() != null) {

                        SharedPreferences.Editor editor = shpref.edit();
                        editor.putString("cartshop", cartResp.getResult().getStore());
                        editor.apply();
                        if (cartResp.getResult().getProducts().size() > 0) {
                            if (cartResp.getResult().getProducts() != null) {
                                qtysizelist.clear();
                                //checksamestore();
                                cfbinding.emptycarttext.setVisibility(View.INVISIBLE);
                                cfbinding.progressBar7.setVisibility(View.INVISIBLE);
                                cfbinding.cartfullnestedlay.setVisibility(View.VISIBLE);
                                cfbinding.carttotalprice.setText("₹ " + cartResp.getResult().getSubtotal()+"("+String.valueOf(cartResp.getResult().getProducts().size())+" Items)");
                                cfbinding.cartdeliverycharge.setText("₹ " + cartResp.getResult().getShipping_charge());
                                cfbinding.cartgrandtotal.setText("₹ " + cartResp.getResult().getTotal_price());
                                if(promo_applied.equals(true)) {
                                    cfbinding.discountapplied.setVisibility(View.GONE);
                                    cfbinding.textView23.setVisibility(View.GONE);
                                }
                                amount = Integer.valueOf(cartResp.getResult().getSubtotal());
                                for (int i = 0; i < cartResp.getResult().getProducts().size(); i++) {


                                    qtysizelist.add(i, new cartModel.cartqtyandsize(cartResp.getResult().getProducts()
                                            .get(i).getProduct_id(), cartResp.getResult().getProducts()
                                            .get(i).getProduct_name(), cartResp.getResult().getProducts()
                                            .get(i).getVariant_id(), cartResp.getResult().getProducts()
                                            .get(i).getSize(), cartResp.getResult().getProducts()
                                            .get(i).getQty()));

                                }
                                loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                                //  productitemAdapter.notifyDataSetChanged();

                            } else {
                                cfbinding.progressBar7.setVisibility(View.INVISIBLE);
                                cfbinding.emptycarttext.setText(View.VISIBLE);
                                cfbinding.cartfullnestedlay.setVisibility(View.INVISIBLE);
                                loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                                //  productitemAdapter.notifyDataSetChanged();
                            }
                        } else {
                            cfbinding.progressBar7.setVisibility(View.INVISIBLE);
                            cfbinding.emptycarttext.setVisibility(View.VISIBLE);
                            cfbinding.cartfullnestedlay.setVisibility(View.INVISIBLE);
                            loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                            // productitemAdapter.notifyDataSetChanged();

                        }
                    } else {
                        cfbinding.progressBar7.setVisibility(View.INVISIBLE);
                        cfbinding.emptycarttext.setVisibility(View.VISIBLE);
                        cfbinding.cartfullnestedlay.setVisibility(View.INVISIBLE);
                        loaditems(cartResp.getResult().getStore(), cartResp.getResult().getCart_id());
                        // productitemAdapter.notifyDataSetChanged();
                    }
                } else {
                    loaditems(null, null);
                    cfbinding.progressBar7.setVisibility(View.INVISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                    cfbinding.emptycarttext.setVisibility(View.VISIBLE);
                    cfbinding.cartfullnestedlay.setVisibility(View.INVISIBLE);
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
        storeid = store_id;
        cartid = cart_id;
        cartitems.clear();
        for (cartModel.cartqtyandsize qty : qtysizelist) {
            for (int i=0;i<catViewModel.getnbyshopModel().getValue().getAll_products().size();i++) {
                if (catViewModel.getnbyshopModel().getValue().getAll_products().get(i).
                        getProduct_id().equals(qty.getProduct_id())) {
                    cartitems.add(catViewModel.getnbyshopModel().getValue().getAll_products().get(i));
                }
            }
        }
        productitemAdapter = new cartproditemAdapter(getContext(), cartitems,
                qtysizelist, storeid);
        productitemAdapter.setStore_id(store_id);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        cfbinding.cartitemsrec.setLayoutManager(llm);
        cfbinding.cartitemsrec.setAdapter(productitemAdapter);
        cfbinding.cartitemsrec.setHasFixedSize(true);
        cfbinding.cartitemsrec.setItemViewCacheSize(50);
        cfbinding.cartitemsrec.setDrawingCacheEnabled(true);
        cfbinding.cartitemsrec.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");

        productitemAdapter.setonproductsClick(new cartproditemAdapter.onproductsClick() {
            @Override
            public void onSizeClick(int position, String size_name) {
                // Toast.makeText(getContext(), size_name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onaddClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        amount = amount + Integer.valueOf(productitemAdapter.productmodel.get(position).getProduct_variants().get(sizepos)
                                .getSelling_price());

                        cfbinding.carttotalprice.setText("₹ " + String.valueOf(amount));

                    }
                }, 50);
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();

                Call<cartModel.cartResp> call = null;
                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                call = logregApiInterface.add_cart(lat,longit, userid,
                        prod_id, productitemAdapter.productmodel.get(position).getProduct_variants().get(sizepos).getVariation_id(), qty
                ,cart_id);


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

            @Override
            public void onplusClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        amount = amount + Integer.valueOf(productitemAdapter.productmodel.get(position).getProduct_variants().get(sizepos)
                                .getSelling_price());

                        cfbinding.carttotalprice.setText("₹ " + String.valueOf(amount));

                    }
                }, 50);
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<cartModel.cartResp> call = logregApiInterface.update_cart(null, userid,
                        prod_id, productitemAdapter.productmodel.get(position).getProduct_variants().get(sizepos).getVariation_id(), qty,cart_id);

                call.enqueue(new Callback<cartModel.cartResp>() {
                    @Override
                    public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("error code", String.valueOf(response.code()));
                            return;
                        }

                        cartModel.cartResp resp = response.body();
                        Log.d("msg", resp.getMessage().toString());
                        if (resp.getMessage().equals("Product added successfully")) {
                            checkcartexists();
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
            }

            @Override
            public void onminusClick(int position, int sizepos, String prod_id, String size_id, String sizename, String qty) {
                if (Integer.parseInt(qty) > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            amount = amount - Integer.valueOf(productitemAdapter.productmodel.get(position).getProduct_variants().get(sizepos)
                                    .getSelling_price());

                            cfbinding.carttotalprice.setText("₹ " + String.valueOf(amount));

                        }
                    }, 50);
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                    Call<cartModel.cartResp> call = logregApiInterface.update_cart(null, userid,
                            prod_id, productitemAdapter.productmodel.get(position).getProduct_variants().get(sizepos).getVariation_id(), qty,cart_id);

                    call.enqueue(new Callback<cartModel.cartResp>() {
                        @Override
                        public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                            if (!response.isSuccessful()) {
                                Log.d("error code", String.valueOf(response.code()));
                                return;
                            }

                            cartModel.cartResp resp = response.body();
                            Log.d("msg", resp.getMessage().toString());
                            if (resp.getMessage().equals("Product added successfully")) {
                                checkcartexists();
                                LoadCart();
                            } else {

                                Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                            // Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            amount = amount - Integer.valueOf(productitemAdapter.productmodel.get(position).getProduct_variants().get(sizepos)
                                    .getSelling_price());

                            cfbinding.carttotalprice.setText("₹ " + String.valueOf(amount));
                        }
                    }, 50);
                    api_baseurl baseurl=new api_baseurl();
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                    Call<cartModel.cartResp> call = logregApiInterface.remove_product(null, userid,
                            prod_id, productitemAdapter.productmodel.get(position).getProduct_variants().get(sizepos).getVariation_id(), qty,cart_id);


                    call.enqueue(new Callback<cartModel.cartResp>() {
                        @Override
                        public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                            if (!response.isSuccessful()) {
                                Log.d("error code", String.valueOf(response.code()));
                                return;
                            }

                            cartModel.cartResp resp = response.body();
                            Log.d("msg", resp.getMessage().toString());
                            if (resp.getMessage().equals("Product added successfully")) {
                                checkcartexists();
                                LoadCart();
                            } else {

                                Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public void checkcartexists() {
        SharedPreferences shpref = getActivity().getSharedPreferences("userlogged", 0);
        String userid = shpref.getString("userid", "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
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

    private void loadmat(double sellat, double sellongit, String curlocat) {
//        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.google_map5);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(supportMapFragment!=null) {
//                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(@NonNull GoogleMap googleMap) {
//                            final LatLng[] latLng = {new LatLng(sellat, sellongit)};
//                            MarkerOptions markerOptions = new MarkerOptions().position(latLng[0])
//                                    .title("Current Location").draggable(true);
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[0], 18));
//                            googleMap.addMarker(markerOptions).setDraggable(true);
//
////                        psbinding.recentrebtn.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[0], 18));
////                            }
////                        });
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//                                        @Override
//                                        public void onMarkerDragStart(@NonNull Marker marker) {
//
//                                        }
//
//                                        @Override
//                                        public void onMarkerDrag(@NonNull Marker marker) {
//
//                                        }
//
//                                        @Override
//                                        public void onMarkerDragEnd(@NonNull Marker marker) {
//                                            latLng[0] = marker.getPosition();
//                                            LatLng new_latlng = marker.getPosition();
//                                            lat = String.valueOf(new_latlng.latitude);
//                                            longit = String.valueOf(new_latlng.longitude);
//
//
//                                            Geocoder geocoder = new Geocoder(getContext()
//                                                    , Locale.getDefault());
//                                            try {
//                                                List<Address> addresses = geocoder.getFromLocation(new_latlng.latitude, new_latlng.longitude, 1);
//                                                cfbinding.newaddrtxt.setText(addresses.get(0).getAddressLine(0));
//
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    });
//                                }
//                            }, 1000);
//
//                        }
//                    });
//                }
//            }
//        }, 100);

        cfbinding.savelocat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfbinding.useraddressTxt.setText(cfbinding.newaddrtxt.getText().toString());
                cfbinding.usermobileTxt.setText(cfbinding.newnumtxt.getText().toString());
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                cfbinding.detchangeDialog.setAnimation(animation);
                cfbinding.detchangeDialog.setVisibility(View.INVISIBLE);
                username = cfbinding.newnametxt.getText().toString();
                useraddress = cfbinding.newaddrtxt.getText().toString();
                usernumber = cfbinding.newnumtxt.getText().toString();
                cfbinding.useraddressTxt.setText(useraddress);

            }
        });
    }
}