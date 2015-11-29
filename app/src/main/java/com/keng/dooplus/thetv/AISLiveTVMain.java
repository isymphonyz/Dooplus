package com.keng.dooplus.thetv;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.keng.dooplus.thetv.adapter.AISLiveTVMenuAdapter;
import com.keng.dooplus.thetv.database.AISLiveTVChannelDatabase;
import com.keng.dooplus.thetv.database.AISLiveTVConntentTypeDatabase;
import com.keng.dooplus.thetv.fragment.AISLiveTVFragmentHome;
import com.keng.dooplus.thetv.model.NavDrawerItem;
import com.keng.dooplus.thetv.utils.AppPreference;
import com.keng.dooplus.thetv.utils.MyConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dooplus on 11/17/15 AD.
 */
public class AISLiveTVMain extends AppCompatActivity {

    // onConfigurationChanged
    int oldOptions;

    int mPosition = -1;
    String mTitle = "";

    // Array of strings storing country names
    String[] mCountries ;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] mFlags = new int[]{
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    // Array of strings to initial counts
    String[] mCount = new String[]{
            "", "", "", "", "",
            "", "", "", "", "" };

    public AISLiveTVFragmentHome fragment;
    public static DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static LinearLayout mDrawer ;
    private List<HashMap<String,String>> mList ;
    private SimpleAdapter mAdapter;
    final private String COUNTRY = "Menu";
    final private String FLAG = "flag";
    final private String COUNT = "count";

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    //private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItemsTH;
    private ArrayList<NavDrawerItem> navDrawerItemsEng;
    private AISLiveTVMenuAdapter adapter;

    private String urlLogout = MyConfiguration.HOST + MyConfiguration.LOGOUT;

    AISLiveTVConntentTypeDatabase databaseContentType;
    AISLiveTVChannelDatabase databaseChannel;
    private SQLiteDatabase sqliteContentType = null;
    private final String DB_NAME = "AISLIVETV";
    private final String CONTENT_TYPE_TABLE_NAME = "CONTENT_TYPE";
    private ArrayList<Integer> chTypeIDList;
    private ArrayList<String> chTypeCodeList;
    private ArrayList<String> chTypeEngList;
    private ArrayList<String> chTypeTHList;
    private ArrayList<String> chTypeImgList;
    private ArrayList<String> chTypeImgDarkList;
    private ArrayList<String> chTypeImgBase64List;
    private ArrayList<Boolean> chTypeClickableList;
    private ArrayList<Boolean> chTypeIsSelectList;

    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        appPreference = new AppPreference(this);

        databaseContentType = new AISLiveTVConntentTypeDatabase(this, sqliteContentType);
        sqliteContentType = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);

        getContentType();

        oldOptions = getWindow().getDecorView().getSystemUiVisibility();

        // Getting an array of country names
        mCountries = getResources().getStringArray(R.array.home_menu);

        // Title of the activity
        mTitle = (String)getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        // Getting a reference to the drawer listview
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        // Getting a reference to the sidebar drawer ( Title + ListView )
        mDrawer = ( LinearLayout) findViewById(R.id.drawer);

        // Each row in the list stores country name, count and flag
        mList = new ArrayList<HashMap<String,String>>();


        for(int i=0; i<mCountries.length; i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put(COUNTRY, mCountries[i]);
            hm.put(COUNT, mCount[i]);
            hm.put(FLAG, Integer.toString(mFlags[i]) );
            mList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { FLAG,COUNTRY,COUNT };

        // Ids of views in listview_layout
        int[] to = { R.id.flag , R.id.country , R.id.count};

        // Instantiating an adapter to store each items
        // R.layout.drawer_layout defines the layout of each item
        mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from, to);

        // Getting reference to DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        // Creating a ToggleButton for NavigationDrawer with drawer event listener
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.mipmap.ic_drawer , R.string.drawer_open, R.string.drawer_close){

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                //highlightSelectedCountry();
                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle(getText(R.string.txt_select_your_menu));
                supportInvalidateOptionsMenu();
            }
        };

        // Setting event listener for the drawer
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // ItemClick event handler for the drawer items
        /*mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                // Increment hit count of the drawer list item
                //incrementHitCount(position);

                if(position < 5) { // Show fragment for countries : 0 to 4
                    showFragment(position);
                }else{ // Show message box for countries : 5 to 9
                    Toast.makeText(getApplicationContext(), mCountries[position], Toast.LENGTH_LONG).show();
                }

                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawer);
            }
        });*/


        // Enabling Up navigation
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Setting the adapter to the listView
        mDrawerList.setAdapter(mAdapter);

        navDrawerItemsTH = new ArrayList<NavDrawerItem>();
        navDrawerItemsEng = new ArrayList<NavDrawerItem>();
        Log.d("AISSlideMenu", "chTypeIDList size " + chTypeEngList.size());
        for(int i=0; i<chTypeIDList.size(); i++) {
            Log.d("AISSlideMenu", "navDrawerItems add " + chTypeEngList.get(i));
            //navDrawerItems.add(new NavDrawerItem(chTypeEngList.get(i), navMenuIcons.getResourceId(0, -1)));
            navDrawerItemsTH.add(new NavDrawerItem(chTypeTHList.get(i), chTypeImgList.get(i)));
            navDrawerItemsEng.add(new NavDrawerItem(chTypeEngList.get(i), chTypeImgList.get(i)));
        }

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        if(appPreference.getIsThaiLanguage()) {
            adapter = new AISLiveTVMenuAdapter(this, navDrawerItemsTH, chTypeImgBase64List, chTypeClickableList, chTypeIsSelectList);
        } else {
            adapter = new AISLiveTVMenuAdapter(this, navDrawerItemsEng, chTypeImgBase64List, chTypeClickableList, chTypeIsSelectList);
        }
        adapter.setListView(mDrawerList);
        mDrawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mDrawerList.invalidateViews();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.mipmap.sidemenu_button, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                //getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                //invalidateOptionsMenu();
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                //getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                //invalidateOptionsMenu();
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        showFragment(0);
    }

    public void showFragment(int position){

        //Currently selected country
        mTitle = mCountries[position];

        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        AISLiveTVFragmentHome fragmentHome = new AISLiveTVFragmentHome();
        AISLiveTVFragmentHome fragmentHome1 = new AISLiveTVFragmentHome();
        AISLiveTVFragmentHome fragmentHome2 = new AISLiveTVFragmentHome();

        if(position == 0) {
            Bundle data = new Bundle();
            data.putInt("position", position);
            fragmentHome.setArguments(data);
            ft.remove(fragmentHome);
            ft.replace(R.id.content_frame, fragmentHome);
            //ft.replace(R.id.content_frame, fragmentSpot);
            fragment = fragmentHome;
        } else if(position == 1) {
            Bundle data = new Bundle();
            data.putInt("position", position);
            fragmentHome1.setArguments(data);
            ft.remove(fragmentHome1);
            ft.replace(R.id.content_frame, fragmentHome1);
        } else if(position == 2) {
            Bundle data = new Bundle();
            data.putInt("position", position);
            fragmentHome2.setArguments(data);
            ft.remove(fragmentHome2);
            ft.replace(R.id.content_frame, fragmentHome2);
        } else {
            Toast.makeText(getApplicationContext(), "ยังไม่เปิดให้บริการ", Toast.LENGTH_SHORT).show();
        }

        ft.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            oldOptions = getWindow().getDecorView().getSystemUiVisibility();
            int newOptions = oldOptions;
            newOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            newOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            newOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            newOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
            newOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(newOptions);
            //getActionBar().hide();
        } else
        {
            getWindow().getDecorView().setSystemUiVisibility(oldOptions);
            //getActionBar().show();
        }
    }

    public void getContentType() {
        chTypeIDList = null;
        chTypeCodeList = null;
        chTypeEngList = null;
        chTypeTHList = null;
        chTypeImgList = null;
        chTypeImgDarkList = null;
        chTypeImgBase64List = null;
        chTypeClickableList = null;
        chTypeIsSelectList = null;

        chTypeIDList = new ArrayList<Integer>();
        chTypeCodeList = new ArrayList<String>();
        chTypeEngList = new ArrayList<String>();
        chTypeTHList = new ArrayList<String>();
        chTypeImgList = new ArrayList<String>();
        chTypeImgDarkList = new ArrayList<String>();
        chTypeImgBase64List = new ArrayList<String>();
        chTypeClickableList = new ArrayList<Boolean>();
        chTypeIsSelectList = new ArrayList<Boolean>();

        chTypeIDList.add(0);
        chTypeCodeList.add("0");
        chTypeEngList.add("All Channels");
        chTypeTHList.add("ช่องทั้งหมด");
        chTypeImgList.add("");
        chTypeImgDarkList.add("");
        chTypeImgBase64List.add("");
        chTypeClickableList.add(false);
        chTypeIsSelectList.add(false);

        String query = "SELECT * FROM " + CONTENT_TYPE_TABLE_NAME + " ORDER BY defaultType ASC;";
        Log.d("AISSlideMenu", "Query Preparation");
        Cursor c = sqliteContentType.rawQuery(query, null);
        Log.d("AISSlideMenu", "Query Success");
        if (c != null ) {
            Log.d("AISSlideMenu", "Cursor isn't null");
            int x = 0;
            if (c.moveToFirst()) {
                do {
                    //answer = c.getString(c.getColumnIndex("answer"));
                    //Log.d("AISSlideMenu", "ContentID: " + c.getInt(c.getColumnIndex("chTypeID")));
                    //Log.d("AISSlideMenu", "ContentName: " + c.getString(c.getColumnIndex("chTypeEng")));
                    //Log.d("AISSlideMenu", "Base64: " + c.getString(c.getColumnIndex("base64")));

                    chTypeIDList.add(c.getInt(c.getColumnIndex("chTypeID")));
                    chTypeCodeList.add(c.getString(c.getColumnIndex("chTypeCode")));
                    chTypeEngList.add(c.getString(c.getColumnIndex("chTypeEng")));
                    chTypeTHList.add(c.getString(c.getColumnIndex("chTypeTH")));
                    chTypeImgList.add(c.getString(c.getColumnIndex("chTypeImg")));
                    chTypeImgDarkList.add(c.getString(c.getColumnIndex("chTypeImgDark")));
                    chTypeImgBase64List.add(c.getString(c.getColumnIndex("base64")));
                    chTypeClickableList.add(true);
                    if(x == 0) {
                        chTypeIsSelectList.add(true);
                    } else {
                        chTypeIsSelectList.add(false);
                    }
                    x++;
                } while (c.moveToNext());
            }
        }
        Log.d("AISSlideMenu", "Query Finish");

        chTypeIDList.add(1000);
        chTypeCodeList.add("1000");
        chTypeEngList.add("Setting");
        chTypeTHList.add("ตั้งค่า");
        chTypeImgList.add("");
        chTypeImgDarkList.add("");
        chTypeImgBase64List.add("");
        chTypeClickableList.add(false);
        chTypeIsSelectList.add(false);

        chTypeIDList.add(1001);
        chTypeCodeList.add("1001");
        chTypeEngList.add("Favorites");
        chTypeTHList.add("ช่องโปรด");
        chTypeImgList.add("" + R.mipmap.favourite);
        chTypeImgDarkList.add("");
        chTypeImgBase64List.add("" + R.mipmap.favourite);
        chTypeClickableList.add(true);
        chTypeIsSelectList.add(false);

        chTypeIDList.add(1002);
        chTypeCodeList.add("1002");
        chTypeEngList.add("Language TH/EN");
        chTypeTHList.add("เปลี่ยนภาษา TH/EN");
        chTypeImgList.add("" + R.mipmap.language);
        chTypeImgDarkList.add("");
        chTypeImgBase64List.add("" + R.mipmap.language);
        chTypeClickableList.add(true);
        chTypeIsSelectList.add(false);

        chTypeIDList.add(1003);
        chTypeCodeList.add("1003");
        chTypeEngList.add("Profile");
        chTypeTHList.add("โปรไฟล์");
        chTypeImgList.add("" + R.mipmap.setting);
        chTypeImgDarkList.add("");
        chTypeImgBase64List.add("" + R.mipmap.setting);
        chTypeClickableList.add(true);
        chTypeIsSelectList.add(false);

        chTypeIDList.add(1004);
        chTypeCodeList.add("1004");
        if(appPreference.getLoginStatus()) {
            chTypeEngList.add("Log out");
            chTypeTHList.add("ออกจากระบบ");
        } else {
            chTypeEngList.add("Log in");
            chTypeTHList.add("เข้าระบบ");
        }
        chTypeImgList.add("" + R.mipmap.sign_out);
        chTypeImgDarkList.add("");
        chTypeImgBase64List.add("" + R.mipmap.sign_out);
        chTypeClickableList.add(true);
        chTypeIsSelectList.add(false);

        databaseContentType.closeDB();
        databaseContentType = null;
        sqliteContentType = null;
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            if(chTypeClickableList.get(position)) {
                if(position == chTypeClickableList.size()-1) { // Log out
                    //finish();
                    //System.exit(0);
                    //logout();
                    if(chTypeEngList.get(position).contains("in")) {
                        Intent intent = new Intent();
                        intent.setClassName(getPackageName(), getPackageName() + ".AISLiveTVRequestOTP");
                        finish();
                        startActivity(intent);
                    } else {
                        //logout();
                    }
                } else if(position == chTypeClickableList.size()-2) { // Profile
                    //showProfileDialog();
                } else if(position == chTypeClickableList.size()-3) { // Change Language
                    appPreference.setIsThaiLanguage(!appPreference.getIsThaiLanguage());
                    if(appPreference.getIsThaiLanguage()) {
                        //adapter.setChannelNameList(channelNameEngList);
                        //adapter.setChannelSubNameList(channelNameTHList);
                        //adapter.setNavDrawerItems(navDrawerItemsEng);
                        //adapter.notifyDataSetChanged();
                        //adapter.notifyDataSetChanged();
                        adapter = new AISLiveTVMenuAdapter(AISLiveTVMain.this, navDrawerItemsTH, chTypeImgBase64List, chTypeClickableList, chTypeIsSelectList);
                    } else {
                        //adapter.setChannelNameList(channelNameTHList);
                        //adapter.setChannelSubNameList(channelNameEngList);
                        //adapter.setNavDrawerItems(navDrawerItemsTH);
                        //adapter.notifyDataSetChanged();
                        //adapter.notifyDataSetChanged();
                        adapter = new AISLiveTVMenuAdapter(AISLiveTVMain.this, navDrawerItemsEng, chTypeImgBase64List, chTypeClickableList, chTypeIsSelectList);
                    }
                    adapter.setListView(mDrawerList);
                    mDrawerList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    mDrawerList.invalidateViews();
                    fragment.setChangeLanguage();
                } else if(position == chTypeClickableList.size()-4) { // Favorite
                    displayView(position);

                    for(int x=0; x<chTypeIsSelectList.size(); x++) {
                        chTypeIsSelectList.set(x, false);
                    }
                    chTypeIsSelectList.set(position, true);
                    chTypeImgList.set(position, "" + R.mipmap.favourite_hover);
                    chTypeImgBase64List.set(position, "" + R.mipmap.favourite_hover);
                    adapter.notifyDataSetChanged();
                    mDrawerList.invalidateViews();
                } else {
                    if(appPreference.getIsThaiLanguage()) {
                        //AISLiveTVFragmentHome.txtProgram.setText(chTypeTHList.get(position));
                    } else {
                        //AISLiveTVFragmentHome.txtProgram.setText(chTypeEngList.get(position));
                    }
                    displayView(position);
                    appPreference.setContentTypeCode(chTypeCodeList.get(position));

                    for(int x=0; x<chTypeIsSelectList.size(); x++) {
                        chTypeIsSelectList.set(x, false);
                    }
                    chTypeIsSelectList.set(position, true);
                    chTypeImgList.set(chTypeClickableList.size()-4, "" + R.mipmap.favourite);
                    chTypeImgBase64List.set(chTypeClickableList.size()-4, "" + R.mipmap.favourite);
                    adapter.notifyDataSetChanged();
                    mDrawerList.invalidateViews();
                }

            }
            mDrawerLayout.closeDrawer(mDrawer);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
		if(fragment == null) {
            fragment = new AISLiveTVFragmentHome(this, 1, adapter);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else {
            fragment.setChannel(chTypeIDList.get(position));
            //fragment.setEPG(chTypeCodeList.get(position));
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(chTypeEngList.get(position));
        //mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerLayout.closeDrawer(mDrawer);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title.toString();
        //getActionBar().setTitle(mTitle);
        //getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
}
