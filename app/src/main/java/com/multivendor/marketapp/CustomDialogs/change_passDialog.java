package com.multivendor.marketapp.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class change_passDialog extends AppCompatDialogFragment {
    TextView pass;
    Button btn;
    Boolean clicked=false;
    View progbar;
    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.change_pass,null);
        builder.setView(view);
        progbar=view.findViewById(R.id.progressBar6);
        pass=view.findViewById(R.id.newpasset);
        btn=view.findViewById(R.id.savepass);

        viewfunc();

        return builder.create();
    }

    private void viewfunc() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().isEmpty()) {
                    pass.setError("Please Enter A New Password!");
                }
                else {
                    if(clicked.equals(false)) {
                        clicked = true;
                        progbar.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.INVISIBLE);
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
                        String userid=sharedPreferences.getString("userid","");
                        Call<loginresResponse.verifyforgpass> call = logregApiInterface.changepass(userid,pass.getText().toString());
                        call.enqueue(new Callback<loginresResponse.verifyforgpass>() {
                            @Override
                            public void onResponse(Call<loginresResponse.verifyforgpass> call, Response<loginresResponse.verifyforgpass> response) {
                                if (!response.isSuccessful()) {

                                    Log.d("Code", response.message().toString());
                                }
                                loginresResponse.verifyforgpass resp = response.body();

                                //Log.d("resultotp", resp.getOtpstat());

                                if (resp.getMessage().equals("Password change successfully.!")) {
                                    Toast.makeText(getActivity(), "Password Changed!", Toast.LENGTH_SHORT).show();
                                    clicked=false;
                                    progbar.setVisibility(View.GONE);
                                    btn.setVisibility(View.VISIBLE);
                                    dismiss();

                                }
                                else {
                                    progbar.setVisibility(View.GONE);
                                    btn.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "There Was An Error.", Toast.LENGTH_SHORT).show();
                                    clicked=false;
                                }
                            }

                            @Override
                            public void onFailure(Call<loginresResponse.verifyforgpass> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                clicked=false;
                                progbar.setVisibility(View.GONE);
                                btn.setVisibility(View.VISIBLE);

                            }
                        });

                    }
                }
            }
        });
    }
}
