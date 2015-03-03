package com.yxh.googlemap;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.analytics.i;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.Toast;


public class MarkerPagerActivity extends FragmentActivity {

	private static final String TAG = "MarkerPagerActivity";
	
	private ViewPager mViewPager;
	private MarkerDatabaseManager mMarkerDB;
	private ProjectDatabaseManager mProjectDB;
	private ArrayList<MarkerObject> mMarkers ;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Log.d(TAG, "onCreate()");
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		// 加载数据
		openDB();
		
		// 获取所有的Project
		final List<Project> projects = mProjectDB.getAllProject();
		
		// 得到从OldProjectActivity传过来的projectID;
		Intent intent = getIntent();
		final String projectID = intent.getStringExtra(MarkerFragment.EXTRA_PROJECT_ID);
		Log.i(TAG, "getIntent() projectID: " + projectID);

		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				Log.d(TAG, "getCount() projects.size: " + projects.size());
				return projects.size();
			}
			
			@Override
			public Fragment getItem(int postion) {
				Project project = projects.get(postion);
				Log.d(TAG, "getItem, projectID: " + project.getId());
				return MarkerFragment.newInstance(String.valueOf(project.getId()));
			}
		} );
		
		// 将projects中的id与从OldProjectActivity传过来的id匹配，相同则显示
		for (int i = 0; i <projects.size(); i++){
			if (String.valueOf(projects.get(i).getId()).equals(projectID)) {
				mViewPager.setCurrentItem(i);
			}
		}
	
	 	// 监听ViewPager当前显示页面的状态变化
	 	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int postion) {
				//每次页面滑动切换，activity标题更新显示当前project名称
				Project project = projects.get(postion);
				if (project.getProjectName() != null) {
					setTitle(project.getProjectName());
					Log.d(TAG, "onPagerSelected() position: " + postion);
 				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int  state) {
				 Log.i(TAG, "onPageScrollStateChanged_state: " + state);
				
			}
		});
	 	
	}// end of onCreate()


	private void openDB() {
		// 加载project数据库
		mProjectDB = new ProjectDatabaseManager(this);
		try {
			mProjectDB.open();
			Log.i(TAG, "打开project数据库");
		} catch (Exception e) {
			Log.i(TAG, "打开project数据库遇到问题");
		}

		// 加载marker 数据库
		mMarkerDB = new MarkerDatabaseManager(this);
		try {
			mMarkerDB.open();
			Log.i(TAG, "打开marker数据库");
		} catch (Exception e) {
			Log.i(TAG, "打开marker数据库遇到问题");
		}
	}
	

}
