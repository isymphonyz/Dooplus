package com.keng.dooplus.thetv.database;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.keng.dooplus.thetv.utils.AppPreference;
import com.keng.dooplus.thetv.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AISLiveTVDatabaseConnection extends AsyncTask<String, Void, String>{

	private AsyncDatabase ayncConnection = null;
	public static String DATABASE_ERROR = "DATABASE_ERROR";
	public static String DATABASE_SUCCESS = "DATABASE_SUCCESS";
	public static String DATA_FORMAT_ERROR = "DATA_FORMAT_ERROR";

	AISLiveTVConntentTypeDatabase databaseContentType;
	AISLiveTVChannelDatabase databaseChannel;
	AppPreference appPreference;
	
	ArrayList<String> chTypeIDList;
	ArrayList<String> chTypeEngList;
	ArrayList<String> chTypeCodeList;
	
	public AISLiveTVDatabaseConnection(AsyncDatabase ayncConnection) {
		this.ayncConnection = ayncConnection;
	}

	public void setDatabaseContentType(AISLiveTVConntentTypeDatabase databaseContentType) {
		this.databaseContentType = databaseContentType;
	}
	
	public void setDatabaseChannel(AISLiveTVChannelDatabase databaseChannel) {
		this.databaseChannel = databaseChannel;
	}
	
	public void setAppPreference(AppPreference appPreference) {
		this.appPreference = appPreference;
	}
	
	protected String doInBackground(String... data) {

		String result = "";

		databaseContentType.deleteContentTypeTable();
		databaseContentType.createContentTypeTable();
		
		databaseChannel.deleteChannelTable();
		databaseChannel.createChannelTable();
		
		try {
			String defaultType = "";
			String chTypeID = "";
			String chTypeCode = "";
			String chTypeEng = "";
			String chTypeTH = "";
			String chTypeImg = "";
			String chTypeImgDark = "";
			String dbChName = "";
			String dbChLink = "";
			String dbChEPG = "";
			String enable = "";
			chTypeIDList = new ArrayList<String>();
			chTypeEngList = new ArrayList<String>();
			chTypeCodeList = new ArrayList<String>();

			String chID = "";
			String chOrder = "";
			String chTitleEng = "";
			String chTitleTH = "";
			String chImg = "";
			String chImgDark = "";
			String chCharging = "";
			String chSimLock = "";
			String _enable = "";
			String forIOS = "";
			
			//databaseContentType.deleteContentTypeTable();
			//databaseChannel.deleteChannelTable();
			JSONObject jObj = new JSONObject(data[0]);
			JSONArray jArrayType = jObj.getJSONArray("Type");
			for(int x=0; x<jArrayType.length(); x++) {
				defaultType = jArrayType.getJSONObject(x).getString("DefaultType");
				chTypeID = jArrayType.getJSONObject(x).getString("Ch_type_id");
				chTypeCode = jArrayType.getJSONObject(x).getString("Ch_type_code").replace("'","");
				chTypeEng = jArrayType.getJSONObject(x).getString("Ch_type_eng").replace("'","");
				chTypeTH = jArrayType.getJSONObject(x).getString("Ch_type_th").replace("'","");
				chTypeImg = jArrayType.getJSONObject(x).getString("Ch_type_img");
				chTypeImgDark = jArrayType.getJSONObject(x).getString("Ch_type_imgdark");
				dbChName = jArrayType.getJSONObject(x).getString("DB_ChName");
				dbChEPG = jArrayType.getJSONObject(x).getString("DB_ChLink");
				dbChLink = jArrayType.getJSONObject(x).getString("DB_ChEpg");
				enable = jArrayType.getJSONObject(x).getString("Enable");

				chTypeIDList.add(chTypeID);
				chTypeEngList.add(chTypeEng);
				chTypeCodeList.add(chTypeCode);
				databaseContentType.insertContentTypeData(defaultType, chTypeID, chTypeCode, chTypeEng, chTypeTH, chTypeImg, chTypeImgDark, dbChName, dbChLink, dbChEPG, enable, convertImageToBase64(chTypeImg));
			}
			
			JSONObject jObjContent = jObj.getJSONObject("Content");
			for(int x=0; x<chTypeEngList.size(); x++) {
				Log.d("AISLiveTV", "Content Type: " + chTypeEngList.get(x));
				JSONArray jArrayContent = jObjContent.getJSONArray(chTypeEngList.get(x));
				for(int y=0; y<jArrayContent.length(); y++) {
					chID = jArrayContent.getJSONObject(y).getString("Ch_id");
					chOrder = jArrayContent.getJSONObject(y).getString("Ch_order");
					chTitleEng = jArrayContent.getJSONObject(y).getString("Ch_title_eng").replace("'","");
					chTitleTH = jArrayContent.getJSONObject(y).getString("Ch_title_th").replace("'","");
					chImg = jArrayContent.getJSONObject(y).getString("Ch_img");
					chImgDark = jArrayContent.getJSONObject(y).getString("Ch_img_dark");
					chCharging = jArrayContent.getJSONObject(y).getString("Ch_charging").replace("'","");
					chSimLock = jArrayContent.getJSONObject(y).getString("Ch_simLock").replace("'","");
					_enable = jArrayContent.getJSONObject(y).getString("Enable");
					forIOS = jArrayContent.getJSONObject(y).getString("For_ios");
					databaseChannel.insertChannelData(chTypeIDList.get(x), chID, chOrder, chTitleEng, chTitleTH, chImg, chImgDark, chCharging, chSimLock, _enable, forIOS, 0, convertImageToBase64(chImg));
				}
			}
			appPreference.setDatabaseStatus(true);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("AISLiveTV", "Exception: " + e.toString());
		}
		
		databaseChannel.closeDB();
		databaseContentType.closeDB();
		databaseChannel = null;
		databaseContentType = null;
		
	    return result;
	}

	protected void onPostExecute(String result) {
		// TODO: check this.exception 
		// TODO: do something with the feed
		Log.d("Connection", result);
		this.ayncConnection.databaseCallback(result);
	}
	
	public String convertImageToBase64(String url) {
		String base64 = "";
		try {
			Bitmap bitmapOrg = Utils.getBitmapFromURL(url);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			bitmapOrg.compress(Bitmap.CompressFormat.PNG, 100, bao);
			byte [] ba = bao.toByteArray();
			base64 = Base64.encodeToString(ba, Base64.DEFAULT);
		} catch(Exception e) {
			
		}
		return base64;
	}
}
