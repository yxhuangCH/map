package com.yxh.googlemap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
 * ��������������
 */
 public class NetwordStatus {
	
	private static final String TAG = "NetwordStatus";

	public void checkNetwordStatus(Context context){
		Log.i(TAG, "checkNetwordStatus");
		if (context != null) {
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
	        //1.�ж��Ƿ����������� 
	        boolean networkAvailable = networkInfo.isAvailable();
	        //2.��ȡ��ǰ�������ӵ�������Ϣ
	        int networkType = networkInfo.getType();
	        
	        if(ConnectivityManager.TYPE_WIFI == networkType){
	            //��ǰΪwifi����
	        	
	        	Log.i(TAG, "��ǰ����ΪWiFi");
	        }else if(ConnectivityManager.TYPE_MOBILE == networkType){
	            //��ǰΪmobile����
	        	Log.i(TAG, "��ǰ����Ϊ�ƶ�����");
	        }
	    }
	}

}
