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
		
		// ��������
		openDB();
		
		// ��ȡ���е�Project
		final List<Project> projects = mProjectDB.getAllProject();
		
		// �õ���OldProjectActivity��������projectID;
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
		
		// ��projects�е�id���OldProjectActivity��������idƥ�䣬��ͬ����ʾ
		for (int i = 0; i <projects.size(); i++){
			if (String.valueOf(projects.get(i).getId()).equals(projectID)) {
				mViewPager.setCurrentItem(i);
			}
		}
	
	 	// ����ViewPager��ǰ��ʾҳ���״̬�仯
	 	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int postion) {
				//ÿ��ҳ�滬���л���activity���������ʾ��ǰproject����
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
		// ����project���ݿ�
		mProjectDB = new ProjectDatabaseManager(this);
		try {
			mProjectDB.open();
			Log.i(TAG, "��project���ݿ�");
		} catch (Exception e) {
			Log.i(TAG, "��project���ݿ���������");
		}

		// ����marker ���ݿ�
		mMarkerDB = new MarkerDatabaseManager(this);
		try {
			mMarkerDB.open();
			Log.i(TAG, "��marker���ݿ�");
		} catch (Exception e) {
			Log.i(TAG, "��marker���ݿ���������");
		}
	}
	

}
