package com.yxh.googlemap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
 * 检测网络连接情况
 */
 public class NetwordStatus {
	
	private static final String TAG = "NetwordStatus";

	public void checkNetwordStatus(Context context){
		Log.i(TAG, "checkNetwordStatus");
		if (context != null) {
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
	        //1.判断是否有网络连接 
	        boolean networkAvailable = networkInfo.isAvailable();
	        //2.获取当前网络连接的类型信息
	        int networkType = networkInfo.getType();
	        
	        if(ConnectivityManager.TYPE_WIFI == networkType){
	            //当前为wifi网络
	        	
	        	Log.i(TAG, "当前网络为WiFi");
	        }else if(ConnectivityManager.TYPE_MOBILE == networkType){
	            //当前为mobile网络
	        	Log.i(TAG, "当前网络为移动网络");
	        }
	    }
	}

}
