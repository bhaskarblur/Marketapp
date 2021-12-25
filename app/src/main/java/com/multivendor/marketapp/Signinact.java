package com.multivendor.marketapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.databinding.ActivitySigninactBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signinact extends AppCompatActivity {
    ActivitySigninactBinding sgbinding;
    private Boolean otpsent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sgbinding = ActivitySigninactBinding.inflate(getLayoutInflater());
        setContentView(sgbinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        viewfunc();


        sgbinding.logingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signinact.this, com.multivendor.marketapp.Loginact.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });


    }

    private void viewfunc() {

        sgbinding.passinvisimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show pass and change icon
                sgbinding.passinvisimg2.setVisibility(View.INVISIBLE);
                sgbinding.passvisimg2.setVisibility(View.VISIBLE);
                sgbinding.signpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                sgbinding.signpass.setSelection(sgbinding.signpass.getText().length());
            }
        });


        sgbinding.passvisimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show pass and change icon
                sgbinding.passinvisimg2.setVisibility(View.VISIBLE);
                sgbinding.passvisimg2.setVisibility(View.INVISIBLE);
                sgbinding.signpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                sgbinding.signpass.setSelection(sgbinding.signpass.getText().length());
            }
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        sgbinding.getotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sgbinding.signphone.getText().toString().isEmpty()) {
                    sgbinding.signphone.setError("Enter Number.");
                    Toast.makeText(Signinact.this, "Please Enter Mobile Number First.", Toast.LENGTH_SHORT).show();
                } else if (sgbinding.signphone.getText().toString().length() < 10) {
                    sgbinding.signphone.setError("Invalid Number.");
                    Toast.makeText(Signinact.this, "Invalid Mobile Number.", Toast.LENGTH_SHORT).show();
                } else {
                    if (otpsent.equals(true)) {
                        Toast.makeText(Signinact.this, "Please Wait for 5 second to send again.", Toast.LENGTH_SHORT).show();
                    } else if (otpsent == false) {
                        otpsent = true;
                        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                        Call<loginresResponse.sendotp> call = logregApiInterface.sendOTP(sgbinding.signphone.getText().toString());
                        call.enqueue(new Callback<loginresResponse.sendotp>() {
                            @Override
                            public void onResponse(Call<loginresResponse.sendotp> call, Response<loginresResponse.sendotp> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("Code", response.message().toString());
                                    return;
                                }
                                sgbinding.getotpbtn.setText("Resend");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        otpsent = false;
                                    }
                                }, 5000);
                                loginresResponse.sendotp resp = response.body();

                                //  Log.d("successotp", resp.getsuccesss());
                                if (resp.getstat() != null) {
                                    Log.d("otp_status", resp.getstat());
                                }

                                Log.d("msg", resp.getmessage());
                                if (resp.getmessage().equals("OTP Send.!") && resp.getstat().equals("success")) {
                                    Toast.makeText(Signinact.this, "OTP Sent.", Toast.LENGTH_SHORT).show();
                                } else if (resp.getmessage().toString().contains("Phone number already registered.!")) {
                                    Toast.makeText(Signinact.this, "This Account Already Exists!", Toast.LENGTH_SHORT).show();
                                    sgbinding.getotpbtn.setText("Get OTP");
                                    otpsent=false;

                                } else {
                                    Toast.makeText(Signinact.this, "There Was An Error In Sending OTP.", Toast.LENGTH_SHORT).show();
                                    sgbinding.getotpbtn.setText("Get OTP");
                                    otpsent=false;
                                }


                            }

                            @Override
                            public void onFailure(Call<loginresResponse.sendotp> call, Throwable t) {
                                otpsent = true;
                                Toast.makeText(Signinact.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            }
        });


        sgbinding.signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sgbinding.signname.getText().toString().isEmpty()) {
                    sgbinding.signname.setError("Enter Name.");
                    Toast.makeText(Signinact.this, "Please Enter Your Name.", Toast.LENGTH_SHORT).show();
                } else if (sgbinding.signphone.getText().toString().isEmpty()) {
                    sgbinding.signname.setError("Enter Number..");
                    Toast.makeText(Signinact.this, "Please Enter Your Number.", Toast.LENGTH_SHORT).show();
                } else if (sgbinding.signpass.getText().toString().isEmpty()) {
                    Toast.makeText(Signinact.this, "Please Enter Your Password.", Toast.LENGTH_SHORT).show();
                } else if (sgbinding.taccheck.isChecked() == false) {
                    Toast.makeText(Signinact.this, "Please Accept The Terms And Conditions.", Toast.LENGTH_SHORT).show();
                } else {
                    sgbinding.progressBar.setVisibility(View.VISIBLE);
                    sgbinding.signinbtn.setVisibility(View.INVISIBLE);
                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);
                    Call<loginresResponse.register> call = logregApiInterface.signin(sgbinding.signname.getText().toString().trim(), sgbinding.signphone.getText().toString(),
                            sgbinding.signotp.getText().toString(), sgbinding.signpass.getText().toString(), "user");

                    call.enqueue(new Callback<loginresResponse.register>() {
                        @Override
                        public void onResponse(Call<loginresResponse.register> call, Response<loginresResponse.register> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Error Code", String.valueOf(response.code()));
                                return;
                            }

                            loginresResponse.register resp = response.body();
                            if (resp.getRegistration() != null) {
                                Log.d("stats", resp.getRegistration());
                            }
                            // Log.d("message", resp.getmessage().toString());
                            if (resp.getRegistration() != null) {
                                if (resp.getRegistration().equals("success")) {
                                    Toast.makeText(Signinact.this, "Registered!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Signinact.this, com.multivendor.marketapp.Loginact.class));
                                    finish();
                                    sgbinding.progressBar.setVisibility(View.GONE);
                                    sgbinding.signinbtn.setVisibility(View.VISIBLE);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                }
                            } else if (resp.getmessage().equals("Invalid otp or phone.!")) {
                                sgbinding.progressBar.setVisibility(View.GONE);
                                sgbinding.signinbtn.setVisibility(View.VISIBLE);
                                Toast.makeText(Signinact.this, "Incorrect OTP!", Toast.LENGTH_SHORT).show();
                            } else if (resp.getmessage().toString().contains("pre-registered")) {
                                sgbinding.progressBar.setVisibility(View.GONE);
                                sgbinding.signinbtn.setVisibility(View.VISIBLE);
                                Toast.makeText(Signinact.this, "This Account Already Exists!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Signinact.this, "There was an error.", Toast.LENGTH_SHORT).show();
                            }
                            sgbinding.progressBar.setVisibility(View.GONE);
                            sgbinding.signinbtn.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onFailure(Call<loginresResponse.register> call, Throwable t) {
                            Log.d("Error", t.toString());
                            sgbinding.progressBar.setVisibility(View.GONE);
                            sgbinding.signinbtn.setVisibility(View.VISIBLE);
                        }
                    });


                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();

    }
}