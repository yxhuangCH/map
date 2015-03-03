package com.yxh.googlemap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

public class ListViewDeletFragment extends DialogFragment {
	
	private static final String TAG ="ListViewDeletFragment";
	private String mDialogTitle;
	private String mProjectID;
	private ProjectDatabaseManager mProjectDB;
	private MarkerDatabaseManager mMarkerDB;
//	public boolean isDelet = false;     //标识是否删除
	
	public ListViewDeletFragment(String projectName, String projectID) {
		mDialogTitle = projectName;
		mProjectID = projectID;
	}
	
	
	//定义一个接口，用于返回是否删除的标识符
	public interface DeletProjectListener{
		boolean isDeletProject(boolean delet);   //标识是否删除
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		// 加载数据库
		openDB();
	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(mDialogTitle)
		           .setItems(R.array.city, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					//从数据库的project、marker表格中删除相应的工程	
					mProjectDB.deleteSingleProject(mProjectID);
					mMarkerDB.deleteSingleMarker(mProjectID);
					DeletProjectListener listener = (DeletProjectListener) getActivity();
					listener.isDeletProject(true);
					}
				});
		
		return builder.create();
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

		// 加载marker 数据库
		mMarkerDB = new MarkerDatabaseManager(getActivity());
		try {
			mMarkerDB.open();
			Log.i(TAG, "打开marker数据库");
		} catch (Exception e) {
			Log.i(TAG, "打开marker数据库遇到问题");
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		//关闭数据库
		mProjectDB.close();
		mMarkerDB.close();
	}
}
