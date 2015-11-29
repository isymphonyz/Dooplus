package com.keng.dooplus.thetv.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AISLiveTVChannelDatabase {
	private Context activity = null;
	private SQLiteDatabase sqlite = null;
	
	private final String DB_NAME = "AISLIVETV";
	private final String CHANNEL_TABLE_NAME = "CHANNEL";
	
	public AISLiveTVChannelDatabase(Context activity, SQLiteDatabase sqlite) {
		this.activity = activity;
		this.sqlite = sqlite;
		this.sqlite = this.activity.openOrCreateDatabase(DB_NAME, this.activity.MODE_PRIVATE, null);
		//deleteChannelTable();
		createChannelTable();
	}
	
	//============================= Channel ====================================
	public void createChannelTable() {
		sqlite.execSQL("CREATE TABLE IF NOT EXISTS " + CHANNEL_TABLE_NAME + 
				" (chTypeID INTEGER, chID VARCHAR, chOrder INTEGER, chTitleEng VARCHAR, chTitleTH VARCHAR, chImg VARCHAR, chImgDark VARCHAR, chCharging VARCHAR, chSimLock VARCHAR, enable INTEGER, forIOS INTEGER, favorite INTEGER, base64 BLOB, UNIQUE(chTypeID, chID) ON CONFLICT IGNORE);");
	}
	
	public void selectChannel(String fileName, String customerStoreID, String candidateStaffID, String systemUserID, int status) {
		sqlite.execSQL("SELECT * FROM " + CHANNEL_TABLE_NAME + " WHERE status=" + status + 
				" AND filename='" + fileName + "' AND customerStoreID='" + customerStoreID + "'" + 
				" AND candidateStaffID='" + candidateStaffID + "' AND systemUserID='" + systemUserID + "'" + 
				";");
	}
	
	public void insertChannelData(String chTypeID, String chID, String chOrder, String chTitleEng, String chTitleTH, String chImg, String chImgDark, String chCharging, String chSimLock, String enable, String forIOS, int favorite, String base64) {
		Log.d("AISLiveTV", "Insert: " + chTitleEng + ", " + chTypeID);
		sqlite.execSQL("INSERT INTO " + CHANNEL_TABLE_NAME + " (chTypeID, chID, chOrder, chTitleEng, chTitleTH, chImg, chImgDark, chCharging, chSimLock, enable, forIOS, favorite, base64)" + 
				" VALUES (" + chTypeID + ", '" + chID + "', " + chOrder + ", " + 
				"'" + chTitleEng + "', '" + chTitleTH  + "', '" + chImg  + "', '" + chImgDark + "', " + 
				"'" + chCharging + "', '" + chSimLock + "', " + enable + ", " + forIOS + ", " + favorite + ", " + 
				"'" + base64 + "')");
	}
	
	public void updateChannelData(String chID, int favorite) {
		sqlite.execSQL("UPDATE " + CHANNEL_TABLE_NAME + " SET favorite=" + favorite +
				" WHERE chID='" + chID + "'" + 
				";");
	}
	
	public void deleteChannelData(String filename, String productID) {
		sqlite.execSQL("DELETE FROM " + CHANNEL_TABLE_NAME + " WHERE productID='" + productID + "' AND filename='" + filename + "';");
	}
	
	public void deleteChannelTable() {
		sqlite.execSQL("DROP TABLE IF EXISTS " + CHANNEL_TABLE_NAME + ";");
	}
	//============================== End Channel ==========================================	
	
	public void closeDB() {
		sqlite.close();
	}
}
