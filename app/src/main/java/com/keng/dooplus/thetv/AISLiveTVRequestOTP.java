package com.keng.dooplus.thetv;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.keng.dooplus.thetv.customview.RSUTextView;
import com.keng.dooplus.thetv.utils.AppPreference;
import com.keng.dooplus.thetv.utils.MyConfiguration;
import com.keng.dooplus.thetv.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Dooplus on 11/18/15 AD.
 */
public class AISLiveTVRequestOTP extends AppCompatActivity {

    private ProgressBar progressBar;
    private ProgressBar progressBarDialog;
    private EditText inputPhone;
    private Button btnSubmit;
    private RSUTextView txtEmailSignIn;
    private Typeface tf;

    private EditText inputEmail;
    private EditText inputPassword;

    private AppPreference appPreference;
    private Utils utils;

    OkHttpClient okHttpClient;
    Request.Builder builder;
    Request request;
    RequestBody formBody;
    private String urlOTPRequest = MyConfiguration.OTP_REQUEST_URL;
    private String urlSignin = MyConfiguration.HOST + MyConfiguration.SIGN_IN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_otp);

        appPreference = new AppPreference(this);
        utils = new Utils(this);

        tf = Typeface.createFromAsset(this.getAssets(), "fonts/rsu-light.ttf");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        inputPhone = (EditText) findViewById(R.id.inputPhone);
        inputPhone.setTypeface(tf);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(tf);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = inputPhone.getText().toString();
                if(phone.length() == 10) {
                    getOTP(phone);
                } else {
                    Toast.makeText(getApplicationContext(), getText(R.string.otp_request_error_phone).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtEmailSignIn = (RSUTextView) findViewById(R.id.txtEmailSignIn);
        txtEmailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AISLiveTVRequestOTP.this);
                dialog.setContentView(R.layout.dialog_request_otp_email_sign_in);
                dialog.setTitle(getText(R.string.otp_request_dialog_title));

                progressBarDialog = (ProgressBar) dialog.findViewById(R.id.progressBar);

                inputEmail = (EditText) dialog.findViewById(R.id.inputEmail);
                inputEmail.setTypeface(tf);

                inputPassword = (EditText) dialog.findViewById(R.id.inputPassword);
                inputPassword.setTypeface(tf);

                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCancel.setTypeface(tf);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button btnSignIn = (Button) dialog.findViewById(R.id.btnSignIn);
                btnSignIn.setTypeface(tf);
                btnSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBarDialog.setVisibility(View.VISIBLE);
                        String email = inputEmail.getText().toString();
                        String password = inputPassword.getText().toString();
                        signIn(urlSignin, email, password, "");
                    }
                });

                dialog.show();
            }
        });
    }
    
    public void getOTP(String phone) {

        String device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        appPreference.setDeviceID(device_id);
        appPreference.setPhone(phone);

        progressBar.setVisibility(View.VISIBLE);
        okHttpClient = new OkHttpClient();
        builder = new Request.Builder();
        formBody = new FormEncodingBuilder()
                .add("cmd", MyConfiguration.OTP_COMMAND_REQUEST)
                .add("msisdn", phone)
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
                        checkOTPResult(result);
                    }
                });
            }
        });
    }

    public void checkOTPResult(String result) {
        //textResult.setText(strResult);
        Log.d("AISLiveTV", "Result: " + result);
        progressBar.setVisibility(View.GONE);
        try {
            String status = result.split("<SUCCESS>")[1].split("</SUCCESS>")[0].replace(" ", "").toLowerCase();
            String detail = result.split("<DETAIL>")[1].split("</DETAIL>")[0];
            String transactionID = result.split("<TRANSACTIONID>")[1].split("</TRANSACTIONID>")[0];
            if(status.equals("true")) {
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVValidateOTP");
                intent.putExtra("transactionID", transactionID);
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

    public void signIn(String url, String email, String password, String fbID) {
        String device_id = appPreference.getDeviceID();
        String device_model = Build.MODEL;
        String device_name = Build.MANUFACTURER;
        String sys_name = utils.getOSVersionName();
        String sys_version = "" + Build.VERSION.RELEASE;
        String app_version = utils.getAppVersion();
        String carrier = utils.getCarrierName();
        String connection = utils.getConnectionType();
        String city = "";
        String state = "";
        String country = "";
        String locale = utils.getLocale();
        String zipcode = "";

        //isSignInConnection = true;
        okHttpClient = new OkHttpClient();
        builder = new Request.Builder();
        formBody = new FormEncodingBuilder()
                .add("email", email)
                .add("password", password)
                .build();
        request = builder.url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                connectionCallBack("onFailure Error - " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    try {
                        connectionCallBack(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        connectionCallBack("IOException Error - " + e.getMessage());
                    }
                } else {
                    connectionCallBack("Not Success - code : " + response.code());
                }
            }

            public void connectionCallBack(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkSignInResult(result);
                    }
                });
            }
        });
    }

    public void checkSignInResult(String result) {
        Log.d("AISLiveTV", "Result: " + result);
        progressBarDialog.setVisibility(View.GONE);
        try {
            //isSignInConnection = false;
            String activityName = "";
            JSONObject jObj = new JSONObject(result);
            String status = jObj.getString("status");
            String detail = jObj.getString("detail");
            String mobile = jObj.getString("mobile");
            //String username = jObj.getString("username");
            //Toast.makeText(getApplicationContext(), "status: " + status, Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "detail: " + detail, Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "mobile: " + mobile, Toast.LENGTH_LONG).show();

            Log.d("AISLiveTV", "Android Version: " + Build.VERSION.SDK_INT);
            if(status.equals("1") || status.equals("2") || status.equals("")) {
                if(Build.VERSION.SDK_INT > 15) {
                    activityName = ".AISLiveTVMain";
                } else {
                    activityName = ".AISLiveTVMainNoDrawer";
                }
                appPreference.setLoginStatus(true);
                appPreference.setPermissionCode(detail);
                appPreference.setPhone(mobile);
                appPreference.setUsername(inputEmail.getText().toString());
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName() + activityName);
                intent.putExtra("chTypeID", 1);
                finish();
                startActivity(intent);
            } else if(status.equals("4")) {
                Log.d("AISLIVETV", result);
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVRequestOTP");
                //intent.putExtra("fromFB", isFromFB);
                //finish();
                startActivity(intent);
            } else {
                Log.d("AISLIVETV", result);
                Toast.makeText(getApplicationContext(), detail, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
