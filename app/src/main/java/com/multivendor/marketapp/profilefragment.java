package com.multivendor.marketapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.databinding.FragmentProfilefragmentBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class profilefragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentProfilefragmentBinding pfbinding;
    private SharedPreferences sharedPreferences;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profilefragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static profilefragment newInstance(String param1, String param2) {
        profilefragment fragment = new profilefragment();
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
        pfbinding = FragmentProfilefragmentBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");
        viewfunctions();
        loadData(userid);
        return pfbinding.getRoot();


    }

    private void viewfunctions() {

        pfbinding.helplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://wa.me/918765255956"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        pfbinding.termscondlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://marketapp.co.in/conditions.html"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        pfbinding.ratepstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("\"market://details?id=" + getActivity().getPackageName());
                Intent gotoapp = new Intent(Intent.ACTION_VIEW, uri);
                gotoapp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                gotoapp.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                try {
                    startActivity(gotoapp);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });

        pfbinding.sendfeedtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("\"market://details?id=" + getActivity().getPackageName());
                Intent gotoapp = new Intent(Intent.ACTION_VIEW, uri);
                gotoapp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                gotoapp.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                try {
                    startActivity(gotoapp);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });
        pfbinding.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharemsg = new Intent(Intent.ACTION_VIEW);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Market App");
                    String shareMessage= "Install Market App Now.\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        pfbinding.settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), profilesettingsact.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            }
        });

        pfbinding.logouttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("" +
                        "Are you sure, you want to log out?").setTitle("Log Out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getActivity(), Loginact.class));
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        pfbinding.myorderlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myordhisFragment home = new myordhisFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, home);
                transaction.addToBackStack("B");
                transaction.commit();
            }
        });

        pfbinding.ordershistlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myordhisFragment home = new myordhisFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, home);
                transaction.addToBackStack("B");
                transaction.commit();
            }
        });

        pfbinding.favorderlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favshopFragment home = new favshopFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, home);
                transaction.addToBackStack("B");
                transaction.commit();
            }
        });
        pfbinding.addbooklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    addressbookFragment home = new addressbookFragment();
               // FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
              //  transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
              //  transaction.replace(R.id.mainfragment, home);
             //   transaction.addToBackStack("B");
            //    transaction.commit();
            }
        });
    }

    private void loadData(String userId) {

        String imgurl = sharedPreferences.getString("userimage", "data");
        String name = sharedPreferences.getString("username", "");
        String address = sharedPreferences.getString("useraddress", "");

        pfbinding.userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("image",imgurl);
                zoom_imageDialog zoom_imageDialog1 = new zoom_imageDialog();
                zoom_imageDialog1.setArguments(bundle);
                zoom_imageDialog1.show(getParentFragmentManager(), "zoom_imagDialog1");
            }

        });
        if (imgurl != null) {
            final int radius = 50;
            final int margin = 50;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            if (imgurl != null && !imgurl.equals("data")) {
                Picasso.get().load(imgurl).transform(new CropCircleTransformation()).into(pfbinding.userimg);
            }
            else if(imgurl.equals("data")){
                Picasso.get().load(R.drawable.sampleuserimg1).into(pfbinding.userimg);
            }
        }

        if (name != null) {

            pfbinding.username.setText(name);
        }
        if (address != null) {
            pfbinding.useraddress.setText(address);

        }
        else {
            pfbinding.useraddress.setText("No Address");
        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<loginresResponse.login> call = logregApiInterface.getprofile(userId);

        call.enqueue(new Callback<loginresResponse.login>() {
            @Override
            public void onResponse(Call<loginresResponse.login> call, Response<loginresResponse.login> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }
                loginresResponse.login resp = response.body();
                if (resp.getmessage() != null) {
                    Log.d("message", resp.getmessage());
                }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                if(resp.getResult()!=null) {
                    if (resp.getResult().getImage() != null) {

                        editor.putString("userimage", resp.getResult().getImage());
                        //     Picasso.get().load(resp.getResult().getImage()).fit().into(pfbinding.shopimg);
                        editor.apply();
                    }
                    if (resp.getResult().getName() != null) {
                        editor.putString("username", resp.getResult().getName());
                        //    pfbinding.shopname.setText(resp.getResult().getStore_name());
                        editor.apply();
                    }
                    if (resp.getResult().getAddress() != null) {

                        editor.putString("useraddress", resp.getResult().getAddress());
                        //     pfbinding.shopaddress.setText(resp.getResult().getAddress());
                        editor.apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<loginresResponse.login> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}