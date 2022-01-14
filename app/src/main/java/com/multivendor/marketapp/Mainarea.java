package com.multivendor.marketapp;

import static android.content.ContentValues.TAG;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.databinding.ActivityMainareaBinding;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Mainarea extends AppCompatActivity {

    ActivityMainareaBinding mabidning;
    private Integer backclicks = 0;
    final int PERMISSION_CODE = 1001;
    private SharedPreferences sharedPreferences;

    //Define a request code to send to Google Play services
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mabidning = ActivityMainareaBinding.inflate(getLayoutInflater());
        setContentView(mabidning.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.secgrey, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.secgrey));
        }

        //getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
           // startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        bottombarholder();
        handleprofilecomp();



    }



    private void handleprofilecomp() {

        mabidning.pfcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim0 = AnimationUtils.loadAnimation(Mainarea.this, R.anim.slide_in_up);
                mabidning.pfnotstlay.setAnimation(anim0);
                mabidning.pfnotstlay.setVisibility(View.INVISIBLE);

            }
        });
        mabidning.pfsetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Mainarea.this, profilesettingsact.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
        sharedPreferences = getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<loginresResponse.login> call = logregApiInterface.getprofile(userid);

        call.enqueue(new Callback<loginresResponse.login>() {
            @Override
            public void onResponse(Call<loginresResponse.login> call, Response<loginresResponse.login> response) {
                if (!response.isSuccessful()) {
                    Log.d("error code:", String.valueOf(response.code()));
                    return;
                }

                loginresResponse.login resp = response.body();

                if(resp.getResult()!=null) {
                if (resp.getResult().getName() != null &&
                        resp.getResult().getAddress() != null && !resp.getResult().getName().toString().equals(" ")
                        && !resp.getResult().getAddress().toString().equals(" ")) {

                    mabidning.pfnotstlay.setVisibility(View.GONE);
                } else {
                    mabidning.pfnotstlay.setVisibility(View.VISIBLE);
                    Animation anim0 = AnimationUtils.loadAnimation(Mainarea.this, R.anim.slide_out_up);
                    mabidning.pfnotstlay.setAnimation(anim0);
                }
                }
            }

            @Override
            public void onFailure(Call<loginresResponse.login> call, Throwable t) {

            }
        });
    }

    private void bottombarholder() {
        homefragment df = new homefragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainfragment, df);
        transaction.commit();

        mabidning.homelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.bottomhomeicon).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#0881E3"));

                Picasso.get().load(R.drawable.bottomordersicon).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomcarticon).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomprofileicon).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#929292"));

                mabidning.homeselector.setVisibility(View.VISIBLE);
                mabidning.orderselector.setVisibility(View.INVISIBLE);
                mabidning.cartselector.setVisibility(View.INVISIBLE);
                mabidning.profileselector.setVisibility(View.INVISIBLE);

                homefragment df = new homefragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_fast_2, R.anim.fade);
                transaction.replace(R.id.mainfragment, df);
                transaction.commit();
            }
        });

        mabidning.orderlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.bottomhomenotselected).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomorderselected).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#0881E3"));

                Picasso.get().load(R.drawable.bottomcarticon).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomprofileicon).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#929292"));


                mabidning.homeselector.setVisibility(View.INVISIBLE);
                mabidning.orderselector.setVisibility(View.VISIBLE);
                mabidning.cartselector.setVisibility(View.INVISIBLE);
                mabidning.profileselector.setVisibility(View.INVISIBLE);
                orderfragment df = new orderfragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_fast_2, R.anim.fade);
                transaction.replace(R.id.mainfragment, df);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        mabidning.profiellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.bottomhomenotselected).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomordersicon).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomcarticon).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomprofileselected).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#0881E3"));


                mabidning.homeselector.setVisibility(View.INVISIBLE);
                mabidning.orderselector.setVisibility(View.INVISIBLE);
                mabidning.cartselector.setVisibility(View.INVISIBLE);
                mabidning.profileselector.setVisibility(View.VISIBLE);
                profilefragment df = new profilefragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_fast_2, R.anim.fade);
                transaction.replace(R.id.mainfragment, df);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        mabidning.cartlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.bottomhomenotselected).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomordersicon).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomcartselected).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#0881E3"));

                Picasso.get().load(R.drawable.bottomprofileicon).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#929292"));
                mabidning.homeselector.setVisibility(View.INVISIBLE);
                mabidning.orderselector.setVisibility(View.INVISIBLE);
                mabidning.cartselector.setVisibility(View.VISIBLE);
                mabidning.profileselector.setVisibility(View.INVISIBLE);

                cartfragment df = new cartfragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_fast_2, R.anim.fade);
                transaction.replace(R.id.mainfragment, df);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();

            if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
                Picasso.get().load(R.drawable.bottomhomeicon).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#0881E3"));

                Picasso.get().load(R.drawable.bottomordersicon).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomcarticon).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomprofileicon).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#929292"));

                mabidning.homeselector.setVisibility(View.VISIBLE);
                mabidning.orderselector.setVisibility(View.INVISIBLE);
                mabidning.cartselector.setVisibility(View.INVISIBLE);
                mabidning.profileselector.setVisibility(View.INVISIBLE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).
                    setTitle("Exit?").setMessage("Do you want to exit the app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }

//        backclicks++;
//        if(backclicks==1) {
//            Toast.makeText(this, "Press again to exit!", Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    backclicks=0;
//                }
//            },2500);
//        }
//        if(backclicks>=2) {
//            finish();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}


