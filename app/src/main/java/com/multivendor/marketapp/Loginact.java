package com.multivendor.marketapp;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.multivendor.marketapp.ApiWork.LogregApiInterface;
import com.multivendor.marketapp.CustomDialogs.forg1_dialog;
import com.multivendor.marketapp.Fragments.LoginFragment;
import com.multivendor.marketapp.Models.loginresResponse;
import com.multivendor.marketapp.databinding.ActivityLoginactBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Loginact extends AppCompatActivity {

    ActivityLoginactBinding lgbinding;
    private SharedPreferences setregist;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lgbinding = ActivityLoginactBinding.inflate(getLayoutInflater());
        setContentView(lgbinding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        SharedPreferences getregist = getSharedPreferences("userlogged", 0);
        setregist = getSharedPreferences("userlogged", 0);
        String logcheck = getregist.getString("userlogged", "");

        if (logcheck.equals("yes")) {
            startActivity(new Intent(Loginact.this, Mainarea.class));
            finish();
        }

    }

//    private void getfirebaseToken() {
//
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        token = task.getResult();
//
//                        // Log and toast
//                        String msg = token;
//                        Log.d("token", msg);
//                    }
//                });
//    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();

            if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
                // set the home tab as default;


                LoginFragment homeFragment=new LoginFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.auth_fragcontainer, homeFragment);
                transaction.commit();

            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).
                    setTitle("Exit?").setMessage("Do you want to exit the app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }

    }

//    private void viewfunc() {
//
//        lgbinding.passinvisimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //show pass and change icon
//                lgbinding.passinvisimg.setVisibility(View.INVISIBLE);
//                lgbinding.passvisimg.setVisibility(View.VISIBLE);
//                lgbinding.passtxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                lgbinding.passtxt.setSelection(lgbinding.passtxt.getText().length());
//            }
//        });
//
//
//        lgbinding.passvisimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //show pass and change icon
//                lgbinding.passinvisimg.setVisibility(View.VISIBLE);
//                lgbinding.passvisimg.setVisibility(View.INVISIBLE);
//                lgbinding.passtxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                lgbinding.passtxt.setSelection(lgbinding.passtxt.getText().length());
//            }
//        });
//    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}