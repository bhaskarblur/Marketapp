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

public class forg1_dialog extends AppCompatDialogFragment {
  TextView number;
  Button btn;
  View progbar;
  Boolean clicked=false;
  @NonNull
  @Override
  public Dialog onCreateDialog( Bundle savedInstanceState) {
    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
    LayoutInflater inflater=getActivity().getLayoutInflater();
    View view=inflater.inflate(R.layout.forg_pass1,null);
    builder.setView(view);
    progbar=view.findViewById(R.id.progressBar6);
    number=view.findViewById(R.id.signphone2);
    btn=view.findViewById(R.id.otpsend);

    viewfunc();

    return builder.create();
  }

  private void viewfunc() {
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(number.getText().toString().isEmpty()) {
          number.setError("Enter Number.");
          Toast.makeText(getContext(), "Please Enter Your Number.", Toast.LENGTH_SHORT).show();
        }

        else if(number.getText().toString().length()<10 || number.getText().toString().length()>10) {
          number.setError("Enter A Valid Number.");
          Toast.makeText(getContext(), "Please Enter A Valid Number.", Toast.LENGTH_SHORT).show();
        }

        else {
          if(clicked.equals(false)) {
            clicked = true;
            progbar.setVisibility(View.VISIBLE);
            btn.setVisibility(View.INVISIBLE);
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                    .addConverterFactory(GsonConverterFactory.create()).build();

            LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

            Call<loginresResponse.forgotpass> call = logregApiInterface.doforgotpass(number.getText().toString());
            call.enqueue(new Callback<loginresResponse.forgotpass>() {
              @Override
              public void onResponse(Call<loginresResponse.forgotpass> call, Response<loginresResponse.forgotpass> response) {
                if (!response.isSuccessful()) {

                  Log.d("Code", response.message().toString());
                }
                loginresResponse.forgotpass resp = response.body();

                //Log.d("resultotp", resp.getOtpstat());

                if (resp.getmessage().equals("OTP Send.!")) {
                  progbar.setVisibility(View.GONE);
                  btn.setVisibility(View.VISIBLE);
                  Bundle bundle = new Bundle();
                  bundle.putString("mobile", number.getText().toString());
                  com.multivendor.marketapp.CustomDialogs.forg2_dialog forg2_dialog = new forg2_dialog();
                  forg2_dialog.setArguments(bundle);
                  forg2_dialog.setCancelable(false);
                  forg2_dialog.show(getParentFragmentManager(), "forg2_dialog");

                  dismiss();

                }
                else if(resp.getmessage().toString().contains("not registered")) {
                  clicked=false;
                  progbar.setVisibility(View.GONE);
                  btn.setVisibility(View.VISIBLE);
                  Toast.makeText(getContext(), "No Such Account Exists.", Toast.LENGTH_SHORT).show();
                }
                else {
                  progbar.setVisibility(View.GONE);
                  btn.setVisibility(View.VISIBLE);
                  Toast.makeText(getContext(), "There Was An Error.", Toast.LENGTH_SHORT).show();
                  clicked=false;
                }
              }

              @Override
              public void onFailure(Call<loginresResponse.forgotpass> call, Throwable t) {
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
