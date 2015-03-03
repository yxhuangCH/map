package com.yxh.googlemap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

public class MapLocationService extends Service {

	private static int NOTIFICATION_ID = 1;
	private static final String TAG = "MapLocationService";
	
	private NotificationManager mNotificationManager;

	/**
     * ����һ��Handler ������������Activity ����Ϣ
     */

    private class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "IncomingHandler handleMessage");
            super.handleMessage(msg);
        }
    }

    Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind()");
        return mMessenger.getBinder();
    }

	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		displayNotification();
		Log.i(TAG, "onCreate() method was invoked");
	}

	// ��״̬������ʾ֪ͨ
	private void displayNotification(){
		Intent intent = new Intent(MapLocationService.this, MapFragmentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MapLocationService.this, 0, intent, 0);
        
		 NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	                        .setSmallIcon(R.drawable.ic_launcher)
	                        .setContentTitle("������ͼ����")
	                        .setContentText("����������ͼ������¼...")
	                        .setOngoing(true)                                //����Ϊ�������
	                        .setContentIntent(pendingIntent);
		 
	        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}


	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy() method was invoked");
		mNotificationManager.cancel(NOTIFICATION_ID); //���֪ͨ
		super.onDestroy();
	}

}
