package com.multivendor.marketapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.multivendor.marketapp.databinding.FragmentMyordhisBinding;

import java.util.List;


public class myordhisFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentMyordhisBinding myordhisBinding;
    private com.multivendor.marketapp.Adapters.homequickordAdapter homequickordAdapter;
    private com.multivendor.marketapp.Adapters.ordlayAdapter ordersAdapter;
    private com.multivendor.marketapp.ViewModel.orderViewModel orderViewModel;
    String userid;
    public myordhisFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static myordhisFragment newInstance(String param1, String param2) {
        myordhisFragment fragment = new myordhisFragment();
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
        myordhisBinding=FragmentMyordhisBinding.inflate(inflater,container,false);
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
        orderViewModel.getQuickordModel().observe(getActivity(), new Observer<List<quickorderModel.quickordResult>>() {
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
        return myordhisBinding.getRoot();
    }

    private void loadordhisrec() {
        homequickordAdapter=new homequickordAdapter(getActivity(),orderViewModel.getQuickordModel().getValue());
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        //llm.setReverseLayout(true);
        //llm.setStackFromEnd(true);
        myordhisBinding.ordhistrec.setLayoutManager(llm);
        myordhisBinding.ordhistrec.setAdapter(homequickordAdapter);

    }

    private void loadorderrec() {
        ordersAdapter=new ordlayAdapter(getActivity(),orderViewModel.getOrderModel().getValue());
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        myordhisBinding.myordrec.setLayoutManager(llm);
        myordhisBinding.myordrec.setAdapter(ordersAdapter);
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

        myordhisBinding.myordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myordhisBinding.myordtxt.setTextColor(Color.parseColor("#0881E3"));
                myordhisBinding.myordtxt.setText("My Orders");
                myordhisBinding.ordhistxt.setText("Quick Orders");
                myordhisBinding.ordhistxt.setTextColor(Color.parseColor("#686868"));
                myordhisBinding.ordhistlay.setVisibility(View.INVISIBLE);
                myordhisBinding.myordlay.setVisibility(View.VISIBLE);
                myordhisBinding.myordsel.setVisibility(View.VISIBLE);
                myordhisBinding.ordhissel.setVisibility(View.INVISIBLE);
            }
        });

        myordhisBinding.ordhistxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myordhisBinding.ordhistxt.setTextColor(Color.parseColor("#0881E3"));
                myordhisBinding.ordhistxt.setText("Quick Orders");
                myordhisBinding.myordtxt.setText("My Orders");
                myordhisBinding.myordtxt.setTextColor(Color.parseColor("#686868"));
                myordhisBinding.ordhistlay.setVisibility(View.VISIBLE);
                myordhisBinding.myordlay.setVisibility(View.INVISIBLE);
                myordhisBinding.myordsel.setVisibility(View.INVISIBLE);
                myordhisBinding.ordhissel.setVisibility(View.VISIBLE);
            }
        });
        myordhisBinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilefragment home=new profilefragment();
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_right);
                transaction.replace(R.id.mainfragment,home);
                transaction.commit();
            }
        });
    }
}