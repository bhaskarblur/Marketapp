package com.multivendor.marketapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Toast;

import com.multivendor.marketapp.Adapters.ordproductAdapter;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.ViewModel.vieworderViewModel;
import com.multivendor.marketapp.databinding.ActivityOrdhisBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ordhisAct extends AppCompatActivity {
    private ActivityOrdhisBinding ohbinding;
    private com.multivendor.marketapp.Adapters.ordproductAdapter ordproductAdapter;
    private com.multivendor.marketapp.ViewModel.vieworderViewModel vieworderViewModel;
    private List<productitemModel> prodlist = new ArrayList<>();
    private String orderid;
    private String storeid;
    private String userid;
    private String storename;
    private String storenumb;
    private Boolean shortbol = false;
    private String ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ohbinding = ActivityOrdhisBinding.inflate(getLayoutInflater());
        setContentView(ohbinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        SharedPreferences sharedPreferences = getSharedPreferences("userlogged", 0);
        Intent intent = getIntent();
        userid = sharedPreferences.getString("userid", "");
        orderid = intent.getStringExtra("orderid");
        storeid = intent.getStringExtra("storeid");
        vieworderViewModel = new ViewModelProvider(this).get(vieworderViewModel.class);
        vieworderViewModel.initwork(userid, storeid, orderid);
        vieworderViewModel.getGetordinfo().observe(this, new Observer<cartModel.singlecartResult>() {
            @Override
            public void onChanged(cartModel.singlecartResult cartResult) {
                loadinfo();
                viewfunctions();
            }
        });
        vieworderViewModel.getAllproductModel().observe(this, new Observer<List<productitemModel>>() {
            @Override
            public void onChanged(List<productitemModel> productitemModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (productitemModels.size() > 0) {
                            if(vieworderViewModel.getGetordinfo().getValue()!=null) {
                                for (cartModel.productResult selprods : vieworderViewModel.getGetordinfo().getValue().getProducts()) {
                                    for (productitemModel prodmodel : productitemModels) {
                                        if (prodmodel.getProduct_id().equals(selprods.getProduct_id())) {
                                            prodlist.add(prodmodel);
                                        }
                                    }
                                }
                                loadData();
                            }
                        }
                    }

                }, 100);
            }
        });

        loadstoreinfo();

    }

    private void loadstoreinfo() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<userAPIResp.sellerinfo> call = logregApiInterface.get_shopinfo(storeid, "", "");

        call.enqueue(new Callback<userAPIResp.sellerinfo>() {
            @Override
            public void onResponse(Call<userAPIResp.sellerinfo> call, Response<userAPIResp.sellerinfo> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                userAPIResp.sellerinfo storedata = response.body();
                Log.d("message", storedata.getMessage());
                if (storedata.getResult() != null) {
                    storename = storedata.getResult().getStore_name();
                    storenumb = storedata.getResult().getPhone();
                    ohbinding.custstorename.setText("Ordered From: " + storename);
                }
            }

            @Override
            public void onFailure(Call<userAPIResp.sellerinfo> call, Throwable t) {
                Log.d("errorshops", t.getMessage().toString());
            }
        });
    }

    private void loadinfo() {
        ohbinding.orditemcount.setText(String.valueOf(vieworderViewModel.getGetordinfo()
                .getValue().getProducts().size()));

        int totalPrice =  Integer.parseInt(vieworderViewModel.getGetordinfo().getValue().getTotal_price())
                - Integer.parseInt(vieworderViewModel.getGetordinfo().getValue().getShipping_charge());

        ohbinding.orderamoun.setText("₹ " + totalPrice);

        ohbinding.orddelvchar.setText("₹ " + vieworderViewModel.getGetordinfo().getValue()
                .getShipping_charge());

        ohbinding.grandtotal.setText("₹ "+vieworderViewModel.getGetordinfo().getValue().getTotal_price());

        if (vieworderViewModel.getGetordinfo().getValue().getPayment_method() != null) {
            if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("prepaid")) {
                if (vieworderViewModel.getGetordinfo().getValue().getTransaction_id() != null) {
                    ohbinding.custpaymeth.setText("Payment Method: " + "UPI " + "(Txn:" + vieworderViewModel.getGetordinfo().getValue().getTransaction_id() + ")");
                } else {
                    ohbinding.custpaymeth.setText("Payment Method: " + "UPI");
                }

            } else if ((vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("cod"))) {
                ohbinding.custpaymeth.setText("Payment Method: " + "COD");
            }
        } else {
            ohbinding.custpaymeth.setText("Payment Method: " + "");
        }
        if (!vieworderViewModel.getGetordinfo().getValue().getStatus().equals("Delivered")) {
            if (vieworderViewModel.getGetordinfo().getValue().getPayment_method() != null) {
                if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("prepaid")) {
                    ohbinding.custpaystat.setText("Payment Status: " + "Paid");
                } else if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("cod")) {
                    ohbinding.custpaystat.setText("Payment Status: " + "Pending");
                }
            } else {
                ohbinding.custpaystat.setText("Payment Status: " + "Pending");
            }
        } else {
            ohbinding.custpaystat.setText("Payment Status: " + "Paid");
        }
        ohbinding.custname3.setText("Name: " + vieworderViewModel.getGetordinfo().getValue().getCustomer_name());
        ohbinding.custphone3.setText("Address: " + vieworderViewModel.getGetordinfo().getValue().getCustomer_address());
        ohbinding.custaddr3.setText("Phone: " + vieworderViewModel.getGetordinfo().getValue().getCustomer_number());
        for (int i = 0; i < vieworderViewModel.getGetordinfo().getValue().getOrderstatus().size(); i++) {
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Accepted")) {
                ohbinding.orderprogbar.setProgress(20);
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Packing")) {
                ohbinding.orderprogbar.setProgress(40);
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Ready_For_Delivery")) {
                ohbinding.orderprogbar.setProgress(55);
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Out_For_Delivery")) {
                ohbinding.orderprogbar.setProgress(120);
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Delivered")) {
                ohbinding.orderprogbar.setProgress(120);
                ohbinding.custpaystat.setText("Payment Status: Paid");
            }
        }
    }

    private void loadData() {

        ordproductAdapter = new ordproductAdapter(this, prodlist, vieworderViewModel.getGetordinfo().
                getValue().getProducts());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        ohbinding.itemsrec.setLayoutManager(llm);
        ohbinding.itemsrec.setAdapter(ordproductAdapter);
    }

    private void viewfunctions() {
        ohbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });

        ohbinding.repeatordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shortbol.equals(false)) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                    Call<cartModel.cartResp> call = logregApiInterface.re_order(userid, vieworderViewModel
                            .getGetordinfo().getValue().getOrder_id());

                    call.enqueue(new Callback<cartModel.cartResp>() {
                        @Override
                        public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                            if (!response.isSuccessful()) {
                                Log.d("errocode", String.valueOf(response.code()));
                                return;
                            }

                            cartModel.cartResp resp = response.body();
                            Toast.makeText(ordhisAct.this, "Order Added To Cart.", Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                            Log.d("Failure", String.valueOf(t.getMessage()));
                        }
                    });
                } else {
                    Toast.makeText(ordhisAct.this, "Please Clear Your Cart First.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ohbinding.ordnotrecbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ohbinding.notrecdialog.setVisibility(View.VISIBLE);
                ohbinding.helpcont.setText("Contact: " + storenumb + " (" + storename + ")");
                ohbinding.helpordid.setText("Order Id :" + orderid);
                ohbinding.helpordtot.setText("Total: " + vieworderViewModel.getGetordinfo().getValue()
                        .getTotal_price());
                ohbinding.helppaymeth.setText("Payment Method: " + vieworderViewModel.getGetordinfo().getValue()
                        .getPayment_method());
                ohbinding.helporddate.setText("Order Date: " + vieworderViewModel.getGetordinfo().getValue()
                        .getOrder_date());


            }
        });

        ohbinding.closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ohbinding.notrecdialog.setVisibility(View.GONE);
            }
        });

        ohbinding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    ratings = String.valueOf(rating);


                }
            }
        });

        ohbinding.comsubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratings != null) {
                    putRatings();
                } else {
                    Toast.makeText(ordhisAct.this, "Please Rate First.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void putRatings() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
        Call<loginresResponse.forgotpass> call = logregApiInterface.giveratings(userid, storeid,orderid, ratings,
                ohbinding.ordcomment.getText().toString());

        call.enqueue(new Callback<loginresResponse.forgotpass>() {
            @Override
            public void onResponse(Call<loginresResponse.forgotpass> call, Response<loginresResponse.forgotpass> response) {
                if (!response.isSuccessful()) {
                    Log.d("Errorcode", String.valueOf(response.code()));
                    return;
                }
                loginresResponse.forgotpass resp = response.body();
                if (resp.getmessage().equals("Rating added successfully.!")) {
                    Toast.makeText(ordhisAct.this, "Thanks For Rating!", Toast.LENGTH_SHORT).show();
                    ohbinding.ratingBar.setVisibility(View.INVISIBLE);
                    ohbinding.comsubmitbtn.setVisibility(View.INVISIBLE);
                    ohbinding.ordcomment.setVisibility(View.INVISIBLE);
                    ohbinding.textView46.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(ordhisAct.this, "Error While Rating!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<loginresResponse.forgotpass> call, Throwable t) {
                Log.d("failure", t.getMessage());
            }
        });
    }

    public void checkcartexists() {
        SharedPreferences shpref = getSharedPreferences("userlogged", 0);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}