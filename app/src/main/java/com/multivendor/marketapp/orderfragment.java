package com.multivendor.marketapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multivendor.marketapp.Adapters.homequickordAdapter;
import com.multivendor.marketapp.Adapters.ordlayAdapter;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.Models.ordersModel;
import com.multivendor.marketapp.Models.quickorderModel;
import com.multivendor.marketapp.databinding.FragmentOrderfragmentBinding;

import java.util.List;


public class orderfragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private com.multivendor.marketapp.Adapters.ordlayAdapter ordersAdapter;
    private com.multivendor.marketapp.Adapters.homequickordAdapter homequickordAdapter;
    private FragmentOrderfragmentBinding ofbinding;
    private com.multivendor.marketapp.ViewModel.orderViewModel orderViewModel;
    String userid;

    public orderfragment() {
        // Required empty public constructor
    }


    public static orderfragment newInstance(String param1, String param2) {
        orderfragment fragment = new orderfragment();
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
        ofbinding=FragmentOrderfragmentBinding.inflate(inflater,container,false);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
        userid=sharedPreferences.getString("userid","");
        orderViewModel=new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketapp.ViewModel.orderViewModel.class);
        orderViewModel.initwork(userid);
        orderViewModel.getOrderModel().observe(getActivity(), new Observer<List<cartModel.singlecartResult>>() {
            @Override
            public void onChanged(List<cartModel.singlecartResult> ordersModels) {
                if(ordersModels!=null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ordersAdapter.notifyDataSetChanged();
                        }
                    },100);

                }
            }
        });
        orderViewModel.getQuickordModel().observe(getActivity(), new Observer<List<quickorderModel .quickordResult>>() {
            @Override
            public void onChanged(List<quickorderModel .quickordResult> ordersModels) {
                if(ordersModels!=null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            homequickordAdapter.notifyDataSetChanged();
                        }
                    },100);

                }
            }
        });
        loadorderrec();
        loadordhisrec();
        viewfunctions();
        return ofbinding.getRoot();


    }

    private void loadordhisrec() {
        homequickordAdapter=new homequickordAdapter(getActivity(),orderViewModel.getQuickordModel().getValue());
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        //  llm.setReverseLayout(true);
        //  llm.setStackFromEnd(true);
        ofbinding.ordhistrec.setLayoutManager(llm);
        ofbinding.ordhistrec.setAdapter(homequickordAdapter);

    }

    private void loadorderrec() {
        ordersAdapter=new ordlayAdapter(getActivity(),orderViewModel.getOrderModel().getValue());
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        //llm.setReverseLayout(true);
      //  llm.setStackFromEnd(true);
        ofbinding.myordrec.setLayoutManager(llm);
        ofbinding.myordrec.setAdapter(ordersAdapter);
        ordersAdapter.setoncardClick(new ordlayAdapter.onorderClick() {
            @Override
            public void oncardclick(int position) {
                if(ordersAdapter.ordlist.get(position).getStatus().equals("Delivered")) {
                    Intent intent=new Intent(getContext(),ordhisAct.class);
                    intent.putExtra("userid",ordersAdapter.ordlist.get(position)
                            .getUser_id());
                    intent.putExtra("storeid",ordersAdapter.ordlist.get(position).getStore());
                    intent.putExtra("orderid",ordersAdapter.ordlist.get(position)
                            .getOrder_id());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                }
                else {
                    Intent intent=new Intent(getContext(),myordAct.class);
                    intent.putExtra("userid",ordersAdapter.ordlist.get(position)
                            .getUser_id());
                    intent.putExtra("storeid",ordersAdapter.ordlist.get(position).getStore());
                    intent.putExtra("orderid",ordersAdapter.ordlist.get(position)
                            .getOrder_id());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                }

            }
        });


    }

    private void viewfunctions() {

        ofbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ofbinding.myordtxt.setText("My Orders");

        ofbinding.myordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ofbinding.myordtxt.setTextColor(Color.parseColor("#0881E3"));
                ofbinding.myordtxt.setText("My Orders");
                ofbinding.ordhistxt.setText("Quick Orders");
                ofbinding.ordhistxt.setTextColor(Color.parseColor("#686868"));
                ofbinding.ordhistlay.setVisibility(View.INVISIBLE);
                ofbinding.myordlay.setVisibility(View.VISIBLE);
                ofbinding.myordsel.setVisibility(View.VISIBLE);
                ofbinding.ordhissel.setVisibility(View.INVISIBLE);
            }
        });

        ofbinding.ordhistxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ofbinding.ordhistxt.setTextColor(Color.parseColor("#0881E3"));
                ofbinding.ordhistxt.setText("Quick Orders");
                ofbinding.myordtxt.setText("My Orders");
                ofbinding.myordtxt.setTextColor(Color.parseColor("#686868"));
                ofbinding.ordhistlay.setVisibility(View.VISIBLE);
                ofbinding.myordlay.setVisibility(View.INVISIBLE);
                ofbinding.myordsel.setVisibility(View.INVISIBLE);
                ofbinding.ordhissel.setVisibility(View.VISIBLE);
            }
        });
    }
}