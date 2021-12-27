package com.multivendor.marketapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multivendor.marketapp.Adapters.reviewAdapter;
import com.multivendor.marketapp.Adapters.showszAdapter;


public class fragmentnewProduct extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private com.multivendor.marketapp.Adapters.reviewAdapter reviewAdapter;
    private showszAdapter sizeadapter;
    public fragmentnewProduct() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static fragmentnewProduct newInstance(String param1, String param2) {
        fragmentnewProduct fragment = new fragmentnewProduct();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmentnew_product, container, false);
    }
}