package com.yxh.googlemap;
import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MyHandler extends Handler {
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILURE = 2;
	   WeakReference<MainActivity> mActivity;
	 
	   MyHandler(MainActivity activity) {
	      mActivity = new WeakReference<MainActivity>(activity);
	   }
	 
	   @Override
	   public void handleMessage(Message msg) {
	      MainActivity mainActivity = mActivity.get();
	      if(mainActivity == null)
	         return;
	      switch (msg.what){
			case LOGIN_SUCCESS:
				Toast.makeText(mainActivity, "��½�ɹ���", Toast.LENGTH_SHORT).show();
				break;
			case LOGIN_FAILURE:
				Toast.makeText(mainActivity, "�û������������������µ�½��", Toast.LENGTH_SHORT).show();
	      }
	   }
	}
