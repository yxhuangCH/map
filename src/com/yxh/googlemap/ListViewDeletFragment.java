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
//	public boolean isDelet = false;     //��ʶ�Ƿ�ɾ��
	
	public ListViewDeletFragment(String projectName, String projectID) {
		mDialogTitle = projectName;
		mProjectID = projectID;
	}
	
	
	//����һ���ӿڣ����ڷ����Ƿ�ɾ���ı�ʶ��
	public interface DeletProjectListener{
		boolean isDeletProject(boolean delet);   //��ʶ�Ƿ�ɾ��
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		// �������ݿ�
		openDB();
	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(mDialogTitle)
		           .setItems(R.array.city, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					//�����ݿ��project��marker�����ɾ����Ӧ�Ĺ���	
					mProjectDB.deleteSingleProject(mProjectID);
					mMarkerDB.deleteSingleMarker(mProjectID);
					DeletProjectListener listener = (DeletProjectListener) getActivity();
					listener.isDeletProject(true);
					}
				});
		
		return builder.create();
	}
		
	
	private void openDB() {
		// ����project���ݿ�
		mProjectDB = new ProjectDatabaseManager(getActivity());
		try {
			mProjectDB.open();
			Log.i(TAG, "��project���ݿ�");
		} catch (Exception e) {
			Log.i(TAG, "��project���ݿ���������");
		}

		// ����marker ���ݿ�
		mMarkerDB = new MarkerDatabaseManager(getActivity());
		try {
			mMarkerDB.open();
			Log.i(TAG, "��marker���ݿ�");
		} catch (Exception e) {
			Log.i(TAG, "��marker���ݿ���������");
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		//�ر����ݿ�
		mProjectDB.close();
		mMarkerDB.close();
	}
}
