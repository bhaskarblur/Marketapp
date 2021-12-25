package com.multivendor.marketapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.multivendor.marketapp.Adapters.ordprodAdapter;
import com.multivendor.marketapp.Adapters.qordimgAdapter;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.ordprodModel;
import com.multivendor.marketapp.Models.quickorderModel;
import com.multivendor.marketapp.ViewModel.quickordViewModel;
import com.multivendor.marketapp.databinding.ActivityActQuickordseeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class act_quickordsee extends AppCompatActivity {
    private ActivityActQuickordseeBinding qobinding;
    private com.multivendor.marketapp.Adapters.ordprodAdapter ordprodAdapter;
    private com.multivendor.marketapp.ViewModel.quickordViewModel quickordViewModel;
    private com.multivendor.marketapp.Adapters.qordimgAdapter qordimgAdapter;
    List<quickorderModel.quick_products> finalprodlist = new ArrayList<>();
    String userid;
    String quickorderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qobinding = ActivityActQuickordseeBinding.inflate(getLayoutInflater());
        setContentView(qobinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        quickorderid = intent.getStringExtra("orderid");
        quickordViewModel = new ViewModelProvider(this).get(com.multivendor.marketapp.ViewModel.quickordViewModel.class);
        quickordViewModel.initwork(userid, quickorderid);
        quickordViewModel.getOrdprodModel().observe(this, new Observer<List<quickorderModel.quick_products>>() {
            @Override
            public void onChanged(List<quickorderModel.quick_products> ordprodModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finalprodlist = ordprodModels;
                        ordprodAdapter.notifyDataSetChanged();
                    }
                }, 100);
            }
        });
        quickordViewModel.getProdimgModel().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        qordimgAdapter.notifyDataSetChanged();
                    }
                }, 100);
            }
        });
        quickordViewModel.getWholedata().observe(this, new Observer<quickorderModel.quickordResult>() {
            @Override
            public void onChanged(quickorderModel.quickordResult quickordResult) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(quickordResult.getPayment_method()!=null) {
                            if (quickordResult.getPayment_method().equals("prepaid")) {
                                if (quickordResult.getTransaction_id() != null) {
                                    qobinding.custpaymeth.setText("Payment Method: " + "UPI " + "(Txn:" + quickordResult.getTransaction_id() + ")");
                                } else {
                                    qobinding.custpaymeth.setText("Payment Method: " + "UPI");
                                }

                            } else if ((quickordResult.getPayment_method().equals("cod"))) {
                                qobinding.custpaymeth.setText("Payment Method: " + "COD");
                            }
                        }
                        else {
                            qobinding.custpaymeth.setText("Payment Method: "+"COD");
                        }

                        if(quickordResult.getStatus()!=null) {
                            if (!quickordResult.getStatus().equals("Delivered")) {
                                if (quickordResult.getPayment_method() != null) {
                                    if (quickordResult.getPayment_method().equals("prepaid")) {
                                        qobinding.custpaystat.setText("Payment Status: " + "Paid");
                                    } else if (quickordResult.getPayment_method().equals("cod")) {
                                        qobinding.custpaystat.setText("Payment Status: " + "Pending");
                                    }
                                } else {
                                    qobinding.custpaystat.setText("Payment Status: " + "Pending");
                                }
                            } else {
                                qobinding.custpaystat.setText("Payment Status: " + "Pending");
                            }
                        }
                        else {
                            qobinding.custpaystat.setText("Payment Status: " + "Pending");
                        }
                        qobinding.custname.setText("Name: "+quickordResult.getCustomer_name());
                        qobinding.custaddr.setText("Address: "+quickordResult.getCustomer_address());
                        qobinding.custphone.setText("Phone: "+quickordResult.getCustomer_phone());
                        if (quickordResult.getExpected_delivery() != null) {
                            qobinding.expdelvtx.setText("Expected delivery in " + quickordResult.getExpected_delivery() + " hours.");
                        }
                        if (quickordResult.getSubtotal() != null) {
                            qobinding.carttotalprice.setText("₹ " + quickordResult.getSubtotal());
                        }
                        if (quickordResult.getShipping_charge() != null) {
                            qobinding.cartdeliverycharge.setText("₹ " + quickordResult.getShipping_charge());
                        }
                        if (quickordResult.getTotal_price() != null) {
                            qobinding.cartgrandtotal.setText("₹ " + quickordResult.getTotal_price());
                        }

                        if (quickordResult.getSeller_status() != null) {
                            if (quickordResult.getSeller_status().equals("Accepted")) {
                                qobinding.placeorderbtn.setVisibility(View.VISIBLE);
                                qobinding.ordrejbtn.setVisibility(View.VISIBLE);

                            }
                            else if (quickordResult.getSeller_status().equals("Rejected")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Rejected By Seller.");
                            }

                        }
                        else {
                            qobinding.placeorderbtn.setVisibility(View.INVISIBLE);
                            qobinding.ordrejbtn.setVisibility(View.INVISIBLE);
                            qobinding.orstatustxt.setVisibility(View.VISIBLE);
                            qobinding.orstatustxt.setText("Wait For Seller To Respond");
                        }
                        if (quickordResult.getUser_status() != null) {
                            if (quickordResult.getUser_status().equals("Accepted")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Confirmed.");
                            }
                            else if (quickordResult.getUser_status().equals("Rejected")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Rejected.");
                            }
                        }

                        if (quickordResult.getStatus() != null) {
                            if (quickordResult.getStatus().equals("Delivered")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                // qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Payment Done And Order Complete.");
                                qobinding.custpaystat.setText("Payment Status: " + "Paid");
                            }
                        }

                        for (int i = 0; i < quickordResult.getOrderstatus().size(); i++) {

                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Accepted")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Confirmed.");
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Rejected")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Rejected By Seller.");
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Packing")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Packing Order.");
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Ready_To_Ship")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Ready To Ship.");
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Out_For_Delivery")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Out For Delivery.");
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Delivered")) {
                                qobinding.custpaystat.setText("Payment Status: Paid");

                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Cancelled") ||
                            quickordResult.getStatus().equals("Cancelled")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Cancelled (Reason: "+quickordResult.getCancel_reason()+")");
                            }
                        }
                        if (quickordResult.getStatus() != null) {
                            if (quickordResult.getStatus().equals("Delivered")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                // qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Payment Done And Order Complete.");
                                qobinding.custpaystat.setText("Payment Status: " + "Paid");
                            }
                        }
                    }
                }, 100);
            }
        });
        viewfunc();
        loadData();

    }

    private void loadData() {
        ordprodAdapter = new ordprodAdapter(this, quickordViewModel.getOrdprodModel().getValue(), false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        qobinding.prodlistrec.setLayoutManager(llm);
        qobinding.prodlistrec.setAdapter(ordprodAdapter);
        qobinding.prodlistrec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        qordimgAdapter = new qordimgAdapter(this, quickordViewModel.getProdimgModel().getValue());
        LinearLayoutManager llm1 = new LinearLayoutManager(this);
        llm1.setOrientation(RecyclerView.HORIZONTAL);
        qobinding.prodimgrec.setLayoutManager(llm1);
        qobinding.prodimgrec.setAdapter(qordimgAdapter);


    }

    private void viewfunc() {
        qobinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });

        qobinding.placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<quickorderModel.singlequickordResp> call = logregApiInterface.user_quickFinal(userid, quickorderid
                        , "Accepted");

                call.enqueue(new Callback<quickorderModel.singlequickordResp>() {
                    @Override
                    public void onResponse(Call<quickorderModel.singlequickordResp> call, Response<quickorderModel.singlequickordResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        quickorderModel.singlequickordResp resp = response.body();

                        if (resp.getResult() != null) {
                            Toast.makeText(act_quickordsee.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                            qobinding.placeorderbtn.setVisibility(View.GONE);
                            qobinding.ordrejbtn.setVisibility(View.GONE);
                            qobinding.orstatustxt.setVisibility(View.VISIBLE);
                            qobinding.orstatustxt.setText("Order Confirmed!");
                        }
                        else {
                            Toast.makeText(act_quickordsee.this, "There Was An Error, Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<quickorderModel.singlequickordResp> call, Throwable throwable) {
                        Log.d("Failure",throwable.getMessage());
                    }
                });
            }
        });

        qobinding.ordrejbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://market.saatirmind.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<quickorderModel.singlequickordResp> call = logregApiInterface.user_quickFinal(userid, quickorderid
                        , "Rejected");

                call.enqueue(new Callback<quickorderModel.singlequickordResp>() {
                    @Override
                    public void onResponse(Call<quickorderModel.singlequickordResp> call, Response<quickorderModel.singlequickordResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        quickorderModel.singlequickordResp resp = response.body();

                        if (resp.getResult() != null) {
                            Toast.makeText(act_quickordsee.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                            qobinding.placeorderbtn.setVisibility(View.GONE);
                            qobinding.ordrejbtn.setVisibility(View.GONE);
                            qobinding.orstatustxt.setVisibility(View.VISIBLE);
                            qobinding.orstatustxt.setText("Order Rejected By You!");
                        }
                        else {
                            Toast.makeText(act_quickordsee.this, "There Was An Error, Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<quickorderModel.singlequickordResp> call, Throwable throwable) {
                        Log.d("Failure",throwable.getMessage());
                    }
                });
            }
        });

    }
    @Override
    public void finish() {
        super.finish();
        this.getViewModelStore().clear();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}