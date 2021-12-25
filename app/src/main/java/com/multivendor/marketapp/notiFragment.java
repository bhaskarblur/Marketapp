package com.multivendor.marketapp;

import android.content.SharedPreferences;
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

import com.multivendor.marketapp.Adapters.notiAdapter;
import com.multivendor.marketapp.Models.notiModel;
import com.multivendor.marketapp.ViewModel.notiViewModel;
import com.multivendor.marketapp.databinding.FragmentNotiBinding;

import java.util.List;


public class notiFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentNotiBinding nfbinding;
    private notiViewModel notiViewModel;
    private com.multivendor.marketapp.Adapters.notiAdapter notiAdapter;
    public notiFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static notiFragment newInstance(String param1, String param2) {
        notiFragment fragment = new notiFragment();
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
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userlogged",0);
        String userid=sharedPreferences.getString("userid","");
        notiViewModel=new ViewModelProvider(getActivity()).get(com.multivendor.marketapp.ViewModel.notiViewModel.class);
        notiViewModel.initwork(userid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nfbinding=FragmentNotiBinding.inflate(inflater,container,false);
        notiViewModel.getNotiData().observe(getActivity(), new Observer<List<notiModel.notiResult>>() {
            @Override
            public void onChanged(List<notiModel.notiResult> notiResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(notiResults.size()>0) {
                            notiAdapter.notifyDataSetChanged();
                        }
                        else {
                            nfbinding.nonotilay.setVisibility(View.VISIBLE);
                            nfbinding.notilay.setVisibility(View.INVISIBLE);

                        }
                    }
                },200);
            }
        });
        viewfunctions();
        loadNotis();
        return nfbinding.getRoot();
    }

    private void loadNotis() {
        notiAdapter=new notiAdapter(getActivity(),notiViewModel.getNotiData().getValue());
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        nfbinding.notirec.setLayoutManager(llm);
        nfbinding.notirec.setAdapter(notiAdapter);
    }

    private void viewfunctions() {
        nfbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homefragment home=new homefragment();
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_right);
                transaction.replace(R.id.mainfragment,home);
                transaction.commit();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getViewModelStore().clear();
    }
}