package com.keng.dooplus.thetv;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.keng.dooplus.thetv.utils.AppPreference;
import com.keng.dooplus.thetv.utils.MyConfiguration;
import com.keng.dooplus.thetv.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Dooplus on 11/18/15 AD.
 */
public class AISLiveTVValidateOTP extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText inputOTP;
    private Button btnCancel;
    private Button btnRequestPassword;
    private Button btnSubmit;
    private Typeface tf;

    private AppPreference appPreference;
    private Utils utils;

    OkHttpClient okHttpClient;
    Request.Builder builder;
    Request request;
    RequestBody formBody;
    private String urlOTPRequest = MyConfiguration.OTP_REQUEST_URL;
    private String urlOTPValidate = MyConfiguration.OTP_VALIDATE_URL;
    private String urlSignin = MyConfiguration.HOST + MyConfiguration.SIGN_IN;

    Bundle extras;
    String transactionID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validate_otp);

        appPreference = new AppPreference(this);
        utils = new Utils(this);

        extras = getIntent().getExtras();
        transactionID = extras.getString("transactionID");

        tf = Typeface.createFromAsset(this.getAssets(), "fonts/rsu-light.ttf");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        inputOTP = (EditText) findViewById(R.id.inputOTP);
        inputOTP.setTypeface(tf);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(tf);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = inputOTP.getText().toString();
                if (otp.length() == 4) {
                    //isOTPRequest = false;
                    appPreference.setOTP(otp);

                    progressBar.setVisibility(View.VISIBLE);
                    okHttpClient = new OkHttpClient();
                    builder = new Request.Builder();
                    formBody = new FormEncodingBuilder()
                            .add("msisdn", appPreference.getPhone())
                            .add("TransactionID", transactionID)
                            .add("cmd", MyConfiguration.OTP_COMMAND_VALIDATE)
                            .add("OTP", otp)
                            .add("u", MyConfiguration.OTP_USERNAME)
                            .add("p", MyConfiguration.OTP_PASSWORD)
                            .add("service", MyConfiguration.OTP_SERVICE)
                            .build();
                    request = builder.url(urlOTPValidate).post(formBody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            connectionCallBack("Error - " + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) {
                            if (response.isSuccessful()) {
                                try {
                                    connectionCallBack(response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    connectionCallBack("Error - " + e.getMessage());
                                }
                            } else {
                                connectionCallBack("Not Success - code : " + response.code());
                            }
                        }

                        public void connectionCallBack(final String result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    checkValidateOTPResult(result);
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.otp_validate_error_otp).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRequestPassword = (Button) findViewById(R.id.btnRequestPassword);
        btnRequestPassword.setTypeface(tf);
        btnRequestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                okHttpClient = new OkHttpClient();
                builder = new Request.Builder();
                formBody = new FormEncodingBuilder()
                        .add("cmd", MyConfiguration.OTP_COMMAND_REQUEST)
                        .add("msisdn", appPreference.getPhone())
                        .add("u", MyConfiguration.OTP_USERNAME)
                        .add("p", MyConfiguration.OTP_PASSWORD)
                        .add("service", MyConfiguration.OTP_SERVICE)
                        .build();
                request = builder.url(urlOTPRequest).post(formBody).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        connectionCallBack("Error - " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response) {
                        if (response.isSuccessful()) {
                            try {
                                connectionCallBack(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                connectionCallBack("Error - " + e.getMessage());
                            }
                        } else {
                            connectionCallBack("Not Success - code : " + response.code());
                        }
                    }

                    public void connectionCallBack(final String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkRequestOTPResult(result);
                            }
                        });
                    }
                });
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setTypeface(tf);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVRequestOTP");
                //intent.putExtra("isAutoLogin", false);
                startActivity(intent);
            }
        });
    }

    public void checkValidateOTPResult(String result) {
        Log.d("AISLiveTV", "Result: " + result);
        progressBar.setVisibility(View.GONE);
        try {
            String status = result.split("<SUCCESS>")[1].split("</SUCCESS>")[0].replace(" ", "").toLowerCase();
            String detail = result.split("<DETAIL>")[1].split("</DETAIL>")[0];
            String transactionID = result.split("<TRANSACTIONID>")[1].split("</TRANSACTIONID>")[0];
            if (status.equals("true")) {
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVMain");
                //intent.putExtra("transactionID", transactionID);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), detail, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void checkRequestOTPResult(String result) {
        Log.d("AISLiveTV", "Result: " + result);
        progressBar.setVisibility(View.GONE);
        try {
            String status = result.split("<SUCCESS>")[1].split("</SUCCESS>")[0].replace(" ", "").toLowerCase();
            String detail = result.split("<DETAIL>")[1].split("</DETAIL>")[0];
            String transactionID = result.split("<TRANSACTIONID>")[1].split("</TRANSACTIONID>")[0];
            if (status.equals("true")) {
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVForgetPasswordOTPValidate");
                intent.putExtra("transactionID", transactionID);
                //startActivity(intent);
                //finish();
            } else {
                Toast.makeText(getApplicationContext(), detail, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
