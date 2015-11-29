package com.keng.dooplus.thetv.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AISLiveTVConntentTypeDatabase {
	private Context activity = null;
	private SQLiteDatabase sqlite = null;
	
	private final String DB_NAME = "AISLIVETV";
	private final String CONTENT_TYPE_TABLE_NAME = "CONTENT_TYPE";
	private final String CHOICE_TABLE_NAME = "CHOICE";
	
	public AISLiveTVConntentTypeDatabase(Context activity, SQLiteDatabase sqlite) {
		this.activity = activity;
		this.sqlite = sqlite;
		this.sqlite = this.activity.openOrCreateDatabase(DB_NAME, this.activity.MODE_PRIVATE, null);
		createContentTypeTable();
		createChoiceTable();
	}
	
	//============================= ContentType ====================================
	public void createContentTypeTable() {
		sqlite.execSQL("CREATE TABLE IF NOT EXISTS " + CONTENT_TYPE_TABLE_NAME + 
				" (defaultType INTEGER, chTypeID INTEGER, chTypeCode VARCHAR, chTypeEng VARCHAR, chTypeTH VARCHAR, chTypeImg VARCHAR, chTypeImgDark VARCHAR, dbChName VARCHAR, dbChLink VARCHAR, dbChEPG VARCHAR, enable INTEGER, base64 BLOB, UNIQUE(chTypeID) ON CONFLICT IGNORE);");
	}
	
	public void selectContentType(String fileName, String customerStoreID, String candidateStaffID, String systemUserID, int status) {
		sqlite.execSQL("SELECT * FROM " + CONTENT_TYPE_TABLE_NAME + " WHERE status=" + status + 
				" AND filename='" + fileName + "' AND customerStoreID='" + customerStoreID + "'" + 
				" AND candidateStaffID='" + candidateStaffID + "' AND systemUserID='" + systemUserID + "'" + 
				";");
	}
	
	public void insertContentTypeData(String defaultType, String chTypeID, String chTypeCode, String chTypeEng, String chTypeTH, String chTypeImg, String chTypeImgDark, String dbChName, String dbChLink, String dbChEPG, String enable, String base64) {
		Log.d("AISLiveTV", "Insert chTypeID: " + chTypeID + ", chTypeEng: " + chTypeEng);
		sqlite.execSQL("INSERT INTO " + CONTENT_TYPE_TABLE_NAME + " (defaultType, chTypeID, chTypeCode, chTypeEng, chTypeTH, chTypeImg, chTypeImgDark, dbChName, dbChLink, dbChEPG, enable, base64)" +
				" VALUES (" + defaultType + ", " + chTypeID + ", '" + chTypeCode + "', " + 
				"'" + chTypeEng + "', '" + chTypeTH  + "', '" + chTypeImg  + "', '" + chTypeImgDark + "', " + 
				"'" + dbChName + "', '" + dbChLink + "', '" + dbChEPG + "', " + enable + ", " + 
				"'" + base64 + "')");
	}
	
	public void updateContentTypeData(String fileName, String customerStoreID, String candidateStaffID, String systemUserID, int ContentTypeID, int questionID, String userAnswer, int status) {
		sqlite.execSQL("UPDATE " + CONTENT_TYPE_TABLE_NAME + " SET userAnswer='" + userAnswer + "', status=" + status +
				" WHERE filename='" + fileName + "' AND customerStoreID='" + customerStoreID + "'" + 
				" AND candidateStaffID='" + candidateStaffID + "' AND systemUserID='" + systemUserID + "'" + 
				" AND ContentTypeID=" + ContentTypeID + " AND questionID=" + questionID + 
				";");
	}
	
	public void deleteContentTypeData(String filename, String productID) {
		sqlite.execSQL("DELETE FROM " + CONTENT_TYPE_TABLE_NAME + " WHERE productID='" + productID + "' AND filename='" + filename + "';");
	}
	
	public void deleteContentTypeTable() {
		sqlite.execSQL("DROP TABLE IF EXISTS " + CONTENT_TYPE_TABLE_NAME + ";");
	}
	//============================== End ContentType ==========================================	
	
	//============================= Choice ====================================
	public void createChoiceTable() {
		sqlite.execSQL("CREATE TABLE IF NOT EXISTS " + CHOICE_TABLE_NAME + 
				" (filename VARCHAR, customerStoreID VARCHAR, candidateStaffID VARCHAR, systemUserID VARCHAR, ContentTypeID INTEGER, questionID INTEGER, choiceID INTEGER, sequence INTEGER, title VARCHAR, value VARCHAR, UNIQUE(filename, ContentTypeID, questionID, choiceID) ON CONFLICT IGNORE);");
	}
	
	public void selectChoiceData(String fileName, String customerStoreID, String candidateStaffID, String systemUserID, String ContentTypeID, String questionID) {
		sqlite.execSQL("SELECT * FROM " + CHOICE_TABLE_NAME + " WHERE ContentTypeID=" + ContentTypeID + "" + 
				" AND filename='" + fileName + "' AND customerStoreID='" + customerStoreID + "'" + 
				" AND candidateStaffID='" + candidateStaffID + "' AND systemUserID='" + systemUserID + "'" + 
				" AND questionID=" + questionID + 
				" ORDER BY sequence ASC;");
	}
	
	public void insertChoiceData(String fileName, String customerStoreID, String candidateStaffID, String systemUserID, String ContentTypeID, String questionID, String choiceID, String sequence, String title, String value) {
		String query = "INSERT INTO " + CHOICE_TABLE_NAME + " (filename, customerStoreID, candidateStaffID, systemUserID, ContentTypeID, questionID, choiceID, sequence, title, value)" + 
				" VALUES ('" + fileName + "', '" + customerStoreID + "', '" + candidateStaffID + "', '" + 
				systemUserID + "', " + ContentTypeID  + ", " + questionID  + ", " + choiceID + ", " + 
				sequence + ", '" + title + "', '" + value + "')";
		Log.d("PCConsult", "Query: " + query);
		Log.d("PCConsult", "Insert Choice");
		sqlite.execSQL("INSERT INTO " + CHOICE_TABLE_NAME + " (filename, customerStoreID, candidateStaffID, systemUserID, ContentTypeID, questionID, choiceID, sequence, title, value)" + 
				" VALUES ('" + fileName + "', '" + customerStoreID + "', '" + candidateStaffID + "', '" + 
				systemUserID + "', " + ContentTypeID  + ", " + questionID  + ", " + choiceID + ", " + 
				sequence + ", '" + title + "', '" + value + "')");
	}
	
	public void updateChoiceData(String fileName, String customerStoreID, String candidateStaffID, String systemUserID, String ContentTypeID, String questionID, String choiceID, String userAnswer) {
		sqlite.execSQL("UPDATE " + CHOICE_TABLE_NAME + " SET userAnswer='" + userAnswer + "'" +
				" WHERE filename='" + fileName + "' AND customerStoreID='" + customerStoreID + "'" + 
				" AND candidateStaffID='" + candidateStaffID + "' AND systemUserID='" + systemUserID + "'" + 
				" AND ContentTypeID=" + ContentTypeID + " AND questionID=" + questionID + " AND choiceID=" + choiceID + 
				";");
	}
	
	public void deleteChoiceData(int locationID, String locationName) {
		sqlite.execSQL("DELETE FROM " + CHOICE_TABLE_NAME + " WHERE locationID=" + locationID + "' AND locationName='" + locationName + "';");
	}
	
	public void deleteChoiceTable() {
		sqlite.execSQL("DROP TABLE IF EXISTS " + CHOICE_TABLE_NAME + ";");
	}
	//============================== End Choice ==========================================
	
	public void closeDB() {
		sqlite.close();
	}
}
