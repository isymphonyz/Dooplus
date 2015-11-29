package com.keng.dooplus.thetv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppPreference {
	private final String preferenceName = "AISLIVETV";
	private final String DATABASE_STATUS = "database_status";
	private final String TOKEN = "token";
	private final String SCREEN_WIDTH = "screen_width";
	private final String SCREEN_HEIGHT = "screen_height";
	private final String LOGIN_STATUS = "login_status";
	private final String DEVICE_ID = "deviceID";
	private final String PHONE = "phone";
	private final String OTP = "otp";
	private final String USERNAME = "username";
	private final String PERMISSION_CODE = "premission_code";
	private final String LANGUAGE = "language";
	private final String FACEBOOK_TOKEN = "facebook_token";
	private final String VIDEO_STATUS = "video_status";
	private final String CONTENT_TYPE_CODE = "contentTypeCode";
	private final String CHANNEL_ID = "channel_id";
	private final String CHANNEL_RESOLUTION = "channel_resolution";
	private final String FULLSCREEN = "fullscreen";
	private final String VIDEO_ASPECT_RATIO = "video_aspect_ration";
	private final String CHANNEL_UPDATE_TIME = "channel_update_time";
	private final String EPG_UPDATE_TIME = "epg_update_time";
	private final String AUTO_LOGIN_STATUS = "auto_login_status";
	private final String IS_REGISTER_ACTIVITY = "is_register_activity";
	private final String FACEBOOK_ID = "facebookID";
	private final String FACEBOOK_NAME = "facebookName";
	private final String FACEBOOK_DOB = "facebookDOB";
	private final String FACEBOOK_GENDER = "facebookGender";
	private final String FACEBOOK_EMAIL = "facebookEmail";
	private final String AUTOLOGIN = "autologin";

	private SharedPreferences preference = null;
	private Context context = null;
	private static AppPreference appPreference = null;
	
	public AppPreference(Context context) {
		this.context = context;
		int mode = Context.MODE_PRIVATE;
		this.preference = this.context.getSharedPreferences(this.preferenceName, mode);
	}
	
	public static AppPreference getInstance(Context context) {
		if(appPreference == null) {
			appPreference = new AppPreference(context);
		}
		return appPreference;
	}
	
	public void setDatabaseStatus(boolean status) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.DATABASE_STATUS, status);
		editor.commit();
	}
	
	public boolean getDatabseStatus() {
		boolean status = this.preference.getBoolean(this.DATABASE_STATUS, false);
		return status;
	}
	
	public void setToken(String token) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.TOKEN, token);
		editor.commit();
	}
	
	public String getToken() {
		String token = this.preference.getString(this.TOKEN, "");
		return token;
	}
	
	public void setLoginStatus(boolean status) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.LOGIN_STATUS, status);
		editor.commit();
	}
	
	public boolean getLoginStatus() {
		boolean status = this.preference.getBoolean(this.LOGIN_STATUS, false);
		return status;
	}
	
	public void setDeviceID(String deviceID) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.DEVICE_ID, deviceID);
		editor.commit();
	}
	
	public String getDeviceID() {
		String deviceID = this.preference.getString(this.DEVICE_ID, "");
		return deviceID;
	}
	
	public void setPhone(String phone) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.PHONE, phone);
		editor.commit();
	}
	
	public String getPhone() {
		String phone = this.preference.getString(this.PHONE, "");
		return phone;
	}
	
	public void setOTP(String otp) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.OTP, otp);
		editor.commit();
	}
	
	public String getOTP() {
		String otp = this.preference.getString(this.OTP, "");
		return otp;
	}
	
	public void setUsername(String username) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.USERNAME, username);
		editor.commit();
	}
	
	public String getUsername() {
		String username = this.preference.getString(this.USERNAME, "");
		return username;
	}
	
	public void setPermissionCode(String code) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.PERMISSION_CODE, code);
		editor.commit();
	}
	
	public String getPermissionCode() {
		String code = this.preference.getString(this.PERMISSION_CODE, "");
		return code;
	}
	
	public void setIsThaiLanguage(boolean isThaiLanguage) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.LANGUAGE, isThaiLanguage);
		editor.commit();
	}
	
	public boolean getIsThaiLanguage() {
		boolean isThaiLanguage = this.preference.getBoolean(this.LANGUAGE, false);
		return isThaiLanguage;
	}
	
	public void setFacebookToken(String facebookToken) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_TOKEN, facebookToken);
		editor.commit();
	}
	
	public String getFacebookToken() {
		String facebookToken = this.preference.getString(this.FACEBOOK_TOKEN, "");
		return facebookToken;
	}
	
	public void setVideoStatus(boolean status) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.VIDEO_STATUS, status);
		editor.commit();
	}
	
	public boolean getVideoStatus() {
		boolean status = this.preference.getBoolean(this.VIDEO_STATUS, false);
		return status;
	}
	
	public void setIsFullscreen(boolean status) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.FULLSCREEN, status);
		editor.commit();
	}
	
	public boolean getIsFullscreen() {
		boolean status = this.preference.getBoolean(this.FULLSCREEN, false);
		return status;
	}
	
	public void setVideoAspectRation(float ratio) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putFloat(this.VIDEO_ASPECT_RATIO, ratio);
		editor.commit();
	}
	
	public float getVideoAspectRatio() {
		float ratio = this.preference.getFloat(this.VIDEO_ASPECT_RATIO, (16/9));
		return ratio;
	}
	
	public void setChannelUpdateTime(String date) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.CHANNEL_UPDATE_TIME, date);
		editor.commit();
	}
	
	public String getChannelUpdateTime() {
		String date = this.preference.getString(this.CHANNEL_UPDATE_TIME, "");
		return date;
	}
	
	public void setEPGUpdateTime(String date) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.EPG_UPDATE_TIME, date);
		editor.commit();
	}
	
	public String getEPGUpdateTime() {
		String date = this.preference.getString(this.EPG_UPDATE_TIME, "");
		return date;
	}
	
	public void setContentTypeCode(String contentTypeCode) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.CONTENT_TYPE_CODE, contentTypeCode);
		editor.commit();
	}
	
	public String getContentTypeCode() {
		String contentTypeCode = this.preference.getString(this.CONTENT_TYPE_CODE, "");
		return contentTypeCode;
	}
	
	public void setChannelID(String channelID) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.CHANNEL_ID, channelID);
		editor.commit();
	}
	
	public String getChannelID() {
		String channelID = this.preference.getString(this.CHANNEL_ID, "");
		return channelID;
	}
	
	public void setChannelResolution(String resolution) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.CHANNEL_RESOLUTION, resolution);
		editor.commit();
	}
	
	public String getChannelResolution() {
		String resolution = this.preference.getString(this.CHANNEL_RESOLUTION, "H");
		return resolution;
	}
	
	public void setScreenWidth(int width) {
		Log.d("AISLiveTVSlideMenu", "SetScreenWidth: " + width);
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putInt(this.SCREEN_WIDTH, width);
		editor.commit();
	}
	
	public int getScreenWidth() {
		int width = this.preference.getInt(this.SCREEN_WIDTH, 480);
		return width;
	}
	
	public void setScreenHeight(int height) {
		Log.d("AISLiveTVSlideMenu", "SetScreenHeight: " + height);
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putInt(this.SCREEN_HEIGHT, height);
		editor.commit();
	}
	
	public int getScreenHeight() {
		int height = this.preference.getInt(this.SCREEN_HEIGHT, 320);
		return height;
	}
	
	public void setAutoLoginStatus(boolean status) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.AUTO_LOGIN_STATUS, status);
		editor.commit();
	}
	
	public boolean getAutoLoginStatus() {
		boolean status = this.preference.getBoolean(this.AUTO_LOGIN_STATUS, false);
		return status;
	}
	
	public void setIsRegisterActivity(boolean status) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.IS_REGISTER_ACTIVITY, status);
		editor.commit();
	}
	
	public boolean getIsRegisterActivity() {
		boolean status = this.preference.getBoolean(this.IS_REGISTER_ACTIVITY, false);
		return status;
	}
	
	public void setFacebookID(String id) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_ID, id);
		editor.commit();
	}
	
	public String getFacebookID() {
		String id = this.preference.getString(this.FACEBOOK_ID, "");
		return id;
	}
	
	public void setFacebookName(String name) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_NAME, name);
		editor.commit();
	}
	
	public String getFacebookName() {
		String name = this.preference.getString(this.FACEBOOK_NAME, "");
		return name;
	}
	
	public void setFacebookEmail(String email) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_EMAIL, email);
		editor.commit();
	}
	
	public String getFacebookEmail() {
		String email = this.preference.getString(this.FACEBOOK_EMAIL, "");
		return email;
	}
	
	public void setFacebookDOB(String dob) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_DOB, dob);
		editor.commit();
	}
	
	public String getFacebookDOB() {
		String dob = this.preference.getString(this.FACEBOOK_DOB, "");
		return dob;
	}
	
	public void setFacebookGender(String gender) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putString(this.FACEBOOK_GENDER, gender);
		editor.commit();
	}
	
	public String getFacebookGender() {
		String gender = this.preference.getString(this.FACEBOOK_GENDER, "");
		return gender;
	}
	
	public void setAutoLogin(boolean autoLogin) {
		SharedPreferences.Editor editor = this.preference.edit();
		editor.putBoolean(this.AUTOLOGIN, autoLogin);
		editor.commit();
	}
	
	public boolean getAutoLogin() {
		boolean autoLogin = this.preference.getBoolean(this.AUTOLOGIN, false);
		return autoLogin;
	}
}
