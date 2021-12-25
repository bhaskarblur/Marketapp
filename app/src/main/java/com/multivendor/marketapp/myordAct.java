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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.multivendor.marketapp.Adapters.ordproductAdapter;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.productitemModel;
import com.multivendor.marketapp.ViewModel.vieworderViewModel;
import com.multivendor.marketapp.databinding.ActivityMyordBinding;

import java.util.ArrayList;
import java.util.List;

public class myordAct extends AppCompatActivity {
    private ActivityMyordBinding mybinding;
    private com.multivendor.marketapp.Adapters.ordproductAdapter ordproductAdapter;
    private com.multivendor.marketapp.ViewModel.vieworderViewModel vieworderViewModel;
    private List<productitemModel> prodlist = new ArrayList<>();
    private String orderid;
    private String storeid;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mybinding = ActivityMyordBinding.inflate(getLayoutInflater());
        setContentView(mybinding.getRoot());
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
            }
        });
        vieworderViewModel.getAllproductModel().observe(this, new Observer<List<productitemModel>>() {
            @Override
            public void onChanged(List<productitemModel> productitemModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (productitemModels.size() > 0) {
                            if (vieworderViewModel.getGetordinfo().getValue() != null) {
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

        viewfunctions();
    }

    private void loadinfo() {

        mybinding.orditemcount.setText(String.valueOf(vieworderViewModel.getGetordinfo()
                .getValue().getProducts().size()));

        int totalPrice =  Integer.parseInt(vieworderViewModel.getGetordinfo().getValue().getTotal_price())
                - Integer.parseInt(vieworderViewModel.getGetordinfo().getValue().getShipping_charge());

        mybinding.orderamoun.setText("₹ " + totalPrice);

        mybinding.orddelvchar.setText("₹ " + vieworderViewModel.getGetordinfo().getValue()
                .getShipping_charge());

        mybinding.grandtotal.setText("₹ "+vieworderViewModel.getGetordinfo().getValue().getTotal_price());

        if (vieworderViewModel.getGetordinfo().getValue().getPayment_method() != null) {
            if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("prepaid")) {
                if (vieworderViewModel.getGetordinfo().getValue().getTransaction_id() != null) {
                    mybinding.custpaymeth.setText("Payment Method: " + "UPI " + "(Txn:" + vieworderViewModel.getGetordinfo().getValue().getTransaction_id() + ")");
                } else {
                    mybinding.custpaymeth.setText("Payment Method: " + "UPI");
                }

            } else if ((vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("cod"))) {
                mybinding.custpaymeth.setText("Payment Method: " + "COD");
            }
        } else {
            mybinding.custpaymeth.setText("Payment Method: " + "");
        }
        if (!vieworderViewModel.getGetordinfo().getValue().getStatus().equals("Delivered")) {
            if (vieworderViewModel.getGetordinfo().getValue().getPayment_method() != null) {
                if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("prepaid")) {
                    mybinding.custpaystat.setText("Payment Status: " + "Paid");
                } else if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("cod")) {
                    mybinding.custpaystat.setText("Payment Status: " + "Pending");
                }
            } else {
                mybinding.custpaystat.setText("Payment Status: " + "Pending");
            }
        } else {
            mybinding.custpaystat.setText("Payment Status: " + "Paid");
        }
        mybinding.custname4.setText("Name: " + vieworderViewModel.getGetordinfo().getValue().getCustomer_name());
        mybinding.custphone.setText("Address: " + vieworderViewModel.getGetordinfo().getValue().getCustomer_address());
        mybinding.custaddr.setText("Phone: " + vieworderViewModel.getGetordinfo().getValue().getCustomer_number());

        for (int i = 0; i < vieworderViewModel.getGetordinfo().getValue().getOrderstatus().size(); i++) {
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Accepted")) {
                mybinding.orderprogbar.setProgress(20);
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Packing")) {
                mybinding.orderprogbar.setProgress(40);
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Ready_To_Ship")) {
                mybinding.orderprogbar.setProgress(55);
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Out_For_Delivery")) {
                mybinding.orderprogbar.setProgress(80);
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().equals("Delivered")) {
                mybinding.orderprogbar.setProgress(100);
                mybinding.orstatustxt.setVisibility(View.VISIBLE);
                mybinding.orstatustxt.setText("Order Delivered");
            }
            if (vieworderViewModel.getGetordinfo().getValue().getOrderstatus().get(i).getStatus().toString().equals("Cancelled") ||
                    vieworderViewModel.getGetordinfo().getValue().getStatus().equals("Cancelled")) {
                //oast.makeText(this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                mybinding.orderprogbar.setProgress(0);
                mybinding.orstatustxt.setVisibility(View.VISIBLE);
                mybinding.orstatustxt.setText("Order Cancelled (Reason: " + vieworderViewModel.getGetordinfo().getValue().getCancel_reason() + ")");
                //   mybinding..setVisibility(View.GONE);
                //      mybinding.ordrejbtn.setVisibility(View.GONE);
                //  mybinding.orstatustxt.setVisibility(View.VISIBLE);
                //mybinding.orstatustxt.setText("Order Cancelled (Reason: "+quickordResult.getCancel_reason()+")");
            }

        }

    }

    private void loadData() {
        ordproductAdapter = new ordproductAdapter(this, prodlist, vieworderViewModel.getGetordinfo().
                getValue().getProducts());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mybinding.itemsrec.setLayoutManager(llm);
        mybinding.itemsrec.setAdapter(ordproductAdapter);
    }

    private void viewfunctions() {

        mybinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}