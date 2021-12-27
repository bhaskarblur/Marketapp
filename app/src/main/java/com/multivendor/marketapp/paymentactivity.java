package com.multivendor.marketapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.databinding.ActivityPaymentactivityBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class paymentactivity extends AppCompatActivity {

    private ActivityPaymentactivityBinding pmbinding;
    private String storeid;
    String userid;
    private String cartid;
    private String name;
    private String amount;
    private String address;
    private String number;
    private String delvinstr;
    private String sellerUPI;
    private String lat;
    private String longit;
    private String selected = "upi";
    private String MOA_amount;
    private FusedLocationProviderClient fusedLocationProviderClient;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pmbinding = ActivityPaymentactivityBinding.inflate(getLayoutInflater());
        setContentView(pmbinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("userlogged", 0);
        userid = sharedPreferences.getString("userid", "");
        Intent intent = getIntent();
        storeid = intent.getStringExtra("store_id");
        cartid = intent.getStringExtra("cart_id");
        name = intent.getStringExtra("username");
        number = intent.getStringExtra("usernumber");
        address = intent.getStringExtra("address");
        amount = intent.getStringExtra("amount");
        pmbinding.amounttxt.setText("₹ " + amount);
        delvinstr = intent.getStringExtra("deliveryinstr");
        if (cartid != null) {
            getinfo();
        }
        getlatlong();
    }

    @SuppressLint("MissingPermission")
    private void getlatlong() {

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

    private boolean isPackageInstalled(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void viewfunctions() {
        pmbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        pmbinding.upilay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "upi";
                pmbinding.upilay.getBackground().setTint(Color.parseColor("#F5F5F5"));
                pmbinding.codlayout.getBackground().setTint(Color.parseColor("#FFFFFF"));
            }
        });

        pmbinding.codlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected="cod";
                pmbinding.codlayout.getBackground().setTint(Color.parseColor("#F5F5F5"));
                pmbinding.upilay.getBackground().setTint(Color.parseColor("#FFFFFF"));
            }
        });
        pmbinding.paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected.equals("cod")) {
                    if (Integer.parseInt(amount) >= Integer.parseInt(MOA_amount)) {
                        confirmorder(userid, storeid, cartid, amount, name, number, address,
                                "cod", "success", "nothing", delvinstr);
                    } else {
                        Toast.makeText(paymentactivity.this, "This shop accept minimum order of Rs " + MOA_amount, Toast.LENGTH_SHORT).show();
                    }
                } else if (selected.equals("upi")) {
                    if (Integer.parseInt(amount) >= Integer.parseInt(MOA_amount)) {

                        Uri uri = new Uri.Builder().scheme("upi").authority("pay")
                                .appendQueryParameter("pa", "8299189690@okbizaxis")
                                .appendQueryParameter("pn", "LMART SOLUTIONS PVT LTD")
                                .appendQueryParameter("am", amount.toString().replace("₹ ", ""))
                                .appendQueryParameter("cu", "INR")
                                .build();

                        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                        upiPayIntent.setData(uri);
                        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

                        if (chooser.resolveActivity(getPackageManager()) != null) {
                            try {
                                startActivityForResult(upiPayIntent, GOOGLE_PAY_REQUEST_CODE);
                            } catch (Exception e) {
                                Toast.makeText(paymentactivity.this, "No UPI Apps Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(paymentactivity.this, "No UPI Apps Found!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(paymentactivity.this, "Minimum order is Rs " + MOA_amount, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void confirmorder(String userid, String storeid, String cartid, String amount,
                              String name, String number, String address, String pay_method,
                              String pay_status, String transid, String delvinstr) {
        Toast.makeText(paymentactivity.this, "Confirming Your Order!", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.cartResp> call = logregApiInterface.confirm_order(userid, storeid, cartid, amount, name
                , number, address, pay_method, pay_status, transid, delvinstr, lat, longit);

        call.enqueue(new Callback<cartModel.cartResp>() {
            @Override
            public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("errorcode", String.valueOf(response.code()));
                    return;
                }

                cartModel.cartResp resp = response.body();
                Log.d("message", resp.getMessage());

                startActivity(new Intent(paymentactivity.this, orderplacedactivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            }

            @Override
            public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                Log.d("failed", t.getMessage());
                startActivity(new Intent(paymentactivity.this, orderplacedactivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String status = new String();
        if (data != null) {
            if (data.getData() != null) {
                status = data.getDataString().toLowerCase();
            }
            if (requestCode == RESULT_OK) {
                try {
                    if (status.equals("success")) {
                        String resper = data.getStringExtra("response");
                        Log.d("response", resper);
                        ArrayList<String> breakresp = new ArrayList<>();
                        breakresp.add(resper);
                        upiPaymentDataOperation(breakresp);
                    } else {
                        Toast.makeText(paymentactivity.this, "Payment Failed!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(paymentactivity.this, "There Was An Error While Paying.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void upiPaymentDataOperation(ArrayList<String> breakresp) {
        String str = breakresp.get(0);
        Log.d("description:", str);
        String paycancel = "";
        String status = "";
        String approvalrefno = "";
        String resp[] = str.split("&");
        for (int i = 0; i < resp.length; i++) {
            String equalstr[] = resp[i].split("=");
            if (equalstr.length >= 2) {
                if (equalstr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalstr[1].toLowerCase();
                } else if (equalstr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) ||
                        equalstr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalrefno = equalstr[1];
                } else {
                    paycancel = "Payment Cancelled By User.";
                }
            }

            if (status.equals("success")) {
                Log.d("success with RefNo:", approvalrefno);
                Toast.makeText(paymentactivity.this, "Please Wait!", Toast.LENGTH_SHORT).show();
                confirmorder(userid, storeid, cartid, amount, name, number, address, "prepaid", "success",
                        approvalrefno, delvinstr);
            }
        }

    }

    private void getinfo() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
        Call<userAPIResp.sellerinfo> call = logregApiInterface.get_paymentstore(storeid, cartid);

        call.enqueue(new Callback<userAPIResp.sellerinfo>() {
            @Override
            public void onResponse(Call<userAPIResp.sellerinfo> call, Response<userAPIResp.sellerinfo> response) {
                if (!response.isSuccessful()) {
                    Log.d("errorcode", String.valueOf(response.code()));
                    return;
                }

                userAPIResp.sellerinfo resp = response.body();

                sellerUPI = resp.getResult().getUpi_id();
                if (sellerUPI == null) {
                    pmbinding.codimg.setVisibility(View.VISIBLE);
                    pmbinding.codtxt.setVisibility(View.VISIBLE);
                    pmbinding.codtxt1.setVisibility(View.VISIBLE);
                    pmbinding.submitbtn.setVisibility(View.VISIBLE);
                    pmbinding.upiradio.setVisibility(View.INVISIBLE);
                    pmbinding.paybtn.setVisibility(View.INVISIBLE);

                }
                viewfunctions();
                MOA_amount = resp.getResult().getMin_order_amount();
//                Toast.makeText(paymentactivity.this, "Choose Any Of These To Proceed.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<userAPIResp.sellerinfo> call, Throwable t) {
                Toast.makeText(paymentactivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}