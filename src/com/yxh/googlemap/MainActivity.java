package com.yxh.googlemap;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yxh.googlemap.LoginDialogFragment.LoginInputListener;

public class MainActivity extends Activity implements OnClickListener,
		LoginInputListener {

	private static final String TAG = "ManActivity";
	private LocationManager lm;
	private Context context;
	private View view;

	// ����Handler
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILURE = 2;
	// public MyHandler myHandler;

	// �ж� Wi-Fi ����.
	private static boolean wifiConnected = false;
	// �ж��ƶ���������.
	private static boolean mobileConnected = false;

	private Long mExitTime = 0L; // �˳�ʱ��

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ���GPS״̬
		chackGPSStatues();
		checkNetwordStatus();
		// myClickHandler(view);

		Button startNewPro = (Button) findViewById(R.id.startNewProj);
		Button oldPro = (Button) findViewById(R.id.oldProj);
		Button logIn = (Button) findViewById(R.id.log_in);
		startNewPro.setOnClickListener(this);
		oldPro.setOnClickListener(this);
		logIn.setOnClickListener(this);

		// myHandler.sendEmptyMessage(MyHandler.LOGIN_SUCCESS);
	}

	// �ж�GPS״̬
	private void chackGPSStatues() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		TextView gpsStatues = (TextView) findViewById(R.id.gpsStatues);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			gpsStatues.setText("GPSδ����");
			gpsStatues.setTextColor(android.graphics.Color.RED);
		} else {
			gpsStatues.setText("GPS�ѿ���");
			gpsStatues.setTextColor(android.graphics.Color.GREEN);
		}
	}// end of chackGPSStatues()

	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.startNewProj) {
			// ת��ȥ�½�����Ŀ����
			Intent intent1 = new Intent(this, NewProjectActivity.class);
			startActivity(intent1);
		} else if (id == R.id.oldProj) {
			// ת��ȥ��Ŀ�б����
			Intent intent2 = new Intent(this, OldProjectActivity.class);
			startActivity(intent2);
		} else if (id == R.id.log_in) {
			// ������½�Ի���
			showLoginDialog();
			// ������½�߳�
			// sendRequestWithHttpClientPost();
		}
	}// end of onClick()

	public void checkNetwordStatus() {

		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
		Log.i(TAG, "checkNetwordStatus()");

		if (networkInfo != null && networkInfo.isConnected()) {

			Toast.makeText(this, "�����Ѿ�������", Toast.LENGTH_SHORT).show();
			// ��ȡ��ǰ�������ӵ�������Ϣ
			int networkType = networkInfo.getType();

			if (ConnectivityManager.TYPE_WIFI == networkType) {
				// ��ǰΪwifi����
				wifiConnected = true;
				Toast.makeText(this, "�����Ѿ�������,��ǰ����ΪWiFi", Toast.LENGTH_LONG)
						.show();
				Log.i(TAG, "��ǰ����ΪWiFi");

			} else if (ConnectivityManager.TYPE_MOBILE == networkType) {
				// ��ǰΪmobile����
				mobileConnected = true;
				Toast.makeText(this, "�����Ѿ�������,��ǰ����Ϊ�ƶ�����", Toast.LENGTH_LONG)
						.show();
				Log.i(TAG, "��ǰ����Ϊ�ƶ�����");
			}
		}
	}// end of checkNetwork

	// ��ʾ��½����
	private void showLoginDialog() {

		LoginDialogFragment loginDialog = new LoginDialogFragment();
		loginDialog.show(getFragmentManager(), "loginDialog");

		Log.i(TAG, "showLoginDialog()");

	}

	public void onLoginInputComplete(String username, String password) {
		// Toast.makeText(this, "�ʺţ�" + username + ",  ���� :" + password,
		// Toast.LENGTH_SHORT).show();
		Log.i(TAG, "onLoginInputComplete() " + "username: " + username
				+ " password: " + password);

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "�û��������벻��Ϊ��", Toast.LENGTH_LONG).show();
			return;
		}

		// ��½������
		sendRequestWithHttpClientPost(username, password);
	}
	
	// �洢������Ϣ
	@Override
	public void saveResponse(String postResult) {
	
		    Log.i(TAG, "saveRespones postResult :" + postResult);
			
		}
	

	private Handler myHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOGIN_SUCCESS:
				Toast.makeText(MainActivity.this, "��½�ɹ���", Toast.LENGTH_SHORT).show();
				break;
			case LOGIN_FAILURE:
				Toast.makeText(MainActivity.this, "�û������������������µ�½��",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	// ��½������
	public void sendRequestWithHttpClientPost(final String username, final String password) {
		Log.i(TAG, "sendRequestWitheHttpClientPost()" + "\nusername: "
				+ username + " password: " + password);
		new Thread(new Runnable() {

			@Override
			public void run() {
				BasicCookieStore cookie= getCookie("http://www.zwepd.com/app/data/login.php");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("action", "getPro"));
				params.add(new BasicNameValuePair("fristPage", "1"));
				params.add(new BasicNameValuePair("limit", "100"));
				String result= getProjectInfo("http://www.zwepd.com/app/data/login.php",params,cookie); //JSON�ַ���
				//����json
			}
		}).start();
	}
    
	/**
	 * ��ȡ������Cookie
	 * @param url ��½��ַ
	 * @return BasicCookieStore
	 */
	public BasicCookieStore getCookie(String url){
		BasicCookieStore respCookies=null;
		try{
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String path = "http://www.zwepd.com/app/data/Login.php";
				HttpPost httpPost = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("login", "1"));
				params.add(new BasicNameValuePair("UserName", "hyx"));
				params.add(new BasicNameValuePair("PassWord", "admin"));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				httpPost.setEntity(entity);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// �������Ӧ���ɹ�
					Log.i(TAG, "��½�ķ����룺 "+ httpResponse.getStatusLine().getStatusCode());
					String htmlCharset = "UTF-8"; 
					String postResult = EntityUtils.toString(httpResponse.getEntity(),htmlCharset);
					//����Ӧ�ĵ�½�ɹ��ж�
					BasicCookieStore localCookies = new BasicCookieStore();
					respCookies = (BasicCookieStore) httpClient.getCookieStore();
				}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return respCookies;
	}

	/**
	 * ��ȡ������Ϣ
	 * @param url ��ַ
	 * @param params POST����
	 * @param cookie Cookie
	 * @return ��Ŀ��Ϣ�ַ���
	 */
	public String getProjectInfo(String url, List<NameValuePair> params, BasicCookieStore cookie){
		String v=""; //���
		try{
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				httpPost.setEntity(entity);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// �������Ӧ���ɹ�
					String htmlCharset = "UTF-8"; 
					v = EntityUtils.toString(httpResponse.getEntity(),htmlCharset);
					//����Ӧ�ĵ�½�ɹ��ж�
				}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return v;
	}
	
	protected void readResult3(String postResult) {
		JSONObject jsonObject = new JSONObject(); 
		try {  
            JSONArray jsonArray = new JSONArray(postResult);  
            for (int i = 0; i < jsonArray.length(); i++) {  
                jsonObject = jsonArray.getJSONObject(i);  
                int status = jsonObject.getInt("status");  
                String msg = jsonObject.getString("msg");  
                System.out.println("status:" + status);  
                System.out.println("msg:" + msg);  
            }  
        } catch (JSONException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
	}
	
	
	protected void readResult2(String postResult) {
		Log.i(TAG, "readResult2()" + postResult);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(postResult);
			 int status = jsonObject.getInt("status");  
			 System.out.println("status:" + status);  
			 
			String msg=jsonObject.getString("msg");
			System.out.println("msg:" + msg);  
			
		} catch (JSONException e) {
			e.printStackTrace();
		}  
		
	}
	

	protected void readResult(String postResult) {
		  try{  
	            JsonReader reader = new JsonReader(new StringReader(postResult));  
	            reader.beginArray();  
	            while(reader.hasNext()){  
	                reader.beginObject();  
	                while(reader.hasNext()){  
	                    String tagName = reader.nextName();  
	                    if(tagName.equals("status")){  
	                        System.out.println(reader.nextInt());  
	                    }  
	                    else if(tagName.equals("msg")){  
	                        System.out.println(reader.nextString());  
	                    }  
	                }  
	                reader.endObject();  
	            }  
	            reader.endArray();  
	        }  
	        catch(Exception e){  
	            e.printStackTrace();  
	        }
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				// ���������back����ʱ��С�����룬���˳�
				Toast.makeText(MainActivity.this, "�ٰ�һ���˳�Ӧ��", Toast.LENGTH_LONG).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	

}
