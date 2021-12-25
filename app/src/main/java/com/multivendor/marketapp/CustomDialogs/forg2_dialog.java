package com.multivendor.marketapp.CustomDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class forg2_dialog extends AppCompatDialogFragment {
    TextView otp;
    TextView newpass;
    Button btn;
    String phone;
    View progbar;
    View back;
    Boolean clicked=false;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.forg_pass2,null);
        builder.setView(view);
        setCancelable(false);
        otp=view.findViewById(R.id.otpet);
        newpass=view.findViewById(R.id.newpasset);
        btn=view.findViewById(R.id.otpsend);
        back=view.findViewById(R.id.backbtn);
        progbar=view.findViewById(R.id.progressBar6);
        Bundle bundle=getArguments();
        if(bundle!=null) {
            phone=bundle.getString("mobile");
            Toast.makeText(getActivity(),"OTP Sent At "+phone, Toast.LENGTH_SHORT).show();
        }
        viewfunc();

        return builder.create();
    }

    private void viewfunc() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.multivendor.marketapp.CustomDialogs.forg1_dialog forg1_dialog=new com.multivendor.marketapp.CustomDialogs.forg1_dialog();
                forg1_dialog.show(getParentFragmentManager(),"forg1_dialog");
                dismiss();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().isEmpty()) {
                    otp.setError("Enter OTP.");
                    Toast.makeText(getActivity(), "Please Enter Your OTP.", Toast.LENGTH_SHORT).show();
                }

                else if(otp.getText().toString().length()<3 || otp.getText().toString().length()>4) {
                    otp.setError("Enter A Valid OTP");
                    Toast.makeText(getActivity(), "Please Enter A Valid OTP.", Toast.LENGTH_SHORT).show();
                }
                else if(newpass.getText().toString().isEmpty()) {
                    newpass.setError("Enter New Password");
                    Toast.makeText(getActivity(), "Please Enter Your New Password!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (clicked.equals(false)) {
                        clicked = true;
                        progbar.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.INVISIBLE);
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                        Call<loginresResponse.verifyforgpass> call = logregApiInterface.verifyforgpass(phone.toString(), otp.getText().toString(),
                                newpass.getText().toString());

                        call.enqueue(new Callback<loginresResponse.verifyforgpass>() {
                            @Override
                            public void onResponse(Call<loginresResponse.verifyforgpass> call, Response<loginresResponse.verifyforgpass> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("Error Code", String.valueOf(response.code()));
                                }

                                loginresResponse.verifyforgpass verifyforgpass = response.body();
                                // Log.d("result", verifyforgpass.getPasschange());
                                Log.d("number",phone.toString());
                                 Log.d("msg",verifyforgpass.getMessage());
                                if (verifyforgpass.getMessage().equals("Password Updated Successfully.!")) {
                                    Toast.makeText(getActivity(), "Password Changed", Toast.LENGTH_SHORT).show();
                                    Log.d("Password Changed","yes");
                                    progbar.setVisibility(View.GONE);
                                    btn.setVisibility(View.VISIBLE);
                                    dismiss();
                                } else if(verifyforgpass.getMessage().toString().contains("wrong otp")){
                                    Toast.makeText(getContext(), "Incorrect OTP.", Toast.LENGTH_SHORT).show();
                                    Log.d("Password Changed","no");
                                    progbar.setVisibility(View.GONE);
                                    btn.setVisibility(View.VISIBLE);
                                    clicked = false;
                                }
                                else {
                                    Toast.makeText(getContext(), "There Was An Error.", Toast.LENGTH_SHORT).show();
                                    clicked = false;
                                    progbar.setVisibility(View.GONE);
                                    btn.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<loginresResponse.verifyforgpass> call, Throwable t) {
                                Log.d("error", t.getMessage());
                                progbar.setVisibility(View.GONE);
                                btn.setVisibility(View.VISIBLE);
                                clicked = false;
                            }
                        });

                    }
                }



            }
        });
    }
}
