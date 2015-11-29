package com.keng.dooplus.thetv.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keng.dooplus.thetv.R;
import com.keng.dooplus.thetv.model.NavDrawerItem;
import com.keng.dooplus.thetv.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Dooplus on 11/18/15 AD.
 */
public class AISLiveTVMenuAdapter extends BaseAdapter {

    private Activity activity;
    private Context context;
    private ListView listView;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private ArrayList<String> chTypeImgBase64List;
    private ArrayList<Boolean> chTypeClickableList;
    private ArrayList<Boolean> chTypeIsSelectList;
    Typeface tf;

    public AISLiveTVMenuAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    public AISLiveTVMenuAdapter(Activity activity, ArrayList<NavDrawerItem> navDrawerItems){
        this.activity = activity;
        this.navDrawerItems = navDrawerItems;
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helveticaneuelight.ttf");
    }

    public AISLiveTVMenuAdapter(Activity activity, ArrayList<NavDrawerItem> navDrawerItems, ArrayList<String> chTypeImgBase64List){
        this.activity = activity;
        this.navDrawerItems = navDrawerItems;
        this.chTypeImgBase64List = chTypeImgBase64List;
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helveticaneuelight.ttf");
    }

    public AISLiveTVMenuAdapter(Activity activity, ArrayList<NavDrawerItem> navDrawerItems, ArrayList<String> chTypeImgBase64List, ArrayList<Boolean> chTypeClickableList){
        this.activity = activity;
        this.navDrawerItems = navDrawerItems;
        this.chTypeImgBase64List = chTypeImgBase64List;
        this.chTypeClickableList = chTypeClickableList;
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helveticaneuelight.ttf");
    }

    public AISLiveTVMenuAdapter(Activity activity, ArrayList<NavDrawerItem> navDrawerItems, ArrayList<String> chTypeImgBase64List, ArrayList<Boolean> chTypeClickableList, ArrayList<Boolean> chTypeIsSelectList){
        this.activity = activity;
        this.navDrawerItems = navDrawerItems;
        this.chTypeImgBase64List = chTypeImgBase64List;
        this.chTypeClickableList = chTypeClickableList;
        this.chTypeIsSelectList = chTypeIsSelectList;
        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/helveticaneuelight.ttf");
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public void setNavDrawerItems(ArrayList<NavDrawerItem> navDrawerItems) {
        this.navDrawerItems = navDrawerItems;
    }

    public void setImageBase64List(ArrayList<String> chTypeImgBase64List) {
        this.chTypeImgBase64List = chTypeImgBase64List;
    }

    public void setClickableList(ArrayList<Boolean> chTypeClickableList) {
        this.chTypeClickableList = chTypeClickableList;
    }

    public void setIsSelectList(ArrayList<Boolean> chTypeIsSelectList) {
        this.chTypeIsSelectList = chTypeIsSelectList;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		/*if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }*/

        LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.drawer_list_item, null);

        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        txtTitle.setText(navDrawerItems.get(position).getTitle());
        txtTitle.setTypeface(tf);

        //imgIcon.setImageResource(navDrawerItems.get(position).getIcon());

        //Log.d("AISSlideMenuBase64: " + chTypeImgBase64List.get(position));
        //imgIcon.setTag(navDrawerItems.get(position).getContentImage());
        //imageLoader.DisplayImage(navDrawerItems.get(position).getContentImage(), activity, imgIcon);

        if(chTypeClickableList.get(position)) {
            try {
                if(position<chTypeClickableList.size()-4) {
                    Glide.with(activity)
                            .load(navDrawerItems.get(position).getContentImage())
                            .placeholder(R.mipmap.ic_launcher)
                            .crossFade()
                            .into(imgIcon);
                } else {
                    Glide.with(activity)
                            .load(Integer.parseInt(chTypeImgBase64List.get(position)))
                            .placeholder(R.mipmap.ic_launcher)
                            .crossFade()
                            .into(imgIcon);
                }
                //imgIcon.setImageResource(Integer.parseInt(chTypeImgBase64List.get(position)));
            } catch(NumberFormatException nfe) {
                imgIcon.setImageBitmap(Utils.getBitmapFromBase64(chTypeImgBase64List.get(position)));
            }
        } else {
            imgIcon.setVisibility(View.GONE);
            txtTitle.setPadding(12, 0, 12, 0);
            //txtTitle.setTextAppearance(activity, android.R.style.TextAppearance_DeviceDefault_Small);
            layout.setBackgroundColor(Color.rgb(20, 21, 24));
        }

        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        Log.d("AISLiveTV", "Color Channel: " + navDrawerItems.get(position).getTitle());
        Log.d("AISLiveTV", "Color Click: " + chTypeClickableList.get(position) + ", Select: " + chTypeIsSelectList.get(position));
        if(!chTypeClickableList.get(position) || chTypeIsSelectList.get(position)) {
            txtTitle.setTextColor(Color.WHITE);
        } else {
            txtTitle.setTextColor(Color.rgb(149, 165, 166));
        }

        this.notifyDataSetChanged();
        //listView.invalidateViews();

        return convertView;
    }
}
