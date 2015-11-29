package com.keng.dooplus.thetv.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Utils {

	Activity activity;
	
	public Utils(Activity activity) {
		this.activity = activity;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 2048;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public boolean networkIsConnected() {
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState() == NetworkInfo.State.CONNECTED
				|| connectivityManager.getNetworkInfo(
						ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			// we are connected to a network
			connected = true;
		} else
			connected = false;

		return connected;
	}
	
	public String getStringFromJSON(int file) {
		String json = "";
		try {
	        Resources res = activity.getResources();
	        InputStream in_s = res.openRawResource(file);

	        byte[] b = new byte[in_s.available()];
	        in_s.read(b);
	        json = new String(b);
	    } catch (Exception e) {
	        // e.printStackTrace();
	    }
		return json;
	}
	
	public String getOSVersionName() {
		StringBuilder builder = new StringBuilder();
		builder.append("android : ").append(Build.VERSION.RELEASE);

		String fieldName = "";
		Field[] fields = Build.VERSION_CODES.class.getFields();
		for (Field field : fields) {
		    fieldName = field.getName();
		    int fieldValue = -1;

		    try {
		        fieldValue = field.getInt(new Object());
		    } catch (IllegalArgumentException e) {
		        e.printStackTrace();
		    } catch (IllegalAccessException e) {
		        e.printStackTrace();
		    } catch (NullPointerException e) {
		        e.printStackTrace();
		    }

		    if (fieldValue == Build.VERSION.SDK_INT) {
		        builder.append(" : ").append(fieldName).append(" : ");
		        builder.append("sdk=").append(fieldValue);
		    }
		}
		
		return fieldName;
	}
	
	public String getAppVersion() {
		PackageInfo pInfo;
        String version = "";
		try {
			pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
	        version = pInfo.versionName;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return version;
	}

	public String getCarrierName() {
		TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		String carrierName = manager.getSimOperatorName();
		
		return carrierName;
	}
	
	public String getConnectionType() {
		if(Connectivity.isConnectedWifi(activity)) {
			return "wifi";
		} else {
			return Connectivity.getSubTypeName(activity);
		}
	}

	public String getLocale() {
		return Locale.getDefault().getDisplayLanguage();
	}
	
	public static Bitmap getBitmapFromURL(String src) {
		Log.d("AISLiveTV", "URL: " + src);
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static Bitmap getBitmapFromBase64(String strBase64) {
		byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
		return bitmap;
	}
	
}
