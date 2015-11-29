package com.keng.dooplus.thetv.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.keng.dooplus.thetv.AISLiveTVMain;
import com.keng.dooplus.thetv.R;
import com.keng.dooplus.thetv.adapter.AISLiveTVEPGAdapter;
import com.keng.dooplus.thetv.adapter.AISLiveTVMenuAdapter;
import com.keng.dooplus.thetv.adapter.AISLiveTVProgramAdapter;
import com.keng.dooplus.thetv.customview.RSUTextView;
import com.keng.dooplus.thetv.database.AISLiveTVChannelDatabase;
import com.keng.dooplus.thetv.model.NavDrawerItem;
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

@SuppressLint({ "NewApi", "ValidFragment" })
public class AISLiveTVFragmentHome extends Fragment {

    private static final String TAG = "AISLiveTV";
    public static AISLiveTVFragmentHome fragmentHome;

    static public Activity activity;

    private FrameLayout frameLayout;
    private View v = null;

    public static ProgressBar progressBar;
    private ImageView btnSlideMenu;
    private RelativeLayout topMenuLayout;
    private RelativeLayout programLayout;
    private RSUTextView txtChannelName;
    private LinearLayout layoutVideoSetting;
    public static ImageView btnSD;
    public static ImageView btnPlayPause;
    public static ImageView btnHD;
    private static VideoView videoView;
    private ListView listView;
    private AISLiveTVProgramAdapter adapterProgram;
    private AISLiveTVEPGAdapter adapterEPG;
    public static AISLiveTVMenuAdapter adapterDrawerSlideMenu;
    private ArrayList<NavDrawerItem> navDrawerItemsTH;
    private ArrayList<NavDrawerItem> navDrawerItemsEng;
    Typeface tf;

    private AppPreference appPreference;
    private Utils utils;

    // Layout Video Setting
    private boolean isLayoutVideoSettingVisible = false;
    private Handler handler = null;
    String[] resulution = {"240p", "320p", "480p"};
    String[] quality = {"L", "M", "H"};
    String res = "H";

    AISLiveTVChannelDatabase database;
    private SQLiteDatabase sqliteChannel = null;
    private final String DB_NAME = "AISLIVETV";
    private final String CHANNEL_TABLE_NAME = "CHANNEL";

    private String adsURL = MyConfiguration.HOST + MyConfiguration.ADVERTISING_NEW;
    private String getChannelURL = MyConfiguration.HOST + MyConfiguration.GET_CHANNEL;
    private String getVideoChannelURL = MyConfiguration.HOST + MyConfiguration.GET_VIDEO_CHANNEL;
    private String logoutURL = MyConfiguration.HOST + MyConfiguration.LOGOUT;
    private ArrayList<String> chIDList;
    private ArrayList<Integer> chOrderList;
    private ArrayList<String> channelNameEngList;
    private ArrayList<String> channelNameTHList;
    public static ArrayList<String> channelImgList;
    private ArrayList<String> channelImgDarkList;
    private ArrayList<String> channelChargingList;
    private ArrayList<String> channelSimLockList;
    private ArrayList<Integer> channelEnableList;
    private ArrayList<Integer> channelForIOSList;
    private ArrayList<Integer> channelFavoriteList;
    public static ArrayList<String> channelImageBase64List;
    private ArrayList<Integer> chTypeIDList;
    private ArrayList<String> chTypeCodeList;
    private ArrayList<String> chTypeEngList;
    private ArrayList<String> chTypeTHList;
    private ArrayList<String> chTypeImgList;
    private ArrayList<String> chTypeImgDarkList;
    private ArrayList<String> chTypeImgBase64List;
    private ArrayList<Boolean> chTypeClickableList;
    private ArrayList<Boolean> chTypeIsSelectList;
    private ArrayList<String> epgChannelTypeCodeList;
    private ArrayList<String> epgChannelIDList;
    private ArrayList<String> epgImageList;
    private ArrayList<String> epgProgramList;
    private ArrayList<String> epgStartTimeList;
    private ArrayList<String> epgEndTimeList;
    public static ArrayList<String> epgBase64List;

    AISLiveTVMain mainActivity;
    int chTypeID = 0;

    // URL Streaming
    String urlVideo = "http://radio2.thaidhost.com:8888/show/live/playlist.m3u8";
    //String urlVideo = "";//

    OkHttpClient okHttpClient;
    Request.Builder builder;
    Request request;
    RequestBody formBody;

    private final Handler mHandler = new Handler();

    public AISLiveTVFragmentHome(){}

    public AISLiveTVFragmentHome(Activity activity){
        this.activity = activity;
        appPreference = new AppPreference(this.activity);
        utils = new Utils(activity);
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helveticaneuelight.ttf");
    }

    public AISLiveTVFragmentHome(Activity activity, int chTypeID){
        this.activity = activity;
        appPreference = new AppPreference(this.activity);
        this.chTypeID = chTypeID;
        utils = new Utils(activity);
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helveticaneuelight.ttf");
    }

    public AISLiveTVFragmentHome(Activity activity, int chTypeID, AISLiveTVMain mainActivity){
        this.activity = activity;
        appPreference = new AppPreference(this.activity);
        this.chTypeID = chTypeID;
        utils = new Utils(activity);
        this.mainActivity = mainActivity;
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helveticaneuelight.ttf");
    }

    public AISLiveTVFragmentHome(Activity activity, int chTypeID, AISLiveTVMenuAdapter adapterDrawerSlideMenu){
        this.activity = activity;
        appPreference = new AppPreference(this.activity);
        this.chTypeID = chTypeID;
        utils = new Utils(activity);
        this.adapterDrawerSlideMenu = adapterDrawerSlideMenu;
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helveticaneuelight.ttf");
    }

    public void setChannelTypeID(int chTypeID) {
        this.chTypeID = chTypeID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        frameLayout = new FrameLayout(getActivity());

        fragmentHome = new AISLiveTVFragmentHome();
        activity = getActivity();
        appPreference = new AppPreference(activity);
        utils = new Utils(activity);

        // Creating view correspoding to the fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        topMenuLayout = (RelativeLayout) v.findViewById(R.id.topMenuLayout);
        programLayout = (RelativeLayout) v.findViewById(R.id.programLayout);
        txtChannelName = (RSUTextView) v.findViewById(R.id.txtChannelName);
        layoutVideoSetting = (LinearLayout) v.findViewById(R.id.layoutVideoSetting);
        btnSD = (ImageView) v.findViewById(R.id.btnSD);
        btnPlayPause = (ImageView) v.findViewById(R.id.btnPlayPause);
        btnHD = (ImageView) v.findViewById(R.id.btnHD);
        listView = (ListView) v.findViewById(R.id.listView);

        btnSlideMenu = (ImageView) v.findViewById(R.id.btnSliderMenu);
        btnSlideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AISLiveTV", "mDrawerLayout Click");
                if(AISLiveTVMain.mDrawerLayout.isShown()) {
                    Log.d("AISLiveTV", "mDrawerLayout isShown");
                    //MainActivity.mDrawerLayout.openDrawer(MainActivity.mDrawerList);
                    AISLiveTVMain.mDrawerLayout.openDrawer(AISLiveTVMain.mDrawer);
                    AISLiveTVMain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    Log.d("AISLiveTV", "mDrawerLayout isn't Shown");
                    //MainActivity.mDrawerLayout.openDrawer(MainActivity.mDrawerList);
                    AISLiveTVMain.mDrawerLayout.openDrawer(AISLiveTVMain.mDrawer);
                    AISLiveTVMain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        });

        videoView = (VideoView) v.findViewById(R.id.videoView);
        setVideoController(urlVideo);

        setChannel(1);

        listView.performItemClick(
                listView.getAdapter().getView(0, null, null),
                0,
                listView.getAdapter().getItemId(0));
        listView.requestFocusFromTouch();
        listView.setSelection(0);

        return v;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {

        super.onConfigurationChanged(newConfig);

        frameLayout.removeAllViews();

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_home, null);

        frameLayout.addView(v);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            topMenuLayout.setVisibility(View.GONE);
            programLayout.setVisibility(View.GONE);
        } else
        {
            topMenuLayout.setVisibility(View.VISIBLE);
            programLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setVideoController(final String urlVideo) {
        Log.d("AISLiveTV", "setVideoController");
        progressBar.setVisibility(View.VISIBLE);
        videoView.setVideoPath(urlVideo);
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.VISIBLE);
                videoView.setVideoPath(urlVideo);
                return false;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.VISIBLE);
                videoView.setVideoPath(urlVideo);
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.GONE);
                videoView.start();
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Log.d(TAG, "Video onTouch");
                //Toast.makeText(getActivity(), "Video onTouch", Toast.LENGTH_SHORT).show();
                if (!isLayoutVideoSettingVisible) {
                    isLayoutVideoSettingVisible = true;
                    layoutVideoSetting.setVisibility(View.VISIBLE);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isLayoutVideoSettingVisible = false;
                            layoutVideoSetting.setVisibility(View.GONE);
                        }
                    }, 2600);
                }
                return false;
            }
        });

        btnSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView.isPlaying()) {
                    videoView.pause();
                    btnPlayPause.setImageResource(R.mipmap.btn_play);
                } else {
                    videoView.start();
                    btnPlayPause.setImageResource(R.mipmap.btn_pause);
                }
            }
        });

        btnHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setChannel(final int chTypeID) {
        //if(chTypeID != this.chTypeID || isFirstTime) {

        database = null;
        sqliteChannel = null;

        database = new AISLiveTVChannelDatabase(activity, sqliteChannel);
        sqliteChannel = activity.openOrCreateDatabase(DB_NAME, activity.MODE_PRIVATE, null);

        if(true) {
            //this.chTypeID = chTypeID;
            chIDList = null;
            chOrderList = null;
            channelNameEngList = null;
            channelNameTHList = null;
            channelImgList = null;
            channelImgDarkList = null;
            channelChargingList = null;
            channelSimLockList = null;
            channelEnableList = null;
            channelForIOSList = null;
            channelFavoriteList = null;
            channelImageBase64List = null;

            chIDList = new ArrayList<String>();
            chOrderList = new ArrayList<Integer>();
            channelNameEngList = new ArrayList<String>();
            channelNameTHList = new ArrayList<String>();
            channelImgList = new ArrayList<String>();
            channelImgDarkList = new ArrayList<String>();
            channelChargingList = new ArrayList<String>();
            channelSimLockList = new ArrayList<String>();
            channelEnableList = new ArrayList<Integer>();
            channelForIOSList = new ArrayList<Integer>();
            channelFavoriteList = new ArrayList<Integer>();
            channelImageBase64List = new ArrayList<String>();

			/*database = null;
			sqliteChannel = null;

			database = new AISLiveTVChannelDatabase(activity, sqliteChannel);
			sqliteChannel = activity.openOrCreateDatabase(DB_NAME, activity.MODE_PRIVATE, null);*/

            String query = "";
            if(chTypeID == 1001) {
                query = "SELECT DISTINCT * FROM " + CHANNEL_TABLE_NAME + " WHERE favorite=1 " + " GROUP BY chTitleEng ORDER BY chTypeID ASC;";
            } else {
                query = "SELECT * FROM " + CHANNEL_TABLE_NAME + " WHERE chTypeID=" + chTypeID + " ORDER BY chOrder ASC;";
                //query = "SELECT * FROM " + CHANNEL_TABLE_NAME + " WHERE chTypeID=" + "1" + " ORDER BY chOrder ASC;";
            }
            Log.d("AISLiveTV", "Channel Query Preparation");
            Log.d("AISLiveTV", "Query: " + query);
            Cursor c = sqliteChannel.rawQuery(query, null);
            Log.d("AISLiveTV", "Channel Query Success");
            if (c != null ) {
                Log.d("AISLiveTV", "Cursor isn't null");
                if (c.moveToFirst()) {
                    do {
                        //answer = c.getString(c.getColumnIndex("answer"));
                        Log.d("AISLiveTV", "ChannelName: " + c.getString(c.getColumnIndex("chTitleEng")));
                        Log.d("AISLiveTV", "ChannelOrder: " + c.getInt(c.getColumnIndex("chOrder")));

                        chIDList.add(c.getString(c.getColumnIndex("chID")));
                        chOrderList.add(c.getInt(c.getColumnIndex("chOrder")));
                        channelNameEngList.add(c.getString(c.getColumnIndex("chTitleEng")));
                        channelNameTHList.add(c.getString(c.getColumnIndex("chTitleTH")));
                        channelImgList.add(c.getString(c.getColumnIndex("chImg")));
                        channelImgDarkList.add(c.getString(c.getColumnIndex("chImgDark")));
                        channelChargingList.add(c.getString(c.getColumnIndex("chCharging")));
                        channelSimLockList.add(c.getString(c.getColumnIndex("chSimLock")));
                        channelEnableList.add(c.getInt(c.getColumnIndex("enable")));
                        channelForIOSList.add(c.getInt(c.getColumnIndex("forIOS")));
                        channelFavoriteList.add(c.getInt(c.getColumnIndex("favorite")));
                        channelImageBase64List.add(c.getString(c.getColumnIndex("base64")));
                    } while (c.moveToNext());
                }
                c.close();
            }
            Log.d("AISLiveTV", "Channel Query Finish");

            adapterProgram = new AISLiveTVProgramAdapter(activity);
            //adapterProgram.setAutoCompleteTextView(txtAutoComplete);
            adapterProgram.setContentTypeCodeList(chTypeCodeList);
            adapterProgram.setChannelIDList(chIDList);
            if(appPreference.getIsThaiLanguage()) {
                adapterProgram.setChannelNameList(channelNameTHList);
                adapterProgram.setChannelSubNameList(channelNameEngList);
                adapterProgram.notifyDataSetChanged();

                //adapterAutoComplete.addAll(channelNameTHList);
                //adapterAutoComplete.notifyDataSetChanged();
                //txtAutoComplete.invalidate();
            } else {
                adapterProgram.setChannelNameList(channelNameEngList);
                adapterProgram.setChannelSubNameList(channelNameTHList);
                adapterProgram.notifyDataSetChanged();

                //adapterAutoComplete.addAll(channelNameEngList);
                //adapterAutoComplete.notifyDataSetChanged();
                //txtAutoComplete.invalidate();
            }
            //adapter.setChannelNameList(channelNameEngList);
            //adapter.setChannelSubNameList(channelNameTHList);
            adapterProgram.setChannelImageList(channelImgList);
            adapterProgram.setChannelImageDarkList(channelImgDarkList);
            adapterProgram.setChannelFavoriteList(channelFavoriteList);
            adapterProgram.setChannelImageBase64List(channelImageBase64List);
            adapterProgram.setChannelChargingList(channelChargingList);
            //adapterProgram.setAdapter(adapterProgram);
            //adapterProgram.setProgressBar(progressBar);
            //adapterProgram.setVideoView(videoView);
            adapterProgram.setTextChannelName(txtChannelName);
            adapterProgram.setChTypeID(chTypeID);
            adapterProgram.setAdapter(adapterProgram);
            listView.setAdapter(adapterProgram);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Log.d("AISLiveTV", "setOnItemClickListener");

                    if(appPreference.getIsThaiLanguage()) {
                        txtChannelName.setText(channelNameTHList.get(position));
                    } else {
                        txtChannelName.setText(channelNameEngList.get(position));
                    }

                    if(appPreference.getPermissionCode().toLowerCase().equals("f") || channelChargingList.get(position).toLowerCase().equals("free")) {
                        if(chIDList.get(position).length() > 1) {
                            appPreference.setChannelID(chIDList.get(position));
                            String chTypeCode = appPreference.getContentTypeCode();
                            String versionApp = utils.getAppVersion();
                            String chID = chIDList.get(position);
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
                            progressBar.setVisibility(View.VISIBLE);
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
                                    progressBar.setVisibility(View.GONE);
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
                                            Log.d("AISLiveTV", "connectionCallBack");
                                            setVideoController(urlVideo);
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
                        Toast.makeText(getActivity(), "Permission: " + appPreference.getPermissionCode(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Charge: " + channelChargingList.get(position), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            adapterProgram.notifyDataSetChanged();
            listView.invalidate();
        }

        database.closeDB();
        sqliteChannel.close();
        sqliteChannel = null;
        database = null;
    }

    public void setChangeLanguage() {
        if(appPreference.getIsThaiLanguage()) {
            Log.d("AISLiveTV", "SetChangeLamguage to Thai");
            //appPreference.setIsThaiLanguage(false);
            adapterProgram.setChannelNameList(channelNameTHList);
            adapterProgram.setChannelSubNameList(channelNameEngList);
            //adapterDrawerSlideMenu.setNavDrawerItems(navDrawerItemsEng);
            //adapterDrawerSlideMenu.notifyDataSetChanged();
            adapterProgram.notifyDataSetChanged();
        } else {
            Log.d("AISLiveTV", "SetChangeLamguage to Eng");
            //appPreference.setIsThaiLanguage(true);
            adapterProgram.setChannelNameList(channelNameEngList);
            adapterProgram.setChannelSubNameList(channelNameTHList);
            //adapterDrawerSlideMenu.setNavDrawerItems(navDrawerItemsTH);
            //adapterDrawerSlideMenu.notifyDataSetChanged();
            adapterProgram.notifyDataSetChanged();
        }
    }
}
