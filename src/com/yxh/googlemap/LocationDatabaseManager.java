package com.yxh.googlemap;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

public class LocationDatabaseManager {
	 private MapDatabaseHelper mapDatabaseHelper;
	 private SQLiteDatabase locationDB;
	 
	 private static final String TAG = "LocationDatabaseManager";
	
	public LocationDatabaseManager(Context context){
		mapDatabaseHelper = new MapDatabaseHelper(context);
	}

	   public void open() throws SQLException{
	         locationDB = mapDatabaseHelper.getWritableDatabase();	         
	         Log.i(TAG, "得到数据库表格");
	    }
	    
	    public void close(){
	        locationDB.close();	        
	        Log.i(TAG, "关闭数据表格");
	    }
	    
	    /*
	     * 往Location数据表格中增加 location
	     */
	    public void addLocation(Location location, long id){
	    	ContentValues cv = new ContentValues();
	    	cv.put(MapDatabaseHelper.LOCATION_LATITUDE, location.getLatitude());
	    	cv.put(MapDatabaseHelper.LOCATION_LONGTITUDE, location.getLongitude());
	    	cv.put(MapDatabaseHelper.LOCATION_ALTITUDE, location.getAltitude());
	    	cv.put(MapDatabaseHelper.PROJECT_ID, id);
	    	  
	    	Log.i(TAG, "addLocation()  Latitude: " + location.getLatitude());
	    	Log.i(TAG, "addLocation()  Longtitude: " + location.getLongitude());
	    	Log.i(TAG, "addLocation()  Altitude: " + location.getAltitude());
	    	Log.i(TAG, "addLocation()  ProjectId: " + id);
	    	Log.i(TAG, "addLocation()  cv: " + cv);	    	
	    	
	    	locationDB.insert(MapDatabaseHelper.TABLE_LOCATION, null, cv); 
	    	
	    	Log.i(TAG, "addLocation成功");
	    }
	    
	    /*
	     * 得到最新的location信息
	     */
	    public List<LocationObject> getLocation(){
	    	
	    	List<LocationObject> locations = new ArrayList<LocationObject>();
	    	
	    	Cursor cursor = locationDB.query(MapDatabaseHelper.TABLE_LOCATION, null, null, null,null, null, null);
	    	
	    	if (cursor.getCount() != 0 && cursor.moveToLast()){
	    		
	    		String id = cursor.getString(cursor.getColumnIndex(MapDatabaseHelper.LOCATION_ID));
	    		String latitude = cursor.getString(cursor.getColumnIndex(MapDatabaseHelper.LOCATION_LATITUDE));
	    		String longitude = cursor.getString(cursor.getColumnIndex(MapDatabaseHelper.LOCATION_LONGTITUDE));
	    		String altitude = cursor.getString(cursor.getColumnIndex(MapDatabaseHelper.LOCATION_ALTITUDE));
	    		String projectId = cursor.getString(cursor.getColumnIndex(MapDatabaseHelper.PROJECT_ID));
	    		
	    		LocationObject mLocationObject = new LocationObject(id, latitude, longitude, altitude, projectId);
	    		
	    		locations.add(mLocationObject);
	    		
	    		Log.i(TAG, "getLocation()  cursorCount: " + cursor.getCount());
	    		Log.i(TAG, "getLocation()  id: " + id);
	    		Log.i(TAG, "getLocation()  latitude: " + latitude);
	    		Log.i(TAG, "getLocation()  longitude: " + longitude);
	    		Log.i(TAG, "getLocation()  altitude: " + altitude);
	    		Log.i(TAG, "getLocation()  projectId: " + projectId);
	    		Log.i(TAG, "getLocation()  Locations: " + locations.toString().trim());
	    		
	    	}
	    	cursor.close();
			return locations;			
			
	    }
	    
	    
}

