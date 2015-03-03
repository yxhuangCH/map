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

public class MarkerDatabaseManager {

	private MapDatabaseHelper mapDatabaseHelper;
	private SQLiteDatabase markerDB;
	
	private static final String TAG = "MarkerDatabaseManager";

//	private boolean mOneProject = false; // �Ƿ�Ϊͬһ�����̱�ʶ

	public MarkerDatabaseManager(Context context) {
		mapDatabaseHelper = new MapDatabaseHelper(context);
	}

	public void open() throws SQLException {
		markerDB = mapDatabaseHelper.getWritableDatabase();
		Log.i(TAG, "�õ����ݿ���");
	}

	public void close() {
		markerDB.close();
		Log.i(TAG, "�ر����ݱ��:marker");
	}

	/*
	 * ��Marker���ݱ�������� markerLocation
	 */
	public void addMarkerLocation(Location location, String projectName, long id) {
		ContentValues cv = new ContentValues();
		cv.put(MapDatabaseHelper.LOCATION_LATITUDE, location.getLatitude());
		cv.put(MapDatabaseHelper.LOCATION_LONGTITUDE, location.getLongitude());
		cv.put(MapDatabaseHelper.LOCATION_ALTITUDE, location.getAltitude());
		cv.put(MapDatabaseHelper.PROJECT_NAME, projectName);
		cv.put(MapDatabaseHelper.PROJECT_ID, id);

		Log.i(TAG, "Latitude: " + location.getLatitude());
		Log.i(TAG, "Langtitude: " + location.getLongitude());
		Log.i(TAG, "Altitude: " + location.getAltitude());
		Log.i(TAG, "ProjectName: " + projectName);
		Log.i(TAG, "ProjectId: " + id);
		Log.i(TAG, "cv: " + cv);

		markerDB.insert(MapDatabaseHelper.TABLE_MARKER, null, cv);

		Log.i(TAG, "marker�����ݿ�ɹ�");
	}

	public void addMarkerLocation(double latitude, double longitude,
			double dAltitude, String projectName, String id) {
		ContentValues cv = new ContentValues();
		cv.put(MapDatabaseHelper.LOCATION_LATITUDE, latitude);
		cv.put(MapDatabaseHelper.LOCATION_LONGTITUDE, longitude);
		cv.put(MapDatabaseHelper.LOCATION_ALTITUDE, dAltitude);
		cv.put(MapDatabaseHelper.PROJECT_NAME, projectName);
		cv.put(MapDatabaseHelper.PROJECT_ID, id);

		Log.i(TAG, "addMarkerLocation()  Latitude: " + latitude);
		Log.i(TAG, "addMarkerLocation()  Longtitude: " + longitude);
		Log.i(TAG, "addMarkerLocation()  Altitude: " + dAltitude);
		Log.i(TAG, "addMarkerLocation()  ProjectName: " + projectName);
		Log.i(TAG, "addMarkerLocation()  ProjectId: " + id);
		Log.i(TAG, "addMarkerLocation()  cv: " + cv);

		markerDB.insert(MapDatabaseHelper.TABLE_MARKER, null, cv);

		Log.i(TAG, "marker�����ݿ�ɹ�");
	}

	/**
	 * 
	 * @param latitude
	 *            ����
	 * @param longitude
	 *            γ��
	 * @param dAltitude
	 *            ����
	 * @param projectName
	 *            ��Ŀ����
	 * @param id
	 *            ��ĿID
	 * @param markerAttribute
	 *            ������
	 */
	public void addMarkerLocation(double latitude, double longitude,
			double dAltitude, String projectName, String id,
			String markerAttribute) {
		ContentValues cv = new ContentValues();
		cv.put(MapDatabaseHelper.LOCATION_LATITUDE, latitude);
		cv.put(MapDatabaseHelper.LOCATION_LONGTITUDE, longitude);
		cv.put(MapDatabaseHelper.LOCATION_ALTITUDE, dAltitude);
		cv.put(MapDatabaseHelper.PROJECT_NAME, projectName);
		cv.put(MapDatabaseHelper.PROJECT_ID, id);
		cv.put(MapDatabaseHelper.MARKER_ATTRIBUTE, markerAttribute);

		Log.i(TAG, "addMarkerLocation()  Latitude: " + latitude);
		Log.i(TAG, "addMarkerLocation()  Longtitude: " + longitude);
		Log.i(TAG, "addMarkerLocation()  Altitude: " + dAltitude);
		Log.i(TAG, "addMarkerLocation()  ProjectName: " + projectName);
		Log.i(TAG, "addMarkerLocation()  ProjectId: " + id);
		Log.i(TAG, "addMarkerLocation()  markerAttribute: " + markerAttribute);

		Log.i(TAG, "addMarkerLocation()  cv: " + cv);

		markerDB.insert(MapDatabaseHelper.TABLE_MARKER, null, cv);

		Log.i(TAG, "marker�����ݿ�ɹ�");
	}

	public void addMarkerLocation(int markerId, double latitude,
			double longitude, double dAltitude, String projectName, String id,
			String markerAttribute) {
		ContentValues cv = new ContentValues();
		cv.put(MapDatabaseHelper.MARKER_ID, markerId);
		cv.put(MapDatabaseHelper.LOCATION_LATITUDE, latitude);
		cv.put(MapDatabaseHelper.LOCATION_LONGTITUDE, longitude);
		cv.put(MapDatabaseHelper.LOCATION_ALTITUDE, dAltitude);
		cv.put(MapDatabaseHelper.PROJECT_NAME, projectName);
		cv.put(MapDatabaseHelper.PROJECT_ID, id);
		cv.put(MapDatabaseHelper.MARKER_ATTRIBUTE, markerAttribute);

		Log.i(TAG, "addMarkerLocation()  markerId: " + markerId);
		Log.i(TAG, "addMarkerLocation()  Latitude: " + latitude);
		Log.i(TAG, "addMarkerLocation()  Longtitude: " + longitude);
		Log.i(TAG, "addMarkerLocation()  Altitude: " + dAltitude);
		Log.i(TAG, "addMarkerLocation()  ProjectName: " + projectName);
		Log.i(TAG, "addMarkerLocation()  ProjectId: " + id);
		Log.i(TAG, "addMarkerLocation()  markerAttribute: " + markerAttribute);

		Log.i(TAG, "addMarkerLocation()  cv: " + cv);

		markerDB.insert(MapDatabaseHelper.TABLE_MARKER, null, cv);

		Log.i(TAG, "marker�����ݿ�ɹ�");
	}

	// ��ȡMarkerID,����������һ��markerʱ��ID����1
	public int getMarkerID(String currentProject) {
		Log.i(TAG, "getMarkerID()");
		int MaxID = 0; // ����MarkerID
		Cursor cursor = markerDB.query(MapDatabaseHelper.TABLE_MARKER, null,
				null, null, null, null, null);

		// �ж��Ƿ�Ϊͬһ������
		boolean oneproject = oneProject(currentProject);
		if (oneproject) {
			// �����ͬһ�����̣��������һ��markerId,�򷵻�0.
			if (cursor.moveToLast()) {
				// ����Cursor����ȡ�����һ��markerId
				String id = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.MARKER_ID));
				Log.i(TAG, "getMarkerID() id: " + id);
				MaxID = Integer.parseInt(id);
				Log.i(TAG, "if MaxID: " + MaxID);
			}
		} else {
			MaxID = 0;
		}
		cursor.close();
		Log.i(TAG, "MaxID: " + MaxID);
		return MaxID;
	}

	// ��ȡMarker
	public List<MarkerObject> getMyMarkers() {

		List<MarkerObject> markers = new ArrayList<MarkerObject>();

		Cursor cursor = markerDB.query(MapDatabaseHelper.TABLE_MARKER, null,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String id = cursor.getString(cursor
					.getColumnIndex(MapDatabaseHelper.MARKER_ID));
			String latitude = cursor.getString(cursor
					.getColumnIndex(MapDatabaseHelper.LOCATION_LATITUDE));
			String longitude = cursor.getString(cursor
					.getColumnIndex(MapDatabaseHelper.LOCATION_LONGTITUDE));
			String altitude = cursor.getString(cursor
					.getColumnIndex(MapDatabaseHelper.LOCATION_ALTITUDE));
			String projectName = cursor.getString(cursor
					.getColumnIndex(MapDatabaseHelper.PROJECT_NAME));
			String projectId = cursor.getString(cursor
					.getColumnIndex(MapDatabaseHelper.PROJECT_ID));
			String markerAttribute = cursor.getString(cursor
					.getColumnIndex(MapDatabaseHelper.MARKER_ATTRIBUTE));

			Log.i(TAG, "getMyMarkers()  id: " + id);
			Log.i(TAG, "getMyMarkers()  latitude: " + latitude);
			Log.i(TAG, "getMyMarkers()  longitude: " + longitude);
			Log.i(TAG, "getMyMarkers()  altitude: " + altitude);
			Log.i(TAG, "getMyMarkers()  projectName: " + projectName);
			Log.i(TAG, "getMyMarkers()  projectId: " + projectId);
			Log.i(TAG, "getMyMarkers()  markerAttribute: " + markerAttribute);

			long Lid;
			Double dLatitude;
			Double dLongitude;
			Double dAltitude;

			try {
				Lid = Long.parseLong(id);
				dLatitude = Double.parseDouble(latitude);
				dLongitude = Double.parseDouble(longitude);
				dAltitude = Double.parseDouble(altitude);

				MarkerObject m = new MarkerObject(Lid, dLatitude, dLongitude,
						dAltitude, projectName, projectId, markerAttribute);

				markers.add(m);

				Log.i(TAG, "getMyMarkers()  id: " + Lid);
				Log.i(TAG, "getMyMarkers()  latitude: " + dLatitude);
				Log.i(TAG, "getMyMarkers()  longitude: " + dLongitude);
				Log.i(TAG, "getMyMarkers()  altitude: " + dAltitude);
				Log.i(TAG, "getMyMarkers()  projectName: " + projectName);
				Log.i(TAG, "getMyMarkers()  projectId: " + projectId);
				Log.i(TAG, "getMyMarkers()  projectId: " + markerAttribute);

			} catch (Exception e) {
				e.printStackTrace();
			}

			cursor.moveToNext();
		}
		cursor.close();

		return markers;
	}

	// ɾ������Marker���ݴ����ݱ����
	public void deleteSingleMarker(String my_id) {
		markerDB.delete(MapDatabaseHelper.TABLE_MARKER, "marker_id = ? ",
				new String[] { my_id });
		Log.i(TAG, "ɾ������Marker�ɹ�, my_id: " + my_id);
	}

	// ɾ���������̵�Marker���ݴ����ݱ����
	public void deleteAllMarker(String currentProjectId) {
		markerDB.delete(MapDatabaseHelper.TABLE_MARKER, "project_id = ? ",
				new String[] { currentProjectId });
		Log.i(TAG, "ɾ����������Marker�ɹ���currentProjectId: " + currentProjectId);
	}

	// �ж��Ƿ�Ϊͬһ������
	public boolean oneProject(String string) {
		boolean oneproject = false;
//		 String previousProjectName = previousProjectName();
         String lastProjectName = lastProjectName();
         Log.i(TAG, "oneProject() lastProjectName: " + lastProjectName + "   currentName: " + string);
		if (lastProjectName.equals(string) ) {
			// ������һ���е���Ŀ������ǰһ�е���Ŀ������ͬ������Ϊ��ͬһ������
			oneproject = true;
		}
		Log.i(TAG, "onProject() oneproject: " + oneproject);
		return oneproject;
	}
	
	// �Եõ����һ�е���Ŀ����
	public String lastProjectName(){
		String lastProjectName = "";
		Cursor cursor = markerDB.query(MapDatabaseHelper.TABLE_MARKER, null, null, null, null, null, null);
		if(cursor.moveToLast()){
			lastProjectName = cursor.getString(cursor.getColumnIndex(MapDatabaseHelper.PROJECT_NAME));
			Log.i(TAG, "lastProjectName: " + lastProjectName);
		}
		return lastProjectName;
	}

	// �Ե������ڶ��е���Ŀ����
	public String previousProjectName(){
		Log.i(TAG, "previousProjectName()");
		String previousProjectName = "";
		Cursor cursor = markerDB.query(MapDatabaseHelper.TABLE_PROJECT, null, null, null, null, null, null);
		int count = cursor.getCount();
		Log.i(TAG, "count: " + count);
		
		if (cursor.moveToPosition(count -2 )) {
		 previousProjectName = cursor.getString(cursor.getColumnIndex(MapDatabaseHelper.PROJECT_NAME));
			Log.i(TAG, "previousProjectName: " + previousProjectName);
		}
		return previousProjectName;
	}
	

	// ����ͬһ�����̵�Maker
	// ��ȡMarker
		public List<MarkerObject> getOneMyMarkers(String projectID) {

			List<MarkerObject> markers = new ArrayList<MarkerObject>();

			Cursor cursor = markerDB.query(MapDatabaseHelper.TABLE_MARKER, null,
					"project_id=?", new String[]{projectID}, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String id = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.MARKER_ID));
				String latitude = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.LOCATION_LATITUDE));
				String longitude = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.LOCATION_LONGTITUDE));
				String altitude = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.LOCATION_ALTITUDE));
				String projectName = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.PROJECT_NAME));
				String projectId = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.PROJECT_ID));
				String markerAttribute = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.MARKER_ATTRIBUTE));

				long Lid;
				Double dLatitude;
				Double dLongitude;
				Double dAltitude;

				try {
					Lid = Long.parseLong(id);
					dLatitude = Double.parseDouble(latitude);
					dLongitude = Double.parseDouble(longitude);
					dAltitude = Double.parseDouble(altitude);

					MarkerObject m = new MarkerObject(Lid, dLatitude, dLongitude,
							dAltitude, projectName, projectId, markerAttribute);

					markers.add(m);

					Log.i(TAG, "getOneMyMarkers()  id: " + Lid);
					Log.i(TAG, "getOneMyMarkers()  latitude: " + dLatitude);
					Log.i(TAG, "getOneMyMarkers()  longitude: " + dLongitude);
					Log.i(TAG, "getOneMyMarkers()  altitude: " + dAltitude);
					Log.i(TAG, "getOneMyMarkers()  projectName: " + projectName);
					Log.i(TAG, "getOneMyMarkers()  projectId: " + projectId);
					Log.i(TAG, "getOneMyMarkers()  projectId: " + markerAttribute);

				} catch (Exception e) {
					e.printStackTrace();
				}

				cursor.moveToNext();
			}
			cursor.close();

			return markers;
		}

}
