package com.yxh.googlemap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MapDatabaseHelper extends SQLiteOpenHelper{
	
	private static final String TAG = "MapDatabaseHelper";
	private static final String DB_NAME = "maps.db";
	private  static final int VERSION = 1;
	
	// Project表格的属性
	public  static final String TABLE_PROJECT = "project";
	public static final  String PROJECT_START_DATE = "project_start_date";
	public static final String PROJECT_ID = "project_id";
	public static final String PROJECT_NAME = "project_name";
	public static final String PROJECT_DESCRIBE = "project_describe";
	public static final String PROJECT_CREATE_TIME = "project_create_time";
	
	// Location表格的属性
	public  static final String TABLE_LOCATION = "location";
	public static final String LOCATION_ID = "location_id";	
    public static final String LOCATION_LATITUDE = "latitude";
    public static final String LOCATION_LONGTITUDE = "longtitude";
    public static final String LOCATION_ALTITUDE = "altitude";
    
    
    // Marker表格属性
    public  static final String TABLE_MARKER = "marker";
    public static final String MARKER_ID = "marker_id";
    public static final String MARKER_ATTRIBUTE = "marker_attribute";
    

	public MapDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);		
	}
	
	// 创建Project数据表格
	public static final String CREATE_PROJECT = " create table  " + TABLE_PROJECT +" ( " 
					+ PROJECT_ID + " integer primary key autoincrement, " +PROJECT_NAME + " VARCHAR not null,"
					+ PROJECT_DESCRIBE + " VARCHAR," + PROJECT_CREATE_TIME + " VARCHAR not null)";
	  // 创建Location数据表格
		public static final String CREATE_LOCATION = " create table  " + TABLE_LOCATION +" ( " 				
				+ LOCATION_ID + " integer primary key autoincrement," + LOCATION_LATITUDE + " real," 
				+ LOCATION_LONGTITUDE + " real," + LOCATION_ALTITUDE + " real," + PROJECT_ID + " real)";
		
	  // 创建Marker数据表格 
       public static final String CREATE_MARKER = " create table " + TABLE_MARKER + " ( "
    		   	+ MARKER_ID + " real," + LOCATION_LATITUDE + " real," 
				+ LOCATION_LONGTITUDE + " real," + LOCATION_ALTITUDE + " real," + PROJECT_NAME + " varchar, " 
    		   	+ PROJECT_ID + " real," + MARKER_ATTRIBUTE + " varchar)";

    // 数据库第一次被创建时onCreate会被调用  
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建project 数据表
		db.execSQL(CREATE_PROJECT);
		Log.i(TAG, "创建 project 数据表成功" + CREATE_PROJECT);
		
		// 创建 location数据表
		db.execSQL(CREATE_LOCATION);
		Log.i(TAG, "创建 locaiton 数据表成功" + CREATE_LOCATION);
		
		// 创建 marker 数据表
		db.execSQL(CREATE_MARKER);
		Log.i(TAG, "创建 marker 数据表成功 " + CREATE_MARKER);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		   db.execSQL("DROP TABLE IF EXISTS" + TABLE_PROJECT);
		   db.execSQL("DROP TABLE IF EXISTS" + TABLE_LOCATION);		
		   db.execSQL("DROP TABLE IF EXISTS" + TABLE_MARKER);		     
		  
	        onCreate(db);
		
	}

}
