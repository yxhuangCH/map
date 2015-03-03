package com.yxh.googlemap;

import android.location.Location;

public class MarkerObject {
	private long mId;
	private double mLatitude;
	private double mLongtitude;
	private double mAltitude;
	private String projectName;
	private String projectId;
	private String markerAttribute;
	
	
	public MarkerObject(long id, Location location){
		this.mId = id;
		this.mLatitude = location.getLatitude();
		this.mLongtitude = location.getLongitude();
		this.mAltitude = location.getAltitude();
	}
	
	public MarkerObject(long id, double latitude, double longitude,
			double altitude, String projectName, String projectId) {
		this.mId = id;
		this.mLatitude = latitude;
		this.mLongtitude = longitude;
		this.mAltitude =altitude;
		this.projectName = projectName;
		this.projectId = projectId;
	}
	
	public MarkerObject(long id, double latitude, double longitude,
			double altitude, String projectName, String projectId, String markerAttribute) {
		this.mId = id;
		this.mLatitude = latitude;
		this.mLongtitude = longitude;
		this.mAltitude =altitude;
		this.projectName = projectName;
		this.projectId = projectId;
		this.markerAttribute = markerAttribute;
	}

	public long getmId() {
		return mId;
	}

	public void setmId(long mId) {
		this.mId = mId;
	}

	public double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public double getmLongtitude() {
		return mLongtitude;
	}

	public void setmLongtitude(double mLongtitude) {
		this.mLongtitude = mLongtitude;
	}

	public double getmAltitude() {
		return mAltitude;
	}

	public void setmAltitude(double mAltitude) {
		this.mAltitude = mAltitude;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getMarkerAttribute() {
		return markerAttribute;
	}

	public void setMarkerAttribute(String markerAttribute) {
		this.markerAttribute = markerAttribute;
	}
	
}
