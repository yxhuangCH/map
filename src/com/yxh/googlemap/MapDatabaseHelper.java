package com.yxh.googlemap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MapDatabaseHelper extends SQLiteOpenHelper{
	
	private static final String TAG = "MapDatabaseHelper";
	private static final String DB_NAME = "maps.db";
	private  static final int VERSION = 1;
	
	// Project��������
	public  static final String TABLE_PROJECT = "project";
	public static final  String PROJECT_START_DATE = "project_start_date";
	public static final String PROJECT_ID = "project_id";
	public static final String PROJECT_NAME = "project_name";
	public static final String PROJECT_DESCRIBE = "project_describe";
	public static final String PROJECT_CREATE_TIME = "project_create_time";
	
	// Location��������
	public  static final String TABLE_LOCATION = "location";
	public static final String LOCATION_ID = "location_id";	
    public static final String LOCATION_LATITUDE = "latitude";
    public static final String LOCATION_LONGTITUDE = "longtitude";
    public static final String LOCATION_ALTITUDE = "altitude";
    
    
    // Marker�������
    public  static final String TABLE_MARKER = "marker";
    public static final String MARKER_ID = "marker_id";
    public static final String MARKER_ATTRIBUTE = "marker_attribute";
    

	public MapDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);		
	}
	
	// ����Project���ݱ��
	public static final String CREATE_PROJECT = " create table  " + TABLE_PROJECT +" ( " 
					+ PROJECT_ID + " integer primary key autoincrement, " +PROJECT_NAME + " VARCHAR not null,"
					+ PROJECT_DESCRIBE + " VARCHAR," + PROJECT_CREATE_TIME + " VARCHAR not null)";
	  // ����Location���ݱ��
		public static final String CREATE_LOCATION = " create table  " + TABLE_LOCATION +" ( " 				
				+ LOCATION_ID + " integer primary key autoincrement," + LOCATION_LATITUDE + " real," 
				+ LOCATION_LONGTITUDE + " real," + LOCATION_ALTITUDE + " real," + PROJECT_ID + " real)";
		
	  // ����Marker���ݱ�� 
       public static final String CREATE_MARKER = " create table " + TABLE_MARKER + " ( "
    		   	+ MARKER_ID + " real," + LOCATION_LATITUDE + " real," 
				+ LOCATION_LONGTITUDE + " real," + LOCATION_ALTITUDE + " real," + PROJECT_NAME + " varchar, " 
    		   	+ PROJECT_ID + " real," + MARKER_ATTRIBUTE + " varchar)";

    // ���ݿ��һ�α�����ʱonCreate�ᱻ����  
	@Override
	public void onCreate(SQLiteDatabase db) {
		//����project ���ݱ�
		db.execSQL(CREATE_PROJECT);
		Log.i(TAG, "���� project ���ݱ�ɹ�" + CREATE_PROJECT);
		
		// ���� location���ݱ�
		db.execSQL(CREATE_LOCATION);
		Log.i(TAG, "���� locaiton ���ݱ�ɹ�" + CREATE_LOCATION);
		
		// ���� marker ���ݱ�
		db.execSQL(CREATE_MARKER);
		Log.i(TAG, "���� marker ���ݱ�ɹ� " + CREATE_MARKER);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		   db.execSQL("DROP TABLE IF EXISTS" + TABLE_PROJECT);
		   db.execSQL("DROP TABLE IF EXISTS" + TABLE_LOCATION);		
		   db.execSQL("DROP TABLE IF EXISTS" + TABLE_MARKER);		     
		  
	        onCreate(db);
		
	}

}
