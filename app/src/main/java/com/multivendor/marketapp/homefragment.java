package com.multivendor.marketapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.*;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.multivendor.marketapp.Adapters.bannerAdapter;
import com.multivendor.marketapp.Adapters.categoriesAdapter;
import com.multivendor.marketapp.Adapters.nbyshopAdapter;
import com.multivendor.marketapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketapp.Models.bannermodel;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.ViewModel.homefragViewModel;
import com.multivendor.marketapp.databinding.FragmentHomefragmentBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class homefragment extends Fragment implements LocationListener {


    private FragmentHomefragmentBinding hmbinding;
    private com.multivendor.marketapp.ViewModel.homefragViewModel hmViewModel;
    private static final String ARG_PARAM1 = "param1";
    private com.multivendor.marketapp.Adapters.bannerAdapter bannerAdapter;
    private com.multivendor.marketapp.Adapters.bannerAdapter bannerAdapter3;
    private com.multivendor.marketapp.Adapters.bannerAdapter bannerAdapter2;
    private static final String ARG_PARAM2 = "param2";
    private Integer pos = 0;
    private SharedPreferences sharedPreferences;
    private String mParam1;
    private String mParam2;
    private String lat;
    private String longit;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;
    private List<bannermodel.singleBannerresp> bannerlist1 = new ArrayList<>();
    private List<bannermodel.singleBannerresp> bannerlist2 = new ArrayList<>();
    private List<bannermodel.singleBannerresp> bannerlist3 = new ArrayList<>();
    private com.multivendor.marketapp.Adapters.nbyshopAdapter nbadapter;
    private com.multivendor.marketapp.Adapters.nbyshopAdapter nbadapter1;
    private com.multivendor.marketapp.Adapters.nbyshopAdapter nbadapter2;
    private com.multivendor.marketapp.Adapters.nbyshopAdapter nbadapter3;
    private Boolean dataloaded=false;
    private String userid;
    private String cityname;
    public homefragment() {

    }


    public static homefragment newInstance(String param1, String param2) {
        homefragment fragment = new homefragment();
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
        hmbinding = FragmentHomefragmentBinding.inflate(inflater, container, false);
        pos = 0;
        sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        userid = sharedPreferences.getString("userid", "");
        String username = sharedPreferences.getString("username", "");
        String useraddr = sharedPreferences.getString("useraddress", "");
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (useraddr != null) {
            //      hmbinding.locattext.setText(useraddr);
        } else {
            //     hmbinding.locattext.setText("No Address");
        }
//        if (username != null) {
//            String sourceString = "<b>" + username + "</b> ";
//            hmbinding.usertxt.setText("Hello " + Html.fromHtml(sourceString) + "!");
//        } else {
//            hmbinding.usertxt.setText("Hello User!");
//        }


        hmViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(homefragViewModel.class);
        hmViewModel.initwork();

        hmbinding.homefragscroll.setVisibility(View.INVISIBLE);
        hmbinding.progressBar2.setVisibility(View.VISIBLE);
        hmbinding.retrybtn.setVisibility(View.INVISIBLE);
        hmbinding.rettxt.setVisibility(View.INVISIBLE);
        hmViewModel.getBannerModel().observe(getActivity(), new Observer<bannermodel.banneresult>() {
            @Override
            public void onChanged(bannermodel.banneresult bannermodels) {
                if (bannermodels.getBanner1list().size() > 0) {
                    bannerlist1.clear();
                    bannerlist1 = bannermodels.getBanner1list();
                    bannerAdapter = new bannerAdapter(getActivity(), bannerlist1);
                    hmbinding.bannerrv.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    hmbinding.bannerrv.setAdapter(bannerAdapter);
                    hmbinding.bannerrv.setCurrentItem(0);

                }
                if (bannermodels.getBanner2list().size() > 0) {
                    bannerlist2.clear();
                    bannerlist2 = bannermodels.getBanner2list();
                    bannerAdapter2 = new bannerAdapter(getActivity(), bannerlist2);
                    hmbinding.bannerrv2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    hmbinding.bannerrv2.setAdapter(bannerAdapter2);
                    hmbinding.bannerrv2.setCurrentItem(0);
                    rotatebanner2();
                }

                if (bannermodels.getBanner3list()!=null && bannermodels.getBanner3list().size() > 0) {
                    bannerlist3.clear();
                    bannerlist3 = bannermodels.getBanner3list();
                    bannerAdapter3 = new bannerAdapter(getActivity(), bannerlist3);
                    hmbinding.bannerrv3.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    hmbinding.bannerrv3.setAdapter(bannerAdapter3);
                    hmbinding.bannerrv3.setCurrentItem(0);
                    rotatebanner3();
                }
            }
        });

        getlatlong();
        viewfunction();
        handleMenu();
        return hmbinding.getRoot();
    }

    private void handleMenu() {
        hmbinding.drawerlayout.closeDrawer(GravityCompat.START);

        hmbinding.menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.drawerlayout.openDrawer(GravityCompat.START);
            }
        });

        View view1= hmbinding.navMenu.getHeaderView(0);
        TextView nametxt=view1.findViewById(R.id.username2);
        ImageView userimage=view1.findViewById(R.id.user_image);
        TextView numbertxt=view1.findViewById(R.id.usernumber);
        TextView addrtxt=view1.findViewById(R.id.useraddress2);
        View edittxt=view1.findViewById(R.id.editopen);
        MenuItem item = hmbinding.navMenu.getMenu().findItem(R.id.menu_logout);
        MenuItem item1 = hmbinding.navMenu.getMenu().findItem(R.id.menu_myord);
        MenuItem item2 = hmbinding.navMenu.getMenu().findItem(R.id.menu_termcond);
        MenuItem item3 = hmbinding.navMenu.getMenu().findItem(R.id.menu_privpol);
        MenuItem item4 = hmbinding.navMenu.getMenu().findItem(R.id.menu_custserv);
        MenuItem item5 = hmbinding.navMenu.getMenu().findItem(R.id.menu_share);
        SpannableString s1 = new SpannableString("My Orders");
        SpannableString s2 = new SpannableString("Terms And Conditions");
        SpannableString s3 = new SpannableString("Privacy Policy");
        SpannableString s4 = new SpannableString("Customer Service");
        SpannableString s5 = new SpannableString("Share And Earn");
        SpannableString s = new SpannableString("Log Out");
        item.setTitle(s);
        item1.setTitle(s1);
        item2.setTitle(s2);
        item3.setTitle(s3);
        item4.setTitle(s4);
        item5.setTitle(s5);

        s.setSpan(new ForegroundColorSpan(Color.parseColor("#F24747")), 0, s.length(), 0);
        s1.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, s1.length(), 0);
        s2.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, s2.length(), 0);
        s3.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, s3.length(), 0);
        s4.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, s4.length(), 0);
        s5.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, s5.length(), 0);
        edittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),profilesettingsact.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });

        hmbinding.navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.menu_myord:
                        orderfragment homeFragment=new orderfragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.mainfragment, homeFragment);
                        transaction.addToBackStack("A");
                        transaction.setCustomAnimations(R.anim.fade_fast_2, R.anim.fade);
                        transaction.commit();
                        break;
                    case R.id.menu_termcond:
                        Uri uri = Uri.parse("http://marketapp.co.in/conditions.html"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                    case R.id.menu_privpol:
                        Uri uri1 = Uri.parse("http://marketapp.co.in/policy.html"); // missing 'http://' will cause crashed
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                        startActivity(intent1);
                        break;
                    case R.id.menu_custserv:
                        String url = "https://api.whatsapp.com/send?phone=8299189690";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case R.id.menu_share:
                        Intent sharemsg = new Intent(Intent.ACTION_VIEW);
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Market App");
                            String shareMessage= "Install Market App Now.\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.menu_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("" +
                                "Are you sure, you want to log out?").setTitle("Log Out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                startActivity(new Intent(getActivity(), Loginact.class));
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                }
                return false;
            }
        });
        if(!sharedPreferences.getString("userid","").toString().isEmpty()) {
            String imgurl = sharedPreferences.getString("userimage", "data");
            String name = sharedPreferences.getString("username", "");
            String number = sharedPreferences.getString("usermobile", "");
            String address = sharedPreferences.getString("useraddress", "");

            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("image", imgurl);
                    zoom_imageDialog zoom_imageDialog1 = new zoom_imageDialog();
                    zoom_imageDialog1.setArguments(bundle);
                    zoom_imageDialog1.show(getParentFragmentManager(), "zoom_imagDialog1");
                }

            });
            if (imgurl != null) {
                final int radius = 50;
                final int margin = 50;
                final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                if (imgurl != null && !imgurl.equals("data")) {
                    Picasso.get().load(imgurl).transform(new CropCircleTransformation()).into(userimage);
                } else if (imgurl.equals("data")) {
                    Picasso.get().load(R.drawable.sampleuserimg1).into(userimage);
                }
            }

            if (name != null) {

                nametxt.setText(name);
            }
            if (address != null) {
                addrtxt.setText(address);

            } else {
                addrtxt.setText("No Address");
            }

            if (number != null) {
                numbertxt.setText(number);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getlatlong() {
        String usercity=getActivity().getSharedPreferences("userlogged",0).getString("usercity","");
        hmViewModel.getlocation(userid,lat, longit,usercity);
        if(hmViewModel.getnbyshopModel()!=null) {
            hmViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                @Override
                public void onChanged(newProductModel.homeprodResult nbyshopsModels) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadcatrec();
                            loadnearbyshoprec();
                            dataloaded=true;
                            if (nbyshopsModels.getDeal_day_products().size() > 0) {
                                nbadapter.notifyDataSetChanged();
                                hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                hmbinding.noshoptxt.setVisibility(View.INVISIBLE);
                            }
                            if (nbyshopsModels.getTop_sell_products().size() > 0) {
                                nbadapter1.notifyDataSetChanged();
                                hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                hmbinding.noshoptxt.setVisibility(View.INVISIBLE);
                            }

                            if (nbyshopsModels.getBest_deal_products().size() > 0) {
                                nbadapter2.notifyDataSetChanged();
                                hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                hmbinding.noshoptxt.setVisibility(View.INVISIBLE);
                            }
                            if(nbyshopsModels.getAll_products().size() > 0) {
                                nbadapter3.notifyDataSetChanged();
                                hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                hmbinding.noshoptxt.setVisibility(View.INVISIBLE);
                            }
                            else {
                                hmbinding.homefragscroll.setVisibility(View.INVISIBLE);
                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                hmbinding.noshoptxt.setVisibility(View.VISIBLE);
                            }
                            if(nbyshopsModels.getCart_items()!=null &&!nbyshopsModels.getCart_items().equals("0") ) {
                                hmbinding.itemlay.setVisibility(View.VISIBLE);
                                hmbinding.cartitemtxt.setText(nbyshopsModels.getCart_items());
                            }
                            else {
                                hmbinding.itemlay.setVisibility(View.INVISIBLE);
                            }

                        }
                    }, 1500);
                }
            });

        }
//        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dataloaded.equals(false)) {
                    dataloaded=true;
                    hmbinding.homefragscroll.setVisibility(View.INVISIBLE);
                    hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                    hmbinding.retrybtn.setVisibility(View.VISIBLE);
                    hmbinding.rettxt.setVisibility(View.VISIBLE);

                }
            }
        },5000);
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
                                if (dataloaded.equals(false)) {
                                    Location location = task.getResult();
                                    if (location != null) {
                                        dataloaded = true;
                                        lat = String.valueOf(location.getLatitude());
                                        longit = String.valueOf(location.getLongitude());

                                        Geocoder geocoder = null;
                                        if(getContext()!=null) {
                                            geocoder = new Geocoder(getActivity()
                                                    , Locale.getDefault());
                                        }
                                        try {
                                            if(geocoder!=null) {
                                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                                hmViewModel.getlocation(userid,lat, longit,addresses.get(0).getLocality().toString());
                                                cityname=addresses.get(0).getLocality().toString();
//                                                hmbinding.locattext.setText(addresses.get(0).getLocality());
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if(hmViewModel.getnbyshopModel()!=null) {
                                            hmViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                                                @Override
                                                public void onChanged(newProductModel.homeprodResult nbyshopsModels) {
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            loadcatrec();
                                                            loadnearbyshoprec();
                                                            if (nbyshopsModels.getDeal_day_products().size() > 0) {
                                                                nbadapter.notifyDataSetChanged();
                                                                hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                                                hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                                                hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                                            }
                                                            if (nbyshopsModels.getTop_sell_products().size() > 0) {
                                                                nbadapter1.notifyDataSetChanged();
                                                                hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                                                hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                                                hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                                            }
                                                            if (nbyshopsModels.getBest_deal_products().size() > 0) {
                                                                nbadapter2.notifyDataSetChanged();
                                                                hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                                                hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                                                hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                                                hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                                            }

                                                            if(nbyshopsModels.getCart_items()!=null &&!nbyshopsModels.getCart_items().equals("0") ) {
                                                                hmbinding.itemlay.setVisibility(View.VISIBLE);
                                                                hmbinding.cartitemtxt.setText(nbyshopsModels.getCart_items());
                                                            }
                                                            else {
                                                                hmbinding.itemlay.setVisibility(View.INVISIBLE);
                                                            }

                                                        }
                                                    }, 1500);
                                                }
                                            });

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
                                                Geocoder geocoder = null;
                                                if(getContext()!=null) {
                                                     geocoder = new Geocoder(getActivity()
                                                            , Locale.getDefault());
                                                }
                                                try {
                                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                                    hmViewModel.getlocation(userid,lat, longit,addresses.get(0).getLocality().toString());

//                                                    hmbinding.locattext.setText(addresses.get(0).getLocality());
                                                    Toast.makeText(getContext(), "Hi", Toast.LENGTH_SHORT).show();
                                                    cityname=addresses.get(0).getLocality().toString();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                hmViewModel.getnbyshopModel().observe(getActivity(), new Observer<newProductModel.homeprodResult>() {
                                                    @Override
                                                    public void onChanged(newProductModel.homeprodResult nbyshopsModels) {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                loadcatrec();
                                                                loadnearbyshoprec();
                                                                if (nbyshopsModels.getDeal_day_products().size() > 0) {
                                                                    nbadapter.notifyDataSetChanged();
                                                                    hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                                                    hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                                                    hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                                                    hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                                                }
                                                                if (nbyshopsModels.getTop_sell_products().size() > 0) {
                                                                    nbadapter1.notifyDataSetChanged();
                                                                    hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                                                    hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                                                    hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                                                    hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                                                }
                                                                if (nbyshopsModels.getBest_deal_products().size() > 0) {
                                                                    nbadapter2.notifyDataSetChanged();
                                                                    hmbinding.homefragscroll.setVisibility(View.VISIBLE);
                                                                    hmbinding.progressBar2.setVisibility(View.INVISIBLE);
                                                                    hmbinding.retrybtn.setVisibility(View.INVISIBLE);
                                                                    hmbinding.rettxt.setVisibility(View.INVISIBLE);
                                                                }
                                                                if(nbyshopsModels.getCart_items()!=null &&!nbyshopsModels.getCart_items().equals("0") ) {
                                                                    hmbinding.itemlay.setVisibility(View.VISIBLE);
                                                                    hmbinding.cartitemtxt.setText(nbyshopsModels.getCart_items());
                                                                }
                                                                else {
                                                                    hmbinding.itemlay.setVisibility(View.INVISIBLE);
                                                                }
                                                                loadnearbyshoprec();
                                                            }

                                                        }, 1500);
                                                    }
                                                });

                                            }
                                        };
                                    }
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

    @SuppressLint("MissingPermission")
    private void GetcurrLocation() {
        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = String.valueOf(location.getLatitude());
                longit = String.valueOf(location.getLongitude());

            }
        });

    }

    private void viewfunction() {

        hmbinding.swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getViewModelStore().clear();
                hmbinding.swipelayout.setRefreshing(false);
                homefragment homeFragment=new homefragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainfragment, homeFragment);
                transaction.commit();

            }
        });

//        makequickfragment notiFragment = new makequickfragment();
//        Bundle bundle=new Bundle();
//        bundle.putString("product_id","1");
//        notiFragment.setArguments(bundle);
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
//        transaction.replace(R.id.mainfragment, notiFragment);
//        transaction.addToBackStack("A");
//        transaction.commit();

       // startActivity(new Intent(getContext(),paymentactivity.class));

        hmbinding.bagicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartfragment notiFragment = new cartfragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                transaction.replace(R.id.mainfragment, notiFragment);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });
        hmbinding.quickordicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("storeid","1");
                makequickfragment catfrag = new makequickfragment();
                catfrag.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left);
                transaction.replace(R.id.mainfragment, catfrag);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });
        hmbinding.retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               homefragment notiFragment = new homefragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                transaction.replace(R.id.mainfragment, notiFragment);
                transaction.commit();
            }
        });
        hmbinding.notiicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiFragment notiFragment = new notiFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, notiFragment);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        hmbinding.searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("lat", lat);
                bundle.putString("long", longit);
                bundle.putString("city_name",cityname);
                searchFragment searchFragment = new searchFragment();
                searchFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, searchFragment);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        hmbinding.catseeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.multivendor.marketapp.categoriesFragment catFragment = new categoriesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("selectedCategory", "0");
                bundle.putString("selectedCategoryname",hmViewModel.getcatmodel().getValue().get(0).getName());
                catFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, catFragment);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        hmbinding.nearbshopsall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.multivendor.marketapp.categoriesFragment catFragment = new categoriesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("selectedCategory", "0");
                bundle.putString("selectedCategoryname",hmViewModel.getcatmodel().getValue().get(0).getName());
                catFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, catFragment);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        hmbinding.onbprog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.bannerrv.setCurrentItem(0, true);
                hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#000000"));
                hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        hmbinding.onbprog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.bannerrv.setCurrentItem(1, true);
                hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#000000"));
                hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
                hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        hmbinding.onbprog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.bannerrv.setCurrentItem(2, true);
                hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#000000"));
                hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
                hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        hmbinding.onbprog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.bannerrv.setCurrentItem(3, true);
                hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#000000"));
                hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        hmbinding.bannerrv.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#000000"));
                    hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                } else if (position == 1) {
                    hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#000000"));
                    hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                } else if (position == 2) {
                    hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#000000"));
                    hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                } else if (position == 3) {
                    hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#000000"));
                    hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                    hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    private void loadnearbyshoprec() {
        if(hmViewModel.getnbyshopModel().getValue().getDeal_day_products()!=null) {
            nbadapter = new nbyshopAdapter(getContext(), hmViewModel.getnbyshopModel().getValue().getDeal_day_products()        );
            GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
            llm.setOrientation(RecyclerView.HORIZONTAL);
            hmbinding.dealdayrec.setLayoutManager(llm);
            hmbinding.dealdayrec.setAdapter(nbadapter);
        }
        if(hmViewModel.getnbyshopModel().getValue().getTop_sell_products()!=null) {
            nbadapter1 = new nbyshopAdapter(getContext(), hmViewModel.getnbyshopModel().getValue().getTop_sell_products());
            GridLayoutManager llm1 = new GridLayoutManager(getActivity(), 2);
            llm1.setOrientation(RecyclerView.HORIZONTAL);
            hmbinding.topselrec.setLayoutManager(llm1);
            hmbinding.topselrec.setAdapter(nbadapter1);
        }
        if(hmViewModel.getnbyshopModel().getValue().getBest_deal_products()!=null) {
            nbadapter2 = new nbyshopAdapter(getContext(), hmViewModel.getnbyshopModel().getValue().getBest_deal_products());
            GridLayoutManager llm2 = new GridLayoutManager(getActivity(), 2);
            llm2.setOrientation(RecyclerView.HORIZONTAL);
            hmbinding.bestdealrec.setLayoutManager(llm2);
            hmbinding.bestdealrec.setAdapter(nbadapter2);
        }
        if(hmViewModel.getnbyshopModel().getValue().getAll_products()!=null) {
            nbadapter3 = new nbyshopAdapter(getContext(), hmViewModel.getnbyshopModel().getValue().getAll_products());
            GridLayoutManager llm3 = new GridLayoutManager(getActivity(), 2);
            llm3.setOrientation(RecyclerView.VERTICAL);
            hmbinding.allprodsRec.setLayoutManager(llm3);
            hmbinding.allprodsRec.setAdapter(nbadapter3);
        }
    }

    private void loadcatrec() {
        if(Objects.requireNonNull(hmViewModel.getnbyshopModel()
                .getValue()).getAll_categories()!=null) {
            com.multivendor.marketapp.Adapters.categoriesAdapter categoriesAdapter = new categoriesAdapter(getContext(), Objects.requireNonNull(hmViewModel.getnbyshopModel()
                    .getValue()).getAll_categories());

            GridLayoutManager llm = new GridLayoutManager(getContext(), 2);
            llm.setOrientation(RecyclerView.HORIZONTAL);
            hmbinding.categoriesrv.setLayoutManager(llm);
            hmbinding.categoriesrv.setAdapter(categoriesAdapter);
        }
    }

    private void rotatebanner() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bannerAdapter.getItemCount() > 0) {
                    if (bannerAdapter.getItemCount() > hmbinding.bannerrv.getCurrentItem() && hmbinding.bannerrv.getCurrentItem() == 0) {
                        hmbinding.bannerrv.setCurrentItem(1, true);
                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#0881E3"));
                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;
                    }
                    if (bannerAdapter.getItemCount() > hmbinding.bannerrv.getCurrentItem() && hmbinding.bannerrv.getCurrentItem() == 1) {
                        hmbinding.bannerrv.setCurrentItem(2, true);
                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#0881E3"));
                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;
                    }
                    if (bannerAdapter.getItemCount() > hmbinding.bannerrv.getCurrentItem() && hmbinding.bannerrv.getCurrentItem() == 2) {
                        hmbinding.bannerrv.setCurrentItem(3, true);
                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#0881E3"));
                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;
                    }

                    if (bannerAdapter.getItemCount() > hmbinding.bannerrv.getCurrentItem() && hmbinding.bannerrv.getCurrentItem() == 2) {
                        hmbinding.bannerrv.setCurrentItem(0, true);
                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;
                    }
                    if (hmbinding.bannerrv.getCurrentItem() == bannerAdapter.getItemCount() - 1) {
                        hmbinding.bannerrv.setCurrentItem(0);
                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                        return;

                    } else {
                        hmbinding.bannerrv.setCurrentItem(0);
                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner();
                    }
                }


            }
        }, 5000);
    }

    private void rotatebanner2() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bannerAdapter2.getItemCount() > 0) {
                    if (bannerAdapter2.getItemCount() > hmbinding.bannerrv2.getCurrentItem() && hmbinding.bannerrv2.getCurrentItem() == 0) {
                        hmbinding.bannerrv2.setCurrentItem(1, true);
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner2();
                        return;
                    }
                    if (bannerAdapter2.getItemCount() > hmbinding.bannerrv2.getCurrentItem() && hmbinding.bannerrv2.getCurrentItem() == 1) {
                        hmbinding.bannerrv2.setCurrentItem(2, true);
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner2();
                        return;
                    }
                    if (bannerAdapter2.getItemCount() > hmbinding.bannerrv2.getCurrentItem() && hmbinding.bannerrv2.getCurrentItem() == 2) {
                        hmbinding.bannerrv.setCurrentItem(3, true);
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner2();
                        return;
                    }

                    if (bannerAdapter2.getItemCount() > hmbinding.bannerrv2.getCurrentItem() && hmbinding.bannerrv2.getCurrentItem() == 2) {
                        hmbinding.bannerrv2.setCurrentItem(0, true);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner2();
                        return;
                    }
                    if (hmbinding.bannerrv2.getCurrentItem() == bannerAdapter2.getItemCount() - 1) {
                        hmbinding.bannerrv2.setCurrentItem(0);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner2();
                        return;

                    } else {
                        hmbinding.bannerrv2.setCurrentItem(0);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner2();
                    }
                }


            }
        }, 5000);
    }

    private void rotatebanner3() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bannerAdapter3.getItemCount() > 0) {
                    if (bannerAdapter3.getItemCount() > hmbinding.bannerrv3.getCurrentItem() && hmbinding.bannerrv3.getCurrentItem() == 0) {
                        hmbinding.bannerrv3.setCurrentItem(1, true);
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner3();
                        return;
                    }
                    if (bannerAdapter3.getItemCount() > hmbinding.bannerrv3.getCurrentItem() && hmbinding.bannerrv3.getCurrentItem() == 1) {
                        hmbinding.bannerrv3.setCurrentItem(2, true);
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner3();
                        return;
                    }
                    if (bannerAdapter3.getItemCount() > hmbinding.bannerrv3.getCurrentItem() && hmbinding.bannerrv3.getCurrentItem() == 2) {
                        hmbinding.bannerrv3.setCurrentItem(3, true);
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner3();
                        return;
                    }

                    if (bannerAdapter3.getItemCount() > hmbinding.bannerrv3.getCurrentItem() && hmbinding.bannerrv3.getCurrentItem() == 2) {
                        hmbinding.bannerrv3.setCurrentItem(0, true);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner3();
                        return;
                    }
                    if (hmbinding.bannerrv3.getCurrentItem() == bannerAdapter3.getItemCount() - 1) {
                        hmbinding.bannerrv3.setCurrentItem(0);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner3();
                        return;

                    } else {
                        hmbinding.bannerrv3.setCurrentItem(0);
//                        hmbinding.onbprog1.getBackground().setTint(Color.parseColor("#0881E3"));
//                        hmbinding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
//                        hmbinding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
                        rotatebanner3();
                    }


                }
            }
        }, 5000);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onResume() {
        super.onResume();
        dataloaded=false;
        View bottom=getActivity().findViewById(R.id.bottomnav);
        bottom.setVisibility(View.GONE);
        getlatlong();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().getViewModelStore().clear();
    }
}