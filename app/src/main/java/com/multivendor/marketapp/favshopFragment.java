package com.multivendor.marketapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multivendor.marketapp.Adapters.favshoplat;
import com.multivendor.marketapp.Adapters.nbyshopAdapter;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.nbyshopsModel;
import com.multivendor.marketapp.Models.userAPIResp;
import com.multivendor.marketapp.databinding.FragmentFavshopBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class favshopFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentFavshopBinding fsbinding;
    private com.multivendor.marketapp.Adapters.favshoplat adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<nbyshopsModel> shoplist = new ArrayList<>();

    public favshopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favshopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static favshopFragment newInstance(String param1, String param2) {
        favshopFragment fragment = new favshopFragment();
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
        fsbinding = FragmentFavshopBinding.inflate(inflater, container, false);
        viewfunctions();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");
        Call<userAPIResp.getStores> call = logregApiInterface.get_favshop(userid);
        call.enqueue(new Callback<userAPIResp.getStores>() {
            @Override
            public void onResponse(Call<userAPIResp.getStores> call, Response<userAPIResp.getStores> response) {
                if (!response.isSuccessful()) {
                    Log.d("errocode:", String.valueOf(response.code()));
                    return;
                }

                userAPIResp.getStores getStores = response.body();
                if(shoplist.size()<1) {
                    if (getStores.getResult() != null) {
                        for (nbyshopsModel shopmodel : getStores.getResult()) {
                            shoplist.add(shopmodel);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

            }


            @Override
            public void onFailure(Call<userAPIResp.getStores> call, Throwable t) {

            }
        });

        loadData();
        return fsbinding.getRoot();
    }

    private void loadData() {
        adapter = new favshoplat(getActivity(), shoplist);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        fsbinding.favshopsrec.setLayoutManager(llm);
        fsbinding.favshopsrec.setAdapter(adapter);
    }

    private void viewfunctions() {
        fsbinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilefragment home = new profilefragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainfragment, home);
                transaction.commit();
            }
        });
    }
}