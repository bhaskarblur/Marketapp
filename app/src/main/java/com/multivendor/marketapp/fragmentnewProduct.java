package com.multivendor.marketapp;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multivendor.marketapp.Adapters.bannerAdapter;
import com.multivendor.marketapp.Adapters.productImageAdapter;
import com.multivendor.marketapp.Adapters.reviewAdapter;
import com.multivendor.marketapp.Adapters.showszAdapter;
import com.multivendor.marketapp.ApiWork.ApiWork;
import com.multivendor.marketapp.Constants.api_baseurl;
import com.multivendor.marketapp.Models.newProductModel;
import com.multivendor.marketapp.databinding.FragmentFragmentnewProductBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class fragmentnewProduct extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private com.multivendor.marketapp.Adapters.reviewAdapter reviewAdapter;
    private showszAdapter sizeadapter;
    private String selected_size;
    private String product_id;
    private FragmentFragmentnewProductBinding binding;
    private List<newProductModel.reviewResult> reviewList = new ArrayList<>();
    private List<newProductModel.sizeandquat> sizeList = new ArrayList<>();
    api_baseurl baseurl = new api_baseurl();
    private List<newProductModel.productImage> imageList = new ArrayList<>();
    private String userid;
    private com.multivendor.marketapp.Adapters.productImageAdapter imagesAdapter;
    private List<newProductModel.productCartresp> inCartList = new ArrayList<>();

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
        Bundle bundle = getArguments();
        product_id = bundle.getString("product_id");
        userid = getActivity().getSharedPreferences("userlogged", 0).getString("userid", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFragmentnewProductBinding.inflate(inflater, container, false);
        Managefuncs();
        viewfuncs();
        loadData();
        return binding.getRoot();
    }

    private void viewfuncs() {
        binding.onbprog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imagebanner.setCurrentItem(0, true);
                binding.onbprog.getBackground().setTint(Color.parseColor("#0881E3"));
                binding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        binding.onbprog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imagebanner.setCurrentItem(1, true);
                binding.onbprog2.getBackground().setTint(Color.parseColor("#0881E3"));
                binding.onbprog.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        binding.onbprog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imagebanner.setCurrentItem(2, true);
                binding.onbprog3.getBackground().setTint(Color.parseColor("#0881E3"));
                binding.onbprog.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog4.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        binding.onbprog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imagebanner.setCurrentItem(3, true);
                binding.onbprog4.getBackground().setTint(Color.parseColor("#0881E3"));
                binding.onbprog2.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog3.getBackground().setTint(Color.parseColor("#C6C6C6"));
                binding.onbprog.getBackground().setTint(Color.parseColor("#C6C6C6"));
            }
        });

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        binding.heartofficon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to favourite
            }
        });

        binding.heartonicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove from favourite;
            }
        });
    }


    private void Managefuncs() {

        sizeadapter = new showszAdapter(getContext(), sizeList);
        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        llm1.setOrientation(RecyclerView.HORIZONTAL);
        binding.sizerec.setLayoutManager(llm1);
        binding.sizerec.setAdapter(sizeadapter);
        sizeadapter.setonbtnclickListener(new showszAdapter.onbtnclick() {
            @Override
            public void onCLICK(int position, String id) {
                selected_size = id.toString();
                if (inCartList.size() > 0) {
                    for (int i = 0; i < inCartList.size(); i++) {
                        if (inCartList.get(i).getVariant_id().equals(selected_size)) {
                            binding.addctLay.setVisibility(View.INVISIBLE);
                            binding.qtytxt.setText(inCartList.get(i).getQuantity().toString());
                            binding.plusLay.setVisibility(View.VISIBLE);
                            binding.minusLay.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            binding.addctLay.setVisibility(View.VISIBLE);
                            binding.qtytxt.setText("0");
                            binding.plusLay.setVisibility(View.INVISIBLE);
                            binding.minusLay.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    binding.addctLay.setVisibility(View.VISIBLE);
                    binding.plusLay.setVisibility(View.INVISIBLE);
                    binding.qtytxt.setText("0");
                    binding.minusLay.setVisibility(View.INVISIBLE);
                }
            }
        });

        reviewAdapter = new reviewAdapter(getContext(), reviewList);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        llm2.setOrientation(RecyclerView.VERTICAL);
        binding.reviewrec.setLayoutManager(llm2);
        binding.reviewrec.setAdapter(reviewAdapter);

    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.apibaseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<newProductModel.productdetailResp> call = apiWork.getproduct_details(userid, product_id);

        call.enqueue(new Callback<newProductModel.productdetailResp>() {
            @Override
            public void onResponse(Call<newProductModel.productdetailResp> call, Response<newProductModel.productdetailResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                newProductModel.productdetailResp productdata = response.body();


                Log.d("message", productdata.getSuccess());

                if (productdata.getResult() != null) {
                    if (productdata.getResult().getProduct_images().size() > 0) {
                        imageList = productdata.getResult().getProduct_images();
                        imagesAdapter = new productImageAdapter(getActivity(), imageList);
                        binding.imagebanner.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                        binding.imagebanner.setAdapter(imagesAdapter);
                        binding.imagebanner.setCurrentItem(0);

                        if (productdata.getResult().getProduct_images().size() < 4) {
                            binding.onbprog4.setVisibility(View.INVISIBLE);
                        }
                        if (productdata.getResult().getProduct_images().size() < 3) {
                            binding.onbprog3.setVisibility(View.INVISIBLE);
                            binding.onbprog4.setVisibility(View.INVISIBLE);
                        }
                        if (productdata.getResult().getProduct_images().size() < 2) {
                            binding.onbprog2.setVisibility(View.INVISIBLE);
                            binding.onbprog3.setVisibility(View.INVISIBLE);
                            binding.onbprog4.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (productdata.getResult().getProduct_variants().size() > 0) {
                        sizeList = productdata.getResult().getProduct_variants();
                        selected_size = sizeList.get(0).getVariation_id().toString();
                        sizeadapter.notifyDataSetChanged();
                    }
                    if (productdata.getResult().getProduct_reviews().size() > 0) {
                        reviewList = productdata.getResult().getProduct_reviews();
                        reviewAdapter.notifyDataSetChanged();
                    }
                    if (productdata.getResult().getIn_cart().size() > 0) {
                        inCartList = productdata.getResult().getIn_cart();
                    }
                    binding.productname.setText(productdata.getResult().getProduct_name().toString());
                    binding.productdesc.setText(productdata.getResult().getProduct_description().toString());
                    binding.prodprice.setText(productdata.getResult().getProduct_price().toString());
                    binding.cutprice.setText(productdata.getResult().getProduct_cut_price().toString());
                    binding.cutprice.setPaintFlags(binding.cutprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.disctxt.setText(productdata.getResult().getDiscount_rate());
                    if(productdata.getResult().getIn_favourites().equals("yes")) {
                        binding.heartonicon.setVisibility(View.VISIBLE);
                        binding.heartofficon.setVisibility(View.INVISIBLE);
                    }
                    else {
                        binding.heartonicon.setVisibility(View.INVISIBLE);
                        binding.heartofficon.setVisibility(View.VISIBLE);
                    }
                    if (productdata.getResult().getIn_cart() != null) {
                        for (int i = 0; i < productdata.getResult().getIn_cart().size(); i++) {
                            if (productdata.getResult().getIn_cart().get(i).getVariant_id().equals(selected_size)) {
                                binding.addctLay.setVisibility(View.INVISIBLE);
                                binding.qtytxt.setText(productdata.getResult().getIn_cart().get(i).getQuantity().toString());
                                binding.plusLay.setVisibility(View.VISIBLE);
                                binding.minusLay.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        binding.addctLay.setVisibility(View.VISIBLE);
                        binding.qtytxt.setText("0");
                        binding.plusLay.setVisibility(View.INVISIBLE);
                        binding.minusLay.setVisibility(View.INVISIBLE);
                    }

                }


            }

            @Override
            public void onFailure(Call<newProductModel.productdetailResp> call, Throwable t) {
                Log.d("errorDetail", t.getMessage().toString());
            }
        });

    }
}