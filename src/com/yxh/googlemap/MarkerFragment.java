package com.yxh.googlemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MarkerFragment extends Fragment{
	
	public  static final String EXTRA_PROJECT_ID = "com.yxh.googlemap.project_id";
	public  static final String PROJECT_ID = "project_id";
	private static final String TAG = "MarkerFragment";
	private static final String MARKER_NUMBER = "markerNumber";
	private static final String MARKER_LATITUDE = "markerLatitude";
	private static final String MARKER_LONGITUDE = "markerLongitude";
	private static final String MARKER_ALITUDE = "markerAlitude";
	private static final String MARKER_ATTRIBUTE = "markerAttribute";
	private static String mTemProjectID;
	
	private String mProjectID;
	private ProjectDatabaseManager mProjectDB;
	private MarkerDatabaseManager mMarkerDB;
	private List<Project> mProject;
	private ArrayList<HashMap<String, Object>> list;
	private ListView mMarkerListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate( )");
		//加载数据
		openDB();
		
//		if (savedInstanceState != null){
			mProjectID = (String) getArguments().getSerializable(PROJECT_ID);
//		}
		Log.d(TAG, "after openDB()  mProjectID: " + mProjectID);
		
//		mProjectID = (String) getActivity().getIntent().getSerializableExtra(PROJECT_ID);
		
		mProject = mProjectDB.getProject(mProjectID);
		
	}
	
	private void openDB() {
		// 加载project数据库
		mProjectDB = new ProjectDatabaseManager(getActivity());
		try {
			mProjectDB.open();
			Log.i(TAG, "打开project数据库");
		} catch (Exception e) {
			Log.i(TAG, "打开project数据库遇到问题");
		}
		
		// 加载marker数据库
		mMarkerDB= new MarkerDatabaseManager(getActivity());
		try {
			mMarkerDB.open();
			Log.d(TAG, "打开marker数据库");
		} catch (Exception e) {
			Log.d(TAG, "打开marker数据库遇到问题");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		View view = inflater.inflate(R.layout.fragment_marker, container, false);

		TextView projectName = (TextView) view.findViewById(R.id.fragment_projectName);
		TextView projectID = (TextView) view.findViewById(R.id.fragment_projectID);
		TextView projectCreatedTime = (TextView) view.findViewById(R.id.fragment_projectCreatedTime);
	    mMarkerListView  = (ListView) view.findViewById(R.id.listView_fragment_marker);
		
		projectName.setText(mProject.get(0).getProjectName());
		projectID.setText(String.valueOf(mProject.get(0).getId()));
		projectCreatedTime.setText(mProject.get(0).getProjectCreatedTime());

		list = new ArrayList<HashMap<String, Object>>();
		
		 SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), getData(),
				 R.layout.marker_list_item,
				 new String[]{MARKER_NUMBER, MARKER_LATITUDE, MARKER_LONGITUDE, MARKER_ALITUDE, MARKER_ATTRIBUTE}, 
				 new int[]{R.id.marker_number, R.id.marker_latitude, R.id.marker_longitude, R.id.marker_altitude, R.id.marker_attribute });
		
			// 绑定Adapter
		 mMarkerListView.setAdapter(simpleAdapter);
	
		return view;
	}
	
	public static MarkerFragment newInstance(String projectID){
		    Log.d(TAG, "newInstance()   projectID: " + projectID);
		    mTemProjectID = projectID;
		    Bundle args = new Bundle();
	        args.putSerializable(PROJECT_ID, projectID);
	        MarkerFragment fragment = new MarkerFragment();
	        fragment.setArguments(args);
	        return fragment;
	}
	
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		Log.i(TAG, "onSaveInstanceState mTemProjectID: " + mTemProjectID);
//		outState.putString(PROJECT_ID, mTemProjectID);
//		
//	}
	
	   // 初始化一个List
		private List<HashMap<String, Object>> getData() {
			Log.d(TAG, "getData()  mProjectID: " + mProjectID);
			// 新建一个集合类，用于存放多条数据
			HashMap<String, Object> map = null;

			List<MarkerObject> mp = mMarkerDB.getOneMyMarkers(mProjectID);
			for (int i = 0; i < mp.size(); i++) {
				map = new HashMap<String, Object>();
				map.put(MARKER_NUMBER, mp.get(i).getmId());
				map.put(MARKER_LATITUDE, mp.get(i).getmLatitude());
				map.put(MARKER_LONGITUDE, mp.get(i).getmLongtitude());
				map.put(MARKER_ALITUDE, mp.get(i).getmAltitude());
				map.put(MARKER_ATTRIBUTE, mp.get(i).getMarkerAttribute());

				list.add(map);
			}

			Log.i(TAG, "ListView getData()");
			return list;
		}

		
		

}
