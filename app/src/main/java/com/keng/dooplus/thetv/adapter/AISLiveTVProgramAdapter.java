package com.keng.dooplus.thetv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.keng.dooplus.thetv.R;
import com.keng.dooplus.thetv.database.AISLiveTVChannelDatabase;
import com.keng.dooplus.thetv.fragment.AISLiveTVFragmentHome;
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
import java.util.ArrayList;

/**
 * Created by Dooplus on 11/18/15 AD.
 */
public class AISLiveTVProgramAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;

    AISLiveTVChannelDatabase database;
    private SQLiteDatabase sqliteChannel = null;
    private final String DB_NAME = "AISLIVETV";
    private final String CHANNEL_TABLE_NAME = "CHANNEL";

    Utils utils;
    AppPreference appPreference;
    ProgressBar progressBar;
    AutoCompleteTextView txtAutoComplete;
    VideoView videoView;
    TextView txtChannelName;
    Typeface tf;

    private AISLiveTVProgramAdapter adapter;
    private ArrayList<String> contentTypeCodeList;
    private ArrayList<String> channelIDList;
    private ArrayList<String> channelNameList;
    private ArrayList<String> channelSubNameList;
    private ArrayList<String> channelImageList;
    private ArrayList<String> channelImageDarkList;
    private ArrayList<Integer> channelFavoriteList;
    private ArrayList<String> channelImageBase64List;
    private ArrayList<String> channelChargingList;
    int chTypeID = 0;


    String urlVideo = "http://radio2.thaidhost.com:8888/show/live/playlist.m3u8";

    OkHttpClient okHttpClient;
    Request.Builder builder;
    Request request;
    RequestBody formBody;

    String getVideoChannelURL = MyConfiguration.HOST + MyConfiguration.GET_VIDEO_CHANNEL;
    String checkFoxMemberURL = MyConfiguration.HOST + MyConfiguration.GET_FOX;
    String getFoxChannelURL = MyConfiguration.HOST + MyConfiguration.GET_FOX_CHANNEL;
    String registerFoxChannelURL = MyConfiguration.HOST + MyConfiguration.GET_FOX_PASSWORD;
    String foxHowToUseURL = MyConfiguration.HOST + MyConfiguration.FOX_HOW_TO_USE;

    public AISLiveTVProgramAdapter(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        utils = new Utils(activity);
        appPreference = new AppPreference(activity);

        database = new AISLiveTVChannelDatabase(activity, sqliteChannel);
        sqliteChannel = activity.openOrCreateDatabase(DB_NAME, activity.MODE_PRIVATE, null);

        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/rsu-light.ttf");
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setAutoCompleteTextView(AutoCompleteTextView txtAutoComplete) {
        this.txtAutoComplete = txtAutoComplete;
    }

    public void setVideoView(VideoView videoView) {
        this.videoView = videoView;
    }

    public void setTextChannelName(TextView txtChannelName) {
        this.txtChannelName = txtChannelName;
    }

    public void setAdapter(AISLiveTVProgramAdapter adapter) {
        this.adapter = adapter;
    }

    public void setContentTypeCodeList(ArrayList<String> contentTypeCodeList) {
        this.contentTypeCodeList = contentTypeCodeList;
    }

    public void setChannelIDList(ArrayList<String> channelIDList) {
        this.channelIDList = channelIDList;
    }

    public void setChannelNameList(ArrayList<String> channelNameList) {
        this.channelNameList = channelNameList;
    }

    public void setChannelSubNameList(ArrayList<String> channelSubNameList) {
        this.channelSubNameList = channelSubNameList;
    }

    public void setChannelImageList(ArrayList<String> channelImageList) {
        this.channelImageList = channelImageList;
    }

    public void setChannelImageDarkList(ArrayList<String> channelImageDarkList) {
        this.channelImageDarkList = channelImageDarkList;
    }

    public void setChannelFavoriteList(ArrayList<Integer> channelFavoriteList) {
        this.channelFavoriteList = channelFavoriteList;
    }

    public void setChannelImageBase64List(ArrayList<String> channelImageBase64List) {
        this.channelImageBase64List = channelImageBase64List;
    }

    public void setChannelChargingList(ArrayList<String> channelChargingList) {
        this.channelChargingList = channelChargingList;
    }

    public void setChTypeID(int chTypeID) {
        this.chTypeID = chTypeID;
    }

    @Override
    public int getCount() {
        return channelNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public RelativeLayout layout;
        public ImageView imgChannel;
        public ImageButton btnFavorite;
        public TextView txtName;
        public TextView txtSubName;
    }

    ViewHolder holder;
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View vi = convertView;
        vi = inflater.inflate(R.layout.channel_list_item, null);
        holder = new ViewHolder();
        holder.layout = (RelativeLayout) vi.findViewById(R.id.layout);
        holder.txtName = (TextView) vi.findViewById(R.id.txtName);
        holder.txtSubName = (TextView) vi.findViewById(R.id.txtSubName);
        holder.imgChannel = (ImageView) vi.findViewById(R.id.imgChannel);
        holder.btnFavorite = (ImageButton) vi.findViewById(R.id.btnFavorite);

        holder.txtName.setText(channelNameList.get(position));
        holder.txtName.setTypeface(tf);

        holder.txtSubName.setText(channelSubNameList.get(position));
        holder.txtSubName.setTypeface(tf);

        //holder.imgChannel.setTag(channelImageList.get(position));
        //imageLoader.DisplayImage(channelImageList.get(position), activity, holder.imgChannel);
        holder.imgChannel.setImageBitmap(Utils.getBitmapFromBase64(channelImageBase64List.get(position)));

        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("AISLiveTV", "Adapter setOnClickListener");
                if(appPreference.getIsThaiLanguage()) {
                    txtChannelName.setText(channelNameList.get(position));
                } else {
                    txtChannelName.setText(channelNameList.get(position));
                }

                if(appPreference.getPermissionCode().toLowerCase().equals("f") || channelChargingList.get(position).toLowerCase().equals("free")) {
                    if(channelIDList.get(position).length() > 1) {
                        appPreference.setChannelID(channelIDList.get(position));
                        String chTypeCode = appPreference.getContentTypeCode();
                        String versionApp = utils.getAppVersion();
                        String chID = channelIDList.get(position);
                        String connection = utils.getConnectionType();
                        String carrier = utils.getCarrierName();
                        String deviceDateTime = "";
                        String udid = appPreference.getDeviceID();
                        String phoneNo = appPreference.getPhone();
                        String fromApp = "Android";
                        //String deviceType = "" + utils.getOSVersionName();
                        String deviceType = "Android";
                        String osVersion = "" + Build.VERSION.RELEASE;
                        String street = "-";
                        String city = "-";
                        String state = "-";
                        String country = "-";
                        String locale = utils.getLocale();
                        String zipcode = "-";
                        String channelType = "" + chTypeID;
                        String quality = appPreference.getChannelResolution();

                        Log.d("AISLiveTV", "DeviceType: " + deviceType);
                        Log.d("AISLiveTV", "chTypeCode: " + chTypeCode);
                        Log.d("AISLiveTV", "chID: " + ":" + chID);
                        //Toast.makeText(activity, "phoneNo:" + phoneNo, Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.VISIBLE);
                        okHttpClient = new OkHttpClient();
                        builder = new Request.Builder();
                        formBody = new FormEncodingBuilder()
                                .add("ch_type_code", chTypeCode)
                                .add("version_app", versionApp)
                                .add("ch_id", chID)
                                .add("connection", connection)
                                .add("carrier", carrier)
                                .add("device_datetime", deviceDateTime)
                                .add("udid", udid)
                                .add("phone_no", phoneNo)
                                .add("from_app", fromApp)
                                .add("device_type", deviceType)
                                .add("os_version", osVersion)
                                .add("street", street)
                                .add("city", city)
                                .add("state", state)
                                .add("country", country)
                                .add("locale", locale)
                                .add("zipcode", zipcode)
                                .add("channel_type", channelType)
                                .add("quality", quality)
                                .build();
                        request = builder.url(getVideoChannelURL).post(formBody).build();
                        okHttpClient.newCall(request).enqueue(new Callback() {

                            Handler mainHandler = new Handler(Looper.getMainLooper());

                            @Override
                            public void onFailure(Request request, IOException e) {
                                connectionCallBack("Error - " + e.getMessage());
                            }

                            @Override
                            public void onResponse(final Response response) {
                                mainHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
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
                                });
                            }

                            public void connectionCallBack(final String result) {
                                Log.d("AISLiveTV", "Result: " + result);
                                //progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject jObj = new JSONObject(result);
                                    String status = jObj.getString("status");
                                    String title = "";
                                    String detail = "";
                                    String remark = "";
                                    if(jObj.has("title")) {
                                        title = jObj.getString("title");
                                    }
                                    if(jObj.has("detail")) {
                                        detail = jObj.getString("detail");
                                    }
                                    if(jObj.has("remark")) {
                                        remark = jObj.getString("remark");
                                    }
                                    if(status.toLowerCase().replace(" ", "").equals("ok")) {
                                        urlVideo = detail;
                                        //setVideoChannel(path);
                                        Log.d("AISLiveTV", "connectionCallBack detail: " + urlVideo);
                                        AISLiveTVFragmentHome.fragmentHome.setVideoController(urlVideo);
                                    } else if(status.toLowerCase().replace(" ", "").equals("error")) {
                                        //showAlertDialog(detail);
                                    } else if(status.equals("1")) {
                                        //progressBar.setVisibility(View.VISIBLE);
                                        okHttpClient = new OkHttpClient();
                                        builder = new Request.Builder();
                                        //request = builder.url(getFoxChannelURL + "?ch_id=" + foxChannelID + ",device_type=android").build();
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
                                                Log.d("AISLiveTV", "Result: " + result);
                                                //progressBar.setVisibility(View.GONE);
                                                try {
                                                    JSONObject jObj = new JSONObject(result);
                                                    String status = jObj.getString("status");
                                                    String title = jObj.getString("title");
                                                    String detail = jObj.getString("detail");
                                                    String remark = jObj.getString("remark");
                                                    if(status.toLowerCase().replace(" ", "").equals("ok")) {
                                                        urlVideo = detail;
                                                        //setVideoChannel(path);
                                                        //AISLiveTVFragment.setVideoChannel(path);
                                                        //showFoxSuccessDialog(remark);
                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remark));
                                                        activity.startActivity(browserIntent);
                                                        //Intent intent = new Intent();
                                                        //intent.setClassName(activity.getPackageName(), activity.getPackageName() + ".AISLiveTVWebView");
                                                        //activity.startActivity(intent);
                                                    } else if(status.toLowerCase().replace(" ", "").equals("error")) {
                                                        //showAlertDialog(title);
                                                    }
                                                } catch (Exception e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                    //progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    //progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }/* else {
                            Log.d("AISLiveTV", "Fox URL only");
                            foxChannelID = channelIDList.get(position);
                            if(channelNameList.get(position).toLowerCase().contains("movie") || channelSubNameList.get(position).toLowerCase().contains("movie")) {
                                foxURL = "http://www.foxmoviesplay.com";
                            } else if(channelNameList.get(position).toLowerCase().contains("sport") || channelSubNameList.get(position).toLowerCase().contains("sport")) {
                                foxURL = "http://www.foxplayasia.com";
                            } else if(channelNameList.get(position).toLowerCase().contains("scm") || channelSubNameList.get(position).toLowerCase().contains("scm")) {
                                foxURL = "http://www.scmplay.com/movies/";
                            }

                            Log.d("AISLiveTV", "Phone No: " + appPreference.getPhone());
                            //progressBar.setVisibility(View.VISIBLE);
                            okHttpClient = new OkHttpClient();
                            builder = new Request.Builder();
                            formBody = new FormEncodingBuilder()
                                    .add("phone_no", appPreference.getPhone())
                                    .build();
                            request = builder.url(checkFoxMemberURL).post(formBody).build();
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
                                    Log.d("AISLiveTV", "Result: " + result);
                                    //progressBar.setVisibility(View.GONE);
                                    try {
                                        JSONObject jObj = new JSONObject(result);
                                        String status = jObj.getString("status");
                                        String title = jObj.getString("title");
                                        String detail = jObj.getString("user");
                                        String pass = "";
                                        if(jObj.has("pass")) {
                                            pass = jObj.getString("pass");
                                        }
                                        //String remark = jObj.getString("remark");
                                        if(status.equals("0")) {
                                            title += "\n\n" + detail;
                                            showAlertDialog(title);
                                        } else if(status.equals("1")) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(foxURL));
                                            //activity.startActivity(browserIntent);
                                            showFoxSuccessDialog(title, detail);
                                        } else if(status.equals("2")) {
                                            //showPasswordDialog(detail);
                                            showFoxRegisterDialog(detail, pass);
                                        } else {
                                            //progressBar.setVisibility(View.VISIBLE);
                                            okHttpClient = new OkHttpClient();
                                            builder = new Request.Builder();
                                            request = builder.url(getFoxChannelURL + "?ch_id=" + foxChannelID + ",device_type=android").build();
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
                                                    Log.d("AISLiveTV", "Result: " + result);
                                                    //progressBar.setVisibility(View.GONE);
                                                    try {
                                                        JSONObject jObj = new JSONObject(result);
                                                        String status = jObj.getString("status");
                                                        String title = jObj.getString("title");
                                                        String detail = jObj.getString("detail");
                                                        String remark = jObj.getString("remark");
                                                        if(status.toLowerCase().replace(" ", "").equals("ok")) {
                                                            path = detail;
                                                            //setVideoChannel(path);
                                                            //AISLiveTVFragment.setVideoChannel(path);
                                                            //showFoxSuccessDialog(remark);
                                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remark));
                                                            activity.startActivity(browserIntent);
                                                            //Intent intent = new Intent();
                                                            //intent.setClassName(activity.getPackageName(), activity.getPackageName() + ".AISLiveTVWebView");
                                                            //activity.startActivity(intent);
                                                        } else if(status.toLowerCase().replace(" ", "").equals("error")) {
                                                            showAlertDialog(title);
                                                        }
                                                    } catch (Exception e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                        //progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                        //progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }*/
                } else {
                    //showAlertDialog("");
                }
                /*txtAutoComplete.setVisibility(View.GONE);
                if(appPreference.getPermissionCode().toLowerCase().equals("f") || channelChargingList.get(position).toLowerCase().equals("free")) {
                    txtChannelName.setText(channelNameList.get(position));
                    if(channelIDList.get(position).length() > 1) {
                        appPreference.setChannelID(channelIDList.get(position));
                        String chTypeCode = appPreference.getContentTypeCode();
                        String versionApp = utils.getAppVersion();
                        String chID = channelIDList.get(position);
                        String connection = utils.getConnectionType();
                        String carrier = utils.getCarrierName();
                        String deviceDateTime = "";
                        String udid = appPreference.getDeviceID();
                        String phoneNo = appPreference.getPhone();
                        String fromApp = "Android";
                        //String deviceType = "" + utils.getOSVersionName();
                        String deviceType = "Android";
                        String osVersion = "" + Build.VERSION.RELEASE;
                        String street = "-";
                        String city = "-";
                        String state = "-";
                        String country = "-";
                        String locale = utils.getLocale();
                        String zipcode = "-";
                        String channelType = "" + chTypeID;
                        String quality = appPreference.getChannelResolution();

                        Log.d("AISLiveTV", "DeviceType: " + deviceType);
                        Log.d("AISLiveTV", "chTypeCode: " + chTypeCode);
                        Log.d("AISLiveTV", "chID: " + ":" + chID);
                        //Toast.makeText(activity, "phoneNo:" + phoneNo, Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.VISIBLE);
                        okHttpClient = new OkHttpClient();
                        builder = new Request.Builder();
                        formBody = new FormEncodingBuilder()
                                .add("ch_type_code", chTypeCode)
                                .add("version_app", versionApp)
                                .add("ch_id", chID)
                                .add("connection", connection)
                                .add("carrier", carrier)
                                .add("device_datetime", deviceDateTime)
                                .add("udid", udid)
                                .add("phone_no", phoneNo)
                                .add("from_app", fromApp)
                                .add("device_type", deviceType)
                                .add("os_version", osVersion)
                                .add("street", street)
                                .add("city", city)
                                .add("state", state)
                                .add("country", country)
                                .add("locale", locale)
                                .add("zipcode", zipcode)
                                .add("channel_type", channelType)
                                .add("quality", quality)
                                .build();
                        request = builder.url(getVideoChannelURL).post(formBody).build();
                        okHttpClient.newCall(request).enqueue(new Callback() {

                            Handler mainHandler = new Handler(Looper.getMainLooper());

                            @Override
                            public void onFailure(Request request, IOException e) {
                                updateView("Error - " + e.getMessage());
                            }

                            @Override
                            public void onResponse(final Response response) {
                                mainHandler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (response.isSuccessful()) {
                                            try {
                                                updateView(response.body().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                updateView("Error - " + e.getMessage());
                                            }
                                        } else {
                                            updateView("Not Success - code : " + response.code());
                                        }
                                    }
                                });
                            }

                            public void updateView(final String result) {
                                Log.d("AISLiveTV", "Result: " + result);
                                //progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject jObj = new JSONObject(result);
                                    String status = jObj.getString("status");
                                    String title = "";
                                    String detail = "";
                                    String remark = "";
                                    if(jObj.has("title")) {
                                        title = jObj.getString("title");
                                    }
                                    if(jObj.has("detail")) {
                                        detail = jObj.getString("detail");
                                    }
                                    if(jObj.has("remark")) {
                                        remark = jObj.getString("remark");
                                    }
                                    if(status.toLowerCase().replace(" ", "").equals("ok")) {
                                        path = detail;
                                        //setVideoChannel(path);
                                        if(isSignInConnection) {
                                            isSignInConnection = false;
                                            String mobile = jObj.getString("mobile");
                                            appPreference.setPermissionCode(detail);
                                            appPreference.setPhone(mobile);
                                        } else {
                                            //Toast.makeText(activity, "UpdateView", Toast.LENGTH_SHORT).show();
                                            Log.d("AISLiveTV", "UpdateView");
                                            AISLiveTVFragmentV2.setVideoChannel(path);
                                        }
                                    } else if(status.toLowerCase().replace(" ", "").equals("error")) {
                                        showAlertDialog(detail);
                                    } else if(status.equals("1")) {
                                        //progressBar.setVisibility(View.VISIBLE);
                                        okHttpClient = new OkHttpClient();
                                        builder = new Request.Builder();
                                        request = builder.url(getFoxChannelURL + "?ch_id=" + foxChannelID + ",device_type=android").build();
                                        okHttpClient.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Request request, IOException e) {
                                                updateView("Error - " + e.getMessage());
                                            }

                                            @Override
                                            public void onResponse(Response response) {
                                                if (response.isSuccessful()) {
                                                    try {
                                                        updateView(response.body().string());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        updateView("Error - " + e.getMessage());
                                                    }
                                                } else {
                                                    updateView("Not Success - code : " + response.code());
                                                }
                                            }

                                            public void updateView(final String result) {
                                                Log.d("AISLiveTV", "Result: " + result);
                                                //progressBar.setVisibility(View.GONE);
                                                try {
                                                    JSONObject jObj = new JSONObject(result);
                                                    String status = jObj.getString("status");
                                                    String title = jObj.getString("title");
                                                    String detail = jObj.getString("detail");
                                                    String remark = jObj.getString("remark");
                                                    if(status.toLowerCase().replace(" ", "").equals("ok")) {
                                                        path = detail;
                                                        //setVideoChannel(path);
                                                        //AISLiveTVFragment.setVideoChannel(path);
                                                        //showFoxSuccessDialog(remark);
                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remark));
                                                        activity.startActivity(browserIntent);
                                                        //Intent intent = new Intent();
                                                        //intent.setClassName(activity.getPackageName(), activity.getPackageName() + ".AISLiveTVWebView");
                                                        //activity.startActivity(intent);
                                                    } else if(status.toLowerCase().replace(" ", "").equals("error")) {
                                                        showAlertDialog(title);
                                                    }
                                                } catch (Exception e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                    //progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    //progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else {
                        Log.d("AISLiveTV", "Fox URL only");
                        foxChannelID = channelIDList.get(position);
                        if(channelNameList.get(position).toLowerCase().contains("movie") || channelSubNameList.get(position).toLowerCase().contains("movie")) {
                            foxURL = "http://www.foxmoviesplay.com";
                        } else if(channelNameList.get(position).toLowerCase().contains("sport") || channelSubNameList.get(position).toLowerCase().contains("sport")) {
                            foxURL = "http://www.foxplayasia.com";
                        } else if(channelNameList.get(position).toLowerCase().contains("scm") || channelSubNameList.get(position).toLowerCase().contains("scm")) {
                            foxURL = "http://www.scmplay.com/movies/";
                        }

                        Log.d("AISLiveTV", "Phone No: " + appPreference.getPhone());
                        //progressBar.setVisibility(View.VISIBLE);
                        okHttpClient = new OkHttpClient();
                        builder = new Request.Builder();
                        formBody = new FormEncodingBuilder()
                                .add("phone_no", appPreference.getPhone())
                                .build();
                        request = builder.url(checkFoxMemberURL).post(formBody).build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                updateView("Error - " + e.getMessage());
                            }

                            @Override
                            public void onResponse(Response response) {
                                if (response.isSuccessful()) {
                                    try {
                                        updateView(response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        updateView("Error - " + e.getMessage());
                                    }
                                } else {
                                    updateView("Not Success - code : " + response.code());
                                }
                            }

                            public void updateView(final String result) {
                                Log.d("AISLiveTV", "Result: " + result);
                                //progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject jObj = new JSONObject(result);
                                    String status = jObj.getString("status");
                                    String title = jObj.getString("title");
                                    String detail = jObj.getString("user");
                                    String pass = "";
                                    if(jObj.has("pass")) {
                                        pass = jObj.getString("pass");
                                    }
                                    //String remark = jObj.getString("remark");
                                    if(status.equals("0")) {
                                        title += "\n\n" + detail;
                                        showAlertDialog(title);
                                    } else if(status.equals("1")) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(foxURL));
                                        //activity.startActivity(browserIntent);
                                        showFoxSuccessDialog(title, detail);
                                    } else if(status.equals("2")) {
                                        //showPasswordDialog(detail);
                                        showFoxRegisterDialog(detail, pass);
                                    } else {
                                        //progressBar.setVisibility(View.VISIBLE);
                                        okHttpClient = new OkHttpClient();
                                        builder = new Request.Builder();
                                        request = builder.url(getFoxChannelURL + "?ch_id=" + foxChannelID + ",device_type=android").build();
                                        okHttpClient.newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Request request, IOException e) {
                                                updateView("Error - " + e.getMessage());
                                            }

                                            @Override
                                            public void onResponse(Response response) {
                                                if (response.isSuccessful()) {
                                                    try {
                                                        updateView(response.body().string());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        updateView("Error - " + e.getMessage());
                                                    }
                                                } else {
                                                    updateView("Not Success - code : " + response.code());
                                                }
                                            }

                                            public void updateView(final String result) {
                                                Log.d("AISLiveTV", "Result: " + result);
                                                //progressBar.setVisibility(View.GONE);
                                                try {
                                                    JSONObject jObj = new JSONObject(result);
                                                    String status = jObj.getString("status");
                                                    String title = jObj.getString("title");
                                                    String detail = jObj.getString("detail");
                                                    String remark = jObj.getString("remark");
                                                    if(status.toLowerCase().replace(" ", "").equals("ok")) {
                                                        path = detail;
                                                        //setVideoChannel(path);
                                                        //AISLiveTVFragment.setVideoChannel(path);
                                                        //showFoxSuccessDialog(remark);
                                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remark));
                                                        activity.startActivity(browserIntent);
                                                        //Intent intent = new Intent();
                                                        //intent.setClassName(activity.getPackageName(), activity.getPackageName() + ".AISLiveTVWebView");
                                                        //activity.startActivity(intent);
                                                    } else if(status.toLowerCase().replace(" ", "").equals("error")) {
                                                        showAlertDialog(title);
                                                    }
                                                } catch (Exception e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                    //progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    //progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                } else {
                    showAlertDialog("");
                }*/
            }
        });

        if(channelFavoriteList.get(position) == 0) {
            holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        }
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("AISSliderMenuFavorite", "Favorite Click.");
                //Toast.makeText(activity, "Click", Toast.LENGTH_SHORT).show();
                holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                if(channelFavoriteList.get(position) == 0) {
                    channelFavoriteList.set(position, 1);
                    database.updateChannelData(channelIDList.get(position), 1);
                    holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                } else {
                    channelFavoriteList.set(position, 0);
                    database.updateChannelData(channelIDList.get(position), 0);
                    holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                }
                adapter.notifyDataSetChanged();
            }
        });

        return vi;
    }
}
