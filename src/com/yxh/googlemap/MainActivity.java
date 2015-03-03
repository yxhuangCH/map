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

	// 用于Handler
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILURE = 2;
	// public MyHandler myHandler;

	// 判断 Wi-Fi 连接.
	private static boolean wifiConnected = false;
	// 判断移动网络连接.
	private static boolean mobileConnected = false;

	private Long mExitTime = 0L; // 退出时间

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 检查GPS状态
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

	// 判断GPS状态
	private void chackGPSStatues() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		TextView gpsStatues = (TextView) findViewById(R.id.gpsStatues);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			gpsStatues.setText("GPS未开启");
			gpsStatues.setTextColor(android.graphics.Color.RED);
		} else {
			gpsStatues.setText("GPS已开启");
			gpsStatues.setTextColor(android.graphics.Color.GREEN);
		}
	}// end of chackGPSStatues()

	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.startNewProj) {
			// 转跳去新建立项目界面
			Intent intent1 = new Intent(this, NewProjectActivity.class);
			startActivity(intent1);
		} else if (id == R.id.oldProj) {
			// 转跳去项目列表界面
			Intent intent2 = new Intent(this, OldProjectActivity.class);
			startActivity(intent2);
		} else if (id == R.id.log_in) {
			// 弹出登陆对话框
			showLoginDialog();
			// 启动登陆线程
			// sendRequestWithHttpClientPost();
		}
	}// end of onClick()

	public void checkNetwordStatus() {

		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
		Log.i(TAG, "checkNetwordStatus()");

		if (networkInfo != null && networkInfo.isConnected()) {

			Toast.makeText(this, "网络已经连接上", Toast.LENGTH_SHORT).show();
			// 获取当前网络连接的类型信息
			int networkType = networkInfo.getType();

			if (ConnectivityManager.TYPE_WIFI == networkType) {
				// 当前为wifi网络
				wifiConnected = true;
				Toast.makeText(this, "网络已经连接上,当前网络为WiFi", Toast.LENGTH_LONG)
						.show();
				Log.i(TAG, "当前网络为WiFi");

			} else if (ConnectivityManager.TYPE_MOBILE == networkType) {
				// 当前为mobile网络
				mobileConnected = true;
				Toast.makeText(this, "网络已经连接上,当前网络为移动网络", Toast.LENGTH_LONG)
						.show();
				Log.i(TAG, "当前网络为移动网络");
			}
		}
	}// end of checkNetwork

	// 显示登陆界面
	private void showLoginDialog() {

		LoginDialogFragment loginDialog = new LoginDialogFragment();
		loginDialog.show(getFragmentManager(), "loginDialog");

		Log.i(TAG, "showLoginDialog()");

	}

	public void onLoginInputComplete(String username, String password) {
		// Toast.makeText(this, "帐号：" + username + ",  密码 :" + password,
		// Toast.LENGTH_SHORT).show();
		Log.i(TAG, "onLoginInputComplete() " + "username: " + username
				+ " password: " + password);

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
			return;
		}

		// 登陆服务器
		sendRequestWithHttpClientPost(username, password);
	}
	
	// 存储返回信息
	@Override
	public void saveResponse(String postResult) {
	
		    Log.i(TAG, "saveRespones postResult :" + postResult);
			
		}
	

	private Handler myHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOGIN_SUCCESS:
				Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
				break;
			case LOGIN_FAILURE:
				Toast.makeText(MainActivity.this, "用户名或者密码错误，请从新登陆！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	// 登陆服务器
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
				String result= getProjectInfo("http://www.zwepd.com/app/data/login.php",params,cookie); //JSON字符串
				//解码json
			}
		}).start();
	}
    
	/**
	 * 获取服务器Cookie
	 * @param url 登陆地址
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
					// 请求和响应都成功
					Log.i(TAG, "登陆的返回码： "+ httpResponse.getStatusLine().getStatusCode());
					String htmlCharset = "UTF-8"; 
					String postResult = EntityUtils.toString(httpResponse.getEntity(),htmlCharset);
					//做相应的登陆成功判断
					BasicCookieStore localCookies = new BasicCookieStore();
					respCookies = (BasicCookieStore) httpClient.getCookieStore();
				}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return respCookies;
	}

	/**
	 * 获取工程信息
	 * @param url 地址
	 * @param params POST参数
	 * @param cookie Cookie
	 * @return 项目信息字符串
	 */
	public String getProjectInfo(String url, List<NameValuePair> params, BasicCookieStore cookie){
		String v=""; //结果
		try{
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				httpPost.setEntity(entity);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// 请求和响应都成功
					String htmlCharset = "UTF-8"; 
					v = EntityUtils.toString(httpResponse.getEntity(),htmlCharset);
					//做相应的登陆成功判断
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
				// 如果按两次back键的时间小于两秒，则退出
				Toast.makeText(MainActivity.this, "再按一下退出应用", Toast.LENGTH_LONG).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	

}
