package com.multivendor.marketapp.CustomDialogs;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.cartModel;
import com.multivendor.marketapp.R;
import com.multivendor.marketapp.catalogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class clearcartDialog extends AppCompatDialogFragment {
    Button clearcart;
    Button cancel;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.cartexists_dialog,null);
        builder.setView(view);

        clearcart=view.findViewById(R.id.clearcartbtn);
        cancel=view.findViewById(R.id.cancelbtn);

        viewfunc();
        return builder.create();
    }

    private void viewfunc() {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
        String userid=sharedPreferences.getString("userid","");
        clearcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<cartModel.cartResp> call=logregApiInterface.clear_cart(userid);

                call.enqueue(new Callback<cartModel.cartResp>() {
                    @Override
                    public void onResponse(Call<cartModel.cartResp> call, Response<cartModel.cartResp> response) {
                        if(!response.isSuccessful()) {
                            Log.d("errorcode",String.valueOf(response.code()));
                            return;
                        }

                        cartModel.cartResp resp=response.body();


                        if(resp.getMessage().equals("Clear Cart successfully")) {

                            Toast.makeText(getActivity(), "Cart cleared!", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                        else {
                            Toast.makeText(getActivity(), "There was an error!", Toast.LENGTH_SHORT).show();
                            dismiss();

                        }
                    }

                    @Override
                    public void onFailure(Call<cartModel.cartResp> call, Throwable t) {
                        Log.d("error",t.getMessage());
                        dismiss();
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogFragment catalogFragment=new catalogFragment();
                catalogFragment.checkcartexists();
                catalogFragment.checksamestore();
                catalogFragment.LoadCart();
                dismiss();
            }
        });
    }

}
