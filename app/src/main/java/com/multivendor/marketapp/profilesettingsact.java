package com.multivendor.marketapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.CustomDialogs.change_passDialog;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.databinding.ActivityProfilesettingsactBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profilesettingsact extends AppCompatActivity {

    private ActivityProfilesettingsactBinding psbinding;
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private Uri imguri;
    String userid;
    private Boolean sameimg = false;
    String base64img = new String();
    private String lat;
    private String longit;
    private LocationManager locationManager;
    private Boolean needimage = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String mLastLocation;
    SharedPreferences shpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        psbinding = ActivityProfilesettingsactBinding.inflate(getLayoutInflater());
        setContentView(psbinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        shpref = getSharedPreferences("userlogged", 0);
        userid = shpref.getString("userid", "");
        loadData(userid);
        viewfunctions();

    }


    private void loadmat(double sellat, double sellongit, String curlocat) {
        psbinding.currlocattxt.setText(curlocat);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        final LatLng[] latLng = {new LatLng(sellat, sellongit)};
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng[0])
                                .title("Current Location").draggable(true);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[0], 18));
                        googleMap.addMarker(markerOptions).setDraggable(true);

                        psbinding.recentrebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[0], 18));
                            }
                        });
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
                                        latLng[0] =marker.getPosition();
                                        LatLng new_latlng = marker.getPosition();
                                        lat = String.valueOf(new_latlng.latitude);
                                        longit = String.valueOf(new_latlng.longitude);


                                        Geocoder geocoder = new Geocoder(profilesettingsact.this
                                                , Locale.getDefault());
                                        try {
                                            List<Address> addresses = geocoder.getFromLocation(new_latlng.latitude, new_latlng.longitude, 1);
                                            psbinding.currlocattxt.setText(addresses.get(0).getAddressLine(0));
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
        }, 100);

        psbinding.savelocat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psbinding.addresschange.setText(psbinding.currlocattxt.getText().toString());
                Animation anim0 = AnimationUtils.loadAnimation(profilesettingsact.this, R.anim.slide_out_down);
                psbinding.locatcard.setAnimation(anim0);
                psbinding.picklocat.setVisibility(View.GONE);
                psbinding.locatcard.setVisibility(View.INVISIBLE);

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getlatlong() {
        psbinding.addresschange.setText("Getting Location");
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            LocationRequest request = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
            fusedLocationProviderClient.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                }
            }, Looper.getMainLooper());
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
                        Geocoder geocoder = new Geocoder(profilesettingsact.this
                                , Locale.getDefault());

                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                           psbinding.addresschange.setText(addresses.get(0).getAddressLine(0));
                            Animation anim0 = AnimationUtils.loadAnimation(profilesettingsact.this, R.anim.slide_in_down);
                            psbinding.locatcard.setAnimation(anim0);
                            psbinding.locatcard.setVisibility(View.VISIBLE);
                            loadmat(location.getLatitude(), location.getLongitude(), addresses.get(0).getAddressLine(0));
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
                                Geocoder geocoder = new Geocoder(profilesettingsact.this
                                        , Locale.getDefault());

                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    loadmat(location.getLatitude(), location.getLongitude(), addresses.get(0).getAddressLine(0));
                                    Animation anim0 = AnimationUtils.loadAnimation(profilesettingsact.this, R.anim.slide_in_down);
                                    psbinding.locatcard.setAnimation(anim0);
                                    psbinding.locatcard.setVisibility(View.VISIBLE);
                                    psbinding.addresschange.setText(addresses.get(0).getAddressLine(0));
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

    private void loadData(String userid) {

        psbinding.phonechange.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        psbinding.passchange.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<loginresResponse.login> call = logregApiInterface.getprofile(userid);

        call.enqueue(new Callback<loginresResponse.login>() {
            @Override
            public void onResponse(Call<loginresResponse.login> call, Response<loginresResponse.login> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                loginresResponse.login resp = response.body();
                if (resp.getmessage() != null) {
                    Log.d("message", resp.getmessage());
                }

                if (resp.getmessage().equals("User profile.!")) {
                    if (resp.getResult().getName() != null) {
                        psbinding.unamechange.setText(resp.getResult().getName());
                        needimage = false;
                    }
                    if (resp.getResult().phone != null) {
                        psbinding.phonechange.setText(resp.getResult().getPhone());
                    }
                    if (resp.getResult().getAddress() != null) {
                        psbinding.addresschange.setText(resp.getResult().getAddress());
                    } else {
                        psbinding.picklocat.setVisibility(View.VISIBLE);
                        psbinding.updatebtn.setVisibility(View.INVISIBLE);
                    }
                    if (resp.getResult().getImage() != null) {
                        imguri = Uri.parse(resp.getResult().getImage());
                        Log.d("img", String.valueOf(imguri));
                        sameimg = true;
                        base64img = resp.getResult().getImage();
                        final int radius = 35;
                        final int margin = 35;
                        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                        Picasso.get().load(imguri).transform(new CropCircleTransformation()).into(psbinding.userimg);

                    }
                }
            }

            @Override
            public void onFailure(Call<loginresResponse.login> call, Throwable t) {
                Toast.makeText(profilesettingsact.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewfunctions() {
        psbinding.picklocat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlatlong();
            }
        });
        psbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profilesettingsact.this, Mainarea.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });

        psbinding.avataredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();
            }
        });

        psbinding.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psbinding.progressBar4.setVisibility(View.VISIBLE);
                psbinding.updatebtn.setVisibility(View.INVISIBLE);
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                try {
                    if (imguri != null) {
                        if (sameimg.equals(false)) {
                            InputStream is = getContentResolver().openInputStream(imguri);
                            Bitmap image = BitmapFactory.decodeStream(is);
                            ByteArrayOutputStream by = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 50, by);
                            base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Call<loginresResponse.login> call;
                if (needimage.equals(true)) {
                    Log.d("newimg", "yh");
                    call = logregApiInterface.updateprofile(userid, psbinding.unamechange.getText().toString()
                            , psbinding.addresschange.getText().toString(), base64img);
                } else {
                    Log.d("newimg", "no");
                    call = logregApiInterface.noimgprofileupdate(userid, psbinding.unamechange.getText().toString()
                            , psbinding.addresschange.getText().toString());
                }
                call.enqueue(new Callback<loginresResponse.login>() {
                    @Override
                    public void onResponse(Call<loginresResponse.login> call, Response<loginresResponse.login> response) {
                        if (!response.isSuccessful()) {

                            Log.d("Code", response.message().toString());
                            return;
                        }

                        loginresResponse.login resp = response.body();
                        if (resp.getmessage() != null) {
                            Log.d("message", resp.getmessage());
                        }
                        if (resp.getmessage().equals("Profile updated successfully.!")) {
                            psbinding.progressBar4.setVisibility(View.GONE);
                            psbinding.updatebtn.setVisibility(View.VISIBLE);
                            Toast.makeText(profilesettingsact.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                            Log.d("profup", "updated");
                            SharedPreferences.Editor editor = shpref.edit();
                            editor.putString("username", resp.getResult().getName());
                            editor.putString("storename", resp.getResult().getStore_name());
                            editor.putString("useraddress", resp.getResult().getAddress());
                            editor.putString("userimage", resp.getResult().getImage());
                            editor.putString("usernumber",resp.getResult().getPhone());
                           // Log.d("respimg", resp.getResult().getImage());

                            editor.commit();

                        } else {
                            psbinding.progressBar4.setVisibility(View.GONE);
                            psbinding.updatebtn.setVisibility(View.VISIBLE);
                            Toast.makeText(profilesettingsact.this, "An error has occured!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<loginresResponse.login> call, Throwable t) {
                        psbinding.progressBar4.setVisibility(View.GONE);
                        psbinding.updatebtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        psbinding.passchangetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_passDialog change_passDialog = new change_passDialog();
                change_passDialog.show(getSupportFragmentManager(), "changepassword");
            }
        });
    }

    private void startCropActivity() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
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
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imguri = result.getUri();
                needimage = true;
                sameimg = false;
                final int radius = 20;
                final int margin = 20;
                final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                Picasso.get().load(imguri).transform(transformation).into(psbinding.userimg);
            }

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}