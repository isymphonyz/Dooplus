package com.keng.dooplus.thetv.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dooplus on 11/18/15 AD.
 */
public class AISLiveTVEPGAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;

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

    public AISLiveTVEPGAdapter(Activity a) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return 0;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
