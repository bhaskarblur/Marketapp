package com.multivendor.marketapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.multivendor.marketapp.Adapters.ordprodAdapter;
import com.multivendor.marketapp.Adapters.qordimgAdapter;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.Models.ordprodModel;
import com.multivendor.marketapp.Models.quickorderModel;
import com.multivendor.marketapp.ViewModel.quickordViewModel;
import com.multivendor.marketapp.databinding.FragmentMakequickfragmentBinding;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class makequickfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private FragmentMakequickfragmentBinding qobinding;
    private com.multivendor.marketapp.Adapters.ordprodAdapter ordprodAdapter;
    private com.multivendor.marketapp.ViewModel.quickordViewModel quickordViewModel;
    private com.multivendor.marketapp.Adapters.qordimgAdapter qordimgAdapter;
    List<quickorderModel.quick_products> finalprodlist = new ArrayList<>();
    List<String> imagelist = new ArrayList<>();
    String storeid;
    private String lat;
    private String longit;
    private String username;
    private String useraddress;
    private String usernumber;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public makequickfragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static makequickfragment newInstance(String param1, String param2) {
        makequickfragment fragment = new makequickfragment();
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
        qobinding = FragmentMakequickfragmentBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        storeid = bundle.getString("storeid");
        loadData();
        viewfunc();
        getlatlong();
        return qobinding.getRoot();
    }

    private void loadData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        String name = sharedPreferences.getString("username", "");
        String addr = sharedPreferences.getString("useraddress", "");
        String number = sharedPreferences.getString("usernumber", "");
        useraddress=sharedPreferences.getString("useraddress", "");
        username=sharedPreferences.getString("username", "");
        usernumber=  sharedPreferences.getString("usernumber", "");
        qobinding.usernameTxt.setText(name);
        qobinding.useraddressTxt.setText(addr);
        qobinding.usermobileTxt.setText(number);
        qobinding.newnametxt.setText(username);
        qobinding.newaddrtxt.setText(useraddress);
        qobinding.newnumtxt.setText(usernumber);
        finalprodlist = new ArrayList<>();
        finalprodlist.add(new quickorderModel.quick_products(null, null, null));
        ordprodAdapter = new ordprodAdapter(getContext(), finalprodlist, true,false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        qobinding.prodlistrec.setLayoutManager(llm);
        qobinding.prodlistrec.setAdapter(ordprodAdapter);

        imagelist = new ArrayList<>();

        qordimgAdapter = new qordimgAdapter(getContext(), imagelist);
        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        llm1.setOrientation(RecyclerView.HORIZONTAL);
        qobinding.prodimgrec.setLayoutManager(llm1);
        qobinding.prodimgrec.setAdapter(qordimgAdapter);


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
                        loadmat(Double.parseDouble(lat), Double.parseDouble(longit), "Your Location");
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {

                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude()
                                    , location.getLongitude(), 1);


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


    private void openchangeDialog() {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_down);
        qobinding.detchangeDialog.setAnimation(animation);
        qobinding.detchangeDialog.setVisibility(View.VISIBLE);

        qobinding.picklocat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlatlong();
            }
        });
        qobinding.savelocat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                qobinding.detchangeDialog.setAnimation(animation);
                qobinding.detchangeDialog.setVisibility(View.INVISIBLE);
                username = qobinding.newnametxt.getText().toString();
                useraddress = qobinding.newaddrtxt.getText().toString();
                usernumber = qobinding.newnumtxt.getText().toString();
            }
        });
    }

    private void viewfunc() {

        qobinding.listTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qobinding.listselector.setVisibility(View.VISIBLE);
                qobinding.listlayout.setVisibility(View.VISIBLE);
                qobinding.listTxt.setTextColor(Color.parseColor("#000000"));
                qobinding.uploadlayout.setVisibility(View.INVISIBLE);
                qobinding.uploadselector.setVisibility(View.INVISIBLE);
                qobinding.uploadTxt.setTextColor(Color.parseColor("#5F5F5F"));
            }
        });

        qobinding.uploadTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qobinding.listselector.setVisibility(View.INVISIBLE);
                qobinding.listlayout.setVisibility(View.INVISIBLE);
                qobinding.listTxt.setTextColor(Color.parseColor("#5F5F5F"));
                qobinding.uploadlayout.setVisibility(View.VISIBLE);
                qobinding.uploadselector.setVisibility(View.VISIBLE);
                qobinding.uploadTxt.setTextColor(Color.parseColor("#000000"));
            }
        });

        qobinding.userinfochange2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openchangeDialog();
            }
        });
        qobinding.backbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();

            }
        });
        qobinding.prodlistadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordprodAdapter.prodList.add(new quickorderModel.quick_products(null, null, null));
                ordprodAdapter.notifyDataSetChanged();

            }
        });

        qobinding.addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropActivity();
            }
        });
        qobinding.placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddQuickOrder();
            }
        });
    }

    private void AddQuickOrder() {
        if (ordprodAdapter.prodList.get(0).getProduct_name() != null && ordprodAdapter.prodList.get(0).getQty() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
            String userid = sharedPreferences.getString("userid", "");
            StringBuilder productname = new StringBuilder();
            StringBuilder qty = new StringBuilder();
            StringBuilder image = new StringBuilder();

            for (int i = 0; i < ordprodAdapter.prodList.size(); i++) {
                if (ordprodAdapter.prodList.size() > 1) {
                    if (ordprodAdapter.prodList.get(i).getProduct_name() != null &&
                            !ordprodAdapter.prodList.get(i).getProduct_name().equals("") &&
                            ordprodAdapter.prodList.get(i).getQty() != null &&
                            !ordprodAdapter.prodList.get(i).getQty().equals("")) {
                        productname.append(ordprodAdapter.prodList.get(i).getProduct_name() + ",");
                        qty.append(ordprodAdapter.prodList.get(i).getQty() + ",");
                    }
                } else {
                    productname.append(ordprodAdapter.prodList.get(i).getProduct_name());
                    qty.append(ordprodAdapter.prodList.get(i).getQty());
                }
            }

            for (int i = 0; i < qordimgAdapter.imglist.size(); i++) {
                if (qordimgAdapter.imglist.size() > 1) {
                    String base64img;
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(Uri.parse(qordimgAdapter.imglist.get(i)));
                        Bitmap image1 = BitmapFactory.decodeStream(is);
                        ByteArrayOutputStream by = new ByteArrayOutputStream();
                        image1.compress(Bitmap.CompressFormat.JPEG, 50, by);
                        base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                        image.append(base64img + ",");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    String base64img;
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(Uri.parse(qordimgAdapter.imglist.get(i)));
                        Bitmap image1 = BitmapFactory.decodeStream(is);
                        ByteArrayOutputStream by = new ByteArrayOutputStream();
                        image1.compress(Bitmap.CompressFormat.JPEG, 50, by);
                        base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                        image.append(base64img);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            qobinding.placeorderbtn.setText("Placing Order");
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

            Call<quickorderModel.AddquickordResp> call = logregApiInterface.add_quickorders(storeid, userid, productname.toString(), qty.toString()
                    , image.toString(), qobinding.usernameTxt.getText().toString(), qobinding.usernameTxt.getText().toString(),
                    qobinding.usermobileTxt.getText().toString(),lat,longit,qobinding.cartdesc2.getText().toString(),
                    qobinding.ordinst.getText().toString());

            call.enqueue(new Callback<quickorderModel.AddquickordResp>() {
                @Override
                public void onResponse(Call<quickorderModel.AddquickordResp> call, Response<quickorderModel.AddquickordResp> response) {
                    if (!response.isSuccessful()) {
                        Log.d("errorcode", String.valueOf(response.code()));
                        return;
                    }

                    quickorderModel.AddquickordResp resp = response.body();
                    Log.d("message",resp.getMessage());
                    if (resp.getMessage().equals("Product added successfully")) {
                        Toast.makeText(getContext(), "Order Sent!", Toast.LENGTH_SHORT).show();
                        qobinding.placeorderbtn.setVisibility(View.INVISIBLE);
                        qobinding.placeorderbtn.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });
                        qobinding.orstatustxt2.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "There Was An Error!", Toast.LENGTH_SHORT).show();
                        qobinding.placeorderbtn.setText("Send");
                    }

                }

                @Override
                public void onFailure(Call<quickorderModel.AddquickordResp> call, Throwable throwable) {
                    Log.d("Failure", throwable.getMessage());
                    Toast.makeText(getContext(), "There Was An Error!", Toast.LENGTH_SHORT).show();
                    qobinding.placeorderbtn.setText("Send");
                }
            });
        }
            else {
            Toast.makeText(getActivity(), "Please Write Atleast 1 Product.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void startCropActivity() {
        CropImage.activity()
                .start(getContext(), this);

    }
    private void loadmat(double sellat, double sellongit, String curlocat) {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getActivity().
                getSupportFragmentManager().findFragmentById(R.id.google_map4);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(supportMapFragment!=null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            final LatLng[] latLng = {new LatLng(sellat, sellongit)};
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng[0])
                                    .title("Current Location").draggable(true);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[0], 18));
                            googleMap.addMarker(markerOptions).setDraggable(true);

//                        psbinding.recentrebtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[0], 18));
//                            }
//                        });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                        @Override
                                        public void onMarkerDragStart(@NonNull Marker marker) {

                                        }

                                        @Override
                                        public void onMarkerDrag(@NonNull Marker marker) {

                                        }

                                        @Override
                                        public void onMarkerDragEnd(@NonNull Marker marker) {
                                            latLng[0] = marker.getPosition();
                                            LatLng new_latlng = marker.getPosition();
                                            lat = String.valueOf(new_latlng.latitude);
                                            longit = String.valueOf(new_latlng.longitude);


                                            Geocoder geocoder = new Geocoder(getContext()
                                                    , Locale.getDefault());
                                            try {
                                                List<Address> addresses = geocoder.getFromLocation(new_latlng.latitude, new_latlng.longitude, 1);
                                                qobinding.newaddrtxt.setText(addresses.get(0).getAddressLine(0));

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }, 1000);

                        }
                    });
                }
            }
        }, 100);

        qobinding.savelocat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qobinding.useraddressTxt.setText(qobinding.newaddrtxt.getText().toString());
                qobinding.usermobileTxt.setText(qobinding.newnumtxt.getText().toString());
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_down);
                qobinding.detchangeDialog.setAnimation(animation);
                qobinding.detchangeDialog.setVisibility(View.INVISIBLE);
                username = qobinding.newnametxt.getText().toString();
                useraddress = qobinding.newaddrtxt.getText().toString();
                usernumber = qobinding.newnumtxt.getText().toString();

            }
        });
    }
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imagelist.add(String.valueOf(result.getUri()));
                qordimgAdapter.notifyDataSetChanged();
            }

        }
    }
}