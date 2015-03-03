package com.yxh.googlemap;

import android.content.Context;

public class Project {
	private long mId;
	private String mProjectName;
	private String  mProjectDescribe;
	private String projectCreatedTime;
	
	public Project(Context context){
		mId = 1;
	}
	public Project(long mId, String mProjectName, String mProjectDescribe, String projectCreatedTime) {
		super();
		this.mId = mId;
		this.mProjectName = mProjectName;
		this.mProjectDescribe = mProjectDescribe;
		this.projectCreatedTime = projectCreatedTime;
	}
	
	public Project(long mId, String mProjectName, String projectCreatedTime){
		super();
		this.mId = mId;
		this.mProjectName = mProjectName;
		this.projectCreatedTime = projectCreatedTime;
	}
	
	public String getProjectCreatedTime() {
		return projectCreatedTime;
	}
	public void setProjectCreatedTime(String projectCreatedTime) {
		this.projectCreatedTime = projectCreatedTime;
	}
	public long getId() {
		return mId;
	}
	public void setId(long id) {
		this.mId = id;
	}
	
	public String getProjectName() {
		return mProjectName;
	}
	public void setProjectName(String projectName) {
		this.mProjectName = projectName;
	}
	
	public String getProjectDescribe() {
		return mProjectDescribe;
	}
	public void setProjectDescribe(String projectDescribe) {
		this.mProjectDescribe = projectDescribe;
	}

	
}
