package com.keng.dooplus.thetv;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.keng.dooplus.thetv.database.AISLiveTVChannelDatabase;
import com.keng.dooplus.thetv.database.AISLiveTVConntentTypeDatabase;
import com.keng.dooplus.thetv.database.AISLiveTVDatabaseConnection;
import com.keng.dooplus.thetv.database.AsyncDatabase;
import com.keng.dooplus.thetv.utils.AppPreference;
import com.keng.dooplus.thetv.utils.MyConfiguration;
import com.keng.dooplus.thetv.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AISLiveTVLandingPage extends AppCompatActivity implements AsyncDatabase {

    protected boolean _active = true;
    protected int _splashTime = 1600; // time to display the splash screen in ms
    private boolean doubleBackToExitPressedOnce = false;

    private ProgressBar progressBar;

    private AppPreference appPreference;
    private Utils utils;

    OkHttpClient okHttpClient;
    Request.Builder builder;
    Request request;
    RequestBody formBody;
    private String urlAutoLogin = MyConfiguration.HOST + MyConfiguration.AUTO_LOGIN_URL;
    private String urlOTPValidate = MyConfiguration.OTP_VALIDATE_URL;
    private String urlSignin = MyConfiguration.HOST + MyConfiguration.SIGN_IN;

    AISLiveTVConntentTypeDatabase databaseContentType;
    AISLiveTVChannelDatabase databaseChannel;
    private SQLiteDatabase sqliteContentType = null;
    private SQLiteDatabase sqliteChannel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        appPreference = new AppPreference(this);
        utils = new Utils(this);

        databaseContentType = new AISLiveTVConntentTypeDatabase(this, sqliteContentType);
        //sqliteContentType = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        databaseChannel = new AISLiveTVChannelDatabase(this, sqliteChannel);
        //sqliteChannel = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Intent intent = new Intent();
                    intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVRequestOTP");
                    //startActivity(intent);
                    //finish();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //stuff that updates ui
                            autoLogin();
                        }
                    });
                }
            }
        };
        splashTread.start();
    }

    public void onBackPressed() {
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), getText(R.string.txt_landingpage_back_to_exit).toString(), Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);*/
    }

    public void autoLogin() {
        progressBar.setVisibility(View.VISIBLE);
        okHttpClient = new OkHttpClient();
        builder = new Request.Builder();
        formBody = new FormEncodingBuilder()
                .add("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                .add("ch_update", appPreference.getChannelUpdateTime())
                .add("epg_update", appPreference.getEPGUpdateTime())
                .add("app_version", utils.getAppVersion())
                .build();
        request = builder.url(urlAutoLogin).post(formBody).build();
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
                        //textResult.setText(strResult);
                        checkAutoLoginResult(result);
                    }
                });
            }
        });
    }

    public void checkAutoLoginResult(String result) {
        Log.d("AISLiveTV", "Result: " + result);
        //progressBar.setVisibility(View.GONE);
        try {
            Log.d("AISLiveTV", "Get Channel");
            Log.d("AISLiveTV", "Data: " + result);
            Log.d("AISLiveTV", "deviceID: " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            Log.d("AISLiveTV", "lastChUpdate1: " + appPreference.getChannelUpdateTime());
            Log.d("AISLiveTV", "lastEpgUpdate1: " + appPreference.getEPGUpdateTime());

            String lastChUpdate = "";
            String lastEpgUpdate = "";
            String channelData = "";
            try {
                JSONObject jObj = new JSONObject(result);
                lastChUpdate = jObj.getString("lastChUpdate");
                lastEpgUpdate = jObj.getString("lastEpgUpdate");
                Log.d("AISLiveTV", "lastChUpdate2: " + lastChUpdate);
                Log.d("AISLiveTV", "lastEpgUpdate2: " + lastEpgUpdate);

                if(!appPreference.getChannelUpdateTime().equals(lastChUpdate)) {
                    JSONObject jObjChannelUpdate = jObj.getJSONObject("ch_update");
                    channelData = jObjChannelUpdate.toString();
                    AISLiveTVDatabaseConnection databaseConnection = new AISLiveTVDatabaseConnection(AISLiveTVLandingPage.this);
                    databaseConnection.setAppPreference(appPreference);
                    databaseConnection.setDatabaseContentType(databaseContentType);
                    databaseConnection.setDatabaseChannel(databaseChannel);
                    databaseConnection.execute(channelData);
                } else {
                    databaseCallback("");
                }
                if(!appPreference.getEPGUpdateTime().equals(lastEpgUpdate)) {
                    JSONObject jObjEPGUpdate = jObj.getJSONObject("epg_update");
                    //epgData = jObjEPGUpdate.toString();
                    //writeEPGToFile(epgData);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("AISLiveTV", "JSONException: " + e.toString());
                databaseCallback("");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("AISLiveTV", "Exception: " + e.toString());
            databaseCallback("");
        }
    }

    @Override
    public void databaseCallback(String result) {
        progressBar.setVisibility(View.GONE);

        sqliteChannel = null;
        sqliteContentType = null;

		/*if(appPreference.getLoginStatus() || !appPreference.getFacebookToken().equals("")) {
        	Intent intent = new Intent();
			intent.setClassName(getPackageName(), getPackageName() + ".MainActivity");
			finish();
			startActivity(intent);
        } else {
        	Intent intent = new Intent();
	    	intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVSignIn");
	    	startActivity(intent);
	        finish();
        }*/

        //EPGConnection connection = new EPGConnection(this);
        //connection.execute(getEPGURL);

        /*if(chTypeCodeList == null) {
            chTypeCodeList = getContentTypeIDList();
        }

        AISLiveTVEPGDatabaseConnection epgDatabaseConnection = new AISLiveTVEPGDatabaseConnection(this);
        epgDatabaseConnection.setDatabaseEPG(databaseEPG);
        epgDatabaseConnection.setChTypeCodeList(chTypeCodeList);*/
        //epgDatabaseConnection.execute(epgData);

        TelephonyManager telephonyManager =((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
        String simOperatorName = telephonyManager.getSimOperatorName();
        String networkOperatorName = telephonyManager.getNetworkOperatorName();

        if(appPreference.getLoginStatus() || simOperatorName.toLowerCase().contains("ais") || networkOperatorName.toLowerCase().contains("ais") || simOperatorName.toLowerCase().contains("gsm") || networkOperatorName.toLowerCase().contains("gsm") || simOperatorName.toLowerCase().contains("advance") || networkOperatorName.toLowerCase().contains("advance")) {
            appPreference.setLoginStatus(true);
            appPreference.setPermissionCode("C");
            Log.d("AISLiveTV", "LandingPageV2 to MainActivity");
            //appPreference.setPhone("");
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVMain");
            finish();
            startActivity(intent);
        } else {
            Log.d("AISLiveTV", "LandingPageV2 to AISLiveTVSignInV2");
            Intent intent = new Intent();
            //intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVSignInV2");
            intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVRequestOTP");
            intent.putExtra("isAutoLogin", false);
            startActivity(intent);
            finish();
        }
    }
}
